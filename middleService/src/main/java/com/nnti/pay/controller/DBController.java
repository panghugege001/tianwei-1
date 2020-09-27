package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.DbPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.XlbPayVo;
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
 * Created by wander on 2017/3/10.
 * 多宝支付
 */
@Controller
@RequestMapping("/db")
public class DBController extends BasePayController {

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
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");
        
        DbPayVo vo = JSON.readValue(mp.getRemain(), DbPayVo.class);

        String type = "";
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            if (mp.getPayWay().equals(2)) {
                type = vo.getWebH();// H5 微信
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getWebH();// H5 支付宝
            } else if (mp.getPayWay().equals(4) || mp.getPayWay().equals(10)) {
                type = vo.getWebH();// H5 京东
            } else if (mp.getPayWay().equals(7)) {
                type = vo.getWebH();// H5 QQ
            }
        } else {
            if (mp.getPayWay().equals(2)) {
                type = vo.getPc();//PC微信扫码
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getPc();//PC支付宝扫码
            } else if (mp.getPayWay().equals(4) || mp.getPayWay().equals(10)) {
                type = vo.getPc();//PC京东扫码
            } else if (mp.getPayWay().equals(7)) {
                type = vo.getPc();//PCQQ
            }
        }

        // 组织请求数据
        if (MyUtils.isNotEmpty(vo.getType())) {
            type = vo.getType();
        }
        vo.setWxZfb(mp.getMerchantCode(), orderId, orderAmount, loginName + "," + mp.getId(), mp.getNotifyUrl(), mp.getShopUrl(), type);

        String param = MyUtils.join("=", "&", "parter", vo.getParter(), "type", vo.getType(), "value", vo.getValue(), "orderid", vo.getOrderid(), "callbackurl", vo.getCallbackurl());

        String sign = DigestUtils.signByMD5(param, mp.getSignKey());

        log.info("签名：" + sign);

        ReturnVo returnVo = new ReturnVo();

        if (mp.getPayWay() == 1 || mp.getPayPlatform().equals("dbjd")  ||  (mp.getPayWay() == 2 && usetype == Constant.USE_TYPE_WEB && mp.getPayPlatform().contains("dbwx")) || 
           (mp.getPayWay() == 7 && usetype == Constant.USE_TYPE_WEB && mp.getPayPlatform().contains("dbqq"))) {
            vo.setSign(sign);

            Map pays = MyUtils.describe(vo, "parter", "type", "value", "orderid", "callbackurl", "attach", "sign");
            log.info(orderId + ":请求参数：" + pays);

            returnVo.setType("1");
            returnVo.setUrl(mp.getPayUrl());
            returnVo.setParams(pays);
        } else {

            String param_p = MyUtils.join("=", "&", "onlyqr", vo.getOnlyqr(), "attach", vo.getAttach(), "sign", sign);

            log.info(orderId + ":请求参数：" + param + "&" + param_p);

            String result = MyWebUtils.getHttpContentByBtParam(mp.getPayUrl(), param + "&" + param_p);

            log.info(orderId + ":返回结果：" + result);

            String code, data = "";

            if (MyUtils.isNotEmpty(result) && result.startsWith("http")) {
                data = result.substring(0, result.indexOf("&"));
            } else {
                code = ErrorCode.SC_30000_111.getCode();
                if (result.indexOf("error:") >= 0) {
                    data = orderId + ":返回失败： " + result.substring(result.indexOf("error:"));
                } else {
                    data = orderId + ":返回失败： 支付接口不稳定，请重新支付";
                }
                throw new BusinessException(code, data);
            }

            returnVo.setType("3");
            returnVo.setData(data);

            log.info(orderId + ":返回二维码：" + data);
        }
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping("/zfb_wx_return")
    public String zfb_wx_return(DbPayVo vo, HttpServletRequest req) throws Exception {

        log.info("多宝 接收参数：" + MyWebUtils.getRequestParameters(req));

        String orderid = vo.getOrderid();
        String opstate = vo.getOpstate();
        String ovalue = vo.getOvalue();
        String sign = vo.getSign();
        String sysorderid = vo.getSysorderid();
        String attach = vo.getAttach();
        String msg = vo.getMsg();

        try {
            String attachs[] = attach.split(",");//用户名，商户号
            String loginName = attachs[0];
            String merId = attachs[1];

            Assert.notEmpty(orderid, opstate, ovalue, sign, attach, loginName, merId);

            Assert.isTrue(validationTrustIp(req, DictionaryType.DB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue("0".equals(opstate), ErrorCode.SC_30000_111.getType());

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String sign_n = MyUtils.join("=", "&", "orderid", orderid, "opstate", opstate, "ovalue", ovalue + mp.getSignKey());

            String sign_y = DigestUtils.signByMD5(sign_n);

            log.info("签名：" + sign_y);

            Assert.isTrue(sign_y.equals(sign), ErrorCode.SC_30000_114.getType());
            
            //查询订单状态
            String result = PayOrderController.queryPayOrder(vo, mp, null, req);
            
            Assert.isTrue(result.equals("SUCCESS"), "查询订单交易失败");
            
            

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
                    tradeService.doPayFlow(orderid, ovalue, loginName, mp, "",null);
                    return "opstate=0";
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

        } catch (BusinessException e) {
            log.error("新贝支付宝或微信回调异常：", e);
        }

        return ERROR;
    }
    
    
    
    
    /**
     *  网银支付
     */
    @ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {


        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String bankCode = payRequestVo.getBankCode();
        String orderAmount = payRequestVo.getOrderAmount();
        
        Assert.notEmpty(loginName, platformId, orderAmount, bankCode, usetype);

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"_");

        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }

        // 组织请求数据
        String platformID = mp.getMerchantCode();
        String merchNo = mp.getMerchantCode();
        String tradeDate = DateUtil.format(DateUtil.YYYYMMDD, new Date());
        String merchUrl = mp.getNotifyUrl();
        DbPayVo vo = JSON.readValue(mp.getRemain(), DbPayVo.class);
        // 组织请求数据
        vo.setWxZfb(mp.getMerchantCode(), orderId, orderAmount, loginName + "," + mp.getId(), mp.getNotifyUrl(), mp.getShopUrl(), bankCode);

        String param = MyUtils.join("=", "&", "parter", vo.getParter(), "type", vo.getType(), "value", vo.getValue(), "orderid", vo.getOrderid(), "callbackurl", vo.getCallbackurl());

        String sign = DigestUtils.signByMD5(param, mp.getSignKey());

        log.info("签名：" + sign);

        ReturnVo returnVo = new ReturnVo();

        vo.setSign(sign);

        Map pays = MyUtils.describe(vo, "parter", "type", "value", "orderid", "callbackurl", "attach", "sign");
        log.info(orderId + ":请求参数：" + pays);

        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);
        
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":db online_pay请求参数：" + pays);
        return transfer(returnVo);
    }
    
    
    /**
     *  快捷支付
     */
    @ResponseBody
    @RequestMapping(value = "/onlineQuick_pay", method = RequestMethod.POST)
    public Response onlineQuick_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {


        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String bankCode = payRequestVo.getBankCode();
        String orderAmount = payRequestVo.getOrderAmount();
        
        Assert.notEmpty(loginName, platformId, orderAmount, bankCode, usetype);

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"_");

        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }

        // 组织请求数据
        String platformID = mp.getMerchantCode();
        String merchNo = mp.getMerchantCode();
        String tradeDate = DateUtil.format(DateUtil.YYYYMMDD, new Date());
        String merchUrl = mp.getNotifyUrl();
        DbPayVo vo = JSON.readValue(mp.getRemain(), DbPayVo.class);
        // 组织请求数据
        vo.setWxZfb(mp.getMerchantCode(), orderId, orderAmount, loginName + "," + mp.getId(), mp.getNotifyUrl(), mp.getShopUrl(), bankCode);

        String param = MyUtils.join("=", "&", "parter", vo.getParter(), "type", vo.getType(), "value", vo.getValue(), "orderid", vo.getOrderid(), "callbackurl", vo.getCallbackurl());

        String sign = DigestUtils.signByMD5(param, mp.getSignKey());

        log.info("签名：" + sign);

        ReturnVo returnVo = new ReturnVo();

        vo.setSign(sign);

        Map pays = MyUtils.describe(vo, "parter", "type", "value", "orderid", "callbackurl", "attach", "sign");
        log.info(orderId + ":请求参数：" + pays);

        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);
        
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":dbQuick online_pay请求参数：" + pays);
        return transfer(returnVo);
    }
    
    
    

    @ResponseBody
    @RequestMapping("/online_pay_return")
    public String online_pay_return(DbPayVo vo, HttpServletRequest req) throws Exception {

        log.info("多宝网银 接收参数：" + MyWebUtils.getRequestParameters(req));

        String orderid = vo.getOrderid();
        String opstate = vo.getOpstate();
        String ovalue = vo.getOvalue();
        String sign = vo.getSign();
        String sysorderid = vo.getSysorderid();
        String attach = vo.getAttach();
        String msg = vo.getMsg();

        try {
            String attachs[] = attach.split(",");//用户名，商户号
            String loginName = attachs[0];
            String merId = attachs[1];

            Assert.notEmpty(orderid, opstate, ovalue, sign, attach, loginName, merId);

            Assert.isTrue(validationTrustIp(req, DictionaryType.DB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue("0".equals(opstate), ErrorCode.SC_30000_111.getType());

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String sign_n = MyUtils.join("=", "&", "orderid", orderid, "opstate", opstate, "ovalue", ovalue + mp.getSignKey());

            String sign_y = DigestUtils.signByMD5(sign_n);

            log.info("签名：" + sign_y);

            Assert.isTrue(sign_y.equals(sign), ErrorCode.SC_30000_114.getType());
            
            //查询订单状态
            String result = PayOrderController.queryPayOrder(vo, mp, null, req);
            
            Assert.isTrue(result.equals("SUCCESS"), "查询订单交易失败");

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
                    tradeService.doPayFlow(orderid, ovalue, loginName, mp, "",null);
                    return "opstate=0";
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

        } catch (BusinessException e) {
            log.error("新贝支付宝或微信回调异常：", e);
        }

        return ERROR;
    }
}
