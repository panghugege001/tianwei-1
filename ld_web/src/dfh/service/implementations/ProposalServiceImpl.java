package dfh.service.implementations;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.dao.AgentDao;
import dfh.dao.LogDao;
import dfh.dao.OperatorDao;
import dfh.dao.ProposalDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Accountinfo;
import dfh.model.Banktransfercons;
import dfh.model.Cashin;
import dfh.model.Cashout;
import dfh.model.Concessions;
import dfh.model.Const;
import dfh.model.Newaccount;
import dfh.model.Offer;
import dfh.model.Prize;
import dfh.model.Proposal;
import dfh.model.Rebankinfo;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.Xima;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.model.enums.VipLevel;
import dfh.remote.RemoteCaller;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.ProposalService;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class ProposalServiceImpl extends UniversalServiceImpl implements ProposalService {

	private static Logger log = Logger.getLogger(ProposalServiceImpl.class);
	private LogDao logDao;
	private OperatorDao operatorDao;
	private ProposalDao proposalDao;
	private SeqDao seqDao;
	private TaskDao taskDao;
	private TradeDao tradeDao;
	private UserDao userDao;
	private AgentDao agentDao;
	private String msg;
	
	public ProposalServiceImpl() {
	}

	public String addCashin(String proposer, String loginname, String aliasName, String title, String from, Double money,
			String corpBankName, String remark, String accountNo,String bankaccount,String saveway,String cashintime) throws GenericDfhRuntimeException {
		log.info("add Cashin proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
//		if (!user.getRole().equals(title)) {
//			msg = "未找到该类型的帐号,可能用户类型选择错误";
//			return msg;
//		}
		// 提款必须大于100元
		if (money < 100.0) {
			msg = "100元以上才能存款";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.CASHIN)) {
			msg = "该用户已提交过存款提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.CASHIN);
				Cashin cashin = new Cashin(pno, user.getRole(), user.getLoginname(), aliasName, money, accountNo, corpBankName, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CASHIN.getCode(), user.getLevel(), loginname, money,
						user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				proposal.setBankaccount(bankaccount);
				proposal.setSaveway(saveway);
				SimpleDateFormat yyyy_MM_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				cashin.setCashintime(yyyy_MM_d.parse(cashintime)); 
				save(cashin);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}


	public String addConcession(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit, String payType,
			String remark) throws GenericDfhRuntimeException {
		log.info("add Cashout proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotCancledProposal(loginname, ProposalType.CONCESSIONS)) {
			msg = "已经提交或曾经申请过开户优惠提案";
			return msg;
		}

		if (msg == null) {
			String pno = seqDao.generateProposalPno(ProposalType.CONCESSIONS);
			try {
				Concessions concessions = new Concessions(pno, user.getRole(), loginname, payType, firstCash, tryCredit, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CONCESSIONS.getCode(), user.getLevel(), loginname,
						tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(concessions);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}

	/**
	 * 转账优惠
	 */
	public String addBankTransferCons(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit,
			String payType, String remark) throws GenericDfhRuntimeException {
		log.info("add BankTransferCons proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.BANKTRANSFERCONS)) {
			msg = "该用户已提交过转账优惠提案，尚未审批完";
			return msg;
		}
		if (msg == null) {
			String pno = seqDao.generateProposalPno(ProposalType.BANKTRANSFERCONS);
			try {
				Banktransfercons cons = new Banktransfercons(pno, user.getRole(), loginname, payType, firstCash, tryCredit, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.BANKTRANSFERCONS.getCode(), user.getLevel(),
						loginname, tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(cons);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}

	public String addNewAccount(String proposer, String loginname, String pwd, String title, String from, String aliasName, String phone,
			String email, String role, String remark) {
		log.info("add NewAccount proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		if (user != null) {
			msg = "该用户已存在";
			return msg;
		}
		if (UserRole.MONEY_CUSTOMER.getCode().equals(title)
				&& !(loginname.startsWith(Constants.PREFIX_MONEY_CUSTOMER) || loginname.startsWith(Constants.PREFIX_PARTNER_SUBMEMBER)))
			msg = UserRole.MONEY_CUSTOMER.getText() + "应以" + Constants.PREFIX_MONEY_CUSTOMER + "或" + Constants.PREFIX_PARTNER_SUBMEMBER + "开头";
		else if (UserRole.AGENT.getCode().equals(title) && !loginname.startsWith(Constants.PREFIX_AGENT))
			msg = UserRole.AGENT.getText() + "应以" + Constants.PREFIX_AGENT + "开头";
		// else if (UserRole.PARTNER.getCode().equals(title) && !loginname.startsWith(Constants.PREFIX_PARTNER))
		// msg = UserRole.PARTNER.getText() + "应以" + Constants.PREFIX_PARTNER + "开头";
		if (msg != null)
			return msg;
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.NEWACCOUNT)) {
			msg = "已提交过该帐号的开户提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.NEWACCOUNT);
				Newaccount newAccount = new Newaccount(pno, role, loginname, pwd, phone, email, aliasName, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.NEWACCOUNT.getCode(), VipLevel.COMMON.getCode(),
						loginname, null, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(newAccount);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	public String addReBankInfo(String proposer, String loginname, String title, String from, String accountName, String accountNo,
			String accountType, String bank, String accountCity, String bankAddress, String ip, String remark) throws GenericDfhRuntimeException {
		log.info("add reBankInfo proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.REBANKINFO)) {
			msg = "该用户已提交过银改提案，尚未处理完";
			return msg;
		}
		if (get(Accountinfo.class, loginname) == null) {
			msg = "客户尚未完成银行资料";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.REBANKINFO);
				Rebankinfo rebankinfo = new Rebankinfo(pno, user.getRole(), loginname, accountName, accountNo, bank, accountType, accountCity,
						bankAddress, ip, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.REBANKINFO.getCode(), user.getLevel(), loginname,
						null, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(rebankinfo);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	public String addXima(String proposer, String loginname, String title, String from, Date startTime, Date endTime, Double firstCash,
			Double rate, String payType, String remark) throws GenericDfhRuntimeException {
		log.info("add Xima proposal");
		String msg = null;

		if (startTime != null && endTime.before(startTime)) {
			msg = "结算结束时间必须大于结算开始时间";
			return msg;
		}

		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.XIMA)) {
			msg = "已提交过洗码优惠提案，尚未处理完";
			return msg;
		}
		if (msg == null) {
			String pno = seqDao.generateProposalPno(ProposalType.XIMA);
			try {
				Double tryCredit = Double.valueOf(Math.abs(firstCash.doubleValue()) * Math.abs(rate.doubleValue()));
				// 洗码最多5W
				if (tryCredit > 50000)
					tryCredit = 50000.0;
				Xima xima = new Xima(pno, user.getRole(), loginname, payType, firstCash, tryCredit, DateUtil.convertToTimestamp(startTime),
						DateUtil.convertToTimestamp(endTime), rate, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname,
						tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(xima);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}

	public String addPrize(String proposer, String loginname, String title, String from, Date startTime, Date endTime, Double firstCash,
			Double rate, String payType, String remark) throws GenericDfhRuntimeException {
		log.info("add Xima proposal");
		String msg = null;

		if (startTime != null && endTime.before(startTime)) {
			msg = "结算结束时间必须大于结算开始时间";
			return msg;
		}

		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.PRIZE)) {
			msg = "已提交过幸运抽奖提案，尚未处理完";
			return msg;
		}
		if (msg == null) {
			String pno = seqDao.generateProposalPno(ProposalType.PRIZE);
			try {
				Double tryCredit = Double.valueOf(Math.abs(firstCash.doubleValue()) * Math.abs(rate.doubleValue()));
				Xima xima = new Xima(pno, user.getRole(), loginname, payType, firstCash, tryCredit, DateUtil.convertToTimestamp(startTime),
						DateUtil.convertToTimestamp(endTime), rate, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.PRIZE.getCode(), user.getLevel(), loginname,
						tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(xima);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}

	public String audit(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.SUBMITED.getCode().intValue())
			msg = "该提案不是待审核状态";
		else
			try {
				proposal.setFlag(ProposalFlagType.AUDITED.getCode());
				proposal.setRemark("审核:" + StringUtils.trimToEmpty(remark));
				taskDao.auditTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	public String cancle(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() == ProposalFlagType.CANCLED.getCode().intValue()
				|| proposal.getFlag().intValue() == ProposalFlagType.EXCUTED.getCode().intValue())// update sun
			msg = "该提案已取消或已执行";
		else
			try {
				if (proposal.getType().intValue() == ProposalType.CASHOUT.getCode().intValue())
					tradeDao.changeCredit(proposal.getLoginname(), Double.valueOf(Math.abs(proposal.getAmount().doubleValue())),
							CreditChangeType.CASHOUT_RETURN.getCode(), pno, (new StringBuilder("退还提款,")).append(operator).toString());
				proposal.setFlag(ProposalFlagType.CANCLED.getCode());
				proposal.setRemark(proposal.getRemark() + ";取消:" + StringUtils.trimToEmpty(remark));
				taskDao.cancleTask(pno, operator, ip);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	public String clientCancle(String pno, String loginname, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.SUBMITED.getCode().intValue())
			msg = "该提案已取消或已执行或已审核";
		else
			try {
				if (proposal.getType().intValue() == ProposalType.CASHOUT.getCode().intValue())
					tradeDao.changeCredit(proposal.getLoginname(), Double.valueOf(Math.abs(proposal.getAmount().doubleValue())),
							CreditChangeType.CASHOUT_RETURN.getCode(), pno, (new StringBuilder("退还提款,")).append(loginname).toString());
				proposal.setFlag(ProposalFlagType.CANCLED.getCode());
				proposal.setRemark(proposal.getRemark() + ";取消:" + StringUtils.trimToEmpty(remark));
				taskDao.cancleTask(pno, loginname, ip);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	public String excute(String pno, String operator, String ip, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.AUDITED.getCode().intValue()) {
			msg = "该提案不是待执行状态";
		} else {
			Integer type = proposal.getType();
			if (type.intValue() == ProposalType.CASHIN.getCode().intValue()) {
				Cashin cashin = (Cashin) get(Cashin.class, pno, LockMode.UPGRADE);
				if (cashin == null)
					throw new GenericDfhRuntimeException("存款提案记录不存在");
				tradeDao.changeCredit(cashin.getLoginname(), cashin.getMoney(), CreditChangeType.CASHIN.getCode(), pno, remark);
				Double transferConsMoney = Constants.getTransferConsMoney(cashin.getMoney());
				if (transferConsMoney > 0)
					tradeDao
							.changeCredit(cashin.getLoginname(), transferConsMoney, CreditChangeType.BANK_TRANSFER_CONS.getCode(), null, "系统自动添加");

				// 设置用户 isCashin字段
				userDao.setUserCashin(proposal.getLoginname());
			} else if (type.intValue() == ProposalType.CONCESSIONS.getCode().intValue()) {
				Concessions concessions = (Concessions) get(Concessions.class, pno);
				if (concessions == null)
					throw new GenericDfhRuntimeException("首存优惠提案记录不存在");
				tradeDao.changeCredit(concessions.getLoginname(), Math.abs(concessions.getTryCredit()), CreditChangeType.FIRST_DEPOSIT_CONS
						.getCode(), pno, remark);
			} else if (type.intValue() == ProposalType.BANKTRANSFERCONS.getCode().intValue()) {
				Banktransfercons cons = (Banktransfercons) get(Banktransfercons.class, pno);
				if (cons == null)
					throw new GenericDfhRuntimeException("转账优惠提案记录不存在");
				tradeDao.changeCredit(cons.getLoginname(), Math.abs(cons.getTryCredit()), CreditChangeType.BANK_TRANSFER_CONS.getCode(), pno,
						remark);
			} else if (type.intValue() == ProposalType.REBANKINFO.getCode().intValue()) {
				Rebankinfo rebankinfo = (Rebankinfo) get(Rebankinfo.class, pno);
				if (rebankinfo == null)
					throw new GenericDfhRuntimeException("银改提案记录不存在");
				Accountinfo accountinfo = (Accountinfo) get(Accountinfo.class, rebankinfo.getLoginname());
				accountinfo.setAccountCity(rebankinfo.getAccountCity());
				accountinfo.setAccountName(rebankinfo.getAccountName());
				accountinfo.setAccountNo(rebankinfo.getAccountNo());
				accountinfo.setAccountType(rebankinfo.getAccountType());
				accountinfo.setBank(rebankinfo.getBank());
				accountinfo.setBankAddress(rebankinfo.getBankAddress());
				accountinfo.setLastModifyTime(DateUtil.now());
				update(accountinfo);
			} else if (type.intValue() == ProposalType.XIMA.getCode().intValue()) {
				Xima xima = (Xima) get(Xima.class, pno);
				if (xima == null)
					throw new GenericDfhRuntimeException("洗码优惠提案记录不存在");
				// userDao.setLevel(xima.getLoginname(), VipLevel.BAIJIN.getCode(), Constants.DEFAULT_OPERATOR);
				tradeDao.changeCredit(xima.getLoginname(), Double.valueOf(Math.abs(xima.getTryCredit().doubleValue())),
						CreditChangeType.XIMA_CONS.getCode(), pno, remark);
			} else if (type.intValue() == ProposalType.PRIZE.getCode().intValue()) {
				Prize prize = (Prize) get(Prize.class, pno);
				if (prize == null)
					throw new GenericDfhRuntimeException("幸运抽奖提案记录不存在");
				tradeDao.changeCredit(prize.getLoginname(), Double.valueOf(Math.abs(prize.getTryCredit().doubleValue())), CreditChangeType.PRIZE
						.getCode(), pno, remark);
			} else if (type.intValue() == ProposalType.OFFER.getCode().intValue()) {
				Offer offer = (Offer) get(Offer.class, pno);
				if (offer == null)
					throw new GenericDfhRuntimeException("再存优惠提案记录不存在");
				tradeDao.changeCredit(offer.getLoginname(), Math.abs(offer.getMoney().doubleValue()), CreditChangeType.OFFER_CONS.getCode(), pno,
						remark);
			}
			try {
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				proposal.setRemark(proposal.getRemark() + ";执行:" + StringUtils.trimToEmpty(remark));
				taskDao.excuteTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.EXCUTE, "ip:" + ip + ";pno:" + pno);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}

	/*
	 * public String addCashout(String proposer, String loginname, String title, String from, Double money, String ip, String remark, String
	 * notifyNote, String notifyPhone) throws GenericDfhRuntimeException { log.info("add Cashout proposal"); String msg = null; Users user =
	 * (Users) get(Users.class, loginname, LockMode.UPGRADE); msg = userDao.checkUserForProposal(user); if (msg != null) return msg; if
	 * (user.getCredit() < money) { msg = "用户额度不足，无法申请提款"; return msg; } if (!user.getRole().equals(title)) { msg = "未找到该类型的帐号,可能用户类型选择错误"; return
	 * msg; } if (proposalDao.existNotAuditedProposal(loginname, ProposalType.CASHOUT)) { msg = "该用户已提交过提款提案，尚未审批完"; return msg; } if (msg ==
	 * null) try { Accountinfo accountinfo = (Accountinfo) get(Accountinfo.class, loginname); if (accountinfo == null) { msg = "用户尚未完善银行资料"; }
	 * else if (!user.getAliasName().equals(accountinfo.getAccountName())) msg = "用户账户姓名与银行信息姓名不一致"; else { String pno =
	 * seqDao.generateProposalPno(ProposalType.CASHOUT); tradeDao.changeCredit(loginname, Math.abs(money) * -1,
	 * CreditChangeType.CASHOUT.getCode(), pno, null); Cashout cashout = new Cashout(pno, user.getRole(), user.getLoginname(), money,
	 * accountinfo.getAccountName(), accountinfo.getAccountType(), accountinfo.getAccountCity(), accountinfo.getBankAddress(),
	 * accountinfo.getAccountNo(), accountinfo.getBank(), user.getPhone(), user.getEmail(), ip, remark, notifyNote, notifyPhone); Proposal
	 * proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CASHOUT.getCode(), user.getLevel(), loginname, money,
	 * ProposalFlagType.SUBMITED.getCode(), from, null, null); save(cashout); save(proposal); taskDao.generateTasks(pno, proposer); msg = null; }
	 * } catch (Exception e) { e.printStackTrace(); throw new GenericDfhRuntimeException(e.getMessage()); } return msg; }
	 */

	// 审核执行
	public String auditExcute(String pno, String operator, String ip, String remark) {
		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() != ProposalFlagType.SUBMITED.getCode().intValue()) {
			msg = "该提案不是待审核状态";
		} else {
			try {
				proposal.setFlag(ProposalFlagType.AUDITED.getCode());
				proposal.setRemark("审核:" + StringUtils.trimToEmpty(remark));
				save(proposal);
				taskDao.auditTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.AUDIT, "ip:" + ip + ";pno:" + pno);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new GenericDfhRuntimeException(ex.getMessage());
			}
			proposal = (Proposal) get(Proposal.class, pno);
			Integer type = proposal.getType();
			if (type.intValue() == ProposalType.CASHIN.getCode().intValue()) {
				Cashin cashin = (Cashin) get(Cashin.class, pno, LockMode.UPGRADE);
				if (cashin == null)
					throw new GenericDfhRuntimeException("存款提案记录不存在");
				tradeDao.changeCredit(cashin.getLoginname(), cashin.getMoney(), CreditChangeType.CASHIN.getCode(), pno, remark);
				Double transferConsMoney = Constants.getTransferConsMoney(cashin.getMoney());
				if (transferConsMoney > 0)
					tradeDao
							.changeCredit(cashin.getLoginname(), transferConsMoney, CreditChangeType.BANK_TRANSFER_CONS.getCode(), null, "系统自动添加");
			} else if (type.intValue() == ProposalType.CONCESSIONS.getCode().intValue()) {
				Concessions concessions = (Concessions) get(Concessions.class, pno);
				if (concessions == null)
					throw new GenericDfhRuntimeException("首存优惠提案记录不存在");
				tradeDao.changeCredit(concessions.getLoginname(), Math.abs(concessions.getTryCredit()), CreditChangeType.FIRST_DEPOSIT_CONS
						.getCode(), pno, remark);
			} else if (type.intValue() == ProposalType.BANKTRANSFERCONS.getCode().intValue()) {
				Banktransfercons cons = (Banktransfercons) get(Banktransfercons.class, pno);
				if (cons == null)
					throw new GenericDfhRuntimeException("转账优惠提案记录不存在");
				tradeDao.changeCredit(cons.getLoginname(), Math.abs(cons.getTryCredit()), CreditChangeType.BANK_TRANSFER_CONS.getCode(), pno,
						remark);
			} else if (type.intValue() == ProposalType.REBANKINFO.getCode().intValue()) {
				Rebankinfo rebankinfo = (Rebankinfo) get(Rebankinfo.class, pno);
				if (rebankinfo == null)
					throw new GenericDfhRuntimeException("银改提案记录不存在");
				Accountinfo accountinfo = (Accountinfo) get(Accountinfo.class, rebankinfo.getLoginname());
				accountinfo.setAccountCity(rebankinfo.getAccountCity());
				accountinfo.setAccountName(rebankinfo.getAccountName());
				accountinfo.setAccountNo(rebankinfo.getAccountNo());
				accountinfo.setAccountType(rebankinfo.getAccountType());
				accountinfo.setBank(rebankinfo.getBank());
				accountinfo.setBankAddress(rebankinfo.getBankAddress());
				accountinfo.setLastModifyTime(DateUtil.now());
				update(accountinfo);
			} else if (type.intValue() == ProposalType.XIMA.getCode().intValue()) {
				Xima xima = (Xima) get(Xima.class, pno);
				if (xima == null)
					throw new GenericDfhRuntimeException("洗码优惠提案记录不存在");
				// if (xima.getFirstCash().doubleValue() >= 300 * 10000)
				// userDao.setLevel(xima.getLoginname(), VipLevel.BAIJIN.getCode(), Constants.DEFAULT_OPERATOR);
				tradeDao.changeCredit(xima.getLoginname(), Double.valueOf(Math.abs(xima.getTryCredit().doubleValue())),
						CreditChangeType.XIMA_CONS.getCode(), pno, remark);
			}
			try {
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				proposal.setRemark(proposal.getRemark() + ";执行:" + StringUtils.trimToEmpty(remark));
				taskDao.excuteTask(pno, operator, ip);
				logDao.insertOperationLog(operator, OperationLogType.EXCUTE, "ip:" + ip + ";pno:" + pno);
				msg = null;
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return msg;
	}

	// 后台添加用户
	public String operatorAddNewAccount(String proposer, String loginname, String pwd, String title, String from, String aliasName, String phone,
			String email, String role, String remark, String ipaddress) {
		log.info("add NewAccount operatorAddNewAccount");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		if (user != null) {
			msg = "该用户已存在";
			return msg;
		}
		if (UserRole.MONEY_CUSTOMER.getCode().equals(title)
				&& !(loginname.startsWith(Constants.PREFIX_MONEY_CUSTOMER) || loginname.startsWith(Constants.PREFIX_PARTNER_SUBMEMBER)))
			msg = UserRole.MONEY_CUSTOMER.getText() + "应以" + Constants.PREFIX_MONEY_CUSTOMER + "或" + Constants.PREFIX_PARTNER_SUBMEMBER + "开头";
		else if (UserRole.AGENT.getCode().equals(title) && !loginname.startsWith(Constants.PREFIX_AGENT))
			msg = UserRole.AGENT.getText() + "应以" + Constants.PREFIX_AGENT + "开头";
		// else if (UserRole.PARTNER.getCode().equals(title) && !loginname.startsWith(Constants.PREFIX_PARTNER))
		// msg = UserRole.PARTNER.getText() + "应以" + Constants.PREFIX_PARTNER + "开头";
		if (msg != null)
			return msg;
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.NEWACCOUNT)) {
			msg = "已提交过该帐号的开户提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.NEWACCOUNT);
				Newaccount newAccount = new Newaccount(pno, role, loginname, pwd, phone, email, aliasName, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.NEWACCOUNT.getCode(), VipLevel.COMMON.getCode(),
						loginname, null, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(newAccount);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
				msg = auditExcute(pno, proposer, ipaddress, "ok");
				if (msg != null) {
					throw new GenericDfhRuntimeException(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	// 添加首存优惠
	public String addUserConcession(String proposer, String loginname, String title, String from, Double firstCash, Double tryCredit,
			String payType, String remark, String ipaddress) throws GenericDfhRuntimeException {
		log.info("add Cashout proposal");
		String msg = null;
		// FirstlyCashin firstlyCashin = userDao.getFirstCashin(loginname);// 用户第一笔存款
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		// if (firstlyCashin == null)
		msg = "您还没有存款";
		// else if (firstlyCashin.getMoney().doubleValue() < firstCash.doubleValue())
		// msg = "你的第一笔存款金额为:" + firstlyCashin.getMoney();
		if (new Double(firstCash.doubleValue() * 0.68).intValue() != tryCredit.intValue())
			msg = "申请金额为:第一笔存款金额的68%";
		// else if
		// (DateUtil.convertToTimestamp(DateUtil.getToday()).after(DateUtil.parseDateForStandard(DateUtil.getMontHreduce(user.getCreatetime(),
		// 30))))
		// msg = "您开户已经超过30天不能再享受开户优惠";
		else {
			log.info("add Cashout proposal");
			msg = userDao.checkUserForProposal(user);
			if (msg != null)
				return msg;
			if (!user.getRole().equals(title)) {
				msg = "未找到该类型的帐号,可能用户类型选择错误";
				return msg;
			}
			if (proposalDao.existNotCancledProposal(loginname, ProposalType.CONCESSIONS)) {
				msg = "已经提交或曾经申请过首存款优惠提案";
				return msg;
			}
			if (msg == null) {
				try {
					String pno = seqDao.generateProposalPno(ProposalType.CONCESSIONS);
					Concessions concessions = new Concessions(pno, user.getRole(), loginname, payType, firstCash, tryCredit, remark);
					Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CONCESSIONS.getCode(), user.getLevel(),
							loginname, tryCredit, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
					save(concessions);
					save(proposal);
					taskDao.generateTasks(pno, proposer);
				} catch (Exception e) {
					e.printStackTrace();
					throw new GenericDfhRuntimeException(e.getMessage());
				}
			}
		}
		return msg;
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public OperatorDao getOperatorDao() {
		return operatorDao;
	}

	public ProposalDao getProposalDao() {
		return proposalDao;
	}

	public SeqDao getSeqDao() {
		return seqDao;
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public void setOperatorDao(OperatorDao operatorDao) {
		this.operatorDao = operatorDao;
	}

	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public AgentDao getAgentDao() {
		return agentDao;
	}

	public void setAgentDao(AgentDao agentDao) {
		this.agentDao = agentDao;
	}

	public String addOffer(String proposer, String loginname, String title, String from, Double firstCash, Double money, String remark)
			throws GenericDfhRuntimeException {
		log.info("add addOffer proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.OFFER)) {
			msg = "该用户已提交过促销优惠提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.OFFER);
				Offer offer = new Offer(pno, title, loginname, firstCash, money, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.OFFER.getCode(), user.getLevel(), loginname, money,
						user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(offer);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	public String addPrize(String proposer, String loginname, String title, String from, Double amount, String remark)
			throws GenericDfhRuntimeException {
		log.info("add addPrize proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotFinishedProposal(loginname, ProposalType.PRIZE)) {
			msg = "该用户已提交过幸运抽奖提案，尚未处理完";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.PRIZE);
				Prize prize = new Prize(pno, title, loginname, amount, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.PRIZE.getCode(), user.getLevel(), loginname, amount,
						user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null);
				save(prize);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	/**
	 * 申请提款
	 */
	public String addCashout(String proposer, String loginname, String pwd, String title, String from, Double money, String accountName,
			String accountNo, String accountType, String bank, String accountCity, String bankAddress, String email, String phone,
			String ip, String remark,String msflag) throws GenericDfhRuntimeException {
		money = Math.abs(money);
		log.info("add Cashout proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getPassword().equalsIgnoreCase(EncryptionUtil.encryptPassword(pwd))) {
			msg="密码错误";
			return msg;
		}
		if (user.getCredit() < money) {
			msg = "用户额度不足,无法申请提款";
			return msg;
		}
		// 提款必须大于10元
		if (money < 1) {
			msg = "1元以上才能申请提款";
			return msg;
		}
		if (!StringUtils.equalsIgnoreCase(user.getAccountName(), accountName)) {
			msg = "用户真实姓名" + user.getAccountName() + "和提款姓名" + accountName + "不一致";
			return msg;
		}
		if (!user.getRole().equals(title)) {
			msg = "未找到该类型的帐号,可能用户类型选择错误";
			return msg;
		}
		if (proposalDao.existNotAuditedProposal(loginname, ProposalType.CASHOUT)) {
			msg = "该用户已提交过提款提案，尚未审批完";
			return msg;
		}
		
		if(msflag.equals("1")){
			
			if (money > 999.0) {
				msg = "抱歉，1000元以下才能使用秒付提款";
				return msg;
			}
			
			Userstatus userstatus =(Userstatus) userDao.get(Userstatus.class, loginname, LockMode.UPGRADE);
			if(userstatus!=null && userstatus.getTouzhuflag()==1){
				msg = "抱歉，你现在申请了优惠，暂时不能使用秒付提款";
				return msg;
			}
			
			Const cons = (Const) userDao.get(Const.class, "民生银行秒付");
			if(cons!=null && cons.getValue().equals("0")){
				msg = "抱歉，秒付提款功能暂时关闭，请使用5分钟提款";
				return msg;
			}
		}
		
		
		List<Proposal> list = proposalDao.getCashoutToday(loginname);
		if (list.size()>=Integer.parseInt(Configuration.getInstance().getValue("CashoutTodayCount"))) {
			msg = "抱歉，提款失败！\\n每天只可以提款【"+Configuration.getInstance().getValue("CashoutTodayCount")+"】次";
			return msg;
		}
		
		double oldCashoutAmount=0d;
		for (int i = 0; i < list.size(); i++) {
			Proposal proposal = list.get(i);
			oldCashoutAmount+=proposal.getAmount();
		}
		
		if ((oldCashoutAmount+money)>Double.parseDouble(Configuration.getInstance().getValue("CashoutTodayAmount"))) {
			msg = "抱歉，提款失败！\\n" +
					"每天最大提款额度为【"+Configuration.getInstance().getValue("CashoutTodayAmount")+"】元\\n" +
					"您已经提款【"+oldCashoutAmount+"】元，还可以提款【"+String.valueOf(Double.parseDouble(Configuration.getInstance().getValue("CashoutTodayAmount"))-oldCashoutAmount)+"】元";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.CASHOUT);
				tradeDao.changeCredit(loginname, money * -1, CreditChangeType.CASHOUT.getCode(), pno, null);
				Cashout cashout = new Cashout(pno, user.getRole(), user.getLoginname(), money, accountName, accountType, accountCity,
						bankAddress, accountNo, bank, phone, email, ip, remark, null, null);

				// 记录提款后的本地和远程额度
				Double afterLocalCredit = -1.0, afterRemoteCredit = -1.0;
				try {
					afterLocalCredit = user.getCredit();
					afterRemoteCredit = RemoteCaller.queryCredit(user.getLoginname());
				} catch (Exception e) {
					e.printStackTrace();
					try {
						afterRemoteCredit = RemoteCaller.queryCredit(user.getLoginname());
					} catch (Exception e1) {
						e1.printStackTrace();
						//如果没查到远程额度，标记为-1.0
						afterRemoteCredit=-1.0;
					}
				}

				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CASHOUT.getCode(), user.getLevel(), loginname,
						money, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, null, null, afterLocalCredit==null?-1.0:afterLocalCredit, afterRemoteCredit==null?-1.0:afterRemoteCredit);
				proposal.setBankname(bank);
				save(cashout);
				if(loginname.equals("test")){
					proposal.setMsflag(1);
//					String ENDPOINT = "http://192.168.0.169:8080/msWS/services/msBankService";
//					String QNAME = "http://service.webservice.ms/";
//					if(Axis1Util.getConnection(ENDPOINT+"?wsdl")){
//						Object[] obj = new Object[] {pno,accountName,accountNo, "icbc", ""+money,"秒提" };
//						Integer result = (Integer) Axis1Util.getAxisCall_AddOrder().invoke(obj);
//						if(result !=null && result.intValue()==1){
//							proposal.setMsflag(1);
//						}
//					}
				}
				if(msflag.equals("1")){
					proposal.setMsflag(1);
//					String ENDPOINT = "http://192.168.0.169:8080/msWS/services/msBankService";
//					String QNAME = "http://service.webservice.ms/";
//					if(Axis1Util.getConnection(ENDPOINT+"?wsdl")){
//						Object[] obj = new Object[] {pno,accountName,accountNo, "icbc", ""+money,"秒提" };
//						Integer result = (Integer) Axis1Util.getAxisCall_AddOrder().invoke(obj);
//						if(result !=null && result.intValue()==1){
//							proposal.setMsflag(1);
//						}
//					}
				}
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	/*
	 * 查找自本次提款的上次提款的时刻额度和时间
	 */
	public Proposal getLastSuccCashout(String loginname, Date before) {
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("loginname", loginname)).add(
				Restrictions.eq("type", ProposalType.CASHOUT.getCode())).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode())).add(
				Restrictions.lt("createTime", before)).addOrder(Order.desc("createTime"));
		List<Proposal> list = findByCriteria(dc,0,1);
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public Integer totalProposals(Date starttime, Date endtime, String loginname,
			Integer type,String username) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*) from proposal ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = proposal.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '"+formatStr2+"' ");
			}
			if(type != 0){
				sql.append(" and  proposal.type="+type+"  ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and proposal.loginname like '%"+username+"%' ");
			}
			
			//List numbers = proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getOneInteger(sql.toString());
			//return ((BigInteger)numbers.get(0)).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		
	}

	@Override
	public Integer totalPayorder(Date starttime, Date endtime,
			String loginname, String username) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*) from payorder ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = payorder.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '"+formatStr2+"' ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and payorder.loginname like '%"+username+"%' ");
			}
			//List numbers = proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getOneInteger(sql.toString());
			//return ((BigInteger)numbers.get(0)).intValue();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	@Override
	public Integer totalCounts(Date starttime, Date endtime, String loginname,
			String username) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*) from proposal ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = proposal.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '"+formatStr2+"' ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and proposal.loginname like '%"+username+"%' ");
			}
			
			sql.append(" union all select count(*) from payorder ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = payorder.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '"+formatStr2+"' ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and payorder.loginname like '%"+username+"%' ");
			}
			//List numbers = proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getTwoInteger(sql.toString());
			//return (((BigInteger)numbers.get(0)).intValue()+((BigInteger)numbers.get(1)).intValue());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	
	
	@Override
	public List<Proposal> searchSubPayorderamount(Date starttime, Date endtime,
			String loginname, String username, int pageno, int length) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select payorder.loginname,payorder.createTime,payorder.money from payorder ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = payorder.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '"+formatStr2+"' ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and payorder.loginname like '%"+username+"%' ");
			}
			sql.append(" order by payorder.createTime ");
			int offset=(pageno-1)*length;
			//List list = proposalDao.getSessionFactory().openSession().createSQLQuery(sql.toString()).setFirstResult(offset).setMaxResults(length).list();
			List list = agentDao.getList(sql.toString(), offset, length);
			if (list ==null) {
				return null;
			}
			else{
				List<Proposal> proposals = new ArrayList<Proposal>();
				for(int i=0;i<list.size();i++){
					Object[] o=(Object[]) list.get(i);
					Proposal proposal = new Proposal();
					proposal.setLoginname((String)o[0]);
					proposal.setCreateTime((Timestamp)o[1]);
					proposal.setAmount((Double)o[2]);
					proposal.setType(1);
					proposals.add(proposal);
				}
				return proposals;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Proposal> searchSubProposalamount(Date starttime, Date endtime,
			String loginname, Integer type, String username, int pageno,
			int length) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select proposal.loginname,proposal.createTime,proposal.amount,proposal.type from proposal ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = proposal.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '"+formatStr2+"' ");
			}
			if(type != 0){
				sql.append(" and  proposal.type="+type+"  ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and proposal.loginname like '%"+username+"%' ");
			}
			sql.append(" order by proposal.createTime ");
			int offset=(pageno-1)*length;
			//List list = proposalDao.getSessionFactory().openSession().createSQLQuery(sql.toString()).setFirstResult(offset).setMaxResults(length).list();
			List list = agentDao.getList(sql.toString(), offset, length);
			if (list ==null) {
				return null;
			}
			else{
				List<Proposal> proposals = new ArrayList<Proposal>();
				for(int i=0;i<list.size();i++){
					Object[] o=(Object[]) list.get(i);
					Proposal proposal = new Proposal();
					proposal.setLoginname((String)o[0]);
					proposal.setCreateTime((Timestamp)o[1]);
					proposal.setAmount((Double)o[2]);
					proposal.setType((Integer)o[3]);
					proposals.add(proposal);
				}
				return proposals;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Proposal> searchSubTotal(Date starttime, Date endtime,
			String loginname, String username, int pageno,
			int length) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select proposal.loginname,proposal.createTime,proposal.amount,proposal.type from proposal ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = proposal.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '"+formatStr2+"' ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and proposal.loginname like '%"+username+"%' ");
			}
			
			sql.append("  union all select payorder.loginname,payorder.createTime,payorder.money,newaccount from payorder ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = payorder.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '"+formatStr2+"' ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and payorder.loginname like '%"+username+"%' ");
			}
			sql.append("  order by createTime desc ");
			int offset=(pageno-1)*length;
			//List list = proposalDao.getSessionFactory().openSession().createSQLQuery(sql.toString()).setFirstResult(offset).setMaxResults(length).list();
			List list = agentDao.getList(sql.toString(), offset, length);
			if (list ==null) {
				return null;
			}else{
				List<Proposal> proposals = new ArrayList<Proposal>();
				for(int i=0;i<list.size();i++){
					Object[] o=(Object[]) list.get(i);
					Proposal proposal = new Proposal();
					proposal.setLoginname((String)o[0]);
					proposal.setCreateTime((Timestamp)o[1]);
					proposal.setAmount((Double)o[2]);
					proposal.setType((Integer)o[3]);
					proposals.add(proposal);
				}
				return proposals;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Double totalProposalamount(Date starttime, Date endtime, String loginname,
			Integer type, String username) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select sum(proposal.amount) from proposal ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = proposal.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '"+formatStr2+"' ");
			}
			if(type != 0){
				sql.append(" and  proposal.type="+type+"  ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and proposal.loginname like '%"+username+"%' ");
			}
			//List numbers = proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getOneDouble(sql.toString());
			//return(Double)numbers.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	@Override
	public Double totalPayorderamount(Date starttime, Date endtime,
			String loginname, String username) {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select sum(payorder.money) from payorder ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = payorder.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '"+formatStr2+"' ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and payorder.loginname like '%"+username+"%' ");
			}
			//List numbers = proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getOneDouble(sql.toString());
			//return(Double)numbers.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	@Override
	public Double totalamount(Date starttime, Date endtime, String loginname,
			String username) {
		try { 
			StringBuilder sql = new StringBuilder();
			sql.append("select sum(proposal.amount) from proposal ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = proposal.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '"+formatStr2+"' ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and proposal.loginname like '%"+username+"%' ");
			}
			
			sql.append(" union all select sum(payorder.money) from payorder ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = payorder.loginname ");
			sql.append(" where users.agent = '"+loginname+"' ");
			
			if (starttime != null ){
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '"+formatStr2+"' ");
			}
			if (endtime != null ){
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '"+formatStr2+"' ");
			}
			if(username!=null&&!username.equals("")){
				sql.append(" and payorder.loginname like '%"+username+"%' ");
			}
//			List numbers = proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getTwoDouble(sql.toString());
//			Double double1 = (Double)numbers.get(0);
//			if(double1 == null){
//				double1 = 0.0;
//			}
//			Double double2 = (Double)numbers.get(1);
//			if(double2 == null){
//				double2 = 0.0;
//			}
//			return double1+double2;
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	

}
