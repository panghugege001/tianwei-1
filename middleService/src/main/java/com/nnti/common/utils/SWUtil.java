package com.nnti.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class SWUtil extends PlatformConfigUtil  {
	private static Logger log = Logger.getLogger(SWUtil.class);
	//product
	private static String  dspurl="https://api.m27613.com/v1/";
	private static String  bourl="https://bo.gc.skywind-tech.com/v1/";

	//TEST
//	private static String  dspurl="https://api.gcpstg.m27613.com/v1/";
//	private static String  bourl="https://bo.gcpstg.m27613.com/v1/";
	
	
	public static String httpPost(String url, JSONObject data,String token){
		PostMethod method = new PostMethod(url);
		HttpClient httpclient = HttpUtil.createHttpClient();
		method.setRequestHeader("Connection", "close");
		if(token != null){
			method.setRequestHeader("X-ACCESS-TOKEN", token);
		}
		method.setRequestHeader("Accept", "application/json");
		BufferedReader reader = null;
		try {
			if(data != null){
				method.setRequestHeader("Content-Type", "application/json");
				RequestEntity re = new StringRequestEntity(data.toString(), "application/json", "UTF-8");
				method.setRequestEntity(re);
			}
			httpclient.executeMethod(method);
			reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));  
			StringBuffer stringBuffer = new StringBuffer();  
			String str = "";  
			while((str = reader.readLine())!=null){  
			   stringBuffer.append(str);  
			}
			String result = stringBuffer.toString();
			int responseCode = method.getStatusCode();
			log.info("请求的url:" + url);
			log.info("响应代码:" + responseCode);
			log.info("响应报文:"+result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(method!=null){
				method.releaseConnection();
			}
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String httpGet(String url, JSONObject data,String token){
		GetMethod method = new GetMethod(url);
		HttpClient httpclient = HttpUtil.createHttpClient();
		method.setRequestHeader("Connection", "close");
		if(token != null){
			method.setRequestHeader("X-ACCESS-TOKEN", token);
		}
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader("Accept", "application/json");
		BufferedReader reader = null;
		try {
			httpclient.executeMethod(method);
			reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));  
			StringBuffer stringBuffer = new StringBuffer();  
			String str = "";  
			while((str = reader.readLine())!=null){  
				stringBuffer.append(str);  
			}
			String result = stringBuffer.toString();
			int responseCode = method.getStatusCode();
			log.info("请求的url:" + url);
			log.info("响应代码:" + responseCode);
			log.info("响应报文:"+result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally{
			if(method!=null){
				method.releaseConnection();
			}
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String login(String product){
		HashMap<String, String> map = swMap.get(product);
		String secretKey = map.get("secretKey");
		String username = map.get("username");
		String password = map.get("password");
		
		String url = dspurl + "login";
		JSONObject data = new JSONObject();
		data.put("secretKey",secretKey);
		data.put("username",username);
		data.put("password",password);
		
		String result = httpPost(url, data, null);
		if(StringUtil.isBlank(result)){
			return "";
		}else{
			JSONObject json = JSONObject.fromObject(result);
			if (json.containsKey("accessToken")) {
				String accessToken = json.getString("accessToken");
				return accessToken;
			}
			String message = json.getString("message");
			log.info("获取令牌失败,错误信息："+message);
			return "";
		}
	}
	
	public static Boolean createPlayer(String product,String loginname){
		HashMap<String, String> map = swMap.get(product);
		String prefix = map.get("prefix");
		String token =  login(product);
		if("".equals(token)){
			return false;
		}
		String url = dspurl + "players";
		JSONObject data = new JSONObject();
		data.put("code",prefix+loginname);
		data.put("firstName","da");
		data.put("lastName","yun");
		data.put("email",loginname+"@gmail.com");
		data.put("country","CN");
		data.put("currency","CNY");
		data.put("language","zh-cn");
		String result = httpPost(url, data,token);
		if(StringUtil.isBlank(result)){
			return Boolean.FALSE;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			String code = jsonObj.getString("code");
			if ((prefix+loginname).equals(code)) {
				return Boolean.TRUE;
			}
			if ("100".equals(code)) {//表示已经创建过
				return Boolean.TRUE;
			}
			String  message = jsonObj.getString("message");
			log.info(" 创建用户失败，错误信息："+message);
			return Boolean.FALSE;
		}
	}
	
	public static Double getPlayerMoney(String product,String loginname){
		HashMap<String, String> map = swMap.get(product);
		String prefix = map.get("prefix");
		String token =  login(product);
		
		if("".equals(token)){
			return null;
		}
		String url = dspurl + "players/"+prefix+loginname;
		String result = httpGet(url, null,token);
		if(StringUtil.isBlank(result)){
			return null;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			String code = jsonObj.getString("code");
			if ((prefix+loginname).equals(code)) {
				JSONObject balances = (JSONObject) jsonObj.get("balances");
				JSONObject cny = (JSONObject) balances.get("CNY");
				Double main = cny.getDouble("main");
				return main;
			}
			if ("102".equals(code)) {//玩家不存在，则帮忙创建
				createPlayer(product,loginname);
				return 0.0;
			}
			String  message = jsonObj.getString("message");
			log.info(" 获取余额失败，错误信息："+message);
			return null;
		}
	}
	
	/**
	 * 转入游戏
	 * @return
	 */
	public static Boolean transferToSW(String product,String loginname, Double amount,String extTrxId) {
		HashMap<String, String> map = swMap.get(product);
		String prefix = map.get("prefix");
		String token =  login(product);
		
		if("".equals(token)){
			return null;
		}
		String url = dspurl + "payments/transfers/in";
		JSONObject data = new JSONObject();
		data.put("playerCode",prefix+loginname);
		data.put("currency","CNY");
		data.put("amount",amount);
		data.put("extTrxId",extTrxId);
		String result = httpPost(url, data,token);
		if(StringUtil.isBlank(result)){
			return Boolean.FALSE;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("extTrxId")) {
				String  _extTrxId = jsonObj.getString("extTrxId");
				if(_extTrxId.equals(extTrxId)){
					log.info(loginname + " 转入游戏成功：" + jsonObj);
					return Boolean.TRUE;
				}else{
					return Boolean.FALSE;
				}
			}
			String  message = jsonObj.getString("message");
			log.info(loginname + " 转入游戏失败，错误信息：" + message);
			return false;
		}
	}

	/**
	 * 转出游戏
	 * @return
	 */
	public static Boolean tranferFromSW(String product, String loginname, Double amount,String extTrxId) {
		HashMap<String, String> map = swMap.get(product);
		String prefix = map.get("prefix");
		String token =  login(product);
		
		if("".equals(token)){
			return null;
		}
		String url = dspurl + "payments/transfers/out";
		JSONObject data = new JSONObject();
		data.put("playerCode",prefix+loginname);
		data.put("currency","CNY");
		data.put("amount",amount);
		data.put("extTrxId",extTrxId);
		String result = httpPost(url, data,token);
		if(StringUtil.isBlank(result)){
			return Boolean.FALSE;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("extTrxId")) {
				String  _extTrxId = jsonObj.getString("extTrxId");
				if(_extTrxId.equals(extTrxId)){
					log.info(loginname + " 转出游戏成功：" + jsonObj);
					return Boolean.TRUE;
				}else{
					return Boolean.FALSE;
				}
			}
			String  message = jsonObj.getString("message");
			log.info(loginname + " 转出游戏失败，错误信息：" + message);
			return false;
		}
	}
	
	public static void main(String[] args){
		String loginname = "dytest02";
		String product = "dy";
//		System.out.println(login(product));
//		System.out.println(createPlayer(product,loginname));
//		System.out.println(transferToSW(product,loginname,1.0,System.currentTimeMillis()+"1"));
//		System.out.println(tranferFromSW(product,loginname,8.0,System.currentTimeMillis()+"1"));
		System.out.println(getPlayerMoney(product,loginname));
	}
}
