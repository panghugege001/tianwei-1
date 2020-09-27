package com.nnti.personal.service.implementations;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.StaticData;
import com.nnti.common.extend.ApplicationContextProvider;
import com.nnti.common.model.vo.Transfer;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.ITransferService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.AGINUtil;
import com.nnti.common.utils.BalanceUtil;
import com.nnti.common.utils.DTUtil;
import com.nnti.common.utils.EAUtil;
import com.nnti.common.utils.EBETUtil;
import com.nnti.common.utils.MGUtil;
import com.nnti.common.utils.MWGUtils;
import com.nnti.common.utils.NTUtil;
import com.nnti.common.utils.NTwoUtil;
import com.nnti.common.utils.NumericUtil;
import com.nnti.common.utils.OGUtils;
import com.nnti.common.utils.PNGUtil;
import com.nnti.common.utils.PTUtil;
import com.nnti.common.utils.QTUtil;
import com.nnti.common.utils.SWUtil;
import com.nnti.common.utils.ShaBaUtil;
import com.nnti.common.utils.SlotUtil;
import com.nnti.common.utils.TTGUtil;
import com.nnti.personal.model.dto.AccountTransferDTO;
import com.nnti.personal.model.dto.SelfExperienceDTO;
import com.nnti.personal.service.interfaces.IAccountTransferService;
import com.nnti.personal.service.interfaces.IActivitiesService;
import com.nnti.personal.service.interfaces.IPlatformTransferService;

@Service("accountTransferService")
public class AccountTransferServiceImpl implements IAccountTransferService {

	private static Logger log = Logger.getLogger(AccountTransferServiceImpl.class);

	private static ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private ITransferService transferService;
	@Autowired
	private IUserService userService;

	
	// 个人中心->个人钱包->户内转账->提交
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String submit(AccountTransferDTO dto) throws Exception {

		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		String source = dto.getSource();
		String target = dto.getTarget();
		Double amount = dto.getAmount();

		if (StringUtils.isBlank(product)) {

			return "产品标识不能为空！";
		}

		if (StringUtils.isBlank(source)) {

			return "来源账号不能为空！";
		}

		if (StringUtils.isBlank(target)) {

			return "目标账号不能为空！";
		}

		if (null == amount) {

			return "转账金额不能为空！";
		}

		if (!NumericUtil.isNumeric(String.valueOf(amount))) {

			return "转账金额必须为整数！";
		}

		if (source.equalsIgnoreCase(target)) {

			return "来源账户和目标账户相同，相同账户不能进行转账操作！";
		}

		if (!(Constant.SELF.equalsIgnoreCase(source) || Constant.SELF.equalsIgnoreCase(target))) {

			return "游戏账户不能进行转账操作！";
		}
		
		String str = (source + target).toUpperCase();

		HashMap<String, String> map = StaticData.transferMap.get(str);

		if (null == map) {

			return "没有找到来源账户为[" + source + "]和目标账户为[" + target + "]的配置信息，请客服联系技术处理！";
		}

		String beanId = map.get("beanId");
		String method = map.get("method");
		String platform = map.get("platform");
		String type = map.get("type");

		IPlatformTransferService service = ApplicationContextProvider.getApplicationContext().getBean(beanId, IPlatformTransferService.class);

		Method m = null;
		Object obj = null;
		String message = null;

		try {

			Class cls = service.getClass();
			m = cls.getMethod(method, new Class[] { AccountTransferDTO.class });
			obj = m.invoke(service, new Object[] { dto });
		} catch (Exception e) {

			e.printStackTrace();
			log.error("AccountTransferServiceImpl类的submit方法利用反射执行对应的类方法时发生异常，异常信息：" + e.getCause().getMessage());
			message = e.getCause().getMessage();
		}

		if (StringUtils.isNotBlank(message)) {

			return "系统繁忙，请稍后重试！";
		}

		Map<String, String> resultMap = null;

		try {

			resultMap = mapper.readValue(String.valueOf(obj), Map.class);
		} catch (Exception e) {

			e.printStackTrace();
			log.error("将方法返回的数据转换成对象时发生异常，异常信息：" + e.getMessage());
		}

		if (null == resultMap) {

			return "系统繁忙，请稍后重试！";
		}

		if (null == resultMap.get("sign")) {

			return resultMap.get("message");
		}

		String referenceId = resultMap.get("referenceId");
		
		// 转入金额至游戏账户
		if ("IN".equalsIgnoreCase(type)) {
			String password = resultMap.get("password");
			message = BalanceUtil.transferInAccount(product, platform, loginName,password, amount, referenceId);
		}

		if (StringUtils.isNotBlank(message)) {

			log.error("玩家" + loginName + "转入" + platform + "平台出现错误，转账金额为" + amount + "元，订单号：" + referenceId + "，错误信息：" + message);

			// 自动补单暂时先关闭，采用手动补单
			/*Date currentDate = DateUtil.getCurrentDate();

			User user = userService.get(loginName);
			Double credit = user.getCredit();

			// 新增额度记录
			CreditLog creditLog = new CreditLog();
			creditLog.setLoginName(loginName);
			creditLog.setType(StaticData.creditMap.get(str));
			creditLog.setCredit(credit);
			creditLog.setRemit(amount);
			creditLog.setNewCredit(NumericUtil.add(credit, amount));
			creditLog.setCreateTime(currentDate);
			creditLog.setRemark(platform + "转账错误，系统自动退还，订单号：" + referenceId);

			creditLogService.create(creditLog);

			String remark = platform + "转账错误，改变前订单：" + referenceId + "，改变前：" + (credit + amount) + "，改变后：" + credit
					+ "，改变额度：" + amount + "，自动退还订单：" + referenceId + "，改变前：" + credit + "，改变后：" + (credit + amount)
					+ "，改变额度：" + amount + "，系统已自动退还，请检查是否有误。";

			// 新增会员事件记录
			ActionLog actionLog = new ActionLog();
			actionLog.setLoginName(loginName);
			actionLog.setAction(ActionLogType.CREDIT_RECORD.getCode());
			actionLog.setCreateTime(currentDate);
			actionLog.setRemark(remark);

			actionLogService.create(actionLog);

			// 更新玩家记录
			User updateUser = new User();
			updateUser.setLoginName(loginName);
			updateUser.setCredit(amount);
			updateUser.setCreditDay(-1 * amount);

			userService.update(updateUser);*/

			return message;
		}

		return resultMap.get("message");
	}

	// 个人中心->自助优惠->优惠券专区->红包优惠券->提交
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String redEnvelopeCoupon(AccountTransferDTO dto) throws Exception {

		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		String couponCode = dto.getCouponCode();
		String platform = dto.getPlatform();
		userService = ApplicationContextProvider.getApplicationContext().getBean("userServiceImpl", IUserService.class);

		User user = userService.get(loginName);
		if (null == user.getBirthday() || StringUtils.isBlank(user.getEmail())||StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getAccountName())) {

			return "请先完善您的基本资料再来申请优惠哦^_^";
		}
		
		if (StringUtils.isBlank(product)) {

			return "产品标识不能为空！";
		}

		if (StringUtils.isBlank(couponCode)) {

			return "优惠代码不能为空！";
		}

		if (StringUtils.isBlank(platform)) {

			return "转账游戏平台不能为空！";
		}

		String str = platform.toUpperCase();

		HashMap<String, String> map = StaticData.preferentialMap.get(str);

		if (null == map) {

			return "没有找到游戏平台为[" + str + "]的配置信息，请客服联系技术处理！";
		}

		String beanId = map.get("beanId");
		String method = map.get("redEnvelopeMethod");

		IPlatformTransferService service = ApplicationContextProvider.getApplicationContext().getBean(beanId, IPlatformTransferService.class);

		Method m = null;
		Object obj = null;
		String message = null;

		try {

			Class cls = service.getClass();
			m = cls.getMethod(method, new Class[] { AccountTransferDTO.class });
			obj = m.invoke(service, new Object[] { dto });
		} catch (Exception e) {

			e.printStackTrace();
			log.error("AccountTransferServiceImpl类的redEnvelopeCoupon方法利用反射执行对应的类方法时发生异常，异常信息：" + e.getCause().getMessage());
			message = e.getCause().getMessage();
		}

		if (StringUtils.isNotBlank(message)) {

			return "系统繁忙，请稍后重试！";
		}

		Map<String, String> resultMap = null;

		try {

			resultMap = mapper.readValue(String.valueOf(obj), Map.class);
		} catch (Exception e) {

			e.printStackTrace();
			log.error("将方法返回的数据转换成对象时发生异常，异常信息：" + e.getMessage());
		}

		if (null == resultMap) {

			return "系统繁忙，请稍后重试！";
		}

		if (null == resultMap.get("sign")) {

			return resultMap.get("message");
		}

		String referenceId = resultMap.get("referenceId");
		String password = resultMap.get("password");
		Double giftMoney = Double.valueOf(String.valueOf(resultMap.get("giftMoney")));
		// 转入金额至游戏账户
		message = BalanceUtil.transferInAccount(product, platform, loginName,password, giftMoney, referenceId);

		if (StringUtils.isNotBlank(message)) {

			return message;
		}

		return resultMap.get("message");
	}

	// 个人中心->自助优惠->优惠券专区->存送优惠券->提交
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String depositCoupon(AccountTransferDTO dto) throws Exception {

		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		String couponCode = dto.getCouponCode();
		String platform = dto.getPlatform();
		Double amount = dto.getAmount();
		
		userService = ApplicationContextProvider.getApplicationContext().getBean("userServiceImpl", IUserService.class);

		User user = userService.get(loginName);
		if (null == user.getBirthday() || StringUtils.isBlank(user.getEmail())||StringUtils.isBlank(user.getPhone()) || StringUtils.isBlank(user.getAccountName())) {

			return "请先完善您的基本资料再来申请优惠哦^_^";
		}

		if (StringUtils.isBlank(product)) {

			return "产品标识不能为空！";
		}

		if (StringUtils.isBlank(couponCode)) {

			return "优惠代码不能为空！";
		}

		if (StringUtils.isBlank(platform)) {

			return "转账游戏平台不能为空！";
		}

		if (null == amount) {

			return "转账金额不能为空！";
		}

		String str = platform.toUpperCase();

		HashMap<String, String> map = StaticData.preferentialMap.get(str);

		if (null == map) {

			return "没有找到游戏平台为[" + str + "]的配置信息，请客服联系技术处理！";
		}

		String beanId = map.get("beanId");
		String method = map.get("depositMethod");

		IPlatformTransferService service = ApplicationContextProvider.getApplicationContext().getBean(beanId, IPlatformTransferService.class);

		Method m = null;
		Object obj = null;
		String message = null;

		try {

			Class cls = service.getClass();
			m = cls.getMethod(method, new Class[] { AccountTransferDTO.class });
			obj = m.invoke(service, new Object[] { dto });
		} catch (Exception e) {

			e.printStackTrace();
			log.error("AccountTransferServiceImpl类的depositCoupon方法利用反射执行对应的类方法时发生异常，异常信息：" + e.getCause().getMessage());
			message = e.getCause().getMessage();
		}

		if (StringUtils.isNotBlank(message)) {

			return "系统繁忙，请稍后重试！";
		}

		Map<String, String> resultMap = null;

		try {

			resultMap = mapper.readValue(String.valueOf(obj), Map.class);
		} catch (Exception e) {

			e.printStackTrace();
			log.error("将方法返回的数据转换成对象时发生异常，异常信息：" + e.getMessage());
		}

		if (null == resultMap) {

			return "系统繁忙，请稍后重试！";
		}

		if (null == resultMap.get("sign")) {

			return resultMap.get("message");
		}

		String referenceId = resultMap.get("referenceId");
		Double giftMoney = Double.valueOf(String.valueOf(resultMap.get("giftMoney")));
		String password = resultMap.get("password");
		// 转入金额至游戏账户
		message = BalanceUtil.transferInAccount(product, platform, loginName,password, giftMoney, referenceId);

		if (StringUtils.isNotBlank(message)) {

			return message;
		}

		return resultMap.get("message");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String experienceGold(SelfExperienceDTO dto) throws Exception {

		dto.setTitle("自助体验金");
		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		String platform = dto.getPlatform();

		if (StringUtils.isBlank(product)) {

			return "产品标识不能为空！";
		}

		if (StringUtils.isBlank(platform)) {

			return "转账游戏平台不能为空！";
		}

		String str = platform.toUpperCase();

		HashMap<String, String> map = StaticData.preferentialMap.get(str);

		if (null == map) {

			return str + "平台体验金暂未开启!";
		}

		String beanId = map.get("beanId");
		String method = map.get("experienceMethod");

		IPlatformTransferService service = ApplicationContextProvider.getApplicationContext().getBean(beanId,
				IPlatformTransferService.class);

		Method m = null;
		Object obj = null;
		String message = null;

		try {

			Class cls = service.getClass();
			m = cls.getMethod(method, new Class[] { SelfExperienceDTO.class });
			obj = m.invoke(service, new Object[] { dto });
		} catch (Exception e) {

			e.printStackTrace();
			log.error(
					"AccountTransferServiceImpl类的experienceGold方法利用反射执行对应的类方法时发生异常，异常信息：" + e.getCause().getMessage());
			message = e.getCause().getMessage();
		}

		if (StringUtils.isNotBlank(message)) {

			return message;
		}

		Map<String, String> resultMap = null;

		try {

			resultMap = mapper.readValue(String.valueOf(obj), Map.class);
		} catch (Exception e1) {

			e1.printStackTrace();
			log.error("将方法返回的数据转换成对象时发生异常，异常信息：" + e1.getMessage());
		}

		if (null == resultMap) {

			return "系统繁忙，请稍后重试！";
		}

		if (null == resultMap.get("sign")) {

			return resultMap.get("message");
		}

		Double amount = Double.valueOf(String.valueOf(resultMap.get("amount")));
		String referenceId = resultMap.get("referenceId");
		User user = userService.get(loginName);
		// 转入金额至游戏账户
		message = BalanceUtil.transferInAccount(product, platform, loginName,user.getPassword(), amount, referenceId);

		if (StringUtils.isNotBlank(message)) {

			return message;
		}

		return resultMap.get("message");
	}

	// 个人中心->个人钱包->户内转账->一键转账
	@SuppressWarnings("unchecked")
	public String oneKeyGameMoney(AccountTransferDTO dto) throws Exception {
		StringBuilder msg = new StringBuilder();
		String product = dto.getProduct();
		Double gameMoney = 0.0;
		String loginName = dto.getLoginName();
		if (StringUtils.isBlank(product)) {

			return "产品标识不能为空！";
		}
		// 获取7天的转账记录
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(java.util.Calendar.DATE, -7);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("loginName", loginName);
		paramsMap.put("createTime", cal.getTime());
		List<Transfer> listTransfer = transferService.findList(paramsMap);
		Map<String, String> transferMap = new HashMap<String, String>();
		// 去重
		for (Transfer transfer : listTransfer) {
			transferMap.put(transfer.getTarget(), transfer.getSource());
		}
		HashMap<String, String> map = new HashMap<String, String>();
		for (String transferName : transferMap.keySet()) {
			// DT转主账户 DTSELF
			if (Constant.DT.equalsIgnoreCase(transferName)) {
				String source = Constant.DT;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
				// 获取金额
				String remit = DTUtil.getAmount(product, loginName);
				if (null != checkGameMoney(remit)) {
					continue;
				} else {
					gameMoney = Double.parseDouble(remit);
				}
			}
			// NEWPT转主账户NEWPTSELF
			else if (Constant.NEWPT.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = PTUtil.getPlayerMoney(product, loginName);
				String source = Constant.NEWPT;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				// 对外显示的还是PT
				dto.setSource(Constant.PT);
				dto.setTarget(target);
			}
			// QT转主账户 QTSELF
			else if (Constant.QT.equalsIgnoreCase(transferName)) {
				String source = Constant.QT;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
				// 获取金额
				String remit = QTUtil.getBalance(product, loginName);
				if (null != checkGameMoney(remit)) {
					continue;
				} else {
					gameMoney = Double.parseDouble(remit);
				}
			}
			// TTG转主账户 TTGSELF
			else if (Constant.TTG.equalsIgnoreCase(transferName)) {
				String source = Constant.TTG;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
				// 获取金额
				String remit = TTGUtil.getPlayerAccount(product, loginName);
				if (null != checkGameMoney(remit)) {
					continue;
				} else {
					gameMoney = Double.parseDouble(remit);
				}
			}
			// SBA转主账户 SBASELF
			else if (Constant.SBA.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = ShaBaUtil.getUserBalance(product, loginName);
				String source = Constant.SBA;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// PNG转主账户 TTGSELF
			else if (Constant.PNG.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = PNGUtil.getBalance(product, loginName);
				String source = Constant.PNG;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// N2LIVE转主账户 N2LIVESELF
			else if (Constant.N2LIVE.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = NTwoUtil.getPlayerMoney(product, loginName);
				String source = Constant.N2LIVE;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// NT转主账户 NTSELF
			else if (Constant.NT.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = NTUtil.getMoney(product, loginName);
				String source = Constant.NT;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// MG转主账户 MGSELF
			else if (Constant.MG.equalsIgnoreCase(transferName)) {
				// 获取金额
				User user = userService.get(loginName);
				gameMoney = MGUtil.getBalance(product, loginName,user.getPassword());
				String source = Constant.MG;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// EBETAPP转主账户 EBETAPPSELF
			else if (Constant.EBETAPP.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = EBETUtil.getBalance(product, loginName);
				String source = Constant.EBETAPP;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// AGIN转主账户 AGINSELF
			else if (Constant.AGIN.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = AGINUtil.getBalance(product, loginName);
				String source = Constant.AGIN;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// EA转主账户 EASELF
			else if (Constant.EA.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = EAUtil.getPlayerMoney(product, loginName);
				String source = Constant.EA;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// MWG转主账户 MWGSELF
			else if (Constant.MWG.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = MWGUtils.getUserBalance(product, loginName);
				String source = Constant.MWG;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// OG转主账户 OGSELF
			else if (Constant.OG.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = OGUtils.getBalance(product, loginName);
				String source = Constant.OG;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// SLOT 老虎机账户转主账户 SLOTSELF
			else if (Constant.SLOT.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = SlotUtil.getBalance(product, loginName);
				String source = Constant.SLOT;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// SW 老虎机账户转主账户 SWSELF
			else if (Constant.SW.equalsIgnoreCase(transferName)) {
				// 获取金额
				gameMoney = SWUtil.getPlayerMoney(product, loginName);
				String source = Constant.SW;
				String target = Constant.SELF;
				String str = (source + target).toUpperCase();
				map = StaticData.transferMap.get(str);
				dto.setSource(source);
				dto.setTarget(target);
			}
			// 默认为转入到主账户 直接跳过
			else {
				continue;
			}
			String result = checkGameMoney(String.valueOf(gameMoney));
			if (null == result) {
				// 设置转账金额
				dto.setAmount(new Double(gameMoney.intValue()));
				String beanId = map.get("beanId");
				IPlatformTransferService service = ApplicationContextProvider.getApplicationContext().getBean(beanId,
						IPlatformTransferService.class);
				String message = service.transferOut(dto);
				Map<String, String> resultMap = null;
				try {
					resultMap = mapper.readValue(message, Map.class);
				} catch (Exception e1) {
					e1.printStackTrace();
					log.error("将方法返回的数据转换成对象时发生异常，异常信息：" + e1.getMessage() + "\n");
				}
				if (null == resultMap) {

					msg.append(dto.getSource() + "转账到主账户失败。");
				}
				if (null == resultMap.get("sign")) {

					msg.append(dto.getSource() + "转账到主账户失败——" + resultMap.get("message") + "\n");
				}
			} else {
				continue;
			}

		}
		return msg.toString();
	}

	public static String checkGameMoney(String gameMoney) {
		if (StringUtils.isBlank(gameMoney)) {
			return "转账金额不能为空！";
		}
		//{1,2} 改为 + OG有三位小数
		if (!gameMoney.matches("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d)+)?$")) {
			return "转账金额必须为数字！";
		}
		Double remit = Double.parseDouble(gameMoney);
		if (remit < 1.0) {
			return "转账金额不能小于等于1元！";
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.nnti.personal.service.interfaces.IAccountTransferService#activities(com.nnti.personal.model.dto.AccountTransferDTO)
	 */
	@Override
	public String activities(AccountTransferDTO dto) throws Exception {
		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		String couponCode = dto.getCouponCode();
		String platform = dto.getPlatform();
		if (StringUtils.isBlank(product)) {

			return "产品标识不能为空！";
		}

		if (StringUtils.isBlank(couponCode)) {

			return "优惠代码不能为空！";
		}

		if (StringUtils.isBlank(platform)) {

			return "转账游戏平台不能为空！";
		}
		
		String str = platform.toUpperCase();

		HashMap<String, String> map = StaticData.preferentialMap.get(str);

		if (null == map) {

			return "没有找到游戏平台为[" + str + "]的配置信息，请客服联系技术处理！";
		}

		String beanId = map.get("beanId");
		String method = map.get("christmasActivitiesMethd");

		IActivitiesService service = ApplicationContextProvider.getApplicationContext().getBean(beanId, IActivitiesService.class);

		Method m = null;
		Object obj = null;
		String message = null;

		try {

			Class cls = service.getClass();
			m = cls.getMethod(method, new Class[] { AccountTransferDTO.class });
			obj = m.invoke(service, new Object[] { dto });
		} catch (Exception e) {

			e.printStackTrace();
			log.error("AccountTransferServiceImpl类的activities方法利用反射执行对应的类方法时发生异常，异常信息：" + e.getCause().getMessage());
			message = e.getCause().getMessage();
		}

		if (StringUtils.isNotBlank(message)) {

			return "系统繁忙，请稍后重试！";
		}

		Map<String, String> resultMap = null;

		try {

			resultMap = mapper.readValue(String.valueOf(obj), Map.class);
		} catch (Exception e) {

			e.printStackTrace();
			log.error("将方法返回的数据转换成对象时发生异常，异常信息：" + e.getMessage());
		}

		if (null == resultMap) {

			return "系统繁忙，请稍后重试！";
		}

		if (null == resultMap.get("sign")) {

			return resultMap.get("message");
		}
		String referenceId = resultMap.get("referenceId");
		Double giftMoney = Double.valueOf(String.valueOf(resultMap.get("amount")));
		User user = userService.get(loginName);
		// 转入金额至游戏账户
		message = BalanceUtil.transferInAccount(product, platform, loginName,user.getPassword(), giftMoney, referenceId);

		if (StringUtils.isNotBlank(message)) {

			return message;
		}

		return resultMap.get("message");
	}
}