package com.nnti.pay.controller;

import java.util.Arrays;
import java.util.List;
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
import com.nnti.pay.controller.vo.MFPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * @author pony
 * @Description 秒付支付
 */
@Controller
@RequestMapping("/mf")
public class MFPayController extends BasePayController {

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

        User user = basicService.getUser(loginName);
        validationLoginName(loginName, user);

        validationAgent(user.getRole());
        //根据商户号查询商户信息
        MerchantPay mp = merchantPayService.findById(platformId);
        
        validationMerchantPay(orderAmount, mp);

        validationAmountCutAll(loginName, mp);
        

        String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");
        
        MFPayVo vo = JSON.readValue(mp.getRemain(), MFPayVo.class); 
        List list = Arrays.asList(new Object[]{"100","200","300","500","1000","2000","3000","5000"});
        Assert.isTrue(list.contains(orderAmount), "该支付目前只支持如下下单金额：</br>300,500,1000,2000,3000,5000");
        String type = "";
        if (usetype == Constant.USE_TYPE_WEB) {
            loginName = "wap_" + loginName;
            if (mp.getPayWay().equals(2)) {
                type = vo.getWap();// wap 微信
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getWap();// wap 支付宝
            } else if (mp.getPayWay().equals(7)) {
                type = vo.getWap();// wap QQ
            }
        } else {
            if (mp.getPayWay().equals(2)) {
                type = vo.getPc();//PC微信扫码
            } else if (mp.getPayWay().equals(1)) {
                type = vo.getPc();//PC支付宝扫码
            } else if (mp.getPayWay().equals(7)) {
                type = vo.getPc();//PCQQ扫码
            }
        }
        // 组织请求数据
        vo.setOnline_pay(mp.getMerchantCode(), orderId, orderAmount,type,mp.getNotifyUrl(), mp.getShopUrl(),mp.getSystemCode(),loginName + "," + mp.getId());
        String params = vo.getMer_order_no()+vo.getMerchant_no()+vo.getNotify_url()+vo.getOrder_amount()+vo.getPay_type()+vo.getProduct_name()+vo.getRemark()+vo.getReturn_url();
    	String sign = DigestUtils.md5(params+mp.getSignKey());
    	vo.setSign(sign);
    	
    	Map<String, String> maps = MyUtils.describe(vo,"merchant_no", "mer_order_no", "order_amount", "pay_type", "notify_url",
        		"return_url", "product_name", "remark","sign");
    	log.info("订单号" + orderId + "发送请求数据为：" + maps);
    	
		ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(maps);
        return transfer(returnVo);
    }

	@ResponseBody
    @RequestMapping(value = "/online_pay_return")
    public String online_pay_return(MFPayVo vo, HttpServletRequest req) {
        log.info("秒付 接收参数：" + MyWebUtils.getRequestParameters(req));
        
        String result = vo.getOrder_status();
        String remark = vo.getRemark();
        String sign = vo.getSign();
        String orderid = vo.getMer_order_no();
        String amount = vo.getOrder_amount();
        try {
            Assert.isTrue(validationTrustIp(req, DictionaryType.GTPay_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
            
            String loginName = remark.split(",")[0];
            String merId = remark.split(",")[1];
            

            Assert.isTrue(result.equals("2"), ErrorCode.SC_30000_111.getType());
            MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
            Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

            String params = vo.getMer_order_no()+vo.getMerchant_no()+vo.getOrder_amount()+vo.getOrder_status()+vo.getPay_type()+vo.getRemark();
            
        	String _sign = DigestUtils.md5(params+mp.getSignKey());
        	
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
