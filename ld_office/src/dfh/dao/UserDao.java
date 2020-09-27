package dfh.dao;

import java.sql.SQLException;
import java.util.*;

import dfh.model.*;
import dfh.utils.AESUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.orm.hibernate3.HibernateCallback;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.model.enums.VipLevel;
import dfh.model.enums.WarnLevel;
import dfh.model.notdb.FirstlyCashin;
import dfh.security.EncryptionUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class UserDao extends UniversalDao {

	private LogDao logDao;

	private static Logger log = Logger.getLogger(UserDao.class);
	private StandardPBEStringEncryptor configurationEncryptor;


	public int executeUpdate(String sql, Map<String, Object> params) {
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setProperties(params);

		return query.executeUpdate();
	}

	
	public Users getUserByAgcode(String agcode)throws Exception{
		Criteria c = this.getSession().createCriteria(Users.class);
		c.add(Restrictions.eq("agcode", agcode)).add(Restrictions.eq("role", UserRole.AGENT.getCode()));
		return (Users) c.uniqueResult();
	}

	public Map<Object, Object> getResultMap(String sql){
		Map<Object, Object> map = new HashMap<Object, Object>();
		Query query = this.getSession().createSQLQuery(sql);
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {
			Object[] arr = (Object[]) list.get(i);
			Object obj1 = arr[0];
			Object obj2 = arr[1];
			map.put(obj1, obj2);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<Users> findEntities(String sql)throws Exception{
		return this.getSession().createQuery(sql).list();
	}
	
	public List loadUsers(Date start,Date end){
		Criteria c = this.getSession().createCriteria(Users.class);
		c.add(Restrictions.ge("createtime", start)).add(Restrictions.le("createtime", end));
		return c.list();
	}

	public StandardPBEStringEncryptor getConfigurationEncryptor() {
		return configurationEncryptor;
	}

	public void setConfigurationEncryptor(StandardPBEStringEncryptor configurationEncryptor) {
		this.configurationEncryptor = configurationEncryptor;
	}
	
	
	// 根据给定用户集合，查询与之关联的所有数据，names来自proposal表
	public List getUsers(List names){
		Criteria c = this.getSession().createCriteria(Users.class);
		c.add(Property.forName("loginname").in(names)).add(Restrictions.eq("flag", 0));
		c.addOrder(Order.desc("createtime"));
		return c.list();
	}
	
	
	// 根据给定来源网址，查询其代理下线用户集
	public List getAgents(String referWebsite){
		String hql="select userTwo.loginname " +
		"from users userOne inner join users userTwo on userTwo.agcode=userOne.agcode " +
		"where userTwo.flag=0 and userOne.flag=0 and userTwo.role=? and userOne.role=? and userOne.referWebsite=?";
		Query query = this.getSession().createSQLQuery(hql);
		query.setParameter(0, UserRole.MONEY_CUSTOMER.getCode()).setParameter(1, UserRole.AGENT.getCode()).setParameter(2, referWebsite);
		return query.list();
	}
	
	public List getUserPhoneListByLevel(Integer level,Date start,Date end){
		String hql="select phone from Users where flag=0 and createtime >=? and createtime <=? and level=? and phone <> ''";
		Query query = this.getSession().createQuery(hql).setParameter(0, start).setParameter(1, end).setParameter(2, level);
		return query.list(); 
	}
	
	public List getUserPhoneList(String type,Date start,Date end){
		String hql="select phone from Users where flag=0 and createtime >=? and createtime <=? and role=? and phone <> ''";
		Query query = this.getSession().createQuery(hql).setParameter(0, start).setParameter(1, end).setParameter(2, type);
		return query.list();
	}

	public String checkIsUserAccountName(String accountName) {
		String msg = null;
		if ((Integer) findByCriteria(
				DetachedCriteria.forClass(Users.class).add(Restrictions.eq("flag", Constants.FLAG_TRUE)).add(
						Restrictions.eq("accountName", accountName)).setProjection(Projections.rowCount())).get(0) > 0)
			msg = "该真实姓名已存在";
		return msg;
	}

	public UserDao() {
	}

	// 普通帐号用小写
	public String addCustomer(String loginname, String pwd, String accountName, String aliasName, String phone, String email, String qq,
			String referWebsite, String ipaddress) throws GenericDfhRuntimeException {
		String msg = null;
		loginname=StringUtil.lowerCase(loginname);
		log.info("begin to add a " + UserRole.MONEY_CUSTOMER.getText());
		Users customer = (Users) get(Users.class, loginname);

		if (customer != null) {
			msg = "用户 " + loginname + " 已存在";
			log.warn(msg);
		} else if (StringUtils.isEmpty(pwd)) {
			msg = "密码不应为空";
			log.warn(msg);
			// } else if (checkIsUserAccountName(accountName) != null) {
			// msg = "该真实姓名已存在，请联系在线客服";
			// log.warn(msg);\
			//
		} else if (checkExsitForCreateUser(accountName, ipaddress, email)) {
			msg = "真实姓名或开户IP或邮箱 重复";
			log.warn(msg);
		} else {
			String md5Str = StringUtil.getRandomString(8);
			customer = new Users();
			customer.setRole(UserRole.MONEY_CUSTOMER.getCode());
			customer.setLoginname(loginname);
			customer.setPassword(EncryptionUtil.encryptPassword(pwd));
			customer.setLevel(VipLevel.TIANBING.getCode());
			customer.setAccountName(accountName);
			customer.setAliasName(aliasName);
			customer.setPhone(phone);
			customer.setEmail(email);
			customer.setQq(qq);
			customer.setMd5str(md5Str);
			customer.setReferWebsite(referWebsite);
			customer.setPwdinfo(encrypt(pwd));
			customer.setCredit(Constants.CREDIT_ZERO);
			customer.setCreatetime(DateUtil.now());
			customer.setRegisterIp(ipaddress);

			// 根据来源网查找上级代理
			if (StringUtils.isNotEmpty(referWebsite)) {
				Users agent = getAgentByWebsite(referWebsite);
				if (agent != null) {
					customer.setAgcode(agent.getAgcode());
					customer.setAgent(agent.getLoginname());
				}
			}
			save(customer);

			// emailSender.sendMailNotify(loginname, pwd, role.getText(), email);
		}
		return msg;
	}

	// 代理帐号
	public String addAgent(String acode, String loginname, String name, String phone, String email, String qq, String referWebsite,
			String ipaddress, Integer agentType) throws GenericDfhRuntimeException {
		String msg = null;
		loginname=StringUtil.lowerCase(loginname);
		log.info("begin to add a " + UserRole.AGENT.getText());
		Users agent = (Users) get(Users.class, loginname);

		if (agent != null) {
			msg = "用户 " + loginname + " 已存在";
			log.warn(msg);
		} else if (getAgentCountByAcode(acode) > 0) {
			msg = "代理ID：" + acode + " 被注册过";
			log.warn(msg);
		} else if (getAgentByWebsite(referWebsite)!=null) {
			msg = "网址已被注册";
			log.warn(msg);
		} else {
			try {
			String md5Str = StringUtil.getRandomString(8);
				agent = new Users();
				agent.setAgcode(acode);
				agent.setRole(UserRole.AGENT.getCode());
				agent.setLoginname(loginname);
				agent.setAccountName(name);
				agent.setAliasName(name);
				agent.setPhone(AESUtil.aesEncrypt(phone, AESUtil.KEY));
				agent.setEmail(AESUtil.aesEncrypt(email, AESUtil.KEY));
				agent.setMd5str(md5Str);
				agent.setReferWebsite(referWebsite);
				agent.setPwdinfo("none");
				agent.setQq(qq);
				agent.setCredit(Constants.CREDIT_ZERO);
				agent.setCreatetime(DateUtil.now());
				agent.setRegisterIp(ipaddress);
				agent.setId(Integer.parseInt(generateUserID()));

			save(agent);
			
			Userstatus status = new Userstatus() ;
			status.setLoginname(loginname);
			status.setTouzhuflag(0);
			status.setCashinwrong(0);
			status.setSlotaccount(0.0);
			save(status);
			
			AgentAddress agentAddress = new AgentAddress();
			agentAddress.setLoginname(loginname);
			agentAddress.setAddress(referWebsite);
			agentAddress.setDeleteflag(0);
			agentAddress.setCreatetime(new Date());
			save(agentAddress);
			// emailSender.sendMailNotify(loginname, pwd, role.getText(), email);
			if(null != agentType){
				InternalAgency inAgency = new InternalAgency(loginname, agentType, new Date(), null);
				save(inAgency);
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return msg;
	}
	
	// 代理帐号(代理推荐码)
		public String addAgentForRecommendedCode(String acode, String loginname, String name, String phone, String email, String qq, String partner ,String referWebsite,
				String ipaddress, Integer agentType) throws GenericDfhRuntimeException {
			String msg = null;
			loginname=StringUtil.lowerCase(loginname);
			log.info("begin to add a " + UserRole.AGENT.getText());
			Users agent = (Users) get(Users.class, loginname);

			if (agent != null) {
				msg = "用户 " + loginname + " 已存在";
				log.warn(msg);
			} else if (getAgentCountByAcode(acode) > 0) {
				msg = "代理ID：" + acode + " 被注册过";
				log.warn(msg);
			} else if (getAgentByWebsite(referWebsite)!=null) {
				msg = "网址已被注册";
				log.warn(msg);
			} else {
				try {
				String md5Str = StringUtil.getRandomString(8);
				agent = new Users();
				agent.setAgcode(acode);
				agent.setRole(UserRole.AGENT.getCode());
				agent.setLoginname(loginname);
				agent.setAccountName(name);
				agent.setAliasName(name);
				agent.setPhone(AESUtil.aesEncrypt(phone, AESUtil.KEY));
				agent.setEmail(AESUtil.aesEncrypt(email, AESUtil.KEY));
				agent.setMd5str(md5Str);
				agent.setReferWebsite(referWebsite);
				agent.setPwdinfo("none");
				agent.setQq(qq);
				agent.setPartner(partner);
				agent.setCredit(Constants.CREDIT_ZERO);
				agent.setCreatetime(DateUtil.now());
				agent.setRegisterIp(ipaddress);
				agent.setId(Integer.parseInt(generateUserID()));

				save(agent);
				
				Userstatus status = new Userstatus() ;
				status.setLoginname(loginname);
				status.setTouzhuflag(0);
				status.setCashinwrong(0);
				status.setSlotaccount(0.0);
				save(status);
				
				AgentAddress agentAddress = new AgentAddress();
				agentAddress.setLoginname(loginname);
				agentAddress.setAddress(referWebsite);
				agentAddress.setDeleteflag(0);
				agentAddress.setCreatetime(new Date());
				save(agentAddress);
				// emailSender.sendMailNotify(loginname, pwd, role.getText(), email);
				if(null != agentType){
					InternalAgency inAgency = new InternalAgency(loginname, agentType, new Date(), null);
					save(inAgency);
				}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return msg;
		}
	
	public String generateUserID() {
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_USERID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Integer.parseInt(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_USERID);
			seq.setSeqValue("30001");
			save(seq);
		}
		return seq.getSeqValue();
	}

	public Users getAgentByWebsite(String referwebsite) {
		if (StringUtils.isNotEmpty(referwebsite)) {
			DetachedCriteria agentCriteria = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.AGENT.getCode()));
			agentCriteria = agentCriteria.add(Restrictions.eq("referWebsite", referwebsite)).add(Restrictions.eq("flag", Constants.ENABLE));
			List list = getHibernateTemplate().findByCriteria(agentCriteria);
			if (list != null && list.size() > 0)
				return (Users) list.get(0);
		}
		return null;
	}
	public Users getAgentByWebsite2(String referwebsite) {
		if (StringUtils.isNotEmpty(referwebsite)) {
			DetachedCriteria agentCriteria = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.AGENT.getCode()));
			agentCriteria = agentCriteria.add(Restrictions.eq("referWebsite", referwebsite));
			List list = getHibernateTemplate().findByCriteria(agentCriteria);
			if (list != null && list.size() > 0)
				return (Users) list.get(0);
		}
		return null;
	}

	private int getAgentCountByAcode(String acode) {
		DetachedCriteria agentCriteria = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.AGENT.getCode()));
		agentCriteria = agentCriteria.add(Restrictions.eq("agcode", acode));
		List list = getHibernateTemplate().findByCriteria(agentCriteria);
		if (list != null)
			return list.size();
		return 0;
	}

	public boolean checkPartnerNotExsit(String partner) {
		if (StringUtils.isNotEmpty(partner)) {
			Users userPartner = (Users) this.get(Users.class, partner);
			// if (userPartner == null || !userPartner.getRole().equals(UserRole.PARTNER.getCode())) {
			// log.warn("该合作伙伴账号不存在");
			// return true;
			// }
		}
		return false;
	}

	public String checkCustomer(Users customer) {
		String msg = null;
		try {
			if (customer == null) {
				msg = "该会员不存在";
				log.warn(msg);
			} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
				msg = "该会员已经被禁用";
				log.warn(msg);
				// } else if (!customer.getRole().equals(UserRole.FREE_CUSTOMER.getCode()) &&
				// !customer.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())) {
				// msg = "该帐号不是可游戏会员";
				// log.warn(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return msg;
	}

	public String checkCustomerForTransfer(Users customer) {
		String msg = null;
		try {
			if (customer == null) {
				msg = "该会员不存在";
				log.warn(msg);
				// } else if (!customer.getRole().equals(UserRole.FREE_CUSTOMER.getCode()) &&
				// !customer.getRole().equals(UserRole.MONEY_CUSTOMER.getCode())) {
				// msg = "该帐号不是可游戏会员";
				// log.warn(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return msg;
	}

	public String checkUser(Users customer) {
		String msg = null;
		if (customer == null) {
			msg = "该会员不存在";
			log.warn(msg);
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
			msg = "该会员已经被禁用";
			log.warn(msg);
		}
		return msg;
	}

	public String checkUserForProposal(Users customer) {
		String msg = null;
		if (customer == null) {
			msg = "该会员不存在";
			log.warn(msg);
		} else if (customer.getFlag().intValue() == Constants.DISABLE.intValue()) {
			msg = "该会员已经被禁用";
			log.warn(msg);
			// } else if (customer.getRole().equals(UserRole.FREE_CUSTOMER.getCode())) {
			// msg = "试玩会员不能参与提案申请";
			// log.warn(msg);
		}
		return msg;
	}

	public Date getLastXimaEndTime(String loginname) {
		final String userName = loginname;
		Date date = (Date) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("select max(x.endTime) from proposal p,xima x where p.pno=x.pno and p.flag="
						+ ProposalFlagType.EXCUTED.getCode() + " and p.loginname='" + userName + "'");
				return sqlQuery.uniqueResult();
			}
		});
		if (date != null) {
			return date;
		}
		return null;
	}

	public Date getFirstProposalCreditTime(String loginname) {
		final String userName = loginname;
		Date date = (Date) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("select min(this_.createTime) as y0_ from proposal this_ where this_.loginname='"
						+ userName + "' and this_.flag=" + ProposalFlagType.EXCUTED.getCode() + " and this_.type="
						+ ProposalType.CASHIN.getCode() + "");
				return sqlQuery.uniqueResult();
			}
		});
		if (date != null) {
			return date;
		}
		return null;
	}

	public Date getFirstPayOrderCreditTime(String loginname) {
		final String userName = loginname;
		Date date = (Date) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("select min(this_.createTime) as y0_ from payOrder this_ where this_.loginname='"
						+ userName + "' and this_.flag=" + Constants.FLAG_TRUE + "");
				return sqlQuery.uniqueResult();
			}
		});
		if (date != null) {
			return date;
		}
		return null;
	}

	// 有效投注额
	public Double getValidBetAmount(String loginname, Date start, Date end, String userRole) {
		Double totalBetAmount = 0.00;
		DetachedCriteria dcBetRecord = DetachedCriteria.forClass(Betrecords.class).add(Restrictions.sqlRestriction("result!=''")).add(
				Restrictions.sqlRestriction("result!=0")).add(Restrictions.sqlRestriction("result is not null"));
		if (StringUtils.isNotEmpty(loginname))
			dcBetRecord = dcBetRecord.add(Restrictions.eq("passport", loginname));
		if (StringUtils.isNotEmpty(userRole)) {
			if (userRole.equals(UserRole.MONEY_CUSTOMER.getCode()))
				dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" + Constants.PREFIX_MONEY_CUSTOMER + "%"));
			// if (userRole.equals(UserRole.FREE_CUSTOMER.getCode()))
			// dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" + Constants.PREFIX_FREE_CUSTOMER + "%"));
		}
		if (start != null)
			dcBetRecord = dcBetRecord.add(Restrictions.ge("billTime", start));
		if (end != null)
			dcBetRecord = dcBetRecord.add(Restrictions.lt("billTime", end));
		dcBetRecord.setProjection(Projections.projectionList().add(Projections.sum("billAmount")).add(Projections.sum("result")));
		List resultBetRecord = getHibernateTemplate().findByCriteria(dcBetRecord);
		Object[] arrayBetRecord = (Object[]) resultBetRecord.get(0);
		if (arrayBetRecord[0] != null)
			totalBetAmount += (Double) arrayBetRecord[0];
		return totalBetAmount;
	}

	// 洗码率
	public Double getXimaRate(Double validaBetAmount) {
		Double rate = 0.0;
		if (validaBetAmount <= 100 * 10000) {
			rate = 0.0045;
//		} else if (validaBetAmount > 50 * 10000 && validaBetAmount <= 100 * 10000) {
//			rate = 0.004;
		} else if (validaBetAmount > 100 * 10000 && validaBetAmount <= 300 * 10000) {
			rate = 0.006;
		} else if (validaBetAmount > 300 * 10000 && validaBetAmount <= 500 * 10000) {
			rate = 0.008;
		} else if (validaBetAmount > 500 * 10000) {
			rate = 0.01;
		}
		return rate;
	}

	/**
	 * @author sun
	 * @return success null
	 */
	public String checkCommissionrecords(String loginname, Integer year, Integer month) {
		String msg = null;
		DetachedCriteria dc = DetachedCriteria.forClass(Commissionrecords.class).add(
				Restrictions.eq("id", new CommissionrecordsId(loginname, year, month)));
		List list = getHibernateTemplate().findByCriteria(dc);
		if (list != null && list.size() > 0)
			msg = "此用户本月已经结算过佣金";
		return msg;
	}

	/**
	 * 用户第一笔存款
	 * 
	 * @author sun
	 * @return
	 */
	public FirstlyCashin getFirstCashin(String loginname) {
		if (StringUtils.isEmpty(loginname))
			return null;
		FirstlyCashin firstlyCashin = null;
		try {
			Date firstlyPTime = getFirstProposalCreditTime(loginname);
			if (firstlyPTime != null) {
				DetachedCriteria dcProposal = DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("loginname", loginname)).add(
						Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode())).add(Restrictions.eq("type", ProposalType.CASHIN.getCode()))
						.add(Restrictions.eq("createTime", firstlyPTime));
				Proposal proposal = (Proposal) getHibernateTemplate().findByCriteria(dcProposal).get(0);
//				System.out.println(proposal.toString());

				firstlyCashin = new FirstlyCashin();
				firstlyCashin.setLoginname(proposal.getLoginname());
				firstlyCashin.setMoney(proposal.getAmount());
				firstlyCashin.setCreateTime(proposal.getCreateTime());
			}
			Date firstlyPayOrderTime = getFirstPayOrderCreditTime(loginname);
			if (firstlyPTime != null && firstlyPayOrderTime != null) {
				if (!firstlyPTime.after(firstlyPayOrderTime)) {
					return firstlyCashin;
				}
			}
			if (firstlyPayOrderTime != null) {
				DetachedCriteria dcPayorder = DetachedCriteria.forClass(Payorder.class).add(Restrictions.eq("loginname", loginname)).add(
						Restrictions.eq("flag", Constants.FLAG_TRUE)).add(Restrictions.eq("createTime", firstlyPayOrderTime));
				Payorder payOrder = (Payorder) getHibernateTemplate().findByCriteria(dcPayorder).get(0);
				firstlyCashin = new FirstlyCashin();
				firstlyCashin.setLoginname(payOrder.getLoginname());
				firstlyCashin.setMoney(payOrder.getMoney());
				firstlyCashin.setCreateTime(payOrder.getCreateTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return firstlyCashin;
	}

	/**
	 * 加密
	 * 
	 * @author sun
	 * @return
	 */
	public String encrypt(String arg0) {
		return configurationEncryptor.encrypt(arg0);
	}

	/**
	 * 解密
	 * 
	 * @author sun
	 * @return
	 */
	public String decrypt(String arg0) {
		return configurationEncryptor.decrypt(arg0);
	}

	public String setLevel(String loginname, Integer level, String operator) {
		if (StringUtils.isEmpty(loginname))
			return "帐号不能为空";

		String msg = null;
		Users user = (Users) get(Users.class, loginname);
		if (user == null) {
			msg = "帐号不存在";
		} else {
			Integer srcLevel = user.getLevel();
			if (srcLevel.intValue() == level.intValue())
				return "修改级别与现有级别相同";
			user.setLevel(level);
			// user.setRemoteUrl(getRemoteUrlFromConst(user));
			update(user);
			logDao.insertOperationLog(operator, OperationLogType.SETLEVEL, "将会员" + loginname + "的等级从[" + VipLevel.getText(srcLevel) + "]改为["
					+ VipLevel.getText(level) + "]");
			msg = null;
		}
		return msg;
	}

	public Proposal getConcessions(String loginname) {
		if (StringUtils.isEmpty(loginname))
			return null;
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("loginname", loginname));//.add(Restrictions.eq("type", ProposalType.CONCESSIONS.getCode()))
		List list = getHibernateTemplate().findByCriteria(dc);
		if (list != null && list.size() > 0)
			return (Proposal) list.get(0);
		return null;
	}

	public void setUserCashin(String loginname) {
		if (StringUtils.isNotEmpty(loginname)) {
			Users user = (Users) get(Users.class, loginname);
			if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
				user.setIsCashin(Constants.FLAG_TRUE.intValue());
				save(user);
			}
		}
	}

	public List<Users> getSubUsers(String agent) {
		List<Users> list = new ArrayList<Users>();
		if (StringUtils.isNotEmpty(agent)) {
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode())).add(
					Restrictions.eq("agent", agent));
			list = findByCriteria(dc);
		}
		return list;
	}

	/**
	 * 同姓名 同ip 同邮箱 不能重复注册
	 * 
	 * @param accountname
	 * @param ip
	 * @param email
	 * @return
	 */
	public boolean checkExsitForCreateUser(String accountname, String ip, String email) {
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode()));
		Junction disjuction = Restrictions.disjunction();
		if (StringUtils.isNotEmpty(accountname))
			disjuction = disjuction.add(Restrictions.eq("accountName", accountname));
		if (StringUtils.isNotEmpty(email))
			disjuction = disjuction.add(Restrictions.eq("email", email));
		if (StringUtils.isNotEmpty(ip))
			disjuction = disjuction.add(Restrictions.eq("registerIp", ip));
		dc = dc.add(disjuction);
		int size = findByCriteria(dc).size();
		log.info("checkExsitForCreateUser size:" + size);
		return size > 0;
	}
	
	public boolean updateAgcode(String oldagcode,String newagcode){
		Query q = this.getSession().createQuery("update Users set agcode=? where agcode=?");
		q.setParameter(0, newagcode);
		q.setParameter(1, oldagcode);
		return q.executeUpdate()>=1?true:false;
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public String setWarnLevel(String loginname, Integer warnlevel,
			String operatorLoginname, String warnremark) {
		if (StringUtils.isEmpty(loginname))
			return "帐号不能为空";

		String msg = null;
		Users user = (Users) get(Users.class, loginname);
		if (user == null) {
			msg = "帐号不存在";
		} else {
			Integer srcwarnLevel = user.getWarnflag();
			if (srcwarnLevel.intValue() == warnlevel.intValue())
				return "修改级别与现有级别相同";
			user.setWarnflag(warnlevel);
			user.setWarnremark(warnremark);
			// user.setRemoteUrl(getRemoteUrlFromConst(user));
			update(user);
			logDao.insertOperationLog(operatorLoginname, OperationLogType.SETWARNLEVEL, "将会员" + loginname + "的警告等级从[" + WarnLevel.getText(srcwarnLevel) + "]改为["
					+ WarnLevel.getText(warnlevel) + "]" +"备注："+warnremark);
			msg = null;
		}
		return msg;
	}
	
	public Double getAgentProposal(Date start,Date end){
		Criteria criteria = getSession().createCriteria(Proposal.class);
		criteria.add(Restrictions.eq("type", 550));
		criteria.add(Restrictions.eq("flag", 2));
		criteria.add(Restrictions.ge("executeTime", start));
		criteria.add(Restrictions.lt("executeTime", end));
		criteria.setProjection(Projections.sum("amount"));
		List list = criteria.list();
		if(list!=null && list.size()>0 && list.get(0)!=null){
			Iterator iterator =  list.iterator();
			while(iterator.hasNext()) {
			    return (Double)iterator.next();      
			}
		}
		return 0.00;
	}

	@SuppressWarnings("unchecked")
	public List queryList(String sql, Map<String, Object> params){
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.list();
	}
	
	public Integer getCount(String sql, Map<String, Object> params){
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return Integer.parseInt(query.uniqueResult().toString());
	}
	
	public int excuteSql(String sql, Map<String, Object> params){
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.executeUpdate();
	}
	
	public boolean updatePayOrderValidation(String wechat, Double amount, String billno){
		Query q = this.getSession().createSQLQuery("update payorder_validation set status='1', bankcard='"+wechat+"', remark='补单"+billno+"' where status='0' and type='wechat' and amount="+amount);
		return q.executeUpdate()>=1?true:false;
	}

	public String getRole(String loginname) {
		String sql="select role from users where loginname=:loginname";
		Query query = this.getSession().createSQLQuery(sql).setString("loginname", loginname);
		return (String)query.uniqueResult();
	}
	
	public Boolean bindEmployeeNO(String loginname, String employeeNo) {
		String sql ="update operator set validType = 2 ,employeeNo = ? where username = ?";
		Query q = this.getSession().createSQLQuery(sql).setString(0, employeeNo).setString(1, loginname);
		return q.executeUpdate()>=1?Boolean.TRUE:Boolean.FALSE;
	}
	
	public List<AgentAddress> getAgentAddressList(String loginname) {
		List<AgentAddress> agentAddressList = (List<AgentAddress>)this.getSession().createSQLQuery("select * from agent_address where loginname = '" + loginname + "' and deleteflag=0").list();
		return agentAddressList;
	}
	
	public Boolean batchUpdate(String belongto, String ids) {
		String sql = "update internal_agency set belongto = ? where loginname in ("+ids+") ";
		Query q = this.getSession().createSQLQuery(sql).setString(0, belongto);
		return q.executeUpdate()>=1?Boolean.TRUE:Boolean.FALSE;
	}
}
