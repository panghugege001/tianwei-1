package com.nnti.office.dao.auth;

import java.util.List;
import java.util.Map;

import com.nnti.office.model.auth.Operator;

import dfh.model.ReCaptchaConfig;

public interface MybatisOperatorDao {
	
	public Operator getOperatorByUsername(String loginname);
	
	public void updateFirstDayWeek(Operator operator);
	
	public void emptySms(Operator operator);
	
	public void updatePasswordNumber(Operator operator);
	
	public void disableOperator(Operator operator);
	
	public void updateAfterLogin(Operator operator);
	
	public List<Operator> getAllOperator();
	
	public List<Operator> searchOperator(Operator operator);
	
	public void updateOperator(Operator operator);
	
	public void resetPasswordWrongTimes(Operator operator);
	
	public void deleteOperator(String username);
	
	public void insertOperator(Operator operator);
	
	public void updateAuthority(Map<String,Object> params);
	
	public List<ReCaptchaConfig> getReCaptchaConfig(Map<String, String> map);
}
