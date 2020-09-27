package com.nnti.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class BBinUtils {
	private static Logger log = Logger.getLogger(BBinUtils.class);
	
	private static final String URL="http://api.tcy789.com/direct.aspx";
	private static final String LOGINURL="http://777.tcy789.com";
//	private static final String USERKEY="d3w1e5tyusp";
	private static final String USERKEY="45erq1@98$";
	private static final String PREFIX = "DY8";
	private static final String AGENT = "dayun";
	private static final String ENCODE_TYPE = "UTF-8";
	
    
    public static String matcher(String pattern, String verifyData) {

        verifyData = verifyData.replaceAll("[\r\n]", "");

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(verifyData);
        String result = "";
        while (m.find()) {
            result += m.group(1) + ",";
        }
        if (result.length() > 1) {
            return result.substring(0, result.length() - 1);
        }
        return result;
    }
    //是否拼接value为空的key
	public static <T> String obj2UrlParam(Map<String,Object> map) {
		StringBuilder and = new StringBuilder();

		for (Map.Entry<String,Object> param : map.entrySet()) {
            if (and.length() != 0){
            	and.append('$');
            }
            and.append(param.getKey());
            and.append('=');
            and.append(param.getValue());
        }

		return and.toString();
	}
	
	public static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(0, false));
		params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
		params.setParameter("http.socket.timeout", 100000);
		httpclient.setParams(params);
		return httpclient;
	}
	
	/**
	 * 发送get请求
	 * @param method api名称
	 * @param parameters 参数
	 * @return
	 */
	private static String sendApi(String url) {

		HttpClient httpClient = createHttpClient();
		GetMethod med = new GetMethod(url);
		med.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		BufferedReader reader = null;
		try {
			httpClient.executeMethod(med);
			reader = new BufferedReader(new InputStreamReader(med.getResponseBodyAsStream()));  
			StringBuffer stringBuffer = new StringBuffer();  
			String str = "";  
			while((str = reader.readLine())!=null){  
			   stringBuffer.append(str);  
			}
			String result = stringBuffer.toString();
			int responseCode = med.getStatusCode();
			log.info("请求的url:" + url);
			log.info("响应代码:" + responseCode);
			log.info("响应报文:"+result);
			if(responseCode != 200){
				return null;
			}
			return result;
		} catch (Exception e) {
			log.error("请求第三方异常:"+e.getMessage());
			e.printStackTrace();
		} finally {
			if (med != null) {
				med.releaseConnection();
			}
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static Boolean CheckAndCreateAccount(String loginname){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("agent", AGENT);
		map.put("username", PREFIX+loginname);
		map.put("moneysort", "RMB");
		map.put("password", "12345678");
		map.put("method", "caca");
		
		String params = obj2UrlParam(map);
		try {
			params = new String(Base64.encodeBase64(params.getBytes(ENCODE_TYPE)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
		String key = DigestUtils.md5Hex(params+USERKEY).toLowerCase();
		String geturl = URL+"?params="+params+"&key="+key;
		String result = sendApi(geturl);
		if (StringUtils.isNotBlank(result)) {
			String status = matcher("<result>(.*?)</result>", result);
			if ("1".equals(status)) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} else {
			return Boolean.FALSE;
		}
	}
	
	public static Double GetBalance(String product,String loginname){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("agent", AGENT);
		map.put("username", PREFIX+loginname);
		map.put("moneysort", "RMB");
		map.put("password", "12345678");
		map.put("platformname", "bbin");
		map.put("method", "gb");
		
		String params = obj2UrlParam(map);
		try {
			params = new String(Base64.encodeBase64(params.getBytes(ENCODE_TYPE)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("bbin获取余额异常"+e.getMessage());
			return null;
		}
		String key = DigestUtils.md5Hex(params+USERKEY).toLowerCase();
		String geturl = URL+"?params="+params+"&key="+key;
		String result = sendApi(geturl);
		if (StringUtils.isNotBlank(result)) {
			result = matcher("<result>(.*?)</result>", result);
			if (result.contains(".")) {
				return Double.parseDouble(result);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static Boolean transferToBbin(String product,String loginname,Double money,String tranferno){
		return TransferCredit(loginname, "IN", money, tranferno);
	}
	
	public static Boolean transferFromBbin(String product,String loginname,Double money,String tranferno){
		return TransferCredit(loginname, "OUT", money, tranferno);
	}
	
	
	public static Boolean TransferCredit(String loginname,String type,Double money,String tranferno){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("agent", AGENT);
		map.put("username", PREFIX+loginname);
		map.put("moneysort", "RMB");
		map.put("password", "12345678");
		map.put("billno", tranferno);
		map.put("type", type);
		map.put("usertype", 1);//1 正常 0 测试
		map.put("credit", money);
		map.put("platformname", "bbin");
		map.put("method", "ptc");
		
		String params = obj2UrlParam(map);
		log.info("bbin转账请求参数："+params);
		try {
			params = new String(Base64.encodeBase64(params.getBytes(ENCODE_TYPE)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
		String key = DigestUtils.md5Hex(params+USERKEY).toLowerCase();
		String geturl = URL+"?params="+params+"&key="+key;
		String result = sendApi(geturl);
		if (StringUtils.isNotBlank(result)) {
			String status = matcher("<result>(.*?)</result>", result);
			if ("1".equals(status)) {
				return Boolean.TRUE;
			}else if("2".equals(status)){
				//调ConfirmTransferCredit
				return ConfirmTransferCredit(loginname, tranferno);
			}else {
				return Boolean.FALSE;
			}
		} else {
			return Boolean.FALSE;
		}
	}
	
	//查询转帐
	public static Boolean ConfirmTransferCredit(String loginname,String tranferno){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("agent", AGENT);
		map.put("username", PREFIX+loginname);
		map.put("moneysort", "RMB");
		map.put("password", "12345678");
		map.put("billno", tranferno);
		map.put("method", "ctc");
		
		String params = obj2UrlParam(map);
		try {
			params = new String(Base64.encodeBase64(params.getBytes(ENCODE_TYPE)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
		String key = DigestUtils.md5Hex(params+USERKEY).toLowerCase();
		String geturl = URL+"?params="+params+"&key="+key;
		String result = sendApi(geturl);
		if (StringUtils.isNotBlank(result)) {
			String status = matcher("<result>(.*?)</result>", result);
			if ("1".equals(status)) {
				return Boolean.TRUE;
			}else {
				return Boolean.FALSE;
			}
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * 进入游戏
	 * @param loginname
	 * @param domain
	 * @param gametype1.视讯2.体育3.彩票4.电子游戏5捕鱼游戏
	 * @param gamekind 默认为 0(详细见bbin电子游戏种类表)
	 * @param iframe 默认为 0,如果采用 iframe框架请设置为 1, https非框架iframe=2,框架 iframe=3
	 * @return
	 */
	public static String TransferGame (String loginname,String domain){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("agent", AGENT);
		map.put("username", PREFIX+loginname);
		map.put("moneysort", "RMB");
		map.put("password", "12345678");
		map.put("domain", domain);
		map.put("gametype", 4);
		map.put("gamekind", 0);
		map.put("iframe", 0);
		map.put("platformname", "bbin");
		map.put("lang", "zh");
		map.put("method", "tg");
		
		String params = obj2UrlParam(map);
		try {
			params = new String(Base64.encodeBase64(params.getBytes(ENCODE_TYPE)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		String key = DigestUtils.md5Hex(params+USERKEY).toLowerCase();
		String geturl = URL+"?params="+params+"&key="+key;
		String result = sendApi(geturl);
		if (StringUtils.isNotBlank(result)) {
			if (result.startsWith("<html>")) {
				int start = result.indexOf("value='");
				int end = result.indexOf("'><input");
				String value =result.substring(start+7,end);
				result = LOGINURL+"?uid="+value+"&langx=zh-cn";
				return result;
			}else {
				return null;
			}
		} else {
			return null;
		}
	}


	public static void main(String[] args) {
		String loginname = "dytest01";
//		System.out.println(CheckAndCreateAccount(loginname));
//		System.out.println(transferToBbin("dy",loginname,1.0,System.currentTimeMillis()+"123"));
		System.out.println(transferFromBbin("dy",loginname,1.0,System.currentTimeMillis()+"1"));
		System.out.println(GetBalance("dy",loginname));
		//System.out.println(ConfirmTransferCredit(loginname,"123"));
		//System.out.println(TransferGame(loginname, "www.dayunguoji.com"));
		/*String s4 = "YWdlbnQ9ZGF5dW4kcGFzc3dvcmQ9MTIzNDU2NzgkbWV0aG9kPXB0YyRtb25leXNvcnQ9Uk1CJHVzZXJ0eXBlPTEkdHlwZT1PVVQkY3JlZGl0PTQwODgwLjAkYmlsbG5vPTIzMDkxOTIxMzUzMTkyOTM0NzUkdXNlcm5hbWU9RFk4amE3NDA1NTcwNyRwbGF0Zm9ybW5hbWU9YmJpbg==";
		String s5 = "YWdlbnQ9ZGF5dW4kcGFzc3dvcmQ9MTIzNDU2NzgkbWV0aG9kPXB0YyRtb25leXNvcnQ9Uk1CJHVzZXJ0eXBlPTEkdHlwZT1PVVQkY3JlZGl0PTIwNDAwLjAkYmlsbG5vPTIzMDkxOTIxMzI1ODYzOTM4MTQkdXNlcm5hbWU9RFk4amE3NDA1NTcwNyRwbGF0Zm9ybW5hbWU9YmJpbg==";
		String s6 = "YWdlbnQ9ZGF5dW4kcGFzc3dvcmQ9MTIzNDU2NzgkbWV0aG9kPXB0YyRtb25leXNvcnQ9Uk1CJHVzZXJ0eXBlPTEkdHlwZT1JTiRjcmVkaXQ9MjA0MDAuMCRiaWxsbm89MjMwOTE5MjEzMzE0NzYzNjY0NCR1c2VybmFtZT1EWThqYTc0MDU1NzA3JHBsYXRmb3JtbmFtZT1iYmlu";
		String sa = new String(Base64.decodeBase64(s4));
		String sb = new String(Base64.decodeBase64(s5));
		String sc = new String(Base64.decodeBase64(s6));
		System.out.println(sa);
		System.out.println(sb);
		System.out.println(sc);*/
	}
}
