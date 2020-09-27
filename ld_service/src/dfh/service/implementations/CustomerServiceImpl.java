package dfh.service.implementations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dfh.model.*;
import dfh.service.interfaces.*;
import dfh.utils.*;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;

import dfh.dao.LogDao;
import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.UserRole;
import dfh.model.notdb.FirstlyCashin;
import dfh.model.weCustomer.UserInfo;
import dfh.remote.ErrorCode;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.LoginValidationBean;
import dfh.remote.bean.SportBookLoginValidationBean;
import dfh.security.EncryptionUtil;
import dfh.skydragon.webservice.SbValidateWS;

public class CustomerServiceImpl extends UniversalServiceImpl implements CustomerService {

	private static Logger log = Logger.getLogger(CustomerServiceImpl.class);
	private LogDao logDao;
	private TradeDao tradeDao;
	private TransferDao transferDao;
	private UserDao userDao;
	private NotifyService notifyService;
	private IMemberSignrecord memberService;
	private LoginTokenService loginTokenService;
	

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

	public IMemberSignrecord getMemberService() {
		return memberService;
	}

	public void setMemberService(IMemberSignrecord memberService) {
		this.memberService = memberService;
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

	public String modifyPayPassword(String loginname, String oldPwd, String newPwd,int isPassWord,ProposalService proposalService) throws GenericDfhRuntimeException {
		String msg = null;
		Users user = (Users) get(Users.class, loginname);
//		Map map = new HashMap<String, String>();
//		map.put("loginname", loginname);
//		map.put("questionid", "7");
		Question question = proposalService.queryQuestionByCondition(loginname,"7");
		if(question != null && isPassWord == 0) return "您已经设定取款密码，请用取款密码修改！";

		if (user == null) {
			msg = "帐号不存在";
			return msg;
		}else if (isPassWord == 0 && !user.getPassword().equals(EncryptionUtil.encryptPassword(oldPwd))) {
			// 0 是登录密码验证
			msg = "旧密码不正确";
			return msg;
		} else if(isPassWord == 1  && question != null && question.getContent() != null && !question.getContent().equals(EncryptionUtil.encryptPassword(oldPwd))   ) {
			// 1 是支付密码验证
			msg = "原支付密码不正确";
			return msg;
		}
		if(question != null) {
			question.setContent(EncryptionUtil.encryptPassword(newPwd));
			update(question);
			logDao.insertActionLog(loginname, ActionLogType.MODIFY_BANK_INFO,"修改银行支付密码");
			return null;
		}else {
			msg =  proposalService.savePayPassWordQuestion( loginname, 7,EncryptionUtil.encryptPassword(newPwd));
			if(msg.equals("绑定成功") == true) {
				logDao.insertActionLog(loginname, ActionLogType.MODIFY_BANK_INFO,"修改银行支付密码");
				return null;
			}else {
				return msg;
			}
		}

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
	public String modifyCustomerInfo(Integer sms,String loginname, String aliasName, String phone, String email, String qq, String ip,String mailaddress,String microchannel,String birthday,String accountName) throws GenericDfhRuntimeException {
		sms=0;
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null) {
			msg = "帐号不存在";
		} else {
			String oldAliasName = user.getAliasName();
			String oldPhone = user.getPhone();
			String oldQq = user.getQq();
			String oldMicrochannel = user.getMicrochannel();
			if(!StringUtils.isBlank(aliasName)){
				user.setAliasName(aliasName);
			}
			if(!StringUtils.isBlank(qq) && !qq.contains("*")){
			user.setQq(qq);
			}
			if(StringUtils.isNotEmpty(email) && !email.contains("*")&&StringUtils.isEmpty(user.getEmail())){
				try {
					String emails = AESUtil.aesEncrypt(email,AESUtil.KEY);
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("email", emails);
					params.put("loginname", loginname);

					String chkSql = "select count(*) from users where email=:email and loginname<>:loginname";
					if (getCount(chkSql, params) > 0) {
						msg ="该邮箱已存在，请修改";
						return msg;
					}
					
					user.setEmail(AESUtil.aesEncrypt(email,AESUtil.KEY));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(StringUtils.isNotEmpty(accountName) ){
				user.setAccountName(accountName);
			}
			if(!StringUtils.isBlank(microchannel) && !microchannel.contains("*")){
				user.setMicrochannel(microchannel);
			}
			if (birthday != null && !birthday.equals("")&&user.getBirthday()==null) {
				int year = Integer.parseInt(birthday.split("-")[0]);
				int month = Integer.parseInt(birthday.split("-")[1]);
				int day = Integer.parseInt(birthday.split("-")[2]);
				user.setBirthday(DateUtil.date2Timestampyyyy_MM_dd_HH(DateUtil.getDate(year, month, day, 12), 12));
			}
			update(user);

			StringBuffer remark = new StringBuffer(ip + ";");
			if (!StringUtils.equals(oldAliasName, aliasName))
				remark.append("原昵称[" + oldAliasName + "]改为[" + aliasName + "];");
			if (!StringUtils.equals(oldPhone, phone))
				remark.append("原电话[" + oldPhone + "]改为[" + phone + "];");
			if (!StringUtils.equals(oldQq, qq))
				if(!qq.contains("*")){
				remark.append("原QQ[" + oldQq + "]改为[" + qq + "];");
				}
			if (!StringUtils.equals(oldMicrochannel, microchannel))
				if(!microchannel.contains("*")){
				remark.append("原wechat[" + oldMicrochannel + "]改为[" + microchannel + "];");
				}

			logDao.insertActionLog(loginname, ActionLogType.MODIFY_CUS_INFO, remark.toString());
		}
		return msg;
	}
	public String updateQQ(String loginname, String qq) throws GenericDfhRuntimeException {
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null) {
			msg = "帐号不存在";
		} else {
			String oldQq = user.getQq();
			if(!StringUtils.isBlank(qq) && !qq.contains("*")){
				user.setQq(qq);
			}
			update(user);

			StringBuffer remark = new StringBuffer(";");

			if (!StringUtils.equals(oldQq, qq))
				if(!qq.contains("*")){
					remark.append("原QQ[" + oldQq + "]改为[" + qq + "];");
				}
			logDao.insertActionLog(loginname, ActionLogType.MODIFY_CUS_INFO, remark.toString());
		}
		return msg;
	}
	public String updateWeiXin(String loginname, String microchannel) throws GenericDfhRuntimeException {
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null) {
			msg = "帐号不存在";
		} else {
			String oldMicrochannel = user.getMicrochannel();
			if(!StringUtils.isBlank(microchannel) && !microchannel.contains("*")){
				user.setMicrochannel(microchannel);
			}
			update(user);

			StringBuffer remark = new StringBuffer(";");
			if (!StringUtils.equals(oldMicrochannel, microchannel))
				if(!microchannel.contains("*")){
					remark.append("原wechat[" + oldMicrochannel + "]改为[" + microchannel + "];");
				}

			logDao.insertActionLog(loginname, ActionLogType.MODIFY_CUS_INFO, remark.toString());
		}
		return msg;
	}


	public Double checkWithdrawRecord(String loginname,Date date){
		Double sumMoney=0.0;
		DetachedCriteria dc1 = DetachedCriteria.forClass(Proposal.class);
		dc1.add(Restrictions.eq("loginname", loginname));
		dc1.add(Restrictions.eq("type", 503));
		dc1.add(Restrictions.eq("flag", 2));
		dc1.add(Restrictions.ge("createTime",date));
		dc1.setProjection(Projections.sum("amount"));
		List list1 = this.findByCriteria(dc1);
		Double result1= 0.0;
		result1 =(Double)list1.get(0);
		if(null==result1){
			result1=0.0;
		}

		return result1;
	}

	/**
	 * 检查该玩家某个时间点之后的存款量
	 * @param loginname
	 * @return
	 */
	public Double checkDepositRecord(String loginname,Date date){
		Double sumMoney=0.0;
		DetachedCriteria dc1 = DetachedCriteria.forClass(Payorder.class);
		dc1.add(Restrictions.eq("loginname", loginname));
		dc1.add(Restrictions.eq("type", 0));
		dc1.add(Restrictions.ge("createTime",date));
		dc1.setProjection(Projections.sum("money"));
		List list1 = this.findByCriteria(dc1);
		Double result1= 0.0;
		result1 =(Double)list1.get(0);
		if(null==result1){
			result1=0.0;
		}
		DetachedCriteria dc2 = DetachedCriteria.forClass(Proposal.class);
		dc2.add(Restrictions.eq("loginname", loginname));
		dc2.add(Restrictions.eq("type", 502));
		dc2.add(Restrictions.eq("flag", 2));
		dc2.add(Restrictions.ge("createTime",date));
		dc2.setProjection(Projections.sum("amount"));
		List list2 = this.findByCriteria(dc2);
		Double result2= 0.0;
		result2 =(Double)list2.get(0);
		if(null==result2){
			result2=0.0;
		}
		sumMoney=result1+result2;
		return sumMoney;
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
						//notifyService.sendSms(user.getPhone(), "尊敬的客户"+loginname+",你在"+DateUtil.formatDateForStandard(new Date())+"时修改密码，新密码是"+newPwd+",如果发现异常请联系客服");
						//Thread.sleep(10000);
					}
				}
				if(service.indexOf("4")!= -1){//发邮件
					if(null != user.getEmail()){
						//notifyService.sendEmail(user.getEmail(), "e路发账户修改密码通知","e路发客户"+loginname+",你在"+DateUtil.formatDateForStandard(new Date())+"时修改密码，新密码是"+newPwd+",如果发现异常请联系客服");
						//Thread.sleep(10000);
					}
				}
			} catch (Exception e) {
				log.error(e);
			}
			logDao.insertActionLog(loginname, ActionLogType.MODIFY_PWD, ip);
			if(user.getRole().equals("MONEY_CUSTOMER")){
				//更新论坛密码
				updateBBSPassword(user.getLoginname(),newPwd);
				//更新pt密码
				Boolean b = PtUtil.updatePlayerPassword(loginname, newPwd);
				if(!b.booleanValue()){
					msg="更新PT登录密码错误";
				}
			}
		}
		return msg;
	}
	
	/**
	 * 修改论坛密码
	 * @param loginname
	 * @param password
	 */
	public static void updateBBSPassword(String loginname, String password) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String databasesIp = Configuration.getInstance().getValue("bbs_ip");
			String databasesPort = Configuration.getInstance().getValue("bbs_port");
			String databases = Configuration.getInstance().getValue("bbs_databases");
			String dbuser = Configuration.getInstance().getValue("bbs_user");
			String dbpassword = Configuration.getInstance().getValue("bbs_password");
			String usrl = "jdbc:mysql://" + databasesIp + ":" + databasesPort + "/" + databases + "?useUnicode=true&characterEncoding=UTF-8";
			conn = DriverManager.getConnection(usrl, dbuser, dbpassword);
			String sqlTwo = "update pre_ucenter_members set password='" + EncryptionUtil.encryptPassword(password) + "' where username='" + loginname + "'";
			stmt = conn.prepareStatement(sqlTwo);
			stmt.executeUpdate(sqlTwo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void synBbsMemberInfo(String loginname, String password) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String databasesIp = Configuration.getInstance().getValue("bbs_ip");
			String databasesPort = Configuration.getInstance().getValue("bbs_port");
			String databases = Configuration.getInstance().getValue("bbs_databases");
			String dbuser = Configuration.getInstance().getValue("bbs_user");
			String dbpassword = Configuration.getInstance().getValue("bbs_password");
			String usrl = "jdbc:mysql://" + databasesIp + ":" + databasesPort + "/" + databases + "?useUnicode=true&characterEncoding=UTF-8";
			conn = DriverManager.getConnection(usrl, dbuser, dbpassword);
			// 判断用户是否已经同步过了
			String sql = "select username from pre_ucenter_members where username='" + loginname + "'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String sqlTwo = "update pre_ucenter_members set password='" + EncryptionUtil.encryptPassword(password) + "' where username='" + loginname + "'";
				stmt.executeUpdate(sqlTwo);
				return;
			}
			String sqlTwo = "insert into pre_ucenter_members(username, password, email, salt) values ('" + loginname + "', '" + EncryptionUtil.encryptPassword(password) + "', '" + loginname + "@s.com','"+StringUtil.getRandomString(6)+"')";
			stmt.executeUpdate(sqlTwo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String chooseservice(String loginname, String service, String ip){
		String msg = null;
//		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		Users user = (Users) get(Users.class, loginname);
		if (user == null){
			msg = "帐号不存在";
		}
		else{
			if(StringUtils.isBlank(service)){
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

	public String register(String howToKnow,Integer sms,String loginname, String pwd, String accountName, String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress,String city,String birthday,String intro ,String agentcode,String ioBB,String microchannel) {
		return userDao.addCustomer(howToKnow,sms,loginname, pwd, accountName, aliasName, phone, email, qq, referWebsite, ipaddress,city,birthday,intro,agentcode,ioBB,microchannel);
	}
	
	public String registerTwo(String howToKnow,Integer sms,String loginname, String pwd, String accountName, String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress,String city,String birthday,String intro,Double gifTamount,String agentStr,String type,String ioBB,String microchannel) {
		return userDao.addCustomerTwo(howToKnow,sms,loginname, pwd, accountName, aliasName, phone, email, qq, referWebsite, ipaddress,city,birthday,intro,gifTamount,agentStr,type,ioBB,microchannel);
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
			users.setFlag(2);
			users.setRemark("客户登录时，连续输错5次密码，账号密码被被锁");
			userDao.updatelockUser(users);
		}
		return null;
	}
	
	public String addAgent(String howToKnow,String loginname, String pwd, String accountName, String phone, String email, String qq, String referWebsite, String ipaddress,String city,String microchannel,String partner){
		return userDao.addAgent(howToKnow,loginname, pwd, accountName, phone, email, qq, referWebsite, ipaddress,city,microchannel,partner);
	}


	@Override
	public Boolean wetherHaveSameInfo(Users user) {
//		真实姓名、手机号、ip、email
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode()));
		Disjunction disjunction = Restrictions.disjunction();
//		disjunction.add(Restrictions.eq("accountName", user.getAccountName()));
		disjunction.add(Restrictions.eq("phone", user.getPhone()));
//		disjunction.add(Restrictions.eq("email", user.getEmail()));
//		
//		disjunction.add(Restrictions.eq("lastLoginIp", user.getLastLoginIp()));
//		disjunction.add(Restrictions.eq("registerIp", user.getRegisterIp()));
		dc.add(disjunction);
		int size = this.findByCriteria(dc).size();
		return size>1;
	}
	
	/**
	 * 三个月内 同IP 或者同玩家姓名 是否有领取过8元体验金  三个月内领过则不允许，三个月内没领过 则允许
	 */
	@Override
	public Boolean isUsePt8YouHui(String loginname,String accountName,String registeIp) {
		
		//查询该玩家是否领过，如果领过 则不能再领
		DetachedCriteria dc2 = DetachedCriteria.forClass(Transfer.class);
		dc2.add(Restrictions.eq("loginname", loginname));
		dc2.add(Restrictions.in("target", Arrays.asList(new String[]{"newpt","ttg","gpi","nt","qt","mg","dt"}))) ;
		dc2.add(Restrictions.or(Restrictions.eq("remit", 8.00), Restrictions.sqlRestriction(" remark like '%元自助优惠' ")));
		List transfers1 = this.findByCriteria(dc2) ;
		if(null != transfers1 && transfers1.size()>0 && null != transfers1.get(0)){
			return true ;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date begindate = calendar.getTime();//当前日期减去三个月
		//查询同IP或者同姓名下的所有玩家
		DetachedCriteria dc1=DetachedCriteria.forClass(Users.class);
		dc1.add(Restrictions.or(Restrictions.eq("accountName",accountName), Restrictions.eq("registerIp",registeIp)));
		List<Users> list = this.findByCriteria(dc1);
		List listLoginName = new ArrayList();
		if(list!=null && list.size()>0 && list.get(0)!=null){
			for(Users user : list){
				listLoginName.add(user.getLoginname());
			}
		}
		
		//查询同IP或者同姓名三个月内是否有8元自助优惠领取
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.in("loginname", listLoginName));
		dc.add(Restrictions.in("target", Arrays.asList(new String[]{"newpt","ttg","gpi","nt","qt","mg","dt"}))) ;
		dc.add(Restrictions.ge("createtime", begindate)) ;
		dc.add(Restrictions.or(Restrictions.eq("remit", 8.00), Restrictions.sqlRestriction(" remark like '%元自助优惠' ")));
		List transfers = this.findByCriteria(dc) ;
		if(null != transfers && transfers.size()>0 && null != transfers.get(0)){
			return true ;
		}else{
			return false ;
		}
	}
	
	/**
	 * 是否领取过8元体验金   无时间限制
	 */
	@Override
	public Boolean isUsePt8YouHuiNoTime(String loginname,String accountName,String registeIp) {
		//查询同IP或者同姓名下的所有玩家
		DetachedCriteria dc1=DetachedCriteria.forClass(Users.class);
		dc1.add(Restrictions.or(Restrictions.eq("accountName",accountName), Restrictions.eq("registerIp",registeIp)));
		List<Users> list = this.findByCriteria(dc1);
		List listLoginName = new ArrayList();
		if(list!=null && list.size()>0 && list.get(0)!=null){
			for(Users user : list){
				listLoginName.add(user.getLoginname());
			}
		}
		//查询同IP或者同姓名是否有8元自助优惠领取
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.in("loginname", listLoginName));
		dc.add(Restrictions.in("target", Arrays.asList(new String[]{"newpt","ttg","gpi","nt","qt","mg","dt"}))) ;
		dc.add(Restrictions.or(Restrictions.eq("remit", 8.00), Restrictions.sqlRestriction(" remark like '%元自助优惠' ")));
		List transfers = this.findByCriteria(dc) ;
		if(null != transfers && transfers.size()>0 && null != transfers.get(0)){
			return true ;
		}else{
			return false ;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCardNums(String loginname) {
		DetachedCriteria dc = DetachedCriteria.forClass(Userbankinfo.class) ;
		dc.add(Restrictions.eq("loginname", loginname)) ;
		dc.add(Restrictions.eq("flag", 0)) ;
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("bankno")) ;
		dc.setProjection(pList);
		return this.findByCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean repeatCards(String loginname) {
		List<String> list = getCardNums(loginname) ;
		if(list.size() == 0){
			return false ;
		}
		
		List<String> players =  (List<String>) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("SELECT  substring(bankno, 1, LENGTH(bankno)-2) from userbankinfo where bankname !='支付宝' GROUP BY substring(bankno, 1, LENGTH(bankno)-2) having count(*) > 1 ");
				return sqlQuery.list() ;
			}
		});
		for (int i = 0; i < list.size(); i++) {
			log.info(list.get(i));
			if(players.contains(list.get(i).substring(0, list.get(i).length()-2))){
				return true ;
			}
		}
		return false ;
	}

	@Override
	public IESnare getIESanre(String device) {
		DetachedCriteria dc = DetachedCriteria.forClass(IESnare.class) ;
		dc.add(Restrictions.eq("device", device));
		List<IESnare> list = this.findByCriteria(dc);
		return list==null||list.size()==0?null:list.get(0);
	}
	
	/**
	 * 根据cpuID 查询所有符合的数据
	 * @param device
	 * @return
	 */
	@Override
	public List<IESnare> getAllIESanre(String device) {
		DetachedCriteria dc = DetachedCriteria.forClass(IESnare.class) ;
		dc.add(Restrictions.eq("device", device));
		List<IESnare> list = this.findByCriteria(dc);
		return list==null||list.size()==0?null:list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String validateGPIAccess(String token) {
		DetachedCriteria dc = DetachedCriteria.forClass(GPIToken.class) ;
		dc.add(Restrictions.eq("token", token));
		List<GPIToken> list = this.findByCriteria(dc);
		String code;
		String username = null;
		Integer isTest = 0;
		if(list != null && list.size()==1){
			GPIToken gpiToken = list.get(0);
			//验证时间有效性
			long diff = (new Date()).getTime() - gpiToken.getUpdateTime().getTime();
			if(diff / (1000 * 60) > 5){
				code = "03";   //认证失败
			}else{
				code = "0";    //认证成功
				username = gpiToken.getLoginname();
				isTest = gpiToken.getIsTest();
			}
		}else{
			code = "03";   //认证失败
		}
		return RemoteCaller.getGPIAccessValidateXML(code, username, isTest);
	}
	
	/* 代理处理部分开始 */
	/**
	 * 查询代理每月数据汇总,其中每日实际上是昨日
	 */
	@Override
	public String queryAgentMonthlyReport(String loginname) {
		String month_start = DateUtil.fmtyyyy_MM_d(DateUtil.getStartDayOfMonth(new Date())) + " 00:00:00";
		String yesterday_start = DateUtil.fmtyyyy_MM_d(DateUtil.getMontToDate(new Date(), -1))+" 00:00:00";
		String yesterday_end = DateUtil.fmtyyyy_MM_d(DateUtil.getMontToDate(new Date(), -1))+" 23:59:59";
		
		//执行查询月汇总数据
		final StringBuffer sql_a = new StringBuffer("select sum(profitall) as profitall,sum(ximafee) as ximafee,sum(couponfee) as couponfee,sum(betall) ");
		sql_a.append("as betall,sum(amount) as amount from ptcommissions pt1 where pt1.agent='"+loginname+"' and ");
		sql_a.append("(pt1.createTime>='"+ month_start +"' and ");
		sql_a.append("pt1.createTime<='"+ yesterday_end +"');");
		StringBuffer rs = new StringBuffer();
		String nowrs = "";
		HashMap valmap = (HashMap) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createSQLQuery(sql_a.toString());  
                return (HashMap)query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
			}
		});
		nowrs = JSONObject.fromObject(valmap).toString();
		rs.append(nowrs.substring(0, nowrs.length()-1));
		
		//执行查询用户总数以及月总数,天总数
		final StringBuffer sql_b = new StringBuffer("select count(1) as reg,(select count(1) from users u1 where u1.agent='"+loginname+"' and ");
		sql_b.append("(u1.createtime>='"+ yesterday_start +"' and u1.createtime<='"+ yesterday_end +"')) as daily_reg, ");
		sql_b.append("(select count(1) from users u2 where u2.agent='"+loginname+"' and (u2.createtime>='"+ month_start +"' and ");
		sql_b.append("u2.createtime<='"+ yesterday_end +"')) as monthly_reg ");
		sql_b.append("from users u3 where u3.agent='"+ loginname +"'");
		valmap = (HashMap) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createSQLQuery(sql_b.toString());  
                return (HashMap)query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
			}
		});
		Integer daily_reg = Integer.valueOf(valmap.get("daily_reg").toString());//保存每日注册用户数
		nowrs = JSONObject.fromObject(valmap).toString();
		rs.append(","+nowrs.substring(1, nowrs.length()-1));
		
		//执行查询每日输赢和佣金等
		final StringBuffer sql_c = new StringBuffer("select sum(profitall) as daily_profitall,sum(ximafee) as daily_ximafee,sum(amount) as daily_amount, ");
		sql_c.append("sum(couponfee) as daily_couponfee ");
		sql_c.append("from ptcommissions pt1 where pt1.agent='"+ loginname +"' and (pt1.createTime>='"+ yesterday_start +"' ");
		sql_c.append("and pt1.createTime<='"+ yesterday_end +"')");
		valmap = (HashMap) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createSQLQuery(sql_c.toString());  
                return (HashMap)query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
			}
		});
		nowrs = JSONObject.fromObject(valmap).toString();
		rs.append(","+nowrs.substring(1, nowrs.length()-1));
		
		//执行查询每日转换率,每日点击量,总点击量
		/*final StringBuffer sql_d = new StringBuffer("SELECT SUM(agent_pv) as agent_pv,(select sum(agent_pv) from agent_count c1 where c1.agent='"+ loginname +"' and ");
		sql_d.append("(c1.createtime>='"+ yesterday_start +"' and c1.createtime<='"+ yesterday_end +"')) as agent_pv_day, ");
		sql_d.append("(select count(1) from proposal p1 where (p1.createTime>='"+ yesterday_start +"' and p1.createTime<='"+ yesterday_end +"') ");
		sql_d.append("and p1.flag=2 and p1.loginname in(select u1.loginname from users u1 where u1.agent='"+loginname+"')) as proposal, ");
		sql_d.append("(select count(1) from payorder p2 where (p2.createTime>='"+ yesterday_start +"' and p2.createTime<='"+ yesterday_end +"') ");
		sql_d.append("and p2.flag=0 and p2.type=0 and p2.loginname in(select u1.loginname from users u1 where u1.agent='"+loginname+"')) as payorder");
		sql_d.append(" FROM agent_count WHERE agent='"+ loginname +"'");*/
		final StringBuffer sql_d = new StringBuffer("select ");
		sql_d.append("(select count(1) from proposal p1 where (p1.createTime>='"+ yesterday_start +"' and p1.createTime<='"+ yesterday_end +"') ");
		sql_d.append("and p1.flag=2 and p1.loginname in(select u1.loginname from users u1 where u1.agent='"+loginname+"')) as proposal, ");
		sql_d.append("(select count(1) from payorder p2 where (p2.createTime>='"+ yesterday_start +"' and p2.createTime<='"+ yesterday_end +"') ");
		sql_d.append("and p2.flag=0 and p2.type=0 and p2.loginname in(select u1.loginname from users u1 where u1.agent='"+loginname+"')) as payorder;");
		valmap = (HashMap) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createSQLQuery(sql_d.toString());  
                return (HashMap)query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
			}
		});
		Integer convert = (Integer.parseInt(valmap.get("proposal").toString())+Integer.parseInt(valmap.get("payorder").toString()));
		valmap.put("success_convert", daily_reg==0?0:convert/daily_reg);
		valmap.remove("proposal");
		valmap.remove("payorder");
		nowrs = JSONObject.fromObject(valmap).toString();
		rs.append(","+nowrs.substring(1));
		
		return rs.toString();
	}
	/* 代理处理部分结束 */
	
	@Override
	public String queryAgentReport(String loginname) {
		String month_start = DateUtil.fmtyyyy_MM_d(DateUtil.getStartDayOfMonth(new Date())) + " 00:00:00"; //本月第一天
		String lastmooth_lastday = DateUtil.fmtyyyy_MM_d(DateUtil.getMontToDate(DateUtil.getStartDayOfMonth(new Date()),-1)) + " 00:00:00"; //上个月最后一天
		String yesterday_start = DateUtil.fmtyyyy_MM_d(DateUtil.getMontToDate(new Date(), 0))+" 00:00:00";
		String yesterday_end = DateUtil.fmtyyyy_MM_d(DateUtil.getMontToDate(new Date(), 0))+" 23:59:59";
		
		//执行查询月汇总数据
		final StringBuffer sql_a = new StringBuffer("select sum(profitall) as profitall,sum(ximafee) as ximafee,sum(couponfee) as couponfee,sum(betall) ");
		sql_a.append("as betall,sum(amount) as amount from ptcommissions pt1 where pt1.agent='"+loginname+"' and ");
		sql_a.append("(pt1.createdate>='"+ lastmooth_lastday +"' ); ");
		StringBuffer rs = new StringBuffer();
		String nowrs = "";
		HashMap valmap = (HashMap) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createSQLQuery(sql_a.toString());  
                return (HashMap)query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
			}
		});
		nowrs = JSONObject.fromObject(valmap).toString();
		rs.append(nowrs.substring(0, nowrs.length()-1));
		
		//执行查询用户总数以及月总数,天总数
		final StringBuffer sql_b = new StringBuffer("select count(1) as reg,(select count(1) from users u1 where u1.agent='"+loginname+"' and ");
		sql_b.append("(u1.createtime>='"+ yesterday_start +"' and u1.createtime<='"+ yesterday_end +"')) as daily_reg, ");
		sql_b.append("(select count(1) from users u2 where u2.agent='"+loginname+"' and (u2.createtime>='"+ month_start +"' and ");
		sql_b.append("u2.createtime<='"+ yesterday_end +"')) as monthly_reg ");
		sql_b.append("from users u3 where u3.agent='"+ loginname +"'");
		valmap = (HashMap) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query query = session.createSQLQuery(sql_b.toString());  
                return (HashMap)query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
			}
		});
		Integer daily_reg = Integer.valueOf(valmap.get("daily_reg").toString());//保存每日注册用户数
		nowrs = JSONObject.fromObject(valmap).toString();
		rs.append(","+nowrs.substring(1, nowrs.length()));
		
		return rs.toString();
	}

	@Override
	public Double getDoubleValueBySql(String sql, Map<String, Object> params) {
		return userDao.getDoubleValue(sql, params);
	}

	@Override
	public Integer getCount(String sql, Map<String, Object> params) {
		return userDao.getCount(sql, params);
	}
	
	public LoginTokenService getLoginTokenService() {
		return loginTokenService;
	}

	public void setLoginTokenService(LoginTokenService loginTokenService) {
		this.loginTokenService = loginTokenService;
	}
	
	@Override
	public String nTwoGameLoginValidation(LoginValidationBean bean) {
		log.info("nTwoGameLoginValidation...");
		String status = null;
		String errdesc = null;

		Users customer = (Users) get(Users.class, bean.getUserid());
		if (customer == null) {
			status = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
			errdesc = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
			status = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
			errdesc = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
		}else if (StringUtils.equals(customer.getRole(), UserRole.AGENT.getCode()) ) {
			status = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
			errdesc = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
		} else if (!customer.getPassword().equals(EncryptionUtil.encryptPassword(bean.getPwd()))) {
			status = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
			errdesc = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
		} else {
			log.info("login game successfully");
			status = ErrorCode.SUCCESS.getCode();
			errdesc = ErrorCode.SUCCESS.getText();
			logDao.insertActionLog(bean.getUserid(), ActionLogType.LOGIN_GAME, null);
		}
		
		return NTwoUtil.getNTwoGameLoginValidationResponseXml(bean.getId(), bean.getUserid(), customer != null ? customer.getAliasName() : "", status, errdesc, customer != null ? customer.getAgcode(): "");
	}
	
	@Override
	public String nTwoGameAutoLoginValidation(String elementId, String userid, String uuid, String clinetIp) {
		log.info("nTwoGameAutoLoginValidation...");
		String status = null;
		String errdesc = null;
		Users customer = null;
		try {
			if (loginTokenService.isValidToken(NTwoUtil.PLATFORM, uuid,userid)) {
				customer = (Users) get(Users.class, userid);
				if (customer == null) {
					status = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
					errdesc = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
				} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
					status = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
					errdesc = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
				} else if (StringUtils.equals(customer.getRole(), UserRole.AGENT.getCode()) ) {
					status = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
					errdesc = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
				} else {
					log.info("login game successfully");
					status = ErrorCode.SUCCESS.getCode();
					errdesc = ErrorCode.SUCCESS.getText();
				}
			} else {
				status = ErrorCode.ERR_INVALID_REQ.getCode();
				errdesc = ErrorCode.ERR_INVALID_REQ.getCode();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			status = ErrorCode.ERR_INVALID_REQ.getCode();
			errdesc = ErrorCode.ERR_INVALID_REQ.getCode();
		}
		return NTwoUtil.getNTwoGameAutoLoginValidationResponseXml(elementId, userid, customer != null ? customer.getAliasName() : "", status, errdesc, customer != null ? customer.getAgcode(): "", uuid, clinetIp);
	} 
	
	@Override
	public String nTwoGetTicketValidation(String id, String username, String date, String sign) {
		log.info("nTwoGetTicketValidation...");
		String status = null;
		String errdesc = null;
		Users customer = null;
		String token = null;
		try {
			if (NTwoUtil.validSign(username, date, sign)) {
				customer = (Users) get(Users.class, username);
				if (customer == null) {
					status = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
					errdesc = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
				} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
					status = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
					errdesc = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
				} else if (StringUtils.equals(customer.getRole(), UserRole.AGENT.getCode()) ) {
					status = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
					errdesc = ErrorCode.ERROR_ACCOUNT_SUSPENDED.getCode();
				} else {
					token = loginTokenService.createLoginToken(NTwoUtil.PLATFORM, username);
					status = ErrorCode.SUCCESS.getCode();
					errdesc = ErrorCode.SUCCESS.getText();
				}
			} else {
				throw new IllegalAccessException("Sign验证失败");
			}
		} catch(Exception e) {
			e.printStackTrace();
			status = ErrorCode.ERR_INVALID_REQ.getCode();
			errdesc = ErrorCode.ERR_INVALID_REQ.getCode();
		}
		return NTwoUtil.getNTwoGameTicketResponeXml(id, username, token, status);
	}
	
	@Override
	public String nTwoSingleLoginUrl(String loginname) {
		StringBuffer sb = new StringBuffer();
		sb.append(NTwoUtil.HTTP).append(NTwoUtil.DOMAINNAME).append(NTwoUtil.SINGLELOGIN_URL).append("?lang=zh-cn&merchantcode=").append(NTwoUtil.MERCHANT_CODE);
		try {
			if (StringUtils.isNotEmpty(loginname)) {//玩家登陆环境
				String token = loginTokenService.createLoginToken(NTwoUtil.PLATFORM, loginname);
				sb.append("&userId=").append(loginname).append("&uuId=").append(token);
			}
			return sb.toString();
		
		} catch(Exception e){
			e.printStackTrace();
			return sb.toString();
		}
	}

	@Override
	public String nTwoAppLoginUrl(String loginname) {
		StringBuffer sb = new StringBuffer();
		sb.append(NTwoUtil.AZURITEAPP);
		try {
			if (StringUtils.isNotEmpty(loginname)) {//玩家登陆环境
				sb.append("view?merchantcode=").append(NTwoUtil.MERCHANT_CODE).append("&lang=zh-cn");
				String token = loginTokenService.createLoginToken(NTwoUtil.PLATFORM, loginname);
				sb.append("&userId=").append(loginname).append("&uuId=").append(token);
			} else {
				sb.append("merchantcode=").append(NTwoUtil.MERCHANT_CODE).append("&lang=zh-cn");
			}
			return sb.toString();
		
		} catch(Exception e){
			e.printStackTrace();
			return sb.toString();
		}
	}
	
	@Override
	public List<UrgeOrder> queryUrgeOrderList(String loginname) {
		return userDao.queryUrgeOrderList(loginname);
	}
	
	@Override
	public Byte checkthirdOrder(String tempDepositTime,
			String thirdOrder) {
		return userDao.checkthirdOrder(tempDepositTime,thirdOrder);
	}
	
	public Users getAgentByWebSiteNew(String address){
		return this.userDao.getAgentByWebSiteNew(address);
	}
	
	public Users getAgentById(Integer id){
		return this.userDao.getAgentById(id);
	}
	
	/**
	 * 检查玩家[ip]与[真实姓名]是否跟其他注册玩家重覆 
	 * */
	public boolean isUserIpOrNameDuplicate(String accountName,String registeIp){
		DetachedCriteria dc1=DetachedCriteria.forClass(Users.class);
		dc1.add(Restrictions.or(Restrictions.eq("accountName",accountName), Restrictions.eq("registerIp",registeIp)));
		List<Users> list = this.getHibernateTemplate().findByCriteria(dc1);
		for(int i=0;i<=list.size()-1; i++)
//			System.out.println(list.get(i).getLoginname());
		if(list!=null && list.size()>1){
			return true;
		}
		return false;
	}
	
	/**
	 * 检查玩家是否已申请过[app下载彩金]
	 * */
	public boolean isApplyAppPreferential(String loginName){
		DetachedCriteria dc=DetachedCriteria.forClass(AppPreferential.class);
		dc.add(Restrictions.eq("loginname",loginName));
		dc.add(Restrictions.eq("version",SynchronizedAppPreferentialUtil.appRewardVersion));
		List<Users> list = this.getHibernateTemplate().findByCriteria(dc);
		if(list!=null && list.size()>0){
			System.out.println("[已申请过app下载彩金!]");
			return true;
		}
		System.out.println("[尚未申请app下载彩金!]");
		return false;
	}
	
	public int excuteSql(String sql, Map<String, Object> params){
		return userDao.excuteSql(sql,params);
	}
	
	public UserInfo getUserInfo(String username){
		return this.userDao.getUserInfo(username);
	}

	
	
	
}
