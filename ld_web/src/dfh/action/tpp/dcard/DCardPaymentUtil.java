package dfh.action.tpp.dcard;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import dfh.model.Const;
import dfh.utils.AxisUtil;
import dfh.utils.DateUtil;
import dfh.utils.GsonUtil;
import dfh.utils.HttpUtils;
import dfh.utils.RSAWithSoftware;
import dfh.utils.StringUtil;

public class DCardPaymentUtil {
	private static Logger log = Logger.getLogger(DCardPaymentUtil.class);
	
	private static final DCardPaymentUtil instance = new DCardPaymentUtil();
	
	private final String RSA_S_PublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJFG7qhKApQm6BjsRPCX9JgT0Bc45sdx7Ty142Wl4bJ/ms1Q03FFikaB2lVj5sDDlPEy3Dv/qG1IGykr6Ob91U7i1S4DbF+ryiDVTrw+KlB+P4cYoGyqnbTH+C6cGALEKCozfoIfd/8YVEAECQLEP2rv1pptMaaxYAW4F3FbcZTQIDAQAB";
	
	private final String RSA_S_PrivateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAKDpmjJl6xRz5IM4"
										+ "0fNJKyknv5CXTixJLh7NCfrh9JZlf8CS76bkkmta4KoyO2xIoPVzoTMOp+CDpSnp"
										+ "00od4F/d6J1KpwLDjxX61khipPg5T7s2P7M6dNVGoxFzko7s9RW7O0W7GVmE8UOn"
										+ "5YLe8VGm7grvYubbCnWfLHj8WavLAgMBAAECgYAHx+P15em9HnGBSuEUE4irv8lK"
										+ "k5ZrG8rIIIAICp8KGrRXuoMuHcVavZU5hZ5L+SMXW1UDJAVIHPWCllTqM5Gi3XX5"
										+ "z+6uQcO5UlFLdXG+1Pgs5hbI1iyahI+BT0jKqNCBhBh/i/pZJ3+ADBcwMfjqbGNq"
										+ "V393KuUZ5iBy7kGA2QJBAMz9qyfa3lCaacC/uR7YfBwzxqxzT58fnsK+JA0pgN1t"
										+ "2dqTlQvtRwGtLNDqdAfXseoJfSh9j0Qlomdqsl/ei58CQQDI9A1d+KNnPmuY9Tbo"
										+ "5uDfZAYgpSJ/1MskU5wmV3LovUAw86eZIIJ9ZZHnmlaP6/MpZZXcuQUAIcB1NFIl"
										+ "iLBVAkEAjmPKBtYmRNDtkoDagw2XMDPa1iEG9p4NNnLDMg1ZQPY0NHr9NhqsjwKO"
										+ "tdOVo3j1UX8j3ANTh9+obrOTkbpROwJBALQjuvvbULfTh+bVIwUDOuBmrOuEvPl0"
										+ "yynkXZ0tVU/3ntyC/2HQd8lrTLEedKyNqiwY6Y+0uBvXJcyrO1x0PQUCQQCIfJFU"
										+ "0xnKKTXSZ7uwePZTv8SM+flrx19LNIUJ37D0EjGcZh20Xk9Ik5R2ydzyhhNvvpU4"
										+ "3xvrFEte+3r3Hsjo";
	
	private final String merchant_code = "2000295548";
	private final String dCardUrl = "https://api.dinpay.com/gateway/api/dcard";
	private final Pattern pattern = Pattern.compile("^\\d+(\\.[0]+)*$");
	
	private DCardPaymentUtil(){};
	
	public static DCardPaymentUtil getInstance(){
		return instance;
	}
	
	public void pay(String userName,String card_code, String card_no,String card_password, String card_amount){
		
		try {
			
			double amount = Double.parseDouble(card_amount);
			
			if(!pattern.matcher(card_amount).find()){
				GsonUtil.GsonObject(toResultJson("[提示]存款金额必须为整数！",false));
				return;
			}     
			// 判断订单金额
			if (amount < 1) {
				GsonUtil.GsonObject(toResultJson("[提示]1元以上或者1元才能存款！",false));
				return;
			}
			if (amount > 50000) {
				GsonUtil.GsonObject(toResultJson("[提示]存款金额不能超过5000！",false));
				return;
			}

			if(!StringUtil.isAvaliableBankCard(card_code)){
				GsonUtil.GsonObject(toResultJson("[提示]点卡类型不存在，请重新选择。",false));
				return;
			}

			//查看在线支付是否开启
			Const constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "智付点卡1" }, Const.class);

			if (constPay == null) {
				GsonUtil.GsonObject(toResultJson("[提示]该支付方式不存在！",false));
				return;
			}
			if ("0".equals(constPay.getValue())) {
				GsonUtil.GsonObject(toResultJson("[提示]点卡支付正在维护！",false));
				return;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderNo", new Object[] {userName}, String.class);
			if (orderNo == null) {
				GsonUtil.GsonObject(toResultJson("[提示]获取商家订单号失败！",false));
				return;
			}
			// 接口版本(必选)固定值:V3.0
			String interface_version = "V3.0";
			// 后台通知地址(必填)
			String notify_url = "http://pay.jiekoue68.com:2112/asp/dcardDinpayNotify.aspx";
			
			// 商家定单号(必填)
			String order_no = userName + "_flag_" + card_code + "_e68" + orderNo;
			// 商家定单时间(必填)
			String order_time = DateUtil.formatDateForStandard(new Date());
			// 签名方式(必填)
			String sign_type = "RSA-S";
			// 业务类型(必填)固定值:dcard_pay
			String service_type = "dcard_pay";
			String encrypt_info = "";
			
			// 组织订单信息
			StringBuffer signSrc = new StringBuffer();
			//组织订单信息
			if(!"".equals(card_amount)) {
				signSrc.append("card_amount=").append(card_amount).append("&");
			}
			if(!"".equals(card_code)) {
				signSrc.append("card_code=").append(card_code).append("&");
			}
			signSrc.append("client_ip=").append("127.0.0.1&");
			String encrypt_params = card_no + "|" + card_password ;
			if(!"".equals(encrypt_params)) {
				encrypt_info = RSAWithSoftware.encryptByPublicKey(encrypt_params, RSA_S_PublicKey); 
				signSrc.append("encrypt_info=").append(encrypt_info).append("&");
			}
			if (!"".equals(interface_version)) {
				signSrc.append("interface_version=").append(interface_version).append("&");
			}
			if (!"".equals(merchant_code)) {
				signSrc.append("merchant_code=").append(merchant_code).append("&");
			}
			if (!"".equals(notify_url)) {
				signSrc.append("notify_url=").append(notify_url).append("&");
			}
			if (!"".equals(order_no)) {
				signSrc.append("order_no=").append(order_no).append("&");
			}
			if (!"".equals(order_time)) {
				signSrc.append("order_time=").append(order_time).append("&");
			}
			if (!"".equals(service_type)) {
				signSrc.append("service_type=").append(service_type);
			}
			String sign = "";
			if("RSA-S".equals(sign_type)) {
				sign = RSAWithSoftware.signByPrivateKey(signSrc.toString(), RSA_S_PrivateKey);
			}
			Map<String, String> params = new HashMap<String, String>();
			params.put("sign", sign);
			params.put("interface_version", interface_version);
			params.put("sign_type", sign_type);
			params.put("service_type", service_type);
			params.put("merchant_code", merchant_code);
			params.put("order_no", order_no);
			params.put("order_time", order_time);
			params.put("card_code", card_code);
			params.put("card_amount", card_amount);
			params.put("encrypt_info", encrypt_info);
			params.put("notify_url", notify_url);
			params.put("client_ip", "127.0.0.1");
			String result = submitDCardDinpay(params);
			log.info(userName + "  智富点卡订支付：" + result);
			if(StringUtils.isNotBlank(result)){
				Document document = DocumentHelper.parseText(result);
				if(document.selectSingleNode( "/dinpay/trade_status").getText().equalsIgnoreCase("ACCEPTED_SUCCESS")){
					/*//验签
					if(vefitySyncSign(document)){
						//玩家额度
						String creditResult = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), 
								AxisUtil.NAMESPACE, "dinpaySyncUpdateCredit", new Object[] {orderNo, card_amount, user.getLoginname(), order_no, merchant_code}, String.class);
						if(null == creditResult){
							setError_info("[提示]支付成功");
						}else{
							setError_info("[提示]" + creditResult);
						}
						
					}*/
					//不做处理，使用回调处理
					GsonUtil.GsonObject(toResultJson("[提示]已提交至支付平台处理...",true));
					return;
				}
			}
			GsonUtil.GsonObject(toResultJson("[提示]支付失败！",false));
		} catch (Exception e) {
			e.printStackTrace();
			GsonUtil.GsonObject(toResultJson("[提示]支付失败！",false));
		}
	}

	/**
	 * 提交至智付
	 * @param params
	 * @return
	 */
	private String submitDCardDinpay(Map<String, String> params){
		NameValuePair[] paramArry = new NameValuePair[params.size()];
		int i = 0;
		for (String key : params.keySet()) {
			paramArry[i] = new NameValuePair(key, params.get(key));
			i++;
		}
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(dCardUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");
		method.setRequestBody(paramArry);
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param message 訊息
	 * @param success 成功/失敗
	 * @return map
	 */
	private Object toResultJson(Object message,boolean success){
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("success", success);
		result.put("message", message);
		return result;
	}
	
}
