package com.nnti.personal.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nnti.common.constants.FunctionCode;
import com.nnti.common.controller.BaseController;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.personal.model.dto.ExperienceSmsDTO;
import com.nnti.personal.model.dto.SelfExperienceDTO;
import com.nnti.personal.model.vo.ExperienceSms;
import com.nnti.personal.service.interfaces.IExperienceSmsService;

@RestController
@RequestMapping("/self")
public class ExperienceSmsController extends BaseController {

	
	
	
	@Autowired
	private IExperienceSmsService experienceSmsService;
	@Autowired
	IUserService userService;

	private static Logger log = Logger.getLogger(ExperienceSmsController.class);

	@RequestMapping(value = "/sms/saveSms", method = { RequestMethod.POST })
	public DataTransferDTO saveSms(@RequestParam(value = "requestData", defaultValue = "") String requestData,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String requestJsonData = (String) request.getAttribute("requestJsonData");

		Gson gson = new GsonBuilder().create();

		ExperienceSmsDTO dto = gson.fromJson(requestJsonData, ExperienceSmsDTO.class);

		String msg = "";

		try {
			experienceSmsService.saveExperienceSms(dto);
		} catch (Exception e) {
			msg = "保存短信错误，系统繁忙。";
			e.printStackTrace();
		}

		if (StringUtils.isNotBlank(msg)) {

			failure(FunctionCode.SC_20000_111.getCode(), msg);
		}
		
		log.info(msg);

		return success(FunctionCode.SC_10000.getCode(), "保存短信成功！", null);
	}

	@RequestMapping(value = "/sms/getSms", method = { RequestMethod.POST })
	public DataTransferDTO getSms(@RequestParam(value = "requestData", defaultValue = "") String requestData,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String requestJsonData = (String) request.getAttribute("requestJsonData");

		Gson gson = new GsonBuilder().create();

		ExperienceSmsDTO dto = gson.fromJson(requestJsonData, ExperienceSmsDTO.class);

		String msg = "";

		try {

			ExperienceSms experienceSms = experienceSmsService.getExperienceSms(dto);
			msg = experienceSms.getSmsContent();
		} catch (Exception e) {
			e.printStackTrace();
			msg = "获取短信错误，系统繁忙。";
		}

		return success(msg);
	}
	
	/**
	 * 自助体验金短信反转获取验证码
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/sms/getPhoneAndValidateCode", method = { RequestMethod.POST })
	public DataTransferDTO getPhoneAndValidateCode(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");

		Gson gson = new GsonBuilder().create();
		SelfExperienceDTO dto = gson.fromJson(requestJsonData, SelfExperienceDTO.class);

		String loginName = dto.getLoginName();
		
		String msg= experienceSmsService.getPhoneAndValidateCode(loginName);
		
		return success(msg);
	}	
	
	/**
	 * 自助体验金短信校验短信号码
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = "/sms/checkValidateCode", method = { RequestMethod.POST })
	public DataTransferDTO checkValidateCode(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");

		Gson gson = new GsonBuilder().create();
		SelfExperienceDTO dto = gson.fromJson(requestJsonData, SelfExperienceDTO.class);

		String loginName = dto.getLoginName();
		log.info("checkValidateCode产品"+dto.getProduct());
		
		String msg= experienceSmsService.checkValidateCode(loginName);
		
		return success(msg);
	}

}