package com.nnti.common.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.nnti.common.security.EncryptionUtil;

import net.sf.json.JSONObject;

public class ChessUtil {

	private static Logger log = Logger.getLogger(ChessUtil.class);
	  
	public static final String RESULT_SUCC = "SUCCESS";
	public static final String ENCODE_TYPE = "UTF-8";
	
	private static final String PREFIX = "DY8";
	public static final String ACCOUNTTYPE = "chess";
	public static final String AGENT = "1035";
	public static final String APPKEY = "da115af41bc0934d4987a60ec57f84f8";
	public static final String CG_URL = "http://api.761city.com:10018/agent/";
	
    public static String encrypt(String src) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = APPKEY.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(APPKEY.substring(0,16).getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] dataBytes = src.getBytes("utf-8");
        byte[] encrypted = cipher.doFinal(dataBytes);
        String encrypt = new String(org.apache.commons.codec.binary.Base64.encodeBase64(encrypted));
        return encrypt;
    }
    
	
	public static JSONObject sendApi(String params,String func) throws Exception {
		URL url = new URL(CG_URL+func+"?");
		
		Map<String, Object> baseparams = new LinkedHashMap<>();
		baseparams.put("agent", AGENT);
		String timestamp = Long.toString(System.currentTimeMillis()/1000);
		baseparams.put("timestamp", timestamp);
		baseparams.put("params", params);
		String sign = EncryptionUtil.encryptPassword(AGENT+timestamp+APPKEY);
		baseparams.put("sign", sign);

		String response = httpPostRequest(func,url, baseparams);
		JSONObject json = JSONObject.fromObject(response);
		return json;
	}

	private static String httpPostRequest(String func,URL url, Map<String,Object> params) throws IOException{
		StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(param.getKey());
            postData.append('=');
            postData.append(param.getValue());
        }
        
        // 如果请求 有问题, 请把这串请求串 提供给761技术检视
        System.out.printf("[%s_RequestURL]:%s%s\n",func, url, postData);
        byte[] postDataBytes = postData.toString().getBytes(ENCODE_TYPE);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), ENCODE_TYPE));

        StringBuffer resInfo = new StringBuffer();
        for (int c; (c = in.read()) >= 0;)
        {
        	char ch = (char)c;
        	resInfo.append(ch);
        }
        System.out.printf("[%s_Response]:%s\n\n",func, resInfo);
        return resInfo.toString();    
	}
	
	public static String cg761Login(String loginname) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("acc", PREFIX+loginname);
		JSONObject json = JSONObject.fromObject(param);
		String aesEnc = encrypt(json.toString());
		String params = URLEncoder.encode(aesEnc, ENCODE_TYPE);
		JSONObject result = sendApi(params,"login");
		String code = result.getString("code");
		if("0".equals(code)){
			JSONObject data = result.getJSONObject("d");
			String url = data.getString("url");
			return url;
		}else{
			String msg = result.getString("m");
			log.info(msg);
		}
		return null;
	}
	
	public static Double getBalance(String product,String loginname) throws Exception{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("acc", PREFIX+loginname);
		JSONObject json = JSONObject.fromObject(param);
		String aesEnc = encrypt(json.toString());
		String params = URLEncoder.encode(aesEnc, ENCODE_TYPE);
		
		JSONObject result = sendApi(params,"getbalance");
		String code = result.getString("code");
		if("0".equals(code)){
			JSONObject data = result.getJSONObject("d");
			Double balance = data.getDouble("balance");
			return balance;
		}else{
			String msg = result.getString("m");
			log.info(msg);
		}
		return null;
	}
	
	public static Boolean transferToChess(String product,String loginname,Double money,String orderid){
		Map<String, Object> param = new HashMap<String, Object>();
		
		String acc = PREFIX+loginname;
		param.put("acc", acc);
		//String orderid = AGENT+DateUtil.getDateID()+acc;
		param.put("orderid", orderid);
		param.put("acc", PREFIX+loginname);
		param.put("money", money);
		
		JSONObject json = JSONObject.fromObject(param);
		try {
			String aesEnc = encrypt(json.toString());
			String params = URLEncoder.encode(aesEnc, ENCODE_TYPE);
			
			JSONObject result = sendApi(params,"payup");
			String code = result.getString("code");
			if("0".equals(code)){
				return Boolean.TRUE;
			}else{
				String msg = result.getString("m");
				log.info(msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}
	public static Boolean transferFromChess(String product,String loginname,Double money,String orderid){
		Map<String, Object> param = new HashMap<String, Object>();
		
		String acc = PREFIX+loginname;
		param.put("acc", acc);
		param.put("orderid", orderid);
		param.put("acc", PREFIX+loginname);
		param.put("money", money);
		
		JSONObject json = JSONObject.fromObject(param);
		try {
			String aesEnc = encrypt(json.toString());
			String params = URLEncoder.encode(aesEnc, ENCODE_TYPE);
			
			JSONObject result = sendApi(params,"paydown");
			String code = result.getString("code");
			if("0".equals(code)){
				return Boolean.TRUE;
			}else{
				String msg = result.getString("m");
				log.info(msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}
	
	
	
	public static void main(String[] args) throws Exception {
		String loginName = "dytest01";
		Double money = 100.0;
		String product = "dy";
		//System.out.println(cg761Login(loginName));
//		System.out.println(transferToChess(product,loginName,money,System.currentTimeMillis()+"121232334"));
//		System.out.println(transferFromChess(product,loginName,money,"12123233"));
		System.out.println(getBalance(product,loginName));
		
    }

}
