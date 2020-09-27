package dfh.utils.transfer;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import dfh.model.Actionlogs;
import dfh.model.Users;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.EBetCode;
import dfh.remote.bean.EBetResp;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.EBetUtil;

public class SynchrEbetInUtil {

	private static Logger log = Logger.getLogger(SynchrEbetInUtil.class);

	private static SynchrEbetInUtil instance = null;

	private SynchrEbetInUtil() {

	}

	public static SynchrEbetInUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrEbetInUtil();
		} else {
			return instance;
		}
	}

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
		
//		synchronized(user.getId()) {
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
//		}
	}

}
