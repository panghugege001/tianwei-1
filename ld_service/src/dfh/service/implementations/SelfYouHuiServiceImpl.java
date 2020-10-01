package dfh.service.implementations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dfh.dao.*;
import dfh.model.*;
import dfh.utils.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.AutoYouHuiVo;
import dfh.exception.GenericDfhRuntimeException;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.remote.ErrorCode;
import dfh.remote.RemoteCaller;
import dfh.remote.RemoteConstant;
import dfh.remote.bean.DepositPendingResponseBean;
import dfh.remote.bean.DspResponseBean;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.ISelfYouHuiService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.TransferService;
import net.sf.json.JSONObject;

public class SelfYouHuiServiceImpl implements ISelfYouHuiService {
	private static Logger log = Logger.getLogger(SelfYouHuiServiceImpl.class);

	private ProposalService proposalService;
	private CustomerService cs;
	private SeqService seqService;

	private TransferService transferService;
	private TaskDao taskDao;
	private SeqDao seqDao;
	private TradeDao tradeDao;
	private TransferDao transferDao;
	private YouHuiConfigDao youHuiConfigDao;
	private LogDao logDao;

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public SeqService getSeqService() {
		return seqService;
	}

	public void setSeqService(SeqService seqService) {
		this.seqService = seqService;
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public CustomerService getCs() {
		return cs;
	}

	public void setCs(CustomerService cs) {
		this.cs = cs;
	}

	public SeqDao getSeqDao() {
		return seqDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public ProposalService getProposalService() {
		return proposalService;
	}

	public void setProposalService(ProposalService proposalService) {
		this.proposalService = proposalService;
	}

	public TransferService getTransferService() {
		return transferService;
	}

	public void setTransferService(TransferService transferService) {
		this.transferService = transferService;
	}

	// 全民闯关金额转入pt
	public String selfTransferPtEmigrated(String loginname, Double remit) {
		System.out.println("--------------------------------------------");
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			return "玩家不存在。";
		}
		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			remoteCredit = PtUtil.getPlayerMoney(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家余额异常失败";
		}
		if (null == remoteCredit) {
			return "获取玩家余额异常,请稍后再试";
		}
		if (remoteCredit >= 5) {
			return "PT平台金额必须小于5元,才能转入操作！";
		}
		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			return "您正在使用手工再存优惠,不能使用自助再存。";
		}
		String mul = "20";
		String pno = seqDao.generateProposalPno(ProposalType.EMIGRATED391);
		String remark = "全民闯关奖金转入PT游戏," + mul + "倍流水限制" + remit + "元";

		String transID = seqService.generateTransferID();
		String msg = transferService.transfer4Pt4Emigrated(transID, loginname, remit, remark);
		log.info("奖金转入PT：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.EMIGRATED391.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(mul);// ----------------流水倍数
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "E" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);
		user.setShippingcodePt(shippingcode);
		transferDao.update(user);
		// 记录下到目前为止的投注额度 begin
		String platform = "pttiger";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		transferDao.save(record);
		// 记录下到目前为止的投注额度 end
		Userstatus userstatus = (Userstatus) transferDao.get(Userstatus.class, loginname, LockMode.UPGRADE);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			transferDao.save(status);
		}

		return vo.getMessage();
	}

	/**
	 * 活动金额转入ea
	 */
	@Override
	public String selfTransferEaActivity(String loginname, Double remit, ActivityConfig config) {

		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行EA转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {

			remoteCredit = RemoteCaller.queryCredit(loginname);
			if (null == remoteCredit) {
				return "获取玩家EA游戏账户余额错误!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家EA游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "EA平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.ACTIVITY442);
		String remark = config.getTitle() + "转入EA奖金," + config.getMultiple() + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.ACTIVITY442.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, remark, null);
		proposal.setBetMultiples(config.getMultiple().toString());// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		/*
		 * String sqlCouponId = seqDao.generateYhjID(); String codeOne =
		 * dfh.utils.StringUtil.getRandomString(3); String codeTwo =
		 * dfh.utils.StringUtil.getRandomString(3); String shippingcode = "Z" +
		 * codeOne + sqlCouponId + codeTwo;
		 * proposal.setShippingCode(shippingcode);
		 */

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "E" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);
		user.setShippingcode(shippingcode);
		cs.update(user);

		// 记录下到目前为止的投注额度 begin
		String platform = "ea";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的ttg投注额
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
		prams.put("platform", "ea");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.ACTIVITY442.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);
		logDao.insertCreditLog(user.getLoginname(), ProposalType.ACTIVITY442.getText(), user.getCredit(), remit,
				user.getCredit(), "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));
		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		try {
			DepositPendingResponseBean depositPendingResponseBean = RemoteCaller.depositPendingRequest(loginname, remit,
					transID, user.getAgcode());
			if (null != depositPendingResponseBean
					&& depositPendingResponseBean.getStatus().equals(ErrorCode.SUCCESS.getCode())) {
				RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(),
						ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), user.getAgcode());

				transferService.addTransfer(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
						Constants.FAIL, ProposalType.ACTIVITY442.getText(), remark);
				if (StringUtils.isNotBlank(user.getShippingcode())) {
					transferService.updateUserShippingcodeSql(user);
				}
				return null;
			} else {
				return "转账失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	/**
	 * 活动金额转入ag
	 */
	@Override
	public String selfTransferAgActivity(String loginname, Double remit, ActivityConfig config) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行AG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = RemoteCaller.queryDspAginCredit(loginname);
			if (null != money && org.apache.commons.lang3.math.NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取玩家AG游戏账户余额错误!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家AG游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "AG平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.ACTIVITY442);
		String remark = config.getTitle() + "转入AG奖金," + config.getMultiple() + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.ACTIVITY442.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, remark, null);
		proposal.setBetMultiples(config.getMultiple().toString());// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "F" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);
		/*
		 * String sqlCouponId = seqDao.generateYhjID(); String codeOne =
		 * dfh.utils.StringUtil.getRandomString(3); String codeTwo =
		 * dfh.utils.StringUtil.getRandomString(3); String shippingcode = "Z" +
		 * codeOne + sqlCouponId + codeTwo;
		 * proposal.setShippingCode(shippingcode);
		 */
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "agin";
		// 记录下到目前为止的投注额度
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.ACTIVITY442.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);
		logDao.insertCreditLog(user.getLoginname(), ProposalType.ACTIVITY442.getText(), user.getCredit(), remit,
				user.getCredit(), "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		try {
			DspResponseBean dspResponseBean = RemoteCaller.depositPrepareDspAginRequest(loginname, remit, transID);
			DspResponseBean dspConfirmResponseBean = null;
			if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
				dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, remit, transID, 1);
				if (null != dspConfirmResponseBean && dspConfirmResponseBean.getInfo().equals("0")) {
					transferService.addTransferforAginDsp(Long.parseLong(transID), loginname, user.getCredit(), remit,
							Constants.IN, Constants.FAIL, ProposalType.ACTIVITY442.getText(), remark);
					return null;
				} else {
					return "转入失败";
				}
			} else {
				return "转入失败";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	/**
	 * 活动金额转入mg
	 */
	@Override
	public String selfTransferMgActivity(String loginname, Double remit, ActivityConfig config) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行MG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			// remoteCredit = MGSUtil.getBalance(loginname,user.getPassword());
			remoteCredit = MGSUtil.getBalance(loginname);
			if (null == remoteCredit) {
				return "获取玩家MG游戏账户余额错误!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家MG游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "MG平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.ACTIVITY442);

		String remark = config.getTitle() + "转入MG奖金," + config.getMultiple() + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.ACTIVITY442.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, remark, null);
		proposal.setBetMultiples(config.getMultiple().toString());// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "mg";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.ACTIVITY442.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);
		logDao.insertCreditLog(user.getLoginname(), ProposalType.ACTIVITY442.getText(), user.getCredit(), remit,
				user.getCredit(), "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforMg(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, ProposalType.ACTIVITY442.getText(), remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 活动金额转入dt
	 */
	@Override
	public String selfTransferDtActivity(String loginname, Double remit, ActivityConfig config) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行DT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = DtUtil.getamount(loginname);
			if (null != money && org.apache.commons.lang3.math.NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取玩家DT游戏账户余额错误!"; // +money
			}
		} catch (Exception e) {
			log.error("获取玩家" + loginname + "DT余额错误", e);
			return "获取玩家DT游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "DT平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.ACTIVITY442);
		String remark = config.getTitle() + "转入DT奖金," + config.getMultiple() + "倍流水，送" + remit;
		// 转账
		String transID = seqService.generateTransferID();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.ACTIVITY442.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, remark, null);
		proposal.setBetMultiples(config.getMultiple().toString());// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "dt";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.ACTIVITY442.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);
		logDao.insertCreditLog(user.getLoginname(), ProposalType.ACTIVITY442.getText(), user.getCredit(), remit,
				user.getCredit(), "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforDt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, ProposalType.ACTIVITY442.getText(), remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 活动金额转入png
	 */
	@Override
	public String selfTransferPngActivity(String loginname, Double remit, ActivityConfig config) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行PNG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			remoteCredit = PNGUtil.getBalance(loginname);
			if (null == remoteCredit) {
				return "获取玩家PNG游戏账户余额错误!"; // +money
			}
		} catch (Exception e) {
			log.error("获取玩家" + loginname + "PNG余额错误", e);
			return "获取玩家PNG游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "PNG平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.ACTIVITY442);
		String remark = config.getTitle() + "转入PNG奖金," + config.getMultiple() + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.ACTIVITY442.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, remark, null);
		proposal.setBetMultiples(config.getMultiple().toString());// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "png";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.ACTIVITY442.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);
		logDao.insertCreditLog(user.getLoginname(), ProposalType.ACTIVITY442.getText(), user.getCredit(), remit,
				user.getCredit(), "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforPng(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 活动金额转入ttg
	 */
	@Override
	public String selfTransferTtgActivity(String loginname, Double remit, ActivityConfig config) {

		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行TTG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = PtUtil1.getPlayerAccount(loginname);
			if (null != money && org.apache.commons.lang3.math.NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取玩家TTG游戏账户余额错误!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家TTG游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "TTG平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.ACTIVITY442);
		String remark = config.getTitle() + "转入TTG奖金," + config.getMultiple() + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.ACTIVITY442.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, remark, null);
		proposal.setBetMultiples(config.getMultiple().toString());// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		/*
		 * String sqlCouponId = seqDao.generateYhjID(); String codeOne =
		 * dfh.utils.StringUtil.getRandomString(3); String codeTwo =
		 * dfh.utils.StringUtil.getRandomString(3); String shippingcode = "Z" +
		 * codeOne + sqlCouponId + codeTwo;
		 * proposal.setShippingCode(shippingcode);
		 */
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "ttg";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的ttg投注额
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
		prams.put("platform", "ttg");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.ACTIVITY442.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);
		logDao.insertCreditLog(user.getLoginname(), ProposalType.ACTIVITY442.getText(), user.getCredit(), remit,
				user.getCredit(), "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));
		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		try {
			// Boolean b = PtUtil1.addPlayerAccountPraper(loginname,
			// remit.intValue());
			// if(null != b && b){
			transferService.addTransferforTt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, ProposalType.ACTIVITY442.getText(), remark);
			return null;
			/*
			 * }else{ vo.setMessage("奖金转入ttg失败  ， 数据回滚。"); throw new
			 * GenericDfhRuntimeException("奖金转入ttg失败  ， 数据回滚。"); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// 活动奖金转入pt
	public String selfTransferPtActivity(String loginname, Double remit, ActivityConfig config) {
		Users user = (Users) cs.get(Users.class, loginname);

		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			return "玩家不存在。";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			remoteCredit = PtUtil.getPlayerMoney(loginname);
			if (null == remoteCredit) {
				return "获取玩家PT游戏账户余额错误!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家PT游戏账户余额异常";
		}
		if (remoteCredit >= 5) {
			return "PT平台金额必须小于5元,才能转入操作！";
		}
		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			return "您正在使用手工再存优惠,不能使用自助再存。";
		}

		String pno = seqDao.generateProposalPno(ProposalType.ACTIVITY442);
		String remark = config.getTitle() + "转入PT奖金," + config.getMultiple() + "倍流水，送" + remit;

		// 转账transfer4Pt4Sign
		String transID = seqService.generateTransferID();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.ACTIVITY442.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, remark, null);
		proposal.setBetMultiples(config.getMultiple().toString());
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "Q" + codeOne + sqlCouponId + codeTwo;// 开头Q
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);

		user.setShippingcodePt(shippingcode);
		cs.update(user);
		// 记录下到目前为止的投注额度 begin
		String platform = "pttiger";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		logDao.insertCreditLog(user.getLoginname(), ProposalType.ACTIVITY442.getText(), user.getCredit(), remit,
				user.getCredit(), "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		// Boolean deposit = PtUtil.getDepositPlayerMoney(loginname,remit);
		/*
		 * if (deposit != null && deposit) { } else {
		 * vo.setMessage("签到奖金转入PT失败，请联系客服！"); throw new
		 * GenericDfhRuntimeException( "签到奖金转入PT失败  ， 数据回滚。"); }
		 */
		transferService.addTransferforNewPt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
				Constants.FAIL, ProposalType.ACTIVITY442.getText(), remark);

		return vo.getMessage();
	}

	/**
	 * 活动金额转入nt
	 */
	@Override
	public String selfTransferNTActivity(String loginname, Double remit, ActivityConfig config) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行NT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
			remoteCredit = ntm.getBoolean("result") ? ntm.getDouble("balance") : null;
			if (null == remoteCredit) {
				return "获取NT余额错误:" + ntm.getString("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家NT余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "NT平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.ACTIVITY442);
		String remark = config.getTitle() + "转入NT奖金," + config.getMultiple() + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.ACTIVITY442.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, remark, null);
		proposal.setBetMultiples(config.getMultiple().toString());// 无需流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "nt";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的nt投注额
		String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		prams.put("startTime", cd.getTime());
		// prams.put("platform", "ttg");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.ACTIVITY442.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);
		logDao.insertCreditLog(user.getLoginname(), ProposalType.ACTIVITY442.getText(), user.getCredit(), remit,
				user.getCredit(), "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforNT(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, ProposalType.ACTIVITY442.getText(), remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 活动金额转入qt
	 */
	@Override
	public String selfTransferQTActivity(String loginname, Double remit, ActivityConfig config) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行QT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = QtUtil.getBalance(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取QT余额错误:" + money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家QT余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "QT平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.ACTIVITY442);
		String remark = config.getTitle() + "转入QT奖金," + config.getMultiple() + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.ACTIVITY442.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, remark, null);
		proposal.setBetMultiples(config.getMultiple().toString());// 无需流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		proposalService.save(offer);
		proposalService.save(proposal);
		// 记录下到目前为止的投注额度 begin
		String platform = "qt";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的ttg投注额
		String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		prams.put("startTime", cd.getTime());
		// prams.put("platform", "ttg");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.ACTIVITY442.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);
		logDao.insertCreditLog(user.getLoginname(), ProposalType.ACTIVITY442.getText(), user.getCredit(), remit,
				user.getCredit(), "referenceNo:" + pno + ";" + StringUtils.trimToEmpty(remark));

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		try {
			transferService.addTransferforQt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, ProposalType.ACTIVITY442.getText(), remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 全民闯关金额转入nt
	 */
	@Override
	public String selfTransferNTEmigrated(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行NT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
			remoteCredit = ntm.getBoolean("result") ? ntm.getDouble("balance") : null;
			if (null == remoteCredit) {
				return "获取NT余额错误:" + ntm.getString("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家NT余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "NT平台金额必须小于5元,才能转入！";
		}
		String mul = "20";
		String pno = seqDao.generateProposalPno(ProposalType.EMIGRATED391);
		String remark = "全民闯关奖金转入NT游戏," + mul + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConponNT4Emigrated(transID, loginname, remit, remark);
		log.info("NT全民闯关奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.EMIGRATED391.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(mul);// -----------流水限制
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "nt";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的nt投注额
		String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		prams.put("startTime", cd.getTime());
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.EMIGRATED391.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforNT(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 自助红包金额转入ttg
	 */
	@Override
	public String selfTransferTtgHB(String loginname, Double remit, String betMultiples, Integer deposit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行TTG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = PtUtil1.getPlayerAccount(loginname);
			if (null != money && org.apache.commons.lang3.math.NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取TTG余额错误:" + money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家TTG余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "TTG平台金额必须小于5元,才能转入！";
		}
		String mul = betMultiples;
		String pno = seqDao.generateProposalPno(ProposalType.HB499);
		// String remark = "自助红包奖励转入TTG,"+mul+"倍流水，送" + remit;
		String remark = "自助红包奖励转入TTG,5倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4TTG4HB(transID, loginname, remit, remark, deposit);
		log.info("TTG自助红包奖励 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.HB499.getCode(), user.getLevel(),
				loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, null,
				null);
		proposal.setBetMultiples("5");//
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "E" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "ttg";
		// 获取0点到领取时的TTG投注额
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
		prams.put("platform", "ttg");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.HB499.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforTt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "从红包账户转至TTG账户", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 自助红包金额转入pt
	public String selfTransferPtHB(String loginname, Double remit, String betMultiples, Integer deposit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			return "玩家不存在。";
		}
		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			remoteCredit = PtUtil.getPlayerMoney(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家余额异常失败";
		}
		if (null == remoteCredit) {
			return "获取玩家余额异常,请稍后再试";
		}
		if (remoteCredit >= 5) {
			return "PT平台金额必须小于5元,才能转入操作！";
		}
		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			return "您正在使用手工再存优惠,不能使用自助再存。";
		}
		String mul = betMultiples;
		String pno = seqDao.generateProposalPno(ProposalType.HB499);
		// String remark = "自助红包奖金转入PT游戏,"+mul+"倍流水限制" + remit+"元";
		String remark = "自助红包奖励转入PT,5倍流水，送" + remit;

		String transID = seqService.generateTransferID();
		String msg = transferService.transfer4Pt4HB(transID, loginname, remit, remark, deposit);
		log.info("奖金转入PT：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.HB499.getCode(), user.getLevel(),
				loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, null,
				null);
		proposal.setBetMultiples("5");// ----------------流水倍数
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "E" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);
		user.setShippingcodePt(shippingcode);
		transferDao.update(user);
		// 记录下到目前为止的投注额度 begin
		String platform = "pttiger";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		transferDao.save(record);
		// 记录下到目前为止的投注额度 end
		Userstatus userstatus = (Userstatus) transferDao.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			transferDao.save(status);
		}

		return vo.getMessage();
	}

	/**
	 * 自助红包金额转入nt
	 */
	@Override
	public String selfTransferNTHB(String loginname, Double remit, String betMultiples, Integer deposit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行NT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
			remoteCredit = ntm.getBoolean("result") ? ntm.getDouble("balance") : null;
			if (null == remoteCredit) {
				return "获取NT余额错误:" + ntm.getString("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家NT余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "NT平台金额必须小于5元,才能转入！";
		}
		String mul = betMultiples;
		String pno = seqDao.generateProposalPno(ProposalType.HB499);
		// String remark = "自助红包奖金转入NT游戏,"+mul+"倍流水，送" + remit;
		String remark = "自助红包奖金转入NT游戏5倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConponNT4HB(transID, loginname, remit, remark, deposit);
		log.info("NT自助红包奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.HB499.getCode(), user.getLevel(),
				loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, null,
				null);
		proposal.setBetMultiples("5");// -----------流水限制
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "nt";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的nt投注额
		String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		prams.put("startTime", cd.getTime());
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.HB499.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforNT(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "从红包账户转至NT账户", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 自助红包金额转入qt
	 */
	@Override
	public String selfTransferQtHB(String loginname, Double remit, String betMultiples, Integer deposit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行QT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = QtUtil.getBalance(loginname);
			if (null != money && org.apache.commons.lang3.math.NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取QT余额错误:" + money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家QT余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "QT平台金额必须小于5元,才能转入！";
		}
		String mul = betMultiples;
		String pno = seqDao.generateProposalPno(ProposalType.HB499);
		// String remark = "自助红包奖金转入QT游戏,"+mul+"倍流水，送" + remit;
		String remark = "自助红包奖金转入QT游戏5倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4Qt4HB(transID, loginname, remit, remark, deposit);
		log.info("QT好友推荐奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.HB499.getCode(), user.getLevel(),
				loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, null,
				null);
		proposal.setBetMultiples("5");//
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "E" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "qt";
		// 获取0点到领取时的QT投注额
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
		prams.put("platform", "qt");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.HB499.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforQt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "从红包账户转至QT账户", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 自助红包金额转入MG
	 */
	@Override
	public String selfTransferMgHB(String loginname, Double remit, String betMultiples, Integer deposit) {

		Users user = (Users) cs.get(Users.class, loginname);
		if (user == null || user.getWarnflag().equals(4))
			return "抱歉，您不能进行MG转入。Q";

		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			// remoteCredit = MGSUtil.getBalance(loginname,user.getPassword());
			remoteCredit = MGSUtil.getBalance(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家MG余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "MG平台金额必须小于5元,才能转入！";
		}
		String mul = betMultiples;
		String pno = seqDao.generateProposalPno(ProposalType.HB499);
		// String remark = "自助红包奖金转入MG游戏,"+mul+"倍流水，送" + remit;
		String remark = "自助红包奖金转入MG游戏5倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4MG4HB(transID, loginname, remit, remark, deposit);
		log.info("MG全民团战 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.HB499.getCode(), user.getLevel(),
				loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, null,
				null);
		proposal.setBetMultiples("5");//
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "mg";
		// 获取0点到领取时MG投注额
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.HB499.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforMg(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "红包账户金额转至MG", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 自助红包金额转入dt
	 */
	@Override
	public String selfTransferDtHB(String loginname, Double remit, String betMultiples, Integer deposit) {

		Users user = (Users) cs.get(Users.class, loginname);
		if (user == null || user.getWarnflag().equals(4))
			return "抱歉，您不能进行DT转入。Q";

		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			String money = DtUtil.getamount(loginname);
			if (null != money && org.apache.commons.lang3.math.NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取DT余额错误:" + money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家DT余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "DT平台金额必须小于5元,才能转入！";
		}
		String mul = betMultiples;
		String pno = seqDao.generateProposalPno(ProposalType.HB499);
		// String remark = "自助红包奖金转入DT游戏,"+mul+"倍流水，送" + remit;
		String remark = "自助红包奖金转入DT游戏5倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4DT4HB(transID, loginname, remit, remark, deposit);
		log.info("DT自助红包奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.HB499.getCode(), user.getLevel(),
				loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, null,
				null);
		proposal.setBetMultiples("5");//
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "dt";
		// 获取0点到领取时DT投注额
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.HB499.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforDt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "红包账户金额转至DT", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String selfTransferSlotHB(String loginname, Double remit, String betMultiples, Integer deposit) {

		Users user = (Users) cs.get(Users.class, loginname);
		if (user == null || user.getWarnflag().equals(4))
			return "抱歉，您不能进行老虎机平台转入。Q";

		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			// Double money = SlotUtil.getBalance(loginname);
			Double money = null;
			if (null != money) {
				remoteCredit = money;
			} else {
				return "获取老虎机平台余额错误:" + money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家老虎机平台余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "老虎机平台金额必须小于5元,才能转入！";
		}
		String mul = betMultiples;
		String pno = seqDao.generateProposalPno(ProposalType.HB499);
		// String remark = "自助红包奖金转入DT游戏,"+mul+"倍流水，送" + remit;
		String remark = "自助红包奖金转入老虎机游戏5倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4Slot4HB(transID, loginname, remit, remark, deposit);

		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.HB499.getCode(), user.getLevel(),
				loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, null,
				null);
		proposal.setBetMultiples("5");//
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "slot";
		// 获取0点到领取时DT投注额
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.HB499.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			// transferService.addTransferforDt(Long.parseLong(transID),
			// loginname, user.getCredit(), remit, Constants.IN, Constants.FAIL,
			// "红包账户金额转至老虎机平台", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 全民闯关金额转入qt
	 */
	@Override
	public String selfTransferQtEmigrated(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行QT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = QtUtil.getBalance(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取QT余额错误:" + money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家QT余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "QT平台金额必须小于5元,才能转入！";
		}
		String mul = "20";
		String pno = seqDao.generateProposalPno(ProposalType.EMIGRATED391);
		String remark = "全民闯关奖金转入QT游戏," + mul + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4Qt4Emigrated(transID, loginname, remit, remark);
		log.info("QT好友推荐奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.EMIGRATED391.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(mul);//
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "E" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "qt";
		// 获取0点到领取时的QT投注额
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
		prams.put("platform", "qt");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.EMIGRATED391.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforQt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 全民闯关金额转入ttg
	 */
	@Override
	public String selfTransferTtgEmigrated(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行TTG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = PtUtil1.getPlayerAccount(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取TTG余额错误:" + money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家TTG余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "TTG平台金额必须小于5元,才能转入！";
		}
		String mul = "20";
		String pno = seqDao.generateProposalPno(ProposalType.EMIGRATED391);
		String remark = "全民闯关奖励转入TTG," + mul + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4TTG4Emigrated(transID, loginname, remit, remark);
		log.info("TTG全民闯关奖励 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.EMIGRATED391.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(mul);//
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "E" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "ttg";
		// 获取0点到领取时的TTG投注额
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
		prams.put("platform", "ttg");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.EMIGRATED391.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforTt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AutoYouHuiVo selfYouHui90(String loginname, Double remit) {
		// PT首存优惠-68%-15倍
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();

		if (null == user || user.getWarnflag().equals(4)) {
			vo.setMessage("抱歉，您不能自助PT首存优惠。Q");
			return vo;
		}
		// else if(!user.getWarnflag().equals(3)){//对于非安全玩家，需要查询看 同姓名 同IP下
		// 三个月内是否领取过体验金
		// 查询同IP或者同姓名的所有玩家
		DetachedCriteria dc1 = DetachedCriteria.forClass(Users.class);
		dc1.add(Restrictions.or(Restrictions.eq("accountName", user.getAccountName()),
				Restrictions.eq("registerIp", user.getRegisterIp())));
		List<Users> list = transferService.findByCriteria(dc1);
		List listLoginName = new ArrayList();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			for (Users users : list) {
				listLoginName.add(users.getLoginname());
			}
		}
		// 查询这些玩家三个月内是否领过首存 如果没领过 则可以继续进行下去
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date begindate = calendar.getTime();// 当前日期减去三个月
		DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.in("loginname", listLoginName));
		criteria.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		criteria.add(Restrictions.eq("type", ProposalType.SELFPT90.getCode()));
		criteria.add(Restrictions.ge("createTime", begindate));
		List<Proposal> proposals = transferService.findByCriteria(criteria);
		if (null != proposals && proposals.size() > 0) {
			vo.setMessage("抱歉，您不能自助PT首存优惠。");
			return vo;
		}
		// }

		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("target", "newpt"));
		// dc.add(Restrictions.ne("remit", 8.00)) ;
		dc.add(Restrictions.sqlRestriction(" remark not like '%自助优惠' "));
		List transfers = transferService.findByCriteria(dc);
		if (null != transfers && transfers.size() > 0 && null != transfers.get(0)) {
			vo.setMessage("抱歉，您不是第一次转账到PT，不能自助PT首存优惠。");
		} else {
			// 后期远程金额
			Double remoteCredit = 0.00;
			try {
				remoteCredit = PtUtil.getPlayerMoney(loginname);
			} catch (Exception e) {
				e.printStackTrace();
				vo.setMessage("获取 玩家余额异常失败");
				return vo;
			}
			if (remoteCredit >= 5) {
				vo.setMessage("PT平台金额必须小于5元,才能自助优惠！");
				return vo;
			}

			// 送优惠 (不能超过888元)
			YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFPT90.getText(), user.getLevel());
			if (null == config) {
				vo.setMessage("优惠配置未开启");
				return vo;
			}

			// Double changeMoney = Math.abs(remit * 0.68) > 888 ? 888 :
			// Math.abs(remit * 0.68);
			Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
					: Math.abs(Arith.mul(remit, config.getPercent()));
			changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
			String pno = seqDao.generateProposalPno(ProposalType.SELFPT90);
			String remark = "PT自助首存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

			// 转账
			String transID = seqService.generateTransferID();
			String msg = transferService.transferPtAndSelfYouHuiInModify(transID, loginname, remit, remark);
			log.info("自助PT首存优惠 转账信息：" + msg);
			if (null != msg) {
				vo.setMessage(msg);
				return vo;
			}

			Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFPT90.getCode(),
					user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
					Constants.FROM_FRONT, null, null);
			proposal.setBetMultiples(config.getBetMultiples().toString());
			proposal.setGifTamount(changeMoney);
			proposal.setExecuteTime(new Date());

			String sqlCouponId = seqDao.generateYhjID();
			String codeOne = dfh.utils.StringUtil.getRandomString(3);
			String codeTwo = dfh.utils.StringUtil.getRandomString(3);
			String shippingcode = "L" + codeOne + sqlCouponId + codeTwo;// 首存开头L
			proposal.setShippingCode(shippingcode);

			proposalService.save(offer);
			proposalService.save(proposal);

			user.setShippingcodePt(shippingcode);
			cs.update(user);
			// 记录下到目前为止的投注额度 begin
			String platform = "pttiger";
			PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0); // validBet位null表示还没有通过定时器刷入投注额
			proposalService.save(record);
			// 记录下到目前为止的投注额度 end

			Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname, LockMode.UPGRADE);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				// status.setTouzhuflag(0);
				status.setCashinwrong(0);
				proposalService.save(status);
			}

			vo.setGiftMoney(Arith.add(changeMoney, remit));
			vo.setMessage("自助PT首存优惠成功SUCCESS");
		}
		return vo;
	}

	public AutoYouHuiVo selfYouHui91(String loginname, Double remit) {
		// PT首存优惠-68%-15倍
		Users user = (Users) cs.get(Users.class, loginname);

		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			vo.setMessage("玩家不存在。");
			return vo;
		}

		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			remoteCredit = PtUtil.getPlayerMoney(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("PT平台金额必须小于5元,才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFPT91.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFPT91)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			vo.setMessage("您正在使用手工再存优惠,不能使用自助再存。");
			return vo;
		}

		// 送优惠 (不能超过588元)
		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFPT91);
		String remark = "PT自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.transferPtAndSelfYouHuiInModify(transID, loginname, remit, remark);
		log.info("自助PT次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFPT91.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "M" + codeOne + sqlCouponId + codeTwo;// 开头M
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);

		user.setShippingcodePt(shippingcode);
		cs.update(user);
		// 记录下到目前为止的投注额度 begin
		String platform = "pttiger";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname, LockMode.UPGRADE);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助PT次存优惠成功SUCCESS");
		return vo;
	}

	@Override
	public AutoYouHuiVo selfYouHui92(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			vo.setMessage("玩家不存在。");
			return vo;
		}

		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			remoteCredit = RemoteCaller.queryCredit(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家余额异常失败");
			return vo;
		}
		if (remoteCredit >= 20) {
			vo.setMessage("EA平台金额必须小于20,才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFPT92.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFPT92)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(remit * config.getPercent());

		String pno = seqDao.generateProposalPno(ProposalType.SELFPT92);
		String remark = "EA自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.transferEaIn(transID, loginname, remit);
		log.info("自助EA次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFPT92.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "E" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);

		user.setShippingcode(shippingcode);
		cs.update(user);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(1);
			proposalService.save(status);
		} else {
			// userstatus.setTouzhuflag(1);
			proposalService.update(userstatus);
		}

		try {
			DepositPendingResponseBean depositPendingResponseBean = RemoteCaller.depositPendingRequest(loginname,
					changeMoney + remit, transID, user.getAgcode());
			if (null != depositPendingResponseBean
					&& depositPendingResponseBean.getStatus().equals(ErrorCode.SUCCESS.getCode())) {
				RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(),
						ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), user.getAgcode());

				transferService.addTransfer(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
						Constants.FAIL, depositPendingResponseBean.getPaymentid(), null);
				if (StringUtils.isNotBlank(user.getShippingcode())) {
					transferService.updateUserShippingcodeSql(user);
				}
				vo.setMessage("自助EA次存优惠成功");
			} else {
				vo.setMessage("自助EA次存优惠失败，请联系客服！");
				throw new GenericDfhRuntimeException("自助优惠转账不成功  ， 数据回滚。");
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("自助EA次存优惠失败，请联系客服！");
			throw new GenericDfhRuntimeException("自助优惠转账不成功  ， 数据回滚。");
		}

		return vo;
	}

	/**
	 * AG自助次存优惠
	 */
	@SuppressWarnings("unused")
	@Override
	public AutoYouHuiVo selfYouHui93(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			vo.setMessage("玩家不存在。");
			return vo;
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = RemoteCaller.queryDspCredit(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				vo.setMessage("获取AG余额错误:" + money);
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家余额异常失败");
			return vo;
		}
		if (remoteCredit >= 20) {
			vo.setMessage("AG平台金额必须小于20,才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFPT93.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFPT93)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(remit * config.getPercent());

		String pno = seqDao.generateProposalPno(ProposalType.SELFPT93);
		String remark = "AG自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.transferInforDspIn(transID, loginname, remit);

		log.info("自助AG次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFPT93.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "E" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "ag";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFPT93.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(1);
			proposalService.save(status);
		} else {
			// userstatus.setTouzhuflag(1);
			proposalService.update(userstatus);
		}

		try {
			DspResponseBean dspResponseBean = RemoteCaller.depositPrepareDspRequest(loginname, changeMoney + remit,
					transID);
			DspResponseBean dspConfirmResponseBean = null;
			if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
				dspConfirmResponseBean = RemoteCaller.depositConfirmDspRequest(loginname, changeMoney + remit, transID,
						1);
				if (null != dspConfirmResponseBean && dspConfirmResponseBean.getInfo().equals("0")) {
					transferService.addTransferforDsp(Long.parseLong(transID), loginname, user.getCredit(), remit,
							Constants.IN, Constants.FAIL, "", null);
					vo.setMessage("自助AG次存优惠成功");
				} else {
					vo.setMessage("自助AG次存优惠失败，请联系客服！");
					throw new GenericDfhRuntimeException("自助优惠转账不成功  ， 数据回滚。");
				}
			} else {
				vo.setMessage("自助AG次存优惠失败，请联系客服！");
				throw new GenericDfhRuntimeException("自助优惠转账不成功  ， 数据回滚。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
	}

	/**
	 * AGIN自助次存优惠
	 */
	@Override
	public AutoYouHuiVo selfYouHui94(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			vo.setMessage("玩家不存在。");
			return vo;
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = RemoteCaller.queryDspAginCredit(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				vo.setMessage("获取AGIN余额错误:" + money);
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家余额异常失败");
			return vo;
		}
		if (remoteCredit >= 20) {
			vo.setMessage("AGIN平台金额必须小于20,才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFPT94.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFPT94)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(remit * config.getPercent());

		String pno = seqDao.generateProposalPno(ProposalType.SELFPT94);
		String remark = "AGIN自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.transferInforAginDspIn(transID, loginname, remit);
		log.info("自助AGIN次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFPT94.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "F" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "agin";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFPT94.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(1);
			proposalService.save(status);
		} else {
			// userstatus.setTouzhuflag(1);
			proposalService.update(userstatus);
		}

		try {
			DspResponseBean dspResponseBean = RemoteCaller.depositPrepareDspAginRequest(loginname, changeMoney + remit,
					transID);
			DspResponseBean dspConfirmResponseBean = null;
			if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
				dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, changeMoney + remit,
						transID, 1);
				if (null != dspConfirmResponseBean && dspConfirmResponseBean.getInfo().equals("0")) {
					transferService.addTransferforAginDsp(Long.parseLong(transID), loginname, user.getCredit(), remit,
							Constants.IN, Constants.FAIL, "", null);
					vo.setMessage("自助AGIN次存优惠成功");
				} else {
					vo.setMessage("自助AGIN次存优惠失败，请联系客服！");
					throw new GenericDfhRuntimeException("自助优惠转账不成功  ， 数据回滚。");
				}
			} else {
				vo.setMessage("自助AGIN次存优惠失败，请联系客服！");
				throw new GenericDfhRuntimeException("自助优惠转账不成功  ， 数据回滚。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
	}

	@Override
	public AutoYouHuiVo selfYouHui95(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			vo.setMessage("玩家不存在。");
			return vo;
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = RemoteCaller.queryBbinCredit(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				vo.setMessage("获取BBIN余额错误:" + money);
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("BBIN平台金额必须小于5,才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFPT95.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFPT95)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(remit * config.getPercent());

		String pno = seqDao.generateProposalPno(ProposalType.SELFPT95);
		String remark = "BBIN自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.transferInforBbinIn(transID, loginname, remit);
		log.info("自助BBIN次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFPT95.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "H" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "bbin";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFPT95.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(1);
			proposalService.save(status);
		} else {
			// userstatus.setTouzhuflag(1);
			proposalService.update(userstatus);
		}

		try {
			DspResponseBean dspResponseBean = RemoteCaller.depositBbinRequest(loginname,
					remit.intValue() + changeMoney.intValue(), user.getCredit(), transID);
			log.info(dspResponseBean.getInfo());
			if (null != dspResponseBean && dspResponseBean.getInfo().equals("11100")) {
				transferService.addTransferforBbin(Long.parseLong(transID), loginname, user.getCredit(), remit,
						Constants.IN, Constants.FAIL, "", "转入成功");
				vo.setMessage("自助BBIN次存优惠成功");
			} else {
				vo.setMessage("自助BBIN次存优惠失败，请联系客服！");
				throw new GenericDfhRuntimeException("自助优惠转账不成功  ， 数据回滚。");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
	}

	@Override
	public AutoYouHuiVo selfYouHui96(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (user.getWarnflag().equals(3) || user.getWarnflag().equals(0))) {

		} else {
			vo.setMessage("抱歉，您不能自助EBET首存优惠。Q");
			return vo;
		}

		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("target", "ebet"));
		// dc.add(Restrictions.ne("remit", 8.00)) ;
		dc.add(Restrictions.sqlRestriction(" remark not like '%自助优惠' "));
		List transfers = transferService.findByCriteria(dc);
		if (null != transfers && transfers.size() > 0 && null != transfers.get(0)) {
			vo.setMessage("抱歉，您不是第一次转账到EBET，不能自助EBET首存优惠。");
			return vo;
		} else {
			// 后期远程金额
			Double remoteCredit = 0.00;

			try {
				String money = RemoteCaller.queryEbetCredit(loginname);
				if (null != money && NumberUtils.isNumber(money)) {
					remoteCredit = Double.valueOf(money);
				} else {
					vo.setMessage("获取EBET余额错误:" + money);
					return vo;
				}
			} catch (Exception e) {
				e.printStackTrace();
				vo.setMessage("获取 玩家余额异常失败");
				return vo;
			}
			if (remoteCredit >= 20) {
				vo.setMessage("EBET平台金额必须小于20,才能自助优惠！");
				return vo;
			}

			YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFEBET96.getText(), user.getLevel());
			if (null == config) {
				vo.setMessage("优惠配置未开启");
				return vo;
			}
			// 判断优惠使用剩余次数是否够用
			if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFEBET96)) {
				vo.setMessage("超过最大使用次数。");
				return vo;
			}

			Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
					: Math.abs(remit * config.getPercent());

			String pno = seqDao.generateProposalPno(ProposalType.SELFEBET96);
			String remark = "EBET自助首存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

			// 转账
			String transID = seqService.generateTransferID();
			String msg = transferService.transferInforEbetIn(transID, loginname, remit,
					ProposalType.SELFEBET96.getText(), remark);
			log.info("EBET自助首存优惠 转账信息：" + msg);
			if (null != msg) {
				vo.setMessage(msg);
				return vo;
			}

			Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFEBET96.getCode(),
					user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
					Constants.FROM_FRONT, null, null);
			proposal.setBetMultiples(config.getBetMultiples().toString());
			proposal.setGifTamount(changeMoney);
			proposal.setExecuteTime(new Date());

			String sqlCouponId = seqDao.generateYhjID();
			String codeOne = dfh.utils.StringUtil.getRandomString(3);
			String codeTwo = dfh.utils.StringUtil.getRandomString(3);
			String shippingcode = "I" + codeOne + sqlCouponId + codeTwo;
			proposal.setShippingCode(shippingcode);

			proposalService.save(offer);
			proposalService.save(proposal);

			// 记录下到目前为止的投注额度 begin
			String platform = "ebet";
			PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
			proposalService.save(record);
			// 记录下到目前为止的投注额度 end

			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(pno);
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalType.SELFEBET96.getText());
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			proposalService.save(selfRecord);

			Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				// status.setTouzhuflag(1);
				proposalService.save(status);
			} else {
				// userstatus.setTouzhuflag(1);
				proposalService.update(userstatus);
			}

			try {
				// DspResponseBean dspResponseBean =
				// RemoteCaller.depositBbinRequest(loginname,
				// remit.intValue()+changeMoney.intValue(), user.getCredit(),
				// transID);
				// TODO 开始Ebet转账
				String ebetT = RemoteCaller.tradeEbetRequest(loginname, remit.intValue() + changeMoney.intValue(), "IN",
						transID);
				log.info("Ebet优惠转账错误消息(成功为空):" + ebetT);
				if (null == ebetT && StringUtils.isEmpty(ebetT)) {
					transferService.addTransferforEbet(Long.parseLong(transID), loginname, user.getCredit(), remit,
							Constants.IN, Constants.FAIL, "", "转入成功");
					vo.setMessage("EBET自助首存优惠成功");
				} else {
					vo.setMessage("EBET自助首存优惠失败，请联系客服！");
					throw new GenericDfhRuntimeException("自助优惠转账不成功  ， 数据回滚。");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return vo;
		}
	}

	@Override
	public AutoYouHuiVo selfYouHui97(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (user.getWarnflag().equals(3) || user.getWarnflag().equals(0))) {

		} else {
			vo.setMessage("抱歉，您不能自助EBET次存优惠。Q");
			return vo;
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = RemoteCaller.queryEbetCredit(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				vo.setMessage("获取EBET余额错误:" + money);
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家余额异常失败");
			return vo;
		}
		if (remoteCredit >= 20) {
			vo.setMessage("EBET平台金额必须小于20,才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFEBET97.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFEBET97)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(remit * config.getPercent());

		String pno = seqDao.generateProposalPno(ProposalType.SELFEBET97);
		String remark = "EBET自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.transferInforEbetIn(transID, loginname, remit, ProposalType.SELFEBET97.getText(),
				remark);
		log.info("EBET自助次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFEBET97.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "I" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "ebet";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFEBET97.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(1);
			proposalService.save(status);
		} else {
			// userstatus.setTouzhuflag(1);
			proposalService.update(userstatus);
		}

		try {
			// DspResponseBean dspResponseBean =
			// RemoteCaller.depositBbinRequest(loginname,
			// remit.intValue()+changeMoney.intValue(), user.getCredit(),
			// transID);
			// TODO 开始Ebet转账
			String ebetT = RemoteCaller.tradeEbetRequest(loginname, remit.intValue() + changeMoney.intValue(), "IN",
					transID);
			log.info("Ebet优惠转账错误消息(成功为空):" + ebetT);
			if (null == ebetT && StringUtils.isEmpty(ebetT)) {
				transferService.addTransferforEbet(Long.parseLong(transID), loginname, user.getCredit(), remit,
						Constants.IN, Constants.FAIL, "", "转入成功");
				vo.setMessage("EBET自助次存优惠成功");
			} else {
				vo.setMessage("EBET自助次存优惠失败，请联系客服！");
				throw new GenericDfhRuntimeException("自助优惠转账不成功  ， 数据回滚。");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo;
	}

	public String selfYouHui8Yuan(String loginname, String platform) {
		Users user = (Users) cs.get(Users.class, loginname);
		if (null == user) {
			return "玩家不存在。";
		}
		if (user.getFlag() == 1) {
			return "玩家被禁用";
		}
		boolean useFlag = cs.isUsePt8YouHui(loginname, user.getAccountName(), user.getRegisterIp());
		if (useFlag) {
			return "您已经使用过体验金或信息重复";
		}
		Double changeMoney = null;
		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFPT8.getText(), user.getLevel());
		if (null == config) {
			return "优惠配置未开启";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;
		String target = "";
		String creditChangeType = "";
		try {
			if (platform.equals("PT")) {
				remoteCredit = PtUtil.getPlayerMoney(loginname);
				target = RemoteConstant.PAGESITENEWPT;
				creditChangeType = CreditChangeType.TRANSFER_MEWPTIN.getCode();
			} else if (platform.equals("GPI")) {
				remoteCredit = GPIUtil.getBalance(loginname);
				target = RemoteConstant.PAGESITEGPI;
				creditChangeType = CreditChangeType.TRANSFER_GPIIN.getCode();
			} else if (platform.equals("TTG")) {
				remoteCredit = Double.valueOf(PtUtil1.getPlayerAccount(loginname));
				target = RemoteConstant.PAGESITETT;
				creditChangeType = CreditChangeType.TRANSFER_TTGIN.getCode();
			} else if (platform.equals("NT")) {
				JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
				remoteCredit = ntm.getDouble("balance");
				target = RemoteConstant.PAGESITENT;
				creditChangeType = CreditChangeType.TRANSFER_NTIN.getCode();
			} else if (platform.equals("QT")) {
				remoteCredit = Double.valueOf(QtUtil.getBalance(loginname));
				target = RemoteConstant.PAGESITEQT;
				creditChangeType = CreditChangeType.TRANSFER_QTIN.getCode();
			} else if (platform.equals("MG")) {
				// remoteCredit =
				// Double.valueOf(MGSUtil.getBalance(loginname,user.getPassword()))
				// ;
				remoteCredit = Double.valueOf(MGSUtil.getBalance(loginname));
				target = RemoteConstant.PAGESITEMG;
				creditChangeType = CreditChangeType.TRANSFER_MGIN.getCode();
			} else if (platform.equals("DT")) {
				remoteCredit = Double.valueOf(DtUtil.getamount(loginname));
				target = RemoteConstant.PAGESITEDT;
				creditChangeType = CreditChangeType.TRANSFER_DTIN.getCode();
			} else {
				return "无效平台";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家余额异常失败";
		}
		if (remoteCredit >= 1) {
			return platform + "平台金额必须小于等于1,才能使用体验金！";
		}

		changeMoney = config.getAmount();
		String name = platform + changeMoney + "元自助优惠";

		// 转账
		String transID = seqService.generatePt8TransferID(user.getId());

		transferDao.addTransferINNewPt8Yuan(Long.parseLong(transID), loginname, user.getCredit(), changeMoney, name,
				target);

		String pno = seqDao.generatePt8Yuan();
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFPT8.getCode(), user.getLevel(),
				loginname, changeMoney, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, name,
				null);
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		seqDao.save(proposal);
		Creditlogs log = new Creditlogs();
		log.setLoginname(loginname);
		log.setType(creditChangeType);
		log.setCredit(user.getCredit());
		log.setRemit(changeMoney);
		log.setNewCredit(user.getCredit());
		log.setRemark(name);
		log.setCreatetime(DateUtil.getCurrentTimestamp());
		seqDao.save(log);

		user.setWarnflag(3); // 使用了8元自助，安全等级设置为安全
		seqDao.update(user);

		String result = changeMoney + ";" + transID + ";" + name;
		return result;
	}

	@Override
	public YouHuiConfig queryYouHuiConfigSingle(String title, Integer level) {
		return youHuiConfigDao.getYouHuiConfig(title, level);
	}

	public List<YouHuiConfig> queryYouHuiConfig(Integer level) {
		return youHuiConfigDao.getAllYouHuiConfig(level);
	}

	/**
	 * 优惠使用此数是否超过总的次数
	 * 
	 * @return
	 */
	public Boolean youhuiContinueOrStop(Integer times, Integer timesflag, String loginname, ProposalType type) {
		return youHuiConfigDao.youhuiContinueOrStop(times, timesflag, loginname, type);
	}

	public TransferDao getTransferDao() {
		return transferDao;
	}

	public void setTransferDao(TransferDao transferDao) {
		this.transferDao = transferDao;
	}

	public YouHuiConfigDao getYouHuiConfigDao() {
		return youHuiConfigDao;
	}

	public void setYouHuiConfigDao(YouHuiConfigDao youHuiConfigDao) {
		this.youHuiConfigDao = youHuiConfigDao;
	}

	@Override
	public AutoYouHuiVo selfYouHui98(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			vo.setMessage("抱歉，您不能自助TTG首存优惠。Q");
			return vo;
		}
		// if(!user.getWarnflag().equals(3)){//对于非安全玩家，需要查询看 同姓名 同IP下
		// 三个月内是否领取过体验金
		// 查询同IP或者同姓名的所有玩家
		DetachedCriteria dc1 = DetachedCriteria.forClass(Users.class);
		dc1.add(Restrictions.or(Restrictions.eq("accountName", user.getAccountName()),
				Restrictions.eq("registerIp", user.getRegisterIp())));
		List<Users> list = transferService.findByCriteria(dc1);
		List listLoginName = new ArrayList();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			for (Users users : list) {
				listLoginName.add(users.getLoginname());
			}
		}
		// 查询这些玩家三个月内是否领过首存 如果没领过 则可以继续进行下去
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date begindate = calendar.getTime();// 当前日期减去三个月
		DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.in("loginname", listLoginName));
		criteria.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		criteria.add(Restrictions.eq("type", ProposalType.SELFEBET98.getCode()));
		criteria.add(Restrictions.ge("createTime", begindate));
		List<Proposal> proposals = transferService.findByCriteria(criteria);
		if (null != proposals && proposals.size() > 0) {
			vo.setMessage("抱歉，您不能自助TTG首存优惠。");
			return vo;
		}
		// }
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("target", "ttg"));
		// dc.add(Restrictions.ne("remit", 8.00)) ;
		dc.add(Restrictions.sqlRestriction(" remark not like '%自助优惠' "));
		List transfers = transferService.findByCriteria(dc);
		if (null != transfers && transfers.size() > 0 && null != transfers.get(0)) {
			vo.setMessage("抱歉，您不是第一次转账到ttg，不能自助ttg首存优惠。");
			return vo;
		} else {
			// 后期远程金额
			Double remoteCredit = 0.00;

			try {
				String money = PtUtil1.getPlayerAccount(loginname);
				if (null != money && NumberUtils.isNumber(money)) {
					remoteCredit = Double.valueOf(money);
				} else {
					vo.setMessage("获取ttg余额错误:" + money);
					return vo;
				}
			} catch (Exception e) {
				e.printStackTrace();
				vo.setMessage("获取 玩家余额异常失败");
				return vo;
			}
			if (remoteCredit >= 5) {
				vo.setMessage("ttg平台金额必须小于5元，才能自助优惠！");
				return vo;
			}

			YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFEBET98.getText(), user.getLevel());
			if (null == config) {
				vo.setMessage("优惠配置未开启");
				return vo;
			}
			// 判断优惠使用剩余次数是否够用
			if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFEBET98)) {
				vo.setMessage("超过最大使用次数。");
				return vo;
			}

			Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
					: Math.abs(Arith.mul(remit, config.getPercent()));
			changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
			String pno = seqDao.generateProposalPno(ProposalType.SELFEBET98);
			String remark = "TTG自助首存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

			// 转账
			String transID = seqService.generateTransferID();

			String msg = transferService.selfConpon4TTG(transID, loginname, remit, ProposalType.SELFEBET98.getText(),
					remark);
			log.info("TTG自助首存优惠 转账信息：" + msg);
			if (null != msg) {
				vo.setMessage(msg);
				return vo;
			}

			Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFEBET98.getCode(),
					user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
					Constants.FROM_FRONT, null, null);
			proposal.setBetMultiples(config.getBetMultiples().toString());
			proposal.setGifTamount(changeMoney);
			proposal.setExecuteTime(new Date());

			String sqlCouponId = seqDao.generateYhjID();
			String codeOne = dfh.utils.StringUtil.getRandomString(3);
			String codeTwo = dfh.utils.StringUtil.getRandomString(3);
			String shippingcode = "Y" + codeOne + sqlCouponId + codeTwo;
			proposal.setShippingCode(shippingcode);

			proposalService.save(offer);
			proposalService.save(proposal);

			// 记录下到目前为止的投注额度 begin
			String platform = "ttg";
			// 获取0点到领取时的TTG投注额
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
			prams.put("platform", "ttg");
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
			proposalService.save(record);
			// 记录下到目前为止的投注额度 end

			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(pno);
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalType.SELFEBET98.getText());
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			proposalService.save(selfRecord);

			Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				// status.setTouzhuflag(1);
				proposalService.save(status);
			} else {
				// userstatus.setTouzhuflag(1);
				proposalService.update(userstatus);
			}

			try {
				Boolean b = PtUtil1.addPlayerAccountPraper(loginname, Arith.add(remit, changeMoney));
				if (null != b && b) {
					transferService.addTransferforTt(Long.parseLong(transID), loginname, user.getCredit(), remit,
							Constants.IN, Constants.FAIL, "", "转入成功");
					vo.setMessage("TTG自助首存优惠成功");
				} else {
					vo.setMessage("TTG自助首存优惠失败，请联系客服！");
					throw new GenericDfhRuntimeException("TTG自助优惠转账不成功  ， 数据回滚。");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return vo;
		}
	}

	@Override
	public AutoYouHuiVo selfYouHui99(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			vo.setMessage("抱歉，您不能自助TTG次存优惠。Q");
			return vo;
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = PtUtil1.getPlayerAccount(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				vo.setMessage("获取TTG余额错误:" + money);
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家TTG余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("TTG平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFEBET99.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFEBET99)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFEBET99);
		String remark = "TTG自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4TTG(transID, loginname, remit, ProposalType.SELFEBET99.getText(),
				remark);
		log.info("TTG自助次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFEBET99.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "Z" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "ttg";
		// 获取0点到领取时的TTG投注额
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
		prams.put("platform", "ttg");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFEBET99.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助TTG次存优惠成功SUCCESS");
		return vo;
	}

	@Override
	public AutoYouHuiVo selfYouHui4GPI702(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();

		if (null != user && (!user.getWarnflag().equals(4))) {
		} else {
			vo.setMessage("抱歉，您不能自助GPI首存优惠");
			return vo;
		}
		if (!user.getWarnflag().equals(3)) {// 对于非安全玩家，需要查询看 同姓名 同IP下
											// 三个月内是否领取过体验金
			// 查询同IP或者同姓名的所有玩家
			DetachedCriteria dc1 = DetachedCriteria.forClass(Users.class);
			dc1.add(Restrictions.or(Restrictions.eq("accountName", user.getAccountName()),
					Restrictions.eq("registerIp", user.getRegisterIp())));
			List<Users> list = transferService.findByCriteria(dc1);
			List listLoginName = new ArrayList();
			if (list != null && list.size() > 0 && list.get(0) != null) {
				for (Users users : list) {
					listLoginName.add(users.getLoginname());
				}
			}
			// 查询这些玩家三个月内是否领过首存 如果没领过 则可以继续进行下去
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			Date begindate = calendar.getTime();// 当前日期减去三个月
			DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
			criteria.add(Restrictions.in("loginname", listLoginName));
			criteria.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			criteria.add(Restrictions.eq("type", ProposalType.SELFGPI702.getCode()));
			criteria.add(Restrictions.ge("createTime", begindate));
			List<Proposal> proposals = transferService.findByCriteria(criteria);
			if (null != proposals && proposals.size() > 0) {
				vo.setMessage("抱歉，您不能自助GPI首存优惠。");
				return vo;
			}
		}

		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("target", "gpi"));
		dc.add(Restrictions.sqlRestriction(" remark not like '%自助优惠' "));
		List transfers = transferService.findByCriteria(dc);
		if (null != transfers && transfers.size() > 0 && null != transfers.get(0)) {
			vo.setMessage("抱歉，您不是第一次转账到GPI，不能使用自助GPI首存优惠");
		} else {
			// 后期远程金额
			Double remoteCredit = 0.00;
			try {
				remoteCredit = GPIUtil.getBalance(loginname);
			} catch (Exception e) {
				e.printStackTrace();
				vo.setMessage("获取 玩家余额异常失败");
				return vo;
			}
			if (remoteCredit >= 5) {
				vo.setMessage("您的GPI账户余额超过5元，不能使用该优惠");
				return vo;
			}

			YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFGPI702.getText(), user.getLevel());
			if (null == config) {
				vo.setMessage("优惠配置未开启");
				return vo;
			}

			Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
					: Math.abs(Arith.mul(remit, config.getPercent()));
			changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
			String pno = seqDao.generateProposalPno(ProposalType.SELFGPI702);
			String remark = "GPI自助首存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

			// 转账
			String transID = seqService.generateTransferID();
			String msg = transferService.selfConpon4GPI(transID, user, remit, remark);
			log.info("自助GPI首存优惠 转账信息：" + msg);
			if (null != msg) {
				vo.setMessage(msg);
				return vo;
			}

			Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFGPI702.getCode(),
					user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
					Constants.FROM_FRONT, null, null);
			proposal.setBetMultiples(config.getBetMultiples().toString());
			proposal.setGifTamount(changeMoney);
			proposal.setExecuteTime(new Date());

			proposalService.save(offer);
			proposalService.save(proposal);

			// 记录下到目前为止的投注额度
			// 获取0点到领取时的GPI投注额
			String totalBetSql = "select sum(bet) from platform_data where loginname=:username and starttime>=:startTime and platform in(:gpi, :rslot, :png, :bs, :ctxm)";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			// prams.put("platform", "gpi");
			prams.put("gpi", "gpi");
			prams.put("rslot", "rslot");
			prams.put("png", "png");
			prams.put("bs", "bs");
			prams.put("ctxm", "ctxm");

			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(pno, loginname, "gpi", betAmount, new Date(), 0);
			proposalService.save(record);

			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(pno);
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform("gpi");
			selfRecord.setSelfname(ProposalType.SELFGPI702.getText());
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			proposalService.save(selfRecord);

			String deposit = GPIUtil.credit(loginname, Arith.add(changeMoney, remit), transID);
			if (deposit != null && deposit.equals(GPIUtil.GPI_SUCCESS_CODE)) {
				vo.setMessage("自助GPI次存优惠成功");
			} else {
				vo.setMessage("自助GPI次存优惠失败，请联系客服！");
				throw new GenericDfhRuntimeException("自助优惠转账不成功，数据回滚");
			}
			return vo;
		}
		return vo;

	}

	@Override
	public AutoYouHuiVo selfYouHui4GPI703(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			vo.setMessage("玩家不存在");
			return vo;
		}
		if (user.getFlag() == 1) {
			vo.setMessage("该账号已经禁用");
			return vo;
		}
		Double localCredit = user.getCredit();
		if (localCredit < remit) {
			vo.setMessage("额度不足");
			return vo;
		}
		Double remoteCredit = 0.00;
		try {
			remoteCredit = GPIUtil.getBalance(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("GPI账户余额必须小于5元,才能使用该优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFGPI703.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFGPI703)) {
			vo.setMessage("超过最大使用次数");
			return vo;
		}

		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			vo.setMessage("您正在使用手工再存优惠,不能使用自助再存。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFGPI703);
		String remark = "GPI自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4GPI(transID, user, remit, remark);
		log.info("自助GPI次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFGPI703.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		/*
		 * String sqlCouponId = seqDao.generateYhjID(); String codeOne =
		 * dfh.utils.StringUtil.getRandomString(3); String codeTwo =
		 * dfh.utils.StringUtil.getRandomString(3); String shippingcode = "M" +
		 * codeOne + sqlCouponId + codeTwo ;//开头M
		 * proposal.setShippingCode(shippingcode);
		 */

		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度
		// 获取0点到领取时的GPI投注额
		String totalBetSql = "select sum(bet) from platform_data where loginname=:username and starttime>=:startTime and platform in(:gpi, :rslot, :png, :bs, :ctxm)";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		prams.put("startTime", cd.getTime());
		// prams.put("platform", "gpi");
		prams.put("gpi", "gpi");
		prams.put("rslot", "rslot");
		prams.put("png", "png");
		prams.put("bs", "bs");
		prams.put("ctxm", "ctxm");

		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, "gpi", betAmount, new Date(), 0);

		proposalService.save(record);

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform("gpi");
		selfRecord.setSelfname(ProposalType.SELFGPI703.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		String deposit = GPIUtil.credit(loginname, Arith.add(changeMoney, remit), transID);
		if (deposit != null && deposit.equals(GPIUtil.GPI_SUCCESS_CODE)) {
			vo.setMessage("自助GPI次存优惠成功");
		} else {
			vo.setMessage("自助GPI次存优惠失败，请联系客服！");
			throw new GenericDfhRuntimeException("自助优惠转账不成功，数据回滚");
		}
		return vo;
	}

	@Override
	public AutoYouHuiVo selfYouHui4GPI704(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			vo.setMessage("玩家不存在");
			return vo;
		}
		if (user.getFlag() == 1) {
			vo.setMessage("该账号已经禁用");
			return vo;
		}
		Double localCredit = user.getCredit();
		if (localCredit < remit) {
			vo.setMessage("额度不足");
			return vo;
		}
		Double remoteCredit = 0.00;
		try {
			remoteCredit = GPIUtil.getBalance(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取玩家余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5.0) {
			vo.setMessage("GPI账户余额必须小于5元,才能使用该优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFGPI704.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFGPI704)) {
			vo.setMessage("超过最大使用次数");
			return vo;
		}

		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			vo.setMessage("您正在使用手工再存优惠,不能使用自助再存。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFGPI704);
		String remark = "GPI限时存送优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4GPI(transID, user, remit, remark);
		log.info("自助GPI限时存送优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFGPI704.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度
		// 获取0点到领取时的GPI投注额
		String totalBetSql = "select sum(bet) from platform_data where loginname=:username and starttime>=:startTime and platform in(:gpi, :rslot, :png, :bs, :ctxm)";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		prams.put("startTime", cd.getTime());
		// prams.put("platform", "gpi");
		prams.put("gpi", "gpi");
		prams.put("rslot", "rslot");
		prams.put("png", "png");
		prams.put("bs", "bs");
		prams.put("ctxm", "ctxm");

		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, "gpi", betAmount, new Date(), 0);

		proposalService.save(record);

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform("gpi");
		selfRecord.setSelfname(ProposalType.SELFGPI704.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		String deposit = GPIUtil.credit(loginname, Arith.add(changeMoney, remit), transID);
		if (deposit != null && deposit.equals(GPIUtil.GPI_SUCCESS_CODE)) {
			vo.setMessage("自助GPI限时存送优惠成功");
		} else {
			vo.setMessage("自助GPI限时存送优惠失败，请联系客服！");
			throw new GenericDfhRuntimeException("自助优惠转账不成功，数据回滚");
		}
		return vo;
	}

	// PT限时优惠
	@Override
	public AutoYouHuiVo selfYouHui4PT705(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			vo.setMessage("玩家不存在。");
			return vo;
		}

		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			remoteCredit = PtUtil.getPlayerMoney(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("PT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFPT705.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFPT705)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		// 查询后台是否有手动提交再存优惠
		/*
		 * DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		 * offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		 * offerDc.add(Restrictions.eq("flag",
		 * ProposalFlagType.EXCUTED.getCode()));
		 * offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		 * offerDc.add(Restrictions.eq("remark", "BEGIN"+user.getLoginname()));
		 * List<Proposal> offers = proposalService.findByCriteria(offerDc);
		 * if(null != offers && offers.size()>0){
		 * vo.setMessage("您正在使用手工再存优惠,不能使用自助再存。"); return vo; }
		 */

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFPT705);
		String remark = "PT限时优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.transferPtAndSelfYouHuiInModify(transID, loginname, remit, remark);
		log.info("自助PT限时优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFPT705.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "X" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		transferDao.save(offer);
		transferDao.save(proposal);

		user.setShippingcodePt(shippingcode);
		transferDao.update(user);
		// 记录下到目前为止的投注额度 begin
		String platform = "pttiger";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		transferDao.save(record);
		// 记录下到目前为止的投注额度 end

		Userstatus userstatus = (Userstatus) transferDao.get(Userstatus.class, loginname, LockMode.UPGRADE);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			transferDao.save(status);
		}

		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助PT限时优惠成功SUCCESS");
		return vo;
	}

	// TTG限时优惠
	@Override
	public AutoYouHuiVo selfYouHui4TTG706(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			vo.setMessage("抱歉，您不能自助TTG限时优惠。Q");
			return vo;
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = PtUtil1.getPlayerAccount(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				vo.setMessage("获取TTG余额错误:" + money);
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家TTG余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("TTG平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFTTG706.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFTTG706)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFTTG706);
		String remark = "TTG自助限时优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4TTG(transID, loginname, remit, ProposalType.SELFTTG706.getText(),
				remark);
		log.info("TTG自助限时优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFTTG706.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "TX" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		transferDao.save(offer);
		transferDao.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "ttg";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的GPI投注额
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
		prams.put("platform", "ttg");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		transferDao.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFTTG706.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		transferDao.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			transferDao.save(status);
		}
		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助TTG限时优惠成功SUCCESS");
		return vo;
	}

	/* QT优惠start */
	@Override
	public AutoYouHuiVo selfYouHuiQTFirst(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user || user.getWarnflag().equals(4)) {
			vo.setMessage("抱歉，您不能自助QT首存优惠。Q");
			return vo;
		}
		// if(!user.getWarnflag().equals(3)){//对于非安全玩家，需要查询看 同姓名 同IP下
		// 三个月内是否领取过体验金
		// 查询同IP或者同姓名的所有玩家
		DetachedCriteria dc1 = DetachedCriteria.forClass(Users.class);
		dc1.add(Restrictions.or(Restrictions.eq("accountName", user.getAccountName()),
				Restrictions.eq("registerIp", user.getRegisterIp())));
		List<Users> list = transferService.findByCriteria(dc1);
		List listLoginName = new ArrayList();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			for (Users users : list) {
				listLoginName.add(users.getLoginname());
			}
		}
		// 查询这些玩家三个月内是否领过首存 如果没领过 则可以继续进行下去
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date begindate = calendar.getTime();// 当前日期减去三个月
		DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.in("loginname", listLoginName));
		criteria.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		criteria.add(Restrictions.eq("type", ProposalType.SELFQTFIRST.getCode()));
		criteria.add(Restrictions.ge("createTime", begindate));
		List<Proposal> proposals = transferService.findByCriteria(criteria);
		if (null != proposals && proposals.size() > 0) {
			vo.setMessage("抱歉，您不能自助QT首存优惠。");
			return vo;
		}
		// }
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("target", "qt"));
		// dc.add(Restrictions.ne("remit", 8.00)) ;
		dc.add(Restrictions.sqlRestriction(" remark not like '%自助优惠' "));
		List transfers = transferService.findByCriteria(dc);
		if (null != transfers && transfers.size() > 0 && null != transfers.get(0)) {
			vo.setMessage("抱歉，您不是第一次转账到QT，不能自助QT首存优惠。");
			return vo;
		} else {
			// 获取远程金额
			Double remoteCredit = 0.00;
			try {
				String money = QtUtil.getBalance(loginname);
				if (null != money && NumberUtils.isNumber(money)) {
					remoteCredit = Double.valueOf(money);
				} else {
					vo.setMessage("获取QT余额错误:" + money);
					return vo;
				}
			} catch (Exception e) {
				e.printStackTrace();
				vo.setMessage("获取 玩家余额异常失败");
				return vo;
			}
			if (remoteCredit >= 5) {
				vo.setMessage("QT平台金额必须小于5元，才能自助优惠！");
				return vo;
			}

			YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFQTFIRST.getText(), user.getLevel());
			if (null == config) {
				vo.setMessage("优惠配置未开启");
				return vo;
			}
			// 判断优惠使用剩余次数是否够用
			if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFQTFIRST)) {
				vo.setMessage("超过最大使用次数。");
				return vo;
			}

			Double changeMoney = Math.abs(Arith.mul(remit, config.getPercent())) > config.getLimitMoney()
					? config.getLimitMoney() : Math.abs(Arith.mul(remit, config.getPercent()));
			changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
			String pno = seqDao.generateProposalPno(ProposalType.SELFQTFIRST);
			String remark = "QT自助首存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

			// 转账
			String transID = seqService.generateTransferID();

			String msg = transferService.selfCouponQT(transID, loginname, remit, ProposalType.SELFQTFIRST.getText(),
					remark);
			log.info("QT自助首存优惠 转账信息：" + msg);
			if (null != msg) {
				vo.setMessage(msg);
				return vo;
			}

			Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFQTFIRST.getCode(),
					user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
					Constants.FROM_FRONT, null, null);
			proposal.setBetMultiples(config.getBetMultiples().toString());
			proposal.setGifTamount(changeMoney);
			proposal.setExecuteTime(new Date());

			/*
			 * TODO 优惠券码生成注释掉 String sqlCouponId = seqDao.generateYhjID();
			 * String codeOne = dfh.utils.StringUtil.getRandomString(3); String
			 * codeTwo = dfh.utils.StringUtil.getRandomString(3); String
			 * shippingcode = "NTF" + codeOne + sqlCouponId + codeTwo;
			 * proposal.setShippingCode(shippingcode);
			 */

			proposalService.save(offer);
			proposalService.save(proposal);

			// 记录下到目前为止的投注额度 begin
			String platform = "qt";
			// 获取0点到领取时的qt投注额
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
			prams.put("platform", "qt");
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
			proposalService.save(record);
			// 记录下到目前为止的投注额度 end

			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(pno);
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalType.SELFQTFIRST.getText());
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			proposalService.save(selfRecord);

			Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				// status.setTouzhuflag(1);
				proposalService.save(status);
			} else {
				// userstatus.setTouzhuflag(1);
				proposalService.update(userstatus);
			}

			try {
				String b = QtUtil.getDepositPlayerMoney(loginname, Arith.add(remit, changeMoney), transID);
				if (null != b && QtUtil.RESULT_SUCC.equals(b)) {
					transferService.addTransferforQt(Long.parseLong(transID), loginname, user.getCredit(), remit,
							Constants.IN, Constants.FAIL, "", "转入成功");
					vo.setMessage("QT自助首存优惠成功");
				} else {
					vo.setMessage("QT自助首存优惠失败，请联系客服！");
					throw new GenericDfhRuntimeException("QT自助优惠转账不成功  ， 数据回滚。");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return vo;
		}
	}

	/**
	 * QT次存优惠
	 */
	@Override
	public AutoYouHuiVo selfYouHuiQTTwice(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user || user.getWarnflag().equals(4)) {
			vo.setMessage("抱歉，您不能自助QT次存优惠。Q");
			return vo;
		}

		// 获取远程金额
		Double remoteCredit = 0.00;

		try {
			String money = QtUtil.getBalance(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				vo.setMessage("获取QT余额错误:" + money);
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家QT余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("QT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFQTTWICE.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFQTTWICE)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			vo.setMessage("您正在使用手工再存优惠,不能使用自助再存。");
			return vo;
		}

		Double changeMoney = Math.abs(Arith.mul(remit, config.getPercent())) > config.getLimitMoney()
				? config.getLimitMoney() : Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFQTTWICE);
		String remark = "QT自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfCouponQT(transID, loginname, remit, ProposalType.SELFQTTWICE.getText(),
				remark);
		log.info("QT自助次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFQTTWICE.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		/*
		 * String sqlCouponId = seqDao.generateYhjID(); String codeOne =
		 * dfh.utils.StringUtil.getRandomString(3); String codeTwo =
		 * dfh.utils.StringUtil.getRandomString(3); String shippingcode = "Z" +
		 * codeOne + sqlCouponId + codeTwo;
		 * proposal.setShippingCode(shippingcode);
		 */

		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "qt";
		// 获取0点到领取时的QT投注额
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
		prams.put("platform", "qt");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFQTTWICE.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		try {
			String b = QtUtil.getDepositPlayerMoney(loginname, Arith.add(remit, changeMoney), transID);
			if (null != b && QtUtil.RESULT_SUCC.equals(b)) {
				transferService.addTransferforQt(Long.parseLong(transID), loginname, user.getCredit(), remit,
						Constants.IN, Constants.FAIL, "", "转入成功");
				vo.setMessage("QT自助次存优惠成功");
			} else {
				vo.setMessage("QT自助次存优惠失败，请联系客服！");
				throw new GenericDfhRuntimeException("QT自助次存优惠转账不成功  ， 数据回滚。");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		vo.setGiftMoney(Arith.add(remit, changeMoney));
		return vo;
	}

	@Override
	public AutoYouHuiVo selfYouHuiQTSpec(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user || user.getWarnflag().equals(4)) {
			vo.setMessage("抱歉，您不能自助QT限时优惠。Q");
			return vo;
		}

		// 获取远程金额
		Double remoteCredit = 0.00;
		try {
			String money = QtUtil.getBalance(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				vo.setMessage("获取QT余额错误:" + money);
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家QT余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("QT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFQTSPEC.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFQTSPEC)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(Arith.mul(remit, config.getPercent())) > config.getLimitMoney()
				? config.getLimitMoney() : Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFQTSPEC);
		String remark = "QT自助限时优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfCouponQT(transID, loginname, remit, ProposalType.SELFQTSPEC.getText(), remark);
		log.info("QT自助限时优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFQTSPEC.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		/*
		 * String sqlCouponId = seqDao.generateYhjID(); String codeOne =
		 * dfh.utils.StringUtil.getRandomString(3); String codeTwo =
		 * dfh.utils.StringUtil.getRandomString(3); String shippingcode = "TX" +
		 * codeOne + sqlCouponId + codeTwo;
		 * proposal.setShippingCode(shippingcode);
		 */

		transferDao.save(offer);
		transferDao.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "qt";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的QT投注额
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
		prams.put("platform", "qt");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFQTSPEC.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		transferDao.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			transferDao.save(status);
		}

		try {
			String b = QtUtil.getDepositPlayerMoney(loginname, Arith.add(remit, changeMoney), transID);
			if (null != b && QtUtil.RESULT_SUCC.equals(b)) {
				transferService.addTransferforQt(Long.parseLong(transID), loginname, user.getCredit(), remit,
						Constants.IN, Constants.FAIL, "", "转入成功");
				vo.setMessage("QT限时优惠成功");
			} else {
				vo.setMessage("QT限时优惠失败，请联系客服！");
				throw new GenericDfhRuntimeException("QT限时优惠转账不成功  ， 数据回滚。");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		vo.setGiftMoney(changeMoney + remit);
		// vo.setMessage("自助TTG限时优惠成功SUCCESS");
		return vo;
	}
	/* QT优惠end */

	/* NT优惠start */
	@Override
	public AutoYouHuiVo selfYouHuiNTFirst(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user || user.getWarnflag().equals(4)) {
			vo.setMessage("抱歉，您不能自助NT首存优惠。Q");
			return vo;
		}
		// if(!user.getWarnflag().equals(3)){//对于非安全玩家，需要查询看 同姓名 同IP下
		// 三个月内是否领取过体验金
		// 查询同IP或者同姓名的所有玩家
		DetachedCriteria dc1 = DetachedCriteria.forClass(Users.class);
		dc1.add(Restrictions.or(Restrictions.eq("accountName", user.getAccountName()),
				Restrictions.eq("registerIp", user.getRegisterIp())));
		List<Users> list = transferService.findByCriteria(dc1);
		List listLoginName = new ArrayList();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			for (Users users : list) {
				listLoginName.add(users.getLoginname());
			}
		}
		// 查询这些玩家三个月内是否领过首存 如果没领过 则可以继续进行下去
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date begindate = calendar.getTime();// 当前日期减去三个月
		DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.in("loginname", listLoginName));
		criteria.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		criteria.add(Restrictions.eq("type", ProposalType.SELFNTFIRST.getCode()));
		criteria.add(Restrictions.ge("createTime", begindate));
		List<Proposal> proposals = transferService.findByCriteria(criteria);
		if (null != proposals && proposals.size() > 0) {
			vo.setMessage("抱歉，您不能自助NT首存优惠。");
			return vo;
		}
		// }
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("target", "nt"));
		// dc.add(Restrictions.ne("remit", 8.00)) ;
		dc.add(Restrictions.sqlRestriction(" remark not like '%自助优惠' "));
		List transfers = transferService.findByCriteria(dc);
		if (null != transfers && transfers.size() > 0 && null != transfers.get(0)) {
			vo.setMessage("抱歉，您不是第一次转账到nt，不能自助nt首存优惠。");
			return vo;
		} else {
			// 获取远程金额
			Double remoteCredit = 0.00;
			try {
				JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
				remoteCredit = ntm.getBoolean("result") ? ntm.getDouble("balance") : null;
				if (remoteCredit == null) {
					vo.setMessage("获取nt余额错误:" + ntm.getString("error"));
					return vo;
				}
			} catch (Exception e) {
				e.printStackTrace();
				vo.setMessage("获取 玩家余额异常失败");
				return vo;
			}
			if (remoteCredit >= 5) {
				vo.setMessage("nt平台金额必须小于5元，才能自助优惠！");
				return vo;
			}

			YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFNTFIRST.getText(), user.getLevel());
			if (null == config) {
				vo.setMessage("优惠配置未开启");
				return vo;
			}
			// 判断优惠使用剩余次数是否够用
			if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFNTFIRST)) {
				vo.setMessage("超过最大使用次数。");
				return vo;
			}

			Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
					: Math.abs(Arith.mul(remit, config.getPercent()));
			changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
			String pno = seqDao.generateProposalPno(ProposalType.SELFNTFIRST);
			String remark = "NT自助首存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

			// 转账
			String transID = seqService.generateTransferID();

			String msg = transferService.selfCouponNT(transID, loginname, remit, ProposalType.SELFNTFIRST.getText(),
					remark);
			log.info("NT自助首存优惠 转账信息：" + msg);
			if (null != msg) {
				vo.setMessage(msg);
				return vo;
			}

			Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFNTFIRST.getCode(),
					user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
					Constants.FROM_FRONT, null, null);
			proposal.setBetMultiples(config.getBetMultiples().toString());
			proposal.setGifTamount(changeMoney);
			proposal.setExecuteTime(new Date());

			/*
			 * TODO 优惠券码生成注释掉 String sqlCouponId = seqDao.generateYhjID();
			 * String codeOne = dfh.utils.StringUtil.getRandomString(3); String
			 * codeTwo = dfh.utils.StringUtil.getRandomString(3); String
			 * shippingcode = "NTF" + codeOne + sqlCouponId + codeTwo;
			 * proposal.setShippingCode(shippingcode);
			 */

			proposalService.save(offer);
			proposalService.save(proposal);

			// 记录下到目前为止的投注额度 begin
			String platform = "nt";
			// 获取0点到领取时的nt投注额
			String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			// prams.put("platform", "nt");
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
			proposalService.save(record);
			// 记录下到目前为止的投注额度 end

			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(pno);
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalType.SELFNTFIRST.getText());
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			proposalService.save(selfRecord);

			Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				// status.setTouzhuflag(1);
				proposalService.save(status);
			} else {
				// userstatus.setTouzhuflag(1);
				proposalService.update(userstatus);
			}

			transferService.addTransferforNT(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", "转入成功");
			vo.setGiftMoney(Arith.add(remit, changeMoney));
			vo.setMessage("NT自助首存优惠成功SUCCESS");
			return vo;
		}
	}

	/**
	 * NT次存优惠
	 */
	@Override
	public AutoYouHuiVo selfYouHuiNTTwice(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user && user.getWarnflag().equals(4)) {
			vo.setMessage("抱歉，您不能自助NT次存优惠。");
			return vo;
		}

		// 获取远程金额
		Double remoteCredit = 0.00;

		try {
			JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
			remoteCredit = ntm.getBoolean("result") ? ntm.getDouble("balance") : null;
			if (remoteCredit == null) {
				vo.setMessage("获取nt余额错误:" + ntm.getString("error"));
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家NT余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("NT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFNTTWICE.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFNTTWICE)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			vo.setMessage("您正在使用手工再存优惠,不能使用自助再存。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFNTTWICE);
		String remark = "NT自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfCouponNT(transID, loginname, remit, ProposalType.SELFNTTWICE.getText(),
				remark);
		log.info("NT自助次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFNTTWICE.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		/*
		 * String sqlCouponId = seqDao.generateYhjID(); String codeOne =
		 * dfh.utils.StringUtil.getRandomString(3); String codeTwo =
		 * dfh.utils.StringUtil.getRandomString(3); String shippingcode = "Z" +
		 * codeOne + sqlCouponId + codeTwo;
		 * proposal.setShippingCode(shippingcode);
		 */

		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "nt";
		// 获取0点到领取时的NT投注额
		String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		prams.put("startTime", cd.getTime());
		// prams.put("platform", "nt");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFNTTWICE.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		transferService.addTransferforNT(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
				Constants.FAIL, "", "转入成功");
		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助NT次存优惠成功SUCCESS");
		return vo;
	}

	@Override
	public AutoYouHuiVo selfYouHuiNTSpec(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user && user.getWarnflag().equals(4)) {
			vo.setMessage("抱歉，您不能自助NT限时优惠。Q");
			return vo;
		}

		// 获取远程金额
		Double remoteCredit = 0.00;
		try {
			JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
			remoteCredit = ntm.getBoolean("result") ? ntm.getDouble("balance") : null;
			if (remoteCredit == null) {
				vo.setMessage("获取nt余额错误:" + ntm.getString("error"));
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家NT余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("NT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFNTSPEC.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFNTSPEC)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFNTSPEC);
		String remark = "NT自助限时优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfCouponNT(transID, loginname, remit, ProposalType.SELFNTSPEC.getText(), remark);
		log.info("NT自助限时优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFNTSPEC.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		/*
		 * String sqlCouponId = seqDao.generateYhjID(); String codeOne =
		 * dfh.utils.StringUtil.getRandomString(3); String codeTwo =
		 * dfh.utils.StringUtil.getRandomString(3); String shippingcode = "TX" +
		 * codeOne + sqlCouponId + codeTwo;
		 * proposal.setShippingCode(shippingcode);
		 */

		transferDao.save(offer);
		transferDao.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "nt";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的GPI投注额
		String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		prams.put("startTime", cd.getTime());
		// prams.put("platform", "nt");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		transferDao.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFNTSPEC.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		transferDao.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			transferDao.save(status);
		}

		transferService.addTransferforNT(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
				Constants.FAIL, "", "转入成功");
		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助NT限时优惠成功SUCCESS");
		return vo;
	}

	/* NT优惠end */
	@Override
	public Double getDeposit(String loginname) {
		return youHuiConfigDao.getDeposit(loginname);
	}

	@Override
	public String getBirthdayMoney(String loginname) {
		Users user = (Users) cs.get(Users.class, loginname);
		if (null == user) {
			return null;
		}

		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc.add(Restrictions.eq("loginname", user.getLoginname()));
		dc.add(Restrictions.eq("type", ProposalType.BIRTHDAY.getCode()));
		List<Proposal> list = proposalService.findByCriteria(dc);
		boolean bool = true;
		if (!list.isEmpty()) {
			for (Proposal proposal : list) {
				// 如果当期年份存在表示领取过
				if (DateUtil.getYear(new Date()).equals(DateUtil.getYear(proposal.getCreateTime()))) {
					bool = false;
					break;
				}
			}

		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.BIRTHDAY.getText(), user.getLevel());
		if (null == config) {
			return "未配置生日礼金活动奖励";
		}
		// 此处添加校验，判断当月存款达到100
		/*
		 * if(getDeposit(loginname)<100){ //"当月存款不足，需当月存款达到100。"; return null; }
		 */
		double limitMoney = config.getLimitMoney();
		int limitMoneyInt = (int) limitMoney;
		// 判断优惠使用剩余次数是否够用
		/*
		 * if(!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(),
		 * loginname, ProposalType.BIRTHDAY)){
		 * 
		 * return null; }
		 */
		/*
		 * Date currDate = new Date(); String year = DateUtil.getYear(currDate);
		 * String month = DateUtil.getMonth(user.getBirthday()); String day =
		 * DateUtil.getDay(user.getBirthday()); Date newDay =
		 * DateUtil.parseDateForStandard(year+"-"+month+"-"+day+" 00:00:00");
		 * Date beforeDays = DateUtil.getMontToDate(newDay, -3); Date afterDays
		 * = DateUtil.getMontToDate(newDay, 4); if(currDate.after(afterDays) ||
		 * currDate.before(beforeDays)){ return null; }
		 */
		int id = config.getId();
		if (limitMoneyInt > 0) {
			if (!bool) {
				return limitMoneyInt + "元" + id + "元0";
			}
			return limitMoneyInt + "元" + id + "元1";

		} else {
			return null;
		}

	}

	/**
	 * 签到金额转入mg
	 */
	@Override
	public String selfTransferMgSign(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行MG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			// remoteCredit = MGSUtil.getBalance(loginname,user.getPassword());
			remoteCredit = MGSUtil.getBalance(loginname);
			if (null == remoteCredit) {
				return "获取玩家MG游戏账户余额错误!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家MG游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "MG平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.SIGNDEPOSIT420);
		String remark = "MG签到奖金,10倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4MG4Sign(transID, loginname, remit,
				ProposalType.SIGNDEPOSIT420.getText(), remark);
		log.info("MG签到奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SIGNDEPOSIT420.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples("10");// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "mg";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SIGNDEPOSIT420.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforMg(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 签到金额转入dt
	 */
	@Override
	public String selfTransferDtSign(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行DT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = DtUtil.getamount(loginname);
			if (null != money && org.apache.commons.lang3.math.NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取玩家DT游戏账户余额错误!"; // +money
			}
		} catch (Exception e) {
			log.error("获取玩家" + loginname + "DT余额错误", e);
			return "获取玩家DT游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "DT平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.SIGNDEPOSIT420);
		String remark = "DT签到奖金,10倍流水，送" + remit;
		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4DT4Sign(transID, loginname, remit,
				ProposalType.SIGNDEPOSIT420.getText(), remark);
		log.info("DT签到奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SIGNDEPOSIT420.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples("10");// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "dt";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SIGNDEPOSIT420.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforDt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 签到金额转入slot
	 */
	@Override
	public String selfTransferSlotSign(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行SLOT转入。Q";
		}
		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			// remoteCredit = SlotUtil.getBalance(loginname);
		} catch (Exception e) {
			log.error("获取玩家" + loginname + "SLOT余额错误", e);
			return "获取玩家SLOT游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "SLOT平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.SIGNDEPOSIT420);
		String remark = "SLOT签到奖金,10倍流水，送" + remit;
		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4SLOT4Sign(transID, loginname, remit,
				ProposalType.SIGNDEPOSIT420.getText(), remark);
		log.info("SLOT签到奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SIGNDEPOSIT420.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples("10");// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "slot";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SIGNDEPOSIT420.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforSlot(Long.parseLong(transID), loginname, user.getCredit(), remit,
					Constants.IN, Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 签到金额转入ttg
	 */
	@Override
	public String selfTransferTtgSign(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行TTG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = PtUtil1.getPlayerAccount(loginname);
			if (null != money && org.apache.commons.lang3.math.NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取TTG余额错误:" + money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家TTG余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "TTG平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.SIGNDEPOSIT420);
		String remark = "TTG签到奖金,10倍流水，送" + remit;

		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SIGNDEPOSIT420.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples("10");// 10倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "Z" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		// 记录下到目前为止的投注额度 begin
		String platform = "ttg";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的ttg投注额
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
		prams.put("platform", platform);
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4TTG4Sign(transID, loginname, remit,
				ProposalType.SIGNDEPOSIT420.getText(), remark);
		log.info("TTG签到奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);

		proposalService.save(offer);
		proposalService.save(proposal);

		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SIGNDEPOSIT420.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			// Boolean b = PtUtil1.addPlayerAccountPraper(loginname,
			// remit.intValue());
			// if(null != b && b){
			transferService.addTransferforTt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
			/*
			 * }else{ vo.setMessage("奖金转入ttg失败  ， 数据回滚。"); throw new
			 * GenericDfhRuntimeException("奖金转入ttg失败  ， 数据回滚。"); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	// 签到奖金转入pt
	public String selfTransferPtSign(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);

		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			return "玩家不存在。";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			remoteCredit = PtUtil.getPlayerMoney(loginname);
			if (null == remoteCredit) {
				return "获取玩家PT游戏账户余额错误!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家PT游戏账户余额异常";
		}
		if (remoteCredit >= 5) {
			return "PT平台金额必须小于5元,才能转入操作！";
		}
		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			return "您正在使用手工再存优惠,不能使用自助再存。";
		}

		String pno = seqDao.generateProposalPno(ProposalType.SIGNDEPOSIT420);
		String remark = "PT签到奖金,10倍流水，送" + remit + "元";

		// 转账transfer4Pt4Sign
		String transID = seqService.generateTransferID();
		String msg = transferService.transfer4Pt4Sign(transID, loginname, remit, remark);
		log.info("奖金转入PT：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SIGNDEPOSIT420.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples("10");
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());

		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "Q" + codeOne + sqlCouponId + codeTwo;// 开头Q
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);

		user.setShippingcodePt(shippingcode);
		cs.update(user);

		// 记录下到目前为止的投注额度 begin
		String totalBetSql = "";
		totalBetSql = "select sum(bet) from platform_data where loginname=:username and platform=:platform";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		prams.put("platform", "pttiger");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);

		String platform = "pttiger";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		// Boolean deposit = PtUtil.getDepositPlayerMoney(loginname,remit);
		/*
		 * if (deposit != null && deposit) { } else {
		 * vo.setMessage("签到奖金转入PT失败，请联系客服！"); throw new
		 * GenericDfhRuntimeException( "签到奖金转入PT失败  ， 数据回滚。"); }
		 */
		return vo.getMessage();
	}

	/**
	 * 签到金额转入nt
	 */
	@Override
	public String selfTransferNTSign(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行NT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
			remoteCredit = ntm.getBoolean("result") ? ntm.getDouble("balance") : null;
			if (null == remoteCredit) {
				return "获取玩家NT游戏账户余额错误!"; // +ntm.getString("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家NT游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "NT平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.SIGNDEPOSIT420);
		String remark = "NT签到奖金,10倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConponNT4Sign(transID, loginname, remit, ProposalType.SIGNDEPOSIT420.getText(),
				remark);
		log.info("NT签到奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SIGNDEPOSIT420.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples("10");// 10倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "nt";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的ttg投注额
		String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		prams.put("startTime", cd.getTime());
		// prams.put("platform", "ttg");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SIGNDEPOSIT420.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		try {
			transferService.addTransferforNT(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 签到金额转入qt
	 */
	@Override
	public String selfTransferQTSign(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行QT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = QtUtil.getBalance(loginname);
			if (null != money && org.apache.commons.lang3.math.NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取玩家QT游戏账户余额错误!"; // +money
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家QT游戏账户余额异常！";
		}
		if (remoteCredit >= 5) {
			return "QT平台金额必须小于5元,才能转入！";
		}
		String pno = seqDao.generateProposalPno(ProposalType.SIGNDEPOSIT420);
		String remark = "QT签到奖金,10倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4QT4Sign(transID, loginname, remit,
				ProposalType.SIGNDEPOSIT420.getText(), remark);
		log.info("QT签到奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SIGNDEPOSIT420.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples("10");// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		/*
		 * String sqlCouponId = seqDao.generateYhjID(); String codeOne =
		 * dfh.utils.StringUtil.getRandomString(3); String codeTwo =
		 * dfh.utils.StringUtil.getRandomString(3); String shippingcode = "Z" +
		 * codeOne + sqlCouponId + codeTwo;
		 * proposal.setShippingCode(shippingcode);
		 */
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "qt";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的ttg投注额
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
		prams.put("platform", "qt");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SIGNDEPOSIT420.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforQt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙";
		}
	}

	/**
	 * 好友推荐金额转入ttg
	 */
	@Override
	public String selfTransferTtgFriend(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行TTG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = PtUtil1.getPlayerAccount(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取TTG余额错误:" + money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家TTG余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "TTG平台金额必须小于5元,才能转入！";
		}
		String mul = checkSystemConfig("type011", "001", "否");
		String pno = seqDao.generateProposalPno(ProposalType.FRIEND390);
		String remark = "TTG推荐好友奖金," + mul + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4TTG4Friend(transID, loginname, remit, ProposalType.FRIEND390.getText(),
				remark);
		log.info("TTG好友推荐奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.FRIEND390.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(mul);//
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "F" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "ttg";
		// 获取0点到领取时的TTG投注额
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
		prams.put("platform", "ttg");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.FRIEND390.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforTt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	// 好友推荐转入pt
	public String selfTransferPtFriend(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user) {
			return "玩家不存在。";
		}
		// 后期远程金额
		Double remoteCredit = 0.00;
		try {
			remoteCredit = PtUtil.getPlayerMoney(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家余额异常失败";
		}
		if (null == remoteCredit) {
			return "获取玩家余额异常,请稍后再试";
		}
		if (remoteCredit >= 5) {
			return "PT平台金额必须小于5元,才能转入操作！";
		}
		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			return "您正在使用手工再存优惠,不能使用自助再存。";
		}
		String mul = checkSystemConfig("type011", "001", "否");
		String pno = seqDao.generateProposalPno(ProposalType.FRIEND390);
		String remark = "PT好友推荐奖金," + mul + "倍流水限制" + remit + "元";

		String transID = seqService.generateTransferID();
		String msg = transferService.transfer4Pt4Friend(transID, loginname, remit, remark);
		log.info("奖金转入PT：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.FRIEND390.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(mul);// ----------------流水倍数
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "F" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);

		proposalService.save(offer);
		proposalService.save(proposal);
		user.setShippingcodePt(shippingcode);
		transferDao.update(user);
		// 记录下到目前为止的投注额度 begin
		String platform = "pttiger";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		transferDao.save(record);
		// 记录下到目前为止的投注额度 end
		Userstatus userstatus = (Userstatus) transferDao.get(Userstatus.class, loginname, LockMode.UPGRADE);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			transferDao.save(status);
		}

		return vo.getMessage();
	}

	/**
	 * 好友推荐金额转入nt
	 */
	@Override
	public String selfTransferNTFriend(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行NT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
			remoteCredit = ntm.getBoolean("result") ? ntm.getDouble("balance") : null;
			if (null == remoteCredit) {
				return "获取NT余额错误:" + ntm.getString("error");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家NT余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "NT平台金额必须小于5元,才能转入！";
		}
		String mul = checkSystemConfig("type011", "001", "否");
		String pno = seqDao.generateProposalPno(ProposalType.FRIEND390);
		String remark = "NT好友推荐奖金," + mul + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConponNT4Friend(transID, loginname, remit, ProposalType.FRIEND390.getText(),
				remark);
		log.info("NT签到奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.FRIEND390.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(mul);// -----------流水限制
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "nt";
		// 记录下到目前为止的投注额度
		// 获取0点到领取时的nt投注额
		String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", proposal.getLoginname());
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		prams.put("startTime", cd.getTime());
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.FRIEND390.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforNT(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 好友推荐金额转入qt
	 */
	@Override
	public String selfTransferQtFriend(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行QT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = QtUtil.getBalance(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取QT余额错误:" + money;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家QT余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "QT平台金额必须小于5元,才能转入！";
		}
		String mul = checkSystemConfig("type011", "001", "否");
		String pno = seqDao.generateProposalPno(ProposalType.FRIEND390);
		String remark = "QT推荐好友奖金," + mul + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4Qt4Friend(transID, loginname, remit, ProposalType.FRIEND390.getText(),
				remark);
		log.info("QT好友推荐奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.FRIEND390.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(mul);//
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposal.setRemark(remark);
		String sqlCouponId = seqDao.generateYhjID();
		String codeOne = dfh.utils.StringUtil.getRandomString(3);
		String codeTwo = dfh.utils.StringUtil.getRandomString(3);
		String shippingcode = "F" + codeOne + sqlCouponId + codeTwo;
		proposal.setShippingCode(shippingcode);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "qt";
		// 获取0点到领取时的QT投注额
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
		prams.put("platform", "qt");
		Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, betAmount, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.FRIEND390.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforQt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 好友推荐金额转入mg
	 */
	@Override
	public String selfTransferMgFriend(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行MG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.0;
		try {
			// remoteCredit = MGSUtil.getBalance(loginname,user.getPassword());
			remoteCredit = MGSUtil.getBalance(loginname);
			if (remoteCredit == null) {
				throw new Exception("获取MG余额出错");
			}
		} catch (Exception e) {
			log.error("获取玩家" + loginname + "MG余额出错", e);
			return "获取MG余额出错！";
		}

		if (remoteCredit >= 5) {
			return "MG平台金额必须小于5元,才能转入！";
		}
		String mul = checkSystemConfig("type011", "001", "否");
		String pno = seqDao.generateProposalPno(ProposalType.FRIEND390);
		String remark = "MG推荐好友奖金," + mul + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4Mg4Friend(transID, loginname, remit, ProposalType.FRIEND390.getText(),
				remark);
		log.info("MG好友推荐奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Date now = new Date();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.FRIEND390.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(mul);
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(now);
		proposal.setRemark(remark);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "mg";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, now, 0);// 网络抓投注额，只要时间
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.FRIEND390.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(now);
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforMg(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 好友推荐金额转入dt
	 */
	@Override
	public String selfTransferDtFriend(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行DT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = DtUtil.getamount(loginname);
			if (null != money && NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取DT余额错误:" + money;
			}
		} catch (Exception e) {
			log.error("获取玩家" + loginname + "DT余额错误", e);
			return "获取 玩家DT余额异常失败";
		}
		if (remoteCredit >= 5) {
			return "DT平台金额必须小于5元,才能转入！";
		}
		String mul = checkSystemConfig("type011", "001", "否");
		String pno = seqDao.generateProposalPno(ProposalType.FRIEND390);
		String remark = "DT推荐好友奖金," + mul + "倍流水，送" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4Dt4Friend(transID, loginname, remit, ProposalType.FRIEND390.getText(),
				remark);
		log.info("DT好友推荐奖金 转账信息：" + msg);
		if (null != msg) {
			return msg;
		}
		Date now = new Date();
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.FRIEND390.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(mul);//
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(now);
		proposal.setRemark(remark);
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "dt";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, now, 0);// 网络抓投注额，只要时间
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.FRIEND390.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(now);
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforDt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private String checkSystemConfig(String typeNo, String itemNo, String flag) {
		String str = "";
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		if (!StringUtils.isEmpty(typeNo)) {
			dc = dc.add(Restrictions.eq("typeNo", typeNo));
		}
		if (!StringUtils.isEmpty(itemNo)) {
			dc = dc.add(Restrictions.eq("itemNo", itemNo));
		}
		if (!StringUtils.isEmpty(flag)) {
			dc = dc.add(Restrictions.eq("flag", flag));
		}
		List<SystemConfig> list = proposalService.findByCriteria(dc);
		if (null != list && list.size() > 0) {
			str = list.get(0).getValue();
		}
		return str;
	}

	/* MG优惠start */
	@Override
	public AutoYouHuiVo selfYouHuiMGFirst(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user || user.getWarnflag().equals(4)) {
			vo.setMessage("抱歉，您不能自助MG首存优惠。Q");
			return vo;
		}
		// if(!user.getWarnflag().equals(3)){//对于非安全玩家，需要查询看 同姓名 同IP下
		// 三个月内是否领取过体验金
		// 查询同IP或者同姓名的所有玩家
		DetachedCriteria dc1 = DetachedCriteria.forClass(Users.class);
		dc1.add(Restrictions.or(Restrictions.eq("accountName", user.getAccountName()),
				Restrictions.eq("registerIp", user.getRegisterIp())));
		List<Users> list = transferService.findByCriteria(dc1);
		List listLoginName = new ArrayList();
		if (list != null && list.size() > 0 && list.get(0) != null) {
			for (Users users : list) {
				listLoginName.add(users.getLoginname());
			}
		}
		// 查询这些玩家三个月内是否领过首存 如果没领过 则可以继续进行下去
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date begindate = calendar.getTime();// 当前日期减去三个月
		DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.in("loginname", listLoginName));
		criteria.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		criteria.add(Restrictions.eq("type", ProposalType.SELFMGFIRST.getCode()));
		criteria.add(Restrictions.ge("createTime", begindate));
		List<Proposal> proposals = transferService.findByCriteria(criteria);
		if (null != proposals && proposals.size() > 0) {
			vo.setMessage("抱歉，您不能自助MG首存优惠。");
			return vo;
		}
		// }
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("target", "mg"));
		// dc.add(Restrictions.ne("remit", 8.00)) ;
		dc.add(Restrictions.sqlRestriction(" remark not like '%自助优惠' "));
		List transfers = transferService.findByCriteria(dc);
		if (null != transfers && transfers.size() > 0 && null != transfers.get(0)) {
			vo.setMessage("抱歉，您不是第一次转账到MG，不能自助MG首存优惠。");
			return vo;
		} else {
			// 获取远程金额
			// 后期远程金额
			Double remoteCredit = null;

			try {
				// remoteCredit =
				// MGSUtil.getBalance(loginname,user.getPassword());
				remoteCredit = MGSUtil.getBalance(loginname);
				if (null == remoteCredit) {
					vo.setMessage("获取MG余额错误:" + remoteCredit);
					return vo;
				}
			} catch (Exception e) {
				e.printStackTrace();
				vo.setMessage("获取 玩家MG余额异常失败");
				return vo;
			}
			if (remoteCredit >= 5) {
				vo.setMessage("MG平台金额必须小于5元，才能自助优惠！");
				return vo;
			}

			YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFMGFIRST.getText(), user.getLevel());
			if (null == config) {
				vo.setMessage("优惠配置未开启");
				return vo;
			}
			// 判断优惠使用剩余次数是否够用
			if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFMGFIRST)) {
				vo.setMessage("超过最大使用次数。");
				return vo;
			}

			Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
					: Math.abs(Arith.mul(remit, config.getPercent()));
			changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
			String pno = seqDao.generateProposalPno(ProposalType.SELFMGFIRST);
			String remark = "MG自助首存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

			// 转账
			String transID = seqService.generateTransferID();

			String msg = transferService.selfCouponMG(transID, loginname, remit, ProposalType.SELFMGFIRST.getText(),
					remark);
			log.info("MG自助首存优惠 转账信息：" + msg);
			if (null != msg) {
				vo.setMessage(msg);
				return vo;
			}

			Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFMGFIRST.getCode(),
					user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
					Constants.FROM_FRONT, null, null);
			proposal.setBetMultiples(config.getBetMultiples().toString());
			proposal.setGifTamount(changeMoney);
			proposal.setExecuteTime(new Date());

			proposalService.save(offer);
			proposalService.save(proposal);

			// 记录下到目前为止的投注额度 begin
			String platform = "mg";
			PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
			proposalService.save(record);
			// 记录下到目前为止的投注额度 end

			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(pno);
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalType.SELFMGFIRST.getText());
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			proposalService.save(selfRecord);

			Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				// status.setTouzhuflag(1);
				proposalService.save(status);
			} else {
				// userstatus.setTouzhuflag(1);
				proposalService.update(userstatus);
			}

			transferService.addTransferforMg(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", "转入成功");
			vo.setGiftMoney(Arith.add(remit, changeMoney));
			vo.setMessage("MG自助首存优惠成功SUCCESS");
			return vo;
		}
	}

	/**
	 * MG次存优惠
	 */
	@Override
	public AutoYouHuiVo selfYouHuiMGTwice(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user && user.getWarnflag().equals(4)) {
			vo.setMessage("抱歉，您不能自助MG次存优惠。");
			return vo;
		}

		// 获取远程金额
		Double remoteCredit = null;

		try {
			// remoteCredit = MGSUtil.getBalance(loginname,user.getPassword());
			remoteCredit = MGSUtil.getBalance(loginname);
			if (null == remoteCredit) {
				vo.setMessage("获取MG余额错误:" + remoteCredit);
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家MG余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("MG平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFMGTWICE.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFMGTWICE)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));
		List<Proposal> offers = proposalService.findByCriteria(offerDc);
		if (null != offers && offers.size() > 0) {
			vo.setMessage("您正在使用手工再存优惠,不能使用自助再存。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFMGTWICE);
		String remark = "MG自助次存优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfCouponMG(transID, loginname, remit, ProposalType.SELFMGTWICE.getText(),
				remark);
		log.info("MG自助次存优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFMGTWICE.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "mg";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFMGTWICE.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}

		transferService.addTransferforMg(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
				Constants.FAIL, "", "转入成功");
		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助MG次存优惠成功SUCCESS");
		return vo;
	}

	@Override
	public AutoYouHuiVo selfYouHuiMGSpec(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null == user && user.getWarnflag().equals(4)) {
			vo.setMessage("抱歉，您不能自助MG限时优惠。Q");
			return vo;
		}

		// 获取远程金额
		// 获取远程金额
		Double remoteCredit = null;

		try {
			// remoteCredit = MGSUtil.getBalance(loginname,user.getPassword());
			remoteCredit = MGSUtil.getBalance(loginname);
			if (null == remoteCredit) {
				vo.setMessage("获取MG余额错误:" + remoteCredit);
				return vo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setMessage("获取 玩家MG余额异常失败");
			return vo;
		}
		if (remoteCredit >= 5) {
			vo.setMessage("MG平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFMGSPEC.getText(), user.getLevel());
		if (null == config) {
			vo.setMessage("优惠配置未开启");
			return vo;
		}
		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFMGSPEC)) {
			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);/* 精确到小数点后2位 */
		String pno = seqDao.generateProposalPno(ProposalType.SELFMGSPEC);
		String remark = "MG自助限时优惠," + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfCouponMG(transID, loginname, remit, ProposalType.SELFMGSPEC.getText(), remark);
		log.info("MG自助限时优惠 转账信息：" + msg);
		if (null != msg) {
			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFMGSPEC.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(config.getBetMultiples().toString());
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		transferDao.save(offer);
		transferDao.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "mg";
		Date now = new Date();
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, now, 0);
		transferDao.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELFMGSPEC.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(now);
		transferDao.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			transferDao.save(status);
		}

		transferService.addTransferforMg(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
				Constants.FAIL, "", "转入成功");
		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助MG限时优惠成功SUCCESS");
		return vo;
	}
	/* MG优惠end */

	// 自助DT首存优惠
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo selfYouHuiDTFirst(String loginname, Double remit) {

		AutoYouHuiVo vo = new AutoYouHuiVo();

		Users user = (Users) cs.get(Users.class, loginname);

		if (null == user || user.getWarnflag().equals(4)) {

			vo.setMessage("抱歉，您不能自助DT首存优惠。");
			return vo;
		}
		// 对于非安全玩家，需要查询同姓名或者同IP下三个月内是否领取过体验金
		// else if (!user.getWarnflag().equals(3)) {

		List<String> listLoginName = new ArrayList<String>();

		// 查询同姓名或者同IP的所有用户
		DetachedCriteria dc1 = DetachedCriteria.forClass(Users.class);
		dc1.add(Restrictions.or(Restrictions.eq("accountName", user.getAccountName()),
				Restrictions.eq("registerIp", user.getRegisterIp())));

		List<Users> list = transferService.findByCriteria(dc1);

		if (null != list && !list.isEmpty()) {

			for (Users users : list) {

				listLoginName.add(users.getLoginname());
			}
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date begindate = calendar.getTime();

		// 查询和当前用户相同姓名或者相同IP的玩家是否在三个月内领过首存，如果没领过，则可以继续进行下去
		DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.in("loginname", listLoginName));
		criteria.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		criteria.add(Restrictions.eq("type", ProposalType.SELFDTFIRST.getCode()));
		criteria.add(Restrictions.ge("createTime", begindate));

		List<Proposal> proposals = transferService.findByCriteria(criteria);

		if (null != proposals && !proposals.isEmpty()) {

			vo.setMessage("抱歉，您不能自助DT首存优惠。");
			return vo;
		}
		// }

		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.eq("loginname", loginname));
		dc.add(Restrictions.eq("target", "dt"));
		dc.add(Restrictions.sqlRestriction(" remark not like '%自助优惠' "));

		List<Transfer> transfers = transferService.findByCriteria(dc);

		if (null != transfers && !transfers.isEmpty()) {

			vo.setMessage("抱歉，您不是第一次转账到DT，不能自助DT首存优惠。");
			return vo;
		}

		// DT账户余额
		Double remoteCredit = 0.00;

		try {

			String money = DtUtil.getamount(loginname);

			if (StringUtils.isNotEmpty(money) && NumberUtils.isNumber(money)) {

				remoteCredit = Double.valueOf(money);
			} else {

				vo.setMessage("获取DT账户余额错误：" + money);
				return vo;
			}
		} catch (Exception e) {

			log.error("selfYouHuiDTFirst方法获取DT账户余额出现异常，异常信息：" + e.getMessage());

			vo.setMessage("获取玩家DT账户余额异常失败。");
			return vo;
		}

		if (remoteCredit >= 5) {

			vo.setMessage("DT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFDTFIRST.getText(), user.getLevel());

		if (null == config) {

			vo.setMessage("优惠配置尚未开启。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);

		String pno = seqDao.generateProposalPno(ProposalType.SELFDTFIRST);

		String remark = "自助DT首存优惠，" + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		String transID = seqService.generateTransferID();

		String msg = transferService.transferDTAndSelfYouHuiInModify(pno, ProposalType.SELFDTFIRST.getText(), transID,
				loginname, remit, remark);
		log.info("自助DT首存优惠转账信息：" + msg);

		if (StringUtils.isNotEmpty(msg)) {

			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);

		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFDTFIRST.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(String.valueOf(config.getBetMultiples()));
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		proposalService.save(offer);
		proposalService.save(proposal);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname, LockMode.UPGRADE);

		if (null == userstatus) {

			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);

			proposalService.save(status);
		}

		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助DT首存优惠成功SUCCESS");

		return vo;
	}

	// 自助DT次存优惠
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo selfYouHuiDTTwice(String loginname, Double remit) {

		AutoYouHuiVo vo = new AutoYouHuiVo();

		Users user = (Users) cs.get(Users.class, loginname);

		if (null == user) {

			vo.setMessage("玩家不存在。");
			return vo;
		}

		// DT账户余额
		Double remoteCredit = 0.00;

		try {

			String money = DtUtil.getamount(loginname);

			if (StringUtils.isNotEmpty(money) && NumberUtils.isNumber(money)) {

				remoteCredit = Double.valueOf(money);
			} else {

				vo.setMessage("获取DT账户余额错误：" + money);
				return vo;
			}
		} catch (Exception e) {

			log.error("selfYouHuiDTTwice方法获取DT账户余额出现异常，异常信息：" + e.getMessage());

			vo.setMessage("获取玩家DT账户余额异常失败。");
			return vo;
		}

		if (remoteCredit >= 5) {

			vo.setMessage("DT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFDTTWICE.getText(), user.getLevel());

		if (null == config) {

			vo.setMessage("优惠配置尚未开启。");
			return vo;
		}

		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFDTTWICE)) {

			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		// 查询后台是否有手动提交再存优惠
		DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
		offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
		offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		offerDc.add(Restrictions.eq("loginname", user.getLoginname()));
		offerDc.add(Restrictions.eq("remark", "BEGIN" + user.getLoginname()));

		List<Proposal> offers = proposalService.findByCriteria(offerDc);

		if (null != offers && !offers.isEmpty()) {

			vo.setMessage("您正在使用手工再存优惠，不能使用自助再存。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);

		String pno = seqDao.generateProposalPno(ProposalType.SELFDTTWICE);

		String remark = "自助DT次存优惠，" + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		String transID = seqService.generateTransferID();

		String msg = transferService.transferDTAndSelfYouHuiInModify(pno, ProposalType.SELFDTTWICE.getText(), transID,
				loginname, remit, remark);
		log.info("自助DT次存优惠转账信息：" + msg);

		if (StringUtils.isNotEmpty(msg)) {

			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);

		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFDTTWICE.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(String.valueOf(config.getBetMultiples()));
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		proposalService.save(offer);
		proposalService.save(proposal);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname, LockMode.UPGRADE);

		if (null == userstatus) {

			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);

			proposalService.save(status);
		}

		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助DT次存优惠成功SUCCESS");

		return vo;
	}

	// 自助DT限时优惠
	public AutoYouHuiVo selfYouHuiDTSpec(String loginname, Double remit) {

		AutoYouHuiVo vo = new AutoYouHuiVo();

		Users user = (Users) cs.get(Users.class, loginname);

		if (null == user) {

			vo.setMessage("玩家不存在。");
			return vo;
		}

		// DT账户余额
		Double remoteCredit = 0.00;

		try {

			String money = DtUtil.getamount(loginname);

			if (StringUtils.isNotEmpty(money) && NumberUtils.isNumber(money)) {

				remoteCredit = Double.valueOf(money);
			} else {

				vo.setMessage("获取DT账户余额错误：" + money);
				return vo;
			}
		} catch (Exception e) {

			log.error("selfYouHuiDTTwice方法获取DT账户余额出现异常，异常信息：" + e.getMessage());

			vo.setMessage("获取玩家DT账户余额异常失败。");
			return vo;
		}

		if (remoteCredit >= 5) {

			vo.setMessage("DT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}

		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFDTSPEC.getText(), user.getLevel());

		if (null == config) {

			vo.setMessage("优惠配置尚未开启。");
			return vo;
		}

		// 判断优惠使用剩余次数是否够用
		if (!youhuiContinueOrStop(config.getTimes(), config.getTimesflag(), loginname, ProposalType.SELFDTSPEC)) {

			vo.setMessage("超过最大使用次数。");
			return vo;
		}

		Double changeMoney = Math.abs(remit * config.getPercent()) > config.getLimitMoney() ? config.getLimitMoney()
				: Math.abs(Arith.mul(remit, config.getPercent()));
		changeMoney = Arith.round(changeMoney, 2);

		String pno = seqDao.generateProposalPno(ProposalType.SELFDTSPEC);

		String remark = "自助DT限时优惠，" + config.getBetMultiples() + "倍流水，存" + remit + "送" + changeMoney;

		String transID = seqService.generateTransferID();

		String msg = transferService.transferDTAndSelfYouHuiInModify(pno, ProposalType.SELFDTSPEC.getText(), transID,
				loginname, remit, remark);
		log.info("自助DT限时优惠转账信息：" + msg);

		if (StringUtils.isNotEmpty(msg)) {

			vo.setMessage(msg);
			return vo;
		}

		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, changeMoney, remark);

		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELFDTSPEC.getCode(),
				user.getLevel(), loginname, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(String.valueOf(config.getBetMultiples()));
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());

		transferDao.save(offer);
		transferDao.save(proposal);

		Userstatus userstatus = (Userstatus) transferDao.get(Userstatus.class, loginname, LockMode.UPGRADE);

		if (null == userstatus) {

			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);

			transferDao.save(status);
		}

		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助DT限时优惠成功SUCCESS");

		return vo;
	}

	// App下载彩金
	@Override
	public String platformAmountAndRecordAPPRewardLog(String loginname, String platform) {
		Users user = (Users) cs.get(Users.class, loginname);
		if (null == user) {
			return "玩家不存在。";
		}
		if (user.getFlag() == 1) {
			return "玩家被禁用";
		}
		// 验证是否领过
		boolean useFlag = cs.isApplyAppPreferential(loginname);
		if (useFlag) {
			return "您已经使用过APP下载彩金";
		}
		// 验证优惠是否开启
		Double changeMoney = null;
		YouHuiConfig config = queryYouHuiConfigSingle(ProposalType.SELFHELP_APP_PREFERENTIAL.getText(),
				user.getLevel());
		if (null == config) {
			return "优惠配置未开启";
		}

		// [转账到我方平台]
		Double remoteCredit = 0.00;
		String target = "";
		String creditChangeType = "";
		try {
			if (platform.equals("PT")) {
				remoteCredit = PtUtil.getPlayerMoney(loginname);
				target = RemoteConstant.PAGESITENEWPT;
				creditChangeType = CreditChangeType.TRANSFER_MEWPTIN.getCode();
			} else if (platform.equals("GPI")) {
				remoteCredit = GPIUtil.getBalance(loginname);
				target = RemoteConstant.PAGESITEGPI;
				creditChangeType = CreditChangeType.TRANSFER_GPIIN.getCode();
			} else if (platform.equals("TTG")) {
				remoteCredit = Double.valueOf(PtUtil1.getPlayerAccount(loginname));
				target = RemoteConstant.PAGESITETT;
				creditChangeType = CreditChangeType.TRANSFER_TTGIN.getCode();
			} else if (platform.equals("NT")) {
				JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
				remoteCredit = ntm.getDouble("balance");
				target = RemoteConstant.PAGESITENT;
				creditChangeType = CreditChangeType.TRANSFER_NTIN.getCode();
			} else if (platform.equals("QT")) {
				remoteCredit = Double.valueOf(QtUtil.getBalance(loginname));
				target = RemoteConstant.PAGESITEQT;
				creditChangeType = CreditChangeType.TRANSFER_QTIN.getCode();
			} else if (platform.equals("MG")) {
				// remoteCredit =
				// Double.valueOf(MGSUtil.getBalance(loginname,user.getPassword()))
				// ;
				remoteCredit = Double.valueOf(MGSUtil.getBalance(loginname));
				target = RemoteConstant.PAGESITEMG;
				creditChangeType = CreditChangeType.TRANSFER_MGIN.getCode();
			} else if (platform.equals("DT")) {
				remoteCredit = Double.valueOf(DtUtil.getamount(loginname));
				target = RemoteConstant.PAGESITEDT;
				creditChangeType = CreditChangeType.TRANSFER_DTIN.getCode();
			} else {
				return "无效平台";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取 玩家余额异常失败";
		}
		if (remoteCredit >= 1) {
			return platform + "平台金额必须小于1,才能领取APP下载彩金！";
		}

		changeMoney = config.getAmount();
		String name = platform + changeMoney + "元APP下载彩金自助优惠";

		// [纪录transfer]
		String transID = seqService.generateTransferID();
		transferDao.addTransferINNewPt8Yuan(Long.parseLong(transID), loginname, user.getCredit(), changeMoney, name,
				target);

		// [纪录proposal]
		String pno = seqDao.generatePt8Yuan();
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(),
				ProposalType.SELFHELP_APP_PREFERENTIAL.getCode(), user.getLevel(), loginname, changeMoney,
				user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, name, null);
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());
		seqDao.save(proposal);
		// [记录app_reward]
		saveAppPreferential(loginname, Long.parseLong(transID));
		// [纪录Creditlogs]
		Creditlogs log = new Creditlogs();
		log.setLoginname(loginname);
		log.setType(creditChangeType);
		log.setCredit(user.getCredit());
		log.setRemit(changeMoney);
		log.setNewCredit(user.getCredit());
		log.setRemark(name);
		log.setCreatetime(DateUtil.getCurrentTimestamp());
		seqDao.save(log);

		String result = changeMoney + ";" + transID + ";" + name;
		return result;

	}

	private void saveAppPreferential(String loginname, Long transferId) {
		AppPreferential appReward = new AppPreferential();
		appReward.setLoginname(loginname);
		appReward.setTransferId(transferId);
		appReward.setVersion(SynchronizedAppPreferentialUtil.appRewardVersion);
		seqDao.save(appReward);
	}

	@Override
	public Object get(Class clazz, Serializable id) {
		return proposalService.get(clazz, id);
	}

	/**
	 * 红包雨转MG
	 */
	@Override
	public String selfTransferMgRedRain(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行MG转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;
		String password = user.getPassword();
		try {
			remoteCredit = MGSUtil.getBalance(loginname);
			if (null == remoteCredit) {
				return "获取玩家MG游戏账户余额错误!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家MG游戏账户余额异常！";
		}
		/*
		 * if (remoteCredit >= 5) { return "MG平台金额必须小于5元,才能转入！"; }
		 */
		DetachedCriteria dc = DetachedCriteria.forClass(RaincouponRecord.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		dc = dc.add(Restrictions.eq("isused", 20));
		List<RaincouponRecord> list = proposalService.getHibernateTemplate()
				.findByCriteria(dc.addOrder(Order.desc("createtime")));
		String multiples = "";
		/*
		 * if(list==null||list.size()<1){ return "没有红包记录"; }
		 */
		if (list == null || list.size() < 1) {
			multiples = "10";
		} else {
			RaincouponRecord raincouponRecord = list.get(0);
			multiples = raincouponRecord.getCoupon();
		}
		String pno = seqDao.generateProposalPno(ProposalType.SELF_101);
		String remark = "红包雨余额转入MG平台," + multiples + "倍流水，转入" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4MG4RedRain(transID, loginname, remit, ProposalType.SELF_101.getText(),
				remark);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELF_101.getCode(),
				user.getLevel(), loginname, 0.0, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(multiples);// 十倍流水
		proposal.setGifTamount(remit);
		proposal.setRemark(remark);
		proposal.setExecuteTime(new Date());
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "mg";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELF_101.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforMg(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 红包雨账户转入DT
	 */
	@Override
	public String selfTransferDtRedRain(String loginname, Double remit) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行DT转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;

		try {
			String money = DtUtil.getamount(loginname);
			if (null != money && org.apache.commons.lang3.math.NumberUtils.isNumber(money)) {
				remoteCredit = Double.valueOf(money);
			} else {
				return "获取玩家DT游戏账户余额错误!"; // +money
			}
		} catch (Exception e) {
			log.error("获取玩家" + loginname + "DT余额错误", e);
			return "获取玩家DT游戏账户余额异常！";
		}
		/*
		 * if (remoteCredit >= 5) { return "DT平台金额必须小于5元,才能转入！"; }
		 */
		DetachedCriteria dc = DetachedCriteria.forClass(RaincouponRecord.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		dc = dc.add(Restrictions.eq("isused", 10));
		List<RaincouponRecord> list = proposalService.getHibernateTemplate()
				.findByCriteria(dc.addOrder(Order.desc("createtime")));
		if (list == null || list.size() < 1) {
			return "数据异常";
		}
		RaincouponRecord raincouponRecord = list.get(0);
		String pno = seqDao.generateProposalPno(ProposalType.SELF_101);
		if (raincouponRecord.getTimes() == null) {
			raincouponRecord.setTimes(0);
		}
		String remark = "红包雨余额转入DT平台," + raincouponRecord.getTimes() + "倍流水，转入" + remit;
		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4DT4RedRain(transID, loginname, remit, ProposalType.SELF_101.getText(),
				remark);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELF_101.getCode(),
				user.getLevel(), loginname, remit, null, ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, null,
				null);
		proposal.setBetMultiples(raincouponRecord.getTimes().toString());// 十倍流水
		proposal.setGifTamount(0.0);
		proposal.setExecuteTime(new Date());
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "dt";
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, 0.0, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELF_101.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforDt(Long.parseLong(transID), loginname, user.getCredit(), remit, Constants.IN,
					Constants.FAIL, "", remark);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	

	/**
	 * 红包雨转入平台(BG )
	 */
	@Override
	public String selfTransferRedRain(String loginname, Double remit, String plat) {
		Users user = (Users) cs.get(Users.class, loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo();
		if (null != user && (!user.getWarnflag().equals(4))) {

		} else {
			return "抱歉，您不能进行" + plat + "转入。Q";
		}

		// 后期远程金额
		Double remoteCredit = 0.00;
		String password = user.getPassword();
		try {
			remoteCredit = BGUtil.getBalance(loginname);
			if (null == remoteCredit) {
				return "获取玩家" + plat + "游戏账户余额错误!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "获取玩家" + plat + "游戏账户余额异常！";
		}
		/*
		 * if (remoteCredit >= 5) { return "MG平台金额必须小于5元,才能转入！"; }
		 */
		DetachedCriteria dc = DetachedCriteria.forClass(RaincouponRecord.class);
		dc = dc.add(Restrictions.eq("loginname", loginname));
		dc = dc.add(Restrictions.eq("isused", 20));
		List<RaincouponRecord> list = proposalService.getHibernateTemplate()
				.findByCriteria(dc.addOrder(Order.desc("createtime")));
		String multiples = "";
		/*
		 * if(list==null||list.size()<1){ return "没有红包记录"; }
		 */
		if (list == null || list.size() < 1) {
			multiples = "10";
		} else {
			RaincouponRecord raincouponRecord = list.get(0);
			multiples = raincouponRecord.getCoupon();
		}
		String pno = seqDao.generateProposalPno(ProposalType.SELF_101);
		String remark = "红包雨余额转入" + plat + "平台," + multiples + "倍流水，转入" + remit;

		// 转账
		String transID = seqService.generateTransferID();
		String msg = transferService.selfConpon4RedRain(transID, loginname, remit, ProposalType.SELF_101.getText(),
				remark,plat);
		if (null != msg) {
			return msg;
		}
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginname, 0.0, remit, remark);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.SELF_101.getCode(),
				user.getLevel(), loginname, 0.0, user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, null, null);
		proposal.setBetMultiples(multiples);// 十倍流水
		proposal.setGifTamount(remit);
		proposal.setRemark(remark);
		proposal.setExecuteTime(new Date());
		proposalService.save(offer);
		proposalService.save(proposal);

		// 记录下到目前为止的投注额度 begin
		String platform = "";
		if (plat.equals("BG")) {
			platform = "bg";
		} else if (plat.equals("CQ9")) {
			platform = "cq9";
		} else if (plat.equals("pg")) {
			platform = "pg";
		}
		PreferentialRecord record = new PreferentialRecord(pno, loginname, platform, null, new Date(), 0);
		proposalService.save(record);
		// 记录下到目前为止的投注额度 end

		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginname);
		selfRecord.setPlatform(platform);
		selfRecord.setSelfname(ProposalType.SELF_101.getText());
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		proposalService.save(selfRecord);

		Userstatus userstatus = (Userstatus) proposalService.get(Userstatus.class, loginname);
		if (userstatus == null) {
			Userstatus status = new Userstatus();
			status.setLoginname(loginname);
			status.setCashinwrong(0);
			// status.setTouzhuflag(0);
			proposalService.save(status);
		}
		try {
			transferService.addTransferforPlat(Long.parseLong(transID), loginname, user.getCredit(), remit,
					Constants.IN, Constants.FAIL, "", remark, plat);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}