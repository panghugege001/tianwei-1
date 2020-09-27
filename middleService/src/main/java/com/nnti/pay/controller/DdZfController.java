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
import com.nnti.pay.controller.vo.DdzfPayVo;
import com.nnti.pay.controller.vo.NewXlbPayVo;
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

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Addis on 2017/1/19.
 * 多多支付业务
 */
@Controller
@RequestMapping("/ddzf")
public class DdZfController extends BasePayController {

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
        String bankCode = payRequestVo.getBankCode();
        
        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");
        
        DecimalFormat currentNumberFormat = new DecimalFormat("#0.00");
        orderAmount = currentNumberFormat.format(Double.parseDouble(orderAmount));

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
        DdzfPayVo vo = gson.fromJson(mp.getRemain(), DdzfPayVo.class);
        if("".equals(vo.getBankCode()) || null == vo.getBankCode()){
        	vo.setBankCode(bankCode);
        }
        vo.setWxZfb(mp.getMerchantCode(), orderId, orderAmount,"abc", loginName, mp.getShopUrl(), mp.getNotifyUrl());
        String paramsStr = MyUtils.obj2UrlParam(vo, true,
                "MerId", "OrdId", "OrdAmt", "PayType", "CurCode", "BankCode", "ProductInfo",
                "Remark", "ReturnURL", "NotifyURL","SignType");
        vo.setSignInfo(DigestUtils.signByMD5(paramsStr, "&MerKey="+mp.getSignKey()));
        
        log.info("签名数据：" + paramsStr+"&MerKey="+mp.getSignKey());
        
        ReturnVo returnVo = new ReturnVo();
        Map pays = MyUtils.describe(vo, "SignInfo","MerId", "OrdId", "OrdAmt", "PayType", "CurCode", "BankCode","ProductInfo","Remark","ReturnURL","NotifyURL", "SignType");
        log.info(orderId + ":请求参数：" + pays);
        
        returnVo.setType("1");  
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping("/zfb_wx_return")
    public String zfb_wx_return(DdzfPayVo vo, HttpServletRequest req) throws Exception {
    	
    	 log.info("多多支付回调接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String MerId = vo.getMerId();
        String OrdId = vo.getOrdId();
        String OrdAmt = vo.getOrdAmt();
        String OrdNo = vo.getOrdNo();
        String ResultCode = vo.getResultCode();
        String Remark = vo.getRemark();
        String SignType = vo.getSignType();
        String SignInfo = vo.getSignInfo();
        String msg;

        log.info("ddzf接收参数：" + MyWebUtils.getRequestParameters(req));
        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.DDPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");   

            MerchantPay mp = merchantPayService.findByMerNo(MerId);
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            Assert.isTrue(ResultCode.equals("success002"), "充值失败");
            
            String str = MyUtils.obj2UrlParam(vo, true, "MerId", "OrdId", "OrdAmt", "OrdNo", "ResultCode", "Remark", "SignType");
            
            // 验证签名
            Assert.isTrue(SignInfo.equalsIgnoreCase(DigestUtils.signByMD5(DigestUtils.signByMD5(str), mp.getSignKey())), "多多支付验证签名失败");
            

            Boolean lockFlag = false;
            String loginName = Remark;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(OrdId, OrdAmt, loginName, mp, "",null);
                    return "success|9999";
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
