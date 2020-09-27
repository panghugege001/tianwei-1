package dfh.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.model.PlatformData;
import dfh.model.bean.QtBetVo;
/**
 * 新的util-中心钱包模式用。
 * @author Lin
 *
 */
public class QtUtilForTp {
	private static Logger log = Logger.getLogger(QtUtilForTp.class);
	
	private static final String QT_BASE_URL = "https://api.qtplatform.com/";//测试："https://api-int.qtplatform.com";//线上："https://api.qtplatform.com";
	//检索访问令牌
	private static final String ACCESS_TOKEN_URL = QT_BASE_URL + "/v1/auth/token?grant_type=password&response_type=token&username={username}&password={password}";
	//撤销访问令牌
	private static final String REVOKE_TOKEN_URL = QT_BASE_URL + "/v1/auth/token";
	//游戏启动
	private static final String GAME_LAUNCHER_URL = QT_BASE_URL + "/v1/games/{gameId}/launch-url";
	
	//返回结果
	public static final String RESULT_SUCC = "SUCCESS";
	public static final String RESULT_FAIL = "FAIL";
	
	private static final String CURRENCY = "CNY";
	private static final String COUNTRY = "CN";
	private static final String LANG = "zh_CN";
	//试玩//真钱
	public static final String MODE_DEMO = "demo";
	public static final String MODE_REAL = "real";
	//游戏模式
	public static final String CLIENTTYPE_HTML5 = "mobile";
	public static final String CLIENTTYPE_FLASH = "desktop";
	//returnUrl
	private static final String RETURNURL = "http://www.youle88.com";
	private static final String API_USER = "api_dcld";
	private static final String API_PASSWORD = "z4gXuK9Z";
	
	
	private static final String PRODUCT_PREFIX = "k";
	
	/**
	 * 获得游戏地址
	 * @return
	 */
	public static String getGameUrl(String gameId, String playerId, String mode, String clientType, String domain){
		Map<String,String> retuenMap = SlotUtil.getQTLoadingTicket(playerId, gameId);
		String ticket = retuenMap.get("ticket");
		playerId = retuenMap.get("playerName");//带产品前缀的用户名
		System.out.println("ticket = " + ticket + ",playerId = " + playerId);
		String backStr = getAccessToken(API_USER, API_PASSWORD);
		if(!RESULT_FAIL.equals(backStr)){
			String token = backStr;
			backStr = gameLauncher(token, gameId, playerId, CURRENCY, COUNTRY, LANG, mode, clientType, (StringUtils.isNotEmpty(domain)?domain:RETURNURL),ticket);
			if(!RESULT_FAIL.equals(backStr)){
				revokeAccessToken(token);
				return backStr;
			}
			revokeAccessToken(token);
		}
		return RESULT_FAIL;
	}
	
	
	/********************************************************下面是QT的基础API********************************************************************/
	
	/**
	 * 取得访问令牌
	 * @param loginname
	 * @param password
	 * @return
	 */
	public static String getAccessToken(String apiUser, String apiPassword){
		HttpClient httpClient = null;
		PostMethod method = null;
		try {
			String url = ACCESS_TOKEN_URL.replace("{username}", apiUser).replace("{password}", apiPassword);
			httpClient = HttpUtils.createHttpClient();
			method = new PostMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if(statusCode >= 400 && statusCode < 600){
				log.info(apiUser + " 取得令牌失败1：" + phpHtml);
				return RESULT_FAIL;
			}else if(statusCode == HttpStatus.SC_OK){
				if (phpHtml == null || "".equals(phpHtml)) {
					log.info(" QT API 接口出现问题！");
					return RESULT_FAIL;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if(jsonObj.containsKey("access_token")){
					log.info(apiUser + " 取得令牌成功：" + phpHtml);
					return jsonObj.getString("access_token");
				}
			}
			log.info(apiUser + " 取得令牌失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 取得访问令牌消息: " + e.toString());
			return RESULT_FAIL;
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}

	/**
	 * 撤销访问令牌
	 * @return
	 */
	public static String revokeAccessToken(String token) {
		HttpClient httpClient = null;
		DeleteMethod method = null;
		try {
			String url = REVOKE_TOKEN_URL;
			httpClient = HttpUtils.createHttpClient();
			method = new DeleteMethod(url);
			method.setRequestHeader("Authorization", "Bearer " + token);
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if(statusCode >= 400 && statusCode < 600){
				log.info(token + " 令牌撤销失败1：" + phpHtml);
				return RESULT_FAIL;
			}else if(statusCode == HttpStatus.SC_NO_CONTENT){
				log.info(token + " 令牌撤销成功！");
				return RESULT_SUCC;
			}
			log.info(token + " 令牌撤销失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 撤销访问令牌消息: " + e.toString());
			return RESULT_FAIL;
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}

	/**
	 * 游戏启动
	 * @return
	 */
	public static String gameLauncher(String token, String gameId, String playerId, String currency, 
			String country, String lang, String mode, String clientType, String returnUrl,String ticket){
		HttpClient httpClient = null;
		PostMethod method = null;
		try {
			String url = GAME_LAUNCHER_URL.replace("{gameId}", gameId);
			httpClient = HttpUtils.createHttpClient();
			method = new PostMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.setRequestHeader("Authorization", "Bearer " + token);
			Map map = new HashMap();
			map.put("playerId", playerId);
			map.put("currency", currency);
			map.put("country", country);
			map.put("lang", lang);
			map.put("mode", mode);
			map.put("device", clientType);
			map.put("returnUrl", returnUrl);
			map.put("walletSessionId", ticket);
			JSONObject jsonObject = JSONObject.fromObject(map);
			method.setRequestBody(jsonObject.toString());
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if(statusCode >= 400 && statusCode < 600){
				log.info("token:" + token + ", gameId:" + gameId + ", playerId:" + playerId + " 游戏启动失败1：" + phpHtml);
				return RESULT_FAIL;
			}else {//if(statusCode == HttpStatus.SC_CREATED) {	//文档是SC_CREATED，实际返回的是SC_OK
				if (phpHtml == null || "".equals(phpHtml)) {
					log.info(" QT API 接口出现问题！");
					return RESULT_FAIL;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (jsonObj.containsKey("url")) {
					log.info("token:" + token + ", gameId:" + gameId + ", playerId:" + playerId + " 游戏启动成功：" + phpHtml);
					return jsonObj.getString("url");
				}
			}
			log.info("token:" + token + ", gameId:" + gameId + ", playerId:" + playerId + " 游戏启动失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 游戏启动失败 消息: " + e.toString());
			return RESULT_FAIL;
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}
	
	/********************************************************公共辅助方法********************************************************************/
	
	public static void main(String[] args) {
		//System.out.println(getAccessToken("api_e6866", "5vlchoxbt9"));
		//String token = getAccessToken("api_long88", "ay5lqy3nmi");
		//String str = gameList(token);
		//String abcString = gameTransactions(token, "woodytest", "2015-11-30T00:00:00", "2015-11-30T23:59:59", 100, 0);
		
		Map<String, QtBetVo> map = new HashMap<String, QtBetVo>();
		//String abcString = getBetTotal("", "2015-12-02T00:00:00", "2015-12-03T00:00:00", 100, 0, map);
		//System.out.println(str);
		String backStr = QtUtilForTp.getGameUrl("OGS-volcanoeruption", "woodytest", "real", "desktop", "");
		System.out.println(backStr);
	}
	
}
