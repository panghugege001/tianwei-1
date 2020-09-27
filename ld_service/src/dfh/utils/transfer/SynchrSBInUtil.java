package dfh.utils.transfer;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import dfh.model.Actionlogs;
import dfh.model.Users;
import dfh.model.enums.ActionLogType;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.DspResponseBean;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class SynchrSBInUtil {
	private static Logger log = Logger.getLogger(SynchrSBInUtil.class);
	
	private static SynchrSBInUtil instance = null;
	
	private SynchrSBInUtil(){
		
	}
	
	public static SynchrSBInUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrSBInUtil();
		} else {
			return instance;
		}
	}
	
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
		
//		synchronized (user.getId()) {
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
//							transferService.updateUserCreditSql(user1, remit);
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
//		}
	}
	
}
