package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.security.RSAWithSoftware;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.DinpayPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/1/19.
 * 智付支付平台
 */
@Controller
@RequestMapping("/dinpay")
public class DinpayController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /*** 微信 支付宝 支付*/
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

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");

        // 商家定单时间(必填)
        String order_time = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date());
        // 客户端IP（必填）
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        DinpayPayVo vo = JSON.readValue(mp.getRemain(), DinpayPayVo.class);
        ReturnVo returnVo = new ReturnVo();
        vo.setZfbWx(mp.getApiUrl(), loginName + "," + mp.getId(), mp.getMerchantCode(), orderId, orderAmount, mp.getNotifyUrl(), order_time, loginName, "", "", "", customerIp);

        if (7 == mp.getPayWay()) {
            String signSrc = MyUtils.obj2UrlParam(vo, false, "bank_code", "extra_return_param", "input_charset", "interface_version", "merchant_code", "notify_url", "order_amount", "order_no",
                    "order_time", "pay_type", "product_name", "redo_flag", "return_url", "service_type");
            String sign = RSAWithSoftware.signByPrivateKey(signSrc, mp.getSignKey());

            vo.setSign(sign);
            Map pays = MyUtils.describe(vo, "apiUrl", "sign", "merchant_code", "service_type", "interface_version", "input_charset", "notify_url",
                    "sign_type", "order_no", "order_time", "order_amount", "product_name","redo_flag", "return_url", "pay_type", "bank_code", "extra_return_param");
            log.info(orderId + ":请求参数：" + pays);

            returnVo.setType("1");
            returnVo.setUrl(mp.getPayUrl());
            returnVo.setParams(pays);
        } else {

            String signSrc = MyUtils.obj2UrlParam(vo, false, "client_ip", "extend_param", "extra_return_param", "interface_version", "merchant_code", "notify_url", "order_amount",
                    "order_no", "order_time", "product_code", "product_desc", "product_name", "product_num", "service_type");
            String sign = RSAWithSoftware.signByPrivateKey(signSrc, mp.getSignKey());

            vo.setSign(sign);
            Map pays = MyUtils.describe(vo, "sign", "merchant_code", "order_no", "order_amount", "service_type", "notify_url",
                    "interface_version", "sign_type", "order_time", "product_name", "extra_return_param", "client_ip");
            log.info(orderId + ":请求参数：" + pays);
            String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), MyWebUtils.getListNamevaluepair(pays));
            log.info(orderId + ":返回结果：" + result);

            String code, data = "";
            if (MyUtils.isNotEmpty(result)) {
                code = MyUtils.matcher("<resp_code>(.*?)</resp_code>", result);
                if ("SUCCESS".equals(code)) {
                    data = MyUtils.matcher("<qrcode>(.*?)</qrcode>", result);
                } else {
                    String info = MyUtils.matcher("<resp_desc>(.*?)</resp_desc>", result);
                    throw new BusinessException(ErrorCode.SC_10001.getCode(), info);
                }
            }

            returnVo.setType("2");
            returnVo.setData(data);

            log.info(orderId + ":返回二维码：" + data);
        }

        return transfer(returnVo);
    }

    /*** 支付宝 微信 支付回掉*/
    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(DinpayPayVo vo, HttpServletRequest req) {

        log.info("dinpay接收参数：" + MyWebUtils.getRequestParameters(req));

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

            Assert.isTrue(validationTrustIp(req, DictionaryType.ZF_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            String loginName = extra_return_param.split(",")[0];
            String merId = extra_return_param.split(",")[1];

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            //查询订单状态
            String result = PayOrderController.queryPayOrder(vo, mp, null, req);
            
            Assert.isTrue(result.equals("SUCCESS"), "查詢訂單交易失敗");
            
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

        Assert.notEmpty(loginName, platformId, usetype, orderAmount, bankCode);
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


        DinpayPayVo vo = JSON.readValue(mp.getRemain(), DinpayPayVo.class);
        vo.setOnline(mp.getApiUrl(), mp.getMerchantCode(), bankCode, orderId, orderAmount + "",
                mp.getNotifyUrl(), order_time, loginName, customerIp, "", loginName + "," + mp.getId(), "", "", "", mp.getShopUrl(), "");

        ReturnVo returnVo = new ReturnVo();
        Map pays = null;
        if (MyUtils.isNotEmpty(vo.getPay_type())) {//新的商户  

            String signSrc = MyUtils.obj2UrlParam(vo, false, "bank_code", "extra_return_param", "input_charset", "interface_version", "merchant_code", "notify_url", "order_amount", "order_no",
                    "order_time", "pay_type", "product_name", "redo_flag", "return_url", "service_type");
            String sign = RSAWithSoftware.signByPrivateKey(signSrc, mp.getSignKey());
            
            vo.setSign(sign);
            pays = MyUtils.describe(vo, "apiUrl", "sign", "merchant_code", "service_type", "interface_version", "input_charset", "notify_url",
                    "sign_type", "order_no", "order_time", "order_amount", "product_name","redo_flag", "return_url", "pay_type", "bank_code", "extra_return_param");
            log.info(orderId + ":请求参数：" + pays);

        } else {//旧的商户
        	
            String signSrc = MyUtils.obj2UrlParam(vo, false, "bank_code", "client_ip", "extend_param", "extra_return_param", "input_charset", "interface_version",
                    "merchant_code", "notify_url", "order_amount", "order_no", "order_time", "product_code", "product_desc", "product_name", "product_num","redo_flag",
                    "return_url", "service_type");
            String sign = RSAWithSoftware.signByPrivateKey(signSrc, mp.getSignKey());

            vo.setSign(sign);
            pays = MyUtils.describe(vo, "url", "sign", "merchant_code", "bank_code", "order_no",
                    "order_amount", "service_type", "input_charset", "notify_url", "interface_version", "sign_type", "order_time", "product_name",
                    "client_ip", "extend_param", "extra_return_param", "product_code", "product_desc", "product_num","redo_flag", "return_url", "show_url");
        }
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":dinpay 请求参数：" + pays);
        return transfer(returnVo);
    }

    /*** 网银支付回掉*/
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(DinpayPayVo vo, HttpServletRequest req) {

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
        
        String loginName = extra_return_param.split(",")[0];
        String merId = extra_return_param.split(",")[1];
        

        log.info("dinpay接收参数：" + MyWebUtils.getRequestParameters(req));

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.ZF_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            //查询订单状态
            String result = PayOrderController.queryPayOrder(vo, mp, null, req);
            
            Assert.isTrue(result.equals("SUCCESS"), "查询订单状态失败");
            
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

    /**** 点卡支付 后台处理*/
    @ResponseBody
    @RequestMapping("point_card")
    public Response point_card(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);
        Long platformId = payRequestVo.getPlatformId();
        String orderAmount = payRequestVo.getOrderAmount();
        String loginName = payRequestVo.getLoginName();
        Integer usetype = payRequestVo.getUsetype();
        String cardNo = payRequestVo.getCardNo();
        String cardCode = payRequestVo.getCardCode();
        String cardPassword = payRequestVo.getCardPassword();

        Assert.notEmpty(platformId, orderAmount, loginName, cardNo, cardCode, cardPassword, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        cardCode = cardCode.trim();
        cardNo = cardNo.trim();
        cardPassword = cardPassword.trim();


        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        String type = mp.getPayCenterUrl().replaceAll("/", "") + "_type";
        validationCard(type, cardCode);

        validationAmountCutAll(loginName, mp);

        String prefix = loginName;

        prefix = loginName + "_" + cardCode;

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, prefix, "100");

        DinpayPayVo vo = gson.fromJson(mp.getRemain(), DinpayPayVo.class);

        String order_no = orderId;
        String order_time = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date());

        String encrypt_params = cardNo + "|" + cardPassword;

        String encrypt_info = RSAWithSoftware.encryptByPublicKey(encrypt_params, vo.getRSA_S_PublicKey());
        vo.setPointCard(order_no, orderAmount, order_time, cardCode, mp.getMerchantCode(), mp.getNotifyUrl(), encrypt_info, "127.0.0.1");

        String signSrc = MyUtils.obj2UrlParam(vo, false, "card_amount", "card_code", "client_ip", "encrypt_info", "interface_version", "merchant_code",
                "notify_url", "order_no", "order_time", "service_type");

        log.info("加密字符串：" + signSrc);


        String sign = RSAWithSoftware.signByPrivateKey(signSrc, vo.getRSA_S_PrivateKey());

        List<NameValuePair> list = MyWebUtils.getListNamevaluepair("sign", sign, "interface_version", vo.getInterface_version(), "sign_type", vo.getSign_type(), "service_type", vo.getService_type(),
                "merchant_code", vo.getMerchant_code(), "order_no", order_no, "order_time", order_time, "card_code", cardCode, "card_amount", orderAmount, "encrypt_info", encrypt_info,
                "notify_url", vo.getNotify_url(), "client_ip", vo.getClient_ip());

        log.info(orderId + ":dinpay 请求参数：" + list);

        String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), list);

        log.info(orderId + ":请求结果：" + result);

        String code = "";
        String message = ErrorCode.SC_30000_111.getType();
        if (MyUtils.isNotEmpty(result)) {
            code = MyUtils.matcher("<trade_status>(.*?)</trade_status>", result);
            message = MyUtils.matcher("<error_msg>(.*?)</error_msg>", result);

            if (MyUtils.isNotEmpty(code) && "ACCEPTED_SUCCESS".equals(code)) {
                message = "已受理, 正在提交后台处理...";
                code = ErrorCode.SC_10000.getCode();
            } else {
                throw new BusinessException(ErrorCode.SC_30000_111.getCode(), message);
            }
        }
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("4");
        returnVo.setData(message);

        return transfer(returnVo);
    }

    /*** 点卡支付回调 */
    @ResponseBody
    @RequestMapping(value = "/point_card_return")
    public String point_card_return(DinpayPayVo vo, HttpServletRequest req) {

        log.info("dinpay 接收参数：" + MyWebUtils.getRequestParameters(req));

        String merchant_code = vo.getMerchant_code();
        String notify_id = vo.getNotify_id();
        String interface_version = vo.getInterface_version();
        String sign_type = vo.getSign_type();
        String dinpaySign = vo.getSign();
        String order_no = vo.getOrder_no();
        String trade_no = vo.getTrade_no();
        String pay_date = vo.getPay_date();
        String card_code = vo.getCard_code();
        String card_amount = vo.getCard_amount();
        String card_no = vo.getCard_no();
        String card_actual_amount = vo.getCard_actual_amount();
        String trade_status = vo.getTrade_status();

        String message;

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.ZF_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.notEmpty(card_amount, trade_status);

            Assert.isTrue(trade_status.equalsIgnoreCase(SUCCESS), ErrorCode.SC_30000_111.getType());

            String signSrc = MyUtils.obj2UrlParam(vo, false,
                    "card_actual_amount", card_actual_amount, "card_amount", card_amount, "card_code", card_code,
                    "card_no", card_no, "interface_version", interface_version, "merchant_code", merchant_code, "notify_id", notify_id,
                    "order_no", order_no, "pay_date", pay_date, "trade_no", trade_no, "trade_status", trade_status);

            log.info("签名字符串：" + signSrc);

            MerchantPay mp = merchantPayService.findByMerNo(merchant_code);
            
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
  
            DinpayPayVo pvo = gson.fromJson(mp.getRemain(), DinpayPayVo.class);  
            
            Assert.isTrue(RSAWithSoftware.validateSignByPublicKey(signSrc, pvo.getZhifu_PublicKey(), dinpaySign), "验签失败");  

            log.info("验签通过...");
            
            //查询订单状态
            mp.setSignKey(pvo.getRSA_S_PrivateKey());
            
            String result = PayOrderController.queryPayOrder(vo, mp, null, req);
            
            Assert.isTrue(result.equals("SUCCESS"), "查询订单状态失败");
            

            String loginName = order_no.split("_")[0];

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
                    tradeService.doPayPointCardFlow(order_no, Double.parseDouble(card_actual_amount), loginName, card_code, mp,null,null);
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

        } catch (Exception e) {
            log.error("点卡回调异常：", e);
        }
        return ERROR;
    }

}
