package com.nnti.office.service.auth;

import java.util.List;
import java.util.Map;

import com.nnti.office.model.auth.Operator;

import dfh.model.ReCaptchaConfig;

public interface OperatorService {
	
	public static String DEFAULT_PASSWORD = "nntipassword";
	
	public String judgeWetherToChangePwd(String loginname);
	
	public Operator getOperatorByUsername(String loginname);
	
	public String validateSms(Operator op, String smsValidPwd);
	
	public String login(Operator operator, String password, String ip);
	
	public List<Operator> getAllOperator();
	
	public List<Operator> searchOperator(Operator operator);

	public void insertOperatorLog(Operator operator, String remark);
	
	public void updateOperator(Operator operator);
	
	public void deleteOperator(String username);
	
	public void insertOperator(Operator operator);
	
	public List<ReCaptchaConfig> getReCaptchaConfig(Map<String, String> map);
	
}
