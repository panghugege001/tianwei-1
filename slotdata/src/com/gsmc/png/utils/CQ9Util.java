package com.gsmc.png.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gsmc.png.model.CqData;

public class CQ9Util {

	private static Logger log = Logger.getLogger(CQ9Util.class);
	// API Token
	private static String API_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOiI1ZjUzMGEzMTc4MDdhYTAwMDFlOThlYzEiLCJhY2NvdW50IjoidGlhbndlaXl1bGVTdGFnaW5nIiwib3duZXIiOiI1ZDI1NGVmYjAxOTg4ZTAwMDFhNmFkNTEiLCJwYXJlbnQiOiI1ZDI1NGVmYjAxOTg4ZTAwMDFhNmFkNTEiLCJjdXJyZW5jeSI6IkNOWSIsImp0aSI6Ijc0NzA2NjIwMiIsImlhdCI6MTU5OTI3NzYxNywiaXNzIjoiQ3lwcmVzcyIsInN1YiI6IlNTVG9rZW4ifQ.x2lNVPY__JC86d4FTtiiSRMypA8XO9ch_J5EE1id_m4";
	// API URL
	private static String CQ9URL = "http://api.cqgame.games";
	// get bet
	private static String GET_BET_URL = CQ9URL + "/gameboy/order/view";



	/**
	 * 拉取CQ9订单
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<CqData> getbets(String startTime, String endTime) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		GetMethod method = new GetMethod(
				GET_BET_URL + "?starttime=" + startTime + "&endtime=" + endTime + "&page=1&pagesize=500");
		method.setRequestHeader("Authorization", API_TOKEN);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			org.codehaus.jettison.json.JSONObject json = new org.codehaus.jettison.json.JSONObject(result);
			String dataStr = json.getString("data");
			if(StringUtil.isNotEmpty(dataStr)){
				com.alibaba.fastjson.JSONObject dataJson = JSON.parseObject(json.getString("data"));
				JSONArray cqArray = dataJson.getJSONArray("Data");
				List<CqData> list = new ArrayList<>();
				for (int i = 0; i < cqArray.size(); i++) {
					CqData vo = new CqData();
					//订单号
					vo.setBillNo(cqArray.getJSONObject(i).getString("round"));
					vo.setPlatform(cqArray.getJSONObject(i).getString("gamehall"));
					vo.setGameCode(cqArray.getJSONObject(i).getString("gamecode"));
					vo.setGameType(cqArray.getJSONObject(i).getString("gametype"));
					vo.setDeviceType(cqArray.getJSONObject(i).getString("gameplat"));
					vo.setPlayName(cqArray.getJSONObject(i).getString("account"));
					vo.setBalance(cqArray.getJSONObject(i).getDouble("balance"));
					vo.setBetAmount(cqArray.getJSONObject(i).getDouble("bet"));
					vo.setValidBetAmount(cqArray.getJSONObject(i).getDouble("bet"));
					vo.setReturnAmount(cqArray.getJSONObject(i).getDouble("win"));
					vo.setBetTime(DateUtil.convertUTCfu4toUTC8(cqArray.getJSONObject(i).getString("bettime")));
					vo.setSettleTime(DateUtil.convertUTCfu4toUTC8(cqArray.getJSONObject(i).getString("endroundtime")));
					vo.setCreateTime(new Date());
					list.add(vo);
				}
				return list;
			}
		} catch (Exception e) {
			log.error("拉取CQ9订单异常,信息：" + e.getMessage());
		}

		return null;
	}

	/**
	 * 流程：1.建立player 2.login 获取userToken 3.获取游戏连接
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String userToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50IjoiYXVzdGluOTkwIiwib3duZXIiOiI1ZDI1NGVmYjAxOTg4ZTAwMDFhNmFkNTEiLCJjdXJyZW5jeSI6IkNOWSIsInBhcmVudCI6IjVmNTMwYTMxNzgwN2FhMDAwMWU5OGVjMSIsInVzZXJpZCI6IjVmNjRiZDMzN2E3YTE2MDAwMTEzNTRiNiIsIm5pY2tuYW1lIjoiYXVzdGluOTkwIiwidHlwZSI6IlBMQVlFUiIsIm1heGltdW0iOjAsIm1pbmltdW0iOjAsImRlZmF1bHQiOjAsInJvbGUiOiIiLCJ3dXJsIjoiaHR0cDovL3RyYW5zYWN0aW9uOjEzMjQiLCJ3dG9rZW4iOiIiLCJpcCI6IjEwLjAuMC4xNDAiLCJsb2NhdGlvbiI6InRhaXdhbiIsImdhbWV0ZWFtIjoiIiwiQmV0VGhyZXNob2xkcyI6IiIsImp0aSI6IjgyODg2MDMiLCJpYXQiOjE2MDA0MzkxNzYsImlzcyI6IkN5cHJlc3MiLCJzdWIiOiJVc2VyVG9rZW4ifQ.CHvP1ZS2SbtK1J3CX6MnA_h6fZJEyi8FCd41qt7AFRM";

		// System.out.print(getGameUrl(userToken, "105", "mobile", true));
		// System.out.print(getBalance("kavin998"));
		// System.out.print(transferIn("kavin998","guo138688",500.00));
		// System.out.print(transferOut("kavin998","guo138688",300.00));
		// System.out.print(loginOut("kavin998"));

	}

}
