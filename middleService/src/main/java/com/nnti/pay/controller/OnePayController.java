/**
 * 
 */
package com.nnti.pay.controller;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.api.rb.security.AesEncryption;
import com.nnti.pay.controller.vo.LfPayVo;
import com.nnti.pay.controller.vo.OnePayVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.XlbPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.security.lf.CoderUtil;
import com.nnti.pay.security.lf.FCSOpenApiResponse;
import com.nnti.pay.security.lf.JsonUtil;
import com.nnti.pay.security.lf.RSACoderUtil;
import com.nnti.pay.security.lf.WebUtil;
import com.nnti.pay.security.lf.json.JSONObject;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

import sun.util.logging.resources.logging;

/**
 * @author Addis Onepay 支付
 *
 */

@Controller
@RequestMapping("/onepay")
public class OnePayController extends BasePayController {

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
		
		 // 客户端IP
        if (!MyUtils.isNotEmpty(customerIp)) {
            customerIp = "127.0.0.1";
        }

		String orderId = basicService.createBillNo(loginName, orderAmount, mp, remark, "_");

		if (usetype == Constant.USE_TYPE_WEB) {
			loginName = "wap_" + loginName;
		}

		OnePayVo vo = JSON.readValue(mp.getRemain(), OnePayVo.class);
		String orderDate = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
		vo.setWxZfb(mp.getMerchantCode(), orderAmount, orderId, orderDate, customerIp, mp.getSignKey(), loginName);

		String paramsStr = orderId + "|" + orderAmount + "|" + mp.getMerchantCode() + "|" + customerIp + "|" + orderDate
				+ "|" +vo.getKey();
		vo.setSign(DigestUtils.signByMD5(paramsStr));

		Map pays = MyUtils.describe(vo, "order_no", "amount", "ag_account", "pay_ip", "pay_time", "sign","attach", "key","sign_type","order_type");

		log.info(orderId + ":请求参数：" + pays);

		ReturnVo returnVo = new ReturnVo();
		returnVo.setType("1");
		returnVo.setUrl(mp.getPayUrl());
		returnVo.setParams(pays);
		return transfer(returnVo);
	}

	@ResponseBody
	@RequestMapping("/zfb_wx_return")
	public String zfb_wx_return(OnePayVo vo, HttpServletRequest req) throws Exception {
		String MerNo = vo.getAg_account();
		String orderNo = vo.getOrder_no();
		String orderStatus = vo.getStatus();
		String amout = vo.getAmount();
		String sign = vo.getSign();
		String attach = vo.getAttach();
		String msg;

		log.info("onePay回调接收参数：" + MyWebUtils.getRequestParameters(req));
		try {

			Assert.isTrue(validationTrustIp(req, DictionaryType.ONEPAT_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

			MerchantPay mp = merchantPayService.findByMerNo(MerNo);
			Assert.notEmpty(mp);
			Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

			Assert.isTrue(orderStatus.equals("SUCCESS"), "充值失败");
			
			
			OnePayVo vo2 = JSON.readValue(mp.getRemain(), OnePayVo.class);

			String paramsStr = orderNo + "|" + amout + "|" + MerNo + "|" + vo2.getKey();
			log.info("signStr:" + DigestUtils.signByMD5(paramsStr));
			 // 验证签名
            Assert.isTrue(sign.equalsIgnoreCase(DigestUtils.signByMD5(paramsStr)), "万付验证失败");


			Boolean lockFlag = false;
			String loginName = attach;

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
					tradeService.doPayFlow(orderNo, amout, loginName, mp, "",null);
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

		} catch (BusinessException e) {
			log.error("回掉异常:", e);
		}
		return ERROR;
	}

}
