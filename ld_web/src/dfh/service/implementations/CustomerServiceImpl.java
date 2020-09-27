package dfh.service.implementations;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import dfh.dao.LogDao;
import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Accountinfo;
import dfh.model.Actionlogs;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.UserRole;
import dfh.model.notdb.FirstlyCashin;
import dfh.remote.ErrorCode;
import dfh.remote.RemoteCaller;
import dfh.remote.RemoteConstant;
import dfh.remote.bean.LoginValidationBean;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.NotifyService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class CustomerServiceImpl extends UniversalServiceImpl implements CustomerService {

	private static Logger log = Logger.getLogger(CustomerServiceImpl.class);
	private LogDao logDao;
	private TradeDao tradeDao;
	private TransferDao transferDao;
	private UserDao userDao;
	private NotifyService notifyService;

	public CustomerServiceImpl() {
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public NotifyService getNotifyService() {
		return notifyService;
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

	public Double getRemoteCredit(String loginname) {
		try {
			Double credit = RemoteCaller.queryCredit(loginname);
			return credit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public TransferDao getTransferDao() {
		return transferDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public String loginWeb(String loginname, String password, String ip) {
		log.info("begin to login web");
		loginname = StringUtils.lowerCase(loginname);
		String msg = null;
		Users customer = (Users) get(Users.class, loginname);
		if (customer == null) {
			msg = "该会员不存在";
			log.warn(msg);
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
			msg = "该会员已经被禁用,请联系在线客服";
			log.warn(msg);
		} else if (!customer.getPassword().equals(EncryptionUtil.encryptPassword(password))) {
			msg = "密码错误";
			log.warn(msg);
		} else {
			log.info("login web successfully");
			customer.setLastLoginIp(ip);
			customer.setLastLoginTime(DateUtil.now());
			customer.setLoginTimes(Integer.valueOf(customer.getLoginTimes().intValue() + 1));
			update(customer);
			logDao.insertActionLog(loginname, ActionLogType.LOGIN, ip);
			msg = null;
		}
		return msg;
	}

	public String loginWebForBoth(String loginname, String password, String ip) {
		log.info("begin to login web");
		loginname = StringUtils.lowerCase(loginname);
		String msg = null;
		Users customer = (Users) get(Users.class, loginname);
		if (customer == null) {
			msg = "该会员不存在";
			log.warn(msg);
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
			msg = "该会员已经被禁用,请联系在线客服";
			log.warn(msg);
		} else if (!customer.getPassword().equals(EncryptionUtil.encryptPassword(password))) {
			msg = "密码错误";
			log.warn(msg);
		} else {
			log.info("login web successfully");
			customer.setLastLoginIp(ip);
			customer.setLastLoginTime(DateUtil.now());
			customer.setLoginTimes(Integer.valueOf(customer.getLoginTimes().intValue() + 1));
			update(customer);
			logDao.insertActionLog(loginname, ActionLogType.LOGIN, ip);
			msg = customer.getRole();
		}
		return msg;
	}
	
	public String gameLoginValidation(LoginValidationBean bean) {
		log.info("begin to login validation");
		String status = null;
		String errdesc = null;

		// 运营测试使用，运营时删除
		/*
		if (!RemoteConstant.TEST_ACCOUNTS.contains(bean.getUserid())) {
			log.info("not testaccount,can't login");
			status = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
			return RemoteCaller.getLoginValidationResponseXml(bean.getId(), bean.getUserid(), "", status, errdesc, "");
		}
		*/

		Users customer = (Users) get(Users.class, bean.getUserid());
		if (customer == null) {
			status = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
			log.warn(status);
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
			status = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
			errdesc = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
			log.warn(status);
		}else if (StringUtils.equals(customer.getRole(), UserRole.AGENT.getCode()) ) {
			status = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
			errdesc = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
			log.warn(status);
		} else if (!customer.getPassword().equals(EncryptionUtil.encryptPassword(bean.getPwd()))) {
			status = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
			errdesc = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
			log.warn(status);
		} else {
			log.info("login game successfully");
			status = ErrorCode.SUCCESS.getCode();
			//下面是新加的
			errdesc = "SUCCESS";
			logDao.insertActionLog(bean.getUserid(), ActionLogType.LOGIN_GAME, null);
		}
		return RemoteCaller.getLoginValidationResponseXml(bean.getId(), bean.getUserid(), customer != null ? customer.getAliasName() : "", status, errdesc, customer != null ? customer.getAgcode()
				: "");
	}

	public String modifyBankInfo(String loginname, String bank, String bankAddress, String accountType, String accountName, String accountNo, String accountCity, String ip)
			throws GenericDfhRuntimeException {
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null)
			msg = "帐号不存在";
		// else if (user.getRole().equals(UserRole.FREE_CUSTOMER.getCode())) {
		// msg = "试玩会员无需银行资料";
		// }
		else {
			if (get(Accountinfo.class, loginname) != null) {
				msg = "银改资料已完善，只能首次更改";
				return msg;
			}
			if (!user.getAliasName().equals(accountName)) {
				msg = "收款人必须跟真实姓名一致";
				return msg;
			}
			Accountinfo accountinfo = new Accountinfo(loginname, accountName, accountNo, bank, accountType, accountCity, bankAddress, DateUtil.now(), DateUtil.now());
			save(accountinfo);
			logDao.insertActionLog(loginname, ActionLogType.MODIFY_BANK_INFO, ip);
			msg = null;
		}
		return msg;
	}

	// 邮箱不可更改
	public String modifyCustomerInfo(Integer sms,String loginname, String aliasName, String phone, String email, String qq, String ip) throws GenericDfhRuntimeException {
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null) {
			msg = "帐号不存在";
		} else {
			String oldAliasName = user.getAliasName();
			String oldPhone = user.getPhone();
			String oldQq = user.getQq();

			user.setAliasName(aliasName);
			user.setPhone(phone);
			// user.setEmail(email);
			user.setQq(qq);
			user.setSms(sms);
			update(user);

			StringBuffer remark = new StringBuffer(ip + ";");
			if (!StringUtils.equals(oldAliasName, aliasName))
				remark.append("原昵称[" + oldAliasName + "]改为[" + aliasName + "];");
			if (!StringUtils.equals(oldPhone, phone))
				remark.append("原电话[" + oldPhone + "]改为[" + phone + "];");
			if (!StringUtils.equals(oldQq, qq))
				remark.append("原QQ[" + oldQq + "]改为[" + qq + "];");

			logDao.insertActionLog(loginname, ActionLogType.MODIFY_CUS_INFO, remark.toString());
		}
		return msg;
	}

	public String modifyPassword(String loginname, String oldPwd, String newPwd, String ip) throws GenericDfhRuntimeException {
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null)
			msg = "帐号不存在";
		else if (!user.getPassword().equals(EncryptionUtil.encryptPassword(oldPwd))) {
			msg = "旧密码不正确";
		} else {
			user.setPassword(EncryptionUtil.encryptPassword(newPwd));
			user.setPwdinfo(userDao.encrypt(newPwd));
			update(user);
			try {
				String service=user.getAddress();
				if(null==service){
					service="";
				}
				if(service.indexOf("3")!= -1){//发短信
					if(null != user.getPhone()){
						notifyService.sendSms(user.getPhone(), "龙都客户"+loginname+",你在"+DateUtil.formatDateForStandard(new Date())+"时修改密码，新密码是"+newPwd+",如果发现异常请联系客服");
						//Thread.sleep(10000);
					}
				}
				if(service.indexOf("4")!= -1){//发邮件
					if(null != user.getEmail()){
						notifyService.sendEmail(user.getEmail(), "龙都账户修改密码通知","龙都客户"+loginname+",你在"+DateUtil.formatDateForStandard(new Date())+"时修改密码，新密码是"+newPwd+",如果发现异常请联系客服");
						//Thread.sleep(10000);
					}
				}
			} catch (Exception e) {
				log.error(e);
			}
			logDao.insertActionLog(loginname, ActionLogType.MODIFY_PWD, ip);
		}
		return msg;
	}
	
	public String chooseservice(String loginname, String service, String ip){
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null){
			msg = "帐号不存在";
		}
		else{
			if(service==null){
				user.setAddress("");
			}else{
				user.setAddress(service);
			}
			update(user);
			logDao.insertActionLog(loginname, ActionLogType.SERVICE, ip);
		}
		
		return msg;
	}

	public String queryUserLastXimaEndTime(String loginname) {
		final String userName = loginname;
		Date date = (Date) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("select max(x.endTime) from proposal p,xima x where p.pno=x.pno and p.flag=" + ProposalFlagType.EXCUTED.getCode() + " and p.loginname='"
						+ userName + "'");
				return sqlQuery.uniqueResult();
			}
		});
		if (date != null) {
			return DateUtil.formatDateForStandard(date);
		}
		return "";
	}

	public Double queryValidBetAmount(String loginname, Date start, Date end) {
		return userDao.getValidBetAmount(loginname, start, end, null);
	}

	public FirstlyCashin getFirstCashin(String loginname) {
		return userDao.getFirstCashin(loginname);
	}

	public String checkIsUserAliasName(String aliasName) {
		return userDao.checkIsUserAccountName(aliasName);
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public void setTransferDao(TransferDao transferDao) {
		this.transferDao = transferDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String register(String howToKnow,Integer sms,String loginname, String pwd, String accountName, String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress,String intro) {
		return userDao.addCustomer(howToKnow,sms,loginname, pwd, accountName, aliasName, phone, email, qq, referWebsite, ipaddress,intro);
	}
	
	public List<Users> getSubUsers(String agent){
		return userDao.getSubUsers(agent);
	}

	@Override
	public Userstatus getUserstatus(String loginname) {
		// TODO Auto-generated method stub
		return (Userstatus)userDao.get(Userstatus.class, loginname);
	}

	@Override
	public String modifyUserstatus(Userstatus userstatus,Actionlogs actionlog) {
		userDao.saveOrUpdate(userstatus);
		if(actionlog != null){
			logDao.save(actionlog);
		}
		return null;
	}

	@Override
	public String lockUser(String loginname) {
		Users users = (Users)userDao.get(Users.class, loginname);
		if(null != users){
			users.setFlag(1);
			users.setRemark("客户登录时，连续输错5次密码，账号被冻结");
			userDao.update(users);
		}
		return null;
	}

}
