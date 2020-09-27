package app.service.implementations;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.model.Offer;
import dfh.model.PreferentialRecord;
import dfh.model.Proposal;
import dfh.model.SelfRecord;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.UserRole;
import dfh.utils.Arith;
import dfh.utils.Constants;
import dfh.utils.NTUtils;
import app.dao.BaseDao;
import app.model.po.PreferentialConfig;
import app.model.po.ProposalExtend;
import app.model.vo.PlatformDepositVO;
import app.service.interfaces.IPlatformDepositService;
import app.service.interfaces.ISequenceService;
import app.service.interfaces.ITransferInfoService;
import app.util.DateUtil;

public class NTDepositServiceImpl extends BaseService implements IPlatformDepositService {

	private static Logger log = Logger.getLogger(NTDepositServiceImpl.class);
	
	private BaseDao baseDao;
	
	private ISequenceService sequenceService;
	private ITransferInfoService transferInfoService;
	
	// 首存优惠
	@SuppressWarnings("unchecked")
	public PlatformDepositVO firstDeposit(PlatformDepositVO vo) {
		
		Integer id = vo.getId();
		String platform = vo.getPlatform();
		Integer youhuiType = vo.getYouhuiType();
		String loginName = vo.getLoginName();
		Double remit = vo.getRemit();
		String proposalRemark = vo.getRemark();
		
		log.info("NTDepositServiceImpl类的firstDeposit方法参数为：【id=" + id + ",platform=" + platform + ",youhuiType=" + youhuiType + ",loginName=" + loginName + ",remit=" + remit + "】");
		
		Users user = (Users) baseDao.get(Users.class, loginName);
		
		if (null == user || user.getFlag() == 1) {
			
			vo.setMessage("该玩家账号不存在或已被禁用！");
			return vo;
		}
		
		Integer level = user.getLevel();
		
		if (null == level) {
		
			vo.setMessage("未设置玩家等级，请联系客服设置后再进行申请优惠操作！");
			return vo;
		}
		
		if (user.getWarnflag().equals(4)) {
			
			vo.setMessage("抱歉，您不能自助NT首存优惠。");
			return vo;
		}
		
		if (!user.getWarnflag().equals(3)) {
			
			Boolean flag = isReceiveExperienceGold(baseDao.getHibernateTemplate(), loginName, youhuiType);
			
			if (flag) {
				
				vo.setMessage("抱歉，您已申请过自助NT首存优惠，不能重复申请此优惠！");
				return vo;
			}
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class);
		dc.add(Restrictions.eq("loginname", loginName));
		dc.add(Restrictions.eq("target", "nt"));
		dc.add(Restrictions.sqlRestriction(" remark not like '%自助优惠' "));

		List<Transfer> transferList = baseDao.findByCriteria(dc);
		
		if (null != transferList && !transferList.isEmpty()) {
			
			vo.setMessage("抱歉，您不是第一次转账到NT，不能自助NT首存优惠。");
			return vo;
		}
		
		//获取远程金额
		Double remoteCredit = 0.00;
		
		String money = NTUtils.getNTMoney(loginName);
		
		if (StringUtils.isBlank(money)) {
			
			vo.setMessage("获取玩家NT平台余额失败，请稍后重试！");
			return vo;
		}
		
		JSONObject jsonObject = JSONObject.fromObject(money);
		remoteCredit = jsonObject.getBoolean("result") ? jsonObject.getDouble("balance") : null;
		
		if (remoteCredit == null) {
			
			vo.setMessage("获取NT余额错误:" + jsonObject.getString("error"));
			return vo;
		}
		
		if (remoteCredit >= 5) {
			
			vo.setMessage("NT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}
		
		Map<String, Object> resultMap = preferentialLogicHandle(baseDao.getHibernateTemplate(), id, platform, youhuiType, loginName, level);
		
		if (null == resultMap.get("data")) {
			
			vo.setMessage(resultMap.get("message").toString());
			return vo;
		}
		
		PreferentialConfig config = (PreferentialConfig) resultMap.get("data");
		
		Double changeMoney = Math.abs(Arith.mul(remit, config.getPercent())) > config.getLimitMoney() ? config.getLimitMoney() : Math.abs(Arith.mul(remit, config.getPercent()));
		// 精确到小数点后2位
		changeMoney = Arith.round(changeMoney, 2);
		
		String remark = proposalRemark + "自助NT首存优惠," + config.getBetMultiples() + "倍流水,存" + remit + "送" + changeMoney;
		
		String transferId = sequenceService.generateTransferId();
		
		String msg = transferInfoService.transferNT(transferId, loginName, remit, remark);
		
		if (StringUtils.isNotBlank(msg)) {
			
			// spring的事务管理默认只对出现运行期异常(Java.lang.RuntimeException及其子类)进行回滚。
			// 如果一个方法抛出Exception或者Checked异常，spring事务管理默认不进行回滚。
			throw new RuntimeException(msg);
		}

		String pno = sequenceService.generateProposalNo(youhuiType);
		
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginName, remit, changeMoney, remark);
		
		Proposal proposal = new Proposal(pno, "system", DateUtil.getCurrentTime(), youhuiType, user.getLevel(), loginName, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, proposalRemark, null);
		proposal.setBetMultiples(String.valueOf(config.getBetMultiples()));
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());
		
		baseDao.save(offer);
		baseDao.save(proposal);
		
		ProposalExtend proposalExtend = new ProposalExtend();
		proposalExtend.setPno(pno);
		proposalExtend.setPlatform(platform);
		proposalExtend.setPreferentialId(id);
		proposalExtend.setCreateTime(new Date());
		proposalExtend.setUpdateTime(new Date());
		
		baseDao.save(proposalExtend);
		
		// 记录下到目前为止的投注额度 begin
		
		Double betAmount = queryBetAmountFromPTPROFIT(baseDao.getHibernateTemplate(), loginName);
		
		PreferentialRecord record = new PreferentialRecord(pno, loginName, "nt", betAmount, new Date(), 0);
		baseDao.save(record);
		
		// 记录下到目前为止的投注额度 end
		
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginName);
		selfRecord.setPlatform("nt");
		selfRecord.setSelfname("自助NT首存优惠");
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		
		baseDao.save(selfRecord);
		
		Userstatus userstatus = (Userstatus) baseDao.get(Userstatus.class, loginName);
		
		if (null == userstatus) {
			
			Userstatus status = new Userstatus();
			status.setLoginname(loginName);
			status.setCashinwrong(0);

			baseDao.save(status);
		}
		
		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助NT首存优惠成功！");
		
		return vo;
	}

	// 次存优惠
	public PlatformDepositVO timesDeposit(PlatformDepositVO vo) {
		
		Integer id = vo.getId();
		String platform = vo.getPlatform();
		Integer youhuiType = vo.getYouhuiType();
		String loginName = vo.getLoginName();
		Double remit = vo.getRemit();
		String proposalRemark = vo.getRemark();
		
		log.info("NTDepositServiceImpl类的timesDeposit方法参数为：【id=" + id + ",platform=" + platform + ",youhuiType=" + youhuiType + ",loginName=" + loginName + ",remit=" + remit + "】");
		
		Users user = (Users) baseDao.get(Users.class, loginName);
		
		if (null == user || user.getFlag() == 1) {
			
			vo.setMessage("该玩家账号不存在或已被禁用！");
			return vo;
		}
		
		Integer level = user.getLevel();
		
		if (null == level) {
		
			vo.setMessage("未设置玩家等级，请联系客服设置后再进行申请优惠操作！");
			return vo;
		}
		
		if (user.getWarnflag().equals(4)) {
			
			vo.setMessage("抱歉，您不能自助NT次存优惠。");
			return vo;
		}
		
		//获取远程金额
		Double remoteCredit = 0.00;
		
		String money = NTUtils.getNTMoney(loginName);
		
		if (StringUtils.isBlank(money)) {
			
			vo.setMessage("获取玩家NT平台余额失败，请稍后重试！");
			return vo;
		}
		
		JSONObject jsonObject = JSONObject.fromObject(money);
		remoteCredit = jsonObject.getBoolean("result") ? jsonObject.getDouble("balance") : null;
		
		if (remoteCredit == null) {
			
			vo.setMessage("获取NT余额错误:" + jsonObject.getString("error"));
			return vo;
		}
		
		if (remoteCredit >= 5) {
			
			vo.setMessage("NT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}
		
		Map<String, Object> resultMap = preferentialLogicHandle(baseDao.getHibernateTemplate(), id, platform, youhuiType, loginName, level);
		
		if (null == resultMap.get("data")) {
			
			vo.setMessage(resultMap.get("message").toString());
			return vo;
		}
		
		PreferentialConfig config = (PreferentialConfig) resultMap.get("data");
		
		Double changeMoney = Math.abs(Arith.mul(remit, config.getPercent())) > config.getLimitMoney() ? config.getLimitMoney() : Math.abs(Arith.mul(remit, config.getPercent()));
		// 精确到小数点后2位
		changeMoney = Arith.round(changeMoney, 2);
		
		String remark = proposalRemark + "自助NT次存优惠," + config.getBetMultiples() + "倍流水,存" + remit + "送" + changeMoney;
		
		String transferId = sequenceService.generateTransferId();
		
		String msg = transferInfoService.transferNT(transferId, loginName, remit, remark);
		
		if (StringUtils.isNotBlank(msg)) {
			
			// spring的事务管理默认只对出现运行期异常(Java.lang.RuntimeException及其子类)进行回滚。
			// 如果一个方法抛出Exception或者Checked异常，spring事务管理默认不进行回滚。
			throw new RuntimeException(msg);
		}

		String pno = sequenceService.generateProposalNo(youhuiType);
		
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginName, remit, changeMoney, remark);
		
		Proposal proposal = new Proposal(pno, "system", DateUtil.getCurrentTime(), youhuiType, user.getLevel(), loginName, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, proposalRemark, null);
		proposal.setBetMultiples(String.valueOf(config.getBetMultiples()));
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());
		
		baseDao.save(offer);
		baseDao.save(proposal);
		
		ProposalExtend proposalExtend = new ProposalExtend();
		proposalExtend.setPno(pno);
		proposalExtend.setPlatform(platform);
		proposalExtend.setPreferentialId(id);
		proposalExtend.setCreateTime(new Date());
		proposalExtend.setUpdateTime(new Date());
		
		baseDao.save(proposalExtend);
		
		// 记录下到目前为止的投注额度 begin
		
		Double betAmount = queryBetAmountFromPTPROFIT(baseDao.getHibernateTemplate(), loginName);
		
		PreferentialRecord record = new PreferentialRecord(pno, loginName, "nt", betAmount, new Date(), 0);
		baseDao.save(record);
		
		// 记录下到目前为止的投注额度 end
		
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginName);
		selfRecord.setPlatform("nt");
		selfRecord.setSelfname("自助NT次存优惠");
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		
		baseDao.save(selfRecord);
		
		Userstatus userstatus = (Userstatus) baseDao.get(Userstatus.class, loginName);
		
		if (null == userstatus) {
			
			Userstatus status = new Userstatus();
			status.setLoginname(loginName);
			status.setCashinwrong(0);

			baseDao.save(status);
		}
		
		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助NT次存优惠成功！");
		
		return vo;
	}
	
	// 限时优惠
	public PlatformDepositVO limitedTime(PlatformDepositVO vo) {
		
		Integer id = vo.getId();
		String platform = vo.getPlatform();
		Integer youhuiType = vo.getYouhuiType();
		String loginName = vo.getLoginName();
		Double remit = vo.getRemit();
		String proposalRemark = vo.getRemark();
		
		log.info("NTDepositServiceImpl类的limitedTime方法参数为：【id=" + id + ",platform=" + platform + ",youhuiType=" + youhuiType + ",loginName=" + loginName + ",remit=" + remit + "】");
		
		Users user = (Users) baseDao.get(Users.class, loginName);
		
		if (null == user || user.getFlag() == 1) {
			
			vo.setMessage("该玩家账号不存在或已被禁用！");
			return vo;
		}
		
		Integer level = user.getLevel();
		
		if (null == level) {
		
			vo.setMessage("未设置玩家等级，请联系客服设置后再进行申请优惠操作！");
			return vo;
		}
		
		if (user.getWarnflag().equals(4)) {
			
			vo.setMessage("抱歉，您不能自助NT限时优惠。");
			return vo;
		}
		
		//获取远程金额
		Double remoteCredit = 0.00;
		
		String money = NTUtils.getNTMoney(loginName);
		
		if (StringUtils.isBlank(money)) {
			
			vo.setMessage("获取玩家NT平台余额失败，请稍后重试！");
			return vo;
		}
		
		JSONObject jsonObject = JSONObject.fromObject(money);
		remoteCredit = jsonObject.getBoolean("result") ? jsonObject.getDouble("balance") : null;
		
		if (remoteCredit == null) {
			
			vo.setMessage("获取NT余额错误:" + jsonObject.getString("error"));
			return vo;
		}
		
		if (remoteCredit >= 5) {
			
			vo.setMessage("NT平台金额必须小于5元，才能自助优惠！");
			return vo;
		}
		
		Map<String, Object> resultMap = preferentialLogicHandle(baseDao.getHibernateTemplate(), id, platform, youhuiType, loginName, level);
		
		if (null == resultMap.get("data")) {
			
			vo.setMessage(resultMap.get("message").toString());
			return vo;
		}
		
		PreferentialConfig config = (PreferentialConfig) resultMap.get("data");
		
		Double changeMoney = Math.abs(Arith.mul(remit, config.getPercent())) > config.getLimitMoney() ? config.getLimitMoney() : Math.abs(Arith.mul(remit, config.getPercent()));
		// 精确到小数点后2位
		changeMoney = Arith.round(changeMoney, 2);
		
		String remark = proposalRemark + "自助NT限时优惠," + config.getBetMultiples() + "倍流水,存" + remit + "送" + changeMoney;
		
		String transferId = sequenceService.generateTransferId();
		
		String msg = transferInfoService.transferNT(transferId, loginName, remit, remark);
		
		if (StringUtils.isNotBlank(msg)) {
			
			// spring的事务管理默认只对出现运行期异常(Java.lang.RuntimeException及其子类)进行回滚。
			// 如果一个方法抛出Exception或者Checked异常，spring事务管理默认不进行回滚。
			throw new RuntimeException(msg);
		}

		String pno = sequenceService.generateProposalNo(youhuiType);
		
		Offer offer = new Offer(pno, UserRole.MONEY_CUSTOMER.getCode(), loginName, remit, changeMoney, remark);
		
		Proposal proposal = new Proposal(pno, "system", DateUtil.getCurrentTime(), youhuiType, user.getLevel(), loginName, remit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, proposalRemark, null);
		proposal.setBetMultiples(String.valueOf(config.getBetMultiples()));
		proposal.setGifTamount(changeMoney);
		proposal.setExecuteTime(new Date());
		
		baseDao.save(offer);
		baseDao.save(proposal);
		
		ProposalExtend proposalExtend = new ProposalExtend();
		proposalExtend.setPno(pno);
		proposalExtend.setPlatform(platform);
		proposalExtend.setPreferentialId(id);
		proposalExtend.setCreateTime(new Date());
		proposalExtend.setUpdateTime(new Date());
		
		baseDao.save(proposalExtend);
		
		// 记录下到目前为止的投注额度 begin
		
		Double betAmount = queryBetAmountFromPTPROFIT(baseDao.getHibernateTemplate(), loginName);
		
		PreferentialRecord record = new PreferentialRecord(pno, loginName, "nt", betAmount, new Date(), 0);
		baseDao.save(record);
		
		// 记录下到目前为止的投注额度 end
		
		SelfRecord selfRecord = new SelfRecord();
		selfRecord.setPno(pno);
		selfRecord.setLoginname(loginName);
		selfRecord.setPlatform("nt");
		selfRecord.setSelfname("自助NT限时优惠");
		selfRecord.setType(0);
		selfRecord.setCreatetime(new Date());
		
		baseDao.save(selfRecord);
		
		Userstatus userstatus = (Userstatus) baseDao.get(Userstatus.class, loginName);
		
		if (null == userstatus) {
			
			Userstatus status = new Userstatus();
			status.setLoginname(loginName);
			status.setCashinwrong(0);

			baseDao.save(status);
		}
		
		vo.setGiftMoney(Arith.add(changeMoney, remit));
		vo.setMessage("自助NT限时优惠成功！");
		
		return vo;
	}
	
	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public ISequenceService getSequenceService() {
		return sequenceService;
	}

	public void setSequenceService(ISequenceService sequenceService) {
		this.sequenceService = sequenceService;
	}

	public ITransferInfoService getTransferInfoService() {
		return transferInfoService;
	}

	public void setTransferInfoService(ITransferInfoService transferInfoService) {
		this.transferInfoService = transferInfoService;
	}	
}