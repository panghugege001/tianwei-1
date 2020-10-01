package com.nnti.personal.service.implementations;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.CreditChangeType;
import com.nnti.common.constants.PlatformCode;
import com.nnti.common.constants.ProposalFlagType;
import com.nnti.common.constants.ProposalType;
import com.nnti.common.constants.UserRole;
import com.nnti.common.model.vo.Const;
import com.nnti.common.model.vo.CreditLog;
import com.nnti.common.model.vo.PreferentialRecord;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.model.vo.ProposalExtend;
import com.nnti.common.model.vo.SelfRecord;
import com.nnti.common.model.vo.Transfer;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.IConstService;
import com.nnti.common.service.interfaces.ICreditLogService;
import com.nnti.common.service.interfaces.IPreferentialRecordService;
import com.nnti.common.service.interfaces.IProposalExtendService;
import com.nnti.common.service.interfaces.IProposalService;
import com.nnti.common.service.interfaces.ISelfRecordService;
import com.nnti.common.service.interfaces.ISequenceService;
import com.nnti.common.service.interfaces.ITransferService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.BGUtil;
import com.nnti.common.utils.BalanceUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.NumericUtil;
import com.nnti.personal.model.dto.AccountTransferDTO;
import com.nnti.personal.model.dto.SelfExperienceDTO;
import com.nnti.personal.model.vo.CouponConfig;
import com.nnti.personal.model.vo.ExperienceGoldConfig;
import com.nnti.personal.service.interfaces.ICouponConfigService;
import com.nnti.personal.service.interfaces.IPlatformTransferService;

@Service("bgTransferService")
public class BGTransferServiceImpl extends BaseService implements IPlatformTransferService {

	private static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private ISequenceService sequenceService;
	@Autowired
	private ITransferService transferService;
	@Autowired
	IConstService constService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ICreditLogService creditLogService;
	@Autowired
	private IProposalService proposalService;
	@Autowired
	private IProposalExtendService proposalExtendService;
	@Autowired
	private IPreferentialRecordService preferentialRecordService;
	@Autowired
	private ISelfRecordService selfRecordService;
	@Autowired
	private ICouponConfigService couponConfigService;

	// 转入
	@Transactional(rollbackFor = Exception.class)
	public String transferIn(AccountTransferDTO dto) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String message = "";

		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		Double amount = dto.getAmount();

		Date currentDate = DateUtil.getCurrentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(currentDate);

		message = this.transferLimit(loginName, amount);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		Const st = constService.get("转账BG");

		if (null == st || "0".equals(st.getValue())) {

			message = "BG转账正在维护中！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		// 获取BG账户余额
		Double remoteCredit = BGUtil.getBalance(loginName);

		if (null == remoteCredit) {

			message = "获取BG账户余额失败，请稍后重试！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("remoteCredit", remoteCredit);
		paramsMap.put("platform", PlatformCode.Transfer.getCode(Constant.BG));
		paramsMap.put("loginName", loginName);
		paramsMap.put("product", product);
		paramsMap.put("currentDate", currentDate);
		paramsMap.put("type", "0");
		paramsMap.put("action", Constant.IN);

		message = this.preferentialBetLimit(paramsMap);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		User user = userService.get(loginName);

		Double credit = user.getCredit();
		Double creditDay = user.getCreditDay();
		String lastTime = user.getCreditDayDate();

		String transferId = sequenceService.generateTransferId();

		// 新增转账记录
		Transfer transfer = new Transfer();
		transfer.setId(Long.parseLong(transferId));
		transfer.setSource(PlatformCode.ProductSign.getCode(product));
		transfer.setTarget(PlatformCode.Transfer.getCode(Constant.BG));
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
		creditLog.setType(CreditChangeType.TRANSFER_BG_IN);
		creditLog.setCredit(credit);
		creditLog.setRemit(-1 * amount);
		creditLog.setNewCredit(NumericUtil.sub(credit, amount));
		creditLog.setCreateTime(currentDate);
		creditLog.setRemark("referenceNo:" + transferId + ";");

		creditLogService.create(creditLog);

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

		resultMap.put("referenceId", transferId);
		resultMap.put("sign", "SUCCESS");
		resultMap.put("message", "转账成功！");

		return mapper.writeValueAsString(resultMap);
	}

	// 转出
	@Transactional(rollbackFor = Exception.class)
	public String transferOut(AccountTransferDTO dto) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		String message = "";

		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		Double amount = dto.getAmount();

		Date currentDate = DateUtil.getCurrentDate();

		User user = userService.get(loginName);

		if (null == user) {

			message = "玩家账号不存在！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		if (null == user.getFlag() || 1 == user.getFlag().intValue()) {

			message = "玩家账号已被禁用，请联系在线客服处理！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		if (null == user.getLevel()) {

			message = "未设置账号等级，请联系在线客服设置后再进行操作！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		if (!(UserRole.MONEY_CUSTOMER.getCode().equals(user.getRole()))) {

			message = "代理不允许操作！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		Const st = constService.get("转账BG");

		if (null == st || "0".equals(st.getValue())) {

			message = "BG转账正在维护中！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		// 获取BG账户余额
		Double remoteCredit = BGUtil.getBalance(loginName);

		if (null == remoteCredit) {

			message = "获取BG账户余额失败，请稍后重试！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		if (remoteCredit < amount) {

			message = "转出失败，余额不足！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("remoteCredit", remoteCredit);
		paramsMap.put("platform", PlatformCode.Transfer.getCode(Constant.BG));
		paramsMap.put("loginName", loginName);
		paramsMap.put("product", product);
		paramsMap.put("currentDate", currentDate);
		paramsMap.put("type", "0");
		paramsMap.put("action", Constant.OUT);

		message = this.preferentialBetLimit(paramsMap);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		String transferId = sequenceService.generateTransferId();
		
		message = BalanceUtil.transferOutAccount(product, Constant.BG, loginName,user.getPassword(), amount, transferId);

		if (StringUtils.isNotBlank(message)) {

			throw new RuntimeException(message);
		}

		Double credit = user.getCredit();

		// 新增转账记录
		Transfer transfer = new Transfer();
		transfer.setId(Long.parseLong(transferId));
		transfer.setSource(PlatformCode.Transfer.getCode(Constant.BG));
		transfer.setTarget(PlatformCode.ProductSign.getCode(product));
		transfer.setRemit(amount);
		transfer.setLoginName(loginName);
		transfer.setCreateTime(currentDate);
		transfer.setCredit(credit);
		transfer.setNewCredit(NumericUtil.add(credit, amount));
		transfer.setFlag(0);
		transfer.setRemark("转出成功");

		transferService.create(transfer);

		// 新增额度记录
		CreditLog creditLog = new CreditLog();
		creditLog.setLoginName(loginName);
		creditLog.setType(CreditChangeType.TRANSFER_BG_OUT);
		creditLog.setCredit(credit);
		creditLog.setRemit(amount);
		creditLog.setNewCredit(NumericUtil.add(credit, amount));
		creditLog.setCreateTime(currentDate);
		creditLog.setRemark("referenceNo:" + transferId + ";");

		creditLogService.create(creditLog);

		// 更新玩家记录
		User updateUser = new User();
		updateUser.setLoginName(loginName);
		updateUser.setCredit(amount);

		userService.update(updateUser);

		resultMap.put("sign", "SUCCESS");
		resultMap.put("message", "转账成功！");

		return mapper.writeValueAsString(resultMap);
	}

	// 红包优惠券
	@Transactional(rollbackFor = Exception.class)
	public String redEnvelopeCoupon(AccountTransferDTO dto) throws Exception {

		Map<String, String> valuesMap = new HashMap<String, String>();

		valuesMap.put("product", dto.getProduct());
		valuesMap.put("loginName", dto.getLoginName());
		valuesMap.put("couponCode", dto.getCouponCode());
		valuesMap.put("amount", "0.00");
		valuesMap.put("couponType", ProposalType.SELF_419.getCode().toString());
		valuesMap.put("creditType", CreditChangeType.TRANSFER_REDCOUPONS_BGIN);

		return commonCoupon(valuesMap);
	}

	// 存送优惠券
	@Transactional(rollbackFor = Exception.class)
	public String depositCoupon(AccountTransferDTO dto) throws Exception {

		Map<String, String> valuesMap = new HashMap<String, String>();

		valuesMap.put("product", dto.getProduct());
		valuesMap.put("loginName", dto.getLoginName());
		valuesMap.put("couponCode", dto.getCouponCode());
		valuesMap.put("platform", dto.getPlatform());
		valuesMap.put("amount", String.valueOf(dto.getAmount()));
		valuesMap.put("couponType", ProposalType.SELF_319.getCode().toString());
		valuesMap.put("creditType", CreditChangeType.TRANSFER_BG_COUPON);

		return commonCoupon(valuesMap);
	}

	private String commonCoupon(Map<String, String> valuesMap) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 所属产品标识
		String product = valuesMap.get("product");
		// 领取账号
		String loginName = valuesMap.get("loginName");
		// 优惠券代码
		String couponCode = valuesMap.get("couponCode");
		// 转账游戏平台(如果是红包优惠券，该值为空；如果是存送优惠券，该值为实际转账平台)
		String platform = valuesMap.get("platform");
		// 转账金额(如果是红包优惠券，该值为0；如果是存送优惠券，该值为实际转账金额)
		Double amount = Double.parseDouble(valuesMap.get("amount"));
		// 优惠券类型(419:红包优惠券/319:存送优惠券)
		String couponType = valuesMap.get("couponType");
		// 额度类型
		String creditType = valuesMap.get("creditType");

		Date currentDate = DateUtil.getCurrentDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(currentDate);

		String message = this.transferLimit(loginName, amount);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("couponCode", couponCode);
		paramsMap.put("couponType", couponType);

		List<CouponConfig> couponList = couponConfigService.findList(paramsMap);

		if (null == couponList || couponList.isEmpty() || couponList.size() > 1) {

			resultMap.put("message", "优惠代码错误，请联系在线客服处理！");
			return mapper.writeValueAsString(resultMap);
		}

		CouponConfig couponConfig = couponList.get(0);

		if ("0".equals(couponConfig.getStatus())) {

			resultMap.put("message", "优惠代码未审核，请联系在线客服处理！");
			return mapper.writeValueAsString(resultMap);
		}

		if ("2".equals(couponConfig.getStatus())) {

			resultMap.put("message", "优惠代码已使用，请联系在线客服处理！");
			return mapper.writeValueAsString(resultMap);
		}

		if (StringUtils.isNotBlank(couponConfig.getLoginName())) {

			if (!(loginName.equals(couponConfig.getLoginName()))) {

				resultMap.put("message", "使用优惠代码的会员账号不匹配，请联系在线客服处理！");
				return mapper.writeValueAsString(resultMap);
			}
		}

		if (StringUtils.isNotBlank(platform)) {

			if (!(platform.equalsIgnoreCase(couponConfig.getPlatformId()))) {

				resultMap.put("message", "使用优惠代码与实际转账平台不一致，请联系在线客服处理！");
				return mapper.writeValueAsString(resultMap);
			}
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(currentDate);
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);

		Date startTime = calendar.getTime();

		if (!startTime.before(couponConfig.getAuditTime())) {

			resultMap.put("message", "优惠代码已过期，请联系在线客服处理！");
			return mapper.writeValueAsString(resultMap);
		}

		if (couponConfig.getMinAmount() != null && couponConfig.getMinAmount() > 0.0) {

			if (amount < couponConfig.getMinAmount()) {

				resultMap.put("message", "使用此优惠代码最低转账金额为" + couponConfig.getMinAmount() + "元，如果疑问，请联系在线客服处理！");
				return mapper.writeValueAsString(resultMap);
			}
		}

		if (couponConfig.getMaxAmount() != null && couponConfig.getMaxAmount() > 0.0) {

			if (amount > couponConfig.getMaxAmount()) {

				resultMap.put("message", "使用此优惠代码最高转账金额为" + couponConfig.getMaxAmount() + "元，如果疑问，请联系在线客服处理！");
				return mapper.writeValueAsString(resultMap);
			}
		}

		Const st = constService.get("转账BG");

		if (null == st || "0".equals(st.getValue())) {

			resultMap.put("message", "BG转账正在维护中！");
			return mapper.writeValueAsString(resultMap);
		}

		// 获取BG账户余额
		Double remoteCredit = BGUtil.getBalance(loginName);

		if (null == remoteCredit) {

			resultMap.put("message", "获取BG账户余额失败，请稍后重试！");
			return mapper.writeValueAsString(resultMap);
		}

		if (remoteCredit >= 5) {

			resultMap.put("message", Constant.BG + "平台金额必须小于5元，才能使用优惠劵！");
			return mapper.writeValueAsString(resultMap);
		}

		paramsMap.put("loginName", loginName);
		paramsMap.put("platform", PlatformCode.Platform.getCode(Constant.BG));
		paramsMap.put("conditionType", 0);
		paramsMap.put("type", 1);
		paramsMap.put("updateTime", currentDate);

		// 更新流水数据为已达到流水要求
		selfRecordService.updateList(paramsMap);

		User user = userService.get(loginName);

		Double credit = user.getCredit();
		Double creditDay = user.getCreditDay();
		String lastTime = user.getCreditDayDate();

		String transferId = sequenceService.generateTransferId();

		// 赠送金额
		Double giftAmount = 0.0;
		// 最终转账金额
		Double remit = 0.0;
		// 额度记录备注
		String creditLogRemark = "";
		// 提案记录备注
		String proposalRemark = "";

		if (ProposalType.SELF_419.getCode().toString().equals(couponType)) {

			giftAmount = couponConfig.getGiftAmount();

			remit = NumericUtil.add(amount, giftAmount);

			creditLogRemark = "referenceNo:" + transferId + ";领取BG红包优惠券,优惠劵金额(" + giftAmount + "),优惠券代码(" + couponCode + ")";

			proposalRemark = "BG红包优惠券";
		} else if (ProposalType.SELF_319.getCode().toString().equals(couponType)) {

			giftAmount = NumericUtil.mul(amount, couponConfig.getPercent());

			if (giftAmount >= couponConfig.getLimitMoney()) {

				giftAmount = couponConfig.getLimitMoney();
			}

			remit = NumericUtil.add(amount, giftAmount);

			NumberFormat nf = NumberFormat.getPercentInstance();

			creditLogRemark = "referenceNo:" + transferId + ";领取BG" + nf.format(couponConfig.getPercent()) + "存送优惠券,转入游戏金额(" + remit + "),存入金额(" + amount + "),优惠劵金额(" + giftAmount + "),优惠券代码(" + couponCode + ")";

			proposalRemark = "BG" + nf.format(couponConfig.getPercent()) + "存送优惠券";
		}

		// 新增转账记录
		Transfer transfer = new Transfer();
		transfer.setId(Long.parseLong(transferId));
		transfer.setSource(PlatformCode.ProductSign.getCode(product));
		transfer.setTarget(PlatformCode.Transfer.getCode(Constant.BG));
		transfer.setRemit(remit);
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
		creditLog.setType(creditType);
		creditLog.setCredit(credit);
		creditLog.setRemit(amount * -1);
		creditLog.setNewCredit(NumericUtil.sub(credit, amount));
		creditLog.setCreateTime(currentDate);
		creditLog.setRemark(creditLogRemark);

		creditLogService.create(creditLog);

		String pno = sequenceService.generateProposalNo(couponType);

		// 新增提案记录
		Proposal proposal = new Proposal();
		proposal.setPno(pno);
		proposal.setProposer("system");
		proposal.setCreateTime(currentDate);
		proposal.setExecuteTime(currentDate);
		proposal.setType(Integer.parseInt(couponType));
		proposal.setQuickly(user.getLevel());
		proposal.setLoginName(loginName);
		proposal.setAgent(user.getAgent());
		proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
		proposal.setWhereIsFrom(Constant.FROM_FRONT);
		proposal.setRemark(proposalRemark);
		proposal.setGenerateType("");
		proposal.setBetMultiples(String.valueOf(couponConfig.getBetMultiples()));
		proposal.setAmount(amount);
		proposal.setGiftAmount(giftAmount);

		proposalService.create(proposal);

		// 新增投注记录
		PreferentialRecord preferentialRecord = new PreferentialRecord(pno, loginName, PlatformCode.Platform.getCode(Constant.BG), null, currentDate, 0);

		preferentialRecordService.create(preferentialRecord);

		SelfRecord newSelfRecord = new SelfRecord();
		newSelfRecord.setPno(pno);
		newSelfRecord.setLoginName(loginName);
		newSelfRecord.setPlatform(PlatformCode.Platform.getCode(Constant.BG));
		newSelfRecord.setSelfName(ProposalType.getText(Integer.parseInt(couponType)));
		newSelfRecord.setType(0);
		newSelfRecord.setCreateTime(currentDate);
		newSelfRecord.setUpdateTime(currentDate);
		newSelfRecord.setRemark(null);

		selfRecordService.create(newSelfRecord);

		// 更新玩家记录
		User updateUser = new User();

		updateUser.setCredit(-1 * amount);
		updateUser.setLoginName(loginName);
		updateUser.setCreditDayDate(today);

		if (StringUtils.isBlank(lastTime) || !today.equals(lastTime)) {

			updateUser.setCreditDay(-1 * creditDay + remit);
		} else {

			updateUser.setCreditDay(remit);
		}

		userService.update(updateUser);

		paramsMap.put("status", "2");
		paramsMap.put("loginName", loginName);
		paramsMap.put("receiveTime", currentDate);
		paramsMap.put("id", couponConfig.getId());

		couponConfigService.update(paramsMap);

		resultMap.put("referenceId", transferId);
		resultMap.put("giftMoney", remit);
		resultMap.put("sign", "SUCCESS");
		resultMap.put("message", "转账成功！");

		return mapper.writeValueAsString(resultMap);
	}

	// 体验金
	@Transactional(rollbackFor = Exception.class)
	public String experienceGold(SelfExperienceDTO dto) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		String platform = dto.getPlatform();
		Integer type = ProposalType.SELF_771.getCode();
		User user = userService.get(loginName);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("conditionTime", DateUtil.format(new Date()));
		params.put("vip", user.getLevel());
		params.put("platformName", dto.getPlatform());
		params.put("channel", dto.getChannel());
		params.put("title", dto.getTitle());
		params.put("sid", dto.getSid());
		params.put("postCode", user.getPostCode());
		params.put("loginName", user.getLoginName());
		params.put("type", type);

		// 校验操作
		String result = commitPTSelf(params);

		if (result != null) {
			resultMap.put("message", result);
			return mapper.writeValueAsString(resultMap);
		}
		// 获取BG账户余额
		Double remoteCredit = BGUtil.getBalance(loginName);

		if (null == remoteCredit) {
			resultMap.put("message", "获取BG账户余额失败，请稍后重试！");
			return mapper.writeValueAsString(resultMap);
		}

		List<ExperienceGoldConfig> list = findExperienceConfigList(params);
		if (null == list || list.isEmpty()) {
			resultMap.put("message", "优惠配置未开启！");
			return mapper.writeValueAsString(resultMap);
		}
		ExperienceGoldConfig experienceGoldConfig = list.get(0);
		if (remoteCredit >= experienceGoldConfig.getMinMoney()) {

			resultMap.put("message", Constant.BG + "平台金额必须小于" + experienceGoldConfig.getMinMoney() + "元，才能使用自助体验金！");
			return mapper.writeValueAsString(resultMap);
		}

		Double amount = experienceGoldConfig.getAmount();
		Integer id = experienceGoldConfig.getId();
		String transferId = sequenceService.generateTransferId();
		Date currentDate = new Date();
		Double credit = user.getCredit();
		String remark = platform + amount.intValue() + "元自助体验金自助优惠";

		// 新增转账记录
		Transfer transfer = new Transfer();
		transfer.setId(Long.parseLong(transferId));
		transfer.setSource(PlatformCode.ProductSign.getCode(product));
		transfer.setTarget(PlatformCode.Transfer.getCode(Constant.BG));
		transfer.setRemit(amount);
		transfer.setLoginName(loginName);
		transfer.setCreateTime(currentDate);
		transfer.setCredit(credit);
		transfer.setNewCredit(credit);
		transfer.setFlag(0);
		transfer.setRemark(remark);

		transferService.create(transfer);

		String pno = sequenceService.generateProposalNo(String.valueOf(type));
		// 新增提案记录
		Proposal proposal = new Proposal();
		proposal.setPno(pno);
		proposal.setProposer("system");
		proposal.setCreateTime(currentDate);
		proposal.setType(type);
		proposal.setQuickly(user.getLevel());
		proposal.setLoginName(loginName);
		proposal.setAmount(amount);
		proposal.setAgent(user.getAgent());
		proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
		proposal.setWhereIsFrom(Constant.FROM_FRONT);
		proposal.setRemark(remark + "（EXPERIENCE VOUCHER）");
		proposal.setGenerateType("");
		// 无流水倍数
		// proposal.setBetMultiples();
		proposal.setGiftAmount(amount);
		proposal.setExecuteTime(currentDate);

		proposalService.create(proposal);

		// 新增提案扩展记录
		ProposalExtend proposalExtend = new ProposalExtend();
		proposalExtend.setPno(pno);
		proposalExtend.setPlatform("experience");
		proposalExtend.setPreferentialId(Long.valueOf(id.toString()));
		proposalExtend.setSid(dto.getSid());
		proposalExtend.setCreateTime(currentDate);
		proposalExtend.setUpdateTime(currentDate);

		proposalExtendService.create(proposalExtend);

		// 新增额度记录
		CreditLog creditLog = new CreditLog();
		creditLog.setLoginName(loginName);
		creditLog.setType(CreditChangeType.TRANSFER_BG_IN);
		creditLog.setCredit(credit);
		creditLog.setRemit(amount);
		creditLog.setNewCredit(credit);
		creditLog.setCreateTime(currentDate);
		creditLog.setRemark(remark + "（EXPERIENCE VOUCHER）");

		creditLogService.create(creditLog);

		// 更新玩家记录
		User updateUser = new User();
		updateUser.setLoginName(loginName);
		updateUser.setWarnFlag(3); // 使用了8元自助，安全等级设置为安全

		userService.update(updateUser);

		resultMap.put("referenceId", transferId);
		resultMap.put("amount", amount);
		resultMap.put("sign", "SUCCESS");
		resultMap.put("message", "转账成功！");
		return mapper.writeValueAsString(resultMap);
	}
}