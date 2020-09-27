package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.User;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.LfPayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.security.lf.*;
import com.nnti.pay.security.lf.json.JSONObject;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wander on 2017/4/26. 乐付 支付
 */
@Controller
@RequestMapping("/lf")
public class LFController extends BasePayController {
	@Autowired
	private IBasicService basicService;
	@Autowired
	private IMerchantPayService merchantPayService;
	@Autowired
	private ITradeService tradeService;

	/*** 微信支付 */
	@ResponseBody
	@RequestMapping("/zfb_wx")
	public Response zfb_wx(HttpServletRequest req,
			@RequestParam(value = "requestData", required = false) String requestData) throws Exception {

		PayRequestVo payRequestVo = pasePayRequest(req, requestData);

		String loginName = payRequestVo.getLoginName();
		Long platformId = payRequestVo.getPlatformId();
		Integer usetype = payRequestVo.getUsetype();
		String customerIp = payRequestVo.getCustomerIp();
		String orderAmount = payRequestVo.getOrderAmount();

		Assert.notEmpty(loginName, platformId, usetype, orderAmount);
		loginName = loginName.trim();

		Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

		User user = basicService.getUser(loginName);
		validationLoginName(loginName, user);

		validationAgent(user.getRole());
		// 根据商户号查询商户信息
		MerchantPay mp = merchantPayService.findById(platformId);
		validationMerchantPay(orderAmount, mp);

		String remark = loginName;

		validationAmountCutAll(loginName, mp);
		if (usetype == Constant.USE_TYPE_WEB) {
			remark = "wap_" + loginName;
		}
		remark = remark + "_" + mp.getId();

		String orderId = basicService.createBillNo(loginName, orderAmount, mp, remark, "_");

		LfPayVo vo = JSON.readValue(mp.getRemain(), LfPayVo.class);
		vo.setZfbWx(orderAmount, orderId, mp.getMerchantCode(), remark, mp.getSystemCode(), mp.getSystemCode(),
				mp.getNotifyUrl());

		String signSrc="";
		if (vo.getAli_pay_type() !=null && vo.getAli_pay_type().equals("ali_sm")) {
			 signSrc = MyUtils.obj2UrlParam(vo, true, "amount_str", "out_trade_no", "partner", "remark",
					"service", "sub_body", "subject", "ali_pay_type", "return_url");
		}
		else if(vo.getQq_pay_type() != null && vo.getQq_pay_type().equals("qq_sm")){
			signSrc = MyUtils.obj2UrlParam(vo, true, "amount_str", "out_trade_no", "partner", "remark",
					"service", "sub_body", "subject", "qq_pay_type", "return_url");
		}
		else{
			 signSrc = MyUtils.obj2UrlParam(vo, true, "amount_str", "out_trade_no", "partner", "remark",
					"service", "sub_body", "subject", "wx_pay_type", "return_url");
		}

		log.info("验签串码:"+signSrc);
		
		String sign = URLEncoder.encode(RSACoderUtil.sign(signSrc.getBytes("UTF-8"), mp.getSignKey()), "UTF-8");

		vo.setSign(sign);
		vo.setContent(RSACoderUtil.getParamsWithDecodeByPublicKey(signSrc, "UTF-8", vo.getPublicKey()));
		Map pays = MyUtils.describe(vo, "partner", "content", "input_charset", "sign_type", "sign");
		log.info(orderId + ":请求参数：partner:" + vo.getPartner() + ",remark:" + vo.getRemark() + ",amount_str:"
				+ vo.getAmount_str() + ",return_url:" + vo.getReturn_url());
		String result = WebUtil.doPost(mp.getPayUrl(), pays, "UTF-8", 3000, 15000);
		
		
		log.info("乐付微信支付宝qq接口返回result:"+result);

		String data = "", webchatUrl;

		FCSOpenApiResponse openApiResponse = (FCSOpenApiResponse) JsonUtil.parseObject(result,
				FCSOpenApiResponse.class);
		if (openApiResponse.getIs_succ().equals("T")) {
			if (StringUtils.isNotEmpty(openApiResponse.getResponse())) {
				String responseCharset = openApiResponse.getCharset();
				byte[] byte64 = CoderUtil.decryptBASE64(openApiResponse.getResponse());
				String responseResult = new String(RSACoderUtil.decryptByPrivateKey(byte64, mp.getSignKey()),
						responseCharset);
				boolean verify = RSACoderUtil.verify(responseResult.getBytes(responseCharset), vo.getPublicKey(),
						openApiResponse.getSign());
				if (verify) {
					log.info(" responseResult: " + responseResult);
					if (StringUtils.isNotEmpty(responseResult)) {
						JSONObject jsonObject = new JSONObject(responseResult);
						String base64QRCode = jsonObject.getString("base64QRCode");
						if(responseResult.contains("qq_pay_sm_url")){
							data = jsonObject.getString("qq_pay_sm_url");
						}
						else {
							data = jsonObject.getString("wx_pay_sm_url");
						}
						if (StringUtils.isNotEmpty(base64QRCode)) {
							webchatUrl = base64QRCode;
						}
					}
					openApiResponse.setResult(responseResult);
				} else {
					throw new BusinessException(ErrorCode.SC_10001.getCode(),
							"ERROR ## doSign by fcsPublicKey has an error");
				}
			}
		} else {
			throw new BusinessException(ErrorCode.SC_10001.getCode(), openApiResponse.getFault_reason());
		}

		ReturnVo returnVo = new ReturnVo();
		returnVo.setType("2");
		returnVo.setData(data);

		log.info(orderId + ":返回二维码：" + data);
		return transfer(returnVo);
	}

	/*** 支付宝 微信 支付回掉 */
	@ResponseBody
	@RequestMapping(value = "/zfb_wx_return")
	public String zfb_wx_return(LfPayVo vo, HttpServletRequest req) {

		log.info("lf 接收参数：" + MyWebUtils.getRequestParameters(req));

		String content = vo.getContent();
		String sign = vo.getSign();
		String sign_type = vo.getSign_type();
		String input_charset = vo.getInput_charset();
		String request_time = vo.getRequest_time();
		String out_trade_no = vo.getOut_trade_no();
		String status = vo.getStatus();

		try {

			Assert.isTrue(validationTrustIp(req, DictionaryType.LF_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

			String loginName = "", merId;
			String split[] = out_trade_no.split("_");
			int i = 2;
			for (int t = split.length - 2; i < t; i++) {
				loginName = loginName + split[i] + "_";
			}
			loginName = loginName.substring(0, loginName.length() - 1);
			merId = split[i];

			MerchantPay mp = merchantPayService.findById(Long.parseLong(merId));
			Assert.isTrue(mp.getUseable() == 1, "通道已禁用");
			LfPayVo remainVo = gson.fromJson(mp.getRemain(), LfPayVo.class);

			String paramsStr = RSACoderUtil.rsaDecrypt(content, mp.getSignKey()); // 获取加密的明文
																					// 解出来
			log.info("paramsStr:" + paramsStr);
			net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(paramsStr);
			String amount_str = jsonObject.getString("amount_str");
			String remark = jsonObject.getString("remark");

			log.info("pulicKey:" + remainVo.getPublicKey());

			boolean flag = RSACoderUtil.verifySign2(paramsStr, sign, remainVo.getPublicKey());

			log.info("验签::" + flag);

			Assert.isTrue(flag && "1".equals(status), "验签失败或者交易失败");

			Boolean lockFlag = false;

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
					tradeService.doPayFlow(out_trade_no, amount_str, loginName, mp, "",null);
					return SUCCESS;
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
			log.error("交易异常：", e);
		}
		return null;
	}

	public static String getURL(Map<String, String> paramsMap) {
		String url = null;
		if ((paramsMap != null) && (paramsMap.size() > 0)) {
			Iterator it = paramsMap.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = paramsMap.get(key);
				if (url == null) {
					url = key + "=" + value;
				} else {
					url = url + "&" + key + "=" + value;
				}
			}
		}
		return url;
	}
}
