package com.nnti.personal.service.implementations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.CreditChangeType;
import com.nnti.common.constants.PlatformCode;
import com.nnti.common.constants.ProposalFlagType;
import com.nnti.common.constants.ProposalType;
import com.nnti.common.model.vo.CreditLog;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.model.vo.ProposalExtend;
import com.nnti.common.model.vo.Transfer;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.ICommonService;
import com.nnti.common.service.interfaces.ICreditLogService;
import com.nnti.common.service.interfaces.IProposalExtendService;
import com.nnti.common.service.interfaces.IProposalService;
import com.nnti.common.service.interfaces.ISequenceService;
import com.nnti.common.service.interfaces.ITransferService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.NumericUtil;
import com.nnti.personal.model.dto.ActivityDTO;
import com.nnti.personal.model.vo.RedEnvelopeActivity;
import com.nnti.personal.service.interfaces.IActivityService;
import com.nnti.personal.service.interfaces.IRedEnvelopeActivityService;

@Service
public class ActivityServiceImpl implements IActivityService {

	@Autowired
	private IUserService userService;
	@Autowired
	private IRedEnvelopeActivityService redEnvelopeActivityService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IProposalService proposalService;
	@Autowired
	private IProposalExtendService proposalExtendService;
	@Autowired
	private ISequenceService sequenceService;
	@Autowired
	private ITransferService transferService;
	@Autowired
	private ICreditLogService creditLogService;

	// 红包雨活动->领取
	public String receiveRedEnvelope(ActivityDTO dto) throws Exception {

		String product = dto.getProduct();
		String loginName = dto.getLoginName();
		String title = dto.getTitle();

		List<RedEnvelopeActivity> resultList = new ArrayList<RedEnvelopeActivity>();
		Boolean flag1 = false;
		Boolean flag2 = false;
		Boolean flag = false;
		RedEnvelopeActivity redEnvelopeActivity = null;

		User user = userService.get(loginName);

		if (null == user) {

			return "您输入的账号不存在，请核对；若无账号，请您先注册！";
		}

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("title", title);
		paramsMap.put("vip", String.valueOf(user.getLevel()));
		paramsMap.put("conditionTime", DateUtil.getCurrentDate());

		List<RedEnvelopeActivity> list = redEnvelopeActivityService.findList(paramsMap);

		if (null == list || list.isEmpty()) {

			return "活动尚未开启，请联系在线客服处理！";
		}

		for (RedEnvelopeActivity temp : list) {

			String[] vipArr = temp.getVip().split(",");
			List<String> vipList = Arrays.asList(vipArr);

			if (vipList.contains(String.valueOf(user.getLevel()))) {

				flag = true;
				break;
			}
		}

		if (!flag) {

			return "当前等级未找到相应的活动，请联系在线客服处理！";
		}

		for (RedEnvelopeActivity temp : list) {

			String vip = temp.getVip();
			Double depositAmount = temp.getDepositAmount();
			Double betAmount = temp.getBetAmount();

			if (null != depositAmount) {

				flag1 = true;
			}

			if (null != betAmount) {

				flag2 = true;
			}

			for (String str : vip.split(",")) {

				if (str.equals(String.valueOf(user.getLevel()))) {

					resultList.add(temp);
					break;
				}
			}
		}

		if (resultList.size() == 0) {

			return "当前等级未找到相应的活动，请联系在线客服处理！";
		}

		paramsMap.put("loginName", loginName);

		if (flag1) {

			Collections.sort(resultList, new Comparator<RedEnvelopeActivity>() {

				public int compare(RedEnvelopeActivity arg0, RedEnvelopeActivity arg1) {

					return arg1.getDepositAmount().compareTo(arg0.getDepositAmount());
				}
			});

			for (RedEnvelopeActivity ra : resultList) {

				paramsMap.put("startTime", ra.getDepositStartTime());
				paramsMap.put("endTime", ra.getDepositEndTime());

				Double amount = commonService.getDeposit(paramsMap);

				if (amount >= ra.getDepositAmount()) {

					redEnvelopeActivity = ra;
					break;
				}
			}

			if (null == redEnvelopeActivity) {

				return "您的存款尚未达到要求，暂时无法领取！";
			}
		}

		if (flag2) {

			Collections.sort(resultList, new Comparator<RedEnvelopeActivity>() {

				public int compare(RedEnvelopeActivity arg0, RedEnvelopeActivity arg1) {

					return arg1.getBetAmount().compareTo(arg0.getBetAmount());
				}
			});

			for (RedEnvelopeActivity ra : resultList) {

				paramsMap.put("startTime", ra.getBetStartTime());
				paramsMap.put("endTime", ra.getBetEndTime());

				Double amount = commonService.sumProfitBetTotal(paramsMap);

				if (amount >= redEnvelopeActivity.getDepositAmount()) {

					redEnvelopeActivity = ra;
					break;
				}
			}

			if (null == redEnvelopeActivity) {

				return "您的投注尚未达到要求，暂时无法领取！";
			}
		}

		paramsMap.put("type", ProposalType.SELF_101.getCode());
		paramsMap.put("startTime", DateUtil.getTodayByZeroHour());
		paramsMap.put("endTime", DateUtil.getTodayByEndHour());

		List<Proposal> proposalList = proposalService.findList(paramsMap);

		if (null != proposalList && !proposalList.isEmpty()) {

			if (proposalList.size() >= redEnvelopeActivity.getTimes()) {

				return "今天次数已经用光了，明天再来吧！";
			}
		}

		if (redEnvelopeActivity.getMaxReceiveTimes() != null && redEnvelopeActivity.getMaxReceiveTimes() > 0) {

			paramsMap.put("platform", "redenvelope");
			paramsMap.put("preferentialId", Long.parseLong(String.valueOf(redEnvelopeActivity.getId())));

			Integer count = proposalService.count(paramsMap);

			if (count >= redEnvelopeActivity.getMaxReceiveTimes()) {

				return "很抱歉，当前红包已领取完毕！";
			}
		}

		String pno = sequenceService.generateProposalNo(String.valueOf(ProposalType.SELF_101.getCode()));
		String transferId = sequenceService.generateTransferId();

		Double bonus = this.generateBonus(redEnvelopeActivity.getMinBonus(), redEnvelopeActivity.getMaxBonus());

		Date currentDate = DateUtil.getCurrentDate();
		Double credit = user.getCredit();
		Double remit = 0.0;

		if ("SELF".equalsIgnoreCase(redEnvelopeActivity.getPlatformId())) {

			remit = bonus;
		}

		String remark = "红包雨活动,转入主账户金额为" + remit + "元";

		// 新增转账记录
		Transfer transfer = new Transfer();
		transfer.setId(Long.parseLong(transferId));
		transfer.setSource(PlatformCode.ProductSign.getCode(product));
		transfer.setTarget(PlatformCode.Transfer.getCode(redEnvelopeActivity.getPlatformId()));
		transfer.setRemit(bonus);
		transfer.setLoginName(loginName);
		transfer.setCreateTime(currentDate);
		transfer.setCredit(credit);
		transfer.setNewCredit(NumericUtil.add(credit, remit));
		transfer.setFlag(0);
		transfer.setRemark("转入成功");

		transferService.create(transfer);

		// 新增额度记录
		CreditLog creditLog = new CreditLog();
		creditLog.setLoginName(loginName);
		creditLog.setType(CreditChangeType.TRANSFER_SELF_IN);
		creditLog.setCredit(credit);
		creditLog.setRemit(bonus);
		creditLog.setNewCredit(NumericUtil.add(credit, remit));
		creditLog.setCreateTime(currentDate);
		creditLog.setRemark("referenceNo:" + transferId + ";" + remark);

		creditLogService.create(creditLog);

		// 新增提案记录
		Proposal proposal = new Proposal();
		proposal.setPno(pno);
		proposal.setProposer("system");
		proposal.setCreateTime(currentDate);
		proposal.setType(ProposalType.SELF_101.getCode());
		proposal.setQuickly(user.getLevel());
		proposal.setLoginName(loginName);
		proposal.setAgent(user.getAgent());
		proposal.setFlag(ProposalFlagType.EXCUTED.getCode());
		proposal.setWhereIsFrom(Constant.FROM_FRONT);
		proposal.setRemark(remark);
		proposal.setGenerateType("");
		proposal.setAmount(bonus);
		proposal.setExecuteTime(currentDate);

		proposalService.create(proposal);

		// 新增提案扩展记录
		ProposalExtend proposalExtend = new ProposalExtend();
		proposalExtend.setPno(pno);
		proposalExtend.setPlatform("redenvelope");
		proposalExtend.setPreferentialId(Long.parseLong(String.valueOf(redEnvelopeActivity.getId())));
		proposalExtend.setSid("");
		proposalExtend.setCreateTime(currentDate);
		proposalExtend.setUpdateTime(currentDate);

		proposalExtendService.create(proposalExtend);

		// 更新玩家记录
		User updateUser = new User();

		updateUser.setLoginName(loginName);
		updateUser.setCredit(bonus);

		userService.update(updateUser);

		return "恭喜您，抢到" + bonus + "元，领取后刷新主账户余额查看！";
	}

	// 根据最小红利和最大红利，随机生成红利值
	public Double generateBonus(Double minBonus, Double maxBonus) {

		Random rand = new Random();

		Double randNumber = rand.nextDouble() * (maxBonus - minBonus) + minBonus;

		return Double.parseDouble(NumericUtil.formatDouble(randNumber));
	}
}