package com.nnti.office.service.auth.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.office.dao.auth.MybatisOperatorDao;
import com.nnti.office.dao.auth.RoleDao;
import com.nnti.office.model.auth.Operator;
import com.nnti.office.model.auth.UserRole;
import com.nnti.office.service.auth.OperatorService;
import com.nnti.office.service.auth.RoleService;
import com.nnti.office.service.log.OperationLogService;
import com.nnti.office.util.MD5Util;

import dfh.model.ReCaptchaConfig;
import dfh.model.enums.OperationLogType;
import dfh.security.EncryptionUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

@Service
public class OperatorServiceImpl implements OperatorService{
	
	@Autowired
	MybatisOperatorDao operatorDao;
	
	@Autowired
	OperationLogService operationLogService;
	
	@Autowired
	RoleService roleService;
	
	@Autowired
	RoleDao roleDao;


	
	@Override
	public List<ReCaptchaConfig> getReCaptchaConfig(Map<String, String> map) {
		return operatorDao.getReCaptchaConfig(map); 
	}
	
	@Override
	public String judgeWetherToChangePwd(String loginname) {
		Operator op = operatorDao.getOperatorByUsername(loginname);
		//判断7天内是否修改过密码
		if(null == op){
			return null ;
		}
		if(null == op.getFirstDayWeek()){
			op.setFirstDayWeek(DateUtil.getYYYY_MM_DD());
			operatorDao.updateFirstDayWeek(op);
			return "success";
		}
		if((null != op.getFirstDayWeek()) && ((DateUtil.getYYYY_MM_DD().getTime() - op.getFirstDayWeek().getTime())/86400000 < 7)){
			return "success" ;
		}
		return "changePwd";
	}

	@Override
	public Operator getOperatorByUsername(String loginname) {
		return operatorDao.getOperatorByUsername(loginname);
	}
	
	public String validateSms(Operator operator, String smsValidPwd) {
		if(smsValidPwd == null){
			return "请输入短信验证码";
		}
		if(operator.getSmsPwd() == null || operator.getSmsUpdateTime() == null){
			return "请重新发送验证码";
		}
		Date limit = new Date(operator.getSmsUpdateTime().getTime() + 10*60*1000);
		Date now = new Date();
		if(limit.compareTo(now) < 0){
			operator.setSmsPwd(null);
			operator.setSmsUpdateTime(null);
			operatorDao.emptySms(operator);
			return "您短信验证码超时，有效时间为10分钟";
		}
		if(!operator.getSmsPwd().equalsIgnoreCase(smsValidPwd)){
			return "您输入的短信验证码有误";
		}
		return null;
	}
	
	public String login(Operator operator, String password, String ip) {
		String msg = null;
		if (operator == null) {
			msg = "帐号不存在";
		} else if (operator.getEnabled().equals(Constants.FLAG_FALSE)) {
			msg = "帐号已被禁用";
			operator.setSmsPwd(null);//只要登录就清空手机短信验证码。
			operator.setSmsUpdateTime(null);
			operatorDao.emptySms(operator);
		} else if (!operator.getPassword().equals(EncryptionUtil.encryptPassword(password))) {
			//密码不正确超过5次 冻结用户
			if(operator.getPasswordNumber()==null){
				operator.setPasswordNumber(1);
			    operatorDao.updatePasswordNumber(operator);
			}else if(operator.getPasswordNumber()>=5){
				operator.setEnabled(Constants.FLAG_FALSE);
				msg = "账户已被禁用";
				operator.setSmsPwd(null);//只要登录就清空手机短信验证码。
				operator.setSmsUpdateTime(null);
				operatorDao.disableOperator(operator);
				return msg;
			}else{
				int passwordNumber = operator.getPasswordNumber();
				operator.setPasswordNumber(passwordNumber+1);
				operatorDao.updatePasswordNumber(operator);
			}
			msg = "密码不正确";
		} else {
			//如果登录正常，则将登录错误此数置为0次
			operator.setPasswordNumber(0);
			operator.setSmsPwd(null);//只要登录就清空手机短信验证码。
			operator.setSmsUpdateTime(null);
			operator.setLoginTimes(Integer.valueOf(operator.getLoginTimes().intValue() + 1));
			operator.setLastLoginTime(DateUtil.now());
			operator.setLastLoginIp(ip);
			operator.setRandomStr(StringUtil.getRandomString(10));
			operationLogService.insertOperationLog(operator.getUsername(), OperationLogType.LOGIN, ip);
			operatorDao.updateAfterLogin(operator);
		}
		return msg;
	}



	@Override
	public List<Operator> getAllOperator() {
		return operatorDao.getAllOperator();
	}

	@Override
	public List<Operator> searchOperator(Operator operator) {
		return operatorDao.searchOperator(operator);
	}

	@Override
	public void insertOperatorLog(Operator operator, String remark) {

	}

	@Override
	public void updateOperator(Operator operator) {
		Operator operatorInDb = operatorDao.getOperatorByUsername(operator.getUsername()); 
		if(operatorInDb.getEnabled().equals(1) && operator.getEnabled().equals(0)) {
			operator.setPasswordNumber(0);
			operatorDao.resetPasswordWrongTimes(operator);
		}
		operatorDao.updateOperator(operator);
	}

	@Override
	public void deleteOperator(String username) {
		operatorDao.deleteOperator(username);
		roleService.deleteUserRoleByUsername(username);
	}

	@Override
	public void insertOperator(Operator operator) {
		//insert operator
		operator.setCreateTime(new Date());
		operator.setPassword(getDefaultEncripedPassword());
		operator.setPasswordNumber(0);
		operator.setFirstDayWeek(new Date(0));
		operatorDao.insertOperator(operator);
		
		//insert user role
		Integer roleId = roleDao.getRoleId(operator.getAuthority());
		UserRole userRole = new UserRole();
		userRole.setUsername(operator.getUsername());
		userRole.setRoleId(roleId);
		userRole.setCreateTime(new Date());
		roleDao.insertUserRole(userRole);
	}
	
	private String getDefaultEncripedPassword() {
		return MD5Util.MD5(DEFAULT_PASSWORD);
	}
	
}
