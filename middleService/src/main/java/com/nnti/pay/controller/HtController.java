package com.nnti.pay.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.Base64Util;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.HcPayVo;
import com.nnti.pay.controller.vo.HtPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.enums.HcResponseCode;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.security.jh.CertificateCoder;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * Created by Addis on 2017/2/13. 汇通支付
 */
@Controller
@RequestMapping("/ht")
public class HtController extends BasePayController {

	@Autowired
	private IBasicService basicService;
	@Autowired
	private IMerchantPayService merchantPayService;
	@Autowired
	private ITradeService tradeService;

	@ResponseBody
	@RequestMapping("/online_pay")
	public Response online_pay(HttpServletRequest req,
			@RequestParam(value = "requestData", required = false) String requestData) throws Exception {

		PayRequestVo payRequestVo = pasePayRequest(req, requestData);

		String loginName = payRequestVo.getLoginName();
		Long platformId = payRequestVo.getPlatformId();
		String orderAmount = payRequestVo.getOrderAmount();
		String bankCode = payRequestVo.getBankCode()==null?"wxzfb":payRequestVo.getBankCode();

		Assert.notEmpty(loginName, platformId, orderAmount);
		loginName = loginName.trim();

		Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

		User user = basicService.getUser(loginName);
		validationLoginName(loginName, user);

		validationAgent(user.getRole());
		// 根据商户号查询商户信息
		MerchantPay mp = merchantPayService.findById(platformId);
		validationMerchantPay(orderAmount, mp);

		validationAmountCutAll(loginName, mp);

		String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");

		// 组织请求数据
		String tradeDate = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date());
		HtPayVo vo = gson.fromJson(mp.getRemain(), HtPayVo.class);
		vo.setOnline(mp.getNotifyUrl(), mp.getShopUrl(), bankCode, mp.getMerchantCode(), orderId, orderAmount, tradeDate, "", "", loginName);
		vo.setCustomer_ip("127.0.0.1");
		vo.setReq_referer("123");
		
		Map<String, String> map = new HashMap<>();
		map.put("notify_url", mp.getNotifyUrl());
		map.put("return_url", mp.getShopUrl());
		map.put("pay_type", vo.getPay_type());
		map.put("bank_code",bankCode);
		map.put("merchant_code", mp.getMerchantCode());
		map.put("order_no", orderId);
		map.put("order_amount", orderAmount);
		map.put("order_time", tradeDate);
		map.put("customer_ip", vo.getCustomer_ip());
		map.put("req_referer", vo.getReq_referer());
		map.put("return_params", loginName);
		String sign = MyUtils.getSign(map, mp.getSignKey()	);
		map.put("sign", sign);

		ReturnVo returnVo = new ReturnVo();
		returnVo.setType("1");
		returnVo.setUrl(mp.getPayUrl());
		returnVo.setParams(map);

		log.info(orderId + ":ht online_pay请求参数：" + map);
		return transfer(returnVo);
	}
	


	@ResponseBody
	@RequestMapping("/online_pay_return")
	public String online_pay_return(HtPayVo vo, HttpServletRequest req) {

		log.info("汇通支付回调接收参数：" + MyWebUtils.getRequestParameters(req));

		String merchant_code = vo.getMerchant_code();
		String sign = vo.getSign();
		String order_no = vo.getOrder_no();
		String order_amount = vo.getOrder_amount();
		String order_time = java.net.URLDecoder.decode(vo.getOrder_time());
		String return_params = vo.getReturn_params();
		String trade_no = vo.getTrade_no();
		String trade_time =java.net.URLDecoder.decode(vo.getTrade_time());
		String trade_status = vo.getTrade_status();
		String notify_type = vo.getNotify_type();
		
		try {
			Assert.isTrue(validationTrustIp(req, DictionaryType.HT_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

			MerchantPay mp = merchantPayService.findByMerNo(merchant_code);
			Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
			Assert.isTrue(trade_status.equals(success), "充值失败");
			Assert.notEmpty(mp);
			
			Map<String, String> map = new HashMap<>();
			map.put("merchant_code", merchant_code);
			map.put("order_no", order_no);
			map.put("order_amount", order_amount);
			map.put("order_time",order_time);
			map.put("return_params", return_params);
			map.put("trade_no", trade_no);
			map.put("trade_time", trade_time);
			map.put("trade_status", trade_status);
			map.put("notify_type", notify_type);
			String signStr = MyUtils.getSign(map, mp.getSignKey()	);
			
			Assert.isTrue(signStr.equals(sign), ErrorCode.SC_30000_114.getType());
			
			//查询订单状态
            String result = PayOrderController.queryPayOrder(vo, mp, null, req);
            
            Assert.isTrue(result.equals("SUCCESS"), "查询订单状态交易失败");
			

			Boolean lockFlag = false;
			String loginName = return_params;

			final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient,
					GenerateNodePath.generateUserLockForUpdate(loginName));

			try {
				lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
			} catch (Exception e) {
				log.error("玩家：" + loginName + "获取锁发生异常，异常信息：", e);
				lockFlag = true;
			}

			try {
				if (lockFlag) {
					tradeService.doPayFlow(order_no, order_amount, loginName, mp, "",null);
					return success;
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
		return ERROR;
	}
}
