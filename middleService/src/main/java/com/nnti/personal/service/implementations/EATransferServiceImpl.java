package com.nnti.personal.service.implementations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.CreditChangeType;
import com.nnti.common.constants.PlatformCode;
import com.nnti.common.constants.UserRole;
import com.nnti.common.model.vo.Const;
import com.nnti.common.model.vo.CreditLog;
import com.nnti.common.model.vo.Transfer;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.IConstService;
import com.nnti.common.service.interfaces.ICreditLogService;
import com.nnti.common.service.interfaces.ISequenceService;
import com.nnti.common.service.interfaces.ITransferService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.BalanceUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.EAUtil;
import com.nnti.common.utils.NumericUtil;
import com.nnti.personal.model.dto.AccountTransferDTO;
import com.nnti.personal.model.dto.SelfExperienceDTO;
import com.nnti.personal.service.interfaces.IPlatformTransferService;

@Service("eaTransferService")
public class EATransferServiceImpl extends BaseService implements IPlatformTransferService {

//	private static Logger log = Logger.getLogger(EATransferServiceImpl.class);

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

		Const st = constService.get("转账EA");

		if (null == st || "0".equals(st.getValue())) {

			message = "EA转账正在维护中！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		// 获取EA账户余额
		Double remoteCredit = EAUtil.getPlayerMoney(product, loginName);

		if (null == remoteCredit) {

			message = "获取EA账户余额失败，请稍后重试！";
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
		transfer.setTarget(PlatformCode.Transfer.getCode(Constant.EA));
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
		creditLog.setType(CreditChangeType.TRANSFER_IN);
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

		Const st = constService.get("转账EA");

		if (null == st || "0".equals(st.getValue())) {

			message = "EA转账正在维护中！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		// 获取EA账户余额
		Double remoteCredit = EAUtil.getPlayerMoney(product, loginName);

		if (null == remoteCredit) {

			message = "获取EA账户余额失败，请稍后重试！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		if (remoteCredit < amount) {

			message = "转出失败，余额不足！";
			resultMap.put("message", message);
			return mapper.writeValueAsString(resultMap);
		}

		String transferId = sequenceService.generateTransferId();

		message = BalanceUtil.transferOutAccount(product, Constant.EA, loginName,null, amount, transferId);

		if (StringUtils.isNotBlank(message)) {

			throw new RuntimeException(message);
		}

		Double credit = user.getCredit();

		// 新增转账记录
		Transfer transfer = new Transfer();
		transfer.setId(Long.parseLong(transferId));
		transfer.setSource(PlatformCode.Transfer.getCode(Constant.EA));
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
		creditLog.setType(CreditChangeType.TRANSFER_OUT);
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

		return null;
	}

	// 存送优惠券
	@Transactional(rollbackFor = Exception.class)
	public String depositCoupon(AccountTransferDTO dto) throws Exception {

		return null;
	}

	// 体验金
	@Transactional(rollbackFor = Exception.class)
	public String experienceGold(SelfExperienceDTO dto) throws Exception {

		return null;
	}
}