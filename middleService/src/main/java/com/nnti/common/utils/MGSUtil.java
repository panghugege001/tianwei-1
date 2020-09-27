package com.nnti.common.utils;

import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.dom4j.Document;
import org.dom4j.Element;
import net.sf.json.JSONObject;

public class MGSUtil {

	private static Logger log = Logger.getLogger(MGSUtil.class);

	private static final String MGURL = "https://api.adminserv88.com";

	private static final String MGMEMBERURL = MGURL + "/https://api.adminserv88.com";
	// 认证
	private static final String LOGIN_URL = MGURL + "/oauth/token";
	// 获取游戏连接
	private static final String GET_GAME_URL = MGURL + "/v1/launcher/item";
	// 获取会员信息
	private static final String GET_MEMBER_ACCOUNT_URL = MGURL + "/v1/account";
	// 创建玩家账号
	private static final String CREATE_PLAYER_ACCOUNT_URL = MGURL + "/v1/account/member";
	// 获取余额
	private static final String GET_BALANCE_URL = MGURL + "/v1/wallet";
	private static final String GET_TRANSFER_URL = MGURL + "/v1/transaction";
	// Auth账户和密码
	private static final String AUTH_USERNAME = "IGZAR_auth";
	private static final String AUTH_PASSWORD = "Af49pKcw8aT2yEfZVjRs";

	// API用户和密码
	private static final String API_USERNAME = "tianweiyule_API";
	private static final String API_PASSWORD = "EvkZ99UzWNjP7zYgss9M";

	// parentId
	private static final Integer parentId = 205867652;

	private static final String j_username = "tianweiZAR";
	private static final String j_password = "2wcM5XVWHQgZeTfN";

	public static String Login(String account, String password, String itemId, String appId, String token,
			String demoMode) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(GET_GAME_URL);
		method.setRequestHeader("Authorization", "Bearer " + token);
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader("X-DAS-TX-ID", "TEXT-TX-ID15");
		method.setRequestHeader("X-DAS-CURRENCY", "CNY");
		method.setRequestHeader("X-DAS-TZ", "UTC+8");
		method.setRequestHeader("X-DAS-LANG", "zh_CN");
		method.setRequestHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		JSONObject json = new JSONObject();
		// 1试玩 0正式
		if (demoMode.equals("1")) {
			json.put("demo", true);
		} else {
			json.put("ext_ref", account);
		}
		json.put("item_id", itemId);
		json.put("app_id", appId);
		JSONObject context = new JSONObject();
		context.put("ip","112.199.92.42");
		context.put("session_key", "xfdf");
		context.put("lang", "zh_CN");
		context.put("user_agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0");
		json.put("login_context", context.toString());
		try {
			RequestEntity re = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
			method.setRequestEntity(re);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject a = new org.codehaus.jettison.json.JSONObject(result);
			if (a.has("error")) {
				// 如果有error返回,查看是否没有创建用户
				// 查询用户信息
				Boolean getUserResult = getUserAccountMsg(account, token);
				// true即已注册
				if (getUserResult == false) {
					// 创建用户
					String createPlayerAccount = createPlayerAccount(account, password, token);
					if (StringUtil.isEmpty(createPlayerAccount)) {
						return null;
					} else {
						return Login(account, password, itemId, appId, token, demoMode);
					}
				}
			}
			return a.getString("data");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 创建玩家账号
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	private static String createPlayerAccount(String userName, String password, String token) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(CREATE_PLAYER_ACCOUNT_URL);
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader("Authorization", "Bearer " + token);
		method.setRequestHeader("X-DAS-TX-ID", "TEXT-TX-ID15");
		method.setRequestHeader("X-DAS-CURRENCY", "CNY");
		method.setRequestHeader("X-DAS-TZ", "UTC+8");
		method.setRequestHeader("X-DAS-LANG", "zh_CN");
		method.setRequestHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		JSONObject data = new JSONObject();
		data.put("parent_id", parentId);
		data.put("username", userName);
		data.put("password", password);
		data.put("ext_ref", userName);
		try {
			RequestEntity re = new StringRequestEntity(data.toString(), "application/json", "UTF-8");
			method.setRequestEntity(re);
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject js = new org.codehaus.jettison.json.JSONObject(result);
			if (!js.has("error")) {
				return "success";
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取会员信息
	 * 
	 * @param userName
	 * @param token
	 * @return
	 */
	private static Boolean getUserAccountMsg(String userName, String token) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		GetMethod method = new GetMethod(GET_MEMBER_ACCOUNT_URL + "?ext_ref=" + userName);
		method.setRequestHeader("Content-Type", "application/json");
		method.setRequestHeader("Authorization", "Bearer " + token);
		method.setRequestHeader("Accept", "application/json ;charset=UTF-8");
		method.setRequestHeader("X-DAS-TX-ID", "TEXT-TX-ID15");
		method.setRequestHeader("X-DAS-CURRENCY", "CNY");
		method.setRequestHeader("X-DAS-TZ", "UTC+8");
		method.setRequestHeader("X-DAS-LANG", "zh_CN");
		method.setRequestHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject js = new org.codehaus.jettison.json.JSONObject(result);
			Boolean isError = js.has("error");
			if (isError == true) {
				return false;
			}
		} catch (Exception e) {
			log.info("获取会员:" + userName + "信息异常");
			log.error(e.getMessage());
		}
		return true;
	}

	/**
	 * 获取认证Token
	 * 
	 * @return
	 */
	public static String getAuthToken() {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(LOGIN_URL);
		method.setRequestHeader("Authorization", "Basic SUdaQVJfYXV0aDpBZjQ5cEtjdzhhVDJ5RWZaVmpScw==");
		method.setRequestHeader("X-DAS-TX-ID", "TEXT-TX-ID15");
		method.setRequestHeader("X-DAS-CURRENCY", "CNY");
		method.setRequestHeader("X-DAS-TZ", "UTC+8");
		method.setRequestHeader("X-DAS-LANG", "zh_CN");
		method.setRequestHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		method.setParameter("grant_type", "password");
		method.setParameter("username", API_USERNAME);
		method.setParameter("password", API_PASSWORD);

		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject js = new org.codehaus.jettison.json.JSONObject(result);
			String accessToken = js.get("access_token").toString();
			return accessToken;
		} catch (Exception e) {
			log.info(e.getMessage());
		}
		return null;
	}

	// 修改玩家密码
	public static Boolean changepassword(String loginname, String oldpassword, String newpassword) {

		return Boolean.FALSE;
	}

	// 修改玩家密码 无旧密码
	public static Boolean changepassword(String mguserid, String newpassword) {

		return Boolean.FALSE;
	}

	/**
	 * 登录游戏 demoMode为true是试玩，为false是真钱
	 * 
	 * @param account
	 * @param password
	 * @param itemId
	 * @param appId
	 * @param lobbyurl
	 * @param demoMode
	 * @return
	 */
	public static String launchGameUrl(String account, String password, String itemId, String appId, String lobbyurl,
			String demoMode) {
		// 获取token
		String token = getAuthToken();
		if (StringUtils.isNotEmpty(token)) {
			// 登录,获取游戏连接
			String result = Login(account, password, itemId, appId, token, demoMode);
			if (StringUtils.isNotBlank(result)) {
				return result;
			}
		}
		return null;
	}

	public static Double getBalance(String accountNo) {
		// MG用户获取余额(1.未注册会员要先创建会员 2.已注册会员直接获取余额)
		String token = getAuthToken();
		if (StringUtils.isNotBlank(token)) {
			// 查看会员是否已注册
			Boolean isCreated = getUserAccountMsg(accountNo, token);
			// false创建会员
			if (isCreated == false) {
				String createUser = createPlayerAccount(accountNo, accountNo, token);
				if (StringUtils.isNotEmpty(createUser)) {
					log.info("创建MG会员账号:" + accountNo);
				}
			}
			HttpClient httpClient = HttpUtils.createHttpClient();
			GetMethod method = new GetMethod(GET_BALANCE_URL + "?account_ext_ref=" + accountNo);
			method.setRequestHeader("Content-Type", "application/json");
			method.setRequestHeader("Authorization", "Bearer " + token);
			method.setRequestHeader("X-DAS-TX-ID", "TEXT-TX-ID14");
			method.setRequestHeader("X-DAS-CURRENCY", "CNY");
			method.setRequestHeader("X-DAS-TZ", "UTC+8");
			method.setRequestHeader("X-DAS-LANG", "zh_CN");
			method.setRequestHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			try {
				httpClient.executeMethod(method);
				String result = method.getResponseBodyAsString();
				org.codehaus.jettison.json.JSONObject js = new org.codehaus.jettison.json.JSONObject(result);
				if (js.has("data")) {
					Double balance = null;
					JSONArray feedsArray = js.getJSONArray("data");
					for (int i = 0; i < feedsArray.length(); i++) {
						org.codehaus.jettison.json.JSONObject sonObject = feedsArray.getJSONObject(i);
						balance = sonObject.getDouble("credit_balance");
					}
					return balance;
				}
			} catch (Exception e) {
				log.info("会员:" + accountNo + ",获取余额异常");
				log.error(e.getMessage());
			}
		}
		return null;
	}

	public static String transferToMG(String loginname, String password, Double amount, String tx_id) {
		/*
		 * String token = Login(loginname, password);
		 * 
		 * String xml = "<mbrapi-changecredit-call "+
		 * "timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
		 * "apiusername=\"apiadmin\" "+ "apipassword=\"apipassword\" "+
		 * "token=\""+token+"\" "+ "product=\"casino\" "+ "operation=\"topup\" "
		 * + "amount=\""+amount+"\" "+ "tx-id=\""+tx_id+"\" "+ "/>";
		 * 
		 * //System.out.println(xml); String result =HttpSend(MGMEMBERURL, xml,
		 * "", "POST"); String status =
		 * parseXml(result,"/mbrapi-changecredit-resp","status");
		 * if("0".equals(status)){ return null; }
		 */
		return "fail";
	}

	public static String tranferFromMG(String loginname, String password, Double amount, String tx_id) {
		/*
		 * String token = Login(loginname, password);
		 * 
		 * String xml = "<mbrapi-changecredit-call "+
		 * "timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
		 * "apiusername=\"apiadmin\" "+ "apipassword=\"apipassword\" "+
		 * "token=\""+token+"\" "+ "product=\"casino\" "+
		 * "operation=\"withdraw\" "+ "amount=\""+amount+"\" "+
		 * "tx-id=\""+tx_id+"\" "+ "/>";
		 * 
		 * //System.out.println(xml); String result =HttpSend(MGMEMBERURL, xml,
		 * "", "POST"); String status =
		 * parseXml(result,"/mbrapi-changecredit-resp","status");
		 * if("0".equals(status)){ return null; }
		 */
		return "fail";
	}

	public static String parseXml(String xml, String path, String value) {
		xml = StringUtils.trimToEmpty(xml);
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element element = (Element) doc.getRootElement().selectSingleNode(path);
			String balance = element.attributeValue(value);
			return balance;
		} else {
			log.info("document parse failed:" + xml);
		}
		return null;
	}

	/**
	 * 转账到MG
	 * 
	 * @param product
	 * @param loginName
	 * @param password
	 * @param giftMoney
	 * @param referenceId
	 * @param referenceId
	 * @return
	 */

	public static String transferInAndOutMG(String product, String accountNo, String password, Double giftMoney,
			String txId, String transferType) {
		String token = getAuthToken();
		if (StringUtils.isNotBlank(token)) {
			// 查看会员是否已注册
			Boolean isCreated = getUserAccountMsg(accountNo, token);
			// false创建会员
			if (isCreated == false) {
				String createUser = createPlayerAccount(accountNo, accountNo, token);
				if (StringUtils.isNotEmpty(createUser)) {
					log.info("创建MG会员账号:" + accountNo);
				}
			}
			HttpClient httpClient = HttpUtils.createHttpClient();
			PostMethod method = new PostMethod(GET_TRANSFER_URL);
			method.setRequestHeader("Content-Type", "application/json");
			method.setRequestHeader("Authorization", "Bearer " + token);
			method.setRequestHeader("X-DAS-TX-ID", txId);
			method.setRequestHeader("X-DAS-CURRENCY", "CNY");
			method.setRequestHeader("X-DAS-TZ", "UTC+8");
			method.setRequestHeader("X-DAS-LANG", "zh_CN");
			method.setRequestHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			try {
				org.codehaus.jettison.json.JSONObject data = new org.codehaus.jettison.json.JSONObject();
				data.put("account_ext_ref", accountNo);
				data.put("category", "TRANSFER");			
				data.put("balance_type", "CREDIT_BALANCE");
				data.put("amount", giftMoney.toString());
				if(transferType.equals("IN")){
					data.put("type", "CREDIT");
					data.put("external_ref", "TW_IN_" + new Date().getTime());
				}else{
					data.put("type", "DEBIT");
					data.put("external_ref", "TW_OUT_" + new Date().getTime());					
				}
				
				org.codehaus.jettison.json.JSONObject obj = new org.codehaus.jettison.json.JSONObject();
				obj.put("description", "账号:" + accountNo + "转账到MG" + giftMoney.toString() + "元");
				obj.put("mypromokey", "promokey:" + accountNo);
				data.put("meta_data", obj);
				JSONArray array = new JSONArray();
				array.put(data);
				RequestEntity entity = new StringRequestEntity(array.toString(), "application/json", "UTF-8");
				method.setRequestEntity(entity);
				httpClient.executeMethod(method);
				String result = method.getResponseBodyAsString();
				org.codehaus.jettison.json.JSONObject js = new org.codehaus.jettison.json.JSONObject(result);
				if (js.has("error")) {
					org.codehaus.jettison.json.JSONObject jb = (org.codehaus.jettison.json.JSONObject) js.get("error");
					String message = jb.get("message").toString();
					log.error(message);
					return message;
				} else {
					if (!js.has("data")) {
						return "FAIL";
					}
				}
			} catch (Exception e) {
				log.info("会员:" + accountNo + "转账异常");
				log.error(e.getMessage());
			}
		}
		return null;
	}

	
	public static void main(String[] args) throws Exception {
		String loginname = "twtest01";
		int itemId = 1222;
		int appId = 1002;
		String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0aWFud2VpeXVsZV9BUEkiLCJjdHgiOjQ1LCJwaWQiOjQ5MzMsImFuIjoidGlhbndlaXl1bGVUZXN0IiwidGlkIjoxLCJjbGllbnRfaWQiOiJJR1pBUl9hdXRoIiwiYXAiOiIxLDEwMDEsMTc0NzUxODQ4LDIwNTg2NzY1MiIsInVpZCI6MjA0ODQyOTMyLCJhdCI6NCwic2NvcGUiOlsiYXVkaXQ6ciIsImxhdW5jaGVyX2l0ZW06ciIsInR4OnIiLCJjb21wbGlhbmNlOnIiLCJhcHBfbmFtZTpyIiwiZXhjaGFuZ2VfcmF0ZXM6ciIsImNhbXBhaWduOnciLCJhcHBfaW5zdGFsbGVkOnIiLCJ1c2VyOnciLCJ3YWxsZXQ6ciIsImNhbXBhaWduOnIiLCJ0b2tlbjp3IiwicmVwb3J0OnIiLCJ1c2VyOnIiLCJhY2NhcHA6ciIsImNhdGVnb3J5OnIiLCJhY2NvdW50OnciLCJpdGVtOnIiLCJ0eDp3IiwiYWNjb3VudDpyIl0sImV4cCI6MTU5OTY1Mjg3NCwiYWlkIjoyMDU4Njc2NTIsInVyIjozLCJqdGkiOiJkZGE2MWI5Mi0wNzZkLTQ0Y2EtYmJkZS01ZGFlYjdiNTY1M2YifQ.SMzGUnzzY-msb6eeArsA37IMP1YDgYzzYsnOkrEWWlY";
		// System.out.print(getBalance("austin100"));
		//System.out.print(transferInAndOutMG("tw", "kavin123", "13232", 1.01, "4345","IN"));

	}


}
