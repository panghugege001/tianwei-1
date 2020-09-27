package dfh.action.customer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import dfh.action.SubActionSupport;
import dfh.model.Const;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.HttpUtils;
import dfh.utils.RSAWithSoftware;
import dfh.utils.StringUtil;

/**
 * 智付点卡
 * V3.0
 */
public class DCardAction extends SubActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(DCardAction.class);
	
	//接口版本(必选)固定值:V3.0
	private String interface_version;
	//签名方式(必填)
	private String sign_type;
	//业务类型(必填)
	private String service_type;
	//商家号（必填）
	private String merchant_code;
	//商家定单号(必填)
	private String order_no;
	//商家定单时间(必填) 
	private String order_time;
	//商品编号(选填)
	private String card_code;
	//定单金额（必填）
	private String card_amount;
	//商品描述（选填）
	private String card_no;
	//商品名称（必填）
	private String card_password;
	//敏感数据加密域
	private String encrypt_info;
	//签名
	private String sign;
	//回调地址
	private String notify_url;
	
	// 错误信息
	private String error_info;
	
	//设置在支付后台的商家公钥
	//private final String RSA_S_PublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCg6ZoyZesUc+SDONHzSSspJ7+Ql04sSS4ezQn64fSWZX/Aku+m5JJrWuCqMjtsSKD1c6EzDqfgg6Up6dNKHeBf3eidSqcCw48V+tZIYqT4OU+7Nj+zOnTVRqMRc5KO7PUVuztFuxlZhPFDp+WC3vFRpu4K72Lm2wp1nyx4/FmrywIDAQAB";
	//加密卡号密码的支付公钥
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
	
	private final String dCardUrl = "https://api.dinpay.com/gateway/api/dcard";
	
	/**
	 * 点卡即时到账交易接口接入
	 * @return
	 */
	public String dcardRedirect(){
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return SUCCESS;
			}
			if(user.getFlag()==1){
				setError_info("[提示]该玩家已经冻结！");
				return SUCCESS;
			}
			//查看在线支付是否开启
			Const constPay=null;
			constPay = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "selectDeposit", new Object[] { "智付点卡1" }, Const.class);
			// 商家号（必填）
			merchant_code = "2000295548";

			if (constPay == null) {
				setError_info("[提示]该支付方式不存在！");
				return SUCCESS;
			}
			if (constPay.getValue().equals("0")) {
				setError_info("[提示]在线支付正在维护！");
				return SUCCESS;
			}
			// 判断订单金额
			if (StringUtils.isBlank(card_amount)) {
				setError_info("[提示]存款额度不能为空！");
				return SUCCESS;
			}
			try {
				if (Double.parseDouble(card_amount) < 10) {
					setError_info("[提示]10元以上或者10元才能存款！");
					return SUCCESS;
				}
				if (Double.parseDouble(card_amount) > 5000) {
					setError_info("[提示]存款金额不能超过5000！");
					return SUCCESS;
				}
			} catch (Exception e) {
				setError_info("[提示]存款金额必须是数字！");
				return SUCCESS;
			}
			if(!card_amount.matches("^\\d*[1-9]\\d*$")){ 
				setError_info("[提示]存款金额必须为整数！");
				return SUCCESS ;
			}
			//获取商家订单号
			String orderNo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getOrderNo", new Object[] {user.getLoginname()}, String.class);
			
			if (orderNo == null) {
				setError_info("[提示]获取商家订单号失败！");
				return SUCCESS;
			}
			//点卡类型
			if (StringUtils.isBlank(card_code)) {
				setError_info("[提示]点卡类型不能为空！");
				return SUCCESS;
			}
			if(!StringUtil.isAvaliableBankCard(card_code)){
				setError_info("[提示]点卡类型不存在，请重新选择。");
				return SUCCESS;
			}
			// 接口版本(必选)固定值:V3.0
			interface_version = "V3.0";
			// 后台通知地址(必填)
			notify_url = "http://pay.jiekoue68.com:2112/asp/dcardDinpayNotify.aspx";
			
			// 商家定单号(必填)
			order_no = user.getLoginname() + "_flag_" + card_code + "_e68" + orderNo;
			// 商家定单时间(必填)
			order_time = DateUtil.formatDateForStandard(new Date());
			// 签名方式(必填)
			sign_type = "RSA-S";
			// 业务类型(必填)固定值:dcard_pay
			service_type = "dcard_pay";
			
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
			//System.out.println(signSrc.toString());
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
			log.info(user.getLoginname() + "  智富点卡订支付：" + result);
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
					setError_info("[提示]已提交至支付平台处理...");
					return SUCCESS;
				}
			}
			setError_info("[提示]支付失败");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			setError_info("网络繁忙,请稍后再试！");
			return SUCCESS;
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
		log.info(result);
		return result;
	}
	
	/**
	 * 同步返回内容验签（只检验成功的订单）
	 * @throws Exception 
	 */
	private boolean vefitySyncSign(Document document) throws Exception{
		String sign = document.selectSingleNode( "/dinpay/sign").getText();
		
		String card_amount_v = document.selectSingleNode( "/dinpay/card_amount").getText();
		String card_code_v = document.selectSingleNode( "/dinpay/card_code").getText();
		String card_no_v = document.selectSingleNode( "/dinpay/card_no").getText();
		String merchant_code_v = document.selectSingleNode( "/dinpay/merchant_code").getText();
		String order_no_v = document.selectSingleNode( "/dinpay/order_no").getText();
		String trade_no_v = document.selectSingleNode( "/dinpay/trade_no").getText();
		String trade_status_v = document.selectSingleNode( "/dinpay/trade_status").getText();
		
		StringBuffer signSrc = new StringBuffer();
		if(!"".equals(card_amount_v)) {
			signSrc.append("card_amount=").append(card_amount_v).append("&");
		}
		if(!"".equals(card_code_v)) {
			signSrc.append("card_code=").append(card_code_v).append("&");
		}
		if (!"".equals(card_no_v)) {
			signSrc.append("card_no=").append(card_no_v).append("&");
		}
		if (!"".equals(merchant_code_v)) {
			signSrc.append("merchant_code=").append(merchant_code_v).append("&");
		}
		if (!"".equals(order_no_v)) {
			signSrc.append("order_no=").append(order_no_v).append("&");
		}
		if (!"".equals(trade_no_v)) {
			signSrc.append("trade_no=").append(trade_no_v).append("&");
		}
		if (!"".equals(trade_status_v)) {
			signSrc.append("trade_status=").append(trade_status_v);
		}
		if(RSAWithSoftware.signByPrivateKey(signSrc.toString(), RSA_S_PrivateKey).equals(sign)){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 新智付点卡回调通知
	 * @throws Exception 
	 */
	private final String zhifu_PublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2U2Xf6FoQK7XiF9pH1/x+4VU8o13iy9wez9oFtFMZI5EQLIzI5/iSQTnu8qHcY27pmEODT0UJj6XL1f/J0lXDViQQtPf/x5sLudm3m/TntdcOLwksqtAySRljUwqLZPXLnvP3zX96uvQcVHrBEXeAedgZ5VsNPbHlbBPGwLXUtQIDAQAB";
	public void dcardDinpayNotify() throws Exception{
		//商家号（必填）
		String merchant_code = (String) getRequest().getParameter("merchant_code");
		//通知id
		String notify_id = (String)getRequest().getParameter("notify_id");
		//接口版本(必选)固定值:V3.0
		String interface_version = (String) getRequest().getParameter("interface_version");
		//签名方式(必填)
		String sign_type =(String) getRequest().getParameter("sign_type");
		//签名
		String dinpaySign=(String)getRequest().getParameter("sign");
		//商家定单号(必填)
		String order_no = (String) getRequest().getParameter("order_no");
		//智付订单号
		String trade_no =(String)getRequest().getParameter("trade_no");
		//销卡成功时间
		String pay_date =(String)getRequest().getParameter("pay_date");
		//商品编号(选填)
		String card_code = (String) getRequest().getParameter("card_code");
		//点卡金额（必填）
		String card_amount = (String) getRequest().getParameter("card_amount");
		//商品描述（选填）
		String card_no = (String) getRequest().getParameter("card_no");
		//销卡实际金额
		String card_actual_amount =(String)getRequest().getParameter("card_actual_amount");
		//订单状态
		String trade_status =(String)getRequest().getParameter("trade_status");
		
		if (StringUtils.isNotBlank(trade_status)
				&& trade_status.equalsIgnoreCase("SUCCESS")) {
			// 验签
			StringBuffer signSrc = new StringBuffer();
			if (!"".equals(card_actual_amount)) {
				signSrc.append("card_actual_amount=").append(card_actual_amount).append("&");
			}
			// 组织订单信息
			if (!"".equals(card_amount)) {
				signSrc.append("card_amount=").append(card_amount).append("&");
			}
			if (!"".equals(card_code)) {
				signSrc.append("card_code=").append(card_code).append("&");
			}
			if (!"".equals(card_no)) {
				signSrc.append("card_no=").append(card_no).append("&");
			}
			if (!"".equals(interface_version)) {
				signSrc.append("interface_version=").append(interface_version).append("&");
			}
			if (!"".equals(merchant_code)) {
				signSrc.append("merchant_code=").append(merchant_code).append("&");
			}
			if (!"".equals(notify_id)) {
				signSrc.append("notify_id=").append(notify_id).append("&");
			}
			if (!"".equals(order_no)) {
				signSrc.append("order_no=").append(order_no).append("&");
			}
			if (!"".equals(pay_date)) {
				signSrc.append("pay_date=").append(pay_date).append("&");
			}
			if (!"".equals(trade_no)) {
				signSrc.append("trade_no=").append(trade_no).append("&");
			}
			if (!"".equals(trade_status)) {
				signSrc.append("trade_status=").append(trade_status);
			}

			/*String ourSign = RSAWithSoftware.signByPrivateKey(signSrc.toString(), RSA_S_PrivateKey);
			log.info("ourSign:" + ourSign);*/
			if (RSAWithSoftware.validateSignByPublicKey(signSrc.toString(), zhifu_PublicKey, dinpaySign)) {
				// 玩家额度
				AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_1 + "UserWebService", false), AxisUtil.NAMESPACE, "dinpaySyncUpdateCredit",
						new Object[] { trade_no, card_actual_amount, order_no, card_code, merchant_code }, String.class);
			} else {
				log.warn("!!!!!!!!!!!!! 智付点卡异步回调验签失败 !!!!!!!!!");
			}
		}
		
		getResponse().getWriter().print("SUCCESS");
	}
	
	public String getInterface_version() {
		return interface_version;
	}

	/*public void setInterface_version(String interface_version) {
		this.interface_version = interface_version;
	}*/

	public String getSign_type() {
		return sign_type;
	}

	/*public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}*/

	public String getService_type() {
		return service_type;
	}

	/*public void setService_type(String service_type) {
		this.service_type = service_type;
	}*/

	public String getMerchant_code() {
		return merchant_code;
	}

	/*public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}*/

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getOrder_time() {
		return order_time;
	}

	/*public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}*/

	public String getCard_code() {
		return card_code;
	}

	public void setCard_code(String card_code) {
		this.card_code = card_code;
	}

	public String getCard_amount() {
		return card_amount;
	}

	public void setCard_amount(String card_amount) {
		this.card_amount = card_amount;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getCard_password() {
		return card_password;
	}

	public void setCard_password(String card_password) {
		this.card_password = card_password;
	}

	public String getEncrypt_info() {
		return encrypt_info;
	}

	/*public void setEncrypt_info(String encrypt_info) {
		this.encrypt_info = encrypt_info;
	}*/

	public String getSign() {
		return sign;
	}

	/*public void setSign(String sign) {
		this.sign = sign;
	}*/

	public String getNotify_url() {
		return notify_url;
	}

	/*public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}*/

	public String getError_info() {
		return error_info;
	}

	public void setError_info(String error_info) {
		this.error_info = error_info;
	}
	
}
