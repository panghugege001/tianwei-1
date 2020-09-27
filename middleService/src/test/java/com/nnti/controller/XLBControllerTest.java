package com.nnti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nnti.common.security.AESUtil;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.pay.controller.vo.PayRequestVo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

import java.util.Map;

/**
 * Created by wander on 2017/1/25.
 */
public class XLBControllerTest {

	private ObjectMapper JSON = new ObjectMapper();

	@Test
	public void zfb_wx() throws Exception {

		String url = "http://localhost:7080/xlb/zfb_wx";
		PayRequestVo vo = new PayRequestVo();
		vo.setLoginName("woodytest");
		vo.setPlatformId(43L);
		vo.setOrderAmount("10.00");
		vo.setCustomerIp("127.0.0.1");

		Gson gson = new Gson();
		String requestJson = gson.toJson(vo);
		requestJson = AESUtil.encrypt(requestJson);

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(url);
		method.setParameter("requestData", requestJson);

		int statusCode = client.executeMethod(method);
		if (statusCode != HttpStatus.SC_OK) {
			System.out.println("Method failed：" + method.getStatusLine());
		}

		byte[] responseBody = method.getResponseBody();
		String responseString = new String(responseBody);

		System.out.println("返回的明文json数据：" + responseString);
	}

	public static void main(String[] args) {
		System.out.println("aaaaaaaaaaabbbbbbb");
	}

	@Test
	public void zfb_wx_return() throws Exception {

		// String url = "http://206.161.248.35:8080/xb/zfb_wx_return";
		String url = "http://60.12.123.52:3113/xb/zfb_wx_return.php";
		Map map = new HashedMap();
		map.put("test", "sadfsd");

		String responseString = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(map));

		System.out.println("返回的明文json数据：" + responseString);
	}

	public void test(){
        System.out.println("dddd");
    }
}
