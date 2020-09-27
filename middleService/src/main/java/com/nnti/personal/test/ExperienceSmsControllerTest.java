package com.nnti.personal.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import com.google.gson.Gson;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.security.AESUtil;
import com.nnti.personal.model.dto.ExperienceSmsDTO;

public class ExperienceSmsControllerTest {

//	@Test
//	public void saveSms() throws Exception {
//		ExperienceSmsDTO dto = new ExperienceSmsDTO();
//		dto.setSmsContent("5621");
//		dto.setPhone("13112341234");
//		dto.setGatewayId("COM4");
//		dto.setIpAddress("123.123.123.123");
//		Gson gson = new Gson();
//		String requestJson = gson.toJson(dto);
//		requestJson = AESUtil.encrypt(requestJson);
//
//		HttpClient client = new HttpClient();
//		PostMethod method = new PostMethod("http://127.0.0.1:9999/self/sms/saveSms");
//		method.setParameter("requestData", requestJson);
//
//		int statusCode = client.executeMethod(method);
//
//		if (statusCode != HttpStatus.SC_OK) {
//
//			System.out.println("Method failed：" + method.getStatusLine());
//		}
//
//		byte[] responseBody = method.getResponseBody();
//		String responseString = new String(responseBody);
//
//		DataTransferDTO responseBean = gson.fromJson(responseString, DataTransferDTO.class);
//		responseString = AESUtil.decrypt(responseBean.getResponseData());
//		System.out.println("返回的明文json数据：" + responseString);
//	}
	
	@Test
	public void getExperienceSms() throws Exception {
		ExperienceSmsDTO dto = new ExperienceSmsDTO();
		dto.setPhone("13112341234");
		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:9999/self/sms/getSms");
		method.setParameter("requestData", requestJson);
		
		int statusCode = client.executeMethod(method);
		
		if (statusCode != HttpStatus.SC_OK) {
			
			System.out.println("Method failed：" + method.getStatusLine());
		}
		
		byte[] responseBody = method.getResponseBody();
		String responseString = new String(responseBody);
		
		DataTransferDTO responseBean = gson.fromJson(responseString, DataTransferDTO.class);
		responseString = AESUtil.decrypt(responseBean.getResponseData());
		System.out.println("返回的明文json数据：" + responseString);
	}
	
	
	@Test
	public void checkValidateCode() throws Exception {
		ExperienceSmsDTO dto = new ExperienceSmsDTO();
		dto.setLoginName("boots123");
		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:9999/self/sms/checkValidateCode");
		method.setParameter("requestData", requestJson);
		
		int statusCode = client.executeMethod(method);
		
		if (statusCode != HttpStatus.SC_OK) {
			
			System.out.println("Method failed：" + method.getStatusLine());
		}
		
		byte[] responseBody = method.getResponseBody();
		String responseString = new String(responseBody);
		
		DataTransferDTO responseBean = gson.fromJson(responseString, DataTransferDTO.class);
		responseString = AESUtil.decrypt(responseBean.getResponseData());
		System.out.println("返回的明文json数据：" + responseString);
	}
	
	
	@Test
	public void getPhoneAndCode() throws Exception {
		ExperienceSmsDTO dto = new ExperienceSmsDTO();
		dto.setLoginName("boots123");
		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:9999/self/sms/getPhoneAndValidateCode");
		method.setParameter("requestData", requestJson);
		
		int statusCode = client.executeMethod(method);
		
		if (statusCode != HttpStatus.SC_OK) {
			
			System.out.println("Method failed：" + method.getStatusLine());
		}
		
		byte[] responseBody = method.getResponseBody();
		String responseString = new String(responseBody);
		
		DataTransferDTO responseBean = gson.fromJson(responseString, DataTransferDTO.class);
		responseString = AESUtil.decrypt(responseBean.getResponseData());
		System.out.println("返回的明文json数据：" + responseString);
	}
	
}


