package com.nnti.personal.service.implementations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.common.constants.ActionLogType;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.CreditChangeType;
import com.nnti.common.constants.PlatformCode;
import com.nnti.common.constants.ProposalFlagType;
import com.nnti.common.constants.ProposalType;
import com.nnti.common.constants.UserRole;
import com.nnti.common.model.vo.ActionLog;
import com.nnti.common.model.vo.CreditLog;
import com.nnti.common.model.vo.Offer;
import com.nnti.common.model.vo.PreferentialRecord;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.model.vo.ProposalExtend;
import com.nnti.common.model.vo.SelfRecord;
import com.nnti.common.model.vo.Transfer;
import com.nnti.common.model.vo.User;
import com.nnti.common.model.vo.UserStatus;
import com.nnti.common.service.interfaces.IActionLogService;
import com.nnti.common.service.interfaces.ICommonService;
import com.nnti.common.service.interfaces.ICreditLogService;
import com.nnti.common.service.interfaces.ILosePromoService;
import com.nnti.common.service.interfaces.IOfferService;
import com.nnti.common.service.interfaces.IPreferentialRecordService;
import com.nnti.common.service.interfaces.IProposalExtendService;
import com.nnti.common.service.interfaces.IProposalService;
import com.nnti.common.service.interfaces.ISelfRecordService;
import com.nnti.common.service.interfaces.ISequenceService;
import com.nnti.common.service.interfaces.ITransferService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.service.interfaces.IUserStatusService;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.NumericUtil;
import com.nnti.common.utils.QTUtil;
import com.nnti.personal.model.dto.SelfDepositDTO;
import com.nnti.personal.model.vo.PreferentialConfig;
import com.nnti.personal.service.interfaces.IPlatformDepositService;

@Service("qtDepositService")
public class QTDepositServiceImpl extends BaseService implements IPlatformDepositService {

	private static Logger log = Logger.getLogger(QTDepositServiceImpl.class);

	private static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private IUserService userService;
	@Autowired
	private ISequenceService sequenceService;
	@Autowired
	private ILosePromoService losePromoService;
	@Autowired
	private ISelfRecordService selfRecordService;
	@Autowired
	private ICreditLogService creditLogService;
	@Autowired
	private ITransferService transferService;
	@Autowired
	private IOfferService offerService;
	@Autowired
	private IProposalService proposalService;
	@Autowired
	private IProposalExtendService proposalExtendService;
	@Autowired
	private IPreferentialRecordService preferentialRecordService;
	@Autowired
	private IUserStatusService userStatusService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IActionLogService actionLogService;

	// 首存优惠
	@Transactional(rollbackFor = Exception.class)
	public String firstDeposit(SelfDepositDTO dto) throws Exception {

		String loginName = dto.getLoginName();
		String type = dto.getType();
		String id = dto.getId();

		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 首存优惠限制
		String msg = this.firstLimit(loginName, Constant.QT, type, id);

		if (StringUtils.isNotBlank(msg)) {

			resultMap.put("message", msg);
			return mapper.writeValueAsString(resultMap);
		}

		return common(dto, "首存优惠", "first deposit");
	}

	// 次存优惠
	@Transactional(rollbackFor = Exception.class)
	public String timesDeposit(SelfDepositDTO dto) throws Exception {

		return common(dto, "次存优惠", "times deposit");
	}

	// 限时优惠
	@Transactional(rollbackFor = Exception.class)
	public String limitedTime(SelfDepositDTO dto) throws Exception {

		return common(dto, "限时优惠", "limited time");
	}

	private String common(SelfDepositDTO dto, String preferentialType, String description) throws Exception {

		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		String platform = dto.getPlatform();
		String id = dto.getId();
		String type = dto.getType();
		Double amount = dto.getAmount();
		String channel = dto.getChannel();
		String sid = dto.getSid();

		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 自助优惠限制
		Object obj = this.preferentialLimit(product, loginName, platform, id, type, sid, amount);

		if (obj instanceof String) {

			resultMap.put("message", String.valueOf(obj));
			return mapper.writeValueAsString(resultMap);
		}

		// 获取QT账户余额
		Double remoteCredit = null;

		String money = QTUtil.getBalance(product, loginName);

		if (StringUtils.isNotBlank(money) && NumberUtils.isNumber(money)) {

			remoteCredit = Double.valueOf(money);
		}

		if (null == remoteCredit) {

			resultMap.put("message", "获取QT账户余额失败，请稍后重试！");
			return mapper.writeValueAsString(resultMap);
		}

		if (remoteCredit >= 5) {

			resultMap.put("message", Constant.QT + "平台金额必须小于5元，才能自助优惠！");
			return mapper.writeValueAsString(resultMap);
		}

		User user = userService.get(loginName);
		Double credit = user.getCredit();
		Double creditDay = user.getCreditDay();
		String lastTime = user.getCreditDayDate();

		if (credit < amount) {

			resultMap.put("message", "主账户余额不足！");
			return mapper.writeValueAsString(resultMap);
		}

		PreferentialConfig config = (PreferentialConfig) obj;

		Date currentDate = DateUtil.getCurrentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(currentDate);

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("userName", loginName);
		params.put("platform", PlatformCode.Platform.getCode(Constant.QT));
		params.put("conditionStatus", "1");
		params.put("status", "2");

		// 更新救援金数据为已处理
		losePromoService.update(params);

		params.put("loginName", loginName);
		params.put("conditionType", 0);
		params.put("type", 1);
		params.put("updateTime", currentDate);

		// 更新流水数据为已达到流水要求
		selfRecordService.updateList(params);

		Double changeMoney = Math.abs(NumericUtil.mul(amount, config.getPercent())) > config.getLimitMoney() ? config.getLimitMoney() : Math.abs(NumericUtil.mul(amount, config.getPercent()));
		changeMoney = NumericUtil.round(changeMoney, 2);

		String pno = sequenceService.generateProposalNo(type);

		String remark = "自助" + Constant.QT + preferentialType + "," + config.getBetMultiples() + "倍流水,存" + amount + "送" + changeMoney + "(self-help QT " + description + " preferential)";

		String transferId = sequenceService.generateTransferId();

		// 新增转账记录
		Transfer transfer = new Transfer();
		transfer.setId(Long.parseLong(transferId));
		transfer.setSource(PlatformCode.ProductSign.getCode(product));
		transfer.setTarget(PlatformCode.Transfer.getCode(Constant.QT));
		transfer.setRemit(amount);
		transfer.setLoginName(loginName);
		transfer.setCreateTime(currentDate);
		transfer.setCredit(credit);
		transfer.setNewCredit(NumericUtil.sub(credit, amount));
		transfer.setFlag(0);
		transfer.setRemark("转入成功");

		transferService.create(transfer);

		// 新增额度记录
		CreditLog creditLog = new CreditLog();
		creditLog.setLoginName(loginName);
		creditLog.setType(CreditChangeType.TRANSFER_QT_IN);
		creditLog.setCredit(credit);
		creditLog.setRemit(-1 * amount);
		creditLog.setNewCredit(NumericUtil.sub(credit, amount));
		creditLog.setCreateTime(currentDate);
		creditLog.setRemark("referenceNo:" + transferId + ";" + remark);

		creditLogService.create(creditLog);

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("loginName", loginName);
		paramsMap.put("platform", PlatformCode.Platform.getCode(Constant.QT));
		paramsMap.put("startTime", DateUtil.getTodayByZeroHour());

		Double betAmount = commonService.sumPlatformBet(paramsMap);
		log.info("玩家" + loginName + "领取" + Constant.QT + preferentialType + "前的有效总投注额为：" + betAmount);

		// 新增投注记录
		PreferentialRecord preferentialRecord = new PreferentialRecord(pno, loginName, PlatformCode.Platform.getCode(Constant.QT), betAmount, currentDate, 0);

		preferentialRecordService.create(preferentialRecord);

		SelfRecord newSelfRecord = new SelfRecord();
		newSelfRecord.setPno(pno);
		newSelfRecord.setLoginName(loginName);
		newSelfRecord.setPlatform(PlatformCode.Platform.getCode(Constant.QT));
		newSelfRecord.setSelfName(ProposalType.getText(Integer.parseInt(type)));
		newSelfRecord.setType(0);
		newSelfRecord.setCreateTime(currentDate);
		newSelfRecord.setUpdateTime(currentDate);
		newSelfRecord.setRemark(null);

		selfRecordService.create(newSelfRecord);

		// 新增优惠记录
		Offer offer = new Offer();
		offer.setPno(pno);
		offer.setTitle(UserRole.MONEY_CUSTOMER.getCode());
		offer.setLoginName(loginName);
		offer.setFirstCash(amount);
		offer.setMoney(changeMoney);
		offer.setRemark(remark);

		offerService.create(offer);

		// 新增提案记录
		Proposal proposal = new Proposal();
		proposal.setPno(pno);
		proposal.setProposer("system");
		proposal.setCreateTime(currentDate);
		proposal.setType(Integer.parseInt(type));
		proposal.setQuickly(user.getLevel());
		proposal.setLoginName(loginName);
		proposal.setAmount(amount);
		proposal.setAgent(user.getAgent());
		proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
		proposal.setWhereIsFrom(Constant.FROM_FRONT);
		proposal.setRemark(channel);
		proposal.setGenerateType("");
		proposal.setBetMultiples(String.valueOf(config.getBetMultiples()));
		proposal.setGiftAmount(changeMoney);
		proposal.setExecuteTime(currentDate);

		proposalService.create(proposal);

		// 新增提案扩展记录
		ProposalExtend proposalExtend = new ProposalExtend();
		proposalExtend.setPno(pno);
		proposalExtend.setPlatform(platform);
		proposalExtend.setPreferentialId(Long.parseLong(id));
		proposalExtend.setSid(sid);
		proposalExtend.setCreateTime(currentDate);
		proposalExtend.setUpdateTime(currentDate);

		proposalExtendService.create(proposalExtend);

		// 更新玩家记录
		User updateUser = new User();
		updateUser.setLoginName(loginName);
		updateUser.setCredit(-1 * amount);
		updateUser.setCreditDayDate(today);

		if (StringUtils.isBlank(lastTime) || !today.equals(lastTime)) {

			updateUser.setCreditDay(-1 * creditDay + amount);
		} else {

			updateUser.setCreditDay(amount);
		}

		userService.update(updateUser);

		// 新增玩家状态扩展记录
		UserStatus userStatus = userStatusService.get(loginName);

		if (null == userStatus) {

			userStatus = new UserStatus();
			userStatus.setLoginName(loginName);
			userStatus.setCashInWrong(0);

			userStatusService.create(userStatus);
		}

		if (StringUtils.isNotBlank(sid)) {

			// 新增会员事件记录
			ActionLog actionLog = new ActionLog();
			actionLog.setLoginName(loginName);
			actionLog.setAction(ActionLogType.APP_MACHINE_CODE.getCode());
			actionLog.setCreateTime(currentDate);
			actionLog.setRemark(sid);

			actionLogService.create(actionLog);
		}

		Double giftMoney = NumericUtil.add(amount, changeMoney);

		resultMap.put("giftMoney", giftMoney);
		resultMap.put("referenceId", transferId);
		resultMap.put("message", "自助" + Constant.QT + preferentialType + "成功！");

		return mapper.writeValueAsString(resultMap);
	}
}