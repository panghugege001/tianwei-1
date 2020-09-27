package dfh.utils.transfer;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.Actionlogs;
import dfh.model.LosePromo;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.WeekSent;
import dfh.model.enums.ActionLogType;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.QtUtil;

public class SynchrQtInUtil {

	private static Logger log = Logger.getLogger(SynchrQtInUtil.class);

	private static SynchrQtInUtil instance = null;

	private SynchrQtInUtil() {

	}

	public static SynchrQtInUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrQtInUtil();
		} else {
			return instance;
		}
	}

	public synchronized String transferQtAndSelfYouHuiIn(TransferService transferService, String seqId, String loginname, Double remit, String remark) {
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		
//		synchronized (user.getId()) {
			// 转账前先判断额度是否可以获取到
			Object output;
			try {
				output = (loginname != null ? QtUtil.getBalance(loginname) : null);
				if (output == null || QtUtil.RESULT_FAIL.equals(output)) {
					log.info(loginname + "获取额度超时!系统繁忙!");
					return "系统繁忙!请稍后再试";
				}
				if(!NumberUtils.isNumber(output.toString())){
					log.info(loginname + " 金额不是数字!"+output.toString());
					return "系统繁忙!请稍后再试";
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info(loginname + "获取额度超时!系统繁忙!" + e.toString());
				return "系统繁忙!请稍后再试";
			}
			
			//负盈利反赠处理
			Double qtBalance = Double.parseDouble(output.toString());
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "qt"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = transferService.findByCriteria(c);
			if(qtBalance >= 5.00 && losePromoList.size() > 0){
				//如果存在已领取的负盈利反赠，不允许转入
				log.info("玩家已领取负盈利反赠，且余额大于5元，不允许转入" +loginname);
				return "您已领取救援金且QT余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
			}
			//将已领取的负盈利反赠更新为已处理
			for (LosePromo losePromo : losePromoList) {
				losePromo.setStatus("2");
				transferService.update(losePromo);
			}
			
			//周周回馈处理
			DetachedCriteria dc=DetachedCriteria.forClass(WeekSent.class);
			dc.add(Restrictions.eq("username", loginname));
			dc.add(Restrictions.eq("platform", "qt"));
			dc.add(Restrictions.eq("status", "1"));
			List<WeekSent> weekSentList = transferService.findByCriteria(dc);
			if(qtBalance > 5.00 && weekSentList.size() > 0){
				//如果存在已领取的周周回馈，不允许转入
				log.info("玩家已领取周周回馈，且余额大于5元，不允许转入" +loginname);
				return "您已领取周周回馈且QT余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
			}
			//将已领取的周周回馈更新为已处理
			for (WeekSent ws : weekSentList) {
				ws.setStatus("2");
				transferService.update(ws);
			}

			//PT8元体验金
			if(qtBalance >=1 && qtBalance<100){
				DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
				dc8.add(Restrictions.eq("loginname", loginname)) ;
				dc8.add(Restrictions.eq("target", "qt"));
				dc8.addOrder(Order.desc("createtime"));
				List<Transfer> transfers = transferService.findByCriteria(dc8, 0, 10) ;
				if(null != transfers && transfers.size()>0){
					Transfer transfer = transfers.get(0);
					if(null != transfer){
						if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
							return "您正在使用体验金，QT余额大于等于100或者小于1的时候才能进行户内转账";
						}
					}
				}
				
			}
			
			double oldCredit = Math.abs(user.getCredit());
			String msg = transferService.transferInforQtIn(seqId, loginname, remit, null, remark);
			if (msg == null) {
				Users user1 = transferService.getUsers(loginname);
				remit = Math.abs(remit);
				double newCredit = Math.abs(user1.getCredit());
				if (oldCredit != newCredit) {
					try {
						log.info(loginname + " QT转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
						// 转进QT成功
						String deposit = QtUtil.getDepositPlayerMoney(loginname, Double.valueOf(remit), seqId);
						if (QtUtil.RESULT_SUCC.equals(deposit)) {
							transferService.addTransferforQt(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
							//transferService.updateUserShippingcodePtSql(user1);
							log.info("转账成功！" + seqId + "***账户:" + loginname);
							return null;
						} else {
							// 转账失败 退还额度
							/*
							Creditlogs creditLog = new Creditlogs();
							creditLog.setLoginname(loginname);
							creditLog.setType(CreditChangeType.TRANSFER_MEWPTIN.getCode());
							creditLog.setCredit(newCredit);
							creditLog.setRemit(remit);
							creditLog.setNewCredit(newCredit + remit);
							creditLog.setRemark("NEWPT转账错误!自动退还:" + seqId + "订单");
							creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
							transferService.save(creditLog);
							*/
							// 打印日志
							Actionlogs actionlog = new Actionlogs();
							actionlog.setLoginname(loginname);
							actionlog.setRemark(null);
							actionlog.setCreatetime(DateUtil.now());
							actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
							String info = "QT转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
							actionlog.setRemark(info);
							transferService.save(actionlog);
							log.info(info);
							// 退还款项
							//transferService.updateUserCreditSql(user1, remit);
							log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
							return "出现未知情况!扣除款项！请联系在线客服!";
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
						return "出现未知情况!扣除款项！请联系在线客服!";
					}
				} else {
					log.info("QT转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
					log.info("转账额度出现问题！请联系在线客服：" + loginname);
					return "转账额度出现问题！请联系在线客服";
				}
			}
			return msg;
//		}
	}
	
	
	/*public synchronized String transferQtAndSelfYouHuiInQD(TransferService transferService, String seqId, String loginname, Double remit, Proposal proposal,List<SignRecord> list) {
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		// 转账前先判断额度是否可以获取到
		Object output;
		try {
			output = (loginname != null ? QtUtil.getBalance(loginname) : null);
			if (output == null || QtUtil.RESULT_FAIL.equals(output)) {
				log.info(loginname + "获取额度超时!系统繁忙!");
				return "系统繁忙!请稍后再试";
			}
			if(!NumberUtils.isNumber(output.toString())){
				log.info(loginname + " 金额不是数字!"+output.toString());
				return "系统繁忙!请稍后再试";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(loginname + "获取额度超时!系统繁忙!" + e.toString());
			return "系统繁忙!请稍后再试";
		}
		
		//负盈利反赠处理
		Double qtBalance = Double.parseDouble(output.toString());
		DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "qt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = transferService.findByCriteria(c);
		if(qtBalance >= 5.00 && losePromoList.size() > 0){
			//如果存在已领取的负盈利反赠，不允许转入
			log.info("玩家已领取负盈利反赠，且余额大于5元，不允许转入" +loginname);
			return "您已领取救援金且QT余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的负盈利反赠更新为已处理
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			transferService.update(losePromo);
		}
		//周周回馈处理
		DetachedCriteria dc=DetachedCriteria.forClass(WeekSent.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("platform", "qt"));
		dc.add(Restrictions.eq("status", "1"));
		List<WeekSent> weekSentList = transferService.findByCriteria(dc);
		if(qtBalance > 5.00 && weekSentList.size() > 0){
			//如果存在已领取的周周回馈，不允许转入
			log.info("玩家已领取周周回馈，且余额大于5元，不允许转入" +loginname);
			return "您已领取周周回馈且QT余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的周周回馈更新为已处理
		for (WeekSent ws : weekSentList) {
			ws.setStatus("2");
			transferService.update(ws);
		}
		
		//PT8元体验金
		if(qtBalance >=1 && qtBalance<100){
			DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
			dc8.add(Restrictions.eq("loginname", loginname)) ;
			dc8.add(Restrictions.eq("target", "qt")) ;
			dc8.addOrder(Order.desc("createtime"));
			List<Transfer> transfers = transferService.findByCriteria(dc8, 0, 10) ;
			if(null != transfers && transfers.size()>0){
				Transfer transfer = transfers.get(0);
				if(null != transfer){
					if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
						return "您正在使用体验金，QT余额大于等于100或者小于1的时候才能进行户内转账";
					}
				}
			}
		}
		double oldCredit = Math.abs(user.getCredit());
		String msg=null;
		msg = transferService.transferInforQtInQD(seqId, loginname, remit, null, proposal.getRemark());
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
				try {
					log.info(loginname + " QT转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					// 转进QT成功
					String deposit = QtUtil.getDepositPlayerMoney(loginname, Double.valueOf(remit), seqId);
					if (QtUtil.RESULT_SUCC.equals(deposit)) {
						transferService.addTransferforQt(Long.parseLong(seqId), loginname, oldCredit, 0.0, Constants.IN, Constants.FAIL, "", "转入成功");
						log.info("转账成功！" + seqId + "***账户:" + loginname);
						transferService.save(proposal);
						for(SignRecord sR : list){
					    	sR.setIsused("1");
					    	sR.setRemark(proposal.getPno()+":"+proposal.getRemark());
					    	sR.setUpdatetime(new Date());
					    	transferService.update(sR);
					    }
						return null;
					} else {
						// 打印日志
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info = "QT转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + oldCredit + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + newCredit  + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
						actionlog.setRemark(info);
						transferService.save(actionlog);
						log.info(info);
						// 退还款项
						log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
						return "签到金额转入QT出现未知情况!如有疑问请联系在线客服!";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "签到金额转入QT出现未知情况！如有疑问请联系在线客服!";
				}
		}
		return msg;

	}*/

}
