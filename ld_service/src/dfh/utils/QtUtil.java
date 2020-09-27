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

public class QtUtil {
	private static Logger log = Logger.getLogger(QtUtil.class);
	
	private static final String QT_BASE_URL = "https://api.qtplatform.com";
	//检索访问令牌
	private static final String ACCESS_TOKEN_URL = QT_BASE_URL + "/v1/auth/token?grant_type=password&response_type=token&username={username}&password={password}";
	//撤销访问令牌
	private static final String REVOKE_TOKEN_URL = QT_BASE_URL + "/v1/auth/token";
	//准备转账
	private static final String PREPARE_TRANSFER_URL = QT_BASE_URL + "/v1/fund-transfers";
	//完成转账
	private static final String COMPLETE_TRANSFER_URL = QT_BASE_URL + "/v1/fund-transfers/{transferId}/status";
	//取消交易
	private static final String CANCEL_TRANSFER_URL = QT_BASE_URL + "/v1/fund-transfers/{transferId}/status";
	//检索转账细节
	private static final String TRANSFER_DETAILS_URL = QT_BASE_URL + "/v1/fund-transfers/{transferId}";
	//检索玩家余额
	private static final String PLAYER_BALANCE_URL = QT_BASE_URL + "/v1/wallet/ext/{playerId}";
	//游戏启动
	private static final String GAME_LAUNCHER_URL = QT_BASE_URL + "/v1/games/{gameId}/launch-url";
	//游戏交易记录
	private static final String GAME_TRANSACTIONS_URL = QT_BASE_URL + "/v1/game-transactions?from={fromDateTime}&to={toDateTime}&size={size}&page={page}";//access_token={access_token}&
	//游戏列表
	private static final String GAME_LIST_URL = QT_BASE_URL + "/v1/games";
	//玩家NGR报表
	private static final String PLAYER_NGR_URL = QT_BASE_URL + "/v1/ngr-player?from={fromDateTime}&to={toDateTime}&embed={embed}";
	
	//返回结果
	public static final String RESULT_SUCC = "SUCCESS";
	public static final String RESULT_FAIL = "FAIL";
	//交易类型可以是CREDIT也可以是DEBIT，CREDIT表明存款到QT钱包，DEBIT表明从QT钱包提款
	private static final String TYPE_CREDIT = "CREDIT";
	private static final String TYPE_DEBIT = "DEBIT";
	//转账的状态:待定，已完成，已取消
	private static final String STATUS_PENDING = "PENDING";
	private static final String STATUS_COMPLETED = "COMPLETED";
	private static final String STATUS_CANCELLED = "CANCELLED";
	
	private static final String CURRENCY = "CNY";
	private static final String COUNTRY = "CN";
	private static final String LANG = "zh_CN";
	//试玩//真钱
	public static final String MODE_DEMO = "demo";
	public static final String MODE_REAL = "real";
	//游戏模式
	public static final String CLIENTTYPE_HTML5 = "mobile";
	public static final String CLIENTTYPE_FLASH = "desktop";
//	public static final String CLIENTTYPE_HTML5 = "html5";
//	public static final String CLIENTTYPE_FLASH = "flash";
	//游戏交易类型，例如BET, PAYOUT, ROLLBACK(回滚只针对BET)
	private static final String TYPE_BET = "BET";
	private static final String TYPE_PAYOUT = "PAYOUT";
	private static final String TYPE_ROLLBACK = "ROLLBACK";
	//returnUrl
	private static final String RETURNURL = "http://www.longdu9.cc";
	private static final String API_USER = "api_ld";
	private static final String API_PASSWORD = "cc95f35d";
	
	private static  String API_TOKEN=null;
	
	/**
	 * 玩家账号创建是在完成转账后自动创建，另一种是启动游戏后创建
	 */
	
	/**
	 * 获得玩家余额
	 * @param loginname
	 * @param password
	 * @return
	 */
	public static String getBalance(String playerId) {
//		String backStr = getAccessToken(API_USER, API_PASSWORD);
//		if(!RESULT_FAIL.equals(backStr)){
			String token = API_TOKEN;
			if (token==null) {
				API_TOKEN=getAccessToken(API_USER, API_PASSWORD);
			}
			String backStr = getPlayerBalance(API_TOKEN, playerId);
			if(!RESULT_FAIL.equals(backStr)){
//				revokeAccessToken(token);
				return backStr;
			}
//			revokeAccessToken(token);
//		}
		return RESULT_FAIL;
	}
	
	/**
	 * 转入游戏
	 * @return
	 */
	public static String getDepositPlayerMoney(String playerId, Double amount, String referenceId){
		if(StringUtils.isEmpty(referenceId))
			referenceId = UUID.randomUUID().toString();
//		String backStr = getAccessToken(API_USER, API_PASSWORD);
//		if(!RESULT_FAIL.equals(backStr)){
		String token = API_TOKEN;
		if (token==null) {
			API_TOKEN=getAccessToken(API_USER, API_PASSWORD);
		}
			String backStr = prepareTransfer(token, TYPE_CREDIT, referenceId, playerId, amount, CURRENCY);
			if(!RESULT_FAIL.equals(backStr)){
				JSONObject jsonObj = JSONObject.fromObject(backStr);
				String transferId = jsonObj.getString("id");
				backStr = completeTransfer(token, transferId);
				if(!RESULT_FAIL.equals(backStr)){
					jsonObj = JSONObject.fromObject(backStr);
					if(transferId.equals(jsonObj.getString("id")) && STATUS_COMPLETED.equals(jsonObj.getString("status"))){
//						revokeAccessToken(token);
						return RESULT_SUCC;
					}
				}
//			}
//			revokeAccessToken(token);
		}
		return RESULT_FAIL;
	}
	
	/**
	 * 转出游戏
	 * @return
	 */
	public static String getWithdrawPlayerMoney(String playerId, Double amount, String referenceId){
		if(StringUtils.isEmpty(referenceId))
			referenceId = UUID.randomUUID().toString();
//		String backStr = getAccessToken(API_USER, API_PASSWORD);
//		if(!RESULT_FAIL.equals(backStr)){
		String token = API_TOKEN;
		if (token==null) {
			API_TOKEN=getAccessToken(API_USER, API_PASSWORD);
		}
		String backStr = prepareTransfer(token, TYPE_DEBIT, referenceId, playerId, amount, CURRENCY);
			if(!RESULT_FAIL.equals(backStr)){
				JSONObject jsonObj = JSONObject.fromObject(backStr);
				String transferId = jsonObj.getString("id");
				backStr = completeTransfer(token, transferId);
				if(!RESULT_FAIL.equals(backStr)){
					jsonObj = JSONObject.fromObject(backStr);
					if(transferId.equals(jsonObj.getString("id")) && STATUS_COMPLETED.equals(jsonObj.getString("status"))){
//						revokeAccessToken(token);
						return RESULT_SUCC;
					}
				}
//			}
//			revokeAccessToken(token);
		}
		return RESULT_FAIL;
	}
	
	/**
	 * 获得游戏地址
	 * @return
	 */
	public static String getGameUrl(String gameId, String playerId, String mode, String clientType, String domain){
//		String backStr = getAccessToken(API_USER, API_PASSWORD);
//		if(!RESULT_FAIL.equals(backStr)){
		String token = API_TOKEN;
		if (token==null) {
			API_TOKEN=getAccessToken(API_USER, API_PASSWORD);
		}
		String backStr = gameLauncher(token, gameId, playerId, CURRENCY, COUNTRY, LANG, mode, clientType, (StringUtils.isNotEmpty(domain)?domain:RETURNURL));
			if(!RESULT_FAIL.equals(backStr)){
//				revokeAccessToken(token);
				return backStr;
			}
//			revokeAccessToken(token);
//		}
		return RESULT_FAIL;
	}
	
	/**
	 * 获取投注额信息,playerId为空时查询所有玩家游戏记录
	 * 方式一：根据 游戏交易记录API 查询统计
	 * @param playerId
	 * @param fromDateTime
	 * @param toDateTime
	 * @param size
	 * @param page		从0开始
	 * @param map
	 * @return
	 * [{playerId, bet, payout, rollback},...{playerId, bet, payout, rollback}]
	 * 实际投注额：bet-rollback
	 * 输赢值：实际投注额-payout
	 */
	public static String getBetTotal(String playerId, String fromDateTime, String toDateTime, Integer size, Integer page, Map<String, QtBetVo> map){
		try {
//			String backStr = getAccessToken(API_USER, API_PASSWORD);
//			if(!RESULT_FAIL.equals(backStr)){
				String token = API_TOKEN;
				if (token==null) {
					API_TOKEN=getAccessToken(API_USER, API_PASSWORD);
				}
				String backStr = gameTransactions(token, playerId, fromDateTime, toDateTime, size, page);
				if(!RESULT_FAIL.equals(backStr)){
					JSONObject jsonObj = JSONObject.fromObject(backStr);
					log.info("playerId:" + playerId + ", fromDateTime:" + fromDateTime + ", toDateTime:" + toDateTime 
							+ ", size:" + size + ", page:" + page + ", totalCount:" + jsonObj.get("totalCount"));
					JSONArray jsonArray = jsonObj.getJSONArray("items");
					for(int i=0;i<jsonArray.size();i++){
						JSONObject item = jsonArray.getJSONObject(i);
						String name = item.getString("playerId");
						String type = item.getString("type");
						Double amount = item.getDouble("amount");
						QtBetVo betVo;
						if(map.containsKey(name)){
							betVo = map.get(name);
							if(TYPE_BET.equals(type)){
								betVo.setBet(betVo.getBet()+amount);
							}else if(TYPE_PAYOUT.equals(type)){
								betVo.setPayout(betVo.getPayout()+amount);
							}else if(TYPE_ROLLBACK.equals(type)){
								betVo.setRollback(betVo.getRollback()+amount);
							}
						}else{
							betVo = new QtBetVo(name, 0.0, 0.0, 0.0);
							if(TYPE_BET.equals(type)){
								betVo.setBet(amount);
							}else if(TYPE_PAYOUT.equals(type)){
								betVo.setPayout(amount);
							}else if(TYPE_ROLLBACK.equals(type)){
								betVo.setRollback(amount);
							}
							map.put(name, betVo);
						}
					}
					if(jsonObj.containsKey("links")){
						getBetTotal(playerId, fromDateTime, toDateTime, size, page+1, map);		//递归
					}
					QtBetVo[] a = new QtBetVo[map.size()];
					JSONArray jsonarray = JSONArray.fromObject(Arrays.asList(map.values().toArray(a)));

					revokeAccessToken(token);
					return jsonarray.toString();
				}
//				revokeAccessToken(token);
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RESULT_FAIL;
	}
	
	/**
	 * 获取投注额信息
	 * 方式二：根据 玩家NGR报表API 查询
	 * @param fromDateTime
	 * @param toDateTime
	 * @return
	 */
	public static String getBetTotalByNGR(String fromDateTime, String toDateTime, Map<String, QtBetVo> map){
		try {
//			String backStr = getAccessToken(API_USER, API_PASSWORD);
//			if(!RESULT_FAIL.equals(backStr)){
				String token = API_TOKEN;
				if (token==null) {
					API_TOKEN=getAccessToken(API_USER, API_PASSWORD);
				}
				String backStr = playerNGR(token, fromDateTime, toDateTime);
				if(!RESULT_FAIL.equals(backStr)){
					JSONObject jsonObj = JSONObject.fromObject(backStr);
					log.info("fromDateTime:" + fromDateTime + ", toDateTime:" + toDateTime + ", totalCount:" + jsonObj.get("totalCount"));
					JSONArray jsonArray = jsonObj.getJSONArray("items");
					for(int i=0;i<jsonArray.size();i++){
						JSONObject item = jsonArray.getJSONObject(i);
						String name = item.getString("playerId");
						Double totalBet = item.getDouble("totalBet");
						Double totalPayout = item.getDouble("totalPayout");
						Double totalNGR = item.getDouble("totalNGR");//输赢值
						QtBetVo betVo = new QtBetVo(name, 0.0, 0.0, 0.0);
						betVo.setBet(totalBet);
						betVo.setPayout(totalPayout);
						map.put(name, betVo);
					}
					QtBetVo[] a = new QtBetVo[map.size()];
					JSONArray jsonarray = JSONArray.fromObject(Arrays.asList(map.values().toArray(a)));

					revokeAccessToken(token);
					return jsonarray.toString();
				}
//				revokeAccessToken(token);
//			}
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 准备转账
	 * @return
	 */
	public static String prepareTransfer(String token, String type, String referenceId, String playerId, Double amount, String currency) {
		HttpClient httpClient = null;
		PostMethod method = null;
		try {
			String url = PREPARE_TRANSFER_URL;
			httpClient = HttpUtils.createHttpClient();
			method = new PostMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.setRequestHeader("Authorization", "Bearer " + token);
			Map map = new HashMap();
			map.put("type", type);map.put("referenceId", referenceId);map.put("playerId", playerId);map.put("amount", amount);map.put("currency", currency);
			JSONObject jsonObject = JSONObject.fromObject(map);
			method.setRequestBody(jsonObject.toString());
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if(statusCode >= 400 && statusCode < 600){
				log.info("playerId:" + playerId + ", referenceId:" + referenceId + " 准备转账失败1：" + phpHtml);
				return RESULT_FAIL;
			}else if (statusCode == HttpStatus.SC_CREATED) {
				if(phpHtml == null || "".equals(phpHtml)){
					log.info(" QT API 接口出现问题！");
					return RESULT_FAIL;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if(jsonObj.containsKey("id") && jsonObj.containsKey("status") && STATUS_PENDING.equals(jsonObj.getString("status"))){
					log.info("playerId:" + playerId + ", referenceId:" + referenceId + " 准备转账成功：" + phpHtml);
					return phpHtml;
				}
			}
			log.info("playerId:" + playerId + ", referenceId:" + referenceId + " 准备转账失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 准备转账失败 消息: " + e.toString());
			return RESULT_FAIL;
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}

	/**
	 * 完成交易
	 * @return
	 */
	public static String completeTransfer(String token, String transferId) {
		HttpClient httpClient = null;
		PutMethod method = null;
		try {
			String url = COMPLETE_TRANSFER_URL.replace("{transferId}", transferId);
			httpClient = HttpUtils.createHttpClient();
			method = new PutMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.setRequestHeader("Authorization", "Bearer " + token);
			method.setRequestBody("{\"status\":\"COMPLETED\"}");
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if(statusCode >= 400 && statusCode < 600){
				log.info("token:" + token + ", transferId:" + transferId + " 完成交易失败1：" + phpHtml);
				return RESULT_FAIL;
			}else if(statusCode == HttpStatus.SC_OK) {
				if (phpHtml == null || "".equals(phpHtml)) {
					log.info(" QT API 接口出现问题！");
					return RESULT_FAIL;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (jsonObj.containsKey("id") && jsonObj.containsKey("status") && STATUS_COMPLETED.equals(jsonObj.getString("status"))) {
					log.info("token:" + token + ", transferId:" + transferId + " 完成交易成功：" + phpHtml);
					return phpHtml;
				}
			}
			log.info("token:" + token + ", transferId:" + transferId + " 完成交易失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 完成交易失败 消息: " + e.toString());
			return RESULT_FAIL;
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}
	
	/**
	 * 取消交易
	 * @return
	 */
	public static String cancelTransfer(String token, String transferId) {
		HttpClient httpClient = null;
		PutMethod method = null;
		try {
			String url = CANCEL_TRANSFER_URL.replace("{transferId}", transferId);
			httpClient = HttpUtils.createHttpClient();
			method = new PutMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.setRequestHeader("Authorization", "Bearer " + token);
			method.setRequestBody("{\"status\":\"CANCELLED\"}");
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if(statusCode >= 400 && statusCode < 600){
				log.info("token:" + token + ", transferId:" + transferId + " 取消交易失败1：" + phpHtml);
				return RESULT_FAIL;
			}else if(statusCode == HttpStatus.SC_OK) {
				if (phpHtml == null || "".equals(phpHtml)) {
					log.info(" QT API 接口出现问题！");
					return RESULT_FAIL;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (jsonObj.containsKey("id") && jsonObj.containsKey("status") && STATUS_CANCELLED.equals(jsonObj.getString("status"))) {
					log.info("token:" + token + ", transferId:" + transferId + " 取消交易成功：" + phpHtml);
					return phpHtml;
				}
			}
			log.info("token:" + token + ", transferId:" + transferId + " 取消交易失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 取消交易失败 消息: " + e.toString());
			return RESULT_FAIL;
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}
	
	/**
	 * 检索转账细节
	 * @return
	 */
	public static String getTransferDetails(String token, String transferId) {
		HttpClient httpClient = null;
		GetMethod method = null;
		try {
			String url = TRANSFER_DETAILS_URL.replace("{transferId}", transferId);
			httpClient = HttpUtils.createHttpClient();
			method = new GetMethod(url);
			method.setRequestHeader("Authorization", "Bearer " + token);
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if(statusCode >= 400 && statusCode < 600){
				log.info("token:" + token + ", transferId:" + transferId + " 检索转账细节失败1：" + phpHtml);
				return RESULT_FAIL;
			}else if(statusCode == HttpStatus.SC_OK) {
				if (phpHtml == null || "".equals(phpHtml)) {
					log.info(" QT API 接口出现问题！");
					return RESULT_FAIL;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (jsonObj.containsKey("id") && jsonObj.containsKey("status")) {
					log.info("token:" + token + ", transferId:" + transferId + " 检索转账细节成功：" + phpHtml);
					return phpHtml;
				}
			}
			log.info("token:" + token + ", transferId:" + transferId + " 检索转账细节失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 检索转账细节失败 消息: " + e.toString());
			return RESULT_FAIL;
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}

	/**
	 * 检索玩家余额
	 * @return
	 */
	public static String getPlayerBalance(String token, String playerId) {
		HttpClient httpClient = null;
		GetMethod method = null;
		try {
			String url = PLAYER_BALANCE_URL.replace("{playerId}", playerId);
			httpClient = HttpUtils.createHttpClient();
			method = new GetMethod(url);
			method.setRequestHeader("Authorization", "Bearer " + token);
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if (statusCode==401) {
				API_TOKEN=getAccessToken(API_USER, API_PASSWORD);
				return getPlayerBalance(API_TOKEN,playerId);
				 
			}
			if(statusCode >= 400 && statusCode < 600){
				log.info("token:" + token + ", playerId:" + playerId + " 检索玩家余额失败1：" + phpHtml);
				return RESULT_FAIL;
			}else if(statusCode == HttpStatus.SC_OK) {
				if (phpHtml == null || "".equals(phpHtml)) {
					log.info(" QT API 接口出现问题！");
					return RESULT_FAIL;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (jsonObj.containsKey("amount")){
					log.info("token:" + token + ", playerId:" + playerId + " 检索玩家余额成功：" + phpHtml);
					return jsonObj.getString("amount");
				}
			}
			log.info("token:" + token + ", playerId:" + playerId + " 检索玩家余额失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 检索玩家余额失败 消息: " + e.toString());
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
			String country, String lang, String mode, String clientType, String returnUrl){
		HttpClient httpClient = null;
		PostMethod method = null;
		try {
			String url = GAME_LAUNCHER_URL.replace("{gameId}", gameId);
			httpClient = HttpUtils.createHttpClient();
			method = new PostMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.setRequestHeader("Authorization", "Bearer " + token);
			Map map = new HashMap();
			map.put("playerId", playerId);map.put("currency", currency);map.put("country", country);map.put("lang", lang);map.put("mode", mode);
			map.put("device", clientType);map.put("returnUrl", returnUrl);
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
	
	/**
	 * 游戏交易记录
	 * @return
	 */
	public static String gameTransactions(String token, String playerId, String fromDateTime, String toDateTime, Integer size, Integer page) {
		HttpClient httpClient = null;
		GetMethod method = null;
		try {
			String url = GAME_TRANSACTIONS_URL.replace("{fromDateTime}", fromDateTime).replace("{toDateTime}", toDateTime)
					.replace("{size}", size.toString()).replace("{page}", page.toString());
			if(StringUtil.isNotBlank(playerId)){
				url = url + "&playerId=" + playerId;
			}
			httpClient = HttpUtils.createHttpClient();
			method = new GetMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.setRequestHeader("Authorization", "Bearer " + token);
			method.setRequestHeader("Accept", "application/json");
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if(statusCode >= 400 && statusCode < 600){
				log.info("token:" + token + ", playerId:" + playerId + " 游戏交易失败1：" + phpHtml);
				return RESULT_FAIL;
			}else if(statusCode == HttpStatus.SC_OK) {
				if (phpHtml == null || "".equals(phpHtml)) {
					log.info(" QT API 接口出现问题！");
					return RESULT_FAIL;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (jsonObj.containsKey("totalCount") && jsonObj.containsKey("items")) {
					log.info("token:" + token + ", playerId:" + playerId + " 游戏交易成功：");// + phpHtml
					return phpHtml;
				}
			}
			log.info("token:" + token + ", playerId:" + playerId + " 游戏交易失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 游戏交易失败 消息: " + e.toString());
			return RESULT_FAIL;
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}
	
	/**
	 * 游戏列表
	 * @return
	 */
	public static String gameList(String token) {
		HttpClient httpClient = null;
		GetMethod method = null;
		try {
			String url = GAME_LIST_URL;
			httpClient = HttpUtils.createHttpClient();
			method = new GetMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.setRequestHeader("Authorization", "Bearer " + token);
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if(statusCode >= 400 && statusCode < 600){
				log.info("token:" + token + " 游戏列表失败1：" + phpHtml);
				return RESULT_FAIL;
			}else if(statusCode == HttpStatus.SC_OK) {
				if (phpHtml == null || "".equals(phpHtml)) {
					log.info(" QT API 接口出现问题！");
					return RESULT_FAIL;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (jsonObj.containsKey("totalCount") && jsonObj.containsKey("items")) {
					log.info("token:" + token + " 游戏列表成功：" + phpHtml);
					return phpHtml;
				}
			}
			log.info("token:" + token + " 游戏列表失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 游戏列表失败 消息: " + e.toString());
			return RESULT_FAIL;
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}
	
	/**
	 * 玩家NGR报表
	 * 
	 * {embed}确定什么样的信息应包含在结果中。有效值是项目and/or摘要(items,summary)
	 * @return
	 */
	public static String playerNGR(String token, String fromDateTime, String toDateTime){
		HttpClient httpClient = null;
		GetMethod method = null;
		try {
			String url = PLAYER_NGR_URL.replace("{fromDateTime}", fromDateTime).replace("{toDateTime}", toDateTime).replace("{embed}", "items");
			httpClient = HttpUtils.createHttpClient();
			method = new GetMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.setRequestHeader("Authorization", "Bearer " + token);
			method.setRequestHeader("Accept", "application/json");
			int statusCode = httpClient.executeMethod(method);
			log.info("Response Code: " + statusCode);
			String phpHtml = method.getResponseBodyAsString();
			if(statusCode >= 400 && statusCode < 600){
				log.info("token:" + token + ", fromDateTime:" + fromDateTime + ", toDateTime:" + toDateTime + " 玩家NGR报表失败1：" + phpHtml);
				return RESULT_FAIL;
			}else if(statusCode == HttpStatus.SC_OK) {
				if (phpHtml == null || "".equals(phpHtml)){
					log.info(" QT API 接口出现问题！");
					return RESULT_FAIL;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (jsonObj.containsKey("requestedBy") && jsonObj.containsKey("items")) {
					log.info("token:" + token + ", fromDateTime:" + fromDateTime + ", toDateTime:" + toDateTime + " 玩家NGR报表成功：");// + phpHtml
					return phpHtml;
				}
			}
			log.info("token:" + token + ", fromDateTime:" + fromDateTime + ", toDateTime:" + toDateTime + " 玩家NGR报表失败2：" + phpHtml);
			return RESULT_FAIL;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 玩家NGR报表失败 消息: " + e.toString());
			return RESULT_FAIL;
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}
	
	/********************************************************公共辅助方法********************************************************************/
	public static List<PlatformData> parseBetString(String phpHtml){
		List<PlatformData> list = new ArrayList<PlatformData>();
		JSONArray array = JSONArray.fromObject(phpHtml);
		JSONObject object;
		for(int i = 0; i < array.size(); i++) {
			object = array.getJSONObject(i);
			Double realBet = Arith.sub(object.getDouble("bet"), object.getDouble("rollback"));
			Double payout = object.getDouble("payout");
			PlatformData bean = new PlatformData();
			bean.setLoginname(object.getString("playerId"));
			bean.setBet(realBet);
			bean.setProfit(Arith.sub(realBet, payout));
			list.add(bean);
		}
		return list;
	}
	
}
