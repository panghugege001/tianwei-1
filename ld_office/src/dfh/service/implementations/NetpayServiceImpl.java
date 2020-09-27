package dfh.service.implementations;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.dao.BankinfoDao;
import dfh.dao.LogDao;
import dfh.dao.SeqDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Creditlogs;
import dfh.model.DepositWechat;
import dfh.model.GameTransfers;
import dfh.model.Netpay;
import dfh.model.Payorder;
import dfh.model.QuotalRevision;
import dfh.model.Sbcoupon;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.Banktype;
import dfh.model.enums.CommonGfbEnum;
import dfh.model.enums.CommonZfEnum;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.PayOrderFlagType;
import dfh.model.enums.PayType;
import dfh.service.interfaces.NetpayService;
import dfh.utils.Arith;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class NetpayServiceImpl extends UniversalServiceImpl implements NetpayService {

	private static Logger log = Logger.getLogger(NetpayServiceImpl.class);
	private LogDao logDao;
	private TradeDao tradeDao;
	private UserDao userDao;
	private BankinfoDao bankinfoDao;
	private SeqDao seqDao;

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

	public BankinfoDao getBankinfoDao() {
		return bankinfoDao;
	}

	public void setBankinfoDao(BankinfoDao bankinfoDao) {
		this.bankinfoDao = bankinfoDao;
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

	@Override
	public String repairNetpayOrder(String operator, String billno,
			String payAmount, String ip, String payBillno, String payTime,
			String payPlatform,String loginname,String msg) {
		// TODO Auto-generated method stub
		Payorder payOrder=null;
		try {
			payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder!=null) {
			return "支付单号："+billno+" 已经存在，请重新核对";
		}
		
		msg=msg==null?"":msg;
		Object o=userDao.get(Users.class, loginname);
		if (o==null) {
			return "您输入的会员账号不存在，请重新输入";
		}
		Users users = (Users)o; 
		
		Payorder payorder=new Payorder();
		payorder.setBillno(billno);
		payorder.setPayPlatform(payPlatform);
		payorder.setFlag(PayOrderFlagType.SUCESS.getCode());
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(Double.valueOf(payAmount));
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setIp(ip);
		payorder.setCreateTime(DateUtil.now());
		payorder.setReturnTime(new java.sql.Timestamp(DateUtil.parseDateForStandard(payTime).getTime()));
		payorder.setMsg("后台补单 ;ips单号:"+payBillno+";"+msg);
		payorder.setType(PayType.Init.getCode());
		tradeDao.save(payorder);
		//更新用户存款类型 
//		users.setIsCashin(0);
//		tradeDao.update(users);
//		logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (ips单号:"+billno+" 金额："+payAmount+")");
//		tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, "后台补单 ;易宝单号:"+payBillno+";"+msg);
//		
//		userDao.setUserCashin(loginname);
//		
//		bankinfoDao.changeAmountOnline(4, Double.valueOf(payAmount),billno);
		return null;
	}
	
	@Override
	public String repairNetpayOrderZf(String operator, String billno,
			String payAmount, String ip, String payBillno, String payTime,
			String payPlatform,String loginname,String msg) {
		// TODO Auto-generated method stub
		Payorder payOrder=null;
		try {
			payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder!=null) {
			return "支付单号："+billno+" 已经存在，请重新核对";
		}
		
		msg=msg==null?"":msg;
		Object o=userDao.get(Users.class, loginname);
		if (o==null) {
			return "您输入的会员账号不存在，请重新输入";
		}
		Users users = (Users)o; 
		
		Payorder payorder=new Payorder();
		payorder.setBillno(billno);
		payorder.setPayPlatform(payPlatform);
		payorder.setFlag(PayOrderFlagType.SUCESS.getCode());
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(Double.valueOf(payAmount));
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setIp(ip);
		payorder.setCreateTime(DateUtil.now());
		payorder.setReturnTime(new java.sql.Timestamp(DateUtil.parseDateForStandard(payTime).getTime()));
		if(payPlatform.equals("zf")){
			payorder.setMsg("后台补单 ;zf单号:"+billno+";"+msg+";操作人："+operator);
		}else if(payPlatform.equals("zf1")){
			payorder.setMsg("后台补单 ;zf1单号:"+billno+";"+msg+";操作人："+operator);
		}else if(payPlatform.equals("zf2")){
			payorder.setMsg("后台补单 ;zf2单号:"+billno+";"+msg+";操作人："+operator);
		}else if(payPlatform.equals("zf3")){
			payorder.setMsg("后台补单 ;zf3单号:"+billno+";"+msg+";操作人："+operator);
		}else if(CommonZfEnum.existKey(payPlatform)){
			payorder.setMsg("后台补单 ;"+payPlatform+"单号:"+billno+";"+msg+";操作人："+operator);
		}
		else if(payPlatform.equals("dk")){
			payorder.setMsg("后台补单 ;dk单号:"+billno+";"+msg+";操作人："+operator);
		}else if(payPlatform.equals("dk1")){
			payorder.setMsg("后台补单 ;dk1单号:"+billno+";"+msg+";操作人："+operator);
		}else if(payPlatform.equals("dk2")){
			payorder.setMsg("后台补单 ;dk2单号:"+billno+";"+msg+";操作人："+operator);
		}else if(payPlatform.equals("dk3")){
			payorder.setMsg("后台补单 ;dk3单号:"+billno+";"+msg+";操作人："+operator);
		}else if(payPlatform.equals("zfdk")){
			payorder.setMsg("后台补单 ;zfdk单号:"+billno+";"+msg+";操作人："+operator);
		}
		else if(payPlatform.equals("zfwx")){
			payorder.setMsg("后台补单 ;zfwx单号:"+billno+";"+msg+";操作人："+operator);
		}else{
			payorder.setMsg("后台补单 ;"+payPlatform+"单号:"+billno+";"+msg+";操作人："+operator);
		}
		payorder.setType(PayType.Init.getCode());
		tradeDao.save(payorder);
//		//更新用户存款类型 
//		users.setIsCashin(0);
//		tradeDao.update(users);
//		
//		logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (zf单号:"+billno+" 金额："+payAmount+")");
//		
//		tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, "后台补单 ;智付单号:"+billno+";"+msg);
//		
//		userDao.setUserCashin(loginname);
//		
//		bankinfoDao.changeAmountOnline(4, Double.valueOf(payAmount),billno);
		return null;
	}
	
	@Override
	public String repairNetpayOrderZfb(String operator, String billno,
			String payAmount, String ip, String payBillno, String payTime,
			String payPlatform,String loginname,String msg) {
		// TODO Auto-generated method stub
		Payorder payOrder=null;
		try {
			payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder!=null) {
			return "支付单号："+billno+" 已经存在，请重新核对";
		}
		
		msg=msg==null?"":msg;
		Object o=userDao.get(Users.class, loginname);
		if (o==null) {
			return "您输入的会员账号不存在，请重新输入";
		}
		Users users = (Users)o; 
		
		Payorder payorder=new Payorder();
		payorder.setBillno(billno);
		payorder.setPayPlatform(payPlatform);
		payorder.setFlag(PayOrderFlagType.SUCESS.getCode());
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(Double.valueOf(payAmount));
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setIp(ip);
		payorder.setCreateTime(DateUtil.now());
		payorder.setReturnTime(new java.sql.Timestamp(DateUtil.parseDateForStandard(payTime).getTime()));
		if(payPlatform.equals("zfb")){
			payorder.setMsg("后台补单 ;zfb单号:"+billno+";"+msg+";操作人："+operator);
		}
		payorder.setType(PayType.Init.getCode());
		tradeDao.save(payorder);
//		//更新用户存款类型 
//		users.setIsCashin(0);
//		tradeDao.update(users);
//		
//		logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (zf单号:"+billno+" 金额："+payAmount+")");
//		
//		tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, "后台补单 ;智付单号:"+billno+";"+msg);
//		
//		userDao.setUserCashin(loginname);
//		
//		bankinfoDao.changeAmountOnline(4, Double.valueOf(payAmount),billno);
		return null;
	}
	
	@Override
	public String repairGameOrder(String operator, String billno,
			String payAmount, String ip, String payBillno, String payTime,
			String payPlatform,String loginname,String msg) {
		// TODO Auto-generated method stub
		GameTransfers payOrder=null;
		try {
			payOrder = (GameTransfers) get(GameTransfers.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder!=null) {
			return "支付单号："+billno+" 已经存在，请重新核对";
		}
		
		msg=msg==null?"":msg;
		Object o=userDao.get(Users.class, loginname);
		if (o==null) {
			return "您输入的会员账号不存在，请重新输入";
		}
		Users users = (Users)o; 
		
		GameTransfers payorder=new GameTransfers();
		payorder.setBillno(billno);
		payorder.setPayPlatform(payPlatform);
		payorder.setFlag(PayOrderFlagType.SUCESS.getCode());
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(Double.valueOf(payAmount));
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setIp(ip);
		payorder.setCreateTime(DateUtil.now());
		payorder.setReturnTime(new java.sql.Timestamp(DateUtil.parseDateForStandard(payTime).getTime()));
		payorder.setMsg("后台补单 ;"+payPlatform+"单号:"+billno+";"+msg);
		payorder.setType(PayType.Init.getCode());
		payorder.setOperator(operator);
		tradeDao.save(payorder);
//		//更新用户存款类型 
//		users.setIsCashin(0);
//		tradeDao.update(users);
//		
//		logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (zf单号:"+billno+" 金额："+payAmount+")");
//		
//		tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, "后台补单 ;智付单号:"+billno+";"+msg);
//		
//		userDao.setUserCashin(loginname);
//		
//		bankinfoDao.changeAmountOnline(4, Double.valueOf(payAmount),billno);
		return null;
	}

	public String repairNetpayOrderHf(String operator, String billno,String payAmount, String ip, String payBillno, String payTime,String payPlatform,String loginname,String msg) {
		Payorder payOrder=null;
		try {
			payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			log.error("查询payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder!=null) {
			DecimalFormat df = new DecimalFormat("0.00");
			if(payOrder.getType()==2 && df.format(payOrder.getMoney()).equals(df.format(Double.parseDouble(payAmount)))&&loginname.equals(payOrder.getLoginname())){
				payOrder.setType(PayType.Init.getCode());
				tradeDao.update(payOrder);
				return null;
			}
			return "支付单号："+billno+" 已经存在，请重新核对";
		}
		
		msg=msg==null?"":msg;
		Object o=userDao.get(Users.class, loginname);
		if (o==null) {
			return "您输入的会员账号不存在，请重新输入";
		}
		Users users = (Users)o; 
		
		Payorder payorder=new Payorder();
		payorder.setBillno(billno);
		payorder.setPayPlatform(payPlatform);
		payorder.setFlag(PayOrderFlagType.SUCESS.getCode());
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(Double.valueOf(payAmount));
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setIp(ip);
		payorder.setCreateTime(DateUtil.now());
		payorder.setReturnTime(new java.sql.Timestamp(DateUtil.parseDateForStandard(payTime).getTime()));
		payorder.setMsg("后台补单 ;hf单号:"+billno+";"+msg);
		
		if("wechat".equals(payPlatform)){
			payorder.setMsg(msg.trim());
		}
		
		payorder.setType(PayType.Init.getCode());
		tradeDao.save(payorder);
//		//更新用户存款类型 
//		users.setIsCashin(0);
//		tradeDao.update(users);
//		
//		logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (zf单号:"+billno+" 金额："+payAmount+")");
//		
//		tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, "后台补单 ;智付单号:"+billno+";"+msg);
//		
//		userDao.setUserCashin(loginname);
//		
//		bankinfoDao.changeAmountOnline(4, Double.valueOf(payAmount),billno);
		return null;
	}
	
	public String repairNetpayOrderHc(String operator, String billno,
			String payAmount, String ip, String payBillno, String payTime,
			String payPlatform,String loginname,String msg) {
		// TODO Auto-generated method stub
		Payorder payOrder=null;
		try {
			payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder!=null) {
			DecimalFormat df = new DecimalFormat("0.00");
			if(payOrder.getType()==2 && df.format(payOrder.getMoney()).equals(df.format(Double.parseDouble(payAmount)))&&loginname.equals(payOrder.getLoginname())){
				payOrder.setType(PayType.Init.getCode());
				tradeDao.update(payOrder);
				return null;
			}
			return "支付单号："+billno+" 已经存在，请重新核对";
		}
		
		msg=msg==null?"":msg;
		Object o=userDao.get(Users.class, loginname);
		if (o==null) {
			return "您输入的会员账号不存在，请重新输入";
		}
		Users users = (Users)o; 
		
		Payorder payorder=new Payorder();
		payorder.setBillno(billno);
		payorder.setPayPlatform(payPlatform);
		payorder.setFlag(PayOrderFlagType.SUCESS.getCode());
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(Double.valueOf(payAmount));
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setIp(ip);
		payorder.setCreateTime(DateUtil.now());
		payorder.setReturnTime(new java.sql.Timestamp(DateUtil.parseDateForStandard(payTime).getTime()));
		payorder.setMsg("后台补单 ;"+msg+";操作人："+operator);
		payorder.setType(PayType.Init.getCode());
		tradeDao.save(payorder);
//		//更新用户存款类型 
//		users.setIsCashin(0);
//		tradeDao.update(users);
//		
//		logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (zf单号:"+billno+" 金额："+payAmount+")");
//		
//		tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, "后台补单 ;智付单号:"+billno+";"+msg);
//		
//		userDao.setUserCashin(loginname);
//		
//		bankinfoDao.changeAmountOnline(4, Double.valueOf(payAmount),billno);
		return null;
	}
	
	public String repairNetpayOrderBfb(String operator, String billno,
			String payAmount, String ip, String payBillno, String payTime,
			String payPlatform,String loginname,String msg) {
		// TODO Auto-generated method stub
		Payorder payOrder=null;
		try {
			payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder!=null) {
			DecimalFormat df = new DecimalFormat("0.00");
			if(payOrder.getType()==2 && df.format(payOrder.getMoney()).equals(df.format(Double.parseDouble(payAmount)))&&loginname.equals(payOrder.getLoginname())){
				payOrder.setType(PayType.Init.getCode());
				tradeDao.update(payOrder);
				return null;
			}
			return "支付单号："+billno+" 已经存在，请重新核对";
		}
		
		msg=msg==null?"":msg;
		Object o=userDao.get(Users.class, loginname);
		if (o==null) {
			return "您输入的会员账号不存在，请重新输入";
		}
		Users users = (Users)o; 
		
		Payorder payorder=new Payorder();
		payorder.setBillno(billno);
		payorder.setPayPlatform(payPlatform);
		payorder.setFlag(PayOrderFlagType.SUCESS.getCode());
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(Double.valueOf(payAmount));
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setIp(ip);
		payorder.setCreateTime(DateUtil.now());
		payorder.setReturnTime(new java.sql.Timestamp(DateUtil.parseDateForStandard(payTime).getTime()));
		payorder.setMsg("后台补单 ;"+msg+"操作人："+operator);
		payorder.setType(PayType.Init.getCode());
		tradeDao.save(payorder);
//		//更新用户存款类型 
//		users.setIsCashin(0);
//		tradeDao.update(users);
//		
//		logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (zf单号:"+billno+" 金额："+payAmount+")");
//		
//		tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, "后台补单 ;智付单号:"+billno+";"+msg);
//		
//		userDao.setUserCashin(loginname);
//		
//		bankinfoDao.changeAmountOnline(4, Double.valueOf(payAmount),billno);
		return null;
	}
	
	public String repairNetpayOrderGfb(String operator, String billno, String payAmount, String ip, String payBillno, String payTime, 
			String payPlatform,String loginname,String msg) {
		// TODO Auto-generated method stub
		Payorder payOrder=null;
		try {
			payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder!=null) {
			DecimalFormat df = new DecimalFormat("0.00");
			if(payOrder.getType()==2 && df.format(payOrder.getMoney()).equals(df.format(Double.parseDouble(payAmount)))&&loginname.equals(payOrder.getLoginname())){
				payOrder.setType(PayType.Init.getCode());
				tradeDao.update(payOrder);
				return null;
			}
			return "支付单号："+billno+" 已经存在，请重新核对";
		}
		
		msg=msg==null?"":msg;
		Object o=userDao.get(Users.class, loginname);
		if (o==null) {
			return "您输入的会员账号不存在，请重新输入";
		}
		Users users = (Users)o; 
		
		Payorder payorder=new Payorder();
		payorder.setBillno(billno);
		payorder.setPayPlatform(payPlatform);
		payorder.setFlag(PayOrderFlagType.SUCESS.getCode());
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(Double.valueOf(payAmount));
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setIp(ip);
		payorder.setCreateTime(DateUtil.now());
		payorder.setReturnTime(new java.sql.Timestamp(DateUtil.parseDateForStandard(payTime).getTime()));
		payorder.setMsg("后台补单 ;"+msg+"操作人："+operator);
		payorder.setType(PayType.Init.getCode());
		tradeDao.save(payorder);
		return null;
	}
	
	public String submitProposal(String billno, String operator, String ip, String remark){
		Payorder payOrder=null;
		try {
			payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder==null) {
			return "支付单号："+billno+" 不存在，请重新核对";
		}
		if(payOrder.getType()==1){
			try {
				Users users=(Users) get(Users.class, payOrder.getLoginname(), LockMode.UPGRADE);
				if(users==null){
					return "该用户不存在";
				}
				String loginname=payOrder.getLoginname();
				Double payAmount=payOrder.getMoney();
				if(payOrder.getPayPlatform().equals("ips")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (ips单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeAmountOnline(4, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("hf")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (hf单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeHfAmountOnline(4, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().contains("汇付")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (hf单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					int a=441;
					if(payOrder.getPayPlatform().equals("汇付1")){
						a= 441;
					}else if(payOrder.getPayPlatform().equals("汇付2")){
						a= 442;
					}else if(payOrder.getPayPlatform().equals("汇付3")){
						a= 443;
					}else if(payOrder.getPayPlatform().equals("汇付4")){
						a= 444;
					}else if(payOrder.getPayPlatform().equals("汇付5")){
						a= 445;
					}else if(payOrder.getPayPlatform().equals("汇付6")){
						a= 446;
					}else if(payOrder.getPayPlatform().equals("汇付7")){
						a= 447;
					}else {
						System.out.println("HF1-7补单出错！");
						return null;
					}
					bankinfoDao.changeHfAmountOnline(a, Double.valueOf(payAmount),billno);
				}
				else if(payOrder.getPayPlatform().equals("zf")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (zf单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeZfAmountOnline(40, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("zf1")){
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (zf1单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeZf1AmountOnline(41, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("zf2")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (zf2单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeZf23AmountOnline(42, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("zf3")){
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (zf3单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeZf23AmountOnline(43, Double.valueOf(payAmount),billno);
				}else if(CommonZfEnum.existKey(payOrder.getPayPlatform())) {//通用智付
					CommonZfEnum cmzf = CommonZfEnum.getCommonZf(payOrder.getPayPlatform());
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  ("+cmzf.getCode()+"单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					if(payOrder.getPayPlatform().contains("wx")){
						payAmount = Arith.mul(Double.valueOf(payAmount), 0.992);
					}
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeCommonZfAmountOnline(cmzf, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("hc")){
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (hc单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeHcAmountOnline(50, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("dk")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (dk单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeZfDianKaAmountOnline(4000, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("dk1")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (dk1单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeZfDianKaAmountOnline(4001, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("dk2")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (dk2单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeZfDianKaAmountOnline(4002, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("dk3")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (dk3单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeZfDianKaAmountOnline(4003, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("zfdk")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (zfdk单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeZfDianKaAmountOnline(4010, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("zfb")){
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip+"  (zfb单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg()+"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeZfbAmountOnline(60, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("bfb")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (bfb单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeBfbAmountOnline(61, Double.valueOf(payAmount),billno);
				}else if(CommonGfbEnum.existKey(payOrder.getPayPlatform())) {
					CommonGfbEnum cmgfb = CommonGfbEnum.getCommonGfb(payOrder.getPayPlatform());
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  ("+cmgfb.getCode()+"单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeCommonGfbAmountOnline(cmgfb, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("lfwx")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (lfwx单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeLfwxAmountOnline(Integer.parseInt(Banktype.LFWXZF450.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("xbwx")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (xbwx单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeXbwxAmountOnline(Integer.parseInt(Banktype.XBWXZF460.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("kdzfb")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (kdzfb单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.991);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeKdZfAmountOnline(Integer.parseInt(Banktype.KDZF470.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("kdwxzf")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (kdzf单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.976);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeKdWxZfAmountOnline(Integer.parseInt(Banktype.KDWXZF471.getCode()), Double.valueOf(payAmount),billno);
								
				}else if(payOrder.getPayPlatform().equals("kdwxzf2")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (kdwxzf2单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.976);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeKdWxZfAmountOnline2(Integer.parseInt(Banktype.KDWXZF474.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("kdwxzf3")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (kdzf3单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.992);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeKdWxZfAmountOnline3(Integer.parseInt(Banktype.KDWXZF478.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("zfdk1")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (zfdk1单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeXbwxAmountOnline(Integer.parseInt(Banktype.DIANKA4011.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("haier")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (haier单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, payAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeHaierAmountOnline(Integer.parseInt(Banktype.HAIER472.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("hhbwxzf")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (hhbwxzf单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.992);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeHHbWxZfAmountOnline(Integer.parseInt(Banktype.HHBZF481.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("jubzfb")) {
					//更新用户存款类型 
					users.setIsCashin(0);    
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (jubzfb单号:"+billno+" 金额："+payAmount+") 说明:"+remark);  
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.991);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeJubZfbAmountOnline(Integer.parseInt(Banktype.JUBZFB473.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("xlb")) {   
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (xlb单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.992);      
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeXlbAmountOnline(Integer.parseInt(Banktype.XLBWX485.getCode()),  Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("wechat")){
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (wechat单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeWechatAmountOnline(payOrder.getMsg(), Double.valueOf(payAmount), billno);
					userDao.updatePayOrderValidation(payOrder.getMsg(), Double.valueOf(payAmount), billno);
				}else if(payOrder.getPayPlatform().equals("xlbwy")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (xlbwy单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, payAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeXlbWyAmountOnline(Integer.parseInt(Banktype.XLBWY486.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("yfzfb")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (yfzfb单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.991);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeYfZfbAmountOnline(Integer.parseInt(Banktype.YFZFB488.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("hcymd")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (hcymd单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeHcYmdAmountOnline(51, Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("xbzfb")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (xbzfb单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.991);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeXbZfbAmountOnline(Integer.parseInt(Banktype.XBZFB489.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("yfwx")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (yfwx单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.992);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeYfWxAmountOnline(Integer.parseInt(Banktype.YFWX492.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("ybzfb")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (ybzfb单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.991);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeYbZfbAmountOnline(Integer.parseInt(Banktype.YBZFB491.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("qwzfb")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (qwzfb单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.991);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeQwZfbAmountOnline(Integer.parseInt(Banktype.QWZFB493.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("kdzfb2")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (kdzfb2单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.991);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeKdZfb2AmountOnline(Integer.parseInt(Banktype.KDZFB494.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("qwwx")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (qwwx单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					Double realAmount = Arith.mul(Double.valueOf(payAmount), 0.992);
					tradeDao.changeCredit(loginname, realAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeQwWxAmountOnline(Integer.parseInt(Banktype.QWWX495.getCode()), Double.valueOf(payAmount),billno);
				}else if(payOrder.getPayPlatform().equals("xlbzfb")) {
					//更新用户存款类型 
					users.setIsCashin(0);
					tradeDao.update(users);
					logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (xlbzfb单号:"+billno+" 金额："+payAmount+") 说明:"+remark);
					tradeDao.changeCredit(loginname, payAmount, CreditChangeType.NETPAY.getCode(),  billno, payOrder.getMsg() +"说明:"+remark);
					userDao.setUserCashin(loginname);
					bankinfoDao.changeXlbZfbAmountOnline(Integer.parseInt(Banktype.XLBZFB497.getCode()), Double.valueOf(payAmount),billno);
				}
				payOrder.setType(0);
				payOrder.setMsg(payOrder.getMsg()+" 说明："+remark);
				tradeDao.update(payOrder);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return "失败";
			}
		}else if(payOrder.getType()==0){
			return "支付单号："+billno+" 不需要审核！";
		}else{
			return "支付单号："+billno+" 已经取消！";
		}
	}
	
	public String submitGameProposal(String billno, String operator, String ip, String remark){
		GameTransfers payOrder=null;
		try {
			payOrder = (GameTransfers) get(GameTransfers.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder==null) {
			return "支付单号："+billno+" 不存在，请重新核对";
		}
		if(payOrder.getType()==1){
			try {
				Users users=(Users) get(Users.class, payOrder.getLoginname(), LockMode.UPGRADE);
				if(users==null){
					return "该用户不存在";
				}
				String loginname=payOrder.getLoginname();
				Double payAmount=payOrder.getMoney();
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip + "  ("+payOrder.getPayPlatform()+"单号:" + billno + " 金额：" + payAmount + ") 说明:" + remark);
				tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAYHL.getCode(), billno, payOrder.getMsg() + "说明:" + remark);
				payOrder.setType(0);
				payOrder.setMsg(payOrder.getMsg()+" 说明："+remark);
				tradeDao.update(payOrder);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return "失败";
			}
		}else if(payOrder.getType()==0){
			return "单号："+billno+" 不需要审核！";
		}else{
			return "单号："+billno+" 已经取消！";
		}
	}
	
	public String submitQuotalProposal(String billno, String operator, String ip, String remark) {
		QuotalRevision quotalog=null;
		try {
			quotalog = (QuotalRevision) get(QuotalRevision.class, Integer.parseInt(billno), LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (quotalog==null) {
			return "额度增减单号："+billno+" 不存在，请重新核对";
		}
		
		if(quotalog.getExamine()==0 && quotalog.getType().equals(CreditChangeType.CHANGE_QUOTALSLOT.getCode())){
			try {
				Users users=(Users) get(Users.class, quotalog.getLoginname(), LockMode.UPGRADE);
				if(users==null){
					return "该用户不存在";
				}
				String loginname=quotalog.getLoginname();
				Double payAmount=quotalog.getRemit();
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip + "  (额度增减单号:" + billno + " 金额：" + payAmount + ") 说明:" + remark);
				changeAgentSlotCreditBySql(users, Double.valueOf(payAmount), CreditChangeType.CHANGE_QUOTAL.getCode(), billno+"", quotalog.getRemark() + "说明:" + remark);
				quotalog.setExamine(1);
				quotalog.setExamineRemark(remark);
				quotalog.setExaminetime(DateUtil.getCurrentTimestamp());
				quotalog.setExamineOperator(operator);
				quotalog.setExamineIp(ip);
				tradeDao.update(quotalog);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return "失败";
			}
		}else if(quotalog.getExamine()==0){
			try {
				Users users=(Users) get(Users.class, quotalog.getLoginname(), LockMode.UPGRADE);
				if(users==null){
					return "该用户不存在";
				}
				String loginname=quotalog.getLoginname();
				Double payAmount=quotalog.getRemit();
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER, ip + "  (额度增减单号:" + billno + " 金额：" + payAmount + ") 说明:" + remark);
				tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.CHANGE_QUOTAL.getCode(), billno+"", quotalog.getRemark() + "说明:" + remark);
				quotalog.setExamine(1);
				quotalog.setExamineRemark(remark);
				quotalog.setExaminetime(DateUtil.getCurrentTimestamp());
				quotalog.setExamineOperator(operator);
				quotalog.setExamineIp(ip);
				tradeDao.update(quotalog);
				return null;
			} catch (Exception e) {
				e.printStackTrace();
				return "失败";
			}
		}else if(quotalog.getExamine()==1){
			return "单号："+billno+" 已审核通过！";
		}else{
			return "单号："+billno+" 已审核不通过！";
		} 
	}
	
	public void changeAgentSlotCreditBySql(Users lockedUser, Double remit,
			String creditLogType, String referenceNo, String remark)
			throws GenericDfhRuntimeException {
		String msg = null;
		try {
			if (lockedUser == null) {
				msg = "用户不存在";
				throw new GenericDfhRuntimeException(msg);
			}
			if(remit==0.0){
				log.info("额度变化为0，不操作..");
				return;
			}
			
			Userstatus slotAccount = (Userstatus) get(Userstatus.class, lockedUser.getLoginname());
			if(null == slotAccount){
				slotAccount = new Userstatus() ;
				slotAccount.setLoginname(lockedUser.getLoginname());
				slotAccount.setTouzhuflag(0);
				slotAccount.setCashinwrong(0);
				slotAccount.setSlotaccount(0.0);
			}
			// 代理的额度允许为负
			if (StringUtils.equals(lockedUser.getRole(), "AGENT")) {
				if (StringUtils.isNotEmpty(referenceNo)) {
					DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", creditLogType)).add(
							Restrictions.like("remark", "referenceNo:" + referenceNo, MatchMode.START)).setProjection(Projections.rowCount());
					List list = findByCriteria(dc);
					Integer rowCount = (Integer) list.get(0);
					if (rowCount > 0) {
						msg = "单号[" + referenceNo + "]已支付过额度";
						throw new GenericDfhRuntimeException(msg);
					}
				}
				
				Double credit = slotAccount.getSlotaccount();
				Double newCredit = credit + remit;
				slotAccount.setSlotaccount(newCredit);
				saveOrUpdate(slotAccount);
				
				log.info("用户："+lockedUser.getLoginname()+"  额度增加到："+newCredit);
				Creditlogs log = new Creditlogs();
				log.setLoginname(lockedUser.getLoginname());
				log.setType(creditLogType);
				log.setCredit(credit);
				log.setRemit(remit);
				log.setNewCredit(newCredit);
				log.setRemark(remark);
				log.setCreatetime(DateUtil.getCurrentTimestamp());
				save(log);
				msg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		}finally {
			if (msg != null)
				throw new GenericDfhRuntimeException(msg);
			else {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String submitSbProposal(Integer id, String operator, String ip, String remark){
		Sbcoupon sbcoupon= (Sbcoupon) get(Sbcoupon.class, id, LockMode.UPGRADE);
		sbcoupon.setStatus(1);
		sbcoupon.setRemark(sbcoupon.getRemark()+"  审核备注："+remark);
		tradeDao.update(sbcoupon);
		return null;
	}
	
	public String submitCancel(String billno, String operator, String ip, String remark){
		Payorder payOrder=null;
		try {
			payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder==null) {
			return "支付单号："+billno+" 不存在，请重新核对";
		}
		if(payOrder.getType()==1){
			tradeDao.delete(Payorder.class, payOrder.getBillno());
//			String pno = seqDao.generatePayID();
			String pno = RandomStringUtils.randomNumeric(1);
			String billnoC = (payOrder.getBillno().replace(payOrder.getLoginname(), "")+"_"+pno);
			payOrder.setBillno(billnoC);
			payOrder.setType(-1);
			payOrder.setMsg(payOrder.getMsg()+" 说明："+remark);
			save(payOrder);
			if(payOrder.getPayPlatform().equals("ips")){
			   logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：(ips单号:"+billno+" 说明:"+remark);
			}else if(payOrder.getPayPlatform().equals("hf")){
			   logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：(hf单号:"+billno+" 说明:"+remark);
			}else if(payOrder.getPayPlatform().equals("zf")){
			   logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：(zf单号:"+billno+" 说明:"+remark);
			}else if(payOrder.getPayPlatform().equals("zf1")){
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：(zf1单号:"+billno+" 说明:"+remark);
			}else if(payOrder.getPayPlatform().equals("zf2")){
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：(zf2单号:"+billno+" 说明:"+remark);
			}else if(payOrder.getPayPlatform().equals("zf3")){
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：(zf3单号:"+billno+" 说明:"+remark);
			}else if(CommonZfEnum.existKey(payOrder.getPayPlatform())){
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：("+payOrder.getPayPlatform()+"单号:"+billno+" 说明:"+remark);
			}else if(payOrder.getPayPlatform().equals("zfdk")){
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：(zfdk单号:"+billno+" 说明:"+remark);
			}else if(payOrder.getPayPlatform().equals("hc")){
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：(hc单号:"+billno+" 说明:"+remark);
			}else if(payOrder.getPayPlatform().equals("zfb")){
			   logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：(zfb单号:"+billno+" 说明:"+remark);
			}else if(payOrder.getPayPlatform().equals("bfb")){
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：(bfb单号:"+billno+" 说明:"+remark);
			}else if(CommonGfbEnum.existKey(payOrder.getPayPlatform())){
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：("+payOrder.getPayPlatform()+"单号:"+billno+" 说明:"+remark);
			}
			return null;
		}else if(payOrder.getType()==0){
			return "支付单号："+billno+" 不需要审核！";
		}else{
			return "支付单号："+billno+" 已经取消！";
		}
	}
	
	public String submitGameCancel(String billno, String operator, String ip, String remark){
		GameTransfers payOrder=null;
		try {
			payOrder = (GameTransfers) get(GameTransfers.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder==null) {
			return "支付单号："+billno+" 不存在，请重新核对";
		}
		if(payOrder.getType()==1){
			tradeDao.delete(GameTransfers.class, payOrder.getBillno());
			String pno = seqDao.generatePayID();
			payOrder.setBillno(payOrder.getBillno()+"_c"+pno);
			payOrder.setType(-1);
			payOrder.setMsg(payOrder.getMsg()+" 说明："+remark);
			save(payOrder);
			logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  取消补单：("+payOrder.getPayPlatform()+"单号:"+billno+" 说明:"+remark);
			return null;
		}else if(payOrder.getType()==0){
			return "单号："+billno+" 不需要审核！";
		}else{
			return "单号："+billno+" 已经取消！";
		}
	}
	

	public String submitQuotalCancel(String billno, String operator, String ip, String remark){
		QuotalRevision qrevision=null;
		try {
			qrevision = (QuotalRevision) get(QuotalRevision.class, Integer.parseInt(billno), LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (qrevision==null) {
			return "额度增减单号："+billno+" 不存在，请重新核对";
		}
		if(qrevision.getExamine() == 0){ 
			qrevision.setExamine(2);
			qrevision.setExaminetime(DateUtil.getCurrentTimestamp());
			qrevision.setExamineRemark(remark);
			qrevision.setExamineIp(ip);
			qrevision.setNewCredit(qrevision.getCredit());
			save(qrevision);
			logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  额度修改不通过 单号:"+billno+" 说明:"+remark);
			return null;
		}else if(qrevision.getExamine()==1){
			return "单号："+billno+" 已审核通过！";
		}else{
			return "单号："+billno+" 已审核不通过！";
		}
	}
	public SeqDao getSeqDao() {
		return seqDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}
	
	
	/**
	 * 乐富微信
	 * @param operator
	 * @param billno
	 * @param payAmount
	 * @param ip
	 * @param payBillno
	 * @param payTime
	 * @param payPlatform
	 * @param loginname
	 * @param msg
	 * @return
	 */
	public String repairNetpayOrderLfwx(String operator, String billno,
			String payAmount, String ip, String payBillno, String payTime,
			String payPlatform,String loginname,String msg) {
		// TODO Auto-generated method stub
		Payorder payOrder=null;
		try {
			payOrder = (Payorder) get(Payorder.class, billno, LockMode.UPGRADE);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询payorder库表时发生异常", e);
			return "系统繁忙，请重新操作";
		}
		if (payOrder!=null) {
			DecimalFormat df = new DecimalFormat("0.00");
			if(payOrder.getType()==2 && df.format(payOrder.getMoney()).equals(df.format(Double.parseDouble(payAmount)))&&loginname.equals(payOrder.getLoginname())){
				payOrder.setType(PayType.Init.getCode());
				tradeDao.update(payOrder);
				return null;
			}
			return "支付单号："+billno+" 已经存在，请重新核对";
		}
		
		msg=msg==null?"":msg;
		Object o=userDao.get(Users.class, loginname);
		if (o==null) {
			return "您输入的会员账号不存在，请重新输入";
		}
		Users users = (Users)o; 
		
		Payorder payorder=new Payorder();
		payorder.setBillno(billno);
		payorder.setPayPlatform(payPlatform);
		payorder.setFlag(PayOrderFlagType.SUCESS.getCode());
		payorder.setNewaccount(Constants.FLAG_FALSE);
		payorder.setLoginname(loginname);
		payorder.setAliasName(users.getAccountName());
		payorder.setMoney(Double.valueOf(payAmount));
		payorder.setPhone(users.getPhone());
		payorder.setEmail(users.getEmail());
		payorder.setIp(ip);
		payorder.setCreateTime(DateUtil.now());
		payorder.setReturnTime(new java.sql.Timestamp(DateUtil.parseDateForStandard(payTime).getTime()));
		payorder.setMsg("后台补单 ;lfwx单号:"+billno+";"+msg);
		payorder.setType(PayType.Init.getCode());
		tradeDao.save(payorder);
//		//更新用户存款类型 
//		users.setIsCashin(0);
//		tradeDao.update(users);
//		
//		logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (zf单号:"+billno+" 金额："+payAmount+")");
//		
//		tradeDao.changeCredit(loginname, Double.valueOf(payAmount), CreditChangeType.NETPAY.getCode(), billno, "后台补单 ;智付单号:"+billno+";"+msg);
//		
//		userDao.setUserCashin(loginname);
//		
//		bankinfoDao.changeAmountOnline(4, Double.valueOf(payAmount),billno);
		return null;
	}
	
	public String submitWechatProposal(DepositWechat dWechat, String loginname, String operator, String ip){
		String returnmsg=null;
		try {
			if(get(Payorder.class, dWechat.getBillno())==null){
				Users users=(Users) get(Users.class, loginname, LockMode.UPGRADE);
				Payorder payorder=new Payorder();
				payorder.setBillno(dWechat.getBillno());
				payorder.setPayPlatform("wechat");
				payorder.setMsg("转入:"+dWechat.getWechat());
				
				payorder.setFlag(0);
				payorder.setNewaccount(Constants.FLAG_FALSE);
				payorder.setLoginname(users.getLoginname());
				payorder.setAliasName(users.getAccountName());
				payorder.setMoney(dWechat.getAmount());
				payorder.setPhone(users.getPhone());
				payorder.setEmail(users.getEmail());
				payorder.setCreateTime(DateUtil.now());
				tradeDao.save(payorder);
				
				dWechat.setDealtime(DateUtil.now());
				dWechat.setState(1);
				dWechat.setRemark("说明:手工处理by "+operator);
				dWechat.setUsername(users.getLoginname());
				tradeDao.update(dWechat);
				//更新用户存款方式
				users.setIsCashin(0);
				tradeDao.update(users);
				logDao.insertOperationLog(operator, OperationLogType.REPAIR_PAYORDER,  ip+"  (wechat单号:"+dWechat.getBillno()+" 金额："+dWechat.getAmount()+")");
				tradeDao.changeCredit(users.getLoginname(), dWechat.getAmount(), CreditChangeType.NETPAY.getCode(), dWechat.getBillno(), "转入:"+dWechat.getWechat()+";说明:手工处理by "+operator);
				userDao.setUserCashin(users.getLoginname());
				bankinfoDao.changeWechatAmountOnline(dWechat.getWechat(), dWechat.getAmount(), dWechat.getBillno());
			}else{
				returnmsg="微信单号已被使用";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "失败";
		}
		
		return returnmsg;
	}
	
	
}
