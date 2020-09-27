package com.nnti.pay.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.nnti.pay.controller.vo.BPPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * @author pony
 * @Description bossPay支付
 */
@Controller
@RequestMapping("/bp")
public class BPController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /**
     * 所有支付
     */
    @SuppressWarnings("rawtypes")
	@ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();
        String bankcode = payRequestVo.getBankCode();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");
        
        /*Double d = Math.floor(Math.random()*90 + 10);
        if(orderAmount.contains(".")){
        	orderAmount = orderAmount.substring(0, orderAmount.lastIndexOf("."))+"."+d.intValue();//2位小数
	    }else{
	    	orderAmount = orderAmount+"."+d.intValue();//2位小数
	    }*/

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        if (mp.getPayWay().equals(2)) {//微信H5不加小数
        	orderAmount = orderAmount.split("\\.")[0];
        }
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);
        

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");
        
        BPPayVo vo = JSON.readValue(mp.getRemain(), BPPayVo.class);
        List list = Arrays.asList(new Object[]{"10","20","30","40","50","60","70","80","90","100","200","300","400","500","600","700","800","900","1000","2000","3000"});
        List listzfb = Arrays.asList(new Object[]{"100","500","1000","2000","3000"});

        String type = "";
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            if (mp.getPayWay().equals(2)) {
                type = vo.getWap();// wap 微信
                Assert.isTrue(list.contains(orderAmount), "该支付目前只支持如下下单金额：</br>10,20,30,40,50,60,70,80,90</br>100,200,300,400,500,600,700,800,900</br>1000,2000,3000");
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getWap();// wap 支付宝
                Assert.isTrue(listzfb.contains(orderAmount), "该支付目前只支持如下下单金额：</br>100,500,1000,2000,3000");
            }else if (mp.getPayWay().equals(7)) {
                type = vo.getWap();// wap QQ
            }else if (mp.getPayWay().equals(3)) {
            	Assert.isTrue(StringUtils.isNotBlank(bankcode), "请选择支付银行");
                type = bankcode;// 在线支付
            }else if (mp.getPayWay().equals(4)) {
                type = vo.getWap();// 快捷
            }else if (mp.getPayWay().equals(10)) {
                type = vo.getWap();// wap 京东
            }else if (mp.getPayWay().equals(13)) {
                type = vo.getWap();// wap 银联
            }
        } else {
            if (mp.getPayWay().equals(2)) {
                type = vo.getPc();//PC微信扫码
                //Assert.isTrue(list.contains(orderAmount), "该支付目前只支持如下下单金额：</br>10,20,30,40,50,60,70,80,90</br>100,200,300,400,500,600,700,800,900</br>1000,2000,3000");
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getPc();//PC支付宝扫码
                Assert.isTrue(listzfb.contains(orderAmount), "该支付目前只支持如下下单金额：</br>100,500,1000,2000,3000");
            } else if (mp.getPayWay().equals(7)) {
                type = vo.getPc();//PCQQ
            } else if (mp.getPayWay().equals(3)) {
            	Assert.isTrue(StringUtils.isNotBlank(bankcode), "请选择支付银行");
                type = bankcode;// 在线支付
            } else if (mp.getPayWay().equals(4)) {
                type = vo.getPc();//快捷
            } else if (mp.getPayWay().equals(10)) {
                type = vo.getPc();// wap 京东
            } else if (mp.getPayWay().equals(13)) {
                type = vo.getPc();// wap 银联
            }
        }
        long date = new Date().getTime();
        String OrderDate = date/1000+"";
        // 组织请求数据
        vo.setOnline_pay(mp.getMerchantCode(),type,  orderAmount,orderId, mp.getNotifyUrl(), mp.getShopUrl(),OrderDate, loginName + "," + mp.getId());

        String param = "MerchantCode=["+vo.getMerchantCode()+"]OrderId=["+vo.getOrderId()+"]Amount=["+vo.getAmount()+"]NotifyUrl=["+vo.getNotifyUrl()+"]OrderDate=["+vo.getOrderDate()+"]BankCode=["+vo.getBankCode()+"]TokenKey=["+mp.getSignKey()+"]";

        String sign = DigestUtils.signByMD5(param).toUpperCase();
        log.info("签名：" + sign);
        vo.setSign(sign);

        ReturnVo returnVo = new ReturnVo();
        Map pays = MyUtils.describe(vo, "MerchantCode","BankCode", "OrderId", "Amount", "NotifyUrl", "ReturnUrl","OrderDate", "Remark", "Sign");
        log.info(orderId + ":请求参数：" + pays);

        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(BPPayVo vo, HttpServletRequest req) {
        log.info("bossPay 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String result = vo.getStatus();
        String remark = vo.getRemark();
        String sign = vo.getSign();
        String orderid = vo.getOrderId();
        String amount = vo.getAmount();
        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = remark.split(",")[0];
            String merId = remark.split(",")[1];
            

            Assert.isTrue(result.equals("1"), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            String paramsStr = "MerchantCode=["+vo.getMerchantCode()+"]OrderId=["+orderid+"]OutTradeNo=["+vo.getOutTradeNo()+"]Amount=["+amount+"]OrderDate=["+vo.getOrderDate()+"]BankCode=["+vo.getBankCode()+"]Remark=["+remark+"]Status=["+result+"]Time=["+vo.getTime()+"]TokenKey=["+mp.getSignKey()+"]";
    		String _sign = DigestUtils.signByMD5(paramsStr).toUpperCase();
            
            Assert.isTrue(_sign.equals(sign), ErrorCode.SC_30000_114.getType());

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
                    tradeService.doPayFlow(orderid, amount, loginName, mp, "",null);
                    return "success";
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
        return "";
    }
}
