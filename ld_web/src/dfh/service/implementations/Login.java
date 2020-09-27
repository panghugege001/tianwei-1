package dfh.service.implementations;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import dfh.dao.LogDao;
import dfh.model.Users;
import dfh.model.enums.ActionLogType;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.ILogin;
import dfh.service.interfaces.NotifyService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.IPSeeker;

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
	public Users login(String loginname, String password, String ip) {
		// TODO Auto-generated method stub
		log.info(loginname+" begin to login web");
		loginname = StringUtils.lowerCase(loginname);
		Users customer = (Users) get(Users.class, loginname);
		if (customer == null) {
			msg = "该会员不存在";
			log.warn(msg);
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
			msg = "该会员已经被禁用,请联系在线客服";
			log.warn(msg);
			customer=null;
		} else if (!customer.getPassword().equals(EncryptionUtil.encryptPassword(password))) {
			msg = "密码错误";
			log.warn(msg);
			customer=null;
		} else {
			log.info("login web successfully");
			customer.setLastLoginIp(ip);
			customer.setLastLoginTime(DateUtil.now());
			customer.setLoginTimes(Integer.valueOf(customer.getLoginTimes().intValue() + 1));
			update(customer);
			logDao.insertActionLog(loginname, ActionLogType.LOGIN, ip);
			try {
				String service=customer.getAddress();
				if(null==service){
					service="";
				}
				IPSeeker seeker = (IPSeeker)ServletActionContext.getRequest().getSession().getServletContext().getAttribute("ipSeeker");
				String city="";
				if(null!=seeker.getAddress(ip)&&!seeker.getAddress(ip).equals("CZ88.NET")){
					city=seeker.getAddress(ip);
				}
				if(service.indexOf("1")!= -1){//发短信
					if(null != customer.getPhone()){
						notifyService.sendSms(customer.getPhone(), "龙都客户"+loginname+",你在"+DateUtil.formatDateForStandard(new Date())+"时刚刚登录，登录ip是"+ip+",登录地点是"+city+",如果发现登录异常请联系客服");
						//Thread.sleep(10000);
					}
				}
				if(service.indexOf("2")!= -1){//发邮件
					if(null != customer.getEmail()){
						notifyService.sendEmail(customer.getEmail(), "尊敬的客户你好,你的帐号刚刚登录","龙都客户"+loginname+",你在"+DateUtil.formatDateForStandard(new Date())+"时刚刚登录，登录ip是"+ip+",登录地点是"+city+",如果发现登录异常请联系客服");
						//Thread.sleep(10000);
					}
				}
			} catch (Exception e) {
				log.error(e);
			}
			msg = null;
		}
		return customer;
	}

}
