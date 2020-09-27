package dfh.service.implementations;

import dfh.utils.DateUtil;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;

import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.exception.RemoteApiException;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.enums.CreditChangeType;
import dfh.remote.ErrorCode;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.DepositPendingResponseBean;
import dfh.remote.bean.WithdrawalConfirmationResponseBean;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;

public class TransferServiceImpl extends UniversalServiceImpl implements TransferService {

	private static Logger log = Logger.getLogger(TransferServiceImpl.class);
	private TradeDao tradeDao;
	private TransferDao transferDao;
	private UserDao userDao;

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public TransferDao getTransferDao() {
		return transferDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public void setTransferDao(TransferDao transferDao) {
		this.transferDao = transferDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	private void throwException() throws Exception {
		if (true)
			throw new Exception("手动抛出异常");
	}

	@Override
	public void updateSignamount() {


		transferDao.updateSignamount(0.0, DateUtil.getStartTime(), DateUtil.getEndTime());


	}
	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferIn(String transID, String loginname, Double remit) {
		remit = Math.abs(remit);
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferIn:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			Double localCredit = customer.getCredit();

			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				DepositPendingResponseBean depositPendingResponseBean = RemoteCaller.depositPendingRequest(customer.getLoginname(), remit, transID, customer.getAgcode());

				// 如果操作成功
				if (depositPendingResponseBean != null && depositPendingResponseBean.getStatus().equals(ErrorCode.SUCCESS.getCode())) {
					Transfer transfer = transferDao.addTransfer(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, depositPendingResponseBean.getPaymentid(), null);
					tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_IN.getCode(), transID, null);
					// 标记额度已被扣除
					creditReduce = true;

					try {
						RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), customer.getAgcode());
					} catch (Exception e) {
						log.warn("depositConfirmationResponse try again!");
						RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), depositPendingResponseBean
								.getAgcode());
					}

					transfer.setFlag(Constants.FLAG_TRUE);
					transfer.setRemark("转入成功");
					update(transfer);
				} else
					result = "转账状态错误:" + ErrorCode.getChText(depositPendingResponseBean.getStatus());
			}
		} catch (Exception e) {

			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce)
				result = "转账发生异常 :" + e.getMessage();
			else
				result = "系统繁忙，请重新尝试";

			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID IP，不明不错，因此回滚)
			if (e instanceof RemoteApiException)
				throw new GenericDfhRuntimeException(e.getMessage());
		}
		return result;
	}

	public String transferOut(String transID, String loginname, Double remit) {
		remit = Math.abs(remit);
		String result = null;
		log.info("begin to transferOut:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			Double localCredit = customer.getCredit();
			// 检查远程额度
			Double remoteCredit = RemoteCaller.queryCredit(loginname);

			log.info("remote credit:" + remoteCredit);
			if (remoteCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			}

			// 转账操作
			WithdrawalConfirmationResponseBean withdrawalConfirmationResponseBean = null;
			try {
				withdrawalConfirmationResponseBean = RemoteCaller.withdrawPendingRequest(customer.getLoginname(), remit, transID, customer.getAgcode());
			} catch (Exception e) {
				log.warn("withdrawPendingRequest try again!");
				withdrawalConfirmationResponseBean = RemoteCaller.withdrawPendingRequest(customer.getLoginname(), remit, transID, customer.getAgcode());
			}

			if (withdrawalConfirmationResponseBean != null && withdrawalConfirmationResponseBean.getStatus().equals(ErrorCode.SUCCESS.getCode())) {
				transferDao.addTransfer(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.SUCESS, withdrawalConfirmationResponseBean.getPaymentid(), "转出成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
			} else {
				result = "转账状态错误:" + ErrorCode.getChText(withdrawalConfirmationResponseBean.getStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}
}
