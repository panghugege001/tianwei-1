package com.nnti.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class PTUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(PTUtil.class);
	
	private static Integer TIME_OUT = 5*10000;
	//private static Integer TIME_OUT_LONG = 60*1000;
	
	private static String CallAPI(String url,String pt_password,String pt_key) {
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			String path = PTUtil.class.getClassLoader().getResource("").getPath();

			File file = new File(path+"resource/CNY.p12");

			FileInputStream fis = new FileInputStream(file);
			ks.load(fis, pt_password.toCharArray());

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(ks, pt_password.toCharArray());
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
			connection.setRequestProperty("X_ENTITY_KEY", pt_key);
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
			log.info(" PT API 接口出现问题！"+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	// 获取远程游戏金额
	public static Double getPlayerMoney(String product, String loginName) {
		HashMap<String, String> map = ptMap.get(product);
		String PT_KEY = map.get("PT_KEY");
		String PT_SERVICE = map.get("PT_SERVICE");
		String PALY_START = map.get("PALY_START");
		String PT_PASSWORD = map.get("PT_PASSWORD");
		
		String url = PT_SERVICE + "/player/info/playername/" + PALY_START + loginName;
		String result = CallAPI(url,PT_PASSWORD,PT_KEY);
		if(result == null){
			return null;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("result")) {
				log.info(loginName + " 获取PT远程游戏金额为：" + jsonObj);
				JSONObject jsonObjTwo = JSONObject.fromObject(jsonObj.getString("result"));
				if (jsonObjTwo.containsKey("BALANCE")) {
					Double balance = jsonObjTwo.getDouble("BALANCE");
					log.info(loginName + " 获取PT远程游戏金额为：" + balance);
					return balance;
				}
				log.info(loginName + " 获取PT远程游戏金额失败 用户不存在" + jsonObj);
				return null;
			}
			log.info(loginName + " 获取PT远程游戏金额失败 用户不存在 " + jsonObj);
			return null;
		}
	}

	// 退出游戏
	public static Boolean getPlayerLoginOut(String product, String loginName) {

		HashMap<String, String> map = ptMap.get(product);
		String PT_KEY = map.get("PT_KEY");
		String PT_SERVICE = map.get("PT_SERVICE");
		String PALY_START = map.get("PALY_START");
		String PT_PASSWORD = map.get("PT_PASSWORD");
		
		String url = PT_SERVICE + "/player/logout/playername/" + PALY_START + loginName;
		String result = CallAPI(url,PT_PASSWORD,PT_KEY);
		if(result == null){
			return false;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			log.info(loginName + " pt玩家退出游戏 " + jsonObj);
			if (jsonObj.containsKey("result")) {
				return true;
			}
			log.info(loginName + " pt玩家退出游戏" + jsonObj);
			return false;
		}
	}

	// 转出游戏
	public static Boolean getWithdrawPlayerMoney(String product, String loginName, Double amount) {
		getPlayerLoginOut(product, loginName);
		HashMap<String, String> map = ptMap.get(product);
		String PT_KEY = map.get("PT_KEY");
		String PT_SERVICE = map.get("PT_SERVICE");
		String PALY_START = map.get("PALY_START");
		String PT_PASSWORD = map.get("PT_PASSWORD");
		String PT_ADMINNAME = map.get("ADMINNAME");
		
		String url = PT_SERVICE + "/player/withdraw/playername/" + PALY_START + loginName + "/amount/" + amount + "/adminname/"+PT_ADMINNAME;
		String result = CallAPI(url,PT_PASSWORD,PT_KEY);
		if(result == null){
			return false;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("result")) {
				log.info(loginName + " 转出游戏成功：" + jsonObj);
				return true;
			}
			log.info(loginName + " 转出游戏失败 用户不存在 " + jsonObj);
			return false;
		}
	}

	// 转入游戏
	public static Boolean getDepositPlayerMoney(String product, String loginName, Double amount) {
		HashMap<String, String> map = ptMap.get(product);
		String PT_KEY = map.get("PT_KEY");
		String PT_SERVICE = map.get("PT_SERVICE");
		String PALY_START = map.get("PALY_START");
		String PT_PASSWORD = map.get("PT_PASSWORD");
		String PT_ADMINNAME = map.get("ADMINNAME");
		
		String url = PT_SERVICE + "/player/deposit/playername/" + PALY_START + loginName + "/amount/" + amount + "/adminname/"+PT_ADMINNAME;
		String result = CallAPI(url,PT_PASSWORD,PT_KEY);
		if(result == null){
			return false;
		}else{
			JSONObject jsonObj = JSONObject.fromObject(result);
			if (jsonObj.containsKey("result")) {
				log.info(loginName + " 转入游戏成功：" + jsonObj);
				return true;
			}
			log.info(loginName + " 转入游戏失败 " + jsonObj);
			return false;
		}
	}

	public static void main(String[] args) {
		System.out.println(getDepositPlayerMoney("tw", "twtest01",1.0));
		//System.out.println(getWithdrawPlayerMoney("dy", "dytest01",1.0));
		System.out.println(getPlayerMoney("tw", "twtest01"));
	}
}