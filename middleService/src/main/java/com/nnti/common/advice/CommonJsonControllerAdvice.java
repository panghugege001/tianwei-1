package com.nnti.common.advice;

import com.google.gson.Gson;
import com.nnti.common.constants.FunctionCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.model.dto.MessageDTO;
import com.nnti.common.security.AESUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(basePackages = { "com.nnti.personal.controller", "com.nnti.withdraw.controller",
		"com.nnti.game.controller" })
public class CommonJsonControllerAdvice {

	private static Logger log = Logger.getLogger(CommonJsonControllerAdvice.class);

	@InitBinder
	public void initAdvice(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String requestJsonData = request.getParameter("requestData");

		if (StringUtils.isNotEmpty(requestJsonData)) {

			requestJsonData = AESUtil.decrypt(requestJsonData);
			request.setAttribute("requestJsonData", requestJsonData);
		}
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public DataTransferDTO exceptionAdvice(HttpServletRequest request, HttpServletResponse response, Exception e) {

		String requestForApi = request.getServletPath();
		log.error("请求接口：" + requestForApi + "，进入exceptionAdvice方法，异常信息：" + e.getMessage());

		BusinessException be = null;

		if (e instanceof BusinessException) {

			be = (BusinessException) e;
		} else if (e instanceof NullPointerException) {

			be = new BusinessException(FunctionCode.SC_10001.getCode(), "系统繁忙！");
		} else {

			be = new BusinessException(FunctionCode.SC_10001.getCode(), "系统繁忙！！");
		}

		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(be.getCode());
		messageDTO.setMessage(be.getMessage());

		Gson gson = new Gson();
		String str = gson.toJson(messageDTO);

		try {

			str = AESUtil.encrypt(str);

			return new DataTransferDTO(str);
		} catch (Exception ex) {

			log.error("CommonJsonControllerAdvice类的exceptionAdvice方法对数据进行加密时出现异常，异常信息：" + ex.getMessage());
		}

		return null;
	}
}