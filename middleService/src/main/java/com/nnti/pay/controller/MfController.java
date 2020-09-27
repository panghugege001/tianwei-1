package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.security.RSAWithSoftware;
import com.nnti.common.utils.*;
import com.nnti.pay.api.rb.ApiUtil;
import com.nnti.pay.api.rb.security.AesEncryption;
import com.nnti.pay.controller.vo.MifPayVo;
import com.nnti.pay.controller.vo.MifResponseVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.RbPayVo;
import com.nnti.pay.controller.vo.RbResponseVo;
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
 * Created by Addis on 2017/10/12.
 * 米付宝
 */
@Controller
@RequestMapping("/mif")
public class MfController extends BasePayController {

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
        String customerIp = payRequestVo.getCustomerIp();

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

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"");
        
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        
        // 客户端IP
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }

        // 组织请求数据
        String tradeDate = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
        MifPayVo vo = JSON.readValue(mp.getRemain(), MifPayVo.class);
        vo.setWxZfb2(mp.getMerchantCode(), orderId, amount, "ABC", "ABC", loginName, tradeDate, mp.getShopUrl(), mp.getNotifyUrl(),"warIII",customerIp);
        
        ReturnVo returnVo = new ReturnVo();
        if (7 == mp.getPayWay() && usetype == Constant.USE_TYPE_WEB) {
        	mp.setPayUrl("https://api.judzf.com/wap/pay");
        	
        	String paramsStr = MyUtils.obj2UrlParam(vo, true,
            		 "arrivalType", "ext", "goodsExplain", "goodsName", "merUrl", "merchantCode","noticeUrl","orderCreateTime","outOrderId","payType","totalAmount");
            vo.setSign(DigestUtils.signByMD5(paramsStr + "&KEY="+mp.getSignKey()));

            Map pays = MyUtils.describe(vo,"sign", "arrivalType", "ext", "goodsExplain", "goodsName", "merUrl", "merchantCode","noticeUrl","orderCreateTime","outOrderId","payType","totalAmount");
            log.info(orderId + ":wap--请求参数：" + pays);
            
            
            returnVo.setType("1");
            returnVo.setUrl(mp.getPayUrl());   
            returnVo.setParams(pays);
        }
        else {
        	vo.setAmount(amount);
        	
        	String paramsStr = MyUtils.obj2UrlParam(vo, true,
            		"amount", "arrivalType", "deviceNo", "ext", "goodsExplain", "goodsMark", "goodsName", "ip", "merchantCode","noticeUrl","orderCreateTime","outOrderId","payChannel");
            vo.setSign(DigestUtils.signByMD5(paramsStr + "&KEY="+mp.getSignKey()));

            System.out.println("paramsStr:"+paramsStr);
            
            //构建请求参数
            String url = mp.getPayUrl();

            log.info(orderId + ":mif zfb_wx请求参数：" + url);

            String result = MyWebUtils.getHttpContentByBtParam(url, paramsStr + "&sign=" + vo.getSign());
            
            log.info(orderId + ":返回数据：" + result);

            MifResponseVo responseVo = JSON.readValue(result, MifResponseVo.class);
            Assert.isTrue("00".equals(responseVo.getCode()), responseVo.getMsg());

            String qrcode = responseVo.getData().getUrl();
            log.info(orderId + ":返回二维码：" + qrcode);

           
            returnVo.setType("2");
            returnVo.setData(qrcode);
        }
        return transfer(returnVo);        
    }

    @ResponseBody
    @RequestMapping("/zfb_wx_return")
    public String zfb_wx_return(MifPayVo vo, HttpServletRequest req) throws Exception {
        String merchantCode = vo.getMerchantCode();
        String instructCode = vo.getInstructCode();
        String outOrderId = vo.getOutOrderId();
        String transTime = vo.getTransTime();
        String totalAmount = vo.getTotalAmount();
        String ext = vo.getExt();
        String sign = vo.getSign();

        String msg;

        log.info("米付接收参数：" + MyWebUtils.getRequestParameters(req));
        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.MIF_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            MerchantPay mp = merchantPayService.findByMerNo(merchantCode);
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String str = MyUtils.obj2UrlParam(vo, true, "ext", "instructCode", "merchantCode", "outOrderId", "totalAmount", "transTime");
            
            String _sign = DigestUtils.signByMD5(str+"&KEY="+mp.getSignKey());
            
            System.out.println(str+"&KEY="+mp.getSignKey());

            // 验证签名
			Assert.isTrue(vo.getSign().equals(_sign), "米付支付回调签名验证失败：orderId ：" + vo.getOutOrderId());
			
			Double amount = NumericUtil.div(Double.valueOf(vo.getTotalAmount()), 100);

            Boolean lockFlag = false;
            String loginName = ext;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(outOrderId, amount.toString(), loginName, mp, "",null);
                    return "{\"code\":\"00\"}";
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
            log.error("回掉异常:", e);
        }
        return  "{\"code\":\"error\"}";
    }
    
    
    
    
    
    /**
     * 网银 快捷支付
     */
    @ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();
        String customerIp = payRequestVo.getCustomerIp();
        String bankcard = payRequestVo.getBankcard();
        String bankname = payRequestVo.getBankname();

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

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"");
        
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }
        
        // 客户端IP
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }
        

        // 组织请求数据
        String tradeDate = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
        MifPayVo vo = JSON.readValue(mp.getRemain(), MifPayVo.class);
        
        vo.setOnline(mp.getMerchantCode(), orderId, amount, "ABC", "ABC", tradeDate, mp.getShopUrl(), mp.getNotifyUrl(), bankname, bankcard, loginName);
        
        String paramsStr = MyUtils.obj2UrlParam(vo, true,"bankCardNo","bankCardType","bankCode", "ext", "goodsExplain","goodsName",
        		                                         "merUrl","merchantCode","noticeUrl","orderCreateTime","outOrderId","totalAmount");
        vo.setSign(DigestUtils.signByMD5(paramsStr + "&KEY="+mp.getSignKey()));

        System.out.println("paramsStr:"+paramsStr);
        
        //构建请求参数
        String url = mp.getPayUrl();


        Map pays = MyUtils.describe(vo, "sign","bankCardNo","bankCardType","bankCode", "ext", "goodsExplain","goodsName",
                "merUrl","merchantCode","noticeUrl","orderCreateTime","outOrderId","totalAmount");
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(url);
        returnVo.setParams(pays);

        log.info(orderId + ":mif online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping("/online_pay_return")
    public String online_pay_return(MifPayVo vo, HttpServletRequest req) throws Exception {
        String merchantCode = vo.getMerchantCode();
        String instructCode = vo.getInstructCode();
        String outOrderId = vo.getOutOrderId();
        String transTime = vo.getTransTime();
        String totalAmount = vo.getTotalAmount();
        String ext = vo.getExt();
        String sign = vo.getSign();

        String msg;

        log.info("米付接收参数：" + MyWebUtils.getRequestParameters(req));
        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.MIF_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            MerchantPay mp = merchantPayService.findByMerNo(merchantCode);
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String str = MyUtils.obj2UrlParam(vo, true, "ext", "instructCode", "merchantCode", "outOrderId", "totalAmount", "transTime");
            
            String _sign = DigestUtils.signByMD5(str+"&KEY="+mp.getSignKey());
            
            System.out.println(str+"&KEY="+mp.getSignKey());

            // 验证签名
			Assert.isTrue(vo.getSign().equals(_sign), "米付支付回调签名验证失败：orderId ：" + vo.getOutOrderId());
			
			Double amount = NumericUtil.div(Double.valueOf(vo.getTotalAmount()), 100);

            Boolean lockFlag = false;
            String loginName = ext;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(outOrderId, amount.toString(), loginName, mp, "",null);
                    return "{\"code\":\"00\"}";
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
            log.error("回掉异常:", e);
        }
        return  "{\"code\":\"error\"}";
    }
}
