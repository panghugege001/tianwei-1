package com.nnti.personal.service.interfaces;

import com.nnti.personal.model.dto.ExperienceSmsDTO;
import com.nnti.personal.model.vo.ExperienceSms;

public interface IExperienceSmsService {

	/**
	 * 新增短信
	 */
	void saveExperienceSms(ExperienceSmsDTO dto) throws Exception;

	/**
	 * 获取短信
	 */
	ExperienceSms getExperienceSms(ExperienceSmsDTO dto) throws Exception;

	String getPhoneAndValidateCode(String loginName) throws Exception;

	String checkValidateCode(String loginName) throws Exception;
}