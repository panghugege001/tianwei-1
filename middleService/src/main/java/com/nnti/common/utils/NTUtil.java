package com.nnti.common.utils;

import java.util.HashMap;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

public class NTUtil extends PlatformConfigUtil {
	
	private static Logger log = Logger.getLogger(NTUtil.class);
	
	public static Double getMoney(String product, String loginName) {

		HttpClient httpClient = null;
		GetMethod postMethod = null;
		
		try {
			
			HashMap<String, String> map = ntMap.get(product);
			String dspurl = map.get("dspurl");
			String skyToken = map.get("skyToken");
			String skySecretkey = map.get("skySecretkey");
			String group_id = map.get("group_id");
			String startWith = map.get("startWith");
			
			String url = dspurl + "/api/get_balance?token=" + skyToken + "&secret_key=" + skySecretkey + "&format=json&group_id="
					   + group_id + "&software=netent&user_id=" + startWith + loginName;
			
			httpClient = HttpUtil.createHttpClient();
			
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			
			int statusCode = httpClient.executeMethod(postMethod);
			
			if (HttpStatus.SC_OK == statusCode) {
				
				String result = postMethod.getResponseBodyAsString();
//				log.info("result：" + result);
				
				JSONObject json = JSONObject.fromObject(result);
				
				if (null == json.get("error")) {
					
					return NumericUtil.div(json.getDouble("balance"), 100);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.error("执行getMoney方法发生异常，异常信息：" + e.getMessage());
			return null;
		} finally {

			if (null != postMethod) {

				postMethod.releaseConnection();
			}
		}

		return null;
	}
	
	public static String changeMoney(String product, String loginName, Double amount) {
		
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		
		try {
			
			HashMap<String, String> map = ntMap.get(product);
			String dspurl = map.get("dspurl");
			String skyToken = map.get("skyToken");
			String skySecretkey = map.get("skySecretkey");
			String group_id = map.get("group_id");
			String startWith = map.get("startWith");
			
			amount = NumericUtil.mul(amount, 100);
			Integer iamount = amount.intValue();
			
			String url = dspurl + "/api/change_balance?token=" + skyToken + "&secret_key=" + skySecretkey + "&format=json&group_id="
					   + group_id + "&user_id=" + startWith + loginName + "&software=netent&amount=" + iamount;
			
			httpClient = HttpUtil.createHttpClient();
			
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			
			int statusCode = httpClient.executeMethod(postMethod);
			
			if (HttpStatus.SC_OK == statusCode) {
				
				String result = postMethod.getResponseBodyAsString();
//				log.info("result：" + result);
				
				JSONObject json = JSONObject.fromObject(result);
				
				if (null == json.get("error")) {
					
					return null;
				} else {
					
					return String.valueOf(json.get("error"));
				}
			} else {
				
				return String.valueOf(statusCode);
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.error("执行getMoney方法发生异常，异常信息：" + e.getMessage());
			return e.getMessage();
		} finally {

			if (null != postMethod) {

				postMethod.releaseConnection();
			}
		}
	}
	
	public static void main(String [] args) throws Exception {
	
		System.out.println(getMoney("qy", "devtest999"));
		System.out.println(changeMoney("qy", "devtest999", 1.00));
	}
}