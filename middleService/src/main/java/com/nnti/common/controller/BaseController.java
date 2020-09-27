package com.nnti.common.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nnti.common.constants.FunctionCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.model.dto.MessageDTO;
import com.nnti.common.security.AESUtil;

public class BaseController {

	protected final String SUCCESS = "SUCCESS";

	/**
	 * 执行失败，直接抛出业务异常
	 */
	public void failure(String code, String message) throws BusinessException {

		throw new BusinessException(code, message);
	}

	/**
	 * 执行成功，直接返回业务数据
	 */
	public DataTransferDTO success(Object obj) throws BusinessException {

		return success(FunctionCode.SC_10000.getCode(), null, obj);
	}

	public DataTransferDTO success(String code, String message, Object obj) throws BusinessException {

		MessageDTO dto = new MessageDTO();
		dto.setCode(code);
		dto.setMessage(message);
		dto.setData(obj);

		String result = null;

		try {

			Gson gson = new GsonBuilder().create();
			result = gson.toJson(dto);
		} catch (Exception e) {

			throw new BusinessException(FunctionCode.SC_10001.getCode(), "数据转换异常！");
		}

		result = AESUtil.encrypt(result);

		return new DataTransferDTO(result);
	}
}