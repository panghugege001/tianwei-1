package com.nnti.personal.service.implementations;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import com.nnti.common.model.vo.BigBang;
import com.nnti.common.model.vo.Const;
import com.nnti.common.model.vo.CreditLog;
import com.nnti.common.model.vo.LosePromo;
import com.nnti.common.model.vo.PreferTransferRecord;
import com.nnti.common.model.vo.PreferentialRecord;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.model.vo.ProposalExtend;
import com.nnti.common.model.vo.Transfer;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.IBigBangService;
import com.nnti.common.service.interfaces.IConstService;
import com.nnti.common.service.interfaces.ICreditLogService;
import com.nnti.common.service.interfaces.ILosePromoService;
import com.nnti.common.service.interfaces.IPreferTransferRecordService;
import com.nnti.common.service.interfaces.IPreferentialRecordService;
import com.nnti.common.service.interfaces.IProposalExtendService;
import com.nnti.common.service.interfaces.IProposalService;
import com.nnti.common.service.interfaces.ISequenceService;
import com.nnti.common.service.interfaces.ITransferService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.BalanceUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.NumericUtil;
import com.nnti.common.utils.PTUtil;
import com.nnti.personal.model.dto.AccountTransferDTO;
import com.nnti.personal.model.dto.SelfExperienceDTO;
import com.nnti.personal.model.vo.ChristmasActivities;
import com.nnti.personal.model.vo.CollectPromo;
import com.nnti.personal.model.vo.CouponConfig;
import com.nnti.personal.model.vo.ExperienceGoldConfig;
import com.nnti.personal.service.interfaces.IActivitiesService;
import com.nnti.personal.service.interfaces.IChristmasActivitiesService;
import com.nnti.personal.service.interfaces.ICollectPromoService;
import com.nnti.personal.service.interfaces.ICouponConfigService;
import com.nnti.personal.service.interfaces.IPlatformTransferService;

@Service("ptTransferService")
public class PTTransferServiceImpl extends BaseService implements IPlatformTransferService,IActivitiesService {

	private static Logger log = Logger.getLogger(PTTransferServiceImpl.class);

	private static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private ISequenceService sequenceService;
	@Autowired
	private ILosePromoService losePromoService;
	@Autowired
	private ITransferService transferService;
	@Autowired
	IConstService constService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ICreditLogService creditLogService;
	@Autowired
	private IBigBangService bigBangService;
	@Autowired
	private IProposalService proposalService;
	@Autowired
	private IProposalExtendService proposalExtendService;
	@Autowired
	private IPreferentialRecordService preferentialRecordService;
	@Autowired
	private IPreferTransferRecordService preferTransferRecordService;
	@Autowired
	private ICollectPromoService collectPromoService;
	@Autowired
	private ICouponConfigService couponConfigService;
	@Autowired
	private IChristmasActivitiesService christmasActivitiesService;
	
	
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

		log.info("玩家" + loginName + "申请主账户转PT账户，申请时间：" + DateUtil.format(DateUtil.YYYYMMDDHHMMSS, currentDate) + "，转账金额：" + amount);

		message = this.transferLimit(loginName, amount);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		Const st = constService.get("转账NEWPT");

		if (null == st || "0".equals(st.getValue())) {

			message = "PT转账正在维护中！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		// 获取PT账户余额
		Double remoteCredit = PTUtil.getPlayerMoney(product, loginName);

		if (null == remoteCredit) {

			resultMap.put("message", "获取PT账户余额失败，请稍后重试！");
			return mapper.writeValueAsString(resultMap);
		}

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("product", product);
		paramsMap.put("remoteCredit", remoteCredit);
		paramsMap.put("platform", PlatformCode.Transfer.getCode(Constant.PT));
		paramsMap.put("loginName", loginName);
		paramsMap.put("target", PlatformCode.Transfer.getCode(Constant.PT));
		paramsMap.put("platformName", Constant.PT);

		message = this.prizeLimit(paramsMap);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		paramsMap.put("userName", loginName);
		paramsMap.put("status", "1");
		paramsMap.put("platform", PlatformCode.Platform.getCode(Constant.PT));

		List<LosePromo> losePromoList = losePromoService.findList(paramsMap);

		if (remoteCredit >= 5 && null != losePromoList && !losePromoList.isEmpty()) {

			message = "您已领取救援金，游戏账户余额需小于5元方可转账！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		paramsMap.put("status", "2");
		paramsMap.put("conditionStatus", "1");

		// 更新救援金数据为已处理
		losePromoService.update(paramsMap);

		paramsMap.put("platform", PlatformCode.Transfer.getCode(Constant.PT));
		paramsMap.put("status", "1");

		List<CollectPromo> collectPromoList = collectPromoService.findList(paramsMap);

		if (remoteCredit >= 5 && null != collectPromoList && !collectPromoList.isEmpty()) {

			message = "您已领取集字优惠，游戏账户余额需小于5元方可转账！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		paramsMap.put("status", "2");
		paramsMap.put("conditionStatus", "1");

		// 更新集字优惠数据为已处理
		collectPromoService.update(paramsMap);

		paramsMap.put("platform", Constant.PT);
		paramsMap.put("conditionStatus", "2");

		List<BigBang> bigBangList = bigBangService.findList(paramsMap);

		if (remoteCredit >= 5 && null != bigBangList && !bigBangList.isEmpty()) {

			for (BigBang bb : bigBangList) {

				Double requiredBet = NumericUtil.mul(bb.getGiftMoney(), bb.getTimes());

				Double validBet = getPlatformDataBet(loginName, PlatformCode.Platform.getCode(Constant.PT), DateUtil.format(bb.getGetTime()));

				Double betAmount = bb.getBetAmount();
				betAmount = null == betAmount ? 0.00 : betAmount;

				Double realBet = NumericUtil.sub(validBet, betAmount);

				if (requiredBet > realBet) {

					message = "您已领取活动彩金，流水须达到" + requiredBet + "元才能转入，目前有效流水为" + realBet + "元，或者游戏账户余额小于5元方可转账！";
					resultMap.put("message", message);
					return mapper.writeValueAsString(resultMap);
				}
			}

			paramsMap.put("status", "3");

			// 更新大爆炸数据为已处理
			bigBangService.updateList(paramsMap);
		}

		paramsMap.put("currentDate", currentDate);
		paramsMap.put("action", Constant.IN);

		message = betLimit(paramsMap);

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
		transfer.setTarget(PlatformCode.Transfer.getCode(Constant.PT));
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
		creditLog.setType(CreditChangeType.TRANSFER_PT_IN);
		creditLog.setCredit(credit);
		creditLog.setRemit(-1 * amount);
		creditLog.setNewCredit(NumericUtil.sub(credit, amount));
		creditLog.setCreateTime(currentDate);
		creditLog.setRemark("referenceNo:" + transferId + ";" + DateUtil.format(DateUtil.YYYYMMDDHHMMSS, currentDate));

		creditLogService.create(creditLog);

		Double dayCredit = amount;

		if (StringUtils.isBlank(lastTime) || !today.equals(lastTime)) {

			dayCredit = -1 * creditDay + amount;
		}

		Map<String, Object> userMap = new HashMap<String, Object>();

		userMap.put("credit", -1 * amount);
		userMap.put("creditDayDate", today);
		userMap.put("creditDay", dayCredit);
		userMap.put("shippingCodePt", null);
		userMap.put("loginName", loginName);

		// 更新玩家记录
		userService.updateShippingCode(userMap);

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

		Const st = constService.get("转账NEWPT");

		if (null == st || "0".equals(st.getValue())) {

			message = "PT转账正在维护中！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		// 获取PT账户余额
		Double remoteCredit = PTUtil.getPlayerMoney(product, loginName);

		if (null == remoteCredit) {

			resultMap.put("message", "获取PT账户余额失败，请稍后重试！");
			return mapper.writeValueAsString(resultMap);
		}

		if (remoteCredit < amount) {

			message = "转出失败，余额不足！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("product", product);
		paramsMap.put("remoteCredit", remoteCredit);
		paramsMap.put("platform", PlatformCode.Transfer.getCode(Constant.PT));
		paramsMap.put("loginName", loginName);
		paramsMap.put("target", PlatformCode.Transfer.getCode(Constant.PT));
		paramsMap.put("platformName", Constant.PT);

		message = this.prizeLimit(paramsMap);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		paramsMap.put("currentDate", currentDate);
		paramsMap.put("userName", loginName);
		paramsMap.put("status", "1");
		paramsMap.put("platform", PlatformCode.Platform.getCode(Constant.PT));

		message = this.losePromoBetLimit(paramsMap);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		paramsMap.put("status", "1");
		paramsMap.put("platform", PlatformCode.Transfer.getCode(Constant.PT));

		message = this.collectPromoBetLimit(paramsMap);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		paramsMap.put("platform", Constant.PT);
		paramsMap.put("conditionStatus", "2");
		paramsMap.put("source", PlatformCode.Platform.getCode(Constant.PT));
		paramsMap.put("transferOutAmount", amount);

		message = this.bigBangBetLimit(paramsMap);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		paramsMap.put("action", Constant.OUT);

		message = betLimit(paramsMap);

		if (StringUtils.isNotBlank(message)) {

			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		message = BalanceUtil.transferOutAccount(product, Constant.PT, loginName,null, amount, null);

		if (StringUtils.isNotBlank(message)) {

			throw new RuntimeException(message);
		}

		Double credit = user.getCredit();

		String transferId = sequenceService.generateTransferId();

		// 新增转账记录
		Transfer transfer = new Transfer();
		transfer.setId(Long.parseLong(transferId));
		transfer.setSource(PlatformCode.Transfer.getCode(Constant.PT));
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
		creditLog.setType(CreditChangeType.TRANSFER_PT_OUT);
		creditLog.setCredit(credit);
		creditLog.setRemit(amount);
		creditLog.setNewCredit(NumericUtil.add(credit, amount));
		creditLog.setCreateTime(currentDate);
		creditLog.setRemark("referenceNo:" + transferId + ";");

		creditLogService.create(creditLog);

		Map<String, Object> userMap = new HashMap<String, Object>();

		userMap.put("credit", amount);
		userMap.put("shippingCodePt", null);
		userMap.put("loginName", loginName);

		// 更新玩家记录
		userService.updateShippingCode(userMap);

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
		valuesMap.put("creditType", CreditChangeType.TRANSFER_REDCOUPONS_NEWPT);

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
		valuesMap.put("creditType", CreditChangeType.TRANSFER_NEWPT_COUPON);

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

		Const st = constService.get("转账NEWPT");

		if (null == st || "0".equals(st.getValue())) {

			resultMap.put("message", "PT转账正在维护中！");
			return mapper.writeValueAsString(resultMap);
		}

		// 获取PT账户余额
		Double remoteCredit = PTUtil.getPlayerMoney(product, loginName);

		if (null == remoteCredit) {

			resultMap.put("message", "获取PT账户余额失败，请稍后重试！");
			return mapper.writeValueAsString(resultMap);
		}

		if (remoteCredit >= 5) {

			resultMap.put("message", Constant.PT + "平台金额必须小于5元，才能使用优惠劵！");
			return mapper.writeValueAsString(resultMap);
		}

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

			creditLogRemark = "referenceNo:" + transferId + ";领取PT红包优惠券,优惠劵金额(" + giftAmount + "),优惠券代码(" + couponCode + ")";

			proposalRemark = "PT红包优惠券";
		} else if (ProposalType.SELF_319.getCode().toString().equals(couponType)) {

			giftAmount = NumericUtil.mul(amount, couponConfig.getPercent());

			if (giftAmount >= couponConfig.getLimitMoney()) {

				giftAmount = couponConfig.getLimitMoney();
			}

			remit = NumericUtil.add(amount, giftAmount);

			NumberFormat nf = NumberFormat.getPercentInstance();

			creditLogRemark = "referenceNo:" + transferId + ";领取PT" + nf.format(couponConfig.getPercent()) + "存送优惠券,转入游戏金额(" + remit + "),存入金额(" + amount + "),优惠劵金额(" + giftAmount + "),优惠券代码(" + couponCode + ")";

			proposalRemark = "PT" + nf.format(couponConfig.getPercent()) + "存送优惠券";
		}

		// 新增转账记录
		Transfer transfer = new Transfer();
		transfer.setId(Long.parseLong(transferId));
		transfer.setSource(PlatformCode.ProductSign.getCode(product));
		transfer.setTarget(PlatformCode.Transfer.getCode(Constant.PT));
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
		proposal.setShippingCode(couponCode);
		proposal.setAmount(amount);
		proposal.setGiftAmount(giftAmount);

		proposalService.create(proposal);

		// 获取数据总投注额
		Double sumBetAmount = getPlatformDataBet(loginName, PlatformCode.Platform.getCode(Constant.PT), null);

		// 新增投注记录
		PreferentialRecord preferentialRecord = new PreferentialRecord(pno, loginName, PlatformCode.Platform.getCode(Constant.PT), sumBetAmount, currentDate, 0);

		preferentialRecordService.create(preferentialRecord);

		// 更新玩家记录
		User updateUser = new User();

		updateUser.setCredit(-1 * amount);
		updateUser.setLoginName(loginName);
		updateUser.setCreditDayDate(today);
		updateUser.setShippingCodePt(couponCode);

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

		resultMap.put("giftMoney", remit);
		resultMap.put("password", user.getPassword());
		resultMap.put("sign", "SUCCESS");
		resultMap.put("message", "转账成功！");

		return mapper.writeValueAsString(resultMap);
	}

	private String betLimit(Map<String, Object> valuesMap) throws Exception {

		String loginName = String.valueOf(valuesMap.get("loginName"));
		Double remoteCredit = Double.valueOf(String.valueOf(valuesMap.get("remoteCredit")));
		Date currentDate = (Date) valuesMap.get("currentDate");
		String platform = PlatformCode.Platform.getCode(Constant.PT);
		String action = String.valueOf(valuesMap.get("action"));

		User user = userService.get(loginName);

		if (StringUtils.isNotBlank(user.getShippingCodePt())) {

			Map<String, Object> paramsMap = new HashMap<String, Object>();

			paramsMap.put("shippingCode", user.getShippingCodePt());
			paramsMap.put("flag", 2);

			List<Proposal> proposalList = proposalService.findProposalList(paramsMap);

			if (null == proposalList || proposalList.isEmpty()) {

				return "优惠码出现错误！";
			}

			Proposal proposal = proposalList.get(0);

			if (null == proposal.getExecuteTime()) {

				return "执行时间出现错误！";
			}

			if (((new Date()).getTime() - proposal.getExecuteTime().getTime()) < 6 * 60 * 1000) {

				return "申请自助优惠五分钟内不允许户内转账！";
			}

			// 需要达到的总投注额
			Double allAmount = NumericUtil.add(proposal.getAmount(), proposal.getGiftAmount()) * Integer.parseInt(proposal.getBetMultiples());
			// 有效投注额
			Double validBetAmount = null;
			// 优惠名称
			String typeName = ProposalType.getText(proposal.getType());

			PreferentialRecord preferentialRecord = preferentialRecordService.get(proposal.getPno());

			if (1 == preferentialRecord.getType()) {

				validBetAmount = -1.0;
			} else {

				// 获取数据总投注额
				Double sumBetAmount = getPlatformDataBet(loginName, platform, null);

				Double validBet = preferentialRecord.getValidBet();
				validBet = null == validBet ? 0.00 : validBet;

				validBetAmount = Double.parseDouble(NumericUtil.formatDouble(NumericUtil.sub(sumBetAmount, validBet)));
			}

			log.info(loginName + "->自助优惠流水限制参数validBetAmount：" + validBetAmount + "，allAmount：" + allAmount + "，remoteCredit：" + remoteCredit);

			if (validBetAmount != -1.0) {

				if (validBetAmount >= allAmount || remoteCredit < 5) {

				} else {

					Double needBet = Double.parseDouble(NumericUtil.formatDouble(NumericUtil.sub(allAmount, validBetAmount)));

					PreferTransferRecord preferTransferRecord = new PreferTransferRecord(loginName, validBetAmount, needBet, typeName, currentDate, platform, action);

					preferTransferRecordService.create(preferTransferRecord);

					StringBuilder sb = new StringBuilder();

					sb.append("您目前存在流水限制，还需要" + NumericUtil.formatDouble(allAmount - validBetAmount) + "的投注额才能进行户内转账，请您继续进行投注哦^_^");

					return sb.toString();
				}
			}
		}

		return null;
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
		// 获取PT账户余额
		Double remoteCredit = PTUtil.getPlayerMoney(product, loginName);

		if (null == remoteCredit) {

			resultMap.put("message", "获取PT账户余额失败，请稍后重试！");
			return mapper.writeValueAsString(resultMap);
		}
		List<ExperienceGoldConfig> list = findExperienceConfigList(params);
		if (null == list || list.isEmpty()) {
			resultMap.put("message", "优惠配置未开启！");
			return mapper.writeValueAsString(resultMap);
		}
		ExperienceGoldConfig experienceGoldConfig = list.get(0);
		if (remoteCredit >= experienceGoldConfig.getMinMoney()) {

			resultMap.put("message", Constant.PT + "平台金额必须小于" + experienceGoldConfig.getMinMoney() + "元，才能使用自助体验金！");
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
		transfer.setTarget(PlatformCode.Transfer.getCode(Constant.PT));
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
		creditLog.setType(CreditChangeType.TRANSFER_PT_IN);
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
	
	/**
	 * @Description: 存送三重奏
	 * @param @param dto
	 * @param @throws Exception    参数  
	 * @return String    返回类型  
	 * @date  2017年12月11日上午11:02:34
	 */
	@Transactional(rollbackFor = Exception.class)
	public String christmasActivities(AccountTransferDTO dto) throws Exception {
		System.out.println("PT开始-------运行");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		String platform = dto.getPlatform();
		//转账金额
		Double amount = dto.getAmount();
		Integer type = ProposalType.SELF_105.getCode();
		//一二三存送类型
		String activitiesType = dto.getCouponCode();
		//查询用户信息
		User user = userService.get(loginName);
		//账户金额
		Double credit = user.getCredit();
		//当期系统时间
		Date currentDate = new Date();
		if (credit < amount) {
			resultMap.put("message", "主账户余额不足！");
			return mapper.writeValueAsString(resultMap);
		}
		//充值金额
		Double orderMoney = this.getOrderMoney(loginName);
		if(amount > orderMoney){
			resultMap.put("message", "对不起您的条件不符合申请，请充值后进行申请！");
			return mapper.writeValueAsString(resultMap);
		}
		// 获取PT账户余额
		Double remoteCredit = PTUtil.getPlayerMoney(product, loginName);
		if (null == remoteCredit) {
			resultMap.put("message", "获取PT账户余额失败，请稍后重试！");
			return mapper.writeValueAsString(resultMap);
		}
		//过滤一重奏限制5元做法
		if(remoteCredit >= 5){
		   resultMap.put("message", Constant.PT + "平台金额必须小于5元，申请赠送金额！");
		   return mapper.writeValueAsString(resultMap);
	    }
		
		// 获取数据总投注额
		Double sumBetAmount = getPlatformDataBet(loginName, platform, null);
		String msg = null;
		// 需要达到的领取投注额
		//排除一重奏 需要<5元限制//领取一重奏后，再次领取二重奏时，需一重奏达到流水或者一重奏的转账平台金额低于五元方可领取，以此类推
		if(activitiesType.equals("存送二重奏")){
			if(amount < 500){
				resultMap.put("message", "您申请二重奏转账金额不足！最低额度为500元");
				return mapper.writeValueAsString(resultMap);
			}
			//校验
			//msg = this.checkAmount(loginName, platform, amount, sumBetAmount);
		} else if (activitiesType.equals("存送三重奏")){
			if(amount >= 500 && amount < 1000) {
				resultMap.put("message", "您申请三重奏转账金额不足！最低额度为1000元");
				return mapper.writeValueAsString(resultMap);
			}
			//校验
			//msg = this.checkAmount(loginName, platform, amount, sumBetAmount);
		} else {
			if (amount < 100) {
				resultMap.put("message", "您申请一重奏转账金额不足！最低额度为100元");
				return mapper.writeValueAsString(resultMap);
			}
		}
	    //获取当前存送类型
		ChristmasActivities christmasActivities = christmasActivitiesService.getByType(dto.getCouponCode());
		
		String transferId = sequenceService.generateTransferId();
		//总转账金额 = 本金+本金*赠送比例（赠送的金额）
		Double totalAmount = 0.0;
		//控制最高金额上限
		Double giveAmount = 0.0;
		Double bigMoney = 1000 * Double.valueOf(christmasActivities.getPercentage());
		if(amount > 1000){
			totalAmount = amount + bigMoney;
			giveAmount = 1000 * Double.valueOf(christmasActivities.getPercentage());
		}else{
			totalAmount = amount + amount * Double.valueOf(christmasActivities.getPercentage());
			giveAmount = amount * Double.valueOf(christmasActivities.getPercentage());
		}
		// 新增转账记录
		Transfer transfer = new Transfer();
		transfer.setId(Long.parseLong(transferId));
		transfer.setSource(PlatformCode.ProductSign.getCode(product));
		transfer.setTarget(PlatformCode.Transfer.getCode(Constant.PT));
		transfer.setRemit(totalAmount);
		transfer.setLoginName(loginName);
		transfer.setCreateTime(currentDate);
		transfer.setCredit(credit);
		transfer.setNewCredit(NumericUtil.sub(credit, amount));
		transfer.setFlag(0);
		transfer.setRemark(platform + christmasActivities.getType() + "存入成功");

		transferService.create(transfer);
		String pno = sequenceService.generateProposalNo(String.valueOf(type));
		//新增投注记录 增加一条投注记录
		PreferentialRecord preferentialRecord = new PreferentialRecord(pno, loginName, PlatformCode.Platform.getCode(Constant.PT), sumBetAmount, currentDate, 0);
		preferentialRecordService.create(preferentialRecord);
		
		// 新增额度记录
		CreditLog creditLog = new CreditLog();
		creditLog.setLoginName(loginName);
		creditLog.setType(CreditChangeType.TRANSFER_PT_IN);
		creditLog.setCredit(credit);
		creditLog.setRemit(amount);
		creditLog.setNewCredit(NumericUtil.sub(credit, amount));
		creditLog.setCreateTime(currentDate);
		creditLog.setRemark("提案单号："+ pno + "； "+ platform + christmasActivities.getType() + christmasActivities.getMultiple()+"倍流水；申请本金："+amount+" 送："+ giveAmount +"记录");
		creditLogService.create(creditLog);
		
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
		proposal.setRemark("（EXPERIENCE VOUCHER）");
		proposal.setGenerateType(christmasActivities.getType());
		proposal.setBetMultiples(christmasActivities.getMultiple());
		//此处红利金额为本金*倍数
		proposal.setGiftAmount(amount > 1000 ? bigMoney : amount * Double.valueOf(christmasActivities.getPercentage()));
		proposal.setExecuteTime(currentDate);
		String shippingCode = "pt" + new Date().getTime();

		proposal.setShippingCode(shippingCode);
		proposalService.create(proposal);
		
		

		// 更新玩家记录
		User updateUser = new User();
		updateUser.setLoginName(loginName);
		updateUser.setWarnFlag(3); //安全等级设置为安全
		//因为方法写死了累加 所以这个地方为负数
		updateUser.setCredit(-1 * amount);
		updateUser.setShippingCodePt(shippingCode);
		userService.update(updateUser);
		
		resultMap.put("referenceId", transferId);
		resultMap.put("amount", totalAmount);
		resultMap.put("sign", "SUCCESS");
		resultMap.put("message", "转账成功！");
		return mapper.writeValueAsString(resultMap);
	}
}