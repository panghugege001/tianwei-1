package dfh.service.implementations;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.dao.LogDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Netpay;
import dfh.model.Payorder;
import dfh.model.Users;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.PayOrderFlagType;
import dfh.service.interfaces.NetpayService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class NetpayServiceImpl extends UniversalServiceImpl implements NetpayService {

	private static Logger log = Logger.getLogger(NetpayServiceImpl.class);
	private LogDao logDao;
	private TradeDao tradeDao;
	private UserDao userDao;

	public NetpayServiceImpl() {
	}

	public String addNetpayOrder(String billno, String loginname, String password, String netpayName, boolean newAccount, String partner, Double amount, String aliasName, String phone, String email,
			String attach, String ip) throws GenericDfhRuntimeException {
		log.info("start to add netpay order");
		String msg = null;
		Users user = (Users) get(Users.class, loginname);
		Payorder payOrder = null;
		// 如果是新开户
		if (newAccount) {
			if (user != null) {
				msg = "帐号已存在";
				return msg;
			}
			payOrder = new Payorder(billno, netpayName, amount, newAccount ? Constants.FLAG_TRUE : Constants.FLAG_FALSE, password, loginname, aliasName, phone, email, partner, attach, null, null,
					null, ip, DateUtil.now(), PayOrderFlagType.Init.getCode());
			// 如果是充值
		} else {
			if (user == null) {
				msg = "帐号不存在";
				return msg;
			}
			// if (user.getRole().equals(UserRole.FREE_CUSTOMER.getCode())) {
			// msg = "试玩会员不能在线支付";
			// return msg;
			// }
			payOrder = new Payorder(billno, netpayName, amount, newAccount ? Constants.FLAG_TRUE : Constants.FLAG_FALSE, password, loginname, user.getAliasName(), user.getPhone(), user.getEmail(),
					partner, attach, null, null, null, ip, DateUtil.now(), PayOrderFlagType.Init.getCode());
		}
		try {
			save(payOrder);
		} catch (Exception e) {
			e.printStackTrace();
			msg = e.getMessage();
		}
		return msg;
	}

	public String excuteNetpayOrder(String billno, Double amount, Boolean isSucc, String message, String md5Info, String ip) throws GenericDfhRuntimeException {
		log.info("start to excute netpay order");
		String msg = null;
		Payorder payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		if (!isSucc.booleanValue()) {
			payOrder.setFlag(PayOrderFlagType.FAIL.getCode());
			payOrder.setMsg(message);
			payOrder.setMd5info(md5Info);
			payOrder.setReturnTime(DateUtil.now());
			update(payOrder);
			return msg;
		}
		if (payOrder == null) {
			msg = "找不到在线支付单";
			return msg;
		}
		if (payOrder.getMoney().intValue() != amount.intValue()) {
			msg = "支付金额与交易金额不同";
			return msg;
		}
		if (payOrder.getFlag().intValue() != PayOrderFlagType.Init.getCode().intValue()) {
			msg = null;
			return msg;
		}
		try {

			tradeDao.changeCredit(payOrder.getLoginname(), Double.valueOf(Math.abs(amount.doubleValue())), CreditChangeType.NETPAY.getCode(), billno, message);
			payOrder.setFlag(PayOrderFlagType.SUCESS.getCode());
			payOrder.setMsg(message);
			payOrder.setMd5info(md5Info);
			payOrder.setReturnTime(DateUtil.now());
			update(payOrder);

			// 设置用户 isCashin字段
			userDao.setUserCashin(payOrder.getLoginname());

		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}

	public List getAllNetpay() {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Netpay.class).addOrder(Order.asc("next"));
		return findByCriteria(dCriteria);
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public Netpay getNetpayByMerno(String merno) {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Netpay.class).add(Restrictions.eq("merno", merno)).addOrder(Order.asc("next"));
		return (Netpay) findByCriteria(dCriteria).get(0);
	}

	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public String repairNetpayOrder(String operator, String billno, Double amount, String ip) throws GenericDfhRuntimeException {
		log.info("start to excute netpay order");
		String msg = null;
		String message = "后台补单";
		Payorder payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		if (payOrder == null) {
			msg = "找不到在线支付单";
			return msg;
		}
		if (payOrder.getMoney().intValue() != amount.intValue()) {
			msg = "支付金额与交易金额不同";
			return msg;
		}
		if (payOrder.getFlag().intValue() != PayOrderFlagType.Init.getCode().intValue()) {
			msg = null;
			return msg;
		}
		try {

			tradeDao.changeCredit(payOrder.getLoginname(), Double.valueOf(Math.abs(amount.doubleValue())), CreditChangeType.REPAIR_PAYORDER.getCode(), billno, message);
			payOrder.setFlag(PayOrderFlagType.SUCESS.getCode());
			payOrder.setMsg(message);
			payOrder.setMd5info(null);
			payOrder.setReturnTime(DateUtil.now());
			update(payOrder);
			logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, (new StringBuilder("单号:")).append(payOrder.getBillno()).toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		}
		return msg;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String updateNetpay(String billno, Boolean enabled, Integer next) throws GenericDfhRuntimeException {
		log.info("start to repair netpay order");
		String msg = null;
		Netpay netpay = (Netpay) get(Netpay.class, billno);
		if (netpay == null) {
			msg = "找不到在线支付单";
			return msg;
		} else {
			netpay.setFlag(enabled.booleanValue() ? Constants.FLAG_TRUE : Constants.FLAG_FALSE);
			netpay.setNext(next);
			update(netpay);
			return msg;
		}
	}
}
