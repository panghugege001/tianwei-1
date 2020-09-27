package dfh.utils;

import dfh.security.EncryptionUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class KYQPUtil {

	private static Logger log = Logger.getLogger(KYQPUtil.class);
	
	  
	public static final String ENCODE_TYPE = "UTF-8";
	public static final String PREFIX = "k";
	public static final String AGENT_CODE ="LD";
	public static final String KEY = "10523d95aa6a43e49cdccc7921c3c3d8";
	public static final String API_URL = "http://datacenter.itgo88.com";
	
	/**
	 * 登录
	 * @param loginname
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static String kyqpLogin(String loginname,String platform,String ip) throws Exception{
		URL url = new URL(API_URL+"/api/game/logingame"+"?");
		Map<String, Object> paramMap = new LinkedHashMap<>();
		paramMap.put("callbackwebsite", "");
		paramMap.put("gameId", "");
		paramMap.put("ip", ip);
		paramMap.put("mcode", AGENT_CODE);
		paramMap.put("platform", platform.toUpperCase());
		String timestamp = Long.toString(new Date().getTime());
		paramMap.put("timestamp", timestamp);
		paramMap.put("type", "");
		paramMap.put("username", PREFIX+loginname);
		Map<String, Object> newMap = sortMap(paramMap);
		log.info("待签名串:"+newMap.toString()+KEY);
		String sign= EncryptionUtil.md5Encrypt(newMap.toString()+KEY);
		paramMap.put("sign", sign);
		String response = httpPostRequest(url, paramMap);
		log.info("响应报文:"+response);
		JSONObject json = JSONObject.fromObject(response);
		if("0".equals(json.getString("code"))){
			return json.getString("url");
		}else{
			String msg = json.getString("msg");
			log.info(msg);
		}
		return null;
	}
	
	/**
	 * 登录
	 * @param loginname
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	public static String kyqpLogin(String loginname,String platform,String gameId,String ip) throws Exception{
		URL url = new URL(API_URL+"/api/game/logingame"+"?");
		Map<String, Object> paramMap = new LinkedHashMap<>();
		paramMap.put("callbackwebsite", "");
		paramMap.put("gameId", gameId);
		paramMap.put("ip", ip);
		paramMap.put("mcode", AGENT_CODE);
		paramMap.put("platform", platform.toUpperCase());
		String timestamp = Long.toString(new Date().getTime());
		paramMap.put("timestamp", timestamp);
		paramMap.put("type", "");
		paramMap.put("username", PREFIX+loginname);
		Map<String, Object> newMap = sortMap(paramMap);
		log.info("待签名串:"+newMap.toString()+KEY);
		String sign= EncryptionUtil.md5Encrypt(newMap.toString()+KEY);
		paramMap.put("sign", sign);
		String response = httpPostRequest(url, paramMap);
		log.info("响应报文:"+response);
		JSONObject json = JSONObject.fromObject(response);
		if("0".equals(json.getString("code"))){
			return json.getString("url");
		}else{
			String msg = json.getString("msg");
			log.info(msg);
		}
		return null;
	}
	
	 /**
     * 获取用户余额
     * @param username 用户名
     * @param platform 游戏平台
     * @param ip 客户IP
     * @return 余额
     */
    public static Double getBalance(String username,String platform,String ip) throws IOException {
        URL url= new URL(API_URL+"/api/game/getbalance"+"?");
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("username",PREFIX+username);
        paramMap.put("platform",platform.toUpperCase());
        paramMap.put("mcode",AGENT_CODE);
        paramMap.put("ip",ip);
        String timestamp = Long.toString(new Date().getTime());
		paramMap.put("timestamp", timestamp);
        Map<String, Object> newMap = sortMap(paramMap);
        log.info("待签名串:"+newMap.toString()+KEY);
		String sign= EncryptionUtil.md5Encrypt(newMap.toString()+KEY);
		paramMap.put("sign", sign);
		String response = httpPostRequest(url, paramMap);
		log.info("响应报文:"+response);
		JSONObject json = JSONObject.fromObject(response);
		Double balance = 0.0;
        if (!"0".equals(json.getString("code"))) {
            log.info("Response 获取" + platform + "远程游戏金额异常，金额为0,返回消息:" + json);
            return balance;
        }
        balance = json.getDouble("balance");
        log.info("Response 获取" + platform + "远程游戏金额,返回消息:" + json);
        return balance;
    }
    
		
	//map按key排序
	public static Map<String, Object> sortMap(Map<String, Object> map) {
		Map<String, Object> newMap = new LinkedHashMap<String, Object>();
		Object[] key = map.keySet().toArray();
		Arrays.sort(key);
		for (int k = 0; k < key.length; k++) {
			newMap.put((String)key[k], map.get(key[k]));
		}
		return newMap;
	}
	
	private static String httpPostRequest(URL url, Map<String,Object> params) throws IOException
	{
		StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(param.getKey());
            postData.append('=');
            postData.append(param.getValue());
        }
        log.info("请求URL："+url.toString()+postData);
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
        return resInfo.toString();    
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(kyqpLogin("devtest999","KYQP","127.0.0.1"));
		//System.out.println(getBalance("devtest999", "VR", "8.8.8.8"));
    }

}
