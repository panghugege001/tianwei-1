package com.nnti.pay.controller;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

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
import com.nnti.pay.api.rb.mb.utils.DateUtil;
import com.nnti.pay.controller.vo.BSPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

import net.sf.json.JSONObject;

/**
 * 百盛支付
 */
@Controller
@RequestMapping("/bs")
public class BSController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /**
     * 支付宝支付 或者微信支付
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response zfb_wx(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        String amount = NumericUtil.mul(Double.valueOf(orderAmount), 100).intValue()+"";

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());

        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"_");
        Assert.notEmpty(orderId);

        BSPayVo vo = JSON.readValue(mp.getRemain(), BSPayVo.class);
        String paymentTypeCode = "";
        Integer payWay = mp.getPayWay();
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            paymentTypeCode = vo.getWap();
        } else {
        	paymentTypeCode = vo.getPc();
        }
        String payDate = DateUtil.getDateFormatter();
        vo.setOnline_pay(mp.getMerchantCode(),payDate,paymentTypeCode, orderId, amount, mp.getNotifyUrl(), loginName + "," + mp.getId());

        String content = MyUtils.join("=", "&", "MerchantId",vo.getMerchantId(),"NotifyUrl", vo.getNotifyUrl(), "OutPaymentNo",vo.getOutPaymentNo(),
        		"PassbackParams",vo.getPassbackParams(),"PaymentAmount", vo.getPaymentAmount(),"PaymentTypeCode",vo.getPaymentTypeCode(),"Timestamp",vo.getTimestamp());

        String sign = DigestUtils.signByMD5(content, mp.getSignKey());
        vo.setSign(sign);

        Map pays = MyUtils.describe(vo, "MerchantId", "NotifyUrl", "OutPaymentNo", "PassbackParams", "PaymentAmount",
                "PaymentTypeCode","Timestamp", "Sign");

        log.info(orderId + ":bs online_pay请求参数：" + pays);
        ReturnVo returnVo = new ReturnVo();
        if(payWay ==13 || ((payWay == 1 || payWay == 2 ||payWay == 7||payWay == 10)  && usetype == Constant.USE_TYPE_PC )){
        	String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), MyWebUtils.getListNamevaluepair(pays));
        	
        	log.info(orderId + ":返回结果：" + result);
        	
        	JSONObject json = JSONObject.fromObject(result);
        	String code = json.getString("Code");
        	if(!"200".equals(code)){
        		Assert.isTrue(false, json.getString("Message"));
        	}
        	
        	String backQrCodeUrl = json.getString("QrCodeUrl");
        	
        	returnVo.setType("2");
        	returnVo.setData(backQrCodeUrl);
        }
        if((payWay == 1 || payWay == 2 ||payWay == 7||payWay == 10) && usetype == Constant.USE_TYPE_WEB){
            returnVo.setType("1");
            returnVo.setUrl(mp.getPayUrl());
            returnVo.setParams(pays);
        }

        return transfer(returnVo);
    }

    /*** 支付宝 微信 回调接口**/
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String zfb_wx_return(BSPayVo vo, HttpServletRequest req) {

        log.info("bs 接收参数：" + MyWebUtils.getRequestParameters(req));

        try {
	        String code = vo.getCode();
	        if(!"200".equals(code)){
	        	String msg = vo.getMessage();
	        	Assert.isTrue(false, msg);
	        }
	        String paymentState = vo.getPaymentState();
	        Assert.isTrue("S".equals(paymentState), "支付失败");
	        
	        String sign = vo.getSign();
	        String no = vo.getOutPaymentNo();
	
	        String money = vo.getPaymentAmount();//金额
	        String remarks = vo.getPassbackParams();//备注

            String loginName = remarks.split(",")[0];
            String merNo = remarks.split(",")[1];

            Assert.isTrue(validationTrustIp(req, DictionaryType.BSPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Double amount = NumericUtil.div(Double.valueOf(money), 100);

            MerchantPay mp = merchantPayService.findByMerNo(merNo);
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            //Assert.isTrue(RSASignature.doCheck(content, sign, mp.getPublicKey(), "UTF-8"), ErrorCode.SC_30000_114.getType());
            String content = MyUtils.join("=", "&", "Code",vo.getCode(),"MerchantId", vo.getMerchantId(), "OutPaymentNo",vo.getOutPaymentNo(),
            		"PassbackParams",vo.getPassbackParams(),"PaymentAmount", vo.getPaymentAmount(),"PaymentFee",vo.getPaymentFee(),
            		"PaymentNo",vo.getPaymentNo(),"PaymentState",vo.getPaymentState());

            String signStr = DigestUtils.signByMD5(content, mp.getSignKey());
            log.info("加密后字符signStr："+signStr); 
            
            Assert.isTrue(signStr.toUpperCase().equals(sign), ErrorCode.SC_30000_114.getType());
            
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
                    tradeService.doPayFlow(no, amount.toString(), loginName, mp, "",null);
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
            log.error("回调异常：", e);
        }
        return ERROR;
    }
}
