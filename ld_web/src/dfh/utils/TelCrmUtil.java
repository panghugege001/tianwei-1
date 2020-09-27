package dfh.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * 六合彩
 */
public class TelCrmUtil {
	private static Logger log = Logger.getLogger(TelCrmUtil.class);

	private final static String URl = "http://47.90.12.131:12121/bridge/callctrl";
	
	private final static String toneid = "16";
	private final static String STAF = "K";

	public static String call(String id, String phone,  String code) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String getUrl = URl + "?id="+STAF + id + "&callee=" + phone + "&toneid="+ toneid +"&code=" + code  + "&pwd=no&opt=CLICK_TO_IP_CAPTCHA" ;
		System.out.println(getUrl);
		GetMethod method = new GetMethod(getUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");

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
	
	public static String getResult(String id) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String getUrl = URl + "?id="+STAF + id + "&opt=GET_CAPTCHA_RESULT" ;
		System.out.println(getUrl);
		GetMethod method = new GetMethod(getUrl);
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");

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

	public static void main(String[] args) {
		call("woodytest", "13120975740", "8888");
//		getResult("woodytest");
//		System.out.println(RandomStringUtils.randomNumeric(6));
	}

}
