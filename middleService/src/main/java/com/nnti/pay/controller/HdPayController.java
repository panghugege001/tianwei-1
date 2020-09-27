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
import com.nnti.pay.controller.vo.HdPayVo;
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
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Addis on 2018/01/09.
 * 汇达 支付业务
 */
@Controller
@RequestMapping("/hdpay")
public class HdPayController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    @ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();
        String customerIp = payRequestVo.getCustomerIp();

        Assert.notEmpty(loginName, platformId, orderAmount,usetype);

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay merchantPay = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, merchantPay);
        
        // 客户端IP
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }

        String orderId = basicService.createBillNo(loginName, orderAmount, merchantPay, loginName,"_");
        
        // 组织请求数据
        String platformID = merchantPay.getMerchantCode();
        String merchNo = merchantPay.getMerchantCode();
        String tradeDate = DateUtil.format(DateUtil.YYYYMMDD, new Date());
        String merchUrl = merchantPay.getNotifyUrl();
        HdPayVo vo = gson.fromJson(merchantPay.getRemain(), HdPayVo.class);
        
        if (usetype == Constant.USE_TYPE_WEB) {
        	vo.setApiName("WAP_PAY_B2C");
            loginName = "wap_" + loginName;
        }
        
        vo.setOnline(platformID, merchNo, orderId, tradeDate, orderAmount, merchUrl, loginName,"",customerIp);

        String paramsStr = MyUtils.obj2UrlParam(vo, true,"apiName", "apiVersion", "platformID", "merchNo", "orderNo", "tradeDate","amt", "merchUrl", "merchParam", "tradeSummary");
        
        vo.setSignMsg(DigestUtils.signByMD5(paramsStr, merchantPay.getSignKey()));
        
        vo.setApiUrl(merchantPay.getApiUrl());

        Map pays = MyUtils.describe(vo,"apiUrl", "apiName", "apiVersion", "platformID", "merchNo", "orderNo","tradeDate", "amt", "merchUrl", "merchParam", "tradeSummary", "signMsg", "choosePayType");
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(merchantPay.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":hd pay online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    //回调
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(HdPayVo vo, HttpServletRequest req) throws Exception {
        log.info("hd pay接收参数：" + MyWebUtils.getRequestParameters(req));

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

            Assert.isTrue(validationTrustIp(req, DictionaryType.HDPAY_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

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

}
