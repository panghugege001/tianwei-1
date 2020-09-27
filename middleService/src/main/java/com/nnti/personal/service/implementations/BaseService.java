package com.nnti.personal.service.implementations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.PlatformCode;
import com.nnti.common.constants.ProposalFlagType;
import com.nnti.common.constants.ProposalType;
import com.nnti.common.constants.UserRole;
import com.nnti.common.model.vo.BigBang;
import com.nnti.common.model.vo.Const;
import com.nnti.common.model.vo.LosePromo;
import com.nnti.common.model.vo.PreferTransferRecord;
import com.nnti.common.model.vo.PreferentialRecord;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.model.vo.SelfRecord;
import com.nnti.common.model.vo.Transfer;
import com.nnti.common.model.vo.User;
import com.nnti.common.model.vo.UserBankInfo;
import com.nnti.common.model.vo.UserStatus;
import com.nnti.common.service.interfaces.IBigBangService;
import com.nnti.common.service.interfaces.ICommonService;
import com.nnti.common.service.interfaces.IConstService;
import com.nnti.common.service.interfaces.ILosePromoService;
import com.nnti.common.service.interfaces.IPTProfitService;
import com.nnti.common.service.interfaces.IPreferTransferRecordService;
import com.nnti.common.service.interfaces.IPreferentialRecordService;
import com.nnti.common.service.interfaces.IProposalExtendService;
import com.nnti.common.service.interfaces.IProposalService;
import com.nnti.common.service.interfaces.ISelfRecordService;
import com.nnti.common.service.interfaces.ITransferService;
import com.nnti.common.service.interfaces.IUserBankInfoService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.service.interfaces.IUserStatusService;
import com.nnti.common.utils.AGINUtil;
import com.nnti.common.utils.DTUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.NumericUtil;
import com.nnti.common.utils.SlotUtil;
import com.nnti.personal.model.vo.CollectPromo;
import com.nnti.personal.model.vo.ExperienceGoldConfig;
import com.nnti.personal.model.vo.PreferentialConfig;
import com.nnti.personal.service.interfaces.ICollectPromoService;
import com.nnti.personal.service.interfaces.IExperienceConfigService;
import com.nnti.personal.service.interfaces.IPreferentialConfigService;

public class BaseService {

	private static Logger log = Logger.getLogger(BaseService.class);

	@Autowired
	private IExperienceConfigService experienceConfigService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IProposalService proposalService;
	@Autowired
	private ITransferService transferService;
	@Autowired
	private IPreferentialConfigService preferentialConfigService;
	@Autowired
	private IPreferentialRecordService preferentialRecordService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IUserStatusService userStatusService;
	@Autowired
	private IProposalExtendService proposalExtendService;
	@Autowired
	private ISelfRecordService selfRecordService;
	@Autowired
	private IPreferTransferRecordService preferTransferRecordService;
	@Autowired
	private ILosePromoService losePromoService;
	@Autowired
	private IBigBangService bigBangService;
	@Autowired
	private IPTProfitService pTProfitService;
	@Autowired
	private IConstService constService;
	@Autowired
	private IUserBankInfoService userBankInfoService;
	@Autowired
	private ICollectPromoService collectPromoService;

	/****************************************公共逻辑代码开始处****************************************/

	// 转账限制
	public String transferLimit(String loginName, Double amount) throws Exception {

		User user = userService.get(loginName);

		if (null == user) {

			return "玩家账号不存在！";
		}

		if (null == user.getFlag() || 1 == user.getFlag().intValue()) {

			return "玩家账号已被禁用，请联系在线客服处理！";
		}

		if (null == user.getLevel()) {

			return "未设置账号等级，请联系在线客服设置后再进行操作！";
		}

		if (user.getCredit() < amount) {

			return "主账户余额不足！";
		}

		if (!(UserRole.MONEY_CUSTOMER.getCode().equals(user.getRole()))) {

			return "代理不允许操作！";
		}

		// 获取当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());

		// 获取当日限制转入金额
		Double limit = user.getCreditLimit();
		// 获取上一次总转账金额
		Double remit = user.getCreditDay();
		// 获取上一次转账时间
		String lastTime = user.getCreditDayDate();

		// 判断上一次转账时间是否是当天，如果不是，则设置转账金额为0
		if (StringUtils.isBlank(lastTime) || !today.equals(lastTime)) {

			remit = 0.00;
		}

		// -1表示转账没有限制/0表示不能转账/1000表示一天最高转账1000
		if (-1 == limit) {

			return null;
		} else if (0 == limit) {

			return "当前账号不允许进行转账操作，请联系在线客服处理！";
		} else {

			if (limit < amount) {

				return "今天最高转入额度为" + limit + "，目前可以转入额度为：" + (limit - amount);
			}

			Double remitAll = remit + amount;

			if (limit < remitAll) {

				return "今天最高转入额度为" + limit + "，目前可以转入额度为:" + (limit - remitAll);
			}
		}

		return null;
	}
	
	// 抓取各平台投注额
	public Double getBetAmount(String product, String loginName, String platform, String startTime, String endTime, Double validBet) throws Exception {

		Double betAmount = 0.0;

		if (Constant.MG.equalsIgnoreCase(platform)) {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("loginname", loginName);
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			
			betAmount = commonService.sumMgBet(params);
		} else if (Constant.DT.equalsIgnoreCase(platform)) {

			betAmount = DTUtil.getBetAmount(product, loginName, startTime, endTime);
		} else if (Constant.QT.equalsIgnoreCase(platform) || Constant.TTG.equalsIgnoreCase(platform) || PlatformCode.Platform.getCode(Constant.PT).equalsIgnoreCase(platform)) {

			Double sumBet = getPlatformDataBet(loginName, platform, startTime);

			validBet = null == validBet ? 0.00 : validBet;

			betAmount = NumericUtil.sub(sumBet, validBet);
		} else if (Constant.NT.equalsIgnoreCase(platform)) {

			Double sumBet = getPTProfitBet(loginName, startTime);

			validBet = null == validBet ? 0.00 : validBet;

			betAmount = NumericUtil.sub(sumBet, validBet);
		} else if (Constant.PNG.equalsIgnoreCase(platform)) {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("loginname", "DY8"+loginName);
			params.put("startTime", DateUtil.convertUTC8toUTC(startTime));
			params.put("endTime", endTime);
			
			betAmount = commonService.sumPngBet(params);
		} else if (Constant.SW.equalsIgnoreCase(platform)) {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("loginname", loginName);
			params.put("startTime", startTime);
			params.put("endTime", endTime);
			
			betAmount = commonService.sumSwBet(params);
		} else if (Constant.AGIN.equalsIgnoreCase(platform)) {

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("loginname", loginName);
			params.put("startTime", DateUtil.convertUTCXtoUTC(startTime,12));//相差12个小时
			params.put("endTime", DateUtil.convertUTCXtoUTC(endTime,12));
			
			betAmount = commonService.sumAginBet(params);
		} else if (Constant.SLOT.equalsIgnoreCase(platform)) {

			Double sumBet = SlotUtil.getPlayerBetsByTime(product, loginName, startTime, endTime);

			sumBet = null == sumBet ? 0.00 : sumBet;

			validBet = null == validBet ? 0.00 : validBet;

			betAmount = NumericUtil.sub(sumBet, validBet);
		}

		return betAmount;
	}

	// 抓取投注额
	public Double getPlatformDataBet(String loginName, String platform, String startTime) throws Exception {

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("loginName", loginName);
		paramsMap.put("platform", platform);

		if (StringUtils.isNotBlank(startTime)) {

			SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YYYY_MM_DD_HH_MM_SS);
			Date startDate = sdf.parse(startTime);

			Calendar cd = Calendar.getInstance();

			cd.setTime(startDate);
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);

			paramsMap.put("startTime", cd.getTime());
		}

		Double sumBet = commonService.sumPlatformBet(paramsMap);

		return null == sumBet ? 0.00 : sumBet;
	}

	// 抓取投注额
	public Double getPTProfitBet(String loginName, String startTime) throws Exception {

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("loginName", loginName);

		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YYYY_MM_DD_HH_MM_SS);
		Date startDate = sdf.parse(startTime);

		Calendar cd = Calendar.getInstance();

		cd.setTime(startDate);
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);

		paramsMap.put("startTime", cd.getTime());

		Double sumBet = pTProfitService.sumBetCredit(paramsMap);

		return null == sumBet ? 0.00 : sumBet;
	}

	/****************************************公共逻辑代码结束处****************************************/

	/****************************************优惠逻辑代码开始处****************************************/

	// 首存优惠限制
	public String firstLimit(String loginName, String name, String type, String id) throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("functionName", "deposit");

		User user = userService.get(loginName);

		if (null == user) {

			return "玩家账号不存在！";
		}

		// 无效账号不允许申请优惠
		if (4 == user.getWarnFlag().intValue() || 2== user.getWarnFlag().intValue()) {

			return "抱歉，您不能自助" + name + "首存优惠！";
		}

		// 对于非安全玩家，需要查询同姓名/同IP下三个月内是否有领取过体验金，防止玩家套利
		if (!(3 == user.getWarnFlag().intValue())) {

			params.put("accountName", user.getAccountName());
			params.put("registerIp", user.getRegisterIp());

			List<User> userList = userService.findUserList(params);

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);

			Date beginDate = calendar.getTime();

			params.put("userList", userList);
			params.put("flag", ProposalFlagType.EXCUTED.getCode());
			params.put("type", type);
			params.put("createTime", beginDate);

			List<Proposal> proposalList = proposalService.findProposalList(params);

			if (null != proposalList && !proposalList.isEmpty()) {

				return "抱歉，您不能自助" + name + "首存优惠！";
			}
		}

		params.put("loginName", loginName);
		params.put("target", PlatformCode.Transfer.getCode(name));
		params.put("remark", "自助优惠");

		List<Transfer> transferList = transferService.findList(params);

		if (null != transferList && !transferList.isEmpty()) {

			return "抱歉，您不是第一次转账到" + name + "，不能自助" + name + "首存优惠！";
		}

		/*Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("loginname", loginName);
		paramsMap.put("type", type);

		List<Proposal> queryList = proposalService.findProposalList(params);

		if (null != queryList && !queryList.isEmpty()) {

			return "超过最大使用次数！";
		}

		PreferentialConfig config = preferentialConfigService.get(Long.parseLong(id));

		if (null != config && StringUtils.isNotBlank(config.getGroupId())) {

			paramsMap.put("groupId", config.getGroupId());

			List<PreferentialConfig> pcList = preferentialConfigService.findList(paramsMap);

			if (null != pcList && !pcList.isEmpty()) {

				List<Long> preferentialList = new ArrayList<Long>();

				for (PreferentialConfig pc : pcList) {

					preferentialList.add(pc.getId());
				}

				paramsMap.put("preferentialList", preferentialList);

				Integer count = proposalService.countFirstDeposit(paramsMap);

				if (count > 0) {

					return "超过最大使用次数！";
				}
			}
		}*/

		return null;
	}

	// 优惠限制
	public Object preferentialLimit(String product, String loginName, String platform, String id, String type, String sid, Double amount) throws Exception {

		String msg = this.transferLimit(loginName, amount);

		if (StringUtils.isNotBlank(msg)) {

			return msg;
		}

		User user = userService.get(loginName);

		String[] productArr = new String[] { "ul", "lh", "dy", "ql", "ufa", "loh" };
		List<String> productList = Arrays.asList(productArr);

		if (productList.contains(product)) {

			if (null == user.getBirthday() || StringUtils.isBlank(user.getAccountName())) {

				return "请先完善您的基本资料再来申请优惠哦^_^";
			}
		}

		PreferentialConfig config = preferentialConfigService.get(Long.parseLong(id));

		if (null == config || 0 == config.getIsUsed().intValue()) {

			return "优惠配置尚未开启！";
		}

		if (!(config.getPlatformId().equals(platform))) {

			return "申请的优惠平台不一致，请联系在线客服进行核实后再操作！";
		}

		if (!(config.getTitleId().equals(type))) {

			return "申请的优惠类型不一致，请联系在线客服进行核实后再操作！";
		}

		if (0 == config.getDeleteFlag().intValue()) {

			return "申请的优惠已被删除，请联系在线客服进行核实后再操作！";
		}

		if (StringUtils.isBlank(config.getVip())) {

			return "申请的优惠未设置对应的等级，请联系在线客服进行设置后再申请此优惠！";
		}

		if (null == config.getTimesFlag()) {

			return "申请的优惠未配置次数类别，请联系在线客服进行核实后再操作！";
		}

		if (null == config.getTimes()) {

			return "申请的优惠未配置使用次数，请联系在线客服进行核实后再操作！";
		}

		if (!(config.getVip().contains(String.valueOf(user.getLevel())))) {

			return "抱歉，您当前的等级不允许申请此优惠，请联系在线客服进行核实后再操作！";
		}

		if (null == config.getStartTime() || null == config.getEndTime()) {

			return "申请的优惠未设置启用开始时间或者启用结束时间，请联系在线客服进行设置后再申请此优惠！";
		}

		Date currentDate = DateUtil.getCurrentDate();

		if (!(currentDate.getTime() >= config.getStartTime().getTime() && currentDate.getTime() <= config.getEndTime().getTime())) {

			return "该优惠不在申请的时间范围内，请联系在线客服进行核实后再操作！";
		}

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Date executeTime = null;

		int timesFlag = config.getTimesFlag().intValue();

		if (1 == timesFlag) {

			executeTime = DateUtil.getTodayByZeroHour();
		} else if (2 == timesFlag) {

			executeTime = DateUtil.getWeekByFirstDay();
		} else if (3 == timesFlag) {

			executeTime = DateUtil.getMonthByFirstDay();
		} else if (4 == timesFlag) {

			executeTime = DateUtil.getYearByFirstDay();
		}

		paramsMap.put("executeTime", executeTime);
		paramsMap.put("loginName", loginName);
		paramsMap.put("flag", ProposalFlagType.EXCUTED.getCode());
		paramsMap.put("type", type);
		paramsMap.put("platform", platform);
		paramsMap.put("preferentialId", id);

		Integer times = proposalService.preferentialTimes(paramsMap);

		if (null != times && times.intValue() >= config.getTimes().intValue()) {

			return "超过最大使用次数！";
		}

		if (StringUtils.isNotBlank(config.getGroupId())) {

			paramsMap.put("groupId", config.getGroupId());
			paramsMap.put("noContain", "experience");

			times = proposalService.preferentialTimes(paramsMap);

			Integer mutexTimes = config.getMutexTimes();

			if (null != times && null != mutexTimes && 0 != mutexTimes.intValue() && times.intValue() >= mutexTimes.intValue()) {

				return "超过最大使用次数！";
			}

			/*if (null != times && times.intValue() >= config.getTimes().intValue()) {

				return "超过最大使用次数！";
			}*/
		}

		if (null != config.getDepositAmount()) {

			if (null == config.getDepositStartTime()) {

				return "已设置指定时间段内需要的存款额，但未设置开始时间，请联系在线客服进行核实后再操作！";
			}

			if (null == config.getDepositEndTime()) {

				return "已设置指定时间段内需要的存款额，但未设置结束时间，请联系在线客服进行核实后再操作！";
			}

			paramsMap.put("startTime", config.getDepositStartTime());
			paramsMap.put("endTime", config.getDepositEndTime());

			Double deposit = commonService.getDeposit(paramsMap);

			if (deposit < config.getDepositAmount()) {

				return "对不起，您的存款尚未达到本活动的要求！";
			}
		}

		if (null != config.getBetAmount()) {

			if (null == config.getBetStartTime()) {

				return "已设置指定时间段内需要的输赢值，但未设置开始时间，请联系在线客服进行核实后再操作！";
			}

			if (null == config.getBetEndTime()) {

				return "已设置指定时间段内需要的输赢值，但未设置结束时间，请联系在线客服进行核实后再操作！";
			}

			paramsMap.put("startTime", config.getBetStartTime());
			paramsMap.put("endTime", config.getBetEndTime());

			Double bet = commonService.getWinLose(paramsMap);

			if (bet > config.getBetAmount()) {

				return "对不起，您的输值尚未达到本活动的要求！";
			}
		}

		if (StringUtils.isNotBlank(config.getIsPassSms()) && "1".equals(config.getIsPassSms())) {

			UserStatus userStatus = userStatusService.get(loginName);

			if (StringUtils.isNotBlank(userStatus.getSmsFlag()) && "0".equals(userStatus.getSmsFlag())) {

				return "对不起，您未通过短信反向验证，请联系在线客服进行核实后再操作！";
			}
		}

		if (StringUtils.isNotBlank(sid)) {

			if (null != config.getMachineCodeEnabled() && 1 == config.getMachineCodeEnabled()) {

				if (null == config.getMachineCodeTimes()) {

					return "已设置启用机器码验证，但未设置机器码使用次数，请联系在线客服进行核实后再操作！";
				}

				paramsMap.put("sid", sid);

				Integer num = proposalExtendService.count(paramsMap);

				if (num >= config.getMachineCodeTimes().intValue()) {

					return "超过领取使用次数！";
				}
			}
		}

		if (null != config.getLowestAmount()) {

			if (amount < config.getLowestAmount()) {

				return "此优惠最低转账金额为" + config.getLowestAmount() + "元！";
			}
		}

		return config;
	}

	/****************************************优惠逻辑代码结束处****************************************/

	/****************************************户内转账代码开始处****************************************/

	// 彩金限制
	public String prizeLimit(Map<String, Object> paramsMap) throws Exception {

		String product = String.valueOf(paramsMap.get("product"));
		Double remoteCredit = Double.valueOf(String.valueOf(paramsMap.get("remoteCredit")));
		String code = String.valueOf(paramsMap.get("platform"));
		String name = PlatformCode.Transfer.getPlatform(code);

		paramsMap.put("operateType", "experience");

		Double mintyj = 1.0;
		Double maxtyj = 100.0;
		Double minapp = 1.0;
		Double maxapp = 100.0;

		String[] productArr = new String[] { "zb", "loh", "ld" };
		List<String> productList = Arrays.asList(productArr);

		if (productList.contains(product)) {

			maxapp = 388.0;
		}

		paramsMap.put("title", "自助体验金");

		// 查询自助体验金配置
		List<ExperienceGoldConfig> tyjList = experienceConfigService.findList(paramsMap);

		if (null != tyjList && !tyjList.isEmpty()) {

			ExperienceGoldConfig temp = tyjList.get(0);

			mintyj = temp.getMinMoney();
			maxtyj = temp.getMaxMoney();
		}

		paramsMap.put("title", "APP下载彩金");

		// 查询APP下载彩金配置
		List<ExperienceGoldConfig> appList = experienceConfigService.findList(paramsMap);

		if (null != appList && !appList.isEmpty()) {

			ExperienceGoldConfig temp = appList.get(0);

			minapp = temp.getMinMoney();
			maxapp = temp.getMaxMoney();
		}

		List<Transfer> transferList = transferService.findList(paramsMap);

		if (null != transferList && !transferList.isEmpty()) {

			Transfer transfer = transferList.get(0);

			// 最后一笔是体验金的转账记录
			if (StringUtils.isNotBlank(transfer.getRemark()) && transfer.getRemark().contains("自助优惠")) {

				if (remoteCredit >= mintyj && remoteCredit < maxtyj) {

					return "您正在使用体验金，当" + name + "余额大于等于" + maxtyj + "元或者小于" + mintyj + "元方可才能进行户内转账！";
				}
			}

			// 最后一笔是下载彩金的转账记录
			if (StringUtils.isNotBlank(transfer.getRemark()) && transfer.getRemark().contains("APP下载彩金")) {

				if (remoteCredit >= minapp && remoteCredit < maxapp) {

					return "您正在使用下载彩金，当" + name + "余额大于等于" + maxapp + "元或者小于" + minapp + "元方可才能进行户内转账！";
				}
			}
		}

		return null;
	}

	// 救援金流水限制
	public String losePromoBetLimit(Map<String, Object> paramsMap) throws Exception {

		String product = String.valueOf(paramsMap.get("product"));
		String loginName = String.valueOf(paramsMap.get("loginName"));
		String platform = String.valueOf(paramsMap.get("platform"));
		Date currentDate = (Date) paramsMap.get("currentDate");

		List<LosePromo> losePromoList = losePromoService.findList(paramsMap);

		if (null != losePromoList && !losePromoList.isEmpty()) {

			for (LosePromo losePromo : losePromoList) {

				Double requiredBet = NumericUtil.mul(losePromo.getPromo(), losePromo.getTimes());

				Double realBet = this.getBetAmount(product, loginName, platform, DateUtil.format(losePromo.getGetTime()), DateUtil.format(currentDate), losePromo.getBetting());

				if (realBet < requiredBet) {

					return "您领取了救援金，目前还需要" + NumericUtil.formatDouble(requiredBet - realBet) + "的投注额才能进行户内转账，请您继续进行投注哦^_^";
				}
			}

			paramsMap.put("status", "2");
			paramsMap.put("conditionStatus", "1");

			// 更新救援金数据为已处理
			losePromoService.update(paramsMap);
		}

		return null;
	}

	// 大爆炸流水限制
	public String bigBangBetLimit(Map<String, Object> paramsMap) throws Exception {

		String product = String.valueOf(paramsMap.get("product"));
		String loginName = String.valueOf(paramsMap.get("loginName"));
		String platform = String.valueOf(paramsMap.get("source"));
		Date currentDate = (Date) paramsMap.get("currentDate");

		List<BigBang> bigBangList = bigBangService.findList(paramsMap);

		if (null != bigBangList && !bigBangList.isEmpty()) {

			for (BigBang bb : bigBangList) {

				Double requiredBet = NumericUtil.mul(bb.getGiftMoney(), bb.getTimes());

				Double realBet = this.getBetAmount(product, loginName, platform, DateUtil.format(bb.getGetTime()), DateUtil.format(currentDate), bb.getBetAmount());

				if (requiredBet > realBet) {

					return "您已领取活动彩金，流水须达到： " + requiredBet + " 元才能转出，目前有效流水为" + realBet + " 元！";
				}
			}

			paramsMap.put("status", "3");

			bigBangService.updateList(paramsMap);
		}

		return null;
	}
	
	// 大爆炸流水限制
	public String bigBangBetLimit4Slot(Map<String, Object> paramsMap) throws Exception {

		String product = String.valueOf(paramsMap.get("product"));
		String loginName = String.valueOf(paramsMap.get("loginName"));
		String platform = String.valueOf(paramsMap.get("source"));
		Date currentDate = (Date) paramsMap.get("currentDate");

		List<BigBang> bigBangList = bigBangService.findListBySlot(paramsMap);

		if (null != bigBangList && !bigBangList.isEmpty()) {

			for (BigBang bb : bigBangList) {

				Double requiredBet = NumericUtil.mul(bb.getGiftMoney(), bb.getTimes());

				Double realBet = this.getBetAmount(product, loginName, platform, DateUtil.format(bb.getGetTime()), DateUtil.format(currentDate), null);

				if (requiredBet > realBet) {

					return "您已领取活动彩金，流水须达到： " + requiredBet + " 元才能转出，目前有效流水为" + realBet + " 元！";
				}
			}

			paramsMap.put("status", "3");

			bigBangService.updateList4Slot(paramsMap);
		}

		return null;
	}

	// 集字优惠流水限制
	public String collectPromoBetLimit(Map<String, Object> paramsMap) throws Exception {

		String product = String.valueOf(paramsMap.get("product"));
		String loginName = String.valueOf(paramsMap.get("loginName"));
		String platform = String.valueOf(paramsMap.get("platform"));
		Date currentDate = (Date) paramsMap.get("currentDate");

		List<CollectPromo> collectPromoList = collectPromoService.findList(paramsMap);

		if (PlatformCode.Transfer.getCode(Constant.PT).equals(platform)) {

			platform = PlatformCode.Platform.getCode(Constant.PT);
		}

		if (null != collectPromoList && !collectPromoList.isEmpty()) {

			for (CollectPromo collectPromo : collectPromoList) {

				Double requiredBet = NumericUtil.mul(collectPromo.getAmount(), collectPromo.getTimes());

				Double realBet = this.getBetAmount(product, loginName, platform, DateUtil.format(collectPromo.getCreateTime()), DateUtil.format(currentDate), collectPromo.getBetting());

				if (realBet < requiredBet) {

					return "您已领取集字优惠，流水须达到" + requiredBet + "元才能转出，目前流水额为" + realBet + "元！";
				}
			}

			paramsMap.put("status", "2");
			paramsMap.put("conditionStatus", "1");

			// 更新集字优惠数据为已处理
			collectPromoService.update(paramsMap);
		}

		return null;
	}

	// 优惠流水限制
	public String preferentialBetLimit(Map<String, Object> paramsMap) throws Exception {

		String product = String.valueOf(paramsMap.get("product"));
		String loginName = String.valueOf(paramsMap.get("loginName"));
		String platform = String.valueOf(paramsMap.get("platform"));
		Double remoteCredit = Double.valueOf(String.valueOf(paramsMap.get("remoteCredit")));
		Date currentDate = (Date) paramsMap.get("currentDate");
		String action = String.valueOf(paramsMap.get("action"));

		List<SelfRecord> selfRecordList = selfRecordService.findList(paramsMap);

		if (null != selfRecordList && !selfRecordList.isEmpty()) {

			SelfRecord selfRecord = selfRecordList.get(0);

			Proposal proposal = proposalService.get(selfRecord.getPno());

			if (((new Date()).getTime() - proposal.getExecuteTime().getTime()) < 5 * 60 * 1000) {

				return "申请自助优惠五分钟内不允许户内转账！";
			}

			// 需要达到的总投注额
			Double allAmount = NumericUtil.add(proposal.getAmount(), proposal.getGiftAmount()) * Integer.parseInt(proposal.getBetMultiples());
			// 有效投注额
			Double validBetAmount = null;
			// 优惠名称
			String typeName = ProposalType.getText(proposal.getType());

			PreferentialRecord preferentialRecord = preferentialRecordService.get(selfRecord.getPno());

			if (1 == preferentialRecord.getType()) {

				validBetAmount = -1.0;
			} else {

				validBetAmount = this.getBetAmount(product, loginName, platform, DateUtil.format(selfRecord.getCreateTime()), DateUtil.format(currentDate), preferentialRecord.getValidBet());
			}

			log.info(loginName + "->自助优惠流水限制参数validBetAmount：" + validBetAmount + "，allAmount：" + allAmount + "，remoteCredit：" + remoteCredit + "，createTime：" + selfRecord.getCreateTime() + "，platform：" + platform);

			if (validBetAmount != -1.0) {

				if (validBetAmount >= allAmount || remoteCredit < 5) {

					selfRecord.setType(1);
					selfRecord.setRemark("目前有效投注总额为" + validBetAmount + "元，需要达到投注总额为" + allAmount + "元，" + platform.toUpperCase() + "游戏账户余额为" + remoteCredit + "元");
					selfRecord.setUpdateTime(currentDate);

					selfRecordService.update(selfRecord);
				} else {

					Double needBet = NumericUtil.sub(allAmount, validBetAmount);

					PreferTransferRecord preferTransferRecord = new PreferTransferRecord(loginName, validBetAmount, needBet, typeName, currentDate, platform, action);

					preferTransferRecordService.create(preferTransferRecord);

					StringBuilder sb = new StringBuilder();

					sb.append("您目前存在流水限制，还需要" + NumericUtil.formatDouble(allAmount - validBetAmount) + "的投注额才能进行户内转账，请您继续进行投注哦^_^");

					return sb.toString();
				}
			} else {

				selfRecord.setType(1);
				selfRecord.setRemark("后台放行");
				selfRecord.setUpdateTime(currentDate);

				selfRecordService.update(selfRecord);
			}
		}

		return null;
	}

	/****************************************户内转账代码结束处****************************************/
	

	

	

	

	

	

	

	

	

	

	public String commitPTSelf(Map<String, Object> params) throws Exception {
		User user = userService.get(params.get("loginName").toString());
		if (null == user) {
			return  "玩家不存在！";
		}
		if (user.getFlag() == 1) {
			return  "玩家被冻结！";
		}
		String loginName = user.getLoginName();
		log.info("============loginName：=============="+loginName);
		// 1验证是否在其他机器上登录过
		// if(!StringUtils.equals(getPostCode(params.get("sid").toString()),
		// user.getPostCode())){
		//
		// message = "你的账号在其他设备登录过，请重新登录后再领取自助体验金！";
		// resultMap.put("message", message);
		// return mapper.writeValueAsString(resultMap);
		// }
		// 2体验金开关是否开启
		Const st = constService.get("自助体验金");

		if (null == st || "0".equals(st.getValue())) {

			return  "体验金维护中！";
		}
		// 3电话号码是否有重复的。
		Map<String, Object> userParams = new HashMap<String, Object>();
		userParams.put("phone", user.getPhone());
		List<User> userList = userService.findUserList(userParams);
		if (null != userList && userList.size() > 1) {
			return "您的注册资讯重复，无法申请体验金，还请参考我们其他的优惠活动哦～！";
		}
		// 4是否绑定银行卡
		userParams.put("loginname", loginName);
		userParams.put("flag", 0);
		List<UserBankInfo> userBankList = userBankInfoService.findUserBankList(userParams);
		if (null == userBankList || userBankList.size() < 1) {
			return  "请绑定您的银行卡！";
		}

		// 5银行卡是否有重复
		if (haveRepeatCards(userBankList)) {
			return "重复银行卡信息！";
		}
		// 6是否短信验证过
		UserStatus userstatus = userStatusService.get(loginName);

		if (!StringUtils.equals("1", userstatus.getSmsFlag())) {
			return "没有通过短信验证步骤,请通过正确流程获取体验金！";
		}

		// 7查询平台有没有配置自助体验金
		List<ExperienceGoldConfig> list = findExperienceConfigList(params);
		if (null == list || list.isEmpty()) {
			return "优惠配置未开启！";
		}
		ExperienceGoldConfig config = list.get(0);
		
		// 8 app是否已经领取过
		if (null != params.get("sid")) {
			if (config.getMachineCodeEnabled() != null && config.getMachineCodeEnabled() == 1
					&& config.getMachineCodeTimes() != null) {
				Map<String, Object> proposalMap = new HashMap<String, Object>();
				proposalMap.put("platform", "experience");
				proposalMap.put("sid", params.get("sid").toString());
				Integer count = proposalExtendService.count(proposalMap);
				if (count != null && count >= config.getMachineCodeTimes().intValue()) {
					return "该设备已领取过体验金！";
				}
			}
		}
		
		//判断领取次数 只能领取一次判断
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loginName", loginName);
		paramMap.put("type", "701");
		Integer times = proposalService.experienceGoldTimes(paramMap);

		if (null != times && times > 0) {
			return "您已经使用过体验金，请参考其他优惠！";
		}
		
		//判断领取次数 只能领取一次判断
		paramMap = new HashMap<String, Object>();
		paramMap.put("loginName", loginName);
		paramMap.put("platform", "experience");
		

		times = proposalService.preferentialTimes(paramMap);

		if (null != times && times > 0) {
			return "您已经使用过体验金，请参考其他优惠！";
		}

		return null;

	}
	
	public List<ExperienceGoldConfig> findExperienceConfigList(Map<String, Object> params) throws Exception
	{
		List<ExperienceGoldConfig> list = experienceConfigService.findList(params);

		/** 因等级为数字，后台根据等级做条件查询时使用的是like方式，有可能把不是自身等级的优惠也查询出来了，所以需要特殊处理 **/
		if (null != list && !list.isEmpty()) {

			List<ExperienceGoldConfig> returnList = new ArrayList<ExperienceGoldConfig>();

			for (ExperienceGoldConfig temp : list) {

				String vip = temp.getVip();

				if (StringUtils.isNotBlank(vip)) {

					String[] arr = vip.split(",");
					List<String> vipList = Arrays.asList(arr);

					if (vipList.contains(String.valueOf(params.get("vip")))) {

						returnList.add(temp);
					}
				}
			}
			list = returnList;
		}
		return list;
	}

	public String getPostCode(String sid) {

		if (sid.length() < 10) {

			return sid;
		}

		StringBuffer sb = new StringBuffer(sid);
		sb = sb.reverse();

		return sb.substring(0, 8);
	}

	public Boolean haveRepeatCards(List<UserBankInfo> userBankList) throws Exception {
		// 查询所有的卡
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("bankname", "支付宝");
		List<String> players = userBankInfoService.findUserRepeatBankNo(params);
		for (int i = 0; i < userBankList.size(); i++) {
			if (players.contains(
					userBankList.get(i).getBankno().substring(0, userBankList.get(i).getBankno().length() - 2))) {
				log.info("=================is true==============:"+userBankList.get(i).getBankno());
				return true;
			}
		}
		log.info("=================is false==============:");
		return false;

	}
	

	/**
	 * 三个月内 同手机号 或者同玩家姓名 是否有领取过8元体验金 三个月内领过则不允许，三个月内没领过 则允许
	 * 
	 * @throws Exception
	 */
	public Boolean isUsePtYouHui(String loginName, String accountName, String registeIp) throws Exception {

		// 查询该玩家是否领过，如果领过 则不能再领
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("loginNameList", Arrays.asList(new String[] {loginName}));
		paramsMap.put("remark", "自助体验金自助优惠");
		paramsMap.put("target", Arrays.asList(new String[] { "newpt", "ttg", "gpi", "nt", "qt", "mg", "dt", "png", "slot" }));

		List<Transfer> transferList = transferService.findUsedTransferList(paramsMap);

		if (null != transferList && transferList.size() > 0) {
			return true;
		}
		// 查询同IP或者同姓名下的所有玩家
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("accountName", accountName);
		params.put("registerIp", registeIp);
		params.put("functionName", "deposit"); 
		// 查询同IP或者同姓名三个月内是否有8元自助优惠领取
		
		List<User> userList = userService.findUserList(params);
		
		List<String> loginNameList = new ArrayList<String>();
		
		for (User u : userList) {
			loginNameList.add(u.getLoginName());
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		Date beginDate = calendar.getTime();

		paramsMap.put("loginNameList", loginNameList);
		paramsMap.put("remark", "自助体验金自助优惠");
		paramsMap.put("createTime", beginDate);
		paramsMap.put("target", Arrays.asList(new String[] { "newpt", "ttg", "gpi", "nt", "qt", "mg", "dt", "png", "slot" }));

		transferList = transferService.findUsedTransferList(paramsMap);
		if (null != transferList && transferList.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 
	 * @Description: 校验是否达到申请条件
	 * @param @param loginName
	 * @param @param platform
	 * @param @param amount
	 * @param @param sumBetAmount
	 * @param @return
	 * @return String    返回类型  
	 * @date  2017年12月13日上午11:35:29
	 */
	public String checkAmount(String loginName,String platform,Double amount,Double sumBetAmount) throws Exception{
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("type", ProposalType.SELF_105.getCode());
		paramsMap.put("startTime", DateUtil.getTodayByZeroHour());
		paramsMap.put("endTime", DateUtil.getTodayByEndHour());
		paramsMap.put("loginName",loginName);
		Double allAmount = 0.0;
		//截止目前有效投注额
		Double validBet = 0.0;
		//获取
		List<Proposal> proposalList = proposalService.findList(paramsMap);
		if(!proposalList.isEmpty()){
			Proposal  p = proposalList.get(0);
			PreferentialRecord preferentialRecord = preferentialRecordService.get(p.getPno());
			//截止目前有效投注额
			validBet = preferentialRecord.getValidBet();
			// 需要达到的领取投注额
			allAmount = NumericUtil.add(amount, p.getGiftAmount()) * Integer.parseInt(p.getBetMultiples());
		}
		//总投注额 - 申请之前额度 < 需要投注的额度
		if(sumBetAmount - validBet < allAmount){
			return "平台金额必须达到领取后流水，方可继续申请赠送金额！";
		}
		return null;
	}
	
	/**
	 * 
	 * @Description: 查询用户当前天充值记录
	 * @param @param loginName
	 * @return Double    返回类型  
	 * @date  2017年12月13日上午11:35:43
	 */
	public Double getOrderMoney (String loginName) throws Exception{
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("startTime", DateUtil.getTodayByZeroHour());
		paramsMap.put("endTime", DateUtil.getTodayByEndHour());
		paramsMap.put("loginName",loginName);
		return commonService.getDeposit(paramsMap);
	}
	
}