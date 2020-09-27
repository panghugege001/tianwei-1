package dfh.utils;

import java.net.URLEncoder;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class CQ9Util {

	private static Logger log = Logger.getLogger(CQ9Util.class);
	// 平台标志
	private static String GAMEHALL = "cq9";
	// API Token
	private static String API_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiI1ZjUzMGEzMTc4MDdhYTAwMDFlOThlYzEiLCJhY2NvdW50IjoidGlhbndlaXl1bGVTdGFnaW5nIiwib3duZXIiOiI1ZDI1NGVmYjAxOTg4ZTAwMDFhNmFkNTEiLCJwYXJlbnQiOiI1ZDI1NGVmYjAxOTg4ZTAwMDFhNmFkNTEiLCJjdXJyZW5jeSI6IkNOWSIsImp0aSI6Ijc0NzA2NjIwMiIsImlhdCI6MTU5OTI3NzYxNywiaXNzIjoiQ3lwcmVzcyIsInN1YiI6IlNTVG9rZW4ifQ.x2lNVPY__JC86d4FTtiiSRMypA8XO9ch_J5EE1id_m4";
	// API URL
	private static String CQ9URL = "http://api.cqgame.games";
	// player
	private static String CREATE_PLAYER_URL = CQ9URL + "/gameboy/player";
	// login
	private static String LOGIN_URL = CQ9URL + "/gameboy/player/login";
	// gameLink
	private static String GAME_URL = CQ9URL + "/gameboy/player/gamelink";
	// get balance
	private static String GET_BALANCE_URL = CQ9URL + "/gameboy/player/balance/";
	// transfer In
	private static String TRANSFER_IN_URL = CQ9URL + "/gameboy/player/deposit";
	// transfer Out
	private static String TRANSFER_OUT_URL = CQ9URL + "/gameboy/player/withdraw";
	// login out
	private static String LOGIN_OUT_URL = CQ9URL + "/gameboy/player/logout";
	// change password
	private static String CHANGE_PASSWORD_URL = CQ9URL + "/gameboy/player/pwd";

	/**
	 * 建立player,创建游戏账号
	 * 
	 * @return
	 */
	public static String player(String account, String password) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(CREATE_PLAYER_URL);
		method.setRequestHeader("Authorization", API_TOKEN);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		NameValuePair[] value = { new NameValuePair("account", account), new NameValuePair("password", password),
				new NameValuePair("nickname", account) };
		try {
			method.setRequestBody(value);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
			String data = json.getString("data");
			com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("status"));
			String code = obj.getString("code");
			String message = obj.getString("message");
			if (StringUtils.isNotEmpty(data) && code.equals("0")) {
				return "SUCCESS";
			}
			log.info("会员:" + account + "创建CQ9账号失败,返回code信息:" + code + "," + message);
		} catch (Exception e) {
			log.error("会员:" + account + "创建CQ9账号失败," + e.getMessage());
		}
		return null;
	}

	/**
	 * 登录
	 * 
	 * @return
	 */
	public static String login(String account, String password) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(LOGIN_URL);
		method.setRequestHeader("Authorization", API_TOKEN);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		NameValuePair[] value = { new NameValuePair("account", account), new NameValuePair("password", password) };
		try {
			method.setRequestBody(value);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
			String data = json.getString("data");
			com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("status"));
			String code = obj.getString("code");
			String message = obj.getString("message");
			if (code.equals("0")) {
				com.alibaba.fastjson.JSONObject userData = JSON.parseObject(json.getString("data"));
				String userToken = userData.getString("usertoken");
				return userToken;
			} else if (code.equals("14")) {
				// 账号或密码错误。1.账号不存在 2.密码已改
				String createPlayer = player(account, password);
				if (StringUtils.isEmpty(createPlayer)) {
					// 修改密码
					String changePwd = changePwd(account, password);
					if (StringUtils.isNotEmpty(changePwd)) {
						login(account, password);
					}
				}
			} else {
				log.info("会员:" + account + "登录CQ9失败,返回code信息:" + code + "," + message);
				return code;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("会员:" + account + "登录CQ9失败" + "," + e.getMessage());
		}
		return null;
	}

	/**
	 * @param accountNo
	 * @param password
	 * @param gameCode
	 * @param type
	 *            类型 mobile 手机 默认值web
	 * @param isApp
	 *            是否APP
	 * @param demoMode
	 *            是否试玩
	 * @return
	 */
	public static String getGameUrl(String accountNo, String password, String gameCode, String type, Boolean isApp,
			String demoMode) {
		/**
		 * 1.登录游戏 2.登录成功则获取游戏链接，如提示用户名或密码错误，先看是否有该用户名，如有则修改登录密码，重新登录
		 */
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(GAME_URL);
		method.setRequestHeader("Authorization", API_TOKEN);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		String isAppOnline = "N";
		if (isApp == true) {
			isAppOnline = "Y";
		}
		String userToken = login(accountNo, password);
		if (StringUtils.isNotBlank(userToken)) {
			NameValuePair[] value = { new NameValuePair("usertoken", userToken),
					new NameValuePair("gamehall", GAMEHALL), new NameValuePair("gamecode", gameCode),
					new NameValuePair("gameplat", type), new NameValuePair("lang", "zh-cn"),
					new NameValuePair("app", isAppOnline) };
			try {
				method.setRequestBody(value);
				httpClient.executeMethod(method);
				String result = method.getResponseBodyAsString();
				org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
				String data = json.getString("data");
				com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("status"));
				String code = obj.getString("code");
				String message = obj.getString("message");
				if (StringUtils.isNotEmpty(data) && code.equals("0")) {
					com.alibaba.fastjson.JSONObject userData = JSON.parseObject(json.getString("data"));
					String gameUrl = userData.getString("url");
					return gameUrl;
				}
				log.info("获取游戏失败,gameCode为" + gameCode + ",返回code信息:" + code + "," + message);
			} catch (Exception e) {
				log.error("获取游戏失败,gameCode为" + gameCode + ",异常信息:" + e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 获取余额
	 * 
	 * @param accountNo
	 * @return
	 */
	public static String changePwd(String accountNo, String password) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(CHANGE_PASSWORD_URL);
		method.setRequestHeader("Authorization", API_TOKEN);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		try {
			NameValuePair[] value = { new NameValuePair("account", accountNo),
					new NameValuePair("password", password) };
			method.setRequestBody(value);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
			String data = json.getString("data");
			com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("status"));
			String code = obj.getString("code");
			String message = obj.getString("message");
			if (StringUtils.isNotEmpty(data) && code.equals("0")) {
				return "SUCCESS";
			}
			log.info("会员:" + accountNo + "更新CQ9密码失败,返回code信息:" + code + "," + message);
		} catch (Exception e) {
			log.error("会员:" + accountNo + "更新CQ9密码失败,异常信息:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 获取余额
	 * 
	 * @param accountNo
	 * @return
	 */
	public static Double getBalance(String accountNo) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		GetMethod method = new GetMethod(GET_BALANCE_URL + accountNo);
		try {
			method.setRequestHeader("Authorization", API_TOKEN);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
			String data = json.getString("data");
			com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("status"));
			String code = obj.getString("code");
			String message = obj.getString("message");
			if (StringUtils.isNotEmpty(data) && code.equals("0")) {
				com.alibaba.fastjson.JSONObject userData = JSON.parseObject(json.getString("data"));
				Double balance = userData.getDouble("balance");
				return balance;
			}
			log.info("会员:" + accountNo + "获取CQ9余额失败,返回code信息:" + code + "," + message);
		} catch (Exception e) {
			log.error("会员:" + accountNo + "获取CQ9余额失败,异常信息:" + e.getMessage());
		}
		return null;
	}

	/**
	 * 转账-充值
	 * 
	 * @param accountNo
	 * @return
	 */
	public static String transferIn(String accountNo, String password, Double amount) {
		String userToken = login(accountNo, password);
		if (StringUtils.isNotBlank(userToken)) {
			HttpClient httpClient = HttpUtils.createHttpClient();
			PostMethod method = new PostMethod(TRANSFER_IN_URL);
			NameValuePair[] value = { new NameValuePair("account", accountNo),
					new NameValuePair("mtcode", "TRF_IN_CQ9_" + new Date().getTime()),
					new NameValuePair("amount", amount.toString()) };
			try {
				method.setRequestHeader("Authorization", API_TOKEN);
				method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				method.setRequestBody(value);
				httpClient.executeMethod(method);
				String result = method.getResponseBodyAsString();
				org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
				String data = json.getString("data");
				com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("status"));
				String code = obj.getString("code");
				String message = obj.getString("message");
				if (StringUtils.isNotEmpty(data) && code.equals("0")) {
					return "success";
				}
				log.info("会员:" + accountNo + ",充入CQ9金额:" + amount + "失败,返回code信息:" + code + "," + message);
			} catch (Exception e) {
				log.error("会员:" + accountNo + ",充入CQ9金额:" + amount + "失败,异常信息:" + e.getMessage());
			}
		}

		return null;
	}

	/**
	 * 转账-充值
	 * 
	 * @param accountNo
	 * @return
	 */
	public static String transferOut(String accountNo, String password, Double amount) {
		String userToken = login(accountNo, password);
		if (StringUtils.isNotBlank(userToken)) {
			HttpClient httpClient = HttpUtils.createHttpClient();
			PostMethod method = new PostMethod(TRANSFER_OUT_URL);
			NameValuePair[] value = { new NameValuePair("account", accountNo),
					new NameValuePair("mtcode", "TRF_OUT_CQ9_" + new Date().getTime()),
					new NameValuePair("amount", amount.toString()) };
			try {
				method.setRequestHeader("Authorization", API_TOKEN);
				method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				method.setRequestBody(value);
				httpClient.executeMethod(method);
				String result = method.getResponseBodyAsString();
				org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
				String data = json.getString("data");
				com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("status"));
				String code = obj.getString("code");
				String message = obj.getString("message");
				if (StringUtils.isNotEmpty(data) && code.equals("0")) {
					return "SUCCESS";
				}
				log.info("会员:" + accountNo + ",CQ9转出金额:" + amount + "元失败,返回code信息:" + code + "," + message);
			} catch (Exception e) {
				log.error("会员:" + accountNo + ",CQ9转出金额:" + amount + "元失败,异常信息:" + e.getMessage());
			}
		}

		return null;
	}

	/**
	 * 退出游戏
	 * 
	 * @param accountNo
	 * @return
	 */
	public static String loginOut(String accountNo) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(LOGIN_OUT_URL);
		NameValuePair[] value = { new NameValuePair("account", accountNo) };
		try {
			method.setRequestHeader("Authorization", API_TOKEN);
			method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			method.setRequestBody(value);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
			com.alibaba.fastjson.JSONObject obj = JSON.parseObject(json.getString("status"));
			String code = obj.getString("code");
			String message = obj.getString("message");
			if (code.equals("0")) {
				return "SUCCESS";
			}
			log.info("会员:" + accountNo + ",退出CQ9游戏" + "失败,返回code信息:" + code + "," + message);
		} catch (Exception e) {
			log.error("会员:" + accountNo + ",退出CQ9游戏" + "失败,异常信息:" + e.getMessage());
		}

		return null;
	}

	/**
	 * 流程：1.建立player 2.login 获取userToken 3.获取游戏连接
	 * 
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		//String userToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50IjoiYXVzdGluOTkwIiwib3duZXIiOiI1ZDI1NGVmYjAxOTg4ZTAwMDFhNmFkNTEiLCJjdXJyZW5jeSI6IkNOWSIsInBhcmVudCI6IjVmNTMwYTMxNzgwN2FhMDAwMWU5OGVjMSIsInVzZXJpZCI6IjVmNjRiZDMzN2E3YTE2MDAwMTEzNTRiNiIsIm5pY2tuYW1lIjoiYXVzdGluOTkwIiwidHlwZSI6IlBMQVlFUiIsIm1heGltdW0iOjAsIm1pbmltdW0iOjAsImRlZmF1bHQiOjAsInJvbGUiOiIiLCJ3dXJsIjoiaHR0cDovL3RyYW5zYWN0aW9uOjEzMjQiLCJ3dG9rZW4iOiIiLCJpcCI6IjEwLjAuMC4xNDAiLCJsb2NhdGlvbiI6InRhaXdhbiIsImdhbWV0ZWFtIjoiIiwiQmV0VGhyZXNob2xkcyI6IiIsImp0aSI6IjgyODg2MDMiLCJpYXQiOjE2MDA0MzkxNzYsImlzcyI6IkN5cHJlc3MiLCJzdWIiOiJVc2VyVG9rZW4ifQ.CHvP1ZS2SbtK1J3CX6MnA_h6fZJEyi8FCd41qt7AFRM";

		String token = "tw_token_kavin998";
		try {
			System.out.print(URLEncoder.encode(AESUtil.aesEncrypt(token, "AUHS&aSs9a89KJ121")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.print(getGameUrl(userToken, "105", "mobile", true));
		// System.out.print(getBalance("kavin998"));
		// System.out.print(transferIn("kavin998","guo138688",500.00));
		// System.out.print(transferOut("kavin998","guo138688",300.00));
		// System.out.print(loginOut("kavin998"));

	}

}
