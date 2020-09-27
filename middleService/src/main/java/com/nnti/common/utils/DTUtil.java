package com.nnti.common.utils;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DTUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(DTUtil.class);

	// 获取远程游戏金额
	public static String getAmount(String product, String loginName) {

		HttpClient httpClient = null;
		PostMethod method = null;

		try {

			HashMap<String, String> map = dtMap.get(product);
			String URL = map.get("URL");
			String BUSINESS = map.get("BUSINESS");
			String APIKEY = map.get("APIKEY");

			loginName = loginName.toUpperCase();

			String signatureKey = DigestUtils.md5Hex(BUSINESS + "GETAMOUNT" + loginName + APIKEY);

			httpClient = HttpUtil.createHttpClient();

			method = new PostMethod(URL);

			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("METHOD", "GETAMOUNT");
			method.setParameter("BUSINESS", BUSINESS);
			method.setParameter("PLAYERNAME", loginName);
			method.setParameter("SIGNATURE", signatureKey);

			httpClient.executeMethod(method);

			String result = method.getResponseBodyAsString();
//			log.info("玩家" + loginName + "执行getAmount方法，result参数值为：" + result);

			if (StringUtils.isBlank(result)) {

				return null;
			}

			JSONObject jsonObj = JSONObject.fromObject(result);

			if (jsonObj.containsKey("RESPONSECODE")) {

				if ("00000".equalsIgnoreCase(String.valueOf(jsonObj.get("RESPONSECODE")))) {

					return String.valueOf(jsonObj.getString("AMOUNT"));
				} else {

					return null;
				}
			} else {

				return null;
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.info("玩家" + loginName + "执行getAmount方法发生异常，异常信息：" + e.getMessage());

			return null;
		} finally {

			if (method != null) {

				method.releaseConnection();
			}
		}
	}

	// 转出或者转入金额
	public static String withdrawOrDeposit(String product, String loginName, Double price) {

		HttpClient httpClient = null;
		PostMethod method = null;
		String METHOD = null;

		try {

			HashMap<String, String> map = dtMap.get(product);
			String URL = map.get("URL");
			String BUSINESS = map.get("BUSINESS");
			String APIKEY = map.get("APIKEY");

			loginName = loginName.toUpperCase();

			// 如果price的值大于0，则为转入操作，反之为转出操作
			if (price < 0) {

				METHOD = "WITHDRAW";
			} else {

				METHOD = "DEPOSIT";
			}

			price = Math.abs(price);
			String priceStr = String.valueOf(price);

			String signatureKey = DigestUtils.md5Hex(BUSINESS + METHOD + loginName + priceStr + APIKEY);

			httpClient = HttpUtil.createHttpClient();

			method = new PostMethod(URL);

			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("METHOD", METHOD);
			method.setParameter("BUSINESS", BUSINESS);
			method.setParameter("PLAYERNAME", loginName);
			method.setParameter("PRICE", priceStr);
			method.setParameter("SIGNATURE", signatureKey);

			httpClient.executeMethod(method);

			String result = method.getResponseBodyAsString();
//			log.info("玩家" + loginName + "执行withdrawOrDeposit方法，METHOD参数值为：" + METHOD + "，result参数值为：" + result);

			JSONObject jsonObj = JSONObject.fromObject(result);

			if (jsonObj.containsKey("RESPONSECODE")) {

				if ("00000".equalsIgnoreCase(String.valueOf(jsonObj.get("RESPONSECODE")))) {

					if ("WITHDRAW".equals(METHOD)) {

						String status = jsonObj.has("STATUS") == true ? String.valueOf(jsonObj.get("STATUS")) : "0";

						return "success" + status;
					} else if ("DEPOSIT".equals(METHOD)) {

						return "success";
					}

					return "success";
				} else {

					return String.valueOf(jsonObj.get("RESPONSECODE"));
				}
			} else {

				return jsonObj.toString();
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.info("玩家" + loginName + "执行withdrawOrDeposit方法发生异常，异常信息：" + e.getMessage());

			return null;
		} finally {

			if (method != null) {

				method.releaseConnection();
			}
		}
	}

	// 查询指定时间段内玩家的投注额，该方法查询比较慢，可以精确到秒，但是只能查询最近15天的数据，会定期清理数据
	@SuppressWarnings("unchecked")
	public static Double getBetAmount(String product, String loginName, String startTime, String endTime) {

		HttpClient httpClient = null;
		PostMethod method = null;
		String signatureKey = null;
		Double betAmount = 0.0;

		try {

			HashMap<String, String> map = dtMap.get(product);
			String URL = map.get("URL");
			String BUSINESS = map.get("BUSINESS");
			String APIKEY = map.get("APIKEY");

			httpClient = HttpUtil.createHttpClient();

			method = new PostMethod(URL);

			method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			method.setParameter("METHOD", "GETBET");
			method.setParameter("BUSINESS", BUSINESS);
			method.setParameter("START_TIME", startTime);
			method.setParameter("END_TIME", endTime);

			if (StringUtils.isNotBlank(loginName)) {

				loginName = loginName.toUpperCase();
				method.setParameter("PLAYERNAME", loginName);

				signatureKey = DigestUtils.md5Hex(BUSINESS + "GETBET" + loginName + startTime + endTime + APIKEY);
			} else {

				signatureKey = DigestUtils.md5Hex(BUSINESS + "GETBET" + startTime + endTime + APIKEY);
			}

			method.setParameter("SIGNATURE", signatureKey);

			httpClient.executeMethod(method);

			String result = method.getResponseBodyAsString();
//			log.info("玩家" + loginName + "执行getBetAmount方法，result参数值为：" + result);

			JSONObject a = JSONObject.fromObject(result);

			if (a.has("RESPONSECODE")) {

				if (a.get("RESPONSECODE").equals("00000")) {

					JSONObject data = JSONObject.fromObject(a.get("DATA").toString());
					JSONArray jc = JSONArray.fromObject(data.get("BETS").toString());

					if (null != jc && !jc.isEmpty()) {

						Object obj = jc.get(0);
						Map<String, Object> m = (Map<String, Object>) obj;
						String betPrice = String.valueOf(m.get("betPrice"));
						betAmount = Double.parseDouble(betPrice);
					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.info("玩家" + loginName + "执行getBetAmount方法发生异常，异常信息：" + e.getMessage());
		} finally {

			if (method != null) {

				method.releaseConnection();
			}
		}

		return betAmount;
	}

	public static void main(String[] args) throws Exception {
		String loginname = "twtest01";
		String product = "tw";
//		String loginname = "dytest01";
//		String product = "dy";
		System.out.println(withdrawOrDeposit(product, loginname, 1.00));
		System.out.println(getAmount(product, loginname));
//		System.out.println(getBetAmount("tw", "devtest999", "2017-06-06 00:00:00", "2017-06-07 00:00:00"));
	}
}