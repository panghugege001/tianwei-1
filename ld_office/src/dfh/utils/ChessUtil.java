package dfh.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import dfh.model.Users;
import dfh.security.EncryptionUtil;

public class ChessUtil {

	private static Logger log = Logger.getLogger(ChessUtil.class);
	
	  
	public static final String ENCODE_TYPE = "UTF-8";
	private static final String PREFIX = "k";
	public static final String MAINTAIN = "MAINTAIN";
	public static final String AGENT = "1029";
	public static final String APPKEY = "d5fc32569c036af2a2aa8168821181c2";
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

	private static String httpPostRequest(String func,URL url, Map<String,Object> params) throws IOException
	{
		StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(param.getKey());
            postData.append('=');
            postData.append(param.getValue());
        }
        
        // 如果请求 有问题, 请把这串请求串 提供给MW技术检视
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
	
	
	/**
	 * 获取棋牌余额，如果用户不存在则创建用户，成功后返回0
	 * @param loginName
	 * @return
	 * */
	public static Double getBalance(String loginName){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("acc", PREFIX+loginName);
		JSONObject json = JSONObject.fromObject(param);
		try {
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
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//获取游戏记录
	public static List loadrecords(Long stime,Long etime,int page){
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("page", page);
		JSONObject json = JSONObject.fromObject(param);
		try {
			String aesEnc = encrypt(json.toString());
			String params = URLEncoder.encode(aesEnc, ENCODE_TYPE);
			JSONObject result = sendApi(params,"loadrecords");
			String code = result.getString("code");
			if("0".equals(code)){
				JSONObject d = result.getJSONObject("d");
				List list = (List) d.get("data");
				if(list == null || list.size() ==0){
					return null;
				}
				return list;
			}else{
				String msg = result.getString("m");
				log.info(msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void main(String[] args) throws Exception {
		Users user =new Users();
		user.setLoginname("devtest999");
		String loginName = user.getLoginname();
		//System.out.println(cg761Login(user.getLoginname()));
		//System.out.println(getBalance(loginName));
		String sdate = DateUtil.getchangedDate(-1)+" 00:00:00";
		String edate = DateUtil.getchangedDate(-1)+ " 23:59:59";
		Long stime = DateUtil.parseDateForStandard(sdate).getTime()/1000;
		Long etime = DateUtil.parseDateForStandard(edate).getTime()/1000;
		int page = 1;
		System.out.println(loadrecords(stime, etime, page).size());

    }

}
