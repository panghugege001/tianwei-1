package com.gsmc.png.utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gsmc.png.model.PgData;

public class PGUtil {

	public static final String KEY = "AUHS&aSs9a89KJ121";

	private static Logger log = Logger.getLogger(PGUtil.class);

	public static String PgSoftPublicDomain = "https://m.pg-redirect.net";

	public static String OPERATOR_TOKEN = "4ca8430214268f15969d2b0b9796cb1f";

	public static String SECRET_KEY = "05a69320f2d464f0960eb2fb1e8c77e2";

	public static String PgSoftAPIDomain = "https://api.pg-bo.me/external";
	// get bet
	public static String GET_BET_URL = PgSoftAPIDomain + "/Bet/v3/GetHistoryForSpecificTimeRange";
	// 玩家账号前缀
	public static String ACCOUNT_KEY = "tw@";
	// 真钱
	public static Integer zq_type = 1;
	// 试玩
	public static Integer sw_type = 2;
	// 锦标赛
	public static Integer jbs_type = 3;

	/**
	 * 获取订单
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<PgData> getbets(String startTime, String endTime) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(GET_BET_URL);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		NameValuePair[] value = { new NameValuePair("operator_token", OPERATOR_TOKEN),
				new NameValuePair("secret_key", SECRET_KEY), new NameValuePair("count", "1500"),
				new NameValuePair("bet_type", "1"),
				new NameValuePair("from_time", String.valueOf(DateUtil.getTime(startTime))),
				new NameValuePair("to_time", String.valueOf(DateUtil.getTime(endTime))) };
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
				throw new Exception("拉取订单失败,返回code信息:" + code + "," + message);
			}
			com.alibaba.fastjson.JSONObject dataJson = JSON.parseObject(json.toString());
			JSONArray pgArray = dataJson.getJSONArray("data");
			List<PgData> list = new ArrayList<>();
			for (int i = 0; i < pgArray.size(); i++) {
				PgData vo = new PgData();
				String playerName = pgArray.getJSONObject(i).getString("playerName");
				//订单号
				vo.setBillNo(pgArray.getJSONObject(i).getString("betId"));
				vo.setPlatform("PG");
				vo.setGameCode(pgArray.getJSONObject(i).getString("gameId"));
				vo.setGameType(1);
				vo.setDeviceType(pgArray.getJSONObject(i).getInteger("platform"));			
				vo.setPlayName(StringUtils.substringAfter(playerName, "tw@"));
				vo.setBetAmount(pgArray.getJSONObject(i).getBigDecimal("betAmount"));
				vo.setValidBetAmount(pgArray.getJSONObject(i).getBigDecimal("betAmount"));
				vo.setWinAmount(pgArray.getJSONObject(i).getBigDecimal("winAmount"));
				vo.setBalanceBefore(pgArray.getJSONObject(i).getBigDecimal("balanceBefore"));
				vo.setBalanceAfter(pgArray.getJSONObject(i).getBigDecimal("balanceAfter"));
				vo.setBetTime(DateUtil.getTimeStampToDateTime(pgArray.getJSONObject(i).getLong("betTime")));
				vo.setSettleTime(DateUtil.getTimeStampToDateTime(pgArray.getJSONObject(i).getLong("rowVersion")));
				vo.setCreateTime(new Date());
				list.add(vo);
			}
			return list;
		} catch (Exception e) {
			log.error("拉取订单失败,返回code信息:" + e.getMessage());
		}
		return null;
	}

	public static void main(String[] args) {
		getbets("2020-09-27 18:45:00","2020-09-27 19:30:00");
	}

}
