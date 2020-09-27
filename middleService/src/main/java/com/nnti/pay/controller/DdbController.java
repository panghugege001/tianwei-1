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
import com.nnti.pay.controller.vo.DdbPayVo;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Addis on 2017/4/26.
 * 多得宝支付
 */
@Controller
@RequestMapping("/ddb")
public class DdbController extends BasePayController {

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
        String order_time = DateUtil.format("yyyy-MM-dd hh:mm:ss", new Date());
        // 客户端IP（选填）
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        DdbPayVo vo = JSON.readValue(mp.getRemain(), DdbPayVo.class);
        vo.setZfbWx("", customerIp, "name ^Zhang San", loginName + "," + mp.getId(), mp.getMerchantCode(), mp.getNotifyUrl(), orderAmount, orderId, order_time, "", "", loginName, "", mp.getShopUrl());
        ReturnVo returnVo = new ReturnVo();
        
        if (2 == mp.getPayWay()) {
	        String signSrc = MyUtils.obj2UrlParam(vo, false, "bank_code", "client_ip", "extend_param", "extra_return_param", "input_charset", "interface_version",
	                "merchant_code", "notify_url", "order_amount", "order_no", "order_time", "pay_type", "product_code", "product_desc", "product_name", "product_num", "return_url", "service_type");
	        String sign = RSAWithSoftware.signByPrivateKey(signSrc, mp.getSignKey());
	
	        vo.setSign(sign);
	        vo.setUrl(mp.getApiUrl());
	        Map pays = MyUtils.describe(vo, "url", "sign", "merchant_code", "service_type", "interface_version", "input_charset",
	                "notify_url", "sign_type", "order_no", "order_time", "order_amount", "product_name", "return_url", "bank_code", "product_code", "product_num", "product_desc",
	                "client_ip", "extend_param", "extra_return_param", "pay_type", "show_url");
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
        else{
            String signSrc = MyUtils.obj2UrlParam(vo, false, "bank_code", "client_ip", "extend_param", "extra_return_param", "input_charset", "interface_version",
                    "merchant_code", "notify_url", "order_amount", "order_no", "order_time", "pay_type", "product_code", "product_desc", "product_name", "product_num", "return_url", "service_type");
            String sign = RSAWithSoftware.signByPrivateKey(signSrc, mp.getSignKey());

            vo.setSign(sign);
            vo.setUrl(mp.getApiUrl());
            Map pays = MyUtils.describe(vo, "url", "sign", "merchant_code", "service_type", "interface_version", "input_charset",
                    "notify_url", "sign_type", "order_no", "order_time", "order_amount", "product_name", "return_url", "bank_code", "product_code", "product_num", "product_desc",
                    "client_ip", "extend_param", "extra_return_param", "pay_type", "show_url");
            log.info(orderId + ":请求参数：" + pays);

            returnVo.setType("1");
            returnVo.setUrl(mp.getPayUrl());
            returnVo.setParams(pays);
        }

        return transfer(returnVo);
    }

    /*** 支付宝 微信 支付回掉*/
    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(DdbPayVo vo, HttpServletRequest req) {

        log.info("ddb接收参数：" + MyWebUtils.getRequestParameters(req));

        String merchant_code = vo.getMerchant_code();
        // 通知类型
        String notify_type = vo.getNotify_type();
        // 通知校验ID
        String notify_id = vo.getNotify_id();
        // 接口版本
        String interface_version = vo.getInterface_version();
        // 签名方式
        String sign_type = vo.getSign_type();
        // 签名
        String dinpaySign = vo.getSign();
        // 商家订单号
        String order_no = vo.getOrder_no();
        // 商家订单时间
        String order_time = vo.getOrder_time();
        // 商家订单金额
        String order_amount = vo.getOrder_amount();
        // 回传参数
        String extra_return_param = vo.getExtra_return_param();
        // 智付交易定单号
        String trade_no = vo.getTrade_no();
        // 智付交易时间
        String trade_time = vo.getTrade_time();
        // 交易状态 SUCCESS 成功 FAILED 失败
        String trade_status = vo.getTrade_status();
        // 银行交易流水号
        String bank_seq_no = vo.getBank_seq_no();

        try {

            String loginName = extra_return_param.split(",")[0];
            String merId = extra_return_param.split(",")[1];

            Assert.isTrue(validationTrustIp(req, DictionaryType.DDB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            DdbPayVo remainVo = gson.fromJson(mp.getRemain(), DdbPayVo.class);
            /**
             * 签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，
             * 同时将商家支付密钥key放在最后参与签名，组成规则如下：
             * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
             */
            String signStr = MyUtils.obj2UrlParam(vo, false, "bank_seq_no", "extra_return_param", "interface_version",
                    "merchant_code", "notify_id", "notify_type", "order_amount", "order_no", "order_time", "orginal_money","trade_no", "trade_status", "trade_time");

            log.info("验签::" + signStr);

            boolean flag = RSAWithSoftware.validateSignByPublicKey(signStr, remainVo.getDdbPublicKey(), dinpaySign);

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
    
    
    /*** 网银支付*/
    @ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String customerIp = payRequestVo.getCustomerIp();
        String orderAmount = payRequestVo.getOrderAmount();
        String bank_code = payRequestVo.getBankCode();

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
        String order_time = DateUtil.format("yyyy-MM-dd hh:mm:ss", new Date());
        // 客户端IP（选填）
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        DdbPayVo vo = JSON.readValue(mp.getRemain(), DdbPayVo.class);
        vo.setOnline(bank_code, customerIp, "name ^Zhang San", loginName + "," + mp.getId(), mp.getMerchantCode(), mp.getNotifyUrl(), orderAmount, orderId, order_time, "", "", loginName, "", mp.getShopUrl());
        ReturnVo returnVo = new ReturnVo();
        
        String signSrc = MyUtils.obj2UrlParam(vo, false, "bank_code", "client_ip", "extend_param", "extra_return_param", "input_charset", "interface_version",
                "merchant_code", "notify_url", "order_amount", "order_no", "order_time", "pay_type", "product_code", "product_desc", "product_name", "product_num","redo_flag",  "return_url", "service_type");
        String sign = RSAWithSoftware.signByPrivateKey(signSrc, mp.getSignKey());

        vo.setSign(sign);
        vo.setApiUrl(mp.getApiUrl());
        Map pays = MyUtils.describe(vo, "apiUrl", "sign", "merchant_code", "service_type", "interface_version", "input_charset",
                "notify_url", "sign_type", "order_no", "order_time", "order_amount", "product_name", "redo_flag", "return_url", "bank_code", "product_code", "product_num", "product_desc",
                "client_ip", "extend_param", "extra_return_param", "pay_type", "show_url");
        log.info(orderId + ":请求参数：" + pays);

        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        return transfer(returnVo);
    }

    
    /*** 网银 支付回掉*/
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(DdbPayVo vo, HttpServletRequest req) {

        log.info("ddb网银接收参数：" + MyWebUtils.getRequestParameters(req));

        String merchant_code = vo.getMerchant_code();
        // 通知类型
        String notify_type = vo.getNotify_type();
        // 通知校验ID
        String notify_id = vo.getNotify_id();
        // 接口版本
        String interface_version = vo.getInterface_version();
        // 签名方式
        String sign_type = vo.getSign_type();
        // 签名
        String dinpaySign = vo.getSign();
        // 商家订单号
        String order_no = vo.getOrder_no();
        // 商家订单时间
        String order_time = vo.getOrder_time();
        // 商家订单金额
        String order_amount = vo.getOrder_amount();
        // 回传参数
        String extra_return_param = vo.getExtra_return_param();
        // 智付交易定单号
        String trade_no = vo.getTrade_no();
        // 智付交易时间
        String trade_time = vo.getTrade_time();
        // 交易状态 SUCCESS 成功 FAILED 失败
        String trade_status = vo.getTrade_status();
        // 银行交易流水号
        String bank_seq_no = vo.getBank_seq_no();

        try {

            String loginName = extra_return_param.split(",")[0];
            String merId = extra_return_param.split(",")[1];

            Assert.isTrue(validationTrustIp(req, DictionaryType.DDB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            DdbPayVo remainVo = gson.fromJson(mp.getRemain(), DdbPayVo.class);
            /**
             * 签名顺序按照参数名a到z的顺序排序，若遇到相同首字母，则看第二个字母，以此类推，
             * 同时将商家支付密钥key放在最后参与签名，组成规则如下：
             * 参数名1=参数值1&参数名2=参数值2&……&参数名n=参数值n&key=key值
             */
            String signStr = MyUtils.obj2UrlParam(vo, false, "bank_seq_no", "extra_return_param", "interface_version",
                    "merchant_code", "notify_id", "notify_type", "order_amount", "order_no", "order_time", "orginal_money","trade_no", "trade_status", "trade_time");

            log.info("验签::" + signStr);

            boolean flag = RSAWithSoftware.validateSignByPublicKey(signStr, remainVo.getDdbPublicKey(), dinpaySign);

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
    
    
}
