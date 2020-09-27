package dfh.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Creditlogs;
import dfh.model.Payorder;
import dfh.model.Proposal;
import dfh.model.QuotalRevision;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.PayOrderFlagType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.HttpUtils;
import dfh.utils.NumericUtil;

public class TradeDao extends UniversalDao {

	private static Logger log = Logger.getLogger(TradeDao.class);
	LogDao logDao;

	public TradeDao() {
	}
	
	
	/**
	 * 在线支付额度限制（当日存款总额度）
	 * @param loginname
	 * @return boolean  false 没有限制，ture 当日存款已经达到上限
	 */
	public boolean amountConfine(){
		try {
			double cashin_totalAmount=Double.parseDouble(Configuration.getInstance().getValue("cashin_totalAmount"));
			if(cashin_totalAmount==0)
				return false;
			
			Criteria c = this.getSession().createCriteria(Payorder.class);
			c.setProjection(Projections.sum("money"));
			c.add(Restrictions.ge("createTime", DateUtil.getYYYY_MM_DD()));
			Object result = c.uniqueResult();
			if (result==null) {
				return false;
			}
			
			if(((Double)result).doubleValue()>=cashin_totalAmount)
				return true;
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询客户在线支付额度，是否超过了规定的额度时，发生异常。", e);
			return false;
		}
	}

	public List queryBet(String username, Date start, Date end, String game) {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(bettotal) from agprofit a  where loginname=? and a.createTime > ? and a.createTime <= ? and a.flag=2 AND platform in(");
		String[] strings = game.split(",");
		for (String s:strings) {
			sql.append("'").append(s).append("'").append(",");
		}
		String substring = sql.substring(0, sql.length() - 1)+")";
		Query q = this.getSession().createSQLQuery(substring);
		q.setParameter(0, username).setParameter(1, start).setParameter(2, end);
		return q.list();
	}
	/**
	 * 在线支付额度限制（会员当日存款额度）
	 * @param loginname
	 * @return boolean  false 没有限制，ture 当日存款已经达到上限
	 */
	public boolean amountConfine(String loginname){
		try {
			double cashin_MaxAmount=Double.parseDouble(Configuration.getInstance().getValue("cashin_amount"));
			if(cashin_MaxAmount==0)
				return false;
			
			Criteria c = this.getSession().createCriteria(Payorder.class);
			c.setProjection(Projections.sum("money"));
			c.add(Restrictions.eq("loginname", loginname)).add(Restrictions.ge("createTime", DateUtil.getYYYY_MM_DD()));
			Object result = c.uniqueResult();
			if (result==null) {
				return false;
			}
			
			if(((Double)result).doubleValue()>=cashin_MaxAmount)
				return true;
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询客户在线支付额度，是否超过了规定的额度时，发生异常。", e);
			return false;
		}
	}

	
	public boolean useOnlinePay(String loginname){
		try {
			int cashin_count=Integer.parseInt(Configuration.getInstance().getValue("cashin_count"));
			if(cashin_count==0)
				return true;
			
			// query proposal table
			Criteria c = this.getSession().createCriteria(Proposal.class);
			c.setProjection(Projections.count("flag"));
			c.add(Restrictions.eq("loginname", loginname));
			c.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			c.add(Restrictions.eq("type", ProposalType.CASHIN.getCode()));
			Object result = c.uniqueResult();
			int count=result==null?0:((Integer) result).intValue();
			
			if (count<cashin_count) {
				// query payorder table
				c = this.getSession().createCriteria(Payorder.class);
				c.setProjection(Projections.count("flag"));
				c.add(Restrictions.eq("loginname", loginname));
				c.add(Restrictions.eq("flag", PayOrderFlagType.SUCESS.getCode()));
				result = c.uniqueResult();
				count+=(result==null?0:((Integer) result).intValue());
			}
			
			return count>=cashin_count?true:false;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("查询客户存款次数时，发生异常", e);
			return true;
		}
	}
	

	public void changeCredit(String userName, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException {
		log.info("begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			//Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
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
							Restrictions.eq("remark", "referenceNo:" + referenceNo+";ok")).setProjection(Projections.rowCount());
					List list = findByCriteria(dc);
					Integer rowCount = (Integer) list.get(0);
					if (rowCount > 0) {
						msg = "单号[" + referenceNo + "]已支付过额度";
						throw new GenericDfhRuntimeException(msg);
					}
				}
				Double credit = user.getCredit();
				Double newCredit = credit + remit;
				//user.setCredit(newCredit);
				//update(user);
				updateUserCreditSql(user, remit);
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
	
	/**
	 * 生日礼金专用
	 * */
	public void changeCreditAgentSlot(String userName, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException {
		log.info("begin to trade " + NumericUtil.formatDouble(remit) + " for " + userName);
		String msg = null;
		try {
			//Users user = (Users) get(Users.class, userName, LockMode.UPGRADE);
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
							Restrictions.eq("remark", "referenceNo:" + referenceNo+";ok")).setProjection(Projections.rowCount());
					List list = findByCriteria(dc);
					Integer rowCount = (Integer) list.get(0);
					if (rowCount > 0) {
						msg = "单号[" + referenceNo + "]已支付过额度";
						throw new GenericDfhRuntimeException(msg);
					}
				}
				Double credit = user.getCredit();
				Double newCredit = credit + remit;
				//user.setCredit(newCredit);
				//update(user);
				if(StringUtils.equals(user.getRole(), UserRole.MONEY_CUSTOMER.getCode())){
					updateUserCreditSql(user, remit);
					log.info("update user " + userName + " from credit:" + NumericUtil.formatDouble(credit) + " to credit:" + NumericUtil.formatDouble(newCredit));
				} else {
					updateUserStatusSlotCreditSql(user, remit);//代理添加到老虎机佣金
					log.info("update userstatus " + userName + " from slotaccount:" + NumericUtil.formatDouble(credit) + " to slotaccount:" + NumericUtil.formatDouble(newCredit));
				}
				
				
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
	 * 增减额度
	 * @param lockedUser
	 * @param remit
	 * @param creditLogType
	 * @param referenceNo
	 * @param remark
	 * @throws GenericDfhRuntimeException
	 */
	public void changeQuotal(Users lockedUser,String billno, Double remit, String creditLogType, String referenceNo, String remark, String operator, String ip) throws GenericDfhRuntimeException  {
		String msg = null;
		try {
			Users user = (Users) get(Users.class, lockedUser.getLoginname(), LockMode.UPGRADE); 
			QuotalRevision log = new QuotalRevision(); 
			log.setLoginname(lockedUser.getLoginname());
			log.setType(CreditChangeType.CHANGE_QUOTAL.getCode());
			log.setCredit(user.getCredit());
			log.setRemit(remit);
			log.setNewCredit(user.getCredit()+remit);
			log.setRemark(remark);
			log.setCreatetime(DateUtil.getCurrentTimestamp());
			log.setExamine(0);
			log.setOperator(operator);
			log.setIp(ip);
			//log.setBillno(billno);
			save(log);
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
	 * 增减额度(代理老虎机佣金)
	 * @param lockedUser
	 * @param remit
	 * @param creditLogType
	 * @param referenceNo
	 * @param remark
	 * @throws GenericDfhRuntimeException
	 */
	public void changeQuotalForSlot(Users lockedUser,String billno, Double remit, String creditLogType, String referenceNo, String remark, String operator, String ip) throws GenericDfhRuntimeException  {
		String msg = null;
		try {
			Userstatus user = (Userstatus) get(Userstatus.class, lockedUser.getLoginname(), LockMode.UPGRADE); 
			if(null == user ){
				user = new Userstatus() ;
				user.setLoginname(lockedUser.getLoginname());
				user.setTouzhuflag(0);
				user.setCashinwrong(0);
				user.setSlotaccount(0.0);
				save(user);
			}
			if(null != user && null == user.getSlotaccount()){
				user.setSlotaccount(0.0);
				update(user);
			}
			
			QuotalRevision log = new QuotalRevision(); 
			log.setLoginname(lockedUser.getLoginname());
			log.setType(CreditChangeType.CHANGE_QUOTALSLOT.getCode());
			log.setCredit(user.getSlotaccount());
			log.setRemit(remit);
			log.setNewCredit(user.getSlotaccount()+remit);
			log.setRemark(remark);
			log.setCreatetime(DateUtil.getCurrentTimestamp());
			log.setExamine(0);
			log.setOperator(operator);
			log.setIp(ip);
			//log.setBillno(billno);
			save(log);
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
