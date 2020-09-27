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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.TTPayVO;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

import net.sf.json.JSONObject;

/**
 * 天天支付
 */
@Controller
@RequestMapping("/tt")
public class TTController extends BasePayController {

    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private IBasicService basicService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;

    protected Gson gson = new Gson();

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

        //validationAmountCutAll(loginName, mp);

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");

        Map<String, Object> valuesMap = mapper.readValue(mp.getRemain(), Map.class);

        Double d = Double.parseDouble(orderAmount) * 100;
        orderAmount = String.valueOf(d.intValue());

        TTPayVO vo = JSON.readValue(mp.getRemain(), TTPayVO.class);

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
//        public void setOnline_pay(String merchantNo,String orderTime,String customerOrderNo,String amount,String subject,String body,
//        		String payerIp,String payerAccountNo,String payType,String notifyUrl){
        String orderTime = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
        // 组织请求数据
        vo.setOnline_pay(mp.getMerchantCode(),orderTime,orderId,orderAmount,"apple","iphone8",customerIp,"", type, mp.getNotifyUrl(), mp.getShopUrl(),"MD5");
        //"payerAccountNo",
        Map<String, Object> maps = MyUtils.describe(vo, "merchantNo", "orderTime", "customerOrderNo", "amount", "subject", "body", "payerIp",
        		 "notifyUrl", "pageUrl", "channel", "payType", "signType");
        
        String sign = DigestUtils.getSortedSign(maps,mp.getSignKey());

        vo.setSign(sign);

        maps.put("sign", sign);
        log.info("订单号" + orderId + "发送请求数据为：" + maps);

        String result = MyWebUtils.sendFormMsgFastPay(JSON.writeValueAsString(maps), mp.getPayUrl());
        System.out.println(result);
        JSONObject json = JSONObject.fromObject(result);
        
        Assert.isTrue(json.containsKey("qrCode"), "获取支付链接异常，请稍后在尝试");
        
        String qrCode = json.getString("qrCode");
        String code = json.getString("code");
        String msg = json.getString("msg");
        
        Assert.isTrue("SUCCESS".equals(code), msg);
        
        ReturnVo returnVo = new ReturnVo();
        returnVo.setType("2");
        returnVo.setData(qrCode);
        return transfer(returnVo);
    }

    
    @ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(TTPayVO vo, HttpServletRequest req) {
        log.info("天天支付 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String status = vo.getStatus();
        String code = vo.getCode();
        String sign = vo.getSign();
        String orderid = vo.getCustomerOrderNo();
        String amount = vo.getAmount();
        try {
        	log.info("天天支付 接收参数vo：" + JSON.writeValueAsString(vo));
        	amount = NumericUtil.div(Double.valueOf(amount), 100)+"";
        	
            Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = orderid.split("_")[2];
            String merId = "";
            String payplatform = orderid.split("_")[1];
            Map<String, Object> maps = null;
            if("ttyl".equals(payplatform)){
            	merId = "53";
            	Assert.isTrue("SUCCESS".equals(code), ErrorCode.SC_30000_111.getType());
            	maps = MyUtils.describe(vo, "merchantNo", "customerOrderNo", "orderNo", "amount", "code", "signType");
            	
            }
            
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
            String _sign = DigestUtils.getSortedSign(maps,mp.getSignKey());
            
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
