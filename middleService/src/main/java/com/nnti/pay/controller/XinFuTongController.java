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
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.XinfutongVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;
import com.nnti.withdraw.util.SHA;

/**
 * Created by pony on 2018/04/19.
 * 信付通 支付业务
 */
@Controller
@RequestMapping("/xft")
public class XinFuTongController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    @SuppressWarnings("rawtypes")
	@ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        //String bankCode = payRequestVo.getBankCode();
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


        // 组织请求数据
        XinfutongVo vo = gson.fromJson(merchantPay.getRemain(), XinfutongVo.class);
        if (usetype == Constant.USE_TYPE_WEB) {
        	loginName = "wap_" + loginName;
        }else{
        	vo.setIsApp("web");
        }
        vo.setOnline(loginName, vo.getDefaultbank(), vo.getIsApp(), merchantPay.getMerchantCode(), merchantPay.getNotifyUrl(), orderId, merchantPay.getShopUrl(), "warIII", orderAmount);

        String signSrc = MyUtils.obj2UrlParam(vo, false, "body", "charset", "defaultbank", "isApp", "merchantId", "notifyUrl", "orderNo",
                "paymentType", "paymethod", "returnUrl", "sellerEmail", "service", "title", "totalFee");
        
        log.info("signStc:"+signSrc);  
        
        vo.setSign(SHA.signDeposit(signSrc+merchantPay.getSignKey(), "utf-8"));
        
        log.info("sign:"+vo.getSign());
        
        Map pays = MyUtils.describe(vo, "sign", "body", "charset", "defaultbank", "isApp", "merchantId",
                "notifyUrl", "orderNo", "paymentType", "paymethod", "returnUrl", "sellerEmail", "service", "title", "totalFee", "signType");
        
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(merchantPay.getPayUrl()+merchantPay.getMerchantCode()+"-"+orderId);
        returnVo.setParams(pays);

        log.info(orderId + ":信付通 快捷  online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    //回调
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(XinfutongVo vo, HttpServletRequest req) throws Exception {
        log.info("信付通 接收参数：" + MyWebUtils.getRequestParameters(req));

        String body = vo.getBody();
        String is_success = vo.getIs_success();
        String order_no = vo.getOrder_no();
        String seller_id = vo.getSeller_id();
        String total_fee = vo.getTotal_fee();
        String trade_status = vo.getTrade_status();
        String sign = vo.getSign();
        

        try {

            Assert.isTrue(validationTrustIp(req, DictionaryType.ZinFTongPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            MerchantPay mp = merchantPayService.findByMerNo(seller_id);
            Assert.notEmpty(mp);
            Assert.isTrue(is_success.equals("T"), "交易失败");
            Assert.isTrue(trade_status.equals("TRADE_FINISHED"), "充值失败");
            String paramsStr = MyUtils.obj2UrlParam(vo, true,
                    "body","discount","ext_param2","gmt_create","gmt_logistics_modify","gmt_payment","is_success","is_total_fee_adjust","notify_id","notify_time",
                    "notify_type","order_no","payment_type","price","quantity","seller_actions","seller_email","seller_id","title","total_fee",
                    "trade_no", "trade_status");
            
            log.info("签名："+paramsStr+mp.getSignKey());
            
            Assert.isTrue(sign.equalsIgnoreCase(SHA.signDeposit(paramsStr+mp.getSignKey(), "utf-8")), "信付通验证签名失败");

            Boolean lockFlag = false;
            String loginName = body;

            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }

            try {
                if (lockFlag) {
                    tradeService.doPayFlow(order_no, total_fee, loginName, mp, "",null); 
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
