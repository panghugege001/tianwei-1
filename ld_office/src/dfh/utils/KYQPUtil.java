package dfh.utils;


import dfh.security.EncryptionUtil;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class KYQPUtil {

	private static Logger log = Logger.getLogger(KYQPUtil.class);
	
	  
	public static final String ENCODE_TYPE = "UTF-8";
	public static final String PREFIX = "k";
	public static final String AGENT_CODE ="LD";
	public static final String KEY = "10523d95aa6a43e49cdccc7921c3c3d8";
	public static final String API_URL = "http://datacenter.itgo88.com";
	
	private static final String KYQP_PRODUCTDB = "KYQP_DATA_LD";
	private static final String KYQP_DATAURL = "http://www.pngback.rocks:8686/kyqpdata/";
	private static final String VR_PRODUCTDB = "VR_DATA_LD";
	private static final String VR_DATAURL = "http://www.pngback.rocks:8686/vrdata/";
	private static final String APIKEY = "2!@%!sdfJafadf6SV@AWEx67a";
	
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
        paramMap.put("platform",platform);
        paramMap.put("mcode",AGENT_CODE);
        if(StringUtil.isNotEmpty(ip)) {
        	paramMap.put("ip",ip);
        }else {
        	paramMap.put("ip","8.8.8.8");
        }
       
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
    
    /**
     * 获取下注明细
     * @param username 用户名
     * @param platform 游戏平台
     * @param ip 客户IP
     * @return 余额
     */
    public static List getBetRecord(String platform,String startime,String endtime,Integer page,Integer pagesize) throws IOException {
        URL url= new URL(API_URL+"/api/betrecord/list"+"?");
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("username","");
        paramMap.put("platform",platform);
        paramMap.put("mcode",AGENT_CODE);
        paramMap.put("startime",startime);
        paramMap.put("endtime",endtime);
        paramMap.put("page",page);
        paramMap.put("pagesize",pagesize);
       
        String timestamp = Long.toString(new Date().getTime());
		paramMap.put("timestamp", timestamp);
        Map<String, Object> newMap = sortMap(paramMap);
        log.info("待签名串:"+newMap.toString()+KEY);
		String sign= EncryptionUtil.md5Encrypt(newMap.toString()+KEY);
		paramMap.put("sign", sign);
		String response = httpPostRequest(url, paramMap);
		System.out.println(response);
		JSONObject json = JSONObject.fromObject(response);
	    if (!"0".equals(json.getString("code"))) {
	    	log.info("获取" + platform + "注单明细异常，返回消息:" + json.getString("msg"));
            return null;
	    }else {
	    	JSONObject pageJson = json.getJSONObject("page");
	    	List list = (List) pageJson.get("list");
	    	System.out.println(list);
	    	return list;
	    }
    }
    
    /**
     * 获取下注汇总
     * @param username 用户名
     * @param platform 游戏平台
     * @param ip 客户IP
     * @return 余额
     */
    public static List getBetTotal(String username,String platform,String startime,String endtime) throws IOException {
        URL url= new URL(API_URL+"/api/bettotal/list"+"?");
        Map<String, Object> paramMap = new LinkedHashMap<>();
        paramMap.put("username","");
        paramMap.put("platform",platform);
        paramMap.put("mcode",AGENT_CODE);
        paramMap.put("startime",startime);
        paramMap.put("endtime",endtime);
       
        String timestamp = Long.toString(new Date().getTime());
		paramMap.put("timestamp", timestamp);
        Map<String, Object> newMap = sortMap(paramMap);
        log.info("待签名串:"+newMap.toString()+KEY);
		String sign= EncryptionUtil.md5Encrypt(newMap.toString()+KEY);
		paramMap.put("sign", sign);
		String response = httpPostRequest(url, paramMap);
		System.out.println(response);
		JSONObject json = JSONObject.fromObject(response);
	    if (!"0".equals(json.getString("code"))) {
	    	log.info("获取" + platform + "注单汇总异常，返回消息:" + json.getString("msg"));
            return null;
	    }else {
	    	List list = (List) json.get("datas");
	    	System.out.println(list);
	    	return list;
	    }
    }
    
    
    //调用pngdatafeed工程获取洗码数据
  	public static String getKyqpXimaData(String startTime,String endTime){
  		HttpClient httpClient = HttpUtils.createHttpClient();
  		PostMethod method = new PostMethod(KYQP_DATAURL+"getKyqpXimaData.do");
  		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
  		method.setParameter("productdb", KYQP_PRODUCTDB);
  		method.setParameter("startTime", startTime);
  		method.setParameter("endTime", endTime);
  		String signatureKey = DigestUtils.md5Hex(KYQP_PRODUCTDB + APIKEY);
  		method.setParameter("signature", signatureKey);
  		BufferedReader reader = null;
  		try {
  			httpClient.executeMethod(method);
  			
  			reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));  
  			StringBuffer stringBuffer = new StringBuffer();  
  			String str = "";  
  			while((str = reader.readLine())!=null){  
  			   stringBuffer.append(str);  
  			}  
  			String result = stringBuffer.toString();  
  			if("fail".equals(result)){
  				log.error("调用获取投注额服务加密异常！");
  				return null;
  			}
  			reader.close();
  			return result;
  		} catch (Exception e) {
  			e.printStackTrace();
  		} finally {
  			if (method != null) {
  				method.releaseConnection();
  			}
  		}
  		return null;
  	}	
  	
    //调用pngdatafeed工程获取洗码数据
  	public static String getVRXimaData(String startTime,String endTime,String gameKind){
  		HttpClient httpClient = HttpUtils.createHttpClient();
  		PostMethod method = new PostMethod(VR_DATAURL+"getVRXimaData.do");
  		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
  		method.setParameter("productdb", VR_PRODUCTDB);
  		method.setParameter("startTime", startTime);
  		method.setParameter("endTime", endTime);
  		method.setParameter("gameKind", gameKind);
  		String signatureKey = DigestUtils.md5Hex(VR_PRODUCTDB + APIKEY);
  		method.setParameter("signature", signatureKey);
  		BufferedReader reader = null;
  		try {
  			httpClient.executeMethod(method);
  			
  			reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream()));  
  			StringBuffer stringBuffer = new StringBuffer();  
  			String str = "";  
  			while((str = reader.readLine())!=null){  
  			   stringBuffer.append(str);  
  			}  
  			String result = stringBuffer.toString();  
  			if("fail".equals(result)){
  				log.error("调用获取投注额服务加密异常！");
  				return null;
  			}
  			reader.close();
  			return result;
  		} catch (Exception e) {
  			e.printStackTrace();
  		} finally {
  			if (method != null) {
  				method.releaseConnection();
  			}
  		}
  		return null;
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
		//System.out.println(vRLogin("devtest999","KYQP","127.0.0.1"));
		System.out.println(getBalance("devtest999", "VR", ""));
		String startime = "2018-08-02 11:00:00";
		String endtime = "2018-08-02 23:59:59";
		System.out.println(getBetRecord("VR", startime, endtime, 1, 1000));
		//System.out.println(getBetTotal("","KYQP", startime, endtime));
    }

}
