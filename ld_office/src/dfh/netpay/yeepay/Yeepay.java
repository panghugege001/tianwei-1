package dfh.netpay.yeepay;

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
import dfh.utils.NumericUtil;

public class Yeepay {
	private static Log log = LogFactory.getLog(Yeepay.class);
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

	public static boolean callBackValidate(HttpServletRequest request, String merkey) {
		if ((request == null) || (merkey == null))
			return false;

		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		String r_hmac = request.getParameter("hmac");

		String hmac = EncryptionUtil.hmacEncrypt(p1_MerId + r0_Cmd + r1_Code + r2_TrxId + r3_Amt + r4_Cur + r5_Pid + r6_Order + r7_Uid + r8_MP + r9_BType, merkey);
		System.out.println("yeepay hmac:" + r_hmac + ";our hmac:" + hmac);
		return hmac.equals(r_hmac);
	}

	public static String getHamc(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey) {
		String p0_Cmd = "Buy";
		String p1_MerId = merno;
		String p2_Order = billno;
		String p3_Amt = NumericUtil.formatDouble(amount);
		String p4_Cur = "CNY";
		String p5_Pid = "Online Payment";
		String p8_Url = responseUrl;
		String hmac = EncryptionUtil.hmacEncrypt(p0_Cmd + p1_MerId + p2_Order + p3_Amt + p4_Cur + p5_Pid + p8_Url, merkey);
		return hmac;
	}

	private static String getURL(String action, Map<String, String> params) {
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

	public static void main(String[] args) throws Exception {
	}

	public static String requestURL(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey) {
		String p0_Cmd = "Buy";
		String p1_MerId = merno;
		String p2_Order = billno;
		String p3_Amt = NumericUtil.formatDouble(amount);
		String p4_Cur = "CNY";
		String p5_Pid = "Online Payment";
		String p8_Url = responseUrl;
		String hmac = EncryptionUtil.hmacEncrypt(p0_Cmd + p1_MerId + p2_Order + p3_Amt + p4_Cur + p5_Pid + p8_Url, merkey);
		Map params = new HashMap();
		params.put("p0_Cmd", p0_Cmd);
		params.put("p1_MerId", p1_MerId);
		params.put("p2_Order", p2_Order);
		params.put("p3_Amt", p3_Amt);
		params.put("p4_Cur", p4_Cur);
		params.put("p5_Pid", p5_Pid);
		params.put("p8_Url", p8_Url);
		params.put("hmac", hmac);
		return getURL(puturl, params);
	}
}