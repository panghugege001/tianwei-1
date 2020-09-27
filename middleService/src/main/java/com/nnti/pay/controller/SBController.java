package com.nnti.pay.controller;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.SBPayVo;
import com.nnti.pay.controller.vo.ZPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * @author pony
 * @Description 扫呗支付
 */
@Controller
@RequestMapping("/sb")
public class SBController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /**
     * 支付宝支付 或者微信支付
     */
    @ResponseBody
    @RequestMapping(value = "/zfb_wx", method = RequestMethod.POST)
    public Response zfb_wx(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());

        //根据商户号查询商户信息
        MerchantPay merchantPay = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, merchantPay);

        validationAmountCutAll(loginName, merchantPay);

        String orderId = basicService.createBillNo(loginName, orderAmount, merchantPay, loginName,"_");
        Assert.notEmpty(orderId);

        SBPayVo vo = gson.fromJson(merchantPay.getRemain(), SBPayVo.class);
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        vo.setPartner(merchantPay.getMerchantCode());
        vo.setPaymoney(orderAmount);
        vo.setOrdernumber(orderId);
        vo.setCallbackurl(merchantPay.getNotifyUrl());
        vo.setAttach(loginName + "," + merchantPay.getId());
        
        String paramsStr = MyUtils.obj2UrlParam(vo, true, "version", "method","partner","banktype","paymoney", "ordernumber", "callbackurl");
		vo.setSign(DigestUtils.md5(paramsStr+merchantPay.getSignKey()));
		log.info("签名 "+vo.getSign().toLowerCase());

        Map pays = MyUtils.describe(vo,"version","method","partner", "banktype", "paymoney", "ordernumber", "callbackurl",
                    "hrefbackurl", "isshow", "sign", "attach");
        
        log.info(orderId + ":请求参数：" + pays);
        
        String result = MyWebUtils.getHttpContentByParam(merchantPay.getPayUrl(), MyWebUtils.getListNamevaluepair(pays));
        log.info(orderId + ":返回结果：" + result);
        Map resultMap = gson.fromJson(result, Map.class);
        String status = (String) resultMap.get("status");
        String message = (String) resultMap.get("message");
        Assert.isTrue(!"0".equals(status), message);
        String data = (String) resultMap.get("");
        
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(data);

        log.info(orderId + ":sbPay online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    /*** 扫呗支付宝 微信 回调接口**/
    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(ZPayVo vo, HttpServletRequest req) {
        log.info("zbaoPay 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String result = vo.getStatus()+"";
        String attach = vo.getMessage();
        String sign = vo.getSign();
        String orderid = vo.getOrderId();
        String amount = vo.getMoney();
        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.ZBPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = attach.split(",")[0];
            String merId = attach.split(",")[1];
            

            Assert.isTrue(result.equals("0"), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            String paramsStr = MyUtils.obj2UrlParam(vo, true, "orderid", "result","amount", "zborderid", "zborderid");
    		String _sign = DigestUtils.signByMD5Byaddis(paramsStr, mp.getSignKey()).toUpperCase();
            
            Assert.isTrue(_sign.equals(sign), ErrorCode.SC_30000_114.getType());

            Boolean lockFlag = false;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(orderid, amount, loginName, mp, "",null);
                    return "errCode=0";
                } else {
                    log.error("玩家：" + loginName + "未能获取锁，无法执行请求对应的方法....");
                }
            } catch (Exception e) {
                log.error("异常：" + e);
            } finally {
                try {
                    lock.release();
                } catch (Exception e) {
                    log.error("玩家：" + loginName + "释放锁发生异常, 异常信息：" + e);
                }
            }

        } catch (Exception e) {
            log.error("回调异常：", e);
        }
        return "errCode=-1";
    }
}
