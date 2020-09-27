package com.nnti.pay.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.SFBPayVO;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

import net.sf.json.JSONObject;

/**
 * 收付宝支付（捷易付）
 */
@Controller
@RequestMapping("/sfb")
public class SFBController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

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

        Assert.notEmpty(loginName, platformId, orderAmount, usetype);

        Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

        /*if (orderAmount.contains(".")) {

            String s = orderAmount.substring(orderAmount.indexOf(".") + 1);

            StringBuilder sb = new StringBuilder();

            for (int i = s.length(); i < 2; i++) {

                sb.append(0);
            }

            orderAmount = orderAmount + sb.toString();
        } else {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 2; i++) {

                sb.append(0);
            }

            orderAmount = orderAmount + "." + sb.toString();
        }

        DecimalFormat df = new DecimalFormat(".00");

        orderAmount = df.format(Double.parseDouble(orderAmount));*/

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "300");
        String remark = loginName + "," + mp.getId();

        if (StringUtils.isBlank(customerIp)) {

            customerIp = "127.0.0.1";
        }

        SFBPayVO vo = JSON.readValue(mp.getRemain(), SFBPayVO.class);

        String type = "";
        List list = Arrays.asList(new Object[]{"10","20","30","50","100","200","300","500"});
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            if (mp.getPayWay().equals(2)) {
                type = vo.getWap();// wap 微信
                Double damount = Double.parseDouble(orderAmount);
                Integer iamount = damount.intValue();
            	Assert.isTrue(iamount % 100 ==0, "该支付只支持100-1000内整百支付");
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getWap();// wap 支付宝
            }else if (mp.getPayWay().equals(7)) {
                type = vo.getWap();// wap QQ
            }else if (mp.getPayWay().equals(3)) {
            	Assert.isTrue(StringUtils.isNotBlank(bankcode), "请选择支付银行");
            	type = vo.getWap();
            	vo.setBankAccountType("PRIVATE_DEBIT_ACCOUNT");
            	vo.setBankCode(bankcode);
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
                Assert.isTrue(list.contains(orderAmount), "该支付只支持如下下单金额：</br>10、20、30、50、100、200、300、500");
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getPc();//PC支付宝扫码
            } else if (mp.getPayWay().equals(7)) {
                type = vo.getPc();//PCQQ
            } else if (mp.getPayWay().equals(3)) {
            	Assert.isTrue(StringUtils.isNotBlank(bankcode), "请选择支付银行");
                type = vo.getPc();
            	vo.setBankAccountType("PRIVATE_DEBIT_ACCOUNT");
            	vo.setBankCode(bankcode);
            } else if (mp.getPayWay().equals(4)) {
                type = vo.getPc();//快捷
            } else if (mp.getPayWay().equals(10)) {
                type = vo.getPc();// wap 京东
            } else if (mp.getPayWay().equals(13)) {
                type = vo.getPc();// wap 银联
            }
        }

        vo.setOrderPrice(orderAmount);
        vo.setOutTradeNo(orderId);
        vo.setProductType(type);
        vo.setOrderTime(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
        vo.setProductName("war3");
        vo.setOrderIp(customerIp);
        vo.setReturnUrl(mp.getShopUrl());
        vo.setNotifyUrl(mp.getNotifyUrl());
        vo.setRemark(remark);
        vo.setSubPayKey("");
        vo.setPaySecret(mp.getSignKey());

        Map<String, Object> maps = null;
        if (mp.getPayWay() !=3){
        	maps = MyUtils.describe(vo, "notifyUrl", "orderIp", "orderPrice", "orderTime", "outTradeNo", "payKey", "productName", "productType", "remark", "returnUrl");
        }else{
        	maps = MyUtils.describe(vo, "notifyUrl", "orderIp", "orderPrice", "orderTime", "outTradeNo", "payKey", "productName", "productType", "remark", "returnUrl","bankCode","bankAccountType");
        }

        String signature = DigestUtils.getSortedSign(maps,"&paySecret="+vo.getPaySecret());

        vo.setSign(signature);

        Map pays = null;
        if (mp.getPayWay() != 3){
        	pays = MyUtils.describe(vo, "notifyUrl", "orderIp", "orderPrice", "orderTime", "outTradeNo", "payKey", "productName", "productType", "remark", "returnUrl", "sign");
        }else{
        	pays = MyUtils.describe(vo, "bankAccountType","bankCode","notifyUrl", "orderIp", "orderPrice", "orderTime", "outTradeNo", "payKey", "productName", "productType", "remark", "returnUrl", "sign", "subPayKey");
        }

        log.info("玩家" + loginName + "通过收付宝扫码进行支付，订单号为：" + orderId + "，请求参数为：" + pays);

        String result = MyWebUtils.getHttpContentByParam(mp.getPayUrl(), MyWebUtils.getListNamevaluepair(pays));
        log.info(orderId + ":jyf online_pay返回结果：" + result);
        
        JSONObject json = JSONObject.fromObject(result);
        String resultCode = json.getString("resultCode");
        String errMsg = json.getString("errMsg");
        Assert.isTrue("0000".equals(resultCode), errMsg);
        
        Assert.isTrue(json.containsKey("payMessage"), "支付异常，请稍后在尝试");
        String payMessage = json.getString("payMessage");
        
        ReturnVo returnVo = new ReturnVo();
        Integer payWay = mp.getPayWay();
        if(payWay ==13 || ((payWay == 1 || payWay == 2 ||payWay == 7||payWay == 10)  && usetype == Constant.USE_TYPE_PC )){
            
        	
        	returnVo.setType("2");
        	returnVo.setData(payMessage);
        }
        if((payWay == 1 || payWay == 2 ||payWay == 7||payWay == 10) && usetype == Constant.USE_TYPE_WEB){
            returnVo.setType("1");
            returnVo.setUrl(payMessage);
            returnVo.setParams(pays);
        }
        return transfer(returnVo);
    }

    @ResponseBody
    @RequestMapping("/online_pay_return")
    public String online_pay_return(SFBPayVO vo, HttpServletRequest req) throws Exception {

        log.info("SFB回调接收参数：" + MyWebUtils.getRequestParameters(req));

        String customno = vo.getOutTradeNo();
        String status = vo.getTradeStatus();
        String orderPrice = vo.getOrderPrice();
        String sign = vo.getSign();
        String remark = vo.getRemark();

        Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

        if (!("SUCCESS".equals(status))) {

            throw new BusinessException(ErrorCode.SC_10001.getCode(), "支付订单结果异常！");
        }

        String loginName = remark.split(",")[0];
        String merId = remark.split(",")[1];

        Assert.notEmpty(loginName, merId);


        MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
        
        Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

        vo.setPaySecret(mp.getSignKey());

        Map<String, Object> maps = MyUtils.describe(vo,"orderPrice", "orderTime", "outTradeNo", "payKey", "productName",
        		"productType", "remark", "successTime", "tradeStatus", "trxNo");

        String signature = DigestUtils.getSortedSign(maps, "&paySecret="+mp.getSignKey());

        log.info("签名数据对比，第三方传入的签名数据为：" + sign + "，对参数进行加密后的签名数据为：" + signature);

        Assert.isTrue(signature.equals(sign), ErrorCode.SC_30000_114.getType());

        final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));

        Boolean lockFlag = false;

        try {

            lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
        } catch (Exception e) {

            log.error("回调时玩家" + loginName + "获取锁发生异常，异常信息：", e);

            lockFlag = true;
        }

        try {

            if (lockFlag) {

                tradeService.doPayFlow(customno, orderPrice, loginName, mp, "", null);

                return SUCCESS;
            } else {

                log.error("回调时玩家" + loginName + "未能获取锁，无法执行请求对应的方法....");
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {

                lock.release();
            } catch (Exception e) {

                log.error("回调时玩家" + loginName + "释放锁发生异常, 异常信息：" + e);
            }
        }

        return ERROR;
    }
    
    public static void main(String[] args) throws Exception {
    	//bankAccountType=PRIVATE_DEBIT_ACCOUNT&bankCode=ABC&notifyUrl=http://www.baidu.com/async.jsp&orderIp=127.0.0.1&orderPrice=10.00
    	//&orderTime=20180325061646&outTradeNo=20180325061646000002&payKey=dd589c1d05b94fb689bdd779dd589c1d
    	//&productName=Q币&productType=50000103&remark=购买Q币&returnUrl=http://www.baidu.com/sync.jsp&paySecret=2eb8c630b2d0462a8db7971b2eb8c630

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bankAccountType", "PRIVATE_DEBIT_ACCOUNT");
		map.put("bankCode", "ABC");
		map.put("notifyUrl", "http://www.baidu.com/async.jsp");
		map.put("orderIp", "127.0.0.1");
		map.put("orderPrice", "10.00");
		map.put("orderTime", "20180325061646");
		map.put("outTradeNo", "20180325061646000002");
		map.put("payKey", "dd589c1d05b94fb689bdd779dd589c1d");
		map.put("productName", "Q币");
		map.put("productType", "50000103");
		map.put("remark", "购买Q币");
		map.put("returnUrl", "http://www.baidu.com/sync.jsp");
		//map.put("paySecret", "2eb8c630b2d0462a8db7971b2eb8c630");
		String signature = DigestUtils.getSortedSign(map, "&paySecret=2eb8c630b2d0462a8db7971b2eb8c630");
		System.out.println(signature);
		
	}
}
