package com.gsmc.png.utils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gsmc.png.exception.PostFailedException;
import com.gsmc.png.exception.ResponseFailedException;
import com.gsmc.png.security.MySecureProtocolSocketFactory;



public class HttpUtils {
	private static Log log = LogFactory.getLog(HttpUtils.class);
	static Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory(), 443);
	
	public static String ENCODE_TYPE = "UTF-8";
	private static Integer TIME_OUT = 20000;

	public static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", ENCODE_TYPE);
		params.setParameter("http.socket.timeout", TIME_OUT);
		httpclient.setParams(params);
		return httpclient;
	}
	
	public static String postTTGXMLBySSL(String host, Integer port, String url, String affiliateLogin, String affiliateId, String xml)
			throws PostFailedException, ResponseFailedException {
		int statusCode = -999;
		PostMethod post = new PostMethod(url);
		HttpClient client = null;
		try {
			// log.info("POST URL:" + post.getURI());
			//log.info("post xml:" + xml);
			// 指定请求内容的类型
			post.setRequestHeader("T24-Affiliate-Id", affiliateId);
			post.setRequestHeader("T24-Affiliate-Login", affiliateLogin);
			post.setRequestHeader("Content-type", "text/xml");
			// 设置发送的内容
			post.setRequestEntity(new StringRequestEntity(xml));
			client = createHttpClient();
			client.getHostConfiguration().setHost(host, port, myhttps);
			statusCode = client.executeMethod(post);
			if (statusCode != 200) {
				log.warn("the post status is not OK,HttpStatus:" + HttpStatus.getStatusText(statusCode));
				throw new PostFailedException("the post result status is not OK");
			}
			log.info("post successfully");
			String responseBody = post.getResponseBodyAsString();
			log.info("get request:" + xml);
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
	
}