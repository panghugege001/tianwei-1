package com.nnti.pay.controller;

import java.net.URLEncoder;
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
 * Created by wander on 2017/2/13. 汇潮支付 麻袋
 */
@Controller
@RequestMapping("/hc")
public class HcController extends BasePayController {

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
		String bankCode = payRequestVo.getBankCode();

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
		String tradeDate = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
		HcPayVo vo = gson.fromJson(mp.getRemain(), HcPayVo.class);
		vo.setOnline(mp.getMerchantCode(), orderId, orderAmount, tradeDate, mp.getNotifyUrl(), mp.getShopUrl(),
				loginName, "dy");
		if (MyUtils.isNotEmpty(bankCode)) {
			vo.setDefaultBankNumber(bankCode);
		}

		String paramsStr = MyUtils.obj2UrlParam(vo, true, "MerNo", "BillNo", "Amount", "OrderTime", "ReturnURL",
				"AdviceURL");
		vo.setSignInfo(DigestUtils.signByMD5(paramsStr + "&" + mp.getSignKey()).toUpperCase());

		Map pays = MyUtils.describe(vo, "MerNo", "BillNo", "Amount", "ReturnURL", "AdviceURL", "SignInfo", "Remark",
				"defaultBankNumber", "payType", "OrderTime", "products");
		pays.put("apiUrl", mp.getApiUrl());

		ReturnVo returnVo = new ReturnVo();
		returnVo.setType("1");
		returnVo.setUrl(mp.getPayUrl());
		returnVo.setParams(pays);

		log.info(orderId + ":hc online_pay请求参数：" + pays);
		return transfer(returnVo);
	}

	@ResponseBody
	@RequestMapping("/online_pay_return")
	public String online_pay_return(HcPayVo vo, HttpServletRequest req) {

		log.info("汇潮接收参数：" + MyWebUtils.getRequestParameters(req));

		String MerNo = vo.getMerNo();
		String BillNo = vo.getBillNo();
		String OrderNo = vo.getOrderNo();
		String Amount = vo.getAmount();
		String tradeOrder = vo.getTradeOrder();
		String Succeed = vo.getSucceed();
		String Result = vo.getResult();
		String SignInfo = vo.getSignInfo();
		String remark = vo.getRemark();// 测试是否能拿到？

		try {
			Assert.isTrue(validationTrustIp(req, DictionaryType.HC_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

			MerchantPay mp = merchantPayService.findByMerNo(MerNo);
			Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
			Assert.notEmpty(mp);
			String join = MyUtils.obj2UrlParam(vo, true, "MerNo", "BillNo", "OrderNo", "Amount", "Succeed");
			String md5sign = DigestUtils.signByMD5(join + "&" + mp.getSignKey()).toUpperCase();
			Assert.isTrue(SignInfo.equals(md5sign), ErrorCode.SC_30000_114.getType());
			Assert.isNumeric(Succeed);

			log.info("Succeed:" + Succeed);
			Assert.isTrue(HcResponseCode.SUCCESS.value().equals(Succeed), ErrorCode.SC_30000_111.getType());
			
		    //查询订单状态
            String result = PayOrderController.queryPayOrder(vo, mp, null, req);
            
            Assert.isTrue(result.equals("1"), "查询订单状态交易失败");
            

			Boolean lockFlag = false;
			String loginName = BillNo.split("_")[2];

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
					tradeService.doPayFlow(BillNo, Amount, loginName, mp, "",null);
					return ok;
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
		return ERROR + Succeed;
	}

	/*****
	 * 汇潮 支付宝 微信 QQ
	 */
	@ResponseBody
	@RequestMapping("/zfb_wx")
	public Response zfb_wx(HttpServletRequest req,HttpServletResponse res,
			@RequestParam(value = "requestData", required = false) String requestData) throws Exception {

		PayRequestVo payRequestVo = pasePayRequest(req, requestData);
		
		

		String loginName = payRequestVo.getLoginName();
		Long platformId = payRequestVo.getPlatformId();
		String orderAmount = payRequestVo.getOrderAmount();
		String bankCode = payRequestVo.getBankCode();

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
		String tradeDate = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
		HcPayVo vo = gson.fromJson(mp.getRemain(), HcPayVo.class);
		vo.setWx_zfb(mp.getMerchantCode(), orderId, orderAmount, tradeDate, mp.getNotifyUrl(), mp.getShopUrl(),
				loginName, "dy");
		if (MyUtils.isNotEmpty(bankCode)) {
			vo.setDefaultBankNumber(bankCode);
		}

		String paramsStr = MyUtils.obj2UrlParam(vo, true, "MerNo", "BillNo", "Amount", "OrderTime", "AdviceUrl");
		vo.setSignInfo(DigestUtils.signByMD5(paramsStr + "&" + mp.getSignKey()).toUpperCase());

		Map pays = MyUtils.describe(vo, "MerNo", "BillNo", "Amount", "AdviceUrl", "SignInfo", "Remark", "payType",
				"OrderTime", "products");
		
		String url = mp.getPayUrl();

		Map baseXmlMap = new TreeMap();

		baseXmlMap.put("buildXML", buildXML(pays));

		log.info(orderId + ":hc zfb_wx请求参数：" + url);

		MyWebUtils.submitForm(res, baseXmlMap, url);
		
		//log.info(orderId + ":返回数据：" + result);

		ReturnVo returnVo = new ReturnVo();
		returnVo.setType("5");
		returnVo.setUrl(mp.getPayUrl());
		returnVo.setParams(baseXmlMap);

		log.info(orderId + ":hc zfb_wx请求参数：" + pays);
		return transfer(returnVo);
	}

	/**
	 * 
	 * 汇潮 支付宝 微信 QQ 回调
	 * 
	 * @param vo
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/zfb_wx_return")
	public String zfb_wx_return(HcPayVo vo, HttpServletRequest req) {

		log.info("汇潮QQ接收参数：" + MyWebUtils.getRequestParameters(req));

		String MerNo = vo.getMerNo();
		String BillNo = vo.getBillNo();
		String OrderNo = vo.getOrderNo();
		String Amount = vo.getAmount();
		String tradeOrder = vo.getTradeOrder();
		String Succeed = vo.getSucceed();
		String Result = vo.getResult();
		String SignInfo = vo.getSignInfo();
		String remark = vo.getRemark();// 测试是否能拿到？

		try {
			Assert.isTrue(validationTrustIp(req, DictionaryType.HC_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

			MerchantPay mp = merchantPayService.findByMerNo(MerNo);
			Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
			Assert.notEmpty(mp);
			String join = MyUtils.obj2UrlParam(vo, true, "MerNo", "BillNo", "OrderNo", "Amount", "Succeed");
			String md5sign = DigestUtils.signByMD5(join + "&" + mp.getSignKey()).toUpperCase();
			Assert.isTrue(SignInfo.equals(md5sign), ErrorCode.SC_30000_114.getType());
			Assert.isNumeric(Succeed);

			log.info("Succeed:" + Succeed);
			Assert.isTrue(HcResponseCode.SUCCESS.value().equals(Succeed), ErrorCode.SC_30000_111.getType());
			
		    //查询订单状态
            String result = PayOrderController.queryPayOrder(vo, mp, null, req);
            
            Assert.isTrue(result.equals("1"), "查询订单状态交易失败");

			Boolean lockFlag = false;
			String loginName = BillNo.split("_")[2];

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
					tradeService.doPayFlow(BillNo, Amount, loginName, mp, "",null);
					return ok;
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
		return ERROR + Succeed;
	}

	// 生成xml数据
	public static String buildXML(Map<String, String> map) {

		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<ScanPayRequest>\n");
		builder.append("<MerNo>").append(map.get("MerNo")).append("</MerNo>\n");
		builder.append("<BillNo>").append(map.get("BillNo")).append("</BillNo>\n");
		builder.append("<payType>").append(map.get("payType")).append("</payType>\n");
		builder.append("<Amount>").append(map.get("Amount")).append("</Amount>\n");
		builder.append("<OrderTime>").append(map.get("OrderTime")).append("</OrderTime>\n");
		builder.append("<AdviceUrl>").append(map.get("AdviceUrl")).append("</AdviceUrl>\n");
		builder.append("<SignInfo>").append(map.get("SignInfo")).append("</SignInfo>\n");
		builder.append("<products>").append(map.get("products")).append("</products>\n");
		builder.append("<remark>").append(map.get("Remark")).append("</remark>\n");
		builder.append("</ScanPayRequest>");
		System.out.println("xml參數："+builder.toString());
		return Base64Util.encode(builder.toString());
	}
	
	// 生成xml数据
	public static String buildXML2(Map<String, String> map) {

		StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<root tx='"+map.get("tx")+"'>\n");
		builder.append("<merCode>").append(map.get("merCode")).append("</merCode>\n");
		builder.append("<orderNumber>").append(map.get("orderNumber")).append("</orderNumber>\n");
		builder.append("<beginTime>").append(map.get("beginTime")).append("</beginTime>\n");
		builder.append("<endTime>").append(map.get("endTime")).append("</endTime>\n");
		builder.append("<pageIndex>").append(map.get("pageIndex")).append("</pageIndex>\n");
		builder.append("<sign>").append(map.get("sign")).append("</sign>\n");
		builder.append("</root>");
		System.out.println("xml參數："+builder.toString());
		return Base64Util.encode(builder.toString());
	}

}
