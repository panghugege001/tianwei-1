package com.nnti.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.nnti.common.exception.PostFailedException;
import com.nnti.common.exception.ResponseFailedException;
import com.nnti.common.security.MySecureProtocolSocketFactory;

public class HttpUtils {
	static Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);

	private static Log log = LogFactory.getLog(HttpUtils.class);
	public static String ENCODE_TYPE = "UTF-8";
	private static Integer TIME_OUT = 50000;
	private static Integer TIME_OUT_SHORT = 5000;
	private static Integer TIME_OUT_LONG = 60 * 1000;
	//private static String EA_GAME_URL = Configuration.getInstance().getValue("EA_GAME_URL");

	public static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(0, false));
		params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
		params.setParameter("http.socket.timeout", TIME_OUT);
		httpclient.setParams(params);
		return httpclient;
	}

	public static HttpClient createHttpClientShort() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
		params.setParameter("http.socket.timeout", TIME_OUT_SHORT);
		httpclient.setParams(params);
		return httpclient;
	}

	public static HttpClient createHttpClientNoTimeout() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
		httpclient.setParams(params);
		return httpclient;
	}

	public static String get(String action, Map<String, String> params)
			throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		String url = getURL(action, params);
		GetMethod method = new GetMethod(url);
		HttpClient httpclient = createHttpClient();
		try {
			if (url == null)
				throw new PostFailedException("URL is null");
			log.info("TOTAL URL:" + method.getURI());
			method.setRequestHeader("Connection", "close");
			statusCode = httpclient.executeMethod(method);
			if (statusCode != 200) {
				log.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode) + ",response:"
						+ method.getResponseBodyAsString());
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("send successfully");
			String responseBody = method.getResponseBodyAsString();
			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}

	public static String getWithNoTimeout(String action, Map<String, String> params)
			throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		String url = getURL(action, params);
		GetMethod method = new GetMethod(url);
		HttpClient httpclient = createHttpClientNoTimeout();
		try {
			if (url == null)
				throw new PostFailedException("URL is null");
			log.info("TOTAL URL:" + method.getURI());
			method.setRequestHeader("Connection", "close");
			statusCode = httpclient.executeMethod(method);
			if (statusCode != 200) {
				log.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("send successfully");
			String responseBody = method.getResponseBodyAsString();
			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}

	public static String getURL(String action, Map<String, String> params) {
		String url = "";

		if (action == null)
			return url;

		if ((params != null) && (params.size() > 0)) {
			url = action + "?";
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = params.get(key);
				try {
					value = URLEncoder.encode(value, ENCODE_TYPE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 如果值为空，则无需传递
				if (StringUtils.isEmpty(value))
					continue;
				if (url.endsWith("?"))
					url = url + key + "=" + value;
				else
					url = url + "&" + key + "=" + value;
			}
		} else {
			url = action;
		}
		return url;
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(URLEncoder.encode("2009-11-19 05:48:06", "GBK"));
		String xml = "<?xml version=\"1.0\"?><request action=\"clogin\"><element id=\"201005240023\"><properties name=\"userid\">elftest01</properties><properties name=\"password\">123123</properties></element></request>";
		// post("http://127.0.0.1:8080/loginValidate", null);
		// postXML("http://test.168.tl/loginValidate", xml);
		// postXMLBySSL(RemoteConstant.CHECKCLIENT_URL, xml);

		System.out.println(new URL("http://sdf.168.tl/sdf/ss").getProtocol());

	}

	public static String post(String action, Map<String, String> params)
			throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		PostMethod post = new PostMethod(action);
		HttpClient httpclient = createHttpClient();
		try {
			if (params != null) {
				Iterator it = params.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					String value = params.get(key);
					post.setParameter(key, value);
				}
			}
			log.info("POST URL:" + post.getURI());
			post.setRequestHeader("Connection", "close");
			statusCode = httpclient.executeMethod(post);
			if (statusCode != 200) {
				log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("post successfully");
			String responseBody = post.getResponseBodyAsString();
			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
	}

	public static String postXMLBySSL(String host, String url, String xml) throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		PostMethod post = new PostMethod(/*EA_GAME_URL +*/ "trade.htm");
		// PostMethod post = new PostMethod(url);
		HttpClient client = null;
		try {
			// log.info("POST URL:" + post.getURI());
			log.info("post xml:" + xml);
			// 指定请求内容的类型
			// post.setRequestHeader("Content-type", "text/xml;charset=" +
			// Constants.ENCODING + "");
			post.setRequestHeader("Content-type","application/x-www-form-urlencoded;charset=" + Constants.ENCODING + "");
			post.setRequestHeader("Connection", "close");
			// 设置发送的内容
			NameValuePair[] param = { new NameValuePair("host", host), new NameValuePair("url", url), new NameValuePair("xml", xml) };
			post.setRequestBody(param);
			// post.setRequestEntity(new StringRequestEntity(xml));
			client = createHttpClient();
			// client.getHostConfiguration().setHost(host, 443, myhttps);
			statusCode = client.executeMethod(post);
			if (statusCode != 200) {
				log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("post successfully");
			String responseBody = post.getResponseBodyAsString();
			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
	}

	public static String postXMLBySSLShort(String host, String url, String xml)
			throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		PostMethod post = new PostMethod(/*EA_GAME_URL + "balance.htm"*/);
		// PostMethod post = new PostMethod(url);
		HttpClient client = null;
		try {
			// log.info("POST URL:" + post.getURI());
			log.info("post xml:" + xml);
			// 指定请求内容的类型
			post.setRequestHeader("Content-type",
					"application/x-www-form-urlencoded;charset=" + Constants.ENCODING + "");
			// post.setRequestHeader("Content-type", "text/xml;charset=" +
			// Constants.ENCODING + "");
			post.setRequestHeader("Connection", "close");
			// 设置发送的内容
			// post.setRequestEntity(new StringRequestEntity(xml));
			NameValuePair[] param = { new NameValuePair("host", host), new NameValuePair("url", url),
					new NameValuePair("xml", xml) };
			post.setRequestBody(param);

			client = createHttpClientShort();
			// client.getHostConfiguration().setHost(host, 443, myhttps);
			statusCode = client.executeMethod(post);
			if (statusCode != 200) {
				log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("post successfully");
			String responseBody = post.getResponseBodyAsString();
			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
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

	public static String postNTwoXMLBySSL(String host, String url, String xml)
			throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		PostMethod post = new PostMethod(url);
		HttpClient client = null;
		try {
			// log.info("POST URL:" + post.getURI());
			log.info("post xml:" + xml);
			// 指定请求内容的类型
			post.setRequestHeader("Content-type", "text/xml;charset=" +Constants.ENCODING + "");
			// 设置发送的内容
			
			post.setRequestEntity(new StringRequestEntity(xml));
			client = createHttpClient();
			client.getHostConfiguration().setHost(host, 443, myhttps);
			statusCode = client.executeMethod(post);
			if (statusCode != 200) {
				log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("post successfully");
			String responseBody = post.getResponseBodyAsString();
			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
	}
	
	public static String postNTwoXMLBySSL_GZIP(String host, String url, String xml)
			throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		PostMethod post = new PostMethod(url);
		HttpClient client = null;
		try {
			// log.info("POST URL:" + post.getURI());
			//log.info("post xml:" + xml);
			// 指定请求内容的类型
			post.setRequestHeader("Content-type", "text/xml;charset=" +Constants.ENCODING + "");
			// 设置发送的内容
			
			post.setRequestEntity(new StringRequestEntity(xml));
			client = createHttpClient();
			client.getHostConfiguration().setHost(host, 443, myhttps);
			statusCode = client.executeMethod(post);
			if (statusCode != 200) {
				log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("post successfully");
			GZIPInputStream gzin;
			String responseBody = null;
			if (post.getResponseBody() != null || post.getResponseBodyAsStream() != null) {
				if(post.getResponseHeader("Content-Encoding") != null && post.getResponseHeader("Content-Encoding").getValue().toLowerCase().indexOf("gzip") > -1) { 
					//For GZip response  
	                InputStream is = post.getResponseBodyAsStream();  
	                gzin = new GZIPInputStream(is);  
	                      
	                InputStreamReader isr = new InputStreamReader(gzin, post.getResponseCharSet());   
	                java.io.BufferedReader br = new java.io.BufferedReader(isr);  
	                StringBuffer sb = new StringBuffer();  
	                String tempbf;  
	                while ((tempbf = br.readLine()) != null) {  
	                	sb.append(tempbf);  
	                    sb.append("\r\n");  
	                }  
	                isr.close();  
	                gzin.close();  
	                responseBody = sb.toString();  
	            }else {  
	            	responseBody = post.getResponseBodyAsString();  
	            } 
			}
			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		} finally {
			if (post != null) {
				post.releaseConnection();
			}
		}
	}
	
	public static String postRequest(String url, String json) throws ClientProtocolException, IOException {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
		StringEntity se = new StringEntity(json, "UTF-8");
		httpPost.setEntity(se);

		CloseableHttpResponse response = client.execute(httpPost);
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity, "UTF-8");

		return result;
	}
	public static String httpGetRequest(String action, Map<String,String> params, Map<String,String> headerParams) throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		String url = getURL(action, params);

		GetMethod method = new GetMethod(url);
		HttpClient httpclient = createHttpClientNoTimeout();
		if (url == null)
			throw new PostFailedException("URL is null");
		try {
			log.info("TOTAL URL:" + method.getURI());
			method.setRequestHeader("Connection", "close");
			for (Map.Entry<String,String> param : headerParams.entrySet()) {
				log.info("header:"+param.getKey()+":"+param.getValue());
				method.setRequestHeader(param.getKey(),param.getValue());
			}
			statusCode = httpclient.executeMethod(method);

			if (statusCode != 200) {
				log.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("send successfully");
			String responseBody = method.getResponseBodyAsString();
			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		}finally{
			if(method!=null){
				method.releaseConnection();
			}
		}
	}
	public static String httpPostRequest(String action, Map<String,Object> params,Map<String,String> headerParams) throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		PostMethod post = new PostMethod(action);
		post.setRequestHeader("Connection", "close");
		for (Map.Entry<String,String> param : headerParams.entrySet()) {
			log.info("header:"+param.getKey()+":"+param.getValue());
			post.setRequestHeader(param.getKey(),param.getValue());
		}
		HttpClient httpclient = createHttpClientNoTimeout();
		if (params != null) {
			Iterator it = params.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = params.get(key).toString();
				post.setParameter(key, value);
			}
		}
		try {
			log.info("POST URL:" + post.getURI());
			statusCode = httpclient.executeMethod(post);
			if (statusCode != 200) {
				log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("post successfully");
			String responseBody = post.getResponseBodyAsString();
			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		}catch (Exception e) {
			e.printStackTrace();
			throw new PostFailedException(e.getMessage());
		}finally{
			if(post!=null){
				post.releaseConnection();
			}
		}
	}
	
	
	
	public static String postHttpsBySSL(String url, String userName, String password)
			throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		PostMethod method = new PostMethod(url);
		HttpClient client = null;
		try {
			method.setRequestHeader("Authorization", "Basic SUdaQVJfYXV0aDpBZjQ5cEtjdzhhVDJ5RWZaVmpScw=="); 
			method.setRequestHeader("Accept", "application/json ;charset=UTF-8");
			method.setRequestHeader("X-DAS-TX-ID","123e4567-e89b-12d3-a456-4266555");
			method.setRequestHeader("X-DAS-CURRENCY", "CNY");
			method.setRequestHeader("X-DAS-TZ", "UTC+8");
			method.setRequestHeader("X-DAS-LANG", "zh_CN");
			method.setParameter("grant_type", "password");
			method.setParameter("username", userName);
			method.setParameter("password", password);
			client = createHttpClient();
			client.getHostConfiguration().setHost(url, 443, myhttps);
			statusCode = client.executeMethod(method);
			System.out.print(statusCode);
			if (statusCode != 200) {
				log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("post successfully");
			String responseBody = null;
			if (method.getResponseBody() != null || method.getResponseBodyAsStream() != null) {
	            	responseBody = method.getResponseBodyAsString();  
			}
			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
	}
}