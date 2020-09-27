package com.nnti.common.utils;

import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class PGUtil {

	public static final String KEY = "AUHS&aSs9a89KJ121";

	private static Logger log = Logger.getLogger(PGUtil.class);

	public static String PgSoftPublicDomain = "https://m.pg-redirect.net";

	public static String OPERATOR_TOKEN = "4ca8430214268f15969d2b0b9796cb1f";

	public static String SECRET_KEY = "05a69320f2d464f0960eb2fb1e8c77e2";

	public static String PgSoftAPIDomain = "https://api.pg-bo.me/external";
	// get Balance
	public static String GET_BALANCE_URL = PgSoftAPIDomain + "/v3/Cash/GetPlayerWallet";
	// create player account
	public static String CREATE_PLAYER_ACCOUNT_URL = PgSoftAPIDomain + "/v1/Player/Create";
	// transfer in
	public static String TRANSFERIN_URL = PgSoftAPIDomain + "/v3/Cash/TransferIn";
	// transfer out
	public static String TRANSFEROUT_URL = PgSoftAPIDomain + "/v3/Cash/TransferOut";

	// 玩家账号前缀
	public static String ACCOUNT_KEY = "tw@";
	// 真钱
	public static Integer zq_type = 1;
	// 试玩
	public static Integer sw_type = 2;
	// 锦标赛
	public static Integer jbs_type = 3;

	/**
	 * 创建玩家账号
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public static int createPlayerAccount(String account) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(CREATE_PLAYER_ACCOUNT_URL);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		NameValuePair[] value = { new NameValuePair("operator_token", OPERATOR_TOKEN),
				new NameValuePair("secret_key", SECRET_KEY), new NameValuePair("player_name", ACCOUNT_KEY + account),
				new NameValuePair("nickname", account), new NameValuePair("currency", "CNY") };
		int create_account = 1;
		try {
			method.setRequestBody(value);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
			String data = json.getString("data").toString();
			String errorEntity = json.getString("error");
			if (data.equals("null") && !errorEntity.equals("null")) {
				com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("error"));
				String code = obj.getString("code");
				String message = obj.getString("message");
				throw new Exception("会员:" + account + "创建PG账户失败,返回code信息:" + code + "," + message);
			}
			com.alibaba.fastjson.JSONObject accuntJson = JSON.parseObject(json.getString("data"));
			// 1.成功 2.失败
			create_account = accuntJson.getInteger("action_result");
		} catch (Exception e) {
			log.error("会员:" + account + "获取余额失败,返回code信息," + e.getMessage());
			// 0 创建异常
			return 0;
		}
		return create_account;
	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	public static String login(String account, String password, String gameCode, Boolean demoMode) {
		int type = 1;
		if (demoMode == true) {
			// true试玩
			type = 2;
		}
		HttpClient httpClient = HttpUtils.createHttpClient();
		String accountStr = "tw_token_" + ACCOUNT_KEY + account;
		try {
			String operator_player_session = AESUtil.aesEncrypt(accountStr, KEY);
			String url = PgSoftPublicDomain + "/" + gameCode + "/index.html" + "?bet_type=" + type + "&operator_token="
					+ OPERATOR_TOKEN + "&language=zh" + "&operator_player_session="
					+ URLEncoder.encode(operator_player_session, "UTF-8");
			GetMethod method = new GetMethod(url);
			method.setRequestHeader("Content-Type", "application/json");
			int statusCode = httpClient.executeMethod(method);
			if (statusCode == 200) {
				return url;
			}
		} catch (Exception e) {
			log.error("用户:" + account + ",PG老虎机登录异常:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 获取会员余额
	 * 
	 * @param accountNo
	 * @return
	 */
	public static Double getBalance(String accountNo) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(GET_BALANCE_URL);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		NameValuePair[] value = { new NameValuePair("operator_token", OPERATOR_TOKEN),
				new NameValuePair("secret_key", SECRET_KEY),
				new NameValuePair("player_name", ACCOUNT_KEY + accountNo) };
		Double balance = 0.00;
		try {
			method.setRequestBody(value);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
			String data = json.getString("data").toString();
			String errorEntity = json.getString("error");
			if (data.equals("null") && !errorEntity.equals("null")) {
				com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("error"));
				String code = obj.getString("code");
				// 1305 玩家不存在,创建玩家账号
				if (code.equals("1305")) {
					int status = createPlayerAccount(accountNo);
					if (status == 0 || status == 2) {
						throw new Exception("会员:" + accountNo + "创建会员失败,返回值信息:" + status);
					} else {
						// 创建成功,重新回调获取余额
						return getBalance(accountNo);
					}
				}
			}
			com.alibaba.fastjson.JSONObject cash = JSON.parseObject(json.getString("data"));
			balance = cash.getDouble("cashBalance");
		} catch (Exception e) {
			log.error("会员:" + accountNo + "获取余额失败,返回code信息," + e.getMessage());
			return null;
		}
		return balance;
	}

	/**
	 * 充值
	 * 
	 * @param accountNo
	 * @param amount
	 * @return
	 */
	public static String transferIn(String accountNo, Double amount) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(TRANSFERIN_URL);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		NameValuePair[] value = { new NameValuePair("operator_token", OPERATOR_TOKEN),
				new NameValuePair("secret_key", SECRET_KEY), new NameValuePair("player_name", ACCOUNT_KEY + accountNo),
				new NameValuePair("amount", amount.toString()),
				new NameValuePair("transfer_reference", "TRF_IN_PG_" + new Date().getTime()),
				new NameValuePair("currency", "CNY"), };
		try {
			method.setRequestBody(value);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
			String data = json.getString("data");
			String errorEntity = json.getString("error");
			if (data.equals("null") && !errorEntity.equals("null")) {
				com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("error"));
				String code = obj.getString("code");
				String message = obj.getString("message");
				throw new Exception("会员:" + accountNo + "充值失败,返回code信息:" + code + ",message:" + message);
			} else {
				return "SUCCESS";
			}
		} catch (Exception e) {
			log.error("会员:" + accountNo + "充值失败,返回code信息," + e.getMessage());
			return null;
		}
	}
	
	/**
	 * 提款
	 * 
	 * @param accountNo
	 * @param amount
	 * @return
	 */
	public static String transferOut(String accountNo, Double amount) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(TRANSFEROUT_URL);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		NameValuePair[] value = { new NameValuePair("operator_token", OPERATOR_TOKEN),
				new NameValuePair("secret_key", SECRET_KEY), new NameValuePair("player_name", ACCOUNT_KEY + accountNo),
				new NameValuePair("amount", amount.toString()),
				new NameValuePair("transfer_reference", "TRF_OUT_PG_" + new Date().getTime()),
				new NameValuePair("currency", "CNY"), };
		try {
			method.setRequestBody(value);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
			String data = json.getString("data");
			String errorEntity = json.getString("error");
			if (data.equals("null") && !errorEntity.equals("null")) {
				com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("error"));
				String code = obj.getString("code");
				String message = obj.getString("message");
				throw new Exception("会员:" + accountNo + "提款失败,返回code信息:" + code + ",message:" + message);
			} else {
				return "SUCCESS";
			}
		} catch (Exception e) {
			log.error("会员:" + accountNo + "充值失败,返回code信息," + e.getMessage());
			return null;
		}
	}


	public static void main(String[] args) {
		// System.out.print(login("kavin9981", "", "medusa2", false));
		//System.out.print(getBalance("kavin99495"));
		//System.out.print(transferOut("kavin99495",Double.parseDouble("50")));
	}

}
