package dfh.utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.params.CoreConnectionPNames;

import dfh.exception.PostFailedException;
import dfh.exception.ResponseFailedException;
import dfh.security.MySecureProtocolSocketFactory;

public class HttpUtils {
	static Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);

	private static Log log = LogFactory.getLog(HttpUtils.class);
	public static String ENCODE_TYPE = "UTF-8";
	private static Integer TIME_OUT = 50000;
	private static Integer TIME_OUT_SHORT = 5000;

	public static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
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

	public static String get(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
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
				log.warn("the http status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode) + ",response:" + method.getResponseBodyAsString());
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
			if (method != null) {
				method.releaseConnection();
			}
		}
	}
	
	public static String get(String action) throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		GetMethod get = new GetMethod(action);
//		get.setRequestHeader("Connection", "close");
		HttpClient httpclient = new HttpClient() ;
		try {
			log.info("GET URL:" + get.getURI());
			statusCode = httpclient.executeMethod(get);
			if (statusCode != 200) {
				log.warn("the get status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the get result status is not OK");
			}
			log.info("post successfully");
			String responseBody = get.getResponseBodyAsString();
//			log.info("get response sucessfully,reponse:" + responseBody);
			return responseBody;
		}catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		}finally{
			if(get!=null){
				get.releaseConnection();
			}
		}
	}

	public static String getWithNoTimeout(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
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
		}finally{
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

	public static String post(String action, Map<String, String> params) throws PostFailedException, ResponseFailedException {
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
			return responseBody;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseFailedException(e.getMessage());
		}finally{
			if (post != null) {
				post.releaseConnection();
			}
		}
	}

	public static String postXMLBySSL(String host, String url, String xml) throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		PostMethod post = new PostMethod(url);
		HttpClient client = null;
		try {
			log.info("POST URL:" + post.getURI());
			log.info("post xml:" + xml);
			// 指定请求内容的类型
			post.setRequestHeader("Content-type", "text/xml;charset=" + Constants.ENCODING + "");
			post.setRequestHeader("Connection", "close");
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
		}finally{
			if (post != null) {
				post.releaseConnection();
			}
		}
	}
	
	public static String postXMLBySSLShort(String host, String url, String xml) throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		PostMethod post = new PostMethod(url);
		HttpClient client = null;
		try {
			log.info("POST URL:" + post.getURI());
			log.info("post xml:" + xml);
			// 指定请求内容的类型
			post.setRequestHeader("Content-type", "text/xml;charset=" + Constants.ENCODING + "");
			post.setRequestHeader("Connection", "close");
			// 设置发送的内容
			post.setRequestEntity(new StringRequestEntity(xml));
			client = createHttpClientShort();
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
		}finally{
			if (post != null) {
				post.releaseConnection();
			}
		}
	}
}