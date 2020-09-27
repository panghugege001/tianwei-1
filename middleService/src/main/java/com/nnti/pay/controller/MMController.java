package com.nnti.pay.controller;

import java.util.Date;
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
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.MMPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

import net.sf.json.JSONObject;

/**
 * Created by pony on 2018/07/01.
 */
@Controller
@RequestMapping("/mm")
public class MMController extends BasePayController {

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

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());

        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName,"_");
        Assert.notEmpty(orderId);
        orderId = orderId.substring(3);//防止超过30位

        if (!MyUtils.isNotEmpty(payerIp)) {
            payerIp = "127.0.0.1";
        }
        MMPayVo vo = gson.fromJson(mp.getRemain(), MMPayVo.class);
        String type = "";
        String account_type = "";
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
            	type = vo.getWap();//在线支付
                account_type = "PRIVATE_DEBIT_ACCOUNT";
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
            	type = vo.getPc();//在线支付
                account_type = "PRIVATE_DEBIT_ACCOUNT";
            } else if (mp.getPayWay().equals(4)) {
                type = vo.getPc();//快捷
            } else if (mp.getPayWay().equals(10)) {
                type = vo.getPc();// wap 京东
            } else if (mp.getPayWay().equals(13)) {
                type = vo.getPc();// wap 银联
            }
        }
        // 组织请求数据
        String request_time = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
        vo.setBank_code(bankcode);
        vo.setOnlinePay(orderAmount,orderId,payerIp,type,request_time, "war3",mp.getShopUrl(), mp.getNotifyUrl(), loginName + "," + mp.getId(), bankcode,account_type);

        String param = MyUtils.obj2UrlParam(vo, true, "callback_url", "goods_name","ord_amount", "product_type","remark", "request_id", "request_ip", "request_time", "return_url", "trx_key");
        String sign = DigestUtils.signByMD5(param+"&secret_key="+mp.getSignKey()).toUpperCase();
        vo.setSign(sign);

        Map pays = MyUtils.describe(vo, "trx_key", "ord_amount", "request_id", "request_ip", "product_type", "request_time", 
        		"goods_name", "return_url", "callback_url","remark", "sign");

        log.info(orderId + ":mm online_pay请求参数：" + pays);
        
        ReturnVo returnVo = new ReturnVo();
        //Integer payWay = mp.getPayWay();
        String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), MyWebUtils.getListNamevaluepair(pays));
        log.info(orderId + ":mm online_pay返回结果：" + result);
        
        JSONObject json = JSONObject.fromObject(result);
        
        Assert.isTrue(json.containsKey("data"), "获取支付宝链接异常，请稍后在尝试");
        
        String rsp_code = json.getString("rsp_code");
        String rsp_msg = json.getString("rsp_msg");
        String data = json.getString("data");
        Assert.isTrue("0000".equals(rsp_code), rsp_msg);
        
        returnVo.setType("1");
        returnVo.setUrl(data);
        returnVo.setParams(pays);
        /*if(payWay ==13||payWay == 10 || ((payWay == 1 || payWay == 2 ||payWay == 7)  && usetype == Constant.USE_TYPE_PC )){
        }
        if(payWay ==4 || ((payWay == 1 || payWay == 2 ||payWay == 7) && usetype == Constant.USE_TYPE_WEB)){
            returnVo.setType("1");
            returnVo.setUrl(mp.getPayUrl());
            returnVo.setParams(pays);
        }*/

        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(MMPayVo vo, HttpServletRequest req) {

        log.info("mm 接收参数：" + MyWebUtils.getRequestParameters(req));

        String orderid = vo.getRequest_id();
        String trx_status = vo.getTrx_status();
        String ord_amount = vo.getOrd_amount();
        String sign = vo.getSign();
        String remark = vo.getRemark();

        try {
        	Assert.isTrue("SUCCESS".equals(trx_status), "支付失败");
        	
            Assert.isTrue(validationTrustIp(req, DictionaryType.MMPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

            Assert.isTrue(MyUtils.isNotEmpty(orderid), ErrorCode.SC_30000_111.getType());
            
            String loginName = remark.split(",")[0];
            String merId = remark.split(",")[1];

            //根据商户号查询商户信息
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.notEmpty(mp);
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            /*String param = MyUtils.obj2UrlParam(vo, true, "goods_name","ord_amount", "pay_request_id","product_type","remark", 
            		"request_id", "request_time", "trx_key", "trx_status", "trx_time");
            String _sign = DigestUtils.signByMD5(param, "&secret_key="+mp.getSignKey());*/
            
        	Map<String, Object> maps = MyUtils.describe(vo,"trx_time", "goods_name","ord_amount", "pay_request_id","product_type","remark",  "request_id", "request_time", "trx_key", "trx_status");
        	String _sign = DigestUtils.getSortedSign(maps, "&secret_key="+mp.getSignKey());

            Assert.isTrue(sign.equalsIgnoreCase(_sign), ErrorCode.SC_30000_114.getType());

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
                    tradeService.doPayFlow(orderid, ord_amount, loginName, mp, "",null);
                    return "SUCESS";
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
    
    public static void main(String[] args) throws Exception {
    	/*{request_time=20180701210105, request_id=mmzfb_dytest07_4000041, 
    	ord_amount=50.00, remark=wap_dytest07,31, goods_name=war3, 
    	sign=1456C51805B9B7DA5BB7184E2DAE7F01, trx_time=20180701224412, 
    	trx_key=2a7dfbb3a3244039a6c89cdee0c3f358, product_type=20203, rt=/mm/online_pay_return.php, 
    	trx_status=SUCCESS, requestIp=47.75.59.199, pay_request_id=77772018070110181084}*/
    	MMPayVo vo = new MMPayVo();
    	vo.setRequest_time("20180701210105");
    	vo.setRequest_id("mmzfb_dytest07_4000041");
    	vo.setOrd_amount("50.00");
    	vo.setRemark("wap_dytest07,31");
    	vo.setGoods_name("war3");
    	vo.setTrx_time("20180701224412");
    	vo.setTrx_key("2a7dfbb3a3244039a6c89cdee0c3f358");
    	vo.setProduct_type("20203");
    	vo.setTrx_status("SUCCESS");
    	vo.setRequest_ip("47.75.59.199");
    	vo.setPay_request_id("77772018070110181084");
    	//String param = MyUtils.obj2UrlParam(vo, true, "goods_name","ord_amount", "pay_request_id","product_type","remark",  "request_id", "request_time", "trx_key", "trx_status", "trx_time");
    	Map<String, Object> maps = MyUtils.describe(vo,"trx_time", "goods_name","ord_amount", "pay_request_id","product_type","remark",  "request_id", "request_time", "trx_key", "trx_status");
    	String _sign = DigestUtils.getSortedSign(maps, "&secret_key=0730b6bef15d41c5a2d0e40e2544b514");
        //String _sign = DigestUtils.signByMD5(param, "&secret_key=0730b6bef15d41c5a2d0e40e2544b514");
        System.out.println(_sign.equalsIgnoreCase("1456C51805B9B7DA5BB7184E2DAE7F01"));
	}
}
