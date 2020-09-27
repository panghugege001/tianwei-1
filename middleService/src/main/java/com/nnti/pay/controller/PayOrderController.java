package com.nnti.pay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.security.RSAWithSoftware;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.DbPayVo;
import com.nnti.pay.controller.vo.DinpayPayVo;
import com.nnti.pay.controller.vo.HcPayVo;
import com.nnti.pay.controller.vo.HtPayVo;
import com.nnti.pay.controller.vo.NewXlbPayVo;
import com.nnti.pay.controller.vo.SanErPayVo;
import com.nnti.pay.controller.vo.TjfPayVo;
import com.nnti.pay.controller.vo.XlbPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.enums.PayType;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.model.vo.PayOrder;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.IPayOrderService;

/**
 * Created by wander on 2017/2/17.
 */
@Controller
@RequestMapping("payOrder")
public class PayOrderController extends BasePayController {

	protected static Logger log1 = Logger.getLogger(PayOrderController.class);
	protected static ObjectMapper JSON = new ObjectMapper();

	@Autowired
	private IPayOrderService payOrderService;
	@Autowired
	private IMerchantPayService merchantPayService;
	@Autowired
	private IUserService userService;

	@RequestMapping("/main")
	public ModelAndView main() {
		ModelAndView model = new ModelAndView("pay/payOrder");
		return model;
	}

	@ResponseBody
	@RequestMapping("/query")
	public List<PayOrder> query(PayOrder payOrder) {
		List<PayOrder> payOrders = null;// payOrderService.findByCondiction(payOrder);
		return payOrders;
	}

	/*** 修改订单为 待审核 */
	@ResponseBody
	@RequestMapping("/supplement")
	public Response update(HttpServletRequest req) throws Exception {
		String orderid = req.getParameter("orderid"); // 订单号
		String money = req.getParameter("money"); // 订单金额
		String loginname = req.getParameter("loginname"); // 用户名
		String platformId = req.getParameter("platformId"); // 平台id

		Assert.notEmpty(orderid, money);
		Assert.isTrue(NumericUtil.isMoney(money), "金额格式错误");

		PayOrder payOrder = payOrderService.get(orderid.trim());
		if (MyUtils.isNotEmpty(payOrder)) {

			MerchantPay mp = merchantPayService.findByPlatform(payOrder.getPayPlatform());
			Assert.isTrue(mp.getUseable() == 1, "通道已禁用！");

			Assert.isTrue(Double.valueOf(money.trim()).equals(payOrder.getMoney()), "订单金额不匹配,请输入正确的金额");
			Assert.isTrue(!payOrder.getType().equals(PayType.Init.getCode()), "该商户号正在处理中...");
			Assert.isTrue(!payOrder.getType().equals(PayType.FAIL.getCode()), "该商户号已经被取消...");
			Assert.isTrue(!payOrder.getType().equals(PayType.SUCESS.getCode()), "该商户号已经支付成功...");

			PayOrder payOrder_new = new PayOrder(orderid, PayType.Init.getCode());
			payOrder_new.setReturnTime(DateUtil.getCurrentTimestamp());
			payOrderService.update(payOrder_new);
		} else {
			Assert.isTrue(false, "不存在该订单号...");
		}

		return transfer(success);
	}

	/******* 查询订单状态 */
	public static String queryPayOrder(Object obj, MerchantPay mp, HttpServletResponse res, HttpServletRequest req)
			throws Exception {
	    //log1.info("obj 接收参数：" + MyWebUtils.getRequestParameters(req));
		String paramsStr = "";
		// 迅联宝
		if (obj instanceof XlbPayVo) {
			XlbPayVo xlbPayVo = (XlbPayVo) obj;
			paramsStr = MyUtils.obj2UrlParam(xlbPayVo, true, "apiName", "apiVersion", "platformID", "merchNo",
					"orderNo", "tradeDate", "amt");
			paramsStr = paramsStr + "&signMsg=" + DigestUtils.signByMD5(paramsStr, mp.getSignKey());

			// 构建请求参数
			String url = "http://trade.id888.cn:8880/cgi-bin/netpayment/pay_gate.cgi";
			log1.info("xlb查询订单-请求参数：" + paramsStr);
			String result = MyWebUtils.getHttpContentByBtParam(url, paramsStr);
			log1.info("xlb查询订单-返回结果：" + result);

			String success = "";
			if (MyUtils.isNotEmpty(result)) {
				success = MyUtils.matcher("<respCode>(.*?)</respCode>", result);
				if ("00".equals(success)) {
					String status = MyUtils.matcher("<Status>(.*?)</Status>", result);
					return status;
				} else {
					String info = MyUtils.matcher("<respDesc>(.*?)</respDesc>", result);
					return info;
				}
			} else {
				return null;
			}
		}
		// 智付
		else if (obj instanceof DinpayPayVo) {
			DinpayPayVo dinpayPayVo = (DinpayPayVo) obj;
			dinpayPayVo.setService_type("single_trade_query");
			paramsStr = MyUtils.obj2UrlParam(dinpayPayVo, false, "interface_version", "merchant_code", "order_no",
					"service_type", "trade_no");

			String sign = RSAWithSoftware.signByPrivateKey(paramsStr, mp.getSignKey());
			dinpayPayVo.setSign(sign);

			Map map = MyUtils.describe(dinpayPayVo, "service_type", "merchant_code", "interface_version", "sign_type",
					"order_no", "trade_no", "sign");

			String url = "https://query.dinpay.com/query";

			log1.info("zf查询订单-请求参数：" + map);
			String result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(map));
			log1.info("zf查询订单-返回结果：" + result);

			if (MyUtils.isNotEmpty(result)) {
				String success = MyUtils.matcher("<is_success>(.*?)</is_success>", result);
				if ("T".equals(success)) {
					String status = MyUtils.matcher("<trade_status>(.*?)</trade_status>", result);
					return status;
				} else {
					String info = MyUtils.matcher("<error_code>(.*?)</error_code>", result);
					return info;
				}
			} else {
				return null;
			}
		}
		// 多宝
		else if (obj instanceof DbPayVo) {
			DbPayVo dbPayVo = (DbPayVo) obj;
			dbPayVo.setParter(mp.getMerchantCode());

			paramsStr = MyUtils.obj2UrlParam(dbPayVo, true, "orderid", "parter");
			paramsStr = paramsStr + "&sign=" + DigestUtils.signByMD5(paramsStr, mp.getSignKey());

			// 构建请求参数
			String url = "https://gw.169.cc/interface/search.aspx";

			log1.info("db查询订单-请求参数：" + paramsStr);
			String result = MyWebUtils.getHttpContentByBtParam(url, paramsStr);
			log1.info("db查询订单-返回结果：" + result);

			if (MyUtils.isNotEmpty(result) && result.contains("&opstate=0&")) {
				return "SUCCESS";
			} else {
				return null;
			}
		}
		// 32pay
		else if (obj instanceof SanErPayVo) {
			SanErPayVo sanErPayVo = (SanErPayVo) obj;

			paramsStr = MyUtils.obj2UrlParam(sanErPayVo, true, "P_UserId", "P_OrderId", "P_ChannelId", "P_CardId",
					"P_FaceValue");

			sanErPayVo.setP_PostKey(DigestUtils.signByMD5(paramsStr + "&P_PostKey=", mp.getSignKey()));

			Map map = MyUtils.describe(sanErPayVo, "P_UserId", "P_OrderId", "P_ChannelId", "P_CardId", "P_FaceValue",
					"P_PostKey");

			// 构建请求参数
			String url = "https://api.32pay.com/Pay/Query.aspx";

			log1.info("32pay 查询订单-请求参数：" + map);
			String result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(map));
			log1.info("32pay 查询订单-返回结果：" + result);

			if (MyUtils.isNotEmpty(result) && result.contains("&P_flag=1&") && result.contains("&P_status=1&")) {
				return "SUCCESS";
			} else {
				return null;
			}
		}
		// 汇潮
		else if (obj instanceof HcPayVo) {
			HcPayVo hcPayVo = (HcPayVo) obj;
			String signKey = DigestUtils.signByMD5(hcPayVo.getMerNo(), mp.getSignKey()).toUpperCase();

			Map<String, String> pays = new HashMap<String, String>();
			pays.put("merCode", hcPayVo.getMerNo());
			pays.put("orderNumber", hcPayVo.getBillNo());
			pays.put("beginTime", "");
			pays.put("endTime", "");
			pays.put("pageIndex", "");
			pays.put("sign", signKey);
			pays.put("tx", "1001");
			Map baseXmlMap = new TreeMap();
			baseXmlMap.put("requestDomain", HcController.buildXML2(pays));

			// 构建请求参数
			String url = "https://gwapi.yemadai.com/merchantBatchQueryAPI";

			log1.info("hc 查询请求参数：" + baseXmlMap);
			String result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(baseXmlMap));
			log1.info("hc 查询订单返回结果：" + result);

			if (MyUtils.isNotEmpty(result)) {
				String success = MyUtils.matcher("<resultCode>(.*?)</resultCode>", result);
				if ("00".equals(success)) {
					String status = MyUtils.matcher("<orderStatus>(.*?)</orderStatus>", result);
					return status;
				} else {
					String info = MyUtils.matcher("<resultCode>(.*?)</resultCode>", result);
					return info;
				}
			} else {
				return null;
			}
		}
		// 摩宝
		else if (obj instanceof NewXlbPayVo) {
			NewXlbPayVo newXlbPayVo = (NewXlbPayVo) obj;

			newXlbPayVo.setVersion("1.0.0.0");

			newXlbPayVo.setService("TRADE.QUERY");

			paramsStr = MyUtils.join("=", "&", "service", newXlbPayVo.getService(), "version", newXlbPayVo.getVersion(),
					"merId", newXlbPayVo.getMerId(), "tradeNo", newXlbPayVo.getTradeNo(), "tradeDate",
					newXlbPayVo.getTradeDate(), "amount", newXlbPayVo.getAmount());

			newXlbPayVo.setSign(DigestUtils.signByMD5(paramsStr, mp.getSignKey()));

			Map map = MyUtils.describe(newXlbPayVo, "service", "version", "merId", "tradeNo", "tradeDate", "amount",
					"sign");

			// 构建请求参数
			String url = "";
			if (newXlbPayVo.getTradeNo().contains("_xlb")) {
				url = "http://gate.rrrrcc.top/cooperate/gateway.cgi";
			} else {
				url = "http://gate.starspay.com/cooperate/gateway.cgi";
			}

			log1.info("NewXlb 查询订单-请求参数：" + map);
			String result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(map));
			log1.info("NewXlb 查询订单-返回结果：" + result);

			if (MyUtils.isNotEmpty(result)) {
				String success = MyUtils.matcher("<code>(.*?)</code>", result);
				if ("00".equals(success)) {
					String status = MyUtils.matcher("<status>(.*?)</status>", result);
					return status;
				} else {
					String info = MyUtils.matcher("<desc>(.*?)</desc>", result);
					return info;
				}
			} else {
				return null;
			}
		}
		// 汇通
		else if (obj instanceof HtPayVo) {
			HtPayVo htPayVo = (HtPayVo) obj;
			
			Map map = MyUtils.describe(htPayVo, "merchant_code", "order_no", "trade_no");
			
			htPayVo.setSign(MyUtils.getSign(map, mp.getSignKey()));
			
			Map postMap = MyUtils.describe(htPayVo, "merchant_code", "order_no", "trade_no","sign");
			
			// 构建请求参数
			String url = "https://api.huitongvip.com/query.html";

			log1.info("Ht 查询订单-请求参数：" + map);
			String result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(postMap));
			log1.info("Ht 查询订单-返回结果：" + result);

			HtPayVo ht = JSON.readValue(result, HtPayVo.class);
			
			if (MyUtils.isNotEmpty(ht) && ht.getIs_success().equals("true") && ht.getTrade_status().equals("success")) {
				return "SUCCESS";
			} else {
				return null;
			}
		}
		
		// 天机付 
		else if (obj instanceof TjfPayVo) {
			TjfPayVo tjfPayVo = (TjfPayVo) obj;

			tjfPayVo.setVersion("1.0.0.0");

			tjfPayVo.setService("TRADE.QUERY");

			paramsStr = MyUtils.join("=", "&", "service", tjfPayVo.getService(), "version", tjfPayVo.getVersion(),
					"merId", tjfPayVo.getMerId(), "tradeNo", tjfPayVo.getTradeNo(), "tradeDate",
					tjfPayVo.getTradeDate(), "amount", tjfPayVo.getAmount());

			tjfPayVo.setSign(DigestUtils.signByMD5(paramsStr, mp.getSignKey()));

			Map map = MyUtils.describe(tjfPayVo, "service", "version", "merId", "tradeNo", "tradeDate", "amount",
					"sign");

			// 构建请求参数
			String url = "http://gate.iceuptrade.com/cooperate/gateway.cgi";

			log1.info("TjfPay 查询订单-请求参数：" + map);
			String result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(map));
			log1.info("TjfPay 查询订单-返回结果：" + result);

			if (MyUtils.isNotEmpty(result)) {
				String success = MyUtils.matcher("<code>(.*?)</code>", result);
				if ("00".equals(success)) {
					String status = MyUtils.matcher("<status>(.*?)</status>", result);
					return status;
				} else {
					String info = MyUtils.matcher("<desc>(.*?)</desc>", result);
					return info;
				}
			} else {
				return null;
			}
		}
		
		
		return null;
	}

	public static void main(String[] args) throws Exception {
		/*
		 * XlbPayVo vo = new XlbPayVo(); vo.setApiName("MOBO_TRAN_QUERY");
		 * vo.setApiVersion("1.0.0.0"); vo.setPlatformID("210001220011465");
		 * vo.setMerchNo("210001220011465");
		 * vo.setOrderNo("qy_xlbwx_v673303794_4001663");
		 * vo.setTradeDate("20171116"); vo.setAmt("50.00");
		 * 
		 * MerchantPay mp = new MerchantPay();
		 * mp.setSignKey("5eace522c4f3f98f154ec7085efc6a1f"); mp.setPayUrl(
		 * "http://trade.dxgpay.com:8080/cgi-bin/netpayment/pay_gate.cgi");
		 * 
		 * 
		 * DinpayPayVo dipayPayVo = new DinpayPayVo();
		 * dipayPayVo.setService_type("single_trade_query");
		 * dipayPayVo.setMerchant_code("2000295699");
		 * dipayPayVo.setInterface_version("V3.0");
		 * dipayPayVo.setSign_type("RSA-S");
		 * dipayPayVo.setOrder_no("qy_cmzf1_luidei_4000506");
		 * dipayPayVo.setTrade_no("1628018883");
		 * 
		 * MerchantPay mp = new MerchantPay(); mp.setSignKey(
		 * "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMSIRk+ASv9QiqSwRT1VTPpTQwE2negE24cZxqxge/dArRCxnDeJXoqQcb7L8kNfxHCL7cMH1zI4ckw2UIXe2HuFP7/3oJQ4akSmpPF6/NC8j0GtgXlbSYw2hqCgKHJDjxrGukeGJrW9OmG8BGuYBnq2hnn+3IjPBQEqbjd/xeu1AgMBAAECgYEAgH4YeiIG5qZh/wrAOjlq+r81L7Xlx6xlWw8fNdRfOGiwvhlhaW4GwdFujLAK1MHoTS0vZjPaHpuJHwrj2qK4JeQtOE1I+dqoNXM5H++SU5VbzI6732NGIZGyfRxq5YFeAiueutAkROhj6sbUA5tVnnl9lk4/uGKpBvLrOdkzHIECQQDvUc6KfhldakiOef7u1t/+bE++2LbRM3BXtm+v9yUWP5zVyZiskbMkmow5Pa11YIG24qYjWoyTlvxHhqrltcw/AkEA0jsDPVSHQs1qPuRI6YeeS4X3/MG4mk3oG32zbiMkYXvTiR64gvrJi/QIfmLhac9/UCdFKTB/8jJPqjRUjrSbCwJATSFukdAmWKcij1jeQxfZxWQfJ4EtGqubb4mK4Ymj2FHUOErq++NRe2CcBO7N8uCRreHKbeaHHCIzWNIcTkCTfQJASnaGjjmbc0jo4f/f53+WzO9VyBwhs932YY5jeJZP84t1gLL17cbAjqbQP1vstZq+YHiYYZ+BIhs55nKEpMtJwwJBAMIyNiRamA0oqj/DeMxGeptWjKv97DU1zhDRZmn19WRRuGWNwNAViQ74ro2x4LtxewO2NGzgEJRWhSae6CyypAo="
		 * ); mp.setPayUrl("https://query.dinpay.com/query");
		 * 
		 * 
		 * DbPayVo vo = new DbPayVo();
		 * vo.setOrderid("e68_dbwx_cyb420607_4000131"); vo.setParter("987584");
		 * 
		 * MerchantPay mp = new MerchantPay();
		 * mp.setSignKey("6756e105ff82494683dfa7e5764d475e");
		 * mp.setPayUrl("https://gw.169.cc/interface/search.aspx");
		 * 
		 * 
		 * SanErPayVo vo = new SanErPayVo(); vo.setP_UserId("1001840");
		 * vo.setP_OrderId("qy_32wx_hccheng_4000023"); vo.setP_ChannelId("33");
		 * vo.setP_CardId(""); vo.setP_FaceValue("10");
		 * 
		 * MerchantPay mp = new MerchantPay();
		 * mp.setSignKey("53cd21a01d2f47729206b0e526c5ce12");
		 * mp.setPayUrl("https://API.32PAY.COM/pay/query.aspx");
		 * 
		 * 
		 * 
		 * 
		 * HcPayVo vo = new HcPayVo(); vo.setMerNo("41698");
		 * vo.setOrderNo("qy_hckj_8cm5716920_4000822");
		 * 
		 * MerchantPay mp = new MerchantPay();
		 * mp.setSignKey("PdpmRceYFaKwBtb07GcNANCpKtGHtS6EbxPLiE5ZNrc=");
		 * mp.setPayUrl("https://gwapi.yemadai.com/merchantBatchQueryAPI");
		 

		NewXlbPayVo vo = new NewXlbPayVo();

		vo.setService("TRADE.QUERY");
		vo.setVersion("1.0.0.0");
		vo.setMerId("2017082944010105");
		vo.setTradeNo("l8_mbwx_six656_4004608");
		vo.setTradeDate("20171228");
		vo.setAmount("10.00");
		MerchantPay mp = new MerchantPay();
		mp.setSignKey("633d02eb477f4b5a44212f8cd76d8151");
		mp.setPayUrl("http://gate.rrrrcc.top/cooperate/gateway.cgi");
		*/
		
		HtPayVo vo = new HtPayVo();
        vo.setMerchant_code("15149050");
        vo.setOrder_no("l8_htqq_zky321_4000221");
        vo.setTrade_no("1090501229239172");
		MerchantPay mp = new MerchantPay();
		mp.setSignKey("c190a43347965f78748951d51e87200e");
		queryPayOrder(vo, mp, null, null);
	}

}
