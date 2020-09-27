package com.nnti.pay.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.nnti.pay.controller.vo.GTPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * @author pony
 * @Description 高通支付
 */
@Controller
@RequestMapping("/gt")
public class GTController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /**
     * 支付宝支付 或者微信支付
     */
    @SuppressWarnings("rawtypes")
	@ResponseBody
    @RequestMapping(value = "/zfb_wx", method = RequestMethod.POST)
    public Response zfb_wx(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();
        String bankcode = payRequestVo.getBankCode();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);
        
        List list = Arrays.asList(new Object[]{"30","50","100","200","300","500"});
        if (usetype == Constant.USE_TYPE_WEB) {
            if (mp.getPayWay().equals(2)) {
                Assert.isTrue(list.contains(orderAmount), "该支付目前只支持如下下单金额：</br>30,50,100,200,300,500");
            }
        }

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");
        
        GTPayVo vo = JSON.readValue(mp.getRemain(), GTPayVo.class);

        String type = "";

        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            if (mp.getPayWay().equals(2)) {
                type = vo.getWap();// wap 微信
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getWap();// wap 支付宝
            }else if (mp.getPayWay().equals(7)) {
                type = vo.getWap();// wap QQ
            }else if (mp.getPayWay().equals(3)) {
            	Assert.isTrue(StringUtils.isNotBlank(bankcode), "请选择支付银行");
                type = bankcode;// 在线支付
            }else if (mp.getPayWay().equals(10)) {
                type = vo.getWap();// wap 京东
            }else if (mp.getPayWay().equals(13)) {
                type = vo.getWap();// wap 银联
            }else if (mp.getPayWay().equals(4)) {
                type = vo.getWap();// wap 快捷
            }
        } else {
            if (mp.getPayWay().equals(2)) {
                type = vo.getPc();//PC微信扫码
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getPc();//PC支付宝扫码
            } else if (mp.getPayWay().equals(7)) {
                type = vo.getPc();//PCQQ
            }else if (mp.getPayWay().equals(3)) {
            	Assert.isTrue(StringUtils.isNotBlank(bankcode), "请选择支付银行");
                type = bankcode;// 在线支付
            }else if (mp.getPayWay().equals(10)) {
                type = vo.getPc();// pc 京东
            }else if (mp.getPayWay().equals(13)) {
                type = vo.getPc();// pc 银联
            }else if (mp.getPayWay().equals(4)) {
                type = vo.getPc();// pc 快捷 
            }
        }

        // 组织请求数据
        vo.setWxZfb(mp.getMerchantCode(), orderId, orderAmount, loginName + "," + mp.getId(), mp.getNotifyUrl(), mp.getShopUrl(), type);

        String param = MyUtils.join("=", "&", "partner", vo.getPartner(), "banktype", vo.getBanktype(), "paymoney", vo.getPaymoney(), "ordernumber", vo.getOrdernumber(), "callbackurl", vo.getCallbackurl());

        String sign = DigestUtils.signByMD5(param, mp.getSignKey());

        log.info("签名：" + sign);

        ReturnVo returnVo = new ReturnVo();
        
        vo.setSign(sign);

        Map pays = MyUtils.describe(vo, "partner", "banktype", "paymoney", "ordernumber", "callbackurl","hrefbackurl", "attach", "sign");
        log.info(orderId + ":请求参数：" + pays);

        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);
        return transfer(returnVo);
    }

    /*** 高通支付宝 微信 回调接口**/
    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(GTPayVo vo, HttpServletRequest req) {
        log.info("gtPay 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String result = vo.getOrderstatus();
        String attach = vo.getAttach();
        String sign = vo.getSign();
        String orderid = vo.getOrdernumber();
        String amount = vo.getPaymoney();
        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = attach.split(",")[0];
            String merId = attach.split(",")[1];
            

            Assert.isTrue(result.equals("1"), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            String paramsStr = MyUtils.obj2UrlParam(vo, true, "partner", "ordernumber","orderstatus", "paymoney");
    		String _sign = DigestUtils.signByMD5(paramsStr, mp.getSignKey());
            
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
                    return "ok";
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
        return "";
    }
}
