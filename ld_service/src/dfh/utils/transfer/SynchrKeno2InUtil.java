package dfh.utils.transfer;

import org.apache.log4j.Logger;

import dfh.model.Actionlogs;
import dfh.model.Users;
import dfh.model.enums.ActionLogType;
import dfh.remote.DocumentParser;
import dfh.remote.bean.KenoResponseBean;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.Keno2Util;

public class SynchrKeno2InUtil {

	private static Logger log = Logger.getLogger(SynchrKeno2InUtil.class);

	private static SynchrKeno2InUtil instance = null;

	private SynchrKeno2InUtil() {

	}

	public static SynchrKeno2InUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrKeno2InUtil();
		} else {
			return instance;
		}
	}

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
//		synchronized (user.getId()) {
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
//		}
	}

}
