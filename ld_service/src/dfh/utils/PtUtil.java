package dfh.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class PtUtil {

	private static Logger log = Logger.getLogger(PtUtil.class);
	public static String ENCODE_TYPE = "UTF-8";
	private static Integer TIME_OUT = 5*10000;
	private static Integer TIME_OUT_LONG = 60*1000;
	public static String PALY_START = "TW8";
	public final static String PT_ADMINNAME = "CNYKLTWYLAPI";
	public final static String PT_KIOSKNAME = "CNYKLTWYL";
	public final static String PT_TRACKINGID = "TW8";
	public static String PT_SERVICE = "https://kioskpublicapi.mistral88.com";
	public static String PT_KEY = "aa13d5340ad66ce29ea2b3eec6753aefffcf4bffd3caf8829ad624a8b9c9cade57a2fbc4664ab6a6254aa5b6d6b5642b3f23a3f5323ff94fe1c0dae993ace50d";
	public static String PT_PASSWORD = "WpuxWHd8IzQVPtLm";

	public static void main(String[] args) {
		String loginname = "twtest01";
		String password = "a123a123";
		//resetfailedlogin(loginname);
		
		System.out.println(createPlayerName(loginname,password));
		System.out.println(updatePlayerPassword(loginname, password));

//		System.out.println(getDepositPlayerMoney(loginname, 200.00));
//		System.out.println(getWithdrawPlayerMoney(loginname, 100.00));
		System.out.println(getPlayerMoney(loginname,password));
//		System.out.println(getPlayerInfo(loginname));
//		System.out.println(getPlayerLoginOut("woodytest"));
//		String startTime="2018-09-19 00:00:00";
//		String endTime="2018-09-20 23:59:59";
//		System.out.println(getPlayerBet("y911116",startTime,endTime));
//		System.out.println(getPlayerAllBet(startTime,endTime));
//		System.out.println(massUpdate("woodytest", "yahu", "yahu"));
//		System.out.println(getPlayerInfo("dan521"));
//		System.out.println(getPlayerOnlineInfo("woodytest"));
	}
	
	private static String CallAPI(String url) {
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			String path = PtUtil.class.getClassLoader().getResource("").getPath();

			File file = new File(path+"resource/CNY.p12");

			FileInputStream fis = new FileInputStream(file);
			ks.load(fis, PT_PASSWORD.toCharArray());

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, PT_PASSWORD.toCharArray());
			KeyManager[] kms = kmf.getKeyManagers();

			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}

			} };

			// Hostname verification bypass method
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// Set connection properties to use bypass certificate/hostname
			// check methods
			SSLContext sslContext = null;
			sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kms, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

			// Send API call together with entity key for validation
			HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
			connection.setRequestProperty("X_ENTITY_KEY", PT_KEY);
			connection.setConnectTimeout(TIME_OUT);
			connection.setReadTimeout(TIME_OUT);
			int code = connection.getResponseCode();
			if(code != 200){
				log.info(" PT API 接口出现问题！状态码："+code);
				return null;
			}
			InputStream response = connection.getInputStream();
			String resp = IOUtils.toString(response);
			connection.disconnect();
			response.close();
			return resp;

		} catch (Exception e) {
			log.info(" PT API 接口出现问题！");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 创建玩家
	 * @return
	 */
	public static String createPlayerName(String loginname,String password) {
		String url = PT_SERVICE + "/player/create/playername/" + PALY_START + loginname +"/password/" + password + "/adminname/"+PT_ADMINNAME+"/kioskname/"+PT_KIOSKNAME+"/trackingid/"+PT_TRACKINGID+"/custom02/"+PT_TRACKINGID+"/";
		String result = CallAPI(url);
		log.info(" PT CREATE API返回："+result);
		return result;
	}

	/**
	 * 更新密码
	 * @return
	 */
	public static Boolean updatePlayerPassword(String loginname, String password) {
		String url = PT_SERVICE + "/player/update/playername/" + PALY_START + loginname + "/password/" + password;
		String result = CallAPI(url);
		if(result == null){
			return false;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("result")) {
				log.info(loginname + " 更改密码成功：" + jsonObj);
				return true;
			}
			log.info(loginname + " 更改密码失败 用户不存在");
			return false;
		}
	}

	/**
	 * 转入游戏
	 * @return
	 */
	public static Boolean getDepositPlayerMoney(String loginname, Double amount) {
		String url = PT_SERVICE + "/player/deposit/playername/" + PALY_START + loginname + "/amount/" + amount + "/adminname/"+PT_ADMINNAME;
		String result = CallAPI(url);
		if(result == null){
			return false;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("result")) {
				log.info(loginname + " 转入游戏成功：" + jsonObj);
				return true;
			}
			log.info(loginname + " 转入游戏失败 用户不存在 " + jsonObj);
			return false;
		}
	}

	/**
	 * 转出游戏
	 * @return
	 */
	public static Boolean getWithdrawPlayerMoney(String loginname, Double amount) {
		getPlayerLoginOut(loginname);
		String url = PT_SERVICE + "/player/withdraw/playername/" + PALY_START + loginname + "/amount/" + amount + "/adminname/"+PT_ADMINNAME;
		String result = CallAPI(url);
		if(result == null){
			return false;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("result")) {
				log.info(loginname + " 转出游戏成功：" + jsonObj);
				return true;
			}
			log.info(loginname + " 转出游戏失败 用户不存在 " + jsonObj);
			return false;
		}
	}

	/**
	 * 获取远程游戏金额,如果账号不存在就创建一个
	 * @return
	 */
	public static Double getPlayerMoney(String loginname,String password) {
		resetfailedlogin(loginname);
		String url = PT_SERVICE + "/player/info/playername/" + PALY_START + loginname;
		String result = CallAPI(url);
		if(result == null){
			return null;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("result")) {
				log.info(loginname + " 获取PT远程游戏金额为：" + jsonObj);
				JSONObject jsonObjTwo = JSONObject.fromObject(jsonObj.getString("result"));
				if (jsonObjTwo.containsKey("BALANCE")) {
					Double balance = jsonObjTwo.getDouble("BALANCE");
					log.info(loginname + " 获取PT远程游戏金额为：" + balance);
					return balance;
				}
				return null;
			}
			
			Integer errorcode = jsonObj.getInt("errorcode");
			if (errorcode == 41) {
				log.info(loginname + " 获取PT远程游戏金额失败 用户不存在" + jsonObj);
				createPlayerName(loginname, password);
				//updatePlayerPassword(loginname, password);
			}
			return null;
		}
	}
	
	
	/**
	 * 获取远程游戏金额
	 * @return
	 */
	public static Double getPlayerMoney(String loginname) {
		String url = PT_SERVICE + "/player/info/playername/" + PALY_START + loginname;
		String result = CallAPI(url);
		if(result == null){
			return null;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("result")) {
				log.info(loginname + " 获取PT远程游戏金额为：" + jsonObj);
				JSONObject jsonObjTwo = JSONObject.fromObject(jsonObj.getString("result"));
				if (jsonObjTwo.containsKey("BALANCE")) {
					Double balance = jsonObjTwo.getDouble("BALANCE");
					log.info(loginname + " 获取PT远程游戏金额为：" + balance);
					return balance;
				}
				log.info(loginname + " 获取PT远程游戏金额失败 用户不存在" + jsonObj);
				return null;
			}
			log.info(loginname + " 获取PT远程游戏金额失败 用户不存在 " + jsonObj);
			return null;
		}
		
	}

	//清空玩家错误登录次数
	public static void resetfailedlogin(String loginname) {
		String url = PT_SERVICE + "/player/resetfailedlogin/playername/" + PALY_START + loginname;
		String result = CallAPI(url);
		if(result == null){
			return;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("result")) {
				JSONObject jsonObjTwo = JSONObject.fromObject(jsonObj.getString("result"));
				if (jsonObjTwo.containsKey("result")) {
					String msg = jsonObjTwo.getString("result");
					log.info(loginname + "玩家错误次数清理返回信息：" + msg);
				}
			}else{
				log.info(loginname + "玩家错误次数清理返回信息： " + jsonObj);
			}
			return;
		}
		
	}
	
	
	/**
	 * 获取游戏信息
	 * @return
	 */
	public static String getPlayerInfo(String loginname) {
		String url = PT_SERVICE + "/player/info/playername/" + PALY_START + loginname;
		String result = CallAPI(url);
		return result;
	}
	
	/**
	 * 获取是否在线
	 * @return
	 */
	public static Boolean getPlayerOnlineInfo(String loginname) {
		String url = PT_SERVICE + "/player/online/playername/" + PALY_START + loginname;
		String result = CallAPI(url);
		if(result == null){
			return false;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("result")) {
				log.info(loginname + " pt玩家是否在线：" + jsonObj);
				JSONObject jsonObjTwo = JSONObject.fromObject(jsonObj.getString("result"));
				if (jsonObjTwo.containsKey("result")) {
					Integer online = jsonObjTwo.getInt("result");
					log.info(loginname + " pt玩家是否在线：" + online);
					if(online==0){
						return false;
					}
					return true;
				}
				log.info(loginname + " pt玩家是否在线 用户不存在" + jsonObj);
				return false;
			}
			log.info(loginname + " pt玩家是否在线 用户不存在 " + jsonObj);
			return false;
		}
		
	}
	
	/**
	 * 退出游戏
	 * @return
	 */
	public static Boolean getPlayerLoginOut(String loginname) {
		String url = PT_SERVICE + "/player/logout/playername/" + PALY_START + loginname;
		String result = CallAPI(url);
		if(result == null){
			return false;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			log.info(loginname + " pt玩家退出游戏 " + jsonObj);
			if (jsonObj.containsKey("result")) {
				return true;
			}
			log.info(loginname + " pt玩家退出游戏" + jsonObj);
			return false;
		}
	}
	
	/**
	 * 统计玩家投注额
	 * @return
	 */
	public static String getPlayerBet(String loginname,String startTime,String endTime) {
		String url = PT_SERVICE + "/customreport/getdata/reportname/PlayerStats/playername/"+PALY_START+loginname+"/startdate/"+startTime+"/enddate/"+endTime+"/reportby/player";
		String result = CallAPI(url);
		return result;
	}
	
	/**
	 * 统计玩家投注额
	 * @return
	 */
	public static String getPlayerAllBet(String startTime,String endTime) {
		String url = PT_SERVICE + "/customreport/getdata/reportname/PlayerStats/startdate/"+startTime+"/enddate/"+endTime+"/page/1/perPage/50000/reportby/player";
		String result = CallAPI(url);
		return result;
	}
	
	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @param gameType  regular和live的区分
	 * @return
	 */
	public static String getSepPlayerBet(String startTime,String endTime,String gameType) {
		String url = PT_SERVICE + "/customreport/getdata/reportname/GameStats/gametype/"+gameType+"/startdate/"+startTime+"/enddate/"+endTime+"/page/1/perPage/50000/reportby/playername/sortby/players";
		String result = CallAPI(url);
		return result;
	}
	
	/**
	 * 获取玩家某段时间的投注额
	 * @param startTime
	 * @param endTime
	 * @param gameType  
	 * @return
	 */
	public static String getPlayerBets(String startTime,String endTime,String gameType) {
		String url = PT_SERVICE + "/customreport/PlayerBets/reportname/GameStats/gametype/"+gameType+"/startdate/"+startTime+"/enddate/"+endTime+"/page/1/perPage/50000/reportby/playername/sortby/players";
		String result = CallAPI(url);
		return result;
	}
	
	
	
	
	
	
	
	public static String massBatchUpdate(String PALY_START , List<String> users,String trackingid,String custom02) {
		String json = "[";
		for (String username : users) {
			json += "{\"playername\":\""+PALY_START+username+"\",\"custom02\":\""+custom02+"\",\"trackingid\":\""+trackingid+"\"}," ;
		}
		String address = PT_SERVICE+ "/player/massUpdate?params="+json;
		String url = URLEncoder.encode(address) ;
		String result = CallAPI(url);
		return result;
	}


	/**
	 * 获取pt地址
	 * @param key
	 * @return
	 */
	public static String getPhpPtService(String key) {
		try {
			Properties properties = new Properties();
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			return properties.getProperty(key);
		} catch (Exception e) {
			log.info(e.toString());
			return null;
		}
	}
	
	public static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
		params.setParameter("http.socket.timeout", TIME_OUT);
		httpclient.setParams(params);
		return httpclient;
	}
	
	public static HttpClient createLongHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
		params.setParameter("http.socket.timeout", TIME_OUT_LONG);
		httpclient.setParams(params);
		return httpclient;
	}
	
	//goldenrose迁移所用
	public static Double getPlayerOldBalance(String loginname) {
		String PT_SERVICE = "https://kioskpublicapi.morningstar88.com";
		String PT_KEY = "a7e7969d40c484a3ca8eb0363730c9460874b94bbc02f762b8a29084ecd3043aa4b83e73683c8ce63fbb81f2502946ccd79bbe6cb8ba4847f85398ac14d7a8ee";
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = "http://69.172.80.5:6886/newsite/ylInfoUser.php" + "?entity_key=" + PT_KEY + "&url=" + PT_SERVICE + "/player/info/playername/" + PALY_START + loginname;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String phpHtml = postMethod.getResponseBodyAsString();
				if (phpHtml == null || phpHtml.equals("")) {
					log.info(" PT API 接口出现问题！");
					return null;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (jsonObj.containsKey("result")) {
					log.info(loginname + " 获取PT远程游戏金额为：" + jsonObj);
					JSONObject jsonObjTwo = JSONObject.fromObject(jsonObj.getString("result"));
					if (jsonObjTwo.containsKey("BALANCE")) {
						Double balance = jsonObjTwo.getDouble("BALANCE");
						log.info(loginname + " 获取PT远程游戏金额为：" + balance);
						return balance;
					}
					log.info(loginname + " 获取PT远程游戏金额失败 用户不存在" + jsonObj);
					return null;
				}
				log.info(loginname + " 获取PT远程游戏金额失败 用户不存在 " + jsonObj);
				return null;
			} else {
				log.info("Response 获取PT远程游戏金额失败 Code: " + statusCode);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 获取金额失败 消息: " + e.toString());
			return null;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}

	//goldenrose迁移所用
	public static Boolean withdrawOldPlayerMoney(String loginname, Double amount) {
		String PT_SERVICE = "https://kioskpublicapi.morningstar88.com";
		String PT_KEY = "a7e7969d40c484a3ca8eb0363730c9460874b94bbc02f762b8a29084ecd3043aa4b83e73683c8ce63fbb81f2502946ccd79bbe6cb8ba4847f85398ac14d7a8ee";
		
		HttpClient httpClient = null;
		GetMethod postMethod = null;
		try {
			String url = "http://69.172.80.5:6886/newsite/ylWithdrawUser.php" + "?entity_key=" + PT_KEY + "&url=" + PT_SERVICE + "/player/withdraw/playername/" + PALY_START + loginname + "/amount/" + amount + "/adminname/" + PT_ADMINNAME;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new GetMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String phpHtml = postMethod.getResponseBodyAsString();
				if (phpHtml == null || phpHtml.equals("")) {
					log.info(" PT API 接口出现问题！");
					return false;
				}
				JSONObject jsonObj = JSONObject.fromObject(phpHtml);
				if (jsonObj.containsKey("result")) {
					log.info(loginname + " 转出游戏成功：" + jsonObj);
					return true;
				}
				log.info(loginname + " 转出游戏失败 用户不存在 " + jsonObj);
				return false;
			} else {
				log.info("Response 获取金额失败 Code: " + statusCode);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Response 获取金额失败 消息: " + e.toString());
			return false;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
	}
}
