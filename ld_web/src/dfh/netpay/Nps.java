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

import com.eitop.platform.tools.Charset;
import com.eitop.platform.tools.encrypt.MD5Digest;
import com.eitop.platform.tools.encrypt.xStrEncrypt;

import dfh.security.EncryptionUtil;
import dfh.utils.DateUtil;
import dfh.utils.NumericUtil;

public class Nps implements NetpayInterfaces {
	private static Log log = LogFactory.getLog(Nps.class);
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
		String OrderInfo = Charset.ISO8859_2_Gb(request.getParameter("OrderMessage"));// 订单加密信息
		String signMsg = request.getParameter("Digest");// 签名认证
		// 生成认证签名：
		String digest = MD5Digest.encrypt(OrderInfo + merkey);
		return signMsg.equals(digest);
	}

	/**
	 * 得到md5值
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
	public String getHamc(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey) {
		String M_ID = merno;//商户号
		String MODate = DateUtil.fmtDateForBetRecods(DateUtil.getToday());//订单日期
		String MOrderID = billno;//本地订单号
		String MOAmount = NumericUtil.formatDouble(amount);//订单总金额
		String MOCurrency = "1";//人民币支付
		String M_URL = responseUrl;//回调url
		String M_Language = "1";//中文语言
		String State = "0";//支付状态
		return EncryptionUtil.encrypt(M_ID + MODate + MOrderID + MOAmount + MOCurrency + M_URL + M_Language + State);
	}

	public String requestURL(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey,String ip){
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
		String M_ID = merno;// 商户号
		String MODate = DateUtil.fmtDateForBetRecods(DateUtil.getToday());// 订单日期
		String MOrderID = billno;// 本地订单号
		String MOAmount = NumericUtil.formatDouble(amount);// 订单总金额
		String MOCurrency = "1";// 人民币支付
		String M_URL = responseUrl;// 回调url
		String M_Language = "1";// 中文语言
		String State = "0";// 支付状态

		// 以下是初始值
		String S_Name = "name";// 消费者姓名
		String S_Address = "address";// 地址
		String S_PostCode = "518000";// 邮政编码
		String S_Telephone = "13888888888";// 联系电话
		String S_Email = "swaynps@nps.cn";// email
		String R_Name = "rname";// 收货人姓名
		String R_Address = "raddress";// 收货人地址
		String R_PostCode = "518000";// 收货人邮政编码
		String R_Telephone = "13088888888";// 收货人联系电话
		String R_Email = "swaynps@nps.cn";// 收货人email
		String MOComment = "nps";// 备注

		// 组织订单信息
		String m_info = M_ID + "|" + MOrderID + "|" + MOAmount + "|" + MOCurrency + "|" + M_URL + "|" + M_Language;
		String s_info = S_Name + "|" + S_Address + "|" + S_PostCode + "|" + S_Telephone + "|" + S_Email + "|" + R_Name;
		String r_info = R_Address + "|" + R_PostCode + "|" + R_Telephone + "|" + R_Email + "|" + MOComment + "|" + State + "|" + MODate;

		String OrderInfo = m_info + "|" + s_info + "|" + r_info;

		log.info("Nps OrderInfo =" + OrderInfo);
		// 设置密钥
		OrderInfo = xStrEncrypt.StrEncrypt(OrderInfo, merkey);
		// 签名
		String digest = MD5Digest.encrypt(OrderInfo + merkey);

		Map<String, String> params = new HashMap();
		params.put("OrderMessage", OrderInfo);
		params.put("digest", digest);
		params.put("M_ID", M_ID);
		return getURL(puturl, params);
	}

	public static void main(String[] args) {
		// 测试通过
		// Nps nps=new Nps();
		// String url=nps.requestURL("https://payment.nps.cn/ReceiveMerchantAction.do", "11111111111111", "1051018279",100.00, "http://www.google.com/", "qq789abc359451915429");
		// System.out.println(url);
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

}
