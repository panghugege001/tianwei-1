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

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class PtUtil {

	private static Logger log = Logger.getLogger(PtUtil.class);
	public static String ENCODE_TYPE = "UTF-8";
	private static Integer TIME_OUT = 5*10000;
	private static Integer TIME_OUT_LONG = 60*1000;
	public static String PALY_START = "TWYLU";
	public final static String PT_ADMINNAME = "TWYLCNYUAT";
	public final static String PT_KIOSKNAME = "TWYLCNYUAT";
	public final static String PT_TRACKINGID = "TWYLU";
	public static String PT_SERVICE = "https://kioskpublicapi.luckydragon88.com";
	public static String PT_KEY = "5d9722ea1840e9bfc538f5cc93d0394198cea1b89286e38719b7d6e7ef996da1f671b684019ba456dd84d85a2d5426b204556f63ff462601804f5b28e6c3107e";
	public static String PT_PASSWORD = "Dp63SkUC2dMDy4TZ";
	
	public static void main(String[] args) {
//		System.out.println(createPlayerName("dytest04","a123a123"));
		System.out.println(updatePlayerPassword("dytest", "diandian123"));
//		System.out.println(getDepositPlayerMoney("woodytest", 1.00));
		System.out.println(getPlayerMoney("dykf2018"));
//		System.out.println(getWithdrawPlayerMoney("woodytest", 1.00));
//		System.out.println(getPlayerMoney("woodytest"));
		//System.out.println(getPlayerInfo("woodytest1212"));
//		System.out.println(getPlayerLoginOut("woodytest"));
//		String startTime="2014-11-11DAVIDPT00:00:00";
//		String endTime="2014-11-11DAVIDPT23:59:59";
//		System.out.println(getPlayerBet("woodytest",startTime,endTime));
//		System.out.println(getPlayerAllBet(startTime,endTime));
//		System.out.println(getSepPlayerBet(startTime,endTime,"regular"));  //regular/live/both
//		System.out.println(getAllreports());
//		System.out.println(getReportParameter("PlayerGames"));
		
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

	//获取pt所有报表
	public static String getAllreports() {
		String url = PT_SERVICE + "/customreport/getallreports";
		String result = CallAPI(url);
		return result;
	}
	
	//获取pt报表参数
	public static String getReportParameter(String reportName) {
		String url = PT_SERVICE + "/customreport/getform/reportname/"+reportName;
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

}
