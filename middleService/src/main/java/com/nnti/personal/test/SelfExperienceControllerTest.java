package com.nnti.personal.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nnti.common.controller.BaseController;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.security.AESUtil;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.personal.model.dto.AccountTransferDTO;
import com.nnti.personal.model.dto.SelfExperienceDTO;
import com.nnti.personal.service.interfaces.IAccountTransferService;
import com.nnti.personal.service.interfaces.ISelfDepositService;

/**
 * 自助体验金
 * @author Boots
 *
 */
public class SelfExperienceControllerTest extends BaseController {

//	private static Logger log = Logger.getLogger(SelfExperienceController.class);

	
	/**
	 * 提交自助体验金
	 * @param requestData
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@Test
	public void commitPTSelf() throws Exception {
		
		SelfExperienceDTO dto = new SelfExperienceDTO();
		dto.setProduct("yl");
		dto.setLoginName("devtest999");
//		dto.setSource("newpt");
//		dto.setTarget("self");
		dto.setPlatform("PT");
		dto.setSid("huawie10010");
		dto.setTitle("自助体验金");
		dto.setChannel("1");
//		dto.setAmount(10.0);
		
		Gson gson = new Gson();
		String requestJson = gson.toJson(dto);
		requestJson = AESUtil.encrypt(requestJson);
		
		HttpClient client = new HttpClient();
    	PostMethod method = new PostMethod("http://127.0.0.1:9999/self/experience/submit");
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