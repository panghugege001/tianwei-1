package com.nnti.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import net.sf.json.JSONObject;

public class MGUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(MGUtil.class);
	
	
	private static final String MGURL = "https://ag.adminserv88.com";
	private static final String MGMEMBERURL = MGURL+"/member-api-web/member-api";
	private static final String j_username = "dydev";
	private static final String j_password = "QAZqazQAZ";
	private static final Integer ID = 119897644;
	private static final String MA = "ma";
	private static final String PREFIX = "DY8";
	private static final String CURRENCY = "CNY";
	private static final String LANGUAGE = "zh";
	
	public static String HttpSend(String path, String obj, String token,String type) {
		try {
			// 创建连接
			URL url = new URL(path);
			HttpURLConnection connection;
			StringBuffer sbuffer = null;
			// 添加 请求内容
			connection = (HttpURLConnection) url.openConnection();
			// 设置http连接属性
			connection.setDoOutput(true);// http正文内，因此需要设为true, 默认情况下是false;
			connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
			if("PUT".equals(type)){
				connection.setRequestMethod("PUT"); // 可以根据需要 提交// GET、POST、DELETE、PUT等http提供的功能
				connection.setRequestProperty("Accept-Charset", "utf-8"); // 设置编码语言
				connection.setRequestProperty("X-Requested-With","X-Api-Client");
				connection.setRequestProperty("X-Api-Call", "X-Api-Client");
				connection.setRequestProperty("X-Api-Auth", token);  //设置请求的token
				connection.setRequestProperty("Content-Type", " application/json");// 设定// 请求格式// json，也可以设定xml格式的
			}
			if("POST".equals(type)){
				connection.setRequestMethod(type);
				connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8"); 
			}
			connection.setRequestProperty("Connection", "keep-alive"); // 设置连接的状态
			connection.setRequestProperty("Content-Length", obj.getBytes().length + ""); // 设置文件请求的长度

			connection.setReadTimeout(10000);// 设置读取超时时间
			connection.setConnectTimeout(10000);// 设置连接超时时间
			connection.connect();
			OutputStream out = connection.getOutputStream();// 向对象输出流写出数据，这些数据将存到内存缓冲区中
			out.write(obj.getBytes()); // out.write(new String("测试数据").getBytes());
												  //刷新对象输出流，将任何字节都写入潜在的流中
			out.flush();
			// 关闭流对象,此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
			out.close();
			// 读取响应
			if (connection.getResponseCode() == 200) {
				// 从服务器获得一个输入流
				InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());// 调用HttpURLConnection连接对象的getInputStream()函数,
				// 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
				BufferedReader reader = new BufferedReader(inputStream);

				String lines;
				sbuffer = new StringBuffer("");

				while ((lines = reader.readLine()) != null) {
					lines = new String(lines.getBytes(), "utf-8");
					sbuffer.append(lines);
				}
				reader.close();
			} else {
				log.info("请求失败,响应代码：" + connection.getResponseCode());
				log.info("请求失败,错误内容："+connection.getResponseMessage());
				return null;
			}
			// 断开连接
			connection.disconnect();
			return sbuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(0, false));
		params.setParameter("http.protocol.content-charset", "UTF-8");
		params.setParameter("http.socket.timeout", 100000);
		httpclient.setParams(params);
		return httpclient;
	}
	
	/**
	 * 发送post请求
	 * @param method api名称
	 * @param parameters 参数
	 * @return
	 */
	private static String sendPost(String url, Map<String, String> params) {
		HttpClient httpClient = createHttpClient();
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("X-Requested-With", "X-Api-Client"); 
		method.setRequestHeader("X-Api-Call", "X-Api-Client");
		method.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		method.setRequestHeader("Connection", "close");
		method.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		
		NameValuePair[] data = new NameValuePair[params.size()];

		int i = 0;

		for (String pk : params.keySet()) {

			data[i] = new NameValuePair(pk, params.get(pk));
			i++;
		}
		method.setRequestBody(data);
		try {
			httpClient.executeMethod(method);

			int responseCode = method.getStatusCode();
			log.info("请求的url:" + url);
			log.info("请求参数:" + params.toString());
			log.info("响应代码:" + responseCode);
			return method.getResponseBodyAsString();  
		} catch (Exception e) {
			log.error("请求第三方异常:"+e.getMessage());
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return "请求第三方异常";
	}
	
	
	
	public static String getToken(){
		String url = MGURL+"/lps/j_spring_security_check";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("j_username", j_username);
		params.put("j_password", j_password);
		
		String result = sendPost(url, params);
		
		JSONObject json = JSONObject.fromObject(result);
		String token = json.getString("token");
		return token;
	}
	
	public static String createPlayerName(String loginname,String password){
		String url = MGURL+"/lps/secure/network/"+ID+"/downline";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("crId", ID);
		params.put("crType", MA);
		params.put("neId", ID);
		params.put("neType", MA);
		params.put("tarType", "m");
		params.put("username", PREFIX+loginname);
		params.put("name", PREFIX+loginname);
		params.put("password", password);
		params.put("confirmPassword", password);
		params.put("currency", CURRENCY);
		params.put("language", LANGUAGE);
		params.put("email", "");
		params.put("mobile", "");
		Map<String,Boolean> m1 = new HashMap<String,Boolean>();
		m1.put("enable", true);
		params.put("casino", m1);
		Map<String,Boolean> m2 = new HashMap<String,Boolean>();
		m2.put("enable", false);
		params.put("poker", m2);
		
		JSONObject jsonObject = JSONObject.fromObject(params);
		
		String result = HttpSend(url, jsonObject.toString(), getToken(),"PUT");
		return result;
	}
	
	
	public static String Login(String loginname,String password){
		String xml = "<mbrapi-login-call "+
						"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
						"apiusername=\"apiadmin\" "+
						"apipassword=\"apipassword\" "+
						"username=\""+PREFIX+loginname+"\" "+
						"password=\""+password+"\" "+
						"ipaddress=\"127.0.0.1\" "+
						"partnerId=\"88KIOSK\" "+
						"currencyCode=\""+CURRENCY+"\" "+
					  "/>";
		
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		String status = parseXml(result, "/mbrapi-login-resp", "status");
		if("2003".equals(status)){//账号不存在则创建一个
			createPlayerName(loginname, password);
			return Login(loginname, password);
		}
		if("0".equals(status)){
			String token = parseXml(result, "/mbrapi-login-resp", "token");
			return token;
		}
		return null;
	}
	
	//修改玩家密码
	public static Boolean changepassword(String loginname,String oldpassword,String newpassword){
		String token = Login(loginname, oldpassword);
		
		String xml = "<mbrapi-changepassword-call "+
				"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
				"apiusername=\"apiadmin\" "+
				"apipassword=\"apipassword\" "+
				"token=\""+token+"\" "+
				"oldpassword=\""+oldpassword+"\" "+
				"newpassword=\""+newpassword+"\" "+
			  "/>";
		
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		System.out.println(result);
		String status = parseXml(result, "/mbrapi-changepassword-resp", "status");
		if("0".equals(status)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	//demoMode为true是试玩，为false是真钱
	public static String launchGameUrl(String loginname,String password,String gameid,String demoMode){
		String token = Login(loginname, password);
		
		String xml = "<mbrapi-launchurl-call "+
				"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
				"apiusername=\"apiadmin\" "+
				"apipassword=\"apipassword\" "+
				"token=\""+token+"\" "+
				"language=\""+LANGUAGE+"\" "+
				"gameId=\""+gameid+"\" "+
				"bankingUrl=\"www.banking.com\" "+
				"lobbyUrl=\"www.lobby.com\" "+
				"logoutRedirectUrl=\"www.logout.com\" "+
				"demoMode=\""+demoMode+"\" "+
			  "/>";

		System.out.println(xml);
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		String gameurl = parseXml(result, "/mbrapi-launchurl-resp", "launchUrl");
		return gameurl;
	}
	
	public static Double getBalance(String product,String loginname,String password){
		String token = Login(loginname, password);
		if(token == null){
			return null;
		}
		String xml = "<mbrapi-account-call "+
						"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
						"apiusername=\"apiadmin\" "+
						"apipassword=\"apipassword\" "+
						"token=\""+token+"\" "+
					  "/>";

		//System.out.println(xml);
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		String balanceStr = parseXml(result,"/mbrapi-account-resp/wallets/account-wallet","credit-balance");
		if(balanceStr != null){
			return Double.parseDouble(balanceStr);
		}
		return null;
	}
	
	//转入MG
	public static String transferToMG(String product,String loginname,String password,Double amount,String tx_id){
		String token = Login(loginname, password);
		
		String xml = "<mbrapi-changecredit-call "+
						"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
						"apiusername=\"apiadmin\" "+
						"apipassword=\"apipassword\" "+
						"token=\""+token+"\" "+
						"product=\"casino\" "+
						"operation=\"topup\" "+
						"amount=\""+amount+"\" "+
						"tx-id=\""+tx_id+"\" "+
					  "/>";

		//System.out.println(xml);
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		String status = parseXml(result,"/mbrapi-changecredit-resp","status");
		if("0".equals(status)){
			return null;
		}
		return "fail";
	}
	
	//MG转出
	public static String tranferFromMG(String product,String loginname,String password,Double amount,String tx_id){
		String token = Login(loginname, password);
		
		String xml = "<mbrapi-changecredit-call "+
						"timestamp=\""+DateUtil.getYYMMDDHHmmssSSS(new Date()) +" UTC\" "+
						"apiusername=\"apiadmin\" "+
						"apipassword=\"apipassword\" "+
						"token=\""+token+"\" "+
						"product=\"casino\" "+
						"operation=\"withdraw\" "+
						"amount=\""+amount+"\" "+
						"tx-id=\""+tx_id+"\" "+
					  "/>";

		//System.out.println(xml);
		String result =HttpSend(MGMEMBERURL, xml, "", "POST");
		String status = parseXml(result,"/mbrapi-changecredit-resp","status");
		if("0".equals(status)){
			return null;
		}
		return "fail";
	}
	
	public static String parseXml(String xml,String path,String value) {
		xml = StringUtils.trimToEmpty(xml);
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element element = (Element) doc.getRootElement().selectSingleNode(path);
			String balance = element.attributeValue(value);
			return balance;
		} else {
			log.info("document parse failed:" + xml);
		}
		return null;
	}


/*	public static Double getBetsAmount(String product, String loginName, String startTime, String endTime) {

		Double betAmount = 0.0;

		HashMap<String, String> map = mgMap.get(product);
		String mgconsole = map.get("mgconsole");

		Map<String, String> paramsMap = new HashMap<String, String>();

		paramsMap.put("loginname", loginName);
		paramsMap.put("startTime", startTime);
		paramsMap.put("endTime", endTime);
		paramsMap.put("sn", String.valueOf(System.currentTimeMillis()));

		try {

			String result = requestMGConsole(product, paramsMap, mgconsole + "getBetsAmount.php");
//			log.info("玩家" + loginName + "执行getBetsAmount方法，result参数值为：" + result);

			if (StringUtils.isNotEmpty(result)) {

				JSONObject json = JSONObject.fromObject(result);

				if ("1".equalsIgnoreCase(String.valueOf(json.get("code")))) {

					betAmount = NumericUtil.div(-Double.parseDouble(String.valueOf(json.get("info"))), 100);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			log.info("玩家" + loginName + "执行getBetsAmount方法发生异常，异常信息：" + e.getMessage());
		}

		return betAmount;
	}*/


	public static void main(String[] args) throws Exception {
		String loginname = "dytest01";
		String oldpassword = "cf802424ab1302f188bd1ffa77cf6c54";
		String newpassword = "cf802424ab1302f188bd1ffa77cf6c54";
//		System.out.println(transferToMG("dy",loginname, newpassword,2077.0,System.currentTimeMillis()+loginname));
		System.out.println(tranferFromMG("dy",loginname, newpassword,12.0,System.currentTimeMillis()+loginname));
		System.out.println(getBalance("dy",loginname, newpassword));
		
		//System.out.println(tranferFromMG("qy", "devtest999", 1.00));
	}
}