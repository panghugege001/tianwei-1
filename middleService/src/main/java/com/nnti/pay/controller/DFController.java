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
import com.nnti.pay.controller.vo.DFPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.model.vo.PayOrder;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.IPayOrderService;
import com.nnti.pay.service.interfaces.ITradeService;

import net.sf.json.JSONObject;

/**
 * @author pony
 * @Description 东方支付
 */
@Controller
@RequestMapping("/df")
public class DFController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;
    @Autowired
    private IPayOrderService payOrderService;

    /**
     * 所有支付
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
    @RequestMapping(value = "/online_pay", method = RequestMethod.POST)
    public Response online_pay(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        String loginName = payRequestVo.getLoginName();
        Long platformId = payRequestVo.getPlatformId();
        Integer usetype = payRequestVo.getUsetype();
        String orderAmount = payRequestVo.getOrderAmount();
        

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);
        loginName = loginName.trim();

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");
        
        if(orderAmount.contains(".")){
        	orderAmount = orderAmount.substring(0, orderAmount.lastIndexOf("."))+"."+((int)Math.random()*90+10);//2位小数
        }else{
        	orderAmount = orderAmount+"."+((int)Math.random()*90+10);//2位小数
        }

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "300");
        
        DFPayVo vo = new DFPayVo();
        
        // 组织请求数据
        vo.setOnline_pay(mp.getMerchantCode(),orderAmount,"apple", orderId, mp.getNotifyUrl());
        
        String sign = DigestUtils.signByMD5(mp.getMerchantCode()+"pay"+orderAmount+orderId+mp.getSignKey()).toLowerCase();
        log.info("签名：" + sign);
        vo.setSign(sign);
        

        Map pays = MyUtils.describe(vo, "merchantId","totalAmount", "desc", "corp_flow_no", "notify_url", "sign");
        if (mp.getPayWay().equals(1)) {
            loginName = "wap_" + loginName;
            pays.put("return_url", mp.getShopUrl());
            pays.put("type", "2");
            
            ReturnVo returnVo = new ReturnVo();
            log.info(orderId + ":请求参数：" + pays);

            returnVo.setType("8");
            returnVo.setUrl("http://www.7ypay.com/api/Payh5/pay_h5/?"); //+MyUtils.obj2UrlParam(false, pays)
            returnVo.setParams(pays);
            return transfer(returnVo);
            
        } else {
        	log.info(orderId + ":请求参数：" + pays);
        	
        	//String result = MyWebUtils.sendFormMsgFastPay(JSON.writeValueAsString(pays), mp.getPayUrl());
        	String result = MyWebUtils.getHttpContentByBtParam(mp.getPayUrl(),JSON.writeValueAsString(pays));
        	log.info(orderId + ":返回结果：" + result);
        	
        	JSONObject json = JSONObject.fromObject(result);
        	Boolean res = json.getBoolean("Result");
        	String code = json.getString("Code");
        	String rsign = json.getString("sign");
        	String rmerchantId = json.getString("merchantId");
        	String rcorp_flow_no = json.getString("corp_flow_no");
        	
        	String _sign = DigestUtils.signByMD5(rmerchantId+"pay"+rcorp_flow_no+mp.getSignKey()).toLowerCase();
        	
        	Assert.isTrue(res, "交易失败，请稍后再试");
        	Assert.isTrue("00".equals(code), "交易失败，请稍后再试");
        	
        	//Assert.isTrue(_sign.equals(rsign), "交易失败，请稍后再试");
        	//Assert.isTrue("a4c4e19d0d569858b00168c8dc33c43f".equals(rsign), "交易失败，请稍后再试");
        	
        	String qrCodeURL = json.getString("qrCodeURL");
        	ReturnVo returnVo = new ReturnVo();
        	returnVo.setType("2");
        	returnVo.setData(qrCodeURL);
        	return transfer(returnVo);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(DFPayVo vo, HttpServletRequest req) {
        log.info("DFPay 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String result = vo.getResult();
        String code = vo.getCode();
        String sign = vo.getSign();
        String orderid = vo.getCorp_flow_no();
        String amount = vo.getTotalAmount();
        try {
        	Assert.isTrue("00".equals(code), ErrorCode.SC_30000_111.getType());
        	
            Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            PayOrder payOrder = payOrderService.get(orderid);
            String loginName = payOrder.getLoginName();
            String payplatform = payOrder.getPayPlatform();
            String merId = "";
            if("dfzfb".equals(payplatform)){
            	merId = "58";
            }
            if("dfwx".equals(payplatform)){
            	merId = "64";
            }
            

            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            
            String paramsStr = mp.getMerchantCode()+"pay"+orderid+mp.getSignKey();
            
    		String _sign = DigestUtils.signByMD5(paramsStr);
            
            Assert.isTrue(_sign.equalsIgnoreCase(sign), ErrorCode.SC_30000_114.getType());
            
            

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
    
    public static void main(String[] args) throws Exception {
    	//{code=00, result=true, merchantId=8120, desc=apple, sign=89c020bbe019d1badf6d944dd3d17826, totalAmount=50.10, corp_flow_no=dd34567_4000002}
    	 //String paramsStr = mp.getMerchantCode()+"pay"+orderid+mp.getSignKey();
		String str ="8120paydd34567_4000002"+"93a60b3038ac69119c5116fbd4a05986";
		String md5 = DigestUtils.signByMD5(str);
		System.out.println(md5.equals("277a42d28899846c8e15adcdfc59800c"));
	}
}
