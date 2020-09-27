package com.gsmc.png.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.gsmc.png.model.SwData;

import net.sf.json.JSONObject;

public class SWUtil {
	private static Logger log = Logger.getLogger(SWUtil.class);
	private static String  dspurl="https://api.m27613.com/v1/";
	private static String  bourl="https://bo.gc.skywind-tech.com/v1/";
	private static String  secretKey="7f3fdf8b-09c6-47cf-8553-e7c4c18da812";
	private static String  username="Dayun_user";
	private static String  password="QAZqazQAZ123";
	private static String  prefix="DY8";
	
	
	public static String httpPost(String url, JSONObject data,String token){
		PostMethod method = new PostMethod(url);
		HttpClient httpclient = HttpUtils.createHttpClient();
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
			if(StringUtil.isNotBlank(result)){
				log.info("响应报文长度："+result.length()+",内容："+(result.length() > 20 ?result.substring(0, 20)+"......":""));
			}
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
		HttpClient httpclient = HttpUtils.createHttpClient();
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
			if(StringUtil.isNotBlank(result)){
				log.info("响应报文长度："+result.length()+",内容："+(result.length() > 20 ?result.substring(0, 20)+"......":""));
			}
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

	public static String login(){
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

	public static String getGameInfo(){
		String token =  login();
		if("".equals(token)){
			return null;
		}
		String url = dspurl + "games/info/search";
		String result = httpPost(url, null,token);
		return result;
	}
	
	public static Boolean createPlayer(String loginname){
		String token =  login();
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
	/**
	 * 
	 * @param loginname 玩家账号
	 * @param gameCode	游戏编码
	 * @param playmode	fun:试玩，real:真钱游戏
	 * @return
	 */
	public static String loginGame(String loginname,String gameCode,String playmode){
		String token =  login();
		if("".equals(token)){
			return null;
		}
		String url = dspurl+"players/"+prefix+loginname+"/games/"+gameCode+"?playmode="+playmode;
		String result = httpGet(url, null,token);
		if(StringUtil.isBlank(result)){
			return null;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("url")) {
				String  gameurl = jsonObj.getString("url");
				return gameurl;
			}
			log.info("获取游戏链接地址错误");
			return null;
		}
	}
	
	public static Double getPlayerMoney(String loginname){
		String token =  login();
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
				createPlayer(loginname);
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
	public static Boolean transferToSW(String loginname, Double amount,String extTrxId) {
		String token =  login();
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
	public static Boolean tranferFromSW(String loginname, Double amount,String extTrxId) {
		String token =  login();
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

	@SuppressWarnings("rawtypes")
	public static List<SwData> getPlayerBetByHistory(Integer page,String startTime,String endTime){
		String token =  login();
		if("".equals(token)){
			return null;
		}
		Integer offset = (page-1)*100;
		startTime = DateUtil.UTCToUTC8(startTime);
		endTime = DateUtil.UTCToUTC8(endTime);
		String url = dspurl + "history/game/?bet__gt=0&finished=true&isTest=false&firstTs__gte="+startTime+"&firsts__lt="+endTime+"&offset="+offset+"&limit=100";
		String result = httpGet(url, null,token);
		List list = JSONArray.parseArray(result);
		List<SwData> datas = new ArrayList<SwData>();
		if(list !=null && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Map one = (Map) list.get(i);
				SwData vo = new SwData();
				vo.setRoundId((String)one.get("roundId"));
				vo.setBrandId((String)one.get("brandId"));
				String loginname = (String)one.get("playerCode");
				loginname = loginname.substring(3);
				vo.setPlayerCode(loginname);
				vo.setGameCode((String)one.get("gameCode"));
				vo.setCurrency((String)one.get("currency"));
				
				String win = one.get("win")+"";
				vo.setWin(Double.parseDouble(win));
				
				String bet = one.get("bet")+"";
				vo.setBet(Double.parseDouble(bet));
				String revenue = one.get("revenue").toString();
				vo.setRevenue(Double.parseDouble(revenue));
				
				String ts = (String)one.get("ts");
				ts = DateUtil.UTCStringtODefaultString(ts);
				vo.setTs(ts);
				
				vo.setBalanceBefore((String)one.get("balanceBefore"));
				vo.setBalanceAfter((String)one.get("balanceAfter"));
				vo.setDevice((String)one.get("device"));
				String insertedAt = (String)one.get("insertedAt");
				insertedAt = DateUtil.UTCStringtODefaultString(insertedAt);
				vo.setInsertedAt(insertedAt);
				vo.setTotalEvents((String)one.get("totalEvents"));
				datas.add(vo);
			}
			return datas;
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static List getPlayerBetByReport(String loginname){
		String token =  login();
		if("".equals(token)){
			return null;
		}
		String url = dspurl + "report/players?currency=CNY";
		String result = httpGet(url, null,token);
		List list = JSONArray.parseArray(result);
		return list;
	}

	
	
	public static void main(String[] args){
		String loginname = "dytest02";
//		System.out.println(login());
//		System.out.println(getGameInfo());
//		System.out.println(createPlayer(loginname));
		System.out.println(transferToSW(loginname,1.0,System.currentTimeMillis()+"1"));
		System.out.println(tranferFromSW(loginname,1.0,System.currentTimeMillis()+"1"));
		System.out.println(getPlayerMoney(loginname));
//		System.out.println(loginGame(loginname,"sw_tcb","fun"));
//		System.out.println(getPlayerBetByReport(loginname));
		System.out.println(getPlayerBetByHistory(1,"2018-06-24 19:45:00","2018-06-24 19:48:00"));
	}
}
