package com.nnti.pay.controller;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
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
import com.nnti.pay.controller.vo.YFuPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * Created by addis on 2017/7/26. 亿付支付
 */
@Controller
@RequestMapping("/yifu")
public class YFUController extends BasePayController {

	@Autowired
	private IBasicService basicService;
	@Autowired
	private IMerchantPayService merchantPayService;
	@Autowired
	private ITradeService tradeService;

	@ResponseBody
	@RequestMapping("zfb_wx")
	public Response zfb_wx(HttpServletRequest req,
			@RequestParam(value = "requestData", required = false) String requestData) throws Exception {

		PayRequestVo payRequestVo = pasePayRequest(req, requestData);
		String loginName = payRequestVo.getLoginName();
		Long platformId = payRequestVo.getPlatformId();
		Integer usetype = payRequestVo.getUsetype();
		String customerIp = "127.0.0.1";
		String orderAmount = payRequestVo.getOrderAmount();

		Assert.notEmpty(loginName, platformId, orderAmount, usetype);
		loginName = loginName.trim();

		Assert.isTrue(NumericUtil.isMoney(orderAmount), "金额格式错误");

		User user = basicService.getUser(loginName);
		validationLoginName(loginName, user);

		// 根据商户号查询商户信息
		MerchantPay mp = merchantPayService.findById(platformId);
		validationMerchantPay(orderAmount, mp);

		validationAmountCutAll(loginName, mp);

		String orderId = basicService.createBillNo(loginName, orderAmount, mp, loginName, "_");

		if (usetype == Constant.USE_TYPE_WEB) {
			loginName = "wap_" + loginName;
		}

		String orderDate = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date());
		YFuPayVo vo = gson.fromJson(mp.getRemain(), YFuPayVo.class);

		vo.setZfbWx(mp.getMerchantCode(), orderId, orderDate, mp.getNotifyUrl(), mp.getShopUrl(), orderAmount, loginName,
				 "abc", "1", "abc");

		String paramsStr = MyUtils.obj2UrlParam(vo, true, "pay_amount", "pay_applydate", "pay_bankcode", "pay_callbackurl",
				"pay_memberid", "pay_notifyurl", "pay_orderid", "prod_desc", "prod_name", "total_fee");
		vo.setPay_md5sign(DigestUtils.md5(paramsStr + "&key=" + mp.getSignKey()));
		log.info("签名 " +vo.getPay_md5sign());

		Map pays = MyUtils.describe(vo, "pay_md5sign", "pay_memberid", "pay_orderid", "pay_applydate", "pay_bankcode",
				"pay_notifyurl", "pay_callbackurl", "pay_amount", "pay_tradetype", "pay_tongdao", "pay_reserved1","pay_productname","pay_productnum","pay_productdesc");

		log.info(orderId + "请求报文:" + JSON.writeValueAsString(pays));

		ReturnVo returnVo = new ReturnVo();
        returnVo.setType("1");
        returnVo.setUrl(mp.getPayUrl());
        returnVo.setParams(pays);

        log.info(orderId + ":yifu online_pay请求参数：" + pays);
        return transfer(returnVo);
	}
	
	

	@ResponseBody
	@RequestMapping("/zfb_wx_return")
	public String zfb_wx_return(YFuPayVo vo, HttpServletRequest req) {
		log.info("亿付接收回调通知参数:" + MyWebUtils.getRequestParameters(req));

		DecimalFormat df = new DecimalFormat("#.00");
		try {

			Assert.isTrue(validationTrustIp(req, DictionaryType.YIFU_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");

			Assert.isTrue("00".equals(vo.getReturncode()), "亿付支付返回支付结果为处理不成功：State：" + vo.getReturncode());

			MerchantPay mp = merchantPayService.findByMerNo(vo.getMemberid());
			Assert.notEmpty(mp);
			Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

			String paramsStr = MyUtils.obj2UrlParam(vo, true, "amount", "datetime", "memberid", "orderid",
					"returncode");
			String _sign = DigestUtils.md5(paramsStr + "&key=" + mp.getSignKey());

			log.info("_sign:" + _sign);
			Assert.isTrue(vo.getSign().equals(_sign), "亿付支付回调签名验证失败：orderId ：" + vo.getOrderid());

			Boolean lockFlag = false;
			String loginName = vo.getReserved1();

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
					tradeService.doPayFlow(vo.getOrderid(), vo.getAmount().toString(), loginName, mp, "",null);
					return OK;
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
			log.error("亿付支付宝或微信回调异常：", e);
		}

		return ERROR;
	}
}
