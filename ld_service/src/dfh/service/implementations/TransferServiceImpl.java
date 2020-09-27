package dfh.service.implementations;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import dfh.action.vo.AutoYouHuiVo;
import dfh.dao.LogDao;
import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.exception.RemoteApiException;
import dfh.model.Actionlogs;
import dfh.model.Activity;
import dfh.model.AgProfit;
import dfh.model.Bankinfo;
import dfh.model.Const;
import dfh.model.Creditlogs;
import dfh.model.DepositOrder;
import dfh.model.Emigratedapply;
import dfh.model.Emigratedbonus;
import dfh.model.Emigratedrecord;
import dfh.model.Friendbonus;
import dfh.model.Friendbonusrecord;
import dfh.model.HBbonus;
import dfh.model.HBrecord;
import dfh.model.LosePromo;
import dfh.model.Mcquota;
import dfh.model.Offer;
import dfh.model.PTBigBang;
import dfh.model.Payorder;
import dfh.model.PlatformData;
import dfh.model.PreferTransferRecord;
import dfh.model.PreferentialRecord;
import dfh.model.Proposal;
import dfh.model.PtCoupon;
import dfh.model.PtDataNew;
import dfh.model.RedRainWallet;
import dfh.model.Sbcoupon;
import dfh.model.SelfRecord;
import dfh.model.SignAmount;
import dfh.model.SignRecord;
import dfh.model.SystemConfig;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.UsersAgentGame;
import dfh.model.Userstatus;
import dfh.model.WeekSent;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.EBetCode;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.TlyBankFlagEnum;
import dfh.model.enums.UserRole;
import dfh.remote.DocumentParser;
import dfh.remote.ErrorCode;
import dfh.remote.RemoteCaller;
import dfh.remote.bean.DepositPendingResponseBean;
import dfh.remote.bean.DspResponseBean;
import dfh.remote.bean.EBetResp;
import dfh.remote.bean.KenoResponseBean;
import dfh.remote.bean.NTwoCheckClientResponseBean;
import dfh.remote.bean.NTwoWithdrawalConfirmationResponseBean;
import dfh.remote.bean.WithdrawalConfirmationResponseBean;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.IPt8CouponService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.TransferService;
import dfh.utils.Arith;
import dfh.utils.BokUtils;
import dfh.utils.Constants;
import dfh.utils.DatabaseNow;
import dfh.utils.DateUtil;
import dfh.utils.DtUtil;
import dfh.utils.EBetAppUtil;
import dfh.utils.EBetUtil;
import dfh.utils.GPIUtil;
import dfh.utils.JCUtil;
import dfh.utils.Keno2Util;
import dfh.utils.KenoUtil;
import dfh.utils.MGSUtil;
import dfh.utils.NTUtils;
import dfh.utils.NTwoUtil;
import dfh.utils.PNGUtil;
import dfh.utils.PtUtil;
import dfh.utils.PtUtil1;
import dfh.utils.QtUtil;
import dfh.utils.ShaBaUtils;
import dfh.utils.SixLotteryUtil;
import dfh.utils.StringUtil;
import dfh.utils.SynchronizedAppPreferentialUtil;
import dfh.utils.TlyDepositUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TransferServiceImpl extends UniversalServiceImpl implements TransferService {
	
	private static Logger log = Logger.getLogger(TransferServiceImpl.class);
	private TradeDao tradeDao;
	private TransferDao transferDao;
	private UserDao userDao;
	LogDao logDao;
	private SeqService seqService ;
	private IPt8CouponService pt8CouponService ;

	public SeqService getSeqService() {
		return seqService;
	}

	public void setSeqService(SeqService seqService) {
		this.seqService = seqService;
	}


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
	

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferIn(String transID, String loginname, Double remit) {

		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferIn:" + loginname);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		Double localCredit = customer.getCredit();
		try {
			remit = Math.abs(remit);
			
			if (customer.getShippingcode() == null || customer.getShippingcode().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcode()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 531, 532, 533, 534, 535, 537 }));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				// 531 10%存送优惠券的默认投注倍数为10倍，最高1888
				if (type == 531) {
					couponString = "10%存送优惠券";
				}
				// 532 20%存送优惠券的默认投注倍数为10倍，最高1888
				else if (type == 532) {
					couponString = "20%存送优惠券";
				}
				// 533 30%存送优惠券的默认投注倍数为15，最高3888
				else if (type == 533) {
					couponString = "30%存送优惠券";
				}
				// 534 40%存送优惠券的默认投注倍数为18，最高5888
				else if (type == 534) {
					couponString = "40%存送优惠券";
				}
				// 535 50%存送优惠券的默认投注倍数为20，最高8888
				else if (type == 535) {
					couponString = "50%存送优惠券";
				} else if (type == 537) {
					couponString = "注册礼金";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
				// 有效投注额
				SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date beginDate = new Date();
				Date startDate=proposal.getExecuteTime();
				Long day=(Long) ((beginDate.getTime()-proposal.getExecuteTime().getTime())/(24*60*60*1000));  
				if(day>=6){
					Calendar date = Calendar.getInstance();
					date.setTime(beginDate);
					date.set(Calendar.DATE, date.get(Calendar.DATE) - 7);
					date.set(Calendar.MINUTE, date.get(Calendar.MINUTE) + 20);
					startDate = dft.parse(dft.format(date.getTime()));
				}

				Double validBetAmount = RemoteCaller.getTurnOverRequest(loginname, startDate, new Date()).getTurnover();
				validBetAmount = Math.round(validBetAmount * 100.00) / 100.00;
				Double remoteCredit = RemoteCaller.queryCredit(loginname);
				if (validBetAmount < amountAll && remoteCredit >= 20) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者EA游戏金额低于20,方能进行户内转账！";
				}
			}
			

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
						try {
							RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), depositPendingResponseBean.getAgcode());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					transfer.setFlag(Constants.FLAG_TRUE);
					transfer.setRemark("转入成功");
					update(transfer);
					if (customer.getShippingcode() == null || customer.getShippingcode().equals("")) {
						customer.setCreditday(customer.getCreditday() + remit);
						update(customer);
					} else {
						customer.setCreditday(customer.getCreditday() + remit);
						customer.setShippingcode(null);
						update(customer);
					}
				} else
					result = "转账状态错误:" + ErrorCode.getChText(depositPendingResponseBean.getStatus());
			}
		} catch (Exception e) {

//			e.printStackTrace();
//			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
//			if (creditReduce){
//				result = "转账发生异常 :" + e.getMessage();
//			}else{
//				result = "系统繁忙，请重新尝试";
//				Transfer transfer = transferDao.addTransfer(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, null, null);
//				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_IN.getCode(), transID, null);
//			}
			
			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce)
				result = "转账发生异常 :" + e.getMessage();
			else
				result = "系统繁忙，请重新尝试";

			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID
			// IP，不明不错，因此回滚)
			if (e instanceof RemoteApiException)
				throw new GenericDfhRuntimeException(e.getMessage());
		}
		return result;
	}

	public String transferLimitMethod(String loginname, Double newRemit) {
		Users users = getUsers(loginname);
		if (users == null) {
			return "账号不存在!";
		}
		if(users.getFlag()==1){
			return "该账号已经禁用";
		}
		if(users.getRole().equals("AGENT")){
			return "代理不允许游戏";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当前时间
		String today = formatter.format(new Date());
		// 获取上一次时间
		String lastTime = users.getCreditdaydate();
		// 判断是否是当天
		if (lastTime == null || lastTime.equals("") || !lastTime.equals(today)) {
			/*users.setCreditday(0.0);
			users.setCreditdaydate(today);
			update(users);*/
			transferDao.updateUserCdaySql(users, 0.0, today);
		}
		// 限制转入金额
		Double limit = users.getCreditlimit();
		// 获取上一次总转账金额
		Double remit = users.getCreditday();
		// -1转账没有限制 0 表示不能转账 1000表示一天最高转账1000
		if (limit == -1) {
			return null;
		} else if (limit <= remit) {
			return "今天最高转入额度为" + limit + ",目前可以转入额度是：" + (limit - remit);
		} else {
			// 获取总转账金额
			Double remitAll = remit + newRemit;
			if (limit < remitAll) {
				return "今天最高转入额度为" + limit + ",目前可以转入额度是：" + (limit - remit);
			}
			return null;
		}
	}

	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferInEa(String transID, String loginname) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferIn:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getInvitecode()==null || customer.getInvitecode().equals("") || customer.getGifTamount() <= 0){
				return "领取开户礼金失败！";
			}
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double remit = Math.abs(customer.getGifTamount());
			Double remoteCredit = RemoteCaller.queryCredit(loginname);
			Double localCredit = customer.getCredit();
			if (remoteCredit == null) {
				return "必须先登录EA游戏才能领取开户礼金！";
			}
			if (remoteCredit >= 5) {
				return "EA平台金额必须小于5,才能领取开户礼金！";
			}
			DepositPendingResponseBean depositPendingResponseBean = RemoteCaller.depositPendingRequest(customer.getLoginname(), remit, transID, customer.getAgcode());
			// 如果操作成功
			if (depositPendingResponseBean != null && depositPendingResponseBean.getStatus().equals(ErrorCode.SUCCESS.getCode())) {
				Transfer transfer = transferDao.addTransfer(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, depositPendingResponseBean.getPaymentid(), null);
				tradeDao.changeCreditEa(loginname, remit, CreditChangeType.TRANSFER_IN_COUPON.getCode(), transID, "开户礼金:(" + remit + ")", customer.getInvitecode());
				// 标记额度已被扣除
				creditReduce = true;
				try {
					RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), customer.getAgcode());
				} catch (Exception e) {
					log.warn("depositConfirmationResponse try again!");
					try {
						RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), depositPendingResponseBean.getAgcode());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				transfer.setFlag(Constants.FLAG_TRUE);
				transfer.setRemark("转入成功");
				update(transfer);
			} else {
				result = "转账状态错误:" + ErrorCode.getChText(depositPendingResponseBean.getStatus());
			}
		} catch (Exception e) {

			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce)
				result = "转账发生异常 :" + e.getMessage();
			else
				result = "系统繁忙，请重新尝试";

			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID
			// IP，不明不错，因此回滚)
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
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		Double localCredit = customer.getCredit();
		try {
			

			// if(customer!=null){
			// String username=customer.getLoginname();
			// if(username.equals("qyhp")||username.equals("qiany888")
			// ||username.equals("money888go")||username.equals("cc9981")
			// ||username.equals("xiaohui78557")
			// ||username.equals("newsslaichi")){
			// result = "系统繁忙，请重新尝试";
			// return result;
			// }
			// }

			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				// DepositPendingResponseBean depositPendingResponseBean =
				// RemoteCaller.depositPendingRequest(customer.getLoginname(),
				// remit, transID, customer.getAgcode());
				DspResponseBean dspResponseBean = RemoteCaller.depositPrepareDspRequest(loginname, remit, transID);
				DspResponseBean dspConfirmResponseBean = null;
				// System.out.println(dspResponseBean);
				// 如果操作成功
				if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
					Transfer transfer = transferDao.addTransferforDsp(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "预备转入");

					tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_DSPIN.getCode(), transID, null);
					// 标记额度已被扣除
					creditReduce = true;
					//
					try {
						dspConfirmResponseBean = RemoteCaller.depositConfirmDspRequest(loginname, remit, transID, 1);
					} catch (Exception e) {
						log.warn("dspConfirmResponseBean try again!");
						try {
							dspConfirmResponseBean = RemoteCaller.depositConfirmDspRequest(loginname, remit, transID, 1);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					if (dspConfirmResponseBean == null || dspConfirmResponseBean.getInfo().equals("1")) {
						result = "转账发生异常,请稍后再试或联系在线客服";
					} else if(dspConfirmResponseBean.getInfo().equals("0")){
						Transfer confirmtransfer = (Transfer) get(Transfer.class, Long.parseLong(transID), LockMode.UPGRADE);
						confirmtransfer.setFlag(0);
						confirmtransfer.setRemark("转入成功");
						tradeDao.update(confirmtransfer);
						customer.setCreditday(customer.getCreditday() + remit);
						update(customer);
					}else{
						result = dspConfirmResponseBean.getInfo();
					}

					// transfer.setFlag(Constants.FLAG_TRUE);
					// transfer.setRemark("转入成功");
					// update(transfer);
				} else {
					dspConfirmResponseBean = RemoteCaller.depositConfirmDspRequest(loginname, remit, transID, 0);
					result = "转账状态错误:" + dspResponseBean.getInfo();
				}

			}
		} catch (Exception e) {

//			e.printStackTrace();
//			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
//			if (creditReduce){
//				result = "转账发生异常 :" + e.getMessage();
//			}else{
//				result = "系统繁忙，请重新尝试";
//				Transfer transfer = transferDao.addTransferforDsp(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "预备转入");
//
//				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_DSPIN.getCode(), transID, null);
//				
//			}
			
			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce)
				result = "转账发生异常 :" + e.getMessage();
			else
				result = "系统繁忙，请重新尝试";

			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID
			// IP，不明不错，因此回滚)
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
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			if (customer.getShippingcode() == null || customer.getShippingcode().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcode()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 531, 532, 533, 534, 535, 537 ,592}));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();

				couponString = ProposalType.getText(type);
				
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
				// 有效投注额
				SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date beginDate = new Date();
				Date startDate=proposal.getExecuteTime();
				Long day=(Long) ((beginDate.getTime()-proposal.getExecuteTime().getTime())/(24*60*60*1000));  
				if(day>=6){
					Calendar date = Calendar.getInstance();
					date.setTime(beginDate);
					date.set(Calendar.DATE, date.get(Calendar.DATE) - 7);
					date.set(Calendar.MINUTE, date.get(Calendar.MINUTE) + 20);
					startDate = dft.parse(dft.format(date.getTime()));
				}
				Double validBetAmount = RemoteCaller.getTurnOverRequest(loginname, startDate, new Date()).getTurnover();
				validBetAmount = Math.round(validBetAmount * 100.00) / 100.00;
				Double remoteCredit = RemoteCaller.queryCredit(loginname);
				if (validBetAmount < amountAll && remoteCredit >= 20) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者EA游戏金额低于20,方能进行户内转账！";
				}
			}

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
				if (customer.getShippingcode() == null || customer.getShippingcode().equals("")) {

				} else {
					customer.setShippingcode(null);
					update(customer);
				}
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
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			
			//看玩家是否有正在使用的优惠，是否达到流水BEGIN
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "ag"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if(null != selfs && selfs.size()>0){
				SelfRecord record = selfs.get(0);
				if(null != record){
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					if(null != proposal){
						if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						
						// 获取数据总投注额
						Double validBetAmount = getSelfYouHuiBet("ag", loginname, proposal.getPno()) ;
						//后期远程金额
						Double remoteCredit=0.00;
						try {
							String money = RemoteCaller.queryDspCredit(loginname);
							if(null != money && NumberUtils.isNumber(money)){
								remoteCredit = Double.valueOf(money);
							}else{
								return "获取AG余额错误:"+money ;
							}
						} catch (Exception e) {
								return "获取异常失败";
						}
						validBetAmount = validBetAmount==null?0.0:validBetAmount;
						//判断是否达到条件
						log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
						if(validBetAmount != -1.0){
							if ((validBetAmount >= amountAll ||  remoteCredit < 20)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							}else{
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"ag" ,validBetAmount, (amountAll-validBetAmount) , new Date() , ProposalType.SELFPT93.getText() , "IN");
								transferDao.save(transferRecord) ;
								log.info("解除失败");
								return ProposalType.SELFPT93.getText() + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者AG游戏金额低于20元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
							}
						}else{
							record.setType(1);
							record.setRemark(record.getRemark()+";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname+"通过后台控制进行转账");
						}
					}else{
						log.info("AG自助优惠信息出错。Q" + loginname);
						return "AG自助优惠信息出错。Q";
					}
				}else{
					log.info("AG自助优惠信息出错。M" + loginname);
					return "AG自助优惠信息出错。M";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END

			// 预备转账
			DspResponseBean dspPrepareResponseBean = null;
			DspResponseBean dspConfirmResponseBean = null;
			try {
				dspPrepareResponseBean = RemoteCaller.withdrawPrepareDspRequest(loginname, remit, transID);
			} catch (Exception e) {
				log.warn("withdrawPrepareDspRequest try again!");
				dspPrepareResponseBean = RemoteCaller.withdrawPrepareDspRequest(loginname, remit, transID);
			}
			if (dspPrepareResponseBean != null && dspPrepareResponseBean.getInfo().equals("0")) {
				transferDao.addTransferforDsp(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "预备转出");
				dspConfirmResponseBean = RemoteCaller.withdrawConfirmDspRequest(loginname, remit, transID, 1);
				// tradeDao.changeCredit(loginname, remit,
				// CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
			} else {
				dspConfirmResponseBean = RemoteCaller.withdrawConfirmDspRequest(loginname, remit, transID, 0);

			}

			if (dspConfirmResponseBean.getInfo().equals("1")) {
				result = "转账发生异常,请稍后再试或联系在线客服";
			} else if(dspConfirmResponseBean.getInfo().equals("0")){
				Transfer transfer = (Transfer) get(Transfer.class, Long.parseLong(transID), LockMode.UPGRADE);
				transfer.setFlag(0);
				transfer.setRemark("转出成功");
				tradeDao.update(transfer);
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_DSPOUT.getCode(), transID, null);
			}else{
				result = dspConfirmResponseBean.getInfo();
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}

	public String transferOutforkeno(String transID, String loginname, Double remit, String ip) {

		String result = null;
		log.info("begin to transferOutforkeno:" + loginname);
		try {
			remit = Math.abs(remit);
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();

			KenoResponseBean bean = null;
			bean = DocumentParser.parseKenologinResponseRequest(KenoUtil.transferfirst(loginname, -remit, "", ""));
			// System.out.println(bean);
			if (bean != null) {
				if (bean.getName() != null && bean.getName().equals("Error")) {
					result = "转账失败  : " + bean.getValue();
				} else if (bean.getName() != null && bean.getName().equals("FundIntegrationId")) {
					Integer FundIntegrationId = bean.getFundIntegrationId();
					if (FundIntegrationId != null) {
						KenoResponseBean confirmbean = DocumentParser.parseKenologinResponseRequest(KenoUtil.transferconfirm(loginname, -remit, ip, FundIntegrationId, transID));
						// System.out.println(confirmbean);
						if (confirmbean.getName() != null && confirmbean.getName().equals("Remain")) {
							transferDao.addTransferforKneo(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.SUCESS, "", "转出成功");
							tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_KENOOUT.getCode(), transID, null);
						} else {
							result = "转账失败  : " + confirmbean.getValue();
						}
					}
					// transferDao.addTransferforKneo(Long.parseLong(transID),
					// loginname, localCredit, remit, Constants.OUT,
					// Constants.SUCESS, "", "转出成功");
					// tradeDao.changeCredit(loginname, remit,
					// CreditChangeType.TRANSFER_KENOOUT.getCode(), transID,
					// null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}

		return result;
	}

	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferInforkeno(String transID, String loginname, Double remit, String ip) {

		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforkeno:" + loginname);
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		Double localCredit = customer.getCredit();
		try {
			

			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				KenoResponseBean bean = null;
				bean = DocumentParser.parseKenologinResponseRequest(KenoUtil.transferfirst(loginname, remit, "", ""));
				// System.out.println(bean);
				if (bean != null) {
					if (bean.getName() != null && bean.getName().equals("Error")) {
						result = "转账失败  : " + bean.getValue();
					} else if (bean.getName() != null && bean.getName().equals("FundIntegrationId")) {
						Integer FundIntegrationId = bean.getFundIntegrationId();
						if (FundIntegrationId != null) {
							KenoResponseBean confirmbean = DocumentParser.parseKenologinResponseRequest(KenoUtil.transferconfirm(loginname, remit, ip, FundIntegrationId, transID));
							// System.out.println(confirmbean);
							if (confirmbean.getName() != null && confirmbean.getName().equals("Remain")) {
								transferDao.addTransferforKneo(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.SUCESS, "", "转入成功");
								tradeDao.changeCredit(loginname, -remit, CreditChangeType.TRANSFER_KENOIN.getCode(), transID, null);
								// 标记额度已被扣除
								creditReduce = true;
								customer.setCreditday(customer.getCreditday() + remit);
								update(customer);
							} else {
								result = "转账失败  : " + confirmbean.getValue();
							}
						}

					}
				}

			}
		} catch (Exception e) {

			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				result = "系统繁忙，请重新尝试";
				transferDao.addTransferforKneo(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.SUCESS, "", "转入成功");
				tradeDao.changeCredit(loginname, -remit, CreditChangeType.TRANSFER_KENOIN.getCode(), transID, null);
			}
//			
//			e.printStackTrace();
//			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
//			if (creditReduce)
//				result = "转账发生异常 :" + e.getMessage();
//			else
//				result = "系统繁忙，请重新尝试";
//
//			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID
//			// IP，不明不错，因此回滚)
//			if (e instanceof RemoteApiException)
//				throw new GenericDfhRuntimeException(e.getMessage());

		}
		return result;
	}
	
	
	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferInforkenoIn(String transID, String loginname, Double remit, String ip) {

		log.info("开始转入KENO准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账KENO");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("KENO转账正在维护" + loginname);
			return "KENO转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCredit(loginname, -remit, CreditChangeType.TRANSFER_KENOIN.getCode(), transID, null);
			    return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	public String transferOutforkeno2(String transID, String loginname, Double remit, String ip) {

		String result = null;
		log.info("begin to transferOutforkeno2:" + loginname);
		try {
			remit = Math.abs(remit);
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();

			KenoResponseBean bean = null;
			bean = DocumentParser.parseKenologinResponseRequest(Keno2Util.transferfirst(loginname, -remit, "", ""));
			// System.out.println(bean);
			if (bean != null) {
				if (bean.getName() != null && bean.getName().equals("Error")) {
					result = "转账失败  : " + bean.getValue();
				} else if (bean.getName() != null && bean.getName().equals("FundIntegrationId")) {
					Integer FundIntegrationId = bean.getFundIntegrationId();
					if (FundIntegrationId != null) {
						KenoResponseBean confirmbean = DocumentParser.parseKenologinResponseRequest(Keno2Util.transferconfirm(loginname, -remit, ip, FundIntegrationId, transID));
						// System.out.println(confirmbean);
						if (confirmbean.getName() != null && confirmbean.getName().equals("Remain")) {
							transferDao.addTransferforKneo2(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.SUCESS, "", "转出成功");
							tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_KENO2OUT.getCode(), transID, null);
						} else {
							result = "转账失败  : " + confirmbean.getValue();
						}
					}
					// transferDao.addTransferforKneo(Long.parseLong(transID),
					// loginname, localCredit, remit, Constants.OUT,
					// Constants.SUCESS, "", "转出成功");
					// tradeDao.changeCredit(loginname, remit,
					// CreditChangeType.TRANSFER_KENOOUT.getCode(), transID,
					// null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}

		return result;
	}

	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferInforkeno2(String transID, String loginname, Double remit, String ip) {

		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforkeno2:" + loginname);
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		Double localCredit = customer.getCredit();
		try {
			

			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				KenoResponseBean bean = null;
				bean = DocumentParser.parseKenologinResponseRequest(Keno2Util.transferfirst(loginname, remit, "", ""));
				// System.out.println(bean);
				if (bean != null) {
					if (bean.getName() != null && bean.getName().equals("Error")) {
						result = "转账失败  : " + bean.getValue();
					} else if (bean.getName() != null && bean.getName().equals("FundIntegrationId")) {
						Integer FundIntegrationId = bean.getFundIntegrationId();
						if (FundIntegrationId != null) {
							KenoResponseBean confirmbean = DocumentParser.parseKenologinResponseRequest(Keno2Util.transferconfirm(loginname, remit, ip, FundIntegrationId, transID));
							// System.out.println(confirmbean);
							if (confirmbean.getName() != null && confirmbean.getName().equals("Remain")) {
								transferDao.addTransferforKneo2(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.SUCESS, "", "转入成功");
								tradeDao.changeCredit(loginname, -remit, CreditChangeType.TRANSFER_KENO2IN.getCode(), transID, null);
								// 标记额度已被扣除
								creditReduce = true;
								customer.setCreditday(customer.getCreditday() + remit);
								update(customer);
							} else {
								result = "转账失败  : " + confirmbean.getValue();
							}
						}

					}
				}

			}
		} catch (Exception e) {

			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				result = "系统繁忙，请重新尝试";
				transferDao.addTransferforKneo2(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.SUCESS, "", "转入成功");
				tradeDao.changeCredit(loginname, -remit, CreditChangeType.TRANSFER_KENOIN.getCode(), transID, null);
			}
//			
//			e.printStackTrace();
//			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
//			if (creditReduce)
//				result = "转账发生异常 :" + e.getMessage();
//			else
//				result = "系统繁忙，请重新尝试";
//
//			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID
//			// IP，不明不错，因此回滚)
//			if (e instanceof RemoteApiException)
//				throw new GenericDfhRuntimeException(e.getMessage());

		}
		return result;
	}

	public String transferInforBbin(String transID, String loginname, Double remit) {

		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforBbin:" + loginname);
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		Double localCredit = customer.getCredit();
		try {

			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				// DepositPendingResponseBean depositPendingResponseBean =
				// RemoteCaller.depositPendingRequest(customer.getLoginname(),
				// remit, transID, customer.getAgcode());
				DspResponseBean dspResponseBean = RemoteCaller.depositBbinRequest(loginname, remit.intValue(), localCredit, transID);
				// 如果操作成功
				if (dspResponseBean != null && dspResponseBean.getInfo().equals("11100")) {
					transferDao.addTransferforBbin(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
					tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_BBININ.getCode(), transID, null);
					creditReduce = true;
					customer.setCreditday(customer.getCreditday() + remit);
					update(customer);
				} else if(dspResponseBean != null){
					result = "转账状态错误:" + dspResponseBean.getInfo();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				result = "系统繁忙，请重新尝试";
				transferDao.addTransferforBbin(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_BBININ.getCode(), transID, null);
			}
//			e.printStackTrace();
//			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
//			if (creditReduce)
//				result = "转账发生异常 :" + e.getMessage();
//			else
//				result = "系统繁忙，请重新尝试";
//
//			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID
//			// IP，不明不错，因此回滚)
//			if (e instanceof RemoteApiException)
//				throw new GenericDfhRuntimeException(e.getMessage());
		}
		return result;
	}
	
	public String transferInforBbinIn(String transID, String loginname, Double remit) {
		log.info("开始转入BBIN准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账BBIN");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("BBIN转账正在维护" + loginname);
			return "BBIN转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		
		//看玩家是否有正在使用的优惠，是否达到流水BEGIN
				DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
				selfDc.add(Restrictions.eq("loginname", loginname));
				selfDc.add(Restrictions.eq("platform", "bbin"));
				selfDc.add(Restrictions.eq("type", 0));
				selfDc.addOrder(Order.desc("createtime"));
				List<SelfRecord> selfs = this.findByCriteria(selfDc);
				
				if(null != selfs && selfs.size()>0){
					SelfRecord record = selfs.get(0);
					if(null != record){
						Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
						if(null != proposal){
							if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
								return "自助优惠5分钟内不允许户内转账！";
							}
							// 要达到的总投注额
							Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
							
							// 获取数据总投注额
							Double validBetAmount = getSelfYouHuiBet("bbin", loginname, proposal.getPno()) ;
							//后期远程金额
							Double remoteCredit=0.00;
							try {
								String money = RemoteCaller.queryBbinCredit(loginname);
								if(null != money && NumberUtils.isNumber(money)){
									remoteCredit = Double.valueOf(money);
								}else{
									return "获取BBIN余额错误:"+money ;
								}
							} catch (Exception e) {
									return "获取异常失败";
							}
							validBetAmount = validBetAmount==null?0.0:validBetAmount;
							//判断是否达到条件
							log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
							if(validBetAmount != -1.0){
								if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
									record.setType(1);
									record.setUpdatetime(new Date());
									update(record);
									log.info("解除成功");
								}else{
									PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"bbin" ,validBetAmount, (amountAll-validBetAmount) , new Date() , ProposalType.SELFPT95.getText() , "IN");
									transferDao.save(transferRecord) ;
									log.info("解除失败");
									return ProposalType.SELFPT95.getText() + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者BBIN游戏金额低于20元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
								}
							}else{
								record.setType(1);
								record.setRemark(record.getRemark()+";后台放行");
								record.setUpdatetime(new Date());
								update(record);
								log.info(loginname+"通过后台控制进行转账");
							}
						}else{
							log.info("BBIN自助优惠信息出错。Q" + loginname);
							return "BBIN自助优惠信息出错。Q";
						}
					}else{
						log.info("BBIN自助优惠信息出错。M" + loginname);
						return "BBIN自助优惠信息出错。M";
					}
				}
				//看玩家是否有正在使用的优惠，是否达到流水END
				
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
			    tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_BBININ.getCode(), transID, null);
			    return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}


	public String transferOutforBbin(String transID, String loginname, Double remit) {

		String result = null;
		log.info("begin to transferOutforBbin:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			
			//看玩家是否有正在使用的优惠，是否达到流水BEGIN
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "bbin"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if(null != selfs && selfs.size()>0){
				SelfRecord record = selfs.get(0);
				if(null != record){
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					if(null != proposal){
						if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						
						// 获取数据总投注额
						Double validBetAmount = getSelfYouHuiBet("bbin", loginname, proposal.getPno()) ;
						//后期远程金额
						Double remoteCredit=0.00;
						try {
							String money = RemoteCaller.queryBbinCredit(loginname);
							if(null != money && NumberUtils.isNumber(money)){
								remoteCredit = Double.valueOf(money);
							}else{
								return "获取BBIN余额错误:"+money ;
							}
						} catch (Exception e) {
								return "获取异常失败";
						}
						validBetAmount = validBetAmount==null?0.0:validBetAmount;
						//判断是否达到条件
						log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
						if(validBetAmount != -1.0){
							if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							}else{
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"bbin" ,validBetAmount, (amountAll-validBetAmount) , new Date() , ProposalType.SELFPT95.getText() , "IN");
								transferDao.save(transferRecord) ;
								log.info("解除失败");
								return ProposalType.SELFPT95.getText() + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者BBIN游戏金额低于20元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
							}
						}else{
							record.setType(1);
							record.setRemark(record.getRemark()+";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname+"通过后台控制进行转账");
						}
					}else{
						log.info("BBIN自助优惠信息出错。Q" + loginname);
						return "BBIN自助优惠信息出错。Q";
					}
				}else{
					log.info("BBIN自助优惠信息出错。M" + loginname);
					return "BBIN自助优惠信息出错。M";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END

			DspResponseBean dspPrepareResponseBean = null;
			try {
				dspPrepareResponseBean = RemoteCaller.withdrawBbinRequest(loginname, remit.intValue(), localCredit, transID);
			} catch (Exception e) {
				log.warn("withdrawPrepareDspRequest try again!");
			}
			if (dspPrepareResponseBean != null && dspPrepareResponseBean.getInfo().equals("11100")) {
				transferDao.addTransferforBbin(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_BBINOUT.getCode(), transID, null);
				// tradeDao.changeCredit(loginname, remit,
				// CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
			} else if(dspPrepareResponseBean != null){
				result = "转账状态错误:" + dspPrepareResponseBean.getInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}
	
	/**
	 * 真人转入PT游戏
	 */
	public String transferNewPtIn(String transID, String loginname, Double remit) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferNewPtIn:" + loginname);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer==null){
			return "玩家账号不存在！";
		}
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		Double localCredit = customer.getCredit();
		try {
			remit = Math.abs(remit);

			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 571, 572, 573,574 ,575}));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				// 531 10%存送优惠券的默认投注倍数为10倍，最高1888
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 532 20%存送优惠券的默认投注倍数为10倍，最高1888
				else if (type == 572) {
					couponString = "PT68%存送优惠券";
				}
				// 533 30%存送优惠券的默认投注倍数为15，最高3888
				else if (type == 573) {
					couponString = "PT100%存送优惠券";
				}
				else if (type == 574) {
					couponString = "PT88%存送优惠券";
				}
				else if (type == 575) {
					couponString = ProposalType.PT8COUPON.getText();
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				String startStrDate = DateUtil.fmtDateForBetRecods(proposal.getExecuteTime()) ;
				
				String endStrDate = DateUtil.fmtDateForBetRecods(new Date()) ;

				// 获取数据总投注额
				Double validBetAmount = 0.00;
				
				String phpHtml = PtUtil.getPlayerBet(loginname, startStrDate.subSequence(0, 10)+"DAVIDPT"+startStrDate.subSequence(11, startStrDate.length()), endStrDate.subSequence(0, 10)+"DAVIDPT"+endStrDate.subSequence(11, endStrDate.length())) ;
				/*****************************************/
				System.out.println(phpHtml);
				if(phpHtml!=null && !phpHtml.equals("")){
					JSONObject jsonObj = JSONObject.fromObject(phpHtml);
					if (!jsonObj.containsKey("result")) {
						return "返回结果错误";
					}
					int len = jsonObj.getJSONArray("result").size() ;
					if(len >0){
						JSONObject jsonObjDate=JSONObject.fromObject(jsonObj.getJSONArray("result").get(0));
						validBetAmount = Double.valueOf(jsonObjDate.getString("BETS"));
					}else{
						validBetAmount = 0.00 ;
					}
					
				}else{
					log.error("*****获取pt投注额失败！******"+startStrDate);
				}
				/*****************************************/
				//后期远程金额
				Double remoteCredit=0.00;
				try {
				    remoteCredit = PtUtil.getPlayerMoney(loginname);
				} catch (Exception e) {
				    return "获取异常失败";
				}
				//判断是否达到条件
				if (validBetAmount < amountAll && remoteCredit >= 20) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于20,方能进行户内转账！";
				}
			}
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				//转入游戏
				Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, remit);
				if (deposit != null && deposit) {// 转进NEWPT成功
					transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
					tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_MEWPTIN.getCode(), transID, null);
					// 标记额度已被扣除
					creditReduce = true;
					customer.setCreditday(customer.getCreditday()+remit);
					if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

					} else {
						customer.setShippingcodePt(null);
						update(customer);
					}
				} else{
					result = "转账状态错误" ;
				}
			}
		} catch (Exception e) {
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_MEWPTIN.getCode(), transID, null);
				result = "系统繁忙，请重新尝试";
			}
		}
		return result;
	}

	
	/**
	 * PT转入真人账号
	 */
	public String transferNewPtOut(String transID, String loginname, Double remit) {
		String result = null;
		log.info("begin to transferNewPtOut:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				return "玩家账号不存在！";
			}
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			
			/********************************/
			remit = Math.abs(remit);

			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 571, 572, 573,574 ,575}));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				// 531 10%存送优惠券的默认投注倍数为10倍，最高1888
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 532 20%存送优惠券的默认投注倍数为10倍，最高1888
				else if (type == 572) {
					couponString = "PT68%存送优惠券";
				}
				// 533 30%存送优惠券的默认投注倍数为15，最高3888
				else if (type == 573) {
					couponString = "PT100%存送优惠券";
				}
				else if (type == 574) {
					couponString = "PT88%存送优惠券";
				}
				else if (type == 575) {
					couponString = ProposalType.PT8COUPON.getText() ;
				}
				// FIXME
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				String startStrDate = DateUtil.fmtDateForBetRecods(proposal.getExecuteTime()) ;
				
				String endStrDate = DateUtil.fmtDateForBetRecods(new Date()) ;
				// 获取数据总投注额
				Double validBetAmount = 0.00;
				
				String phpHtml = PtUtil.getPlayerBet(loginname, startStrDate.subSequence(0, 10)+"DAVIDPT"+startStrDate.subSequence(11, startStrDate.length()), endStrDate.subSequence(0, 10)+"DAVIDPT"+endStrDate.subSequence(11, endStrDate.length())) ;
				/*****************************************/
				System.out.println(phpHtml);
				if(phpHtml!=null && !phpHtml.equals("")){
					JSONObject jsonObj = JSONObject.fromObject(phpHtml);
					if (!jsonObj.containsKey("result")) {
						return "返回结果错误";
					}
					int len = jsonObj.getJSONArray("result").size() ;
					if(len >0){
						JSONObject jsonObjDate=JSONObject.fromObject(jsonObj.getJSONArray("result").get(0));
						validBetAmount = Double.valueOf(jsonObjDate.getString("BETS"));
					}else{
						validBetAmount = 0.00 ;
					}
					
				}else{
					log.error("*****获取pt投注额失败！******"+startStrDate);
				}
				/*****************************************/
				
				//后期远程金额
				Double remoteCredit=0.00;
				try {
				    remoteCredit = PtUtil.getPlayerMoney(loginname);
				} catch (Exception e) {
					return "获取异常失败";
				}
				//判断是否达到条件
				if (validBetAmount < amountAll && remoteCredit >= 20) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于20,方能进行户内转账！";
				}
			}

			/********************************/
			
			
			
			Double localCredit = customer.getCredit();
			log.info(PtUtil.getPlayerLoginOut(loginname));
			Boolean withdraw = PtUtil.getWithdrawPlayerMoney(loginname, remit);
			if (withdraw != null && withdraw) {// 转出NEWPT成功
				transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_NEWPTOUT.getCode(), transID, null);
				if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

				} else {
					customer.setShippingcodePt(null);
					update(customer);
				}
			} else {
				result = "转账状态错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}

	/* TODO NT旧的转账
	public String transferInforSky(String transID, String loginname, Double remit) {

		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforSky:" + loginname);
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		Double localCredit = customer.getCredit();
		try {
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 571, 572, 573 }));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				// 531 10%存送优惠券的默认投注倍数为10倍，最高1888
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 532 20%存送优惠券的默认投注倍数为10倍，最高1888
				else if (type == 572) {
					couponString = "PT68%存送优惠券";
				}
				// 533 30%存送优惠券的默认投注倍数为15，最高3888
				else if (type == 573) {
					couponString = "PT100%存送优惠券";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				Calendar cal = Calendar.getInstance();
				cal.setTime(proposal.getExecuteTime());
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date startTime = cal.getTime();

				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(new Date());
				cal2.add(Calendar.HOUR_OF_DAY, -7);
				Date endTime = cal2.getTime();

				// 获取数据总投注额
				Double validBetAmount = 0.00;
				DecimalFormat dfFF = new DecimalFormat("#0.00");
				String loginString = SkyUtils.getEffectiveBets(customer.getId(), startTime, endTime);
				JSONObject jsonObj = JSONObject.fromObject(loginString);
				// 判断是否有投注额 如果大于了1个小时没有投注 自动下线
				if (jsonObj.containsKey("stat")) {
					for (Object object : jsonObj.getJSONArray("stat")) {
						List listObject = (List) object;
						if (Integer.parseInt((String) listObject.get(7)) == 0) {
							validBetAmount = Double.parseDouble(dfFF.format(validBetAmount + (Double.parseDouble((String) listObject.get(2)) * Double.parseDouble((String) listObject.get(3)) * Double.parseDouble((String) listObject.get(4))) / 100.00));
						}
					}
				}
				//后期远程金额
				Double remoteCredit=0.00;
				String loginStringTwo = SkyUtils.getSkyMonery(customer.getId());
				JSONObject jsonObjTwo = JSONObject.fromObject(loginStringTwo);
				try {
					if (!jsonObjTwo.containsKey("balance")) {
						return "转账金额失败！";
					} else {
						 remoteCredit = jsonObjTwo.getDouble("balance") / 100.0;
					}
				} catch (Exception e) {
					if (!jsonObjTwo.containsKey("error")) {
						return "获取异常失败";
					} else {
						Integer error = jsonObj.getInt("error");
						if (error == 1) {
							return "参数错误";
						} else if (error == 2) {
							return "货币错误";
						} else if (error == 3) {
							return "token或secret key 有误";
						} else if (error == 4) {
							return "jackpot group id错误";
						} else if (error == 5) {
							return "会员id错误";
						} else if (error == 6) {
							return "用户名不能进入游戏";
						} else if (error == 7) {
							return "资金不足";
						} else if (error == 8) {
							return "日期错误";
						}
					}
				}
				//判断是否达到条件
				if (validBetAmount < amountAll && remoteCredit >= 20) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于20,方能进行户内转账！";
				}
			}

			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				Double reseve3 = Double.parseDouble(String.valueOf((remit) * (100)));
				int bMonery = reseve3.intValue();
				String loginString = SkyUtils.changeMonery(customer.getId(), Integer.valueOf(bMonery));
				JSONObject jsonObj = JSONObject.fromObject(loginString);
				try {
					if (!jsonObj.containsKey("balance")) {
						result = "转账金额失败！";
					} else {
						Double balance = jsonObj.getDouble("balance");
						if (balance != null) {
							Transfer transfer = transferDao.addTransferforPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
							tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_PTIN.getCode(), transID, null);
							creditReduce = true;
							customer.setCreditday(customer.getCreditday() + remit);
							if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

							} else {
								customer.setShippingcodePt(null);
								update(customer);
							}
							
						} else {
							result = "转账金额失败！";
						}
					}
				} catch (Exception e) {
					if (!jsonObj.containsKey("error")) {
						result = "获取异常失败";
					} else {
						Integer error = jsonObj.getInt("error");
						if (error == 1) {
							result = "参数错误";
						} else if (error == 2) {
							result = "货币错误";
						} else if (error == 3) {
							result = "token或secret key 有误";
						} else if (error == 4) {
							result = "jackpot group id错误";
						} else if (error == 5) {
							result = "会员id错误";
						} else if (error == 6) {
							result = "用户名不能进入游戏";
						} else if (error == 7) {
							result = "资金不足";
						} else if (error == 8) {
							result = "日期错误";
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				result = "系统繁忙，请重新尝试";
				transferDao.addTransferforPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_PTIN.getCode(), transID, null);
			}
//			e.printStackTrace();
//			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
//			if (creditReduce)
//				result = "转账发生异常 :" + e.getMessage();
//			else
//				result = "系统繁忙，请重新尝试";
//
//			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID
//			// IP，不明不错，因此回滚)
//			if (e instanceof RemoteApiException)
//				throw new GenericDfhRuntimeException(e.getMessage());
		}
		return result;
	}*/

	/* TODO NT旧的转账
	public String transferOutforSky(String transID, String loginname, Double remit) {
		String result = null;
		log.info("begin to transferOutforSky:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 571, 572, 573 }));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				// 531 10%存送优惠券的默认投注倍数为10倍，最高1888
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 532 20%存送优惠券的默认投注倍数为10倍，最高1888
				else if (type == 572) {
					couponString = "PT68%存送优惠券";
				}
				// 533 30%存送优惠券的默认投注倍数为15，最高3888
				else if (type == 573) {
					couponString = "PT100%存送优惠券";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				Calendar cal = Calendar.getInstance();
				cal.setTime(proposal.getExecuteTime());
				cal.add(Calendar.HOUR_OF_DAY, -7);
				Date startTime = cal.getTime();

				Calendar cal2 = Calendar.getInstance();
				cal2.setTime(new Date());
				cal2.add(Calendar.HOUR_OF_DAY, -7);
				Date endTime = cal2.getTime();

				// 获取数据总投注额
				Double validBetAmount = 0.00;
				DecimalFormat dfFF = new DecimalFormat("#0.00");
				String loginString = SkyUtils.getEffectiveBets(customer.getId(), startTime, endTime);
				JSONObject jsonObj = JSONObject.fromObject(loginString);
				// 判断是否有投注额 如果大于了1个小时没有投注 自动下线
				if (jsonObj.containsKey("stat")) {
					for (Object object : jsonObj.getJSONArray("stat")) {
						List listObject = (List) object;
						if (Integer.parseInt((String) listObject.get(7)) == 0) {
							validBetAmount = Double.parseDouble(dfFF.format(validBetAmount + (Double.parseDouble((String) listObject.get(2)) * Double.parseDouble((String) listObject.get(3)) * Double.parseDouble((String) listObject.get(4))) / 100.00));
						}
					}
				}
				//后期远程金额
				Double remoteCredit=0.00;
				String loginStringTwo = SkyUtils.getSkyMonery(customer.getId());
				JSONObject jsonObjTwo = JSONObject.fromObject(loginStringTwo);
				try {
					if (!jsonObjTwo.containsKey("balance")) {
						return "转账金额失败！";
					} else {
						 remoteCredit = jsonObjTwo.getDouble("balance") / 100.0;
					}
				} catch (Exception e) {
					if (!jsonObjTwo.containsKey("error")) {
						return "获取异常失败";
					} else {
						Integer error = jsonObj.getInt("error");
						if (error == 1) {
							return "参数错误";
						} else if (error == 2) {
							return "货币错误";
						} else if (error == 3) {
							return "token或secret key 有误";
						} else if (error == 4) {
							return "jackpot group id错误";
						} else if (error == 5) {
							return "会员id错误";
						} else if (error == 6) {
							return "用户名不能进入游戏";
						} else if (error == 7) {
							return "资金不足";
						} else if (error == 8) {
							return "日期错误";
						}
					}
				}
				//判断是否达到条件
				if (validBetAmount < amountAll && remoteCredit >= 20) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于20,方能进行户内转账！";
				}
			}
			
			Double localCredit = customer.getCredit();
			Double reseve3 = Double.parseDouble(String.valueOf((remit) * (-100)));
			int bMonery = reseve3.intValue();
			String loginString = SkyUtils.changeMonery(customer.getId(), Integer.valueOf(bMonery));
			JSONObject jsonObj = JSONObject.fromObject(loginString);
			try {
				if (jsonObj.containsKey("balance")) {
					Double balance = jsonObj.getDouble("balance");
					if (balance != null) {
						transferDao.addTransferforSky(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转入成功");
						tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_PTOUT.getCode(), transID, null);
						
						if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

						} else {
							customer.setShippingcodePt(null);
							update(customer);
						}
						
					} else {
						result = "转账异常，重新转账！";
					}
				} else if (jsonObj.containsKey("error")) {
					Integer error = jsonObj.getInt("error");
					if (error == 1) {
						result = "参数错误";
					} else if (error == 2) {
						result = "货币错误";
					} else if (error == 3) {
						result = "token或secret key 有误";
					} else if (error == 4) {
						result = "jackpot group id错误";
					} else if (error == 5) {
						result = "会员id错误";
					} else if (error == 6) {
						result = "用户名不能进入游戏";
					} else if (error == 7) {
						result = "资金不足";
					} else if (error == 8) {
						result = "日期错误";
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = "转账发生异常: " + e.getMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}*/

	/** **********博客户内转账************* */
	@Override
	public String transferInforBok(String transID, String loginname, Double remit) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforBok:" + loginname);
		try {
			remit = Math.abs(remit);
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();

			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				String[] results = BokUtils.changeBokAmountInfo(loginname, remit.intValue(), "IN");
				// 如果操作成功
				if (null != results[0] && "1".equals(results[0])) {
					Transfer transfer = transferDao.addTransferforBok(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
					tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_BOKIN.getCode(), transID, null);
					creditReduce = true;
				} else {
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

			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID
			// IP，不明不错，因此回滚)
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
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();

			String[] results = null;
			try {
				results = BokUtils.changeBokAmountInfo(loginname, remit.intValue(), "OUT");
			} catch (Exception e) {
				log.warn("withdrawPrepareDspRequest try again!");
			}
			if (null != results[0] && "1".equals(results[0])) {
				transferDao.addTransferforBok(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_BOKOUT.getCode(), transID, null);
				// tradeDao.changeCredit(loginname, remit,
				// CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
			} else {
				result = "转账状态错误:" + results[1];
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}

	@Override
	public String transferInforSB(String transID, String loginname, Double remit) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforSB:" + loginname);
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		Double localCredit = customer.getCredit();
		try {
			String sbloginname="e68_"+loginname;
			Sbcoupon sbcoupon=userDao.getSbcoupon( sbloginname);
			//玩家使用了优惠
			if (sbcoupon != null) {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", sbcoupon.getShippingcode()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] {581, 582, 583, 584}));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				if (type == 581) {
					couponString = "188体育红包优惠券";
				}else if (type == 582) {
					couponString = "188体育存10万送2万";
				}else if (type == 583) {
					couponString = "188体育存20万送5万";
				}else if (type == 584) {
					couponString = "188体育20%存送优惠劵";
				}else{
					return "无此类型优惠";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
				//获取投注额
				Calendar date = Calendar.getInstance();
				date.setTime(proposal.getExecuteTime());
				date.add(Calendar.HOUR_OF_DAY, -12);
				Date startDate = date.getTime();
				Double validBetAmount = userDao.getTurnOverRequest(sbloginname, startDate, new Date());
				//获取体育远程额度
				if (validBetAmount < amountAll) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + ",方能进行户内转账！";
				}
			}
			
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				// DepositPendingResponseBean depositPendingResponseBean =
				// RemoteCaller.depositPendingRequest(customer.getLoginname(),
				// remit, transID, customer.getAgcode());
				DspResponseBean dspResponseBean = null;
				try {
					dspResponseBean = RemoteCaller.depositSBRequest(loginname, remit, transID);
				} catch (Exception e) {
					log.error("depositSBRequest异常,transID:" + transID);
					dspResponseBean = RemoteCaller.getTransferStatusSBRequest(transID);
				}
				// 如果操作成功
				if (dspResponseBean != null && "000".equals(dspResponseBean.getInfo())) {
					Transfer transfer = transferDao.addTransferforSB(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
					tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_SBIN.getCode(), transID, null);
					creditReduce = true;
					customer.setCreditday(customer.getCreditday() + remit);
					update(customer);
					//取消优惠
					userDao.updateSbcoupon(sbloginname);
				} else if(dspResponseBean!=null){
					result = "转账状态错误:" + dspResponseBean.getInfo();
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				result = "系统繁忙，请重新尝试";
				transferDao.addTransferforSB(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_SBIN.getCode(), transID, null);
			}
			
//			e.printStackTrace();
//			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
//			if (creditReduce)
//				result = "转账发生异常 :" + e.getMessage();
//			else
//				result = "系统繁忙，请重新尝试";
//
//			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID
//			// IP，不明不错，因此回滚)
//			if (e instanceof RemoteApiException)
//				throw new GenericDfhRuntimeException(e.getMessage());
		}
		return result;
	}

	@Override
	public String transferOutforSB(String transID, String loginname, Double remit) {
		String result = null;
		log.info("begin to transferOutforSB:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			
			String sbloginname="e68_"+loginname;
			Sbcoupon sbcoupon=userDao.getSbcoupon( sbloginname);
			//玩家使用了优惠
			if (sbcoupon != null) {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", sbcoupon.getShippingcode()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] {581, 582, 583, 584}));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				if (type == 581) {
					couponString = "188体育红包优惠券";
				}else if (type == 582) {
					couponString = "188体育存10万送2万";
				}else if (type == 583) {
					couponString = "188体育存20万送5万";
				}else if (type == 584) {
					couponString = "188体育20%存送优惠劵";
				}else{
					return "无此类型优惠";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
				//获取投注额
				Calendar date = Calendar.getInstance();
				date.setTime(proposal.getExecuteTime());
				date.add(Calendar.HOUR_OF_DAY, -12);
				Date startDate = date.getTime();
				Double validBetAmount = userDao.getTurnOverRequest(sbloginname, startDate, new Date());
				if (validBetAmount < amountAll) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + ",方能进行户内转账！";
				}
			}

			DspResponseBean dspPrepareResponseBean = null;
			try {
				dspPrepareResponseBean = RemoteCaller.withdrawSBRequest(loginname, remit, transID);
			} catch (Exception e) {
				log.warn("withdrawPrepareDspRequest try again!");
				dspPrepareResponseBean = RemoteCaller.getTransferStatusSBRequest(transID);
			}
			if (dspPrepareResponseBean != null && "000".equals(dspPrepareResponseBean.getInfo())) {
				transferDao.addTransferforSB(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_SBOUT.getCode(), transID, null);
				//取消存送优惠
				userDao.updateSbcoupon(sbloginname);
				// tradeDao.changeCredit(loginname, remit,
				// CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
			} else if(dspPrepareResponseBean!=null){
				result = "转账状态错误:" + dspPrepareResponseBean.getInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}

	

	
	
	/**
	 * ttg红包优惠处理
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforRedCouponTtg(String transID, String loginname, String couponType, String couponCode, String ip) {

		String result = null;
		log.info("begin to 红包优惠处理:" + couponCode + "玩家" + loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				vo.setMessage("用户不存在！");
				return vo;
			}
			if (customer.getFlag() == 1) {
				vo.setMessage("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			//dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[] {419 }));
			List<Proposal> list = findByCriteria(dc);
			Proposal proposal = null;
			if (list == null || list.size() <= 0) {
				vo.setMessage("优惠代码错误！");
				return vo;
			}
			else if(list !=null && list.size()>0){
				 proposal = list.get(0);
				if(proposal.getFlag()==2){
					vo.setMessage("优惠代码已使用！");
					return vo;
				}
				if(proposal.getFlag()==0){
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			// 获取提案信息
			if (!StringUtil.isEmpty(proposal.getLoginname())) {
				if (!(proposal.getLoginname().equals(loginname))) {
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			couponString = "红包优惠";
			String ProposalTypeText = ProposalType.REDCOUPON419.getText();
		
			String typeString = "";
			// 判断是哪个平台
			if (couponType.equals("ttg")) {
				try {
					typeString = "优惠红包->TTG";
					if (proposal.getType() == 419) {
						try {
							String remoteCreditStr = PtUtil1.getPlayerAccount(customer.getLoginname());
							Double remoteCredit = null;
							if (!StringUtils.isEmpty(remoteCreditStr)) {
								remoteCredit = Double.parseDouble(remoteCreditStr);
							}
							if (remoteCredit != null) {
								// 判断TTG平台用户的金额是否
								log.info("remote credit:" + remoteCredit);
								if (remoteCredit > 5) {
									vo.setMessage("TTG平台金额必须小于5,才能使用红包优惠劵！");
									return vo;
								}
								try {
									String remark = couponString + "送" + proposal.getGifTamount()+"元";
									Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, null, proposal.getGifTamount(), remark);
									String msg = this.transferInforTtgIn(transID, loginname, proposal.getGifTamount(), ProposalTypeText, remark);
									if (null != msg) {
										vo.setMessage("TTG优惠券使用失败：" + msg);
										return vo;
									}
								} catch (Exception e) {
									result = "获取异常失败";
									vo.setMessage(result);
									return vo;
								}
							} else {
								//return "转账金额失败！";
								vo.setMessage("转账金额失败！");
								return vo;
							}
						} catch (Exception e) {
							//return "获取异常失败";
							vo.setMessage("获取异常失败");
							return vo;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					vo.setMessage("系统异常");
					return vo;
				}
			}
			// 更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());		
			proposal.setRemark(typeString + "创建时间:" + proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			proposal.setAgent(customer.getAgent());
			update(proposal);
			// 记录下到目前为止的投注额度 begin
			String platform = "ttg";
			// 记录下到目前为止的投注额度
			// 获取0点到领取时的ttg投注额
			String totalBetSql = "select bet from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			prams.put("platform", "ttg");
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0);
			this.save(record);
			// 记录下到目前为止的投注额度 end
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			this.save(selfRecord);

			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			try {
					this.addTransferforTt(Long.parseLong(transID), loginname, customer.getCredit(), proposal.getGifTamount(), Constants.IN, Constants.FAIL, "", "转入成功");
					result = null;
					vo.setGiftMoney(proposal.getGifTamount());
			} catch (Exception e) {
				result = "TTG优惠券使用失败，请联系客服！";
				e.printStackTrace();
				vo.setMessage(result);
				return vo;
			}
		} catch (Exception e) {
			vo.setMessage("系统异常!");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}
	
	
	
	/**
	 * pt红包优惠处理
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforRedCouponPt(String transID, String loginname, String couponType, String couponCode, String ip) {

		String result = null;
		log.info("begin to 红包优惠处理:" + couponCode + "玩家" + loginname);
		boolean creditReduce = false;
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		String zzID = "转账NEWPT";
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				vo.setMessage("用户不存在！");
				return vo;
			}
			if (customer.getFlag() == 1) {
				vo.setMessage("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			//dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[] {419 }));
			List<Proposal> list = findByCriteria(dc);
			Proposal proposal = null;
			if (list == null || list.size() <= 0) {
				vo.setMessage("优惠代码错误！");
				return vo;
			}
			else if(list !=null && list.size()>0){
				 proposal = list.get(0);
				if(proposal.getFlag()==2){
					vo.setMessage("优惠代码已使用！");
					return vo;
				}
				if(proposal.getFlag()==0){
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			// 获取提案信息
			if (!StringUtil.isEmpty(proposal.getLoginname())) {
				if (!(proposal.getLoginname().equals(loginname))) {
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			couponString = "红包优惠";
			String ProposalTypeText = ProposalType.REDCOUPON419.getText();
			Double localCredit = customer.getCredit();
		
			String typeString = "";
			// 判断是哪个平台
			if (couponType.equals("pt")) {
				try {
					typeString = "优惠红包->PT";
					if (proposal.getType() == 419) {
						try {
							Double remoteCredit =  PtUtil.getPlayerMoney(loginname);
							if (remoteCredit != null) {
								// 判断PT平台用户的金额是否
								log.info("remote credit:" + remoteCredit);
								if (remoteCredit >= 5) {
									vo.setMessage("PT平台金额必须小于5,才能使用该优惠劵！");
									return vo;
								}
								
								//总开关限制
								Const constPt = transferDao.getConsts(zzID);
								if (constPt == null) {
									log.info("平台不存在" + proposal.getLoginname());
									vo.setMessage("平台不存在！");
								}
								if (constPt.getValue().equals("0")) {
									log.info(zzID + "正在维护" + proposal.getLoginname());
									vo.setMessage("正在维护！");
								}
								
								
								try {
									Transfer transfer = transferDao.addTransferForNewPT(Long.parseLong(transID), loginname, localCredit, proposal.getGifTamount(), Constants.IN, Constants.FAIL, "", null);
									tradeDao.changeCreditCouponPt(loginname, 0.0, CreditChangeType.TRANSFER_REDCOUPONS_NEWPT.getCode(), transID, couponString + ":" + "游戏金额(" + proposal.getGifTamount() + ")=存入金额(" + 0.0 + ") + 红包优惠金额(" + proposal.getGifTamount() + ")", proposal.getShippingCode());
									// 标记额度已被扣除
									creditReduce = true;
									transfer.setFlag(Constants.FLAG_TRUE);
									transfer.setRemark("转入成功");
									update(transfer);
									vo.setGiftMoney(proposal.getGifTamount());
								} catch (Exception e) {
									result = "获取异常失败";
									vo.setMessage(result);
									return vo;
								}
							} else {
								//return "转账金额失败！";
								vo.setMessage("转账金额失败！");
								return vo;
							}
						} catch (Exception e) {
							//return "获取异常失败";
							vo.setMessage("获取异常失败");
							return vo;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					vo.setMessage("系统异常");
					return vo;
				}
			}
			// 更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());		
			proposal.setRemark(typeString + "创建时间:" + proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			proposal.setAgent(customer.getAgent());
			update(proposal);
			customer.setCreditday(customer.getCreditday()+proposal.getGifTamount());
			update(customer);
		} catch (Exception e) {
			vo.setMessage("系统异常!");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}
	
	
	
	

	/**
	 * qt红包优惠处理
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforRedCouponQT(String transID, String loginname, String couponType, String couponCode, String ip) {

		String result = null;
		log.info("begin to 红包优惠处理:" + couponCode + "玩家" + loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				vo.setMessage("用户不存在！");
				return vo;
			}
			if (customer.getFlag() == 1) {
				vo.setMessage("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			//dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[] {419 }));
			List<Proposal> list = findByCriteria(dc);
			Proposal proposal = null;
			if (list == null || list.size() <= 0) {
				vo.setMessage("优惠代码错误！");
				return vo;
			}
			else if(list !=null && list.size()>0){
				 proposal = list.get(0);
				if(proposal.getFlag()==2){
					vo.setMessage("优惠代码已使用！");
					return vo;
				}
				if(proposal.getFlag()==0){
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			// 获取提案信息
			if (!StringUtil.isEmpty(proposal.getLoginname())) {
				if (!(proposal.getLoginname().equals(loginname))) {
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			couponString = "红包优惠";
			String ProposalTypeText = ProposalType.REDCOUPON419.getText();
		
			String typeString = "";
			// 判断是哪个平台
			if (couponType.equals("qt")) {
				try {
					typeString = "优惠红包->QT";
					if (proposal.getType() == 419) {
						try {
							String remoteCreditStr = QtUtil.getBalance(customer.getLoginname());
							Double remoteCredit = null;
							if (!StringUtils.isEmpty(remoteCreditStr)) {
								remoteCredit = Double.parseDouble(remoteCreditStr);
							}
							if (remoteCredit != null) {
								// 判断QT平台用户的金额是否
								log.info("remote credit:" + remoteCredit);
								if (remoteCredit > 5) {
									vo.setMessage("QT平台金额必须小于5,才能使用红包优惠劵！");
									return vo;
								}
								try {
									String remark = couponString + "送" + proposal.getGifTamount()+"元";
									Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, null, proposal.getGifTamount(), remark);
									String msg = this.transferInforQtIn(transID, loginname, proposal.getGifTamount(), ProposalTypeText, remark);
									if (null != msg) {
										vo.setMessage("QT优惠券使用失败：" + msg);
										return vo;
									}
									//this.save(offer);
								} catch (Exception e) {
									result = "获取异常失败";
									vo.setMessage(result);
									return vo;
								}
							} else {
								vo.setMessage("转账金额失败！");
								return vo;
							}
						} catch (Exception e) {
							vo.setMessage("获取异常失败");
							return vo;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					vo.setMessage("系统异常");
					return vo;
				}
			}
			// 更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());		
			proposal.setRemark(typeString + "创建时间:" + proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			proposal.setAgent(customer.getAgent());
			update(proposal);
			// 记录下到目前为止的投注额度 begin
			String platform = "qt";
			String totalBetSql = "select bet from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			prams.put("platform", "qt");
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0);
			this.save(record);
			// 记录下到目前为止的投注额度 end
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			this.save(selfRecord);

			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			try {
					this.addTransferforQt(Long.parseLong(transID), loginname, customer.getCredit(), proposal.getGifTamount(), Constants.IN, Constants.FAIL, "", "转入成功");
					result = null;
					vo.setGiftMoney(proposal.getGifTamount());
			} catch (Exception e) {
				result = "QT优惠券使用失败，请联系客服！";
				e.printStackTrace();
				vo.setMessage(result);
				return vo;
			}
		} catch (Exception e) {
			vo.setMessage("系统异常!");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}
	
	
	
	
	/**
	 * nt红包优惠处理
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforRedCouponNT(String transID, String loginname, String couponType, String couponCode, String ip) {

		String result = null;
		log.info("begin to 红包优惠处理:" + couponCode + "玩家" + loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				vo.setMessage("用户不存在！");
				return vo;
			}
			if (customer.getFlag() == 1) {
				vo.setMessage("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			//dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[] {419 }));
			List<Proposal> list = findByCriteria(dc);
			Proposal proposal = null;
			if (list == null || list.size() <= 0) {
				vo.setMessage("优惠代码错误！");
				return vo;
			}
			else if(list !=null && list.size()>0){
				 proposal = list.get(0);
				if(proposal.getFlag()==2){
					vo.setMessage("优惠代码已使用！");
					return vo;
				}
				if(proposal.getFlag()==0){
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			// 获取提案信息
			if (!StringUtil.isEmpty(proposal.getLoginname())) {
				if (!(proposal.getLoginname().equals(loginname))) {
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			couponString = "红包优惠";
			String ProposalTypeText = ProposalType.REDCOUPON419.getText();
		
			String typeString = "";
			// 判断是哪个平台
			if (couponType.equals("nt")) {
				try {
					typeString = "优惠红包->NT";
					if (proposal.getType() == 419) {
						try {
							Double remoteCredit=null;
							JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(customer.getLoginname()));
							if (ntm.getBoolean("result")){
								remoteCredit=ntm.getDouble("balance");
							}
							if (remoteCredit != null) {
								// 判断NT平台用户的金额是否
								log.info("remote credit:" + remoteCredit);
								if (remoteCredit > 5) {
									//return "NT平台金额必须小于5,才能使用该优惠劵！";
									vo.setMessage("NT平台金额必须小于5,才能使用红包优惠劵！");
									return vo;
								}
								try {
									String remark = couponString + "送" + proposal.getGifTamount()+"元";
									Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, null, proposal.getGifTamount(), remark);
									String msg = this.transferToNTJudge(transID, loginname, proposal.getGifTamount(), ProposalTypeText, remark);
									if (null != msg) {
										vo.setMessage("NT优惠券使用失败：" + msg);
										return vo;
									}
									//this.save(offer);
								} catch (Exception e) {
									result = "获取异常失败";
									vo.setMessage(result);
									return vo;
								}
							} else {
								//return "转账金额失败！";
								vo.setMessage("转账金额失败！");
								return vo;
							}
						} catch (Exception e) {
							//return "获取异常失败";
							vo.setMessage("获取异常失败");
							return vo;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					vo.setMessage("系统异常");
					return vo;
				}
			}
			// 更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());		
			proposal.setRemark(typeString + "创建时间:" + proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			proposal.setAgent(customer.getAgent());
			update(proposal);
			// 记录下到目前为止的投注额度 begin
			String platform = "nt";
			//记录下到目前为止的投注额度
			String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0);
			this.save(record);
			// 记录下到目前为止的投注额度 end
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			this.save(selfRecord);

			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			try {
					this.addTransferforNT(Long.parseLong(transID), loginname, customer.getCredit(), proposal.getGifTamount(), Constants.IN, Constants.FAIL, "", "转入成功");
					result = null;
					vo.setGiftMoney(proposal.getGifTamount());
			} catch (Exception e) {
				result = "NT优惠券使用失败，请联系客服！";
				e.printStackTrace();
				vo.setMessage(result);
				return vo;
			}
		} catch (Exception e) {
			vo.setMessage("系统异常!");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}
	
	
	
	
	/**
	 * mg红包优惠处理
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforRedCouponMG(String transID, String loginname, String couponType, String couponCode, String ip) {

		String result = null;
		log.info("begin to 红包优惠处理:" + couponCode + "玩家" + loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				vo.setMessage("用户不存在！");
				return vo;
			}
			if (customer.getFlag() == 1) {
				vo.setMessage("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			//dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[] {419 }));
			List<Proposal> list = findByCriteria(dc);
			Proposal proposal = null;
			if (list == null || list.size() <= 0) {
				vo.setMessage("优惠代码错误！");
				return vo;
			}
			else if(list !=null && list.size()>0){
				 proposal = list.get(0);
				if(proposal.getFlag()==2){
					vo.setMessage("优惠代码已使用！");
					return vo;
				}
				if(proposal.getFlag()==0){
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			// 获取提案信息
			if (!StringUtil.isEmpty(proposal.getLoginname())) {
				if (!(proposal.getLoginname().equals(loginname))) {
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			couponString = "红包优惠";
			String ProposalTypeText = ProposalType.REDCOUPON419.getText();
		
			String typeString = "";
			// 判断是哪个平台
			if (couponType.equals("mg")) {
				try {
					typeString = "优惠红包->MG";
					if (proposal.getType() == 419) {
						try {
							Double remoteCredit=null;
							//获取MG平台余额
							//JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(customer.getLoginname()));
							//remoteCredit= MGSUtil.getBalance(customer.getLoginname(),customer.getPassword());
							remoteCredit= MGSUtil.getBalance(customer.getLoginname());
							if (remoteCredit != null) {
								// 判断MG平台用户的金额是否
								log.info("remote credit:" + remoteCredit);
								if (remoteCredit > 5) {
									vo.setMessage("MG平台金额必须小于5,才能使用红包优惠劵！");
									return vo;
								}
								try {
									String remark = couponString + "送" + proposal.getGifTamount()+"元";
									Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, null, proposal.getGifTamount(), remark);
									String msg = this.transferInforMgIn(transID, loginname, proposal.getGifTamount(), ProposalTypeText, remark);
									if (null != msg) {
										vo.setMessage("MG优惠券使用失败：" + msg);
										return vo;
									}
									//this.save(offer);
								} catch (Exception e) {
									result = "获取异常失败";
									vo.setMessage(result);
									return vo;
								}
							} else {
								//return "转账金额失败！";
								vo.setMessage("转账金额失败！");
								return vo;
							}
						} catch (Exception e) {
							//return "获取异常失败";
							vo.setMessage("获取异常失败");
							return vo;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					vo.setMessage("系统异常");
					return vo;
				}
			}
			// 更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());		
			proposal.setRemark(typeString + "创建时间:" + proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			proposal.setAgent(customer.getAgent());
			update(proposal);
			// 记录下到目前为止的投注额度 begin
			String platform = "mg";
			//记录下到目前为止的投注额度(统计今天00:00:00到现在现在的投注额)
			/*String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0);
			this.save(record);*/
			
			//统计今天00:00:00到现在现在的投注额
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			Date startTime = cd.getTime();
		    Date endTime = new Date();
		    Double betAmount=0.00;
		    try {
		    	//查询投注额
		    	 betAmount = getMgBetsAmount(loginname, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(endTime));
		        System.out.println(betAmount+"--->投注额");
		       } catch (Exception e) {
		        e.printStackTrace();
		        log.error("查询:" + loginname + ",时间：" + DateUtil.formatDateForStandard(startTime)+ "-----" +  DateUtil.formatDateForStandard(endTime) + "MG投注额异常：", e);
		       }
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0);
			this.save(record);
			
			// 记录下到目前为止的投注额度 end
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			this.save(selfRecord);

			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			try {
					this.addTransferforMg(Long.parseLong(transID), loginname, customer.getCredit(), proposal.getGifTamount(), Constants.IN, Constants.FAIL, "", "转入成功");
					result = null;
					vo.setGiftMoney(proposal.getGifTamount());
			} catch (Exception e) {
				result = "MG优惠券使用失败，请联系客服！";
				e.printStackTrace();
				vo.setMessage(result);
				return vo;
			}
		} catch (Exception e) {
			vo.setMessage("系统异常!");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}
	
	
	/**
	 * dt红包优惠处理
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforRedCouponDT(String transID, String loginname, String couponType, String couponCode, String ip) {

		String result = null;
		log.info("begin to 红包优惠处理:" + couponCode + "玩家" + loginname);
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				vo.setMessage("用户不存在！");
				return vo;
			}
			if (customer.getFlag() == 1) {
				vo.setMessage("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			//dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[] {419 }));
			List<Proposal> list = findByCriteria(dc);
			Proposal proposal = null;
			if (list == null || list.size() <= 0) {
				vo.setMessage("优惠代码错误！");
				return vo;
			}
			else if(list !=null && list.size()>0){
				 proposal = list.get(0);
				if(proposal.getFlag()==2){
					vo.setMessage("优惠代码已使用！");
					return vo;
				}
				if(proposal.getFlag()==0){
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			// 获取提案信息
			if (!StringUtil.isEmpty(proposal.getLoginname())) {
				if (!(proposal.getLoginname().equals(loginname))) {
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			couponString = "红包优惠";
			String ProposalTypeText = ProposalType.REDCOUPON419.getText();
		
			String typeString = "";
			// 判断是哪个平台
			if (couponType.equals("dt")) {
				try {
					typeString = "优惠红包->DT";
					if (proposal.getType() == 419) {
						try {
							Double remoteCredit=null;
							//获取DT平台余额
							String money = DtUtil.getamount(loginname);
							if(null != money && NumberUtils.isNumber(money)){
								remoteCredit = Double.valueOf(money);
							}
							if (remoteCredit != null) {
								// 判断DT平台用户的金额是否
								log.info("remote credit:" + remoteCredit);
								if (remoteCredit > 5) {
									vo.setMessage("DT平台金额必须小于5,才能使用红包优惠劵！");
									return vo;
								}
								try {
									String remark = couponString + "送" + proposal.getGifTamount()+"元";
									Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, null, proposal.getGifTamount(), remark);
									String msg = this.transferInforDtIn(transID, loginname, proposal.getGifTamount(), ProposalTypeText, remark);
									if (null != msg) {
										vo.setMessage("DT优惠券使用失败：" + msg);
										return vo;
									}
									//this.save(offer);
								} catch (Exception e) {
									result = "获取异常失败";
									vo.setMessage(result);
									return vo;
								}
							} else {
								//return "转账金额失败！";
								vo.setMessage("转账金额失败！");
								return vo;
							}
						} catch (Exception e) {
							//return "获取异常失败";
							vo.setMessage("获取异常失败");
							return vo;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					vo.setMessage("系统异常");
					return vo;
				}
			}
			// 更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());		
			proposal.setRemark(typeString + "创建时间:" + proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			proposal.setAgent(customer.getAgent());
			update(proposal);
			// 记录下到目前为止的投注额度 begin
			String platform = "dt";
			//记录下到目前为止的投注额度(统计今天00:00:00到现在现在的投注额)
			/*String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0);
			this.save(record);*/
			
			//统计今天00:00:00到现在现在的投注额
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			Date startTime = cd.getTime();
		    Date endTime = new Date();
		    Double betAmount=0.00;
		    try {
		    	//查询投注额
		    	 List lists = DtUtil.getbet(loginname, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(new Date()));
				 if (null != lists && !lists.isEmpty()) {
					Object obj = lists.get(0);
					Map<String, Object> map = (Map<String, Object>) obj;
				    String betPrice = String.valueOf(map.get("betPrice"));
				    betAmount = Double.parseDouble(betPrice);
				  } 
		        System.out.println(betAmount+"--->投注额");
		       } catch (Exception e) {
		        e.printStackTrace();
		        log.error("查询:" + loginname + ",时间：" + DateUtil.formatDateForStandard(startTime)+ "-----" +  DateUtil.formatDateForStandard(endTime) + "DT投注额异常：", e);
		       }
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0);
			this.save(record);
			
			// 记录下到目前为止的投注额度 end
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			this.save(selfRecord);

			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			try {
				
					this.addTransferforDt(Long.parseLong(transID), loginname, customer.getCredit(), proposal.getGifTamount(), Constants.IN, Constants.FAIL, "", "转入成功");
					result = null;
					vo.setGiftMoney(proposal.getGifTamount());
					
			} catch (Exception e) {
				result = "DT优惠券使用失败，请联系客服！";
				e.printStackTrace();
				vo.setMessage(result);
				return vo;
			}
		} catch (Exception e) {
			vo.setMessage("系统异常!");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}
	
	/**
	 * 优惠劵处理TTG
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforCouponTtg(String transID, String loginname, String couponType, Double remit, String couponCode, String ip) {
		String result = null;
		log.info("begin to 优惠券处理:"+couponCode+"玩家"+  loginname);
		boolean creditReduce = false;
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				//return "用户不存在！";
				vo.setMessage("用户不存在！");
				return vo;
			}
			if(customer.getFlag()==1){
				//return "该账号已经禁用";
				vo.setMessage("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		//	dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[]{401,402,403,404}));
			List<Proposal> list = findByCriteria(dc);
			if (list == null || list.size() <= 0) {
				//return "优惠代码错误！";
				vo.setMessage("优惠代码错误！");
				return vo;
			}
			// 获取提案信息
			Proposal proposal = list.get(0);
			if(!StringUtil.isEmpty(proposal.getLoginname())){
				if(!(proposal.getLoginname().equals(loginname))){
					//return "优惠代码错误！";	
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			Double localCredit = customer.getCredit();
			Double amount = 0.0;
			Double gifTamount=0.0;
			String ProposalTypeText="";
			 if (proposal.getType() == 401 ||proposal.getType() == 402||proposal.getType() == 403||proposal.getType() == 404 ) {
				if(50>remit){
					//return "存送优惠卷,存款金额必须大于100！";
					vo.setMessage("存送优惠卷,存款金额必须大于50！");
					return vo;
				}
				if (localCredit < remit) {
					//return "账号额度不足！";
					vo.setMessage("账号额度不足！");
					return vo;
				}
				Integer type = proposal.getType();
				if (type == 401) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ401.getText();
					if (remit < 50) {
						//return "TTG100%最低转账金额是100";
						vo.setMessage("TTG100%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 1;
					if (gifTamount >= 588.00) {
						gifTamount = 588.00;
					}
					amount = remit + gifTamount;
					couponString = "TTG100%存送优惠券15倍流水";
				}else if (type == 402) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ402.getText();
					if (remit < 50) {
						//return "TTG88%最低转账金额是100";
						vo.setMessage("TTG88%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 0.88;
					if (gifTamount >= 888) {
						gifTamount =888.00;
					}
					amount = remit + gifTamount;
					couponString = "TTG88%存送优惠券";
				}else if (type == 403) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ403.getText();
					if (remit < 50) {
						//return "TTG68%最低转账金额是100";
						vo.setMessage("TTG68%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 0.68;
					if (gifTamount >= 1888) {
						gifTamount = 1888.00;
					}
					amount = remit + gifTamount;
					couponString = "TTG68%存送优惠券";
				}else if (type == 404) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ404.getText();
					if (remit < 50) {
						//return "TTG100%最低转账金额是100";
						vo.setMessage("TTG100%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 1;
					if (gifTamount >= 1000) {
						gifTamount = 1000.00;
					}
					amount = remit + gifTamount;
					couponString = "TTG100%存送优惠券20倍流水";
				}
			}else{
				//return "TTG不存在该类型优惠";
				vo.setMessage("TTG不存在该类型优惠");
				return vo;
			}
			String typeString="";
			// 判断是哪个平台
				try {
				typeString = "LONGDU->TTG";
					try {
							String remoteCreditStr = PtUtil1.getPlayerAccount(customer.getLoginname());
							Double remoteCredit=null;
							if(!StringUtils.isEmpty(remoteCreditStr)){
								remoteCredit=Double.parseDouble(remoteCreditStr);
							}
							if (remoteCredit != null) {
								// 判断TTG平台用户的金额是否
								log.info("remote credit:" + remoteCredit);
								if (remoteCredit > 5) {
									//return "TTG平台金额必须小于5,才能使用该优惠劵！";
									vo.setMessage("TTG平台金额必须小于5,才能使用该优惠劵！");
									return vo;
								}
								try {
										    String remark = couponString + proposal.getBetMultiples() + "倍流水，存" + remit + "送" + gifTamount;
											Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, amount, remark);
											String msg = this.transferInforTtgIn(transID, loginname, remit, ProposalTypeText, remark);
											this.save(offer);
											if(null!=msg){
												//return "TTG优惠券使用失败："+msg;
												vo.setMessage("TTG优惠券使用失败："+msg);
												return vo;
											}
								} catch (Exception e) {
										result = "获取异常失败";
										vo.setMessage(result);
										return vo;
								}
							} else {
								//return "转账金额失败！";
								vo.setMessage("转账金额失败！");
								return vo;
							}
					} catch (Exception e) {
							//return "获取异常失败";
							vo.setMessage("获取异常失败");
							return vo;
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				vo.setMessage("获取异常失败");
				return vo;
			}
			//更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());
			proposal.setAmount(remit);
			proposal.setGifTamount(gifTamount);
			proposal.setRemark(typeString+"创建时间:"+proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			if(StringUtil.isEmpty(proposal.getLoginname())){
				proposal.setLoginname(loginname);
			}
			proposal.setAgent(customer.getAgent());
			update(proposal);
			// 记录下到目前为止的投注额度 begin
			String platform = "ttg";
			//记录下到目前为止的投注额度
			//获取0点到领取时的ttg投注额
			String totalBetSql = "select bet from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			prams.put("platform", "ttg");
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0);
			this.save(record);
			// 记录下到目前为止的投注额度 end
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			this.save(selfRecord);
			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			try {
				//Boolean b = PtUtil1.addPlayerAccountPraper(loginname, amount);
				//if(null != b && b){
					this.addTransferforTt(Long.parseLong(transID), loginname, customer.getCredit(), remit, Constants.IN, Constants.FAIL, "", "转入成功");
					result=null;
					vo.setGiftMoney(amount);
				/*}else{
					result="TTG优惠券使用失败，请联系客服！";
					throw new GenericDfhRuntimeException("TTG惠券使用失败,数据回滚。");
				}*/
		} catch (Exception e) {
			result="TTG优惠券使用失败，请联系客服！";
			e.printStackTrace();
			vo.setMessage("TTG优惠券使用失败，请联系客服！");
			return vo;
		}
		}catch (Exception e) {
			vo.setMessage("TTG优惠券使用失败，请联系客服！");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}
	
	/**
	 * 优惠劵处理QT
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforCouponQT(String transID, String loginname, String couponType, Double remit, String couponCode, String ip) {
		String result = null;
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		log.info("begin to 优惠券处理:"+couponCode+"玩家"+  loginname);
		boolean creditReduce = false;
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				//return "用户不存在！";
				vo.setMessage("用户不存在！");
				return vo;
			}
			if(customer.getFlag()==1){
				//return "该账号已经禁用";
				vo.setMessage("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		//	dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[]{426,427,428,429}));
			List<Proposal> list = findByCriteria(dc);
			if (list == null || list.size() <= 0) {
				//return "优惠代码错误！";
				vo.setMessage("优惠代码错误！");
				return vo;
			}
			// 获取提案信息
			Proposal proposal = list.get(0);
			if(!StringUtil.isEmpty(proposal.getLoginname())){
				if(!(proposal.getLoginname().equals(loginname))){
					//return "优惠代码错误！";	
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			Double localCredit = customer.getCredit();
			Double amount = 0.0;
			Double gifTamount=0.0;
			String ProposalTypeText="";
			 if (proposal.getType() == 426 ||proposal.getType() == 427||proposal.getType() == 428||proposal.getType() == 429) {
				if(50>remit){
					//return "存送优惠卷,存款金额必须大于100！";
					vo.setMessage("存送优惠卷,存款金额必须大于50！");
					return vo;
				}
				if (localCredit < remit) {
					//return "账号额度不足！";
					vo.setMessage("账号额度不足！");
					return vo;
				}
				Integer type = proposal.getType();
				if (type == 426) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ426.getText();
					if (remit < 50) {
						//return "QT100%最低转账金额是100";
						vo.setMessage("QT100%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 1;
					if (gifTamount >= 588.00) {
						gifTamount = 588.00;
					}
					amount = remit + gifTamount;
					couponString = "QT100%存送优惠券15倍流水";
				}else if (type == 427) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ427.getText();
					if (remit < 50) {
						//return "QT88%最低转账金额是100";
						vo.setMessage("QT88%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 0.88;
					if (gifTamount >= 888) {
						gifTamount =888.00;
					}
					amount = remit + gifTamount;
					couponString = "QT88%存送优惠券";
				}else if (type == 428) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ428.getText();
					if (remit < 50) {
						//return "QT68%最低转账金额是100";
						vo.setMessage("QT68%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 0.68;
					if (gifTamount >= 1888) {
						gifTamount = 1888.00;
					}
					amount = remit + gifTamount;
					couponString = "QT68%存送优惠券";
				}else if (type == 429) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ429.getText();
					if (remit < 50) {
						//return "QT100%最低转账金额是100";
						vo.setMessage("QT100%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 1;
					if (gifTamount >= 1000) {
						gifTamount = 1000.00;
					}
					amount = remit + gifTamount;
					couponString = "QT100%存送优惠券20倍流水";
				}else {
					//return "不存在该类型优惠";
					vo.setMessage("不存在该类型优惠");
					return vo;
				}
			}else{
				//return "QT不存在该类型优惠";
				vo.setMessage("不存在该类型优惠");
				return vo;
			}
			String typeString="";
			// 判断是哪个平台
				try {
				typeString = "LONGDU->QT";
					try {
							String remoteCreditStr = QtUtil.getBalance(customer.getLoginname());
							Double remoteCredit=null;
							if(remoteCreditStr != null && NumberUtils.isNumber(remoteCreditStr)){
								remoteCredit=Double.parseDouble(remoteCreditStr);
							}
							if (remoteCredit != null) {
								// 判断QT平台用户的金额是否
								log.info("remote credit:" + remoteCredit);
								if (remoteCredit > 5) {
									//return "QT平台金额必须小于5,才能使用该优惠劵！";
									vo.setMessage("QT平台金额必须小于5,才能使用该优惠劵！");
									return vo;
								}
								try {
										    String remark = couponString + proposal.getBetMultiples() + "倍流水，存" + remit + "送" + gifTamount;
											Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, amount, remark);
											String msg = this.transferInforQtIn(transID, loginname, remit, ProposalTypeText, remark);
											if(null!=msg){
												//return "QT优惠券使用失败："+msg;
												vo.setMessage("QT优惠券使用失败："+msg);
												return vo;
											}
											this.save(offer);
								} catch (Exception e) {
										//return "获取异常失败";
										vo.setMessage( "获取异常失败");
										return vo;
								}
							} else {
								//return "转账金额失败！";
								vo.setMessage( "转账金额失败！");
								return vo;
							}
					} catch (Exception e) {
							//return "获取异常失败";
							vo.setMessage( "获取异常失败");
							return vo;
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				vo.setMessage( "系统异常");
				return vo;
			}
			//更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());
			proposal.setAmount(remit);
			proposal.setGifTamount(gifTamount);
			proposal.setRemark(typeString+"创建时间:"+proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			if(StringUtil.isEmpty(proposal.getLoginname())){
				proposal.setLoginname(loginname);
			}
			proposal.setAgent(customer.getAgent());
			update(proposal);
			// 记录下到目前为止的投注额度 begin
			String platform = "qt";
			//记录下到目前为止的投注额度
			//获取0点到领取时的qt投注额
			String totalBetSql = "select bet from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			prams.put("platform", "qt");
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0);
			this.save(record);
			// 记录下到目前为止的投注额度 end
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			this.save(selfRecord);
			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			try {
				//String b = QtUtil.getDepositPlayerMoney(loginname, amount, transID);
				vo.setGiftMoney(amount);
				//if(null != b && QtUtil.RESULT_SUCC.equals(b)){
					this.addTransferforQt(Long.parseLong(transID), loginname, customer.getCredit(), remit, Constants.IN, Constants.FAIL, "", "转入成功");
					result=null;
				/*}else{
					result="QT优惠券使用失败，请联系客服！";
					throw new GenericDfhRuntimeException("QT惠券使用失败,数据回滚。");
				}*/
					vo.setGiftMoney(amount);
		} catch (Exception e) {
			result="QT优惠券使用失败，请联系客服！";
			e.printStackTrace();
			vo.setMessage(result);
			return vo;
		}
		}catch (Exception e) {
			vo.setMessage("系统异常");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}
	
	/**
	 * 优惠劵处理TTG
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforCouponNT(String transID, String loginname, String couponType, Double remit, String couponCode, String ip) {
		String result = null;
		log.info("begin to 优惠券处理:"+couponCode+"玩家"+  loginname);
		boolean creditReduce = false;
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				//return "用户不存在！";
				vo.setMessage("用户不存在！");
				return vo;
			}
			if(customer.getFlag()==1){
				//return "该账号已经禁用";
				vo.setMessage("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		//	dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[]{425,422,423,424}));
			List<Proposal> list = findByCriteria(dc);
			if (list == null || list.size() <= 0) {
				//return "优惠代码错误！";
				vo.setMessage("优惠代码错误！");
				return vo;
			}
			// 获取提案信息
			Proposal proposal = list.get(0);
			if(!StringUtil.isEmpty(proposal.getLoginname())){
				if(!(proposal.getLoginname().equals(loginname))){
					//return "优惠代码错误！";	
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			Double localCredit = customer.getCredit();
			Double amount = 0.0;
			Double gifTamount=0.0;
			String ProposalTypeText="";
			 if (proposal.getType() == 425 ||proposal.getType() == 422||proposal.getType() == 423||proposal.getType() == 424 ) {
				if(50>remit){
					//return "存送优惠卷,存款金额必须大于100！";
					vo.setMessage("存送优惠卷,存款金额必须大于50！");
					return vo;
				}
				if (localCredit < remit) {
					//return "账号额度不足！";
					vo.setMessage("账号额度不足！");
					return vo;
				}
				Integer type = proposal.getType();
				if (type == 425) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ425.getText();
					if (remit < 50) {
						//return "NT100%最低转账金额是100";
						vo.setMessage("NT100%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 1;
					if (gifTamount >= 588.00) {
						gifTamount = 588.00;
					}
					amount = remit + gifTamount;
					couponString = "NT100%存送优惠券15倍流水";
				}else if (type == 422) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ422.getText();
					if (remit < 50) {
						//return "NT88%最低转账金额是100";
						vo.setMessage("NT88%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 0.88;
					if (gifTamount >= 888) {
						gifTamount =888.00;
					}
					amount = remit + gifTamount;
					couponString = "NT88%存送优惠券";
				}else if (type == 423) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ423.getText();
					if (remit < 50) {
						//return "NT68%最低转账金额是100";
						vo.setMessage("NT68%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 0.68;
					if (gifTamount >= 1888) {
						gifTamount = 1888.00;
					}
					amount = remit + gifTamount;
					couponString = "NT68%存送优惠券";
				}else if (type == 424) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ424.getText();
					if (remit < 50) {
						//return "NT100%最低转账金额是100";
						vo.setMessage("NT100%最低转账金额是50");
						return vo;
					}
					gifTamount = remit * 1;
					if (gifTamount >= 1000) {
						gifTamount = 1000.00;
					}
					amount = remit + gifTamount;
					couponString = "NT100%存送优惠券20倍流水";
				}
			}else{
				//return "NT不存在该类型优惠";
				vo.setMessage("NT不存在该类型优惠");
				return vo;
			}
			String typeString="";
			// 判断是哪个平台
			try {
			typeString = "LONGDU->NT";
				try {
					Double remoteCredit=null;
					JSONObject ntm = JSONObject.fromObject(NTUtils.getNTMoney(customer.getLoginname()));
					if (ntm.getBoolean("result")){
						remoteCredit=ntm.getDouble("balance");
					}
					if (remoteCredit != null) {
						// 判断NT平台用户的金额是否
						log.info("remote credit:" + remoteCredit);
						if (remoteCredit > 5) {
							//return "NT平台金额必须小于5,才能使用该优惠劵！";
							vo.setMessage("NT平台金额必须小于5,才能使用该优惠劵！");
							return vo;
						}
						try {
						    String remark = couponString + proposal.getBetMultiples() + "倍流水，存" + remit + "送" + gifTamount;
							Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, amount, remark);
							String msg = this.transferToNTJudge(transID, loginname, remit, ProposalTypeText, remark);
							if(null!=msg){
								//return "NT优惠券使用失败："+msg;
								vo.setMessage("NT优惠券使用失败："+msg);
								return vo;
							}
							this.save(offer);
						} catch (Exception e) {
								result = "获取异常失败";
								vo.setMessage(result);
								return vo;
						}
					} else {
						//return "转账金额失败！";
						vo.setMessage("转账金额失败！");
						return vo;
					}
				} catch (Exception e) {
						//return "获取异常失败";
						vo.setMessage("获取异常失败");
						return vo;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				vo.setMessage("系统异常");
				return vo;
			}
			//更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());
			proposal.setAmount(remit);
			proposal.setGifTamount(gifTamount);
			proposal.setRemark(typeString+"创建时间:"+proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			if(StringUtil.isEmpty(proposal.getLoginname())){
				proposal.setLoginname(loginname);
			}
			proposal.setAgent(customer.getAgent());
			update(proposal);
			// 记录下到目前为止的投注额度 begin
			String platform = "nt";
			//记录下到目前为止的投注额度
			//获取0点到领取时的ttg投注额
			String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			//prams.put("platform", "ttg");
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0);
			this.save(record);
			// 记录下到目前为止的投注额度 end
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(ProposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			this.save(selfRecord);
			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			try {
				//JSONObject cm=JSONObject.fromObject(NTUtils.changeMoney(loginname, amount));
				//if (cm.getBoolean("result")){
					this.addTransferforNT(Long.parseLong(transID), loginname, customer.getCredit(), remit, Constants.IN, Constants.FAIL, "", "转入成功");
					result=null;
				/*}else{
					result="NT优惠券使用失败，请联系客服！"+cm.getString("error");
					throw new GenericDfhRuntimeException("NT惠券使用失败,数据回滚。");
				}*/
					vo.setGiftMoney(amount);
		} catch (Exception e) {
			//result="NT优惠券使用失败，请联系客服！";
			log.error("NT优惠劵使用失败，方法transferInforCouponNT", e);
			vo.setMessage("NT优惠券使用失败，请联系客服！");
			return vo;
		}
		}catch (Exception e) {
			vo.setMessage("系统异常");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}
	
	/**
	 * 优惠劵处理GPI
	 */
	@SuppressWarnings("unchecked")
	public String transferInforCouponGpi(String transID, String loginname, String couponType, Double remit, String couponCode, String ip) {
		String result = null;
		log.info("begin to 优惠券处理:"+couponCode+"玩家"+  loginname);
		boolean creditReduce = false;
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				return "用户不存在！";
			}
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			//dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[]{405,406,407,408}));
			List<Proposal> list = findByCriteria(dc);
			if (list == null || list.size() <= 0) {
				return "优惠代码错误！";
			}
			//查询后台是否有手动提交再存优惠
			DetachedCriteria offerDc = DetachedCriteria.forClass(Proposal.class);
			offerDc.add(Restrictions.eq("type", ProposalType.OFFER.getCode()));
			offerDc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			offerDc.add(Restrictions.eq("loginname", customer.getLoginname()));
			offerDc.add(Restrictions.eq("remark", "BEGIN"+customer.getLoginname()));
			List<Proposal> offers = this.findByCriteria(offerDc);
			if(null != offers && offers.size()>0){
				return "您正在使用手工再存优惠,不能使用自助再存。" ;
			}
			// 获取提案信息
			Proposal proposal = list.get(0);
			if(!StringUtil.isEmpty(proposal.getLoginname())){
				if(!(proposal.getLoginname().equals(loginname))){
					return "优惠代码错误！";	
				}
			}
			Double localCredit = customer.getCredit();
			Double amount = 0.0;//转入gpi金额
			Double gifTamount=0.0;//送的金额
			String ProposalTypeText="";
				if(100>remit){
					return "存送优惠卷,存款金额必须大于100！";
				}
				if (localCredit < remit) {
					return "账号额度不足！";
				}
				Integer type = proposal.getType();
				if (type == 405) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ405.getText();
					if (remit < 100) {
						return "GPI100%最低转账金额是100";
					}
					gifTamount = remit * 1;
					if (gifTamount >= 588.00) {
						gifTamount = 588.00;
					}
					amount = remit + gifTamount;
					couponString = "GPI100%存送优惠券15倍流水";
				}else if (type == 406) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ406.getText();
					if (remit < 100) {
						return "GPI88%最低转账金额是100";
					}
					gifTamount = remit * 0.88;
					if (gifTamount >= 888) {
						gifTamount =888.00;
					}
					amount = remit + gifTamount;
					couponString = "GPI88%存送优惠券";
				}else if (type == 407) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ407.getText();
					if (remit < 100) {
						return "GPI68%最低转账金额是100";
					}
					gifTamount = remit * 0.68;
					if (gifTamount >= 1888) {
						gifTamount = 1888.00;
					}
					amount = remit + gifTamount;
					couponString = "GPI68%存送优惠券";
				}else if (type == 408) {
					ProposalTypeText=ProposalType.COUPONCSYHYHJ408.getText();
					if (remit < 100) {
						return "GPI100%最低转账金额是100";
					}
					gifTamount = remit * 1;
					if (gifTamount >= 1000.00) {
						gifTamount = 1000.00;
					}
					amount = remit + gifTamount;
					couponString = "GPI100%存送优惠券20倍流水";
				}
				
			String typeString="LONGDU->GPI";
					try {
							Double remoteCredit=GPIUtil.getBalance(customer.getLoginname());
							if (remoteCredit != null) {
								// 判断GPI平台用户的金额是否
								log.info("remote credit:" + remoteCredit);
								if (remoteCredit > 5) {
									return "GPI平台金额必须小于5,才能使用该优惠劵！";
								}
								try {
										    String remark = couponString + proposal.getBetMultiples() + "倍流水，存" + remit + "送" + gifTamount;
											String msg = this.selfConpon4GPI(transID, customer, remit,remark) ;
											Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, amount, remark);
											this.save(offer);
											if(null!=msg){
												return "GPI优惠券使用失败："+msg;
											}
								} catch (Exception e) {
										result = "获取异常失败";
								}
							} else {
								return "转账金额失败！";
							}
					} catch (Exception e) {
							return "获取异常失败";
					}
			//更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());
			proposal.setAmount(remit);
			proposal.setGifTamount(gifTamount);
			proposal.setRemark(typeString+"创建时间:"+proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			if(StringUtil.isEmpty(proposal.getLoginname())){
				proposal.setLoginname(loginname);
			}
			proposal.setAgent(customer.getAgent());
			update(proposal);

			//记录下到目前为止的投注额度
			//获取0点到领取时的GPI投注额
			String totalBetSql = "select sum(bet) from platform_data where loginname=:username and starttime>=:startTime and platform in(:gpi, :rslot, :png, :bs, :ctxm)";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", proposal.getLoginname());
			Calendar cd = Calendar.getInstance();
			cd.setTime(new Date());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			//prams.put("platform", "gpi");
			prams.put("gpi", "gpi");
			prams.put("rslot", "rslot");
			prams.put("png", "png");
			prams.put("bs", "bs");
			prams.put("ctxm", "ctxm");
			
			Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);
			PreferentialRecord record = new PreferentialRecord(proposal.getPno() , loginname , "gpi" , betAmount, new Date() , 0) ;
			
			this.save(record);
				
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform("gpi");
			selfRecord.setSelfname(ProposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			this.save(selfRecord);
			
			
			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			
			String deposit = GPIUtil.credit(loginname, amount, transID);
			if (deposit != null && deposit.equals(GPIUtil.GPI_SUCCESS_CODE)) {
				return null;
			} else {
				return "GPI优惠券使用失败，请联系客服！" ;
			}
			
			
		}catch (Exception e) {
			if (creditReduce)
				result = "转账发生异常 :" + e.getMessage();
			else
				result = "系统繁忙，请重新尝试";
			if (e instanceof RemoteApiException){
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		return result;
	}
	
	
	/**
	 * 优惠劵处理
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforCoupon(String transID, String loginname, String couponType, Double remit, String couponCode, String ip) {
		String result = null;
		log.info("begin to transferOutforBbin:" + loginname);
		boolean creditReduce = false;
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		try {
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				//return "用户不存在！";
				vo.setMessage("用户不存在！");
				return vo;

			}
			if(customer.getFlag()==1){
				//return "该账号已经禁用";
				vo.setMessage("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			//dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[] { 531, 532, 533, 534, 535, 536, 571, 572, 573,409,410,411,412,390,391,574,415}));
			List<Proposal> list = findByCriteria(dc); 
			if (list == null || list.size() <= 0) {
				//return "优惠代码错误！";
				vo.setMessage("优惠代码错误！");
				return vo;
			}
			// 获取提案信息
			Proposal proposal = list.get(0);
			if(!StringUtil.isEmpty(proposal.getLoginname())){
				if(!(proposal.getLoginname().equals(loginname))){
					//return "优惠代码错误！";
					vo.setMessage("优惠代码错误！");
					return vo;
				}
			}
			// 判断优惠类型 (537, "邀请码"),(536,"红包优惠券"),(531, "10%存送优惠券"),(532,
			// "20%存送优惠券"),(533, "30%存送优惠券"),(534, "40%存送优惠券"),(535,
			// "50%存送优惠券");
			Double localCredit = customer.getCredit();
			// 存送优惠劵只能是EA
			if (proposal.getType() != 536 && proposal.getType() != 571) {
				if (!couponType.equals("ea") && !couponType.equals("pt")) {
					//return "只能是EA或者PT平台才能使用存送优惠劵！";
					vo.setMessage("只能是EA或者PT平台才能使用存送优惠劵！");
					return vo;
				}
			}
			if (proposal.getType() == 531 || proposal.getType() == 532 || proposal.getType() == 533 || proposal.getType() == 534 || proposal.getType() == 535) {
				if (couponType.equals("pt")) {
					//return "该优惠码是EA平台优惠码!平台选择错误！";
					vo.setMessage("该优惠码是EA平台优惠码!平台选择错误！");
					return vo;
				}
			} else if (proposal.getType() == 571 || proposal.getType() == 572 || proposal.getType() == 573|| proposal.getType() == 574|| proposal.getType() == 409|| proposal.getType() == 410|| proposal.getType() == 411|| proposal.getType() == 412|| proposal.getType() == 419) {
				if (couponType.equals("ea")) {
					//return "该优惠码是PT平台优惠码!平台选择错误！";
					vo.setMessage("该优惠码是PT平台优惠码!平台选择错误！");
					return vo;
				}
			}
			Double amount = 0.0;
			Double gifTamount = 0.0;
			if (proposal.getType() == 531 || proposal.getType() == 532 || proposal.getType() == 533 || proposal.getType() == 534 || proposal.getType() == 535) {
				if(0>=remit){
					//return "存送优惠代码,存款金额必须大于0！";
					vo.setMessage("存送优惠代码,存款金额必须大于0！");
					return vo;
				}
				if (localCredit < remit) {
					//return "账号额度不足！";
					vo.setMessage("账号额度不足！");
					return vo;
				}
				// 公式类型为：（本金+红利）*投注倍数要求=总投注额要求
				// 531 10%投注默认为8倍，进行户内转账的时候赠送上限为888
				Integer type = proposal.getType();
				if (type == 531) {
					if (remit > 8880.00) {
						//return "10%最高转账金额是8880";
						vo.setMessage("10%最高转账金额是8880");
						return vo;
					}
					gifTamount = remit * 0.1;
					if (gifTamount >= 888.00) {
						gifTamount = 888.00;
					}
					amount = remit + gifTamount;
					couponString = "10%存送优惠券";
				}
				// 532 20%存送优惠券的默认投注倍数为10倍，最高1888
				else if (type == 532) {
					if (remit > 9440.00) {
						//return "20%最高转账金额是9440";
						vo.setMessage("20%最高转账金额是9440");
						return vo;
					}
					gifTamount = remit * 0.2;
					if (gifTamount >= 1888.00) {
						gifTamount = 1888.00;
					}
					amount = remit + gifTamount;
					couponString = "20%存送优惠券";
				}
				// 533 30%存送优惠券的默认投注倍数为15，最高3888
				else if (type == 533) {
					if (remit > 12960.00) {
						//return "30%最高转账金额是12960";
						vo.setMessage("30%最高转账金额是12960");
						return vo;
					}
					gifTamount = remit * 0.3;
					if (gifTamount >= 3888.00) {
						gifTamount = 3888.00;
					}
					amount = remit + gifTamount;
					couponString = "30%存送优惠券";
				}
				// 534 40%存送优惠券的默认投注倍数为18，最高5888
				else if (type == 534) {
					if (remit > 14720.00) {
						//return "40%最高转账金额是14720";
						vo.setMessage("40%最高转账金额是14720");
						return vo;
					}
					gifTamount = remit * 0.4;
					if (gifTamount >= 5888.00) {
						gifTamount = 5888.00;
					}
					amount = remit + gifTamount;
					couponString = "40%存送优惠券";
				}
				// 535 50%存送优惠券的默认投注倍数为20，最高8888
				else if (type == 535) {
					if (remit > 17776.00) {
						//return "50%最高转账金额是17776";
						vo.setMessage("50%最高转账金额是17776");
						return vo;
					}
					gifTamount = remit * 0.5;
					if (gifTamount >= 8888.00) {
						gifTamount = 8888.00;
					}
					amount = remit + gifTamount;
					couponString = "50%存送优惠券";
				}
			} else if (proposal.getType() == 572 || proposal.getType() == 573|| proposal.getType() == 409|| proposal.getType() == 410|| proposal.getType() == 411|| proposal.getType() == 412|| proposal.getType() == 419) {
				if(!StringUtil.isEmpty(proposal.getLoginname())){
					if(!(proposal.getLoginname().equals(loginname))){
						//return "优惠代码错误！";	
						vo.setMessage("优惠代码错误！");
						return vo;
					}
				}
				if (localCredit < remit) {
					//return "账号额度不足！";
					vo.setMessage("账号额度不足！");
					return vo;
				}
				Integer type = proposal.getType();
				if (type == 572) {
					if (remit < 200) {
						//return "PT68%最低转账金额是200";
						vo.setMessage("PT68%最低转账金额是200");
						return vo;
					}
					if (remit > 1305.00) {
						//return "PT68%最高转账金额是1305";
						vo.setMessage("PT68%最高转账金额是1305");
						return vo;
					}
					gifTamount = remit * 0.68;
					if (gifTamount >= 888.00) {
						gifTamount = 888.00;
					}
					amount = remit + gifTamount;
					couponString = "68%存送优惠券";
				}
				// 533 30%存送优惠券的默认投注倍数为15，最高3888
				else if (type == 573) {
					if (remit < 100) {
						//return "PT100%最低转账金额是100";
						vo.setMessage("PT100%最低转账金额是100");
						return vo;
					}
					if (remit > 2888) {
						//return "PT100%最高转账金额是2888";
						vo.setMessage("PT100%最高转账金额是2888");
						return vo;
					}
					gifTamount = remit * 1;
					if (gifTamount >= 2888) {
						gifTamount = 2888.00;
					}
					amount = remit + gifTamount;
					couponString = "PT100%存送优惠券";
				}else if(type == 409){

					if (remit < 50) {
						//return "PT100%最低转账金额是100";
						vo.setMessage("PT100%最低转账金额是50");
						return vo;
					}
					gifTamount = Arith.mul(remit , 1);
					if (gifTamount >= 588) {
						gifTamount = 588.00;
					}
					amount = remit + gifTamount;
					couponString = "PT100%存送优惠券15倍流水";
				}else if(type == 410){

					if (remit < 50) {
						//return "PT88%最低转账金额是100";
						vo.setMessage("PT88%最低转账金额是50");
						return vo;
					}
					gifTamount = Arith.mul(remit , 0.88);
					if (gifTamount >= 888) {
						gifTamount = 888.00;
					}
					amount = remit + gifTamount;
					couponString = "PT88%存送优惠券";
				}else if(type == 411){
					if (remit < 50) {
						//return "PT68%最低转账金额是100";
						vo.setMessage("PT68%最低转账金额是50");
						return vo;
					}
					gifTamount = Arith.mul(remit , 0.68);
					if (gifTamount >= 1888) {
						gifTamount = 1888.00;
					}
					amount = remit + gifTamount;
					couponString = "PT68%存送优惠券";
				}else if(type == 412){
					if (remit < 50) {
						//return "PT100%最低转账金额是100";
						vo.setMessage("PT100%最低转账金额是50");
						return vo;
					}
					gifTamount = Arith.mul(remit , 1);
					if (gifTamount >= 1000) {
						gifTamount = 1000.00;
					}
					amount = remit + gifTamount;
					couponString = "PT100%存送优惠券20倍流水";
				}
			} else {
				// 转入游戏金额=存入金额+赠送金额
				remit = 0.0;
				amount = proposal.getGifTamount();
				Integer type = proposal.getType();
				if(type == 571){
					couponString = "PT红包优惠券";
				}else{
					couponString = "红包优惠券";
				}
				
			}
			String typeString = "";
			// 判断是哪个平台
			if (couponType.equals("ea")) {
				return null;
				/*
				if(proposal.getType() == 571){
					//return "该红包优惠劵是PT红包优惠劵，只能转入PT平台！";
					vo.setMessage("该红包优惠劵是PT红包优惠劵，只能转入PT平台！");
					return vo;
				}
				typeString = "LONGDU->EA";
				if (proposal.getType() != 536) {
					// 判断EA平台用户的金额是否
					Double remoteCredit = RemoteCaller.queryCredit(loginname);
					log.info("remote credit:" + remoteCredit);
					if (remoteCredit >= 20) {
						//return "EA平台金额必须小于20,才能使用该优惠劵！";
						vo.setMessage("EA平台金额必须小于20,才能使用该优惠劵！");
						return vo;
					}
					DepositPendingResponseBean depositPendingResponseBean = RemoteCaller.depositPendingRequest(customer.getLoginname(), amount, transID, customer.getAgcode());
					// 如果操作成功
					if (depositPendingResponseBean != null && depositPendingResponseBean.getStatus().equals(ErrorCode.SUCCESS.getCode())) {
						Transfer transfer = transferDao.addTransfer(Long.parseLong(transID), loginname, localCredit, amount, Constants.IN, Constants.FAIL, depositPendingResponseBean.getPaymentid(), null);
						tradeDao.changeCreditCouponEa(loginname, remit * -1, CreditChangeType.TRANSFER_IN_COUPON.getCode(), transID, couponString + ":" + "游戏金额(" + amount + ")=存入金额(" + remit + ") + 优惠劵金额(" + gifTamount + ")", proposal.getShippingCode());
						// 标记额度已被扣除
						creditReduce = true;
						try {
							RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), customer.getAgcode());
						} catch (Exception e) {
							log.warn("depositConfirmationResponse try again!");
							try {
								RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), depositPendingResponseBean.getAgcode());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();

								vo.setMessage("系统异常");
								return vo;
							}
						}
						transfer.setFlag(Constants.FLAG_TRUE);
						transfer.setRemark("转入成功");
						update(transfer);
					} else {
						//return "转账状态错误:" + ErrorCode.getChText(depositPendingResponseBean.getStatus());
						vo.setMessage("转账状态错误:" + ErrorCode.getChText(depositPendingResponseBean.getStatus()));
						return vo;
					}
				} else {
					// 判断上一个优惠码是否用完
					if (customer.getShippingcode() == null || customer.getShippingcode().equals("")) {

					} else {
						Double remoteCredit = RemoteCaller.queryCredit(loginname);
						log.info("remote credit:" + remoteCredit);
						if (remoteCredit >= 20) {
							//return "必须把上一个优惠代码" + customer.getShippingcode() + "用完过后，才能使用！";
							vo.setMessage("必须把上一个优惠代码" + customer.getShippingcode() + "用完过后，才能使用！");
							return vo;
						}
					}
					DepositPendingResponseBean depositPendingResponseBean = RemoteCaller.depositPendingRequest(customer.getLoginname(), amount, transID, customer.getAgcode());
					// 如果操作成功
					if (depositPendingResponseBean != null && depositPendingResponseBean.getStatus().equals(ErrorCode.SUCCESS.getCode())) {
						Transfer transfer = transferDao.addTransfer(Long.parseLong(transID), loginname, localCredit, amount, Constants.IN, Constants.FAIL, depositPendingResponseBean.getPaymentid(), null);
						tradeDao.changeCreditCouponEa(loginname, remit * -1, CreditChangeType.TRANSFER_IN_COUPON.getCode(), transID, couponString + ":" + "游戏金额(" + amount + ")=存入金额(" + proposal.getAmount() + ") + 优惠劵金额(" + proposal.getGifTamount() + ")", null);
						// 标记额度已被扣除
						creditReduce = true;
						try {
							RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), customer.getAgcode());
						} catch (Exception e) {
							log.warn("depositConfirmationResponse try again!");
							try {
								RemoteCaller.depositConfirmationResponse(depositPendingResponseBean.getId(), ErrorCode.SUCCESS.getCode(), depositPendingResponseBean.getPaymentid(), depositPendingResponseBean.getAgcode());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								vo.setMessage("系统异常");
								return vo;
							}
						}
						transfer.setFlag(Constants.FLAG_TRUE);
						transfer.setRemark("转入成功");
						update(transfer);
					} else {
						//return "转账状态错误:" + ErrorCode.getChText(depositPendingResponseBean.getStatus());
						vo.setMessage("转账状态错误:" + ErrorCode.getChText(depositPendingResponseBean.getStatus()));
						return vo;
					}
				}
			*/} else if (couponType.equals("ag")) {
				return null;
				/*
				if(proposal.getType() == 571){
					//return "该红包优惠劵是PT红包优惠劵，只能转入PT平台！";
					vo.setMessage("该红包优惠劵是PT红包优惠劵，只能转入PT平台！");
					return vo;
				}
				typeString = "LONGDU->AG";
				DspResponseBean dspResponseBean = RemoteCaller.depositPrepareDspRequest(loginname, amount, transID);
				DspResponseBean dspConfirmResponseBean = null;
				if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
					transferDao.addTransferforDsp(Long.parseLong(transID), loginname, localCredit, amount, Constants.IN, Constants.FAIL, "", "预备转入");
					tradeDao.changeCreditCoupon(loginname, remit * -1, CreditChangeType.TRANSFER_DSPIN_COUPON.getCode(), transID, couponString + ":" + "游戏金额(" + amount + ")=存入金额(" + proposal.getAmount() + ") + 优惠劵金额(" + proposal.getGifTamount() + ")");
					creditReduce = true;
					try {
						dspConfirmResponseBean = RemoteCaller.depositConfirmDspRequest(loginname, amount, transID, 1);
					} catch (Exception e) {
						log.warn("dspConfirmResponseBean try again!");
						try {
							dspConfirmResponseBean = RemoteCaller.depositConfirmDspRequest(loginname, amount, transID, 1);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							vo.setMessage("系统异常");
							return vo;
						}
					}
					if (dspConfirmResponseBean == null || dspConfirmResponseBean.getInfo().equals("1")) {
						//return "转账发生异常,请稍后再试或联系在线客服";
						vo.setMessage("转账发生异常,请稍后再试或联系在线客服");
						return vo;
					} else if(dspConfirmResponseBean.getInfo().equals("0")){
						Transfer confirmtransfer = (Transfer) get(Transfer.class, Long.parseLong(transID), LockMode.UPGRADE);
						confirmtransfer.setFlag(0);
						confirmtransfer.setRemark("转入成功");
						tradeDao.update(confirmtransfer);
					}else{
						//return dspConfirmResponseBean.getInfo() ;
						vo.setMessage(dspConfirmResponseBean.getInfo());
						return vo;
					}
				} else {
					dspConfirmResponseBean = RemoteCaller.depositConfirmDspRequest(loginname, amount, transID, 0);
					//return "转账状态错误:" + dspResponseBean.getInfo();
					vo.setMessage("转账状态错误:" + dspResponseBean.getInfo());
					return vo;
				}
			*/} else if (couponType.equals("agin")) {
				return null;
				/*
				if(proposal.getType() == 571){
					//return "该红包优惠劵是PT红包优惠劵，只能转入PT平台！";
					vo.setMessage("该红包优惠劵是PT红包优惠劵，只能转入PT平台！");
					return vo;
				}
				typeString = "LONGDU->AGIN";
				DspResponseBean dspResponseBean = RemoteCaller.depositPrepareDspAginRequest(loginname, amount, transID);
				DspResponseBean dspConfirmResponseBean = null;
				if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
					transferDao.addTransferforAginDsp(Long.parseLong(transID), loginname, localCredit, amount, Constants.IN, Constants.FAIL, "", "预备转入");
					tradeDao.changeCreditCoupon(loginname, remit * -1, CreditChangeType.TRANSFER_DSPAGIN_COUPON.getCode(), transID, couponString + ":" + "游戏金额(" + amount + ")=存入金额(" + proposal.getAmount() + ") + 优惠劵金额(" + proposal.getGifTamount() + ")");
					creditReduce = true;
					try {
						dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, amount, transID, 1);
					} catch (Exception e) {
						log.warn("dspConfirmResponseBean try again!");
						try {
							dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, amount, transID, 1);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							vo.setMessage("系统异常");
							return vo;
						}
					}
					if (dspConfirmResponseBean == null || dspConfirmResponseBean.getInfo().equals("1")) {
						//return "转账发生异常,请稍后再试或联系在线客服";
						vo.setMessage("转账发生异常,请稍后再试或联系在线客服");
						return vo;
					} else if(dspConfirmResponseBean.getInfo().equals("0")){
						Transfer confirmtransfer = (Transfer) get(Transfer.class, Long.parseLong(transID), LockMode.UPGRADE);
						confirmtransfer.setFlag(0);
						confirmtransfer.setRemark("转入成功");
						tradeDao.update(confirmtransfer);
					}else{
						//return dspConfirmResponseBean.getInfo();

						vo.setMessage(dspConfirmResponseBean.getInfo());
						return vo;
					}
				} else {
					dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, amount, transID, 0);
					//return "转账状态错误:" + dspResponseBean.getInfo();
					vo.setMessage( "转账状态错误:" + dspResponseBean.getInfo());
					return vo;
				}
			*/} else if (couponType.equals("keno")) {
				return null;
				/*
				try {
					if(proposal.getType() == 571){
						//return "该红包优惠劵是PT红包优惠劵，只能转入PT平台！";
						vo.setMessage("该红包优惠劵是PT红包优惠劵，只能转入PT平台！");
						return vo;
					}
					typeString = "LONGDU->KENO";
					KenoResponseBean bean = null;
					bean = DocumentParser.parseKenologinResponseRequest(KenoUtil.transferfirst(loginname, amount, "", ""));
					if (bean != null) {
						if (bean.getName() != null && bean.getName().equals("Error")) {
							//return "转账失败  : " + bean.getValue();
							vo.setMessage( "转账失败  : " + bean.getValue());
							return vo;
						} else if (bean.getName() != null && bean.getName().equals("FundIntegrationId")) {
							Integer FundIntegrationId = bean.getFundIntegrationId();
							if (FundIntegrationId != null) {
								KenoResponseBean confirmbean = DocumentParser.parseKenologinResponseRequest(KenoUtil.transferconfirm(loginname, amount, ip, FundIntegrationId, transID));
								if (confirmbean.getName() != null && confirmbean.getName().equals("Remain")) {
									transferDao.addTransferforKneo(Long.parseLong(transID), loginname, localCredit, amount, Constants.IN, Constants.SUCESS, "", "转入成功");
									tradeDao.changeCreditCoupon(loginname, -remit, CreditChangeType.TRANSFER_KENOIN_COUPON.getCode(), transID, couponString + ":" + "游戏金额(" + amount + ")=存入金额(" + proposal.getAmount() + ") + 优惠劵金额(" + proposal.getGifTamount() + ")");
								} else {
									//return "转账失败  : " + confirmbean.getValue();
									vo.setMessage("转账失败  : " + confirmbean.getValue());
									return vo;
								}
							}
						}
					}
					creditReduce = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					vo.setMessage("系统异常");
					return vo;
				}
			*/} else if (couponType.equals("bbin")) {
				return null;
				/*
				try {
					if(proposal.getType() == 571){
						//return "该红包优惠劵是PT红包优惠劵，只能转入PT平台！";
						vo.setMessage("该红包优惠劵是PT红包优惠劵，只能转入PT平台！");
						return vo;
					}
					typeString = "LONGDU->BBIN";
					DspResponseBean dspResponseBean = RemoteCaller.depositBbinRequest(loginname, amount.intValue(), localCredit, transID);
					if (dspResponseBean != null && dspResponseBean.getInfo().equals("11100")) {
						transferDao.addTransferforBbin(Long.parseLong(transID), loginname, localCredit, amount, Constants.IN, Constants.FAIL, "", "转入成功");
						tradeDao.changeCreditCoupon(loginname, remit * -1, CreditChangeType.TRANSFER_BBININ_COUPON.getCode(), transID, couponString + ":" + "游戏金额(" + amount + ")=存入金额(" + proposal.getAmount() + ") + 优惠劵金额(" + proposal.getGifTamount() + ")");
						creditReduce = true;
					} else {
						//return "转账状态错误:" + dspResponseBean.getInfo();
						vo.setMessage("转账状态错误:" + dspResponseBean.getInfo());
						return vo;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					vo.setMessage("系统异常");
					return vo;
				}
			*/} else if (couponType.equals("pt")) {
				try {
					if(proposal.getType() == 536){
						//return "该红包优惠劵不是PT红包优惠劵，不能转入PT平台！";
						vo.setMessage("该红包优惠劵不是PT红包优惠劵，不能转入PT平台！");
						return vo;
					}
					typeString = "LONGDU->PT";
					if (proposal.getType() == 571 || proposal.getType() == 572 || proposal.getType() == 573|| proposal.getType() == 409|| proposal.getType() == 410|| proposal.getType() == 411|| proposal.getType() == 412|| proposal.getType() == 419) {
						try {
							Double remoteCredit =  PtUtil.getPlayerMoney(loginname);
							if (remoteCredit != null) {
								// 判断PT平台用户的金额是否
								log.info("remote credit:" + remoteCredit);
								if (remoteCredit >= 5) {
									//return "PT平台金额必须小于5,才能使用该优惠劵！";
									vo.setMessage("PT平台金额必须小于5,才能使用该优惠劵！");
									return vo;
								}
								try {
										//Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, amount);
										//if (deposit != null && deposit) {
											Transfer transfer = transferDao.addTransferForNewPT(Long.parseLong(transID), loginname, localCredit, amount, Constants.IN, Constants.FAIL, "", null);
											tradeDao.changeCreditCouponPt(loginname, remit * -1, CreditChangeType.TRANSFER_NEWPT_COUPON.getCode(), transID, couponString + ":" + "游戏金额(" + amount + ")=存入金额(" + remit + ") + 优惠劵金额(" + gifTamount + ")", proposal.getShippingCode());
											// 标记额度已被扣除
											creditReduce = true;
											transfer.setFlag(Constants.FLAG_TRUE);
											transfer.setRemark("转入成功");
											update(transfer);
									String platform = "pttiger";
									String totalBetSql = "select sum(bet) from platform_data where loginname=:username and platform=:platform";
									Map<String, Object> prams = new HashMap<String, Object>();
									prams.put("username", customer.getLoginname());
									prams.put("platform", platform);
									Double betAmount = transferDao.getDoubleValueBySql(totalBetSql, prams);

									PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, betAmount, new Date(), 0); //validBet位null表示还没有通过定时器刷入投注额
									save(record);
										/*} else {
											//return "转账金额失败！";
											vo.setMessage("转账金额失败！");
											return vo;
										}*/
								} catch (Exception e) {
										result = "获取异常失败";
										vo.setMessage(result);
										return vo;
								}
							} else {
								//return "转账金额失败！";
								vo.setMessage("转账金额失败！");
								return vo;
							}
					} catch (Exception e) {
							//return "获取异常失败";
							vo.setMessage("获取异常失败");
							return vo;
					}
				}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					vo.setMessage("系统异常");
					return vo;
				}
			}
			// 更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());
			if (proposal.getType() != 536 && proposal.getType() != 571) {
				proposal.setAmount(remit);
				proposal.setGifTamount(gifTamount);
			}
			proposal.setRemark(typeString+"创建时间:"+proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			proposal.setAgent(customer.getAgent());
			update(proposal);

			customer.setCreditday(customer.getCreditday() + remit);
			update(customer);
			vo.setGiftMoney(amount);
		} catch (Exception e) {
			vo.setMessage("系统异常");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}
	
	@Override
	public AutoYouHuiVo transferInforPt8Coupon(String loginname, String couponCode,
			String ip) {
		boolean creditReduce = false;
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (customer == null) {
			//return "用户不存在！";
			vo.setMessage("用户不存在！");
			return vo;
		}
		if(customer.getFlag()==1){
			//return "该账号已经禁用";
			vo.setMessage("该账号已经禁用");
			return vo;
		}
		
		PtCoupon coupon = pt8CouponService.queryPtCoupon(couponCode) ;
		if(null == coupon){
			//return "优惠劵不存在！" ;
			vo.setMessage("优惠劵不存在！");
			return vo;
		}else if (coupon.getType().equals("1")){
			//return "优惠劵已经被使用！" ;
			vo.setMessage("优惠劵已经被使用！");
			return vo;
		}
		
		try {
			//boolean flag = PtUtil.getDepositPlayerMoney(loginname, 8.00); //转入8元
			//if(flag){
				coupon.setType("1"); //代表已经使用
				coupon.setUpdatetime(new Date());
				pt8CouponService.updatePtCoupon(coupon);
				
				String pno = seqService.generateProposalPno(ProposalType.PT8COUPON);
				Proposal proposal = new Proposal(pno, coupon.getOperator(), DateUtil.now(), null, null, null, 0.0, "", ProposalFlagType.EXCUTED.getCode(), "后台", null, null);
				proposal.setLoginname(loginname);
				proposal.setBetMultiples("12");  //12倍的投注额度
				proposal.setExecuteTime(new Date());
				proposal.setCreateTime(new Date());
				proposal.setType(ProposalType.PT8COUPON.getCode()) ;
				proposal.setShippinginfo("8元优惠劵");
				proposal.setProposer(coupon.getOperator());
				proposal.setShippingCode(couponCode);
				proposal.setGifTamount(8.00);
				save(proposal);
				String seqId = seqService.generateTransferID();
				Transfer transfer = transferDao.addTransferForNewPT(Long.parseLong(seqId), loginname, customer.getCredit(), 8.00, Constants.IN, Constants.FAIL, "", "转入成功");
				tradeDao.changeCreditCouponPt(loginname, 8.00 * -1, CreditChangeType.TRANSFER_NEWPT_COUPON.getCode(), seqId, "remark",couponCode);
				creditReduce = true;
				System.out.println("***************************************");
				vo.setGiftMoney(8.0);
			/*}else{
				return "转账失败" ;
			}*/
		} catch (Exception e) {
			vo.setMessage("系统异常！");
			return vo;
		}
		vo.setMessage("success");
		return vo;
	}

	public IPt8CouponService getPt8CouponService() {
		return pt8CouponService;
	}

	public void setPt8CouponService(IPt8CouponService pt8CouponService) {
		this.pt8CouponService = pt8CouponService;
	}

	/**
	 * 优惠劵处理
	 */
	@SuppressWarnings("unchecked")
	public String transferInforCouponSb(String transID, String loginname,Double remit, String couponCode, String ip) {
		String result = null;
		log.info("begin to transferOutforBbin:" + loginname);
		boolean creditReduce = false;
		try {
			Date executeTime=new Date();
			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				return "用户不存在！";
			}
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date time=formatter.parse("2014-07-13 05:00:00");
			Date date = new Date();
			if(date.getTime()>time.getTime()){
				return "该优惠劵活动只能在2014-07-13 05:00:00使用!";
			}
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[] { 581, 582, 583 ,584}));
			List<Proposal> list = findByCriteria(dc);
			if (list == null || list.size() <= 0) {
				return "优惠代码错误！";
			}
			//获取体育远程额度
			String sbloginname="e68_"+loginname;
			String sBCredit=RemoteCaller.querySBCredit(loginname);
			if(sBCredit==null || sBCredit.equals("")){
				return "系统繁忙！请稍后再试！";
			}
			Double remoteCredit=0.00;
			try {
				remoteCredit = Double.parseDouble(sBCredit);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return sBCredit;
			}
			if (remoteCredit >= 20) {
				return "188体育平台金额必须小于20,才能使用该优惠劵！";
			}
			// 获取提案信息
			Proposal proposal = list.get(0);
			Double localCredit = customer.getCredit();
			Double amount = 0.0;
			Double gifTamount = 0.0;
			if (proposal.getType() == 582 || proposal.getType() == 583 || proposal.getType() == 584) {
				if (localCredit < remit) {
					return "账号额度不足！";
				}
				Integer type = proposal.getType();
				if (type == 582) {
					if (remit != 100000.00) {
						return "188体育存10万送2万!存入金额不对！";
					}
					gifTamount = 20000.00;
					amount = remit + gifTamount;
					couponString = "188体育存10万送2万";
				}else if (type == 583) {
					if (remit != 200000.00) {
						return "188体育存20万送5万!存入金额不对！";
					}
					gifTamount = 50000.00;
					amount = remit + gifTamount;
					couponString = "188体育存20万送5万";
				}else if (type == 584) {
					if (remit < 1000.00) {
						return "188体育20%存送优惠劵最低转账1000！";
					}
					if (remit > 250000.00) {
						return "188体育20%存送优惠劵最高转账25万";
					}
					gifTamount = remit * 0.2;
					amount = remit + gifTamount;
					couponString = "188体育20%存送优惠劵";
				}
			} else {
				// 转入游戏金额=存入金额+赠送金额
				remit = 0.0;
				amount = proposal.getGifTamount();
				couponString = "188体育红包优惠券";
			}
			String typeString = "";
			// 检查额度
			log.info("current credit:" + localCredit);
			DspResponseBean dspResponseBean = null;
			try {
				dspResponseBean = RemoteCaller.depositSBRequest(loginname, amount, transID);
			} catch (Exception e) {
				log.error("depositSBRequest异常,transID:" + transID);
				dspResponseBean = RemoteCaller.getTransferStatusSBRequest(transID);
			}
			// 如果操作成功
			if (dspResponseBean != null && "000".equals(dspResponseBean.getInfo())) {
				if(userDao.updateSbcoupon(sbloginname)){
					//记录该优惠
					Sbcoupon sbcoupon=new Sbcoupon();
					sbcoupon.setCreatetime(executeTime);
					sbcoupon.setLoginname(sbloginname);
					sbcoupon.setShippingcode(proposal.getShippingCode());
					sbcoupon.setStatus(0);
					Double amountAll = amount * Integer.parseInt(proposal.getBetMultiples());
					sbcoupon.setRemark(couponString + "("+proposal.getShippingCode()+"):" + "游戏金额(" + amount + ")=存入金额(" + remit + ") + 优惠劵金额(" + gifTamount + ") 需要达到投注额为: "+amountAll );
					save(sbcoupon);
					//转入游戏
					transferDao.addTransferforSB(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
					tradeDao.changeCreditSb(loginname, remit * -1, CreditChangeType.TRANSFER_SB_COUPON.getCode(), transID, couponString + ":" + "游戏金额(" + amount + ")=存入金额(" + remit + ") + 优惠劵金额(" + gifTamount + ")");
					creditReduce = true;
					customer.setCreditday(customer.getCreditday() + remit);
					update(customer);
				}else{
					return "更新原来优惠失败！";
				}
			}else if(dspResponseBean!=null){
				result = "转账状态错误:" + dspResponseBean.getInfo();
			}
			// 更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(executeTime);
			proposal.setQuickly(customer.getLevel());
			if (proposal.getType() != 581) {
				proposal.setAmount(remit);
				proposal.setGifTamount(gifTamount);
			}
			proposal.setRemark(typeString);
			update(proposal);

			customer.setCreditday(customer.getCreditday() + remit);
			update(customer);

		} catch (Exception e) {
			System.out.println(e.toString());
			if (creditReduce)
				result = "转账发生异常 :" + e.getMessage();
			else
				result = "系统繁忙，请重新尝试";
			if (e instanceof RemoteApiException)
				throw new GenericDfhRuntimeException(e.getMessage());
		}
		return result;
	}
	
	@Override
	public String transferInforAginDsp(String transID, String loginname, Double remit) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforAginDsp:" + loginname);
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		Double localCredit = customer.getCredit();
		try {
			

			// if(customer!=null){
			// String username=customer.getLoginname();
			// if(username.equals("qyhp")||username.equals("qiany888")
			// ||username.equals("money888go")||username.equals("cc9981")
			// ||username.equals("xiaohui78557")
			// ||username.equals("newsslaichi")){
			// result = "系统繁忙，请重新尝试";
			// return result;
			// }
			// }

			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				// DepositPendingResponseBean depositPendingResponseBean =
				// RemoteCaller.depositPendingRequest(customer.getLoginname(),
				// remit, transID, customer.getAgcode());
				DspResponseBean dspResponseBean = RemoteCaller.depositPrepareDspAginRequest(loginname, remit, transID);
				DspResponseBean dspConfirmResponseBean = null;
				// System.out.println(dspResponseBean);
				// 如果操作成功
				if (dspResponseBean != null && dspResponseBean.getInfo().equals("0")) {
					Transfer transfer = transferDao.addTransferforAginDsp(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "预备转入");

					tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_DSPAGININ.getCode(), transID, null);
					// 标记额度已被扣除
					creditReduce = true;
					//
					try {
						dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, remit, transID, 1);
					} catch (Exception e) {
						log.warn("dspConfirmResponseBean try again!");
						try {
							dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, remit, transID, 1);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					if (dspConfirmResponseBean == null || dspConfirmResponseBean.getInfo().equals("1")) {
						result = "转账发生异常,请稍后再试或联系在线客服";
					} else if(dspConfirmResponseBean.getInfo().equals("0")){
						Transfer confirmtransfer = (Transfer) get(Transfer.class, Long.parseLong(transID), LockMode.UPGRADE);
						confirmtransfer.setFlag(0);
						confirmtransfer.setRemark("转入成功");
						tradeDao.update(confirmtransfer);
						customer.setCreditday(customer.getCreditday() + remit);
						update(customer);
					}else{
						result = dspConfirmResponseBean.getInfo();
					}

					//
					// transfer.setFlag(Constants.FLAG_TRUE);
					// transfer.setRemark("转入成功");
					// update(transfer);
				} else {
					dspConfirmResponseBean = RemoteCaller.depositConfirmDspAginRequest(loginname, remit, transID, 0);
					result = "转账状态错误:" + dspResponseBean.getInfo();
				}

			}
		} catch (Exception e) {

//			e.printStackTrace();
//			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
//			if (creditReduce){
//				result = "转账发生异常 :" + e.getMessage();
//			}else{
//				result = "系统繁忙，请重新尝试";
//				Transfer transfer = transferDao.addTransferforAginDsp(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "预备转入");
//				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_DSPAGININ.getCode(), transID, null);
//			}
			
			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce)
				result = "转账发生异常 :" + e.getMessage();
			else
				result = "系统繁忙，请重新尝试";

			// 如果是远程API返回错误，就一定要回滚。(6.14，发现该问题，DEPOSIT CONFIRM 偶尔会返回XML包,INVALID
			// IP，不明不错，因此回滚)
			if (e instanceof RemoteApiException)
				throw new GenericDfhRuntimeException(e.getMessage());
			
		}
		return result;
	}

	@Override
	public String transferOutforAginDsp(String transID, String loginname, Double remit) {
		String result = null;
		log.info("begin to transferOutforAginDsp:" + loginname);
		try {
			remit = Math.abs(remit);
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			
			//看玩家是否有正在使用的优惠，是否达到流水BEGIN
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "agin"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if(null != selfs && selfs.size()>0){
				SelfRecord record = selfs.get(0);
				if(null != record){
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					if(null != proposal){
						if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						
						// 获取数据总投注额
						Double validBetAmount = getSelfYouHuiBet("agin", loginname, proposal.getPno()) ;
						//后期远程金额
						Double remoteCredit=0.00;
						try {
							String money = RemoteCaller.queryDspAginCredit(loginname);
							if(null != money && NumberUtils.isNumber(money)){
								remoteCredit = Double.valueOf(money);
							}else{
								return "获取AGIN余额错误:"+money ;
							}
						} catch (Exception e) {
								return "获取异常失败";
						}
						validBetAmount = validBetAmount==null?0.0:validBetAmount;
						//判断是否达到条件
						log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
						if(validBetAmount != -1.0){
							if ((validBetAmount >= amountAll ||  remoteCredit < 20)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							}else{
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"agin" ,validBetAmount, (amountAll-validBetAmount) , new Date() , ProposalType.SELFPT94.getText() , "IN");
								transferDao.save(transferRecord) ;
								log.info("解除失败");
								return ProposalType.SELFPT94.getText() + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者AGIN游戏金额低于20元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
							}
						}else{
							record.setType(1);
							record.setRemark(record.getRemark()+";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname+"通过后台控制进行转账");
						}
					}else{
						log.info("AGIN自助优惠信息出错。Q" + loginname);
						return "AGIN自助优惠信息出错。Q";
					}
				}else{
					log.info("AGIN自助优惠信息出错。M" + loginname);
					return "AGIN自助优惠信息出错。M";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END

			// 预备转账
			DspResponseBean dspPrepareResponseBean = null;
			DspResponseBean dspConfirmResponseBean = null;
			try {
				dspPrepareResponseBean = RemoteCaller.withdrawPrepareDspAginRequest(loginname, remit, transID);
			} catch (Exception e) {
				log.warn("withdrawPrepareDspAginRequest try again!");
				dspPrepareResponseBean = RemoteCaller.withdrawPrepareDspAginRequest(loginname, remit, transID);
			}
			if (dspPrepareResponseBean != null && dspPrepareResponseBean.getInfo().equals("0")) {
				transferDao.addTransferforAginDsp(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "预备转出");
				dspConfirmResponseBean = RemoteCaller.withdrawConfirmDspAginRequest(loginname, remit, transID, 1);
				// tradeDao.changeCredit(loginname, remit,
				// CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
			} else {
				dspConfirmResponseBean = RemoteCaller.withdrawConfirmDspAginRequest(loginname, remit, transID, 0);

			}

			if (dspConfirmResponseBean.getInfo().equals("1")) {
				result = "转账发生异常,请稍后再试或联系在线客服";
			} else if(dspConfirmResponseBean.getInfo().equals("0")){
				Transfer transfer = (Transfer) get(Transfer.class, Long.parseLong(transID), LockMode.UPGRADE);
				transfer.setFlag(0);
				transfer.setRemark("转出成功");
				tradeDao.update(transfer);
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_DSPAGINOUT.getCode(), transID, null);

			}else{
				result = dspConfirmResponseBean.getInfo();
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}
	
	public String transferOutforSixLottery(String transID, String loginname, Double remit, String ip) {
		String result = null;
		log.info("begin to transferOutforSixLottery:" + loginname);
		try {
			remit = Math.abs(remit);
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			String withdrawalStr = SixLotteryUtil.withdrawal(loginname, remit, System.currentTimeMillis()+"") ;
			String flag = SixLotteryUtil.compileVerifyData("<status>(.*?)</status>", withdrawalStr) ;
			
			if (flag.equals("0")) { //成功
				transferDao.addTransferforSixLottery(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.SUCESS, "", "转出成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_SIXLOTTERYOUT.getCode(), transID, null);
			}else if(flag.equals("1")){ //失败
				result = "转账失败："+ SixLotteryUtil.compileVerifyData("<error>(.*?)</error>", withdrawalStr) ;;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}
	
	
	
	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferInforSixlottery(String transID, String loginname, Double remit, String ip) {

		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferInforSixlottery:" + loginname);
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				result = "转账失败，额度不足";
				log.info(result);
			} else {
				String despositStr = SixLotteryUtil.deposit(loginname, remit, System.currentTimeMillis()+"") ;
				String flag = SixLotteryUtil.compileVerifyData("<status>(.*?)</status>", despositStr) ;
				
				if (flag.equals("1")) {
					result = "转账失败  : " + SixLotteryUtil.compileVerifyData("<error>(.*?)</error>", despositStr) ;
				} else if (flag.equals("0")) {
					transferDao.addTransferforSixLottery(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.SUCESS, "", "转入成功");
					tradeDao.changeCredit(loginname, -remit, CreditChangeType.TRANSFER_SIXLOTTERYIN.getCode(), transID, null);
					// 标记额度已被扣除
					creditReduce = true;
					customer.setCreditday(customer.getCreditday() + remit);
					update(customer);
				} else {
						result = "转账失败  : " + despositStr;
					}
				}

		} catch (Exception e) {

			e.printStackTrace();
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				result = "系统繁忙，请重新尝试";
				transferDao.addTransferforKneo2(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.SUCESS, "", "转入成功");
				tradeDao.changeCredit(loginname, -remit, CreditChangeType.TRANSFER_SIXLOTTERYIN.getCode(), transID, null);
			}
		}
		return result;
	}
	
	public String transferPtAndSelfYouHuiIn(String transID, String loginname, Double remit,String remark) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferPtInAndSelfYouHui:" + loginname);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer==null){
			return "玩家账号不存在！";
		}
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		
		Double localCredit = customer.getCredit();
		if (localCredit < remit) {
			result = "转账失败，额度不足";
			log.info("current credit:" + localCredit);
			log.info(result);
			return result ;
		}
		try {
			remit = Math.abs(remit);
			String couponString = null ;
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 571, 572, 573,574 ,575 ,590 , 591,409,410,411,412,390, 419,391, 415}));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<6*60*1000){
					return "自助优惠5分钟内不允许户内转账！";
				}
				Integer type = proposal.getType();
				couponString = ProposalType.getText(type);
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				// 获取数据总投注额
				Double validBetAmount = getSelfYouHuiBet("pttiger", loginname, proposal.getPno()) ;
				
				/*****************************************/
				//后期远程金额
				Double remoteCredit=0.00;
				try {
						 remoteCredit = PtUtil.getPlayerMoney(loginname);
				} catch (Exception e) {
						return "获取异常失败";
				}
				validBetAmount = validBetAmount==null?0.0:validBetAmount;
				//判断是否达到条件
				log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
				if(validBetAmount != -1.0){
					if ((validBetAmount >= amountAll ||  remoteCredit < 1)) {
						log.info("解除成功");
					}else{
						PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"pttiger" ,validBetAmount, (amountAll-validBetAmount) , new Date() , couponString , "IN");
						transferDao.save(transferRecord) ;
						log.info("解除失败");
						return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于1元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
					}
				}else{
					log.info(loginname+"通过后台控制进行转账");
				}
			}
			
			// 检查额度
				//转入游戏
			Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, remit);
			if (deposit != null && deposit) {// 转进NEWPT成功
				transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_MEWPTIN.getCode(), transID, remark);
				// 标记额度已被扣除
				creditReduce = true;
				customer.setCreditday(customer.getCreditday()+remit);
				if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

				} else {
					customer.setShippingcodePt(null);
					update(customer);
				}
			} else{
				result = "转账状态错误" ;
			}
		} catch (Exception e) {
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_MEWPTIN.getCode(), transID, null);
				result = "系统繁忙，请重新尝试";
			}
		}
		return result;
	}
	
	
	public String transferTtAndSelfYouHuiInIn(String transID, String loginname, Double remit, String remark) {
		log.info("开始转入TTG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账TTG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("TTG转账正在维护" + loginname);
			return "TTG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		if (localCredit < remit) {
			log.info("转账失败，额度不足" + loginname);
			return "转账失败，额度不足";
		}
		try {
			String couponString = null;
			/*if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 571, 572, 573,574 ,575 ,590 , 591}));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<6*60*1000){
					return "自助优惠5分钟内不允许户内转账！";
				}
				Integer type = proposal.getType();
				couponString = ProposalType.getText(type);
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				// 获取数据总投注额
				Double validBetAmount = getSelfYouHuiBet("pttiger", loginname, proposal.getPno()) ;
				
				*//*****************************************//*
				//后期远程金额
				Double remoteCredit=0.00;
				try {
						 remoteCredit = PtUtil.getPlayerMoney(loginname);
				} catch (Exception e) {
						return "获取异常失败";
				}
				validBetAmount = validBetAmount==null?0.0:validBetAmount;
				//判断是否达到条件
				log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
				if(validBetAmount != -1.0){
					if ((validBetAmount >= amountAll ||  remoteCredit < 1)) {
						log.info("解除成功");
					}else{
						PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"pttiger" ,validBetAmount, (amountAll-validBetAmount) , new Date() , couponString , "IN");
						transferDao.save(transferRecord) ;
						log.info("解除失败");
						return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于1元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
					}
				}else{
					log.info(loginname+"通过后台控制进行转账");
				}
			}*/
			tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_TTGIN.getCode(), transID, remark);
			return null;
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	
	
	public String transferPtAndSelfYouHuiOut(String transID, String loginname, Double remit) {
		String result = null;
		log.info("begin to transferPtAndSelfYouHuiOut:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname);
			if (customer == null) {
				return "玩家账号不存在！";
			}
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			
			/********************************/
			remit = Math.abs(remit);
			String couponString = null ;
			Double localCredit = customer.getCredit();
			
			//后期远程金额
			Double remoteCredit=0.00;
			try {
				remoteCredit = PtUtil.getPlayerMoney(loginname);
			} catch (Exception e) {
				e.printStackTrace();
				return "获取异常失败";
			}
			
			//是否存在已领取的周周回馈，金额小于等于5元或达到流水可进行户内转账
			DetachedCriteria wsc=DetachedCriteria.forClass(WeekSent.class);
			wsc.add(Restrictions.eq("username", loginname));
			wsc.add(Restrictions.eq("platform", "pttiger"));
			wsc.add(Restrictions.eq("status", "1"));
			List<WeekSent> wsList = findByCriteria(wsc);
			for (WeekSent weekSent : wsList) {
				if(remoteCredit > 5){
					Double requiredBet = Arith.mul(weekSent.getPromo(), weekSent.getTimes());
					//计算流水
					String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
					Map<String, Object> prams = new HashMap<String, Object>();
					prams.put("username", loginname);
					prams.put("platform", "pttiger");
					Calendar cd = Calendar.getInstance();
					cd.setTime(weekSent.getGetTime());
					cd.set(Calendar.HOUR_OF_DAY, 0);
					cd.set(Calendar.MINUTE, 0);
					cd.set(Calendar.SECOND, 0);
					cd.set(Calendar.MILLISECOND, 0);
					prams.put("startTime", cd.getTime());
					Double realBet = transferDao.getDoubleValueBySql(totalBetSql, prams);
					if(requiredBet > realBet){
						log.info("玩家：" + loginname + " 已领取周周回馈，PT余额大于5元且未达到提款流水，不允许转出");
						return "您已领取周周回馈，流水须达到 " + requiredBet + " 元或者游戏余额不多于5元才能转出，目前流水额为：" + realBet + " 元";
					}
				}else{
					log.info("玩家：" + loginname + " 已领取周周回馈，PT金额"+remoteCredit+"，准备转出");
				}
			}
			//将已领取周周回馈，更新为已处理
			for (WeekSent ws : wsList) {
				ws.setStatus("2");
				update(ws);
			}
			
			//是否存在已领取的负盈利反赠，如果存在判断流水
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "pttiger"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				Double requiredBet = Arith.mul(losePromo.getPromo(), losePromo.getTimes());
				//计算流水
				String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("username", loginname);
				prams.put("platform", "pttiger");
				Calendar cd = Calendar.getInstance();
				cd.setTime(losePromo.getGetTime());
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				prams.put("startTime", cd.getTime());
				Double realBet = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), losePromo.getBetting());
				if(requiredBet > realBet){
					log.info("玩家：" + loginname + " 已领取负盈利反赠，未达到提款流水，不允许转出");
					return "您已领取救援金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：" + realBet + " 元";
				}
			}
			//将已领取负盈利反赠，更新为已处理
			for (LosePromo losePromo2 : losePromoList) {
				losePromo2.setStatus("2");
				update(losePromo2);
			}
			
			//PT疯狂礼金
			DetachedCriteria bigBangDc = DetachedCriteria.forClass(PTBigBang.class);
			bigBangDc.add(Restrictions.eq("username", loginname));
			bigBangDc.add(Restrictions.eq("status", "2"));
			List<PTBigBang> ptBigBangList = findByCriteria(bigBangDc);
			for (PTBigBang ptBigBang : ptBigBangList) {
				Double requiredBet = Arith.mul(ptBigBang.getGiftMoney(), ptBigBang.getTimes());
				//计算流水
				String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("username", loginname);
				prams.put("platform", "pttiger");
				Calendar cd = Calendar.getInstance();
				cd.setTime(ptBigBang.getGetTime());
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				prams.put("startTime", cd.getTime());
				Double realBet = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), ptBigBang.getBetAmount());
				if(requiredBet > realBet){
					log.info("玩家：" + loginname + " 已领取PT疯狂礼金，未达到流水，不允许转出");
					return "您已领取PT疯狂礼金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：" + realBet + " 元";
				}
			}
			//将已领取的PT疯狂礼金金更新为已处理
			for (PTBigBang ptBigBang : ptBigBangList) {
				ptBigBang.setStatus("3");
				update(ptBigBang);
			}
			
			//PT8元体验金
			if(remoteCredit >=1 && remoteCredit<Constants.MAXIMUMDTOUT){
				DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
				dc8.add(Restrictions.eq("loginname", loginname)) ;
				dc8.add(Restrictions.eq("target", "newpt")) ;
				dc8.addOrder(Order.desc("createtime"));
				List<Transfer> transfers = this.findByCriteria(dc8, 0, 10) ;
				if(null != transfers && transfers.size()>0){
					Transfer transfer = transfers.get(0);
					if(null != transfer){
						if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
							return "您正在使用体验金，PT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
						}
						if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是[app下载彩金]的转账记录
							return "您正在使用app下载彩金，PT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
						}  
					}
				}
			}
			
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] {420,499, 571, 572, 573,574 ,575 ,590 , 591 ,705,409,410,411,412,419,390, 415, 391}));
				List<Proposal> list = findByCriteria(dc);         
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
					return "自助优惠5分钟内不允许户内转账！";
				}
				Integer type = proposal.getType();
				couponString = ProposalType.getText(type);
				// 531 10%存送优惠券的默认投注倍数为10倍，最高1888
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				// 获取数据总投注额
				Double validBetAmount = getSelfYouHuiBet("pttiger", loginname, proposal.getPno()) ;
				
				
				//判断是否达到条件
				validBetAmount = validBetAmount==null?0.0:validBetAmount;
				if(validBetAmount != -1.0){
					if ((validBetAmount >= amountAll ||  remoteCredit < 1)) {
					}else{
						PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"pttiger" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
						transferDao.save(transferRecord) ;
						return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于1元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
					}
				}else{
					log.info(loginname+"通过后台控制进行转账");
				}
			}

			/********************************/
			
			if (remoteCredit < remit) {
				result = "转出失败，额度不足";
				log.info("remoteCredit :" + remoteCredit);
				log.info(result);
				return result ;
			}
			
			
			Boolean withdraw = PtUtil.getWithdrawPlayerMoney(loginname, remit);
			if (withdraw != null && withdraw) {// 转出NEWPT成功
				transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转出成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_NEWPTOUT.getCode(), transID, couponString);
				if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

				} else {
					customer.setShippingcodePt(null);
					update(customer);
				}
				
			} else {
				result = "转账状态错误";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}
	public static void main(String[] args) {
		DateUtil dateUtil = new DateUtil();
		dateUtil.setDatabaseNow(new DatabaseNow());
		System.out.println(dateUtil.getYesterday());
		System.out.println(dateUtil.getToday());
	}
	/**
	 * 检查该玩家老虎机流水量
	 * @param loginname
	 * @return
	 */
	public Map<String,Double> checkSlotStreamRecord(String loginname){
		Map map=new HashMap<String,Double>();
		Date begindate=DateUtil.getToday();
		Date enddate=DateUtil.getTomorrow();
		DetachedCriteria dc1 = DetachedCriteria.forClass(AgProfit.class);
		dc1.add(Restrictions.eq("loginname", loginname));
		dc1.add(Restrictions.ge("createTime",begindate));
		dc1.add(Restrictions.le("createTime",enddate));
		dc1.add(Restrictions.in("platform", new String[]{"ttg","nt","qt"}));
		List<AgProfit> list1 = findByCriteria(dc1);
		for(AgProfit agprofig : list1){
			map.put(agprofig.getPlatform(), agprofig.getBettotal());
		}
		
		
		DetachedCriteria dcpt = DetachedCriteria.forClass(PtDataNew.class);
		dcpt.add(Restrictions.eq("playername", "E"+loginname.toUpperCase()));
		dcpt.add(Restrictions.ge("endtime",DateUtil.getYesterday()));
		dcpt.add(Restrictions.le("endtime",DateUtil.getToday()));
		List<PtDataNew> listpt = findByCriteria(dcpt);
		if(null!=listpt&&listpt.size()>0){
			map.put("pt", listpt.get(0).getBetsTiger());
		}
		return map;
	}
	
	/**
	 * 领取全民闯关奖金
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String doEmigrated(String loginname) {
		String type=null;//报名的类型
		int a=0;
		DetachedCriteria dcapply = DetachedCriteria.forClass(Emigratedapply.class);
		dcapply.add(Restrictions.eq("username", loginname));
		dcapply.add(Restrictions.le("updatetime", DateUtil.getToday()));
		dcapply.add(Restrictions.ge("updatetime", DateUtil.getYesterday()));
		List<Emigratedapply> listpply = findByCriteria(dcapply);
		if(null==listpply||listpply.size()<1){
			return "未报名参与昨天的活动，无法领取奖金！";
		}
		type=listpply.get(0).getType();
		
		DetachedCriteria dcRecord = DetachedCriteria.forClass(Emigratedrecord.class);
		dcRecord.add(Restrictions.eq("username", loginname));
		dcRecord.add(Restrictions.ge("updatetime", DateUtil.getToday()));
		dcRecord.add(Restrictions.eq("type", "1"));//收入
		List<Emigratedrecord> listRecord = findByCriteria(dcRecord);
		if(null!=listRecord&&listRecord.size()>0){
			return "已领取龙都闯关奖金，不允许重复操作";
		}else{
			DetachedCriteria dcRecord1 = DetachedCriteria.forClass(SystemConfig.class);
			dcRecord1.add(Restrictions.eq("typeNo", "type110"));
			dcRecord1.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> listRecord1 = findByCriteria(dcRecord1);
			
			DetachedCriteria dcRecord2 = DetachedCriteria.forClass(SystemConfig.class);
			dcRecord2.add(Restrictions.eq("typeNo", "type111"));
			dcRecord2.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> listRecord2 = findByCriteria(dcRecord2);
			
			DetachedCriteria dcRecord3 = DetachedCriteria.forClass(SystemConfig.class);
			dcRecord3.add(Restrictions.eq("typeNo", "type112"));
			dcRecord3.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> listRecord3 = findByCriteria(dcRecord3);
			
			DetachedCriteria dcRecord4 = DetachedCriteria.forClass(SystemConfig.class);
			dcRecord4.add(Restrictions.eq("typeNo", "type113"));
			dcRecord4.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> listRecord4 = findByCriteria(dcRecord4);
			
			DetachedCriteria dcRecord5 = DetachedCriteria.forClass(SystemConfig.class);
			dcRecord5.add(Restrictions.eq("typeNo", "type114"));
			dcRecord5.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> listRecord5 = findByCriteria(dcRecord5);
			
			DetachedCriteria dcBonus = DetachedCriteria.forClass(Emigratedbonus.class);
			dcBonus.add(Restrictions.eq("username", loginname));
			List<Emigratedbonus> listBonus = findByCriteria(dcBonus);
			Double totalMoney =0.0;//本次总奖金
			StringBuffer remark=new StringBuffer("");
			StringBuffer remarkOut=new StringBuffer("");
			Emigratedbonus eb=null;
			if(null!=listBonus&&listBonus.size()>0){
				 eb=listBonus.get(0);
			}else{
				eb=new Emigratedbonus();
				eb.setMoney(0.0);
			}
			//查询昨日老虎机流水量 创建时间是今天
			Map<String,Double> map=checkSlotStreamRecord(loginname);
			if(null==map||map.isEmpty()){
				return "无流水奖励";
			}
			Double ttgAmount=0.0;//彩金
			Double ptAmount=0.0;//彩金
			Double ntAmount=0.0;//彩金
			Double qtAmount=0.0;//彩金
			Double limitBet=0.0;//投注额
			Double otherAmount=0.0;//额外通关彩金
			if(type.equals("1")){
				if(listRecord1 != null && listRecord1.size() > 0){
				
				limitBet=Double.parseDouble(listRecord1.get(0).getItemNo());//强转
				otherAmount=10.0;
				ttgAmount=Double.parseDouble(listRecord1.get(0).getValue());
				ptAmount=Double.parseDouble(listRecord1.get(0).getValue());
				ntAmount=Double.parseDouble(listRecord1.get(0).getValue());
				qtAmount=Double.parseDouble(listRecord1.get(0).getValue());
				remark.append("龙都-不屈白银:");
				}
			}else if(type.equals("2")){
				if(listRecord2 != null && listRecord2.size() > 0){
					
					limitBet=Double.parseDouble(listRecord2.get(0).getItemNo());//强转
					otherAmount=20.0;
					ttgAmount=Double.parseDouble(listRecord2.get(0).getValue());
					ptAmount=Double.parseDouble(listRecord2.get(0).getValue());
					ntAmount=Double.parseDouble(listRecord2.get(0).getValue());
					qtAmount=Double.parseDouble(listRecord2.get(0).getValue());
					remark.append("龙都-荣耀黄金:");
					}
			}else if(type.equals("3")){
					if(listRecord3 != null && listRecord3.size() > 0){
					limitBet=Double.parseDouble(listRecord3.get(0).getItemNo());//强转
					otherAmount=30.0;
					ttgAmount=Double.parseDouble(listRecord3.get(0).getValue());
					ptAmount=Double.parseDouble(listRecord3.get(0).getValue());
					ntAmount=Double.parseDouble(listRecord3.get(0).getValue());
					qtAmount=Double.parseDouble(listRecord3.get(0).getValue());
					remark.append("龙都-华贵铂金:");
					}
				
			}else if(type.equals("4")){
				if(listRecord4 != null && listRecord4.size() > 0){
					limitBet=Double.parseDouble(listRecord4.get(0).getItemNo());//强转
					otherAmount=40.0;
					ttgAmount=Double.parseDouble(listRecord4.get(0).getValue());
					ptAmount=Double.parseDouble(listRecord4.get(0).getValue());
					ntAmount=Double.parseDouble(listRecord4.get(0).getValue());
					qtAmount=Double.parseDouble(listRecord4.get(0).getValue());
					remark.append("龙都-璀璨钻石:");
					}
			}else if(type.equals("5")){
				if(listRecord5 != null && listRecord5.size() > 0){
					limitBet=Double.parseDouble(listRecord5.get(0).getItemNo());//强转
					otherAmount=50.0;
					ttgAmount=Double.parseDouble(listRecord5.get(0).getValue());
					ptAmount=Double.parseDouble(listRecord5.get(0).getValue());
					ntAmount=Double.parseDouble(listRecord5.get(0).getValue());
					qtAmount=Double.parseDouble(listRecord5.get(0).getValue());
					remark.append("龙都-最强王者:");
					}
			}else{
				return "报名信息异常";
			}
			remarkOut=remarkOut.append("领取"+remark.toString());
			if(null!=map.get("ttg")&&map.get("ttg")>=limitBet){
				totalMoney=Arith.add(totalMoney, ttgAmount);
				a++;
				remark.append("ttg流水："+map.get("ttg")+"奖金:"+ttgAmount+";");
				remarkOut.append("ttg奖金"+ttgAmount+"元;");
			}else{
				remarkOut.append("ttg奖金0元;");
			}
			if(null!=map.get("pt")&&map.get("pt")>=limitBet){
				a++;
				totalMoney=Arith.add(totalMoney, ptAmount);
				remark.append("pt流水："+map.get("pt")+"奖金:"+ptAmount+";");
				remarkOut.append("pt奖金"+ptAmount+"元;");
			}else{
				remarkOut.append("pt奖金0元;");
			}
			if(null!=map.get("nt")&&map.get("nt")>=limitBet){
				a++;
				totalMoney=Arith.add(totalMoney, ntAmount);
				remark.append("nt流水："+map.get("nt")+"奖金:"+ntAmount+";");
				remarkOut.append("nt奖金"+ntAmount+"元;");
			}else{
				remarkOut.append("nt奖金0元;");
			}
			if(null!=map.get("qt")&&map.get("qt")>=limitBet){
				a++;
				totalMoney=Arith.add(totalMoney, qtAmount);
				remark.append("qt流水："+map.get("qt")+"奖金:"+qtAmount+";");
				remarkOut.append("qt奖金"+qtAmount+"元;");
			}else{
				remarkOut.append("qt奖金0元;");
			}
			if(a==4){
				totalMoney=Arith.add(totalMoney, otherAmount);
				remark.append("通关奖励："+otherAmount);
				remarkOut.append("通关奖励"+otherAmount+"元");
			}else{
				remark.append("通关奖励："+0);
				remarkOut.append("通关奖励"+0+"元");
			}
			Emigratedrecord em = new Emigratedrecord();
			em.setUsername(loginname);
			em.setUpdatetime(new Date());
			em.setType("1");//收入
			em.setMoney(totalMoney);
			em.setRemark(remark.toString());
			save(em);
			
			eb.setUpdatetime(new Date());
			eb.setUsername(loginname);
			eb.setMoney(Arith.add(eb.getMoney(),totalMoney));
			saveOrUpdate(eb);
			remarkOut.append("合计奖励："+totalMoney+"元");
			return remarkOut.toString();
		}
	}
	
	@Override
	public String transferPtAndSelfYouHuiInModify(String transID, String loginname, Double remit,String remark) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transferPtInAndSelfYouHui:" + loginname);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer==null){
			return "玩家账号不存在！";
		}
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		
		Double localCredit = customer.getCredit();
		if (localCredit < remit) {
			result = "转账失败，额度不足";
			log.info("current credit:" + localCredit);
			log.info(result);
			return result ;
		}
		try {
			remit = Math.abs(remit);
			String couponString = null ;
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 571, 572, 573,574 ,575 ,590 , 591, 705,409,410,411,412,419,390,415, 391}));
				List<Proposal> list = findByCriteria(dc); 
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<6*60*1000){
					return "自助优惠5分钟内不允许户内转账！";
				}
				Integer type = proposal.getType();
				couponString = ProposalType.getText(type);
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				// 获取数据总投注额
				Double validBetAmount = getSelfYouHuiBet("pttiger", loginname, proposal.getPno()) ;
				
				/*****************************************/
				//后期远程金额
				Double remoteCredit=0.00;
				try {
						 remoteCredit = PtUtil.getPlayerMoney(loginname);
				} catch (Exception e) {
						return "获取异常失败";
				}
				validBetAmount = validBetAmount==null?0.0:validBetAmount;
				//判断是否达到条件
				log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
				if(validBetAmount != -1.0){
					if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
						log.info("解除成功");
					}else{
						PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"pttiger" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
						transferDao.save(transferRecord) ;
						log.info("解除失败");
						return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于1元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
					}
				}else{
					log.info(loginname+"通过后台控制进行转账");
				}
			}
			
			//将已领取的负盈利反赠更新为已处理
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "pttiger"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				losePromo.setStatus("2");
				update(losePromo);
			}
			
			// 检查额度
				//转入游戏
//			Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, remit);
//			if (deposit != null && deposit) {// 转进NEWPT成功
				transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_MEWPTIN.getCode(), transID, remark);
				// 标记额度已被扣除
				creditReduce = true;
				customer.setCreditday(customer.getCreditday()+remit);
				if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

				} else {
					customer.setShippingcodePt(null);
					update(customer);
				}
//			} else{
//				result = "转账状态错误" ;
//			}
		} catch (Exception e) {
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_MEWPTIN.getCode(), transID, null);
				result = "系统繁忙，请重新尝试";
			}
		}
		return result;
	}

	@SuppressWarnings("finally")
	@Override
	public String transfer4EbetIn(String loginname, String transferID, Integer remit, String platform, String catalog, String type) {
		log.info("开始转入EBET准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账EBET");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("EBET转账正在维护" + loginname);
			return "EBET转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCredit(loginname, -Double.valueOf(remit.toString()), CreditChangeType.TRANSFER_EBETIN.getCode(), transferID, null);
			    return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}

	@SuppressWarnings("finally")
	@Override
	public String transfer4Ebet(String loginname, String transferID, Integer remit, String platform, String catalog, String type) {
		String result = null;
		log.info("begin to transfer for ebet:" + loginname);
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (customer == null) {
			return "玩家账号不存在！";
		}
		if (customer.getFlag() == 1) {
			return "该账号已经禁用";
		}
		Double localCredit = customer.getCredit();
		// 检查额度
		log.info("current credit:" + localCredit);
		EBetResp ebetResp = EBetUtil.transfer(loginname, transferID, remit, platform, catalog, type);
		// 如果操作成功
		if (ebetResp != null) {
			if (ebetResp.getCode().equals(EBetCode.success.getCode())) {
				transferDao.addTransferforEbet(Long.parseLong(transferID), loginname, localCredit, Double.valueOf(remit.toString()), Constants.OUT, Constants.FAIL, "", "预备转账");
				EBetResp transferConfirm = EBetUtil.transferConfirm(loginname, transferID);
				if (transferConfirm != null) {
					if (transferConfirm.getCode().equals(EBetCode.success.getCode())) {
						tradeDao.changeCredit(loginname, Double.valueOf(remit.toString()), CreditChangeType.TRANSFER_EBETOUT.getCode(), transferID, null);
						Transfer confirmtransfer = (Transfer) get(Transfer.class, Long.parseLong(transferID), LockMode.UPGRADE);
						confirmtransfer.setFlag(0);
						confirmtransfer.setRemark("转账成功");
						tradeDao.update(confirmtransfer);
					} else {
						// 交易失败退还款
						result = transferConfirm.getMsg();
					}
				} else {
					result = "转账失败";
				}
			} else {
				result = ebetResp.getMsg();
			}
		} else {
			result = "转账失败";
		}
		return result;
	}

	@Override
	public Transfer addTransferforEbet(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforEbet( transID,  loginname,  credit,  remit,  in,  flag,  paymentid,  remark);
	}

	@Override
	public Transfer addTransferforEBetApp(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		return transferDao.addTransferForEBetApp(transID, loginname, localCredit, remit, in, remark);
	}
	
	/**
	 * TTg转出  验证流水
	 */
	public String transferTtAndSelfYouHuiOut(String transferID, String loginname, Double credit) {
		String result = null;
		log.info("begin to transferttg:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			Double remoteCredit=0.00;
			try {
				String money = PtUtil1.getPlayerAccount(loginname);
				if(null != money && NumberUtils.isNumber(money)){
					remoteCredit = Double.valueOf(money);
				}else{
					return "获取TTG余额错误:"+money ;
				}
			} catch (Exception e) {
					return "获取异常失败";
			}
			
			//体验金
			if(remoteCredit >=1 && remoteCredit<Constants.MAXIMUMDTOUT){
				DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
				dc8.add(Restrictions.eq("loginname", loginname)) ;
				dc8.add(Restrictions.eq("target", "ttg")) ;
				dc8.addOrder(Order.desc("createtime"));
				List<Transfer> transfers = this.findByCriteria(dc8, 0, 10) ;
				if(null != transfers && transfers.size()>0){
					Transfer transfer = transfers.get(0);
					if(null != transfer){
						if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
							return "您正在使用体验金，TTG余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
						}
						if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是[app下载彩金]的转账记录
							return "您正在使用app下载彩金，TTG余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
						} 
					}
				}
			}
			//是否存在已领取的周周回馈，金额小于等于5元或达到流水可进行户内转账
			DetachedCriteria wsc=DetachedCriteria.forClass(WeekSent.class);
			wsc.add(Restrictions.eq("username", loginname));
			wsc.add(Restrictions.eq("platform", "ttg"));
			wsc.add(Restrictions.eq("status", "1"));
			List<WeekSent> wsList = findByCriteria(wsc);
			for (WeekSent weekSent : wsList) {
				if(remoteCredit > 5){
					Double requiredBet = Arith.mul(weekSent.getPromo(), weekSent.getTimes());
					//计算流水
					String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
					Map<String, Object> prams = new HashMap<String, Object>();
					prams.put("username", loginname);
					prams.put("platform", "ttg");
					Calendar cd = Calendar.getInstance();
					cd.setTime(weekSent.getGetTime());
					cd.set(Calendar.HOUR_OF_DAY, 0);
					cd.set(Calendar.MINUTE, 0);
					cd.set(Calendar.SECOND, 0);
					cd.set(Calendar.MILLISECOND, 0);
					prams.put("startTime", cd.getTime());
					Double realBet = transferDao.getDoubleValueBySql(totalBetSql, prams);
					if(requiredBet > realBet){
						log.info("玩家：" + loginname + " 已领取周周回馈，ttg余额大于5元且未达到提款流水，不允许转出");
						return "您已领取周周回馈，流水须达到 " + requiredBet + " 元或者游戏余额不多于5元才能转出，目前流水额为：" + realBet + " 元";
					}
				}else{
					log.info("玩家：" + loginname + " 已领取周周回馈，ttg金额"+remoteCredit+"，准备转出");
				}
			}
			//将已领取周周回馈，更新为已处理
			for (WeekSent ws : wsList) {
				ws.setStatus("2");
				update(ws);
			}
			
			//是否存在已领取的负盈利反赠，如果存在判断流水
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "ttg"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				Double requiredBet = Arith.mul(losePromo.getPromo(), losePromo.getTimes());
				//计算流水
				String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("username", loginname);
				prams.put("platform", "ttg");
				Calendar cd = Calendar.getInstance();
				cd.setTime(losePromo.getGetTime());
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				prams.put("startTime", cd.getTime());
				Double realBet = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), losePromo.getBetting());
				if(requiredBet > realBet){
					log.info("玩家：" + loginname + " 已领取负盈利反赠，未达到提款流水，不允许转出");
					return "您已领取救援金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：" + realBet + " 元";
				}
			}
			//将已领取负盈利反赠，更新为已处理
			for (LosePromo losePromo2 : losePromoList) {
				losePromo2.setStatus("2");
				update(losePromo2);
			}
			
			//看玩家是否有正在使用的优惠，是否达到流水BEGIN
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "ttg"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if(null != selfs && selfs.size()>0){
				SelfRecord record = selfs.get(0);
				if(null != record){
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					if(null != proposal){
						if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						
						PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
						Double validBetAmount=null;
						if(preferential.getType().equals(1)){
							validBetAmount=-1.0;
						}else{
						//从使用存送优惠截止到现在的投注额
						String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
						Map<String, Object> prams = new HashMap<String, Object>();
						prams.put("username", loginname);
						prams.put("platform", "ttg");
						Calendar cd = Calendar.getInstance();
						cd.setTime(record.getCreatetime());
						cd.set(Calendar.HOUR_OF_DAY, 0);
						cd.set(Calendar.MINUTE, 0);
						cd.set(Calendar.SECOND, 0);
						cd.set(Calendar.MILLISECOND, 0);
						prams.put("startTime", cd.getTime());
						validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
						
						/*// 获取数据总投注额
						Double validBetAmount = getSelfYouHuiBet("ttg", loginname, proposal.getPno()) ;*/
						}
						//后期远程金额
						validBetAmount = validBetAmount==null?0.0:validBetAmount;
						//判断是否达到条件
						log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
						if(validBetAmount != -1.0){
							if ((validBetAmount >= amountAll ||  remoteCredit < 1)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							}else{
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"ttg" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
								transferDao.save(transferRecord) ;
								log.info("解除失败");
								return "TTG ：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者TTG游戏金额低于1元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
							}
						}else{
							record.setType(1);
							record.setRemark(record.getRemark()+";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname+"通过后台控制进行转账");
						}
					}else{
						log.info("TTG自助优惠信息出错。Q" + loginname);
						return "TTG自助优惠信息出错。Q";
					}
				}else{
					log.info("TTG自助优惠信息出错。M" + loginname);
					return "TTG自助优惠信息出错。M";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END
			

			if (remoteCredit < credit) {
				result = "转出失败，额度不足";
				log.info("remoteCredit :" + remoteCredit);
				log.info(result);
				return result ;
			}
			
			
			Boolean withdraw = PtUtil1.addPlayerAccountPraper(loginname, credit*-1);
			if (withdraw != null && withdraw) {// 转出TT成功
				transferDao.addTransferforTt(Long.parseLong(transferID), loginname, localCredit, credit, Constants.OUT, Constants.FAIL, "", "转出成功");
				tradeDao.changeCredit(loginname, credit, CreditChangeType.TRANSFER_TTGOUT.getCode(), transferID, null);
			} else {
				result = "转账状态错误";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result = "TTG转账发生异常: " + e.getMessage();
		}
		return result;
	}
	
	
	//ttg转账
		@Override
		public String transferInforTtgIn(String transID, String loginname, Double remit, String youhuiType, String remark) {
			log.info("开始转入TTG准备工作:" + loginname);
			// 判断转账是否关闭
			Const constPt = transferDao.getConsts("转账TTG");
			if (constPt == null) {
				log.info("平台不存在" + loginname);
				return "平台不存在";
			}
			if (constPt.getValue().equals("0")) {
				log.info("TTG转账正在维护" + loginname);
				return "TTG转账正在维护";
			}
			// 操作业务
			Users customer = transferDao.getUsers(loginname);
			if (customer.getFlag() == 1) {
				log.info("该账号已经禁用" + loginname);
				return "该账号已经禁用";
			}
			remit = Math.abs(remit);
			Double localCredit = customer.getCredit();
			
			
			//看玩家是否有正在使用的优惠，是否达到流水TTG
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "ttg"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if(null != selfs && selfs.size()>0){
				SelfRecord record = selfs.get(0);
				if(null != record){
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					if(null != proposal){
						if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
						Double validBetAmount =null;
						if(preferential.getType().equals(1)){
							validBetAmount=-1.0;
						}else{
							//从使用存送优惠截止到现在的投注额
							String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
							Map<String, Object> prams = new HashMap<String, Object>();
							prams.put("username", loginname);
							prams.put("platform", "ttg");
							Calendar cd = Calendar.getInstance();
							cd.setTime(record.getCreatetime());
							cd.set(Calendar.HOUR_OF_DAY, 0);
							cd.set(Calendar.MINUTE, 0);
							cd.set(Calendar.SECOND, 0);
							cd.set(Calendar.MILLISECOND, 0);
							prams.put("startTime", cd.getTime());
							validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
						}
						//后期远程金额
						Double remoteCredit=0.00;
						try {
							String money = PtUtil1.getPlayerAccount(loginname);
							if(null != money && NumberUtils.isNumber(money)){
								remoteCredit = Double.valueOf(money);
							}else{
								return "获取TTG余额错误:"+money ;
							}
						} catch (Exception e) {
								return "获取异常失败";
						}
						validBetAmount = validBetAmount==null?0.0:validBetAmount;
						//判断是否达到条件
						log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
						if(validBetAmount != -1.0){
							if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							}else{
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"ttg" ,validBetAmount, (amountAll-validBetAmount) , new Date() , youhuiType , "IN");
								transferDao.save(transferRecord) ;
								log.info("解除失败");
								return youhuiType + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者ttg游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
							}
						}else{
							record.setType(1);
							record.setRemark(record.getRemark()+";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname+"通过后台控制进行转账");
						}
					}else{
						log.info("TTG自助优惠信息出错。Q" + loginname);
						return "TTG自助优惠信息出错。Q";
					}
				}else{
					log.info("TTG自助优惠信息出错。M" + loginname);
					return "TTG自助优惠信息出错。M";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END
			
			
			try {
				// 检查额度
				log.info("current credit:" + localCredit);
				if (localCredit < remit && !"红包优惠".equals(youhuiType)) {
					log.info("转账失败，额度不足" + loginname);
					return "转账失败，额度不足";
				}
				 else if("红包优惠".equals(youhuiType)) {
				     tradeDao.changeCreditIn(customer, 0.0, CreditChangeType.TRANSFER_REDCOUPONS_TTGIN.getCode(), transID, remark);
				    return null;
				}
				 else{
					   tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_TTGIN.getCode(), transID, remark);
				 }
				return null;
			} catch (Exception e) {
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		}
		
	
	
	/**
	 * 
	 * @param platform 自助平台
	 * @param loginname 账号
	 * @param executeTime 优惠 执行时间
	 * @return
	 */
	public Double getSelfYouHuiBet(String platform , String loginname , String pno){
		try {
			//玩家到当前系统时间总的投注额 - 玩家自助优惠的时候玩家的投注额
			DetachedCriteria betDc = DetachedCriteria
					.forClass(PlatformData.class);
			betDc = betDc.add(Restrictions.eq("platform", platform));
			betDc = betDc.add(Restrictions.eq("loginname", loginname));
			betDc.setProjection(Projections.sum("bet"));
			Double betAmount = (Double) this.findByCriteria(betDc).get(0);
			DetachedCriteria recordBetDc = DetachedCriteria
					.forClass(PreferentialRecord.class);
			recordBetDc.add(Restrictions.eq("pno", pno));
			PreferentialRecord record = (PreferentialRecord) this
					.findByCriteria(recordBetDc).get(0);
			//后台人工控制开关
			if(record.getType()==1){
				return -1.0 ;
			}
			if (null != betAmount && null != record && null != record.getValidBet()) {
				return betAmount - record.getValidBet();
			} else {
				return null;
			}
		} catch (Exception e) {
			log.error("getSelfYouHuiBet error :" + loginname+"  "+ pno +"  " + platform);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferInforDspIn(String transID, String loginname, Double remit) {
		log.info("开始转入AG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账AG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("AG转账正在维护" + loginname);
			return "AG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		
		//看玩家是否有正在使用的优惠，是否达到流水BEGIN
				DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
				selfDc.add(Restrictions.eq("loginname", loginname));
				selfDc.add(Restrictions.eq("platform", "ag"));
				selfDc.add(Restrictions.eq("type", 0));
				selfDc.addOrder(Order.desc("createtime"));
				List<SelfRecord> selfs = this.findByCriteria(selfDc);
				
				if(null != selfs && selfs.size()>0){
					SelfRecord record = selfs.get(0);
					if(null != record){
						Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
						if(null != proposal){
							if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
								return "自助优惠5分钟内不允许户内转账！";
							}
							// 要达到的总投注额
							Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
							
							// 获取数据总投注额
							Double validBetAmount = getSelfYouHuiBet("ag", loginname, proposal.getPno()) ;
							//后期远程金额
							Double remoteCredit=0.00;
							try {
								String money = RemoteCaller.queryDspCredit(loginname);
								if(null != money && NumberUtils.isNumber(money)){
									remoteCredit = Double.valueOf(money);
								}else{
									return "获取AG余额错误:"+money ;
								}
							} catch (Exception e) {
									return "获取异常失败";
							}
							validBetAmount = validBetAmount==null?0.0:validBetAmount;
							//判断是否达到条件
							log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
							if(validBetAmount != -1.0){
								if ((validBetAmount >= amountAll ||  remoteCredit < 20)) {
									record.setType(1);
									record.setUpdatetime(new Date());
									update(record);
									log.info("解除成功");
								}else{
									PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"ag" ,validBetAmount, (amountAll-validBetAmount) , new Date() , ProposalType.SELFPT93.getText() , "IN");
									transferDao.save(transferRecord) ;
									log.info("解除失败");
									return ProposalType.SELFPT93.getText() + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者AG游戏金额低于20元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
								}
							}else{
								record.setType(1);
								record.setRemark(record.getRemark()+";后台放行");
								record.setUpdatetime(new Date());
								update(record);
								log.info(loginname+"通过后台控制进行转账");
							}
						}else{
							log.info("AG自助优惠信息出错。Q" + loginname);
							return "AG自助优惠信息出错。Q";
						}
					}else{
						log.info("AG自助优惠信息出错。M" + loginname);
						return "AG自助优惠信息出错。M";
					}
				}
				//看玩家是否有正在使用的优惠，是否达到流水END
				
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
			    tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_DSPIN.getCode(), transID, null);
			    return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	public String transferInforAginDspIn(String transID, String loginname, Double remit) {
		log.info("开始转入AGIN准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账AGIN");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("AGIN转账正在维护" + loginname);
			return "AGIN转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		//看玩家是否有正在使用的优惠，是否达到流水BEGIN
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "agin"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		
		if(null != selfs && selfs.size()>0){
			SelfRecord record = selfs.get(0);
			if(null != record){
				Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
				if(null != proposal){
					if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
						return "自助优惠5分钟内不允许户内转账！";
					}
					// 要达到的总投注额
					Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
					
					// 获取数据总投注额
					Double validBetAmount = getSelfYouHuiBet("agin", loginname, proposal.getPno()) ;
					//后期远程金额
					Double remoteCredit=0.00;
					try {
						String money = RemoteCaller.queryDspAginCredit(loginname);
						if(null != money && NumberUtils.isNumber(money)){
							remoteCredit = Double.valueOf(money);
						}else{
							return "获取AGIN余额错误:"+money ;
						}
					} catch (Exception e) {
							return "获取异常失败";
					}
					validBetAmount = validBetAmount==null?0.0:validBetAmount;
					//判断是否达到条件
					log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
					if(validBetAmount != -1.0){
						if ((validBetAmount >= amountAll ||  remoteCredit < 20)) {
							record.setType(1);
							record.setUpdatetime(new Date());
							update(record);
							log.info("解除成功");
						}else{
							PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"agin" ,validBetAmount, (amountAll-validBetAmount) , new Date() , ProposalType.SELFPT94.getText() , "IN");
							transferDao.save(transferRecord) ;
							log.info("解除失败");
							return ProposalType.SELFPT94.getText() + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者AGIN游戏金额低于20元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
						}
					}else{
						record.setType(1);
						record.setRemark(record.getRemark()+";后台放行");
						record.setUpdatetime(new Date());
						update(record);
						log.info(loginname+"通过后台控制进行转账");
					}
				}else{
					log.info("AGIN自助优惠信息出错。Q" + loginname);
					return "AGIN自助优惠信息出错。Q";
				}
			}else{
				log.info("AGIN自助优惠信息出错。M" + loginname);
				return "AGIN自助优惠信息出错。M";
			}
		}
		//看玩家是否有正在使用的优惠，是否达到流水END
		
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
			    tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_DSPAGININ.getCode(), transID, null);
			    return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferInforkenoIn2(String transID, String loginname, Double remit, String ip) {
		log.info("开始转入KENO2准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账KENO2");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("KENO2转账正在维护" + loginname);
			return "KENO2转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		// 获取额度
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_KENO2IN.getCode(), transID, null);
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	public String transferInforQtIn(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入QT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账QT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("QT转账正在维护" + loginname);
			return "QT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		
		
		//看玩家是否有正在使用的优惠，是否达到流水QT
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "qt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		
		if(null != selfs && selfs.size()>0){
			SelfRecord record = selfs.get(0);
			if(null != record){
				Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
				if(null != proposal){
					if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
						return "自助优惠5分钟内不允许户内转账！";
					}
					// 要达到的总投注额
					Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
					PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
					Double validBetAmount =null;
					if(preferential.getType().equals(1)){
						validBetAmount=-1.0;
					}else{
						//从使用存送优惠截止到现在的投注额
						String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
						Map<String, Object> prams = new HashMap<String, Object>();
						prams.put("username", loginname);
						prams.put("platform", "qt");
						Calendar cd = Calendar.getInstance();
						cd.setTime(record.getCreatetime());
						cd.set(Calendar.HOUR_OF_DAY, 0);
						cd.set(Calendar.MINUTE, 0);
						cd.set(Calendar.SECOND, 0);
						cd.set(Calendar.MILLISECOND, 0);
						prams.put("startTime", cd.getTime());
						validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
					}
					//后期远程金额
					Double remoteCredit=0.00;
					try {
						String money = QtUtil.getBalance(loginname);
						if(!QtUtil.RESULT_FAIL.equals(money) && NumberUtils.isNumber(money)){
							remoteCredit = Double.valueOf(money);
						}else{
							return "获取QT余额错误:"+money ;
						}
					} catch (Exception e) {
							return "获取异常失败";
					}
					validBetAmount = validBetAmount==null?0.0:validBetAmount;
					//判断是否达到条件
					log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
					if(validBetAmount != -1.0){
						if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
							record.setType(1);
							record.setUpdatetime(new Date());
							update(record);
							log.info("解除成功");
						}else{
							PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"qt" ,validBetAmount, (amountAll-validBetAmount) , new Date() , youhuiType , "IN");
							transferDao.save(transferRecord) ;
							log.info("解除失败");
							return youhuiType + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者QT游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
						}
					}else{
						record.setType(1);
						record.setRemark(record.getRemark()+";后台放行");
						record.setUpdatetime(new Date());
						update(record);
						log.info(loginname+"通过后台控制进行转账");
					}
				}else{
					log.info("QT自助优惠信息出错。Q" + loginname);
					return "QT自助优惠信息出错。Q";
				}
			}else{
				log.info("QT自助优惠信息出错。M" + loginname);
				return "QT自助优惠信息出错。M";
			}
		}
		//看玩家是否有正在使用的优惠，是否达到流水END
		
		
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit && !"红包优惠".equals(youhuiType)) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			}
			 else if("红包优惠".equals(youhuiType)) {
			     tradeDao.changeCreditIn(customer, 0.0, CreditChangeType.TRANSFER_REDCOUPONS_QTIN.getCode(), transID, remark);
			    return null;
			}
			 else{
				   tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_QTIN.getCode(), transID, remark);
			 }
			return null;
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	@Override
	public String transferInforQtInQD(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入QT准备工作:" + loginname);
		return null;
		/*// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账QT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("QT转账正在维护" + loginname);
			return "QT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		
		
		//看玩家是否有正在使用的优惠，是否达到流水QT
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "qt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		
		if(null != selfs && selfs.size()>0){
			SelfRecord record = selfs.get(0);
			if(null != record){
				Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
				if(null != proposal){
					if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
						return "自助优惠5分钟内不允许户内转账！";
					}
					// 要达到的总投注额
					Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
					PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
					Double validBetAmount =null;
					if(preferential.getType().equals(1)){
						validBetAmount=-1.0;
					}else{
						//从使用存送优惠截止到现在的投注额
						String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
						Map<String, Object> prams = new HashMap<String, Object>();
						prams.put("username", loginname);
						prams.put("platform", "qt");
						Calendar cd = Calendar.getInstance();
						cd.setTime(record.getCreatetime());
						cd.set(Calendar.HOUR_OF_DAY, 0);
						cd.set(Calendar.MINUTE, 0);
						cd.set(Calendar.SECOND, 0);
						cd.set(Calendar.MILLISECOND, 0);
						prams.put("startTime", cd.getTime());
						validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
					}
					//后期远程金额
					Double remoteCredit=0.00;
					try {
						String money = QtUtil.getBalance(loginname);
						if(!QtUtil.RESULT_FAIL.equals(money) && NumberUtils.isNumber(money)){
							remoteCredit = Double.valueOf(money);
						}else{
							return "获取QT余额错误:"+money ;
						}
					} catch (Exception e) {
							return "获取异常失败";
					}
					validBetAmount = validBetAmount==null?0.0:validBetAmount;
					//判断是否达到条件
					log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
					if(validBetAmount != -1.0){
						if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
							record.setType(1);
							record.setUpdatetime(new Date());
							update(record);
							log.info("解除成功");
						}else{
							PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"qt" ,validBetAmount, (amountAll-validBetAmount) , new Date() , youhuiType , "IN");
							transferDao.save(transferRecord) ;
							log.info("解除失败");
							return youhuiType + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者QT游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
						}
					}else{
						record.setType(1);
						record.setRemark(record.getRemark()+";后台放行");
						record.setUpdatetime(new Date());
						update(record);
						log.info(loginname+"通过后台控制进行转账");
					}
				}else{
					log.info("QT自助优惠信息出错。Q" + loginname);
					return "QT自助优惠信息出错。Q";
				}
			}else{
				log.info("QT自助优惠信息出错。M" + loginname);
				return "QT自助优惠信息出错。M";
			}
		}
		//看玩家是否有正在使用的优惠，是否达到流水END
		
		
		try {
			    tradeDao.changeCreditInQD(customer, remit, CreditChangeType.TRANSFER_QD_COUPON.getCode(), transID, remark);
			    return null;
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}*/
	}
	
	/**
	 * QT转出  验证流水
	 */
	public String transferQtAndSelfYouHuiOut(String transferID, String loginname, Double credit) {
		String result = null;
		log.info("begin to transfer qt:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			
			Double localCredit = customer.getCredit();
			Double remoteCredit=0.00;
			try {
				String money = QtUtil.getBalance(loginname);
				if(!QtUtil.RESULT_FAIL.equals(money) && NumberUtils.isNumber(money)){
					remoteCredit = Double.valueOf(money);
				}else{
					return "获取QT余额错误:"+money ;
				}
			} catch (Exception e) {
					return "获取异常失败";
			}
			//体验金
			if(remoteCredit >=1 && remoteCredit< Constants.MAXIMUMDTOUT){
				DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
				dc8.add(Restrictions.eq("loginname", loginname)) ;
				dc8.add(Restrictions.eq("target", "qt")) ;
				dc8.addOrder(Order.desc("createtime"));
				List<Transfer> transfers = this.findByCriteria(dc8, 0, 10) ;
				if(null != transfers && transfers.size()>0){
					Transfer transfer = transfers.get(0);
					if(null != transfer){
						if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
							return "您正在使用体验金，QT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
						}
						if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是[app下载彩金]的转账记录
							return "您正在使用app下载彩金，QT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
						} 
					}
				}
			}
			
			//是否存在已领取的周周回馈，金额小于等于5元或达到流水可进行户内转账
			DetachedCriteria wsc=DetachedCriteria.forClass(WeekSent.class);
			wsc.add(Restrictions.eq("username", loginname));
			wsc.add(Restrictions.eq("platform", "qt"));
			wsc.add(Restrictions.eq("status", "1"));
			List<WeekSent> wsList = findByCriteria(wsc);
			for (WeekSent weekSent : wsList) {
				if(remoteCredit > 5){
					Double requiredBet = Arith.mul(weekSent.getPromo(), weekSent.getTimes());
					//计算流水
					String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
					Map<String, Object> prams = new HashMap<String, Object>();
					prams.put("username", loginname);
					prams.put("platform", "qt");
					Calendar cd = Calendar.getInstance();
					cd.setTime(weekSent.getGetTime());
					cd.set(Calendar.HOUR_OF_DAY, 0);
					cd.set(Calendar.MINUTE, 0);
					cd.set(Calendar.SECOND, 0);
					cd.set(Calendar.MILLISECOND, 0);
					prams.put("startTime", cd.getTime());
					Double realBet = transferDao.getDoubleValueBySql(totalBetSql, prams);
					if(requiredBet > realBet){
						log.info("玩家：" + loginname + " 已领取周周回馈，QT余额大于5元且未达到提款流水，不允许转出");
						return "您已领取周周回馈，流水须达到 " + requiredBet + " 元或者游戏余额不多于5元才能转出，目前流水额为：" + realBet + " 元";
					}
				}else{
					log.info("玩家：" + loginname + " 已领取周周回馈，QT金额"+remoteCredit+"，准备转出");
				}
			}
			//将已领取周周回馈，更新为已处理
			for (WeekSent ws : wsList) {
				ws.setStatus("2");
				update(ws);
			}
			
			//是否存在已领取的负盈利反赠，如果存在判断流水
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "qt"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				Double requiredBet = Arith.mul(losePromo.getPromo(), losePromo.getTimes());
				//计算流水
				String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("username", loginname);
				prams.put("platform", "qt");
				Calendar cd = Calendar.getInstance();
				cd.setTime(losePromo.getGetTime());
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				prams.put("startTime", cd.getTime());
				Double realBet = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), losePromo.getBetting());
				if(requiredBet > realBet){
					log.info("玩家：" + loginname + " 已领取负盈利反赠，未达到提款流水，不允许转出");
					return "您已领取救援金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：" + realBet + " 元";
				}
			}
			//将已领取负盈利反赠，更新为已处理
			for (LosePromo losePromo2 : losePromoList) {
				losePromo2.setStatus("2");
				update(losePromo2);
			}
			
			//看玩家是否有正在使用的优惠，是否达到流水BEGIN
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "qt"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if(null != selfs && selfs.size()>0){
				SelfRecord record = selfs.get(0);
				if(null != record){
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					if(null != proposal){
						if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
						Double validBetAmount=null;
						if(preferential.getType().equals(1)){
							validBetAmount=-1.0;
						}else{
						//从使用存送优惠截止到现在的投注额
						String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
						Map<String, Object> prams = new HashMap<String, Object>();
						prams.put("username", loginname);
						prams.put("platform", "qt");
						Calendar cd = Calendar.getInstance();
						cd.setTime(record.getCreatetime());
						cd.set(Calendar.HOUR_OF_DAY, 0);
						cd.set(Calendar.MINUTE, 0);
						cd.set(Calendar.SECOND, 0);
						cd.set(Calendar.MILLISECOND, 0);
						prams.put("startTime", cd.getTime());
						validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
						//后期远程金额
						}
						validBetAmount = validBetAmount==null?0.0:validBetAmount;
						//判断是否达到条件
						log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
						if(validBetAmount != -1.0){
							if ((validBetAmount >= amountAll ||  remoteCredit < 1)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							}else{
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"qt" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
								transferDao.save(transferRecord) ;
								log.info("解除失败");
								return "QT ：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者QT游戏金额低于1元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
							}
						}else{
							record.setType(1);
							record.setRemark(record.getRemark()+";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname+"通过后台控制进行转账");
						}
					}else{
						log.info("QT自助优惠信息出错。Q" + loginname);
						return "QT自助优惠信息出错。Q";
					}
				}else{
					log.info("QT自助优惠信息出错。M" + loginname);
					return "QT自助优惠信息出错。M";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END
			

			if (remoteCredit < credit) {
				result = "转出失败，额度不足";
				log.info("remoteCredit :" + remoteCredit);
				log.info(result);
				return result ;
			}
			
			
			String withdraw = QtUtil.getWithdrawPlayerMoney(loginname, credit, transferID);
			if (QtUtil.RESULT_SUCC.equals(withdraw)) {// 转出QT成功
				transferDao.addTransferforQt(Long.parseLong(transferID), loginname, localCredit, credit, Constants.OUT, Constants.FAIL, "", "转出成功");
				tradeDao.changeCredit(loginname, credit, CreditChangeType.TRANSFER_QTOUT.getCode(), transferID, null);
			} else {
				result = "转账状态错误";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result = "QT转账发生异常: " + e.getMessage();
		}
		return result;
	}
	
	public String transferPtAndSelfYouHuiInIn(String transID, String loginname, Double remit, String remark) {
		log.info("开始转入NEWPT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账NEWPT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("NEWPT转账正在维护" + loginname);
			return "NEWPT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		if (localCredit < remit) {
			log.info("转账失败，额度不足" + loginname);
			return "转账失败，额度不足";
		}
		try {
			String couponString = null;
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] {499,420,571, 572, 573,574 ,575 ,590 , 591, 705,409,410,411,412,419,390,415,391}));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<6*60*1000){
					return "自助优惠5分钟内不允许户内转账！";
				}
				Integer type = proposal.getType();
				couponString = ProposalType.getText(type);
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				// 获取数据总投注额
				Double validBetAmount = getSelfYouHuiBet("pttiger", loginname, proposal.getPno()) ;
				
				/*****************************************/
				//后期远程金额
				Double remoteCredit=0.00;
				try {
						 remoteCredit = PtUtil.getPlayerMoney(loginname);
				} catch (Exception e) {
						return "获取异常失败";
				}
				validBetAmount = validBetAmount==null?0.0:validBetAmount;
				//判断是否达到条件
				log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
				if(validBetAmount != -1.0){
					if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
						log.info("解除成功");
					}else{
						PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"pttiger" ,validBetAmount, (amountAll-validBetAmount) , new Date() , couponString , "IN");
						transferDao.save(transferRecord) ;
						log.info("解除失败");
						return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
					}
				}else{
					log.info(loginname+"通过后台控制进行转账");
				}
			}
			tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_MEWPTIN.getCode(), transID, remark);
			return null;
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferInforSixlotteryIn(String transID, String loginname, Double remit, String ip) {
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账SIX");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("六合彩转账正在维护" + loginname);
			return "六合彩转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, -remit, CreditChangeType.TRANSFER_SIXLOTTERYIN.getCode(), transID, null);
			    return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	@Override
	public String transferInforEbetIn(String transID, String loginname, Double remit, String youhuiType,String remark) {
		log.info("开始转入Ebet准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账EBET");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("EBET转账正在维护" + loginname);
			return "EBET转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		
		
		//看玩家是否有正在使用的优惠，是否达到流水BEGIN
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "ebet"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		
		if(null != selfs && selfs.size()>0){
			SelfRecord record = selfs.get(0);
			if(null != record){
				Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
				if(null != proposal){
					if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
						return "自助优惠5分钟内不允许户内转账！";
					}
					// 要达到的总投注额
					Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
					
					// 获取数据总投注额
					Double validBetAmount = getSelfYouHuiBet("ebet", loginname, proposal.getPno()) ;
					//后期远程金额
					Double remoteCredit=0.00;
					try {
						String money = RemoteCaller.queryEbetCredit(loginname);
						if(null != money && NumberUtils.isNumber(money)){
							remoteCredit = Double.valueOf(money);
						}else{
							return "获取EBET余额错误:"+money ;
						}
					} catch (Exception e) {
							return "获取异常失败";
					}
					validBetAmount = validBetAmount==null?0.0:validBetAmount;
					//判断是否达到条件
					log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
					if(validBetAmount != -1.0){
						if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
							record.setType(1);
							record.setUpdatetime(new Date());
							update(record);
							log.info("解除成功");
						}else{
							PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"ebet" ,validBetAmount, (amountAll-validBetAmount) , new Date() , youhuiType , "IN");
							transferDao.save(transferRecord) ;
							log.info("解除失败");
							return youhuiType + " EBET：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者EBET游戏金额低于20元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
						}
					}else{
						record.setType(1);
						record.setRemark(record.getRemark()+";后台放行");
						record.setUpdatetime(new Date());
						update(record);
						log.info(loginname+"通过后台控制进行转账");
					}
				}else{
					log.info("EBET自助优惠信息出错。Q" + loginname);
					return "EBET自助优惠信息出错。Q";
				}
			}else{
				log.info("BBIN自助优惠信息出错。M" + loginname);
				return "BBIN自助优惠信息出错。M";
			}
		}
		//看玩家是否有正在使用的优惠，是否达到流水END
		
		
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
			    tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_EBETIN.getCode(), transID, remark);
			    return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	public String transferEaIn(String transID, String loginname, Double remit) {
		log.info("开始转入EA准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账EA");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("EA转账正在维护" + loginname);
			return "EA转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		// 获取额度
		Double localCredit = customer.getCredit();
		try {
			// 判断是否使用了优惠
			if (customer.getShippingcode() == null || customer.getShippingcode().equals("")) {

			} else {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", customer.getShippingcode()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 442,531, 532, 533, 534, 535, 537 ,592}));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				couponString = ProposalType.getText(type);
				
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
				// 有效投注额
				SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date beginDate = new Date();
				Date startDate = proposal.getExecuteTime();
				Long day = (Long) ((beginDate.getTime() - proposal.getExecuteTime().getTime()) / (24 * 60 * 60 * 1000));
				if (day >= 6) {
					Calendar date = Calendar.getInstance();
					date.setTime(beginDate);
					date.set(Calendar.DATE, date.get(Calendar.DATE) - 7);
					date.set(Calendar.MINUTE, date.get(Calendar.MINUTE) + 20);
					startDate = dft.parse(dft.format(date.getTime()));
				}

				Double validBetAmount = RemoteCaller.getTurnOverRequest(loginname, startDate, new Date()).getTurnover();
				validBetAmount = Math.round(validBetAmount * 100.00) / 100.00;
				Double remoteCredit = RemoteCaller.queryCredit(loginname);
				if (validBetAmount < amountAll && remoteCredit >= 20) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者EA游戏金额低于20,方能进行户内转账！";
				}
			}
			//转入日限额
			customer.setCreditday(customer.getCreditday()+remit);
			// 检查额度
			log.info("current credit:" + localCredit);
			//判断额度是否小于等于0
			if(remit<=0.0){
				log.info("额度变化为0，不操作..");
				return "度变化为0";
			} 
			//判断额度是否大于转账额度
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + localCredit+"******"+loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_IN.getCode(), transID, null);
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	public String transferInforTyIn(String transID, String loginname, Double remit) {
		log.info("开始转入SB准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账SB");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("SB转账正在维护" + loginname);
			return "SB转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			String sbloginname="e68_"+loginname;
			Sbcoupon sbcoupon=userDao.getSbcoupon( sbloginname);
			//玩家使用了优惠
			if (sbcoupon != null) {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", sbcoupon.getShippingcode()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] {581,582,584}));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				if (type == 581) {
					couponString = "188体育红包优惠券";
				}else if (type == 582) {
					couponString = "188体育存2900送580";
				}else if (type == 584) {
					couponString = "188体育20%存送优惠劵";
				}else{
					return "无此类型优惠";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
				//获取投注额
				Calendar date = Calendar.getInstance();
				date.setTime(proposal.getExecuteTime());
				date.add(Calendar.HOUR_OF_DAY, -12);
				Date startDate = date.getTime();
				Double validBetAmount = userDao.getTurnOverRequest(sbloginname, startDate, new Date());
				//获取体育远程额度
				if (validBetAmount < amountAll) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + ",方能进行户内转账！";
				}
			}
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_SBIN.getCode(), transID, null);
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * ebet转账验证流水
	 */
	@Override
	public String transfer4EbetOutVerifyBet(String loginname, String transferID, Integer credit){
		String result = null;
		log.info("begin to transfer4EbetVerifyBet:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			Double oldBalance = EBetUtil.getBalance(loginname, "EBET");
			//看玩家是否有正在使用的优惠，是否达到流水BEGIN
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "ebet"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if(null != selfs && selfs.size()>0){
				SelfRecord record = selfs.get(0);
				if(null != record){
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					if(null != proposal){
						if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						
						// 获取数据总投注额
						Double validBetAmount = getSelfYouHuiBet("ebet", loginname, proposal.getPno()) ;
						//后期远程金额
						Double remoteCredit=0.00;
						try {
							String money = RemoteCaller.queryEbetCredit(loginname);
							if(null != money && NumberUtils.isNumber(money)){
								remoteCredit = Double.valueOf(money);
							}else{
								return "获取EBET余额错误:"+money ;
							}
						} catch (Exception e) {
								return "获取异常失败";
						}
						validBetAmount = validBetAmount==null?0.0:validBetAmount;
						//判断是否达到条件
						log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
						if(validBetAmount != -1.0){
							if ((validBetAmount >= amountAll ||  remoteCredit < 20)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							}else{
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"ebet" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
								transferDao.save(transferRecord) ;
								log.info("解除失败");
								return "EBET ：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者EBET游戏金额低于20元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
							}
						}else{
							record.setType(1);
							record.setRemark(record.getRemark()+";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname+"通过后台控制进行转账");
						}
					}else{
						log.info("EBET自助优惠信息出错。Q" + loginname);
						return "EBET自助优惠信息出错。Q";
					}
				}else{
					log.info("EBET自助优惠信息出错。M" + loginname);
					return "EBET自助优惠信息出错。M";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END
			

			String ebetT = null;
			ebetT = RemoteCaller.tradeEbetRequest(loginname, credit.intValue(), "OUT", transferID);
			if(null == ebetT && StringUtils.isEmpty(ebetT)){
				Double newBalance = EBetUtil.getBalance(loginname, "EBET");
				
				if(oldBalance.equals(newBalance)){
					throw new GenericDfhRuntimeException("EBET转出不成功  ， 数据回滚。");
				}
				transferDao.addTransferforEbet(Long.parseLong(transferID), loginname, customer.getCredit(), credit.doubleValue(), Constants.OUT, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, credit.doubleValue(), CreditChangeType.TRANSFER_EBETOUT.getCode(), transferID, null);
				//vo.setMessage("自助EBET次存优惠成功");
			}else{
				result = "EBET转账状态错误:" + ebetT;
			}
			/*if (wd_res != null && dspPrepareResponseBean.getInfo().equals("11100")) {
				transferDao.addTransferforBbin(Long.parseLong(transferID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_BBINOUT.getCode(), transferID, null);
				// tradeDao.changeCredit(loginname, remit,
				// CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
			} else if(dspPrepareResponseBean!=null){
				result = "转账状态错误:" + dspPrepareResponseBean.getInfo();
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			result = "EBET转账发生异常";
		}
		return result;
	}
	
	@SuppressWarnings("finally")
	@Override
	public String transferJc(String transID, String loginname, Double remit, String toOrFrom) {
		log.info("开始JC转账准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账JC");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("JC转账正在维护" + loginname);
			return "时时彩转账正在维护";
		}
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		try {
			if (customer == null) {
				return "玩家账号不存在";
			}
			if (customer.getFlag() == 1) {
				return "该账号已经禁用";
			}
			Double oldCredit = customer.getCredit();
			//预创建错误日志
			Actionlogs actionlog = new Actionlogs();
			actionlog.setLoginname(loginname);
			actionlog.setRemark(null);
			actionlog.setCreatetime(DateUtil.now());
			actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
			if("TO".equals(toOrFrom)){
				if (customer.getCredit() < remit){
					return "玩家可用金额不足";
				}
				log.info("Transfer to jc --> "+customer.getLoginname());
				//使用查询余额功能检查用户是否存在
				String balbody = JCUtil.balanceJC(customer.getLoginname());
				if (JSONObject.fromObject(balbody).getString("code").equals("j10000")){
					//扣除账户金额
					tradeDao.changeCredit(customer.getLoginname(), -remit, CreditChangeType.TRANSFER_JCIN.getCode(), transID, null);
					//记录转账记录
					Transfer record = transferDao.addTransferforJc(Long.valueOf(transID), customer.getLoginname(), oldCredit, remit, Constants.IN, "转账成功");
					if (record!=null){
						//调用JC转账接口
						String body = JCUtil.transferToJC(customer.getLoginname(), remit);
						JSONObject json = JSONObject.fromObject(body);
						if (json.getString("code").equals("j10000")){
							return null;
						} else {
							//记录错误日志
							String info = "转账至JC错误!改变前订单:" +transID+ " 改变前:" +oldCredit+ "改变后:" +(oldCredit - remit)+ " 改变额度:" +remit+ " 系统已经处理 请检查是有否错误";
							actionlog.setRemark(info);
							save(actionlog);
							log.error("Transfer to jc error --> "+customer.getLoginname()+json+info);
							return json.getString("message");
						}
					} else {
						log.error("Transfer to jc add record fail -->"+customer.getLoginname());
						return "记录交易日志失败,扣除款项";
					}
				} else {
					return JSONObject.fromObject(balbody).getString("message");
				}
			} else if ("FROM".equals(toOrFrom)){
				log.info("Transfer From Jc --> "+customer.getLoginname());
				String balbody = JCUtil.balanceJC(customer.getLoginname());//检查巨彩余额是否不足
				JSONObject baljson = JSONObject.fromObject(balbody);
				if (baljson.getString("code").equals("j10000")){
					Double cbal = baljson.getDouble("balance");
					if (cbal<remit){
						return "时时彩余额不足";
					}
				} else {
					return baljson.getString("message");
				}
				String body = JCUtil.transferFromJC(customer.getLoginname(), remit);
				JSONObject json = JSONObject.fromObject(body);
				if (json.getString("code").equals("j10000")){
					tradeDao.changeCredit(customer.getLoginname(), remit, CreditChangeType.TRANSFER_JCOUT.getCode(), transID, null);
					Transfer record = transferDao.addTransferforJc(Long.valueOf(transID), customer.getLoginname(), oldCredit, remit, Constants.OUT, "转账成功");
					if (record!=null){
						return null;
					} else {
						log.error("Transfer from jc add record fail -->"+customer.getLoginname());
						return "记录交易日志失败,扣除款项";
					}
				} else {
					//记录错误日志
					String info = "转账至JC错误!错误信息:" +json.getString("message")+ " 改变前:" +oldCredit+ "改变后:" +oldCredit+ " 改变额度:" +remit+ " 系统已经处理 请检查是有否错误";
					actionlog.setRemark(info);
					save(actionlog);
					log.error("Transfer from jc error --> "+customer.getLoginname()+json+info);
					return json.getString("message");
				}
			} else {
				return "传入转账类型错误";
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 从NT转出至E68
	 * @param transferID
	 * @param id
	 * @param loginname
	 * @param credit
	 * @return
	 */
	@Override
	public String transferFromNTJudge(String transferID, String loginname, Double credit) {
		String result = null;
		log.info("begin to transferNT:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			Double remoteCredit=0.00;
			try {
				JSONObject balj = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
				if (balj.getBoolean("result")){
					remoteCredit=balj.getDouble("balance");
				} else {
					return "从NT转出时获取余额错误,"+balj.getString("error");
				}
			} catch (Exception e) {
				return "获取异常失败";
			}
			if(remoteCredit >=1 && remoteCredit<Constants.MAXIMUMDTOUT){
				DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
				dc8.add(Restrictions.eq("loginname", loginname)) ;
				dc8.add(Restrictions.eq("target", "nt")) ;
				dc8.addOrder(Order.desc("createtime"));
				List<Transfer> transfers = transferDao.findByCriteria(dc8, 0, 10) ;
				if(null != transfers && transfers.size()>0){
					Transfer transfer = transfers.get(0);
					if(null != transfer){
						if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
							return "您正在使用体验金，NT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
						}
						if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是[app下载彩金]的转账记录
							return "您正在使用app下载彩金，NT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
						} 
					}
				}
			}
			
			//是否存在已领取的周周回馈，金额小于等于5元或达到流水可进行户内转账
			DetachedCriteria wsc=DetachedCriteria.forClass(WeekSent.class);
			wsc.add(Restrictions.eq("username", loginname));
			wsc.add(Restrictions.eq("platform", "nt"));
			wsc.add(Restrictions.eq("status", "1"));
			List<WeekSent> wsList = findByCriteria(wsc);
			for (WeekSent weekSent : wsList) {
				if(remoteCredit > 5){
					Double requiredBet = Arith.mul(weekSent.getPromo(), weekSent.getTimes());
					//计算流水
					String totalBetSql = "select SUM(betCredit) from ptprofit where loginname=:username and starttime>=:startTime";
					Map<String, Object> prams = new HashMap<String, Object>();
					prams.put("username", loginname);
					//prams.put("platform", "NT");
					Calendar cd = Calendar.getInstance();
					cd.setTime(weekSent.getGetTime());
					cd.set(Calendar.HOUR_OF_DAY, 0);
					cd.set(Calendar.MINUTE, 0);
					cd.set(Calendar.SECOND, 0);
					cd.set(Calendar.MILLISECOND, 0);
					prams.put("startTime", cd.getTime());
					Double realBet = transferDao.getDoubleValueBySql(totalBetSql, prams);
					if(requiredBet > realBet){
						log.info("玩家：" + loginname + " 已领取周周回馈，NT余额大于5元且未达到提款流水，不允许转出");
						return "您已领取周周回馈，流水须达到 " + requiredBet + " 元或者游戏余额不多于5元才能转出，目前流水额为：" + realBet + " 元";
					}
				}else{
					log.info("玩家：" + loginname + " 已领取周周回馈，NT金额"+remoteCredit+"，准备转出");
				}
			}
			//将已领取周周回馈，更新为已处理
			for (WeekSent ws : wsList) {
				ws.setStatus("2");
				update(ws);
			}
			
			//是否存在已领取的负盈利反赠，如果存在判断流水
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "nt"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				Double requiredBet = Arith.mul(losePromo.getPromo(), losePromo.getTimes());
				//计算流水
				String totalBetSql = "select SUM(betCredit) from ptprofit where loginname=:username and starttime>=:startTime";
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("username", loginname);
				//prams.put("platform", "NT");
				Calendar cd = Calendar.getInstance();
				cd.setTime(losePromo.getGetTime());
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				prams.put("startTime", cd.getTime());
				Double realBet = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), losePromo.getBetting());
				if(requiredBet > realBet){
					log.info("玩家：" + loginname + " 已领取负盈利反赠，未达到提款流水，不允许转出");
					return "您已领取救援金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：" + realBet + " 元";
				}
			}
			//将已领取负盈利反赠，更新为已处理
			for (LosePromo losePromo2 : losePromoList) {
				losePromo2.setStatus("2");
				update(losePromo2);
			}
			
			//看玩家是否有正在使用的优惠，是否达到流水BEGIN
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "nt"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if(null != selfs && selfs.size()>0){
				SelfRecord record = selfs.get(0);
				if(null != record){
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					if(null != proposal){
						if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
						Double validBetAmount=null;
						if(preferential.getType().equals(1)){
							validBetAmount=-1.0;
						}else{
						//从使用存送优惠截止到现在的投注额
						String totalBetSql = "select SUM(betCredit) from ptprofit where loginname=:username and starttime>=:startTime";
						Map<String, Object> prams = new HashMap<String, Object>();
						prams.put("username", loginname);
						//prams.put("platform", "nt");
						Calendar cd = Calendar.getInstance();
						cd.setTime(record.getCreatetime());
						cd.set(Calendar.HOUR_OF_DAY, 0);
						cd.set(Calendar.MINUTE, 0);
						cd.set(Calendar.SECOND, 0);
						cd.set(Calendar.MILLISECOND, 0);
						prams.put("startTime", cd.getTime());
						validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
						}
						//Double validBetAmount = getSelfYouHuiBet("NT", loginname, proposal.getPno()) ;
						//后期远程金额
						validBetAmount = validBetAmount==null?0.0:validBetAmount;
						//判断是否达到条件
						log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
						if(validBetAmount != -1.0){
							if ((validBetAmount >= amountAll ||  remoteCredit < 1)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							}else{
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"nt" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
								transferDao.save(transferRecord);
								log.info("解除失败");
								return "NT ：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者NT游戏金额低于1元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
							}
						}else{
							record.setType(1);
							record.setRemark(record.getRemark()+";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname+"通过后台控制进行转账");
						}
					}else{
						log.info("NT自助优惠信息出错。Q" + loginname);
						return "NT自助优惠信息出错。Q";
					}
				}else{
					log.info("NT自助优惠信息出错。M" + loginname);
					return "NT自助优惠信息出错。M";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END
			

			if (remoteCredit < credit) {
				result = "转出失败，额度不足";
				log.info("remoteCredit :" + remoteCredit);
				log.info(result);
				return result ;
			}
			
			
			JSONObject withdraw = JSONObject.fromObject(NTUtils.changeMoney(loginname, -credit));
			if (withdraw!=null && withdraw.getBoolean("result")) {// 转出NT成功
				this.addTransferforNT(Long.parseLong(transferID), loginname, localCredit, credit, Constants.OUT, Constants.FAIL, "", "转出成功");
				tradeDao.changeCredit(loginname, credit, CreditChangeType.TRANSFER_NTOUT.getCode(), transferID, null);
			} else {
				result = withdraw.getString("msg");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result = "NT转账发生异常: " + e.getMessage();
		}
		return result;
	}
	
	/**
	 * E68转入至NT
	 * @param transID @param id @param loginname @param remit @param youhuiType @param remark
	 * @return
	 */
	@Override
	public String transferToNTJudge(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入NT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账NT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("NT转账正在维护" + loginname);
			return "NT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		
		
		//看玩家是否有正在使用的优惠，是否达到流水NT
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "nt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		
		if(null != selfs && selfs.size()>0){
			SelfRecord record = selfs.get(0);
			if(null != record){
				Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
				if(null != proposal){
					if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
						return "自助优惠5分钟内不允许户内转账！";
					}
					// 要达到的总投注额
					Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
					PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
					Double validBetAmount =null;
					if(preferential.getType().equals(1)){
						validBetAmount=-1.0;
					}else{
						//从使用存送优惠截止到现在的投注额
						String totalBetSql = "select SUM(betCredit) from ptprofit where loginname=:username and starttime>=:startTime";
						Map<String, Object> prams = new HashMap<String, Object>();
						prams.put("username", loginname);
						//prams.put("platform", "nt");
						Calendar cd = Calendar.getInstance();
						cd.setTime(record.getCreatetime());
						cd.set(Calendar.HOUR_OF_DAY, 0);
						cd.set(Calendar.MINUTE, 0);
						cd.set(Calendar.SECOND, 0);
						cd.set(Calendar.MILLISECOND, 0);
						prams.put("startTime", cd.getTime());
						validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
					}
					//获取NT远程金额
					Double remoteCredit=0.00;
					try {
						JSONObject balj = JSONObject.fromObject(NTUtils.getNTMoney(loginname));
						if (balj.getBoolean("result")){
							remoteCredit=balj.getDouble("balance");
						} else {
							return "转账至NT时获取余额错误,"+balj.getString("error");
						}
					} catch (Exception e) {
							return "获取异常失败";
					}
					validBetAmount = validBetAmount==null?0.0:validBetAmount;
					//判断是否达到条件
					log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
					if(validBetAmount != -1.0){
						if ((validBetAmount >= amountAll || remoteCredit < 5)) {
							record.setType(1);
							record.setUpdatetime(new Date());
							update(record);
							log.info("解除成功");
						}else{
							PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"nt" ,validBetAmount, (amountAll-validBetAmount) , new Date() , youhuiType , "IN");
							transferDao.save(transferRecord) ;
							log.info("解除失败");
							return youhuiType + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者nt游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
						}
					}else{
						record.setType(1);
						record.setRemark(record.getRemark()+";后台放行");
						record.setUpdatetime(new Date());
						update(record);
						log.info(loginname+"通过后台控制进行转账");
					}
				}else{
					log.info("NT自助优惠信息出错。Q" + loginname);
					return "NT自助优惠信息出错。Q";
				}
			}else{
				log.info("NT自助优惠信息出错。M" + loginname);
				return "NT自助优惠信息出错。M";
			}
		}
		//看玩家是否有正在使用的优惠，是否达到流水END
		
		
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit && !"红包优惠".equals(youhuiType)) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			}
			 else if("红包优惠".equals(youhuiType)) {
			     tradeDao.changeCreditIn(customer, 0.0, CreditChangeType.TRANSFER_REDCOUPONS_NTIN.getCode(), transID, remark);
			    return null;
			}
			 else{
				   tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_NTIN.getCode(), transID, remark);
			 }
			return null;
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	
	//获取某个时间段的mg投注额
	private Double getMgBetsAmount(String loginname, String startTime, String endTime) {
		String sql = "select SUM(amnt) bet from mg_data where mbrCode=:loginname and transType='bet' and transTime >=:startTime and transTime<:endTime and transId not in (select refTransId from mg_data where transType='refund' and mbrCode=:loginname) ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return transferDao.getDoubleValueBySql(sql, params);
	}	
	
	
	/************
	 * e68转入 MG
     * @return
	 */
	public String transferInforMgIn(String transID, String loginname, Double remit, String youhuiType, String remark) { 
		log.info("开始转入MG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("MG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("MG转账正在维护" + loginname);
			return "MG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		
		
		//看玩家是否有正在使用的优惠，是否达到流水  MG
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "mg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		
		if(null != selfs && selfs.size()>0){
			SelfRecord record = selfs.get(0);
			if(null != record){
				Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
				if(null != proposal){
					if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
						return "自助优惠5分钟内不允许户内转账！";
					}
					// 要达到的总投注额
					Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
					PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
					Double validBetAmount =null;
					if(preferential.getType().equals(1)){
						validBetAmount=-1.0;
					}else{
						//从使用存送优惠截止到现在的投注额
						/*String totalBetSql = "select SUM(betCredit) from ptprofit where loginname=:username and starttime>=:startTime";
						Map<String, Object> prams = new HashMap<String, Object>();
						prams.put("username", loginname);
						//prams.put("platform", "nt");
						Calendar cd = Calendar.getInstance();
						cd.setTime(record.getCreatetime());
						cd.set(Calendar.HOUR_OF_DAY, 0);
						cd.set(Calendar.MINUTE, 0);
						cd.set(Calendar.SECOND, 0);
						cd.set(Calendar.MILLISECOND, 0);
						prams.put("startTime", cd.getTime());
						validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());*/
						
						Date startTime = record.getCreatetime();
					    Date endTime = new Date();
					    try {
					    	//查询投注额
					        validBetAmount = getMgBetsAmount(loginname, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(endTime));
					        System.out.println(validBetAmount+"--->投注额");
					       } catch (Exception e) {
					        e.printStackTrace();
					        log.error("查询:" + loginname + ",时间：" + DateUtil.formatDateForStandard(startTime)+ "-----" +  DateUtil.formatDateForStandard(endTime) + "MG投注额异常：", e);
					        return "您领取了自助存送优惠，但查询" + DateUtil.formatDateForStandard(startTime)+ "--" +  DateUtil.formatDateForStandard(endTime) + "MG投注额异常,无法进行转账,请稍后再试";
					       }
					}
					//获取MG远程金额
					Double remoteCredit=0.00;
					try {
						//remoteCredit = MGSUtil.getBalance(loginname,customer.getPassword());
						remoteCredit = MGSUtil.getBalance(loginname);
						if (remoteCredit != null) {
							
						} else {
							return "转账至MG时获取余额错误"+remoteCredit;
						}
					} catch (Exception e) {
							return "获取异常失败";
					}
					validBetAmount = validBetAmount==null?0.0:validBetAmount;
					//判断是否达到条件
					log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
					if(validBetAmount != -1.0){
						if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
							record.setType(1);
							record.setUpdatetime(new Date());
							update(record);
							log.info("解除成功");
						}else{
							//PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"nt" ,validBetAmount, (amountAll-validBetAmount) , new Date() , youhuiType , "IN");
							PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"mg" ,validBetAmount, (amountAll-validBetAmount) , new Date() , youhuiType , "IN");
							transferDao.save(transferRecord) ;
							log.info("解除失败");
							return youhuiType + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者mg游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
						}
					}else{
						record.setType(1);
						record.setRemark(record.getRemark()+";后台放行");
						record.setUpdatetime(new Date());
						update(record);
						log.info(loginname+"通过后台控制进行转账");
					}
				}else{
					log.info("MG自助优惠信息出错。Q" + loginname);
					return "MG自助优惠信息出错。Q";
				}
			}else{
				log.info("MG自助优惠信息出错。M" + loginname);
				return "MG自助优惠信息出错。M";
			}
		}
		//看玩家是否有正在使用的优惠，是否达到流水END
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit && !"红包优惠".equals(youhuiType)) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			}
			 else if("红包优惠".equals(youhuiType)) {
			     tradeDao.changeCreditIn(customer, 0.0, CreditChangeType.TRANSFER_REDCOUPONS_MGIN.getCode(), transID, remark);
			    return null;
			}
			 else{
				  tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_MGIN.getCode(), transID, remark);
			 }
			return null;
		} catch (Exception e) {
			log.error("transferToMGJudge error", e);
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	
	
	//DT转账
	@Override
	public String transferInforDtIn(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入DT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constDt = transferDao.getConsts("转账DT");
		if (constDt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constDt.getValue().equals("0")) {
			log.info("DT转账正在维护" + loginname);
			return "DT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		
		
		//看玩家是否有正在使用的优惠，是否达到流水TTG
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "dt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		
		if(null != selfs && selfs.size()>0){
			SelfRecord record = selfs.get(0);
			if(null != record){
				Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
				if(null != proposal){
					if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
						return "自助优惠5分钟内不允许户内转账！";
					}
					// 要达到的总投注额
					Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
					youhuiType = StringUtils.isBlank(youhuiType)? ProposalType.getText(proposal.getType()): youhuiType;
					PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
					Double validBetAmount =null;
					if(preferential.getType().equals(1)){
						validBetAmount=-1.0;
					}else{
						//从使用存送优惠截止到现在的投注额
						List list = DtUtil.getbet(loginname, DateUtil.formatDateForStandard(record.getCreatetime()), DateUtil.formatDateForStandard(new Date()));
						
						if (null != list && !list.isEmpty()) {
							Object obj = list.get(0);
							Map<String, Object> map = (Map<String, Object>) obj;
						    String betPrice = String.valueOf(map.get("betPrice"));
						    validBetAmount = Double.parseDouble(betPrice);
						}
					}
					//后期远程金额
					Double remoteCredit=0.00;
					try {
						String money = DtUtil.getamount(loginname);
						if(null != money && NumberUtils.isNumber(money)){
							remoteCredit = Double.valueOf(money);
						}else{
							return "获取DT余额错误:"+money ;
						}
					} catch (Exception e) {
							return "获取异常失败";
					}
					validBetAmount = validBetAmount==null?0.0:validBetAmount;
					//判断是否达到条件
					log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
					if(validBetAmount != -1.0){
						if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
							record.setType(1);
							record.setUpdatetime(new Date());
							update(record);
							log.info("解除成功");
						}else{
							PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"dt" ,validBetAmount, (amountAll-validBetAmount) , new Date() , youhuiType , "IN");
							transferDao.save(transferRecord) ;
							log.info("解除失败");
							return youhuiType + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者dt游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
						}
					}else{
						record.setType(1);
						record.setRemark(record.getRemark()+";后台放行");
						record.setUpdatetime(new Date());
						update(record);
						log.info(loginname+"通过后台控制进行转账");
					}
				}else{
					log.info("DT自助优惠信息出错。Q" + loginname);
					return "DT自助优惠信息出错。Q";
				}
			}else{
				log.info("DT自助优惠信息出错。M" + loginname);
				return "DT自助优惠信息出错。M";
			}
		}
		//看玩家是否有正在使用的优惠，是否达到流水END
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit && !"红包优惠".equals(youhuiType)) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			}else if("红包优惠".equals(youhuiType) && remark != null && remark.contains("红包优惠") ) {
			    tradeDao.changeCreditIn(customer, 0.0, CreditChangeType.TRANSFER_REDCOUPONS_DTIN.getCode(), transID, remark);
			    return null;
			}else{
				tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_DTIN.getCode(), transID, remark);
			}
			return null;
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String transferDtAndSelfYouHuiOut(String transferID, String loginname, Double credit) {
	
		String result = null;
		
		try {
			
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			
			if (1 == customer.getFlag()) {
				
				return "该账号已经禁用！ ";
			}
			
			Const constPt = transferDao.getConsts("转账DT");
			
			if (null == constPt) {
				
				return "平台不存在！";
			}
			
			if ("0".equals(constPt.getValue())) {
				
				return "DT转账功能正在维护！";
			}
			
			Double localCredit = customer.getCredit();
			
			Double remoteCredit = 0.00;
			
			try {
				
				String money = DtUtil.getamount(loginname);
				
				if (StringUtils.isNotEmpty(money) && NumberUtils.isNumber(money)) {
					
					remoteCredit = Double.valueOf(money);
				} else {
					
					return "获取DT账户余额错误：" + money;
				}
			} catch (Exception e) {
				
				return "获取DT账户余额失败！";
			}
			
			log.info("transferDtAndSelfYouHuiOut方法remoteCredit变量的值为：" + remoteCredit);
			
			if (remoteCredit < credit) {
				
				return "转出失败，额度不足！";
			}
			
			if (remoteCredit >= 1 && remoteCredit < Constants.MAXIMUMDTOUT) {
				
				DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
				dc8.add(Restrictions.eq("loginname", loginname));
				dc8.add(Restrictions.eq("target", "dt"));
				dc8.addOrder(Order.desc("createtime"));
				
				List<Transfer> transfers = transferDao.findByCriteria(dc8, 0, 2);
				
				if (null != transfers && !transfers.isEmpty()) {
					
					Transfer transfer = transfers.get(0);
					
					// 最后一笔是体验金的转账记录
					if (transfer.getRemark().contains("自助优惠")) { 
						
						return "您正在使用体验金，DT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					}
					if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是[app下载彩金]的转账记录
						return "您正在使用app下载彩金，DT余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
					} 
				}
			}
			
			// 是否存在已领取的周周回馈，金额小于等于5元或达到流水可进行户内转账
			DetachedCriteria wsc = DetachedCriteria.forClass(WeekSent.class);
			wsc.add(Restrictions.eq("username", loginname));
			wsc.add(Restrictions.eq("platform", "dt"));
			wsc.add(Restrictions.eq("status", "1"));
			
			List<WeekSent> wsList = findByCriteria(wsc);
			
			if (null != wsList && !wsList.isEmpty()) {
				
				for (WeekSent weekSent : wsList) {
					
					if (remoteCredit > 5) {
						
						Double requiredBet = Arith.mul(weekSent.getPromo(), weekSent.getTimes());
						
						Calendar cd = Calendar.getInstance();
						cd.setTime(weekSent.getGetTime());
						cd.set(Calendar.HOUR_OF_DAY, 0);
						cd.set(Calendar.MINUTE, 0);
						cd.set(Calendar.SECOND, 0);
						cd.set(Calendar.MILLISECOND, 0);
						
						// 计算流水
						String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
						
						Map<String, Object> prams = new HashMap<String, Object>();
						prams.put("username", loginname);
						prams.put("platform", "dt");
						prams.put("startTime", cd.getTime());
						
						Double realBet = transferDao.getDoubleValueBySql(totalBetSql, prams);
						
						if (requiredBet > realBet) {
							
							return "您已领取周周回馈，流水须达到 " + requiredBet + " 元或者游戏余额不多于5元才能转出，目前流水额为：" + realBet + " 元";
						}
					}
				}

				// 将已领取周周回馈，更新为已处理
				for (WeekSent ws : wsList) {
					
					ws.setStatus("2");
					update(ws);
				}
			}
			
			// 是否存在已领取的负盈利反赠，如果存在判断流水
			DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "dt"));
			c.add(Restrictions.eq("status", "1"));
			
			List<LosePromo> losePromoList = findByCriteria(c);
			
			if (null != losePromoList && !losePromoList.isEmpty()) {
				
				for (LosePromo losePromo : losePromoList) {
					
					Double requiredBet = Arith.mul(losePromo.getPromo(), losePromo.getTimes());
					// 计算流水
					
					JSONArray a = JSONArray.fromObject(DtUtil.getbet(loginname, DateUtil.formatDateForStandard(losePromo.getGetTime()), DateUtil.formatDateForStandard(new Date())));
					if (a.size()>0) {
						JSONObject bet=JSONObject.fromObject(a.get(0));
						Double realBet = Double.parseDouble(String.valueOf(bet.get("betPrice"))) ;
						if(requiredBet > realBet){
							log.info("玩家：" + loginname + " 已领取负盈利反赠，未达到提款流水，不允许转出");
							return "您已领取救援金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：" + realBet + " 元";
						}
					}else {
						log.info("玩家：" + loginname + " 已领取负盈利反赠，未达到提款流水，不允许转出");
						return "您已领取救援金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：0  元";
					}

				}
				
				// 将已领取负盈利反赠，更新为已处理
				for (LosePromo losePromo2 : losePromoList) {
					
					losePromo2.setStatus("2");
					update(losePromo2);
				}
			}
			
			// 看玩家是否有正在使用的优惠，是否达到流水BEGIN
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "dt"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if (null != selfs && !selfs.isEmpty()) {

				SelfRecord record = selfs.get(0);

				Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
				
				if (null != proposal) {

					if (((new Date()).getTime() - proposal.getExecuteTime().getTime()) < 5 * 60 * 1000) {
						
						return "自助优惠5分钟内不允许户内转账！";
					}
					
					// 要达到的总投注额
					Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
					
					Double validBetAmount = null;
					
					PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
					
					if (null != preferential) {
						
						if (1 == preferential.getType()) {
							
							validBetAmount = -1.0;
						} else {
							
							List list = DtUtil.getbet(loginname, DateUtil.formatDateForStandard(record.getCreatetime()), DateUtil.formatDateForStandard(new Date()));
							
							if (null != list && !list.isEmpty()) {
								
								Object obj = list.get(0);
								Map<String, Object> map = (Map<String, Object>) obj;
							    String betPrice = String.valueOf(map.get("betPrice"));
							    validBetAmount = Double.parseDouble(betPrice);
							}
						}
					}
					
					validBetAmount = validBetAmount == null ? 0.0 : validBetAmount;
					log.info(loginname + "->自助优惠解除限制参数validBetAmount：" + validBetAmount + "，amountAll:" + amountAll + "，remoteCredit:" + remoteCredit);
					
					if (validBetAmount != -1.0) {
						
						if ((validBetAmount >= amountAll || remoteCredit < 1)) {
							
							record.setType(1);
							record.setUpdatetime(new Date());
							
							update(record);
						} else {
							
							PreferTransferRecord transferRecord = new PreferTransferRecord(loginname, "dt", validBetAmount, (amountAll - validBetAmount), new Date(), null, "IN");
							
							transferDao.save(transferRecord);
							
							return "DT ：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者DT游戏金额低于1元,方能进行户内转账！(还差" + (amountAll - validBetAmount) + ",或稍后5分钟 系统更新记录再来查询)";
						}
					} else {
						
						record.setType(1);
						record.setUpdatetime(new Date());
						record.setRemark(record.getRemark() + ";后台放行");
						
						update(record);
					}
				} else {
					
					log.info("自助DT优惠信息出错，根据pno=“" + record.getPno() + "”去proposal表查询时数据为空！");
						
					return "自助DT优惠信息出错。";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END
			
			String withdraw = DtUtil.withdrawordeposit(loginname, credit * -1);
			
			if (withdraw != null && withdraw.equals("success")) {
				
				transferDao.addTransferforDt(Long.parseLong(transferID), loginname, localCredit, credit, Constants.OUT, Constants.FAIL, "", "转出成功");
				
				tradeDao.changeCredit(loginname, credit, CreditChangeType.TRANSFER_DTOUT.getCode(), transferID, null);
			} else {
				
				result = "DT转账状态错误！";
			}
		} catch (Exception e) {
			
			log.error("DT转账发生异常，异常信息：" + e.getMessage());
			
			result = "DT转账发生异常，请联系客服处理！";
		}
		
		return result;
	}
	
	/**
	 * Dt转出  验证流水
	 */
	/*public String transferDtAndSelfYouHuiOut(String transferID, String loginname, Double credit) {
		String result = null;
		log.info("begin to transferttg:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			Double remoteCredit=0.00;
			try {
				String money = DtUtil.getamount(loginname);
				if(null != money && NumberUtils.isNumber(money)){
					remoteCredit = Double.valueOf(money);
				}else{
					return "获取DT余额错误:"+money ;
				}
			} catch (Exception e) {
					return "获取异常失败";
			}
			if(remoteCredit >=1 && remoteCredit<100){
				DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
				dc8.add(Restrictions.eq("loginname", loginname)) ;
				dc8.add(Restrictions.eq("target", "dt")) ;
				dc8.addOrder(Order.desc("createtime"));
				List<Transfer> transfers = transferDao.findByCriteria(dc8, 0, 2) ;
				if(null != transfers && transfers.size()>0){
					Transfer transfer = transfers.get(0);
					if(null != transfer){
						if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
							return "您正在使用体验金，DT余额大于等于100或者小于1的时候才能进行户内转账";
						}
					}
				}
			}
			//是否存在已领取的周周回馈，金额小于等于5元或达到流水可进行户内转账
			DetachedCriteria wsc=DetachedCriteria.forClass(WeekSent.class);
			wsc.add(Restrictions.eq("username", loginname));
			wsc.add(Restrictions.eq("platform", "dt"));
			wsc.add(Restrictions.eq("status", "1"));
			List<WeekSent> wsList = findByCriteria(wsc);
			for (WeekSent weekSent : wsList) {
				if(remoteCredit > 5){
					Double requiredBet = Arith.mul(weekSent.getPromo(), weekSent.getTimes());
					//计算流水
					String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
					Map<String, Object> prams = new HashMap<String, Object>();
					prams.put("username", loginname);
					prams.put("platform", "dt");
					Calendar cd = Calendar.getInstance();
					cd.setTime(weekSent.getGetTime());
					cd.set(Calendar.HOUR_OF_DAY, 0);
					cd.set(Calendar.MINUTE, 0);
					cd.set(Calendar.SECOND, 0);
					cd.set(Calendar.MILLISECOND, 0);
					prams.put("startTime", cd.getTime());
					Double realBet = transferDao.getDoubleValueBySql(totalBetSql, prams);
					if(requiredBet > realBet){
						log.info("玩家：" + loginname + " 已领取周周回馈，dt余额大于5元且未达到提款流水，不允许转出");
						return "您已领取周周回馈，流水须达到 " + requiredBet + " 元或者游戏余额不多于5元才能转出，目前流水额为：" + realBet + " 元";
					}
				}else{
					log.info("玩家：" + loginname + " 已领取周周回馈，dt金额"+remoteCredit+"，准备转出");
				}
			}
			//将已领取周周回馈，更新为已处理
			for (WeekSent ws : wsList) {
				ws.setStatus("2");
				update(ws);
			}
			
			//是否存在已领取的负盈利反赠，如果存在判断流水
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "dt"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				Double requiredBet = Arith.mul(losePromo.getPromo(), losePromo.getTimes());
				//计算流水
				String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("username", loginname);
				prams.put("platform", "dt");
				Calendar cd = Calendar.getInstance();
				cd.setTime(losePromo.getGetTime());
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				prams.put("startTime", cd.getTime());
				Double realBet = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), losePromo.getBetting());
				if(requiredBet > realBet){
					log.info("玩家：" + loginname + " 已领取负盈利反赠，未达到提款流水，不允许转出");
					return "您已领取救援金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：" + realBet + " 元";
				}
			}
			//将已领取负盈利反赠，更新为已处理
			for (LosePromo losePromo2 : losePromoList) {
				losePromo2.setStatus("2");
				update(losePromo2);
			}
			
			//看玩家是否有正在使用的优惠，是否达到流水BEGIN
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "dt"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if(null != selfs && selfs.size()>0){
				SelfRecord record = selfs.get(0);
				if(null != record){
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					if(null != proposal){
						if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
						Double validBetAmount=null;
						if(preferential.getType().equals(1)){
							validBetAmount=-1.0;
						}else{
						//从使用存送优惠截止到现在的投注额
						String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform=:platform and starttime>=:startTime";
						Map<String, Object> prams = new HashMap<String, Object>();
						prams.put("username", loginname);
						prams.put("platform", "dt");
						Calendar cd = Calendar.getInstance();
						cd.setTime(record.getCreatetime());
						cd.set(Calendar.HOUR_OF_DAY, 0);
						cd.set(Calendar.MINUTE, 0);
						cd.set(Calendar.SECOND, 0);
						cd.set(Calendar.MILLISECOND, 0);
						prams.put("startTime", cd.getTime());
						validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
						}
						//Double validBetAmount = getSelfYouHuiBet("ttg", loginname, proposal.getPno()) ;
						//后期远程金额
						validBetAmount = validBetAmount==null?0.0:validBetAmount;
						//判断是否达到条件
						log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
						if(validBetAmount != -1.0){
							if ((validBetAmount >= amountAll ||  remoteCredit < 1)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							}else{
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"dt" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
								transferDao.save(transferRecord) ;
								log.info("解除失败");
								return "DT ：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者DT游戏金额低于1元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
							}
						}else{
							record.setType(1);
							record.setRemark(record.getRemark()+";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname+"通过后台控制进行转账");
						}
					}else{
						log.info("DT自助优惠信息出错。Q" + loginname);
						return "DT自助优惠信息出错。Q";
					}
				}else{
					log.info("DT自助优惠信息出错。M" + loginname);
					return "DT自助优惠信息出错。M";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END
			

			if (remoteCredit < credit) {
				result = "转出失败，额度不足";
				log.info("remoteCredit :" + remoteCredit);
				log.info(result);
				return result ;
			}
			
			
			String withdraw = DtUtil.withdrawordeposit(loginname, credit*-1);
			if (withdraw != null && withdraw.equals("success")) {// 转出TT成功
				transferDao.addTransferforDt(Long.parseLong(transferID), loginname, localCredit, credit, Constants.OUT, Constants.FAIL, "", "转出成功");
				tradeDao.changeCredit(loginname, credit, CreditChangeType.TRANSFER_DTOUT.getCode(), transferID, null);
			} else {
				result = "转账状态错误";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常";
			throw new GenericDfhRuntimeException("转账发生异常，数据回滚");
		}
		return result;
	}*/

	@Override
	/**
	 *  copy from transferToNTJudge
	 */
	public String transferToEBetApp(String transID, String loginname, Double remit, String youhuiType, String remark) {
		final String platform = "EBetApp";
		final String platformCode = "EBetApp".toLowerCase();
		final String transferConstsCode = "转账EBETAPP";
		log.info("开始转入" + platform + "准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts(transferConstsCode);
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info(platform + "转账正在维护" + loginname);
			return platform + "转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();


		//看玩家是否有正在使用的优惠，是否达到流水EBetApp
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", platformCode));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);

		if (null != selfs && selfs.size() > 0) {
			SelfRecord record = selfs.get(0);
			if (null != record) {
				Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
				if (null != proposal) {
					if (((new Date()).getTime() - proposal.getExecuteTime().getTime()) < 5 * 60 * 1000) {
						return "自助优惠5分钟内不允许户内转账！";
					}
					// 要达到的总投注额
					Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
					PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
					Double validBetAmount = null;
					if (preferential.getType().equals(1)) {
						validBetAmount = -1.0;
					} else {
						//从使用存送优惠截止到现在的投注额
						String totalBetSql = "select SUM(betCredit) from ptprofit where loginname=:username and starttime>=:startTime";
						Map<String, Object> prams = new HashMap<String, Object>();
						prams.put("username", loginname);
						prams.put("startTime", new DateTime(record.getCreatetime()).toDateMidnight().toDate());
						validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
					}

					//获取EBETAPP远程金额
					Double remoteCredit = 0.00;
					try {
						remoteCredit = EBetAppUtil.getBalance(loginname);
					} catch (Exception e) {
						return "转账至" + platform + "时获取余额错误 , error = " + e.getMessage();
					}
					validBetAmount = validBetAmount == null ? 0.0 : validBetAmount;

					//判断是否达到条件
					log.info(loginname + "->自助优惠解除限制参数validBetAmount：" + validBetAmount + "amountAll:" + amountAll + "remoteCredit:" + remoteCredit);
					if (validBetAmount != -1.0) {
						if ((validBetAmount >= amountAll || remoteCredit < 5)) {
							record.setType(1);
							record.setUpdatetime(new Date());
							update(record);
							log.info("解除成功");
						} else {
							PreferTransferRecord transferRecord = new PreferTransferRecord(loginname, platformCode, validBetAmount, (amountAll - validBetAmount), new Date(), youhuiType, "IN");
							transferDao.save(transferRecord);
							log.info("解除失败");
							return youhuiType + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者" + platform + "游戏金额低于5元,方能进行户内转账！(还差" + (amountAll - validBetAmount) + ",或稍后5分钟 系统更新记录再来查询)";
						}
					} else {
						record.setType(1);
						record.setRemark(record.getRemark() + ";后台放行");
						record.setUpdatetime(new Date());
						update(record);
						log.info(loginname + "通过后台控制进行转账");
					}
				} else {
					log.info(platform + "自助优惠信息出错。Q" + loginname);
					return platform + "自助优惠信息出错。Q";
				}
			} else {
				log.info(platform + "自助优惠信息出错。M" + loginname);
				return platform + "自助优惠信息出错。M";
			}
		}
		//看玩家是否有正在使用的优惠，是否达到流水END


		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit && !"红包优惠".equals(youhuiType)) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			}  else {
				tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_EBETAPPIN.getCode(), transID, remark);
			}
			return null;
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}


	/**
	 * 从EBetApp转出至QY
	 *
	 * @param transferID
	 * @param id
	 * @param loginname
	 * @param credit
	 * @return
	 */
	@Override
	public String transferFromEBetApp(String transferID, String loginname, Double credit) {
		final String platform = "EBetApp";
		final String platformCode = "EBetApp".toLowerCase();
		String result = null;
		log.info("begin to transfer" + platform + ":" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer.getFlag() == 1) {
				return "该账号已经禁用";
			}
			if (isLockedUser(loginname)) {
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			Double remoteCredit;
			try {
				remoteCredit = EBetAppUtil.getBalance(loginname);
			} catch (Exception e) {
				return "从" + platform + "转出时获取余额错误 , 获取异常失败 , Message  =" + e.getMessage();
			}
			int maxTryMoney = 100;
			if (isUsingTryMoneyCheck(loginname, platformCode, remoteCredit, maxTryMoney)) {
				return "您正在使用体验金，" + platform + "余额大于等于" + maxTryMoney + "或者小于1的时候才能进行户内转账";
			}

			//是否存在已领取的周周回馈，金额小于等于5元或达到流水可进行户内转账
			try {
				weeklyBonusCheck(platformCode,loginname,remoteCredit,5);
				minusBonusCheck(platformCode,loginname);
			}catch (IllegalAccessException iae){
				return iae.getMessage();
			}

			//看玩家是否有正在使用的优惠，是否达到流水BEGIN
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", platformCode));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);

			if (null != selfs && selfs.size() > 0) {
				SelfRecord record = selfs.get(0);
				if (null != record) {
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					if (null != proposal) {
						if (((new Date()).getTime() - proposal.getExecuteTime().getTime()) < 5 * 60 * 1000) {
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
						Double validBetAmount = null;
						if (preferential.getType().equals(1)) {
							validBetAmount = -1.0;
						} else {
							//从使用存送优惠截止到现在的投注额
							String totalBetSql = "select SUM(betCredit) from ptprofit where loginname=:username and starttime>=:startTime";
							Map<String, Object> prams = new HashMap<String, Object>();
							prams.put("username", loginname);
							prams.put("startTime", new DateTime(record.getCreatetime()).toDateMidnight().toDate());
							validBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
						}
						//Double validBetAmount = getSelfYouHuiBet("NT", loginname, proposal.getPno()) ;
						//后期远程金额
						validBetAmount = validBetAmount == null ? 0.0 : validBetAmount;
						//判断是否达到条件
						log.info(loginname + "->自助优惠解除限制参数validBetAmount：" + validBetAmount + "amountAll:" + amountAll + "remoteCredit:" + remoteCredit);
						if (validBetAmount != -1.0) {
							if ((validBetAmount >= amountAll || remoteCredit < 1)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							} else {
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname, platformCode, validBetAmount, (amountAll - validBetAmount), new Date(), null, "IN");
								transferDao.save(transferRecord);
								log.info("解除失败");
								return platform+" ：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者"+platform+"游戏金额低于1元,方能进行户内转账！(还差" + (amountAll - validBetAmount) + ",或稍后5分钟 系统更新记录再来查询)";
							}
						} else {
							record.setType(1);
							record.setRemark(record.getRemark() + ";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname + "通过后台控制进行转账");
						}
					} else {
						log.info(platform+"自助优惠信息出错。Q" + loginname);
						return platform+"自助优惠信息出错。Q";
					}
				} else {
					log.info(platform+"自助优惠信息出错。M" + loginname);
					return platform+"自助优惠信息出错。M";
				}
			}
			//看玩家是否有正在使用的优惠，是否达到流水END
			if (remoteCredit < credit) {
				result = "转出失败，额度不足";
				log.info("remoteCredit :" + remoteCredit);
				log.info(result);
				return result;
			}

			boolean isSuccess = EBetAppUtil.transfer(loginname, transferID, credit, platformCode, "OUT");
			if (isSuccess) {// 转出成功
				this.addTransferforEBetApp(Long.parseLong(transferID), loginname, localCredit, credit, Constants.OUT, Constants.FAIL, "", "转出成功");
				tradeDao.changeCredit(loginname, credit, CreditChangeType.TRANSFER_EBETAPPOUT.getCode(), transferID, null);
			} else {
				result = platform+"转账异常";
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("转账异常 " + e.getMessage());
			throw new GenericDfhRuntimeException("转账发生异常，数据回滚");
		}
		return result;
	}

	public boolean isLockedUser(String loginname) {
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		return customer.getFlag() == 1;
	}


	private boolean isUsingTryMoneyCheck(String loginname, String platform, Double remoteCredit, int maxTryMoney) {
		if (remoteCredit >= 1 && remoteCredit < maxTryMoney) {
			DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
			dc8.add(Restrictions.eq("loginname", loginname));
			dc8.add(Restrictions.eq("target", platform));
			dc8.addOrder(Order.desc("createtime"));
			List<Transfer> transfers = transferDao.findByCriteria(dc8, 0, 10);
			if (null != transfers && transfers.size() > 0) {
				Transfer transfer = transfers.get(0);
				if (null != transfer) {
					if (transfer.getRemark().contains("自助优惠")) {  //最后一笔是体验金的转账记录
						return true;
					}
				}
			}
		}
		return false;
	}

	//负盈利反赠处理
	private void minusBonusCheck(String platformCode, String loginname) throws IllegalAccessException {
		//是否存在已领取的负盈利反赠，如果存在判断流水
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", platformCode));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			Double requiredBet = Arith.mul(losePromo.getPromo(), losePromo.getTimes());
			//计算流水
			Double realBet  = countBetBalance(loginname,losePromo.getGetTime(),losePromo.getBetting());
			if (requiredBet > realBet) {
				log.info("玩家：" + loginname + " 已领取负盈利反赠，未达到提款流水，不允许转出");
				throw new IllegalAccessException("您已领取救援金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：" + realBet + " 元");
			}
		}
		//将已领取负盈利反赠，更新为已处理
		for (LosePromo losePromo2 : losePromoList) {
			losePromo2.setStatus("2");
			update(losePromo2);
		}
	}

	/**
	 * 周周回馈确认
	 * @param platformCode
	 * @param loginname
	 * @param remoteCredit
	 * @param minLimit
	 * @throws IllegalAccessException
	 */
	private void weeklyBonusCheck(String platformCode, String loginname, double remoteCredit, double minLimit) throws IllegalAccessException {
		DetachedCriteria wsc = DetachedCriteria.forClass(WeekSent.class);
		wsc.add(Restrictions.eq("username", loginname));
		wsc.add(Restrictions.eq("platform", platformCode));
		wsc.add(Restrictions.eq("status", "1"));
		List<WeekSent> wsList = findByCriteria(wsc);
		for (WeekSent weekSent : wsList) {
			if (remoteCredit > minLimit) {
				Double requiredBet = Arith.mul(weekSent.getPromo(), weekSent.getTimes());
				//计算流水
				Double realBet  = countBetBalance(loginname,weekSent.getGetTime(),weekSent.getBetting());
				if (requiredBet > realBet) {
					log.info("玩家：" + loginname + " 已领取周周回馈，"+platformCode+"余额大于5元且未达到提款流水，不允许转出");
					throw new IllegalAccessException("您已领取周周回馈，流水须达到 " + requiredBet + " 元或者游戏余额不多于5元才能转出，目前流水额为：" + realBet + " 元");
				}
			} else {
				log.info("玩家：" + loginname + " 已领取周周回馈，"+platformCode+"金额" + remoteCredit + "，准备转出");
			}
		}
		//将已领取周周回馈，更新为已处理
		for (WeekSent ws : wsList) {
			ws.setStatus("2");
			update(ws);
		}
	}

	/**
	 * 计算流水
	 *
	 * @param loginname
	 * @param countingDate
	 * @param betting
	 * @return
	 */
	public double countBetBalance(String loginname,Date countingDate,double betting){
		String totalBetSql = "select SUM(betCredit) from ptprofit where loginname=:username and starttime>=:startTime";
		Map<String, Object> prams = new HashMap<String, Object>();
		prams.put("username", loginname);
		prams.put("startTime", new DateTime(countingDate).toDateMidnight().toDate());
		return Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), betting);
	}
	
	public Transfer addTransfer(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransfer(transID, loginname, credit, remit, in, flag, paymentid, remark);
	}

	public void updateUserShippingcodeSql(Users users) {
		// TODO Auto-generated method stub
		transferDao.updateUserShippingcodeSql(users);
	}
	
	public void updateUserShippingcodePtSql(Users users) {
		// TODO Auto-generated method stub
		transferDao.updateUserShippingcodePtSql(users);
	}

	@Override
	public Transfer addTransferforQt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforQt( transID,  loginname,  localCredit,  remit,  in,  flag,  paymentid,  remark);
	}
	
	@Override
	public Transfer addTransferforDt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforDt( transID,  loginname,  localCredit,  remit,  in,  flag,  paymentid,  remark);
	}
	@Override
	public Transfer addTransferforSlot(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforSlot( transID,  loginname,  localCredit,  remit,  in,  flag,  paymentid,  remark);
	}
	
	@Override
	public Transfer addTransferforTt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforTt( transID,  loginname,  localCredit,  remit,  in,  flag,  paymentid,  remark);
	}
	
	@Override
	public Transfer addTransferforNT(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforNT(transID, loginname, localCredit, remit, in,paymentid, remark);
	}
	
	@Override
	public Transfer addTransferforDT(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforDT(transID, loginname, localCredit, remit, in, remark);
	}
	
	public void updateUserShippingcodeTtSql(Users users) {
		// TODO Auto-generated method stub
		transferDao.updateUserShippingcodeTtSql(users);
	}

	
	@Override
	public void updateUserCreditSql(Users user, Double remit) {
		// TODO Auto-generated method stub
		transferDao.updateUserCreditSql(user, remit);
	}

	@Override
	public Transfer addTransferforDsp(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforDsp( transID,  loginname,  credit,  remit,  in,  flag,  paymentid,  remark);
	}

	@Override
	public Transfer addTransferforAginDsp(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforAginDsp( transID,  loginname,  credit,  remit,  in,  flag,  paymentid,  remark);
	}

	@Override
	public Transfer addTransferforKneo2(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforKneo2( transID,  loginname,  credit,  remit,  in,  flag,  paymentid,  remark);
	}

	@Override
	public Transfer addTransferforNewPt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforNewPt( transID,  loginname,  localCredit,  remit,  in,  flag,  paymentid,  remark);
	}

	@Override
	public Transfer addTransferforSixLottery(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforSixLottery( transID,  loginname,  credit,  remit,  in,  flag,  paymentid,  remark);
	}

	@Override
	public Transfer addTransferforSB(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforSB( transID,  loginname,  credit,  remit,  in,  flag,  paymentid,  remark) ;
	}
	
	@Override
	public Boolean updateSbcoupon(String loginname) {
		// TODO Auto-generated method stub
		return userDao.updateSbcoupon(loginname);
	}
	
	public Transfer addTransferforBbin(Long transID, String loginname,
			Double credit, Double remit, Boolean in, Boolean flag,
			String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforBbin( transID,  loginname,
				 credit,  remit,  in,  flag,
				 paymentid,  remark);
	}

	@Override
	public Transfer addTransferforKneo(Long transID, String loginname,
			Double credit, Double remit, Boolean in, Boolean flag,
			String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferforKneo( transID,  loginname,
				 credit,  remit,  in,  flag,
				 paymentid,  remark);
	}
	@Override
	public String transferToMain(TransferService transferService, Users users, Integer amout) {

		String pno = seqService.generateProposalPno(ProposalType.ACTIVITY442);
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.ACTIVITY442.getCode(),
				users.getLevel(), users.getLoginname(), amout.doubleValue(), users.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, "VIP每月免费筹码", null);
		proposal.setBetMultiples("0");
		proposal.setExecuteTime(new Date());
		transferDao.save(proposal);
		tradeDao.changeCreditForOnline(users.getLoginname(),amout.doubleValue(),ProposalType.ACTIVITY442.getText(),pno,"VIP每月免费筹码->主账户");
		return "恭喜您！成功领取"+amout+"元免费筹码,请至主帐户查询！";

	}
	@Override
	public String transferInGPI(String seqId, String loginname, Double remit) {
		//转账前先判断额度是否可以获取到
		Double balance;
		try {
			balance = GPIUtil.getBalance(loginname);
			if (balance == null){
				log.info(loginname + "GPI转入：获取额度超时!系统繁忙!");
				return "系统繁忙!请稍后再试";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(loginname + "获取额度超时!系统繁忙!" + e.toString());
			return "系统繁忙!请稍后再试";
		}
		//获取用户
		Users user = getUsers(loginname);
		if (user == null) {
			log.info("用户不存在!*****" + loginname);
			return "用户不存在!";
		}
		
		//体验金
		if(balance >=1 && balance<100){
			DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
			dc8.add(Restrictions.eq("loginname", loginname)) ;
			dc8.add(Restrictions.eq("target", "gpi")) ;
			dc8.addOrder(Order.desc("createtime"));
			List<Transfer> transfers = this.findByCriteria(dc8, 0, 10) ;
			if(null != transfers && transfers.size()>0){
				Transfer transfer = transfers.get(0);
				if(null != transfer){
					if(null != transfer.getRemark() && transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
						return "您正在使用体验金，GPI余额大于等于100或者小于1的时候才能进行户内转账";
					}
				}
			}
			
		}
		//负盈利
		DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "gpi"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = this.findByCriteria(c);
		if(balance >= 5.00 && losePromoList.size() > 0){
			//如果存在已领取的负盈利反赠，不允许转入
			log.info("玩家已领取GPI负盈利反赠，且余额大于5元，不允许转入" +loginname);
			return "您已领取救援金且GPI余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的负盈利反赠更新为已处理
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		
		//周周回馈
		DetachedCriteria dc=DetachedCriteria.forClass(WeekSent.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("platform", "gpi"));
		dc.add(Restrictions.eq("status", "1"));
		List<WeekSent> weekSentList = this.findByCriteria(dc);
		if(balance > 5.00 && weekSentList.size() > 0){
			//如果存在已领取的周周回馈，不允许转入
			log.info("玩家已领取GPI周周回馈，且余额大于5元，不允许转入" +loginname);
			return "您已领取周周回馈且GPI余额大于5元，暂不允许转入。请先将余额转出或继续游戏";
		}
		//将已领取的周周回馈更新为已处理
		for (WeekSent ws : weekSentList) {
			ws.setStatus("2");
			update(ws);
		}
		
		//判断是否有使用优惠，且未达到流水
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "gpi"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc, 0, 1);
		if(selfs != null && selfs.size() > 0){
			SelfRecord selfRecord = selfs.get(0);
			if(balance >= 1.0){
				Proposal proposal = (Proposal) get(Proposal.class, selfRecord.getPno());
				double requiredBetAmount = Arith.add(proposal.getAmount(), proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
				PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, selfRecord.getPno());
				//判断是否人工解除了限制
				if(!preferential.getType().equals(1)){
					//从使用存送优惠截止到现在的投注额
					String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform in(:gpi, :rslot, :png, :bs, :ctxm) and starttime>=:startTime";
					Map<String, Object> prams = new HashMap<String, Object>();
					prams.put("username", loginname);
					//prams.put("platform", "gpi");
					prams.put("gpi", "gpi");
					prams.put("rslot", "rslot");
					prams.put("png", "png");
					prams.put("bs", "bs");
					prams.put("ctxm", "ctxm");
					
					Calendar cd = Calendar.getInstance();
					cd.setTime(selfRecord.getCreatetime());
					cd.set(Calendar.HOUR_OF_DAY, 0);
					cd.set(Calendar.MINUTE, 0);
					cd.set(Calendar.SECOND, 0);
					cd.set(Calendar.MILLISECOND, 0);
					prams.put("startTime", cd.getTime());
					Double realBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
					if(requiredBetAmount > realBetAmount){
						log.info("未达到存送优惠流水要求" + loginname);
						return "您有使用的存送优惠未达到流水要求。当前投注额：" + realBetAmount + "，需要投注额：" + requiredBetAmount;
					}else{
						selfRecord.setType(1);
						selfRecord.setUpdatetime(new Date());
						selfRecord.setRemark("已达到所需流水：" + realBetAmount);
						update(selfRecord);
					}
				}
			}else{
				selfRecord.setType(1);
				selfRecord.setUpdatetime(new Date());
				update(selfRecord);
			}
		}
		
		double oldCredit = Math.abs(user.getCredit());
		String msg = transfer4GPIIn(seqId, loginname, remit);
		if (msg == null) {
			remit = Math.abs(remit);
			user = getUsers(loginname);
			double newCredit = Math.abs(user.getCredit());
			if (oldCredit != newCredit) {
				try {
					log.info(loginname + " GPI转账额度不同：******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit + "******改变额度:" + remit);
					String transferResult = GPIUtil.credit(loginname, remit, seqId); 
					if(transferResult == null){
						//出现异常，到平台查询转账结果
						String checkResult = GPIUtil.checkTransaction(seqId);
						if(checkResult != null && checkResult.equals(GPIUtil.GPI_SUCCESS_CODE)){
							transferDao.addTransferforGPI(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", null);
							log.info(loginname + " GPI转账成功！");
							return null;
						}else{
							log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
							return "出现未知情况!扣除款项！请联系在线客服!";
						}
					}else if (transferResult.equals(GPIUtil.GPI_SUCCESS_CODE)) {
						//成功
						transferDao.addTransferforGPI(Long.parseLong(seqId), loginname, oldCredit, remit, Constants.IN, Constants.FAIL, "", null);
						log.info(loginname + " GPI转账成功！");
						return null;
					} else {
						// 转账失败 退还额度
						Creditlogs creditLog = new Creditlogs();
						creditLog.setLoginname(loginname);
						creditLog.setType(CreditChangeType.TRANSFER_GPIIN.getCode());
						creditLog.setCredit(newCredit);
						creditLog.setRemit(remit);
						creditLog.setNewCredit(newCredit + remit);
						creditLog.setRemark("GPI转账错误!自动退还:" + seqId + "订单");
						creditLog.setCreatetime(DateUtil.getCurrentTimestamp());
						save(creditLog);
						// 打印日志
						Actionlogs actionlog = new Actionlogs();
						actionlog.setLoginname(loginname);
						actionlog.setRemark(null);
						actionlog.setCreatetime(DateUtil.now());
						actionlog.setAction(ActionLogType.CREDIT_RECORD.getCode());
						String info="GPI转账错误!改变前订单:" + seqId +" 改变前:" + oldCredit + "改变后:" + (oldCredit - remit) + " 改变额度:" + remit+" 自动退还订单:" + seqId + " 改变前:" + newCredit + "改变后:" + (newCredit + remit) + " 改变额度:" + remit+" 系统已经处理 请检查是有否错误";
						actionlog.setRemark(info);
						save(actionlog);
						log.info(info);
						// 退还款项
						updateUserCreditSql(user, remit);
						log.info("GPI转入错误:" + GPIUtil.respCodeMap.get(transferResult) + "***" + loginname);
						// 打印日志
						return "GPI转入错误:" + GPIUtil.respCodeMap.get(transferResult);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log.info("出现未知情况!扣除款项！单号:" + seqId + "***账户:" + loginname);
					return "出现未知情况!扣除款项！请联系在线客服!";
				}
			} else {
				log.info("GPI转账额度相同 转账额度出现问题:******" + loginname + "******旧额度:" + oldCredit + "******新额度:" + newCredit);
				log.info("转账额度出现问题！请联系在线客服：" + loginname);
				return "转账额度出现问题！请联系在线客服";
			}
		}
		return msg;
	}

	private String transfer4GPIIn(String seqId, String loginname, Double remit) {
		log.info("开始转入GPI:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账GPI");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("GPI转账正在维护" + loginname);
			return "GPI转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCredit(loginname, -Double.valueOf(remit.toString()), CreditChangeType.TRANSFER_GPIIN.getCode(), seqId, null);
			    return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}

	@Override
	public String transferOutGPI(String seqId, String loginname, Double remit) {
		String result = null;
		log.info("begin to transfer for GPI:" + loginname);
		remit = Math.abs(remit);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if (customer == null) {
			return "玩家账号不存在！";
		}
		if (customer.getFlag() == 1) {
			return "该账号已经禁用";
		}
		Double balance;
		try {
			balance = GPIUtil.getBalance(loginname);
			if (balance == null){
				log.info(loginname + "GPI转入：获取额度超时!系统繁忙!");
				return "系统繁忙!请稍后再试";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(loginname + "获取额度超时!系统繁忙!" + e.toString());
			return "系统繁忙!请稍后再试";
		}
		//体验金
		if(balance >=1 && balance<100){
			DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
			dc8.add(Restrictions.eq("loginname", loginname)) ;
			dc8.add(Restrictions.eq("target", "gpi")) ;
			dc8.addOrder(Order.desc("createtime"));
			List<Transfer> transfers = this.findByCriteria(dc8, 0, 10) ;
			if(null != transfers && transfers.size()>0){
				Transfer transfer = transfers.get(0);
				if(null != transfer){
					if(null != transfer.getRemark() && transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
						return "您正在使用体验金，GPI余额大于等于100或者小于1的时候才能进行户内转账";
					}
				}
			}
			
		}
		//是否存在已领取的周周回馈，金额小于等于5元或达到流水可进行户内转账
		DetachedCriteria wsc=DetachedCriteria.forClass(WeekSent.class);
		wsc.add(Restrictions.eq("username", loginname));
		wsc.add(Restrictions.eq("platform", "gpi"));
		wsc.add(Restrictions.eq("status", "1"));
		List<WeekSent> wsList = findByCriteria(wsc);
		for (WeekSent weekSent : wsList) {
			if(balance > 5){
				Double requiredBet = Arith.mul(weekSent.getPromo(), weekSent.getTimes());
				//计算流水
				String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and starttime>=:startTime and platform in(:gpi, :rslot, :png, :bs, :ctxm)";
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("username", loginname);
				prams.put("gpi", "gpi");
				prams.put("rslot", "rslot");
				prams.put("png", "png");
				prams.put("bs", "bs");
				prams.put("ctxm", "ctxm");
				Calendar cd = Calendar.getInstance();
				cd.setTime(weekSent.getGetTime());
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				prams.put("startTime", cd.getTime());
				Double realBet = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), weekSent.getBetting());
				if(requiredBet > realBet){
					log.info("玩家：" + loginname + " 已领取周周回馈，gpi余额大于5元且未达到提款流水，不允许转出");
					return "您已领取周周回馈，流水须达到 " + requiredBet + " 元或者游戏余额不多于5元才能转出，目前流水额为：" + realBet + " 元";
				}
			}else{
				log.info("玩家：" + loginname + " 已领取周周回馈，gpi金额"+balance+"，准备转出");
			}
		}
		//将已领取周周回馈，更新为已处理
		for (WeekSent ws : wsList) {
			ws.setStatus("2");
			update(ws);
		}
		
		//是否存在已领取的负盈利反赠，如果存在判断流水
		DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "gpi"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			Double requiredBet = Arith.mul(losePromo.getPromo(), losePromo.getTimes());
			//计算流水
			String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and starttime>=:startTime and platform in(:gpi, :rslot, :png, :bs, :ctxm)";
			Map<String, Object> prams = new HashMap<String, Object>();
			prams.put("username", loginname);
			prams.put("gpi", "gpi");
			prams.put("rslot", "rslot");
			prams.put("png", "png");
			prams.put("bs", "bs");
			prams.put("ctxm", "ctxm");
			Calendar cd = Calendar.getInstance();
			cd.setTime(losePromo.getGetTime());
			cd.set(Calendar.HOUR_OF_DAY, 0);
			cd.set(Calendar.MINUTE, 0);
			cd.set(Calendar.SECOND, 0);
			cd.set(Calendar.MILLISECOND, 0);
			prams.put("startTime", cd.getTime());
			Double realBet = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), losePromo.getBetting());
			if(requiredBet > realBet){
				log.info("玩家：" + loginname + " 已领取负盈利反赠，未达到提款流水，不允许转出");
				return "您已领取救援金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：" + realBet + " 元";
			}
		}
		//将已领取负盈利反赠，更新为已处理
		for (LosePromo losePromo2 : losePromoList) {
			losePromo2.setStatus("2");
			update(losePromo2);
		}
		
		
		//判断是否有使用优惠，且未达到流水
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "gpi"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		if(selfs != null && selfs.size() > 0){
			SelfRecord selfRecord = selfs.get(0);
			Proposal proposal = (Proposal) get(Proposal.class, selfRecord.getPno());
			double requiredBetAmount = Arith.add(proposal.getAmount(), proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
			PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, selfRecord.getPno());
			//判断是否人工解除了限制
			if(!preferential.getType().equals(1)){
				//从使用存送优惠截止到现在的投注额
				String totalBetSql = "select SUM(bet) from platform_data where loginname=:username and platform in(:gpi, :rslot, :png, :bs, :ctxm) and starttime>=:startTime";
				Map<String, Object> prams = new HashMap<String, Object>();
				prams.put("username", loginname);
				
				prams.put("gpi", "gpi");
				prams.put("rslot", "rslot");
				prams.put("png", "png");
				prams.put("bs", "bs");
				prams.put("ctxm", "ctxm");
				
				Calendar cd = Calendar.getInstance();
				cd.setTime(selfRecord.getCreatetime());
				cd.set(Calendar.HOUR_OF_DAY, 0);
				cd.set(Calendar.MINUTE, 0);
				cd.set(Calendar.SECOND, 0);
				cd.set(Calendar.MILLISECOND, 0);
				prams.put("startTime", cd.getTime());
				Double realBetAmount = Arith.sub(transferDao.getDoubleValueBySql(totalBetSql, prams), preferential.getValidBet());
				if(requiredBetAmount > realBetAmount){
					log.info("未达到存送优惠流水要求" + loginname);
					return "您有使用的存送优惠未达到流水要求。当前投注额：" + realBetAmount + "，需要投注额：" + requiredBetAmount;
				}else{
					selfRecord.setType(1);
					selfRecord.setUpdatetime(new Date());
					selfRecord.setRemark("已达到流水：" + realBetAmount);
					update(selfRecord);
				}
			}
		}
		
		Double localCredit = customer.getCredit();
		// 检查额度
		log.info("current credit:" + localCredit);
		String transgerResult = GPIUtil.debit(loginname, remit, seqId);
		// 如果操作成功
		if (transgerResult != null) {
			if (transgerResult.equals(GPIUtil.GPI_SUCCESS_CODE)) {			
				transferDao.addTransferforGPI(Long.parseLong(seqId), loginname, localCredit, Double.valueOf(remit.toString()), Constants.OUT, Constants.SUCESS, "", "转出成功");
				tradeDao.changeCredit(loginname, Double.valueOf(remit.toString()), CreditChangeType.TRANSFER_GPIOUT.getCode(), seqId, null);
			} else {
				log.info("转出失败");
				result = GPIUtil.respCodeMap.get(transgerResult);
			}
		} else {
			//检查该笔转账是否成功
			String checkResult = GPIUtil.checkTransaction(seqId);
			if(checkResult != null && checkResult.equals(GPIUtil.GPI_SUCCESS_CODE)){
				transferDao.addTransferforGPI(Long.parseLong(seqId), loginname, localCredit, Double.valueOf(remit.toString()), Constants.OUT, Constants.SUCESS, "", "转出成功");
				tradeDao.changeCredit(loginname, Double.valueOf(remit.toString()), CreditChangeType.TRANSFER_GPIOUT.getCode(), seqId, null);
			}else{
				result = "转账失败";
			}
		}
		return result;
	}
	
	/**
	 * ttg存送优惠转账
	 */
	@Override
	public String selfConpon4TTG(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入TTG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账TTG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("TTG转账正在维护" + loginname);
			return "TTG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的TTG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "ttg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "ttg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}

		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, remit * -1,
						CreditChangeType.TRANSFER_TTGIN.getCode(), transID,
						remark);
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * NT存送转账
	 */
	@Override
	public String selfCouponNT(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入NT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账NT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("NT转账正在维护" + loginname);
			return "NT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的NT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "nt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "nt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}

		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, remit * -1,
						CreditChangeType.TRANSFER_NTIN.getCode(), transID,
						remark);
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * QT存送转账
	 */
	@Override
	public String selfCouponQT(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入QT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账QT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("QT转账正在维护" + loginname);
			return "QT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的QT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "qt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "qt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}

		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, remit * -1,
						CreditChangeType.TRANSFER_QTIN.getCode(), transID,
						remark);
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String selfConpon4GPI(String transID, Users customer, Double remit, String remark) {
		String loginname = customer.getLoginname();
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账GPI");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("GPI转账正在维护" + loginname);
			return "GPI转账正在维护";
		}

		remit = Math.abs(remit);		
		//将之前使用的GPI存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "gpi"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		
		for (SelfRecord selfRecord : selfs) {
			//更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}
		
		//将已领取的负盈利反赠更新为已处理
		DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "gpi"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		
		//从主账户中扣除
		try {
			transferDao.addTransferforGPI(Long.parseLong(transID), loginname, customer.getCredit(), remit, Constants.IN, Constants.FAIL, "", null);
			tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_GPIIN.getCode(), transID, remark);
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return null;
	}
	/**
	 * 根据级别算签到金额
	 * @param level
	 * @return
	 */
	public Double levelAmount(String level){
		Double jiFeng=0.0;
		if("0".equals(level)){
			return 3.0;
		}else if("1".equals(level)){
			return 6.0;
		}else if("2".equals(level)){
			return 8.0;
		}else if("3".equals(level)){
			return 10.0;
		}else if("4".equals(level)){
			return 15.0;
		}else if("5".equals(level)){
			return 30.0;
		}else if("6".equals(level)){
			return 50.0;
		}
		return jiFeng;
	}

	public Boolean checkRecord(String loginname,String device,String level) throws Exception{
		DetachedCriteria dc = DetachedCriteria.forClass(SignRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("isdelete", "0"));//未删除
		dc.add(Restrictions.ge("createtime", DateUtil.getToday()));
		dc.add(Restrictions.eq("type", "0"));//签到记录
		List<SignRecord> list =findByCriteria(dc);
		if(null!=list&&list.size()>0){
			return true;
		}else {
		return false;
		}
	}

	/**
	 * 签到奖金
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String doSignRecord(String loginname,String device,String level) throws Exception {
		return "签到暂时维护";
		/*Date now = new Date();
		DetachedCriteria dc = DetachedCriteria.forClass(SignRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("isdelete", "0"));//未删除
		dc.add(Restrictions.ge("createtime", DateUtil.getBeginOfDate(now)));
		dc.add(Restrictions.eq("type", "0"));//签到记录
		List<SignRecord> list =findByCriteria(dc);
		if(null!=list&&list.size()>0){
			return "今日已签到，不能重复操作";
		}else{
			
			Integer countOfMonth = this.getSignCountOfMonth(now, loginname);
			if(countOfMonth == null){
				countOfMonth = 1;
			} else {
				countOfMonth++;
			}
			System.out.println("countOfMonth" + countOfMonth);
			//先取一次奖金余额  作为是否重复添加的判断
			DetachedCriteria dc3 = DetachedCriteria.forClass(SignAmount.class);
			dc3.add(Restrictions.eq("username", loginname));
			List<SignAmount> list3 = findByCriteria(dc3);
			Double oldAmount =0.0;
			if(null!=list3&&list3.size()>0){
				oldAmount=list3.get(0).getAmountbalane();
			}
			//查询当日存款量
			*//*String result=checkDepositRecord(loginname,DateUtil.getbeforeDay(0),null);
			if(StringUtil.isEmpty(result)){
				return "系统异常，请稍后再试";
			}
			if(Double.parseDouble(result)<10){
				return "当日存款不满足要求，无法签到，还需存款至少："+(10-Double.parseDouble(result))+"元";
			}*//*
			//查询是否连续签到7天
			*//*DetachedCriteria dc1 = DetachedCriteria.forClass(SignRecord.class);
			dc1.add(Restrictions.eq("username", loginname));
			dc1.add(Restrictions.eq("isdelete", "0"));//未删除
			dc1.add(Restrictions.ge("createtime", DateUtil.fmtyyyy_MM_d(DateUtil.getbeforeDay(7))));
			dc1.add(Restrictions.eq("type", "0"));//签到记录
			List<SignRecord> list1 = findByCriteria(dc1);*//*
			*//*Double amount=0.5;
			if(list1.size()>6){
				amount=1.0;
			}*//*

			Double amount = levelAmount(level);
			Double sign_money = amount;
			SignRecord sr = new SignRecord();
			sr.setUsername(loginname);
			sr.setCreatetime(now);
			sr.setIsused("0");
			sr.setIsdelete("0");
			sr.setType("0");
			//更新奖金余额
			DetachedCriteria dc2 = DetachedCriteria.forClass(SignAmount.class);
			dc2.add(Restrictions.eq("username", loginname));
			List<SignAmount> list2 = findByCriteria(dc2);
			SignAmount sa=null;
			if(null!=list2&&list2.size()>0){
				sa = list2.get(0);
				amount=amount+sa.getAmountbalane();
			}else{
				sa = new SignAmount();
				sa.setUsername(loginname);
			}
			sr.setRemark("玩家签到-->改变前额度："+oldAmount+",改变后额度："+amount+",增加额度："+sign_money);
			sr.setAmount(sign_money);
			sr.setMonthCount(countOfMonth);
			save(sr);
			sa.setAmountbalane(amount);
			sa.setUpdatetime(now);
			if(oldAmount!=(sa.getAmountbalane()-sr.getAmount())){
				log.error("签到数据异常，用户："+loginname);
				throw new Exception("网络异常,请重新操作");
			}
			saveOrUpdate(sa);
			return "签到成功，领取签到金额："+sr.getAmount()+"元";
		}*/
	}
	
	private Integer getSignCountOfMonth(Date now, String loginname) {
		
		Date monthBigin = DateUtil.getDateStartOfMonth(now);
		Date monthEnd = DateUtil.getDateEndOfMonth(now);
		DetachedCriteria dc = DetachedCriteria.forClass(SignRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("isdelete", "0"));//未删除
		dc.add(Restrictions.ge("createtime", monthBigin));
		dc.add(Restrictions.le("createtime", monthEnd));
		dc.add(Restrictions.eq("type", "0"));//签到记录
		dc.setProjection(Projections.rowCount());
		List<Integer> list =findByCriteria(dc);
		System.out.println(list.get(0));
		return list.get(0);
	}

	private int countContinueSignNum(String loginname,int continueSignCount){
		Boolean isSignYesterday = isSignYesterday(loginname);
		System.out.println("目前签到日期="+continueSignCount);
		System.out.println("isSignYesterday==="+isSignYesterday);
		if(isSignYesterday){
			return continueSignCount+=1;
		}else{
			return 1;
		}
	}
	
	private boolean isSignYesterday(String loginname){
		DetachedCriteria dc = DetachedCriteria.forClass(SignRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("isdelete", "0"));//未删除
		dc.add(Restrictions.eq("type", "0"));//签到记录
		dc.add(Restrictions.lt("createtime", DateUtil.getToday())); //小余今天 
		dc.add(Restrictions.ge("createtime", DateUtil.getYesterday())); //大于等于昨天  
		List<SignRecord> list =findByCriteria(dc);
		if(list==null||list.size()==0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 检查该玩家某个时间段的存款量
	 * @param loginname
	 * @return
	 */
	public String checkDepositRecord(String loginname,String begindateStr,String enddateStr){
		Double sumMoney=0.0;
		DetachedCriteria dc1 = DetachedCriteria.forClass(Payorder.class);
		dc1.add(Restrictions.eq("loginname", loginname));
		dc1.add(Restrictions.eq("type", 0));
		Date begindate=null;
		Date enddate=null;
		try {
			begindate=DateUtil.fmtyyyy_MM_d(begindateStr);
			log.info("派发奖励:"+loginname+"begindate="+begindate+"enddateStr:"+enddateStr);
			if(StringUtil.isEmpty(begindateStr)||null==begindate||begindateStr.contains("1970")||begindate.getYear()==1970||begindate.toString().contains("1970")){
				log.info("时间获取异常 派发奖励:"+loginname+"begindateStr="+begindateStr+"enddateStr:"+enddateStr);	
				return null;
			}
			if(!StringUtil.isEmpty(enddateStr)){
				enddate=DateUtil.fmtyyyy_MM_d(enddateStr);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dc1.add(Restrictions.ge("createTime",begindate));
		if(null!=enddate){
			dc1.add(Restrictions.le("createTime",enddate));
		}
		dc1.setProjection(Projections.sum("money"));
		List list1 = findByCriteria(dc1);
		Double result1= 0.0;
		result1 =(Double)list1.get(0);
		if(null==result1){
			result1=0.0;
		}
		DetachedCriteria dc2 = DetachedCriteria.forClass(Proposal.class);
		dc2.add(Restrictions.eq("loginname", loginname));
		dc2.add(Restrictions.eq("type", 502));
		dc2.add(Restrictions.eq("flag", 2));
		dc2.add(Restrictions.ge("createTime",begindate));
		if(null!=enddate){
			dc2.add(Restrictions.le("createTime",enddate));
		}
		dc2.setProjection(Projections.sum("amount"));
		List list2 = findByCriteria(dc2);
		Double result2= 0.0;
		result2 =(Double)list2.get(0);
		if(null==result2){
			result2=0.0;
		}
		sumMoney=result1+result2;
		return sumMoney.toString();
	}
	
	
	@Override
	public String transfer4Pt4Sign(String transID, String loginname, Double remit,String remark) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transfer4Pt4Sign:" + loginname);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer==null){
			return "玩家账号不存在！";
		}
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(SignAmount.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		SignAmount sa=null;
		if(null!=list&&list.size()>0){
			sa=(SignAmount) list.get(0);
			localCredit= sa.getAmountbalane();
		}else{
			return "没有奖金信息，无法转入";
		}
		if (localCredit < remit) {
			result = "转账失败，奖金额度不足";
			log.info("current credit:" + localCredit);
			log.info(result);
			return result ;
		}
		try {
			remit = Math.abs(remit);
			String couponString = null ;
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc1 = DetachedCriteria.forClass(Proposal.class);
				dc1 = dc1.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc1 = dc1.add(Restrictions.eq("flag", 2));
				dc1 = dc1.add(Restrictions.in("type", new Object[] { 571, 572, 573,574 ,575 ,590 , 591 ,705,409,410,411,412,419,390}));
				List<Proposal> list1 = findByCriteria(dc1);
				if (list1 == null || list1.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list1.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<6*60*1000){
					return "自助优惠5分钟内不允许转账！";
				}
				Integer type = proposal.getType();
				couponString = ProposalType.getText(type);
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				// 获取数据总投注额
				Double validBetAmount = getSelfYouHuiBet("pttiger", loginname, proposal.getPno()) ;
				
				/*****************************************/
				//后期远程金额
				Double remoteCredit=0.00;
				try {
						 remoteCredit = PtUtil.getPlayerMoney(loginname);
				} catch (Exception e) {
						return "获取异常失败";
				}
				validBetAmount = validBetAmount==null?0.0:validBetAmount;
				//判断是否达到条件
				log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
				if(validBetAmount != -1.0){
					if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
						log.info("解除成功");
					}else{
						PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"pttiger" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
						transferDao.save(transferRecord) ;
						log.info("解除失败");
						return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
					}
				}else{
					log.info(loginname+"通过后台控制进行转账");
				}
			}
			
			//将已领取的负盈利反赠更新为已处理
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "pttiger"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				losePromo.setStatus("2");
				update(losePromo);
			}
			// 检查额度
				transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				sa.setAmountbalane(localCredit-remit);
				update(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_QD_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				// 标记额度已被扣除
				creditReduce = true;
				if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

				} else {
					customer.setShippingcodePt(null);
					update(customer);
				}
		} catch (Exception e) {
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				result = "系统繁忙，请重新尝试";
			}
		}
		return result;
	}

	/**
	 * 签到金额转入游戏mg
	 */
	@Override
	public String selfConpon4MG4Sign(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入MG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账MG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("MG转账正在维护" + loginname);
			return "MG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的MG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "mg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "mg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(SignAmount.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		SignAmount sa=null;
		if(null!=list&&list.size()>0){
			sa=(SignAmount) list.get(0);
			localCredit= sa.getAmountbalane();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_QD_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=localCredit-remit;
				sa.setAmountbalane(localCredit);
				sa.setUpdatetime(new Date());
				sa.setRemark(remark);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_QD_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 签到金额转入游戏dt
	 */
	@Override
	public String selfConpon4DT4Sign(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入DT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账DT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("DT转账正在维护" + loginname);
			return "DT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的DT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "dt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "dt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(SignAmount.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		SignAmount sa=null;
		if(null!=list&&list.size()>0){
			sa=(SignAmount) list.get(0);
			localCredit= sa.getAmountbalane();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_QD_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=localCredit-remit;
				sa.setAmountbalane(localCredit);
				sa.setUpdatetime(new Date());
				sa.setRemark(remark);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_QD_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	/**
	 * 签到金额转入游戏slot
	 */
	@Override
	public String selfConpon4SLOT4Sign(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入SLOT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账SLOT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("SLOT转账正在维护" + loginname);
			return "SLOT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		
		// 将之前使用的SLOT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "slot"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}
		
		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "slot"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(SignAmount.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		SignAmount sa=null;
		if(null!=list&&list.size()>0){
			sa=(SignAmount) list.get(0);
			localCredit= sa.getAmountbalane();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_QD_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=localCredit-remit;
				sa.setAmountbalane(localCredit);
				sa.setUpdatetime(new Date());
				sa.setRemark(remark);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_QD_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 签到金额转入游戏ttg
	 */
	@Override
	public String selfConpon4TTG4Sign(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入TTG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账TTG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("TTG转账正在维护" + loginname);
			return "TTG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的TTG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "ttg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "ttg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(SignAmount.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		SignAmount sa=null;
		if(null!=list&&list.size()>0){
			sa=(SignAmount) list.get(0);
			localCredit= sa.getAmountbalane();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_QD_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=localCredit-remit;
				sa.setAmountbalane(localCredit);
				sa.setUpdatetime(new Date());
				sa.setRemark(remark);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_QD_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 签到金额转入游戏nt
	 */
	@Override
	public String selfConponNT4Sign(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入NT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账NT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("NT转账正在维护" + loginname);
			return "NT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的nt存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "nt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "nt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(SignAmount.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		SignAmount sa=null;
		if(null!=list&&list.size()>0){
			sa=(SignAmount) list.get(0);
			localCredit= sa.getAmountbalane();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_QD_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=localCredit-remit;
				sa.setAmountbalane(localCredit);
				sa.setUpdatetime(new Date());
				sa.setRemark(remark);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_QD_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 签到金额转入游戏qt
	 */
	@Override
	public String selfConpon4QT4Sign(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入QT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账QT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("QT转账正在维护" + loginname);
			return "QT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的QT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "qt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "qt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(SignAmount.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		SignAmount sa=null;
		if(null!=list&&list.size()>0){
			sa=(SignAmount) list.get(0);
			localCredit= sa.getAmountbalane();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_QD_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=localCredit-remit;
				sa.setAmountbalane(localCredit);
				sa.setUpdatetime(new Date());
				sa.setRemark(remark);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_QD_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	
	/**
	 * 好友推荐奖金转入PT
	 */
	@Override
	public String transfer4Pt4Friend(String transID, String loginname, Double remit,String remark) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transfer4Pt4Friend:" + loginname);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer==null){
			return "玩家账号不存在！";
		}
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(Friendbonus.class);
		dc.add(Restrictions.eq("toplineuser", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		Friendbonus fb=null;
		if(null!=list&&list.size()>0){
			fb=(Friendbonus) list.get(0);
			localCredit= fb.getMoney();
			oldCredit=fb.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		if (localCredit < remit) {
			result = "转账失败，奖金额度不足";
			log.info("current credit:" + localCredit);
			log.info(result);
			return result ;
		}
		try {
			remit = Math.abs(remit);
			String couponString = null ;
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc1 = DetachedCriteria.forClass(Proposal.class);
				dc1 = dc1.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc1 = dc1.add(Restrictions.eq("flag", 2));
				dc1 = dc1.add(Restrictions.in("type", new Object[] { 571, 572, 573,574 ,575 ,590 , 591 ,705,409,410,411,412,419,390, 415, 391}));
				List<Proposal> list1 = findByCriteria(dc1);        
				if (list1 == null || list1.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list1.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<6*60*1000){
					return "自助优惠5分钟内不允许转账！";
				}
				Integer type = proposal.getType();
				couponString = ProposalType.getText(type);
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				// 获取数据总投注额
				Double validBetAmount = getSelfYouHuiBet("pttiger", loginname, proposal.getPno()) ;
				
				/*****************************************/
				//后期远程金额
				Double remoteCredit=0.00;
				try {
						 remoteCredit = PtUtil.getPlayerMoney(loginname);
				} catch (Exception e) {
						return "获取异常失败";
				}
				validBetAmount = validBetAmount==null?0.0:validBetAmount;
				//判断是否达到条件
				log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
				if(validBetAmount != -1.0){
					if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
						log.info("解除成功");
					}else{
						PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"pttiger" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
						transferDao.save(transferRecord) ;
						log.info("解除失败");
						return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
					}
				}else{
					log.info(loginname+"通过后台控制进行转账");
				}
			}
			
			//将已领取的负盈利反赠更新为已处理
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "pttiger"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				losePromo.setStatus("2");
				update(losePromo);
			}
			// 检查额度
				transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				fb.setMoney(localCredit-remit);
				fb.setCreatetime(new Date());
				Friendbonusrecord fr = new Friendbonusrecord();
				fr.setCreatetime(new Date());
				fr.setToplineuser(loginname);
				fr.setMoney(remit);
				fr.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+(localCredit-remit));
				fr.setType("2");//支出
				this.save(fr);
				update(fb);
				
				
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_FRIEND_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+(localCredit-remit)));
				// 标记额度已被扣除
				creditReduce = true;
				if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

				} else {
					customer.setShippingcodePt(null);
					update(customer);
				}
		} catch (Exception e) {
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				result = "系统繁忙，请重新尝试";
			}
		}
		return result;
	}
	
	/**
	 * 好友推荐金额转入游戏ttg
	 */
	@Override
	public String selfConpon4TTG4Friend(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入TTG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账TTG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("TTG转账正在维护" + loginname);
			return "TTG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的TTG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "ttg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "ttg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(Friendbonus.class);
		dc.add(Restrictions.eq("toplineuser", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		Friendbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(Friendbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_FRIEND_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=localCredit-remit;
				sa.setMoney(localCredit);
				sa.setCreatetime(new Date());
				Friendbonusrecord fb = new Friendbonusrecord();
				fb.setCreatetime(new Date());
				fb.setToplineuser(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_FRIEND_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 好友推荐金额转入游戏qt
	 */
	@Override
	public String selfConpon4Qt4Friend(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入QT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账QT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("QT转账正在维护" + loginname);
			return "QT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的QT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "qt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "qt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(Friendbonus.class);
		dc.add(Restrictions.eq("toplineuser", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		Friendbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(Friendbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_FRIEND_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=localCredit-remit;
				sa.setMoney(localCredit);
				sa.setCreatetime(new Date());
				Friendbonusrecord fb = new Friendbonusrecord();
				fb.setCreatetime(new Date());
				fb.setToplineuser(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_FRIEND_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 好友推荐金额转入游戏nt
	 */
	@Override
	public String selfConponNT4Friend(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入NT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账NT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("NT转账正在维护" + loginname);
			return "NT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的nt存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "nt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "nt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(Friendbonus.class);
		dc.add(Restrictions.eq("toplineuser", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		Friendbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(Friendbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_FRIEND_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=localCredit-remit;
				sa.setMoney(localCredit);
				sa.setCreatetime(new Date());
				Friendbonusrecord fb = new Friendbonusrecord();
				fb.setCreatetime(new Date());
				fb.setToplineuser(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_FRIEND_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 好友推荐金额转入游戏mg
	 */
	@Override
	public String selfConpon4Mg4Friend(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("好友推荐金开始转入MG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账MG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("MG转账正在维护" + loginname);
			return "MG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的MG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "mg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "mg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(Friendbonus.class);
		dc.add(Restrictions.eq("toplineuser", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		Friendbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(Friendbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_FRIEND_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=Arith.round(Arith.sub(localCredit, remit),2);
				sa.setMoney(localCredit);
				sa.setCreatetime(new Date());
				Friendbonusrecord fb = new Friendbonusrecord();
				fb.setCreatetime(new Date());
				fb.setToplineuser(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_FRIEND_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 好友推荐金额转入游戏dt
	 */
	@Override
	public String selfConpon4Dt4Friend(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入DT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账DT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("DT转账正在维护" + loginname);
			return "DT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的DT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "dt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "dt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(Friendbonus.class);
		dc.add(Restrictions.eq("toplineuser", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		Friendbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(Friendbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_FRIEND_COUPON.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=localCredit-remit;
				sa.setMoney(localCredit);
				sa.setCreatetime(new Date());
				Friendbonusrecord fb = new Friendbonusrecord();
				fb.setCreatetime(new Date());
				fb.setToplineuser(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_FRIEND_COUPON.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 全民闯关金额转入游戏ttg
	 */
	@Override
	public String selfConpon4TTG4Emigrated(String transID, String loginname, Double remit, String remark) {
		log.info("开始转入TTG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账TTG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("TTG转账正在维护" + loginname);
			return "TTG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的TTG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "ttg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "ttg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(Emigratedbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		Emigratedbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(Emigratedbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_EMIGRATED.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=Arith.sub(localCredit, remit);
				sa.setMoney(localCredit);
				sa.setUpdatetime(new Date());
				Emigratedrecord fb = new Emigratedrecord();
				fb.setUpdatetime(new Date());
				fb.setUsername(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_EMIGRATED.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 全民闯关金额转入游戏qt
	 */
	@Override
	public String selfConpon4Qt4Emigrated(String transID, String loginname, Double remit, String remark) {
		log.info("开始转入QT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账QT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("QT转账正在维护" + loginname);
			return "QT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的QT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "qt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "qt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(Emigratedbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		Emigratedbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(Emigratedbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_EMIGRATED.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=Arith.sub(localCredit, remit);
				sa.setMoney(localCredit);
				sa.setUpdatetime(new Date());
				Emigratedrecord fb = new Emigratedrecord();
				fb.setUpdatetime(new Date());
				fb.setUsername(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_EMIGRATED.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 全民闯关金额转入游戏nt
	 */
	@Override
	public String selfConponNT4Emigrated(String transID, String loginname, Double remit, String remark) {
		log.info("开始转入NT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账NT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("NT转账正在维护" + loginname);
			return "NT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的nt存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "nt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "nt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		
		DetachedCriteria dc = DetachedCriteria.forClass(Emigratedbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Emigratedbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(Emigratedbonus) list.get(0);
			localCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_EMIGRATED.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				Double oldCredit=localCredit;
				localCredit=Arith.sub(localCredit, remit);
				sa.setMoney(localCredit);
				sa.setUpdatetime(new Date());
				Emigratedrecord fb = new Emigratedrecord();
				fb.setUpdatetime(new Date());
				fb.setUsername(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_EMIGRATED.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}


	/**
	 * 好友推荐奖金转入PT
	 */
	@Override
	public String transfer4Pt4Emigrated(String transID, String loginname, Double remit,String remark) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transfer4Pt4Emigrated:" + loginname);
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		if(customer==null){
			return "玩家账号不存在！";
		}
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(Emigratedbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		Emigratedbonus fb=null;
		if(null!=list&&list.size()>0){
			fb=(Emigratedbonus) list.get(0);
			localCredit= fb.getMoney();
			oldCredit=fb.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		if (localCredit < remit) {
			result = "转账失败，奖金额度不足";
			log.info("current credit:" + localCredit);
			log.info(result);
			return result ;
		}
		try {
			remit = Math.abs(remit);
			String couponString = null ;
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc1 = DetachedCriteria.forClass(Proposal.class);
				dc1 = dc1.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc1 = dc1.add(Restrictions.eq("flag", 2));
				dc1 = dc1.add(Restrictions.in("type", new Object[] { 571, 572, 573, 574, 575, 590, 591, 705, 409, 410, 411, 412, 415, 391,390 }));
				List<Proposal> list1 = findByCriteria(dc1);
				if (list1 == null || list1.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list1.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<6*60*1000){
					return "自助优惠5分钟内不允许转账！";
				}
				Integer type = proposal.getType();
				couponString = ProposalType.getText(type);
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				// 获取数据总投注额
				Double validBetAmount = getSelfYouHuiBet("pttiger", loginname, proposal.getPno()) ;
				
				/*****************************************/
				//后期远程金额
				Double remoteCredit=0.00;
				try {
						 remoteCredit = PtUtil.getPlayerMoney(loginname);
				} catch (Exception e) {
						return "获取异常失败";
				}
				validBetAmount = validBetAmount==null?0.0:validBetAmount;
				//判断是否达到条件
				log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
				if(validBetAmount != -1.0){
					if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
						log.info("解除成功");
					}else{
						PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"pttiger" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
						transferDao.save(transferRecord) ;
						log.info("解除失败");
						return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
					}
				}else{
					log.info(loginname+"通过后台控制进行转账");
				}
			}
			
			//将已领取的负盈利反赠更新为已处理
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "pttiger"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				losePromo.setStatus("2");
				update(losePromo);
			}
			// 检查额度
				transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
				fb.setMoney(Arith.sub(localCredit, remit));
				fb.setUpdatetime(new Date());
				Emigratedrecord fr = new Emigratedrecord();
				fr.setUpdatetime(new Date());
				fr.setUsername(loginname);
				fr.setMoney(remit);
				fr.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+Arith.sub(localCredit, remit));
				fr.setType("2");//支出
				this.save(fr);
				update(fb);
				
				
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_EMIGRATED.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+Arith.sub(localCredit, remit)));
				// 标记额度已被扣除
				creditReduce = true;
				if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

				} else {
					customer.setShippingcodePt(null);
					update(customer);
				}
		} catch (Exception e) {
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				result = "系统繁忙，请重新尝试";
			}
		}
		return result;
	}
	
	@Override
	public String transferInGameUserFromAgentTiger(String loginnameAgent,
			Double remit, String transIDAgent, String transIDGame, String password) {
		
		Const constPt = transferDao.getConsts("代理转账游戏账号");
		if (constPt == null) {
			log.info("平台不存在" + loginnameAgent);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("代理转账游戏账号正在维护" + loginnameAgent);
			return "代理转账游戏账号正在维护";
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(UsersAgentGame.class);
		dc.add(Restrictions.eq("loginnameAgent", loginnameAgent));
		dc.add(Restrictions.ne("deleteFlag", 1));
		List<UsersAgentGame> list = userDao.findByCriteria(dc);
		if(list == null || list.size() == 0){
			return "您没有绑定游戏账号，不能进行该操作";
		}
		
		if(list.size() > 1){
			return "您绑定的游戏账号数量有问题，请联系客服";
		}
		String loginnameGame = list.get(0).getLoginnameGame();
		//代理老虎机佣金提款、代理真人佣金提款成功过
		DetachedCriteria dc_credit = DetachedCriteria.forClass(Proposal.class);
		dc_credit.add(Restrictions.eq("loginname", loginnameAgent));
		dc_credit.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode()));
		dc_credit.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		dc_credit.add(Restrictions.ne("remark", "转账：代理账户转入游戏账户"));
		dc_credit.setProjection(Projections.rowCount());
		List listRowCount = userDao.findByCriteria(dc_credit);
		Integer rowCount = (Integer) listRowCount.get(0);
		if(rowCount <= 0){
			return "您没有老虎机佣金或者真人佣金提款成功过，不能进行该操作！";
		}
		String result = null;
		log.info("begin to transferInGameUserFromAgentTiger,loginnameAgent:" + loginnameAgent + ",loginnameGame:" + loginnameGame);
		try {
			remit = Math.abs(remit);
			if(remit < 10){
				log.info("转出失败,转出金额：" + remit + ",不足10元。");
				return "转出金额必须大于等于10元";
			}
			
			Users agentUser = (Users) get(Users.class, loginnameAgent);
//			if(agentUser.getFlag() == 1){
//				log.info("转出失败,代理账号禁用：" + loginnameAgent);
//				return "您的代理账号已经禁用，请联系代理专员。";
//			}
//			if(agentUser.getWarnflag() == 2){
//				log.info("转出失败,代理账号危险：" + loginnameAgent);
//				return "您的代理账号是危险账号，不能进行转出操作！请联系代理专员。";
//			}
			
			if(!agentUser.getPassword().equals(EncryptionUtil.encryptPassword(password))){
				log.info("转出失败：代理账号密码错误");
				return "您的代理密码输入错误,请重新操作";
			}
			
			if(agentUser.getCredit() != null && agentUser.getCredit() <= -100){
				log.info("转出失败：代理账号真人佣金为" + agentUser.getCredit() + ",必须大于-100元。");
				return "转出失败：代理账号真人佣金为" + agentUser.getCredit() + ",必须大于-100元。请联系代理专员。";
			}
			
			Users gameUser = (Users) get(Users.class, loginnameGame, LockMode.UPGRADE);
			if(gameUser == null){
				log.info("未找到代理账号：" + loginnameAgent + "绑定的游戏账号：" + loginnameGame);
				return "没有查找到绑定的游戏账号,请联系代理专员。";
			}
//			if(gameUser.getFlag()==1){
//				log.info("转出失败,游戏账号已经禁用：" + loginnameGame);
//				return "该游戏账号已经禁用，请联系代理专员。";
//			}
//			if(gameUser.getWarnflag() == 2){
//				log.info("转出失败,要转入的游戏账号是危险账号:" + loginnameGame);
//				return "要转入的游戏账号是危险账号，不能进行转入操作！请联系代理专员。";
//			}
			
			Userstatus agentStatus = (Userstatus) this.get(Userstatus.class, loginnameAgent);
			if(agentStatus == null || agentStatus.getSlotaccount() == null){
				log.info("没有查询到:" + loginnameGame + "老虎机金额");
				return "没有查询到您的老虎机金额,无法转账";
			}
			if(agentStatus.getSlotaccount() < remit){
				log.info("转出失败,老虎机余额为" + agentStatus.getSlotaccount() + "元,不足" + remit + "元");
				return "您的老虎机余额为：" + agentStatus.getSlotaccount() + "元,不足" + remit + "元";
			}
			
			
			Double localCredit = gameUser.getCredit();
			String remark = "转账：代理账户转入游戏账户";
			//代理账号转出
			Proposal proposalAgent = new Proposal(transIDAgent, agentUser.getLoginname(), DateUtil.now(), ProposalType.CASHOUT.getCode(), agentUser.getLevel(), agentUser.getLoginname(), remit, agentUser.getAgent(), ProposalFlagType.EXCUTED.getCode(),
					Constants.FROM_FRONT, remark, "customer");
			proposalAgent.setExecuteTime(new Date());
			transferDao.save(proposalAgent);
			transferDao.addTransferforAgent(Long.parseLong(transIDAgent), loginnameAgent, agentStatus.getSlotaccount(), remit, Constants.OUT);
			tradeDao.changeCreditForAgentSlot(loginnameAgent, -remit, CreditChangeType.TRANSFER_AGENTOUT.getCode(), transIDAgent, remark);
			//游戏账号转入
			Proposal proposalGame = new Proposal(transIDGame, gameUser.getLoginname(), DateUtil.now(), ProposalType.CASHIN.getCode(), gameUser.getLevel(), gameUser.getLoginname(), remit, gameUser.getAgent(), ProposalFlagType.EXCUTED.getCode(),
					Constants.FROM_FRONT, remark, "customer");
			proposalGame.setExecuteTime(new Date());
			transferDao.save(proposalGame);
			transferDao.addTransferforAgent(Long.parseLong(transIDGame), loginnameGame, gameUser.getCredit(), remit, Constants.IN);
			tradeDao.changeCreditIn(gameUser, remit, CreditChangeType.TRANSFER_LONGDUIN.getCode(), transIDGame, remark);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	/**
	 * NTwo Live
	 * 不进行回滚，无论如何，先把额度扣掉再说
	 */
	@Override
	public String transferInNTwo(String transID, String loginname, Double remit) {
		log.info("开始转入N2Live准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账N2Live");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("N2Live转账正在维护" + loginname);
			return "N2Live转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		// 获取额度
		Double localCredit = customer.getCredit();
		try {
			customer.setCreditday(customer.getCreditday()+remit);
			// 检查额度
			log.info("current credit:" + localCredit);
			//判断额度是否小于等于0
			if(remit<=0.0){
				log.info("额度变化为0，不操作..");
				return "度变化为0";
			} 
			//判断额度是否大于转账额度
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + localCredit+"******"+loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_N2LIVE_IN.getCode(), transID, null);
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	@Override
	public String transferOutNTwo(String transID, String loginname, Double remit) {

		String result = null;
		log.info("begin to transferOutNTwo:" + loginname);
		try {
			remit = Math.abs(remit);
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			
			NTwoCheckClientResponseBean nTwoCheckClientResponseBean = null;
			try {
				nTwoCheckClientResponseBean = NTwoUtil.checkClient(loginname);
				if (nTwoCheckClientResponseBean == null || nTwoCheckClientResponseBean.isFail() || !NumberUtils.isNumber(String.valueOf(nTwoCheckClientResponseBean.getBalance()))) {
					log.info(loginname + " N2Live 无法获取到正确资料!");
					return "系统繁忙!请稍后再试";
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info(loginname + "获取额度超时!系统繁忙!" + e.toString());
				return "系统繁忙!请稍后再试";
			}
			
			Double localCredit = customer.getCredit();
			// 检查远程额度
			Double remoteCredit = nTwoCheckClientResponseBean.getBalance().doubleValue();

			log.info("remote credit:" + remoteCredit);
			if (remoteCredit < remit) {
				log.info(result);
				result = "转账失败，额度不足";
				return result;
			}
			
			NTwoWithdrawalConfirmationResponseBean withdrawalBean = null;
			// 转账操作
			try {
				withdrawalBean = NTwoUtil.withdrawPendingRequest(customer.getLoginname(), remit, transID);
			} catch (Exception e) {
				log.warn("withdrawalBean try again!");
				withdrawalBean = NTwoUtil.withdrawPendingRequest(customer.getLoginname(), remit, transID);
			}

			if (withdrawalBean != null && withdrawalBean.isSuccess()) {
				this.addTransferNTwo(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.SUCESS, withdrawalBean.getPaymentid(), "转出成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_N2LIVE_OUT.getCode(), transID, null);
				if (customer.getShippingcode() == null || customer.getShippingcode().equals("")) {

				} else {
					customer.setShippingcode(null);
					update(customer);
				}
			} else {
				result = "转账状态错误:" + ErrorCode.getChText(withdrawalBean.getStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}
	
	@Override
	public Transfer addTransferNTwo(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		// TODO Auto-generated method stub
		return transferDao.addTransferNTwo(transID, loginname, credit, remit, in, flag, paymentid, null);
	}
	
	public String transferMgValidateIn(String transID, String loginname, Double remit, String remark) {
		log.info("开始转入MG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("MG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("MG转账正在维护" + loginname);
			return "MG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		if (localCredit < remit) {
			log.info("转账失败，额度不足" + loginname);
			return "转账失败，额度不足";
		}
		
		//看玩家是否有正在使用的优惠，是否达到流水MG
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "mg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		
		if(null != selfs && selfs.size()>0){
			SelfRecord record = selfs.get(0);
			if(null != record){
				Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
				String youhuiType = ProposalType.getText(proposal.getType());
				if(null != proposal){
					if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
						return "自助优惠5分钟内不允许户内转账！";
					}
					// 要达到的总投注额
					Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
					PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
					Double validBetAmount =null;
					if(preferential.getType().equals(1)){
						validBetAmount=-1.0;
					}else{
						
						// 活动领取时的时间。
						Date startTime = record.getCreatetime();
						Date endTime = new Date();
						try {
							validBetAmount = getMgBetsAmount(loginname, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(endTime));
						} catch (Exception e) {
							e.printStackTrace();
							log.error("查询:" + loginname + ",时间：" + DateUtil.formatDateForStandard(startTime)+ "-----" +  DateUtil.formatDateForStandard(endTime) + "MG投注额异常：", e);;
							return "您领取了自助存送优惠，但查询" + DateUtil.formatDateForStandard(startTime)+ "--" +  DateUtil.formatDateForStandard(endTime) + "MG投注额异常,无法进行转账,请稍后再试";
						}
					}
					//后期远程金额
					Double remoteCredit = 0.0;
					try {
						//remoteCredit = MGSUtil.getBalance(loginname,customer.getPassword());
						remoteCredit = MGSUtil.getBalance(loginname);
					} catch (Exception e) {
						log.error("查询" + loginname + "MG余额异常：", e);;
						return "查询MG余额异常，请稍后再试";
					}

					validBetAmount = validBetAmount==null?0.0:validBetAmount;
					//判断是否达到条件
					log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
					if(validBetAmount != -1.0){
						if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
							record.setType(1);
							record.setUpdatetime(new Date());
							update(record);
							log.info("解除成功");
						}else{
							PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"mg" ,validBetAmount, (amountAll-validBetAmount) , new Date() , youhuiType , "IN");
							transferDao.save(transferRecord) ;
							log.info("解除失败");
							return youhuiType + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者MG游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
						}
					}else{
						record.setType(1);
						record.setRemark(record.getRemark()+";后台放行");
						record.setUpdatetime(new Date());
						update(record);
						log.info(loginname+"通过后台控制进行转账");
					}
				}else{
					log.info("MG自助优惠信息出错。Q" + loginname);
					return "MG自助优惠信息出错。Q";
				}
			}else{
				log.info("MG自助优惠信息出错。M" + loginname);
				return "MG自助优惠信息出错。M";
			}
		}
				
		try {
			tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_MGIN.getCode(), transID, remark);
			return null;
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	@Override
	public Transfer addTransferforMg(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		return transferDao.addTransferforMg( transID,  loginname,  localCredit,  remit,  in,  flag,  paymentid,  remark);
	}
	
	public String transferMgOut(String transID, String loginname, Double remit) {
		Const constPt = transferDao.getConsts("MG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("MG转账正在维护" + loginname);
			return "MG转账正在维护";
		}
		if(remit.intValue() != remit){
			return "请输入整数";
		}
		String result = null;
		log.info("begin to transferMgOut:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname);
			if (customer == null) {
				return "玩家账号不存在！";
			}
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			
			/********************************/
			remit = Math.abs(remit);
			String couponString = null ;
			Double localCredit = customer.getCredit();
			
			//后期远程金额
			Double remoteCredit=0.00;
			try {
				//remoteCredit = MGSUtil.getBalance(loginname,customer.getPassword());
				remoteCredit = MGSUtil.getBalance(loginname);
			} catch (Exception e) {
				e.printStackTrace();
				return "获取异常失败";
			}

			if (remoteCredit < remit) {
				result = "转出失败，额度不足";
				log.info("remoteCredit :" + remoteCredit);
				log.info(result);
				return result ;
			}
			
			
			//是否存在已领取的负盈利反赠，如果存在判断流水
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "mg"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				Double requiredBet = Arith.mul(losePromo.getPromo(), losePromo.getTimes());
				//计算流水
	
				Double realBet = getMgBetsAmount(loginname, DateUtil.formatDateForStandard(losePromo.getGetTime()), DateUtil.formatDateForStandard(new Date()));
			
				if(requiredBet > realBet){
					log.info("玩家：" + loginname + " 已领取负盈利反赠，未达到提款流水，不允许转出");
					return "您已领取救援金，流水须达到 " + requiredBet + " 元才能转出，目前流水额为：" + realBet + " 元";
				}
			}
			//将已领取负盈利反赠，更新为已处理
			for (LosePromo losePromo2 : losePromoList) {
				losePromo2.setStatus("2");
				update(losePromo2);
			}
			
			//MG体验金
			if(remoteCredit >=1 && remoteCredit<Constants.MAXIMUMDTOUT){
				DetachedCriteria dc8 = DetachedCriteria.forClass(Transfer.class);
				dc8.add(Restrictions.eq("loginname", loginname)) ;
				dc8.add(Restrictions.eq("target", "mg")) ;
				dc8.addOrder(Order.desc("createtime"));
				List<Transfer> transfers = transferDao.findByCriteria(dc8, 0, 1) ;
				if(null != transfers && transfers.size()>0){
					Transfer transfer = transfers.get(0);
					if(null != transfer){
						if(transfer.getRemark().contains("自助优惠")){  //最后一笔是体验金的转账记录
							return "您正在使用体验金，MG余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
						}
						if(transfer.getRemark().contains(SynchronizedAppPreferentialUtil.appPreferential)){  //最后一笔是[app下载彩金]的转账记录
							return "您正在使用app下载彩金，MG余额大于等于" + Constants.MAXIMUMDTOUT + "或者小于1的时候才能进行户内转账";
						} 
					}
				}
			}
			
			//看玩家是否有正在使用的优惠，是否达到流水MG
			DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
			selfDc.add(Restrictions.eq("loginname", loginname));
			selfDc.add(Restrictions.eq("platform", "mg"));
			selfDc.add(Restrictions.eq("type", 0));
			selfDc.addOrder(Order.desc("createtime"));
			List<SelfRecord> selfs = this.findByCriteria(selfDc);
			
			if(null != selfs && selfs.size()>0){
				SelfRecord record = selfs.get(0);
				if(null != record){
					Proposal proposal = (Proposal) get(Proposal.class, record.getPno());
					String youhuiType = ProposalType.getText(proposal.getType());
					if(null != proposal){
						if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<5*60*1000){
							return "自助优惠5分钟内不允许户内转账！";
						}
						// 要达到的总投注额
						Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());
						PreferentialRecord preferential = (PreferentialRecord) get(PreferentialRecord.class, record.getPno());
						Double validBetAmount =null;
						if(preferential.getType().equals(1)){
							validBetAmount=-1.0;
						}else{
							
							// 活动领取时的时间。
							Date startTime = record.getCreatetime();
							Date endTime = new Date();
							try {
								validBetAmount = getMgBetsAmount(loginname, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(endTime));
							} catch (Exception e) {
								e.printStackTrace();
								log.error("查询:" + loginname + ",时间：" + DateUtil.formatDateForStandard(startTime)+ "-----" +  DateUtil.formatDateForStandard(endTime) + "MG投注额异常：", e);
								return "您领取了自助存送优惠，但查询" + DateUtil.formatDateForStandard(startTime)+ "--" +  DateUtil.formatDateForStandard(endTime) + "MG投注额异常,无法进行转账,请稍后再试";
							}
						}
						//后期远程金额
//						Double remoteCredit = 0.0;
//						try {
//							remoteCredit = MGSUtil.getBalance(loginname);
//						} catch (Exception e) {
//							log.error("查询" + loginname + "MG余额异常：", e);;
//							return "查询MG余额异常，请稍后再试";
//						}

						validBetAmount = validBetAmount==null?0.0:validBetAmount;
						//判断是否达到条件
						log.info("amountAll:"+amountAll+"=========validBetAmount:"+validBetAmount);
						log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
						if(validBetAmount != -1.0){
							if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
								record.setType(1);
								record.setUpdatetime(new Date());
								update(record);
								log.info("解除成功");
							}else{
								PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"mg" ,validBetAmount, (amountAll-validBetAmount) , new Date() , youhuiType , "IN");
								transferDao.save(transferRecord) ;
								log.info("解除失败");
								return youhuiType + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者MG游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
							}
						}else{
							record.setType(1);
							record.setRemark(record.getRemark()+";后台放行");
							record.setUpdatetime(new Date());
							update(record);
							log.info(loginname+"通过后台控制进行转账");
						}
					}else{
						log.info("MG自助优惠信息出错。Q" + loginname);
						return "MG自助优惠信息出错。Q";
					}
				}else{
					log.info("MG自助优惠信息出错。M" + loginname);
					return "MG自助优惠信息出错。M";
				}
			}

			String mgMsg = MGSUtil.tranferFromMG(loginname, customer.getPassword(),remit,transID);
			if(mgMsg != null){
				return result;
			}else{
				transferDao.addTransferforMg(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转出成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_MGOUT.getCode(), transID, couponString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}
	
	/**
	 * MG存送转账
	 */
	@Override
	public String selfCouponMG(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入MG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("MG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("MG转账正在维护" + loginname);
			return "MG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的MG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "mg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "mg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}

		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, remit * -1,
						CreditChangeType.TRANSFER_MGIN.getCode(), transID,
						remark);
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	
	@Override
	public Const getConsts(String id) {
		return transferDao.getConsts(id);
	}
	
	public String transferDTAndSelfYouHuiInModify(String pno, String selfname, String transID, String loginname, Double remit, String remark) {
		
		String result = null;
		
		Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
		
		if (null == customer) {
			
			return "玩家账号不存在！";
		}
		
		if (1 == customer.getFlag()) {
			
			return "该账号已经禁用！";
		}
		
		remit = Math.abs(remit);
		
		Double localCredit = customer.getCredit();
		log.info("当前 " + loginname + " 主账户余额：" + localCredit);
		
		if (localCredit < remit) {
			
			return "转账失败，额度不足！";
		}
		
		// 将之前使用的DT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "dt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));

		List<SelfRecord> selfs = this.findByCriteria(selfDc);

		for (SelfRecord selfRecord : selfs) {

			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());

			update(selfRecord);
		}
		
		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "dt"));
		c.add(Restrictions.eq("status", "1"));
		
		List<LosePromo> losePromoList = findByCriteria(c);
		
		for (LosePromo losePromo : losePromoList) {
			
			losePromo.setStatus("2");
			update(losePromo);
		}
		
		try {
			// 新增转账记录
			transferDao.addTransferforDt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "", "转入成功");
			
			// 更新账户信息
			tradeDao.changeCredit(loginname, remit * -1, CreditChangeType.TRANSFER_DTIN.getCode(), transID, remark);
			
			// 更新当前用户总转账金额
			customer.setCreditday(customer.getCreditday() + remit);
			update(customer);
			
			// 记录下到目前为止的投注额度 begin
			PreferentialRecord record = new PreferentialRecord(pno, loginname, "dt", 0.00, new Date(), 0);
			this.save(record);
			// 记录下到目前为止的投注额度 end
			
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(pno);
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform("dt");
			selfRecord.setSelfname(selfname);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());
			
			this.save(selfRecord);
		} catch (Exception e) {
			
			result = e.getMessage();
		}
		
		return result;
	}

	/**
	 * 优惠劵处理MG
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforCouponMG(String transID, String loginname, String couponType, Double remit, String couponCode, String ip) {
		String result = null;
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		log.info("begin to MG优惠券处理:"+couponCode+"玩家"+  loginname);
		boolean creditReduce = false;
		try {
//			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				vo.setMessage("用户不存在！");
				log.error("用户不存在！");
				return vo;
			}
			if(customer.getFlag()==1){
				vo.setMessage("该账号已经禁用");
				log.error("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		//	dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[]{430,431,432,433}));
			List<Proposal> list = findByCriteria(dc);
			if (list == null || list.size() <= 0) {
				vo.setMessage("优惠代码错误！");
				log.error("优惠代码错误！");
				return vo;
			}
			// 获取提案信息
			Proposal proposal = list.get(0);
			if(!StringUtil.isEmpty(proposal.getLoginname())){
				if(!(proposal.getLoginname().equals(loginname))){
					vo.setMessage("优惠代码错误！");
					log.error("优惠代码错误！");
					return vo;
				}
			}
			Double localCredit = customer.getCredit();
			Double amount = 0.0;
			Double gifTamount=0.0;
			String proposalTypeText="";
			if(50>remit){
				vo.setMessage("存送优惠卷,存款金额必须大于50！");
				log.error("存送优惠卷,存款金额必须大于50！");
				return vo;
			}
			if (localCredit < remit) {
				vo.setMessage("账号额度不足！");
				log.error("账号额度不足！");
				return vo;
			}
			Integer type = proposal.getType();
			if (type == 430) {
				proposalTypeText=ProposalType.COUPONCSYHYHJ430.getText();
				gifTamount = remit * 1;
				if (gifTamount >= 588.00) {
					gifTamount = 588.00;
				}
				amount = remit + gifTamount;
//					couponString = "MG100%存送优惠券15倍流水";
			}else if (type == 431) {
				proposalTypeText=ProposalType.COUPONCSYHYHJ431.getText();
				gifTamount = remit * 0.88;
				if (gifTamount >= 888) {
					gifTamount =888.00;
				}
				amount = remit + gifTamount;
//					couponString = "MG88%存送优惠券";
			}else if (type == 432) {
				proposalTypeText=ProposalType.COUPONCSYHYHJ432.getText();
				gifTamount = remit * 0.68;
				if (gifTamount >= 1888) {
					gifTamount = 1888.00;
				}
				amount = remit + gifTamount;
//					couponString = "MG68%存送优惠券";
			}else if (type == 433) {
				proposalTypeText=ProposalType.COUPONCSYHYHJ433.getText();
				gifTamount = remit * 1;
				if (gifTamount >= 1000) {
					gifTamount = 1000.00;
				}
				amount = remit + gifTamount;
//					couponString = "MG100%存送优惠券20倍流水";
			}else {
				vo.setMessage("不存在该类型优惠");
				log.error("不存在该类型优惠");
				return vo;
			}
			String typeString="";
			// 判断是哪个平台
			try {
				typeString = "LONGDU->MG";
					try {
						//Double remoteCredit=MGSUtil.getBalance(customer.getLoginname(),customer.getPassword());
						Double remoteCredit=MGSUtil.getBalance(customer.getLoginname());
						if (remoteCredit != null) {
							// 判断MG平台用户的金额是否
							log.info("remote credit:" + remoteCredit);
							if (remoteCredit > 5) {
								vo.setMessage("MG平台金额必须小于5,才能使用该优惠劵！");
								log.error("MG平台金额必须小于5,才能使用该优惠劵！");
								return vo;
							}
							try {
							    String remark = proposalTypeText + proposal.getBetMultiples() + "倍流水，存" + remit + "送" + gifTamount;
								Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, amount, remark);
								String msg = this.transferInforMgIn(transID, loginname, remit, proposalTypeText, remark);
								if(null!=msg){
									vo.setMessage("MG优惠券使用失败："+msg);
									log.error("MG优惠券使用失败："+msg);
									return vo;
								}
								this.save(offer);
							} catch (Exception e) {
									vo.setMessage("获取异常失败");
									log.error("获取异常失败",e);
									return vo;
							}
						} else {
							vo.setMessage( "转账金额失败！");
							log.error("转账金额失败！");
							return vo;
						}
					} catch (Exception e) {
							vo.setMessage( "获取异常失败");
							log.error("获取异常失败",e);
							return vo;
					}
			} catch (Exception e) {
				vo.setMessage("系统异常");
				log.error("系统异常",e);
				return vo;
			}
			//更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());
			proposal.setAmount(remit);
			proposal.setGifTamount(gifTamount);
			proposal.setRemark(typeString+"创建时间:"+proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			if(StringUtil.isEmpty(proposal.getLoginname())){
				proposal.setLoginname(loginname);
			}
			proposal.setAgent(customer.getAgent());
			update(proposal);
			// 记录下到目前为止的投注额度 begin
			String platform = "mg";
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, 0.0, new Date(), 0);
			this.save(record);
			// 记录下到目前为止的投注额度 end
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(proposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());//新投注额开始时间。
			this.save(selfRecord);
			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			try {
				this.addTransferforMg(Long.parseLong(transID), loginname, customer.getCredit(), remit, Constants.IN, Constants.FAIL, "", "转入成功");
				result=null;
				vo.setGiftMoney(amount);
			} catch (Exception e) {
				result="MG优惠券使用失败，请联系客服！";
				log.error("MG优惠券使用失败:",e);
				vo.setMessage(result);
				return vo;
			}
		}catch (Exception e) {
			vo.setMessage("系统异常");
			log.error("MG优惠券使用失败:", e);
			return vo;
		}
		vo.setMessage("success");
		log.info("用户：" + loginname + ",MG存送优惠券记录更改成功！");
		return vo;
	}
	
	
	/**
	 * 优惠劵处理DT
	 */
	@SuppressWarnings("unchecked")
	public AutoYouHuiVo transferInforCouponDT(String transID, String loginname, String couponType, Double remit, String couponCode, String ip) {
		String result = null;
		AutoYouHuiVo vo = new AutoYouHuiVo() ;
		log.info("begin to DT优惠券处理:"+couponCode+"玩家"+  loginname);
		boolean creditReduce = false;
		try {
//			String couponString = "";
			Users customer = (Users) get(Users.class, loginname, LockMode.UPGRADE);
			if (customer == null) {
				vo.setMessage("用户不存在！");
				log.error("用户不存在！");
				return vo;
			}
			if(customer.getFlag()==1){
				vo.setMessage("该账号已经禁用");
				log.error("该账号已经禁用");
				return vo;
			}
			// 获取现在时间和上个月的现在时间
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			Date date = new Date();
			calendar.setTime(date);
			calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
			// 查询优惠码
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		//	dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.ge("createTime", sdf.parse(sdf.format(calendar.getTime()))));
			dc = dc.add(Restrictions.lt("createTime", sdf.parse(sdf.format(date))));
			dc = dc.add(Restrictions.eq("shippingCode", couponCode));
			dc = dc.add(Restrictions.eq("flag", 1));
			dc = dc.add(Restrictions.in("type", new Object[]{434,435,436,437}));
			List<Proposal> list = findByCriteria(dc);
			if (list == null || list.size() <= 0) {
				vo.setMessage("优惠代码错误！");
				log.error("优惠代码错误");
				return vo;
			}
			// 获取提案信息
			Proposal proposal = list.get(0);
			if(!StringUtil.isEmpty(proposal.getLoginname())){
				if(!(proposal.getLoginname().equals(loginname))){
					vo.setMessage("优惠代码错误！");
					log.error("优惠代码错误");
					return vo;
				}
			}
			Double localCredit = customer.getCredit();
			Double amount = 0.0;
			Double gifTamount=0.0;
			String proposalTypeText="";
			if(50>remit){
				vo.setMessage("存送优惠卷,存款金额必须大于50！");
				log.error("存送优惠卷,存款金额必须大于50");
				return vo;
			}
			if (localCredit < remit) {
				vo.setMessage("账号额度不足！");
				log.error("账号额度不足");
				return vo;
			}
			Integer type = proposal.getType();
			if (type == 434) {
				proposalTypeText=ProposalType.COUPONCSYHYHJ434.getText();
				gifTamount = remit * 1;
				if (gifTamount >= 588.00) {
					gifTamount = 588.00;
				}
				amount = remit + gifTamount;
//					couponString = "DT100%存送优惠券15倍流水";
			}else if (type == 435) {
				proposalTypeText=ProposalType.COUPONCSYHYHJ435.getText();
				gifTamount = remit * 0.88;
				if (gifTamount >= 888) {
					gifTamount =888.00;
				}
				amount = remit + gifTamount;
//					couponString = "DT88%存送优惠券";
			}else if (type == 436) {
				proposalTypeText=ProposalType.COUPONCSYHYHJ436.getText();
				gifTamount = remit * 0.68;
				if (gifTamount >= 1888) {
					gifTamount = 1888.00;
				}
				amount = remit + gifTamount;
//					couponString = "DT68%存送优惠券";
			}else if (type == 437) {
				proposalTypeText=ProposalType.COUPONCSYHYHJ437.getText();
				gifTamount = remit * 1;
				if (gifTamount >= 1000) {
					gifTamount = 1000.00;
				}
				amount = remit + gifTamount;
//					couponString = "DT100%存送优惠券20倍流水";
			}else {
				vo.setMessage("不存在该类型优惠");
				log.error("不存在该类型优惠");
				return vo;
			}
			String typeString="";
			// 判断是哪个平台
			try {
				typeString = "LONGDU->DT";
					try {
						String remoteCreditStr=DtUtil.getamount(customer.getLoginname());
						Double remoteCredit=null;
						if(remoteCreditStr != null && NumberUtils.isNumber(remoteCreditStr)){
							remoteCredit=Double.parseDouble(remoteCreditStr);
						} else {
							log.error("DT存送优惠券查询DT平台余额出错：：：：" + remoteCreditStr);
							vo.setMessage("抱歉，查询DT平台余额出错。");
							return vo;
						}
						if (remoteCredit != null) {
							// 判断DT平台用户的金额是否
							log.info("remote credit:" + remoteCredit);
							if (remoteCredit > 5) {
								vo.setMessage("DT平台金额必须小于5,才能使用该优惠劵！");
								log.error("DT平台金额必须小于5,才能使用该优惠劵！");
								return vo;
							}
							try {
							    String remark = proposalTypeText + proposal.getBetMultiples() + "倍流水，存" + remit + "送" + gifTamount;
								Offer offer = new Offer(proposal.getPno(), UserRole.MONEY_CUSTOMER.getCode(), loginname, remit, amount, remark);
								String msg = this.transferInforDtIn(transID, loginname, remit, proposalTypeText, remark);
								if(null!=msg){
									vo.setMessage("DT优惠券使用失败："+msg);
									log.error("DT优惠券使用失败："+msg);
									return vo;
								}
								this.save(offer);
							} catch (Exception e) {
									vo.setMessage( "获取异常失败");
									log.error("获取异常失败");
									return vo;
							}
						} else {
							vo.setMessage( "转账金额失败！");
							log.error("转账金额失败！");
							return vo;
						}
					} catch (Exception e) {
						vo.setMessage( "获取异常失败");
						log.error("获取异常失败",e);
						return vo;
					}
			} catch (Exception e) {
				vo.setMessage( "系统异常");
				log.error("DT存送优惠券失败：",e);
				return vo;
			}
			//更新提案表
			proposal.setFlag(2);
			proposal.setLoginname(loginname);
			proposal.setExecuteTime(new Date());
			proposal.setQuickly(customer.getLevel());
			proposal.setAmount(remit);
			proposal.setGifTamount(gifTamount);
			proposal.setRemark(typeString+"创建时间:"+proposal.getCreateTime());
			proposal.setCreateTime(new Date());
			if(StringUtil.isEmpty(proposal.getLoginname())){
				proposal.setLoginname(loginname);
			}
			proposal.setAgent(customer.getAgent());
			update(proposal);
			// 记录下到目前为止的投注额度 begin
			String platform = "dt";
			PreferentialRecord record = new PreferentialRecord(proposal.getPno(), loginname, platform, 0.0, new Date(), 0);
			this.save(record);
			// 记录下到目前为止的投注额度 end
			SelfRecord selfRecord = new SelfRecord();
			selfRecord.setPno(proposal.getPno());
			selfRecord.setLoginname(loginname);
			selfRecord.setPlatform(platform);
			selfRecord.setSelfname(proposalTypeText);
			selfRecord.setType(0);
			selfRecord.setCreatetime(new Date());//新投注额开始时间。
			this.save(selfRecord);
			Userstatus userstatus = (Userstatus) this.get(Userstatus.class, loginname);
			if (userstatus == null) {
				Userstatus status = new Userstatus();
				status.setLoginname(loginname);
				status.setCashinwrong(0);
				status.setTouzhuflag(0);
				this.save(status);
			}
			try {
				this.addTransferforDt(Long.parseLong(transID), loginname, customer.getCredit(), remit, Constants.IN, Constants.FAIL, "", "转入成功");
				result=null;
				vo.setGiftMoney(amount);
			} catch (Exception e) {
				result="DT优惠券使用失败，请联系客服！";
				vo.setMessage(result);
				log.error("DT优惠券使用失败:", e);
				return vo;
			}
		}catch (Exception e) {
			vo.setMessage("系统异常");
			log.error("DT优惠券使用失败:", e);
			return vo;
		}
		log.info("用户：" + loginname + ",DT存送优惠券记录更改成功！");
		vo.setMessage("success");
		return vo;
	}
	
	@Override
	public String transferPngOut(String transID, String loginname, Double remit) {
		//转出开关当特殊情况发生可以加上
		/*Const constPt = transferDao.getConsts("转账PNG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("PNG转账正在维护" + loginname);
			return "PNG转账正在维护";
		}*/
		if(remit.intValue() != remit){
			return "请输入整数";
		}
		String result = null;
		log.info("begin to transferPngOut:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname);
			if (customer == null) {
				return "玩家账号不存在！";
			}
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			
			/********************************/
			remit = Math.abs(remit);
			String couponString = null ;
			Double localCredit = customer.getCredit();
			
			//后期远程金额
			Double remoteCredit=0.00;
			try {
				remoteCredit = PNGUtil.getBalance(loginname);
			} catch (Exception e) {
				e.printStackTrace();
				return "获取异常失败";
			}

			if (remoteCredit < remit) {
				result = "转出失败，额度不足";
				log.info(result + "remoteCredit :" + remoteCredit);
				return result ;
			}


			String mgMsg = PNGUtil.tranferFromPNG(loginname, remit);
			if(!"success".equals(mgMsg)){
				result = "PNG转账出现异常，请您稍后操作！";
			}else{
				transferDao.addTransferforPng(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转出成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_PNGOUT.getCode(), transID, couponString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
	}
	
	@Override
	public String transferPngValidateIn(String transID, String loginname, Double remit, String remark) {
		log.info("开始转入PNG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账PNG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("PNG转账正在维护" + loginname);
			return "PNG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		if (localCredit < remit) {
			log.info("转账失败，额度不足" + loginname);
			return "转账失败，额度不足";
		}
		try {
			tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_PNGIN.getCode(), transID, remark);
			return null;
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	@Override
	public Transfer addTransferforPng(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		return transferDao.addTransferforPng( transID,  loginname,  localCredit,  remit,  in,  flag,  paymentid,  remark);
	}
	
	@Override
	public Bankinfo createNewdeposit(String loginname, String banktype,
			String uaccountname, String ubankname, String ubankno, Double amout, boolean force,Double dptRange,String oldDepositId) {

		Bankinfo bank = null;
		String bankname ="";
		Users user = (Users) this.get(Users.class, loginname);
		if(null == user){
			log.info("getNewdeposit:玩家不存在");
			bank = new Bankinfo();
			bank.setMassage("getNewdeposit:玩家不存在");
			return bank;
		}
		
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cals = Calendar.getInstance();
		cals.setTime(new Date());
		cals.add(Calendar.DAY_OF_MONTH, -7);
		
		String tylId=""; //同略云订单号，同略云才使用
		Date date=new Date();
		String depositId= StringUtil.getRandomStrExceptOL0(6);

		Const consts = transferDao.getConsts("秒存存款限制");

		//网银（0,1）
		if(banktype.equals("0") || banktype.equals("1")){   
			
			bankname="网银";
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc = dc.add(Restrictions.eq("type", 1));
			dc = dc.add(Restrictions.eq("isshow", 1));
			dc = dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.in("bankname", new String[] { "招商银行", "兴业银行","平安银行","民生银行","华夏银行","中信银行","广发银行","桂林银行","邮政储蓄银行","农业银行","建设银行","青岛银行","浦发银行","交通银行","工商银行"}));
			dc = dc.add(Restrictions.eq("paytype", 0)); //paytype 0为网银招行
			
			if(user.getCreatetime().getTime()<cals.getTime().getTime()){
				dc.add(Restrictions.in("vpnname", new String[] { "A", "C"}));
			}else{
				dc.add(Restrictions.in("vpnname", new String[] { "B", "C"}));
			}
			
			
			List<Bankinfo> list = this.findByCriteria(dc);
			log.info("获取网银银行的dc：-----"+dc);
			if(list != null && list.size()>0 && list.get(0) != null)
			{
				if (consts!=null && consts.getValue().equals("1"))
				{
					dc.add(Restrictions.le("depositMin",dptRange));      //存款额度大与小与 之前 
					dc.add(Restrictions.ge("depositMax", dptRange));
					List<Bankinfo> list2 = this.findByCriteria(dc);
					if(list2.isEmpty()){
						bank = new Bankinfo();
						bank.setMassage("活跃度不足请先使用其他存款方式");
						return bank;
					}
				}
				
				
				Bankinfo bankinfoResult= getBankinfo(dc,user,oldDepositId);
				if(bankinfoResult==null)
				{
					bank = new Bankinfo();
					bank.setMassage("活跃度不足请先使用其他存款方式");
					return bank;
				}else if(StringUtil.isNotEmpty(bankinfoResult.getMassage())){
					return bankinfoResult;
				}
				else{
					bank = bankinfoResult;
				}
				
			}else{
				log.info(bankname+"后台未开启（条件--》type:1,isshow:1,useable:0,userole:"+user.getLevel()+"）");
				bank = new Bankinfo();
				bank.setMassage("存款银行已关闭，请您暂时使用其他存款方式");
				return bank;
			}
			
		}else if(banktype.equals("2")){
			bankname="支付宝";
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc = dc.add(Restrictions.eq("type", 1));
			dc = dc.add(Restrictions.eq("isshow", 1));
			dc = dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.in("bankname", new String[] {  "招商银行", "兴业银行","平安银行","民生银行","华夏银行","中信银行","广发银行","桂林银行","邮政储蓄银行","农业银行","建设银行","青岛银行","浦发银行","交通银行","工商银行"}));
			dc = dc.add(Restrictions.eq("paytype", 1)); //paytype 1为支付宝招行 
			
			if(user.getCreatetime().getTime()<cals.getTime().getTime()){
				dc.add(Restrictions.in("vpnname", new String[] { "A", "C"}));

			}else{
				dc.add(Restrictions.in("vpnname", new String[] { "B", "C"}));

			}			

			List<Bankinfo> list = this.findByCriteria(dc);
			log.info("获取支付宝的dc：-----"+dc);
			if(list != null && list.size()>0 && list.get(0) != null)
			{
				if (consts!=null && consts.getValue().equals("1"))
				{
					dc.add(Restrictions.le("depositMin",dptRange));      //存款额度大与小与 之前 
					dc.add(Restrictions.ge("depositMax", dptRange));
					List<Bankinfo> list2 = this.findByCriteria(dc);
					if(list2.isEmpty()){
						bank = new Bankinfo();
						bank.setMassage("活跃度不足请先使用其他存款方式");
						return bank;
					}
				}
				
				
				Bankinfo bankinfoResult= getBankinfo(dc,user,oldDepositId);
				if(bankinfoResult==null)
				{
					bank = new Bankinfo();
					bank.setMassage("活跃度不足请先使用其他存款方式");
					return bank;
				}else if(StringUtil.isNotEmpty(bankinfoResult.getMassage())){
					return bankinfoResult;
				}
				else{
					bank = bankinfoResult;
				}
				
			}else{
				log.info(bankname+"后台未开启（条件--》type:1,isshow:1,useable:0,userole:"+user.getLevel()+"）");
				bank = new Bankinfo();
				bank.setMassage("存款银行已关闭，请您暂时使用其他存款方式");
				return bank;
			}
		}else if(banktype.equals("22")){
			bankname="支付宝新版";
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc = dc.add(Restrictions.eq("type", 1));
			dc = dc.add(Restrictions.eq("isshow", 1));
			dc = dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.in("bankname", new String[] {  "招商银行", "兴业银行","平安银行","民生银行","华夏银行","中信银行","广发银行","桂林银行","邮政储蓄银行","农业银行","建设银行","青岛银行","浦发银行","交通银行","工商银行","广西北部湾银行","渤海银行","盛京银行","光大银行"}));
			dc = dc.add(Restrictions.eq("paytype", 22)); //paytype 3为微信转账

			if(user.getCreatetime().getTime()<cals.getTime().getTime()){
				dc.add(Restrictions.in("vpnname", new String[] { "A", "C"}));

			}else{
				dc.add(Restrictions.in("vpnname", new String[] { "B", "C"}));

			}

			List<Bankinfo> list = this.findByCriteria(dc);
			log.info("获取支付宝新版的dc：-----"+dc);
			if(list != null && list.size()>0 && list.get(0) != null)
			{
				if (consts!=null && consts.getValue().equals("1"))
				{
					dc.add(Restrictions.le("depositMin",dptRange));      //存款额度大与小与 之前
					dc.add(Restrictions.ge("depositMax", dptRange));
					List<Bankinfo> list2 = this.findByCriteria(dc);
					if(list2.isEmpty()){
						bank = new Bankinfo();
						bank.setMassage("活跃度不足请先使用其他存款方式");
						return bank;
					}
				}

				Bankinfo bankinfoResult= getBankinfo(dc,user,oldDepositId);
				if(bankinfoResult==null)
				{
					bank = new Bankinfo();
					bank.setMassage("活跃度不足请先使用其他存款方式");
					return bank;
				}else if(StringUtil.isNotEmpty(bankinfoResult.getMassage())){
					return bankinfoResult;
				}else{
					bank = bankinfoResult;
				}
			}
			else
			{
				log.info(bankname+"后台未开启（条件--》type:1,isshow:1,useable:0,userole:"+user.getLevel()+"）");
				bank = new Bankinfo();
				bank.setMassage("存款银行已关闭，请您暂时使用其他存款方式");
				return bank;
			}
		}else if(banktype.equals("4")){
			bankname="微信转账";
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc = dc.add(Restrictions.eq("type", 1));
			dc = dc.add(Restrictions.eq("isshow", 1));
			dc = dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.in("bankname", new String[] {  "招商银行", "兴业银行","平安银行","民生银行","华夏银行","中信银行","广发银行","桂林银行","邮政储蓄银行","农业银行","建设银行","青岛银行","浦发银行","交通银行","工商银行"}));
			dc = dc.add(Restrictions.eq("paytype", 4)); //paytype 3为微信转账 
			
			if(user.getCreatetime().getTime()<cals.getTime().getTime()){
				dc.add(Restrictions.in("vpnname", new String[] { "A", "C"}));

			}else{
				dc.add(Restrictions.in("vpnname", new String[] { "B", "C"}));

			}			

			List<Bankinfo> list = this.findByCriteria(dc);
			log.info("获取微信转账银行的dc：-----"+dc);
			if(list != null && list.size()>0 && list.get(0) != null)
			{
				if (consts!=null && consts.getValue().equals("1"))
				{
					dc.add(Restrictions.le("depositMin",dptRange));      //存款额度大与小与 之前 
					dc.add(Restrictions.ge("depositMax", dptRange));
					List<Bankinfo> list2 = this.findByCriteria(dc);
					if(list2.isEmpty()){
						bank = new Bankinfo();
						bank.setMassage("活跃度不足请先使用其他存款方式");
						return bank;
					}
				}
				
				Bankinfo bankinfoResult= getBankinfo(dc,user,oldDepositId);
				if(bankinfoResult==null)
				{
					bank = new Bankinfo();
					bank.setMassage("活跃度不足请先使用其他存款方式");
					return bank;
				}else if(StringUtil.isNotEmpty(bankinfoResult.getMassage())){
					return bankinfoResult;
				}
				else{
					bank = bankinfoResult;
				}
			}
			else
			{
				log.info(bankname+"后台未开启（条件--》type:1,isshow:1,useable:0,userole:"+user.getLevel()+"）");
				bank = new Bankinfo();
				bank.setMassage("存款银行已关闭，请您暂时使用其他存款方式");
				return bank;
			}
		}else if(banktype.equals("5")){
			bankname="云闪付";
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc = dc.add(Restrictions.eq("type", 1));
			dc = dc.add(Restrictions.eq("isshow", 1));
			dc = dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.in("bankname", new String[] { "云闪付"}));
			dc = dc.add(Restrictions.eq("paytype", 5)); //5 云闪付
			
			if(user.getCreatetime().getTime()<cals.getTime().getTime()){
			    dc.add(Restrictions.in("vpnname", new String[] { "A", "C"}));
			}else{
			    dc.add(Restrictions.in("vpnname", new String[] { "B", "C"}));
			}			
			
			List<Bankinfo> list = this.findByCriteria(dc);
			log.info("获取云闪付银行的dc：-----"+dc);
			if(list != null && list.size()>0 && list.get(0) != null){
				if (consts!=null && consts.getValue().equals("1")){
					dc.add(Restrictions.le("depositMin",dptRange));      //存款额度大与小与 之前 
					dc.add(Restrictions.ge("depositMax", dptRange));
					List<Bankinfo> list2 = this.findByCriteria(dc);
					if(list2.isEmpty()){
						bank = new Bankinfo();
						bank.setMassage("活跃度不足请先使用其他存款方式");
						return bank;
					}
				}
				
				Bankinfo bankinfoResult= getBankinfo(dc,user,oldDepositId);
				if(bankinfoResult==null)
				{
					bank = new Bankinfo();
					bank.setMassage("活跃度不足请先使用其他存款方式");
					return bank;
				}else if(StringUtil.isNotEmpty(bankinfoResult.getMassage())){
					return bankinfoResult;
				}
				else{
					bank = bankinfoResult;
				}
			}else{
				log.info(bankname+"后台未开启（条件--》type:1,isshow:1,useable:0,userole:"+user.getLevel()+"）");
				bank = new Bankinfo();
				bank.setMassage("存款银行已关闭，请您暂时使用其他存款方式");
				return bank;
			}
			
		}else if(banktype.equals("6")){
			
				bankname="银行二维码";
				DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
				dc = dc.add(Restrictions.eq("type", 1));
				dc = dc.add(Restrictions.eq("isshow", 1));
				dc = dc.add(Restrictions.eq("useable", 0));
				dc.add(Restrictions.in("bankname", new String[] { "银行二维码"}));
				dc = dc.add(Restrictions.eq("paytype", 6)); //paytype 3为微信转账 

				
				if(user.getCreatetime().getTime()<cals.getTime().getTime()){
					dc.add(Restrictions.in("vpnname", new String[] { "A", "C"}));

				}else{
					dc.add(Restrictions.in("vpnname", new String[] { "B", "C"}));
				}			

				List<Bankinfo> list = this.findByCriteria(dc);
				log.info("获取银行二维码的dc：-----"+dc);
				if(list != null && list.size()>0 && list.get(0) != null){
					if (consts!=null && consts.getValue().equals("1")){
						dc.add(Restrictions.le("depositMin",dptRange));      //存款额度大与小与 之前 
						dc.add(Restrictions.ge("depositMax", dptRange));
						List<Bankinfo> list2 = this.findByCriteria(dc);
						if(list2.isEmpty()){
							bank = new Bankinfo();
							bank.setMassage("活跃度不足请先使用其他存款方式");
							return bank;
						}
					}
					
					Bankinfo bankinfoResult= getBankinfo(dc,user,oldDepositId);
					if(bankinfoResult==null)
					{
						bank = new Bankinfo();
						bank.setMassage("活跃度不足请先使用其他存款方式");
						return bank;
					}else if(StringUtil.isNotEmpty(bankinfoResult.getMassage())){
						return bankinfoResult;
					}
					else{
						bank = bankinfoResult;
					}
				}else{
					log.info(bankname+"后台未开启（条件--》type:1,isshow:1,useable:0,userole:"+user.getLevel()+"）");
					bank = new Bankinfo();
					bank.setMassage("存款银行已关闭，请您暂时使用其他存款方式");
					return bank;
				}
				
		}else if(banktype.equals("7")){
			bankname="同略云银行";
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc = dc.add(Restrictions.eq("type", 1));
			dc = dc.add(Restrictions.eq("isshow", 1));
			dc = dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.in("bankname", new String[] {TlyBankFlagEnum.getText(ubankname.split("_")[1])}));
			dc = dc.add(Restrictions.eq("paytype", 7));
			
			if(user.getCreatetime().getTime()<cals.getTime().getTime()){
			    dc.add(Restrictions.in("vpnname", new String[] { "A", "C"}));
			}else{
			    dc.add(Restrictions.in("vpnname", new String[] { "B", "C"}));
			}			
			
			List<Bankinfo> list = this.findByCriteria(dc);
			log.info("获取同略云银行的dc：-----"+dc);
			if(list != null && list.size()>0 && list.get(0) != null){
				if (consts!=null && consts.getValue().equals("1")){
					dc.add(Restrictions.le("depositMin",dptRange));      //存款额度大与小与 之前 
					dc.add(Restrictions.ge("depositMax", dptRange));
					List<Bankinfo> list2 = this.findByCriteria(dc);
					if(list2.isEmpty()){
						bank = new Bankinfo();
						bank.setMassage("活跃度不足请先使用其他存款方式");
						return bank;
					}
				}
				
				Bankinfo bankinfoResult= getBankinfo(dc,user,oldDepositId);
				if(bankinfoResult==null)
				{
					bank = new Bankinfo();
					bank.setMassage("活跃度不足请先使用其他存款方式");
					return bank;
				}else if(StringUtil.isNotEmpty(bankinfoResult.getMassage())){
					return bankinfoResult;
				}
				else{
					bank = bankinfoResult;
				}
			}else{
				log.info(bankname+"后台未开启（条件--》type:1,isshow:1,useable:0,userole:"+user.getLevel()+"）");
				bank = new Bankinfo();
				bank.setMassage("存款银行已关闭，请您暂时使用其他存款方式");
				return bank;
			}
		}else if(banktype.equals("8")){
			bankname="微信二维码收款";
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc = dc.add(Restrictions.eq("type", 1));
			dc = dc.add(Restrictions.eq("isshow", 1));
			dc = dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.in("bankname", new String[] { bankname}));
			dc = dc.add(Restrictions.eq("paytype", 8)); //paytype 3为微信转账 
			
			if(user.getCreatetime().getTime()<cals.getTime().getTime()){
				dc.add(Restrictions.in("vpnname", new String[] { "A", "C"}));

			}else{
				dc.add(Restrictions.in("vpnname", new String[] { "B", "C"}));

			}			

			List<Bankinfo> list = this.findByCriteria(dc);
			log.info("微信二维码收款 dc：-----"+dc);
			if(list != null && list.size()>0 && list.get(0) != null){
				if (consts!=null && consts.getValue().equals("1")){
					dc.add(Restrictions.le("depositMin",dptRange));      //存款额度大与小与 之前 
					dc.add(Restrictions.ge("depositMax", dptRange));
					List<Bankinfo> list2 = this.findByCriteria(dc);
					if(list2.isEmpty()){
						bank = new Bankinfo();
						bank.setMassage("活跃度不足请先使用其他存款方式");
						return bank;
					}
				}
				
				Bankinfo bankinfoResult= getBankinfo(dc,user,oldDepositId);
				if(bankinfoResult==null)
				{
					bank = new Bankinfo();
					bank.setMassage("活跃度不足请先使用其他存款方式");
					return bank;
				}else if(StringUtil.isNotEmpty(bankinfoResult.getMassage())){
					return bankinfoResult;
				}
				else{
					bank = bankinfoResult;
				}
			}else{
				log.info(bankname+"后台未开启（条件--》type:1,isshow:1,useable:0,userole:"+user.getLevel()+"）");
				bank = new Bankinfo();
				bank.setMassage("存款银行已关闭，请您暂时使用其他存款方式");
				return bank;
			}
		}else if(banktype.equals("9")){
			bankname="支付宝二维码收款";
			DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
			dc = dc.add(Restrictions.eq("type", 1));
			dc = dc.add(Restrictions.eq("isshow", 1));
			dc = dc.add(Restrictions.eq("useable", 0));
			dc.add(Restrictions.in("bankname", new String[] {bankname}));
			dc = dc.add(Restrictions.eq("paytype",9)); //paytype 3为微信转账 

			if(user.getCreatetime().getTime()<cals.getTime().getTime()){
				dc.add(Restrictions.in("vpnname", new String[] { "A", "C"}));
			}else{
				dc.add(Restrictions.in("vpnname", new String[] { "B", "C"}));
			}			

			List<Bankinfo> list = this.findByCriteria(dc);
			log.info("获取支付宝二维码收款行的dc：-----"+dc);
			if(list != null && list.size()>0 && list.get(0) != null){
				if (consts!=null && consts.getValue().equals("1")){
					dc.add(Restrictions.le("depositMin",dptRange));      //存款额度大与小与 之前 
					dc.add(Restrictions.ge("depositMax", dptRange));
					List<Bankinfo> list2 = this.findByCriteria(dc);
					if(list2.isEmpty()){
						bank = new Bankinfo();
						bank.setMassage("活跃度不足请先使用其他存款方式");
						return bank;
					}
				}
				
				Bankinfo bankinfoResult= getBankinfo(dc,user,oldDepositId);
				if(bankinfoResult==null)
				{
					bank = new Bankinfo();
					bank.setMassage("活跃度不足请先使用其他存款方式");
					return bank;
				}else if(StringUtil.isNotEmpty(bankinfoResult.getMassage())){
					return bankinfoResult;
				}
				else{
					bank = bankinfoResult;
				}
			}else{
				log.info(bankname+"后台未开启（条件--》type:1,isshow:1,useable:0,userole:"+user.getLevel()+"）");
				bank = new Bankinfo();
				bank.setMassage("存款银行已关闭，请您暂时使用其他存款方式");
				return bank;
			}
		}
				
		Date s =null;
		Date e = null;
		try {
			s = DateUtil.ntStart();
			e = DateUtil.ntEnd();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		Double returnAmount=amout;
		Double InputAmount=null;
		if(banktype.equals("22")){
			DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
			dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.eq("status", 0));  //status，0表示未支付，1表示已支付，2表示已废除
			dc.add(Restrictions.ge("createtime",s));
			dc.add(Restrictions.eq("type","22"));
			dc.addOrder(Order.desc("createtime"));
			List<DepositOrder> lists = this.findByCriteria(dc);

			if(lists!=null&&lists.size()>0){
				if(force){
					for(DepositOrder depositOrder : lists){
						depositOrder.setStatus(2);
						depositOrder.setRemark("玩家强制废除订单,时间:"+ DateUtil.getNow());
						this.update(depositOrder);
					}
				} else {
					bank = new Bankinfo();
					bank.setDepositId(lists.get(0).getDepositId());
					bank.setForce(true);
					bank.setMassage("注意！！！您有订单未完成支付，请您先核实。若未支付，请您作废之前订单建立新订单。");
					return bank;
				}
			}

			//5秒内不能提交
			if(lists!=null && lists.size()>0)
			{
				System.out.println((loginname+"相差秒数："+((new Date()).getTime() - lists.get(0).getCreatetime().getTime())/1000 ));
				if(((new Date()).getTime() - lists.get(0).getCreatetime().getTime())/1000 < 5){
					bank = new Bankinfo();
					bank.setMassage("请不要重复提交订单");
					return bank;
				}
			}

			returnAmount = this.getQuota(loginname.trim());
			InputAmount = amout;
			if(returnAmount==0.0){
				bank = new Bankinfo();
				bank.setMassage("5分钟内未操作，请重新操作");
				return bank;
			}
			else {
				if(!this.getQuotaOrdery(returnAmount, ubankno, "22")){
					bank = new Bankinfo();
					bank.setMassage("该笔订单已存在，请更换其他存款金额，或其他存款卡号");
					return bank;
				}
			}
		}else if(banktype.equals("4")){
			
			DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
			dc = dc.add(Restrictions.eq("loginname", loginname));
			dc = dc.add(Restrictions.eq("status", 0));  //status，0表示未支付，1表示已支付，2表示已废除
			dc.add(Restrictions.ge("createtime",s)); 
			dc.add(Restrictions.eq("type","4"));  
			dc.addOrder(Order.desc("createtime"));
			List<DepositOrder> lists = this.findByCriteria(dc);
			
			if(lists!=null&&lists.size()>0){
				if(force){
					for(DepositOrder depositOrder : lists){
						depositOrder.setStatus(2);
						depositOrder.setRemark("玩家强制废除订单,时间:"+ DateUtil.getNow());
						this.update(depositOrder);
					}
				} else {
					bank = new Bankinfo();
					bank.setDepositId(lists.get(0).getDepositId());
					bank.setForce(true);
					bank.setMassage("注意！！！您有订单未完成支付，请您先核实。若未支付，请您作废之前订单建立新订单。");   
					return bank;
				}
			}
			
			//5秒内不能提交
			if(lists!=null && lists.size()>0)
			{
				System.out.println((loginname+"相差秒数："+((new Date()).getTime() - lists.get(0).getCreatetime().getTime())/1000 ));
				if(((new Date()).getTime() - lists.get(0).getCreatetime().getTime())/1000 < 5){
					bank = new Bankinfo();
					bank.setMassage("请不要在5秒内重复提交订单");   
					return bank;
				}
			}
			
			returnAmount = this.getQuota(loginname.trim());
			InputAmount = amout;
			if(returnAmount==0.0){
				bank = new Bankinfo();
				bank.setMassage("5分钟内未操作，请重新操作");
				return bank;
			}
			else {
				if(!this.getQuotaOrdery(returnAmount)){
					bank = new Bankinfo();
					bank.setMassage("请不要重复提交订单");   
					return bank;
				}
			}
		}else if(banktype.equals("5")){
			DetachedCriteria dd = DetachedCriteria.forClass(DepositOrder.class);
			dd = dd.add(Restrictions.eq("loginname", loginname));
			dd = dd.add(Restrictions.eq("status", 0));  //status，0表示未支付，1表示已支付，2表示已废除
			dd.add(Restrictions.ge("createtime",s));   
			dd.add(Restrictions.le("createtime", e));
			dd.add(Restrictions.in("type",new String[]{"5"}));  
			dd.addOrder(Order.desc("createtime"));
			List<DepositOrder> listd = this.findByCriteria(dd);
			
			if(listd!=null&&listd.size()>0){
				if(force){
					for(DepositOrder depositOrder : listd){
						depositOrder.setStatus(2);
						depositOrder.setRemark("玩家强制废除订单,时间:"+ DateUtil.getNow());
						this.update(depositOrder);
					}
				} else {
					bank = new Bankinfo();
					bank.setDepositId(listd.get(0).getDepositId());
					bank.setForce(true);
					bank.setMassage("注意！！！您有订单未完成支付，请您先核实。若未支付，请您作废之前订单建立新订单。");   
					return bank;
				}
			}
			
			if(!this.getQuotaOrdery(amout, ubankno, "5")){
				bank = new Bankinfo();
				bank.setForce(true);
				bank.setMassage("该笔订单已经存在,请不要重复提交或联系客服");   
				return bank;
			}
		} else if (banktype.equals("6")) {
			DetachedCriteria dd = DetachedCriteria.forClass(DepositOrder.class);
			dd = dd.add(Restrictions.eq("loginname", loginname));
			dd = dd.add(Restrictions.eq("status", 0)); // status，0表示未支付，1表示已支付，2表示已废除
			dd.add(Restrictions.ge("createtime", s));
			dd.add(Restrictions.le("createtime", e));
			dd.add(Restrictions.in("type", new String[] { "6" }));
			dd.addOrder(Order.desc("createtime"));
			List<DepositOrder> listd = this.findByCriteria(dd);

			if (listd != null && listd.size() > 0) {
				if (force) {
					for (DepositOrder depositOrder : listd) {
						depositOrder.setStatus(2);
						depositOrder.setRemark("玩家强制废除订单,时间:" + DateUtil.getNow());
						this.update(depositOrder);
					}
				} else {
					bank = new Bankinfo();
					bank.setDepositId(listd.get(0).getDepositId());
					bank.setForce(true);
					bank.setMassage("注意！！！您有订单未完成支付，请您先核实。若未支付，请您作废之前订单建立新订单。");
					return bank;
				}
			}

			returnAmount = this.getQuota(loginname.trim());
			InputAmount = amout;
			if (returnAmount == 0.0) {
				bank = new Bankinfo();
				bank.setMassage("5分钟内未操作，请重新操作");
				return bank;
			}

			if (!this.getQuotaOrdery(returnAmount, ubankno, "6")) {
				bank = new Bankinfo();
				bank.setForce(true);
				bank.setMassage("该笔订单已存在，请更换其他存款金额，或其他存款卡号");
				return bank;
			}

		}else if(banktype.equals("7")){
		   DetachedCriteria dd = DetachedCriteria.forClass(DepositOrder.class);
		   dd = dd.add(Restrictions.eq("loginname", loginname));
		   dd = dd.add(Restrictions.eq("status", 0));  //status，0表示未支付，1表示已支付，2表示已废除
		   dd.add(Restrictions.ge("createtime",s));   
		   dd.add(Restrictions.le("createtime", e));
		   dd.add(Restrictions.in("type",new String[]{"7"}));  
		    dd.addOrder(Order.desc("createtime"));
		   List<DepositOrder> listd = this.findByCriteria(dd);
		   
		if(listd!=null&&listd.size()>0){
			if(force){
				for(DepositOrder depositOrder : listd){
					depositOrder.setStatus(2);
					depositOrder.setRemark("玩家强制废除订单,时间:"+ DateUtil.getNow());
					this.update(depositOrder);
					try {
						TlyDepositUtil.revoke_order(depositOrder.getSpare());
					} catch (Exception e1) {
					    log.info("同略云撤销异常单号"+depositOrder.getSpare());
					}
				}
			} else {
				bank = new Bankinfo();
				bank.setDepositId(listd.get(0).getDepositId());
				bank.setForce(true);
				bank.setMassage("注意！！！您有订单未完成支付，请您先核实。若未支付，请您作废之前订单建立新订单。");   
				return bank;
			}
		}
		
		returnAmount = this.getQuota(loginname.trim());
		InputAmount = amout;
		if(returnAmount==0.0){
			bank = new Bankinfo();
			bank.setMassage("5分钟内未操作，请重新操作");
			return bank;
		}
		else if(!this.getQuotaOrdery(amout, ubankno, "7")){
			bank = new Bankinfo();
			bank.setForce(true);
			bank.setMassage("该笔订单已经存在,请不要重复提交或联系客服");   
			return bank;
		}
		
			try {
			     String tlyOrderId=loginname+"_"+banktype+"_"+ubankname.split("_")[0]+"_"+depositId;   //ubankno 臨時處理為選擇微信 還是
				 JSONObject json=TlyDepositUtil.sendOrder(tlyOrderId,uaccountname, ubankname.split("_")[1], ubankno, bank.getBankcard(), ubankname.split("_")[0],returnAmount.toString(), "", date);
			    if (!json.getBoolean("success")) {
			    	bank = new Bankinfo();
					bank.setForce(false);
					bank.setMassage("此银行维护中,请使用其他用户支付");   
					return bank;
				}else {
					tylId=String.valueOf(json.getInt("id"));
				}
			    
			} catch (Exception e1) {
			    log.info(e1);
				bank = new Bankinfo();
				bank.setForce(false);
				bank.setMassage("此银行维护中,请使用其他用户支付");   
				return bank;
			}
		
		} else if (banktype.equals("8")) {
			DetachedCriteria dd = DetachedCriteria.forClass(DepositOrder.class);
			dd = dd.add(Restrictions.eq("loginname", loginname));
			dd = dd.add(Restrictions.eq("status", 0)); // status，0表示未支付，1表示已支付，2表示已废除
			dd.add(Restrictions.ge("createtime", s));
			dd.add(Restrictions.le("createtime", e));
			dd.add(Restrictions.in("type", new String[] { "8" }));
			dd.addOrder(Order.desc("createtime"));
			List<DepositOrder> listd = this.findByCriteria(dd);

			if (listd != null && listd.size() > 0) {
				if (force) {
					for (DepositOrder depositOrder : listd) {
						depositOrder.setStatus(2);
						depositOrder.setRemark("玩家强制废除订单,时间:" + DateUtil.getNow());
						this.update(depositOrder);
					}
				} else {
					bank = new Bankinfo();
					bank.setDepositId(listd.get(0).getDepositId());
					bank.setForce(true);
					bank.setMassage("注意！！！您有订单未完成支付，请您先核实。若未支付，请您作废之前订单建立新订单。");
					return bank;
				}
			}

		} else if (banktype.equals("9")) {
			DetachedCriteria dd = DetachedCriteria.forClass(DepositOrder.class);
			dd = dd.add(Restrictions.eq("loginname", loginname));
			dd = dd.add(Restrictions.eq("status", 0)); // status，0表示未支付，1表示已支付，2表示已废除
			dd.add(Restrictions.ge("createtime", s));
			dd.add(Restrictions.le("createtime", e));
			dd.add(Restrictions.in("type", new String[] { "9" }));
			dd.addOrder(Order.desc("createtime"));
			List<DepositOrder> listd = this.findByCriteria(dd);

			if (listd != null && listd.size() > 0) {
				if (force) {
					for (DepositOrder depositOrder : listd) {
						depositOrder.setStatus(2);
						depositOrder.setRemark("玩家强制废除订单,时间:" + DateUtil.getNow());
						this.update(depositOrder);
					}
				} else {
					bank = new Bankinfo();
					bank.setDepositId(listd.get(0).getDepositId());
					bank.setForce(true);
					bank.setMassage("注意！！！您有订单未完成支付，请您先核实。若未支付，请您作废之前订单建立新订单。");
					return bank;
				}
			}

		}else {
			DetachedCriteria dd = DetachedCriteria.forClass(DepositOrder.class);
			dd = dd.add(Restrictions.eq("loginname", loginname));
			dd = dd.add(Restrictions.eq("status", 0));  //status，0表示未支付，1表示已支付，2表示已废除
			dd.add(Restrictions.ge("createtime",s));   
			dd.add(Restrictions.le("createtime", e));
			dd.add(Restrictions.in("type",new String[]{"0","1","2","3"}));  
			dd.addOrder(Order.desc("createtime"));
			List<DepositOrder> listd = this.findByCriteria(dd);
			
			if(listd!=null&&listd.size()>0){
				if(force){
					for(DepositOrder depositOrder : listd){
						depositOrder.setStatus(2);
						depositOrder.setRemark("玩家强制废除订单,时间:"+ DateUtil.getNow());
						this.update(depositOrder);
					}
				} else {
					bank = new Bankinfo();
					bank.setDepositId(listd.get(0).getDepositId());
					bank.setForce(true);
					bank.setMassage("注意！！！您有订单未完成支付，请您先核实。若未支付，请您作废之前订单建立新订单。");   
					return bank;
				}
			}
		}
		
		try {
			DepositOrder depositOrder = new DepositOrder();
			depositOrder.setLoginname(loginname);
			depositOrder.setBankname(bankname);
			depositOrder.setDepositId(depositId);
			depositOrder.setCreatetime(date);
			depositOrder.setStatus(0);
			depositOrder.setAccountname(bank.getUsername());
			depositOrder.setBankno(bank.getBankcard());
			depositOrder.setUbankname(ubankname);
			depositOrder.setUbankno(ubankno);   
			depositOrder.setUaccountname(uaccountname.trim());
			depositOrder.setRealname(user.getAccountName().trim());
			depositOrder.setFlag(1);
			depositOrder.setType(banktype);   
			depositOrder.setAmount(returnAmount);
			depositOrder.setInputamount(InputAmount);
			
			if (banktype.equals("7")) {
	        	depositOrder.setSpare(tylId);
			}
			
			this.save(depositOrder);
			bank.setDepositId(depositId);
			bank.setQuota(returnAmount);
			return bank;
		} catch (Exception e1) {
			e1.printStackTrace();
			bank = new Bankinfo();
			bank.setMassage("使用秒存请先绑定真实姓名");
			return bank;
		}
	}
	
	

	/**
	 * 
	 * @param list
	 * @param depositOrder
	 * @return
	 */
	private Bankinfo getBankinfo(DetachedCriteria dc,Users user, String oldDepositId) {

		dc = dc.add(Restrictions.like("userrole", "%"+user.getLevel()+"%"));
		
		if (oldDepositId==null || oldDepositId.equals("")) {
			dc.add(Restrictions.sqlRestriction(" 1=1 order by rand() "));
		}else{
			dc = dc.add(Restrictions.sqlRestriction("1=1 order by depositMin desc "));
		}
		
		List<Bankinfo> list = this.findByCriteria(dc);
		if(list.isEmpty()){
			return null;
			
		}else{

			if (oldDepositId==null || oldDepositId.equals("")) {
				return list.get(0);
			}else if (list.size()==1 && oldDepositId!=null && !oldDepositId.equals("")) {
				log.info("getBankinfo后台未开启（条件--》type:1,isshow:1,useable:0,userole:"+user.getLevel()+"）");
				Bankinfo bank = new Bankinfo();
				bank.setMassage("没有其他银行卡可以获取，请尝试其他存款方式 ");
				return bank;
			}else{
				
				Integer count = null;
				
				DepositOrder depositOrder=(DepositOrder) get(DepositOrder.class, oldDepositId);
				
				if (depositOrder==null ) {
					 return null;
				}

				for (int i = 0; i < list.size(); i++) {
					Bankinfo bank2 = list.get(i);
					if (bank2.getBankcard().equals(depositOrder.getBankno())) {
						if (i == (list.size() - 1))
							count = 0;
						else
							count = i + 1;
					}
				}
				if (count == null) {
					count = 0;
				}
				return list.get(count);
				
			}

		}

	}
	
	


	
	//查询是否存在该额度订单记录
	private  boolean getQuotaOrdery(Double amounts){
		
		DetachedCriteria dcDepositOrder = DetachedCriteria.forClass(DepositOrder.class);		
		dcDepositOrder = dcDepositOrder.add(Restrictions.eq("status", 0));  //status，0表示未支付，1表示已支付，2表示已废除
		dcDepositOrder.add(Restrictions.eq("type","4"));
		dcDepositOrder.add(Restrictions.eq("amount",amounts));
		dcDepositOrder.addOrder(Order.desc("createtime"));
		List<DepositOrder> lists = this.findByCriteria(dcDepositOrder);
		if(lists != null && lists.size()>0){
			return false;
		}
		else {
			return true;
		}
	}
	
	
	//查询是否存在该额度订单记录
	private  boolean getQuotaOrdery(Double amounts ,String ubankno,String type){
		
		DetachedCriteria dcDepositOrder = DetachedCriteria.forClass(DepositOrder.class);		
		dcDepositOrder = dcDepositOrder.add(Restrictions.eq("status", 0));  //status，0表示未支付，1表示已支付，2表示已废除
		if(StringUtil.isNotEmpty(ubankno)){
			dcDepositOrder.add(Restrictions.eq("ubankno",ubankno));
		}
		dcDepositOrder.add(Restrictions.eq("type",type));
		dcDepositOrder.add(Restrictions.eq("amount",amounts));
		dcDepositOrder.addOrder(Order.desc("createtime"));
		List<DepositOrder> lists = this.findByCriteria(dcDepositOrder);
		if(lists != null && lists.size()>0){
			return false;
		}
		else {
			return true;
		}
	}
	
		
	

	//獲取額度
	private  Double getQuota(String loginname){
		DetachedCriteria c = DetachedCriteria.forClass(Mcquota.class);
		c.add(Restrictions.eq("loginname", loginname));
		c.add(Restrictions.eq("status", 1));
		c.addOrder(Order.desc("createtime"));
		List<Mcquota> mcquotaList = this.findByCriteria(c);
		if(mcquotaList != null && mcquotaList.size()>0){
			return mcquotaList.get(0).getAmount();
		}
		return 0.0;
	}
	
	//获取额度小数 唯一
	public   Double amountNumber(String amount,String loginname){
		Double amounts = null;
		try {
			if(amount.contains(".")){
				amount = amount.substring(0, amount.indexOf("."));
			}
			//如果不够再获取整数
	        for(int j=(Integer.parseInt(amount)); j<(Integer.parseInt(amount)+9);j++){
	        	for(int i=1;i<99;i++){
	    			amounts = Double.parseDouble(j+"."+i);
	    			if(this.getIdQuota(amounts))//如果没有
	    			{
	    				//将生成的额度记录存入到数据表
	    				 Mcquota quota = new Mcquota();
			 	         quota.setAmount(amounts);
			 	         quota.setStatus(1);
			 	         quota.setLoginname(loginname);
			 	         quota.setCreatetime(new Date());
			 	         this.save(quota);
			 	         return amounts;
	    			}
	    		}
	        } 			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amounts;
	}
	
	
	//查询是否存在该额度
	private  boolean getIdQuota(Double amounts){
		DetachedCriteria c = DetachedCriteria.forClass(Mcquota.class);
		c.add(Restrictions.eq("amount", amounts));
		c.add(Restrictions.eq("status", 1));
		List<Mcquota> mcquotaList = this.findByCriteria(c);
		if(mcquotaList != null && mcquotaList.size()>0){
			return false;
		}
		else {
			return true;
		}
	}
	
	//废除额度
	private  boolean repealQuota(String loginname){
		DetachedCriteria c = DetachedCriteria.forClass(Mcquota.class);
		c.add(Restrictions.eq("loginname", loginname));
		c.add(Restrictions.eq("status", 1));
		List<Mcquota> mcquotaList = this.findByCriteria(c);
		if(mcquotaList != null && mcquotaList.size()>0){
			//根据额度查询是否存在订单 如果不存在就释放额度
			for(int i=0;i<mcquotaList.size();i++){
				Mcquota mcquota  = mcquotaList.get(i);
				DetachedCriteria dd = DetachedCriteria.forClass(DepositOrder.class);
				dd = dd.add(Restrictions.eq("loginname", loginname));
				dd = dd.add(Restrictions.eq("amount", mcquota.getAmount())); 
				List<DepositOrder> listd = this.findByCriteria(dd);
				if(listd != null && listd.size()>0){
					mcquota.setStatus(0);//是否掉額度
					this.update(mcquota);
				}
			}
			return false;
		}
		else {
			return true;
		}
	}
	
	
	

	
	//获取额度小数 唯一
	public   Double amountNumber_bak(String amount,String loginname){
		if(amount.contains(".")){
			amount = amount.substring(0, amount.indexOf("."));
		}
		int max=90;
        int min=10;
        Random random = new Random();
        int s = 0;
        Double amounts = null;
        
        boolean flag = false;
        while(!flag) {
          //s = random.nextInt(max-min+1) + min;
          s = RandomUtils.nextInt(max-min+1)+min;
   	      amounts = Double.parseDouble(amount+"."+s);
   	      if(this.getIdQuota(amounts)){
   	    	System.out.println("======"+loginname+"---"+amounts+"==不存在保存");
     		//将生成的额度记录存入到数据表
 	        Mcquota quota = new Mcquota();
 	        quota.setAmount(amounts);
 	        quota.setStatus(1);
 	        quota.setLoginname(loginname);
 	        quota.setCreatetime(new Date());
 	        this.save(quota);
 	        flag = true;
 	        break;
	      }
	      else {
	    	System.out.println("======"+loginname+"---"+amounts+"==存在 继续循环！");
	     	flag = false;
	      }
	    }
        return amounts;
	}
	
	
	
	
	
	
	/****
	 * 微信秒存 v2
	 */
	
	@Override
	public Bankinfo createWeiXindeposit(String loginname, Double amout) {
		String depositId;
		Bankinfo bank = new Bankinfo();
		String bankname ="微信";
		Users user = (Users) this.get(Users.class, loginname);
		try {
			DetachedCriteria dc4 = DetachedCriteria.forClass(Bankinfo.class);
			dc4 = dc4.add(Restrictions.eq("type", 1));
			dc4 = dc4.add(Restrictions.eq("isshow", 1));
			dc4 = dc4.add(Restrictions.eq("useable", 0));
			dc4 = dc4.add(Restrictions.eq("vpnname", "A"));
			dc4 = dc4.add(Restrictions.eq("bankname", "微信"));
			dc4 = dc4.add(Restrictions.like("userrole", "%"+user.getLevel()+"%"));
			dc4.add(Restrictions.sqlRestriction("1=1 order by rand()"));
			System.out.println("dc4:"+dc4);
			List<Bankinfo> list4 = this.findByCriteria(dc4);
			if(list4!=null&&list4.size()>0&&list4.get(0)!=null){
				bank = list4.get(0);
			}else{
				bank.setMassage("微信存款已关闭 ");
				return bank ;
			}
			DepositOrder depositOrder = new DepositOrder();
			depositOrder.setLoginname(loginname);
			depositOrder.setBankname(bankname);
			depositId = StringUtil.getRandomStrExceptOL0(6);        
			depositOrder.setDepositId(depositId);
			depositOrder.setCreatetime(new Date());
			depositOrder.setStatus(0);
			depositOrder.setAccountname(bank.getUsername());
			depositOrder.setFlag(1);
			depositOrder.setType("3");  //微信     
			depositOrder.setAmount(amout);		
			this.save(depositOrder);
			bank.setDepositId(depositId);
			return bank;
			
		} catch (Exception e1) {
			e1.printStackTrace();
			bank = new Bankinfo();
			bank.setMassage("系统异常");
			return bank;
		}
	}
	
	public static final String[] levels = {"A","B","C","D","E","F","G","H","I","J","K","L","M"};

	/***
	 * 判断金额是否存在
	 */
	public Integer getUnDepositOrderCount(DepositOrder depositOrder) {
		return transferDao.getUnDepositOrderCount(depositOrder);
	}
	
	/**
	 * 保存订单，确保订单金额唯一
	 * @param payOrder
	 * @return
	 */
	public Integer saveDepositOrder(DepositOrder depositOrder){
		   Integer numSize=0; 
			try {
				String sql = "INSERT into deposit_order(loginname, bankname,depositId, createtime, updatetime,status,accountname,flag, type,amount) SELECT '"+depositOrder.getLoginname()+"', '"+depositOrder.getBankname()+"','"+depositOrder.getDepositId()+"', SYSDATE(),null, 0, '"+depositOrder.getAccountname()+"',1,'3',"+depositOrder.getAmount()+" from dual where not exists (select 1 from deposit_order where amount = "+depositOrder.getAmount()+" and status = 0 and type='3')";
				numSize = transferDao.saveDepositPayOrder(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return numSize;
	}
	/**
	 * 自助红包金额转入游戏ttg
	 */
	@Override
	public String selfConpon4TTG4HB(String transID, String loginname, Double remit, String remark,Integer isdeposit) {
		log.info("开始转入TTG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账TTG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("TTG转账正在维护" + loginname);
			return "TTG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的TTG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "ttg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "ttg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);

		DetachedCriteria dc = DetachedCriteria.forClass(HBbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("phone", isdeposit));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		HBbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(HBbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_HB.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=Arith.sub(localCredit, remit);
				sa.setMoney(localCredit);
				sa.setUpdatetime(new Date());
				HBrecord fb = new HBrecord();
				fb.setUpdatetime(new Date());
				fb.setUsername(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				fb.setDeposit(isdeposit);
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_HB.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}

	@Override
	public String transfer4Pt4HB(String transID, String loginname, Double remit,String remark,Integer isdeposit) {
		String result = null;
		boolean creditReduce = false;
		log.info("begin to transfer4Pt4HB:" + loginname);
		Users customer = (Users) get(Users.class, loginname);
		if(customer==null){
			return "玩家账号不存在！";
		}
		if(customer.getFlag()==1){
			return "该账号已经禁用";
		}

		DetachedCriteria dc = DetachedCriteria.forClass(HBbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("phone", isdeposit));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		HBbonus fb=null;
		if(null!=list&&list.size()>0){
			fb=(HBbonus) list.get(0);
			localCredit= fb.getMoney();
			oldCredit=fb.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		if (localCredit < remit) {
			result = "转账失败，奖金额度不足";
			log.info("current credit:" + localCredit);
			log.info(result);
			return result ;
		}
		try {
			remit = Math.abs(remit);
			String couponString = null ;
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				DetachedCriteria dc1 = DetachedCriteria.forClass(Proposal.class);
				dc1 = dc1.add(Restrictions.eq("shippingCode", customer.getShippingcodePt()));
				dc1 = dc1.add(Restrictions.eq("flag", 2));
				dc1 = dc1.add(Restrictions.in("type", new Object[] { 571, 572, 573,574 ,575 ,590 , 591 ,705,409,410,411,412,419,390,391,392,499}));
				List<Proposal> list1 = findByCriteria(dc1);
				if (list1 == null || list1.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list1.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				if(((new Date()).getTime() - proposal.getExecuteTime().getTime())<6*60*1000){
					return "自助优惠5分钟内不允许转账！";
				}
				Integer type = proposal.getType();
				couponString = ProposalType.getText(type);
				if (type == 571) {
					couponString = "PT红包优惠券";
				}
				// 要达到的总投注额
				Double amountAll = (proposal.getAmount() + proposal.getGifTamount()) * Integer.parseInt(proposal.getBetMultiples());

				// 获取数据总投注额
				Double validBetAmount = getSelfYouHuiBet("pttiger", loginname, proposal.getPno()) ;

				/*****************************************/
				//后期远程金额
				Double remoteCredit=0.00;
				try {
					remoteCredit = PtUtil.getPlayerMoney(loginname);
				} catch (Exception e) {
					return "获取异常失败";
				}
				validBetAmount = validBetAmount==null?0.0:validBetAmount;
				//判断是否达到条件
				log.info(loginname+"->自助优惠解除限制参数validBetAmount："+validBetAmount+"amountAll:"+amountAll+"remoteCredit:"+remoteCredit);
				if(validBetAmount != -1.0){
					if ((validBetAmount >= amountAll ||  remoteCredit < 5)) {
						log.info("解除成功");
					}else{
						PreferTransferRecord transferRecord = new PreferTransferRecord(loginname,"pttiger" ,validBetAmount, (amountAll-validBetAmount) , new Date() , null , "IN");
						transferDao.save(transferRecord) ;
						log.info("解除失败");
						return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + "或者PT游戏金额低于5元,方能进行户内转账！(还差"+(amountAll-validBetAmount)+",或稍后5分钟 系统更新记录再来查询)";
					}
				}else{
					log.info(loginname+"通过后台控制进行转账");
				}
			}

			//将已领取的负盈利反赠更新为已处理
			DetachedCriteria c=DetachedCriteria.forClass(LosePromo.class);
			c.add(Restrictions.eq("username", loginname));
			c.add(Restrictions.eq("platform", "pttiger"));
			c.add(Restrictions.eq("status", "1"));
			List<LosePromo> losePromoList = findByCriteria(c);
			for (LosePromo losePromo : losePromoList) {
				losePromo.setStatus("2");
				update(losePromo);
			}
			// 检查额度
			transferDao.addTransferforNewPt(Long.parseLong(transID), loginname, localCredit, remit, Constants.IN, Constants.FAIL, "红包账户金额转至PT", "转入成功");
			fb.setMoney(Arith.sub(localCredit, remit));
			fb.setUpdatetime(new Date());
			HBrecord fr = new HBrecord();
			fr.setUpdatetime(new Date());
			fr.setUsername(loginname);
			fr.setMoney(remit);
			fr.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+Arith.sub(localCredit, remit));
			fr.setType("2");//支出
			fr.setDeposit(isdeposit);
			this.save(fr);
			update(fb);


			logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_HB.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+Arith.sub(localCredit, remit)));
			// 标记额度已被扣除
			creditReduce = true;
			if (customer.getShippingcodePt() == null || customer.getShippingcodePt().equals("")) {

			} else {
				customer.setShippingcodePt(null);
				update(customer);
			}
		} catch (Exception e) {
			// 当额度被扣除时，发生异常，提示客人,额度未扣除时，告知客人重新尝试。
			if (creditReduce){
				result = "转账发生异常 :" + e.getMessage();
			}else{
				result = "系统繁忙，请重新尝试";
			}
		}
		return result;
	}

	/**
	 * 自助红包金额转入游戏qt
	 */
	@Override
	public String selfConpon4Qt4HB(String transID, String loginname, Double remit, String remark,Integer isdeposit) {
		log.info("开始转入QT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账QT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("QT转账正在维护" + loginname);
			return "QT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的QT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "qt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "qt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);

		DetachedCriteria dc = DetachedCriteria.forClass(HBbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("phone", isdeposit));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		Double oldCredit  =0.0;
		HBbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(HBbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_HB.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=Arith.sub(localCredit, remit);
				sa.setMoney(localCredit);
				sa.setUpdatetime(new Date());
				HBrecord fb = new HBrecord();
				fb.setUpdatetime(new Date());
				fb.setUsername(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				fb.setDeposit(isdeposit);
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_HB.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}

	/**
	 * 自助红包金额转入游戏MG
	 */
	@Override
	public String selfConpon4MG4HB(String transID, String loginname, Double remit, String remark,Integer isdeposit) {
		log.info("开始转入MG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("MG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("MG转账正在维护" + loginname);
			return "MG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的MG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "mg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "mg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);

		DetachedCriteria dc = DetachedCriteria.forClass(HBbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("phone", isdeposit));
		List list =findByCriteria(dc);
		Double localCredit = 0.0;
		Double oldCredit = 0.0;
		HBbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(HBbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_HB.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=Arith.sub(localCredit, remit);
				sa.setMoney(localCredit);
				sa.setUpdatetime(new Date());
				HBrecord fb = new HBrecord();
				fb.setUpdatetime(new Date());
				fb.setUsername(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				fb.setDeposit(isdeposit);
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_HB.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}

	/**
	 * 自助红包金额转入游戏DT
	 */
	@Override
	public String selfConpon4DT4HB(String transID, String loginname, Double remit, String remark,Integer isdeposit) {
		log.info("开始转入DT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账DT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("DT转账正在维护" + loginname);
			return "DT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的DT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "dt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "dt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);

		DetachedCriteria dc = DetachedCriteria.forClass(HBbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("phone", isdeposit));
		List list =findByCriteria(dc);
		Double localCredit = 0.0;
		Double oldCredit = 0.0;
		HBbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(HBbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_HB.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=Arith.sub(localCredit, remit);
				sa.setMoney(localCredit);
				sa.setUpdatetime(new Date());
				HBrecord fb = new HBrecord();
				fb.setUpdatetime(new Date());
				fb.setUsername(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				fb.setDeposit(isdeposit);
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_HB.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}

	@Override
	public String selfConpon4Slot4HB(String transID, String loginname, Double remit, String remark,Integer isdeposit) {
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账SLOT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			return "老虎机平台转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的DT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "slot"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "slot"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);

		DetachedCriteria dc = DetachedCriteria.forClass(HBbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("phone", isdeposit));
		List list =findByCriteria(dc);
		Double localCredit = 0.0;
		Double oldCredit = 0.0;
		HBbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(HBbonus) list.get(0);
			localCredit= sa.getMoney();
			oldCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_HB.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				localCredit=Arith.sub(localCredit, remit);
				sa.setMoney(localCredit);
				sa.setUpdatetime(new Date());
				HBrecord fb = new HBrecord();
				fb.setUpdatetime(new Date());
				fb.setUsername(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				fb.setDeposit(isdeposit);
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_HB.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}


	/**
	 * 自助红包金额转入游戏nt
	 */
	@Override
	public String selfConponNT4HB(String transID, String loginname, Double remit, String remark,Integer isdeposit) {
		log.info("开始转入NT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账NT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("NT转账正在维护" + loginname);
			return "NT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的nt存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "nt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "nt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);

		DetachedCriteria dc = DetachedCriteria.forClass(HBbonus.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("phone", isdeposit));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		HBbonus sa=null;
		if(null!=list&&list.size()>0){
			sa=(HBbonus) list.get(0);
			localCredit= sa.getMoney();
		}else{
			return "没有奖金信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_HB.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				Double oldCredit=localCredit;
				localCredit=Arith.sub(localCredit, remit);
				sa.setMoney(localCredit);
				sa.setUpdatetime(new Date());
				HBrecord fb = new HBrecord();
				fb.setUpdatetime(new Date());
				fb.setUsername(loginname);
				fb.setMoney(remit);
				fb.setRemark(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit);
				fb.setType("2");//支出
				fb.setDeposit(isdeposit);
				this.save(fb);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_HB.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark+"改变前奖金池为："+oldCredit+"改变后奖金池额度为："+localCredit));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}

	@Override
	public String transferOutforTy(String transID, String loginname,
			Double remit) {
		String result = null;
		log.info("begin to transferOutforSB:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname);
			if (customer.getFlag() == 1) {
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();

			String sbloginname = "dream_" + loginname;
			Sbcoupon sbcoupon = userDao.getSbcoupon(sbloginname);
			//玩家使用了优惠
			if (sbcoupon != null) {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", sbcoupon.getShippingcode()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 581, 582, 584 }));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				if (type == 581) {
					couponString = "188体育红包优惠券";
				} else if (type == 582) {
					couponString = "188体育存2900送580";
				} else if (type == 584) {
					couponString = "188体育20%存送优惠劵";
				} else {
					return "无此类型优惠";
				}
				// 要达到的总投注额
				Double amountAll = ( proposal.getAmount() + proposal.getGifTamount() ) * Integer.parseInt(proposal.getBetMultiples());
				//获取投注额
				Calendar date = Calendar.getInstance();
				date.setTime(proposal.getExecuteTime());
				date.add(Calendar.HOUR_OF_DAY, -12);
				Date startDate = date.getTime();
				Double validBetAmount = userDao.getTurnOverRequest(sbloginname, startDate, new Date());
				//获取体育远程额度
				if (validBetAmount < amountAll) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + ",方能进行户内转账！";
				}
			}

			DspResponseBean dspPrepareResponseBean = null;
			try {
				dspPrepareResponseBean = RemoteCaller.withdrawSBRequest(loginname, remit, transID);
			} catch (Exception e) {
				log.warn("withdrawPrepareDspRequest try again!");
				dspPrepareResponseBean = RemoteCaller.getTransferStatusSBRequest(transID);
			}
			if (dspPrepareResponseBean != null && "000".equals(dspPrepareResponseBean.getInfo())) {
				transferDao.addTransferforSB(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_SBOUT.getCode(), transID, null);
				// tradeDao.changeCredit(loginname, remit,
				// CreditChangeType.TRANSFER_OUT.getCode(), transID, null);
				//取消优惠
				userDao.updateSbcoupon(sbloginname);
			} else if (dspPrepareResponseBean != null) {
				result = "转账状态错误:" + dspPrepareResponseBean.getInfo();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
		}
		return result;
		
	}

	@Override
	public String transferInforTy(String transID, String loginname, Double remit) {
		log.info("开始转入SB准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账SB");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("SB转账正在维护" + loginname);
			return "SB转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			String sbloginname = "dream_" + loginname;
			Sbcoupon sbcoupon = userDao.getSbcoupon(sbloginname);
			//玩家使用了优惠
			if (sbcoupon != null) {
				DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
				dc = dc.add(Restrictions.eq("shippingCode", sbcoupon.getShippingcode()));
				dc = dc.add(Restrictions.eq("flag", 2));
				dc = dc.add(Restrictions.in("type", new Object[] { 581, 582, 584 }));
				List<Proposal> list = findByCriteria(dc);
				if (list == null || list.size() <= 0) {
					return "你的优惠码出现错误！";
				}
				Proposal proposal = list.get(0);
				if (proposal.getExecuteTime() == null) {
					return "执行时间出现问题！";
				}
				String couponString = "";
				Integer type = proposal.getType();
				if (type == 581) {
					couponString = "188体育红包优惠券";
				} else if (type == 582) {
					couponString = "188体育存2900送580";
				} else if (type == 584) {
					couponString = "188体育20%存送优惠劵";
				} else {
					return "无此类型优惠";
				}
				// 要达到的总投注额
				Double amountAll = ( proposal.getAmount() + proposal.getGifTamount() ) * Integer.parseInt(proposal.getBetMultiples());
				//获取投注额
				Calendar date = Calendar.getInstance();
				date.setTime(proposal.getExecuteTime());
				date.add(Calendar.HOUR_OF_DAY, -12);
				Date startDate = date.getTime();
				Double validBetAmount = userDao.getTurnOverRequest(sbloginname, startDate, new Date());
				//获取体育远程额度
				if (validBetAmount < amountAll) {
					return couponString + "：目前投注总额是" + validBetAmount + "需达到" + proposal.getBetMultiples() + "倍投注总额" + amountAll + ",方能进行户内转账！";
				}
			}
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_SBIN.getCode(), transID, null);
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		
	}

	@Override
	public String doRechargeRecord(String loginname) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(SignRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("isdelete", "0"));//未删除
		dc.add(Restrictions.ge("createtime", DateUtil.getToday()));
		dc.add(Restrictions.eq("type", "1"));//存款奖金
		List<SignRecord> list = findByCriteria(dc);
		if(null!=list&&list.size()>0){
			return "今日已领取存款奖金，不能重复操作";
		}else{
			DetachedCriteria dc3 = DetachedCriteria.forClass(SignAmount.class);
			dc3.add(Restrictions.eq("username", loginname));
			List<SignAmount> list3 = findByCriteria(dc3);
			Double oldAmount =0.0;
			if(null!=list3&&list3.size()>0){
				 oldAmount=list3.get(0).getAmountbalane();
			}
			//查询今日存款量
			String result=checkDepositRecord(loginname,DateUtil.getbeforeDay(1),DateUtil.getbeforeDay(0));
			if(StringUtil.isEmpty(result)){
				return "系统繁忙，请稍后再试";
			}
			if(Double.parseDouble(result)<500){
				return "昨日存款不满足要求，不能领取奖金，还需存款至少："+(500-Double.parseDouble(result))+"元";
			}
			Double amount=0.0;
			Double sumAmount=Double.parseDouble(result);
			if(sumAmount>=200000){
				amount=288.0;
			}else if(sumAmount>=100000){
				amount=108.0;
			}else if(sumAmount>=30000){
				amount=25.0;
			}else if(sumAmount>=10000){
				amount=16.0;
			}else if(sumAmount>=5000){
				amount=9.0;
			}else if(sumAmount>=2000){
				amount=4.0;
			}else if(sumAmount>=500){
				amount=1.0;
			}
		
			SignRecord sr = new SignRecord();
			sr.setUsername(loginname);
			sr.setCreatetime(new Date());
			sr.setIsused("0");
			sr.setIsdelete("0");
			sr.setType("1");//存款奖金
			sr.setAmount(amount);
			//更新奖金余额
			DetachedCriteria dc2 = DetachedCriteria.forClass(SignAmount.class);
			dc2.add(Restrictions.eq("username", loginname));
			List<SignAmount> list2 = findByCriteria(dc2);
			SignAmount sa=null;
			if(null!=list2&&list2.size()>0){
				 sa = list2.get(0);
				 amount=amount+sa.getAmountbalane();
			}else{
				 sa = new SignAmount();
				 sa.setUsername(loginname);
			}
			sr.setRemark("玩家昨日存款："+sumAmount+"-->改变前额度："+oldAmount+",改变后额度："+amount+",增加额度："+sr.getAmount());
			save(sr);
			sa.setAmountbalane(amount);
			sa.setUpdatetime(new Date());
			saveOrUpdate(sa);
			if(oldAmount!=(sa.getAmountbalane()-sr.getAmount())){
				log.error("存款奖金领取异常，用户："+loginname);
				throw new Exception("网络异常,请重新操作");
			}
			return "存款奖金领取成功："+sr.getAmount()+"元";
		}
	}

	@Override
	public String doStreamRecord(String loginname) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(SignRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("isdelete", "0"));//未删除
		dc.add(Restrictions.ge("createtime", DateUtil.getToday()));
		dc.add(Restrictions.eq("type", "2"));//流水奖金
		List<SignRecord> list = findByCriteria(dc);
		if(null!=list&&list.size()>0){
			return "今日已领取流水奖金，不能重复操作";
		}else{
			DetachedCriteria dc3 = DetachedCriteria.forClass(SignAmount.class);
			dc3.add(Restrictions.eq("username", loginname));
			List<SignAmount> list3 = findByCriteria(dc3);
			Double oldAmount =0.0;
			if(null!=list3&&list3.size()>0){
				 oldAmount=list3.get(0).getAmountbalane();
			}
			//查询昨日老虎机流水量 创建时间是今天
			Double result=checkSlotStreamRecord(loginname,DateUtil.getbeforeDay(0),DateUtil.getbeforeDay(-1));
			if(result==0){
				return "昨日流水不满足要求，或系统正在结算中，请稍后再试，还需流水至少："+(10000-result)+"元";
			}else if(result<10000){
				return "昨日流水不满足要求，或系统正在结算中，请稍后再试，还需流水至少："+(10000-result)+"元";
			}
			Double amount=0.0;
			if(result>=500000){
				amount=62.0;
			}else if(result>=300000){
				amount=38.0;
			}else if(result>=200000){
				amount=21.0;
			}else if(result>=100000){
				amount=10.0;
			}else if(result>=30000){
				amount=4.0;
			}else if(result>=10000){
				amount=1.0;
			}
			SignRecord sr = new SignRecord();
			sr.setUsername(loginname);
			sr.setCreatetime(new Date());
			sr.setIsused("0");
			sr.setIsdelete("0");
			sr.setType("2");//流水奖金
			sr.setAmount(amount);
			//更新奖金余额
			DetachedCriteria dc2 = DetachedCriteria.forClass(SignAmount.class);
			dc2.add(Restrictions.eq("username", loginname));
			List<SignAmount> list2 = findByCriteria(dc2);
			SignAmount sa=null;
			if(null!=list2&&list2.size()>0){
				 sa = list2.get(0);
				 amount=amount+sa.getAmountbalane();
			}else{
				 sa = new SignAmount();
				 sa.setUsername(loginname);
			}
			sr.setRemark("玩家昨日流水："+result+"-->改变前额度："+oldAmount+",改变后额度："+amount+",增加额度："+sr.getAmount()+DateUtil.getbeforeDay(0)+"||"+DateUtil.getbeforeDay(-1));
			save(sr);
			sa.setAmountbalane(amount);
			sa.setUpdatetime(new Date());
			if(oldAmount!=(sa.getAmountbalane()-sr.getAmount())){
				log.error("流水奖金领取异常，用户："+loginname);
				throw new Exception("网络异常,请重新操作");
			}
			saveOrUpdate(sa);
			return "流水奖金领取成功:"+sr.getAmount()+"元";
		}
	}
	
	@Override
	public Transfer addTransferforSba(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		return transferDao.addTransferforSba( transID,  loginname,  localCredit,  remit,  in,  flag,  paymentid,  remark);
	}
	
	public String transferInforSbaTyIn(String transID, String loginname, Double remit) {
		log.info("开始转入沙巴准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账SBA");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("沙巴转账正在维护" + loginname);
			return "沙巴转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}
		remit = Math.abs(remit);
		Double localCredit = customer.getCredit();
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，额度不足" + loginname);
				return "转账失败，额度不足";
			} else {
				tradeDao.changeCreditIn(customer, remit * -1, CreditChangeType.TRANSFER_SBAIN.getCode(), transID, null);
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}	
	
	
	@Override
	public String transferOutforSbaTy(String transID, String loginname, Double remit) {
		String result = null;
		log.info("begin to transferOutforSB:" + loginname);
		try {
			Users customer = (Users) get(Users.class, loginname);
			if(customer.getFlag()==1){
				return "该账号已经禁用";
			}
			Double localCredit = customer.getCredit();
			Boolean res = ShaBaUtils.FundTransfer(transID,loginname, remit,Constants.WITHDRAW);
			if (res) {
				transferDao.addTransferforSba(Long.parseLong(transID), loginname, localCredit, remit, Constants.OUT, Constants.FAIL, "", "转入成功");
				tradeDao.changeCredit(loginname, remit, CreditChangeType.TRANSFER_SBAOUT.getCode(), transID, null);
			} else {
				result = "转账异常请联系客服解决";
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = "转账发生异常: " + e.getMessage();
			throw new GenericDfhRuntimeException("转账发生异常，数据回滚");
		}
		return result;
	}
	/**
	 * 检查该玩家某个时间段的老虎机流水量
	 * @param loginname
	 * @return
	 */
	public Double checkSlotStreamRecord(String loginname,String begindateStr,String enddateStr){
		Date begindate=null;
		Date enddate=null;
		begindate=DateUtil.parseDateForYYYYMMDD(begindateStr);
		if(!StringUtil.isEmpty(enddateStr)){
			enddate=DateUtil.parseDateForYYYYMMDD(enddateStr);
		}
		Double sumMoney=0.0;
		DetachedCriteria dc1 = DetachedCriteria.forClass(AgProfit.class);
		dc1.add(Restrictions.eq("loginname", loginname));
		dc1.add(Restrictions.ge("createTime",begindate));
		dc1.add(Restrictions.in("platform", new String[]{"ttg","newpt","nt","qt","dt","mg"}));
		//if(!StringUtil.isEmpty(enddateStr)){
			dc1.add(Restrictions.le("createTime",enddate));
		//}
		dc1.setProjection(Projections.sum("bettotal"));
		List list1 = findByCriteria(dc1);
		Double result1= 0.0;
		result1 =(Double)list1.get(0);
		if(null==result1){
			result1=0.0;
		}
		return result1;
	}
	
	
	/**
	 * 查询最近几天的转账记录
	 * @param loginname
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	public List<Transfer> findTransferList(String loginname, Date startDate) {
		List<Transfer> transferList = null;
		try {
			transferList = transferDao.findTransferList(loginname, startDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transferList;
	}

	//生日礼金转入主账户
	public String transferBirthdayToMain(Users users, Double amout) {
		String pno = seqService.generateProposalPno(ProposalType.BIRTHDAY);
		//为了防止恶意新增 只要存在一条数据则不再新增 去掉 多次同步线程解放线程问题
		DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
		dc.add(Restrictions.eq("loginname", users.getLoginname()));
		dc.add(Restrictions.eq("type", ProposalType.BIRTHDAY.getCode()));
		List<Proposal> list = findByCriteria(dc);
		if(!list.isEmpty()){
			boolean bool = true;
			for (Proposal proposal : list) {
				//如果当期年份存在表示领取过
				if(DateUtil.getYear(new Date()).equals(DateUtil.getYear(proposal.getCreateTime()))){
					bool = false;
					break;
				}
			}
			if(!bool){
				return "您已领取生日礼金,请明年生日再来领取！";
			}
		}
		Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.BIRTHDAY.getCode(),
				users.getLevel(), users.getLoginname(), amout.doubleValue(), users.getAgent(), ProposalFlagType.EXCUTED.getCode(),
				Constants.FROM_FRONT, "生日礼金", null);
		proposal.setBetMultiples("0");
		proposal.setExecuteTime(new Date());
		transferDao.save(proposal);
		tradeDao.changeCreditForOnline(users.getLoginname(),amout.doubleValue(),CreditChangeType.TRANSFER_BIRTHDAY.getCode().toString(),pno,"生日礼金->主账户");
		return "恭喜您！成功领取"+amout+"元生日礼金,请至主帐户查询！";

	}
	
	@Override
	public String transferSelfToRedRain(String transID, Users users, Double amount, String youhuiType, String remark) {
		try {
			tradeDao.changeCreditIn(users, amount * -1, CreditChangeType.TRANSFER_REDRAINOUT.getCode(), transID, remark);
			return null;
		}catch (Exception e){
			e.printStackTrace();
			return "ERRO";
		}
	}
	
	/**
	 * 红包雨转入MG账户
	 */
	@Override
	public String selfConpon4MG4RedRain(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入MG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账MG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("MG转账正在维护" + loginname);
			return "MG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的MG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "mg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "mg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		DetachedCriteria dc = DetachedCriteria.forClass(RedRainWallet.class);
		dc.add(Restrictions.eq("loginname", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		RedRainWallet sa=null;
		if(null!=list&&list.size()>0){
			sa=(RedRainWallet) list.get(0);
			localCredit= sa.getAmout();
		}else{
			return "没有账户信息，无法转入";
		}

		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_REDRAIN_OUT.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				DecimalFormat df = new DecimalFormat(".00");
				double v = localCredit - remit;
				v=Double.valueOf(df.format(v));
				String s="红包雨余额转入MG "+localCredit+"->"+v;
				localCredit=localCredit-remit;
				localCredit=Double.valueOf(df.format(localCredit));
				sa.setAmout(localCredit);
				sa.setRemark(s);
				this.saveOrUpdate(sa);
				Activity activity = new Activity();
				activity.setActivityName("红包雨活动");
				activity.setUserrole(loginname);
				activity.setCreateDate(new Date());
				activity.setRemark(s);
				transferDao.save(activity);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_REDRAIN_OUT.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	@Override
	public String selfConpon4DT4RedRain(String transID, String loginname, Double remit, String youhuiType, String remark) {
		log.info("开始转入DT准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账DT");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("DT转账正在维护" + loginname);
			return "DT转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的DT存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "dt"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "dt"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);

		DetachedCriteria dc = DetachedCriteria.forClass(RedRainWallet.class);
		dc.add(Restrictions.eq("loginname", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		RedRainWallet sa=null;
		if(null!=list&&list.size()>0){
			sa=(RedRainWallet) list.get(0);
			localCredit= sa.getAmout();
		}else{
			return "没有账户信息，无法转入";
		}
		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_REDRAIN_OUT.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				String s="红包雨余额转入QT "+localCredit+"->"+(localCredit-remit);
				localCredit=localCredit-remit;
				sa.setAmout(localCredit);
				sa.setRemark(s);
				Activity activity = new Activity();
				activity.setActivityName("红包雨活动");
				activity.setUserrole(loginname);
				activity.setCreateDate(new Date());
				activity.setRemark(s);
				this.save(activity);
				this.saveOrUpdate(sa);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_REDRAIN_OUT.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}

	@Override
	public String selfConpon4CQ9RedRain(String transID, String loginname, Double remit, String text, String remark) {
		log.info("开始转入CQ9准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账CQ9");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("CQ9转账正在维护" + loginname);
			return "CQ9转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的MG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "cq9"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "cq9"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		DetachedCriteria dc = DetachedCriteria.forClass(RedRainWallet.class);
		dc.add(Restrictions.eq("loginname", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		RedRainWallet sa=null;
		if(null!=list&&list.size()>0){
			sa=(RedRainWallet) list.get(0);
			localCredit= sa.getAmout();
		}else{
			return "没有账户信息，无法转入";
		}

		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_REDRAIN_OUT.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				DecimalFormat df = new DecimalFormat(".00");
				double v = localCredit - remit;
				v=Double.valueOf(df.format(v));
				String s="红包雨余额转入CQ9 "+localCredit+"->"+v;
				localCredit=localCredit-remit;
				localCredit=Double.valueOf(df.format(localCredit));
				sa.setAmout(localCredit);
				sa.setRemark(s);
				this.saveOrUpdate(sa);
				Activity activity = new Activity();
				activity.setActivityName("红包雨活动");
				activity.setUserrole(loginname);
				activity.setCreateDate(new Date());
				activity.setRemark(s);
				transferDao.save(activity);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_REDRAIN_OUT.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
	
	@Override
	public String selfConpon4PGRedRain(String transID, String loginname, Double remit, String text, String remark) {
		log.info("开始转入PG准备工作:" + loginname);
		// 判断转账是否关闭
		Const constPt = transferDao.getConsts("转账PG");
		if (constPt == null) {
			log.info("平台不存在" + loginname);
			return "平台不存在";
		}
		if (constPt.getValue().equals("0")) {
			log.info("PG转账正在维护" + loginname);
			return "PG转账正在维护";
		}
		// 操作业务
		Users customer = transferDao.getUsers(loginname);
		if (customer.getFlag() == 1) {
			log.info("该账号已经禁用" + loginname);
			return "该账号已经禁用";
		}

		// 将之前使用的MG存送优惠，修改为已达到流水要求
		DetachedCriteria selfDc = DetachedCriteria.forClass(SelfRecord.class);
		selfDc.add(Restrictions.eq("loginname", loginname));
		selfDc.add(Restrictions.eq("platform", "pg"));
		selfDc.add(Restrictions.eq("type", 0));
		selfDc.addOrder(Order.desc("createtime"));
		List<SelfRecord> selfs = this.findByCriteria(selfDc);
		for (SelfRecord selfRecord : selfs) {
			// 更新为流水已达到
			selfRecord.setType(1);
			selfRecord.setUpdatetime(new Date());
			update(selfRecord);
		}

		// 将已领取的负盈利反赠更新为已处理
		DetachedCriteria c = DetachedCriteria.forClass(LosePromo.class);
		c.add(Restrictions.eq("username", loginname));
		c.add(Restrictions.eq("platform", "pg"));
		c.add(Restrictions.eq("status", "1"));
		List<LosePromo> losePromoList = findByCriteria(c);
		for (LosePromo losePromo : losePromoList) {
			losePromo.setStatus("2");
			update(losePromo);
		}
		remit = Math.abs(remit);
		DetachedCriteria dc = DetachedCriteria.forClass(RedRainWallet.class);
		dc.add(Restrictions.eq("loginname", loginname));
		List list =findByCriteria(dc);
		Double localCredit  =0.0;
		RedRainWallet sa=null;
		if(null!=list&&list.size()>0){
			sa=(RedRainWallet) list.get(0);
			localCredit= sa.getAmout();
		}else{
			return "没有账户信息，无法转入";
		}

		try {
			// 检查额度
			log.info("current credit:" + localCredit);
			if (localCredit < remit) {
				log.info("转账失败，奖金额度不足" + loginname);
				return "转账失败，奖金额度不足";
			} else {
				if (StringUtils.isNotEmpty(transID)) {
					DetachedCriteria dc1 = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", CreditChangeType.TRANSFER_REDRAIN_OUT.getCode())).add(
							Restrictions.like("remark", "referenceNo:" + transID, MatchMode.START)).setProjection(Projections.rowCount());
					List list1 = findByCriteria(dc1);
					Integer rowCount = (Integer) list1.get(0);
					if (rowCount > 0) {
						String msg = "单号[" + transID + "]已转入过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				DecimalFormat df = new DecimalFormat(".00");
				double v = localCredit - remit;
				v=Double.valueOf(df.format(v));
				String s="红包雨余额转入PG "+localCredit+"->"+v;
				localCredit=localCredit-remit;
				localCredit=Double.valueOf(df.format(localCredit));
				sa.setAmout(localCredit);
				sa.setRemark(s);
				this.saveOrUpdate(sa);
				Activity activity = new Activity();
				activity.setActivityName("红包雨活动");
				activity.setUserrole(loginname);
				activity.setCreateDate(new Date());
				activity.setRemark(s);
				transferDao.save(activity);
				logDao.insertCreditLog(loginname, CreditChangeType.TRANSFER_REDRAIN_OUT.getCode(), customer.getCredit(), 0.0, customer.getCredit(), "referenceNo:" + transID + ";" + StringUtils.trimToEmpty(remark));
				return null;
			}
		} catch (Exception e) {
			throw new GenericDfhRuntimeException(e.getMessage());
		}
	}
}