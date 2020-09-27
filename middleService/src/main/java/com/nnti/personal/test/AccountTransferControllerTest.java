package com.nnti.personal.test;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;
import com.google.gson.Gson;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.security.AESUtil;
import com.nnti.personal.model.dto.AccountTransferDTO;

public class AccountTransferControllerTest {

	@Test
	public void submit() throws Exception {

		AccountTransferDTO dto = new AccountTransferDTO();
		dto.setProduct("yl");
		dto.setLoginName("devtest999");
		dto.setSource("ebetapp");
		dto.setTarget("self");
		dto.setAmount(21.0);

		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://127.0.0.1:8080/self/transfer/submit");
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