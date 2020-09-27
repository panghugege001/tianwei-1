package com.nnti.pay.controller;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.MMHLPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

import net.sf.json.JSONObject;

/**
 * 美名互联支付
 */
@Controller
@RequestMapping("/mmhl")
public class MMHLController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    protected Gson gson = new Gson();

    @SuppressWarnings("unchecked")
	@ResponseBody
    @RequestMapping("/online_pay")
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        if (null == payRequestVo) {

            throw new BusinessException(ErrorCode.SC_10001.getCode(), "参数转换成对象为【空】！");
        }

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();
        String bankcode = payRequestVo.getBankCode();
        if(bankcode == null){
        	bankcode = "";
        }
        String customerIp = payRequestVo.getCustomerIp();
        
        if (StringUtils.isBlank(customerIp)) {

            customerIp = "127.0.0.1";
        }

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        User user = basicService.getUser(loginName);
        
        validationLoginName(loginName, user);

        validationAgent(user.getRole());

        MerchantPay mp = merchantPayService.findById(platformId);
        
        validationMerchantPay(orderAmount, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "300");
        
        String remark = loginName + "," + mp.getId();

        MMHLPayVo vo = JSON.readValue(mp.getRemain(), MMHLPayVo.class);

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
        // 组织请求数据
        Map<String, Object> maps = null;
        vo.setOnline_pay(mp.getMerchantCode(),type, mp.getNotifyUrl(), mp.getShopUrl(),orderId,orderAmount,remark,"0","");
        if(type.equals("ALICODE")){
        	maps = MyUtils.describe(vo, "body", "mch_id", "notify_url", "out_trade_no", "total_fee","type");
        	String sign = DigestUtils.getSortedSign(maps,mp.getSignKey());
        	sign = sign.toLowerCase(); 
        	vo.setSign(sign);
        	maps.put("sign", sign);
        	log.info("订单号" + orderId + "发送请求数据为：" + maps);
        	
        	String result = MyWebUtils.sendFormMsgFastPay(JSON.writeValueAsString(maps), mp.getPayUrl());
        	System.out.println(result);
        	JSONObject json = JSONObject.fromObject(result);
        	
        	String code = json.getString("error_code");
        	String msg = json.getString("error_msg");
        	
        	Assert.isTrue("0".equals(code), msg);
        	
        	String qr_code = json.getString("qr_code");
        	
        	ReturnVo returnVo = new ReturnVo();
        	returnVo.setType("2");
        	returnVo.setData(qr_code);
        	return transfer(returnVo);
        }
        if(type.equals("ALIH5")){
        	maps = MyUtils.describe(vo, "back_url", "body", "card_type", "mch_id", "notify_url", "out_trade_no", "total_fee","type");
        	String sign = DigestUtils.getSortedSign(maps,mp.getSignKey());
        	sign = sign.toLowerCase();
        	vo.setSign(sign);
        	maps.put("sign", sign);
        	log.info("订单号" + orderId + "发送请求数据为：" + maps);
        	
        	String result = MyWebUtils.sendFormMsgFastPay(JSON.writeValueAsString(maps), mp.getPayUrl());
        	System.out.println(result);
        	JSONObject json = JSONObject.fromObject(result);
        	//Assert.isTrue(json.containsKey("pay_url"), "获取支付链接异常，请稍后在尝试");
        	
        	String code = json.getString("error_code");
        	String msg = json.getString("error_msg");
        	
        	Assert.isTrue("0".equals(code), msg);
        	
        	String pay_url = json.getString("pay_url");
        	
        	ReturnVo returnVo = new ReturnVo();
        	returnVo.setType("8");
        	returnVo.setUrl(pay_url);
        	returnVo.setParams(maps);
        	return transfer(returnVo);
        }
        return null;
    }

    
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(MMHLPayVo vo, HttpServletRequest req) {
        log.info("美名互联支付 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String status = vo.getPay_status();
        String sign = vo.getSign();
        String orderid = vo.getOut_trade_no();//我们系统订单号
        String amount = vo.getTotal_fee();
        String remark = vo.getBody();
        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = remark.split(",")[0];
            String merId = remark.split(",")[1];
            
            Assert.isTrue(status.equals("1"), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            String paramsStr = MyUtils.obj2UrlParam(vo, true,"body", "order_id", "out_trade_no", "pay_status", "total_fee");
	        
    		String _sign = DigestUtils.signByMD5(paramsStr+mp.getSignKey()).toLowerCase();
            
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
                    return "SUCCESS";
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
    	//{error_code=0, pay_status=1, total_fee=1.00, out_trade_no=72_mmhlzfb_4000212, error_msg=, order_id=20180825213118905922, body=dytest07,72, sign=eda4d00e2a9cfc5492ed44f114c3e1dd}
    	MMHLPayVo vo = new MMHLPayVo();
    	vo.setBody("dytest07,72");
    	vo.setOrder_id("20180825213118905922");
    	vo.setOut_trade_no("72_mmhlzfb_4000212");
    	vo.setPay_status("1");
    	vo.setTotal_fee("1.00");
    	vo.setSign("eda4d00e2a9cfc5492ed44f114c3e1dd");
        String paramsStr = MyUtils.obj2UrlParam(vo, true,"body", "order_id", "out_trade_no", "pay_status", "total_fee");
        String _sign = DigestUtils.signByMD5(paramsStr+"u6(7[[-KDp)%jqZ=L9}xZl]G").toLowerCase();
        System.out.println(_sign.equals(vo.getSign()));
	}
}
