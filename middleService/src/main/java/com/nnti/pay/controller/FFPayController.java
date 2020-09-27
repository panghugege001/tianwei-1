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
import com.nnti.pay.controller.vo.FFPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

import net.sf.json.JSONObject;

/**
 * @author pony
 * @Description FFPay支付
 */
@Controller
@RequestMapping("/ff")
public class FFPayController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    /**
     * 所有支付
     */
	@SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();
        String bankcode = payRequestVo.getBankCode();
        String ip = payRequestVo.getCustomerIp();
        if(ip==null){
        	ip= "111.6.79.107";
        }

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");
        
        if(orderAmount.contains(".")){
        	orderAmount = orderAmount.substring(0, orderAmount.lastIndexOf("."))+".00";
	    }else{
	    	orderAmount = orderAmount+".00";
	    }

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);
        

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");
        
        FFPayVo vo = JSON.readValue(mp.getRemain(), FFPayVo.class);
        List list = Arrays.asList(new Object[]{"10.00","20.00","30.00","50.00","100.00","200.00","300.00","500.00"});

        String type = "";
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            if (mp.getPayWay().equals(2)) {
                type = vo.getWap();// wap 微信
                Assert.isTrue(list.contains(orderAmount), "该支付目前只支持如下下单金额：</br>10,20,30,50,100,200,300,500");
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
                Assert.isTrue(list.contains(orderAmount), "该支付目前只支持如下下单金额：</br>10,20,30,50,100,200,300,500");
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
        long date = new Date().getTime();
        String OrderDate = date+"";
        // 组织请求数据
        vo.setOnline_pay(mp.getMerchantCode(),type, orderId, orderAmount,mp.getNotifyUrl(), mp.getShopUrl(),OrderDate,ip,loginName + "," + mp.getId());

        Map<String, String> maps = MyUtils.describe(vo, "MerchantCode","BankCode", "OrderId", "Amount", "NotifyUrl","OrderDate","Ip");
        
        String params = MyUtils.obj2UrlParam(false, maps);
        
    	String sign = DigestUtils.md5(params+"&Key="+mp.getSignKey());
    	
    	sign = sign.toLowerCase();
    	
    	maps.put("Sign", sign);
    	maps.put("ReturnUrl",vo.getReturnUrl());
    	maps.put("Remark", vo.getRemark());
    	log.info("订单号" + orderId + "发送请求数据为：" + maps);

    	String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), MyWebUtils.getListNamevaluepair(maps));
    	System.out.println(result);
    	JSONObject json = JSONObject.fromObject(result);
    	
    	String code = json.getString("resultCode");
    	String msg = json.getString("resultMsg");
    	Boolean success = json.getBoolean("success");
    	System.out.println("success:"+success);
    	
    	Assert.isTrue("200".equals(code), msg);
    	
    	Assert.isTrue(success, msg);
    	
    	JSONObject data1 = json.getJSONObject("data");
    	String rdate =data1.getString("date");
    	String rsign =data1.getString("sign");
    	
    	JSONObject data2 = data1.getJSONObject("data");
    	String info = data2.getString("info");
    	String rtype = data2.getString("type");
    	String bankCode = data2.getString("bankCode");
    	String rdata="{\"bankCode\":\""+bankCode+"\",\"info\":\""+info+"\",\"type\":\""+rtype+"\"}";
    	String rparam = "data="+rdata+"&date="+rdate+"&merchantCode="+mp.getMerchantCode()+"&orderId="+orderId+"&Key="+mp.getSignKey();
    	System.out.println(rparam);
    	
    	if (mp.getPayWay().equals(3)) {
    		ReturnVo returnVo = new ReturnVo();
    		returnVo.setType("4");
    		returnVo.setData(info);
    		return transfer(returnVo);
    	}
    	String _rsign = DigestUtils.md5(rparam).toLowerCase();
    	System.out.println(_rsign);
    	
    	Assert.isTrue(rsign.equals(_rsign), "签名验证失败");
    	
    	ReturnVo returnVo = new ReturnVo();
		if ("url".equals(rtype) && ("ALIPAY".equals(bankCode) || "WECHAT".equals(bankCode))) {
    		returnVo.setType("2");
    		returnVo.setData(info);
    	}else{
    		returnVo.setType("1");
    		returnVo.setUrl(info);
    		returnVo.setParams(maps);
    	}
        return transfer(returnVo);
    }

    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(FFPayVo vo, HttpServletRequest req) {
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

            Map<String, String> maps = MyUtils.describe(vo, "Amount","BankCode", "MerchantCode", "OrderDate", "OrderId","OutTradeNo","Status","Time");
            
            String params = MyUtils.obj2UrlParam(false, maps);
            
        	String _sign = DigestUtils.md5(params+"&Key="+mp.getSignKey());
        	
        	_sign = _sign.toLowerCase();
            
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
    
    public static void main(String[] args) {
//		String ss= "data={\"bankCode\":\"ALIPAY\",\"info\":\"http://118.31.34.117/AlipayRedirect/DoNewStatic?userid=2088332389157492&channelId=176700B1F0FBD3DECFC&amount=100.0&MerchanOrderId=WenFu1544948032715931\",\"type\":\"url\"}&date=1544948033156&merchantCode=100392&orderId=dy_bpzfb_dytest01_4000971&key=248BB6E6630D1BAA2FCB50E71D417F70";
		String ss= "data={\"bankCode\":\"ALIPAY\",\"info\":\"http://118.31.34.117/AlipayRedirect/DoNewStatic?userid=2088332438549755&channelId=1319FC62E8F2B820CFE&amount=100.0&MerchanOrderId=WenFu1544948891302283\",\"type\":\"url\"}&date=1544948891672&merchantCode=100392&orderId=dy_bpzfb_dytest01_4000973&key=248BB6E6630D1BAA2FCB50E71D417F70"; 
		String _rsign = DigestUtils.md5(ss).toLowerCase();
//		String sign = "1eb44355178d5afb4fcbcaf4ffef927b";
		System.out.println(_rsign);
	}
}
