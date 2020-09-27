package dfh.utils.transfer;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import dfh.model.Actionlogs;
import dfh.model.Creditlogs;
import dfh.model.Users;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.CreditChangeType;
import dfh.remote.ErrorCode;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.DepositPendingResponseBean;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class SynchrEaInUtil {

	private static Logger log = Logger.getLogger(SynchrEaInUtil.class);

	private static SynchrEaInUtil instance = null;

	private SynchrEaInUtil() {

	}

	public static SynchrEaInUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrEaInUtil();
		} else {
			return instance;
		}
	}

	public synchronized String transferIn(TransferService transferService, String seqId, String loginname, Double remit) {
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
		
//		synchronized (user.getId()) {
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
//		}
	}

}
