package com.nnti.common.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.security.AESUtil;
import com.nnti.common.service.interfaces.ICommonService;
import com.nnti.common.utils.MyUtils;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {

	@Autowired
	private ICommonService commonService;

	@RequestMapping(value = "/error", produces = "application/html; charset=utf-8")
	public ModelAndView error(String code, String message, String loginname) throws Exception {

		ModelAndView model = new ModelAndView("common/error");

		model.addObject("code", code);
		model.addObject("message", message);

		if (!MyUtils.isNotEmpty(loginname)) {

			model.addObject("message", "请重新登陆");
		}

		return model;
	}

	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/deposit", method = RequestMethod.POST)
	public DataTransferDTO deposit(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {

		if (StringUtils.isNotEmpty(requestData)) {

			String requestJsonData = AESUtil.decrypt(requestData);

			Gson gson = new GsonBuilder().create();

			Map<String, Object> paramsMap = gson.fromJson(requestJsonData, Map.class);

			Double amount = commonService.getDeposit(paramsMap);

			return success(String.valueOf(amount));
		}

		return null;
	}
}