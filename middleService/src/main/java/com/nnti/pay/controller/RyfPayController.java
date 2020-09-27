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
import com.nnti.common.security.EncryptionUtil;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.RyfPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * @author addis
 * @Description 如一付 支付
 */
@Controller
@RequestMapping("/ryf")
public class RyfPayController extends BasePayController {

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
        MerchantPay merchantPay = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, merchantPay);

        validationAmountCutAll(loginName, merchantPay);

        String orderId = basicService.createBillNo(loginName, orderAmount, merchantPay, loginName,"_");
        Assert.notEmpty(orderId);

        RyfPayVo vo = gson.fromJson(merchantPay.getRemain(), RyfPayVo.class);
        if (usetype == Constant.USE_TYPE_WEB) {
        	if(merchantPay.getPayPlatform().contains("ryfwx")){      
        		vo.setP_ChannelID("33");//wx
        	}
        	else if(merchantPay.getPayPlatform().contains("ryfqq")){
        		vo.setP_ChannelID("92");//qq
        	}
        	else{
        		vo.setP_ChannelID("36");//zfb
        	}
        	 
            loginName = "wap_" + loginName;
        }

        vo.setWxZfb(merchantPay.getMerchantCode(), orderId, orderAmount, orderAmount, 1,loginName + "," + merchantPay.getId(),merchantPay.getNotifyUrl(), merchantPay.getShopUrl(), merchantPay.getSignKey(), merchantPay.getApiUrl());
        
        vo.setP_PostKey(EncryptionUtil.encryptPassword(MyUtils.join("|", "|", vo.getP_UserID(), vo.getP_OrderID(), "", "", vo.getP_Price(), vo.getP_ChannelID(), vo.getP_PostKey())));

        vo.setApiUrl(merchantPay.getApiUrl());

        Map pays = MyUtils.describe(vo,"P_UserID", "P_OrderID", "P_FaceValue", "P_ChannelID", "P_Price", "P_Quantity", "P_Description", "P_Notic", "P_Result_URL", "P_Notify_URL", "P_PostKey", "apiUrl","P_ISsmart");
        
        log.info(orderId + ":请求参数：" + pays);
        
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(merchantPay.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":ryt zfb_wx请求参数：" + pays);
        return transfer(returnVo);
    }

    /*** 如一付支付宝 微信 回调接口**/
    @ResponseBody
    @RequestMapping(value = "/zfb_wx_return")
    public String zfb_wx_return(RyfPayVo vo, HttpServletRequest req) {
        log.info("ryf 接收参数：" + MyWebUtils.getRequestParameters(req));

        String userId = vo.getP_UserID();
        String orderId = vo.getP_OrderID();
        String faceValue = vo.getP_FaceValue();
        String channelId = vo.getP_ChannelID();
        String price = vo.getP_Price();
        String notic = vo.getP_Notic();
        String errCode = vo.getP_ErrCode();
        String errMsg = vo.getP_ErrMsg();
        String postKey = vo.getP_PostKey();
        String cardId = vo.getP_CardID();
        String cardPass = vo.getP_CardPass();
        String payMoney = vo.getP_PayMoney();

        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.RuYIFuPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = notic.split(",")[0];
            String merId = notic.split(",")[1];
            

            Assert.isTrue(errCode.equals("0"), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            String signMsg = EncryptionUtil.encryptPassword(MyUtils.join("|", "|", userId, orderId, cardId, cardPass, faceValue, channelId, payMoney, errCode, mp.getSignKey()));
            Assert.isTrue(postKey.equals(signMsg), ErrorCode.SC_30000_114.getType());

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
                    tradeService.doPayFlow(orderId, price, loginName, mp, "",null);
                    return "errCode=0";
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
        return "errCode=-1";
    }
    
    
    
    /**
     * 网银支付
     */
    @ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();
        //String P_ChannelId = payRequestVo.getBankCode();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());

        //根据商户号查询商户信息
        MerchantPay merchantPay = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, merchantPay);

        validationAmountCutAll(loginName, merchantPay);

        String orderId = basicService.createBillNo(loginName, orderAmount, merchantPay, loginName,"_");
        Assert.notEmpty(orderId);

        RyfPayVo vo = gson.fromJson(merchantPay.getRemain(), RyfPayVo.class);
        if (usetype == Constant.USE_TYPE_WEB) {
        	vo.setP_ChannelID("32");//手机快捷
            loginName = "wap_" + loginName;
        }

        vo.setOnline(merchantPay.getMerchantCode(), orderId, orderAmount, orderAmount, 1,loginName + "," + merchantPay.getId(),
                merchantPay.getNotifyUrl(), merchantPay.getShopUrl(), merchantPay.getSignKey(), merchantPay.getApiUrl());
        
        vo.setP_PostKey(EncryptionUtil.encryptPassword(
                MyUtils.join("|", "|", vo.getP_UserID(), vo.getP_OrderID(), "", "", vo.getP_Price(), vo.getP_ChannelID(), vo.getP_PostKey())));

        vo.setApiUrl(merchantPay.getApiUrl());

        Map pays = MyUtils.describe(vo,"P_UserId", "P_OrderId", "P_FaceValue", "P_ChannelId", "P_Price",
                    "P_Quantity", "P_Description", "P_Notic", "P_Result_URL", "P_Notify_URL", "P_PostKey", "apiUrl","P_IsSmart");
        
        log.info(orderId + ":请求参数：" + pays);
        
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(merchantPay.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":hftkj online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    /*** 汇天付网银 回调接口**/
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(RyfPayVo vo, HttpServletRequest req) throws Exception {
        log.info("hft网银 接收参数：" + MyWebUtils.getRequestParameters(req));

        String userId = vo.getP_UserID();
        String orderId = vo.getP_OrderID();
        String faceValue = vo.getP_FaceValue();
        String channelId = vo.getP_ChannelID();
        String price = vo.getP_Price();
        String notic = vo.getP_Notic();
        String errCode = vo.getP_ErrCode();
        String errMsg = vo.getP_ErrMsg();
        String postKey = vo.getP_PostKey();
        String cardId = vo.getP_CardID();
        String cardPass = vo.getP_CardPass();
        String payMoney = vo.getP_PayMoney();

        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.HTFPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = notic.split(",")[0];
            String merId = notic.split(",")[1];
            

            Assert.isTrue(errCode.equals("0"), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            String signMsg = EncryptionUtil.encryptPassword(MyUtils.join("|", "|", userId, orderId, cardId, cardPass, faceValue, channelId, payMoney, errCode, mp.getSignKey()));
            Assert.isTrue(postKey.equals(signMsg), ErrorCode.SC_30000_114.getType());

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
                    tradeService.doPayFlow(orderId, price, loginName, mp, "",null);
                    return "errCode=0";
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
        return "errCode=-1";
    }
    
    
}
