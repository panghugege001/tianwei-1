package dfh.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.params.CoreConnectionPNames;

public class SkyUtils {
	
	static Integer group_id=12;
	static String  dspurl="http://203.175.172.186:8383";
	static String  skyToken="283r4h2reh2f2828rher";
	static String  skySecretkey="4B9DECC8107D75CF5786188491E9886F";
	

	public static String CreateGameGroup(){
		String result = "";
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl + "/api/create_group?token=" + skyToken + "&secret_key=" + skySecretkey + "&format=json&currency=8";
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String loginSkyGame(Integer user_id){
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl +"/api/open_session?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&user_id="+user_id+"&balance=0";
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String getSkyMonery(Integer user_id){
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl+"/api/get_balance?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&user_id="+user_id;
			httpClient = HttpUtils.createHttpClientShort();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String changeMonery(Integer user_id,Integer amount)throws Exception{
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl+"/api/change_balance?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&user_id="+user_id+"&amount="+amount;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String getEffectiveBets(Integer user_id, Date startTime,Date endTime){
		SimpleDateFormat yyyyMMdd= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat HH = new SimpleDateFormat("HH");
		SimpleDateFormat mm = new SimpleDateFormat("mm");
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl+"/api/get_stat?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&user_id="+user_id+"&date_from="+yyyyMMdd.format(startTime)+"&h_from="+HH.format(startTime)+"&m_from="+mm.format(startTime)+"&date_to="+yyyyMMdd.format(endTime)+"&h_to="+HH.format(endTime)+"&m_to="+mm.format(endTime);
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String removieUser(Integer user_id){
		String result = null;
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = dspurl+"/api/close_session?token="+skyToken+"&secret_key="+skySecretkey+"&format=json&group_id="+group_id+"&user_id="+user_id+"&reset_balance=0";
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				result = postMethod.getResponseBodyAsString();
				System.out.println("Pt Response:"+result);
			} else {
				System.err.println("Response Code: " + statusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	
	public static void main(String[] args) {
//		System.out.println(CreateGameGroup());
//		System.out.println(loginSkyGame(10001));
//		System.out.println(removieUser(29437));
//		JSONObject jsonObj = JSONObject.fromObject(getSkyMonery(29437));
//		System.out.println(jsonObj);
//		jsonObj = JSONObject.fromObject(changeMonery(29437,-100));
//		System.out.println(jsonObj);
//		
//		jsonObj = JSONObject.fromObject(changeMonery(29437,-100));
//		System.out.println(jsonObj);
		
		//System.out.println(jsonObj.getInt("error"));
//		Integer name = jsonObj.getInt("user_id");
//		String key = jsonObj.getString("balance");
		
	}
}
