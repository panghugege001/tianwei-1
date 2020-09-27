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
import com.nnti.pay.controller.vo.NewXlbPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.TjfPayVo;
import com.nnti.pay.controller.vo.XlbPayVo;
import com.nnti.pay.controller.vo.XlbResponseVo;
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
 * Created by Addis on 2017/1/19.
 * 天机付 支付业务
 */
@Controller
@RequestMapping("/tjf")
public class TjfController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    @ResponseBody
    @RequestMapping("/zfb_wx")
    public Response zfb_wx(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String customerIp = payRequestVo.getCustomerIp();
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

        // 客户端IP
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }
        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"_");

        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }

        // 组织请求数据
        String tradeDate = DateUtil.format(DateUtil.YYYYMMDD, new Date());
        TjfPayVo vo = gson.fromJson(mp.getRemain(), TjfPayVo.class);
        vo.setWxZfb(mp.getMerchantCode(),orderId, tradeDate, orderAmount, mp.getNotifyUrl(), loginName, "abc",customerIp);
        vo.setExpireTime("60");
        
        if (usetype == Constant.USE_TYPE_WEB) {
            if (mp.getPayWay().equals(2)) {
            	vo.setService(vo.getWapH());// H5 微信
            }
        }

        String paramsStr = MyUtils.obj2UrlParam(vo, true,
                "service", "version", "merId", "typeId", "tradeNo", "tradeDate", "amount",
                "notifyUrl", "extra", "summary","expireTime","clientIp");
        vo.setSign(DigestUtils.signByMD5(paramsStr, mp.getSignKey()));
       
        
        ReturnVo returnVo = new ReturnVo();
        
        if (("TRADE.H5PAY").equals(vo.getService())) {
            Map pays = MyUtils.describe(vo, "service", "version", "merId", "typeId", "tradeNo", "tradeDate", "amount","notifyUrl","extra","summary","expireTime","clientIp","sign");
            log.info(orderId + ":请求参数：" + pays);

            returnVo.setType("1");
            returnVo.setUrl(mp.getPayUrl());
            returnVo.setParams(pays);
        }
        else{
            //构建请求参数
            String url = mp.getPayUrl();
            
            log.info(orderId + ":tjf zfb_wx请求URL：" + url);
            log.info(orderId + ":tjf zfb_wx请求参数：" + paramsStr + "&sign=" + vo.getSign());
        	
            String result = MyWebUtils.getHttpContentByBtParam(url, paramsStr + "&sign=" + vo.getSign());
            log.info(orderId + ":返回数据：" + result);

            String code, data = "";
            if (MyUtils.isNotEmpty(result)) {
                code = MyUtils.matcher("<code>(.*?)</code>", result);
                if ("00".equals(code)) {
                    data = MyUtils.matcher("<qrCode>(.*?)</qrCode>", result);
                    data = DigestUtils.decodeByBase64(data);
                } else {
                    String info = MyUtils.matcher("<desc>(.*?)</desc>", result);
                    throw new BusinessException(ErrorCode.SC_10001.getCode(), info);
                }
            }

            log.info(orderId + ":返回二维码：" + data);
            returnVo.setType("2");
            returnVo.setData(data);
        }
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping("/zfb_wx_return")
    public String zfb_wx_return(TjfPayVo vo, HttpServletRequest req) throws Exception {
    	
    	 log.info("天机付 回调接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String service = vo.getService();
        String merId = vo.getMerId();
        String tradeNo = vo.getTradeNo();
        String tradeDate = vo.getTradeDate();
        String opeNo = vo.getOpeNo();
        String opeDate = vo.getOpeDate();
        String amount = vo.getAmount();
        String status = vo.getStatus();
        String extra = vo.getExtra();
        String payTime = vo.getPayTime();
        String sign = vo.getSign();
        String notifyType = vo.getNotifyType();
        
        String msg;

        log.info("tjf 接收参数：" + MyWebUtils.getRequestParameters(req));
        try {

            //Assert.isTrue(validationTrustIp(req, DictionaryType.XXLB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");   

            MerchantPay mp = merchantPayService.findByMerNo(merId);
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            Assert.isTrue(status.equals("1"), "充值失败");

            String srcMsg = MyUtils.join("=", "&", "service", service, "merId", merId, "tradeNo", tradeNo,
                    "tradeDate", tradeDate, "opeNo", opeNo, "opeDate", opeDate, "amount", amount,
                    "status", status, "extra", extra, "payTime", payTime);
            // 验证签名
            Assert.isTrue(sign.equalsIgnoreCase(DigestUtils.signByMD5(srcMsg, mp.getSignKey())), "天机付 验证签名失败");
            
            //查询订单状态
            String result = PayOrderController.queryPayOrder(vo, mp, null, req);
            
            Assert.isTrue(result.equals("1"), "查询订单状态失败");

            Boolean lockFlag = false;
            String loginName = extra;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(tradeNo, amount, loginName, mp, "",null);
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

        } catch (BusinessException e) {
            log.error("回调异常:", e);
        }
        return ERROR;
    }
    
    

    @ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);    
          
        System.out.println("新迅联宝接收参数：" + MyWebUtils.getRequestParameters(req));       
        
        System.out.println("requestData：" + requestData);           

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();  
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();
        String customerIp = payRequestVo.getCustomerIp();
        String bankcard="";
        String bankname="";
        String phoneNumber="";
        if(payRequestVo.getBankcard() != null && !payRequestVo.getBankcard().equals("")){
        	 bankcard = payRequestVo.getBankcard().replaceAll(" ", "");	
        	 bankname = payRequestVo.getBankname().replaceAll(" ", "");
             phoneNumber = payRequestVo.getPhoneNumber().replaceAll(" ", "");
        }
       
        String bankId = payRequestVo.getBankCode();
        
        Assert.notEmpty(loginName, platformId, orderAmount, usetype);

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);
  
        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay merchantPay = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, merchantPay);

        String orderId = basicService.createBillNo(loginName, orderAmount, merchantPay, loginName,"_");

        // 客户端IP
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }
        
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }

        // 组织请求数据
        String tradeDate = DateUtil.format(DateUtil.YYYYMMDD, new Date());
        NewXlbPayVo vo = gson.fromJson(merchantPay.getRemain(), NewXlbPayVo.class);
        vo.setOnline(merchantPay.getMerchantCode(),orderId , tradeDate, orderAmount, merchantPay.getNotifyUrl(), loginName, "ABC", customerIp,bankId,bankcard,bankname,phoneNumber);

        String paramsStr = MyUtils.obj2UrlParam(vo, true,
                "service", "version", "merId","tradeNo", "tradeDate", "amount",
                "notifyUrl", "extra", "summary","clientIp","bankId");
        vo.setSign(DigestUtils.signByMD5(paramsStr, merchantPay.getSignKey()));
        vo.setUrl(merchantPay.getPayUrl());
        vo.setApiUrl(merchantPay.getApiUrl());

        
        System.out.println("paramsStr:"+paramsStr);
        System.out.println(""+vo.getSign());
        
        ReturnVo returnVo = new ReturnVo();
        Map pays=null;
        
        
        //如果是迅联宝网银
        if(merchantPay.getPayPlatform().contains("xlbWy")){
        	 pays = MyUtils.describe(vo,"sign", "apiUrl","service", "version", "merId", "tradeNo", "tradeDate",
                     "amount", "notifyUrl", "extra", "summary","clientIp","bankId");
        	 
        	 returnVo.setType("1");
             returnVo.setUrl(merchantPay.getPayUrl());
             returnVo.setParams(pays);
             log.info(orderId + ":xlb online_pay请求参数：" + pays);
            
        }
        else {
        	 pays = MyUtils.describe(vo,"sign", "url","service", "version", "merId", "tradeNo", "tradeDate",
                     "amount", "notifyUrl", "extra", "summary","clientIp","bankId","bankcard","bankname","phoneNumber");
        	
        	returnVo.setType("6");
            returnVo.setUrl(merchantPay.getPayUrl());
            returnVo.setParams(pays);
            log.info(orderId + ":mbkj online_pay请求参数：" + pays);
        }
        return transfer(returnVo);
    }

    //回调
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(NewXlbPayVo vo, HttpServletRequest req) throws Exception {
    	 log.info("新迅联宝快捷回调接收参数：" + MyWebUtils.getRequestParameters(req));
         
         String service = vo.getService();
         String merId = vo.getMerId();
         String tradeNo = vo.getTradeNo();
         String tradeDate = vo.getTradeDate();
         String opeNo = vo.getOpeNo();
         String opeDate = vo.getOpeDate();
         String amount = vo.getAmount();
         String status = vo.getStatus();
         String extra = vo.getExtra();
         String payTime = vo.getPayTime();
         String sign = vo.getSign();
         String notifyType = vo.getNotifyType();
         
         String msg;

         log.info("新迅联宝快捷接收参数：" + MyWebUtils.getRequestParameters(req));
         try {

             //Assert.isTrue(validationTrustIp(req, DictionaryType.MBKJ_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");   

             MerchantPay mp = merchantPayService.findByMerNo(merId);
             Assert.notEmpty(mp);
             Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

             Assert.isTrue(status.equals("1"), "充值失败");

             String srcMsg = MyUtils.join("=", "&", "service", service, "merId", merId, "tradeNo", tradeNo,
                     "tradeDate", tradeDate, "opeNo", opeNo, "opeDate", opeDate, "amount", amount,
                     "status", status, "extra", extra, "payTime", payTime);
             // 验证签名
             Assert.isTrue(sign.equalsIgnoreCase(DigestUtils.signByMD5(srcMsg, mp.getSignKey())), "快捷验证签名失败");
             
             //查询订单状态
             String result = PayOrderController.queryPayOrder(vo, mp, null, req);
             
             Assert.isTrue(result.equals("1"), "查询订单状态失败");

             Boolean lockFlag = false;
             String loginName = extra;

             final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

             try {
                 lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
             } catch (Exception e) {
                 log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                 lockFlag = true;
             }

             try {
                 if (lockFlag) {
                     tradeService.doPayFlow(tradeNo, amount, loginName, mp, "",null);
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

         } catch (BusinessException e) {
             log.error("回调异常:", e);
         }
         return ERROR;
    }

}
