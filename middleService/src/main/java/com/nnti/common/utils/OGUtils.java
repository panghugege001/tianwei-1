package com.nnti.common.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

public class OGUtils extends PlatformConfigUtil{

	private static Logger log = Logger.getLogger(OGUtils.class);
	/**
	 * 发送post请求
	 * 
	 * @param method
	 *            api名称
	 * @param parameters
	 *            参数
	 * @return
	 */
	private static String sendPostRequest(String postData,String urlStr) {
		String value = "";
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			OutputStream output = conn.getOutputStream();

			output.write(postData.getBytes());
			output.flush();
			output.close();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			StringBuilder buffer = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			value = buffer.toString();
			//value = URLDecoder.decode(value, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}
	

	/**
	 * 
	 * @param transferId
	 * @param loginname
	 * @param amount 转帐额度，正数为转入，负数为转出
	 * @return
	 */
	public static Boolean transferOpt(String product,String transferId,String loginname,Double amount) {
		String cmd = "transferOpt";
		
		HashMap<String, String> map = ogMap.get(product);
		String prefix = map.get("prefix");
		String urlStr = map.get("url");
		String secrect_key = map.get("secrect_key");
		
		Integer amounti = amount.intValue();
		loginname = prefix+loginname;
		long time = System.currentTimeMillis() / 1000;
		String token = DigestUtils.md5Hex(cmd + loginname + amounti + transferId + time + secrect_key);
		// 构建输入参数为json格式
		JSONObject putObj = new JSONObject();
		try {
			putObj.put("cmd", cmd);
			putObj.put("username", loginname);
			putObj.put("amount", amounti);
			putObj.put("transferId", transferId);
			putObj.put("time", time);
			putObj.put("token", token);
		} catch (JSONException e) {
			e.printStackTrace();
			log.info("og transferOpt构建输入参数异常：" + e.getMessage());
			return Boolean.FALSE;
		}
		
		String str = putObj.toString();
		// 将输入参数加密
		String encryptStr = AESUltil.Encrypt(str, secrect_key);
		// 调用接口
		String result = sendPostRequest(encryptStr,urlStr);
		// 将返回数据解密
		String outStr = AESUltil.Decrypt(result, secrect_key);
		log.info("og transferOpt解密返回：" + outStr);
		
		JSONObject json = JSONObject.fromObject(outStr);
		String res = json.getString("res");
		if("0".equals(res)){
			return Boolean.TRUE;
		}else{
			String msg = json.getString("msg");
			log.info("og transferOpt返回结果：" + msg);
		}
		return Boolean.FALSE;
	}
	
	public static Double getBalance(String product,String loginname) {
		String cmd = "getBalance";
		
		HashMap<String, String> map = ogMap.get(product);
		String prefix = map.get("prefix");
		String urlStr = map.get("url");
		String secrect_key = map.get("secrect_key");
		
		loginname = prefix+loginname;
		
		long time = System.currentTimeMillis() / 1000;
		String token = DigestUtils.md5Hex(cmd + loginname + time + secrect_key);
		// 构建输入参数为json格式
		JSONObject putObj = new JSONObject();
		try {
			putObj.put("cmd", cmd);
			putObj.put("username", loginname);
			putObj.put("time", time);
			putObj.put("token", token);
		} catch (JSONException e) {
			e.printStackTrace();
			log.info("og getBalance构建输入参数异常：" + e.getMessage());
			return null;
		}
		
		String str = putObj.toString();
		// 将输入参数加密
		String encryptStr = AESUltil.Encrypt(str, secrect_key);
		// 调用接口
		String result = sendPostRequest(encryptStr,urlStr);
		// 将返回数据解密
		String outStr = AESUltil.Decrypt(result, secrect_key);
		log.info("og getBalance解密返回：" + outStr);
		
		JSONObject json = JSONObject.fromObject(outStr);
		String res = json.getString("res");
		if("0".equals(res)){
			String balance = json.getString("balance");
			return new Double(balance);
		}else{
			String msg = json.getString("msg");
			log.info("og getBalance返回结果：" + msg);
		}
		return null;
	}
	
	/**
	 * 获取玩家某时段投注额，gameId 和 loginname为空获取所有
	 * @param startDate
	 * @param endDate
	 * @param gameId
	 * @param loginname
	 * @return
	 */
	public static List getMemberReport(String product,String startDate,String endDate,String gameId,String loginname) {
		String cmd = "getMemberReport";
		
		HashMap<String, String> map = ogMap.get(product);
		String prefix = map.get("prefix");
		String urlStr = map.get("url");
		String secrect_key = map.get("secrect_key");
		
		
		loginname = "".equals(loginname) ? "" : prefix + loginname;
		long time = System.currentTimeMillis() / 1000;
		String token = DigestUtils.md5Hex(cmd + startDate+endDate+gameId+loginname + time + secrect_key);
		// 构建输入参数为json格式
		JSONObject putObj = new JSONObject();
		try {
			putObj.put("cmd", cmd);
			putObj.put("startDate", startDate);
			putObj.put("endDate", endDate);
			putObj.put("gameId", gameId);
			putObj.put("username", loginname);
			putObj.put("time", time);
			putObj.put("token", token);
		} catch (JSONException e) {
			e.printStackTrace();
			log.info("og getMemberReport构建输入参数异常：" + e.getMessage());
			return null;
		}
		
		String str = putObj.toString();
		// 将输入参数加密
		String encryptStr = AESUltil.Encrypt(str, secrect_key);
		// 调用接口
		String result = sendPostRequest(encryptStr,urlStr);
		// 将返回数据解密
		String outStr = AESUltil.Decrypt(result, secrect_key);
		log.info("og getMemberReport解密返回：" + outStr);
		
		JSONObject json = JSONObject.fromObject(outStr);
		String res = json.getString("res");
		if("0".equals(res)){
			List list = JSONArray.fromObject(json.get("data"));
			return list;
		}else{
			String msg = json.getString("msg");
			log.info("og transferOpt返回结果：" + msg);
		}
		return null;
	}
	
	

	public static void main(String[] args) throws Exception {
		String loginname = "devtest999"; //moveon23
		String password = "cf802424ab1302f188bd1ffa77cf6c54";
		String oldPwd = "a123a123";
		String newPwd = "a123a123";
		String isMobile = "";
		String transferId = System.currentTimeMillis()+"";
		Double amount = -1.0;
		String startDate = "2017-10-18 00:00:00";
		String endDate = "2017-10-18 23:59:59";
		
		transferOpt("qy",transferId, loginname, amount);
		
		Double balance = getBalance("qy",loginname);
		System.out.println(balance);
		
		List list = getMemberReport("qy",startDate, endDate, "", "");
		System.out.println(list);
		

	}
}
