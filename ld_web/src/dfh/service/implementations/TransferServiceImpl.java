package dfh.service.implementations;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;

import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.exception.RemoteApiException;
import dfh.exception.ResponseFailedException;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.enums.CreditChangeType;
import dfh.remote.DocumentParser;
import dfh.remote.ErrorCode;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.DepositPendingResponseBean;
import dfh.remote.bean.DspResponseBean;
import dfh.remote.bean.KenoResponseBean;
import dfh.remote.bean.WithdrawalConfirmationResponseBean;
import dfh.service.interfaces.TransferService;
import dfh.utils.BokUtils;
import dfh.utils.Constants;
import dfh.utils.KenoUtil;

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

	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferIn(String transID, String loginname, Double remit) {
		
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferIn:" + loginname);
		try {
			remit = Math.abs(remit);
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

	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferInforDsp(String transID, String loginname, Double remit) {
		
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforDsp:" + loginname);
		try {
			remit = Math.abs(remit);
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			Double localCredit = customer.getCredit();
			
//			if(customer!=null){
//				String username=customer.getLoginname();
//				if(username.equals("qyhp")||username.equals("qiany888")
//						||username.equals("money888go")||username.equals("cc9981")
//						||username.equals("xiaohui78557")
//						||username.equals("newsslaichi")){
//					result = "系统繁忙，请重新尝试";
//					return result;
//				}
//			}

			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				//DepositPendingResponseBean depositPendingResponseBean = RemoteCaller.depositPendingRequest(customer.getLoginname(), remit, transID, customer.getAgcode());
				DspResponseBean dspResponseBean = RemoteCaller.depositPrepareDspRequest(loginname, remit, transID);
				DspResponseBean dspConfirmResponseBean=null;
			//	System.out.println(dspResponseBean);
				// 如果操作成功
				if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
					Transfer transfer = transferDao.addTransferforDsp(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "预备转入");
					
					
					tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_DSPIN.getCode(), transID, null);
//					 标记额度已被扣除
					creditReduce = true;
//
					try {
						dspConfirmResponseBean= RemoteCaller.depositConfirmDspRequest(loginname, remit, transID,1);
					} catch (Exception e) {
						log.warn("dspConfirmResponseBean try again!");
						dspConfirmResponseBean= RemoteCaller.depositConfirmDspRequest(loginname, remit, transID,1);;
					}
					
					if(dspConfirmResponseBean==null||dspConfirmResponseBean.getInfo().equals("1")){
						result = "转账发生异常,请稍后再试或联系在线客服";
					}else{
						Transfer confirmtransfer = (Transfer) get(Transfer.class, Long.parseLong(transID), LockMode.UPGRADE);
						confirmtransfer.setFlag(0);
						confirmtransfer.setRemark("转入成功");
						tradeDao.update(confirmtransfer);
					}
//
//					transfer.setFlag(Constants.FLAG_TRUE);
//					transfer.setRemark("转入成功");
//					update(transfer);
				} else{
					dspConfirmResponseBean= RemoteCaller.depositConfirmDspRequest(loginname, remit, transID,0);
					result = "转账状态错误:" + dspResponseBean.getInfo();
				}
					
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
		
		String result = null;
		log.info("begin to transferOut:" + loginname);
		try {
			remit = Math.abs(remit);
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
	
public String transferOutforDsp(String transID, String loginname, Double remit) {
		
	String result = null;
	log.info("begin to transferOutforDsp:" + loginname);
	try {
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		Double localCredit = customer.getCredit();
		
		// 预备转账
		DspResponseBean dspPrepareResponseBean=null;
		DspResponseBean dspConfirmResponseBean=null;
		try {
			dspPrepareResponseBean = RemoteCaller.withdrawPrepareDspRequest(loginname, remit, transID);
		} catch (Exception e) {
			log.warn("withdrawPrepareDspRequest try again!");
			dspPrepareResponseBean = RemoteCaller.withdrawPrepareDspRequest(loginname, remit, transID);
		}
		if(dspPrepareResponseBean!=null&&dspPrepareResponseBean.getInfo().equals("0")){
			transferDao.addTransferforDsp(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "预备转出");
			dspConfirmResponseBean=RemoteCaller.withdrawConfirmDspRequest(loginname, remit, transID,1);
			//tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
		}else{
			dspConfirmResponseBean=RemoteCaller.withdrawConfirmDspRequest(loginname, remit, transID,0);
			
		}
		
		if(dspConfirmResponseBean.getInfo().equals("1")){
			result = "转账发生异常,请稍后再试或联系在线客服";
		}else{
			Transfer transfer = (Transfer) get(Transfer.class, Long.parseLong(transID), LockMode.UPGRADE);
			transfer.setFlag(0);
			transfer.setRemark("转出成功");
			tradeDao.update(transfer);
			tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_DSPOUT.getCode(), transID, null);
			
		}
		
		
	} catch (Exception e) {
		e.printStackTrace();
		result = "转账发生异常: " + e.getMessage();
	}
	return result;
}
	
public String transferOutforkeno(String transID, String loginname, Double remit,String ip) {
	
	String result = null;
	log.info("begin to transferOutforkeno:" + loginname);
	try{
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		Double localCredit = customer.getCredit();
		
		KenoResponseBean bean=null;
		bean = DocumentParser.parseKenologinResponseRequest(KenoUtil.transferfirst(loginname, -remit,"", ""));
		//System.out.println(bean);
		if(bean!= null){
			if(bean.getName()!=null && bean.getName().equals("Error")){
				result = "转账失败  : "+bean.getValue();
			}else if(bean.getName()!=null && bean.getName().equals("FundIntegrationId")){
				Integer FundIntegrationId = bean.getFundIntegrationId();
				if(FundIntegrationId!=null){
					KenoResponseBean confirmbean=DocumentParser.parseKenologinResponseRequest(KenoUtil.transferconfirm(loginname, -remit,ip,FundIntegrationId, transID));
					//System.out.println(confirmbean);
					if(confirmbean.getName()!=null && confirmbean.getName().equals("Remain")){
						transferDao.addTransferforKneo(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.SUCESS, "", "转出成功");
						tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_KENOOUT.getCode(), transID, null);
					}else{
						result = "转账失败  : "+confirmbean.getValue();
					}
				}
//				transferDao.addTransferforKneo(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.SUCESS, "", "转出成功");
//				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_KENOOUT.getCode(), transID, null);
			}
		}
	}catch (Exception e) {
		e.printStackTrace();
		result = "转账发生异常: " + e.getMessage();
	}
	
	return result;
}

/**
 * 不进行回滚，无论如何，先把额度扣掉再说
 */
public String transferInforkeno(String transID, String loginname, Double remit,String ip) {
	
	String result = null;
	boolean creditReduce = false;
	log.info("begin to transferInforkeno:" + loginname);
	try {
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		Double localCredit = customer.getCredit();
	

		// 检查额度
		log.info("current credit:" + localCredit);
		if (localCredit < remit) {
			result = "转账失败，额度不足";
			log.info(result);
		} else {
			KenoResponseBean bean=null;
			bean = DocumentParser.parseKenologinResponseRequest(KenoUtil.transferfirst(loginname, remit, "", ""));
			//System.out.println(bean);
			if(bean!= null){
				if(bean.getName()!=null && bean.getName().equals("Error")){
					result = "转账失败  : "+bean.getValue();
				}else if(bean.getName()!=null && bean.getName().equals("FundIntegrationId")){
					Integer FundIntegrationId = bean.getFundIntegrationId();
					if(FundIntegrationId!=null){
						KenoResponseBean confirmbean=DocumentParser.parseKenologinResponseRequest(KenoUtil.transferconfirm(loginname, remit,ip,FundIntegrationId, transID));
						//System.out.println(confirmbean);
						if(confirmbean.getName()!=null && confirmbean.getName().equals("Remain")){
							transferDao.addTransferforKneo(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.SUCESS, "", "转入成功");
							tradeDao.changeCredit(loginname, -remit, CreditChangeType.TRANSFER_KENOIN.getCode(), transID, null);
						}else{
							result = "转账失败  : "+confirmbean.getValue();
						}
					}
					
				}
			}
	
//				 标记额度已被扣除
				creditReduce = true;
//
				
				
				
				
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

	public String transferInforBbin(String transID, String loginname, Double remit) {
		
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforBbin:" + loginname);
		try {
			remit = Math.abs(remit);
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			Double localCredit = customer.getCredit();
			
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				//DepositPendingResponseBean depositPendingResponseBean = RemoteCaller.depositPendingRequest(customer.getLoginname(), remit, transID, customer.getAgcode());
				DspResponseBean dspResponseBean = RemoteCaller.depositBbinRequest(loginname, remit.intValue(),localCredit, transID);
				DspResponseBean dspConfirmResponseBean=null;
			//	System.out.println(dspResponseBean);
				// 如果操作成功
				if (dspResponseBean != null && dspResponseBean.getInfo().equals("11100")) {
					Transfer transfer = transferDao.addTransferforBbin(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
					tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_BBININ.getCode(), transID, null);
					creditReduce = true;
				} else if(dspResponseBean != null){
					result = "转账状态错误:" + dspResponseBean.getInfo();
				}
					
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

	public String transferOutforBbin(String transID, String loginname, Double remit) {
		
		String result = null;
		log.info("begin to transferOutforBbin:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			Double localCredit = customer.getCredit();
			
			DspResponseBean dspPrepareResponseBean=null;
			try {
				dspPrepareResponseBean = RemoteCaller.withdrawBbinRequest(loginname, remit.intValue(),localCredit, transID);
			} catch (Exception e) {
				log.warn("withdrawPrepareDspRequest try again!");
			}
			if(dspPrepareResponseBean!=null&&dspPrepareResponseBean.getInfo().equals("11100")){
				transferDao.addTransferforBbin(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_BBINOUT.getCode(), transID, null);
				//tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
			}else if(dspPrepareResponseBean != null){
				result = "转账状态错误:" + dspPrepareResponseBean.getInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}
	/************博客户内转账**************/
	@Override
	public String transferInforBok(String transID, String loginname, Double remit) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforBok:" + loginname);
		try {
			remit = Math.abs(remit);
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			Double localCredit = customer.getCredit();
			
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				String []results = BokUtils.changeBokAmountInfo(loginname, remit.intValue(),"IN");
				// 如果操作成功
				if (null!=results[0] && "1".equals(results[0])) {
					Transfer transfer = transferDao.addTransferforBok(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
					tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_BOKIN.getCode(), transID, null);
					creditReduce = true;
				} else{
					result = "转账状态错误:" + results[1];
				}
					
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
	
	public String transferOutforBok(String transID, String loginname, Double remit) {
		
		String result = null;
		log.info("begin to transferOutforBok:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			Double localCredit = customer.getCredit();
			
			String []results = null;
			try {
				results = BokUtils.changeBokAmountInfo(loginname, remit.intValue(),"OUT");
			} catch (Exception e) {
				log.warn("withdrawPrepareDspRequest try again!");
			}
			if(null!=results[0] && "1".equals(results[0])){
				transferDao.addTransferforBok(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_BOKOUT.getCode(), transID, null);
				//tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
			}else{
				result = "转账状态错误:" + results[1];
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}
}
