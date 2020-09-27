package dfh.service.implementations;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import dfh.dao.LogDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.model.Autotask;
import dfh.model.Bankinfo;
import dfh.model.Betrecords;
import dfh.model.Commissionrecords;
import dfh.model.CommissionrecordsId;
import dfh.model.Creditlogs;
import dfh.model.Operator;
import dfh.model.Prize;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.model.Xima;
import dfh.model.enums.AutoGenerateType;
import dfh.model.enums.AutoTaskFlagType;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.model.notdb.Report;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.OperatorService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.NumericUtil;
import dfh.utils.StringUtil;

public class OperatorServiceImpl extends UniversalServiceImpl implements OperatorService {

	private static Logger log = Logger.getLogger(OperatorServiceImpl.class);

	private LogDao logDao;
	private UserDao userDao;
	private SeqDao seqDao;
	private TradeDao tradeDao;
	private TaskDao taskDao;

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public OperatorServiceImpl() {
	}

	public static void main(String[] args) {
		System.out.println(dfh.utils.StringUtil.P_URL.matcher("http://阿扁大.d").matches());
	}

	public String EnableUser(String userName, boolean isEnable, String operator) {
		String msg = null;
		Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
		if (user == null) {
			msg = "未找到该用户";
		} else {
			user.setFlag(isEnable ? Constants.ENABLE : Constants.DISABLE);
			update(user);
			logDao.insertOperationLog(operator, OperationLogType.ENABLE, (isEnable ? "启用" : "禁用") + "会员:" + userName);
			msg = null;
		}
		return msg;
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public SeqDao getSeqDao() {
		return seqDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public String login(String loginname, String password, String ip) {
		String msg = null;
		Operator op = (Operator) get(Operator.class, loginname);
		if (op == null) {
			msg = "帐号不存在";
		} else if (op.getEnabled().equals(Constants.FLAG_FALSE)) {
			msg = "帐号已被禁用";
		} else if (!op.getPassword().equals(EncryptionUtil.encryptPassword(password))) {
			msg = "密码不正确";
		} else {
			op.setLoginTimes(Integer.valueOf(op.getLoginTimes().intValue() + 1));
			op.setLastLoginTime(DateUtil.now());
			op.setLastLoginIp(ip);
			logDao.insertOperationLog(loginname, OperationLogType.LOGIN, ip);
			update(op);
		}
		return msg;
	}

	public String addAgent(String acode, String loginname, String name, String phone, String email, String qq, String referWebsite, String ip,
			String operator) {
		logDao.insertOperationLog(operator, OperationLogType.ADDAGENT, ip);
		return userDao.addAgent(acode, loginname, name, phone, email, qq, referWebsite, ip);
	}

	public String resetPassword(String loginname, String password, String operator) {
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null) {
			msg = "帐号不存在";
		} else {
			String oldPassword = user.getPassword();
			user.setPassword(EncryptionUtil.encryptPassword(password));
			user.setPwdinfo(userDao.encrypt(password));
			update(user);
			logDao.insertOperationLog(operator, OperationLogType.RESET_PWD, loginname + "的原密码:" + oldPassword);
			msg = null;
		}
		return msg;
	}

	public String setLevel(String loginname, Integer level, String operator) {
		return userDao.setLevel(loginname, level, operator);
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String generateCommissionForEach(String loginname, Date endTime, String operator, List<String> userNameList) {
		return null;
	}

	public String generateXimaForEach(String loginname, Date startTime, Date endTime, String operator) {
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname);
		if (user == null) {
			msg = "用户不存在";
		} else if (user.getFlag().intValue() == Constants.DISABLE.intValue()) {
			msg = "用户被禁用";
		} else {
			// 获取上次洗码截止时间
			Date finalTime = userDao.getLastXimaEndTime(loginname);
			Proposal proposalDate = userDao.getConcessions(loginname);// 用户首存优惠
			if (finalTime != null && (startTime.before(finalTime) || startTime.equals(finalTime))) {
				msg = "该用户在" + DateUtil.formatDateForStandard(finalTime) + "内的洗码已经结算过";
			} else if (proposalDate != null && (startTime.before(proposalDate.getCreateTime()) || startTime.equals(proposalDate.getCreateTime()))
					&& (endTime.after(proposalDate.getCreateTime()) || endTime.equals(proposalDate.getCreateTime()))) {
				return "该用户本周享受过开户优惠";
			} else {
				Double validBetAmount = userDao.getValidBetAmount(loginname, startTime, endTime, null);
				if (validBetAmount.intValue() == 0) {
					msg = "用户在该段时间有效投注额为0";
				}
				Double rate = 0.0;
				// Double rate = userDao.getXimaRate(user, validBetAmount);
				if (rate.doubleValue() <= 0) {
					msg = "洗码率为0";
				} else {
					log.info("会员的有效投注额:" + validBetAmount + ";洗码率:" + rate);
					String pno = seqDao.generateProposalPno(ProposalType.XIMA);
					Double tryCredit = Double.valueOf(Math.abs(validBetAmount) * Math.abs(rate));
					String remark = "自动结算洗码";
					Xima xima = new Xima(pno, user.getRole(), loginname, "在线支付", validBetAmount, tryCredit, DateUtil
							.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), rate, remark);
					Proposal proposal = new Proposal(pno, operator, DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname,
							tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
					proposal.setFlag(ProposalFlagType.AUDITED.getCode());
					taskDao.generateTasks(pno, operator);
					save(xima);
					save(proposal);
					msg = null;
				}

			}
		}
		return msg;
	}

	public Autotask startAutoTask(AutoGenerateType type, Integer totalRecords, String operator) {
		Autotask generateTask = new Autotask();
		generateTask.setFlag(AutoTaskFlagType.INIT.getCode());
		generateTask.setTaskType(type.getCode());
		generateTask.setTotalRecords(totalRecords);
		generateTask.setStartTime(DateUtil.now());
		generateTask.setOperator(operator);
		generateTask.setFinishRecords(0);
		generateTask.setFailRecords(0);
		save(generateTask);
		return generateTask;
	}

	public Autotask getLastAutoTask(AutoGenerateType type) {
		List<Autotask> list = findByCriteria(DetachedCriteria.forClass(Autotask.class).add(Restrictions.eq("taskType", type.getCode())).addOrder(
				Order.desc("startTime")), 0, 1);
		if (list == null || list.size() == 0)
			return null;
		else {
			return list.get(0);
		}
	}

	public Autotask refreshAutoTask(Integer taskID, Boolean isSuccess, String remark) {
		Autotask task = (Autotask) get(Autotask.class, taskID, LockMode.UPGRADE);
		if (task == null) {
			return null;
		}
		if (task.getFlag().intValue() == AutoTaskFlagType.STOPPED.getCode()) {
			return task;
		}
		task.setRefreshTime(DateUtil.now());
		task.setFlag(AutoTaskFlagType.PROCEED.getCode());
		if (StringUtils.isNotEmpty(remark))
			task.setRemark(remark);
		if (isSuccess)
			task.setFinishRecords(task.getFinishRecords() + 1);
		else
			task.setFailRecords(task.getFailRecords() + 1);
		// 如果总记录数已达到，标记为已生成
		if (task.getTotalRecords().intValue() == (task.getFailRecords() + task.getFinishRecords()))
			task.setFlag(AutoTaskFlagType.GENERATED.getCode());
		update(task);
		return task;
	}

	public Autotask stopAutoTask(Integer taskID, String remark) {
		Autotask task = (Autotask) get(Autotask.class, taskID, LockMode.UPGRADE);
		if (task == null) {
			return null;
		}
		task.setFlag(AutoTaskFlagType.STOPPED.getCode());
		task.setEndTime(DateUtil.now());
		task.setRemark(remark);
		update(task);
		return task;
	}

	public Autotask finishAutoTask(Integer taskID, String remark) {
		Autotask task = (Autotask) get(Autotask.class, taskID, LockMode.UPGRADE);
		if (task == null) {
			return null;
		}
		task.setFlag(AutoTaskFlagType.EXCUTED.getCode());
		task.setEndTime(DateUtil.now());
		task.setRemark(remark);
		update(task);
		return task;
	}

	public String changeCreditManual(String loginname, Double amount, String remark, String operator) {
		String msg = null;
		if (StringUtils.isNotEmpty(loginname) && amount != null && StringUtils.isNotEmpty(operator)) {
			Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			tradeDao.changeCredit(user, amount, CreditChangeType.CHANGE_MANUAL.getCode(), null, remark);
			logDao.insertOperationLog(loginname, OperationLogType.CHANGE_CREDIT_MANUAL, "为用户" + loginname + "增加/扣除"
					+ NumericUtil.formatDouble(amount));
		}
		return msg;
	}

	public String createSubOperator(String newOperator, String password, String operator) {
		String msg = null;
		String power = "manager";
		Operator currentOperator = (Operator) get(Operator.class, operator);
		if (currentOperator == null) {
			msg = "帐号不存在";
		} else if (!currentOperator.getAuthority().toLowerCase().contains(power)) {
			msg = "用户" + operator + "没有开设子帐号的权限";
		} else if (get(Operator.class, newOperator) != null) {
			msg = "帐号已存在";
		} else {
			String authrities = currentOperator.getAuthority().substring(0, currentOperator.getAuthority().indexOf(power) - 1);
			Operator newOp = new Operator(newOperator, EncryptionUtil.encryptPassword(password), Constants.ENABLE, authrities, 0, DateUtil.now());
			save(newOp);
			logDao.insertOperationLog(operator, OperationLogType.CREATE_SUB_OP, "创建了下级管理员:" + newOperator);
			msg = null;
		}
		return msg;
	}

	public String modifyOwnPassword(String operator, String oldPassword, String newPassword) {
		String msg = null;
		Operator currentOperator = (Operator) get(Operator.class, operator, LockMode.UPGRADE);
		if (currentOperator == null) {
			msg = "帐号不存在";
		} else if (currentOperator.getEnabled().intValue() == Constants.DISABLE.intValue()) {
			msg = "账户已被禁用";
		} else if (!currentOperator.getPassword().equals(EncryptionUtil.encryptPassword(oldPassword))) {
			msg = "旧密码不正确";
		} else if (oldPassword.equals(newPassword)) {
			msg = "新密码与旧密码相同";
		} else {
			currentOperator.setPassword(EncryptionUtil.encryptPassword(newPassword));
			update(currentOperator);
			logDao.insertOperationLog(operator, OperationLogType.MODIFY_OWN_PWD, null);
			msg = null;
		}
		return msg;
	}

	public String partnerSetlower(String loginname, String partner) {
		String msg = null;
		// Users user = (Users) get(Users.class, loginname);
		// Users pUser = (Users) get(Users.class, partner);
		// if (user == null) {
		// msg = "返回消息:未找到" + loginname + "会员账号";
		// } else if (StringUtils.isNotEmpty(user.getPartner())) {
		// msg = loginname + "账号已经有对应的合作伙伴";
		//
		// } else if (user.getFlag() == Constants.DISABLE) {
		// msg = loginname + "账号已经被禁用";
		// } else if (pUser == null) {
		// msg = "未找到" + partner + "合作伙伴账号";
		// // } else if (!pUser.getRole().equals(UserRole.PARTNER.getCode())) {
		// // msg = partner + "不是合作伙伴账号";
		// } else if (pUser.getFlag().intValue() == Constants.DISABLE.intValue()) {
		// msg = "合作伙伴" + partner + "已经被禁用";
		// } else {
		// logDao.insertOperationLog(loginname, OperationLogType.SET_PARTNERLOWER, "会员" + loginname + "合作伙伴" + partner);
		// user.setPartner(partner);
		// update(user);
		// }
		return msg;
	}

	public String modifyCustomerInfo(String loginname, String aliasName, String phone, String email, String qq, String remark, String operator) {
		String msg = null;
		Users user = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (user == null) {
			msg = "帐号不存在";
		} else {
			String oldAliasName = user.getAliasName();
			String oldPhone = user.getPhone();
			String oldEmail = user.getEmail();
			String oldQq = user.getQq();

			user.setAliasName(aliasName);
			user.setPhone(phone);
			user.setEmail(email);
			user.setQq(qq);
			user.setRemark(remark);
			update(user);

			String logRemark = "";
			if (!StringUtils.equals(oldAliasName, aliasName))
				logRemark += "原昵称[" + oldAliasName + "]改为[" + aliasName + "];";
			if (!StringUtils.equals(oldPhone, phone))
				logRemark += "原电话[" + oldPhone + "]改为[" + phone + "];";
			if (!StringUtils.equals(oldEmail, email))
				logRemark += "原邮箱[" + oldEmail + "]改为[" + email + "];";
			if (!StringUtils.equals(oldQq, qq))
				logRemark += "原QQ[" + oldQq + "]改为[" + qq + "];";

			logDao.insertOperationLog(loginname, OperationLogType.MODIFY_CUS_INFO, logRemark);
		}
		return msg;
	}

	// sun
	public double getValidBetAmount(String loginname, Date start, Date end) {
		return userDao.getValidBetAmount(loginname, start, end, null);
	}

	// sun
	public String makePartnerBonus(String loginname, Integer year, Integer month, Double validBetAmount, Double cashinoutAmount, String operator,
			String ip, String remark) {
		String msg = null;
		/*
		 * Users user = (Users) this.get(Users.class, loginname); if (StringUtils.isEmpty(user.getPartner())) msg = "未找到对应的合作伙伴账号"; else if
		 * (userDao.checkCommissionrecords(loginname, year, month) != null) msg = "此用户本月已结算过佣金"; else { Commissionrecords commisionrecords = new
		 * Commissionrecords(); commisionrecords.setId(new CommissionrecordsId(loginname, year, month));
		 * 
		 * commisionrecords.setCashinoutAmount(cashinoutAmount); commisionrecords.setFlag(Constants.FLAG_FALSE);
		 * commisionrecords.setRemark(remark); commisionrecords.setSubLoginname(user.getPartner());
		 * commisionrecords.setValidBetAmount(validBetAmount); commisionrecords.setCreateDate(DateUtil.now());
		 * 
		 * Double sumProfit = (validBetAmount * Constants.PROFITRATE) - (cashinoutAmount * Constants.OUTLAYRATE);// 拥金计算结果 公式(有效投注额*0.002 减去
		 * 存取在线支付总额*0.01) commisionrecords.setProfitAmount(sumProfit);
		 * 
		 * commisionrecords.setOutlayRate(Constants.OUTLAYRATE); commisionrecords.setProfitRate(Constants.PROFITRATE); save(commisionrecords); }
		 */
		return msg;
	}

	// sun
	public String excuteMakePartnerBonus(String subLoginname, Double sumProfitAmount, String operator, String ip, String remark) {
		String msg = null;
		// Users user = (Users) get(Users.class, subLoginname);
		// if (user == null)
		// msg = "不存在此合作伙伴";
		// else if (sumProfitAmount < 500)
		// msg = "结算值小于500累记到下个月";
		// else {
		// final String newSubLoginname = subLoginname;
		// getHibernateTemplate().execute(new HibernateCallback() {
		// public Object doInHibernate(Session session) throws HibernateException, SQLException {
		// session.createSQLQuery("update commissionrecords set flag=0,excuteDate='" + DateUtil.now() + "' where subLoginname='" + newSubLoginname
		// + "'").executeUpdate();
		// return null;
		// }
		// });
		// if (sumProfitAmount > 0) {
		// tradeDao.changeCredit(subLoginname, sumProfitAmount, CreditChangeType.COMMISSION.getCode(), null, remark);
		// }
		// }
		return msg;
	}

	public List excuteSQL(final String sql) {
		List list = null;
		list = (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createSQLQuery(sql).list();
			}
		});
		return list;
	}

	public Report queryReport(Date startTime, Date endTime, String userRole, String loginname) {
		Report report = new Report();
		DetachedCriteria dcBetRecord = DetachedCriteria.forClass(Betrecords.class);

		if (StringUtils.isNotEmpty(userRole)) {
			if (userRole.equals(UserRole.MONEY_CUSTOMER.getCode()))
				dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" + Constants.PREFIX_MONEY_CUSTOMER + "%"));
			// if (userRole.equals(UserRole.FREE_CUSTOMER.getCode()))
			// dcBetRecord = dcBetRecord.add(Restrictions.like("passport", "" + Constants.PREFIX_FREE_CUSTOMER + "%"));
		}
		if (StringUtils.isNotEmpty(loginname)) {
			dcBetRecord = dcBetRecord.add(Restrictions.eq("passport", loginname));
			report.setLoginname(loginname);
		}
		if (startTime != null)
			dcBetRecord = dcBetRecord.add(Restrictions.ge("billTime", startTime));
		if (endTime != null)
			dcBetRecord = dcBetRecord.add(Restrictions.lt("billTime", endTime));

		dcBetRecord.setProjection(Projections.projectionList().add(Projections.sum("billAmount")).add(Projections.sum("result")));
		List resultBetRecord = getHibernateTemplate().findByCriteria(dcBetRecord);
		Object[] arrayBetRecord = (Object[]) resultBetRecord.get(0);

		if (arrayBetRecord[0] != null)
			report.setSumBillAmount((Double) arrayBetRecord[0]);// 总投注额
		if (arrayBetRecord[1] != null)
			report.setSumResult((Double) arrayBetRecord[1]);// 总返利情况
		if (StringUtils.isEmpty(loginname))
			report.setSumAttend(queryAttendGameNumber(startTime, endTime, userRole));// 投注人数

		if (arrayBetRecord[0] != null && arrayBetRecord[1] != null) {
			String winPercent = (NumericUtil.formatDouble((report.getSumResult() / report.getSumBillAmount()) * 100)) + "%";
			if (winPercent.indexOf("-") >= 0)
				winPercent = winPercent.replace("-", "");
			else
				winPercent = "-" + winPercent;
			report.setWinPercent(winPercent);
		}
		report.setSumFactAmount(userDao.getValidBetAmount(loginname, startTime, endTime, userRole));// 有效投注额
		return report;
	}

	public Integer queryAttendGameNumber(Date startTime, Date endTime, String userRole) {
		Integer result = 0;
		DetachedCriteria dc = DetachedCriteria.forClass(Betrecords.class);
		if (StringUtils.isNotEmpty(userRole)) {
			if (userRole.equals(UserRole.MONEY_CUSTOMER.getCode())) {
				dc = dc.add(Restrictions.like("passport", "" + Constants.PREFIX_MONEY_CUSTOMER + "%"));
			}
			// if (userRole.equals(UserRole.FREE_CUSTOMER.getCode())) {
			// dc = dc.add(Restrictions.like("passport", "" + Constants.PREFIX_FREE_CUSTOMER + "%"));
			// }
		}
		if (startTime != null) {
			dc = dc.add(Restrictions.ge("billTime", startTime));
		}
		if (endTime != null) {
			dc = dc.add(Restrictions.lt("billTime", endTime));
		}
		dc.setProjection(Projections.projectionList().add(Projections.countDistinct("passport")));
		List resultCount = getHibernateTemplate().findByCriteria(dc);
		if (resultCount.get(0) != null)
			result = (Integer) resultCount.get(0);// 总投注人数
		return result;
	}

	/*
	 * 获取某段时间内的本地额度变化值,不包含提款
	 */
	public Double getLocalCreditChangeByPeriod(String loginname, Date startTime, Date endTime) {
		DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class);
		dc = dc.add(Restrictions.ne("type", CreditChangeType.TRANSFER_IN.getCode())).add(
				Restrictions.ne("type", CreditChangeType.TRANSFER_OUT.getCode()))
				.add(Restrictions.ne("type", CreditChangeType.CASHOUT.getCode())).add(Restrictions.eq("loginname", loginname)).add(
						Restrictions.gt("createtime", startTime));
		if (endTime != null)
			dc = dc.add(Restrictions.lt("createtime", endTime));
		dc = dc.setProjection(Projections.sum("remit"));
		List<Double> list = findByCriteria(dc);
		return NumericUtil.toDouble(list.size() > 0 ? list.get(0) : null) ;
	}
	
	public List getAllUsers(){
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		dc.add(Restrictions.eq("id",0));
		List<Users> list = findByCriteria(dc);
		System.out.println(dc);
		return list;
	}

	public Double getBankInfoAmountByName(String username){
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.eq("username",username));
		dc.add(Restrictions.eq("type",1));
		dc.add(Restrictions.eq("useable",0));
		List<Bankinfo> list = findByCriteria(dc);
		if(list.size()==1){
			return list.get(0).getAmount();
		}
		return null;
	}
}
