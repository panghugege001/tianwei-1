package dfh.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Actionlogs;
import dfh.model.AgentAddress;
import dfh.model.Betrecords;
import dfh.model.Commissionrecords;
import dfh.model.CommissionrecordsId;
import dfh.model.Payorder;
import dfh.model.Proposal;
import dfh.model.Sbbets;
import dfh.model.Sbcoupon;
import dfh.model.Seq;
import dfh.model.SystemConfig;
import dfh.model.UrgeOrder;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.model.enums.VipLevel;
import dfh.model.notdb.FirstlyCashin;
import dfh.model.weCustomer.UserInfo;
import dfh.security.EncryptionUtil;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class UserDao extends UniversalDao {

	private LogDao logDao;

	private static Logger log = Logger.getLogger(UserDao.class);
	private StandardPBEStringEncryptor configurationEncryptor;

	public StandardPBEStringEncryptor getConfigurationEncryptor() {
		return configurationEncryptor;
	}

	public void setConfigurationEncryptor(StandardPBEStringEncryptor configurationEncryptor) {
		this.configurationEncryptor = configurationEncryptor;
	}

	public List getUserPhoneList(String type, Date start, Date end) {
		// Criteria c = this.getSession().createCriteria(Users.class);
		// c.add(Restrictions.eq("role", type)).add(Restrictions.eq("flag",
		// "0"));
		// c.add(Restrictions.ge("createtime",
		// start)).add(Restrictions.ge("createtime", end));
		// c.add(Restrictions.isNotEmpty("phone"));
		String hql = "select phone from Users where flag=0 and createtime >=? and createtime <=? and role=? and phone <> ''";
		Query query = this.getSession().createQuery(hql).setParameter(0, start).setParameter(1, end).setParameter(2, type);
		return query.list();
	}

	public String checkIsUserAccountName(String accountName) {
		String msg = null;
		if ((Integer) findByCriteria(DetachedCriteria.forClass(Users.class).add(Restrictions.eq("flag", Constants.FLAG_TRUE)).add(Restrictions.eq("accountName", accountName)).setProjection(Projections.rowCount())).get(0) > 0)
			msg = "该真实姓名已存在";
		return msg;
	}

	public UserDao() {
	}

	// 普通帐号用小写
	public String addCustomer(String howToKnow, Integer sms, String loginname, String pwd, String accountName, String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress, String city, String birthday, String intro,String agentcode,String ioBB,String microchannel) throws GenericDfhRuntimeException {
		String msg = null;
		loginname = StringUtil.lowerCase(loginname);
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
		} else {
			String remark = "";
			Integer warnFlag = 0 ;
			int flag = 0;
			if (checkExsitForCreateUserIp(accountName, ipaddress, email,phone)){
				flag=0; 
				remark="有同IP或者同姓名注册";
				warnFlag = 2 ;
			}
			
			if (checkExsitForCreateUserEmail(accountName, ipaddress, email,phone)){
				flag=1; 
				remark="有同手机号或者同邮箱注册";
				warnFlag = 2 ;
				return "有同手机号或者同邮箱注册 ！不允许注册，请使用账号找回密码功能 ！";
			}
			/*if (ioBB==null||ioBB.equals("")){
				flag=0; // 根据要求：新开户用户，如果提供的信息（电子邮件地址，姓名，ip与现有用户相同，可以注册，但是状态为禁用。）
				remark="deviceID:null";
				//warnFlag = 2 ;
			}else{
				remark=remark+"deviceID:"+ioBB;
			}*/

			String md5Str = StringUtil.getRandomString(8);
			customer = new Users();
			customer.setWarnflag(warnFlag);
			customer.setRole(UserRole.MONEY_CUSTOMER.getCode());
			customer.setLoginname(loginname);
			customer.setPassword(EncryptionUtil.encryptPassword(pwd));
			customer.setLevel(VipLevel.TIANBING.getCode());
			/*customer.setAccountName(accountName);*/
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
			customer.setFlag(flag);
			customer.setRemark(remark);
			customer.setSms(0);
			customer.setInvitecode(intro);
			customer.setHowToKnow(howToKnow);
			customer.setId(Integer.parseInt(generateUserID()));
			customer.setPostcode(StringUtil.getRandomString(10));
			customer.setMicrochannel(microchannel);
			if (birthday != null && !birthday.equals("")) {
				int year = Integer.parseInt(birthday.split("-")[0]);
				int month = Integer.parseInt(birthday.split("-")[1]);
				int day = Integer.parseInt(birthday.split("-")[2]);
				customer.setBirthday(DateUtil.date2Timestampyyyy_MM_dd_HH(DateUtil.getDate(year, month, day, 12), 12));
			}
			
			if(sms!=null&&sms==0){
				Userstatus userstatus=new Userstatus();
				userstatus.setLoginname(loginname);
				userstatus.setCashinwrong(0);
				userstatus.setLoginerrornum(0);
				userstatus.setMailflag(0);
				save(userstatus);
			}
			
			// 根据来源网查找上级代理
			Boolean mobileRegisterFlag = false;
			Users agent = null ;
			if(org.apache.commons.lang3.StringUtils.isNotBlank(agentcode) && NumberUtils.isNumber(agentcode)){
				//agentcode优先，找不到代理的话再按照referWebsite来查找
				agent = getAgentById(Integer.parseInt(agentcode)) ;
			}
			if(null != agent){
				mobileRegisterFlag = true;
			}
			if(null == agent && StringUtils.isNotEmpty(referWebsite)){
				agent = getAgentByWebSiteNew(referWebsite);  //getAgentByWebSiteNew
			}
			if(null != agent){
				customer.setAgcode(agent.getAgcode());
				customer.setAgent(agent.getLoginname());
				/*if(agent.getLoginname().trim().toLowerCase().equals("a_jun")){
					customer.setFlag(Constants.DISABLE);
				}*/
			}
			
//			if (StringUtils.isNotEmpty(referWebsite)) {
//				Users agent = getAgentByWebsite(referWebsite);
//				if (agent != null) {
//					customer.setAgcode(agent.getAgcode());
//					customer.setAgent(agent.getLoginname());
//				}
//			}
			this.fillIntro(customer);
			save(customer);
			if(null != agent && mobileRegisterFlag){
				Actionlogs actionlog = new Actionlogs();
				actionlog.setLoginname(loginname);
				actionlog.setCreatetime(app.util.DateUtil.getCurrentTime()); 
				actionlog.setAction(ActionLogType.MOBILE_REGISTER.getCode());
				actionlog.setRemark("ip:" + ipaddress + ";注册地：" + city + ";代理ID：" + agent.getId() + ";代理码：" + agent.getAgcode());
				save(actionlog);
			}

			// emailSender.sendMailNotify(loginname, pwd, role.getText(),
			// email);
		}
		return msg;
	}

	// 普通帐号用小写
	public String addCustomerTwo(String howToKnow, Integer sms, String loginname, String pwd, String accountName, String aliasName, String phone, String email, String qq, String referWebsite, String ipaddress, String city, String birthday, String intro, Double gifTamount, String agentStr,String type,String ioBB,String microchannel) throws GenericDfhRuntimeException {
		String msg = null;
		loginname = StringUtil.lowerCase(loginname);
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
		} else {
			String remark = "";
			int flag = 0;
			Integer warnFlag = 0 ;
			if (checkExsitForCreateUserIp(accountName, ipaddress, email,phone)){
				flag=0; // 根据要求：新开户用户，如果提供的信息（电子邮件地址，姓名，ip与现有用户相同，可以注册，但是状态为禁用。）
				remark="有同IP或者同姓名注册";
				warnFlag = 2 ;
			}
			
			if (checkExsitForCreateUserEmail(accountName, ipaddress, email,phone)){
				flag=1; // 根据要求：新开户用户，如果提供的信息（电子邮件地址，姓名，ip与现有用户相同，可以注册，但是状态为禁用。）
				remark="有同手机号或者同邮箱注册";
				warnFlag = 2 ;
				return "有同手机号或者同邮箱注册 ！不允许注册，请使用账号找回密码功能 ！";
			}
			/*if (ioBB==null||ioBB.equals("")){
				flag=0; // 根据要求：新开户用户，如果提供的信息（电子邮件地址，姓名，ip与现有用户相同，可以注册，但是状态为禁用。）
				remark="deviceID:null";
				//warnFlag = 2 ;
			}else{
				remark=remark+"deviceID:"+ioBB;
			}*/

			String md5Str = StringUtil.getRandomString(8);
			customer = new Users();
			customer.setWarnflag(warnFlag);
			customer.setRole(UserRole.MONEY_CUSTOMER.getCode());
			customer.setLoginname(loginname);
			customer.setPassword(EncryptionUtil.encryptPassword(pwd));
			customer.setLevel(VipLevel.TIANBING.getCode());
			/*customer.setAccountName(accountName);*/
			customer.setAliasName(aliasName);
			customer.setPhone(phone);
			customer.setEmail(email);
			customer.setQq(qq);
			customer.setMd5str(md5Str);
			customer.setPwdinfo(encrypt(pwd));
			customer.setCredit(Constants.CREDIT_ZERO);
			customer.setCreatetime(DateUtil.now());
			customer.setRegisterIp(ipaddress);
			customer.setFlag(flag);
			customer.setRemark(remark);
			customer.setIntro(type);  //之前的邀请码字段用来存储a/b/c/d/e  ， 邀请码放在新建的invitecode字段
			customer.setInvitecode(intro);
			customer.setSms(0);
			customer.setId(Integer.parseInt(generateUserID()));
			customer.setPostcode(StringUtil.getRandomString(10));
			customer.setGifTamount(gifTamount);
			customer.setMicrochannel(microchannel);
			if (birthday != null && !birthday.equals("")) {
				int year = Integer.parseInt(birthday.split("-")[0]);
				int month = Integer.parseInt(birthday.split("-")[1]);
				int day = Integer.parseInt(birthday.split("-")[2]);
				customer.setBirthday(DateUtil.date2Timestampyyyy_MM_dd_HH(DateUtil.getDate(year, month, day, 12), 12));
			}
			// 根据来源网查找上级代理
			Users agent = getAgentByWebsiteLogin(agentStr);
			if (agent != null) {
				customer.setAgcode(agent.getAgcode());
				customer.setAgent(agent.getLoginname());
				customer.setReferWebsite(agent.getReferWebsite());
				customer.setHowToKnow(agent.getHowToKnow());
			}
			
			this.fillIntro(customer);
			save(customer);

			// emailSender.sendMailNotify(loginname, pwd, role.getText(),
			// email);
		}
		return msg;
	}
	private boolean checkPartener(String partner) {

		if(StringUtils.isBlank(partner)){
			return false;
		}

		DetachedCriteria systemConfigDc = DetachedCriteria.forClass(SystemConfig.class);
		systemConfigDc.add(Restrictions.eq("typeNo", "type150"));
		systemConfigDc.add(Restrictions.eq("flag", "否"));
		List<SystemConfig> configList = this.findByCriteria(systemConfigDc,0,1);

		if(configList != null && configList.size() > 0 && StringUtils.isNotBlank(configList.get(0).getValue())){

			String partners = configList.get(0).getValue();
			String[] partnersArr = partners.split(";");
			for(int i = 0; i < partnersArr.length; i++){
				if(partner.equalsIgnoreCase(partnersArr[i])){
					return true;
				}
			}
		} else {
			return true;//如果没有该选项，或该选项为空，或该选项关闭，则不需要修改原来的partner
		}
		return false;
	}
	
	/**
	 * 自动添加客服标识
	 * */
	private void fillIntro(Users customer) {
		
		if("AGENT".equals(customer.getRole()) || StringUtils.isNotBlank(customer.getIntro())){
			return;
		}
		
		if(StringUtils.isNotBlank(customer.getAgent())){
			String InternalAgent = querySystemConfig(getHibernateTemplate() ,"type101","001","否");
			if(InternalAgent != null){
				String [] agents = InternalAgent.split(";");
				if(agents != null && agents.length > 0){
					for(int i = 0; i < agents.length; i++){
						if(customer.getAgent().equals(agents[i])){
							return;
						}
					}
				}
			}
		}
		
		customer.setIntro(this.getIntro());
	}
	
	private synchronized String getIntro() {
		Seq seq = null;
		Object obj = this.get(Seq.class, Constants.SEQ_CSNO);
		int count;
		if (obj != null) {
			seq = (Seq) obj;
			String seqValue = seq.getSeqValue();
			count = Integer.parseInt(seqValue);
		} else {
			count = 0;
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_CSNO);
			seq.setSeqValue("0");
			this.save(seq);
		}
		
		String str = "";
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
		}else if (count == 6) {
			str = "cs7";
			count = 7;
		}else if (count == 7) {
			str = "cs8";
			count = 8;
		}else if (count == 8) {
			str = "cs9";
			count = 9;
		}else if (count == 9) {
			str = "cs10";
			count = 10;
		}else if (count == 10) {
			str = "cs11";
			count = 11;
		}else if (count == 11) {
			str = "cs12";
			count = 0;
		}
		
		seq.setSeqValue(count + "");
		this.update(seq);
		return str;
	}
	
	public  String querySystemConfig(HibernateTemplate getHibernateTemplate,String typeNo,String itemNo,String flag){
		String str="";
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if(!StringUtils.isEmpty(typeNo)){
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if(!StringUtils.isEmpty(itemNo)){
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if(!StringUtils.isEmpty(flag)){
			dc = dc.add(Restrictions.eq("flag", flag));
		}
		List<SystemConfig> list=getHibernateTemplate.findByCriteria(dc);
		if(null!=list&&list.size()>0){
			str=list.get(0).getValue();
		}
		return str;
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

	// 代理帐号
	public String addAgent(String acode, String loginname, String name, String phone, String email, String qq, String referWebsite, String ipaddress) throws GenericDfhRuntimeException {
		String msg = null;
		loginname = StringUtil.lowerCase(loginname);
		log.info("begin to add a " + UserRole.AGENT.getText());
		Users agent = (Users) get(Users.class, loginname);

		if (agent != null) {
			msg = "用户 " + loginname + " 已存在";
			log.warn(msg);
		} else if (getAgentCountByAcode(acode) > 0) {
			msg = "代理ID：" + acode + " 被注册过";
			log.warn(msg);
		} else if (getAgentByWebSiteNew(referWebsite) != null) {
			msg = "网址已被注册";
			log.warn(msg);
		} else {
			String md5Str = StringUtil.getRandomString(8);
			agent = new Users();
			agent.setAgcode(acode);
			agent.setRole(UserRole.AGENT.getCode());
			agent.setLoginname(loginname);
			agent.setAccountName(name);
			agent.setAliasName(name);
			agent.setPhone(phone);
			agent.setEmail(email);
			agent.setMd5str(md5Str);
			agent.setReferWebsite(referWebsite);
			agent.setPwdinfo("none");
			agent.setQq(qq);
			agent.setCredit(Constants.CREDIT_ZERO);
			agent.setCreatetime(DateUtil.now());
			agent.setRegisterIp(ipaddress);

			save(agent);
			
			AgentAddress agentAddress = new AgentAddress();
			agentAddress.setLoginname(loginname);
			agentAddress.setAddress(referWebsite);
			agentAddress.setDeleteflag(0);
			agentAddress.setCreatetime(new Date());
			save(agentAddress);
			Userstatus status = new Userstatus() ;
			status.setLoginname(loginname);
			status.setTouzhuflag(0);
			status.setCashinwrong(0);
			status.setSlotaccount(0.0);
			save(status);
			// emailSender.sendMailNotify(loginname, pwd, role.getText(),
			// email);
		}
		return msg;
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
	
	public Users getAgentById(Integer id) {
			DetachedCriteria agentCriteria = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.AGENT.getCode()));
			agentCriteria = agentCriteria.add(Restrictions.eq("id", id));
			List list = getHibernateTemplate().findByCriteria(agentCriteria);
			if (list != null && list.size() == 1){
				return (Users) list.get(0);
			}else{
				log.error("agentcode:"+id+" 共有"+list.size()+"个代理");
				return null ;
			}
	}
	
	public Users getAgentByWebSiteNew(String address) {
		if(address.contains("www.e68mobile.com")){
			log.error("主域名："+address);
			return null ;
		}
		if(address.startsWith("https://")){
			address = address.replace("https://", "http://");
		}
		DetachedCriteria agentaddressDC = DetachedCriteria.forClass(AgentAddress.class);
		agentaddressDC = agentaddressDC.add(Restrictions.eq("address", address)).add(Restrictions.eq("deleteflag", 0)); //未被逻辑删除的代理地址
		List list = getHibernateTemplate().findByCriteria(agentaddressDC);
		if (list != null && list.size() == 1){
			AgentAddress agentAddress = (AgentAddress) list.get(0);
			if(null != agentAddress){
				DetachedCriteria agentCriteria = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("loginname", agentAddress.getLoginname()))
						.add(Restrictions.eq("role", UserRole.AGENT.getCode()));
				List users = getHibernateTemplate().findByCriteria(agentCriteria);
				if (users != null && users.size() > 0){
					return (Users) users.get(0);
				}else{
					return null ;
				}
			}else{
				return null ;
			}
		}else{
			log.error("address:"+address+" 没有代理");
			return null ;
		}
}

	public Users getAgentByWebsiteLogin(String agent) {
		if (StringUtils.isNotEmpty(agent)) {
			DetachedCriteria agentCriteria = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.AGENT.getCode()));
			agentCriteria = agentCriteria.add(Restrictions.eq("loginname", agent)).add(Restrictions.eq("flag", Constants.ENABLE));
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
			// if (userPartner == null ||
			// !userPartner.getRole().equals(UserRole.PARTNER.getCode())) {
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
				// } else if
				// (!customer.getRole().equals(UserRole.FREE_CUSTOMER.getCode())
				// &&
				// !customer.getRole().equals(UserRole.MONEY_CUSTOMER.getCode()))
				// {
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
				// } else if
				// (!customer.getRole().equals(UserRole.FREE_CUSTOMER.getCode())
				// &&
				// !customer.getRole().equals(UserRole.MONEY_CUSTOMER.getCode()))
				// {
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
			// } else if
			// (customer.getRole().equals(UserRole.FREE_CUSTOMER.getCode())) {
			// msg = "试玩会员不能参与提案申请";
			// log.warn(msg);
		}
		return msg;
	}

	public Date getLastXimaEndTime(String loginname) {
		final String userName = loginname;
		Date date = (Date) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("select max(x.endTime) from proposal p,xima x where p.pno=x.pno and p.flag=" + ProposalFlagType.EXCUTED.getCode() + " and p.loginname='" + userName + "'");
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
				SQLQuery sqlQuery = session.createSQLQuery("select min(this_.createTime) as y0_ from proposal this_ where this_.loginname='" + userName + "' and this_.flag=" + ProposalFlagType.EXCUTED.getCode() + " and this_.type=" + ProposalType.CASHIN.getCode() + "");
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
				SQLQuery sqlQuery = session.createSQLQuery("select min(this_.createTime) as y0_ from payOrder this_ where this_.loginname='" + userName + "' and this_.flag=" + Constants.FLAG_TRUE + "");
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
		DetachedCriteria dcBetRecord = DetachedCriteria.forClass(Betrecords.class).add(Restrictions.sqlRestriction("result!=''")).add(Restrictions.sqlRestriction("result!=0")).add(Restrictions.sqlRestriction("result is not null"));
		if (StringUtils.isNotEmpty(loginname))
			dcBetRecord = dcBetRecord.add(Restrictions.eq("passport", loginname));
		if (StringUtils.isNotEmpty(userRole)) {
			if (userRole.equals(UserRole.MONEY_CUSTOMER.getCode()))
				dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" + Constants.PREFIX_MONEY_CUSTOMER + "%"));
			// if (userRole.equals(UserRole.FREE_CUSTOMER.getCode()))
			// dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" +
			// Constants.PREFIX_FREE_CUSTOMER + "%"));
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
		// Double rate = 0.0;
		// if (validaBetAmount <= 100 * 10000) {
		// rate = 0.008;
		// // } else if (validaBetAmount > 50 * 10000 && validaBetAmount <= 100
		// * 10000) {
		// // rate = 0.004;
		// } else if (validaBetAmount > 100 * 10000 && validaBetAmount <= 300 *
		// 10000) {
		// rate = 0.008;
		// } else if (validaBetAmount > 300 * 10000 && validaBetAmount <= 500 *
		// 10000) {
		// rate = 0.008;
		// } else if (validaBetAmount > 500 * 10000) {
		// rate = 0.008;
		// }
		// return rate;
		Double rate = 0.008;
		return rate;
	}

	/**
	 * @author sun
	 * @return success null
	 */
	public String checkCommissionrecords(String loginname, Integer year, Integer month) {
		String msg = null;
		DetachedCriteria dc = DetachedCriteria.forClass(Commissionrecords.class).add(Restrictions.eq("id", new CommissionrecordsId(loginname, year, month)));
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
				DetachedCriteria dcProposal = DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode())).add(Restrictions.eq("type", ProposalType.CASHIN.getCode())).add(Restrictions.eq("createTime", firstlyPTime));
				Proposal proposal = (Proposal) getHibernateTemplate().findByCriteria(dcProposal).get(0);
				System.out.println(proposal.toString());

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
				DetachedCriteria dcPayorder = DetachedCriteria.forClass(Payorder.class).add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", Constants.FLAG_TRUE)).add(Restrictions.eq("createTime", firstlyPayOrderTime));
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
			logDao.insertOperationLog(operator, OperationLogType.SETLEVEL, "将会员" + loginname + "的等级从[" + VipLevel.getText(srcLevel) + "]改为[" + VipLevel.getText(level) + "]");
			msg = null;
		}
		return msg;
	}

	public Proposal getConcessions(String loginname) {
		if (StringUtils.isEmpty(loginname))
			return null;
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("type", ProposalType.CONCESSIONS.getCode()));
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
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode())).add(Restrictions.eq("agent", agent));
			list = findByCriteria(dc);
		}
		return list;
	}

	/**
	 * 同手机号 和 同邮箱 不 可以重复注册
	 * 
	 * @param accountname
	 * @param ip
	 * @param email
	 * @return
	 */
	public boolean checkExsitForCreateUserEmail(String accountname, String ip, String email, String phone) {
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode()));
		Junction disjuction = Restrictions.disjunction();
		if (StringUtils.isNotEmpty(email))
			disjuction = disjuction.add(Restrictions.eq("email", email));
		if (StringUtils.isNotEmpty(phone))
			disjuction = disjuction.add(Restrictions.eq("phone", phone));
		dc = dc.add(disjuction);
		int size = findByCriteria(dc).size();
		log.info("checkExsitForCreateUser size:" + size);
		return size > 0;
	}
	
	/**
	 * 同姓名 同ip 可以重复注册
	 * 
	 * @param accountname
	 * @param ip
	 * @param email
	 * @return
	 */
	public boolean checkExsitForCreateUserIp(String accountname, String ip, String email, String phone) {
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class).add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode()));
		Junction disjuction = Restrictions.disjunction();
		if (StringUtils.isNotEmpty(accountname))
			disjuction = disjuction.add(Restrictions.eq("accountName", accountname));
		if (StringUtils.isNotEmpty(ip))
			disjuction = disjuction.add(Restrictions.or(Restrictions.eq("registerIp", ip),Restrictions.eq("lastLoginIp", ip)));
		dc = dc.add(disjuction);
		int size = findByCriteria(dc).size();
		log.info("checkExsitForCreateUser size:" + size);
		return size > 0;
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	// 把原来的更新为已审核状态
	public Boolean updateSbcoupon(String loginname) {
		String hql = "Update Sbcoupon set status='1' where status='0' and loginname='" + loginname + "'";
		Query query = this.getSession().createQuery(hql);
		query.executeUpdate();
		return true;
	}

	// 判断是否已经通过
	public Sbcoupon getSbcoupon(String loginname) {
		Criteria criteria = getSession().createCriteria(Sbcoupon.class);
		criteria.add(Restrictions.eq("loginname", loginname));
		criteria.add(Restrictions.eq("status", 0));
		List<Sbcoupon> list = criteria.list();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			return list.get(0);
		}
		return null;
	}

	public Double getTurnOverRequest(String loginname, Date start, Date end) {
		Criteria criteria = getSession().createCriteria(Sbbets.class);
		criteria.add(Restrictions.eq("loginname", loginname));
		criteria.add(Restrictions.ge("createtime", start));
		criteria.add(Restrictions.lt("createtime", end));
		criteria.setProjection(Projections.sum("sbbets"));
		List users = criteria.list();
		if(users!=null && users.size()>0 && users.get(0)!=null){
			Iterator iterator =  users.iterator();
			while(iterator.hasNext()) {
			    return (Double)iterator.next();      
			}
		}
		return 0.00;
	}
	
	// 代理帐号用小写
		public String addAgent(String howToKnow,String loginname, String pwd, String accountName, String phone, String email, String qq,String referWebsite, String ipaddress,String city,String microchannel,String partner) throws GenericDfhRuntimeException {
			if(!"Z".equalsIgnoreCase(partner)){
				if(!this.checkPartener(partner)){
					partner = "A";
				}
			}
		String msg = null;
			loginname=StringUtil.lowerCase(loginname);
			log.info("begin to add a " + UserRole.AGENT.getText());
			
			referWebsite = StringUtil.lowerCase(referWebsite);
			String tmpM = referWebsite;
			if(referWebsite.startsWith("www.")){
				tmpM = referWebsite.replace("www.", "");
			}
			referWebsite = "http://"+tmpM+".longdu95.com";
			String tmpW = "http://www."+tmpM+".longdu95.com";
			String bak1 = "http://"+tmpM+".longdu96.com";
			String bak2 = "http://www."+tmpM+".longdu96.com";
			String bak3 = "http://"+tmpM+".longdu97.com";
			String bak4 = "http://www."+tmpM+".longdu97.com";
			Users agent = (Users) get(Users.class, loginname);
			
			if (agent != null) {
				msg = "用户 " + loginname + " 已存在";
				log.warn(msg);
			}  else if (getAgentByWebSiteNew(referWebsite)!=null) {
				msg = "代理网址已被注册";
				log.warn(msg);
			} else {
				String remark="";
				int flag=0;
				Integer warnFlag = 0 ;
//				if (checkExsitForCreateUserIp(accountName, ipaddress, email,phone)){
//					flag=0; 
//					remark="有同IP或者同姓名注册";
//					warnFlag = 2 ;
//				}
//				
//				if (checkExsitForCreateUserEmail(accountName, ipaddress, email,phone)){
//					flag=0;   //1禁用0不禁用
//					remark="有同手机号或者同邮箱注册";
//					warnFlag = 2 ;
//				}
				String md5Str = StringUtil.getRandomString(8);
				agent = new Users();
				agent.setWarnflag(warnFlag);
				agent.setPassword(EncryptionUtil.encryptPassword(pwd));
				agent.setRole(UserRole.AGENT.getCode());
				agent.setLoginname(loginname);
				agent.setAccountName(accountName);
				agent.setAliasName(accountName);
				agent.setPhone(phone);
				agent.setEmail(email);
				agent.setMd5str(md5Str);
				agent.setReferWebsite(referWebsite);
				agent.setPwdinfo("none");
				agent.setQq(qq);
				agent.setArea(city);
				agent.setPasswdflag(1);
				agent.setCredit(Constants.CREDIT_ZERO);
				agent.setCreatetime(DateUtil.now());
				agent.setRegisterIp(ipaddress);
				agent.setMicrochannel(microchannel);
				
				agent.setPartner(partner);
				agent.setFlag(1); //应代理需求，新注册代理为冻结状态
				
				Integer userid = Integer.parseInt(generateUserID());
				agent.setId(userid);
				//生成代理acode
				agent.setAgcode(StringUtil.formatNumberToDigits(userid, 16));
				
				save(agent);
				saveAgentAddress(loginname,referWebsite);
				saveAgentAddress(loginname,tmpW);
				saveAgentAddress(loginname,bak1);
				saveAgentAddress(loginname,bak2);
				saveAgentAddress(loginname,bak3);
				saveAgentAddress(loginname,bak4);
				/*AgentAddress agentAddress = new AgentAddress();
				agentAddress.setLoginname(loginname);
				agentAddress.setAddress(referWebsite);
				agentAddress.setDeleteflag(0);
				agentAddress.setCreatetime(new Date());
				save(agentAddress);
				AgentAddress agentAddressW = new AgentAddress();
				agentAddressW.setLoginname(loginname);
				agentAddressW.setAddress(tmpW);
				agentAddressW.setDeleteflag(0);
				agentAddressW.setCreatetime(new Date());
				save(agentAddressW);*/
				
				Userstatus status = new Userstatus() ;
				status.setLoginname(loginname);
				status.setTouzhuflag(0);
				status.setCashinwrong(0);
				status.setSlotaccount(0.0);
				save(status);
				// emailSender.sendMailNotify(loginname, pwd, role.getText(), email);
			}
			return msg;
		}
		
		public void saveAgentAddress(String loginname,String referWebsite) {
			AgentAddress agentAddress = new AgentAddress();
			agentAddress.setLoginname(loginname);
			agentAddress.setAddress(referWebsite);
			agentAddress.setDeleteflag(0);
			agentAddress.setCreatetime(new Date());
			save(agentAddress);
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
		
		public Double getDoubleValue(String sql, Map params) {
			Query query = this.getSession().createSQLQuery(sql);
			query.setProperties(params);
			Object obj = query.uniqueResult();
			return null==obj?0.00:Double.parseDouble(obj.toString());
		}
		
		/**
		 * 查出当天玩家提交的待处理以及处理失败的催单条数
		 * @param loginname
		 * @return
		 */
		public List<UrgeOrder> queryUrgeOrderList(String loginname) {
			StringBuffer sbf = new StringBuffer();
			sbf.append("SELECT ");
			sbf.append("	* ");
			sbf.append("FROM ");
			sbf.append("	urge_order t ");
			sbf.append("WHERE ");
			sbf.append("	t.loginname = ? ");
			sbf.append("AND DATE(t.createtime) = CURDATE() ");
			sbf.append("AND t.status in (0,2) ");
			String sql = sbf.toString();
			List<UrgeOrder> list = this.getSession().createSQLQuery(sql).addEntity(UrgeOrder.class).setParameter(0, loginname).list();
			return list;
		}

		/**
		 * 
		 * @param tempDepositTime 存款时间
		 * @param thirdOrder	支付宝订单号
		 * @return 支付宝转账记录处理状态
		 */
		public Byte checkthirdOrder(String tempDepositTime,String thirdOrder) {
			StringBuffer sbf = new StringBuffer();
			sbf.append("SELECT ");
			sbf.append("	status ");
			sbf.append("FROM ");
			sbf.append("	alipay_transfers t ");
			sbf.append("WHERE ");
			sbf.append("	t.transfer_id = ? ");
			sbf.append("AND date(pay_date) = ?; ");

			String sql = sbf.toString();
			Byte status =(Byte) this.getSession().createSQLQuery(sql).setParameter(0, thirdOrder).setParameter(1, tempDepositTime).uniqueResult();
			return status;
		}

	public UserInfo getUserInfo(String username) {
		UserInfo userInfo = null;
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("	a.role, ");
		sbf.append("	a.level, ");
		sbf.append("	a.intro, ");
		sbf.append("	a.partner, ");
		sbf.append("	a.agent, ");
		sbf.append("	b.belongto ");
		sbf.append("FROM ");
		sbf.append("	users a ");
		sbf.append("LEFT JOIN internal_agency b ON a.agent = b.loginname ");
		sbf.append("WHERE ");
		sbf.append("	a.loginname = ? ");
		String sql = sbf.toString();
		List list =this.getSession().createSQLQuery(sql).setParameter(0, username).list();
		if(list != null && list.size()>0){
			userInfo = new UserInfo();
			Object[] obj = (Object[]) list.get(0);
			String role = (String)obj[0];
			if(StringUtil.isNotBlank(role)){
				if(role.equalsIgnoreCase("MONEY_CUSTOMER")){
					userInfo.setRole("1");
				}else if(role.equalsIgnoreCase("AGENT")){
					userInfo.setRole("2");
				}else{
					userInfo.setRole("");
				}
			}else{
				userInfo.setRole("");
			}
			userInfo.setLevel(obj[1]==null?"":(obj[1]+""));
			userInfo.setIntro(obj[2]==null?"":(obj[2]+""));
			userInfo.setPartner(obj[3]==null?"":(obj[3]+""));
			userInfo.setAgent(obj[4]==null?"":(obj[4]+""));
			userInfo.setBelongto(obj[5]==null?"":(obj[5]+""));
		}
		return userInfo;
	}
	
}
