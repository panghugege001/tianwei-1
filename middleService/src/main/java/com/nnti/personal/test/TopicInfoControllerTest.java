package com.nnti.personal.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;
import com.google.gson.Gson;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.security.AESUtil;
import com.nnti.personal.model.dto.TopicInfoDTO;

public class TopicInfoControllerTest {

//	@Test
//	public void queryList() throws Exception {
//		
//		TopicInfoDTO dto = new TopicInfoDTO();
//		dto.setLoginName("devtest999");
//		dto.setPageNum(1);
//		dto.setPageSize(5);
//		
//		Gson gson = new Gson();
//		String requestJson = gson.toJson(dto);
//		requestJson = AESUtil.encrypt(requestJson);
//		
//		HttpClient client = new HttpClient();
//		PostMethod method = new PostMethod("http://127.0.0.1:10020/topic/queryList");
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
	public void deleteTopic() throws Exception {
		
		TopicInfoDTO dto = new TopicInfoDTO();
		dto.setLoginName("devtest999");
		dto.setIds("1");
		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:9999/topic/deleteTopic");
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
	
	/*
	@Test
	public void queryUnRead() throws Exception {
		TopicInfoDTO dto = new TopicInfoDTO();
		dto.setLoginName("devtest999");

		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:10020/topic/queryUnRead");
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
	public void queryTopicById() throws Exception {
		TopicInfoDTO dto = new TopicInfoDTO();
		dto.setLoginName("devtest999");
		dto.setTopicId(4);
		
		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:10020/topic/queryTopicById");
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
	*/
	
	/*@Test
	public void deleteTopicById() throws Exception {
		TopicInfoDTO dto = new TopicInfoDTO();
		dto.setLoginName("devtest999");
		dto.setTopicId(549448);
		
		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:10020/topic/deleteTopicById");
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
	}*/
	
	/*@Test
	public void save() throws Exception {
		TopicInfoDTO dto = new TopicInfoDTO();
		dto.setLoginName("devtest999");
		dto.setTitle("游戏王");
		dto.setContent("游戏王");
		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:10020/topic/save");
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
	}*/
}
