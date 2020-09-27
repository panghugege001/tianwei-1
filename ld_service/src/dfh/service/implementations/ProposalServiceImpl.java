package dfh.service.implementations;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dfh.model.*;
import dfh.service.interfaces.SlaveService;
import dfh.utils.sendemail.AESUtil;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.dao.AgentDao;
import dfh.dao.LogDao;
import dfh.dao.OperatorDao;
import dfh.dao.ProposalDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.model.enums.VipLevel;
import dfh.remote.DocumentParser;
import dfh.remote.RemoteCaller;
import dfh.remote.RemoteConstant;
import dfh.remote.bean.KenoResponseBean;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.utils.Arith;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.DtUtil;
import dfh.utils.EBetUtil;
import dfh.utils.GPIUtil;
import dfh.utils.Keno2Util;
import dfh.utils.MGSUtil;
import dfh.utils.NTUtils;
import dfh.utils.PNGUtil;
import dfh.utils.PtUtil;
import dfh.utils.PtUtil1;
import dfh.utils.QtUtil;
import dfh.utils.SlotUtil;
import dfh.utils.StringUtil;

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
	private SeqService seqService;
	private TransferDao transferDao;

	private String msg;

	public ProposalServiceImpl() {

	}

	public String addCashin(String proposer, String loginname, String aliasName, String title, String from, Double money,
			String corpBankName, String remark, String accountNo, String bankaccount, String saveway, String cashintime)
			throws GenericDfhRuntimeException {

		log.info("add Cashin proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		// if (!user.getRole().equals(title)) {
		// msg = "未找到该类型的帐号,可能用户类型选择错误";
		// return msg;
		// }
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
				Cashin cashin = new Cashin(pno, user.getRole(), user.getLoginname(), aliasName, money, accountNo,
						corpBankName, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CASHIN.getCode(),
						user.getLevel(), loginname, money,
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

	public String addConcession(String proposer, String loginname, String title, String from, Double firstCash,
			Double tryCredit, String payType,
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
				Concessions concessions = new Concessions(pno, user.getRole(), loginname, payType, firstCash, tryCredit,
						remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CONCESSIONS.getCode(),
						user.getLevel(), loginname,
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
	public String addBankTransferCons(String proposer, String loginname, String title, String from, Double firstCash,
			Double tryCredit,
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
				Banktransfercons cons = new Banktransfercons(pno, user.getRole(), loginname, payType, firstCash, tryCredit,
						remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.BANKTRANSFERCONS.getCode(),
						user.getLevel(),
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

	public String addNewAccount(String proposer, String loginname, String pwd, String title, String from, String aliasName,
			String phone,
			String email, String role, String remark) {

		log.info("add NewAccount proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		if (user != null) {
			msg = "该用户已存在";
			return msg;
		}
		if (UserRole.MONEY_CUSTOMER.getCode().equals(title)
				&& !(loginname.startsWith(Constants.PREFIX_MONEY_CUSTOMER) || loginname
						.startsWith(Constants.PREFIX_PARTNER_SUBMEMBER)))
			msg = UserRole.MONEY_CUSTOMER.getText() + "应以" + Constants.PREFIX_MONEY_CUSTOMER + "或"
					+ Constants.PREFIX_PARTNER_SUBMEMBER + "开头";
		else if (UserRole.AGENT.getCode().equals(title) && !loginname.startsWith(Constants.PREFIX_AGENT))
			msg = UserRole.AGENT.getText() + "应以" + Constants.PREFIX_AGENT + "开头";
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
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.NEWACCOUNT.getCode(),
						VipLevel.TIANJIANG.getCode(),
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

	public String addReBankInfo(String proposer, String loginname, String title, String from, String accountName,
			String accountNo,
			String accountType, String bank, String accountCity, String bankAddress, String ip, String remark)
			throws GenericDfhRuntimeException {

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
				Rebankinfo rebankinfo = new Rebankinfo(pno, user.getRole(), loginname, accountName, accountNo, bank,
						accountType, accountCity,
						bankAddress, ip, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.REBANKINFO.getCode(),
						user.getLevel(), loginname,
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

	public String addXima(String proposer, String loginname, String title, String from, Date startTime, Date endTime,
			Double firstCash,
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
				Xima xima = new Xima(pno, user.getRole(), loginname, payType, firstCash, tryCredit,
						DateUtil.convertToTimestamp(startTime),
						DateUtil.convertToTimestamp(endTime), rate, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.XIMA.getCode(),
						user.getLevel(), loginname,
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

	public String addPrize(String proposer, String loginname, String title, String from, Date startTime, Date endTime,
			Double firstCash,
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
				Xima xima = new Xima(pno, user.getRole(), loginname, payType, firstCash, tryCredit,
						DateUtil.convertToTimestamp(startTime),
						DateUtil.convertToTimestamp(endTime), rate, remark);
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.PRIZE.getCode(),
						user.getLevel(), loginname,
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
				|| proposal.getFlag().intValue() == ProposalFlagType.EXCUTED.getCode().intValue())// update
																									// sun
			msg = "该提案已取消或已执行";
		else
			try {
				if (proposal.getType().intValue() == ProposalType.CASHOUT.getCode().intValue())
					tradeDao.changeCredit(proposal.getLoginname(), Double.valueOf(Math.abs(proposal.getAmount()
							.doubleValue())),
							CreditChangeType.CASHOUT_RETURN.getCode(), pno, (new StringBuilder("退还提款,")).append(operator)
									.toString());
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

	@Override
	public void createUnion(String userName,DetachedCriteria dc,Integer level) {
		try {
			Union union = new Union();
			union.setMax(5);
			union.setNums(1);
			union.setLevel(1);
			union.setPresident(userName);
			tradeDao.save(union);
			List<Union> unions = tradeDao.findByCriteria(dc);
			if(!unions.isEmpty()||unions.size()>0){
				Union u = unions.get(0);
				UnionStaff unionStaff = new UnionStaff();
				unionStaff.setUnionId(u.getId());
				unionStaff.setLevel(level);
				unionStaff.setUsername(userName);
				unionStaff.setRemark("会长");
				unionStaff.setUpdateTime(new Date());
				tradeDao.save(unionStaff);
			}
		}catch (Exception e){

		}

	}

	@Override
	public Question queryQuestionForApp(String loginname) {

		DetachedCriteria dc = DetachedCriteria.forClass(Question.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("delflag", 0));
		dc.add(Restrictions.eq("questionid", 7));
		List<Question> questions = proposalDao.findByCriteria(dc);
		if (null != questions && questions.size() > 0 && null != questions.get(0)) {
			return questions.get(0);
		}
		return null;
	}
	@Override
	public void joinUnion(String userName,Integer level,Integer id) {
		try {
				UnionStaff unionStaff = new UnionStaff();
				unionStaff.setUnionId(id);
				unionStaff.setLevel(level);
				unionStaff.setUsername(userName);
				unionStaff.setRemark("申请");
				unionStaff.setUpdateTime(new Date());
				tradeDao.save(unionStaff);
		}catch (Exception e){

		}

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
					tradeDao.changeCredit(proposal.getLoginname(), Double.valueOf(Math.abs(proposal.getAmount()
							.doubleValue())),
							CreditChangeType.CASHOUT_RETURN.getCode(), pno, (new StringBuilder("退还提款,")).append(loginname)
									.toString());
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
				tradeDao.changeCredit(cashin.getLoginname(), cashin.getMoney(), CreditChangeType.CASHIN.getCode(), pno,
						remark);
				Double transferConsMoney = Constants.getTransferConsMoney(cashin.getMoney());
				if (transferConsMoney > 0)
					tradeDao
							.changeCredit(cashin.getLoginname(), transferConsMoney,
									CreditChangeType.BANK_TRANSFER_CONS.getCode(), null, "系统自动添加");

				// 设置用户 isCashin字段
				userDao.setUserCashin(proposal.getLoginname());
			} else if (type.intValue() == ProposalType.CONCESSIONS.getCode().intValue()) {
				Concessions concessions = (Concessions) get(Concessions.class, pno);
				if (concessions == null)
					throw new GenericDfhRuntimeException("首存优惠提案记录不存在");
				tradeDao.changeCredit(concessions.getLoginname(), Math.abs(concessions.getTryCredit()),
						CreditChangeType.FIRST_DEPOSIT_CONS
								.getCode(), pno, remark);
			} else if (type.intValue() == ProposalType.BANKTRANSFERCONS.getCode().intValue()) {
				Banktransfercons cons = (Banktransfercons) get(Banktransfercons.class, pno);
				if (cons == null)
					throw new GenericDfhRuntimeException("转账优惠提案记录不存在");
				tradeDao.changeCredit(cons.getLoginname(), Math.abs(cons.getTryCredit()),
						CreditChangeType.BANK_TRANSFER_CONS.getCode(), pno,
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
				// userDao.setLevel(xima.getLoginname(),
				// VipLevel.BAIJIN.getCode(), Constants.DEFAULT_OPERATOR);
				tradeDao.changeCredit(xima.getLoginname(), Double.valueOf(Math.abs(xima.getTryCredit().doubleValue())),
						CreditChangeType.XIMA_CONS.getCode(), pno, remark);
			} else if (type.intValue() == ProposalType.PRIZE.getCode().intValue()) {
				Prize prize = (Prize) get(Prize.class, pno);
				if (prize == null)
					throw new GenericDfhRuntimeException("幸运抽奖提案记录不存在");
				tradeDao.changeCredit(prize.getLoginname(), Double.valueOf(Math.abs(prize.getTryCredit().doubleValue())),
						CreditChangeType.PRIZE
								.getCode(), pno, remark);
			} else if (type.intValue() == ProposalType.OFFER.getCode().intValue()) {
				Offer offer = (Offer) get(Offer.class, pno);
				if (offer == null)
					throw new GenericDfhRuntimeException("再存优惠提案记录不存在");
				tradeDao.changeCredit(offer.getLoginname(), Math.abs(offer.getMoney().doubleValue()),
						CreditChangeType.OFFER_CONS.getCode(), pno,
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
	 * public String addCashout(String proposer, String loginname, String title,
	 * String from, Double money, String ip, String remark, String notifyNote,
	 * String notifyPhone) throws GenericDfhRuntimeException {
	 * log.info("add Cashout proposal"); String msg = null; Users user = (Users)
	 * get(Users.class, loginname, LockMode.UPGRADE); msg =
	 * userDao.checkUserForProposal(user); if (msg != null) return msg; if
	 * (user.getCredit() < money) { msg = "用户额度不足，无法申请提款"; return msg; } if
	 * (!user.getRole().equals(title)) { msg = "未找到该类型的帐号,可能用户类型选择错误"; return
	 * msg; } if (proposalDao.existNotAuditedProposal(loginname,
	 * ProposalType.CASHOUT)) { msg = "该用户已提交过提款提案，尚未审批完"; return msg; } if (msg
	 * == null) try { Accountinfo accountinfo = (Accountinfo)
	 * get(Accountinfo.class, loginname); if (accountinfo == null) { msg =
	 * "用户尚未完善银行资料"; } else if
	 * (!user.getAliasName().equals(accountinfo.getAccountName())) msg =
	 * "用户账户姓名与银行信息姓名不一致"; else { String pno =
	 * seqDao.generateProposalPno(ProposalType.CASHOUT);
	 * tradeDao.changeCredit(loginname, Math.abs(money) * -1,
	 * CreditChangeType.CASHOUT.getCode(), pno, null); Cashout cashout = new
	 * Cashout(pno, user.getRole(), user.getLoginname(), money,
	 * accountinfo.getAccountName(), accountinfo.getAccountType(),
	 * accountinfo.getAccountCity(), accountinfo.getBankAddress(),
	 * accountinfo.getAccountNo(), accountinfo.getBank(), user.getPhone(),
	 * user.getEmail(), ip, remark, notifyNote, notifyPhone); Proposal proposal
	 * = new Proposal(pno, proposer, DateUtil.now(),
	 * ProposalType.CASHOUT.getCode(), user.getLevel(), loginname, money,
	 * ProposalFlagType.SUBMITED.getCode(), from, null, null); save(cashout);
	 * save(proposal); taskDao.generateTasks(pno, proposer); msg = null; } }
	 * catch (Exception e) { e.printStackTrace(); throw new
	 * GenericDfhRuntimeException(e.getMessage()); } return msg; }
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
				tradeDao.changeCredit(cashin.getLoginname(), cashin.getMoney(), CreditChangeType.CASHIN.getCode(), pno,
						remark);
				Double transferConsMoney = Constants.getTransferConsMoney(cashin.getMoney());
				if (transferConsMoney > 0)
					tradeDao
							.changeCredit(cashin.getLoginname(), transferConsMoney,
									CreditChangeType.BANK_TRANSFER_CONS.getCode(), null, "系统自动添加");
			} else if (type.intValue() == ProposalType.CONCESSIONS.getCode().intValue()) {
				Concessions concessions = (Concessions) get(Concessions.class, pno);
				if (concessions == null)
					throw new GenericDfhRuntimeException("首存优惠提案记录不存在");
				tradeDao.changeCredit(concessions.getLoginname(), Math.abs(concessions.getTryCredit()),
						CreditChangeType.FIRST_DEPOSIT_CONS
								.getCode(), pno, remark);
			} else if (type.intValue() == ProposalType.BANKTRANSFERCONS.getCode().intValue()) {
				Banktransfercons cons = (Banktransfercons) get(Banktransfercons.class, pno);
				if (cons == null)
					throw new GenericDfhRuntimeException("转账优惠提案记录不存在");
				tradeDao.changeCredit(cons.getLoginname(), Math.abs(cons.getTryCredit()),
						CreditChangeType.BANK_TRANSFER_CONS.getCode(), pno,
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
				// userDao.setLevel(xima.getLoginname(),
				// VipLevel.BAIJIN.getCode(), Constants.DEFAULT_OPERATOR);
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
	public String operatorAddNewAccount(String proposer, String loginname, String pwd, String title, String from,
			String aliasName, String phone,
			String email, String role, String remark, String ipaddress) {

		log.info("add NewAccount operatorAddNewAccount");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		if (user != null) {
			msg = "该用户已存在";
			return msg;
		}
		if (UserRole.MONEY_CUSTOMER.getCode().equals(title)
				&& !(loginname.startsWith(Constants.PREFIX_MONEY_CUSTOMER) || loginname
						.startsWith(Constants.PREFIX_PARTNER_SUBMEMBER)))
			msg = UserRole.MONEY_CUSTOMER.getText() + "应以" + Constants.PREFIX_MONEY_CUSTOMER + "或"
					+ Constants.PREFIX_PARTNER_SUBMEMBER + "开头";
		else if (UserRole.AGENT.getCode().equals(title) && !loginname.startsWith(Constants.PREFIX_AGENT))
			msg = UserRole.AGENT.getText() + "应以" + Constants.PREFIX_AGENT + "开头";
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
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.NEWACCOUNT.getCode(),
						VipLevel.TIANJIANG.getCode(),
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
	public String addUserConcession(String proposer, String loginname, String title, String from, Double firstCash,
			Double tryCredit,
			String payType, String remark, String ipaddress) throws GenericDfhRuntimeException {

		log.info("add Cashout proposal");
		String msg = null;
		// FirstlyCashin firstlyCashin = userDao.getFirstCashin(loginname);//
		// 用户第一笔存款
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		// if (firstlyCashin == null)
		msg = "您还没有存款";
		// else if (firstlyCashin.getMoney().doubleValue() <
		// firstCash.doubleValue())
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
					Concessions concessions = new Concessions(pno, user.getRole(), loginname, payType, firstCash, tryCredit,
							remark);
					Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CONCESSIONS.getCode(),
							user.getLevel(),
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

	public SeqService getSeqService() {

		return seqService;
	}

	public void setSeqService(SeqService seqService) {

		this.seqService = seqService;
	}

	public TransferDao getTransferDao() {

		return transferDao;
	}

	public void setTransferDao(TransferDao transferDao) {

		this.transferDao = transferDao;
	}

	public String addOffer(String proposer, String loginname, String title, String from, Double firstCash, Double money,
			String remark)
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
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.OFFER.getCode(),
						user.getLevel(), loginname, money,
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
				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.PRIZE.getCode(),
						user.getLevel(), loginname, amount,
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

	public Double checkPoints(String userName){
		Double betAmount = userDao.getValidBetAmount(userName, null, null, "MONEY_CUSTOMER");
		return betAmount;
	}

	public Question queryQuestionByCondition(String loginname,String questionid ) {
		DetachedCriteria dc = DetachedCriteria.forClass(Question.class);
		dc.add(Restrictions.eq("delflag", 0));
//		if(map != null ) {
////			for (Map.Entry<K, V> entry : map.entrySet()) {
////				String key = (String)entry.getKey();
////				String value =  (String)entry.getValue().toString();
////				System.out.println("key =" + key + " value = " + value);
////				dc.add(Restrictions.eq(key, value));
////			}
//
//			Iterator<Entry> it = map.entrySet().iterator();
//			while (it.hasNext()) {
//			Map.Entry entry = it.next();
//			String key = (String)entry.getKey();
//			String value =(String) entry.getValue();
//			System.out.println("key = " + key + "; value = " + value);
//			dc.add(Restrictions.eq(key, value));
//			}
//		}
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("questionid", new Integer(questionid).intValue()  ));
		List<Question> questions = proposalDao.findByCriteria(dc);
		if (null != questions && questions.size() > 0 && null != questions.get(0)) {
			return questions.get(0);
		}


		return null;
	}
	@Override
	public Boolean questionValidateForQuestion(Users users, String accountName,String phone,String email,String password) {
		try {

			if (!users.getAccountName().equals(accountName)) {
				return false;
			}
			if (!AESUtil.aesDecrypt(users.getPhone(), AESUtil.KEY).equals(phone)){
				return false;
			}
			if (!EncryptionUtil.encryptPassword(password).equals(users.getPassword())) {
				return false;
			}
			if (!AESUtil.aesDecrypt(users.getEmail(), AESUtil.KEY).equals(email)) {
				return false;
			}
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return  false;
		}
	}

	@Override
	public String savePayPassWordQuestion(String loginname, Integer questionid, String content) {
		try {
			Question question = new Question();
			question.setLoginname(loginname);
			question.setQuestionid(questionid);
			question.setContent(content);
			question.setDelflag(0);
			question.setCreatetime(new Date());
			proposalDao.save(question);
			return "绑定成功";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "绑定失败";
	}

	public  Boolean changePresident(int id,int uId,DetachedCriteria dc,DetachedCriteria dc3,SlaveService slaveService){
		List<Union> list = userDao.findByCriteria(dc);
		Double d=0.0;
		String name="";
		if(!list.isEmpty()||list.size()>0) {
			userDao.delete(UnionStaff.class, id);
			Union union = list.get(0);
			List<UnionStaff> byCriteria = userDao.findByCriteria(dc3);
			for (UnionStaff unionStaff:byCriteria){
				Double deposit = slaveService.getAllDeposit(unionStaff.getUsername());
				if(deposit>d){
					d=deposit;
					name=unionStaff.getUsername();
				}
			}
			String president = union.getPresident();
			union.setPresident(name);
			union.setRemark("会长退团由  "+president+"  变为  "+name);
			userDao.update(union);
			return true;
		}return false;
	}

	/**
	 * 申请提款
	 */
	public String addCashout(String proposer, String loginname, String pwd, String title, String from, Double money,
			String accountName,
			String accountNo, String accountType, String bank, String accountCity, String bankAddress, String email,
			String phone,
			String ip, String remark, String msflag,Double gameTolMoney) throws GenericDfhRuntimeException {

		money = Math.abs(money);
		log.info("add Cashout proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getPassword().equalsIgnoreCase(EncryptionUtil.encryptPassword(pwd))) {
			msg = "密码错误";
			return msg;
		}
		if (money < 100.0 && UserRole.AGENT.getCode().equals(title)) {
			msg = "提款金额必须大于100元";
			return msg;
		}
		if (user.getCredit() < money) {
			msg = "用户额度不足,无法申请提款";
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

		if (proposalDao.existNotMsbankAuditedProposal(loginname, ProposalType.CASHOUT)) {
			msg = "抱歉，你之前的秒付提款尚未审核完，暂时不能使用秒付提款";
			return msg;
		}

		List<Proposal> list = proposalDao.getCashoutToday(loginname);
		if (list.size() >= Integer.parseInt(Configuration.getInstance().getValue("CashoutTodayCount"))) {
			msg = "抱歉，提款失败！\\n每天只可以提款【" + Configuration.getInstance().getValue("CashoutTodayCount") + "】次";
			return msg;
		}

		double oldCashoutAmount = 0d;
		for (int i = 0; i < list.size(); i++) {
			Proposal proposal = list.get(i);
			oldCashoutAmount += proposal.getAmount();
		}

		if ((oldCashoutAmount + money) > Double.parseDouble(Configuration.getInstance().getValue("CashoutTodayAmount"))) {
			msg = "抱歉，提款失败！\\n"
					+
					"每天最大提款额度为【"
					+ Configuration.getInstance().getValue("CashoutTodayAmount")
					+ "】元\\n"
					+
					"您已经提款【"
					+ oldCashoutAmount
					+ "】元，还可以提款【"
					+ String.valueOf(Double.parseDouble(Configuration.getInstance().getValue("CashoutTodayAmount"))
							- oldCashoutAmount) + "】元";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.CASHOUT);
				tradeDao.changeCredit(loginname, money * -1, CreditChangeType.CASHOUT.getCode(), pno, null);
				Cashout cashout = new Cashout(pno, user.getRole(), user.getLoginname(), money, accountName, accountType,
						accountCity,
						bankAddress, accountNo, bank, phone, email, ip, remark, null, null);

				// 记录提款后的本地和远程额度
				Double afterLocalCredit = -1.0, afterRemoteCredit = gameTolMoney, afterAgRemoteCredit = -1.0, afterAgInRemoteCredit = -1.0, afterBbinRemoteCredit = -1.0, afterKenoRemoteCredit = -1.0, afterSbRemoteAmount = -1.0, afterSkyRemoteAmount = -1.0;
				afterLocalCredit = user.getCredit();

				try {
					/*
					 * afterRemoteCredit =
					 * RemoteCaller.queryCredit(user.getLoginname()); String
					 * creditsbresult =
					 * RemoteCaller.querySBCredit(user.getLoginname()); String
					 * creditresult
					 * =RemoteCaller.queryDspCredit(user.getLoginname()); String
					 * creditAginresult
					 * =RemoteCaller.queryDspAginCredit(user.getLoginname());
					 * String
					 * creditBresult=RemoteCaller.queryBbinCredit(user.getLoginname
					 * ()); Double
					 * creditKresult=RemoteCaller.queryKenoCredit(user
					 * .getLoginname());
					 */
					// 获取pt远程额度
					/*
					 * try { String loginString =
					 * SkyUtils.getSkyMonery(user.getId()); JSONObject jsonObj =
					 * JSONObject.fromObject(loginString);
					 * if(!jsonObj.containsKey("balance")){ afterSkyRemoteAmount
					 * = -1.0; }else{ afterSkyRemoteAmount =
					 * jsonObj.getDouble("balance"); } } catch (Exception e) {
					 * e.printStackTrace(); }
					 */

					// System.out.println("agcredit "+creditresult);
					/*
					 * if(creditresult.matches("\\d++\\.\\d++|\\d++")){
					 * afterAgRemoteCredit=Double.parseDouble(creditresult); }
					 * if(creditsbresult.matches("\\d++\\.\\d++|\\d++")){
					 * afterSbRemoteAmount=Double.parseDouble(creditsbresult); }
					 * if(creditAginresult.matches("\\d++\\.\\d++|\\d++")){
					 * afterAgInRemoteCredit
					 * =Double.parseDouble(creditAginresult); }
					 * if(creditBresult.matches("\\d++\\.\\d++|\\d++")){
					 * afterBbinRemoteCredit=Double.parseDouble(creditBresult);
					 * } afterKenoRemoteCredit=creditKresult;
					 */
				} catch (Exception e) {
					e.printStackTrace();
					/*
					 * try { afterRemoteCredit =
					 * RemoteCaller.queryCredit(user.getLoginname()); } catch
					 * (Exception e1) { e1.printStackTrace();
					 * //如果没查到远程额度，标记为-1.0 afterRemoteCredit=-1.0; }
					 */
				}

				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CASHOUT.getCode(),
						user.getLevel(), loginname,
						money, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, remark, null,
						afterLocalCredit == null ? -1.0 : afterLocalCredit, afterRemoteCredit == null ? -1.0
								: afterRemoteCredit);
				proposal.setBankname(bank);
				proposal.setAfterAgRemoteAmount(afterAgRemoteCredit);
				proposal.setAfterAgInRemoteAmount(afterAgInRemoteCredit);
				proposal.setAfterBbinRemoteAmount(afterBbinRemoteCredit);
				proposal.setAfterKenoRemoteAmount(afterKenoRemoteCredit);
				proposal.setAfterSbRemoteAmount(afterSbRemoteAmount);
				if (afterSkyRemoteAmount == null) {
					proposal.setAfterSkyRemoteAmount(-1.0);
				} else {
					proposal.setAfterSkyRemoteAmount(afterSkyRemoteAmount / 100);
				}
				save(cashout);
				if (msflag != null && msflag.equals("1")) {
					proposal.setMsflag(1);
				}
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
	public String addCashoutNew(String proposer, String loginname, String pwd, String title, String from, Double money,
			String accountName,
			String accountNo, String accountType, String bank, String accountCity, String bankAddress, String email,
			String phone,
			String ip, String remark, String msflag,Double gameTolMoney) throws GenericDfhRuntimeException {

		money = Math.abs(money);
		log.info("add addCashoutNew proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getPassword().equalsIgnoreCase(EncryptionUtil.encryptPassword(pwd))) {
			msg = "密码错误";
			return msg;
		}
		if (user.getCredit() < money) {
			msg = "用户额度不足,无法申请提款";
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
		Double splitAmount = Double.valueOf(Configuration.getInstance().getValue("CashoutSplitAmount")); // 拆分基数

		if (proposalDao.existNotMsbankAuditedProposal(loginname, ProposalType.CASHOUT)) {
			msg = "抱歉，你之前的秒付提款尚未审核完，暂时不能使用秒付提款";
			return msg;
		}
			
		List<Proposal> list = proposalDao.getCashoutToday(loginname);
		if (list.size() >= Integer.parseInt(Configuration.getInstance().getValue("CashoutTodayCount"))) {
			msg = "抱歉，提款失败！\\n每天只可以提款【" + Configuration.getInstance().getValue("CashoutTodayCount") + "】次";
			return msg;
		}

		double oldCashoutAmount = 0d;
		for (int i = 0; i < list.size(); i++) {
			Proposal proposal = list.get(i);
			oldCashoutAmount += proposal.getAmount();
		}

		if ((oldCashoutAmount + money) > Double.parseDouble(Configuration.getInstance().getValue("CashoutTodayAmount"))) {
			msg = "抱歉，提款失败！\\n"
					+
					"每天最大提款额度为【"
					+ Configuration.getInstance().getValue("CashoutTodayAmount")
					+ "】元\\n"
					+
					"您已经提款【"
					+ oldCashoutAmount
					+ "】元，还可以提款【"
					+ String.valueOf(Double.parseDouble(Configuration.getInstance().getValue("CashoutTodayAmount"))
							- oldCashoutAmount) + "】元";
			return msg;
		}
		if (msg == null)
			try {
				Integer isBigAmount = 0;
				if (money > splitAmount && money <= 200000.0) {
					isBigAmount = 1;
				}
				Double surplus = money % splitAmount;
				int times = new Double((money / splitAmount)).intValue();
				String markStr = remark+";总共:" + money + "元分成" + ((surplus == 0) ? times : (times + 1));
				log.info(loginname + "要提款的总数是：" + money + "分成" + ((surplus == 0) ? times : (times + 1)));
				Date now = DateUtil.now();
				if (surplus == 0) {
					for (int i = 0; i < times; i++) {
						log.info("2:" + splitAmount);
						insertNeedPorposal(isBigAmount, now, loginname, proposer, from, msflag, splitAmount, user,
								accountName, bank, accountType, accountCity, bankAddress, accountNo, phone, email, ip,
								markStr,gameTolMoney);
					}
				} else {
					times = times + 1;
					for (int i = 0; i < times; i++) {
						if (i == times - 1) {
							log.info("3:" + surplus);
							insertNeedPorposal(isBigAmount, now, loginname, proposer, from, msflag, surplus, user,
									accountName, bank, accountType, accountCity, bankAddress, accountNo, phone, email, ip,
									markStr,gameTolMoney);
						} else {
							log.info("4:" + splitAmount);
							insertNeedPorposal(isBigAmount, now, loginname, proposer, from, msflag, splitAmount, user,
									accountName, bank, accountType, accountCity, bankAddress, accountNo, phone, email, ip,
									markStr,gameTolMoney);
						}
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	public void insertNeedPorposal(Integer isBigAmount, Date now, String loginname, String proposer, String from,
			String msflag, Double money, Users user, String accountName, String bank, String accountType,
			String accountCity, String bankAddress, String accountNo, String phone, String email, String ip, String remark,Double gameTolMoney) {

		String pno = seqDao.generateProposalPno(ProposalType.CASHOUT);
		tradeDao.changeCredit(loginname, money * -1, CreditChangeType.CASHOUT.getCode(), pno, null);
		Cashout cashout = new Cashout(pno, user.getRole(), user.getLoginname(), money, accountName, accountType,
				accountCity,
				bankAddress, accountNo, bank, phone, email, ip, remark, null, null);

		// 记录提款后的本地和远程额度
		Double afterLocalCredit = -1.0, afterRemoteCredit = gameTolMoney, afterAgRemoteCredit = -1.0, afterAgInRemoteCredit = -1.0, afterBbinRemoteCredit = -1.0, afterKenoRemoteCredit = -1.0, afterSbRemoteAmount = -1.0, afterSkyRemoteAmount = -1.0;
		afterLocalCredit = user.getCredit();
		Proposal proposal = null;
		proposal = new Proposal(pno, proposer, now, ProposalType.CASHOUT.getCode(), user.getLevel(), loginname,
				money, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, remark, null,
				afterLocalCredit == null ? -1.0 : afterLocalCredit, afterRemoteCredit == null ? -1.0 : afterRemoteCredit);

		proposal.setBankname(bank);
		proposal.setAfterAgRemoteAmount(afterAgRemoteCredit);
		proposal.setAfterAgInRemoteAmount(afterAgInRemoteCredit);
		proposal.setAfterBbinRemoteAmount(afterBbinRemoteCredit);
		proposal.setAfterKenoRemoteAmount(afterKenoRemoteCredit);
		proposal.setAfterSbRemoteAmount(afterSbRemoteAmount);
		proposal.setAfterSkyRemoteAmount(isBigAmount.doubleValue());
		save(cashout);
		if (msflag != null && msflag.equals("1")) {
			proposal.setMsflag(1);
		}
		save(proposal);
		taskDao.generateTasks(pno, proposer);
	}

	/*
	 * 查找自本次提款的上次提款的时刻额度和时间
	 */
	public Proposal getLastSuccCashout(String loginname, Date before) {

		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class).add(Restrictions.eq("loginname", loginname)).add(
				Restrictions.eq("type", ProposalType.CASHOUT.getCode()))
				.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode())).add(
						Restrictions.lt("createTime", before)).addOrder(Order.desc("createTime"));
		List<Proposal> list = findByCriteria(dc, 0, 1);
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public Integer totalProposals(Date starttime, Date endtime, String loginname,
			Integer type, String username) {

		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select count(*) from proposal ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = proposal.loginname ");
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '" + formatStr2 + "' ");
			}
			if (type != 0) {
				sql.append(" and  proposal.type=" + type + "  ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and proposal.loginname like '%" + username + "%' ");
			}

			// List numbers =
			// proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getOneInteger(sql.toString());
			// return ((BigInteger)numbers.get(0)).intValue();
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
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '" + formatStr2 + "' ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and payorder.loginname like '%" + username + "%' ");
			}
			// List numbers =
			// proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getOneInteger(sql.toString());
			// return ((BigInteger)numbers.get(0)).intValue();
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
			sql.append(" where users.agent = '" + loginname + "' ");
			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '" + formatStr2 + "' ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and proposal.loginname like '%" + username + "%' ");
			}

			sql.append(" union all select count(*) from payorder ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = payorder.loginname ");
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '" + formatStr2 + "' ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and payorder.loginname like '%" + username + "%' ");
			}
			// List numbers =
			// proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getTwoInteger(sql.toString());
			// return
			// (((BigInteger)numbers.get(0)).intValue()+((BigInteger)numbers.get(1)).intValue());
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
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '" + formatStr2 + "' ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and payorder.loginname like '%" + username + "%' ");
			}
			sql.append(" order by payorder.createTime ");
			int offset = (pageno - 1) * length;
			// List list =
			// proposalDao.getSessionFactory().openSession().createSQLQuery(sql.toString()).setFirstResult(offset).setMaxResults(length).list();
			List list = agentDao.getList(sql.toString(), offset, length);
			if (list == null) {
				return null;
			}
			else {
				List<Proposal> proposals = new ArrayList<Proposal>();
				for (int i = 0; i < list.size(); i++) {
					Object[] o = (Object[]) list.get(i);
					Proposal proposal = new Proposal();
					proposal.setLoginname((String) o[0]);
					proposal.setCreateTime((Timestamp) o[1]);
					proposal.setAmount((Double) o[2]);
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
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '" + formatStr2 + "' ");
			}
			if (type != 0) {
				sql.append(" and  proposal.type=" + type + "  ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and proposal.loginname like '%" + username + "%' ");
			}
			sql.append(" order by proposal.createTime ");
			int offset = (pageno - 1) * length;
			// List list =
			// proposalDao.getSessionFactory().openSession().createSQLQuery(sql.toString()).setFirstResult(offset).setMaxResults(length).list();
			List list = agentDao.getList(sql.toString(), offset, length);
			if (list == null) {
				return null;
			}
			else {
				List<Proposal> proposals = new ArrayList<Proposal>();
				for (int i = 0; i < list.size(); i++) {
					Object[] o = (Object[]) list.get(i);
					Proposal proposal = new Proposal();
					proposal.setLoginname((String) o[0]);
					proposal.setCreateTime((Timestamp) o[1]);
					proposal.setAmount((Double) o[2]);
					proposal.setType((Integer) o[3]);
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
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '" + formatStr2 + "' ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and proposal.loginname like '%" + username + "%' ");
			}

			sql.append("  union all select payorder.loginname,payorder.createTime,payorder.money,newaccount from payorder ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = payorder.loginname ");
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '" + formatStr2 + "' ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and payorder.loginname like '%" + username + "%' ");
			}
			sql.append("  order by createTime desc ");
			int offset = (pageno - 1) * length;
			// List list =
			// proposalDao.getSessionFactory().openSession().createSQLQuery(sql.toString()).setFirstResult(offset).setMaxResults(length).list();
			List list = agentDao.getList(sql.toString(), offset, length);
			if (list == null) {
				return null;
			} else {
				List<Proposal> proposals = new ArrayList<Proposal>();
				for (int i = 0; i < list.size(); i++) {
					Object[] o = (Object[]) list.get(i);
					Proposal proposal = new Proposal();
					proposal.setLoginname((String) o[0]);
					proposal.setCreateTime((Timestamp) o[1]);
					proposal.setAmount((Double) o[2]);
					proposal.setType((Integer) o[3]);
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
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '" + formatStr2 + "' ");
			}
			if (type != 0) {
				sql.append(" and  proposal.type=" + type + "  ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and proposal.loginname like '%" + username + "%' ");
			}
			// List numbers =
			// proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getOneDouble(sql.toString());
			// return(Double)numbers.get(0);
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
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '" + formatStr2 + "' ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and payorder.loginname like '%" + username + "%' ");
			}
			// List numbers =
			// proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getOneDouble(sql.toString());
			// return(Double)numbers.get(0);
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
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and proposal.createTime < '" + formatStr2 + "' ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and proposal.loginname like '%" + username + "%' ");
			}

			sql.append(" union all select sum(payorder.money) from payorder ");
			sql.append(" left join  users");
			sql.append(" on users.loginname = payorder.loginname ");
			sql.append(" where users.agent = '" + loginname + "' ");

			if (starttime != null) {
				String startstr = starttime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(startstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime > '" + formatStr2 + "' ");
			}
			if (endtime != null) {
				String endstr = endtime.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
				Date date = (Date) sdf.parse(endstr);
				String formatStr2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
				sql.append(" and payorder.createTime < '" + formatStr2 + "' ");
			}
			if (username != null && !username.equals("")) {
				sql.append(" and payorder.loginname like '%" + username + "%' ");
			}
			// List numbers =
			// proposalDao.getHibernateTemplate().getSessionFactory().openSession().createSQLQuery(sql.toString()).list();
			return agentDao.getTwoDouble(sql.toString());
			// Double double1 = (Double)numbers.get(0);
			// if(double1 == null){
			// double1 = 0.0;
			// }
			// Double double2 = (Double)numbers.get(1);
			// if(double2 == null){
			// double2 = 0.0;
			// }
			// return double1+double2;
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	public static void main(String[] args) {

		String str = "0";
		if (str.matches("\\d++\\.\\d++|\\d++")) {
			System.out.println("ss");
		}
	}

	@Override
	public String excuteWeekSent(String pno, String ip, String remark, String target) {

		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "非法操作：不存在该记录";
		else if (proposal.getFlag().intValue() != ProposalFlagType.AUDITED.getCode().intValue()) {
			msg = "非法操作";
		} else {
			Users customer;
			WeekSent weeksent;
			String zzID = "转账NEWPT";
			String platform = "pttiger";
			if (proposal.getType().intValue() == ProposalType.WEEKSENT.getCode().intValue()) {
				try {
					// 操作业务
					customer = transferDao.getUsers(proposal.getLoginname());
					if (customer.getFlag() == 1) {
						log.info("该账号已经禁用" + proposal.getLoginname());
						return "该账号已经禁用";
					}

					log.info("领取周周回馈:" + proposal.getLoginname());
					// 判断转账是否关闭
					if (target.equalsIgnoreCase("ttg")) {
						zzID = "转账TTG";
						platform = "ttg";
					} else if (target.equalsIgnoreCase("gpi")) {
						zzID = "转账GPI";
						platform = "gpi";
					} else if (target.equalsIgnoreCase("nt")) {
						zzID = "转账nt";
						platform = "nt";
					}else if(target.equalsIgnoreCase("qt")){
						zzID = "转账QT";
						platform = "qt";
					}
					Const constPt = transferDao.getConsts(zzID);
					if (constPt == null) {
						log.info("平台不存在" + proposal.getLoginname());
						return "平台不存在";
					}
					if (constPt.getValue().equals("0")) {
						log.info(zzID + "正在维护" + proposal.getLoginname());
						return "转账正在维护";
					}

					Double ptBalance = null;
					if (platform.equalsIgnoreCase("pttiger")) {
						ptBalance = PtUtil.getPlayerMoney(proposal.getLoginname());
					} else if (platform.equalsIgnoreCase("ttg")) {
						String ttgBalance = PtUtil1.getPlayerAccount(proposal.getLoginname());
						if (ttgBalance != null)
							ptBalance = Double.parseDouble(ttgBalance);
					} else if (platform.equalsIgnoreCase("gpi")) {
						ptBalance = GPIUtil.getBalance(proposal.getLoginname());
					} else if (platform.equalsIgnoreCase("nt")) {
						JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(proposal.getLoginname()));
						ptBalance = ntm.getBoolean("result") ? ntm.getDouble("balance") : null;
					}else if(platform.equalsIgnoreCase("qt")){
						String qtm = QtUtil.getBalance(proposal.getLoginname());
						ptBalance = NumberUtils.isNumber(qtm)?Double.parseDouble(qtm):null;
					}

					if (ptBalance == null) {
						log.info(proposal.getLoginname() + "获取额度超时!系统繁忙!");
						return "系统繁忙!请稍后再试";
					}
					if (ptBalance > 5) {
						log.info(proposal.getLoginname() + " 账户余额超过5元，暂时不能领取周周回馈!" + ptBalance);
						return "游戏账户余额超过5元，暂时不能领取周周回馈赠送金额，请继续游戏或将游戏账户余额转出!";
					}

					weeksent = (WeekSent) get(WeekSent.class, pno);
					// 判断玩家是否存在早期派发且未领取的周周回馈，如果有玩家需要先处理
					DetachedCriteria c = DetachedCriteria.forClass(WeekSent.class);
					c.add(Restrictions.eq("username", proposal.getLoginname()));
					c.add(Restrictions.eq("status", "0"));
					c.add(Restrictions.lt("createTime", weeksent.getCreateTime()));
					int unTakenCount = this.proposalDao.findByCriteria(c).size();
					if (unTakenCount > 0) {
						return "请按时间顺序依次领取";
					}

					// 将之前的已领取反赠，更新为已处理
					DetachedCriteria dc = DetachedCriteria.forClass(WeekSent.class);
					dc.add(Restrictions.eq("username", proposal.getLoginname()));
					dc.add(Restrictions.eq("status", "1"));
					dc.add(Restrictions.lt("createTime", weeksent.getCreateTime()));
					List<WeekSent> weekSentRecords = this.proposalDao.findByCriteria(dc);
					for (WeekSent ws : weekSentRecords) {
						ws.setStatus("2");
						update(ws);
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info(proposal.getLoginname() + "获取额度超时!系统繁忙!" + e.toString());
					return "系统繁忙!请稍后再试";
				}
				// 处理提案
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				proposal.setExecuteTime(new Date());
				proposal.setRemark(proposal.getRemark() + ";executed:" + StringUtils.trimToEmpty(remark));
				// taskDao.excuteTask(pno, proposal.getLoginname(), ip);
				logDao.insertOperationLog(proposal.getLoginname(), OperationLogType.EXCUTE, "ip:" + ip + ";pno:" + pno);
				// 获取0点到领取时的老虎机投注额
				String totalBetSql = "select bet from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("username", proposal.getLoginname());
				Calendar cd = Calendar.getInstance();
				cd.setTime(new Date());
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				prams.put("startTime", cd.getTime());
				if (target.equalsIgnoreCase("gpi")) {
					totalBetSql = "select sum(bet) from platform_data where loginname=:username and starttime>=:startTime and platform in(:gpi, :rslot, :png, :bs, :ctxm)";
					prams.put("gpi", "gpi");
					prams.put("rslot", "rslot");
					prams.put("png", "png");
					prams.put("bs", "bs");
					prams.put("ctxm", "ctxm");
				} else if (target.equalsIgnoreCase("nt")) {
					totalBetSql = "select sum(betCredit) from ptprofit where loginname=:username and starttime>=:startTime";
				} else {
					prams.put("platform", platform);
				}
				weeksent.setBetting(transferDao.getDoubleValueBySql(totalBetSql, prams));
				weeksent.setStatus("1");
				weeksent.setGetTime(new Date());
				weeksent.setPlatform(platform);
				update(weeksent);
				// 转账记录
				String seqId = seqService.generateTransferID();
				logDao.insertCreditLog(proposal.getLoginname(), CreditChangeType.WEEKSENT.getCode(), customer.getCredit(),
						-proposal.getAmount(), customer.getCredit(), "referenceNo:" + seqId + ";周周回馈，" + weeksent.getTimes()
								+ "倍流水;" + StringUtils.trimToEmpty(remark));
				// 转入游戏账户
				String targetPlatform = null;
				Boolean deposit = false;
				if (platform.equalsIgnoreCase("pttiger")) {
					deposit = PtUtil.getDepositPlayerMoney(proposal.getLoginname(), proposal.getAmount());
					targetPlatform = RemoteConstant.PAGESITENEWPT;
				} else if (platform.equalsIgnoreCase("ttg")) {
					deposit = PtUtil1.addPlayerAccountPraper(proposal.getLoginname(), proposal.getAmount());
					targetPlatform = RemoteConstant.PAGESITETT;
				} else if (platform.equalsIgnoreCase("gpi")) {
					String resultCode = GPIUtil.credit(proposal.getLoginname(), proposal.getAmount(),
							seqService.generateTransferID());
					if (resultCode != null && resultCode.equals(GPIUtil.GPI_SUCCESS_CODE)) {
						deposit = true;
					}
					targetPlatform = RemoteConstant.PAGESITEGPI;
				} else if (platform.equalsIgnoreCase("nt")) {
					JSONObject cm = JSONObject
							.fromObject(NTUtils.changeMoney(proposal.getLoginname(), proposal.getAmount()));
					deposit = cm.getBoolean("result");
					targetPlatform = RemoteConstant.PAGESITENT;
				}else if(platform.equalsIgnoreCase("qt")){
					String rtnStr = QtUtil.getDepositPlayerMoney(proposal.getLoginname(), proposal.getAmount(), seqId);
					if(rtnStr != null && QtUtil.RESULT_SUCC.equals(rtnStr)){
						deposit = true;
					}
					targetPlatform = RemoteConstant.PAGESITEQT;
				}

				if (null != deposit && deposit) {
					Transfer transfer = new Transfer();
					transfer.setSource(RemoteConstant.WEBSITE);
					transfer.setTarget(targetPlatform);
					transfer.setNewCredit(customer.getCredit());
					transfer.setId(Long.parseLong(seqId));
					transfer.setLoginname(customer.getLoginname());
					transfer.setCredit(customer.getCredit());
					transfer.setRemit(proposal.getAmount());
					transfer.setCreatetime(DateUtil.getCurrentTimestamp());
					transfer.setFlag(Constants.FLAG_TRUE);
					transfer.setRemark("转入成功");
					save(transfer);
				} else {
					msg = "转账失败";
					throw new GenericDfhRuntimeException("转账失败");
				}
				// String msg =
				// transferService.transferPtAndSelfYouHuiInIn(seqId, loginname,
				// remit, remark);
				msg = null;
			}
		}
		return msg;

	}

	@Override
	public String cancelLosePromo(String pno, String ip, String remark) {

		String msg = null;
		Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "找不到该提案";
		else if (proposal.getFlag().intValue() == ProposalFlagType.CANCLED.getCode().intValue()
				|| proposal.getFlag().intValue() == ProposalFlagType.EXCUTED.getCode().intValue())// update
																									// sun
			msg = "该提案已取消或已执行";
		else {
			if (proposal.getType().equals(ProposalType.PROFIT.getCode())) {
				proposal.setFlag(ProposalFlagType.CANCLED.getCode());
				proposal.setRemark(proposal.getRemark() + ";取消:" + StringUtils.trimToEmpty(remark));
				// taskDao.cancleTask(pno, proposal.getLoginname(), ip);

				LosePromo losePromo = (LosePromo) get(LosePromo.class, pno);
				losePromo.setStatus("3");
				update(losePromo);
				msg = null;
			}
		}
		return msg;
	}

	@Override
	public String excuteLosePromo(Proposal proposal, String ip, String remark, String target, String seqId) {
		String msg = null;
		//Proposal proposal = (Proposal) get(Proposal.class, pno, LockMode.UPGRADE);
		if (proposal == null)
			msg = "非法操作：不存在该记录";
		else if (proposal.getFlag().intValue() != ProposalFlagType.AUDITED.getCode().intValue()) {
			msg = "非法操作";
		} else {
			Users customer;
			LosePromo losePromo;
			String zzID = "转账NEWPT";
			String platform = "pttiger";
			if (proposal.getType().intValue() == ProposalType.PROFIT.getCode().intValue()) {
				try {
					// 操作业务
					customer = transferDao.getUsers(proposal.getLoginname());
					if (customer.getFlag() == 1) {
						log.info("该账号已经禁用" + proposal.getLoginname());
						return "该账号已经禁用";
					}
					String password = customer.getPassword();
					log.info("领取负盈利反赠:" + proposal.getLoginname());
					// 判断转账是否关闭
					if (target.equalsIgnoreCase("ttg")) {
						zzID = "转账TTG";
						platform = "ttg";
					} else if (target.equalsIgnoreCase("gpi")) {
						zzID = "转账GPI";
						platform = "gpi";
					} else if (target.equalsIgnoreCase("nt")) {
						zzID = "转账NT";
						platform = "nt";
					}else if(target.equalsIgnoreCase("qt")){
						zzID = "转账QT";
						platform = "qt";
					}else if(target.equalsIgnoreCase("MG")){
						zzID = "转账MG";
						platform = "mg";
					}else if(target.equalsIgnoreCase("DT")){
						zzID = "转账DT";
						platform = "dt";
					}else if(target.equalsIgnoreCase("slot")){
						zzID = "转账SLOT";
						platform = "slot";
					}else if(target.equalsIgnoreCase("png")){
						zzID = "转账PNG";
						platform = "png";
					}else if(target.equalsIgnoreCase("slot")){
						zzID = "转账SLOT";
						platform = "slot";
					 }
					
					
					Const constPt = transferDao.getConsts(zzID);
					if (constPt == null) {
						log.info("平台不存在" + proposal.getLoginname());
						return "平台不存在";
					}
					if (constPt.getValue().equals("0")) {
						log.info(zzID + "正在维护" + proposal.getLoginname());
						return "转账正在维护";
					}

					Double ptBalance = null;
					if (platform.equalsIgnoreCase("pttiger")) {
						ptBalance = PtUtil.getPlayerMoney(proposal.getLoginname());
					} else if (platform.equalsIgnoreCase("ttg")) {
						String ttgBalance = PtUtil1.getPlayerAccount(proposal.getLoginname());
						if (ttgBalance != null)
							ptBalance = Double.parseDouble(ttgBalance);
					} else if (platform.equalsIgnoreCase("gpi")) {
						ptBalance = GPIUtil.getBalance(proposal.getLoginname());
					} else if (platform.equalsIgnoreCase("nt")) {
						JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(proposal.getLoginname()));
						ptBalance = ntm.getBoolean("result") ? ntm.getDouble("balance") : null;
					}else if(platform.equalsIgnoreCase("qt")){
						String qtm = QtUtil.getBalance(proposal.getLoginname());
						ptBalance = NumberUtils.isNumber(qtm)?Double.parseDouble(qtm):null;
					}else if(platform.equalsIgnoreCase("mg")){
						//ptBalance= MGSUtil.getBalance(proposal.getLoginname(),password);
						ptBalance= MGSUtil.getBalance(proposal.getLoginname());
					}else if(platform.equalsIgnoreCase("dt")){
						String qtm = DtUtil.getamount(proposal.getLoginname());
						ptBalance = NumberUtils.isNumber(qtm)?Double.parseDouble(qtm):null;
					}else if(platform.equalsIgnoreCase("png")){
						ptBalance = PNGUtil.getBalance(proposal.getLoginname());
					}else if(platform.equalsIgnoreCase("slot")){
						//ptBalance = SlotUtil.getBalance(proposal.getLoginname(),"");
					}

					if (ptBalance == null) {
						log.info(proposal.getLoginname() + "获取额度超时!系统繁忙!");
						return "系统繁忙!请稍后再试";
					}
					if (ptBalance >= 5) {
						log.info(proposal.getLoginname() + " 账户余额超过5元，暂时不能领取负盈利优惠!" + ptBalance);
						return "游戏账户余额超过5元，暂时不能领取救援金!";
					}

					losePromo = (LosePromo) get(LosePromo.class, proposal.getPno());
					/*// 判断玩家是否存在早期派发且未领取的负盈利反赠，如果有玩家需要先处理
					DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
					c.add(Restrictions.eq("username", proposal.getLoginname()));
					c.add(Restrictions.eq("platform", "pttiger")); // 派发时默认指定为pttiger
					c.add(Restrictions.eq("status", "0"));
					c.add(Restrictions.lt("createTime", losePromo.getCreateTime()));
					int unTakenCount = this.proposalDao.findByCriteria(c).size();
					if (unTakenCount > 0) {
						return "请按时间顺序依次领取处理您的救援金";
					}*/

					// 将之前的已领取反赠，更新为已处理
					DetachedCriteria dc = DetachedCriteria.forClass(LosePromo.class);
					dc.add(Restrictions.eq("username", proposal.getLoginname()));
					dc.add(Restrictions.eq("platform", platform)); // 只处理本次选择游戏平台的救援金
					dc.add(Restrictions.eq("status", "1"));
					//dc.add(Restrictions.lt("createTime", losePromo.getCreateTime()));
					List<LosePromo> losePromoRecords = this.proposalDao.findByCriteria(dc);
					for (LosePromo lp : losePromoRecords) {
						lp.setStatus("2");
						update(lp);
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info(proposal.getLoginname() + "获取额度超时!系统繁忙!" + e.toString());
					return "系统繁忙!请稍后再试";
				}
				// 处理提案
				proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
				proposal.setExecuteTime(new Date());
				proposal.setRemark(proposal.getRemark() + ";executed:" + StringUtils.trimToEmpty(remark));
				update(proposal);
				// taskDao.excuteTask(pno, proposal.getLoginname(), ip);
				logDao.insertOperationLog(proposal.getLoginname(), OperationLogType.EXCUTE, "ip:" + ip + ";pno:" + proposal.getPno());
				// 获取0点到领取时的老虎机投注额
				String totalBetSql = "select bet from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("username", proposal.getLoginname());
				Calendar cd = Calendar.getInstance();
				cd.setTime(new Date());
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				prams.put("startTime", cd.getTime());
				if (target.equalsIgnoreCase("gpi")) {
					totalBetSql = "select sum(bet) from platform_data where loginname=:username and starttime>=:startTime and platform in(:gpi, :rslot, :png, :bs, :ctxm)";
					prams.put("gpi", "gpi");
					prams.put("rslot", "rslot");
					prams.put("png", "png");
					prams.put("bs", "bs");
					prams.put("ctxm", "ctxm");
				} else {
					prams.put("platform", platform);
				}
				losePromo.setBetting(transferDao.getDoubleValueBySql(totalBetSql, prams));
				losePromo.setStatus("1");
				losePromo.setGetTime(new Date());
				losePromo.setPlatform(platform);
				update(losePromo);
				// 转账记录
				//String seqId = seqService.generateTransferID();
				logDao.insertCreditLog(proposal.getLoginname(), CreditChangeType.PROFIT.getCode(), customer.getCredit(), -proposal.getAmount(), customer.getCredit(), "referenceNo:" + seqId
						+ ";负盈利反赠，" + losePromo.getTimes() + "倍流水;" + StringUtils.trimToEmpty(remark));
				// 转入游戏账户
				String targetPlatform = null;
//				Boolean deposit = false;
				if (platform.equalsIgnoreCase("pttiger")) {
					//deposit = PtUtil.getDepositPlayerMoney(proposal.getLoginname(), proposal.getAmount());
					targetPlatform = RemoteConstant.PAGESITENEWPT;
				} else if (platform.equalsIgnoreCase("ttg")) {
					//deposit = PtUtil1.addPlayerAccountPraper(proposal.getLoginname(), proposal.getAmount());
					targetPlatform = RemoteConstant.PAGESITETT;
				} else if (platform.equalsIgnoreCase("gpi")) {
					/*String resultCode = GPIUtil.credit(proposal.getLoginname(), proposal.getAmount(), seqService.generateTransferID());
					if (resultCode != null && resultCode.equals(GPIUtil.GPI_SUCCESS_CODE)) {
						deposit = true;
					}*/
					targetPlatform = RemoteConstant.PAGESITEGPI;
				} else if (platform.equalsIgnoreCase("nt")) {
					/*JSONObject ntm = JSONObject.fromObject(NTUtils.changeMoney(proposal.getLoginname(), proposal.getAmount()));
					deposit = ntm.getBoolean("result") ? true : null;*/
					targetPlatform = RemoteConstant.PAGESITENT;
				}else if(platform.equalsIgnoreCase("qt")){
					/*String rtnStr = QtUtil.getDepositPlayerMoney(proposal.getLoginname(), proposal.getAmount(), seqId);
					if(rtnStr != null && QtUtil.RESULT_SUCC.equals(rtnStr)){
						deposit = true;
					}*/
					targetPlatform = RemoteConstant.PAGESITEQT;
				}else if(platform.equalsIgnoreCase("mg")){
					targetPlatform = "mg";
				}else if(platform.equalsIgnoreCase("dt")){
					targetPlatform = RemoteConstant.PAGESITEDT;
				}else if(platform.equalsIgnoreCase("png")){
					targetPlatform = RemoteConstant.PAGESITEPNG;
				}else if(target.equalsIgnoreCase("slot")){
					targetPlatform = RemoteConstant.PAGESITESLOT;
				}

				Transfer transfer = new Transfer();
				transfer.setSource(RemoteConstant.WEBSITE);
				transfer.setTarget(targetPlatform);
				transfer.setNewCredit(customer.getCredit());
				transfer.setId(Long.parseLong(seqId));
				transfer.setLoginname(customer.getLoginname());
				transfer.setCredit(customer.getCredit());
				transfer.setRemit(proposal.getAmount());
				transfer.setCreatetime(DateUtil.getCurrentTimestamp());
				transfer.setFlag(Constants.FLAG_TRUE);
				transfer.setRemark("转入成功");
				save(transfer);
				
				msg = null;
			}
		}
		return msg;

	}

	@SuppressWarnings("unchecked")
	@Override
	public String drawPTBigBangBonus(Integer id, String ip) {

		DetachedCriteria dc = DetachedCriteria.forClass(PTBigBang.class);
		dc.add(Restrictions.eq("id", id));
		dc.add(Restrictions.eq("status", "1")); // 已派发
		// 24小时内
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		dc = dc.add(Restrictions.ge("distributeTime", calendar.getTime()));

		// PTBigBang ptBigBang = (PTBigBang) get(PTBigBang.class, id);lxr779988
		List<PTBigBang> bigBangList = proposalDao.findByCriteria(dc);

		if (bigBangList != null && bigBangList.size() > 0) {
			Users user;
			PTBigBang ptBigBang = bigBangList.get(0);
			try {
				log.info("PT疯狂礼金开始转入NEWPT准备工作:" + ptBigBang.getUsername());
				// 判断转账是否关闭
				Const constPt = transferDao.getConsts("转账NEWPT");
				if (constPt == null) {
					log.info("平台不存在");
					return "平台不存在";
				}
				if (constPt.getValue().equals("0")) {
					log.info("NEWPT转账正在维护");
					return "PT转账正在维护";
				}
				// 操作业务
				user = transferDao.getUsers(ptBigBang.getUsername());
				if (user.getFlag() == 1) {
					log.info("该账号已经禁用" + ptBigBang.getUsername());
					return "该账号已经禁用";
				}

				Double ptBalance = PtUtil.getPlayerMoney(ptBigBang.getUsername());
				if (ptBalance == null) {
					log.info("获取额度超时!系统繁忙!");
					return "系统繁忙!请稍后再试";
				}
				if (ptBalance >= 5) {
					log.info(ptBigBang.getUsername() + " PT账户余额超过5元，暂时不能领取PT疯狂礼金!" + ptBalance);
					return "PT账户余额超过5元，暂时不能领取PT疯狂礼金!";
				}

				// 将之前的领取的，更新为已处理
				dc = DetachedCriteria.forClass(PTBigBang.class);
				dc.add(Restrictions.eq("username", ptBigBang.getUsername()));
				dc.add(Restrictions.eq("status", "2")); // 领取状态
				dc.add(Restrictions.lt("distributeTime", ptBigBang.getDistributeTime()));
				List<PTBigBang> ptBigBangRecords = this.proposalDao.findByCriteria(dc);
				for (PTBigBang item : ptBigBangRecords) {
					item.setStatus("3"); // 已处理状态
					update(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("PT疯狂礼金转入PT：" + e.toString());
				return "系统繁忙!请稍后再试";
			}
			// 获取0点到领取时的老虎机投注额
			String totalBetSql = "select bet from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", ptBigBang.getUsername());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			prams.put("platform", "pttiger");

			ptBigBang.setBetAmount(transferDao.getDoubleValueBySql(totalBetSql, prams));

			ptBigBang.setStatus("2");
			ptBigBang.setGetTime(new Date());

			// 转入PT账户
			Boolean deposit = PtUtil.getDepositPlayerMoney(ptBigBang.getUsername(), ptBigBang.getGiftMoney());
			if (null != deposit && deposit) {
				// 提案记录
				String pno = this.getSeqDao().generateProposalPno(ProposalType.PTBIGBANG);
				ptBigBang.setRemark(pno);
				update(ptBigBang);
				Proposal proposal = new Proposal(pno, ptBigBang.getUsername(), DateUtil.now(),
						ProposalType.PTBIGBANG.getCode(), user.getLevel(), user.getLoginname(), ptBigBang.getGiftMoney(),
						user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "领取PT疯狂礼金" + ip, "");
				proposal.setExecuteTime(DateUtil.now());
				save(proposal);
				// 转账记录
				String seqId = seqService.generateTransferID();
				Transfer transfer = new Transfer();
				transfer.setSource(RemoteConstant.WEBSITE);
				transfer.setTarget(RemoteConstant.PAGESITENEWPT);
				transfer.setNewCredit(user.getCredit());
				transfer.setId(Long.parseLong(seqId));
				transfer.setLoginname(user.getLoginname());
				transfer.setCredit(user.getCredit());
				transfer.setRemit(ptBigBang.getGiftMoney());
				transfer.setCreatetime(DateUtil.getCurrentTimestamp());
				transfer.setFlag(Constants.FLAG_TRUE);
				transfer.setRemark("转入成功");
				save(transfer);
				logDao.insertCreditLog(ptBigBang.getUsername(), CreditChangeType.TRANSFER_MEWPTIN.getCode(),
						user.getCredit(), -ptBigBang.getGiftMoney(), user.getCredit(), "referenceNo:" + seqId + ";PT大爆炸礼金，"
								+ ptBigBang.getTimes() + "倍流水;" + ip);
			} else {
				msg = "转账失败";
				throw new GenericDfhRuntimeException("转账失败");
			}
			msg = null;
		} else {
			return "非法操作，PT疯狂礼金不存在或已失效";
		}
		return msg;
	}

	/**
	 * 申请提款
	 */
	public String addCashoutForAgentSlot(String proposer, String loginname, String pwd, String title, String from,
			Double money, String accountName,
			String accountNo, String accountType, String bank, String accountCity, String bankAddress, String email,
			String phone,
			String ip, String remark, String msflag) throws GenericDfhRuntimeException {

		money = Math.abs(money);
		log.info("add addCashoutForAgentSlot proposal");
		String msg = null;
		Users user = (Users) userDao.get(Users.class, loginname, LockMode.UPGRADE);
		msg = userDao.checkUserForProposal(user);
		if (msg != null)
			return msg;
		if (!user.getPassword().equalsIgnoreCase(EncryptionUtil.encryptPassword(pwd))) {
			msg = "密码错误";
			return msg;
		}
		Userstatus agentAccount = (Userstatus) userDao.get(Userstatus.class, loginname, LockMode.UPGRADE);
		if (null == agentAccount || null == agentAccount.getSlotaccount() || agentAccount.getSlotaccount() < money) {
			msg = "代理额度不足,无法申请提款";
			return msg;
		}
//		int date = Calendar.getInstance().get(Calendar.DATE);
		Double requireAmount = 100.0;
//		if (date >= 1 && date <= 5) {
//			requireAmount = 100.0;
//		}
		if (agentAccount.getSlotaccount() < requireAmount) {
			msg = "抱歉：代理佣金低于" + requireAmount + "元，暂无法提款！";
			return msg;
		}
		// 提款必须大于100元
		if (money < 100.0) {
			msg = "100元以上才能申请提款";
			return msg;
		}
		// 提款必须小于190000元
		if (money > 190000000000.0) {
			msg = "190000000000元以下才能申请提款";
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

		if (msflag != null && msflag.equals("1")) {
			return "代理不能使用秒提";
		}
		List<Proposal> list = proposalDao.getCashoutToday(loginname);
		// if
		// (list.size()>=Integer.parseInt(Configuration.getInstance().getValue("CashoutTodayCount")))
		// {
		// msg =
		// "抱歉，提款失败！\\n每天只可以提款【"+Configuration.getInstance().getValue("CashoutTodayCount")+"】次";
		// return msg;
		// }

		double oldCashoutAmount = 0d;
		for (int i = 0; i < list.size(); i++) {
			Proposal proposal = list.get(i);
			oldCashoutAmount += proposal.getAmount();
		}

		if ((oldCashoutAmount + money) > Double.parseDouble(Configuration.getInstance().getValue("CashoutTodayAmount"))) {
			msg = "抱歉，提款失败！\\n"
					+
					"每天最大提款额度为【"
					+ Configuration.getInstance().getValue("CashoutTodayAmount")
					+ "】元\\n"
					+
					"您已经提款【"
					+ oldCashoutAmount
					+ "】元，还可以提款【"
					+ String.valueOf(Double.parseDouble(Configuration.getInstance().getValue("CashoutTodayAmount"))
							- oldCashoutAmount) + "】元";
			return msg;
		}
		if (msg == null)
			try {
				String pno = seqDao.generateProposalPno(ProposalType.CASHOUT);
				tradeDao.changeCreditForAgentSlot(loginname, money * -1, CreditChangeType.CASHOUT.getCode(), pno, null);
				Cashout cashout = new Cashout(pno, user.getRole(), user.getLoginname(), money, accountName, accountType,
						accountCity,
						bankAddress, accountNo, bank, phone, email, ip, remark, null, null);

				// 记录提款后的本地和远程额度
				Double afterLocalCredit = -1.0, afterRemoteCredit = -1.0, afterAgRemoteCredit = -1.0, afterAgInRemoteCredit = -1.0, afterBbinRemoteCredit = -1.0, afterKenoRemoteCredit = -1.0, afterSkyRemoteAmount = -1.0, afterSbRemoteAmount = -1.0, afterTyRemoteAmount = -1.0;

				Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.CASHOUT.getCode(),
						user.getLevel(), loginname,
						money, user.getAgent(), ProposalFlagType.SUBMITED.getCode(), from, remark, null,
						afterLocalCredit == null ? -1.0 : afterLocalCredit, afterRemoteCredit == null ? -1.0
								: afterRemoteCredit);
				proposal.setAfterAgRemoteAmount(afterAgRemoteCredit);
				proposal.setAfterAgInRemoteAmount(afterAgInRemoteCredit);
				proposal.setAfterBbinRemoteAmount(afterBbinRemoteCredit);
				proposal.setAfterKenoRemoteAmount(afterKenoRemoteCredit);
				proposal.setAfterSbRemoteAmount(afterSbRemoteAmount);

				proposal.setSaveway("slotmachine"); // 标示该提款为老虎机提款
				if (afterSkyRemoteAmount == null) {
					proposal.setAfterSkyRemoteAmount(-1.0);
				} else {
					proposal.setAfterSkyRemoteAmount(afterSkyRemoteAmount / 100);
				}
				proposal.setBankname(bank);
				save(cashout);
				save(proposal);
				taskDao.generateTasks(pno, proposer);
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		return msg;
	}

	@Override
	public Boolean questionValidate(String loginname, String answer, Integer questionid) {

		try {
			if (StringUtils.isBlank(answer)) {
				return false;
			}
			DetachedCriteria dc = DetachedCriteria.forClass(Question.class);
			dc.add(Restrictions.eq("loginname", loginname));
			dc.add(Restrictions.eq("questionid", questionid));
			dc.add(Restrictions.eq("delflag", 0));
			List<Question> questions = proposalDao.findByCriteria(dc);
			if (null != questions && questions.size() > 0 && null != questions.get(0)) {
				Question qu = questions.get(0);
				if (StringUtils.equals(answer, qu.getContent())) {
					QuestionStatus status = (QuestionStatus) proposalDao.get(QuestionStatus.class, loginname);
					if (null != status) {
						status.setErrortimes(0);
						proposalDao.update(status);
					}
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public Integer questionsNumber(String loginname) {

		DetachedCriteria dc = DetachedCriteria.forClass(Question.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("delflag", 0));
		List<Question> questions = proposalDao.findByCriteria(dc);
		if (null != questions) {
			return questions.size();
		} else {
			return 0;
		}
	}

	@Override
	public Question queryQuestion(String loginname) {

		DetachedCriteria dc = DetachedCriteria.forClass(Question.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("delflag", 0));
		List<Question> questions = proposalDao.findByCriteria(dc);
		if (null != questions && questions.size() > 0 && null != questions.get(0)) {
			return questions.get(0);
		}
		return null;
	}

	@Override
	public String saveQuestion(String loginname, Integer questionid, String content) {

		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Question.class);
			dc.add(Restrictions.eq("loginname", loginname));
			dc.add(Restrictions.eq("questionid", questionid));
			dc.add(Restrictions.eq("delflag", 0));
			List<Question> questions = proposalDao.findByCriteria(dc);
			if (null != questions && questions.size() > 0 && null != questions.get(0)) {
				return "您已经绑定该问题";
			}
			if (questionsNumber(loginname) >= 1) {
				return "只需要绑定一个密保问题";
			}
			Question question = new Question();
			question.setLoginname(loginname);
			question.setQuestionid(questionid);
			question.setContent(content);
			question.setDelflag(0);
			question.setCreatetime(new Date());
			proposalDao.save(question);
			return "绑定成功";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "绑定失败";
	}

	@Override
	public void saveOrUpdateQuestionStatus(String loginname) {

		QuestionStatus status = (QuestionStatus) proposalDao.get(QuestionStatus.class, loginname);
		if (null == status) {
			status = new QuestionStatus();
			status.setLoginname(loginname);
			status.setErrortimes(1);
			status.setCreatetime(new Date());
		} else if (null != status && (new Date().getTime() - status.getUpdatetime().getTime()) < 1000 * 60 * 10) {
			status.setErrortimes(status.getErrortimes() + 1);
		}
		status.setUpdatetime(new Date());
		proposalDao.saveOrUpdate(status);
	}

	@Override
	public QuestionStatus queryQuesStatus(String loginname) {

		return (QuestionStatus) proposalDao.get(QuestionStatus.class, loginname);
	}

	@Override
	public String EnableUser(String userName, boolean isEnable, String operator, String remark) {

		String msg = null;
		Users user = (Users) get(Users.class, userName);
		if (user == null) {
			msg = "未找到该用户";
		} else {
			user.setFlag(isEnable ? Constants.ENABLE : Constants.DISABLE);
			user.setRemark(remark);
			update(user);
			if (user.getFlag() == 0) {
				Userstatus userstatus = (Userstatus) get(Userstatus.class, userName, LockMode.UPGRADE);
				if (null != userstatus) {
					userstatus.setLoginerrornum(0);
					userstatus.setCashinwrong(0);
					update(userstatus);
				}
			}
			logDao.insertOperationLog(operator, OperationLogType.ENABLE, (isEnable ? "启用" : "禁用") + "会员：" + userName
					+ "，备注：" + remark);
			msg = null;
		}
		return msg;
	}

	@Override
	public String updateTaskAmount(String loginname, Double remite, TaskUserRecord record) {

		record.setIsAdd(1);
		record.setUpdatetime(new Date());
		proposalDao.update(record);

		TaskAmount taskAmount = (TaskAmount) proposalDao.get(TaskAmount.class, loginname, LockMode.UPGRADE);
		if (null == taskAmount) {
			taskAmount = new TaskAmount();
			taskAmount.setLoginname(loginname);
			taskAmount.setAmount(remite);
			taskAmount.setCreatetime(new Date());
		} else {
			taskAmount.setAmount(taskAmount.getAmount() + remite);
			taskAmount.setUpdatetime(new Date());
		}
		proposalDao.saveOrUpdate(taskAmount);
		return null;
	}

	@Override
	public String transfertaskAmountInAccount(Users user, Double remit) {

		TaskAmount taskAmount = (TaskAmount) tradeDao.get(TaskAmount.class, user.getLoginname());
		if (null == taskAmount || taskAmount.getAmount() < remit) {
			return "友情提示：任务累计金额不足" + remit;
		}
		// 扣掉任务奖金
		logDao.insertCreditLog(user.getLoginname(), CreditChangeType.YAOYAOLE.getCode(), taskAmount.getAmount(), remit,
				taskAmount.getAmount() - remit, "摇摇乐任务累计金额中扣除");
		taskAmount.setAmount(taskAmount.getAmount() - remit);
		tradeDao.updateTaskAmountSql(taskAmount);
		// 添加奖金到主账户
		String pno = seqDao.generateProposalPno(ProposalType.YAOYAOLE);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.YAOYAOLE.getCode(),
				user.getLevel(), user.getLoginname(), remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(null);
		proposal.setGifTamount(null);
		proposal.setExecuteTime(new Date());
		tradeDao.save(proposal);
		String seqId = seqService.generateTransferID();
		tradeDao.changeCreditIn(user, remit, CreditChangeType.YAOYAOLE.getCode(), seqId, "摇摇乐礼金");
		return "任务礼金转入成功";
	}

	@Override
	public String updateAndAddUsertaskAmount(String loginname) {

		Users user = (Users) proposalDao.get(Users.class, loginname);
		if (null == user) {
			log.error("不存在玩家：" + loginname);
			return "不存在玩家：" + loginname;
		}
		// 查询该玩家所有未完成的任务
		DetachedCriteria dc = DetachedCriteria.forClass(TaskUserRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("isAdd", 0));
		List<TaskUserRecord> records = proposalDao.findByCriteria(dc);
		if (null == records || records.size() == 0) {
			log.info(loginname + "没有未完成的任务");
			return loginname + "没有未完成的任务";
		} else {
			for (TaskUserRecord record : records) {
				TaskList task = (TaskList) proposalDao.get(TaskList.class, record.getTaskId());
				if (null == task || task.getDisable() != 0) {
					log.info(loginname + "没有未完成的任务");
					record.setIsAdd(3); // 关闭
					record.setUpdatetime(new Date());
					proposalDao.update(record);
					log.info(loginname + "任务已关闭");
					continue;
				}
				if (record.getType() == 1) {// PT次次存
					DetachedCriteria ptCiCun = DetachedCriteria.forClass(Proposal.class);
					ptCiCun.add(Restrictions.eq("loginname", record.getUsername()));
					ptCiCun.add(Restrictions.eq("type", ProposalType.SELFPT91.getCode().intValue()));
					ptCiCun.add(Restrictions.eq("flag", 2));
					ptCiCun.add(Restrictions.ge("createTime", record.getCreatetime()));
					if (record.getCreatetime().getTime() <= DateUtil.getchangedDate(0).getTime()) {
						ptCiCun.add(Restrictions.lt("createTime", DateUtil.getDateEnd(record.getCreatetime())));
					}
					List<Proposal> coCunProposals = proposalDao.findByCriteria(ptCiCun);
					if (null != coCunProposals && coCunProposals.size() >= 1) {
						updateTaskAmount(loginname, task.getGiftmoney(), record);
					} else if (record.getCreatetime().getTime() <= DateUtil.getchangedDate(0).getTime()) { // 昨天之前的任务没有完成的，直接过期
						record.setIsAdd(2); // 过期
						record.setUpdatetime(new Date());
						proposalDao.update(record);
					}
				} else if (record.getType() == 2) {// TTG次次存
					DetachedCriteria ttgCiCun = DetachedCriteria.forClass(Proposal.class);
					ttgCiCun.add(Restrictions.eq("loginname", record.getUsername()));
					ttgCiCun.add(Restrictions.eq("type", ProposalType.SELFEBET99.getCode().intValue()));
					ttgCiCun.add(Restrictions.eq("flag", 2));
					ttgCiCun.add(Restrictions.ge("createTime", record.getCreatetime()));
					if (record.getCreatetime().getTime() <= DateUtil.getchangedDate(0).getTime()) {
						ttgCiCun.add(Restrictions.lt("createTime", DateUtil.getDateEnd(record.getCreatetime())));
					}

					List<Proposal> coCunProposals = proposalDao.findByCriteria(ttgCiCun);
					if (null != coCunProposals && coCunProposals.size() >= 1) {
						updateTaskAmount(loginname, task.getGiftmoney(), record);
					} else if (record.getCreatetime().getTime() <= DateUtil.getchangedDate(0).getTime()) { // 昨天之前的任务没有完成的，直接过期
						record.setIsAdd(2); // 过期
						record.setUpdatetime(new Date());
						proposalDao.update(record);
					}
				} else if (record.getType() == 3) {// 微信存款
					DetachedCriteria wxDc = DetachedCriteria.forClass(Payorder.class);
					wxDc.add(Restrictions.eq("loginname", record.getUsername()));
					wxDc.add(Restrictions.in("payPlatform", new String[] { "zfwx", "zfwx1", "lfwx" }));
					wxDc.add(Restrictions.eq("flag", 0));
					wxDc.add(Restrictions.eq("type", 0));
					wxDc.add(Restrictions.ge("createTime", record.getCreatetime()));
					if (record.getCreatetime().getTime() <= DateUtil.getchangedDate(0).getTime()) {
						wxDc.add(Restrictions.lt("createTime", DateUtil.getDateEnd(record.getCreatetime())));
					}
					wxDc.setProjection(Projections.sum("money"));
					List wxList = proposalDao.findByCriteria(wxDc);
					Double wxCunKuan = 0.0;
					if (wxList != null && !wxList.isEmpty() && null != wxList.get(0)) {
						wxCunKuan = (Double) wxList.get(0);
					}
					if (wxCunKuan >= task.getRequireData()) {
						updateTaskAmount(loginname, task.getGiftmoney(), record);
					} else if (record.getCreatetime().getTime() <= DateUtil.getchangedDate(0).getTime()) { // 昨天之前的任务没有完成的，直接过期
						record.setIsAdd(2); // 过期
						record.setUpdatetime(new Date());
						proposalDao.update(record);
					}
				} else if (record.getType() == 4 || record.getType() == 6 || record.getType() == 5) {// 4,6
																										// PT老虎机流水任务
																										// 5.ttg

					DetachedCriteria platFormDc = DetachedCriteria.forClass(PlatformData.class);
					platFormDc.add(Restrictions.eq("loginname", loginname));
					platFormDc.add(Restrictions.eq("starttime", DateUtil.getDateStart(record.getCreatetime())));
					if (record.getType() == 4 || record.getType() == 6) {
						platFormDc.add(Restrictions.eq("platform", "pttiger"));
					} else if (record.getType() == 5) {
						platFormDc.add(Restrictions.eq("platform", "ttg"));
					}
					List<PlatformData> list = proposalDao.findByCriteria(platFormDc);
					Double plaBet = 0.0;
					if (null != list && list.size() > 0 && null != list.get(0)) {
						plaBet = list.get(0).getBet();
					}

					if ((plaBet - record.getHistoryBet()) >= task.getRequireData()) {
						updateTaskAmount(loginname, task.getGiftmoney(), record);
					} else if (record.getCreatetime().getTime() <= DateUtil.getchangedDate(0).getTime()) { // 昨天之前的任务没有完成的，直接过期
						record.setIsAdd(2); // 过期
						record.setUpdatetime(new Date());
						proposalDao.update(record);
					}
				} else if (record.getType() == 7) {// 存款
					Double wxCunKuan = 0.0;
					Double mfCunKuan = 0.0;
					DetachedCriteria wxDc = DetachedCriteria.forClass(Payorder.class);
					wxDc.add(Restrictions.eq("loginname", record.getUsername()));
					wxDc.add(Restrictions.eq("flag", 0));
					wxDc.add(Restrictions.eq("type", 0));
					wxDc.add(Restrictions.ge("createTime", record.getCreatetime()));
					if (record.getCreatetime().getTime() <= DateUtil.getchangedDate(0).getTime()) {
						wxDc.add(Restrictions.lt("createTime", DateUtil.getDateEnd(record.getCreatetime())));
					}
					wxDc.setProjection(Projections.sum("money"));
					List wxList = proposalDao.findByCriteria(wxDc);

					DetachedCriteria proposalSavedc = DetachedCriteria.forClass(Proposal.class);
					proposalSavedc.add(Restrictions.eq("loginname", loginname));
					proposalSavedc.add(Restrictions.eq("flag", 2));
					proposalSavedc.add(Restrictions.eq("type", ProposalType.CASHIN.getCode().intValue()));
					proposalSavedc.add(Restrictions.ge("createTime", record.getCreatetime()));
					if (record.getCreatetime().getTime() <= DateUtil.getchangedDate(0).getTime()) {
						proposalSavedc.add(Restrictions.lt("createTime", DateUtil.getDateEnd(record.getCreatetime())));
					}
					proposalSavedc.setProjection(Projections.sum("amount"));
					List saveList = proposalDao.findByCriteria(proposalSavedc);
					if (saveList != null && !saveList.isEmpty() && null != saveList.get(0)) {
						mfCunKuan = (Double) saveList.get(0);
					}
					if (wxList != null && !wxList.isEmpty() && null != wxList.get(0)) {
						wxCunKuan = (Double) wxList.get(0);
					}

					if (wxCunKuan + mfCunKuan >= task.getRequireData()) {
						updateTaskAmount(loginname, task.getGiftmoney(), record);
					} else if (record.getCreatetime().getTime() <= DateUtil.getchangedDate(0).getTime()) { // 昨天之前的任务没有完成的，直接过期
						record.setIsAdd(2); // 过期
						record.setUpdatetime(new Date());
						proposalDao.update(record);
					}
				}
			}
			return "SUCCESS";
		}

	}

	@Override
	public String saveTask(String loginname, Integer taskId) {

		DetachedCriteria dc = DetachedCriteria.forClass(TaskUserRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.ge("createtime", DateUtil.getchangedDate(0)));
		dc.add(Restrictions.lt("createtime", DateUtil.getchangedDate(1)));
		List<TaskUserRecord> list = proposalDao.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			return "友情提示：每天只能领取一个任务";
		}

		TaskList task = (TaskList) proposalDao.get(TaskList.class, taskId);
		if (task == null || task.getDisable() == 1) {
			return "不存在该任务";
		}
		// 4、5、6为流水任务 4/6为老虎机 5为TTG
		Double historyBet = 0.0;
		DetachedCriteria platFormDc = DetachedCriteria.forClass(PlatformData.class);
		platFormDc.add(Restrictions.eq("loginname", loginname));
		platFormDc.add(Restrictions.eq("starttime", DateUtil.getchangedDate(0)));
		if (task.getType() == 4 || task.getType() == 6) {
			platFormDc.add(Restrictions.eq("platform", "pttiger"));
			List<PlatformData> platformDatas = proposalDao.findByCriteria(platFormDc);
			if (null != platformDatas && platformDatas.size() == 1) {
				historyBet = platformDatas.get(0).getBet();
			}
		} else if (task.getType() == 5) {
			platFormDc.add(Restrictions.eq("platform", "ttg"));
			List<PlatformData> platformDatas = proposalDao.findByCriteria(platFormDc);
			if (null != platformDatas && platformDatas.size() == 1) {
				historyBet = platformDatas.get(0).getBet();
			}
		} else if (task.getType() == 3) {// 微信存款
			DetachedCriteria wxDc = DetachedCriteria.forClass(Payorder.class);
			wxDc.add(Restrictions.eq("loginname", loginname));
			wxDc.add(Restrictions.in("payPlatform", new String[] { "zfwx", "zfwx1", "lfwx" }));
			wxDc.add(Restrictions.eq("flag", 0));
			wxDc.add(Restrictions.eq("type", 0));
			wxDc.add(Restrictions.ge("createTime", DateUtil.getchangedDate(0)));
			wxDc.setProjection(Projections.sum("money"));
			List wxList = proposalDao.findByCriteria(wxDc);
			if (wxList != null && !wxList.isEmpty() && null != wxList.get(0)) {
				historyBet = (Double) wxList.get(0);
			}
		} else if (task.getType() == 7) {// 存款
			DetachedCriteria wxDc = DetachedCriteria.forClass(Payorder.class);
			wxDc.add(Restrictions.eq("loginname", loginname));
			wxDc.add(Restrictions.eq("flag", 0));
			wxDc.add(Restrictions.eq("type", 0));
			wxDc.add(Restrictions.ge("createTime", DateUtil.getchangedDate(0)));
			wxDc.setProjection(Projections.sum("money"));
			List wxList = proposalDao.findByCriteria(wxDc);
			if (wxList != null && !wxList.isEmpty() && null != wxList.get(0)) {
				historyBet = (Double) wxList.get(0);
			}

			DetachedCriteria proposalSavedc = DetachedCriteria.forClass(Proposal.class);
			proposalSavedc.add(Restrictions.eq("loginname", loginname));
			proposalSavedc.add(Restrictions.eq("flag", 2));
			proposalSavedc.add(Restrictions.eq("type", ProposalType.CASHIN.getCode().intValue()));
			proposalSavedc.add(Restrictions.ge("createTime", DateUtil.getchangedDate(0)));
			proposalSavedc.setProjection(Projections.sum("amount"));
			List saveList = proposalDao.findByCriteria(proposalSavedc);
			if (saveList != null && !saveList.isEmpty() && null != saveList.get(0)) {
				historyBet += (Double) saveList.get(0);
			}
		}
		TaskUserRecord record = new TaskUserRecord(loginname, task.getTitle(), taskId, task.getType(), historyBet, 0,
				new Date(), null);
		proposalDao.save(record);
		return "SUCCESS";
	}

	public AlipayAccount getAlipayAccount(String loginname, Integer disable) {

		DetachedCriteria dc = DetachedCriteria.forClass(AlipayAccount.class);
		dc.add(Restrictions.eq("loginname", loginname));
		if (null != disable) {
			dc.add(Restrictions.eq("disable", disable));
		}
		List<AlipayAccount> list = proposalDao.findByCriteria(dc);
		if (null != list && list.size() > 0 && null != list.get(0)) {
			return list.get(0);
		} else {
			return null;
		}
	}

	

	/**
	 * 派发推荐好友奖励
	 */
	@Override
	public String addFriendbonus(String loginname, Double remit, String remark) {
		String topusername=queryFriendbonusrecordSc(loginname,1);
		if(!StringUtil.isEmpty(topusername)){
			 saveFriendbonusrecords(loginname, topusername, remit, remark);
		}
		return null;
	}
	
	/**
	 * 1.查询是否有上层玩家 2.是否领取过8元体验金    验证通过则返回上层姓名
	 * type=0说明是8元体验金的奖励     type=1是首存次存奖励
	 * @param downusername
	 * @return
	 */
	private String queryFriendbonusrecordSc(String downusername,int type){
		//查询该玩家  是否有上层玩家
		String topusername="";
		DetachedCriteria dcSc = DetachedCriteria.forClass(Friendintroduce.class);
		dcSc.add(Restrictions.eq("downlineuser", downusername));
		List<Friendintroduce> friendintroduces = this.findByCriteria(dcSc) ;
		if(null!=friendintroduces&&friendintroduces.size()>0){
			topusername=friendintroduces.get(0).getToplineuser();
		}else{
			return null;
		}
		if(type==0){
			return	topusername;
		}
		//先查询该玩家是否领取8元体验金
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.eq("loginname", downusername));
		dc.add(Restrictions.in("target", Arrays.asList(new String[]{"newpt","ttg","gpi","nt","qt","mg","dt"}))) ;
		dc.add(Restrictions.or(Restrictions.eq("remit", 8.00), Restrictions.sqlRestriction(" remark like '%元自助优惠' ")));
		List transfers = this.findByCriteria(dc) ;
		if(null != transfers && transfers.size()>0 && null != transfers.get(0)){
			return	topusername;
		}
		return null;
		
	}
	
	/**
	 * 
	 * @param downusername
	 * @param remit
	 * @param remark
	 * @param type//0首存奖励 1次存奖励
	 * @return
	 */
	private String saveFriendbonusrecords(String downusername,String topusername,Double remit,String remark){
		Friendbonus fb =null;
		DetachedCriteria dcFb = DetachedCriteria.forClass(Friendbonus.class);
		dcFb.add(Restrictions.eq("toplineuser", topusername));
		List<Friendbonus> friendbonus = this.findByCriteria(dcFb) ;
		if(null!=friendbonus&&friendbonus.size()>0){
			fb=friendbonus.get(0);
		}else{
			fb=new Friendbonus();
			fb.setMoney(0.0);
		}
		//查询该玩家  是否已经奖励过上层玩家 未奖励过 则是首存奖励  已奖励过 则是次存奖励
		String mulStr=null;
		Double mul=null;
		String maxBonusStr=null;
		Double maxBonus=null;
		
		
		//没有领取8元体验金则不能领取任何奖励begin
		DetachedCriteria dcSc1 = DetachedCriteria.forClass(Friendbonusrecord.class);
		dcSc1.add(Restrictions.eq("downlineuser", downusername));
		dcSc1.add(Restrictions.sqlRestriction(" remark  like '体验金奖励%' "));  
		List friendbonusrecords1 = this.findByCriteria(dcSc1) ;
		if(null==friendbonusrecords1||friendbonusrecords1.size()<1){
			return null;
		}
		//没有领取8元体验金则不能领取任何奖励end
		
		
		
		DetachedCriteria dcSc = DetachedCriteria.forClass(Friendbonusrecord.class);
		dcSc.add(Restrictions.eq("downlineuser", downusername));
		dcSc.add(Restrictions.sqlRestriction(" remark  not like '体验金奖励%' "));
		List friendbonusrecords = this.findByCriteria(dcSc) ;
		if(null!=friendbonusrecords&&friendbonusrecords.size()>0){//次存奖励
			mulStr=checkSystemConfig("type010","001","否");
			remark=remark+";次存"+remit+"奖励";
			maxBonusStr=checkSystemConfig("type013","001","否");//首存最高奖励
			//查询玩家奖金池余额
		}else{//首存奖励
			mulStr=checkSystemConfig("type009","001","否");
			remark=remark+";首存"+remit+"奖励";
			maxBonusStr=checkSystemConfig("type012","001","否");//次存最高奖励
		}
		if(!StringUtil.isEmpty(mulStr)){
		mul=Double.parseDouble(mulStr);
		}else{
			return null;
		}
		if(!StringUtil.isEmpty(maxBonusStr)){
			maxBonus=Double.parseDouble(maxBonusStr);
			}else{
				return null;
			}
		Double addRemit=Arith.mul(mul, remit);//奖励的金额
		if(addRemit>maxBonus){
			addRemit=maxBonus;
		}
		Friendbonusrecord fu=new Friendbonusrecord();
		fu.setCreatetime(new Date());
		fu.setDownlineuser(downusername);
		fu.setToplineuser(topusername);
		fu.setMoney(addRemit);
		fu.setRemark(remark+";奖励比例为："+mul+",奖励前余额为:"+fb.getMoney()+",奖励后余额为："+(fb.getMoney()+addRemit));
		fu.setType("1");
		
		fb.setCreatetime(new Date());
		fb.setToplineuser(topusername);
		if(null!=fb.getMoney()){
			fb.setMoney(addRemit+fb.getMoney());	
		}else{
			fb.setMoney(addRemit);
		}
		this.save(fu);
		this.saveOrUpdate(fb);
		
		return null;
	}
	
	
	/**
	 * 
	 * @param downusername
	 * @param remit
	 * @param remark
	 * @param type=2  8元体验金奖励
	 * @return
	 */
	@Override
	public String saveFriendbonusrecordFor8(String downusername,Double addRemit,String remark){
		String topusername=queryFriendbonusrecordSc(downusername,0);
		if(StringUtil.isEmpty(topusername)){
			return null;
		}
		Friendbonus fb =null;
		DetachedCriteria dcFb = DetachedCriteria.forClass(Friendbonus.class);
		dcFb.add(Restrictions.eq("toplineuser", topusername));
		List<Friendbonus> friendbonus = this.findByCriteria(dcFb) ;
		if(null!=friendbonus&&friendbonus.size()>0){
			fb=friendbonus.get(0);
		}else{
			fb=new Friendbonus();
			fb.setMoney(0.0);
		}
		Friendbonusrecord fu=new Friendbonusrecord();
		fu.setCreatetime(new Date());
		fu.setDownlineuser(downusername);
		fu.setToplineuser(topusername);
		fu.setMoney(addRemit);
		fu.setRemark(remark+";奖励前余额为:"+fb.getMoney()+",奖励后余额为："+(Arith.add(fb.getMoney(),addRemit)));
		fu.setType("1");
		
		fb.setCreatetime(new Date());
		fb.setToplineuser(topusername);
		if(null!=fb.getMoney()){
			fb.setMoney(Arith.add(addRemit,fb.getMoney()));	
		}else{
			fb.setMoney(addRemit);
		}
		this.save(fu);
		this.saveOrUpdate(fb);
		
		return null;
	}
	
	
	
	private String checkSystemConfig(String typeNo,String itemNo,String flag){
		String str="";
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if(!StringUtils.isEmpty(typeNo)){
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if(!StringUtils.isEmpty(itemNo)){
			dc = dc.add(Restrictions.eq("itemNo", itemNo));
		}
		if(!StringUtils.isEmpty(flag)){
			dc = dc.add(Restrictions.eq("flag", flag));
		}
		List<SystemConfig> list=this.findByCriteria(dc);
		if(null!=list&&list.size()>0){
			str=list.get(0).getValue();
		}
		return str;
	}
	
	@Override
	public String transferPoint(Users user, Double remit) {
			DetachedCriteria dc=DetachedCriteria.forClass(TotalPoint.class);
			dc.add(Restrictions.eq("username", user.getLoginname()));
			List<TotalPoint>  list = findByCriteria(dc);
			if(null==list||list.size()<1||null==list.get(0)){
				return "积分不足，无法兑换";
			}
			TotalPoint tp =list.get(0);
			Double pointsBalance=tp.getTotalpoints();
			Double mul=500.0;//默认新会元
			if(user.getLevel()==1){
				mul=400.0;	
			}else if(user.getLevel()==2){
				mul=325.0;	
			}else if(user.getLevel()==3){
				mul=280.0;	
			}else if(user.getLevel()==4){
				mul=245.0;	
			}else if(user.getLevel()==5){
				mul=220.0;	
			}else if(user.getLevel()==6){
				mul=100.0;	
			}
			Double changePoint=Arith.mul(remit, mul);
			if(changePoint>pointsBalance){
				return "积分不足，无法兑换";
			}
			tp.setTotalpoints(Arith.sub(pointsBalance, changePoint));
			tp.setUpdatetime(new Date());
			DetailPoint detailP=new DetailPoint();//保存支出积分的明细
			detailP.setCreatetime(new Date());
			detailP.setCreateday(DateUtil.fmtyyyyMMddHHmmss(new Date()));
			detailP.setPoints(changePoint);
			detailP.setSumamount(remit);
			detailP.setType("1");
			detailP.setUsername(tp.getUsername());
			//创建提案
			user = (Users) get(Users.class, user.getLoginname(), LockMode.UPGRADE);
			String pno = seqService.generateProposalPno(ProposalType.POINTDEPOSIT421);
			Proposal proposal = new Proposal(pno, user.getLoginname(), DateUtil.now(), ProposalType.POINTDEPOSIT421.getCode(), user.getLevel(), null, 0.0, null, ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, null, null);
			proposal.setLoginname(user.getLoginname());
			proposal.setCreateTime(new Date());
			proposal.setExecuteTime(new Date());
			proposal.setAgent(user.getAgent());
			proposal.setAmount(remit);
			Double credit=user.getCredit();
			Double newCredit=credit+remit;
			String remark="积分兑换奖金："+remit+",兑换前余额为："+credit+",兑换后余额为："+newCredit+";兑换前积分："+pointsBalance+",兑换后积分："+tp.getTotalpoints()+",消耗积分："+changePoint+"提案号："+pno;
			proposal.setRemark(remark);
			tp.setRemark(remark);
			detailP.setRemark(remark);
			this.save(proposal);//保存提案
			this.save(detailP);//保存支出积分的
			this.update(tp);//更新积分余额
			user.setCredit(newCredit);
			transferDao.updateUserCreditSqlOnline(user.getLoginname() , remit);//更新玩家账户余额
			//保存额度记录
			logDao.insertCreditLog(user.getLoginname(), CreditChangeType.TRANSFER_POINT_INT.getCode(), credit, remit, newCredit, "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));
			return null;
	}
	
	@Override
	public Double getGameMoney(String loginname , String gameType) throws Exception {
		Double money = 0.0 ;
		if(gameType.equals("ea")){
			money = RemoteCaller.queryCredit(loginname) ;
		}else if(gameType.equals("ebet")){
			money = EBetUtil.getBalance(loginname, "EBET");
		}else if(gameType.equals("ag")){
			money = Double.valueOf(RemoteCaller.queryDspCredit(loginname)) ;
		}else if(gameType.equals("agin")){
			money = Double.valueOf(RemoteCaller.queryDspAginCredit(loginname)) ;
		}else if(gameType.equals("bbin")){
			money = Double.valueOf(RemoteCaller.queryBbinCredit(loginname)) ;
		}else if(gameType.equals("newpt")){
			money = PtUtil.getPlayerMoney(loginname) ;
		}else if(gameType.equals("ttg")){
			money = Double.valueOf(PtUtil1.getPlayerAccount(loginname)) ;
		}else if(gameType.equals("nt")){
			String result = NTUtils.getNTMoney(loginname) ;
			JSONObject rj = JSONObject.fromObject(result);
			money = rj.getBoolean("result")?rj.getDouble("balance"):0.0 ;
		}else if(gameType.equals("qt")){
			money = Double.valueOf(QtUtil.getBalance(loginname)) ;
		}else if(gameType.equals("keno2")){
			KenoResponseBean bean = DocumentParser.parseKenocheckcreditResponseRequest(Keno2Util.checkcredit(loginname));
			if (bean != null) {
				if (bean.getName() != null && bean.getName().equals("Credit")) {
					money =  bean.getAmount();
				} else if (bean.getName() != null && bean.getName().equals("Error")) {
					throw new GenericDfhRuntimeException("获取kg余额失败1");
				}
			}else{
				throw new GenericDfhRuntimeException("获取kg余额失败2");
			}
		}
		return money;
	}

	@Override
	public Double getDepositAmount(String loginname, Date starttime, Date endtime) {
		Session session = proposalDao.getHibernateTemplate().getSessionFactory().getCurrentSession() ;
		String timeSql  = "";
		if(null != starttime){
			timeSql +=" and createTime>'"+DateUtil.formatDateForStandard(starttime)+"' ";
		}
		if(endtime != null){
			timeSql +=" and createTime<'"+DateUtil.formatDateForStandard(endtime)+"' ";
		}
		String sql = "SELECT SUM(deptable.depamount) from ( "+
					"select amount depamount , loginname from proposal where type=502 and flag=2 "+timeSql+"  and loginname='"+loginname+"' "+ 
					"UNION ALL  "+
					"select money depamount , loginname from payorder where type=0 and flag=0 "+timeSql+"  and loginname='"+loginname+"' "+ 
					") deptable ";
		Double depAmount = (Double) session.createSQLQuery(sql).uniqueResult();
		return depAmount;
	}
	
	@Override
	public Double getWithdrawAmount(String loginname, Date starttime, Date endtime) {
		Session session = proposalDao.getHibernateTemplate().getSessionFactory().getCurrentSession() ;
		
		String sql = "select sum(amount) from proposal where type=503 and flag=2  and loginname='"+loginname+"' " ;
		if(null != starttime){
			sql += " and createTime>'"+DateUtil.formatDateForStandard(starttime)+"' ";
		}
		if(null != endtime){
			sql +=" and createTime<'"+DateUtil.formatDateForStandard(endtime)+"' ";
		}
		Double depAmount = (Double) session.createSQLQuery(sql).uniqueResult();
		
		return depAmount;
	}
	
	@Override
	public Double getGameProfit(String loginname, Date starttime) {
		Session session = proposalDao.getHibernateTemplate().getSessionFactory().getCurrentSession() ;
		
		String sql = "select sum(amount) from agprofit where loginname='"+loginname+"' " ;
		if(null != starttime){
			sql += " and createTime>'"+DateUtil.formatDateForStandard(starttime)+"' ";
		}
		sql +=" and createTime<'"+DateUtil.fmtyyyy_MM_d(new Date())+" 00:00:00' ";
		Double depAmount = (Double) session.createSQLQuery(sql).uniqueResult();
		
		
		//0点到23点的
		String sqlPlatSlot = "select sum(profit) from platform_data where platform not in('pttiger','ea','agin','aginfish','sb','bbin') and loginname = '"+loginname+"' and starttime >='"+DateUtil.fmtyyyy_MM_d(DateUtil.getchangedDate(-1))+" 00:00:00' ";
		Double depAmountSlot = (Double) session.createSQLQuery(sqlPlatSlot).uniqueResult();
		//12点到12点的
		String sqlPlatLive = "select sum(profit) from platform_data where platform in('ea','agin','aginfish','sb','bbin') and loginname = '"+loginname+"' and starttime >='"+DateUtil.fmtyyyy_MM_d(DateUtil.getchangedDate(-1))+" 12:00:00' ";
		Double depAmountLive = (Double) session.createSQLQuery(sqlPlatLive).uniqueResult();
		
		//NT
		String sqlNt = "select sum(amount) from ptprofit where  loginname = '"+loginname+"' and starttime >='"+DateUtil.fmtyyyy_MM_d(DateUtil.getchangedDate(-1))+" 00:00:00' ";
		Double depAmountNt = (Double) session.createSQLQuery(sqlNt).uniqueResult();
		
		depAmount = (depAmount == null)?0.0:depAmount ;
		depAmountSlot = (depAmountSlot == null)?0.0:depAmountSlot ;
		depAmountLive = (depAmountLive == null)?0.0:depAmountLive ;
		depAmountNt = (depAmountNt == null)?0.0:depAmountNt ;
		return depAmount+depAmountSlot+depAmountLive+depAmountNt;
	}

	@Override
	public Double getYouHuiAmount(String loginname) {
		Double deduct = 0.00;
		String sql = "select SUM(amount) from proposal where loginname=:username and flag=:pstatus and (giftamount is null or giftamount=:amount) and type not in(:depositcode, :withdrawalcode)";
		Map<String, Object> pramas = new HashMap<String, Object>();
		pramas.put("username", loginname);
		pramas.put("pstatus", 2);
		pramas.put("amount", 0);
		pramas.put("depositcode", 502);
		pramas.put("withdrawalcode", 503);
		deduct = proposalDao.getDoubleValueBySql(sql, pramas);
		
		sql = "select SUM(giftamount) from proposal where loginname=:username and flag=:pstatus and giftamount>:amount";
		deduct = Arith.add(deduct, proposalDao.getDoubleValueBySql(sql, pramas));
		return deduct ;
		/*DetachedCriteria proposaldc = DetachedCriteria.forClass(Proposal.class);
		proposaldc.add(Restrictions.eq("loginname", loginname));
		if(null != starttime){
			proposaldc.add(Restrictions.gt("createTime", starttime)) ;
		}
		if(null != endtime){
			proposaldc.add(Restrictions.lt("createTime", endtime)) ;
		}
		proposaldc.add(Restrictions.ne("type", 503));
		List<Proposal> proposals = proposalDao.findByCriteria(proposaldc);
		
		Double couponfee = 0.0 ;
		Double ximafee = 0.0 ;
		Proposal proposal = null ;
		for (int i = 0; i < proposals.size(); i++) {
			proposal = proposals.get(i) ;
			//优惠
			if(proposal.getFlag() == 2){
				if(proposal.getType() == 590 || proposal.getType() == 591 || proposal.getType() == 598 || proposal.getType() == 599 || proposal.getType() == 600 || proposal.getType() == 701 || proposal.getType() == 702 || proposal.getType() == 703 || proposal.getType() == 704|| proposal.getType() == 705|| proposal.getType() == 706 ||proposal.getType() == 707|| proposal.getType() == 708|| proposal.getType() == 709 ||proposal.getType() == 710|| proposal.getType() == 711|| proposal.getType() == 712 || proposal.getType() == 571 
						|| proposal.getType() == 572 || proposal.getType() == 573 || proposal.getType() == 574 || proposal.getType() == 575
						|| proposal.getType() == 401 || proposal.getType() == 402 || proposal.getType() == 403 || proposal.getType() == 404	|| proposal.getType() == 405|| proposal.getType() == 406|| proposal.getType() == 407|| proposal.getType() == 408|| proposal.getType() == 409|| proposal.getType() == 410|| proposal.getType() == 411|| proposal.getType() == 412|| proposal.getType() == 419 
						|| proposal.getType() == 426 || proposal.getType() == 427 || proposal.getType() == 428 || proposal.getType() == 422 || proposal.getType() == 423 || proposal.getType() == 425
						|| proposal.getType() == 537 || proposal.getType() == 531|| proposal.getType() == 532|| proposal.getType() == 533|| proposal.getType() == 534|| proposal.getType() == 535
						|| proposal.getType() == 592 || proposal.getType() == 593 || proposal.getType() == 594|| proposal.getType() == 595 || proposal.getType() == 596 || proposal.getType() == 597
						){ //优惠
					couponfee += proposal.getGifTamount() ;
				}
				//反水
				else if( proposal.getType() == 616 || proposal.getType() == 617 || proposal.getType() == 622 || proposal.getType() == 623 ||proposal.getType() == 624 ||proposal.getType() == 625 ||
						(proposal.getType() == 507 && (proposal.getRemark().equals("ptother系统洗码;执行:") || proposal.getRemark().equals("PTTIGER系统洗码;执行:") || proposal.getRemark().toLowerCase().contains("ttg") || proposal.getRemark().toLowerCase().contains("gpi")|| proposal.getRemark().toLowerCase().contains("qt")|| proposal.getRemark().toLowerCase().contains("nt")))
						|| proposal.getType() == 517  ||proposal.getType() == 611  ||proposal.getType() == 612 ||proposal.getType() == 613 ||proposal.getType() == 614 ||proposal.getType() == 615 ||proposal.getType() == 618||proposal.getType() == 619||proposal.getType() == 620||proposal.getType() == 621
						|| (proposal.getType() == 507 && !(proposal.getRemark().equals("ptother系统洗码;执行:") || proposal.getRemark().equals("PTTIGER系统洗码;执行:") || proposal.getRemark().toLowerCase().contains("ttg") || proposal.getRemark().toLowerCase().contains("gpi")|| proposal.getRemark().toLowerCase().contains("qt")|| proposal.getRemark().toLowerCase().contains("nt")))
						|| proposal.getType() == 506 || proposal.getType() == 509 || proposal.getType() == 513 || proposal.getType() == 518){  //洗码
					ximafee += proposal.getAmount();
				}
			}
			
			if((proposal.getType() == 519 || proposal.getType() == 560 || proposal.getType() == 420|| proposal.getType() == 421) && (proposal.getFlag() == 0 || proposal.getFlag() == 1 || proposal.getFlag() == 2) || proposal.getType() == 390){
					couponfee += proposal.getAmount() ;
			}
		}
		return couponfee + ximafee;*/
	}

	@Override
	public Double getLastDrawLocalAmount(String loginname , String pno) {
		DetachedCriteria logDc = DetachedCriteria.forClass(Creditlogs.class);
		logDc.add(Restrictions.eq("loginname", loginname));
		logDc.add(Restrictions.eq("type", CreditChangeType.CASHOUT.getCode()));
		logDc.add(Restrictions.like("remark", pno, MatchMode.ANYWHERE));
		logDc.addOrder(Order.desc("createtime"));
		List<Creditlogs> logs = proposalDao.findByCriteria(logDc);
		if(null != logs && logs.size()>0){
			return logs.get(0).getNewCredit() ;
		}
		return null;
	}

	@Override
	public List<String> getGameSwitch() {
		DetachedCriteria conDc = DetachedCriteria.forClass(Const.class);
		conDc.add(Restrictions.eq("value", "1"));
		conDc.add(Restrictions.like("id", "draw_", MatchMode.START)) ;
		conDc.setProjection(Projections.property("id"));
		return proposalDao.findByCriteria(conDc);
	}

	@Override
	public List<String> getTranGameType(String loginname, Date starttime) {
		Session session = proposalDao.getHibernateTemplate().getSessionFactory().getCurrentSession() ;
		String oneMonthAgo = DateUtil.getOneMonthAgoFormat() ;
		List<String> trans = (List<String>)session.createSQLQuery("select target from transfer where loginname='"+loginname+"' and createtime>='"+DateUtil.formatDateForStandard(starttime)+"' group by target ")
		.list();
		if(null == trans || trans.size() == 0){
			//如果从最后一次转账后未查到转账记录，那么从一个月前进行查询
			trans = (List<String>) session.createSQLQuery("select target from transfer where loginname='"+loginname+"' and createtime>='"+oneMonthAgo+"' group by target ")
					.list();
		}
		return trans ;
	}
	
	@Override
	public boolean existHadFinishedProposal(String loginname, ProposalType type) {
		// TODO Auto-generated method stub
		return proposalDao.existHadFinishedProposal(loginname, type);
	}
	
	@Override
	public String cancelNewdeposit(String loginname, String depositId){
		
		if(StringUtils.isBlank(loginname) || StringUtils.isBlank(depositId)){
			return "参数不全，请刷新页面重新操作！";
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("depositId", depositId));
		dc.add(Restrictions.eq("status", 0));
		List<DepositOrder> depositOrders = this.findByCriteria(dc);
		
		if(depositOrders != null && depositOrders.size() > 0){
			for(DepositOrder depositOrder : depositOrders){
				depositOrder.setStatus(2);
				depositOrder.setRemark("玩家手动删除订单！时间：" + DateUtil.fmtyyyyMMddHHmmss(new Date()));
				this.update(depositOrder);
			}
			return "success";
		}
		
		return "未查询到有效数据，请您刷新页面重新查询。";
	}
	
	/***
	 * 新秒提
	 */
	
	public Integer updateDepositBank(String loginname,String ubankno,String depositid){
		//支付宝
		if(ubankno ==null || ubankno.equals("")){
			return proposalDao.executeBySql("update deposit_order set flag = 0 where depositId = '"+depositid+"' ");
		}
		else {
			return proposalDao.executeBySql("update deposit_order set flag = 0 where loginname = '"+loginname+"' and  ubankno = '"+ubankno+"' ");	
		}
	}
	
//	//获取同略云订单号
//	public String generateTlyOrderNo() {
//		//return DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(3).toLowerCase();
//		return DateUtil.fmtyyyyMMddHHmmss(new Date()) + RandomStringUtils.randomAlphanumeric(5).toLowerCase();
//	}

}
