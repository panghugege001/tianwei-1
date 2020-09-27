package dfh.utils;

import com.alibaba.fastjson.JSON;
import dfh.model.ActivityConfig;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

import java.util.List;

public class MiddleServiceUtil {
	private static Logger log = Logger.getLogger(MiddleServiceUtil.class);
	
// private static String middleUrl = "http://172.16.35.28:18080/withdraw";
// private static String middleUrl = "http://69.172.86.24:8280/withdraw";

	private static String middleUrl_activity = Configuration.getInstance().getValue("middleservice.activity");







	public static List<ActivityConfig> checkStatus(String title, String entrance, Integer level) {
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		NameValuePair le=null;
		try {
			String url = middleUrl_activity + "/checkStatus";
			httpClient = createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			NameValuePair statu = new NameValuePair( "title" , title );
			NameValuePair en = new NameValuePair( "entrance" , entrance );
			if(null!=level){
				le= new NameValuePair( "level" , level.toString() );
				postMethod.setRequestBody( new NameValuePair[]{statu,en,le});
			}else {
				postMethod.setRequestBody(new NameValuePair[]{statu, en});
			}
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String vipFrees = postMethod.getResponseBodyAsString();
				if (vipFrees == null || vipFrees.equals("")) {
					log.info("中间件没有数据响应");
					return null;
				}
				List<ActivityConfig> activityConfigs = JSON.parseArray(vipFrees, ActivityConfig.class);
				return activityConfigs;
			} else {
				log.info("Response Code: " + statusCode);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 消息: " + e.toString());
			return null;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}







	
	public static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", "UTF-8");
		params.setParameter("http.socket.timeout", 240*1000);
		httpclient.setParams(params);
		return httpclient;
	}

}
