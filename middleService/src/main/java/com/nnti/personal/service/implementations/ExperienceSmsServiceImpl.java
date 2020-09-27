package com.nnti.personal.service.implementations;

import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.common.model.vo.SystemConfig;
import com.nnti.common.model.vo.User;
import com.nnti.common.model.vo.UserStatus;
import com.nnti.common.service.interfaces.ISystemConfigService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.service.interfaces.IUserStatusService;
import com.nnti.common.utils.AESUtil;
import com.nnti.personal.controller.ExperienceSmsController;
import com.nnti.personal.dao.master.IMasterExperienceSmsDao;
import com.nnti.personal.dao.slave.ISlaveExperienceSmsDao;
import com.nnti.personal.model.dto.ExperienceSmsDTO;
import com.nnti.personal.model.vo.ExperienceSms;
import com.nnti.personal.service.interfaces.IExperienceSmsService;

@Service
public class ExperienceSmsServiceImpl implements IExperienceSmsService {

	@Autowired
	private ISlaveExperienceSmsDao slaveExperienceSmsDao;

	@Autowired
	private IMasterExperienceSmsDao masterExperienceSmsDao;

	@Autowired
	private IUserStatusService userStatusService;

	@Autowired
	private ISystemConfigService systemConfigService;

	@Autowired
	private IUserService userService;

	
	private static Logger log = Logger.getLogger(ExperienceSmsServiceImpl.class);
	
	/**
	 * 新增短信
	 */
	public void saveExperienceSms(ExperienceSmsDTO dto) throws Exception {
		ExperienceSms experienceSms = new ExperienceSms();
		Date date = new Date();
		experienceSms.setCreateTime(date);
		experienceSms.setSendTime(date);
		experienceSms.setGatewayId(dto.getGatewayId());
		experienceSms.setIpAddress(dto.getIpAddress());
		experienceSms.setSmsContent(dto.getSmsContent());
		String phone = dto.getPhone();
		if (StringUtils.isNotBlank(phone) && StringUtils.isNumeric(phone)) {
			try {
				//加密电话
				experienceSms.setPhone(AESUtil.aesEncrypt(phone, AESUtil.KEY));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ExperienceSms tempSms = slaveExperienceSmsDao.findExperienceSmsByPhone(experienceSms);
		if (null != tempSms) {
			masterExperienceSmsDao.update(experienceSms);
		} else {
			masterExperienceSmsDao.insert(experienceSms);
		}
	}

	/**
	 * 获取短信
	 */
	public ExperienceSms getExperienceSms(ExperienceSmsDTO dto) throws Exception {
		ExperienceSms experienceSms = new ExperienceSms();
		String phone = dto.getPhone();
		if (StringUtils.isNotBlank(phone) && StringUtils.isNumeric(phone)) {
			try {
				experienceSms.setPhone(AESUtil.aesEncrypt(phone, AESUtil.KEY));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return slaveExperienceSmsDao.findExperienceSmsByPhone(experienceSms);
	}

	/*
	 * 自助体验金短信反转获取验证码
	 * @see com.nnti.personal.service.interfaces.IExperienceSmsService#getPhoneAndValidateCode(java.lang.String)
	 */
	public String getPhoneAndValidateCode(String loginName) throws Exception {
		String code = RandomStringUtils.randomNumeric(4);
		UserStatus userstatus = userStatusService.get(loginName);
		if (userstatus == null) {
			userstatus = new UserStatus();
			userstatus.setLoginName(loginName);
			userstatus.setCashInWrong(0);
			userstatus.setLoginErrorNum(0);
			userstatus.setMailFlag("0");
			userstatus.setValidateCode(code);
			userStatusService.create(userstatus);
		} else if (StringUtils.isNotBlank(userstatus.getValidateCode())) {
			code = userstatus.getValidateCode();
		} else {
			userstatus.setValidateCode(code);
			userStatusService.update(userstatus);
		}
		// 获取手机号
		String phone = "手机号未配置";
		SystemConfig systemConfig = new SystemConfig();
		systemConfig.setTypeNo("type100");
		systemConfig.setFlag("否");
		SystemConfig configs = systemConfigService.get(systemConfig);
		if (null != configs) {
			phone = configs.getValue();
		}
		return "{\"phone\":\"" + phone + "\",\"code\":\"" + code + "\"}";
	}

	/*
	 * 自助体验金短信校验短信号码
	 */
	public String checkValidateCode(String loginName) throws Exception {
		UserStatus userstatus = userStatusService.get(loginName);
		if(null == userstatus)
		{
			return "error:未收到短信,请稍后或者重新发送";
		}
		User user = userService.get(loginName);
		String code = userstatus.getValidateCode();
		ExperienceSms tempSms = new ExperienceSms();
		String phone = AESUtil.aesDecrypt(user.getPhone(), AESUtil.KEY);
		log.info("电话号码："+phone+"用户名："+loginName);
		tempSms.setPhone(user.getPhone());
		ExperienceSms experienceSms = slaveExperienceSmsDao.findExperienceSmsByPhone(tempSms);
		if(null == experienceSms)
		{
			return "error:未收到短信,请稍后或者重新发送";
		}
		String result = experienceSms.getSmsContent();
		Date date = experienceSms.getCreateTime();
		if (StringUtils.isNotBlank(result) && !result.toLowerCase().contains("error")) {
			if (result.contains(code) && (new Date().getTime() - date.getTime()) < 30 * 60 * 1000) {
				userstatus.setValidateCode(null);
				userstatus.setSmsFlag("1");// 持久化验证通过标记到数据库
				userStatusService.update(userstatus);
				return "success";
			} else {
				return "error:短信超时或验证码不正确";
			}
		} else {
			return "error:未收到短信,请稍后或者重新发送";
		}
	}

}