package dfh.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class TelCrmUtil {
	private static Logger log = Logger.getLogger(TelCrmUtil.class);

	public final static String BL_CS_URl = "http://220.229.225.21:8099";

	public final static String BL_TM_URl = "http://220.229.225.21:8099";
	
	/**
	 * 后台供维护使用 千亿10000 e68:10001 long8:10002
	 * 
	 * @param phone
	 * @param ext_no
	 *            坐席号
	 * @param ivr
	 *            语音id
	 * @param route
	 *            坐席号
	 * @return
	 */
	public static String qunHu(String url,String defaultBlUrl, String phone, String ext_no, String ivr, String route) {
		if(StringUtils.isBlank(phone)){
			return "手机号为空";
		}
		if(Integer.parseInt(ext_no)>=333101 && Integer.parseInt(ext_no)<=333140 ){
			url = "http://203.177.51.163";
		}
		
		if(null == url || "".equals(url)){ 
			url=defaultBlUrl;
		}
		
		if (StringUtils.isNotBlank(url)){
			url = url.trim();
		}
		HttpClient httpClient = HttpUtils.createHttpClientNoTimeout();
		String postUrl = url + "/atstar/index.php/status-op?op=dialivrm&dia_num=" + phone + "&ext_no=" + ext_no + "&ivr="
				+ ivr + "&route=" + route + "&bg=1";
		log.error("qunhu url: "+postUrl);
		PostMethod method = new PostMethod(postUrl);
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
		log.error("qunhu result: " + result);
		if(StringUtils.isNotBlank(result) && StringUtils.equals(result, "+OK")){
			return "客服将为您致电，请您稍等片刻！";
		}else{
			return "BL:"+result;
		}
	}
	
	/**
	 * 电销 一对一
	 * 
	 * @param url
	 * @param serviceId
	 * @param phone
	 * @return
	 */
	public static String sendCustomerCallBl(String url, String serviceId, String phone) {

		if (StringUtils.isBlank(phone)) {
			return "手机号为空";
		}
		long l = Long.parseLong(phone);
		String p = "1" + String.valueOf(l);
		l = Long.parseLong(p) * 11 + 159753;
		HttpClient httpClient = HttpUtils.createHttpClient();
		String getUrl = url + "/atstar/index.php/status-op?op=dialv2&dia_num=" + l + "&ext_no=" + serviceId;
		log.info("比邻" + getUrl);
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
		if (StringUtils.isNotBlank(result) && StringUtils.equals(result, "+OK")) {
			return "客服将为您致电，请您稍等片刻！";
		} else {
			return "BL:" + result;
		}
	}

	public static void main(String[] args) {

		// getResult("Lwoodytest111");//13120957748 13120959046
	}

}
