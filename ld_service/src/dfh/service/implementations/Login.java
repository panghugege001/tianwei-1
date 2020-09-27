package dfh.service.implementations;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dfh.dao.LogDao;
import dfh.model.Users;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.UserRole;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.ILogin;
import dfh.service.interfaces.NotifyService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class Login extends UniversalServiceImpl implements ILogin {
	
	private String msg;
	private Logger log=Logger.getLogger(Login.class);
	private NotifyService notifyService;
	private LogDao logDao;

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}
	public NotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return msg;
	}

	@Override
	public Users login(Users customer,String loginname, String password, String ip,String city,String regcity,String clientos,String ioBB) {
		log.info(loginname+" begin to login web");
		loginname = StringUtils.lowerCase(loginname);
		if (customer == null) {
			msg = "该会员不存在";
			log.warn(msg);
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue() && customer.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())) {
			msg = "该会员已经被禁用";
			log.warn(msg);
			customer=null;
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue() && customer.getRole().equals(UserRole.AGENT.getCode())) {
			msg = "代理账号核实中......";
			log.warn(msg);
			customer=null;
		}else if (customer.getFlag().intValue() == 2) {
			msg = "输入错误次数过多，密码被锁!请使用找回密码功能!";
			log.warn(msg);
			customer=null;
		}else if (!customer.getPassword().equals(EncryptionUtil.encryptPassword(password))) {
			msg = "密码错误";
			log.warn(msg);
			customer=null;
		} else {
			
			if(StringUtils.isEmpty(customer.getRandnum())){
				boolean b = true;
				String randnum = "";
				while(b){
					randnum = StringUtil.get4RandomString();
					if(0==getUsersCountByRandNum(randnum)){
						b=false;
						customer.setRandnum(randnum);
					}
				}
			}
			
			log.info("login web successfully");
			customer.setLastLoginIp(ip);
			if(customer.getArea()==null || "".equals(customer.getArea())){
				customer.setArea(regcity);
			}
			customer.setClientos(clientos);
			customer.setLastarea(city);
			customer.setPostcode(StringUtil.getRandomString(10));
			customer.setLastLoginTime(DateUtil.now());
			customer.setLoginTimes(Integer.valueOf(customer.getLoginTimes().intValue() + 1));
			final Users tmpUser = customer;
			/*(new Thread() {
				  public void run() {*/
					  logDao.updateUserSql(tmpUser);
					  logDao.insertActionLog(tmpUser.getLoginname(), ActionLogType.LOGIN,"ip:"+ tmpUser.getLastLoginIp()+";最后登录地址："+tmpUser.getLastarea()+";客户操作系统："+tmpUser.getClientos());
			/*	  }
			}).start();*/
			msg = null;
		}
		return customer;
	}
	
	@Override
	public Users nTwoTicketlogin(Users customer,String loginname, String ip,String city,String regcity,String clientos) { 
		// TODO Auto-generated method stub
		log.info(loginname+" begin to login offical by token");
		loginname = StringUtils.lowerCase(loginname);
		if (customer == null) {
			msg = "该会员不存在";
			log.warn(msg);
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue() && customer.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())) {
			msg = "该会员已经被禁用";
			log.warn(msg);
			customer=null;
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue() && customer.getRole().equals(UserRole.AGENT.getCode())) {
			msg = "代理账号核实中......";
			log.warn(msg);
			customer=null;
		}else if (customer.getFlag().intValue() == 2) {
			msg = "输入错误次数过多，密码被锁!请使用找回密码功能!";
			log.warn(msg);
			customer=null;
		} else {
			log.info("login web successfully");
			customer.setLastLoginIp(ip);
			if(customer.getArea()==null || "".equals(customer.getArea())){
				customer.setArea(regcity);
			}
			customer.setClientos(clientos);
			customer.setLastarea(city);
			customer.setPostcode(StringUtil.getRandomString(10));
			customer.setLastLoginTime(DateUtil.now());
			customer.setLoginTimes(Integer.valueOf(customer.getLoginTimes().intValue() + 1));
			final Users tmpUser = customer;
			logDao.updateUserSql(tmpUser);
			logDao.insertActionLog(tmpUser.getLoginname(), ActionLogType.LOGIN,"ip:"+ tmpUser.getLastLoginIp()+";最后登录地址："+tmpUser.getLastarea()+";客户操作系统："+tmpUser.getClientos());
			msg = null;
		}
		return customer;
	}


	public Users loginByEncryptPassword(Users customer, String loginname, String encryptPassword, String ip, String city, String regcity, String clientos, String ioBB) {
		log.info(loginname + " begin to login web");
		if (customer == null) {
			msg = "该会员不存在";
			log.warn(msg);
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue() && customer.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())) {
			msg = "该会员已经被禁用";
			log.warn(msg);
			customer = null;
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue() && customer.getRole().equals(UserRole.AGENT.getCode())) {
			msg = "代理账号核实中......";
			log.warn(msg);
			customer = null;
		} else if (customer.getFlag().intValue() == 2) {
			msg = "输入错误次数过多，密码被锁!请使用找回密码功能!";
			log.warn(msg);
			customer = null;
		} else if (!customer.getPassword().equals(encryptPassword)) {
			msg = "密码错误";
			log.warn(msg);
			customer = null;
		} else {
			login(customer, ip, city, regcity, clientos);
			msg = "";
		}
		return customer;

	}

	private void login(Users customer, String ip, String city, String regcity, String clientos) {
		log.info("login web successfully");
		customer.setLastLoginIp(ip);
		if (customer.getArea() == null || "".equals(customer.getArea())) {
			customer.setArea(regcity);
		}
		customer.setClientos(clientos);
		customer.setLastarea(city);
		customer.setPostcode(StringUtil.getRandomString(10));
		customer.setLastLoginTime(DateUtil.now());
		customer.setLoginTimes(Integer.valueOf(customer.getLoginTimes().intValue() + 1));
		final Users tmpUser = customer;
		logDao.updateUserSql(tmpUser);
		logDao.insertActionLog(tmpUser.getLoginname(), ActionLogType.LOGIN, "ip:" + tmpUser.getLastLoginIp() + ";最后登录地址：" + tmpUser.getLastarea() + ";客户操作系统：" + tmpUser.getClientos());
	}
}
