package dfh.icbc.getdata.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.nnti.office.model.log.Operationlogs;

import dfh.exception.GenericDfhRuntimeException;
import dfh.icbc.getdata.dao.IGetdataDao;
import dfh.model.AbcTransfers;
import dfh.model.AgConfig;
import dfh.model.AgDataRecord;
import dfh.model.AlipayTransfers;
import dfh.model.BankCreditlogs;
import dfh.model.Bankinfo;
import dfh.model.CmbTransfers;
import dfh.model.Creditlogs;
import dfh.model.DepositOrder;
import dfh.model.DepositWechat;
import dfh.model.IcbcTransfers;
import dfh.model.JCProfitData;
import dfh.model.Mcquota;
import dfh.model.PlatformData;
import dfh.model.PtDataNew;
import dfh.model.PtProfit;
import dfh.model.Seq;
import dfh.model.Task;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.ValidateAmountDeposit;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.HttpUtils;
import dfh.utils.TlyDepositUtil;

public class GetdataDao extends HibernateDaoSupport  implements IGetdataDao{

	private static Logger log = Logger.getLogger(GetdataDao.class);

	@Override
	public List findByCriteria(DetachedCriteria criteria) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().findByCriteria(criteria);
	}


	@Override
	public void saveFanyaLogAll(List list) {
		getHibernateTemplate().saveOrUpdateAll(list);
	}
	

	@Override
	public Object save(Object o) {
		// TODO Auto-generated method stub
		 return getHibernateTemplate().save(o);
	}

	@Override
	public void saveOrUpdate(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
		
	}

	@Override
	public void update(Object o) {
		//getHibernateTemplate().clear();
		getHibernateTemplate().update(o);
		//getHibernateTemplate().flush();
		
	}

	@Override
	public Object get(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	@Override
	public void changeCredit(Users lockedUser, Double remit,
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
			
			// 代理的额度允许为负
			if ((lockedUser.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(lockedUser.getRole(), "MONEY_CUSTOMER")) {
				msg = "额度不足,当前额度:" + lockedUser.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}else{
				
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
				
				updateUserCreditSql(lockedUser, remit);
				
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
//					forceClientToRefreshSession(lockedUser.getLoginname());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Override
	public void changeCreditBySql(Users lockedUser, Double remit,
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
			
			// 代理的额度允许为负
			if ((lockedUser.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(lockedUser.getRole(), "MONEY_CUSTOMER")) {
				msg = "额度不足,当前额度:" + lockedUser.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}else{
				
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

				updateUserCreditSql(lockedUser, remit);
				
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
//					forceClientToRefreshSession(lockedUser.getLoginname());
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

	@Override
	public void changeAmountByName(String username, double amount,String pno) {
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("username", username));
		//c.add(Restrictions.or(Restrictions.eq("type", 1),Restrictions.eq("type", 7)));
		c.add(Restrictions.in("type", new Integer[]{1, 7, 9}));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos =getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		if (bankinfos.size()!=0) {
			bankinfo = bankinfos.get(0);
			Double credit = bankinfo.getAmount();
			updateBankAmountSql(amount, bankinfo.getId());
			//添加银行额度流水
			BankCreditlogs bankCreditlogs = new BankCreditlogs();
			bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
			bankCreditlogs.setBankname(username);
			bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.CASHIN.getCode());
			bankCreditlogs.setNewCredit(credit+amount);
			bankCreditlogs.setCredit(credit);
			bankCreditlogs.setRemit(amount);
			bankCreditlogs.setRemark("referenceNo:"+pno);
			save(bankCreditlogs);
		}else {
			System.out.println("无此username："+username+ "对应的银行帐号");
			log.info("无此username："+username+ "对应的银行帐号");
		}
	}
	
	@Override
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
			if(null != slotAccount && null == slotAccount.getSlotaccount()){
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
				updateSlotAccountSql(slotAccount, remit);
				
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

	
	@Override
	public void changeAmountByNameSql(String username, double amount,String pno) {
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("username", username));
		c.add(Restrictions.in("type", new Integer[]{1, 7, 9}));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos =getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				bankinfo.setAmount(credit+amount);
//				update(bankinfo);
				updateBankAmountSql(amount, bankinfo.getId());
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(username);
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.CASHIN.getCode());
				bankCreditlogs.setNewCredit(credit+amount);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				
			}else {
				System.out.println("无此username："+username+ "对应的银行帐号");
				log.info("无此username："+username+ "对应的银行帐号");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}

	@Override
	public void insertOperationLog(String loginname, String action, String remark) {
		Operationlogs log = new Operationlogs();
		log.setLoginname(loginname);
		log.setAction("EXCUTE");
		log.setRemark(remark);
		log.setCreatetime(DateUtil.getCurrentTimestamp());
		save(log);
		
	}

	@Override
	public void excuteTask(String pno, String operator, String ip) {
		Task excute= getTask(pno,"EXCUTE");
		if (excute == null){
			throw new GenericDfhRuntimeException("找不到对应提案任务");
		}else{
			excute.setAgreeIp(ip);
			excute.setManager(operator);
			excute.setFlag(1);
			excute.setAgreeTime(DateUtil.getCurrentTimestamp());
			update(excute);
			return;
		}
		
	} 
	
	public Task getTask(String pno, String type) {
		if (pno == null || type == null)
			return null;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Task.class);
		dCriteria = dCriteria.add(Restrictions.eq("pno", pno)).add(Restrictions.eq("level", type));
		List list = findByCriteria(dCriteria);
		if (list.size() == 0)
			return null;
		else
			return (Task) list.get(0);
	}

	@Override
	public String generateProposalPno(String type) {
		/*String id = "";
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_PROPOSALID, LockMode.UPGRADE);
//		if (seq != null) {
//			String seqvalue = seq.getSeqValue();
//			if (seqvalue.toString().startsWith(DateUtil.getYYMMDD()))
//				id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.parseInt(seqvalue.substring(6)) + 1, PROPOSAL_DIGITS);
//			else
//				id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.valueOf(1), PROPOSAL_DIGITS);
//			seq.setSeqValue(id);
//			update(seq);
//		} else {
//			seq = new Seq();
//			seq.setSeqName(Constants.SEQ_PROPOSALID);
//			id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.valueOf(1), PROPOSAL_DIGITS);
//			seq.setSeqValue(id);
//			save(seq);
//		}
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_PROPOSALID);
			seq.setSeqValue("1510400000");
			save(seq);
		}
		return  type+seq.getSeqValue();*/
		return  type + DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}
	
	
	public String generateProposalIcbcPno(String type) {
		/*Seq seq = (Seq) get(Seq.class, "SEQ_ICBC", LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_ICBC");
			seq.setSeqValue("1");
			save(seq);
		}
		return type+seq.getSeqValue();*/
		return  type+DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(8).toLowerCase();
	}
	
	
	public String generateProposalAlipayPno(String type) {
		return type + DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(8).toLowerCase();
	}
	
	@Override
	public Date getDatabaseNow(){
		Date date = (Date) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("select now()");
				return sqlQuery.uniqueResult();
			}
		});
		if (date != null) {
			return date;
		}
		return null;
	}

	@Override
	public void generateTasks(String pno, String operator) {
		log.info((new StringBuilder("generate tasks for proposal:")).append(pno).toString());
		Task audit = new Task(pno,"AUDIT",0,DateUtil.now(),null,operator);
		save(audit);
		
		Task excute = new Task(pno,"EXCUTE",0,DateUtil.now(),null,operator);
		save(excute);
		
	}

	@Override
	public Object get(Class clazz, Serializable id, LockMode mode) {
		return getHibernateTemplate().get(clazz, id, mode);
	}
	
	@Override
	public List<Creditlogs> getCashInToday(String loginname) {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Creditlogs.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", "BANK_TRANSFER_CONS")).add(Restrictions.eq("loginname", loginname))
				.add(Restrictions.ge("createtime", DateUtil.getToday()));
		//System.out.println(dCriteria.toString());
		return getHibernateTemplate().findByCriteria(dCriteria);
	}
	
	public static void main(String[] args) {
		new GetdataDao().forceClientToRefreshSession("woodytest");
	}



	@Override
	public IcbcTransfers getIcbcDataBySql() {
		return (IcbcTransfers)this.getSession().createSQLQuery(
				"select * from `icbc_transfers` where `status`=0 limit 1").addEntity(IcbcTransfers.class).uniqueResult();
	}


	@Override
	public CmbTransfers getCmbDataBySql() {
		return (CmbTransfers)this.getSession().createSQLQuery(
		"select * from `cmb_transfers` where `status`=0 limit 1").addEntity(CmbTransfers.class).uniqueResult();
	}



	@Override
	public AbcTransfers getAbcDataBySql() {
		return (AbcTransfers)this.getSession().createSQLQuery(
		"select * from `abc_transfers` where `status`=0 limit 1").addEntity(AbcTransfers.class).uniqueResult();
	}
	
	
	@Override
	public AlipayTransfers getAlipayDataBySql(){
		return (AlipayTransfers)this.getSession().createSQLQuery(
				"select * from `alipay_transfers` where `status`=0 limit 1").addEntity(AlipayTransfers.class).uniqueResult();
	}


	@Override
	public ValidateAmountDeposit getValidateAmountDeposit() {
		return (ValidateAmountDeposit)this.getSession().createSQLQuery(
		"select * from `validateamount_transfer` where `status`=0 limit 1").addEntity(ValidateAmountDeposit.class).uniqueResult();
	}
	
	@Override
	public void processPtProfit(Users ptUser, Double betCredit, Double payout,
			Date starttime, Date endtime) {
		String sql=" insert into ptprofit(uuid,userId,amount,betCredit,starttime,endtime,loginname,payOut) values "+
				"('"+UUID.randomUUID().toString()+"',?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE "+
				"amount=VALUES(amount),betCredit=VALUES(betCredit),payOut=VALUES(payOut)";
		Session session = this.getSession();
		session.createSQLQuery(sql).setParameter(0, ptUser.getId()).setDouble(
				1, betCredit-payout).setDouble(2, betCredit).setTimestamp(3, starttime)
				.setTimestamp(4, endtime).setString(5, ptUser.getLoginname())
				.setDouble(6, payout).executeUpdate();
	}	
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public void processNTProfit(String loginname, Double betCredit, Double payout,Date starttime, Date endtime) {
		Session session = this.getSession();
		DetachedCriteria exist = DetachedCriteria.forClass(PtProfit.class);
		exist.add(Restrictions.eq("loginname", loginname));
		exist.add(Restrictions.eq("starttime", starttime));
		exist.add(Restrictions.eq("endtime", endtime));
		List<PtProfit> ppl = this.getHibernateTemplate().findByCriteria(exist);
		int i=0;
		if (null==ppl || ppl.isEmpty()){
			/*String u_sql="select u1.loginname from Users u1 where u1.id=?";
			String loginname = session.createQuery(u_sql).setParameter(0, id).uniqueResult().toString();*/
			String id = DateUtil.fmtyyyyMMdd(starttime)+loginname;
			String sql=" insert into ptprofit(uuid,amount,betCredit,starttime,endtime,loginname,payOut) values "+
					"('"+id+"',?,?,?,?,?,?)"; //ON DUPLICATE KEY UPDATE "+
					//"amount=VALUES(amount),betCredit=VALUES(betCredit),payOut=VALUES(payOut)";
			i=session.createSQLQuery(sql).setDouble(0, betCredit-payout).setDouble(1, betCredit).setTimestamp(2, starttime)
					.setTimestamp(3, endtime).setString(4, loginname)
					.setDouble(5, payout).executeUpdate();
		} else {
			String uid = ppl.get(0).getUuid();
			String sql="update ptprofit set amount=?,betCredit=?,payOut=? where loginname=? and starttime=? and endtime=?";
			i=session.createSQLQuery(sql).setDouble(0, betCredit-payout).setDouble(1, betCredit)
				.setDouble(2, payout).setString(3, loginname).setTimestamp(4, starttime)
				.setTimestamp(5, endtime).executeUpdate();
		}
		if (i<=0){
			log.error((ppl.isEmpty()?"插入":"更新")+"NT平台数据失败,更新条数为"+i+",用户:"+loginname);
		}
	}
	
	public Boolean processNewPtProfit(String PLAYERNAME, String CODE, String CURRENCYCODE, String ACTIVEPLAYERS, String BALANCECHANGE, String DEPOSITS, String WITHDRAWS, String BONUSES, String COMPS, String PROGRESSIVEBETS, String PROGRESSIVEWINS, String BETS, String WINS, String NETLOSS, String NETPURCHASE, String NETGAMING, String HOUSEEARNINGS, String RNUM, String DATATIME) {
		try {
			String startTime = DATATIME + " 00:00:00";
			String endTime = DATATIME + " 23:59:59";
			String ipPvSql = "SELECT id,PLAYERNAME FROM pt_data WHERE PLAYERNAME=? AND STARTTIME=? AND ENDTIME=?";
			Query ipQuery = this.getSession().createSQLQuery(ipPvSql);
			ipQuery.setParameter(0, PLAYERNAME).setParameter(1, startTime).setParameter(2, endTime);
			List ipList = ipQuery.list();
			if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
				Object[] objects0 = (Object[]) ipList.get(0);
				if (objects0[0] != null) {
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Integer id = Integer.parseInt(objects0[0].toString());
					String sql = " UPDATE pt_data SET " + " PLAYERNAME='" + PLAYERNAME + "',CODE='" + CODE + "',CURRENCYCODE='" + CURRENCYCODE + "',ACTIVEPLAYERS='" + ACTIVEPLAYERS + "',BALANCECHANGE='" + Double.parseDouble(BALANCECHANGE) + "',DEPOSITS='" + Double.parseDouble(DEPOSITS) + "',WITHDRAWS='" + Double.parseDouble(WITHDRAWS) + "',BONUSES='" + Double.parseDouble(BONUSES) + "',COMPS='" + Double.parseDouble(COMPS) + "',PROGRESSIVEBETS='" + Double.parseDouble(PROGRESSIVEBETS) + "',PROGRESSIVEWINS='" + Double.parseDouble(PROGRESSIVEWINS) + "',BETS='" + Double.parseDouble(BETS) + "',WINS='" + Double.parseDouble(WINS) + "',NETLOSS='" + Double.parseDouble(NETLOSS) + "',NETPURCHASE='" + Double.parseDouble(NETPURCHASE) + "',NETGAMING='" + Double.parseDouble(NETGAMING) + "',HOUSEEARNINGS='" + Double.parseDouble(HOUSEEARNINGS) + "',RNUM='" + RNUM+ "',CREATTIME='" + sf.format(new Date()) + "' WHERE ID='" + id + "'";
					this.getSession().createSQLQuery(sql).executeUpdate();
					//log.info("*****更新********" + sql + "******************");
				}
			} else {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sql = "INSERT INTO pt_data(" + " PLAYERNAME,CODE,CURRENCYCODE,ACTIVEPLAYERS,BALANCECHANGE,DEPOSITS,WITHDRAWS,BONUSES,COMPS,PROGRESSIVEBETS,PROGRESSIVEWINS,BETS,WINS,NETLOSS,NETPURCHASE,NETGAMING,HOUSEEARNINGS,RNUM,STARTTIME,ENDTIME,CREATTIME)" + " values ('" + PLAYERNAME + "','" + CODE + "','" + CURRENCYCODE + "','" + ACTIVEPLAYERS + "','" + Double.parseDouble(BALANCECHANGE) + "','" + Double.parseDouble(DEPOSITS) + "','" + Double.parseDouble(WITHDRAWS) + "','" + Double.parseDouble(BONUSES) + "','" + Double.parseDouble(COMPS) + "','" + Double.parseDouble(PROGRESSIVEBETS) + "','" + Double.parseDouble(PROGRESSIVEWINS) + "','" + Double.parseDouble(BETS) + "','" + Double.parseDouble(WINS) + "','" + Double.parseDouble(NETLOSS) + "','" + Double.parseDouble(NETPURCHASE) + "','" + Double.parseDouble(NETGAMING) + "','" + Double.parseDouble(HOUSEEARNINGS) + "','" + RNUM + "','" + startTime + "','" + endTime + "','" + sf.format(new Date()) + "')";
				//log.info("*************" + sql + "******************");
				this.getSession().createSQLQuery(sql).executeUpdate();
				//log.info("*****新增********" + sql + "******************");
			}
			return true;
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public Boolean dealNewPtDataDao( String PLAYERNAME,String FULLNAME,String VIPLEVEL,String COUNTRY,String GAMES,String CURRENCYCODE,String BETS,String WINS,String INCOME,String RNUM,String DATATIME) {
		try {
			String startTime = DATATIME + " 00:00:00";
			String endTime = DATATIME + " 23:59:59";
			String ipPvSql = "SELECT id,PLAYERNAME FROM pt_data_new WHERE PLAYERNAME=? AND STARTTIME=? AND ENDTIME=? ";
			Query ipQuery = this.getSession().createSQLQuery(ipPvSql);
			ipQuery.setParameter(0, PLAYERNAME).setParameter(1, startTime).setParameter(2, endTime);
			List ipList = ipQuery.list();
			if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
				Object[] objects0 = (Object[]) ipList.get(0);
				if (objects0[0] != null) {
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Integer id = Integer.parseInt(objects0[0].toString());
					String sql = " UPDATE pt_data_new SET " + " PLAYERNAME='" + PLAYERNAME + "',FULLNAME='" + FULLNAME + "',VIPLEVEL='" + VIPLEVEL + "',COUNTRY='" + COUNTRY + "',GAMES='" + GAMES + "',CURRENCYCODE='" + CURRENCYCODE + "',BETS='" + Double.parseDouble(BETS) + "',WINS='" + Double.parseDouble(WINS) + "',INCOME='" + INCOME + "',RNUM='" + RNUM  + "' WHERE ID=" + id ;
					this.getSession().createSQLQuery(sql).executeUpdate();
//					log.info(Type+"*****更新********" + sql + "******************");
				}
			} else {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sql = "INSERT INTO pt_data_new( PLAYERNAME,FULLNAME,VIPLEVEL,COUNTRY,GAMES,CURRENCYCODE,BETS,WINS,INCOME,RNUM,STARTTIME,ENDTIME)" + " values ('" + PLAYERNAME + "','" + FULLNAME + "','" + VIPLEVEL + "','" + COUNTRY + "','" + GAMES + "','" + CURRENCYCODE + "','" + Double.parseDouble(BETS) + "','" + Double.parseDouble(WINS) + "','" + INCOME + "','" + Integer.parseInt(RNUM) + "','" + startTime + "','" + endTime + "')";
				this.getSession().createSQLQuery(sql).executeUpdate();
//				log.info(Type+"*****新增********" + sql + "******************");
			}
			return true;
		}  catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	@Override
	public Boolean updateNewPtData(String PLAYERNAME,String DATATIME, Double betsTiger,
			Double winsTiger) {
		String startTime = DATATIME + " 00:00:00";
		String endTime = DATATIME + " 23:59:59";
		String ipPvSql = "SELECT id,PLAYERNAME FROM pt_data_new WHERE PLAYERNAME=? AND STARTTIME=? AND ENDTIME=? ";
		Query ipQuery = this.getSession().createSQLQuery(ipPvSql);
		ipQuery.setParameter(0, PLAYERNAME).setParameter(1, startTime).setParameter(2, endTime);
		List ipList = ipQuery.list();
		
		if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
			Object[] objects0 = (Object[]) ipList.get(0);
			if (objects0[0] != null) {
				Integer id = Integer.parseInt(objects0[0].toString());
				String sql = " UPDATE pt_data_new SET " + " BETS_TIGER='" + betsTiger + "',WINS_TIGER='" + winsTiger  + "' WHERE ID=" + id ;
				this.getSession().createSQLQuery(sql).executeUpdate();
			}
		} else {
			String sql = "INSERT INTO pt_data_new( PLAYERNAME,BETS_TIGER,WINS_TIGER,STARTTIME,ENDTIME)" + " values ('" + PLAYERNAME +"',"+betsTiger+","+ winsTiger +",'" + startTime + "','" + endTime + "')";
			this.getSession().createSQLQuery(sql).executeUpdate();
		}
		return null;
	}
	
	@Override
	public boolean updateSixLotteryPlatForm(PlatformData data) {
		String querysql = "SELECT * FROM platform_data where platform='sixlottery' and starttime='"+DateUtil.formatDateForStandard(data.getStarttime())+"' and loginname='"+data.getLoginname()+"'" ;
		Query query = this.getSession().createSQLQuery(querysql);
		List ipList = query.list();
		int flag = -1 ;
		if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
			Object[] objects0 = (Object[]) ipList.get(0);
			String uuid = objects0[0].toString();
			String updatesql = "UPDATE platform_data set bet=? , profit=? , updatetime=? where uuid=?" ;
			Query updateQuery = this.getSession().createSQLQuery(updatesql);
			updateQuery.setParameter(0, data.getBet()).setParameter(1, data.getProfit()).setParameter(2, DateUtil.fmtyyyyMMddHHmmss(new Date())).setParameter(3, uuid) ;
			flag = updateQuery.executeUpdate() ;
		}else {
			String insertSql = "INSERT INTO platform_data( uuid , platform , loginname , bet , profit , starttime , endtime , updatetime) values(?,?,?,?,?,?,?,?)" ;
			Query insertQuery = this.getSession().createSQLQuery(insertSql);
			insertQuery.setParameter(0, data.getUuid()).setParameter(1, data.getPlatform())
						.setParameter(2, data.getLoginname()).setParameter(3, data.getBet())
						.setParameter(4, data.getProfit()).setParameter(5, data.getStarttime())
						.setParameter(6, data.getEndtime()).setParameter(7, DateUtil.fmtyyyyMMddHHmmss(new Date())) ;
			flag = insertQuery.executeUpdate() ;
		}
		return flag==1?true:false;
	}
	
	@Override
	public boolean updateQtPlatForm(PlatformData data) {
		log.info("qt更新数据："+data.getLoginname());
		String querysql = "SELECT * FROM platform_data where platform='qt' and starttime='"+DateUtil.formatDateForStandard(data.getStarttime())+"' and loginname='"+data.getLoginname()+"'" ;
		Query query = this.getSession().createSQLQuery(querysql);
		List ipList = query.list();
		int flag = -1 ;
		if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
			Object[] objects0 = (Object[]) ipList.get(0);
			String uuid = objects0[0].toString();
			String updatesql = "UPDATE platform_data set bet=? , profit=? , updatetime=? where uuid=?" ;
			Query updateQuery = this.getSession().createSQLQuery(updatesql);
			updateQuery.setParameter(0, data.getBet()).setParameter(1, data.getProfit()).setParameter(2, DateUtil.fmtyyyyMMddHHmmss(new Date())).setParameter(3, uuid) ;
			flag = updateQuery.executeUpdate() ;
			if(ipList.size()>1){
				String delsql = "DELETE FROM platform_data where platform='qt' and starttime='"+DateUtil.formatDateForStandard(data.getStarttime())+"' and loginname='"+data.getLoginname()+"' and uuid!=?";
				Query delQuery = this.getSession().createSQLQuery(delsql);
				delQuery.setParameter(0, uuid);
				flag = delQuery.executeUpdate();
			}
		}else {
			String insertSql = "INSERT INTO platform_data( uuid , platform , loginname , bet , profit , starttime , endtime , updatetime) values(?,?,?,?,?,?,?,?)" ;
			Query insertQuery = this.getSession().createSQLQuery(insertSql);
			insertQuery.setParameter(0, data.getUuid()).setParameter(1, data.getPlatform())
						.setParameter(2, data.getLoginname()).setParameter(3, data.getBet())
						.setParameter(4, data.getProfit()).setParameter(5, data.getStarttime())
						.setParameter(6, data.getEndtime()).setParameter(7, DateUtil.fmtyyyyMMddHHmmss(new Date())) ;
			flag = insertQuery.executeUpdate() ;
		}
		return flag==1?true:false;
	}
	
	public List<PlatformData> selectQtData(Date startT, Date endT){
		String querysql = "SELECT playerid, sum(bet), sum(payout) FROM qtdata where status='COMPLETED' and completed >='"+DateUtil.formatDateForStandard(startT)+"' and completed <='"+DateUtil.formatDateForStandard(endT)+"' group by playerid";
		Query query = this.getSession().createSQLQuery(querysql);
		List ipList = query.list();
		List<PlatformData> list = new ArrayList<PlatformData>();
		if (ipList != null && ipList.size() > 0) {
			for(int i=0; i<ipList.size(); i++){
				Object[] objects = (Object[]) ipList.get(i);
				Double realBet = Double.valueOf(objects[1].toString());
				Double payout = Double.valueOf(objects[2].toString());
				PlatformData bean = new PlatformData();
				bean.setLoginname(objects[0].toString());
				bean.setBet(realBet);
				bean.setProfit(realBet-payout);
				list.add(bean);
			}
		}
		return list;
	}



	@Override
	public int discardValidateAmountOrder() {
		String sql = "update payorder_validation set status = :newStatus where status = :oldStatus and UNIX_TIMESTAMP(createtime) <= UNIX_TIMESTAMP(:preDay)";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("newStatus", "2");  //设置为过期失效
		params.put("oldStatus", "0");

		Calendar calendar = Calendar.getInstance();  //得到日历
		calendar.setTime(new Date());//把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为前一天
		//calendar.add(Calendar.HOUR_OF_DAY, -2);		//设置为前两小时
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
		String dBefore = sdf.format(calendar.getTime());    //格式化前一天
		params.put("preDay", dBefore);
		
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.executeUpdate();
	}
	
	public int updateUserCreditSql(Users user,Double remit){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set credit = credit + ?  WHERE loginname=? ");
		insertQuery.setParameter(0, remit).setParameter(1, user.getLoginname());
		return insertQuery.executeUpdate() ;
	}
	
	public void updateSlotAccountSql(Userstatus slotAccount,Double remit){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE userstatus set slotaccount = slotaccount + ?  WHERE loginname=? ");
		insertQuery.setParameter(0, remit).setParameter(1, slotAccount.getLoginname());
		insertQuery.executeUpdate() ;
	}
   
   public int updateBankAmountSql(Double amount,Integer id){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE bankinfo set amount = amount + ?  WHERE id=? ");
		insertQuery.setParameter(0, amount).setParameter(1, id);
		return insertQuery.executeUpdate() ;
   }
   
   @Override
	public boolean insertJCData(List<JCProfitData> jclist) {
		boolean rs = false;
		try {
			Session session = this.getSession();
			int batch = 0;
			String sqls = "insert into jc_data(PLAYERNAME,STARTTIME,ENDTIME,ORDERSUM,ACTUAL,BONUS,WIN,CREATETIME,UPDATETIME) values";
			StringBuffer sqle = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			for (int j=0;j<jclist.size();j++) {
				//session.save(jcd);
				sqle.append("(?,?,?,?,?,?,?,?,?),");
				params.add(jclist.get(j).getPlayerName());
				params.add(jclist.get(j).getStartTime());
				params.add(jclist.get(j).getEndTime());
				params.add(jclist.get(j).getOrderSum());
				params.add(jclist.get(j).getActual());
				params.add(jclist.get(j).getBonus());
				params.add(jclist.get(j).getWin());
				params.add(DateUtil.now());
				params.add(DateUtil.now());
				batch++;
				if (batch%20==0 || j+1 == jclist.size()){
					Query query = session.createSQLQuery(sqls+sqle.substring(0,sqle.length()-1)+";");
					for (int i = 0; i < batch; i++) {
						query.setParameter(i*9, params.get(i*9));
						query.setParameter(i*9+1, params.get(i*9+1));
						query.setParameter(i*9+2, params.get(i*9+2));
						query.setParameter(i*9+3, params.get(i*9+3));
						query.setParameter(i*9+4, params.get(i*9+4));
						query.setParameter(i*9+5, params.get(i*9+5));
						query.setParameter(i*9+6, params.get(i*9+6));
						query.setParameter(i*9+7, params.get(i*9+7));
						query.setParameter(i*9+8, params.get(i*9+8));
					}
					int i = query.executeUpdate();
					if (i <= 0){
						log.error("Getdatadao --> insertJCData batch insert change row 0");
						return false;
					}
					sqle = new StringBuffer();
					batch=0;
					params = new ArrayList<Object>();
				}
				
			}
			rs = true;
		} catch (Exception e) {
			log.error("Getdatadao --> insertJCData batch insert:",e);
		}
		return rs;
	}
	
	public List<String> queryJCDataByDate(String executeTime) {
		List<String> rl = new ArrayList<String>();
		String sql = "select jd.playerName from JCProfitData jd where DATE_FORMAT(jd.startTime,'%Y-%m-%d')='"+executeTime+"'";
		try {
			rl = getHibernateTemplate().find(sql);
		} catch (Exception e) {
			log.error("Getdatadao --> queryJCDataByDate error:",e);
		}
		return rl;
	}
	
	@Override
	public boolean updateJCData(JCProfitData jcp) {
		StringBuffer sql = new StringBuffer("update jc_data jd set jd.ORDERSUM=:ordersum,jd.ACTUAL=:actual,jd.BONUS=:bonus,jd.WIN=:win,jd.UPDATETIME=:updatetime ");
		sql.append("where jd.PLAYERNAME=:playername and DATE_FORMAT(jd.STARTTIME,'%Y-%m-%d')=:starttime");
		try {
			Query query = this.getSession().createSQLQuery(sql.toString());
			query.setParameter("ordersum", jcp.getOrderSum()).setParameter("actual", jcp.getActual()).setParameter("bonus", jcp.getBonus()).setParameter("win", jcp.getWin())
					.setParameter("updatetime", DateUtil.now()).setParameter("playername", jcp.getPlayerName()).setParameter("starttime", DateUtil.fmtYYYY_MM_DD(jcp.getStartTime()));
			return query.executeUpdate()>0?true:false;
		} catch (Exception e) {
			log.error("Getdatadao --> updateJCData error",e);
		}
		return false;
	}
	
	public String getAgSlotLastTime(String platform){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sfs = new SimpleDateFormat("yyyy-MM-dd");
		AgConfig agConfig=(AgConfig) get(AgConfig.class, platform,LockMode.UPGRADE);
		if (agConfig==null) {
         Calendar calendarLastTime=Calendar.getInstance();		
         calendarLastTime.add(Calendar.DAY_OF_MONTH, -2);
	     return sfs.format(calendarLastTime.getTime())+" 00:00:00";
		}else {
			return sf.format(agConfig.getLastupdateTime());
		}
	}
	
   public void processNewAgSlotProfit(Map<String,AgDataRecord>data,Date lastTime,String platformType) {
		
		try{
			log.info("total 录入数据 size:"+data.size());
			for (String key : data.keySet()) {
				    AgDataRecord agDataRecord=data.get(key);
					save(agDataRecord);
					log.info("更新用户:"+agDataRecord.getPlayerName()+" ----投注次数:"+agDataRecord.getBetcount()+" ----开始时间:"+agDataRecord.getStartTime());
			}
			
			AgConfig agConfig=(AgConfig) get(AgConfig.class, platformType,LockMode.UPGRADE);
			
			if (agConfig==null) 
				save(new AgConfig(platformType, 0, new Date(), lastTime));
			else {
				agConfig.setLastupdateTime(lastTime);
	      	    update(agConfig);
			}
				
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
   
	
	public void updateUserIsCashInSql(String loginname){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set isCashin=0  WHERE loginname=? ");
		insertQuery.setParameter(0, loginname);
		insertQuery.executeUpdate() ;
    }

	public void saveOrUpdateEBetAppProfit(PlatformData profit) {
		DetachedCriteria queryByName = DetachedCriteria.forClass(PlatformData.class);
		queryByName.add(Restrictions.eq("loginname", profit.getLoginname()));
		queryByName.add(Restrictions.eq("starttime", profit.getStarttime()));
		queryByName.add(Restrictions.eq("endtime", profit.getEndtime()));

		List<PlatformData> profits = this.getHibernateTemplate().findByCriteria(queryByName);
		if (null == profits || profits.isEmpty()) {
			getHibernateTemplate().saveOrUpdate(profit);
		} else {
			PlatformData oldRecord = profits.get(0);
			oldRecord.setProfit(profit.getProfit());
			oldRecord.setBet(profit.getBet());
			oldRecord.setEndtime(profit.getEndtime());
			oldRecord.setUpdatetime(new Date());
			getHibernateTemplate().saveOrUpdate(oldRecord);
		}
		getHibernateTemplate().flush();
	}


	
	public DepositWechat getValidateWechatDeposit(){ 
		return (DepositWechat)this.getSession().createSQLQuery(
				"select * from `deposit_wechat` where `state`=0 limit 1").addEntity(DepositWechat.class).uniqueResult();
	}
	public void changeWechatAmountOnline(String wxh,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("accountno", wxh));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				/*bankinfo.setAmount(bankinfo.getAmount()+amount);
				update(bankinfo);*/
				updateBankAmountSql(amount, bankinfo.getId());
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	
	  /****
	   *  同略云
	    * 更新提案记录		   
	    */
	   public void updateProposalSql(String flag ,String mstype, String bankName, String pno,String overTime ){
			Query insertQuery = this.getSession().createSQLQuery("UPDATE proposal  set  flag=?,mstype=?,executeTime=?,bankaccount=?,overtime=? WHERE pno=? ");
			insertQuery.setParameter(0, flag).setParameter(1, mstype).setParameter(2, new Date()).setParameter(3, bankName).setParameter(4, overTime).setParameter(5, pno);
			insertQuery.executeUpdate() ;
		}	   
			   
	   /****
	    * 更新玩家账户额度
	    */
		public void updateUserCreditSql(String loginname , Double remit ){
			Query insertQuery = this.getSession().createSQLQuery("UPDATE users set credit = credit + ? WHERE loginname=? ");
			insertQuery.setParameter(0, remit).setParameter(1, loginname);
			insertQuery.executeUpdate() ;
		}
		
		/****
		    * 更新代理Userstatus
		*/
		public void updateUserstatusSql(String loginname , Double remit ){
			Query insertQuery = this.getSession().createSQLQuery("UPDATE userstatus set slotaccount = slotaccount + ? WHERE loginname=? ");
			insertQuery.setParameter(0, remit).setParameter(1, loginname);
			insertQuery.executeUpdate() ;
		}
		
	    /****
	    *  添加额度记录
	    */
		public void insertCreditLog(String loginname ,String type,Double credit,Double newCredit, Double remit,String remark ){
			Creditlogs log = new Creditlogs();
			log.setLoginname(loginname);
			log.setType(type);
			log.setCredit(credit);
			log.setRemit(remit);
			log.setNewCredit(newCredit);
			log.setRemark(remark);
			log.setCreatetime(DateUtil.getCurrentTimestamp());
			save(log);
		}
		
		   /****
		    *  添加银行额度记录
		    */
			public void insertBankCreditLog(String bankname ,String type,Double credit,Double newCredit, Double remit,String pno ){
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankname);
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.CASHOUT.getCode());
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setNewCredit(credit - remit);
				bankCreditlogs.setRemit(-1*remit); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
			}
			
		   /****
		    * 更新银行 本地和远程
		    */
			public void updateBankinfoSql(Double remit,Double bankamount,String bankuser, String bankname ){
				Query insertQuery = this.getSession().createSQLQuery("UPDATE bankinfo set amount = amount - ?,bankamount = ? WHERE username=? and bankname=? and useable=0 ");
				insertQuery.setParameter(0, remit).setParameter(1, bankamount).setParameter(2, bankuser).setParameter(3, bankname);
				insertQuery.executeUpdate() ;
			}
			
			/***
			 * 
			 * 已审核 待执行
			 */
			public void updateProposalFlagSql(String pno) {
				Query insertQuery = this.getSession().createSQLQuery("UPDATE proposal  set  unknowflag=2, flag=1 WHERE pno=? ");
				insertQuery.setParameter(0, pno);
				insertQuery.executeUpdate() ;
			}
	
		public String getOverTime(Integer mssflag ){
			  Date date = new Date();
			  Date dateTimeCreate =date;
			  String overTime = "0";
		      if ((date.getTime() - 60) >dateTimeCreate.getTime() && mssflag==0){
		    	  overTime = "1";
		      }
		      if ((date.getTime() -300) >dateTimeCreate.getTime() && mssflag==1){
		    	  overTime = "1";
		      }
		      return overTime;
		}
		
	/***
	 * 秒存定时过期处理
	 */
	@Override
	public int discardValidateAmountOrderMc() {
		
//			String sql = "update deposit_order set status = :newStatus where status = :oldStatus and UNIX_TIMESTAMP(createtime) <= UNIX_TIMESTAMP(:preDay)";
		String sql = "update deposit_order set status = :newStatus where status = :oldStatus and createtime>=:timeStart and createtime <= :timeEnd ";
		log.info("执行作废sql：>>>"+sql);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("newStatus", "2");  //设置为过期失效
		params.put("oldStatus", "0");

		Calendar calendar = Calendar.getInstance();  //得到日历
		calendar.setTime(new Date());//把当前时间赋给日历
		//calendar.add(Calendar.DAY_OF_MONTH, -7);  //设置为前一天  
		calendar.add(Calendar.HOUR_OF_DAY, -12);		//设置为前两小时  
		//calendar.add(Calendar.MINUTE,-30);  
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
		String dBefore = sdf.format(calendar.getTime());    //格式化前一天
		params.put("timeEnd", dBefore);
		
		Calendar calendarStart = Calendar.getInstance();  //得到日历
		calendarStart.setTime(new Date());//把当前时间赋给日历
		calendarStart.add(Calendar.DAY_OF_MONTH, -2);//暂定2天
		String dStart = sdf.format(calendarStart.getTime());
		params.put("timeStart", dStart);
		
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.executeUpdate(); 
	}
	
	//清理微信转账额度
	@Override
	public int discardWxZzMcQuota() {
		
		Calendar calendar = Calendar.getInstance();  //得到日历
		calendar.setTime(new Date());//把当前时间赋给日历
		calendar.add(Calendar.HOUR_OF_DAY, 0);		//设置为前0小时
		calendar.add(Calendar.MINUTE,-5);		//设置为前3分钟
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式		
		//查询mcquota表的额度是否存在下单记录  不存在则释放额度
		DetachedCriteria c = DetachedCriteria.forClass(Mcquota.class);
		c.add(Restrictions.eq("status", 1));
		c.add(Restrictions.le("createtime", calendar.getTime()));
		List<Mcquota> mcquotas =getHibernateTemplate().findByCriteria(c);
		log.info("更新条数："+mcquotas.size());
		if(mcquotas != null && mcquotas.size()>0)
		{
			for(int i=0;i<mcquotas.size();i++)
			{
				Mcquota mcquota = (Mcquota)mcquotas.get(i);
				//查询订单表
				DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
				dc.add(Restrictions.eq("amount", mcquota.getAmount()));
				dc.add(Restrictions.eq("loginname", mcquota.getLoginname()));
				dc.add(Restrictions.eq("type","4"));  
				dc.add(Restrictions.eq("status",0));  
				List<DepositOrder> depositOrders =getHibernateTemplate().findByCriteria(dc);
				if(depositOrders == null || depositOrders.size()== 0){
					mcquota.setStatus(0);
					update(mcquota);
				}
			}
			
		}
		return 1;
	}
	
	//作废微信转账 \手机二维码\微信二维码收款\支付宝二维码收款
		@Override
		public int discardWxZzMcOrder() {
			
			String sql = "update deposit_order set status = :newStatus ,remark = :remark ,updatetime = NULL where status = :oldStatus  and createtime <= :timeStart and type in ('4','6','8','9','22')  ";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("newStatus", "2");  //设置为过期失效
			params.put("oldStatus", "0");
			params.put("remark", "系统作废订单，时间："+DateUtil.getNow());

			Calendar calendar = Calendar.getInstance();  //得到日历
			calendar.setTime(new Date());//把当前时间赋给日历
			calendar.add(Calendar.HOUR_OF_DAY, -4);		//设置为前两小时
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
			String dBefore = sdf.format(calendar.getTime());    //格式化前一天
			params.put("timeStart", dBefore);
			
			log.info("做废掉4分钟前创建的微信转账订单，时间条件值为：createtime<="+ dBefore);
			Query query = this.getSession().createSQLQuery(sql);
			query.setProperties(params);
			return query.executeUpdate(); 
		}
			
	/**
	 * 作废tly下单和撤销tly后台订单
	 */
	@Override
	public int discardTlyMcOrder() {

		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(new Date());// 把当前时间赋给日历
		calendar.add(Calendar.HOUR_OF_DAY, -4); // 设置为前两小时
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置时间格式
		String dBefore = sdf.format(calendar.getTime()); // 格式化前一天

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("oldStatus", "0");
		params.put("timeStart", dBefore);

		String selectSql = "select spare from deposit_order  where status = :oldStatus  and createtime <= :timeStart and type = '7'  ";
		Query selectQuery = this.getSession().createSQLQuery(selectSql);
		selectQuery.setProperties(params);
		List ipList = selectQuery.list();
		if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
			for (int i = 0; i < ipList.size(); i++) {
				String id = ipList.get(i).toString();
				try {
					TlyDepositUtil.revoke_order(id);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		String sql = "update deposit_order set status = :newStatus ,remark = :remark    ,updatetime = NULL  where status = :oldStatus  and createtime <= :timeStart and type = '7'  ";
		params.put("newStatus", "2");
		params.put("remark", "系统作废订单，时间：" + DateUtil.getNow());

		log.info("做废掉4小时前创建的同略云订单，时间条件值为：createtime<=" + dBefore);
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.executeUpdate();
	}
	
		
	public Double getDoubleValue(String sql, Map params) {
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		Object obj = query.uniqueResult();
		return null==obj?0.00:Double.parseDouble(obj.toString());
	}
	
	public String generateProposalCmbPno(String type) {
		return  type + DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(8).toLowerCase();
	}
	
	@Override
	public int changeAmountByIdSql(String username, double amount, String pno) {
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("username", username));
		c.add(Restrictions.in("type", new Integer[]{1, 7, 9}));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos =getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		if (bankinfos.size()!=0) {
			bankinfo = bankinfos.get(0);
			Double credit = bankinfo.getAmount();
//			bankinfo.setAmount(credit+amount);
//			update(bankinfo);
			
			//添加银行额度流水
			BankCreditlogs bankCreditlogs = new BankCreditlogs();
			bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
			bankCreditlogs.setBankname(username);
			bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.CASHIN.getCode());
			bankCreditlogs.setNewCredit(credit+amount);
			bankCreditlogs.setCredit(credit);
			bankCreditlogs.setRemit(amount);
			bankCreditlogs.setRemark("referenceNo:"+pno);
			
			save(bankCreditlogs);
			int rows = updateBankAmountSql(amount, bankinfo.getId());
			return rows;
		}else {
			System.out.println("无此username："+username+ "对应的银行帐号");
			log.info("无此username："+username+ "对应的银行帐号");
			return 0 ;
		}
	}
	
	@Override
	public void changeCreditForSecDeposit(Users lockedUser, Double remit, String creditLogType, String referenceNo, String remark) throws GenericDfhRuntimeException {
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
			
			// 代理的额度允许为负
			if ((lockedUser.getCredit().doubleValue() + remit.doubleValue() < 0.0 && remit.doubleValue() < 0.0) && StringUtils.equals(lockedUser.getRole(), "MONEY_CUSTOMER")) {
				msg = "额度不足,当前额度:" + lockedUser.getCredit();
				log.error(msg);
				throw new GenericDfhRuntimeException(msg);
			}else{
				
				Double credit = lockedUser.getCredit();
				Double newCredit = credit + remit;

				int rows = updateUserCreditSql(lockedUser, remit);
				
				log.info("用户："+lockedUser.getLoginname()+"  额度增加到："+newCredit);
				
				Creditlogs log = new Creditlogs();
				log.setLoginname(lockedUser.getLoginname());
				log.setType(creditLogType);
				log.setCredit(credit);
				log.setRemit(remit);
				log.setNewCredit(newCredit);
				log.setRemark(remark);
				log.setCreatetime(DateUtil.getCurrentTimestamp());
				if(rows == 1){
					save(log);
				}
				
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
	
	public List list(String sql, Map<String, Object> paramsMap) {
		
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setProperties(paramsMap);
		
		return query.list();
	}
	
	@Override
	public int excuteSql(String sql, Map<String, Object> params){
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.executeUpdate();
	}	
	
	@Override
	public void saveOrUpdateAll(List list) {
		getHibernateTemplate().saveOrUpdateAll(list);
	}

	@Override
	public List excuteQuerySql(String sql, Map<String, Object> params){
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.list();
	}

	@Override
	public Boolean dealAllPtData(String PLAYERNAME, String FULLNAME, String VIPLEVEL, String cOUNTRY, String gAMES,
			String cURRENCYCODE, String bETS, String wINS, String iNCOME, String rNUM, String executeTime,
			Double bETS_TIGER, Double wINS_TIGER, Double pROGRESSIVE_BETS, Double pROGRESSIVE_WINS,
			Double pROGRESSIVE_FEE, Date createTime, Date STARTTIME, Date ENDTIME)   {
		PtDataNew dataNew = new PtDataNew();
		dataNew.setPlayername(PLAYERNAME);
		dataNew.setFullname(FULLNAME);
		dataNew.setViplevel(VIPLEVEL);
		dataNew.setCountry(cOUNTRY);
		dataNew.setGames(gAMES);
		dataNew.setCurrencycode(cURRENCYCODE);
		dataNew.setBets(Double.parseDouble(bETS));
		dataNew.setWins(Double.parseDouble(wINS));
		dataNew.setIncome(Double.parseDouble(iNCOME));
		dataNew.setRnum(Integer.valueOf(rNUM));
		dataNew.setBetsTiger(bETS_TIGER);
		dataNew.setWinsTiger(wINS_TIGER);
		dataNew.setProgressiveBet(pROGRESSIVE_BETS);
		dataNew.setProgressiveWin(pROGRESSIVE_WINS);
		dataNew.setProgressiveFee(pROGRESSIVE_FEE);
		dataNew.setStarttime(STARTTIME);
		dataNew.setEndtime(ENDTIME);
		dataNew.setCreattime(createTime);
		this.getHibernateTemplate().save(dataNew);
		//this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql).executeUpdate();
		return true;
	}

}
