package dfh.utils;



import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class SmsPwdUtils {

	public static final String URL = Configuration.getInstance().getValue("sms_url");
	
	public static boolean sendSms(String phoneNum, String text){
		
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("phone", phoneNum);
		method.setParameter("content", text);
		method.setParameter("comPort", "6");
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			result = result.replace("\\u0027", "'");
			if(result.contains("200") && result.contains("发送成功")){
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if(method!=null){
				method.releaseConnection();
			}
		}
	}
	public static void main(String[] args) {

//		System.out.println(SmsPwdUtils.sendSms("09177085018","您的验证码为：xiexielaozong"));
//		System.out.println(StringUtil.getRandomCharacter(6));
	}

}
