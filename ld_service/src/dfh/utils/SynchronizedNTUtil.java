package dfh.utils;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.Actionlogs;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.enums.ActionLogType;
import dfh.service.interfaces.AnnouncementService;
import dfh.service.interfaces.TransferService;

/**
 * 处理同步工具类
 * 每处理一个同步功能，需要一个Obj对象，一个带同步块的方法，同步块获取obj对象的锁
 *  
 */
public class SynchronizedNTUtil {
	
	private static Logger log = Logger.getLogger(SynchronizedNTUtil.class);

	private static final SynchronizedNTUtil instance = new SynchronizedNTUtil();
	
	/**
	 * 防止外部实例化
	 */
	private SynchronizedNTUtil(){};
	
	/**
	 * 单例
	 * @return
	 */
	public static SynchronizedNTUtil getInstance(){
		return instance;
	}
	
	/**
	 * IPS
	 */
	private Object IPSPayOrder = new Object();
	public String addIPSPayorder(AnnouncementService service, String billno, Double money, String loginname, String msg, String date, String type){
		synchronized (IPSPayOrder) {
			return service.addPayorder(billno, money, loginname, msg, date, type);
		}
	}
	
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
		
//		synchronized (user.getId()) {
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
			
			if(ptBalance >=1 && ptBalance<100){
				DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
				dc8.add(Restrictions.eq("loginname", loginname)) ;
				dc8.add(Restrictions.eq("target", "nt")) ;
				dc8.addOrder(Order.desc("createtime"));
				List<Transfer> transfers = transferService.findByCriteria(dc8, 0, 10) ;
				if(null != transfers && transfers.size()>0){
					Transfer transfer = transfers.get(0);
					if(null != transfer){
						if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
							return "您正在使用体验金，NT余额大于等于100或者小于1的时候才能进行户内转账";
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
//		}
	}
	
}
