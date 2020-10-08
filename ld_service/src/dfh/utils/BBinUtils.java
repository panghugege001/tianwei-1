package dfh.utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import dfh.model.bean.BBinData;
import dfh.response.BBinRespBody;
import dfh.security.EncryptionUtil;
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

	public static String CreateSession(String loginname) {
		String key = StringUtil.getRandomString(8)
				+ EncryptionUtil.encryptPassword(website + prefix + loginname + createSessionKeyB + getUsEastTime())
				+ StringUtil.getRandomString(1);
		String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername
				+ "&lang=zh-cn&key=" + key;
		String url = dspurl + "/app/WebService/JSON/display.php/CreateSession" + params;
		String resp = sendPost(url);
		BBinRespBody respBody = NewJsonUtil.toObject(resp, BBinRespBody.class);
		String dataStr = NewJsonUtil.toJson(respBody.getData());
		if (respBody.isResult()) {
			BBinData data = NewJsonUtil.toObject(dataStr, BBinData.class);
			String sessionid = data.getSessionid();
			return sessionid;
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

	public static String Login(String loginname, String gameKind, int ingress) {
		CreateMember(loginname);
		String key = StringUtil.getRandomString(8)
				+ EncryptionUtil.encryptPassword(website + prefix + loginname + loginKeyB + getUsEastTime())
				+ StringUtil.getRandomString(1);
		String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername
				+ "&ingress=" + ingress + "&lang=zh-cn&page_site=" + gameKind + "&maintenance_page=0&key=" + key;
		if ("live".equals(gameKind)) {
			params += "&page_present=live";
		}
		String url = loginurl + "/app/WebService/JSON/display.php/Login" + params;
		return url;
	}

	public static Boolean Login2(String loginname, int ingress) {
		CreateMember(loginname);
		String key = StringUtil.getRandomString(8)
				+ EncryptionUtil.encryptPassword(website + prefix + loginname + loginKeyB + getUsEastTime())
				+ StringUtil.getRandomString(1);
		String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername
				+ "&ingress=" + ingress + "&lang=zh-cn&key=" + key;
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
			Boolean flag = a.getBoolean("result");
			if (flag) {
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

	public static String bbinFlashLogin(String loginname, String gameCode) {
		Boolean flag = Login2(loginname, 4);
		if (!flag) {
			return null;
		}

		String key = dfh.utils.StringUtil.getRandomString(8)
				+ EncryptionUtil.encryptPassword(website + prefix + loginname + PlayGameKeyB + getUsEastTime())
				+ StringUtil.getRandomString(8);
		String params = "?website=" + website + "&username=" + prefix + loginname + "&gamekind=5&gametype=" + gameCode
				+ "&lang=zh-cn&key=" + key;
		return loginurl + "/app/WebService/JSON/display.php/PlayGame" + params;
	}

	// H5捕鱼登录
	public static String playGame(String loginname, int gameType, int ingress) {
		// 1：網頁版、2：手機網頁版、4：App 9其他
		Boolean flag = Login2(loginname, ingress);
		if (!flag) {
			return null;
		}
		// 30599 捕鱼达人 30598 捕鱼达人2 38001 捕鱼大师 38002 富贵渔场
		if (gameType == 30599 || gameType == 30598) {
			String key = dfh.utils.StringUtil.getRandomString(8)
					+ EncryptionUtil.encryptPassword(website + prefix + loginname + PlayGameKeyB + getUsEastTime())
					+ StringUtil.getRandomString(8);
			String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername
					+ "&gametype=" + gameType + "&lang=zh-cn&key=" + key;
			return loginurl + "/app/WebService/JSON/display.php/ForwardGameH5By30" + params;
		} else if (gameType == 38001 || gameType == 38002) {
			String key = dfh.utils.StringUtil.getRandomString(8)
					+ EncryptionUtil.encryptPassword(website + prefix + loginname + PlayGameKeyB + getUsEastTime())
					+ StringUtil.getRandomString(8);
			String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername
					+ "&gametype=" + gameType + "&lang=zh-cn&key=" + key;
			return loginurl + "/app/WebService/JSON/display.php/ForwardGameH5By38" + params;
		} else {
			// 老虎机
			String key = dfh.utils.StringUtil.getRandomString(8)
					+ EncryptionUtil.encryptPassword(website + prefix + loginname + PlayGameKeyB + getUsEastTime())
					+ StringUtil.getRandomString(8);
			String params = "?website=" + website + "&username=" + prefix + loginname + "&uppername=" + uppername
					+ "&gametype=" + gameType + "&lang=zh-cn&key=" + key;
			return loginurl + "/app/WebService/JSON/display.php/ForwardGameH5By5" + params;
		}
	}

	private static String getUsEastTime() {// 美东时间yyyyMMdd
		long time = new Date().getTime() - 12 * 3600 * 1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMMdd(calendar.getTime());
	}

	// 获取捕鱼连接
	public static String playWebFishGame(String loginName, int gameType) {
		String sessionId = CreateSession(loginName);
		String key = dfh.utils.StringUtil.getRandomString(8)
				+ EncryptionUtil.encryptPassword(website + PlayGameKeyB + getUsEastTime())
				+ StringUtil.getRandomString(8);
		String params = "?website=" + website + "&gametype=" + gameType + "&sessionid=" + sessionId + "&lang=zh-cn&key="
				+ key;
		String fishUrl = "";
		
		if (gameType == 30599 || gameType == 30598) {
			// 30599 捕鱼达人 30598 捕鱼达人2
			fishUrl = dspurl + "/app/WebService/JSON/display.php/GameUrlBy30" + params;
		}else{
			//38001 捕鱼大师 38002 富贵渔场
			fishUrl = dspurl + "/app/WebService/JSON/display.php/GameUrlBy38" + params;
		}
		String url = "";
		String result = sendPost(fishUrl);
		if (result == null) {
			try {
				throw new Exception("获取捕鱼连接异常");
			} catch (Exception e) {
				log.error("获取捕鱼游戏连接异常!用户名:" + loginName + ",游戏编码：" + gameType);
			}
		} else {
			try {
				JSONObject object = JSONObject.fromObject(result);
				JSONArray feedsArray = object.getJSONArray("data");
				for (int i = 0; i < feedsArray.size(); i++) {
					JSONObject sonObject = feedsArray.getJSONObject(i);
					url = sonObject.getString("html5");
				}
			} catch (Exception e) {
				log.error("获取捕鱼游戏连接异常!用户名:" + loginName + ",游戏编码：" + gameType);
			}
		}
		return url;
	}

	public static void main(String[] args) {
		//System.out.print(playWebFishGame("test9844", 38001));
		// System.out.print(Login("kavin998", "ball"));
	}
}
