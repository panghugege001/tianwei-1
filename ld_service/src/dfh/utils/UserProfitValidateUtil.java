package dfh.utils;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class UserProfitValidateUtil {
	private static Logger log = Logger.getLogger(UserProfitValidateUtil.class);
	
	private final static String  url = "http://123.1.186.208:9999/api/gamebet";
	private final static String  product = "e68";
	
	public static Map<String , Double> getProfitData(String username, String starttime , String endtime) {
		GetMethod method = null ;
		Map<String , Double> map = new HashMap<String, Double>();
		try {
			HttpClient httpClient = new HttpClient();
			HttpClientParams params = new HttpClientParams();
			params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(0, false));
			params.setParameter("http.protocol.content-charset", "UTF-8");
			params.setParameter("http.socket.timeout", 10000);
			httpClient.setParams(params);
			String data  = "gameaccount="+username+"&starttime="+starttime+"&endtime="+endtime+"&producttype="+product;
			method = new GetMethod(url+"?"+data);
			method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
			method.setRequestHeader("Connection", "close");
			String result = "";
				httpClient.executeMethod(method);
				result = method.getResponseBodyAsString();
			log.info(result);
			if(StringUtils.equals(result, "null") || StringUtils.isBlank(result)){
				return null ;
			}
			map.put("profit", Double.valueOf(JSONObject.fromObject(result).get("TotalGameProfit").toString())) ;
			map.put("bet", Double.valueOf(JSONObject.fromObject(result).get("TotalGameBet").toString())) ;
			return map;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return null ;
	}
	
	public static void main(String[] args) {
		System.out.println(getProfitData("emma", "2016-02-01%2000:00:00", "2016-03-21%2023:59:59"));;
//		String startStrDate = "2016-03-01 00:00:00";
//		System.out.println(startStrDate.subSequence(0, 10) + "%20" + startStrDate.subSequence(11, startStrDate.length()));
	}

}
