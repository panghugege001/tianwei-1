package com.gsmc.png.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsmc.png.exception.LiveException;
import com.gsmc.png.exception.LiveNullBodyException;
import com.gsmc.png.model.BgByBetRecordItem;
import com.gsmc.png.model.BgLiveBetRecordItem;
import com.gsmc.png.model.enums.GameKindEnum;
import com.gsmc.png.response.BgBetRecordRep;
import com.gsmc.png.response.BgBetRecordResult;

public class BGUtil {
	private static final Logger logger = LoggerFactory.getLogger(BGUtil.class);
	private static String url = "http://am.bgvip55.com/open-cloud/api";
	private static String bg_prefix = "tw_live_";
	// 代理账号
	private static String agent_account = "test7899";
	// 代理密码
	private static String agent_pwd = "test1234";
	private static String SN = "am00";
	private static String secretKey = "8153503006031672EF300005E5EF6AEF";
	private static int SLEEP_TIME = 5000;
	private static int WHILE_COUNT = 3;
	// 账号不存在
	private static final String USER_NOT_EXISTS = "2213";

	private static <T> T doPost(API api, String id, Map<String, String> params) throws Exception {
		String resp = null;
		Map<String, Object> postData = new HashMap<>();
		postData.put("id", id);
		postData.put("method", api.getMethod());
		postData.put("params", params);
		postData.put("jsonrpc", "2.0");
		resp = HttpClientUtils.doPost(url, NewJsonUtil.toJson(postData), null);
		if (StringUtil.isBlank(resp)) {
			throw new LiveNullBodyException(api.getMethod(), params, resp);
		}
		BgBetRecordRep result = NewJsonUtil.toObject(resp, BgBetRecordRep.class);
		if (result == null) {
			throw new LiveNullBodyException(api.getMethod(), params, resp);
		}
		if (result.getError() != null) {
			Map<String, String> errorMap = NewJsonUtil.toMapObject(NewJsonUtil.toJson(result.getError()));
			throw new LiveException(errorMap.get("code"), errorMap.get("message"), api.getMethod(), params, resp);
		}
		return (T) result;
	}
	
	  public static BgBetRecordResult getBetRecord(String code,Date startDate, Date endDate) throws Exception{
	    String random = UUID.randomUUID().toString();
	    String secretCode = HashUtil.sha1Base64(agent_pwd);
	    String digest = HashUtil.md5Hex(random + SN + secretCode);
	    String startTime = DateUtil.dateToYMDHMS(DateUtil.getUSToAMES(startDate));
	    String endTime = DateUtil.dateToYMDHMS(DateUtil.getUSToAMES(endDate));
	    Map<String,String> params = new HashMap<>();
	    params.put("random",random);
	    params.put("digest",digest);
	    params.put("sn",SN);
	    params.put("agentLoginId",agent_account);
	    params.put("startTime",startTime);
	    params.put("endTime",endTime);
	    params.put("pageIndex",String.valueOf(1));
	    params.put("pageSize",String.valueOf(500));
	    BgBetRecordRep bgBetRecordRep = null;
	    BgBetRecordResult result;
	    API api = null;
	    if(GameKindEnum.LIVE.getCode().equals(code)) {
	      api = API.VIDEO_ORDER_QUERY;
	    } else {
	      params.put("gameType","1");
	      api = API.FISH_ORDER_QUERY;
	    }
	    bgBetRecordRep = doPost(api,random,params);
	    ObjectMapper mapper = new ObjectMapper();
	    if(GameKindEnum.LIVE.getCode().equals(code)) {
	      result = mapper.readValue(JsonUtil.toJson(bgBetRecordRep.getResult()), new TypeReference<BgBetRecordResult<BgLiveBetRecordItem>>() {});
	    } else {
	      result = mapper.readValue(JsonUtil.toJson(bgBetRecordRep.getResult()), new TypeReference<BgBetRecordResult<BgByBetRecordItem>>() {});
	    }
	    return result;
	  }

	private enum API {
		TRANSFER_IN("BG转入", "open.balance.transfer"), TRANSFER_OUT("BG转出", "open.balance.transfer"), BALANCE_GET(
				"BG查询账户余额", "open.balance.get"), USER_GET("BG查询会员是否存在", "open.user.get"), USER_CREATE("BG会员注册",
						"open.user.create"), VIDEO_GAME_URL("BG视讯游戏进入", "open.video.game.url"), VIDEO_TRIAL_GAME_URL(
								"BG视讯游戏试玩进入", "open.video.trial.game.url"), VIDEO_ORDER_QUERY("BG视讯游戏注单查询",
										"open.order.agent.query"), VIDEO_GAME_RESULT("BG视讯查看游戏结果",
												"open.sn.video.order.detail.url.get"), FISH_URL("BG捕鱼进入",
														"open.game.bg.fishing.url"), FISH_TRIAL_URL("BG捕鱼试玩进入",
																"open.game.bg.fishing.trial.url"), FISH_ORDER_QUERY(
																		"BG捕鱼注单查询",
																		"open.order.bg.fishing.agent.query"), FISH_GAME_RESULT(
																				"BG捕鱼查看游戏结果",
																				"open.sn.fish.order.detail.url.get");

		private String name;
		private String method;

		API(String name, String method) {
			this.name = name;
			this.method = method;
		}


		public String getMethod() {
			return method;
		}
	}

	public static void main(String[] args) {
		System.out.print("start...............");
	/*	try {
			BgBetRecordResult bgBetRecordResult = getBetRecord("LIVE",
					DateUtil.parseDateForYYYYmmDDHHSS("2020-10-01 15:30:00"), DateUtil.parseDateForYYYYmmDDHHSS("2020-10-01 15:59:00"));
			System.out.print(bgBetRecordResult);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
