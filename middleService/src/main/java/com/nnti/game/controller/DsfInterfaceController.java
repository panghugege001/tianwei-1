package com.nnti.game.controller;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nnti.common.controller.BaseController;
import com.nnti.common.utils.AESUtil;

/**
 * 提供第三方接口调用
 * 
 * @author austin
 *
 */
@RestController
public class DsfInterfaceController extends BaseController {
	private static Logger log = Logger.getLogger(DsfInterfaceController.class);

	public static String OPERATOR_TOKEN = "4ca8430214268f15969d2b0b9796cb1f";

	public static String SECRET_KEY = "05a69320f2d464f0960eb2fb1e8c77e2";

	public static String KEY = "AUHS&aSs9a89KJ121";

	/**
	 * PG调用接口登录验证
	 * 
	 * @param traceId
	 * @param operator_token
	 * @param secret_key
	 * @param operator_player_session
	 * @param ip
	 * @param custom_parameter
	 * @param game_id
	 * @return
	 */
	@RequestMapping(value = "/VerifySession", method = { RequestMethod.POST })
	public String VerifySession(@RequestParam(value = "operator_token") String operator_token,
			@RequestParam(value = "secret_key") String secret_key,
			@RequestParam(value = "operator_player_session") String operator_player_session) {
		log.info("开始获取PG验证token,operator_player_session:" + operator_player_session);
		JSONObject errorJson = new JSONObject();
		Map<String, Object> map = new HashMap<String, Object>();
		if (StringUtils.isEmpty(operator_token)) {
			map.put("data", null);
			errorJson.put("code", "1034");
			errorJson.put("message", "token不能为空！");
			map.put("error", errorJson);
			return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
		} else {
			if (!operator_token.equals(OPERATOR_TOKEN)) {
				map.put("data", null);
				errorJson.put("code", "1034");
				errorJson.put("message", "该operate_token非本运营商的！");
				map.put("error", errorJson);
				return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
			}
		}
		if (StringUtils.isEmpty(secret_key)) {
			map.put("data", null);
			errorJson.put("code", "1034");
			errorJson.put("message", "secret_key不能为空！");
			map.put("error", errorJson);
			return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
		} else {
			if (!secret_key.equals(SECRET_KEY)) {
				map.put("data", null);
				errorJson.put("code", "1034");
				errorJson.put("message", "该secret_key非本运营商的！");
				map.put("error", errorJson);
				return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
			}
		}
		if (StringUtils.isEmpty(operator_player_session)) {
			map.put("data", null);
			errorJson.put("code", "1034");
			errorJson.put("message", "operator_token不能为空");
			map.put("error", errorJson);
			return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
		}
		try {
			// 解析operator_player_session
			String playerSession = AESUtil.aesDecrypt(URLDecoder.decode(operator_player_session, "UTF-8"), KEY);
			String[] sessionArry = playerSession.trim().split("_");
			if (sessionArry[0].equals("tw") && sessionArry[1].equals("token")) {			
				String account = sessionArry[2];
				String[] tmp  = account.split("@");
				JSONObject accountJson = new JSONObject();
				accountJson.put("player_name", sessionArry[2]);
				accountJson.put("nickname", tmp[1]);
				accountJson.put("currency", "CNY");
				map.put("data", accountJson);
				map.put("error", null);
			} else {
				map.put("data", null);
				errorJson.put("code", "1034");
				errorJson.put("message", "operator_player_session与原session不匹配");
				map.put("error", errorJson);
			}
		} catch (Exception e) {
			log.error("PG验证异常:" + e.getMessage());
		}
		return JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);
	}

}
