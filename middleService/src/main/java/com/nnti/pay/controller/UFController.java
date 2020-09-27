package com.nnti.pay.controller;

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
import com.nnti.pay.controller.vo.LkPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * Created by pony on 2018/06/09.
 */
@Controller
@RequestMapping("/uf")
public class UFController extends BasePayController {

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
        String payerIp = payRequestVo.getCustomerIp();
        String orderAmount = payRequestVo.getOrderAmount();
        String bankcode = payRequestVo.getBankCode();

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");
        /*if(orderAmount.contains(".")){
        	orderAmount = orderAmount.substring(0, orderAmount.lastIndexOf("."))+"."+((int)Math.random()*90+10);//2位小数
        }else{
        	orderAmount = orderAmount+"."+((int)Math.random()*90+10);//2位小数
        }*/
        
        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());

        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"_");
        Assert.notEmpty(orderId);

        if (!MyUtils.isNotEmpty(payerIp)) {
            payerIp = "127.0.0.1";
        }
        LkPayVo vo = gson.fromJson(mp.getRemain(), LkPayVo.class);
        String type = "";
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            if (mp.getPayWay().equals(2)) {
                type = vo.getWap();// wap 微信
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getWap();// wap 支付宝
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
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getPc();//PC支付宝扫码
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
        vo.setType(type);
        vo.setOnlinePay(mp.getMerchantCode(), orderId, mp.getNotifyUrl(), mp.getShopUrl(), orderAmount, loginName + "," + mp.getId(), payerIp);

        String param = MyUtils.obj2UrlParam(vo, true, "parter", "type", "value", "orderid", "callbackurl");
        String sign = DigestUtils.signByMD5(param, mp.getSignKey());
        vo.setSign(sign);

        Map pays = MyUtils.describe(vo, "parter", "type", "value", "orderid", "callbackurl", "hrefbackurl", "attach", "payerIp", "sign");

        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":uf online_pay请求参数：" + pays);
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(LkPayVo vo, HttpServletRequest req) {

        log.info("uf 接收参数：" + MyWebUtils.getRequestParameters(req));

        String orderid = vo.getOrderid();
        String opstate = vo.getOpstate();
        String ovalue = vo.getOvalue();
        String sign = vo.getSign();
        String remark = vo.getAttach();
        String msg = vo.getMsg();

        try {
        	Assert.isTrue("0".equals(opstate), msg);
        	
            Assert.isTrue(validationTrustIp(req, DictionaryType.LKPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue(MyUtils.isNotEmpty(orderid), ErrorCode.SC_30000_111.getType());
            
            String loginName = orderid.split("_")[2];
            String payway = orderid.split("_")[1];
            String merId = "";
            if(payway.equals("ufwx")){
            	merId = "26";
            }else if (payway.equals("ufzfb")){
            	merId = "27";
            }else if (payway.equals("ufqq")){
            	merId = "28";
            }else if (payway.equals("ufwy")){
            	merId = "29";
            }else if (payway.equals("ufyl")){
            	merId = "30";
            }

            //根据商户号查询商户信息
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String param = MyUtils.obj2UrlParam(vo, true, "orderid", "opstate", "ovalue");
            String _sign = DigestUtils.signByMD5(param, mp.getSignKey());

            Assert.isTrue(sign.equals(_sign), ErrorCode.SC_30000_114.getType());

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

        } catch (Exception e) {
            log.error("回调异常：", e);
        }
        return "opstate=-2";
    }
}
