package com.nnti.game.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import com.google.gson.Gson;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.security.AESUtil;
import com.nnti.game.model.dto.UsersDTO;

public class GameCenterControllerTest {

	@Test
	public void loginGameCenter() throws Exception {
		
		UsersDTO dto = new UsersDTO();
		dto.setLoginName("devtest999");
		dto.setPassword("a123a123");
		
		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:9999/gameCenter/loginGameCenter");
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