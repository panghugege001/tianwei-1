package com.nnti.pay.api.rb;

import com.alibaba.fastjson.JSONObject;
import com.nnti.pay.api.rb.security.AesEncryption;
import com.nnti.pay.api.rb.security.HttpsUtil;
import com.nnti.pay.api.rb.security.MD5Util;
import com.nnti.pay.api.rb.security.TimeUtil;

public class ApiUtil {
	private static final String FORMAT = "json";
	private static final String VERSION = "2.0";

	public static String methodInvoke(String openApiUrl, String appid,
			String session, String secretkey, String method, String data) {
		String result = "";
		try {
			String encryptdata = AesEncryption.Encrypt(data, secretkey,
					secretkey);
			String timestamp = TimeUtil.getTime();
			String signstr = MD5Util.string2MD5(secretkey + appid
					+ encryptdata + FORMAT + method + session + timestamp
					+ VERSION + secretkey);
			JSONObject requestObj = new JSONObject();
			requestObj.put("appid", appid);
			requestObj.put("method", method);
			requestObj.put("session", session);
			requestObj.put("format", FORMAT);
			requestObj.put("data", encryptdata);
			requestObj.put("v", VERSION);
			requestObj.put("timestamp", timestamp);
			requestObj.put("sign", signstr);

			result = HttpsUtil.doSslPost(openApiUrl,
					requestObj.toJSONString(), "utf-8");
		} catch (Exception ex) {
			ex.printStackTrace();
			result = ex.getMessage();
		}
		return result;
	}

	public static String methodInvoke(String openApiUrl, String appid,
			String session, String secretkey, String method, String data,
			int redirectflag) {
		String result = "";
		try {
			String encryptdata = AesEncryption.Encrypt(data, secretkey,
					secretkey);
			String timestamp = TimeUtil.getTime();
			String signstr = MD5Util.string2MD5(secretkey + appid
					+ encryptdata + FORMAT + method + session + timestamp
					+ VERSION + secretkey);
			String getdata = "appid=" + appid + "&method=" + method
					+ "&session=" + session + "&format=" + FORMAT
					+ "&data=" + encryptdata + "&v=" + VERSION + "&timestamp="
					+ timestamp + "&sign=" + signstr + "&redirectflag="
					+ redirectflag;
			return openApiUrl + "?" + getdata;
		} catch (Exception ex) {
			ex.printStackTrace();
			result = ex.getMessage();
		}
		return result;
	}
}
