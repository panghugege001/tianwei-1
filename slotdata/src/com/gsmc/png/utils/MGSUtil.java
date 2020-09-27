package com.gsmc.png.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSONArray;
import com.gsmc.png.model.MgData;

import net.sf.json.JSONObject;

public class MGSUtil {

	private static Logger log = Logger.getLogger(MGSUtil.class);

	private static final String MGURL = "https://api.adminserv88.com";

	// 认证
	private static final String LOGIN_URL = MGURL + "/oauth/token";
	// 获取注单
	private static final String GET_BET_URL = MGURL + "/v1/feed/transaction";
	// parentId
	private static final Integer parentId = 205867652;
	// API用户和密码
	private static final String API_USERNAME = "tianweiyule_API";
	private static final String API_PASSWORD = "EvkZ99UzWNjP7zYgss9M";

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

	@SuppressWarnings("rawtypes")
	public static List<MgData> getbets(String startTime, String endTime) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String token = getAuthToken();
		if (StringUtils.isNotEmpty(token)) {
			GetMethod method;
			try {
				method = new GetMethod(
						GET_BET_URL + "?company_id=" + parentId + "&start_time=" + URLEncoder.encode(startTime, "UTF-8")
								+ "&end_time=" + URLEncoder.encode(endTime, "UTF-8") + "&include_transfers=false");
				method.setRequestHeader("Content-Type", "application/json");
				method.setRequestHeader("Authorization", "Bearer " + token);
				method.setRequestHeader("Accept", "application/json ;charset=UTF-8");
				method.setRequestHeader("X-DAS-TX-ID", "TEXT-TX-ID15");
				method.setRequestHeader("X-DAS-CURRENCY", "CNY");
				method.setRequestHeader("X-DAS-TZ", "UTC+8");
				method.setRequestHeader("X-DAS-LANG", "zh_CN");
				method.setRequestHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
				httpClient.executeMethod(method);
				String result = method.getResponseBodyAsString();
				JSONObject js = JSONObject.fromObject(result);
				Boolean isError = js.containsKey("error");
				if (isError == true) {
					return null;
				}
				List<MgData> list = new ArrayList<>();
				net.sf.json.JSONArray feedsArray = js.getJSONArray("data");
				for (int i = 0; i < feedsArray.size(); i++) {
					MgData vo = new MgData();
					vo.setAgentId(feedsArray.getJSONObject(i).getInt("id"));
					//vo.setKeyinfo(feedsArray.getJSONObject(i).getInt("id"));
					JSONObject meta_data = (JSONObject) feedsArray.getJSONObject(i).get("meta_data");
					JSONObject deviceJson = (JSONObject) meta_data.get("context");
					JSONObject mgJson = (JSONObject) meta_data.get("mg");
					vo.setColId(deviceJson.getString("device_type"));
					vo.setMbrCode(feedsArray.getJSONObject(i).getString("account_ext_ref"));
					vo.setTransId(String.valueOf(feedsArray.getJSONObject(i).getInt("id")));
					//游戏appID
					vo.setGameId(Integer.parseInt(feedsArray.getJSONObject(i).getString("application_id")));
					//vo.setTransType(feedsArray.getJSONObject(i).getString("type"));
					vo.setTransType("bet");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
					vo.setTransTime(sdf.parse(feedsArray.getJSONObject(i).getString("transaction_time")));
					vo.setMgsGameId(mgJson.getString("game_id"));
					vo.setMgsActionId(mgJson.getString("action_id"));
					//交易金额
					vo.setAmnt(Double.valueOf(feedsArray.getJSONObject(i).getString("amount")));
					//彩池金额/奖金
					vo.setClrngAmnt(Double.valueOf(feedsArray.getJSONObject(i).getString("pool_amount")));
					vo.setBalance(Double.valueOf(feedsArray.getJSONObject(i).getString("balance")));
					list.add(vo);	
				}
				return list;
			} catch (Exception e) {
				log.error("MG拉取注单信息异常" + e.getMessage());
			}
		}
		return null;

	}

	public static void main(String[] args) throws Exception {
		String startTime = "2020-09-01 17:12:45.000";
		String endTime = "2020-09-17 17:12:45.000";
		System.out.println(getbets(startTime, endTime));
	}

}
