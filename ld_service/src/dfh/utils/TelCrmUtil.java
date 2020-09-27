package dfh.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 六合彩
 */
public class TelCrmUtil {
	private static Logger log = Logger.getLogger(TelCrmUtil.class);

	private final static String URl = "http://47.90.12.131:12121/bridge/callctrl";
	
	private final static String STAF = "K";
	
//	private final static String SMS_URl = "http://60.12.122.197:8080/sms/getSmsInfoSelf.php";

	private final static String SMS_URl = "http://121.58.255.67:8080/sms/getSmsInfoSelf.php";
	
	public static String call(String id, String phone,  String code , String toneid) {
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
	
	/**
	 * 国信呼叫
	 * http://58.96.181.180:12121/bridge/callctrl?callee=${requestScope.encryptPhone}&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller=603
	 * @param serviceId 如：831、832、833
	 * @param phone
	 * @return
	 */
	public static String sendCustomerCallGx(String url , String serviceId , String phone){
		if(StringUtils.isBlank(phone)){
			return "手机号为空";
		}
		long l = Long.parseLong(phone);
		String p = "1" + String.valueOf(l);
		l = Long.parseLong(p) * 11 + 159753;
		HttpClient httpClient = HttpUtils.createHttpClient();
		String getUrl = url + "/bridge/callctrl?callee="+l+"&authtype=auth1&opt=CLICK_TO_IP_DIAL&caller="+serviceId ;
		log.info("国信"+getUrl);
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
		log.info("result-->"+result);
		if(StringUtils.isNotBlank(result) && StringUtils.equals(result, "200")){
			return "客服将为您致电，请您稍等片刻！";
		}else{
			return "GX:"+result;
		}
	}
	
	/**
	 * 比邻http://210.51.190.109/atstar/index.php/status-op?op=dialv2&dia_num=1267583500084&ext_no=888101
	 * @param serviceId
	 * @param phone
	 * @return
	 */
	public static String sendCustomerCallBl(String url , String serviceId , String phone){
		if(StringUtils.isBlank(phone)){
			return "手机号为空";
		}
		long l = Long.parseLong(phone);
		String p = "1" + String.valueOf(l);
		l = Long.parseLong(p) * 11 + 159753;
		HttpClient httpClient = HttpUtils.createHttpClient();
		String getUrl = url+"/atstar/index.php/status-op?op=dialv2&dia_num="+l+"&ext_no="+serviceId ;
		log.info("比邻"+getUrl);
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
		if(StringUtils.isNotBlank(result) && StringUtils.equals(result, "+OK")){
			return "客服将为您致电，请您稍等片刻！";
		}else{
			return "BL:"+result;
		}
	}

	
	public static String getSmsInfo(String phone){
		if(StringUtils.isBlank(phone)){
			return "手机号为空";
		}
		long l = Long.parseLong(phone);
		String p = "1" + String.valueOf(l);
		l = Long.parseLong(p) * 11 + 159753;
		HttpClient httpClient = HttpUtils.createHttpClient();
		String getUrl = SMS_URl ;
		log.info("短信验证"+getUrl);
		PostMethod post = new PostMethod(getUrl);
		post.setRequestBody(new NameValuePair[] {new NameValuePair("phone", phone)});
		
		post.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		post.setRequestHeader("Connection", "close");

		String result = "";
		try {
			httpClient.executeMethod(post);
			result = post.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
		log.info("getSmsInfo-->"+result);
		return result ;
	}

	public static void main(String[] args) {
//		call("Ewoodytest111", "13120957748", "","19");
		getResult("Lwoodytest111");
//		System.out.println(RandomStringUtils.randomNumeric(6));
	}

}
