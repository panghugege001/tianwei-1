package dfh.utils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dfh.model.*;
import dfh.service.interfaces.*;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.AutoXima;
import dfh.action.vo.AutoYouHuiVo;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.EBetCode;
import dfh.model.enums.ProposalType;
import dfh.model.enums.VipLevel;
import dfh.model.enums.WarnLevel;
import dfh.remote.DocumentParser;
import dfh.remote.ErrorCode;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.DepositPendingResponseBean;
import dfh.remote.bean.DspResponseBean;
import dfh.remote.bean.EBetResp;
import dfh.remote.bean.KenoResponseBean;

/**
 * 根据单个用户做同步隔离，每个用户分配一个实例对象
 * 用户所有需要同步控制的业务均可由此处发起
 * 
 *    ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
 *    
 *         严禁 UserSynchronizedPool 以外的实例化调用
 *         
 *              请尽量不要在此类添加全局变量
 *    ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
 */
public class Synchronized4User {

	private static Logger log = Logger.getLogger(Synchronized4User.class);
	
	Synchronized4User(){}
	
	/**
	 * PT 转入
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @param remark
	 * @return
	 */
	public synchronized String transferPtAndSelfYouHuiIn(TransferService transferService, String seqId, String loginname, Double remit, String remark) {
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>同步PT转入开始：" + loginname + "  金额：" + remit);
		// 转账前先判断额度是否可以获取到
		Object output;
		try {
			output = (loginname != null ? PtUtil.getPlayerMoney(loginname) : null);
			if (output == null || output.equals("")) {
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
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		
		//负盈利反赠处理
		Double ptBalance = Double.parseDouble(output.toString());
		DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "pttiger"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = transferService.findByCriteria(c);
		if(ptBalance >= 5.00 && losePromoList.size() > 0){
			//如果存在已领取的负盈利反赠，不允许转入
			log.info("玩家已领取负盈利反赠，且余额大于5元，不允许转入" +loginname);
			return "您已领取救援金且PT余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的负盈利反赠更新为已处理
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			transferService.update(losePromo);
		}
		
		//周周回馈处理
		DetachedCriteria dc=DetachedCriteria.forClass(WeekSent.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("platform", "pttiger"));
		dc.add(Restrictions.eq("status", "1"));
		List<WeekSent> weekSentList = transferService.findByCriteria(dc);
		if(ptBalance > 5.00 && weekSentList.size() > 0){
			//如果存在已领取的周周回馈，不允许转入
			log.info("玩家已领取周周回馈，且余额大于5元，不允许转入" +loginname);
			return "您已领取周周回馈且PT余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的周周回馈更新为已处理
		for (WeekSent ws : weekSentList) {
			ws.setStatus("2");
			transferService.update(ws);
		}
		
		//PT疯狂礼金
		DetachedCriteria bigBangDc = DetachedCriteria.forClass(PTBigBang.class);
		bigBangDc.add(Restrictions.eq("username", loginname));
		bigBangDc.add(Restrictions.eq("status", "2"));
		List<PTBigBang> ptBigBangList = transferService.findByCriteria(bigBangDc);
		if(ptBalance >= 5.00 && ptBigBangList.size() > 0){
			log.info("玩家已领取PT疯狂礼金，且余额大于5元，不允许转入" +loginname);
			return "已领取PT疯狂礼金，且PT余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的PT疯狂礼金更新为已处理
		for (PTBigBang ptBigBang : ptBigBangList) {
			ptBigBang.setStatus("3");
			transferService.update(ptBigBang);
		}
		//PT8元体验金
		if(ptBalance >=1 && ptBalance<Constants.MAXIMUMDTOUT){
			DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
			dc8.add(Restrictions.eq("loginname", loginname)) ;
			dc8.add(Restrictions.eq("target", "newpt")) ;
			dc8.addOrder(Order.desc("createtime"));
			List<Transfer> transfers = transferService.findByCriteria(dc8, 0, 10) ;
			if(null != transfers && transfers.size()>0){
				Transfer transfer = transfers.get(0);
				if(null != transfer){
					if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是app下载彩金的转账记录
						return "您正在使用app下载彩金，PT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					}    
					if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
						return "您正在使用体验金，PT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					}
				}
			}
			
		}
		
		double oldCredit = Math.abs(user.getCredit());
		String msg = transferService.transferPtAndSelfYouHuiInIn(seqId, loginname, remit, remark);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info(loginname + " NEWPT转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					// 转进NEWPT成功
					Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, remit);
					if (null != deposit  && deposit) {
						transferService.addTransferforNewPt(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
						transferService.updateUserShippingcodePtSql(user1);
						log.info("转账成功！" + seqId + "***账户:" + loginname);
						log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>同步PT转入结束：" + loginname + "  金额：" + remit);
						return null;
					} else {
						// 转账失败 退还额度
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info = "NEWPT转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
						actionlog.setRemark(info);
						transferService.save(actionlog);
						log.info(info);
						// 退还款项
						//transferService.updateUserCreditSql(user1, remit);
						log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
						return "出现未知情况!扣除款项！请联系在线客服!";
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>同步PT转入异常：" + loginname + "  金额：" + remit);
					return "出现未知情况!扣除款项！请联系在线客服!";
				}
			} else {
				log.info("NEWPT转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}

	public synchronized String applyActivity(IUpgradeService upgradeService, ProposalService proposalService, TransferService transferService, ISelfYouHuiService selfYouHuiService, String title, String loginname, String platform, String entrance, Users user, String sid) throws Exception {
		Date today = new Date();
		ActivityConfig ac = null;
		String yyyyMM = DateUtil.yyyyMM(new Date());
		String yyyy=DateUtil.yyyy(today);
		Date date = DateUtil.parse_YYYY_MM_DD(DateUtil.fmtYYYY_MM_DD(new Date()));
		String day=DateUtil.getWhichWeek(date);
		String s="";
		List<ActivityConfig> activityConfigs = MiddleServiceUtil.checkStatus(title,entrance,null);
		if ( activityConfigs == null) {
			return "活动不存在";
		} else {
			for (ActivityConfig config : activityConfigs) {
				if (config.getLevel().equals(user.getLevel().toString())) {
					ac = config;
					break;
				}

			}
			if(ac==null){
				return  "您好，您的VIP等级不在奖励范围之内";
			}
			if (ac.getStatus() == 0) {
				return "活动未开启";
			}
			if (ac.getActivitystarttime() != null && ac.getActivityendtime() != null) {
				if (ac.getActivitystarttime().after(today) || ac.getActivityendtime().before(today)) {
					return "活动未开启";
				}
			}

			if(ac.getBetstarttime()!=null&&ac.getBetendtime()!=null) {
				Map<String, Double> total = upgradeService.getTotalBetByUser(loginname, DateUtil.fmtDateForBetRecods(ac.getBetstarttime()), DateUtil.fmtDateForBetRecods(ac.getBetendtime()));
				Double slot = total.get("pt");
				if (slot < ac.getBet()) {
					double v = ac.getBet() - slot;
					return "您的投注额还没达标,还需要" + v + "投注";
				}
			}
			if(ac.getDepositstarttime()!=null&&ac.getDepositendtime()!=null&&null!=ac.getDeposit()) {
				Double depositAmount = upgradeService.getDepositAmount(loginname, DateUtil.fmtDateForBetRecods(ac.getDepositstarttime()), DateUtil.fmtDateForBetRecods(ac.getDepositendtime()));
				if (ac.getDeposit() > depositAmount) {
					double v = ac.getDeposit() - depositAmount;
					return "您的存款额还没达标,还需要" + v + "存款";
				}
			}


			DetachedCriteria dc = DetachedCriteria.forClass(ActivityHistory.class);
			dc.add(Restrictions.eq("username", loginname));
			dc.add(Restrictions.eq("englishtitle", ac.getEnglishtitle()));
			switch (ac.getScope()){
				case "day":
					s=DateUtil.fmtyyyyMMdd(new Date());
					dc.add(Restrictions.eq("scope",s ));
					break;
				case "week":
					s=yyyyMM+day;
					dc.add(Restrictions.eq("scope",s ));
					break;
				case "month":
					s=yyyy+DateUtil.getMonth(new Date());
					dc.add(Restrictions.eq("scope",s ));
					break;
				default:
			}


			List<ActivityHistory> list = proposalService.findByCriteria(dc);

			if (null != list && list.size() > 0&&list.size()>=ac.getTimes()) {
				return "已经领取或超过领取次数";
			}

			if(ac.getMachinecode()==1){
				dc.add(Restrictions.eq("sid",sid ));
				List<ActivityHistory> histories = proposalService.findByCriteria(dc);
				if (null != list && list.size() > 0&&histories.size()>=ac.getSidcount()) {
					return "您的手机已经超过领取次数";
				}
			}

			ActivityHistory record = new ActivityHistory();
			record.setScope(s);//日期
			record.setUsername(loginname);
			record.setTitle(ac.getTitle());
			record.setPhone(AESUtil.aesDecrypt(user.getPhone(),AESUtil.KEY));
			record.setAccountname(user.getAccountName());
			record.setAddress(user.getAddress());
			record.setEnglishtitle(ac.getEnglishtitle());
			record.setSid(sid);
			record.setActivityid(ac.getId());
			record.setCreatetime(new Date());
			record.setLevel(user.getLevel());
			record.setTimes(1);
			if (ac.getMultiplestatus() == 0) {
				String transferToMain = null;
				try {
					transferToMain = SynchronizedUtil.getInstance().transferToMain(transferService, user, ac.getAmount().intValue());
					record.setRemark("领取"+ac.getAmount()+"筹码->主账户");
					proposalService.save(record);
				} catch (Exception e) {
					e.printStackTrace();
					return "领取失败";
				}
				return transferToMain;
			}
			else {
				if(ac.getPlatform().contains(platform)){
					try {
						String info = SynchronizedUtil.getInstance().transferActivity(selfYouHuiService, loginname, ac.getAmount(), platform, ac);
						if(null==info|| org.apache.commons.lang3.StringUtils.isEmpty(info)){
							record.setRemark("领取"+ac.getAmount()+"筹码->"+platform+"账户");
							proposalService.save(record);
							return "获得"+ac.getAmount()+"彩金,转入平台成功";
						}return info;
					} catch (Exception e) {
						e.printStackTrace();
						return "转入失败";
					}
				}else {
					return "不支持此平台的转入";
				}



			}


		}
	}
	
	/**
	 * PT 转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferPtAndSelfYouHuiOut(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferPtAndSelfYouHuiOut(seqId, loginname, remit);
	}

	/**
	 * 领取自助红包
	 * @param loginname
	 * @return
	 */
	public synchronized String  doHB(ProposalService proposalService, CustomerService customerService, String loginname, HBConfig config, Integer hbID){
		Double result = 0.0;

		Integer timesflag = config.getTimesflag();
		Integer times = config.getTimes();
		//查询使用记录
		DetachedCriteria criteria = DetachedCriteria.forClass(HBrecord.class);//查询是否有领取当前红包之外的红包记录
		criteria.add(Restrictions.eq("username", loginname));
		criteria.add(Restrictions.eq("hbid", hbID));
		criteria.add(Restrictions.eq("deposit", config.getType()));
		criteria.add(Restrictions.eq("type", "1"));//收入
		if(timesflag == 1){  //日
			if (config.getType()==1)
				result=customerService.checkWithdrawRecord(loginname,DateUtil.getTodayZeroHour());
			else
				result=customerService.checkDepositRecord(loginname,DateUtil.getTodayZeroHour());
			criteria.add(Restrictions.ge("updatetime", DateUtil.getTodayZeroHour()));
			criteria.add(Restrictions.le("updatetime", DateUtil.getTodayEndHour()));
		}else if (timesflag == 2){ //周
			if (config.getType()==1)
				result=customerService.checkWithdrawRecord(loginname,DateUtil.getFirstDayOfWeek(new Date()));
			else
				result=customerService.checkDepositRecord(loginname, DateUtil.getFirstDayOfWeek(new Date()));
			criteria.add(Restrictions.ge("updatetime", DateUtil.getFirstDayOfWeek(new Date())));
		}else if (timesflag == 3){ //月
			if (config.getType()==1)
				result=customerService.checkWithdrawRecord(loginname,DateUtil.getStartDayOfMonth(new Date()));
			else
				result=customerService.checkDepositRecord(loginname,DateUtil.getStartDayOfMonth(new Date()));
			criteria.add(Restrictions.ge("updatetime", DateUtil.getStartDayOfMonth(new Date())));
		}else if (timesflag == 4){ //年
			if (config.getType()==1)
				result=customerService.checkWithdrawRecord(loginname,DateUtil.getYearFirst(Calendar.getInstance().get(Calendar.YEAR)));
			else
				result=customerService.checkDepositRecord(loginname,DateUtil.getYearFirst(Calendar.getInstance().get(Calendar.YEAR)));
			criteria.add(Restrictions.ge("updatetime", DateUtil.getYearFirst(Calendar.getInstance().get(Calendar.YEAR))));
		}


		List<HBrecord> record1 = proposalService.findByCriteria(criteria);
		if(null != record1 && record1.size() > 0){
			return "今日的"+config.getTitle()+"已领取,请明日再来。";
		}

		Double LimitStartMoney = config.getLimitStartMoney();
		if(Arith.sub(result,LimitStartMoney)>= 0.0){
			Double money = config.getAmount();
			Integer hbid = config.getId();
			DetachedCriteria dcBonusEdit = DetachedCriteria.forClass(HBbonus.class);
			dcBonusEdit.add(Restrictions.eq("username", loginname));
			dcBonusEdit.add(Restrictions.eq("phone", config.getType()));
			List<HBbonus> listBonus = proposalService.findByCriteria(dcBonusEdit);
			HBbonus HBbonus=null;
			if(null!=listBonus&&listBonus.size()>0){
				HBbonus=listBonus.get(0);
			}else{
				HBbonus=new HBbonus();
				HBbonus.setMoney(0.0);
				HBbonus.setPhone(config.getType());
			}

			HBrecord em = new HBrecord();
			em.setHbid(hbid);
			em.setUsername(loginname);
			em.setUpdatetime(new Date());
			em.setType("1");//收入
			em.setMoney(money);
			em.setDeposit(config.getType());
			em.setRemark("玩家领取"+config.getTitle()+"类型红包，金额"+money+"元已入账");
			proposalService.save(em);

			HBbonus.setUpdatetime(new Date());
			HBbonus.setUsername(loginname);
			HBbonus.setMoney(Arith.add(HBbonus.getMoney(),money));
			proposalService.saveOrUpdate(HBbonus);
			return "红包奖励："+money+"元已入账";
		}else{
			return "没达到该类红包的最低要求，无法领取。";
		}

	}
	
	

	
	/**
	 * ttg红包优惠
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferTtgRedCoup(TransferService transferService , String seqId, String loginname, String couponType,String couponCode,String ip){
		AutoYouHuiVo vo = new AutoYouHuiVo();
		vo= transferService.transferInforRedCouponTtg(seqId,loginname,couponType,couponCode,ip);	
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
			PtUtil1.addPlayerAccountPraper(loginname, vo.getGiftMoney());
		}
		return vo;
	}
	
	/**
	 * pt红包优惠
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferPtRedCoup(TransferService transferService , String seqId, String loginname, String couponType,String couponCode,String ip){
		AutoYouHuiVo vo = new AutoYouHuiVo();
		vo= transferService.transferInforRedCouponPt(seqId,loginname,couponType,couponCode,ip);	
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
			 PtUtil.getDepositPlayerMoney(loginname, vo.getGiftMoney());
		}
		return vo;
	}
	
	
	
	/**
	 * qt红包优惠
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferQtRedCoup(TransferService transferService , String seqId, String loginname, String couponType,String couponCode,String ip){
		AutoYouHuiVo vo= transferService.transferInforRedCouponQT(seqId,loginname,couponType,couponCode,ip);
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
			QtUtil.getDepositPlayerMoney(loginname, vo.getGiftMoney(), seqId);
		}
		return vo;
	}
	
	
	/**
	 * nt红包优惠
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferNtRedCoup(TransferService transferService , String seqId, String loginname, String couponType,String couponCode,String ip){
		AutoYouHuiVo vo= transferService.transferInforRedCouponNT(seqId,loginname,couponType,couponCode,ip);
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
			NTUtils.changeMoney(loginname, vo.getGiftMoney());
		}
		return vo;
	}

	
	/**
	 * mg红包优惠
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferMgRedCoup(TransferService transferService , String seqId, String loginname, String couponType,String couponCode,String ip){
		AutoYouHuiVo vo= transferService.transferInforRedCouponMG(seqId,loginname,couponType,couponCode,ip);
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
			Double giftMoney = vo.getGiftMoney();
			Users user = (Users) transferService.get(Users.class, loginname);
			MGSUtil.transferToMG(loginname,user.getPassword(),giftMoney,seqId);
		}
		return vo;
	}
	
	
	
	/**
	 * dt红包优惠
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferDtRedCoup(TransferService transferService , String seqId, String loginname, String couponType,String couponCode,String ip){
		AutoYouHuiVo vo= transferService.transferInforRedCouponDT(seqId,loginname,couponType,couponCode,ip);
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
			Double giftMoney = vo.getGiftMoney();
			try {
				DtUtil.withdrawordeposit(loginname, giftMoney);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return vo;
	}
	
	
	
	
	
	/**
	 * ttg优惠卷
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferTtgCoup(TransferService transferService , String seqId, String loginname, String couponType,Double remit,String couponCode,String ip){
		AutoYouHuiVo vo = new AutoYouHuiVo();
		vo= transferService.transferInforCouponTtg(seqId,loginname,couponType, remit,couponCode,ip);	
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
			PtUtil1.addPlayerAccountPraper(loginname, vo.getGiftMoney());
		}
		return vo;
	}
	
	/**
	 * pt优惠卷
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferPtCoup(TransferService transferService , String seqId, String loginname, String couponType,Double remit,String couponCode,String ip){
		AutoYouHuiVo vo= transferService.transferInforCoupon(seqId,loginname,couponType, remit,couponCode,ip);
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
				 PtUtil.getDepositPlayerMoney(loginname, vo.getGiftMoney());
			}
		return vo;
	}
	
	/**
	 * nt优惠卷
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferNtCoup(TransferService transferService , String seqId, String loginname, String couponType,Double remit,String couponCode,String ip){
		AutoYouHuiVo vo= transferService.transferInforCouponNT(seqId,loginname,couponType, remit,couponCode,ip); 
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
			NTUtils.changeMoney(loginname, vo.getGiftMoney());
		}
		return vo;
	}
	
	/**
	 * qt优惠卷
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferQtCoup(TransferService transferService , String seqId, String loginname, String couponType,Double remit,String couponCode,String ip){
		AutoYouHuiVo vo= transferService.transferInforCouponQT(seqId,loginname,couponType, remit,couponCode,ip);
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
			QtUtil.getDepositPlayerMoney(loginname, vo.getGiftMoney(), seqId);
		}
		return vo;
	}
	/**
	 * 积分
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferPoint(ProposalService proposalService , Users user,Double remit){
		return proposalService.transferPoint(user, remit);
	}
	
	/**
	 * 自助存送
	 * @param selfYouHuiService
	 * @param loginname
	 * @param youhuiType
	 * @param remit
	 * @return
	 */
	public synchronized AutoYouHuiVo transferInBonus(ISelfYouHuiService selfYouHuiService ,String loginname , Integer youhuiType , Double remit) {
		return null;
	}
	
	/**
	 * EA 转入
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferInEA(TransferService transferService, String seqId, String loginname, Double remit) {
		//转账前先判断额度是否可以获取到
		try {
			Object output = (loginname != null ? RemoteCaller.queryCredit(loginname) : null);
			if (output == null || output.equals("")){
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
		//获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		
		double oldCredit = Math.abs(user.getCredit());
		String msg = transferService.transferEaIn(seqId, loginname, remit);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info(loginname + " EA转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					DepositPendingResponseBean depositPendingResponseBean = RemoteCaller.depositPendingRequest(user1.getLoginname(), remit, seqId, user1.getAgcode());
					// 如果操作成功
					if(depositPendingResponseBean==null){
						log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
						return "出现未知情况!扣除款项！请联系在线客服!";
					}else if (depositPendingResponseBean.getStatus().equals(ErrorCode.SUCCESS.getCode())) {
						try {
							RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), user1.getAgcode());
						} catch (Exception e) {
							log.warn("depositConfirmationResponse try again!");
							try {
								RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), depositPendingResponseBean.getAgcode());
							} catch (Exception e1) {
								log.info(loginname + e1.toString());
							}
						}
						log.warn(loginname + "转入游戏单号:" + depositPendingResponseBean.getPaymentid());
						transferService.addTransfer(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, depositPendingResponseBean.getPaymentid(), null);
						if (user1.getShippingcode() == null || user1.getShippingcode().equals("")) {

						} else {
							transferService.updateUserShippingcodeSql(user1);
						}
						log.info(loginname + " EA转账成功！");
						return null;
					} else {
						// 转账失败 退还额度
						Creditlogs creditLog = new Creditlogs();
						creditLog.setLoginname(loginname);
						creditLog.setType(CreditChangeType.TRANSFER_IN.getCode());
						creditLog.setCredit(newCredit);
						creditLog.setRemit(remit);
						creditLog.setNewCredit(newCredit + remit);
						creditLog.setRemark("EA转账错误!自动退还:" + seqId + "订单");
						creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
						transferService.save(creditLog);
						// 打印日志
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info="EA转账错误!改变前订单:" + seqId +" 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit+" 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit+" 系统已经处理 请检查是有否错误";
						actionlog.setRemark(info);
						transferService.save(actionlog);
						log.info(info);
						// 退还款项
						transferService.updateUserCreditSql(user1, remit);
						log.info("转账状态错误:" + ErrorCode.getChText(depositPendingResponseBean.getStatus()) + "***" + loginname);
						// 打印日志
						return "转账状态错误:" + ErrorCode.getChText(depositPendingResponseBean.getStatus());
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "出现未知情况!扣除款项！请联系在线客服!";
				}
			} else {
				log.info("EA转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}
	
	/**
	 * EA 转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferOutEA(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferOut(seqId, loginname, remit);
	}
	/**
	 * AG 转入
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferInforDsp(TransferService transferService , String seqId, String loginname, Double remit){
		//转账前先判断额度是否可以获取到
		try {
			Object output = (loginname != null ? RemoteCaller.queryDspCredit(loginname) : null);
			if (output == null || output.equals("")) {
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
		//获取用户
		Users user = transferService.getUsers(loginname);
		if(user==null){
			log.info("用户不存在!*****"+loginname);
			return "用户不存在!";
		}
		
		double oldCredit=Math.abs(user.getCredit());
		String msg = transferService.transferInforDspIn(seqId, loginname, remit);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit=Math.abs(user1.getCredit());
			if (oldCredit!=newCredit) {
				try {
					log.info(loginname+" AG转账额度不同：******"+loginname+"******旧额度:"+oldCredit+"******新额度:"+newCredit+"******改变额度:"+remit);
					DspResponseBean dspResponseBean = RemoteCaller.depositPrepareDspRequest(loginname, remit, seqId);
					DspResponseBean dspConfirmResponseBean = null;
					if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
						try {
							dspConfirmResponseBean = RemoteCaller.depositConfirmDspRequest(loginname, remit, seqId, 1);
						} catch (Exception e) {
							log.warn("dspConfirmResponseBean try again!");
							try{
								dspConfirmResponseBean = RemoteCaller.depositConfirmDspRequest(loginname, remit, seqId, 1);
							}catch(Exception e1){
								log.info(loginname+e1.toString());
							}
						}
						//只有是0的才是成功的
						if (dspConfirmResponseBean == null || dspConfirmResponseBean.getInfo().equals("1")) {
							log.info(loginname+"转账发生异常,请稍后再试或联系在线客服");
							return "转账发生异常,请稍后再试或联系在线客服";
						}else if(dspConfirmResponseBean.getInfo().equals("0")){
							transferService.addTransferforDsp(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", null);
							log.info(loginname+" AG转账成功！");
							return null;
						}else{
							log.info(loginname+"转账错误信息"+dspConfirmResponseBean.getInfo());
							// 转账失败 退还额度
							/*Creditlogs creditLog = new Creditlogs();
							creditLog.setLoginname(loginname);
							creditLog.setType(CreditChangeType.TRANSFER_DSPIN.getCode());
							creditLog.setCredit(newCredit);
							creditLog.setRemit(remit);
							creditLog.setNewCredit(newCredit + remit);
							creditLog.setRemark("AG转账错误!自动退还:" + seqId + "订单");
							creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
							transferService.save(creditLog);*/
							// 打印日志
							Actionlogs actionlog = new Actionlogs();
							actionlog.setLoginname(loginname);
							actionlog.setRemark(null);
							actionlog.setCreatetime(DateUtil.now());
							actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
							String info="AG转账错误!改变前订单:" + seqId +" 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit+" 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit+" 系统已经处理 请检查是有否错误";
							actionlog.setRemark(info);
							transferService.save(actionlog);
							log.info(info);
							// 退还款项
//							transferService.updateUserCreditSql(user1, remit);
							log.info("转账状态错误处理成功:" + dspConfirmResponseBean.getInfo()+"***"+loginname);
							return "转账状态错误:"+dspConfirmResponseBean.getInfo();
						}
					} else {
						dspConfirmResponseBean = RemoteCaller.depositConfirmDspRequest(loginname, remit, seqId, 0);
						log.info("转账状态错误1:" + dspConfirmResponseBean.getInfo()+"***"+loginname);
						log.info("转账状态错误2:" + dspResponseBean.getInfo()+"***"+loginname);
						// 转账失败 退还额度
						/*Creditlogs creditLog = new Creditlogs();
						creditLog.setLoginname(loginname);
						creditLog.setType(CreditChangeType.TRANSFER_DSPIN.getCode());
						creditLog.setCredit(newCredit);
						creditLog.setRemit(remit);
						creditLog.setNewCredit(newCredit + remit);
						creditLog.setRemark("AG转账错误!自动退还:" + seqId + "订单");
						creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
						transferService.save(creditLog);*/
						// 打印日志
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info="AG转账错误!改变前订单:" + seqId +" 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit+" 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit+" 系统已经处理 请检查是有否错误";
						actionlog.setRemark(info);
						transferService.save(actionlog);
						log.info(info);
						// 退还款项
//						transferService.updateUserCreditSql(user1, remit);
						log.info("转账状态错误处理成功:" + dspConfirmResponseBean.getInfo()+"***"+loginname);
						return "转账状态错误:" + dspResponseBean.getInfo();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "网络繁忙！请稍等再试";
				}
			}else{
				log.info("AG转账额度相同 转账额度出现问题:******"+loginname+"******旧额度:"+oldCredit+"******新额度:"+user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服" ;
			}
		}
		return msg;
	}
	
	/**
	 * AG 转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferOutforDsp(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferOutforDsp(seqId, loginname, remit);
	}
	
	/**
	 * AGIN 转入
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferInforAginDsp(TransferService transferService , String seqId, String loginname, Double remit){
		//转账前先判断额度是否可以获取到
		try {
			Object output = (loginname != null ? RemoteCaller.queryDspAginCredit(loginname) : null);
			if (output == null || output.equals("")) {
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
		//获取用户
		Users user = transferService.getUsers(loginname);
		if(user==null){
			log.info("用户不存在!*****"+loginname);
			return "用户不存在!";
		}
		double oldCredit=Math.abs(user.getCredit());
		String msg = transferService.transferInforAginDspIn(seqId, loginname, remit);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit=Math.abs(user1.getCredit());
			if (oldCredit!=newCredit) {
				try {
					log.info(loginname+" AGIN转账额度不同：******"+loginname+"******旧额度:"+oldCredit+"******新额度:"+newCredit+"******改变额度:"+remit);
					DspResponseBean dspResponseBean = RemoteCaller.depositPrepareDspAginRequest(loginname, remit, seqId);
					DspResponseBean dspConfirmResponseBean = null;
					if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
						try {
							dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, remit, seqId, 1);
						} catch (Exception e) {
							log.warn("dspConfirmResponseBean try again!");
							try{
								dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, remit, seqId, 1);
							}catch(Exception e1){
								log.info(loginname+e1.toString());
							}
						}
						//只有是0的才是成功的
						if (dspConfirmResponseBean == null || dspConfirmResponseBean.getInfo().equals("1")) {
							log.info(loginname+"转账发生异常,请稍后再试或联系在线客服");
							return "转账发生异常,请稍后再试或联系在线客服";
						}else if(dspConfirmResponseBean.getInfo().equals("0")){
							transferService.addTransferforAginDsp(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", null);
							log.info(loginname+" AGIN转账成功！");
							return null;
						}else{
							log.info(loginname+"转账错误信息"+dspConfirmResponseBean.getInfo());
							// 转账失败 退还额度
							/*Creditlogs creditLog = new Creditlogs();
							creditLog.setLoginname(loginname);
							creditLog.setType(CreditChangeType.TRANSFER_DSPAGININ.getCode());
							creditLog.setCredit(newCredit);
							creditLog.setRemit(remit);
							creditLog.setNewCredit(newCredit + remit);
							creditLog.setRemark("AGIN转账错误!自动退还:" + seqId + "订单");
							creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
							transferService.save(creditLog);*/
							// 打印日志
							Actionlogs actionlog = new Actionlogs();
							actionlog.setLoginname(loginname);
							actionlog.setRemark(null);
							actionlog.setCreatetime(DateUtil.now());
							actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
							String info="AGIN转账错误!改变前订单:" + seqId +" 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit+" 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit+" 系统已经处理 请检查是有否错误";
							actionlog.setRemark(info);
							transferService.save(actionlog);
							log.info(info);
							// 退还款项
//							transferService.updateUserCreditSql(user1, remit);
							log.info("转账状态错误处理成功:" + dspConfirmResponseBean.getInfo()+"***"+loginname);
							return "转账状态错误:"+dspConfirmResponseBean.getInfo();
						}
					} else {
						dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, remit, seqId, 0);
						log.info("转账状态错误1:" + dspConfirmResponseBean.getInfo()+"***"+loginname);
						log.info("转账状态错误2:" + dspResponseBean.getInfo()+"***"+loginname);
						// 转账失败 退还额度
						/*Creditlogs creditLog = new Creditlogs();
						creditLog.setLoginname(loginname);
						creditLog.setType(CreditChangeType.TRANSFER_DSPAGININ.getCode());
						creditLog.setCredit(newCredit);
						creditLog.setRemit(remit);
						creditLog.setNewCredit(newCredit + remit);
						creditLog.setRemark("AGIN转账错误!自动退还:" + seqId + "订单");
						creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
						transferService.save(creditLog);*/
						// 打印日志
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info="AGIN转账错误!改变前订单:" + seqId +" 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit+" 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit+" 系统已经处理 请检查是有否错误";
						actionlog.setRemark(info);
						transferService.save(actionlog);
						log.info(info);
						// 退还款项
//						transferService.updateUserCreditSql(user1, remit);
						log.info("转账状态错误处理成功:" + dspConfirmResponseBean.getInfo()+"***"+loginname);
						return "转账状态错误:"+dspConfirmResponseBean.getInfo();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "网络繁忙！请稍等再试";
				}
			}else{
				log.info("AGIN转账额度相同 转账额度出现问题:******"+loginname+"******旧额度:"+oldCredit+"******新额度:"+user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服" ;
			}
		}
		return msg;
	}
	
	/**
	 * AGIN 转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferOutforAginDsp(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferOutforAginDsp(seqId, loginname, remit);
	}
	/**
	 * KG 转入
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @param ip
	 * @return
	 */
	public synchronized String transferInforkeno2(TransferService transferService, String seqId, String loginname, Double remit, String ip) {
		// 转账前先判断额度是否可以获取到
		try {
			KenoResponseBean bean = DocumentParser.parseKenocheckcreditResponseRequest(Keno2Util.checkcredit(loginname));
			if (bean != null) {
				if (bean.getName() != null && bean.getName().equals("Credit")) {

				} else if (bean.getName() != null && bean.getName().equals("Error")) {
					log.info(loginname + "获取额度超时1!系统繁忙!");
					return "系统繁忙!请稍后再试";
				}
			} else {
				log.info(loginname + "获取额度超时2!系统繁忙!");
				return "系统繁忙!请稍后再试";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(loginname + "获取额度超时3!系统繁忙!" + e.toString());
			return "系统繁忙!请稍后再试";
		}
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		double oldCredit = Math.abs(user.getCredit());
		String msg = transferService.transferInforkenoIn2(seqId, loginname, remit, ip);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info(loginname + " Keno2转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					KenoResponseBean bean = null;
					bean = DocumentParser.parseKenologinResponseRequest(Keno2Util.transferfirst(loginname, remit, "", ""));
					if (bean != null) {
						if (bean.getName() != null && bean.getName().equals("Error")) {
							// 转账失败 退还额度
							/*
							Creditlogs creditLog = new Creditlogs();
							creditLog.setLoginname(loginname);
							creditLog.setType(CreditChangeType.TRANSFER_KENO2IN.getCode());
							creditLog.setCredit(newCredit);
							creditLog.setRemit(remit);
							creditLog.setNewCredit(newCredit + remit);
							creditLog.setRemark("KENO2转账错误!自动退还:" + seqId + "订单");
							creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
							transferService.save(creditLog);
							*/
							// 打印日志
							Actionlogs actionlog = new Actionlogs();
							actionlog.setLoginname(loginname);
							actionlog.setRemark(null);
							actionlog.setCreatetime(DateUtil.now());
							actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
							String info = "KENO2转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
							actionlog.setRemark(info);
							transferService.save(actionlog);
							log.info(info);
							// 退还款项
							//transferService.updateUserCreditSql(user1, remit);
							log.info("转账失败  : " + bean.getValue()+"*****"+ loginname);
							return "转账失败  : " + bean.getValue();
						} else if (bean.getName() != null && bean.getName().equals("FundIntegrationId")) {
							Integer FundIntegrationId = bean.getFundIntegrationId();
							if (FundIntegrationId != null) {
								KenoResponseBean confirmbean = DocumentParser.parseKenologinResponseRequest(Keno2Util.transferconfirm(loginname, remit, ip, FundIntegrationId, seqId));
								if(confirmbean==null){
									log.info("出现未知情况!不扣除款项！单号:" + seqId + "***账户:" + loginname);
									return "出现未知情况!不扣除款项！请联系在线客服!";
								}
								if (confirmbean.getName() != null && confirmbean.getName().equals("Remain")) {
									transferService.addTransferforKneo2(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.SUCESS, "", "转入成功");
									log.info("转账成功！" + seqId + "***账户:" + loginname);
									return null;
								} else {
									// 转账失败 退还额度
									/*Creditlogs creditLog = new Creditlogs();
									creditLog.setLoginname(loginname);
									creditLog.setType(CreditChangeType.TRANSFER_KENO2IN.getCode());
									creditLog.setCredit(newCredit);
									creditLog.setRemit(remit);
									creditLog.setNewCredit(newCredit + remit);
									creditLog.setRemark("KENO2转账错误!自动退还:" + seqId + "订单");
									creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
									transferService.save(creditLog);
									*/
									// 打印日志
									Actionlogs actionlog = new Actionlogs();
									actionlog.setLoginname(loginname);
									actionlog.setRemark(null);
									actionlog.setCreatetime(DateUtil.now());
									actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
									String info = "KENO2转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
									actionlog.setRemark(info);
									transferService.save(actionlog);
									log.info(info);
									// 退还款项
									//transferService.updateUserCreditSql(user1, remit);
									return "转账失败  : " + confirmbean.getValue();
								}
							}else{
								log.info("出现未知情况!不扣除款项！单号:" + seqId + "***账户:" + loginname);
								return "出现未知情况!不扣除款项！请联系在线客服!";
							}
						}else{
							log.info("出现未知情况!不扣除款项！单号:" + seqId + "***账户:" + loginname);
							return "出现未知情况!不扣除款项！请联系在线客服!";
						}
					}else{
						log.info("出现未知情况!不扣除款项！单号:" + seqId + "***账户:" + loginname);
						return "出现未知情况!不扣除款项！请联系在线客服!";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("出现未知情况!不扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "出现未知情况!不扣除款项！请联系在线客服!";
				}
			} else {
				log.info("KENO2转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}
	
	/**
	 * KG 转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @param remoteIp
	 * @return
	 */
	public synchronized String transferOutforkeno2(TransferService transferService ,String seqId , String loginname, Double remit, String remoteIp){
		return transferService.transferOutforkeno2(seqId, loginname, remit, remoteIp);
	}
	
	/**
	 * SB 体育转入
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferInSB(TransferService transferService , String seqId, String loginname, Double remit){
		// 转账前先判断额度是否可以获取到
		try {
			Object output = (loginname != null ? RemoteCaller.querySBCredit(loginname) : null);
			if (output == null || output.equals("")) {
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
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		
		double oldCredit = Math.abs(user.getCredit());
		String msg = transferService.transferInforTyIn(seqId, loginname, remit);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info(loginname + " SB转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					DspResponseBean dspResponseBean = null;
					try {
						dspResponseBean = RemoteCaller.depositSBRequest(loginname, remit, seqId);
					} catch (Exception e) {
						log.error("depositSBRequest异常,transID:" + seqId);
						dspResponseBean = RemoteCaller.getTransferStatusSBRequest(seqId);
					}
					// 如果操作成功
					if (dspResponseBean == null) {
						log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
						return "出现未知情况!扣除款项！请联系在线客服!";
					} else if ("000".equals(dspResponseBean.getInfo())) {
						transferService.addTransferforSB(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
						String sbloginname = "e68_" + loginname;
						transferService.updateSbcoupon(sbloginname);
						log.info("转账成功！" + seqId + "***账户:" + loginname);
						return null;
					} else {
						log.info(loginname + "转账错误信息" + dspResponseBean.getInfo());
						// 转账失败 退还额度
						/*Creditlogs creditLog = new Creditlogs();
						creditLog.setLoginname(loginname);
						creditLog.setType(CreditChangeType.TRANSFER_SBIN.getCode());
						creditLog.setCredit(newCredit);
						creditLog.setRemit(remit);
						creditLog.setNewCredit(newCredit + remit);
						creditLog.setRemark("SB转账错误!自动退还:" + seqId + "订单");
						creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
						transferService.save(creditLog);*/
						// 打印日志
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info = "SB转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
						actionlog.setRemark(info);
						transferService.save(actionlog);
						log.info(info);
						// 退还款项
//						transferService.updateUserCreditSql(user1, remit);
						log.info("转账状态错误处理成功:" + dspResponseBean.getInfo() + "***" + loginname);
						return "转账状态错误:" + dspResponseBean.getInfo();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "出现未知情况!扣除款项！请联系在线客服!";
				}
			} else {
				log.info("SB转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}
	
	/**
	 * SB体育 转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferOutSB(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferOutforSB(seqId, loginname, remit);
	}
	
	/**
	 * EBet 转入
	 * @param transferService
	 * @param loginname
	 * @param remit
	 * @param type
	 * @param platform
	 * @param catalog
	 * @param transferID
	 * @return
	 */
	public synchronized String ebetTransferIn(TransferService transferService, String loginname, Integer remit, String type, String platform, String catalog, String transferID) {
		// 转账前先判断额度是否可以获取到
		try {
			Double balance = EBetUtil.getBalance(loginname, "EBET");
			Object output = (loginname != null ? balance == null ? null : balance.toString() : null);
			if (output == null || output.equals("")) {
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
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		double oldCredit = Math.abs(user.getCredit());
		String msg = transferService.transferInforEbetIn(transferID, loginname, remit.doubleValue(), null, null);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info(loginname + " EBET转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					EBetResp ebetResp = EBetUtil.transfer(loginname, transferID, remit, platform, catalog, type);
					// 如果操作成功
					if (ebetResp != null) {
						if (ebetResp.getCode().equals(EBetCode.success.getCode())) {
							EBetResp transferConfirm = EBetUtil.transferConfirm(loginname, transferID);
							if (transferConfirm != null) {
								if (transferConfirm.getCode().equals(EBetCode.success.getCode())) {
									transferService.addTransferforEbet(Long.parseLong(transferID), loginname, oldCredit, Double.valueOf(remit.toString()), Constants.IN, Constants.FAIL, "", "预备转账");
									log.info("转账成功！" + transferID + "***账户:" + loginname);
									return null;
								} else {
									// 转账失败 退还额度
									/*
									Creditlogs creditLog = new Creditlogs();
									creditLog.setLoginname(loginname);
									creditLog.setType(CreditChangeType.TRANSFER_SBIN.getCode());
									creditLog.setCredit(newCredit);
									creditLog.setRemit(Double.valueOf(remit));
									creditLog.setNewCredit(newCredit + remit);
									creditLog.setRemark("EBET转账错误!自动退还:" + transferID + "订单");
									creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
									transferService.save(creditLog);
									*/
									// 打印日志
									Actionlogs actionlog = new Actionlogs();
									actionlog.setLoginname(loginname);
									actionlog.setRemark(null);
									actionlog.setCreatetime(DateUtil.now());
									actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
									String info = "EBET转账错误!改变前订单:" + transferID + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + transferID + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
									actionlog.setRemark(info);
									transferService.save(actionlog);
									log.info(info);
									// 退还款项
									//transferService.updateUserCreditSql(user1,Double.valueOf(remit));
									log.info("转账状态错误处理成功!！单号:" + transferID + "***账户:" + loginname + "***" + transferConfirm.getMsg());
									return "转账失败!" + transferConfirm.getMsg();
								}
							} else {
								log.info("出现未知情况!扣除款项！单号:" + transferID + "***账户:" + loginname);
								return "出现未知情况!扣除款项！请联系在线客服!";
							}
						} else {
							log.info("出现未知情况!扣除款项！单号:" + transferID + "***账户:" + loginname + "***" + ebetResp.getMsg());
							return "出现未知情况!扣除款项！请联系在线客服!" + ebetResp.getMsg();
						}
					} else {
						log.info("出现未知情况!扣除款项！单号:" + transferID + "***账户:" + loginname);
						return "出现未知情况!扣除款项！请联系在线客服!";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("出现未知情况!扣除款项！单号:" + transferID + "***账户:" + loginname);
					return "网络繁忙！请稍等再试";
				}
			} else {
				log.info("EBET转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}
	
	/**
	 * EBet 转出
	 * @param transferService
	 * @param loginname
	 * @param credit
	 * @param type
	 * @param platform
	 * @param catalog
	 * @param transferID
	 * @return
	 */
	public synchronized String transfer4EbetOutVerifyBet(TransferService transferService, String loginname, Integer credit, String type, String platform, String catalog, String transferID){
		return transferService.transfer4EbetOutVerifyBet(loginname, transferID, credit);
	}
	
	/**
	 * DT 转入
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @param remark
	 * @return
	 */
	public synchronized String transferDtAndSelfYouHuiIn(TransferService transferService, String seqId, String loginname, Double remit, String remark) {
		// 转账前先判断额度是否可以获取到
		Object output;
		try {
			output = (loginname != null ? DtUtil.getamount(loginname) : null);
			if (output == null || output.equals("")) {
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
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		//负盈利反赠处理
		Double dtBalance = Double.parseDouble(output.toString());
		DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "dt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = transferService.findByCriteria(c);
		if(dtBalance >= 5.00 && losePromoList.size() > 0){
			//如果存在已领取的负盈利反赠，不允许转入
			log.info("玩家已领取负盈利反赠，且余额大于5元，不允许转入" +loginname);
			return "您已领取救援金且DT余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的负盈利反赠更新为已处理
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			transferService.update(losePromo);
		}
		//周周回馈处理
		DetachedCriteria dc=DetachedCriteria.forClass(WeekSent.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("platform", "dt"));
		dc.add(Restrictions.eq("status", "1"));
		List<WeekSent> weekSentList = transferService.findByCriteria(dc);
		if(dtBalance > 5.00 && weekSentList.size() > 0){
			//如果存在已领取的周周回馈，不允许转入
			log.info("玩家已领取周周回馈，且余额大于5元，不允许转入" +loginname);
			return "您已领取周周回馈且dt余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的周周回馈更新为已处理
		for (WeekSent ws : weekSentList) {
			ws.setStatus("2");
			transferService.update(ws);
		}
		
		if(dtBalance >=1 && dtBalance<Constants.MAXIMUMDTOUT){
			DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
			dc8.add(Restrictions.eq("loginname", loginname)) ;
			dc8.add(Restrictions.eq("target", "dt")) ;
			dc8.addOrder(Order.desc("createtime"));
			List<Transfer> transfers = transferService.findByCriteria(dc8, 0, 2) ;
			if(null != transfers && transfers.size()>0){
				Transfer transfer = transfers.get(0);
				if(null != transfer){
					if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是app下载彩金的转账记录
						return "您正在使用app下载彩金，DT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					}   
					if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
						return "您正在使用体验金，DT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					}
				}
			}
		}
		double oldCredit = Math.abs(user.getCredit());
		String msg = transferService.transferInforDtIn(seqId, loginname, remit,null, remark);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info(loginname + " DT转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					// 转进DT成功
					String deposit = DtUtil.withdrawordeposit(loginname, remit);
					if (null != deposit  && deposit.equals("success")) {
						transferService.addTransferforDt(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
						log.info("转账成功！" + seqId + "***账户:" + loginname);
						return null;
					} else {
						// 打印日志
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info = "DT转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
						actionlog.setRemark(info);
						transferService.save(actionlog);
						log.info(info);
						log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
						return "出现未知情况!扣除款项！请联系在线客服!";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("DT出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "出现未知情况!扣除款项！请联系在线客服!";
				}
			} else {
				log.info("DT转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}
	
	/**
	 * DT 转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferDtAndSelfYouHuiOut(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferDtAndSelfYouHuiOut(seqId, loginname, remit);
	}
	
	/**
	 * TTG  转入
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @param remark
	 * @return
	 */
	public synchronized String transferTtAndSelfYouHuiIn(TransferService transferService, String seqId, String loginname, Double remit, String remark) {
		// 转账前先判断额度是否可以获取到
		Object output;
		try {
			output = (loginname != null ? PtUtil1.getPlayerAccount(loginname) : null);
			if (output == null || output.equals("")) {
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
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		//负盈利反赠处理
		Double ptBalance = Double.parseDouble(output.toString());
		DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "ttg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = transferService.findByCriteria(c);
		if(ptBalance >= 5.00 && losePromoList.size() > 0){
			//如果存在已领取的负盈利反赠，不允许转入
			log.info("玩家已领取负盈利反赠，且余额大于5元，不允许转入" +loginname);
			return "您已领取救援金且TTG余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的负盈利反赠更新为已处理
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			transferService.update(losePromo);
		}
		
		//周周回馈处理
		DetachedCriteria dc=DetachedCriteria.forClass(WeekSent.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("platform", "ttg"));
		dc.add(Restrictions.eq("status", "1"));
		List<WeekSent> weekSentList = transferService.findByCriteria(dc);
		if(ptBalance > 5.00 && weekSentList.size() > 0){
			//如果存在已领取的周周回馈，不允许转入
			log.info("玩家已领取周周回馈，且余额大于5元，不允许转入" +loginname);
			return "您已领取周周回馈且TTG余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的周周回馈更新为已处理
		for (WeekSent ws : weekSentList) {
			ws.setStatus("2");
			transferService.update(ws);
		}
		
		//体验金
		if(ptBalance >=1 && ptBalance<Constants.MAXIMUMDTOUT){
			DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
			dc8.add(Restrictions.eq("loginname", loginname)) ;
			dc8.add(Restrictions.eq("target", "ttg")) ;
			dc8.addOrder(Order.desc("createtime"));
			List<Transfer> transfers = transferService.findByCriteria(dc8, 0, 10) ;
			if(null != transfers && transfers.size()>0){
				Transfer transfer = transfers.get(0);
				if(null != transfer){
					if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是app下载彩金的转账记录
						return "您正在使用app下载彩金，TTG余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					}   
					if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
						return "您正在使用体验金，TTG余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					}
				}
			}
			
		}
		
		double oldCredit = Math.abs(user.getCredit());
		String msg = transferService.transferInforTtgIn(seqId, loginname, remit,null, remark);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info(loginname + " TT转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					// 转进TT成功
					Boolean deposit = PtUtil1.addPlayerAccountPraper(loginname, remit);
					if (null != deposit  && deposit) {
						transferService.addTransferforTt(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
			//			transferService.updateUserShippingcodeTtSql(user1);
						log.info("转账成功！" + seqId + "***账户:" + loginname);
						return null;
					} else {
						/*// 转账失败 退还额度
						
						Creditlogs creditLog = new Creditlogs();
						creditLog.setLoginname(loginname);
						creditLog.setType(CreditChangeType.TRANSFER_TTIN.getCode());
						creditLog.setCredit(newCredit);
						creditLog.setRemit(remit);
						creditLog.setNewCredit(newCredit + remit);
						creditLog.setRemark("TT转账错误!自动退还:" + seqId + "订单");
						creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
						transferService.save(creditLog);*/
						
						// 打印日志
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info = "TTG转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
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
					log.info("TTG出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "出现未知情况!扣除款项！请联系在线客服!";
				}
			} else {
				log.info("TTG转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}
	
	/**
	 * TTG 转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferTtAndSelfYouHuiOut(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferTtAndSelfYouHuiOut(seqId, loginname, remit);
	}
	
	/**
	 * QT 转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @param remark
	 * @return
	 */
	public synchronized String transferQtAndSelfYouHuiIn(TransferService transferService, String seqId, String loginname, Double remit, String remark) {
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
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
		if(qtBalance >=1 && qtBalance<Constants.MAXIMUMDTOUT){
			DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
			dc8.add(Restrictions.eq("loginname", loginname)) ;
			dc8.add(Restrictions.eq("target", "qt"));
			dc8.addOrder(Order.desc("createtime"));
			List<Transfer> transfers = transferService.findByCriteria(dc8, 0, 10) ;
			if(null != transfers && transfers.size()>0){
				Transfer transfer = transfers.get(0);
				if(null != transfer){
					if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是app下载彩金的转账记录
						return "您正在使用app下载彩金，QT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					}   
					if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
						return "您正在使用体验金，QT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
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
	}
	
	/**
	 * QT 转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferQtAndSelfYouHuiOut(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferQtAndSelfYouHuiOut(seqId, loginname, remit);
	}
	
	/**
	 * NT 转入
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @param remark
	 * @return
	 */
	public synchronized String transferToNTJudge(TransferService transferService, String seqId, String loginname, Double remit, String remark) {
		// 转账前先判断额度是否可以获取到
		Object output;
		JSONObject qb;
		try {
			output = (loginname != null ? NTUtils.getNTMoney(loginname) : null);
			qb = JSONObject.fromObject(output.toString());
			if (!qb.getBoolean("result")){
				log.error("转入NT时失败,无法获取玩家额度");
				return "系统繁忙!暂时无法获取您的额度";
			}
			if (output == null || output.equals("")) {
				log.info(loginname + "获取额度超时!系统繁忙!");
				return "系统繁忙!请稍后再试";
			}
			if(!NumberUtils.isNumber(qb.getString("balance"))){
				log.info(loginname + " 金额不是数字!"+output.toString());
				return "系统繁忙!请稍后再试";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(loginname + "获取额度超时!系统繁忙!" + e.toString());
			return "系统繁忙!请稍后再试";
		}
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		Double ptBalance = qb.getDouble("balance");
		/* TODO NT暂无负盈利反赠
		//负盈利反赠处理
		DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "nt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = transferService.findByCriteria(c);
		if(ptBalance >= 5.00 && losePromoList.size() > 0){
			//如果存在已领取的负盈利反赠，不允许转入
			log.info("玩家已领取负盈利反赠，且余额大于5元，不允许转入" +loginname);
			return "您已领取救援金且NT余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的负盈利反赠更新为已处理
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			transferService.update(losePromo);
		}*/
		
		/* TODO NT暂无周周回馈
		//周周回馈处理
		DetachedCriteria dc=DetachedCriteria.forClass(WeekSent.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("platform", "nt"));
		dc.add(Restrictions.eq("status", "1"));
		List<WeekSent> weekSentList = transferService.findByCriteria(dc);
		if(ptBalance > 5.00 && weekSentList.size() > 0){
			//如果存在已领取的周周回馈，不允许转入
			log.info("玩家已领取周周回馈，且余额大于5元，不允许转入" +loginname);
			return "您已领取周周回馈且NT余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的周周回馈更新为已处理
		for (WeekSent ws : weekSentList) {
			ws.setStatus("2");
			transferService.update(ws);
		}*/
		
		if(ptBalance >=1 && ptBalance<Constants.MAXIMUMDTOUT){
			DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
			dc8.add(Restrictions.eq("loginname", loginname)) ;
			dc8.add(Restrictions.eq("target", "nt")) ;
			dc8.addOrder(Order.desc("createtime"));
			List<Transfer> transfers = transferService.findByCriteria(dc8, 0, 10) ;
			if(null != transfers && transfers.size()>0){
				Transfer transfer = transfers.get(0);
				if(null != transfer){
					if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是app下载彩金的转账记录
						return "您正在使用app下载彩金，NT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					}   
					if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
						return "您正在使用体验金，NT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					}
				}
			}
		}
		double oldCredit = Math.abs(user.getCredit());
		String msg = transferService.transferToNTJudge(seqId, loginname, remit,null, remark);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info(loginname + " NT转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					// 开始转账至NT
					JSONObject monj = JSONObject.fromObject(NTUtils.changeMoney(loginname, remit));
					if (monj.getBoolean("result")){ //result为true时转账成功
						transferService.addTransferforNT(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
						log.info("转账成功！" + seqId + "***账户:" + loginname);
						return null;
					} else { //反之转账失败
						/* 记录失败日志 */
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info = "NT转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
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
					log.info("NT出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "出现未知情况!扣除款项！请联系在线客服!";
				}
			} else {
				log.info("NT转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}
	
	/**
	 * NT 转出
	 * @param transferService
	 * @param transferID
	 * @param loginname
	 * @param credit
	 * @return
	 */
	public synchronized String transferFromNTJudge(TransferService transferService, String transferID, String loginname, Double credit){
		return transferService.transferFromNTJudge(transferID, loginname, credit);
	}


	/**
	 * EBetApp 转入
	 *
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferToEBetApp(TransferService transferService, String seqId, String loginname, Double remit) {
		final String type = "IN";
		// 转账前先判断额度是否可以获取到
		Double balance;
		String platform = "EBetApp";
		String platformCode = "EBetApp".toLowerCase();
		try {
			balance = (loginname != null ? EBetAppUtil.getBalance(loginname) : null);
		} catch (Exception e) {
			e.printStackTrace();
			return "系统繁忙!请稍后再试";
		}
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		//负盈利反赠处理
		boolean hasGetMinusBonus = minusBonusCheck(transferService, platformCode, loginname, balance, 5.00);
		if (hasGetMinusBonus) {
			//如果存在已领取的负盈利反赠，不允许转入
			log.info("玩家已领取负盈利反赠，且余额大于5元，不允许转入" + loginname);
			return "您已领取救援金且" + platform + "余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}

		//周周回馈处理
		boolean hasGetWeeklyBonus = weeklyBonusCheck(transferService, platformCode, loginname, balance, 5.00);
		if (hasGetWeeklyBonus) {
			//如果存在已领取的周周回馈，不允许转入
			log.info("玩家已领取周周回馈，且余额大于5元，不允许转入" + loginname);
			return "您已领取周周回馈且" + platform + "余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}

		//体验金确认处理
		if (balance >= 1 && balance < 100) {
			boolean useSelfHelpBonus = selfHelpBonusCheck(transferService, loginname);
			if (useSelfHelpBonus) {
				return "您正在使用体验金，" + platform + "余额大于等于100或者小于1的时候才能进行户内转账";
			}
		}

		//充值
		double oldCredit = Math.abs(user.getCredit());
		//dao进行记录
		String msg = transferService.transferToEBetApp(seqId, loginname, remit, null, type);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info("转账到EBetApp , 转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					// 开始转账至EBetApp
					boolean isTransferSuccess = EBetAppUtil.transfer(loginname, seqId, remit, platformCode, type);
					if (isTransferSuccess) { //result为true时转账成功
						transferService.addTransferforEBetApp(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
						log.info("转账成功！" + seqId + "***账户:" + loginname);
						return null;
					} else { //反之转账失败
						/* 记录失败日志 */
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info = platform + "转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
						actionlog.setRemark(info);
						transferService.save(actionlog);
						log.info(info);
						// 退还款项
						//transferService.updateUserCreditSql(user1, remit);
						log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
						return "出现未知情况!扣除款项！请联系在线客服!";
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info(platform + "出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "出现未知情况!扣除款项！请联系在线客服!";
				}
			} else {
				log.info(platform + "转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}

	//负盈利反赠处理
	private boolean minusBonusCheck(TransferService transferService, String platform, String loginname, double balance, double minLimit) {
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", platform));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = transferService.findByCriteria(c);
		if (balance >= minLimit && losePromoList.size() > 0) {
			return true;
		} else {
			//将已领取的负盈利反赠更新为已处理
			for (LosePromo losePromo : losePromoList) {
				losePromo.setStatus("2");
				transferService.update(losePromo);
			}
			return false;
		}
	}

	private boolean weeklyBonusCheck(TransferService transferService, String platform, String loginname, double balance, double minLimit) {
		DetachedCriteria dc = DetachedCriteria.forClass(WeekSent.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("platform", platform));
		dc.add(Restrictions.eq("status", "1"));
		List<WeekSent> weekSentList = transferService.findByCriteria(dc);
		if (balance > minLimit && weekSentList.size() > 0) {
			return true;
		} else {
			//将已领取的周周回馈更新为已处理
			for (WeekSent ws : weekSentList) {
				ws.setStatus("2");
				transferService.update(ws);
			}
			return false;
		}

	}

	private boolean selfHelpBonusCheck(TransferService transferService, String loginname) {
		DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
		dc8.add(Restrictions.eq("loginname", loginname));
		dc8.add(Restrictions.eq("target", "nt"));
		dc8.addOrder(Order.desc("createtime"));
		List<Transfer> transfers = transferService.findByCriteria(dc8, 0, 10);
		if (null != transfers && transfers.size() > 0) {
			Transfer transfer = transfers.get(0);
			if (null != transfer) {
				//最后一笔是体验金的转账记录
				return transfer.getRemark().contains("自助优惠");
			}
		}
		return false;
	}

	/**
	 * BEetApp 转出
	 *
	 * @param transferService
	 * @param transferID
	 * @param loginname
	 * @param credit
	 * @return
	 */

	public synchronized String transferFromEBetApp(TransferService transferService, String transferID, String loginname, Double credit) {
		return transferService.transferFromEBetApp(transferID, loginname, credit);
	}
	
	/**
	 * 提款
	 * @param proposalService
	 * @param proposer
	 * @param loginname
	 * @param pwd
	 * @param title
	 * @param from
	 * @param money
	 * @param accountName
	 * @param accountNo
	 * @param accountType
	 * @param bank
	 * @param accountCity
	 * @param bankAddress
	 * @param email
	 * @param phone
	 * @param ip
	 * @param remark
	 * @param msflag
	 * @param user
	 * @param splitAmount
	 * @param tkType
	 * @return
	 */
	public synchronized String withdraw(ProposalService proposalService, String proposer, String loginname, String pwd,
			String title, String from, Double money, String accountName, String accountNo, String accountType,
			String bank, String accountCity, String bankAddress, String email, String phone, String ip, String remark,
			String msflag, Users user, Double splitAmount , String tkType) {
		//首先设定玩家提款的都为秒提
        msflag = "1" ;
        remark = "";
		//秒提对象过滤
		if(!bank.equals("支付宝") && (0==user.getLevel() || (1==user.getLevel() && 2==user.getWarnflag()))){
			msflag = "0" ;
			remark = VipLevel.getText(user.getLevel())+WarnLevel.getText(user.getWarnflag());
		}
		if("AGENT".equals(user.getRole())){
			msflag = "0" ;
			remark = "代理提款!";
		}
		if(bank!=null && ("民生银行".equals(bank.trim()) || "上海浦东银行".equals(bank.trim())) || "深圳发展银行".equals(bank.trim())){
			msflag = "0" ;
			remark = "非秒提银行";
		}
		if((money!=null && money>200000)){
			msflag = "0" ;
			remark = "200000以上只能5分钟；";
		}
		if(user.getLevel() >= VipLevel.XINGJUN.getCode() && user.getWarnflag().equals(WarnLevel.WEIXIAN.getCode()) && money > splitAmount){
			msflag = "0" ;
			remark = VipLevel.getText(user.getLevel()) + WarnLevel.getText(user.getWarnflag()) + "提款"+money;
		}
		if(user.getLevel() < VipLevel.XINGJUN.getCode() && money > splitAmount){
			msflag = "0" ;
			remark = VipLevel.getText(user.getLevel())+"提款"+money;
		}
		
		Const cons = (Const) proposalService.get(Const.class, "民生银行秒付");
		if(cons!=null && cons.getValue().equals("0")){
			msflag = "0";
			remark += "秒付关闭";
		}
		if(money<100){
			Const xiaoe = (Const) proposalService.get(Const.class, "小额提款");
			if(xiaoe!=null && xiaoe.getValue().equals("0")){
				return "小额提款通道正在维护";
			}
		}
		
		/*先取消优惠限制秒提*/
		Userstatus userstatus =(Userstatus) proposalService.get(Userstatus.class, loginname);
		if(userstatus!=null && userstatus.getTouzhuflag()==1){
			msflag = "0";
			remark += "财务控制特殊玩家";
		}
		if(!proposalService.existHadFinishedProposal(loginname, ProposalType.CASHOUT)){
			msflag = "0";
			remark += "首提";
		}
		
		String msg;
		if(user.getRole().equals("AGENT") && user.getWarnflag()==2){
			msg = "请您联系代理专员，谢谢！";
			return msg ;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String commMsFlag = msflag ;
		Double gameTolMoney = 0.0 ;
		if(user.getRole().equals("MONEY_CUSTOMER")){
			//开关 、 现在总资产（保罗游戏金额）-最后一次存款时的总资产     获取余额开关
			//获取手机号
			DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
			dc.add(Restrictions.eq("typeNo", "type103"));
			dc.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> configs = proposalService.findByCriteria(dc);
			Date startTime = null ;
			Date endTime = new Date() ;
			if(null != configs && configs.size()>0){
				Double margin = Double.valueOf(configs.get(0).getValue());
				
				Double depAmount = proposalService.getDepositAmount(loginname, null, endTime);//存款额度
				Double withdrawl = proposalService.getWithdrawAmount(loginname, null, endTime);
				depAmount = depAmount == null ?0.0 : depAmount ;
				withdrawl = withdrawl == null ?0.0 : withdrawl ;
				log.info(loginname+"-->depAmount:"+depAmount+"withdrawl:"+withdrawl);
				Double chae = withdrawl - depAmount  ; 
				if(chae < 0){
					if(money + chae >= 0){
						msflag = "0";
					}else{
						if(!(remark.contains("非秒提银行") || (userstatus!=null && userstatus.getTouzhuflag()==1) || (cons!=null && cons.getValue().equals("0")))){
							msflag = "1";
						}
					}
					remark +="存提差"+chae+";";
				}else{
					//查询累计存提款差额
					Double youhuiAmount = proposalService.getYouHuiAmount(loginname) ; //优惠额度
					Double gameProfit = proposalService.getGameProfit(loginname, null) ; //游戏输赢
					if(0.0 == gameProfit){
						msflag = "0" ; //改为5分钟
						remark += "没有游戏输赢记录";
					}
					
					youhuiAmount = youhuiAmount == null ? 0.0 : youhuiAmount ;
					gameProfit = -1*(gameProfit == null ? 0.0 : gameProfit) ;
					if(money + chae >=  + youhuiAmount	+ gameProfit){
						msflag = "0";
					}
					remark +="存提差"+chae+";优惠"+youhuiAmount+"平台输赢"+gameProfit+"";
				}
				
				remark += commMsFlag.equals("1")?"‘本秒’":"‘本5’";
				log.info("提款限制---------------》"+remark);
			}
		}
		
		if (msflag.equals("1") && money > splitAmount && user.getRole().equals("MONEY_CUSTOMER")) {
			msg = proposalService.addCashoutNew(proposer, loginname, pwd, title, from, money, accountName, accountNo,
					accountType, bank, accountCity, bankAddress, email, phone, ip, remark, msflag,gameTolMoney);
		} else {
			if(!"liveall".equals(tkType) && user.getRole().equals("AGENT")){
				msg = proposalService.addCashoutForAgentSlot(proposer, loginname, pwd, title, from, money, accountName, accountNo,
						accountType, bank, accountCity, bankAddress, email, phone, ip, "代理老虎机佣金提款", msflag);
			}else{
				if(StringUtils.isNotBlank(tkType) && !tkType.equals("liveall") && user.getRole().equals("AGENT")){
					return "提款类型不合法";
				}
				if(StringUtils.isNotBlank(tkType) && tkType.equals("liveall") && user.getRole().equals("AGENT")){
					int date = Calendar.getInstance().get(Calendar.DATE);
					DetachedCriteria dc = DetachedCriteria.forClass(AgentVip.class);
					dc.add(Restrictions.eq("id.agent", user.getLoginname())) ;
					dc.addOrder(Order.desc("createtime")) ;
					List<AgentVip> vips = proposalService.findByCriteria(dc);
					if(!(date>=1 && date<=5)){
						if(null == vips || vips.size()==0){
							msg = "因代理等级不够不能提款，请多多努力";
							return msg ;
						}
					}
					if(!(date>=1 && date<=5) && vips.get(0).getLevel()==0){
						msg = "代理提款其他类型的佣金需要在每个月的1号到5号";
						return msg ;
					}
					remark = "代理真人佣金提款";
				}
				msg = proposalService.addCashout(proposer, loginname, pwd, title, from, money, accountName, accountNo,
						accountType, bank, accountCity, bankAddress, email, phone, ip, remark, msflag,gameTolMoney);
				
			}
		}
		if (msg == null) {
			Users user1 =(Users)proposalService.get(Users.class, loginname) ;
			if(user.getCredit().equals(user1.getCredit()) && !(null != tkType && tkType.equals("slotmachine") && user.getRole().equals("AGENT"))){
				String info="额度错误！危险提款前:"+user.getCredit()+"提款后:"+user1.getCredit();
				log.info(info);
				user1.setWarnflag(2);
				proposalService.update(user1);
			}else{
				//提款成功，发送短信 进行提示  先判断玩家是否开通这个服务
				/*String str= user1.getAddress();
				if(!StringUtil.isEmpty(str)&&str.contains("5")){
				SendPhoneMsgUtil spm = new SendPhoneMsgUtil();
				String userphone=user1.getPhone();
				if(!StringUtil.isEmpty(userphone) ){
				try {
					userphone= AESUtil.aesDecrypt(userphone, AESUtil.KEY);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				spm.setPhone(userphone);
				String strName = user1.getLoginname().substring(0, 2)+"***";
				spm.setMsg("亲爱的玩家"+strName+"，您在"+dfh.utils.sendemail.DateUtil.getNow()+"进行提款操作，若有疑问请及时联系在线客服，我们将为您处理，谢谢。");
				spm.start();
				}
				}*/
			
			}
		}
		return msg;
	}
	
	/**
	 * 洗码
	 * @param gameinfoService
	 * @param user
	 * @param endTime
	 * @param startTime
	 * @param platform
	 * @return
	 * @throws Exception
	 */
	public synchronized String selfXima(IGGameinfoService gameinfoService ,Users user, Date endTime, Date startTime , String platform) throws Exception{
		return gameinfoService.execXima(user, endTime, startTime ,platform);
	}
	
	public synchronized String addPayXbWx(AnnouncementService announcementService ,String orderid, Double money, String loginname,String flag){
		return announcementService.addPayXbWxzf(orderid, money, loginname,flag);
	}

	public synchronized String dinpaySyncUpdateCredit(AnnouncementService announcementService, String billno, Double money, String loginname, String card_code, String merchant_code){
		return announcementService.addPayorder4DCard(billno, money, loginname, card_code, merchant_code, "0") ;
	}
	
	public synchronized String addPayKdZf(AnnouncementService announcementService ,String orderid, Double money, String loginname,String flag){
		return announcementService.addPayKdZf(orderid, money, loginname,flag);
	}
	
	public synchronized String addPayHhbZf(AnnouncementService announcementService ,String orderid, Double money, String loginname,String flag){
		return announcementService.addPayHhbZf(orderid, money, loginname,flag);
	}
	
	public synchronized String addPayKdWxZf(AnnouncementService announcementService ,String orderid, Double money, String loginname,String flag){
		return announcementService.addPayKdWxZf(orderid, money, loginname,flag);
	}
	
	
	/***
	 * 口袋微信支付2
	 * @param announcementService
	 * @param orderid
	 * @param money
	 * @param loginname
	 * @param flag
	 * @return
	 */
	public synchronized String addPayKdWxZf2(AnnouncementService announcementService ,String orderid, Double money, String loginname,String flag){
		return announcementService.addPayKdWxZf2(orderid, money, loginname,flag);
	}
	
	/***
	 * 口袋支付宝2 and 口袋微信支付1 and 口袋微信支付2 and 口袋微信支付3
	 * @param announcementService
	 * @param orderid
	 * @param money
	 * @param loginname
	 * @param flag
	 * @return
	 */
	public synchronized String addPayKdWxZfs(AnnouncementService announcementService ,String orderid, Double money,String payName, String loginname,String flag){
		return announcementService.addPayKdWxZfs(orderid, money,payName, loginname,flag);
	}
	
	//汇付宝微信支付
	public synchronized String addPayHhbWxZf(AnnouncementService announcementService ,String orderid, Double money, String loginname,String flag){
		return announcementService.addPayHhbWxZf(orderid, money, loginname,flag);
	}
	//聚宝支付宝支付
	public synchronized String addPayJubZfb(AnnouncementService announcementService ,String orderid, Double money, String loginname,String flag){
		return announcementService.addPayJubZfb(orderid, money, loginname,flag);
	}
	
	//迅联宝微信支付
	public synchronized String addPayXlb(AnnouncementService announcementService ,String orderid, Double money, String loginname,String flag){
		return announcementService.addPayXlb(orderid, money, loginname,flag);
	}
	
	//迅联宝网银支付
	public synchronized String addPayXlbWy(AnnouncementService announcementService ,String orderid, Double money, String loginname,String flag){
		return announcementService.addPayXlbWy(orderid, money, loginname,flag);
	}
	
	//优付支付宝 and 微信
	public synchronized String addPayYfZf(AnnouncementService announcementService ,String orderid, Double money, String payName, String loginname,String flag){
		return announcementService.addPayYfZf(orderid, money, payName, loginname,flag);
	}
	
	//银宝支付宝
	public synchronized String addPayYbZfb(AnnouncementService announcementService ,String orderid, Double money, String payName,String loginname,String flag){
		return announcementService.addPayYbZfb(orderid, money,payName,  loginname,flag);
	}
	
	/**
	 * 代理老虎机金额转入绑定游戏账号
	 * @param transferService
	 * @param loginnameAgent
	 * @param credit
	 * @param seqId
	 * @return
	 * */
	public synchronized String transferInGameUserFromAgentTiger(
			TransferService transferService, String loginnameAgent,
			Double credit, String transIDAgent, String transIDGame, String password) {
		
		return transferService.transferInGameUserFromAgentTiger(loginnameAgent,
			credit, transIDAgent, transIDGame, password);
	}
	
	/**
	 * E68游戏币转入NTwo Live
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	
	/**
	 * NTwo转入E68
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferOutNTwo(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferOutNTwo(seqId, loginname, remit);
	}


	public AutoYouHuiVo transferDtCoup(TransferService transferService,
			String seqId, String loginname, String couponType, Double remit,
			String couponCode, String ip) {
		AutoYouHuiVo vo= transferService.transferInforCouponDT(seqId,loginname,couponType, remit,couponCode,ip);
		if(null!=vo&&vo.getMessage().equals("success")&&null!=vo.getGiftMoney()){
			String deposit = DtUtil.withdrawordeposit(loginname, vo.getGiftMoney());
			if (null != deposit  && deposit.equals("success")) {
				log.info("DT存送优惠券转账成功！" + seqId + "***账户:" + loginname);
			} else {
				log.info("DT存送优惠券转账失败！" + seqId + "***账户:" + loginname);
			}
		}
		return vo;
	}
	
	public synchronized Bankinfo getNewdeposit(TransferService transferService,SlaveService slaveService,CustomerService cs,String loginname, String banktype, String uaccountname,String ubankname, String ubankno, Double amout, boolean force,String depositId) {
		Double dpRange=0.0;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		 
		List list =slaveService.checkTotalExist(loginname);
		if(list !=null && !list.isEmpty()){
			String updateSql = "UPDATE total_deposits SET  alldeposit=:deposit ,updatetime= now() WHERE loginname =:loginname";
			Object[] obj = (Object[]) list.get(0);
			Double alldeposit = (Double) obj[0];
			Date updatetime = DateUtil.parseDateForStandard(obj[1].toString());
			dpRange=alldeposit;
			Calendar c = Calendar.getInstance();  
	        c.add(Calendar.DATE, -1);  
	        Date day = c.getTime();
			//超过一天才去查询存款
			if(updatetime.getTime() < day.getTime()){
				Double deposit = slaveService.getDeposit(loginname);
				params.put("deposit", deposit);
				
				//if (alldeposit<deposit) {
					cs.excuteSql(updateSql, params);
					dpRange=deposit;
				//}
			}
		}else{
			Double deposit = slaveService.getDeposit(loginname);
			String insertSql = "INSERT INTO total_deposits (loginname, alldeposit, createtime,updatetime) VALUES ( :loginname, :deposit, now(),now())";
			params.put("deposit", deposit);
			cs.excuteSql(insertSql, params);
			dpRange=deposit;
		}
		return transferService.createNewdeposit(loginname, banktype, uaccountname, ubankname, ubankno, amout, force,dpRange,depositId);
	}
	
	//获取微信转账小数额度
	public Double getWxZzQuota(TransferService transferService,String loginname,Double amout) {
		return transferService.amountNumber(String.valueOf(amout),loginname);
	}
	
	
	
	
	/**
	 * 微信秒存
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized Bankinfo createWeiXindeposit(TransferService transferService,String loginname, Double amout) {
		return transferService.createWeiXindeposit(loginname, amout);
	}
	
	/**
	 * 沙巴体育转出
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferOutforSbaTy(TransferService transferService , String seqId, String loginname, Double remit){
		return transferService.transferOutforSbaTy(seqId, loginname, remit);
	}
	
	/**
	 * 转入沙巴体育
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param remit
	 * @return
	 */
	public synchronized String transferInforSbaTy(TransferService transferService , String seqId, String loginname, Double remit){
		// 转账前先判断额度是否可以获取到
		try {
			Object output = (loginname != null ? ShaBaUtils.CheckUserBalance(loginname) : null);
			if (output == null || output.equals("")) {
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
		// 获取用户
		Users user = transferService.getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		double oldCredit = Math.abs(user.getCredit());
		String msg = transferService.transferInforSbaTyIn(seqId, loginname, remit);
		if (msg == null) {
			Users user1 = transferService.getUsers(loginname);
			remit = Math.abs(remit);
			double newCredit = Math.abs(user1.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info(loginname + " 沙巴转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					Boolean result = ShaBaUtils.FundTransfer(seqId,loginname, remit,Constants.DEPOSIT);
					// 如果操作成功
					if (!result) {
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info = "SBA转账错误!改变前订单:" + seqId + " 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit + " 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit + " 系统已经处理 请检查是有否错误";
						actionlog.setRemark(info);
						transferService.save(actionlog);
						log.info(info);
						// 退还款项
						//transferService.updateUserCreditSql(user1, remit);
						log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
						return "出现未知情况!扣除款项！请联系在线客服!";
					} else {
						transferService.addTransferforSba(Long.parseLong(seqId),loginname, oldCredit, remit, Constants.IN,Constants.FAIL, "", "转入成功");
						log.info("转账成功！" + seqId + "***账户:" + loginname);
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "出现未知情况!扣除款项！请联系在线客服!";
				}
			} else {
				log.info("SBA转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + user1.getCredit());
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}
	
	/**
	 * 中心钱包自助洗码
	 * @author CK
	 * @param gameinfoService
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public synchronized String execXimaSlot(IGGameinfoService gameinfoService ,String loginname) throws Exception{
		
		Date timeEnd = new Date();
		String message="";
		AutoXima ximaVo2 = gameinfoService.getSlotAutoXimaObject(loginname);
		
		if (ximaVo2.getJsonResult()==null) 
			return "无投注记录";

		List<AutoXima>ximaList=JsonUtil.jsonToBean(ximaVo2.getJsonResult());  //获取洗码信息
		
		for (int i = 0; i < ximaList.size(); i++) {
			message=message+gameinfoService.execXimaSlot(loginname, ximaList.get(i), timeEnd);
		}
		return message+"(可以通过点击【查询明细】按钮进行查询)";
	}

	/**
	 * 主账户转账到红包雨
	 * @param transferService
	 * @param seqId
	 * @param loginname
	 * @param amout
	 * @param string
	 * @return
	 */
	public String transferSelfToRedRain(TransferService transferService, String seqId, String loginname, Double amout,
			String remark)throws Exception {
		try {
			Users user = transferService.getUsers(loginname);
			if (user == null) {
				log.info("用户不存在!*****" + loginname);
				return "用户不存在!";
			}
			if (user.getCredit() < amout) {
				return "账户余额不足";
			}
			Double d=0.0;
			DetachedCriteria selfDc = DetachedCriteria.forClass(RaincouponRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("isused", 10));
			selfDc.add(Restrictions.gt("createtime",DateUtil.ntStart()));
			List<RaincouponRecord> records = transferService.findByCriteria(selfDc);
			for (RaincouponRecord raincouponRecord:records){
				d=d+raincouponRecord.getAmount().doubleValue();
			}
			DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
			dc.add(Restrictions.eq("typeNo", "type987"));
			dc.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> configs = transferService.findByCriteria(dc);
			if(configs==null||configs.size()<1){
				return "每日最高金额未设置";
			}
			SystemConfig systemConfig = configs.get(0);
			if(d+amout>Double.valueOf(systemConfig.getValue())){
				return "超过每日转出限额";
			}
			DecimalFormat df = new DecimalFormat(".00");
			RedRainWallet redRainWallet =(RedRainWallet) transferService.get(RedRainWallet.class, loginname);
			if(redRainWallet==null){
				return "没有红包雨账户";
			}
			Double amout1 = redRainWallet.getAmout();

			Double b=Double.valueOf(df.format(amout1+amout));
			redRainWallet.setAmout(b);
			redRainWallet.setRemark("主账户转入红包雨账户 "+amout1+"->"+redRainWallet.getAmout());
			Activity activity = new Activity();
			activity.setUserrole(loginname);
			activity.setRemark(redRainWallet.getRemark());
			activity.setCreateDate(new Date());
			activity.setActivityName("红包雨活动");
			RaincouponRecord record = new RaincouponRecord();
			record.setLoginname(loginname);
			record.setAmount(amout);
			record.setTypeid(0);//防止报错,无意义
			record.setCreatetime(new Date());
			record.setIsused(10);
			record.setCreatetime(new Date());
			record.setRemark(redRainWallet.getRemark());
			transferService.save(record);
			transferService.save(activity);
			transferService.update(redRainWallet);
			String msg = transferService.transferSelfToRedRain(seqId, user, amout, null, remark);
			if (msg != null) {
				throw new Exception();
				//return "转入失败";
			}
			return "转入成功";
		}catch (Exception e){
			e.printStackTrace();
			throw new Exception();
			//return "转入红包雨账户失败";
		}
	}
	

}
