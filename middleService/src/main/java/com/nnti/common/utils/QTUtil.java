package com.nnti.common.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

public class QTUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(QTUtil.class);

	public static final String RESULT_SUCC = "SUCCESS";
	public static final String RESULT_FAIL = "FAIL";
	// 交易类型可以是CREDIT也可以是DEBIT，CREDIT表明存款到QT钱包，DEBIT表明从QT钱包提款
	private static final String TYPE_CREDIT = "CREDIT";
	private static final String TYPE_DEBIT = "DEBIT";
	private static final String CURRENCY = "CNY";
	// 转账的状态：待定，已完成，已取消
	private static final String STATUS_PENDING = "PENDING";
	private static final String STATUS_COMPLETED = "COMPLETED";
//	private static final String STATUS_CANCELLED = "CANCELLED";

	// 获得玩家余额
	public static String getBalance(String product, String playerId) {

		HashMap<String, String> map = qtMap.get(product);
		String API_USER = map.get("API_USER");
		String API_PASSWORD = map.get("API_PASSWORD");

		String backStr = getAccessToken(product, API_USER, API_PASSWORD);

		if (!RESULT_FAIL.equals(backStr)) {

			String token = backStr;

			backStr = getPlayerBalance(product, token, playerId);

			revokeAccessToken(product, token);

			if (!RESULT_FAIL.equals(backStr)) {

				return backStr;
			}
		}

		return RESULT_FAIL;
	}

	// 转入游戏
	public static String getDepositPlayerMoney(String product, String playerId, Double amount, String referenceId) {

		if (StringUtils.isEmpty(referenceId)) {

			referenceId = UUID.randomUUID().toString();
		}

		HashMap<String, String> map = qtMap.get(product);
		String API_USER = map.get("API_USER");
		String API_PASSWORD = map.get("API_PASSWORD");

		String backStr = getAccessToken(product, API_USER, API_PASSWORD);

		if (!RESULT_FAIL.equals(backStr)) {

			String token = backStr;

			backStr = prepareTransfer(product, token, TYPE_CREDIT, referenceId, playerId, amount, CURRENCY);

			if (!RESULT_FAIL.equals(backStr)) {

				JSONObject jsonObj = JSONObject.fromObject(backStr);
				String transferId = jsonObj.getString("id");

				backStr = completeTransfer(product, token, transferId);

				if (!RESULT_FAIL.equals(backStr)) {

					jsonObj = JSONObject.fromObject(backStr);

					if (transferId.equals(jsonObj.getString("id")) && STATUS_COMPLETED.equals(jsonObj.getString("status"))) {

						revokeAccessToken(product, token);
						return RESULT_SUCC;
					}
				}
			}

			revokeAccessToken(product, token);
		}

		return RESULT_FAIL;
	}

	// 转出游戏
	public static String getWithdrawPlayerMoney(String product, String playerId, Double amount, String referenceId) {

		HashMap<String, String> map = qtMap.get(product);
		String API_USER = map.get("API_USER");
		String API_PASSWORD = map.get("API_PASSWORD");

		String backStr = getAccessToken(product, API_USER, API_PASSWORD);

		if (!RESULT_FAIL.equals(backStr)) {

			String token = backStr;

			backStr = prepareTransfer(product, token, TYPE_DEBIT, referenceId, playerId, amount, CURRENCY);

			if (!RESULT_FAIL.equals(backStr)) {

				JSONObject jsonObj = JSONObject.fromObject(backStr);
				String transferId = jsonObj.getString("id");

				backStr = completeTransfer(product, token, transferId);

				if (!RESULT_FAIL.equals(backStr)) {

					jsonObj = JSONObject.fromObject(backStr);

					if (transferId.equals(jsonObj.getString("id")) && STATUS_COMPLETED.equals(jsonObj.getString("status"))) {

						revokeAccessToken(product, token);

						return RESULT_SUCC;
					}
				}
			}

			revokeAccessToken(product, token);
		}

		return RESULT_FAIL;
	}

	// 准备转账
	@SuppressWarnings("deprecation")
	public static String prepareTransfer(String product, String token, String type, String referenceId, String playerId, Double amount, String currency) {

		HttpClient httpClient = null;
		PostMethod method = null;

		try {

			HashMap<String, String> maps = qtMap.get(product);

			String url = maps.get("PREPARE_TRANSFER_URL");

			httpClient = HttpUtil.createHttpClient();

			method = new PostMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.setRequestHeader("Authorization", "Bearer " + token);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type", type);
			map.put("referenceId", referenceId);
			map.put("playerId", playerId);
			map.put("amount", amount);
			map.put("currency", currency);

			JSONObject jsonObject = JSONObject.fromObject(map);

			method.setRequestBody(jsonObject.toString());

			int statusCode = httpClient.executeMethod(method);

			String phpHtml = method.getResponseBodyAsString();

			if (statusCode >= 400 && statusCode < 600) {

				return RESULT_FAIL;
			} else if (statusCode == HttpStatus.SC_CREATED) {

				if (StringUtils.isEmpty(phpHtml)) {

					log.error("QT API 接口出现问题！");
					return RESULT_FAIL;
				}

				JSONObject jsonObj = JSONObject.fromObject(phpHtml);

				if (jsonObj.containsKey("id") && jsonObj.containsKey("status") && STATUS_PENDING.equals(jsonObj.getString("status"))) {

					return phpHtml;
				}
			}

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

	// 完成交易
	@SuppressWarnings("deprecation")
	public static String completeTransfer(String product, String token, String transferId) {

		HttpClient httpClient = null;
		PutMethod method = null;

		try {

			HashMap<String, String> maps = qtMap.get(product);
			String COMPLETE_TRANSFER_URL = maps.get("COMPLETE_TRANSFER_URL");

			String url = COMPLETE_TRANSFER_URL.replace("{transferId}", transferId);

			httpClient = HttpUtil.createHttpClient();

			method = new PutMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");
			method.setRequestHeader("Authorization", "Bearer " + token);
			method.setRequestBody("{\"status\":\"COMPLETED\"}");

			int statusCode = httpClient.executeMethod(method);

			String phpHtml = method.getResponseBodyAsString();

			if (statusCode >= 400 && statusCode < 600) {

				return RESULT_FAIL;
			} else if (statusCode == HttpStatus.SC_OK) {

				if (StringUtils.isEmpty(phpHtml)) {

					log.error("QT API 接口出现问题！");
					return RESULT_FAIL;
				}

				JSONObject jsonObj = JSONObject.fromObject(phpHtml);

				if (jsonObj.containsKey("id") && jsonObj.containsKey("status") && STATUS_COMPLETED.equals(jsonObj.getString("status"))) {

					return phpHtml;
				}
			}

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

	public static String getPlayerBalance(String product, String token, String playerId) {

		HttpClient httpClient = null;
		GetMethod method = null;

		try {

			HashMap<String, String> map = qtMap.get(product);
			String PLAYER_BALANCE_URL = map.get("PLAYER_BALANCE_URL");

			String url = PLAYER_BALANCE_URL.replace("{playerId}", playerId);

			httpClient = HttpUtil.createHttpClient();

			method = new GetMethod(url);
			method.setRequestHeader("Authorization", "Bearer " + token);

			int statusCode = httpClient.executeMethod(method);

			String phpHtml = method.getResponseBodyAsString();

			if (statusCode >= 400 && statusCode < 600) {

				return RESULT_FAIL;
			} else if (statusCode == HttpStatus.SC_OK) {

				if (StringUtils.isEmpty(phpHtml)) {

					log.error("QT API 接口出现问题！");
					return RESULT_FAIL;
				}

				JSONObject jsonObj = JSONObject.fromObject(phpHtml);

				if (jsonObj.containsKey("amount")) {

					return jsonObj.getString("amount");
				}
			}

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

	// 取得访问令牌
	public static String getAccessToken(String product, String apiUser, String apiPassword) {

		HttpClient httpClient = null;
		PostMethod method = null;

		try {

			HashMap<String, String> map = qtMap.get(product);
			String ACCESS_TOKEN_URL = map.get("ACCESS_TOKEN_URL");

			String url = ACCESS_TOKEN_URL.replace("{username}", apiUser).replace("{password}", apiPassword);

			httpClient = HttpUtil.createHttpClient();

			method = new PostMethod(url);
			method.setRequestHeader("Content-Type", "application/json;charset=utf-8");

			int statusCode = httpClient.executeMethod(method);

			String phpHtml = method.getResponseBodyAsString();

			if (statusCode >= 400 && statusCode < 600) {

				return RESULT_FAIL;
			} else if (statusCode == HttpStatus.SC_OK) {

				if (StringUtils.isEmpty(phpHtml)) {

					log.error("QT API 接口出现问题！");
					return RESULT_FAIL;
				}

				JSONObject jsonObj = JSONObject.fromObject(phpHtml);

				if (jsonObj.containsKey("access_token")) {

					return jsonObj.getString("access_token");
				}
			}

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

	// 撤销访问令牌
	public static String revokeAccessToken(String product, String token) {

		HttpClient httpClient = null;
		DeleteMethod method = null;

		try {

			HashMap<String, String> map = qtMap.get(product);

			String url = map.get("REVOKE_TOKEN_URL");

			httpClient = HttpUtil.createHttpClient();

			method = new DeleteMethod(url);
			method.setRequestHeader("Authorization", "Bearer " + token);

			int statusCode = httpClient.executeMethod(method);

			if (statusCode >= 400 && statusCode < 600) {

				return RESULT_FAIL;
			} else if (statusCode == HttpStatus.SC_NO_CONTENT) {

				return RESULT_SUCC;
			}

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

	public static void main(String[] args) throws Exception {
		String loginname = "dytest01";

		System.out.println(getDepositPlayerMoney("dy", loginname, 10.0, System.currentTimeMillis()+""));
		System.out.println(getWithdrawPlayerMoney("dy", loginname, 9.0, System.currentTimeMillis()+""));
		System.out.println(getBalance("dy", "dytest01"));
	}
}