package dfh.service.implementations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.dao.LogDao;
import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Accountinfo;
import dfh.model.SystemConfig;
import dfh.model.Users;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.notdb.FirstlyCashin;
import dfh.remote.ErrorCode;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.LoginValidationBean;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.CustomerService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class CustomerServiceImpl extends UniversalServiceImpl implements CustomerService {

	private static Logger log = Logger.getLogger(CustomerServiceImpl.class);
	private LogDao logDao;
	private TradeDao tradeDao;
	private TransferDao transferDao;
	private UserDao userDao;

	public CustomerServiceImpl() {

	}

	public LogDao getLogDao() {

		return logDao;
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

	@Override
	public Map<Object, Object> getResultMap(String sql) {
		return userDao.getResultMap(sql);
	}

	public String getRemoteAgCredit(String loginname) {

		try {
			String credit = RemoteCaller.queryDspCredit(loginname);
			return credit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getRemoteBbinCredit(String loginname) {

		try {
			String credit = RemoteCaller.queryBbinCredit(loginname);
			return credit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getRemoteKenoCredit(String loginname) {

		try {
			String credit = RemoteCaller.queryKenoCredit(loginname);
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
			msg = "该会员已经被禁用";
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
			msg = "该会员已经被禁用";
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
		 * if (!RemoteConstant.TEST_ACCOUNTS.contains(bean.getUserid())) {
		 * log.info("not testaccount,can't login"); status =
		 * ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode(); return
		 * RemoteCaller.getLoginValidationResponseXml(bean.getId(),
		 * bean.getUserid(), "", status, errdesc, ""); }
		 */

		Users customer = (Users) get(Users.class, bean.getUserid());
		if (customer == null) {
			status = ErrorCode.ERROR_INVALID_ACCOUNT_ID.getCode();
			log.warn(status);
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
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
			logDao.insertActionLog(bean.getUserid(), ActionLogType.LOGIN_GAME, null);
		}
		return RemoteCaller.getLoginValidationResponseXml(bean.getId(), bean.getUserid(),
				customer != null ? customer.getAliasName() : "", status, errdesc, customer != null ? customer.getAgcode()
						: "");
	}

	public String modifyBankInfo(String loginname, String bank, String bankAddress, String accountType, String accountName,
			String accountNo, String accountCity, String ip)
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
			Accountinfo accountinfo = new Accountinfo(loginname, accountName, accountNo, bank, accountType, accountCity,
					bankAddress, DateUtil.now(), DateUtil.now());
			save(accountinfo);
			logDao.insertActionLog(loginname, ActionLogType.MODIFY_BANK_INFO, ip);
			msg = null;
		}
		return msg;
	}

	// 邮箱不可更改
	public String modifyCustomerInfo(String loginname, String aliasName, String phone, String email, String qq, String ip)
			throws GenericDfhRuntimeException {

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

	public String modifyPassword(String loginname, String oldPwd, String newPwd, String ip)
			throws GenericDfhRuntimeException {

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
			logDao.insertActionLog(loginname, ActionLogType.MODIFY_PWD, ip);
		}
		return msg;
	}

	public String queryUserLastXimaEndTime(String loginname) {

		final String userName = loginname;
		Date date = (Date) getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				SQLQuery sqlQuery = session
						.createSQLQuery("select max(x.endTime) from proposal p,xima x where p.pno=x.pno and p.flag="
								+ ProposalFlagType.EXCUTED.getCode() + " and p.loginname='"
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

	public String register(String loginname, String pwd, String accountName, String aliasName, String phone, String email,
			String qq, String referWebsite, String ipaddress) {

		return userDao.addCustomer(loginname, pwd, accountName, aliasName, phone, email, qq, referWebsite, ipaddress);
	}

	public List<Users> getSubUsers(String agent) {

		return userDao.getSubUsers(agent);
	}

	@Override
	public String getRemoteAgInCredit(String loginname) {

		try {
			String credit = RemoteCaller.queryAgInDspCredit(loginname);
			return credit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getRemoteSbCredit(String loginname) {

		try {
			return RemoteCaller.querySBCredit(loginname);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateIntroForCS() {

		@SuppressWarnings("unchecked")
		List<Users> users = getHibernateTemplate().executeFind(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

				// String InternalAgent =
				// Configuration.getInstance().getValue("InternalAgent") ;
				String InternalAgent = querySystemConfig(getHibernateTemplate(), "type101", "001", "否");
				String[] agents = InternalAgent.split(";");
				if (agents.length < 1) {
					return null;
				}
				String agentStr = "";
				for (int i = 0; i < agents.length; i++) {
					agentStr += ";'" + agents[i] + "'";
				}
				agentStr = agentStr.substring(1, agentStr.length()).replace(";", ",");
				SQLQuery query = session
						.createSQLQuery("select u.* FROM users u left join (select sub_a.loginname , sum(sub_a.amount) amount from agprofit sub_a group by sub_a.loginname) a on a.loginname = u.loginname where  (u.intro='' or u.intro is null ) and u.role='MONEY_CUSTOMER' and (u.agent='' or u.agent is null  or u.agent not in("
								+ agentStr + "))  order by a.amount desc ");
				return query.addEntity(Users.class).list();
			}
		});
		log.info("更新intro---->" + users.size());

		String args[] = { "woodytest", "jack168", "michael", "gtrsc800", "gaotong888", "gaotong", "gtrsc700", "aaron",
				"hchen88", "b464120451", "elfbjl", "zt666", "c15534506660", "wj19680909", "xiao419160", "missout",
				"yfx118118", "uncletwo", "yoyohe", "lid420528", "cheyuanda", "sm789", "zy513699", "sunlin1", "maxnt",
				"q904817235", "liyong6969", "flyhunter", "zyy5168", "sxf888", "a958888", "hlm586878", "m4167", "zdxzdx0571",
				"xu71989", "pkelve", "meifeng", "q904817235", "chch7788", "kim584", "xun168", "ff0088", "xutao12345",
				"lijin1983", "xutao55666", "biankai88", "gaoying1981", "guoguo8888", "guoguo0715" };
		List list = Arrays.asList(args);

		int count = 0;
		for (Object object : users) {
			String str = "";
			Users user = (Users) object;
			if (list.contains(user.getLoginname())) {
				log.info("  大客---->" + user.getLoginname());
				continue;
			}
			log.info("  ---->" + user.getLoginname());
			if (count == 0) {
				str = "cs1";
				count = 1;
			} else if (count == 1) {
				str = "cs2";
				count = 2;
			} else if (count == 2) {
				str = "cs3";
				count = 3;
			} else if (count == 3) {
				str = "cs4";
				count = 4;
			} else if (count == 4) {
				str = "cs5";
				count = 5;
			} else if (count == 5) {
				str = "cs6";
				count = 6;
			}
			else if (count == 6) {
				str = "cs6";
				count = 7;
			} else if (count == 7) {
				str = "cs5";
				count = 8;
			} else if (count == 8) {
				str = "cs4";
				count = 9;
			} else if (count == 9) {
				str = "cs3";
				count = 10;
			} else if (count == 10) {
				str = "cs2";
				count = 11;
			} else if (count == 11) {
				str = "cs1";
				count = 0;
			}
			user.setIntro(str);
			update(user);
		}

	}

	@Override
	public List<Object[]> queryUserByQQ(String qq) {

		String sql = "select u1.loginname,u1.agent,u1.role,u1.mailaddress from Users u1 where u1.qq=? or u1.mailaddress like '"
				+ qq + "%'";
		List<Object[]> uls = userDao.getHibernateTemplate().find(sql, new Object[] { qq });
		return uls;
	}

	private String querySystemConfig(HibernateTemplate getHibernateTemplate, String typeNo, String itemNo, String flag) {

		String str = "";
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if (!StringUtils.isEmpty(typeNo)) {
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if (!StringUtils.isEmpty(itemNo)) {
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if (!StringUtils.isEmpty(flag)) {
			dc = dc.add(Restrictions.eq("flag", flag));
		}
		List<SystemConfig> list = getHibernateTemplate.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			str = list.get(0).getValue();
		}
		return str;
	}

	public int executeUpdate(String sql, Map<String, Object> params) {
		return userDao.executeUpdate(sql,params);
	}
	
	@Override
	public Boolean batchUpdate(String belongto,String ids) {
		return userDao.batchUpdate(belongto,ids);
	}

}
