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
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class Tenpay implements NetpayInterfaces {
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
		String cmdno = request.getParameter("cmdno");// 任务代码
		String pay_result = request.getParameter("pay_result");
		String date = request.getParameter("date");
		String transaction_id = request.getParameter("transaction_id");
		String sp_billno = request.getParameter("sp_billno");
		String total_fee = request.getParameter("total_fee");
		String fee_type = request.getParameter("fee_type");
		String attach = request.getParameter("attach");

		String sign = request.getParameter("sign");
		String digest = sign = EncryptionUtil.encrypt(cmdno + pay_result + date + transaction_id + sp_billno + total_fee + fee_type + attach + merkey).toUpperCase();
		return digest.equals(sign);
	}

	public String getHamc(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey) {
		String cmdno = "1";// 业务代码 财付通(1)
		String date = DateUtil.fmtyyyyMMdd(DateUtil.getToday());// 订单日期
		String bargainor_id = merno;// 商户号
		String transaction_id = bargainor_id + date + Constants.NETPAYORDER + billno.substring(billno.length() - 10, billno.length());// 交易号。订单号
		String sp_billno = billno;// 本地订单号
		String total_fee = (amount * 100) + "";//金额以分为单位
		String fee_type = "1";
		String return_url = responseUrl;//回调url

		String sign = EncryptionUtil.encrypt(cmdno + date + bargainor_id + transaction_id + sp_billno + total_fee + fee_type + return_url + merkey);
		return sign;
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
	public String requestURL(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey){
		return requestURL(puturl, billno, merno, amount, responseUrl, merkey, null);
	}

	public String requestURL(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey, String ip) {
		String cmdno = "1";// 业务代码 财付通(1)
		String date = DateUtil.fmtyyyyMMdd(DateUtil.getToday());// 订单日期
		String bank_type = "0";// 银行类型 财付通支付(0)
		String desc = "add";// 商品交易名称
		String purchaser_id = "123456";// 买方的QQ账号
		String bargainor_id = merno;// 商户号
		String transaction_id = bargainor_id + date + Constants.NETPAYORDER + billno.substring(billno.length() - 10, billno.length());// 交易号。订单号
		String sp_billno = billno;// 本地订单号
		String total_fee = (amount * 100) + "";// 金额以分为单位
		String fee_type = "1";
		String return_url = responseUrl;// 回调url
		String attach = "88888888";// 数据包
		String spbill_create_ip = ip;// 用户ip

		String sign = EncryptionUtil.encrypt(cmdno + date + bargainor_id + transaction_id + sp_billno + total_fee + fee_type + return_url + spbill_create_ip + merkey).toUpperCase();

		Map map = new HashMap<String, String>();

		map.put("cmdno", cmdno);
		map.put("date", date);
		map.put("bank_type", bank_type);
		map.put("desc", desc);
		map.put("purchaser_id", purchaser_id);
		map.put("bargainor_id", bargainor_id);
		map.put("transaction_id", transaction_id);
		map.put("sp_billno", sp_billno);
		map.put("total_fee", total_fee);
		map.put("fee_type", fee_type);
		map.put("return_url", return_url);
		map.put("attach", attach);
		map.put("spbill_create_ip", spbill_create_ip);
		map.put("sign", sign);

		return getURL(puturl, map);
	}

}
