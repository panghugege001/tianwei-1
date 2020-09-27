package com.nnti.common.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import net.sf.json.JSONObject;

public class EBETUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(EBETUtil.class);

	//
//	private static final String BASEURL = "http://47.90.23.45:8888";
	//
	private static final String GETBALANCEURL = "/api/userinfo";
	//
	private static final String TRANSFERURL = "/api/recharge";
	//
	private static final String TRANSFERCONFIRMURL = "/api/rechargestatus";

	// 获取玩家账号余额
	public static Double getBalance(String product, String loginName) {

		try {

			Map<String, String> map = ebetMap.get(product);
			String BASEURL = map.get("BASEURL");
			String DEFAULT_CHANNEL_ID = map.get("DEFAULT_CHANNEL_ID");

			long timeStamp = System.currentTimeMillis();

			Map<String, Object> valuesMap = new HashMap<String, Object>();

			valuesMap.put("timestamp", String.valueOf(timeStamp));
			valuesMap.put("channelId", DEFAULT_CHANNEL_ID);
			valuesMap.put("username", loginName);
			valuesMap.put("signature", sign(product, loginName + timeStamp));

			NameValuePair[] paramData = build(valuesMap);

			String result = query(BASEURL, GETBALANCEURL, paramData);

			Map<String, Object> resultMap = convertFromJsonString(result);

			if (String.valueOf(resultMap.get("status")).equals("200")) {

				return Double.parseDouble(String.valueOf(resultMap.get("money")));
			}
		} catch (Exception e) {

			log.error("执行getBalance方法发生异常，异常信息：" + e.getMessage());
			return null;
		}

		return null;
	}

	// 转入账户
	public static Boolean transferInAccount(String product, String loginName, Double credit, String transferId) {

		return transferEbetApp(product, loginName, credit, transferId);
	}

	// 转出账户
	public static Boolean transferOutAccount(String product, String loginName, Double credit, String transferId) {

		return transferEbetApp(product, loginName, credit * -1, transferId);
	}

	public static boolean transferEbetApp(String product, String loginName, Double credit, String transferId) {

		try {

			Map<String, String> map = ebetMap.get(product);
			String BASEURL = map.get("BASEURL");
			String DEFAULT_CHANNEL_ID = map.get("DEFAULT_CHANNEL_ID");

			long timeStamp = System.currentTimeMillis();

			Map<String, Object> valuesMap = new HashMap<String, Object>();

			valuesMap.put("timestamp", timeStamp);
			valuesMap.put("channelId", DEFAULT_CHANNEL_ID);
			valuesMap.put("username", loginName);
			valuesMap.put("money", String.valueOf(credit));
			valuesMap.put("rechargeReqId", transferId);
			valuesMap.put("signature", sign(product, loginName + timeStamp));

			NameValuePair[] paramData = build(valuesMap);

			String result = query(BASEURL, TRANSFERURL, paramData);
			log.info("玩家" + loginName + "执行transferEbetApp方法，result参数值为：" + result);

			Map<String, Object> resultMap = convertFromJsonString(result);

			if (String.valueOf(resultMap.get("rechargeReqId")).equals(transferId) && String.valueOf(resultMap.get("status")).equals("200")) {

				// 当调用转账接口后，该笔订单有可能会延迟，直接调用确认接口，status的值有可能返回为0(0代表为充值中)，所以需要特殊处理，使当前线程等待五秒，保证调用确认接口返回正确的status值
				Thread.sleep(5000);

				return transferConfirm(product, loginName, transferId);
			}
		} catch (Exception e) {

			log.error("执行transferEbetApp方法发生异常，异常信息：" + e.getMessage());
			return false;
		}

		return false;
	}

	private static boolean transferConfirm(String product, String loginName, String transferId) {

		try {

			Map<String, String> map = ebetMap.get(product);
			String BASEURL = map.get("BASEURL");
			String DEFAULT_CHANNEL_ID = map.get("DEFAULT_CHANNEL_ID");

			Map<String, Object> valuesMap = new HashMap<String, Object>();

			valuesMap.put("channelId", DEFAULT_CHANNEL_ID);
			valuesMap.put("username", loginName);
			valuesMap.put("rechargeReqId", transferId);
			valuesMap.put("signature", sign(product, transferId));

			NameValuePair[] paramData = build(valuesMap);

			String result = query(BASEURL, TRANSFERCONFIRMURL, paramData);
			log.info("玩家" + loginName + "执行transferConfirm方法，result参数值为：" + result);

			Map<String, Object> resultMap = convertFromJsonString(result);

			List<String> list = Arrays.asList(new String[] { "200", "0" });

			if (String.valueOf(resultMap.get("rechargeReqId")).equals(transferId) && list.contains(String.valueOf(resultMap.get("status")))) {

				return true;
			}
		} catch (Exception e) {

			log.error("执行transferConfirm方法发生异常，异常信息：" + e.getMessage());
			return false;
		}

		return false;
	}

	private static NameValuePair[] build(Map<String, Object> valuesMap) throws Exception {

		List<NameValuePair> pairList = new ArrayList<NameValuePair>();

		Iterator<Entry<String, Object>> iterator = valuesMap.entrySet().iterator();

		while (iterator.hasNext()) {

			Entry<String, Object> entry = iterator.next();

			pairList.add(new NameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
		}

		NameValuePair[] paramData = pairList.toArray(new NameValuePair[pairList.size()]);

		return paramData;
	}

	private static String sign(String product, String data) throws Exception {

		Map<String, String> map = ebetMap.get(product);
		String PRIVATEKEY = map.get("PRIVATEKEY");

		byte[] keyBytes = Base64.decodeBase64(PRIVATEKEY.getBytes());
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(priKey);
		signature.update(data.getBytes());

		return new String(Base64.encodeBase64(signature.sign()));
	}

	private static String query(String baseUrl, String queryUri, NameValuePair[] paramData) throws Exception {

		HttpClient httpClient = HttpUtil.createHttpClient();

		PostMethod method = new PostMethod(baseUrl + queryUri);

		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestBody(paramData);

		httpClient.executeMethod(method);

		String result = method.getResponseBodyAsString();

		return result;
	}

	private static Map<String, Object> convertFromJsonString(String result) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		JSONObject jsonObject = JSONObject.fromObject(result);

		resultMap.put("status", jsonObject.getInt("status"));

		if (jsonObject.has("userId")) {

			resultMap.put("userId", jsonObject.getString("userId"));
		}
		if (jsonObject.has("loginname")) {

			resultMap.put("loginname", jsonObject.getString("loginname"));
		}
		if (jsonObject.has("channelId")) {

			resultMap.put("channelId", jsonObject.getInt("channelId"));
		}
		if (jsonObject.has("money")) {

			resultMap.put("money", jsonObject.getInt("money"));
		}
		if (jsonObject.has("timestamp")) {

			resultMap.put("timestamp", jsonObject.getInt("timestamp"));
		}
		if (jsonObject.has("rechargeReqId")) {

			resultMap.put("rechargeReqId", jsonObject.getString("rechargeReqId"));
		}

		return resultMap;
	}

	public static void main(String[] args) throws Exception {

		System.out.println(getBalance("lh", "devtest999"));
//		System.out.println(transferInAccount("qy", "devtest999", 11.0, "123456789"));
//		System.out.println(transferOutAccount("qy", "devtest999", 20.0, "223456789"));
	}
}