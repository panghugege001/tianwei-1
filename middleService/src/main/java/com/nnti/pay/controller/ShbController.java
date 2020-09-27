package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.RSAWithSoftware;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.DinpayPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.ShbPayVo;
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
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by wander on 2017/4/26.
 * 速汇宝支付
 */
@Controller
@RequestMapping("/shb")
public class ShbController extends BasePayController {
    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /*** 微信支付*/
    @ResponseBody
    @RequestMapping("/zfb_wx")
    public Response zfb_wx(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

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
        // 客户端IP（选填）
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        ShbPayVo vo = JSON.readValue(mp.getRemain(), ShbPayVo.class);
        vo.setZfbWx(customerIp, "", loginName + "," + mp.getId(), mp.getMerchantCode(), mp.getNotifyUrl(), orderAmount, orderId, order_time, "", "", loginName, "", mp.getShopUrl());
        String signSrc = MyUtils.obj2UrlParam(vo, false, "client_ip", "extend_param", "extra_return_param", "interface_version", "merchant_code", "notify_url", "order_amount",
                "order_no", "order_time", "product_code", "product_desc", "product_name", "product_num", "service_type");
        String sign = RSAWithSoftware.signByPrivateKey(signSrc, mp.getSignKey());

        vo.setSign(sign);
        Map pays = MyUtils.describe(vo, "sign", "merchant_code", "order_no", "order_amount", "service_type", "notify_url",
                "interface_version", "sign_type", "order_time", "product_name", "extra_return_param", "client_ip");
        log.info(orderId + ":请求参数：" + pays);
        String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), MyWebUtils.getListNamevaluepair(pays));
        log.info(orderId + ":shb返回结果：" + result);

        String code, data = "";
        if (MyUtils.isNotEmpty(result)) {
            code = MyUtils.matcher("<resp_code>(.*?)</resp_code>", result);
            if ("SUCCESS".equals(code)) {
                String result_code = MyUtils.matcher("<result_code>(.*?)</result_code>", result);
                if ("1".equals(result_code)) {
                    throw new BusinessException(ErrorCode.SC_10001.getCode(), MyUtils.matcher("<result_desc>(.*?)</result_desc>", result));
                }
                data = MyUtils.matcher("<qrcode>(.*?)</qrcode>", result);
            } else {
                String info = MyUtils.matcher("<resp_desc>(.*?)</resp_desc>", result);
                throw new BusinessException(ErrorCode.SC_10001.getCode(), info);
            }
        }

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(data);

        log.info(orderId + ":返回二维码：" + data);
        return transfer(returnVo);
    }

    /*** 支付宝 微信 支付回掉*/
    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(DinpayPayVo vo, HttpServletRequest req) {

        log.info("shb接收参数：" + MyWebUtils.getRequestParameters(req));

        String merchant_code = vo.getMerchant_code();
        String notify_type = vo.getNotify_type();
        String notify_id = vo.getNotify_id();
        String interface_version = vo.getInterface_version();
        String sign_type = vo.getSign_type();
        String dinpaySign = vo.getSign();
        String order_no = vo.getOrder_no();
        String order_time = vo.getOrder_time();
        String order_amount = vo.getOrder_amount();
        String extra_return_param = vo.getExtra_return_param();
        String trade_no = vo.getTrade_no();
        String trade_time = vo.getTrade_time();
        String trade_status = vo.getTrade_status();
        String bank_seq_no = vo.getBank_seq_no();

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.SHB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            String loginName = extra_return_param.split(",")[0];
            String merId = extra_return_param.split(",")[1];

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            DinpayPayVo remainVo = gson.fromJson(mp.getRemain(), DinpayPayVo.class);
            /**
             * 签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，
             * 同时将商家支付密钥key放在最后参与签名，组成规则如下：
             * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
             */
            String signStr = MyUtils.obj2UrlParam(vo, false, "bank_seq_no", "extra_return_param", "interface_version",
                    "merchant_code", "notify_id", "notify_type", "order_amount", "order_no", "order_time", "trade_no", "trade_status", "trade_time");

            log.info("验签::" + signStr);

            boolean flag = RSAWithSoftware.validateSignByPublicKey(signStr, remainVo.getZfPublicKey(), dinpaySign);

            log.info("验签结果：" + flag);

            if (flag && trade_status.equals(SUCCESS)) {

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
                        tradeService.doPayFlow(order_no, order_amount, loginName, mp, "",null);
                        return SUCCESS;
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


    /*** 网银支付 */
    @ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String bankCode = payRequestVo.getBankCode();
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

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");

        // 商家定单时间(必填)
        String order_time = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date());
        // 客户端IP（选填）
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        ShbPayVo vo = JSON.readValue(mp.getRemain(), ShbPayVo.class);

        vo.setOnline(mp.getApiUrl(), mp.getMerchantCode(), bankCode, orderId, orderAmount + "",
                mp.getNotifyUrl(), order_time, loginName, customerIp, "", loginName, "", "", "", mp.getShopUrl(), "");
        String signSrc = MyUtils.obj2UrlParam(vo, false, "bank_code", "client_ip", "extend_param", "extra_return_param", "input_charset", "interface_version",
                "merchant_code", "notify_url", "order_amount", "order_no", "order_time", "pay_type", "product_code", "product_desc", "product_name", "product_num",
                "return_url", "service_type");
        String sign = RSAWithSoftware.signByPrivateKey(signSrc, mp.getSignKey());

        vo.setSign(sign);
        Map pays = MyUtils.describe(vo, "apiUrl", "sign", "merchant_code", "bank_code", "order_no",
                "order_amount", "service_type", "input_charset", "notify_url", "interface_version", "sign_type", "order_time", "product_name",
                "client_ip", "extend_param", "extra_return_param", "product_code", "product_desc", "product_num", "return_url", "show_url", "pay_type");

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":shb 请求参数：" + pays);
        return transfer(returnVo);
    }

    /*** 网银支付回掉*/
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(ShbPayVo vo, HttpServletRequest req) {

        String merchant_code = vo.getMerchant_code();
        String notify_type = vo.getNotify_type();
        String notify_id = vo.getNotify_id();
        String interface_version = vo.getInterface_version();
        String sign_type = vo.getSign_type();
        String dinpaySign = vo.getSign();
        String order_no = vo.getOrder_no();
        String order_time = vo.getOrder_time();
        String order_amount = vo.getOrder_amount();
        String extra_return_param = vo.getExtra_return_param();
        String trade_no = vo.getTrade_no();
        String trade_time = vo.getTrade_time();
        String trade_status = vo.getTrade_status();
        String bank_seq_no = vo.getBank_seq_no();

        log.info("shb 接收参数：" + MyWebUtils.getRequestParameters(req));

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.XLB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            MerchantPay mp = merchantPayService.findByMerNo(merchant_code);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            DinpayPayVo remainVo = gson.fromJson(mp.getRemain(), DinpayPayVo.class);
            /**
             * 签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，
             * 同时将商家支付密钥key放在最后参与签名，组成规则如下：
             * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
             */
            String signStr = MyUtils.obj2UrlParam(vo, false, "bank_seq_no", "extra_return_param", "interface_version",
                    "merchant_code", "notify_id", "notify_type", "order_amount", "order_no", "order_time", "trade_no", "trade_status", "trade_time");

            log.info("验签::" + signStr);

            boolean flag = RSAWithSoftware.validateSignByPublicKey(signStr, remainVo.getZfPublicKey(), dinpaySign);

            log.info("验签结果：" + flag);

            String loginName = extra_return_param;

            if (flag && trade_status.equals(SUCCESS)) {

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
                        tradeService.doPayFlow(order_no, order_amount, loginName, mp, "",null);
                        return SUCCESS;
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
