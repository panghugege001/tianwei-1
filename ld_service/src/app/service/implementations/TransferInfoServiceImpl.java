package app.service.implementations;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import app.dao.BaseDao;
import app.service.interfaces.ILogService;
import app.service.interfaces.ITransferInfoService;
import app.util.DateUtil;
import dfh.model.LosePromo;
import dfh.model.PTBigBang;
import dfh.model.PreferTransferRecord;
import dfh.model.Proposal;
import dfh.model.SelfRecord;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.ProposalType;
import dfh.remote.RemoteConstant;

public class TransferInfoServiceImpl extends BaseService implements ITransferInfoService {

	private Logger log = Logger.getLogger(TransferInfoServiceImpl.class);
	
	private BaseDao baseDao;
	
	private ILogService logService;
	
	public String transferLimit(String loginName, Double remit) {

		log.info("transferLimit方法的参数为：【loginName=" + loginName + ",remit=" + remit + "】");
		
		Users users = (Users) baseDao.get(Users.class, loginName);
		
		if (null == users) {
			
			return "未找到玩家“" + loginName + "”的相关账户信息！";
		}
		
		if (users.getFlag() == 1) {
			
			return "该账号已经禁用！";
		}
		
		if ("AGENT".equals(users.getRole())) {
			
			return "代理不允许进行操作！";
		}
		
		remit = Math.abs(remit);
		
		if (users.getCredit() < remit) {
		
			return "主账户金额不足，当前额度：" + users.getCredit() + "元！";
		}
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当前系统时间
		String today = df.format(new Date());
		// 获取上一次玩家转账时间
		String lastTime = users.getCreditdaydate();
		// 限制玩家当日转入金额
		Double limit = users.getCreditlimit();
		// 获取上一次总转账金额
		Double userRemit = users.getCreditday();
				
		if (StringUtils.isEmpty(lastTime) || !lastTime.equals(today)) {
			
			String sql = "UPDATE users set creditday = :creditDay, creditdaydate = :creditDayDate WHERE loginname = :loginName";
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creditDay", 0.0);
			params.put("creditDayDate", today);
			params.put("loginName", loginName);
			
			baseDao.executeUpdate(sql, params);
			
			userRemit = 0.0;
		} else {
			
			String sql = "UPDATE users set creditday = :creditDay WHERE loginname = :loginName";
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("creditDay", (userRemit + remit));
			params.put("loginName", loginName);
			
			baseDao.executeUpdate(sql, params);
		}
		
		// -1:转账没有限制/0:不能转账/1000:当天最高转账1000
		if (limit == -1) {
		
			return null;
		} else if (limit == 0) {
		
			return "不能进行转账操作，请联系客服！";
		} else if (limit < remit) {
		
			return "今天最高转入额度为" + limit + ",目前可以转入额度是：" + (limit - remit);
		} else {
			// 获取总转账金额
			Double remitAll = remit + userRemit;
			
			if (limit < remitAll) {
				
				return "今天最高转入额度为" + limit + ",目前可以转入额度是：" + (limit - remitAll);
			}
		}
		
		log.info("今天最高转入额度为：" + limit + ",上一次转入额度为：" + userRemit + ",累计本次转入额度后为：" + (remit + userRemit));
		
		return null;
	}
	
	@SuppressWarnings({ "unchecked" })
	public String transferPT(String transferId, String loginName, Double remit, Double remoteCredit, String remark) {
		
		String msg = this.transferLimit(loginName, remit);
		
		if (StringUtils.isNotBlank(msg)) {
			
			return msg;
		}
		
		remit = Math.abs(remit);
		
		Users users = (Users) baseDao.get(Users.class, loginName);


		if (StringUtils.isNotBlank(users.getShippingcodePt())) {

			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc.add(Restrictions.eq("shippingCode", users.getShippingcodePt()));
			dc.add(Restrictions.eq("flag", 2));
			dc.add(Restrictions.in("type", new Object[] {499, 571, 572, 573, 574, 575, 590, 591, 705, 409, 410, 411, 412, 415, 419, 390, 391, 801 }));
			
			List<Proposal> list = baseDao.findByCriteria(dc);
			
			if (null == list || list.isEmpty()) {
				
				return "你的优惠码出现错误！";
			}
			
			Proposal proposal = list.get(0);
			
			if (null == proposal.getExecuteTime()) {
				
				return "执行时间出现问题！";
			}
			
			if ((new Date().getTime() - proposal.getExecuteTime().getTime()) < 6 * 60 * 1000) {
				
				return "自助优惠5分钟内不允许户内转账！";
			}
			
			Integer type = proposal.getType();
			
			String couponString = ProposalType.getText(type);
			
			if (type == 571) {
				
				couponString = "PT红包优惠券";
			}
			
			// 要达到的总投注额=(转账金额+红利金额)*流水位数
			Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
			// 获取玩家上一次领取优惠后的总投注额
			Double validBetAmount = getPreferentialBet(baseDao.getHibernateTemplate(), loginName, "pttiger", proposal.getPno());
			
			validBetAmount = validBetAmount == null ? 0.0 : validBetAmount;
			validBetAmount = new Double(new DecimalFormat("#.00").format(validBetAmount));
			
			log.info(loginName + "->自助优惠解除限制参数validBetAmount：" + validBetAmount + ",amountAll:" + amountAll + ",remoteCredit:" + remoteCredit);
			
			if (validBetAmount != -1.0) {
				
				if (!(validBetAmount >= amountAll || remoteCredit < 5)) {
					
					PreferTransferRecord transferRecord = new PreferTransferRecord(loginName, "pttiger", validBetAmount, (amountAll - validBetAmount), new Date(), null, "IN");
					
					baseDao.save(transferRecord);
					
					return couponString + ":目前投注总额是" + validBetAmount + ",需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll 
						   + "或者PT游戏金额低于5元,方能进行户内转账！(还差" + (amountAll - validBetAmount) + ",或稍后5分钟 系统更新记录再来查询)";
				}
			}
		}
		
		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginName));
		c.add(Restrictions.eq("platform", "pttiger"));
		c.add(Restrictions.eq("status", "1"));
		
		List<LosePromo> losePromoList = baseDao.findByCriteria(c);
		
		if (null != losePromoList && !losePromoList.isEmpty()) {
			
			for (LosePromo losePromo : losePromoList) {
				
				losePromo.setStatus("2");
				baseDao.update(losePromo);
			}
		}
		
		// PT大爆炸
		DetachedCriteria d = DetachedCriteria.forClass(PTBigBang.class);
		d.add(Restrictions.eq("username", loginName));
		d.add(Restrictions.eq("status", "2"));
		
		List<PTBigBang> ptBigBangList = baseDao.findByCriteria(d);
		
		if (remoteCredit >= 5 && null != ptBigBangList && !ptBigBangList.isEmpty()) {
			
			return "已领取PT大爆炸活动金，且PT余额大于5元，暂不允许转入。请先将余额转出或继续游戏！";
		}
		
		if (null != ptBigBangList && !ptBigBangList.isEmpty()) {
		
			for (PTBigBang ptBigBang : ptBigBangList) {
				
				ptBigBang.setStatus("3");
				baseDao.update(ptBigBang);
			}
		}
		
		Double credit = users.getCredit();
		
		String sql = "UPDATE users set credit = credit - :credit WHERE loginname = :loginname";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("credit", remit);
		params.put("loginname", loginName);
		
		baseDao.executeUpdate(sql, params);
		
		log.info("update user " + loginName + " from credit " + credit + " to credit " + (credit - remit));
		
		this.transferRecordInPT(transferId, loginName, credit, remit);
		
		logService.addCreditLog(loginName, CreditChangeType.TRANSFER_MEWPTIN.getCode(), credit, remit, (credit + (remit * -1)), "referenceNo:" + transferId + ";" + StringUtils.trimToEmpty(remark));
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String transferMG(String transferId, String loginName, Double remit, String remark) {
	
		String msg = this.transferLimit(loginName, remit);
		
		if (StringUtils.isNotBlank(msg)) {
			
			return msg;
		}
		
		// 将之前使用的MG存送优惠，修改为已达到流水要求
		DetachedCriteria dc = DetachedCriteria.forClass(SelfRecord.class);
		dc.add(Restrictions.eq("loginname", loginName));
		dc.add(Restrictions.eq("platform", "mg"));
		dc.add(Restrictions.eq("type", 0));

		List<SelfRecord> selfList = baseDao.findByCriteria(dc);

		if (null != selfList && !selfList.isEmpty()) {

			for (SelfRecord selfRecord : selfList) {

				selfRecord.setType(1);
				selfRecord.setUpdatetime(new Date());
				baseDao.update(selfRecord);
			}
		}
		
		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginName));
		c.add(Restrictions.eq("platform", "mg"));
		c.add(Restrictions.eq("status", "1"));
		
		List<LosePromo> losePromoList = baseDao.findByCriteria(c);
		
		if (null != losePromoList && !losePromoList.isEmpty()) {
		
			for (LosePromo losePromo : losePromoList) {
				
				losePromo.setStatus("2");
				baseDao.update(losePromo);
			}
		}
		
		remit = Math.abs(remit);
		
		Users users = (Users) baseDao.get(Users.class, loginName);
		
		Double credit = users.getCredit();
		
		String sql = "UPDATE users set credit = credit - :credit WHERE loginname = :loginname";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("credit", remit);
		params.put("loginname", loginName);
		
		baseDao.executeUpdate(sql, params);
		
		log.info("update user " + loginName + " from credit " + credit + " to credit " + (credit - remit));
		
		this.transferRecordInMG(transferId, loginName, credit, remit);
		
		logService.addCreditLog(loginName, CreditChangeType.TRANSFER_MGIN.getCode(), credit, remit, (credit + (remit * -1)), "referenceNo:" + transferId + ";" + StringUtils.trimToEmpty(remark));
	
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String transferDT(String transferId, String loginName, Double remit, String remark) {
		
		String msg = this.transferLimit(loginName, remit);
		
		if (StringUtils.isNotBlank(msg)) {
			
			return msg;
		}
		
		// 将之前使用的DT存送优惠，修改为已达到流水要求
		DetachedCriteria dc = DetachedCriteria.forClass(SelfRecord.class);
		dc.add(Restrictions.eq("loginname", loginName));
		dc.add(Restrictions.eq("platform", "dt"));
		dc.add(Restrictions.eq("type", 0));
		
		List<SelfRecord> selfList = baseDao.findByCriteria(dc);

		if (null != selfList && !selfList.isEmpty()) {

			for (SelfRecord selfRecord : selfList) {

				selfRecord.setType(1);
				selfRecord.setUpdatetime(new Date());
				baseDao.update(selfRecord);
			}
		}
		
		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginName));
		c.add(Restrictions.eq("platform", "dt"));
		c.add(Restrictions.eq("status", "1"));

		List<LosePromo> losePromoList = baseDao.findByCriteria(c);

		if (null != losePromoList && !losePromoList.isEmpty()) {
			
			for (LosePromo losePromo : losePromoList) {

				losePromo.setStatus("2");
				baseDao.update(losePromo);
			}
		}
		
		remit = Math.abs(remit);
		
		Users users = (Users) baseDao.get(Users.class, loginName);
		
		Double credit = users.getCredit();
		
		String sql = "UPDATE users set credit = credit - :credit WHERE loginname = :loginname";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("credit", remit);
		params.put("loginname", loginName);
		
		baseDao.executeUpdate(sql, params);
		
		log.info("update user " + loginName + " from credit " + credit + " to credit " + (credit - remit));
		
		this.transferRecordInDT(transferId, loginName, credit, remit);
		
		logService.addCreditLog(loginName, CreditChangeType.TRANSFER_DTIN.getCode(), credit, remit, (credit + (remit * -1)), "referenceNo:" + transferId + ";" + StringUtils.trimToEmpty(remark));
	
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String transferQT(String transferId, String loginName, Double remit, String remark) {
		
		String msg = this.transferLimit(loginName, remit);
		
		if (StringUtils.isNotBlank(msg)) {
			
			return msg;
		}
		
		// 将之前使用的QT存送优惠，修改为已达到流水要求
		DetachedCriteria dc = DetachedCriteria.forClass(SelfRecord.class);
		dc.add(Restrictions.eq("loginname", loginName));
		dc.add(Restrictions.eq("platform", "qt"));
		dc.add(Restrictions.eq("type", 0));
		
		List<SelfRecord> selfList = baseDao.findByCriteria(dc);
		
		if (null != selfList && !selfList.isEmpty()) {
			
			for (SelfRecord selfRecord : selfList) {
				
				selfRecord.setType(1);
				selfRecord.setUpdatetime(new Date());
				
				baseDao.update(selfRecord);
			}
		}
		
		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginName));
		c.add(Restrictions.eq("platform", "qt"));
		c.add(Restrictions.eq("status", "1"));
		
		List<LosePromo> losePromoList = baseDao.findByCriteria(c);
		
		if (null != losePromoList && !losePromoList.isEmpty()) {
			
			for (LosePromo losePromo : losePromoList) {
			
				losePromo.setStatus("2");
				
				baseDao.update(losePromo);
			}
		}
		
		remit = Math.abs(remit);
		
		Users users = (Users) baseDao.get(Users.class, loginName);
		
		Double credit = users.getCredit();
		
		String sql = "UPDATE users set credit = credit - :credit WHERE loginname = :loginname";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("credit", remit);
		params.put("loginname", loginName);
		
		baseDao.executeUpdate(sql, params);
		
		log.info("update user " + loginName + " from credit " + credit + " to credit " + (credit - remit));
		
		this.transferRecordInQT(transferId, loginName, credit, remit);
		
		logService.addCreditLog(loginName, CreditChangeType.TRANSFER_QTIN.getCode(), credit, remit, (credit + (remit * -1)), "referenceNo:" + transferId + ";" + StringUtils.trimToEmpty(remark));
	
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String transferNT(String transferId, String loginName, Double remit, String remark) {
		
		String msg = this.transferLimit(loginName, remit);
		
		if (StringUtils.isNotBlank(msg)) {
			
			return msg;
		}
		
		// 将之前使用的NT存送优惠，修改为已达到流水要求
		DetachedCriteria dc = DetachedCriteria.forClass(SelfRecord.class);
		dc.add(Restrictions.eq("loginname", loginName));
		dc.add(Restrictions.eq("platform", "nt"));
		dc.add(Restrictions.eq("type", 0));
		
		List<SelfRecord> selfList = baseDao.findByCriteria(dc);
		
		if (null != selfList && !selfList.isEmpty()) {
			
			for (SelfRecord selfRecord : selfList) {
			
				selfRecord.setType(1);
				selfRecord.setUpdatetime(new Date());
			
				baseDao.update(selfRecord);
			}
		}
		
		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginName));
		c.add(Restrictions.eq("platform", "nt"));
		c.add(Restrictions.eq("status", "1"));

		List<LosePromo> losePromoList = baseDao.findByCriteria(c);
		
		if (null != losePromoList && !losePromoList.isEmpty()) {
		
			for (LosePromo losePromo : losePromoList) {
				
				losePromo.setStatus("2");
				
				baseDao.update(losePromo);
			}
		}
		
		remit = Math.abs(remit);
		
		Users users = (Users) baseDao.get(Users.class, loginName);
		
		Double credit = users.getCredit();
		
		String sql = "UPDATE users set credit = credit - :credit WHERE loginname = :loginname";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("credit", remit);
		params.put("loginname", loginName);
		
		baseDao.executeUpdate(sql, params);
		
		log.info("update user " + loginName + " from credit " + credit + " to credit " + (credit - remit));
		
		this.transferRecordInNT(transferId, loginName, credit, remit);
		
		logService.addCreditLog(loginName, CreditChangeType.TRANSFER_NTIN.getCode(), credit, remit, (credit + (remit * -1)), "referenceNo:" + transferId + ";" + StringUtils.trimToEmpty(remark));
	
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public String transferTTG(String transferId, String loginName, Double remit, String remark) {
	
		String msg = this.transferLimit(loginName, remit);
		
		if (StringUtils.isNotBlank(msg)) {
			
			return msg;
		}
		
		// 将之前使用的TTG存送优惠，修改为已达到流水要求
		DetachedCriteria dc = DetachedCriteria.forClass(SelfRecord.class);
		dc.add(Restrictions.eq("loginname", loginName));
		dc.add(Restrictions.eq("platform", "ttg"));
		dc.add(Restrictions.eq("type", 0));
		
		List<SelfRecord> selfList = baseDao.findByCriteria(dc);
		
		if (null != selfList && !selfList.isEmpty()) {
			
			for (SelfRecord selfRecord : selfList) {
				
				selfRecord.setType(1);
				selfRecord.setUpdatetime(new Date());
				
				baseDao.update(selfRecord);
			}
		}
		
		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginName));
		c.add(Restrictions.eq("platform", "ttg"));
		c.add(Restrictions.eq("status", "1"));
		
		List<LosePromo> losePromoList = baseDao.findByCriteria(c);
		
		if (null != losePromoList && !losePromoList.isEmpty()) {
			
			for (LosePromo losePromo : losePromoList) {
				
				losePromo.setStatus("2");
				
				baseDao.update(losePromo);
			}
		}
		
		remit = Math.abs(remit);
		
		Users users = (Users) baseDao.get(Users.class, loginName);
		
		Double credit = users.getCredit();
		
		String sql = "UPDATE users set credit = credit - :credit WHERE loginname = :loginname";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("credit", remit);
		params.put("loginname", loginName);
		
		baseDao.executeUpdate(sql, params);
		
		log.info("update user " + loginName + " from credit " + credit + " to credit " + (credit - remit));
		
		this.transferRecordInTTG(transferId, loginName, credit, remit);
		
		logService.addCreditLog(loginName, CreditChangeType.TRANSFER_TTGIN.getCode(), credit, remit, (credit + (remit * -1)), "referenceNo:" + transferId + ";" + StringUtils.trimToEmpty(remark));
	
		return null;
	}
	
	public void transferRecordInPT(String transferId, String loginName, Double localCredit, Double remit) {
		
		this.transferRecord(transferId, RemoteConstant.WEBSITE, RemoteConstant.PAGESITENEWPT, loginName, localCredit, remit, localCredit - remit);
	}
	
	public void transferRecordOutPT(String transferId, String loginName, Double localCredit, Double remit) {
	
		this.transferRecord(transferId, RemoteConstant.PAGESITENEWPT, RemoteConstant.WEBSITE, loginName, localCredit, remit, localCredit + remit);
	}
	
	public void transferRecordInMG(String transferId, String loginName, Double localCredit, Double remit) {
	
		this.transferRecord(transferId, RemoteConstant.WEBSITE, RemoteConstant.PAGESITEMG, loginName, localCredit, remit, localCredit - remit);
	}
	
	public void transferRecordOutMG(String transferId, String loginName, Double localCredit, Double remit) {
	
		this.transferRecord(transferId, RemoteConstant.PAGESITEMG, RemoteConstant.WEBSITE, loginName, localCredit, remit, localCredit + remit);
	}
	
	public void transferRecordInDT(String transferId, String loginName, Double localCredit, Double remit) {
		
		this.transferRecord(transferId, RemoteConstant.WEBSITE, RemoteConstant.PAGESITEDT, loginName, localCredit, remit, localCredit - remit);
	}
	
	public void transferRecordOutDT(String transferId, String loginName, Double localCredit, Double remit) {
		
		this.transferRecord(transferId, RemoteConstant.PAGESITEDT, RemoteConstant.WEBSITE, loginName, localCredit, remit, localCredit + remit);
	}
	
	public void transferRecordInQT(String transferId, String loginName, Double localCredit, Double remit) {
		
		this.transferRecord(transferId, RemoteConstant.WEBSITE, RemoteConstant.PAGESITEQT, loginName, localCredit, remit, localCredit - remit);
	}
	
	public void transferRecordOutQT(String transferId, String loginName, Double localCredit, Double remit) {
		
		this.transferRecord(transferId, RemoteConstant.PAGESITEQT, RemoteConstant.WEBSITE, loginName, localCredit, remit, localCredit + remit);
	}
	
	public void transferRecordInNT(String transferId, String loginName, Double localCredit, Double remit) {
		
		this.transferRecord(transferId, RemoteConstant.WEBSITE, RemoteConstant.PAGESITENT, loginName, localCredit, remit, localCredit - remit);
	}
	
	public void transferRecordOutNT(String transferId, String loginName, Double localCredit, Double remit) {
		
		this.transferRecord(transferId, RemoteConstant.PAGESITENT, RemoteConstant.WEBSITE, loginName, localCredit, remit, localCredit + remit);
	}
	
	public void transferRecordInTTG(String transferId, String loginName, Double localCredit, Double remit) {
		
		this.transferRecord(transferId, RemoteConstant.WEBSITE, RemoteConstant.PAGESITETT, loginName, localCredit, remit, localCredit - remit);
	}
	
	public void transferRecordOutTTG(String transferId, String loginName, Double localCredit, Double remit) {
		
		this.transferRecord(transferId, RemoteConstant.PAGESITETT, RemoteConstant.WEBSITE, loginName, localCredit, remit, localCredit + remit);
	}
	
	private void transferRecord(String transferId, String source, String target, String loginName, Double localCredit, Double remit, Double newCredit) {
		
		Transfer transfer = new Transfer();
		
		transfer.setId(Long.valueOf(transferId));
		transfer.setSource(source);
		transfer.setTarget(target);
		transfer.setRemit(remit);
		transfer.setLoginname(loginName);
		transfer.setCreatetime(DateUtil.getCurrentTime());
		transfer.setCredit(localCredit);
		transfer.setNewCredit(newCredit);
		transfer.setFlag(0);
		transfer.setPaymentid(null);
		transfer.setRemark("转入成功");
		
		baseDao.save(transfer);
	}
	
	public BaseDao getBaseDao() {
		return baseDao;
	}
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	public ILogService getLogService() {
		return logService;
	}
	
	public void setLogService(ILogService logService) {
		this.logService = logService;
	}
	
}