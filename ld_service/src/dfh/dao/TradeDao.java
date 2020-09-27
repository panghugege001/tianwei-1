package dfh.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Creditlogs;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.UserRole;
import dfh.utils.Constants;
import dfh.utils.HttpUtils;
import dfh.utils.NumericUtil;

public class TradeDao extends UniversalDao {

	private static Logger log = Logger.getLogger(TradeDao.class);
	LogDao logDao;

	public TradeDao() {
		
	}
	
	/**
	 * 转入游戏
	 * @param user
	 * @param remit
	 * @param creditLogType
	 * @param referenceNo
	 * @param remark
	 * @throws GenericDfhRuntimeException
	 */
	public void changeCreditIn(Users user, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException {
		String userName=user.getLoginname();
		log.info("begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			// 代理的额度允许为负
			if ((user.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(user.getRole(), UserRole.MONEY_CUSTOMER.getCode())) {
				msg = "额度不足,当前额度:" + user.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			} else {
				if (StringUtils.isNotEmpty(referenceNo)) {
					DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class).add(Restrictions.eq("type", creditLogType)).add(
							Restrictions.like("remark", "referenceNo:" + referenceNo, MatchMode.START)).setProjection(Projections.rowCount());
					List list = findByCriteria(dc);
					Integer rowCount = (Integer) list.get(0);
					if (rowCount > 0) {
						msg = "单号[" + referenceNo + "]已支付过额度";
						log.error(msg);
						throw new GenericDfhRuntimeException(msg);
					}
				}
				Double credit = user.getCredit();
				Double newCredit = credit + remit;
				updateUserCreditSql(user,remit);
				log.info("update user " + userName + " from credit:" + NumericUtil.formatDouble(credit) + " to credit:" + NumericUtil.formatDouble(newCredit));
				logDao.insertCreditLog(user.getLoginname(), creditLogType, credit, remit, newCredit, "referenceNo:" + referenceNo + ";" + StringUtils.trimToEmpty(remark));
				log.info("finish the trade successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
			if (msg != null){
				throw new GenericDfhRuntimeException(msg);
			}
		}
	}

	public void changeCredit(String userName, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException {
		log.info("begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			Users user = (Users) get(Users.class, userName);
			if (user == null) {
				msg = "用户不存在";
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}
			if(remit==0.0){
				log.info("额度变化为0，不操作..");
				return;
			}
			// 代理的额度允许为负
			if ((user.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(user.getRole(), UserRole.MONEY_CUSTOMER.getCode())) {
				msg = "额度不足,当前额度:" + user.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			} else {
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
				Double credit = user.getCredit();
				Double newCredit = credit + remit;
				updateUserCreditSqlOnline(user.getLoginname() , remit);
				
				log.info("update user " + userName + " from credit:" + NumericUtil.formatDouble(credit) + " to credit:" + NumericUtil.formatDouble(newCredit));
				logDao.insertCreditLog(user.getLoginname(), creditLogType, credit, remit, newCredit, "referenceNo:" + referenceNo + ";" + StringUtils.trimToEmpty(remark));
				log.info("finish the trade successfully");
				msg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
			if (msg != null)
				throw new GenericDfhRuntimeException(msg);
			else {
				try {
//					forceClientToRefreshSession(userName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void changeCreditForAgentSlot(String userName, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException {
		log.info(userName+"begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
			if (user == null) {
				msg = "用户不存在";
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}
			if(remit==0.0){
				log.info("额度变化为0，不操作..");
				return;
			}
			Userstatus slotAccount = (Userstatus) get(Userstatus.class, userName);
			
			// 代理的额度允许为负
			if (StringUtils.equals(user.getRole(), UserRole.AGENT.getCode())) {
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
				updateAgentSlotAccountSql(userName , remit);
				log.info(userName+"update user " + userName + " from credit:" + NumericUtil.formatDouble(credit) + " to credit:" + NumericUtil.formatDouble(newCredit));
				logDao.insertCreditLog(user.getLoginname(), creditLogType, credit, remit, newCredit, "referenceNo:" + referenceNo + ";" + StringUtils.trimToEmpty(remark));
				log.info(userName+"finish the trade successfully");
				msg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
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
	

	
	public void changeCreditCoupon(String userName, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException {
		log.info("begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
			if (user == null) {
				msg = "用户不存在";
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}
			// 代理的额度允许为负
			if ((user.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(user.getRole(), UserRole.MONEY_CUSTOMER.getCode())) {
				msg = "额度不足,当前额度:" + user.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			} else {
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
				Double credit = user.getCredit();
				Double newCredit = credit + remit;
				user.setCredit(newCredit);
				update(user);
				log.info("update user " + userName + " from credit:" + NumericUtil.formatDouble(credit) + " to credit:" + NumericUtil.formatDouble(newCredit));
				logDao.insertCreditLog(user.getLoginname(), creditLogType, credit, remit, newCredit, remark);
				log.info("finish the trade successfully");
				msg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
			if (msg != null)
				throw new GenericDfhRuntimeException(msg);
			else {
				try {
//					forceClientToRefreshSession(userName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void changeCreditCouponEa(String userName, Double remit, String creditLogType, String referenceNo, String remark,String shippingcode) throws GenericDfhRuntimeException {
		log.info("begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
			if (user == null) {
				msg = "用户不存在";
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}
			// 代理的额度允许为负
			if ((user.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(user.getRole(), UserRole.MONEY_CUSTOMER.getCode())) {
				msg = "额度不足,当前额度:" + user.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			} else {
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
				Double credit = user.getCredit();
				Double newCredit = credit + remit;
				user.setCredit(newCredit);
				user.setShippingcode(shippingcode);
				update(user);
				log.info("update user " + userName + " from credit:" + NumericUtil.formatDouble(credit) + " to credit:" + NumericUtil.formatDouble(newCredit));
				logDao.insertCreditLog(user.getLoginname(), creditLogType, credit, remit, newCredit, remark);
				log.info("finish the trade successfully");
				msg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
			if (msg != null)
				throw new GenericDfhRuntimeException(msg);
			else {
				try {
//					forceClientToRefreshSession(userName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void changeCreditCouponPt(String userName, Double remit, String creditLogType, String referenceNo, String remark,String shippingcode) throws GenericDfhRuntimeException {
		log.info("begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
			if (user == null) {
				msg = "用户不存在";
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}
			// 代理的额度允许为负
			if ((user.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(user.getRole(), UserRole.MONEY_CUSTOMER.getCode())) {
				msg = "额度不足,当前额度:" + user.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			} else {
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
				Double credit = user.getCredit();
				Double newCredit = credit + remit;
				user.setShippingcodePt(shippingcode);
				updateUserCreditSqlOnline(user.getLoginname(),remit);
				updateUserShippingcodeTtSql(user);
				log.info("update user " + userName + " from credit:" + NumericUtil.formatDouble(credit) + " to credit:" + NumericUtil.formatDouble(newCredit));
				logDao.insertCreditLog(user.getLoginname(), creditLogType, credit, remit, newCredit, remark);
				log.info("finish the trade successfully");
				msg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
			if (msg != null)
				throw new GenericDfhRuntimeException(msg);
			else {
				try {
//					forceClientToRefreshSession(userName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void changeCreditSb(String userName, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException {
		log.info("begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
			if (user == null) {
				msg = "用户不存在";
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}
			// 代理的额度允许为负
			if ((user.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(user.getRole(), UserRole.MONEY_CUSTOMER.getCode())) {
				msg = "额度不足,当前额度:" + user.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			} else {
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
				Double credit = user.getCredit();
				Double newCredit = credit + remit;
				updateUserCreditSqlOnline(user.getLoginname(),remit);
				log.info("update user " + userName + " from credit:" + NumericUtil.formatDouble(credit) + " to credit:" + NumericUtil.formatDouble(newCredit));
				logDao.insertCreditLog(user.getLoginname(), creditLogType, credit, remit, newCredit, "referenceNo:" + referenceNo + ";" + StringUtils.trimToEmpty(remark));
				log.info("finish the trade successfully");
				msg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
			if (msg != null)
				throw new GenericDfhRuntimeException(msg);
			else {
				try {
//					forceClientToRefreshSession(userName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public void changeCreditEa(String userName, Double remit, String creditLogType, String referenceNo, String remark,String shippingcode) throws GenericDfhRuntimeException {
		log.info("begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
			if (user == null) {
				msg = "用户不存在";
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}
			
			// 代理的额度允许为负
			if ((user.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(user.getRole(), UserRole.MONEY_CUSTOMER.getCode())) {
				msg = "额度不足,当前额度:" + user.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			} else {
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
				updateUserGifTamountSqlOnline(user.getLoginname(),0.0,shippingcode);
				log.info("update user " + userName + " from credit:" +0.0 + " to credit:" + 0.0);
				logDao.insertCreditLog(user.getLoginname(), creditLogType, 0.0, -remit, 0.0, remark);
				log.info("finish the trade successfully");
				msg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
			if (msg != null)
				throw new GenericDfhRuntimeException(msg);
			else {
				try {
//					forceClientToRefreshSession(userName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void changeCreditForOnline(String userName, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException {
		log.info("begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			Users user = (Users) get(Users.class, userName);
			if (user == null) {
				msg = "用户不存在";
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}
			if(remit==0.0){
				log.info("额度变化为0，不操作..");
				return;
			}
			// 代理的额度允许为负
			if ((user.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(user.getRole(), UserRole.MONEY_CUSTOMER.getCode())) {
				msg = "额度不足,当前额度:" + user.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			} else {
				Double credit = user.getCredit();
				Double newCredit = credit + remit;
				updateUserCreditSqlOnline(user.getLoginname(),remit);
				log.info("update user " + userName + " from credit:" + NumericUtil.formatDouble(credit) + " to credit:" + NumericUtil.formatDouble(newCredit));
				logDao.insertCreditLog(user.getLoginname(), creditLogType, credit, remit, newCredit, "referenceNo:" + referenceNo + ";" + StringUtils.trimToEmpty(remark));
				log.info("finish the trade successfully");
				msg = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
			if (msg != null)
				throw new GenericDfhRuntimeException(msg);
			else {
				try {
//					forceClientToRefreshSession(userName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void changeCredit(Users lockedUser, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException {
		String msg = null;
		try {
			if (lockedUser == null) {
				msg = "用户不存在";
			} else if ((lockedUser.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(lockedUser.getRole(), UserRole.MONEY_CUSTOMER.getCode())) {
				msg = "额度不足,当前额度:" + lockedUser.getCredit() + ",交易额度:" + remit;
			} else {
				if(remit==0.0){
					log.info("额度变化为0，不操作..");
					return;
				}
				
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
				Double credit = lockedUser.getCredit();
				Double newCredit = credit + remit;
				lockedUser.setCredit(newCredit);
				update(lockedUser);
				log.info("update user " + lockedUser.getLoginname() + " from credit:" + NumericUtil.formatDouble(credit) + " to credit:" + NumericUtil.formatDouble(newCredit));
				logDao.insertCreditLog(lockedUser.getLoginname(), creditLogType, credit, remit, newCredit, "referenceNo:" + referenceNo + ";" + StringUtils.trimToEmpty(remark));
				log.info("finish the trade successfully");
				msg = null;
				log.error(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GenericDfhRuntimeException(e.getMessage());
		} finally {
			if (msg != null)
				throw new GenericDfhRuntimeException(msg);
			else {
				try {
					forceClientToRefreshSession(lockedUser.getLoginname());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 要去前台网站刷新用户会话，保持额度同步
	 * 
	 * @param loginname
	 */
	private void forceClientToRefreshSession(String loginname) {
		final String cus_loginname = loginname;
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.currentThread();
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				for (int i = 0; i < Constants.CLIENT_URLS.length; i++) {
					String url = Constants.CLIENT_URLS[i];
					Map<String, String> params = new HashMap<String, String>();
					params.put("loginname", cus_loginname);
					try {
						HttpUtils.get(url + Constants.REQUEST_CONTEXT_PATH, params);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	public static void main(String[] args) {
		new TradeDao().forceClientToRefreshSession("elftest01");
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}

}
