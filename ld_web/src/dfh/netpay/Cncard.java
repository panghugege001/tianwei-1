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
import dfh.utils.StringUtil;

public class Cncard implements NetpayInterfaces {

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

	/**
	 * 回调验证
	 * 
	 * @param request
	 * @param merkey
	 *            key值
	 * @return
	 */
	public boolean callBackValidate(HttpServletRequest request, String merkey) {
		if ((request == null) || (merkey == null))
			return false;

		String c_mid = request.getParameter("c_mid");// 商户编号
		String c_order = request.getParameter("c_order");// 本地订单元号
		String c_orderamount = request.getParameter("c_orderamount");// 订单总金额
		String c_ymd = request.getParameter("c_ymd");// 订单产生日期yyyyMMdd
		String c_moneytype = request.getParameter("c_moneytype");// 支付币种
		String c_transnum = request.getParameter("c_transnum");// 网前流水号
		String c_succmark = request.getParameter("c_succmark");// 产生的标识 Y-成功 N-失败
		String c_signstr = request.getParameter("c_signstr");// md5验证

		String hmac = EncryptionUtil.encrypt(c_mid + c_order + c_orderamount + c_ymd + c_transnum + c_succmark + c_moneytype + merkey);
		System.out.println("cncard hmac:" + c_signstr + ";our hmac:" + hmac);

		return hmac.equals(c_signstr);
	}

	public String getHamc(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey) {
		String c_mid = merno;// 商户编号
		String c_order = billno;// 本地订单号
		String c_orderamount = NumericUtil.formatDouble(amount);// 金额
		String c_ymd = DateUtil.fmtyyyyMMdd(DateUtil.getToday());// 订单产生日期
		String c_moneytype = "0";// 对应值固定为0，代表人民币
		String c_retflag = "1";// 返回到c_returl对应的URL
		String c_returl = responseUrl;// 回调url
		String notifytype = "1";// 服务器通知方式

		return EncryptionUtil.encrypt(c_mid + c_order + c_orderamount + c_ymd + c_moneytype + c_retflag + c_returl + notifytype + merkey);// 签名信息
	}

	public String requestURL(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey, String ip) {
		return requestURL(puturl, billno, merno, amount, responseUrl, merkey);
	}

	/**
	 * 
	 * @param puturl
	 *            支付网关
	 * @param billno
	 *            本地单号
	 * @param merno
	 *            商户号
	 * @param amount
	 *            金额
	 * @param responseUrl
	 *            回调url
	 * @param merkey
	 *            商户key
	 * @return
	 */
	public String requestURL(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey) {
		String c_mid = merno;// 商户编号
		String c_order = billno;// 本地订单号
		String c_orderamount = NumericUtil.formatDouble(amount);// 金额
		String c_ymd = DateUtil.fmtyyyyMMdd(DateUtil.getToday());// 订单产生日期
		String c_moneytype = "0";// 对应值固定为0，代表人民币
		String c_retflag = "1";// 返回到c_returl对应的URL
		String c_returl = responseUrl;// 回调url
		String notifytype = "1";// 服务器通知方式

		String c_signstr = EncryptionUtil.encrypt(c_mid + c_order + c_orderamount + c_ymd + c_moneytype + c_retflag + c_returl + notifytype + merkey);// 签名信息

		Map<String, String> params = new HashMap();
		params.put("c_mid", c_mid);
		params.put("c_mid", c_mid);
		params.put("c_order", c_order);
		params.put("c_orderamount", c_orderamount);
		params.put("c_ymd", c_ymd);
		params.put("c_moneytype", c_moneytype);
		params.put("c_retflag", c_retflag);
		params.put("c_returl", c_returl);
		params.put("notifytype", notifytype);
		params.put("c_signstr", c_signstr);

		return getURL(puturl, params);
	}

	public static void main(String[] args) {
		Cncard cncard = new Cncard();
		String resultUrl = cncard.requestURL("https://www.cncard.net/purchase/getorder.asp", "222222222222222222", "1032736", 100.00, "http://www.888echo.net/pay/rCncard.jsp", "59bxaych9j");

		String message = StringUtil.urlEncode(resultUrl);

		System.out.println(message);

	}

}
