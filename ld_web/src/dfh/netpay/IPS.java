package dfh.netpay;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dfh.security.EncryptionUtil;
import dfh.utils.DateUtil;
import dfh.utils.NumericUtil;

public class IPS implements NetpayInterfaces {
	private static Log log = LogFactory.getLog(Cncard.class);
	private static HttpClient httpclient = new HttpClient();
	private static String ENCODE_TYPE = "GBK";
	private static Integer TIME_OUT = Integer.valueOf(10000);
	static {
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
		params.setParameter("http.socket.timeout", TIME_OUT);
		httpclient.setParams(params);
	}

	public boolean callBackValidate(HttpServletRequest request, String merkey) {
		if ((request == null) || (merkey == null))
			return false;
		String billno = request.getParameter("billno");// 本地订单号
		String Currency_type = request.getParameter("Currency_type");// 币种
		String amount = request.getParameter("amount");// 订单金额
		String date = request.getParameter("date");// 订单日期
		String succ = request.getParameter("succ");// 成功标识 Y:支付成功；N：支付失败
		String ipsbillno = request.getParameter("ipsbillno");// ips的订单号
		String signature = request.getParameter("signature");// 验证信息 全部是小写

		String hmac = EncryptionUtil.encrypt(billno + amount + date + succ + ipsbillno + Currency_type + merkey);
		System.out.println("cncard hmac:" + signature + ";our hmac:" + hmac);

		return hmac.equalsIgnoreCase(signature);
	}

	public String getHamc(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey) {
		String Mer_code = merno;// 商户号
		String Billno = billno;// 本地订单号
		String Amount = NumericUtil.formatDouble(amount);// 订单总金额
		String Date = DateUtil.fmtyyyyMMdd(DateUtil.getToday());// 订单产生日期
		String Currency_Type = "RMB";// 币种
		String Gateway_Type = "01";// 01—人民币借记卡（默认值）
		String Merchanturl = responseUrl;// 回调url
		String OrderEncodeType = "2";// 使用md5方式加密
		String RetEncodeType = "12";// 返回时同样使用md5加密方式
		String Rettype = "1";// 选择返回
		String ServerUrl = responseUrl;// 回调url

		String SignMD5 = EncryptionUtil.encrypt(Billno + Amount + Date + Currency_Type + merkey);// 签名信息

		return SignMD5;
	}

	private String getURL(String action, Map<String, String> params) {
		String url = "";

		if (action == null)
			return url;

		if ((params != null) && (params.size() > 0)) {
			url = action + "?";
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = params.get(key);
				if (url.endsWith("?"))
					url = url + key + "=" + value;
				else
					url = url + "&" + key + "=" + value;
			}
		} else {
			url = action;
		}
		return url;
	}

	public String requestURL(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey, String ip) {
		return requestURL(puturl, billno, merno, amount, responseUrl, merkey);
	}

	public String requestURL(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey) {
		String Mer_code = merno;// 商户号
		String Billno = billno;// 本地订单号
		String Amount = NumericUtil.formatDouble(amount);// 订单总金额
		String Date = DateUtil.fmtyyyyMMdd(DateUtil.getToday());// 订单产生日期
		String Currency_Type = "RMB";// 币种
		String Gateway_Type = "01";// 01—人民币借记卡（默认值）
		String Merchanturl = responseUrl;// 回调url
		String OrderEncodeType = "2";// 使用md5方式加密
		String RetEncodeType = "12";// 返回时同样使用md5加密方式
		String Rettype = "1";// 选择返回
		String ServerUrl = responseUrl;// 回调url

		String SignMD5 = EncryptionUtil.encrypt(Billno + Amount + Date + Currency_Type + merkey);// 签名信息

		Map<String, String> params = new HashMap();
		params.put("Mer_code", Mer_code);
		params.put("Billno", Billno);
		params.put("Amount", Amount);
		params.put("Date", Date);
		params.put("Currency_Type", Currency_Type);
		params.put("Gateway_Type", Gateway_Type);
		params.put("Merchanturl", Merchanturl);
		params.put("OrderEncodeType", OrderEncodeType);
		params.put("RetEncodeType", RetEncodeType);
		params.put("Rettype", Rettype);
		params.put("ServerUrl", ServerUrl);
		params.put("SignMD5", SignMD5);

		return getURL(puturl, params);
	}

	public static void main(String[] args) {
		IPS ips = new IPS();
		System.out.println(ips.requestURL("https://pay.ips.com.cn/ipayment.aspx", "12324332523343", "013954", 10.00, "http://www.baidu.com/",
				"XTZtMfkQ73pNe6CTPnobFnAYkE1c4YNkqmWh1ZRu8RQTXLa5qQSvqbFJsaiiDhw8Ge6SA51QvU3OwHazDVMEg1To7mP0QJG15rmSoiULycaw8QwAaJ4IuH2olruyGaZB"));

	}

}
