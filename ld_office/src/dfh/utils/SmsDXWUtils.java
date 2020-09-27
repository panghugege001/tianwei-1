package dfh.utils;


import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;



public class SmsDXWUtils {

	public static final String URL = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
	public static final String ac="1001@501113300015";
	public static final String authkey="257D62C5E1F23A2DC8D5110386BF42BF";
	public static final String action="sendOnce";
	public static final String cgid="7738";
	
	public static String sendSms(String phoneNum, String text){
		
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("action", action);
		method.setParameter("ac", ac);
		method.setParameter("authkey", authkey);
		method.setParameter("cgid", cgid);
		method.setParameter("c", text);
		method.setParameter("m", phoneNum);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			Document doc = DocumentHelper.parseText(result);
			Element root = doc.getRootElement();
			String code = root.attributeValue("result");
			
			return code;

		} catch (HttpException e) {
			e.printStackTrace();
			return SmsDXWUtils.sendSmsOnce(phoneNum, text);
		} catch (IOException e) {
			e.printStackTrace();
			return SmsDXWUtils.sendSmsOnce(phoneNum, text);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 只发送一次
	 * */
	public static String sendSmsOnce(String phoneNum, String text){
		
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(URL);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.setParameter("action", action);
		method.setParameter("ac", ac);
		method.setParameter("authkey", authkey);
		method.setParameter("cgid", cgid);
		method.setParameter("c", text);
		method.setParameter("m", phoneNum);
		try {
			httpClient.executeMethod(method);
			String result = method.getResponseBodyAsString();
			Document doc = DocumentHelper.parseText(result);
			Element root = doc.getRootElement();
			String code = root.attributeValue("result");
			
			return code;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws DocumentException {

		System.out.println(SmsDXWUtils.sendSms("111", "乐#虎+-*/!@#$%^&*()_+{}[]"));
	}
	
	
}
