package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.utils.*;
import com.nnti.pay.api.rb.ApiUtil;
import com.nnti.pay.api.rb.security.AesEncryption;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.RbPayVo;
import com.nnti.pay.controller.vo.RbResponseVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/4/1.
 * 荣邦支付
 */
@Controller
@RequestMapping("/rb")
public class RbController extends BasePayController {

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

        String amount = NumericUtil.mul(Double.valueOf(orderAmount), 100).longValue() + "";

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"_");

        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }

        // 组织请求数据
        RbPayVo vo = JSON.readValue(mp.getRemain(), RbPayVo.class);
        vo.setWxZfb(orderId, loginName, amount, loginName, mp.getNotifyUrl(), mp.getShopUrl());

        Map paramMap = MyUtils.describe(vo, "ordernumber", "body", "paymenttypeid", "subpaymenttypeid", "amount", "businesstype", "extraparams", "fronturl", "backurl");
        String requestStr = JSON.writeValueAsString(paramMap);

        log.info(orderId + ":rb zfb_wx请求参数：" + requestStr);

        String result = ApiUtil.methodInvoke(mp.getPayUrl(), mp.getMerchantCode(), vo.getSession(), mp.getSignKey(), vo.getMasget(), requestStr);

        log.info(orderId + ":返回结果：" + result);

        RbResponseVo responseVo = JSON.readValue(result, RbResponseVo.class);
        Assert.isTrue("0".equals(responseVo.getRet()), responseVo.getMessage());

        String qrcode = responseVo.getData().getQrcode();
        log.info(orderId + ":返回二维码：" + qrcode);

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(qrcode);

        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping("/zfb_wx_return")
    public String zfb_wx_return(String Data, String Sign, String Method, String Appid, HttpServletRequest req, HttpServletResponse response) {

        String msg;
        log.info("荣邦 接收参数：" + MyWebUtils.getRequestParameters(req));

        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.RB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            MerchantPay mp = merchantPayService.findByMerNo(Appid);
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String result = AesEncryption.Desencrypt(Data, mp.getSignKey(), mp.getSignKey());
            log.info("解密结果：" + result);

            RbPayVo vo = JSON.readValue(result, RbPayVo.class);

            Double amount = NumericUtil.div(Double.valueOf(vo.getAmount()), 100);

            Boolean lockFlag = false;
            String loginName = vo.getExtraparams();

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {

                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {

                if (lockFlag) {

                    tradeService.doPayFlow(vo.getOrdernumber(), amount.toString(), loginName, mp, "",null);

                    return "{\"message\": \"成功\",\"response\":\"00\"}";
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
            log.error("荣邦 回调异常：", e);
        }
        return "{\"message\": \"失败\",\"response\":\"-1\"}";
    }

}
