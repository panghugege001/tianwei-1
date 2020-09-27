package app.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;

import com.google.gson.Gson;

import dfh.security.MySecureProtocolSocketFactory;
import dfh.utils.GsonUtil;
/**
 * app 消息推送
 * @author lin
 *
 */
public class PushUtil {
   
	private static String url = "https://www.nntipush.com/message";
	
	
	/**
	 * 公告
	 * @param content
	 * @param userList
	 * @return
	 */
	public static String pushAnnouncement(String content ,List<String> userList){
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		
		JSONObject params = new JSONObject();
		params.put("source", "ul");
		params.put("targetType", "all");
		params.put("alert",content);
		params.put("type","0200");
		
        String transJson = params.toString();
        
		try {
			
			RequestEntity re = new StringRequestEntity(transJson, "application/json", "UTF-8");
			method.setRequestEntity(re);
			
		    Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
		
		    Protocol.registerProtocol("https", myhttps);  
			 
			int statusCode = client.executeMethod(method);
			
			if (statusCode != HttpStatus.SC_OK) {
				
				System.err.println("Method failed: " + method.getStatusLine());
				return "推送失败【" + method.getStatusLine() + "】";
				
			}else{
				
				byte[] responseBody = method.getResponseBody();
				
				String responseString = new String(responseBody);
				
				JSONObject json = JSONObject.fromObject(responseString);
				
				if(json != null){
					
					if("0000".equals(json.getString("code"))){
						
						return "已推送";
						
					}else{
						
						String errorMsg = json.getString("desc");
						
						return "推送失败【" + errorMsg + "】";
					}
					
				}
				
				
			}
			
		} catch (HttpException e) {
			
			return "推送失败【" + e.getMessage() + "】";
			
		} catch (IOException e) {
			
			return "推送失败【" + e.getMessage() + "】";
			
		}
		
		return "";
		
	}
	/**
	 * 站内信
	 * @param content
	 * @param userList
	 * @return
	 */
	public static String pushStationLetter(String content ,String targetType , List<String> userList){
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		
	    Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
		
	    Protocol.registerProtocol("https", myhttps);  
		
		
		JSONObject params = new JSONObject();
		params.put("source", "ul");
		params.put("targetType",targetType);
		
		if("list".equals(targetType)){
			params.put("sourceUsernameList",userList);
		}
		params.put("alert",content);
		params.put("type","0100");
		
        String transJson = params.toString();
        
		try {
			
			RequestEntity re = new StringRequestEntity(transJson, "application/json", "UTF-8");
			method.setRequestEntity(re);
			
			int statusCode = client.executeMethod(method);
			
			if (statusCode != HttpStatus.SC_OK) {
				
				System.err.println("Method failed: " + method.getStatusLine());
				return "推送失败【" + method.getStatusLine() + "】";
				
			}else{
				
				byte[] responseBody = method.getResponseBody();
				
				String responseString = new String(responseBody);
				
				JSONObject json = JSONObject.fromObject(responseString);
				
				if(json != null){
					
					if("0000".equals(json.getString("code"))){
						
						return "已推送";
						
					}else{
						
						String errorMsg = json.getString("desc");
						
						return "推送失败【" + errorMsg + "】";
					}
					
				}
				
				
			}
			
		} catch (HttpException e) {
			
			return "推送失败【" + e.getMessage() + "】";
			
		} catch (IOException e) {
			
			return "推送失败【" + e.getMessage() + "】";
			
		}
		
		return "";
		
	}
	
	public static void main(String[] args) {
		
		//System.out.println(pushAnnouncement("公告https推送", null));
		System.out.println(pushStationLetter("站内信：恭喜发财","all", null));
		
	}
}