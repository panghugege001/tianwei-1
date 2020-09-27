package com.nnti.pay.controller;

import com.alibaba.fastjson.JSONObject;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.RSAWithSoftware;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.JanPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/1/19.
 * 久安钱包
 */
@Controller
@RequestMapping("/jan")
public class JAnController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /*** 代币充值 */
    @ResponseBody
    @RequestMapping("/coin")
    public Response coin(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String customerIp = payRequestVo.getCustomerIp();
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, usetype, orderAmount);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "");
        // 商家定单时间(必填)
        String order_time = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date());

        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        JanPayVo vo = JSON.readValue(mp.getRemain(), JanPayVo.class);
        vo.setCoin(orderAmount, vo.getAssetCode(), vo.getMerchantAddress(), mp.getNotifyUrl(), mp.getMerchantCode(), orderId, user.getLoginName(), user.getAccountName(), user.getPhone(), "+86", user.getLevel() + "");
        String signSrc = MyUtils.obj2UrlParam(vo, true, "amount", "assetCode", "bindAreacode", "bindName", "bindPhone", "bindUserid", "merchantCallbackurl", "merchantId", "merchantOrderid");
        String sign = RSAWithSoftware.signByPrivateKey(signSrc, mp.getSignKey());

        log.info("签名串:" + signSrc);
        vo.setSign(sign);
        Map pays = MyUtils.describe(vo, "sign", "amount", "assetCode", "bindAreacode", "bindName", "bindPhone", "bindUserid", "merchantCallbackurl", "merchantId", "merchantOrderid", "bindUserLevel");

        log.info(orderId + ":请求参数：" + pays);
        String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), MyWebUtils.getListNamevaluepair(pays));
        log.info(orderId + ":返回结果：" + result);

        JSONObject json = JSONObject.parseObject(result);
        String code = json.getString("code");

        String data = null;
        if ("10000".equals(code)) {
            String qr_data = json.getString("data");
            JSONObject qr = JSONObject.parseObject(qr_data);
            data = qr.getString("qrCode");
        } else {
            throw new BusinessException(ErrorCode.SC_10001.getCode(), "获取二维码失败");
        }

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(data);

        log.info(orderId + ":返回二维码：" + data);
        return transfer(returnVo);
    }

    /*** 代币回调 */
    @ResponseBody
    @RequestMapping(value = "/coin_return")
    public String coin_return(JanPayVo vo, HttpServletRequest req) {

        log.info("dinpay接收参数：" + MyWebUtils.getRequestParameters(req));

        String dinpaySign = vo.getSign();

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.JAN_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            String loginName = vo.getBindUserid();

            MerchantPay mp = merchantPayService.findByMerNo(vo.getMerchantId());
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            JanPayVo remainVo = gson.fromJson(mp.getRemain(), JanPayVo.class);

            String signStr = MyUtils.obj2UrlParam(vo, true, "amount", "assetCode", "bindUserid", "jiuanOrderid", "merchantId", "merchantOrderid", "rate");

            log.info("验签::" + signStr);

            boolean flag = RSAWithSoftware.validateSignByPublicKey(signStr, remainVo.getPublicKey(), dinpaySign);

            log.info("验签结果：" + flag);

            if (flag) {

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
                        tradeService.doPayFlow(vo.getMerchantOrderid(), vo.getAmount(), loginName, mp, "", null);
                        return "OK";
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
            }
        } catch (Exception e) {
            log.error("交易异常：", e);
        }
        return null;
    }

}
