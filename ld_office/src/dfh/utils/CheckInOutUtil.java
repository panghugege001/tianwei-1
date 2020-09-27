package dfh.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;


/**
 * 打卡数据
 * 
 * @author
 * 
 */
public class CheckInOutUtil {

	private static Logger log = Logger.getLogger(CheckInOutUtil.class);
	public static String BUSINESS = "CHECKINOUT_E68";
	public static final String URL = Configuration.getInstance().getValue("kaoqin_url")+"/checkData.php";
	public static String APIKEY = "gdfSRTlkjf!sdfJS56SV@AWEx67a";

	// 校验打卡数据
	public static String checkData(String employeeNo) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("METHOD", "QUERY");
		method.setParameter("BUSINESS", BUSINESS);
		method.setParameter("LOGINNAME", employeeNo);
		String signatureKey = DigestUtils.md5Hex(BUSINESS + "QUERY" + employeeNo + APIKEY);
		method.setParameter("SIGNATURE", signatureKey);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			//org.codehaus.jettison.json.JSONObject a = new org.codehaus.jettison.json.JSONObject(result);
			log.info(employeeNo + "获取打卡记录：" + result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return "获取打卡机数据异常，请联系技术！";
	}
	public static void main(String[] args) {
		checkData("61551");
	}
}