package com.nnti.common.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.nnti.common.security.EncryptionUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class BBinUtils {

	private static Logger log = Logger.getLogger(BBinUtils.class);

	private static String dspurl = "http://linkapi.tcy789.net";
	private static String loginurl = "https://888.tcy789.net";
	private static String website = "avia";
	private static String uppername = "dtianweiyule";
	private static String createMemberKeyB = "3QcgFxyY0";
	private static String createSessionKeyB = "fV98jAu";
	private static String loginKeyB = "fV98jAu";
	private static String PlayGameKeyB = "05Rz1lv";
	private static String checkUsrBalanceKeyB = "7pxyd9c0a";
	private static String transferKeyB = "10WyHdOdZ";
	private static String prefix = "k";

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @return
	 */
	private static String sendPost(String url) {

		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod med = new PostMethod(url);
		med.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		try {
			httpClient.executeMethod(med);
			String result = med.getResponseBodyAsString();
			int responseCode = med.getStatusCode();
			log.info("请求的url:" + url);
			log.info("响应代码:" + responseCode);
			log.info("响应报文:" + result);
			if (responseCode != 200) {
				return null;
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			log.info("发送请求异常:" + e.getMessage());
		}
		return null;
	}


	public static String CreateMember(String loginname) {
		String key = StringUtil.getRandomString(7)
				+ EncryptionUtil.encryptPassword(website + prefix + loginname + createMemberKeyB + getUsEastTime())
				+ StringUtil.getRandomString(1);
		String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername + "&key="
				+ key;
		String url = dspurl + "/app/WebService/JSON/display.php/CreateMember" + params;
		String result = sendPost(url);
		return result;
	}

	public static String Login(String loginname, String gameKind) {
		CreateMember(loginname);
		String key = StringUtil.getRandomString(8)
				+ EncryptionUtil.encryptPassword(website + prefix + loginname + loginKeyB + getUsEastTime())
				+ StringUtil.getRandomString(1);
		String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername
				+ "&lang=zh-cn&page_site=" + gameKind + "&maintenance_page=0&key=" + key;
		if ("live".equals(gameKind)) {
			params += "&page_present=live";
		}
		String url = loginurl + "/app/WebService/JSON/display.php/Login" + params;
		return url;
	}

	public static Boolean Login2(String loginname) {
		CreateMember(loginname);
		String key = StringUtil.getRandomString(8)
				+ EncryptionUtil.encryptPassword(website + prefix + loginname + loginKeyB + getUsEastTime())
				+ StringUtil.getRandomString(1);
		String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername
				+ "&lang=zh-cn&key=" + key;
		String url = loginurl + "/app/WebService/JSON/display.php/Login2" + params;
		String result = sendPost(url);
		if (result == null) {
			return Boolean.FALSE;
		}
		try {
			JSONObject a = JSONObject.fromObject(result);
			Boolean flag = a.getBoolean("result");
			if (flag) {
				return flag;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	public static Double GetBalance(String loginname) {
		Boolean flag = Login2(loginname);
		if (!flag) {
			return null;
		}
		String key = StringUtil.getRandomString(4)
				+ EncryptionUtil.encryptPassword(website + prefix + loginname + checkUsrBalanceKeyB + getUsEastTime())
				+ StringUtil.getRandomString(7);

		String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername + "&key="
				+ key;
		String url = dspurl + "/app/WebService/JSON/display.php/CheckUsrBalance" + params;
		String result = sendPost(url);
		if (result == null) {
			return 0.0;
		}
		try {
			JSONObject a = JSONObject.fromObject(result);
			Boolean fg = a.getBoolean("result");
			if (fg) {
				JSONArray data = a.getJSONArray("data");
				JSONObject b = (JSONObject) data.get(0);
				return Double.valueOf(b.get("Balance") + "");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Boolean transferToBbin(String loginname, Double money, String tranferno) {
		return TransferCredit(loginname, "IN", money, tranferno);
	}

	public static Boolean transferFromBbin(String loginname, Double money, String tranferno) {
		return TransferCredit(loginname, "OUT", money, tranferno);
	}

	public static Boolean TransferCredit(String loginname, String type, Double remit, String tranferno) {
		String key = StringUtil.getRandomString(9)
				+ EncryptionUtil
						.encryptPassword(website + prefix + loginname + tranferno + transferKeyB + getUsEastTime())
				+ StringUtil.getRandomString(4);
		String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername
				+ "&remitno=" + tranferno + "&action=" + type + "&remit=" + remit.intValue() + "&key=" + key;
		String url = dspurl + "/app/WebService/JSON/display.php/Transfer" + params;
		String result = sendPost(url);
		if (result == null) {
			return Boolean.FALSE;
		}
		try {
			JSONObject a = JSONObject.fromObject(result);
			Boolean flag = a.getBoolean("result");
			if (flag) {
				return flag;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	
	

	private static String getUsEastTime() {// 美东时间yyyyMMdd
		long time = new Date().getTime() - 12 * 3600 * 1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMMdd(calendar.getTime());
	}

	public static void main(String[] args) {
		//System.out.print(playGameH5("test9878",30599));
		System.out.print(Login("kavin998","ball"));
	}
}
