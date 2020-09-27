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
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/1/19.
 * 讯联宝支付业务
 */
@Controller
@RequestMapping("/xlb")
public class XLBController extends BasePayController {

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
        
        //Assert.isTrue((Double.parseDouble(orderAmount)%10)==0, "该支付只支持10的倍数下单哦");
        
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


        // 组织请求数据  
        String tradeDate = DateUtil.format(DateUtil.YYYYMMDD, new Date());
        XlbPayVo vo = gson.fromJson(mp.getRemain(), XlbPayVo.class);
        List list = Arrays.asList(new Object[]{"20","30","50","100"});
        String type = "";
        if (usetype == Constant.USE_TYPE_WEB) {
            if (mp.getPayWay().equals(1)) {
                type = "ZFBWAP";// wap支付宝
            }
            if (mp.getPayWay().equals(2)) {
            	type = "WXWAP";// 微信H5
            	Assert.isTrue(list.contains(orderAmount), "该支付目前只支持如下下单金额：</br>20,30,50,100");
            }
        }
        
        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"300");
        
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;  
        }
        vo.setBankCode(type);
        
        ReturnVo returnVo = new ReturnVo();
        
        if (vo.getBankCode().equals("ZFBWAP")) {
        	 vo.setApiName("WAP_PAY_B2C");
        	 vo.setApiUrl(mp.getApiUrl());
        	
	       	 vo.setWxZfb(mp.getMerchantCode(), mp.getMerchantCode(), orderId, tradeDate,
	                 payRequestVo.getOrderAmount(), mp.getNotifyUrl(), loginName, "");
	
	         String paramsStr = MyUtils.obj2UrlParam(vo, true,
	                 "apiName", "apiVersion", "platformID", "merchNo", "orderNo", "tradeDate",
	                 "amt", "merchUrl", "merchParam", "tradeSummary");
	         vo.setSignMsg(DigestUtils.signByMD5(paramsStr, mp.getSignKey()));
        	
            Map pays = MyUtils.describe(vo, "apiName", "apiVersion", "platformID", "merchNo", "orderNo", "tradeDate",
                    "amt", "merchUrl", "merchParam", "tradeSummary","signMsg","bankCode","apiUrl");
            log.info(orderId + ":请求参数：" + pays);
            returnVo.setType("1");
            returnVo.setUrl(mp.getPayUrl());
            returnVo.setParams(pays);
        }else if (vo.getBankCode().equals("WXWAP")) {
       	 	vo.setApiName("WAP_PAY_B2C");
       	 	vo.setApiUrl(mp.getApiUrl());
       	
	       	 vo.setWxZfb(mp.getMerchantCode(), mp.getMerchantCode(), orderId, tradeDate,
	                 payRequestVo.getOrderAmount(), mp.getNotifyUrl(), loginName, "");
	
	         String paramsStr = MyUtils.obj2UrlParam(vo, true,
	                 "apiName", "apiVersion", "platformID", "merchNo", "orderNo", "tradeDate",
	                 "amt", "merchUrl", "merchParam", "tradeSummary");
	         vo.setSignMsg(DigestUtils.signByMD5(paramsStr, mp.getSignKey()));
       	
           Map pays = MyUtils.describe(vo, "apiName", "apiVersion", "platformID", "merchNo", "orderNo", "tradeDate",
                   "amt", "merchUrl", "merchParam", "tradeSummary","signMsg","bankCode","apiUrl");
           log.info(orderId + ":请求参数：" + pays);
           returnVo.setType("1");
           returnVo.setUrl(mp.getPayUrl());
           returnVo.setParams(pays);
       
        }else{
        	
        	 vo.setWxZfb(mp.getMerchantCode(), mp.getMerchantCode(), orderId, tradeDate,
                     payRequestVo.getOrderAmount(), mp.getNotifyUrl(), loginName, customerIp);

             String paramsStr = MyUtils.obj2UrlParam(vo, true,
                     "apiName", "apiVersion", "platformID", "merchNo", "orderNo", "tradeDate",
                     "amt", "merchUrl", "merchParam", "tradeSummary", "overTime", "customerIP");
             vo.setSignMsg(DigestUtils.signByMD5(paramsStr, mp.getSignKey()));
        	
            //构建请求参数
            String url = "http://pay.yaica.top:88/res_shop.jsp";

            log.info(orderId + ":xlb zfb_wx请求参数：" + paramsStr);

            String result = MyWebUtils.getHttpContentByBtParam(url, paramsStr + "&signMsg=" + vo.getSignMsg());
            //判断是否是迅联宝支付宝
            if(mp.getPayPlatform().contains("xlbzfb")){
            	result = result.substring(0,result.indexOf("}")+1);
            }
            log.info(orderId + ":返回数据：" + result);

            XlbResponseVo responseVo = gson.fromJson(result, XlbResponseVo.class);
            Assert.isTrue(responseVo.getResultCode().equals("00"), responseVo.getMessage());

            String message = DigestUtils.decodeByBase64(responseVo.getCode());

            log.info(orderId + ":返回二维码：" + message);
            returnVo.setType("2");
            returnVo.setData(message);
        }

        return transfer(returnVo);
    }
    

    @ResponseBody
    @RequestMapping("/zfb_wx_return")
    public String zfb_wx_return(XlbPayVo vo, HttpServletRequest req) throws Exception {
        String apiName = vo.getApiName();
        String notifyTime = vo.getNotifyTime();
        String tradeAmt = vo.getTradeAmt();
        String merchNo = vo.getMerchNo();
        String merchParam = vo.getMerchParam();
        String orderNo = vo.getOrderNo();
        String tradeDate = vo.getTradeDate();
        String accNo = vo.getAccNo();
        String accDate = vo.getAccDate();
        String orderStatus = vo.getOrderStatus();
        String signMsg = vo.getSignMsg();
        String notifyType = vo.getNotifyType();

        String msg;

        log.info("xlb接收参数：" + MyWebUtils.getRequestParameters(req));
        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.XLB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            MerchantPay mp = merchantPayService.findByMerNo(merchNo);
            
            Assert.notEmpty(mp);
            
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            Assert.isTrue(orderStatus.equals("1"), "充值失败");

            String srcMsg = MyUtils.join("=", "&", "apiName", apiName, "notifyTime", notifyTime, "tradeAmt", tradeAmt,
                    "merchNo", merchNo, "merchParam", merchParam, "orderNo", orderNo, "tradeDate", tradeDate,
                    "accNo", accNo, "accDate", accDate, "orderStatus", orderStatus);
            // 验证签名
            Assert.isTrue(signMsg.equalsIgnoreCase(DigestUtils.signByMD5(srcMsg, mp.getSignKey())), "验签失败");
            
//            //查询订单状态
//            vo.setQueryOrder(vo.getMerchNo(), "1.0.0.0", vo.getTradeAmt(), "MOBO_TRAN_QUERY");
//            
//            String result = PayOrderController.queryPayOrder(vo, mp, null, req);
//            
//            Assert.isTrue(result.equals("1"), "查詢訂單交易失敗");

            Boolean lockFlag = false;
            String loginName = merchParam;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(orderNo, tradeAmt, loginName, mp, "",null);
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
            log.error("回掉异常:", e);
        }
        return ERROR;
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String bankCode = payRequestVo.getBankCode();
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName); 
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay merchantPay = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, merchantPay);

        String orderId = basicService.createBillNo(loginName, orderAmount, merchantPay, loginName,"_");

        XlbPayVo vo = gson.fromJson(merchantPay.getRemain(), XlbPayVo.class);
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
        }

        // 组织请求数据
        String platformID = merchantPay.getMerchantCode();
        String merchNo = merchantPay.getMerchantCode();
        String tradeDate = DateUtil.format(DateUtil.YYYYMMDD, new Date());
        String merchUrl = merchantPay.getNotifyUrl();
        vo.setOnline(platformID, merchNo, orderId, tradeDate, orderAmount, merchUrl, loginName, bankCode);

        String paramsStr = MyUtils.obj2UrlParam(vo, true,
                "apiName", "apiVersion", "platformID", "merchNo", "orderNo", "tradeDate",
                "amt", "merchUrl", "merchParam", "tradeSummary");
        
        vo.setSignMsg(DigestUtils.signByMD5(paramsStr, merchantPay.getSignKey()));
        vo.setUrl(merchantPay.getApiUrl());

        Map pays = MyUtils.describe(vo, "apiName", "apiVersion", "platformID", "merchNo", "orderNo",
                "tradeDate", "amt", "merchUrl", "merchParam", "tradeSummary", "signMsg", "url", "choosePayType", "bankCode");
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(merchantPay.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":xlb online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    //回调
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(XlbPayVo vo, HttpServletRequest req) throws Exception {
        log.info("xlb接收参数：" + MyWebUtils.getRequestParameters(req));

        String apiName = vo.getApiName();
        String notifyTime = vo.getNotifyTime();
        String tradeAmt = vo.getTradeAmt();
        String merchNo = vo.getMerchNo();
        String merchParam = vo.getMerchParam();
        String orderNo = vo.getOrderNo();
        String tradeDate = vo.getTradeDate();
        String accNo = vo.getAccNo();
        String accDate = vo.getAccDate();
        String orderStatus = vo.getOrderStatus();
        String signMsg = vo.getSignMsg();

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.XLB_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            MerchantPay mp = merchantPayService.findByMerNo(merchNo);
            
            Assert.notEmpty(mp);
            
            Assert.isTrue(orderStatus.equals("1"), "充值失败");
            
            String paramsStr = MyUtils.obj2UrlParam(vo, true,
                    "apiName", "notifyTime", "tradeAmt", "merchNo", "merchParam", "orderNo",
                    "tradeDate", "accNo", "accDate", "orderStatus");
            Assert.isTrue(signMsg.equalsIgnoreCase(DigestUtils.signByMD5(paramsStr, mp.getSignKey())), ErrorCode.SC_30000_114.getType());
            
            //查询订单状态
            //vo.setQueryOrder(vo.getMerchNo(), "1.0.0.0", vo.getTradeAmt(), "MOBO_TRAN_QUERY");
            
            //String result = PayOrderController.queryPayOrder(vo, mp, null, req);
            
            //Assert.isTrue(result.equals("1"), "查詢訂單交易失敗");

            Boolean lockFlag = false;
            String loginName = merchParam;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(orderNo, tradeAmt, loginName, mp, "",null);
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
            log.error("online_pay_return回调异常：", e);
        }
        return ERROR;
    }

    public static void main(String[] args) {
    	//apiName=MOBO_TRAN_QUERY&apiVersion=1.0.0.0&platformID=210001110013525&merchNo=210001110013525&orderNo=dy_xlbzfb_dytest07_4000195&tradeDate=20180807&amt=10.00&signMsg=3b2571ed2b552b62b8134ec0a80c0893

	}
}
