package com.nnti.controller;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;
import com.google.gson.Gson;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.security.AESUtil;

public class CommonControllerTest {

	@Test
	public void deposit() throws Exception {

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("loginName", "devtest999");
		paramsMap.put("startTime", "2017-01-01 00:00:00");
		paramsMap.put("endTime", "2017-09-27 00:00:00");

		Gson gson = new Gson();

		String requestJson = gson.toJson(paramsMap);

		requestJson = AESUtil.encrypt(requestJson);

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:8080/common/deposit");
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