package dfh.service.implementations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.AgentReferralsVO;
import dfh.dao.GuestbookDao;
import dfh.dao.NetPayDao;
import dfh.dao.ProposalDao;
import dfh.dao.UniversalDao;
import dfh.model.ProxyFlow;
import dfh.model.Users;
import dfh.service.interfaces.ProxyFlowService;

public class ProxyFlowServiceImpl implements ProxyFlowService {
	
	private static Logger log = Logger.getLogger(ProxyFlowServiceImpl.class);

	public UniversalDao universalDao;

	public GuestbookDao guestbookDao;

	private ProposalDao proposalDao;

	private NetPayDao payorderDao;

	
	public List<ProxyFlow> getProxyFlowList(String loginname,String partner,Date start, Date end, Integer CountOfPage, Integer currentPage) {
		long startTimeend=System.nanoTime();   //获取开始时间
		try {
			java.text.DecimalFormat   df=new   java.text.DecimalFormat("#0.00");
			List<ProxyFlow> list = new ArrayList<ProxyFlow>();
			Session getSession = universalDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
			Session getSessionTwo = guestbookDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
			Criteria criteria = getSession.createCriteria(Users.class);
			criteria.add(Restrictions.eq("role", "AGENT"));
			criteria.add(Restrictions.eq("flag", 0));
			if(loginname!=null&&!loginname.equals("")){
				criteria.add(Restrictions.eq("loginname", loginname));
			}
			if (StringUtils.isNotEmpty(partner)) {
				DetachedCriteria pDC = DetachedCriteria.forClass(Users.class);
				pDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("partner", partner));
				criteria.add(Property.forName("loginname").in(pDC));
			}
			criteria.add(Restrictions.isNotNull("referWebsite"));
			criteria.add(Restrictions.ne("referWebsite", ""));
			criteria.addOrder(Order.desc("createtime"));
			criteria.setFirstResult(CountOfPage * (currentPage - 1));
			criteria.setMaxResults(CountOfPage);
			List<Users> users = criteria.list();
			for (Users users2 : users) {
				
				// 创建统计对象
				ProxyFlow flow = new ProxyFlow();
				flow.setCreatetime(users2.getCreatetime());
				flow.setAgent(users2.getLoginname());
				flow.setReferWebsite(users2.getReferWebsite());
				// 会员数量
				Criteria criteriaCount = getSession.createCriteria(Users.class);
				criteriaCount.add(Restrictions.eq("role", "MONEY_CUSTOMER"));
				criteriaCount.add(Restrictions.eq("agent", users2.getLoginname()));
				criteriaCount.add(Restrictions.ge("createtime", start));
				criteriaCount.add(Restrictions.lt("createtime", end));
				Integer agentCount = (Integer) criteriaCount.setProjection(Projections.rowCount()).uniqueResult();
				flow.setAgentCount(agentCount);
				// 获取活跃数据和存款
				Double deposit = 0.0;
				List proposalList = proposalDao.getAgentReferralsCountAtProposal(users2.getLoginname(), start, end);
				List payorderList = payorderDao.getAgentReferralsCountAtPayorder(users2.getLoginname(), start, end);
				Map<String, AgentReferralsVO> usersTotal = this.userListToMap(proposalList, payorderList);
				if (users.isEmpty()) {
					return null;
				}
				Set<String> userSet = usersTotal.keySet();
				Iterator<String> it = userSet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					AgentReferralsVO vo = usersTotal.get(key);
					// 存款金额
					deposit = deposit + vo.getMoney();
				}
				// 活跃会员数量
				flow.setAgentActiveCount(userSet.size());

				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar calendar = Calendar.getInstance();
				Date startTime1 = format1.parse(format1.format(start));
				calendar.setTime(startTime1);
				Calendar lastDate1 = (Calendar) calendar.clone();
				lastDate1.add(Calendar.MONTH, +1);

				Calendar calendar2 = Calendar.getInstance();
				Date endTime1 = format1.parse(format1.format(end));
				calendar2.setTime(endTime1);
				Calendar lastDate2 = (Calendar) calendar2.clone();
				lastDate2.add(Calendar.MONTH, +2);
				standardFmt.format(lastDate2.getTime());

				String flowSql = "(SELECT SUM(a.bettotal) as bettotal FROM agprofit a inner join users u on a.loginname=u.loginname WHERE u.agent=? and a.createTime > ? and a.createTime <= ?)UNION ALL(SELECT SUM(amount) as amount FROM commissions WHERE loginname=? and createTime > ? and createTime <= ?)UNION ALL(SELECT SUM(amount) as amount FROM proposal p inner join users u on p.loginname=u.loginname where u.agent = ? and p.type='503' and p.flag='2' and p.createTime>? and p.createTime<=?)";
				flow.setAmountProfit(0.0 + "");
				flow.setBettotal(0.0 + "");
				flow.setCommission(0.0 + "");
				Query ipQuery0 = getSession.createSQLQuery(flowSql);
				ipQuery0.setParameter(0, users2.getLoginname()).setParameter(1, start).setParameter(2, end).setParameter(3, users2.getLoginname()).setParameter(4, standardFmt.parse(standardFmt.format(lastDate1.getTime()))).setParameter(5, standardFmt.parse(standardFmt.format(lastDate2.getTime()))).setParameter(6, users2.getLoginname()).setParameter(7, start).setParameter(8, end);
				List ipList0 = ipQuery0.list();
				if (ipList0 != null && ipList0.size() > 0 && ipList0.get(0) != null) {
					if (ipList0.get(0) != null) {
						flow.setBettotal(df.format((Double) ipList0.get(0)));
					}
					if (ipList0.get(1) != null) {
						flow.setCommission(df.format((Double) ipList0.get(1)));
					}
					if (ipList0.get(2) != null) {
						flow.setAmountProfit(df.format(deposit - (Double) ipList0.get(2)));
					}
				}
				// ip pv访问量
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sfDay = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = sfDay.format(start) + " 00:00:00";
				String endTime = sfDay.format(end) + " 00:00:00";
				flow.setIpAccess(0);
				flow.setPvAccess(0);
				String ipPvSql = "SELECT SUM(agent_count) as agent_count,SUM(agent_pv) as agent_pv FROM agent_count WHERE agent=? AND createtime>=? AND createtime<?";
				Query ipQuery = getSessionTwo.createSQLQuery(ipPvSql);
				ipQuery.setParameter(0, users2.getLoginname()).setParameter(1, sf.parse(startTime)).setParameter(2, sf.parse(endTime));
				List ipList = ipQuery.list();
				if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
					Object[] objects0 = (Object[]) ipList.get(0);
					if (objects0[0] != null) {
						String ipAccess = objects0[0].toString();
						flow.setIpAccess(Integer.parseInt(ipAccess));
					}
					if (objects0[1] != null) {
						String pvAccess = objects0[1].toString();
						flow.setPvAccess(Integer.parseInt(pvAccess));
					}
				}
				// 存款转换率
				if (flow.getAgentActiveCount() != null && flow.getAgentCount() != null && flow.getAgentCount() > 0) {
					flow.setRegisterDeposit(df.format((Double.parseDouble(String.valueOf(flow.getAgentActiveCount())) / Double.parseDouble(String.valueOf(flow.getAgentCount())) * 100)) + "%");
				} else {
					flow.setRegisterDeposit("0.00%");
				}
				//log.info(flow.toString());
				list.add(flow);
				
				/*
				// 创建统计对象
				ProxyFlow flow = new ProxyFlow();
				flow.setCreatetime(users2.getCreatetime());
				flow.setAgent(users2.getLoginname());
				flow.setReferWebsite(users2.getReferWebsite());
				// 会员数量
				Criteria criteriaCount = getSession.createCriteria(Users.class);
				criteriaCount.add(Restrictions.eq("role", "MONEY_CUSTOMER"));
				criteriaCount.add(Restrictions.eq("agent", users2.getLoginname()));
				Integer agentCount = (Integer) criteriaCount.setProjection(Projections.rowCount()).uniqueResult();
				flow.setAgentCount(agentCount);
				// 获取活跃数据和存款
				// 获取活跃数据和存款
				Double deposit=0.0;
				List proposalList = proposalDao.getAgentReferralsCountAtProposal(users2.getLoginname(), start, end);
				List payorderList = payorderDao.getAgentReferralsCountAtPayorder(users2.getLoginname(), start, end);
				Map<String, AgentReferralsVO> usersTotal = this.userListToMap(proposalList, payorderList);
				if (users.isEmpty()) {
					return null;
				}
				Set<String> userSet = usersTotal.keySet();
				Iterator<String> it = userSet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					AgentReferralsVO vo = usersTotal.get(key);
					// 存款金额
					deposit=deposit+vo.getMoney();
				}
				// 活跃会员数量
				flow.setAgentActiveCount(userSet.size());
				//总赢利额
				Double depositAll=0.0;
				java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date startDate=format.parse("2007-01-01 00:00:00");
				Date endDate=format.parse("2030-01-01 00:00:00");
				List proposalListAll = proposalDao.getAgentReferralsCountAtProposal(users2.getLoginname(), startDate, endDate);
				List payorderListAll = payorderDao.getAgentReferralsCountAtPayorder(users2.getLoginname(), startDate, endDate);
				Map<String, AgentReferralsVO> usersTotalAll = this.userListToMap(proposalListAll, payorderListAll);
				if (users.isEmpty()) {
					return null;
				}
				Set<String> userSetAll = usersTotalAll.keySet();
				Iterator<String> itAll = userSetAll.iterator();
				while (itAll.hasNext()) {
					String key = itAll.next();
					AgentReferralsVO vo = usersTotalAll.get(key);
					// 总存款金额
					depositAll=depositAll+vo.getMoney();
				}
				
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar calendar = Calendar.getInstance();
				Date startTime1=format1.parse(format1.format(start));
				calendar.setTime(startTime1);
				Calendar lastDate1 = (Calendar) calendar.clone();
				lastDate1.add(Calendar.MONTH, +1);

				Calendar calendar2 = Calendar.getInstance();
				Date endTime1=format1.parse(format1.format(end));
				calendar2.setTime(endTime1);
				Calendar lastDate2 = (Calendar) calendar2.clone();
				lastDate2.add(Calendar.MONTH, +2);
				standardFmt.format(lastDate2.getTime());
				
				String flowSql="(SELECT SUM(a.bettotal) as bettotal FROM agprofit a inner join users u on a.loginname=u.loginname WHERE u.agent=?)UNION ALL(SELECT SUM(a.bettotal) as bettotal FROM agprofit a inner join users u on a.loginname=u.loginname WHERE u.agent=? and a.createTime > ? and a.createTime <= ?)UNION ALL(SELECT SUM(amount) as amount FROM commissions WHERE loginname=?)UNION ALL(SELECT SUM(amount) as amount FROM commissions WHERE loginname=? and createTime > ? and createTime <= ?)UNION ALL(SELECT SUM(amount) as amount FROM proposal p inner join users u on p.loginname=u.loginname where u.agent = ? and p.type='503' and p.flag='2')UNION ALL(SELECT SUM(amount) as amount FROM proposal p inner join users u on p.loginname=u.loginname where u.agent = ? and p.type='503' and p.flag='2' and p.createTime>? and p.createTime<=?)";
				flow.setAmountProfitAll(0.0+"");
				flow.setAmountProfit(0.0+"");
				flow.setBettotalAll(0.0+"");
				flow.setBettotal(0.0+"");
				flow.setCommissionAll(0.0+"");
				flow.setCommission(0.0+"");
				Query ipQuery0 = getSession.createSQLQuery(flowSql);
				ipQuery0.setParameter(0, users2.getLoginname()).setParameter(1, users2.getLoginname()).setParameter(2, start).setParameter(3, end).setParameter(4, users2.getLoginname()).setParameter(5, users2.getLoginname()).setParameter(6, standardFmt.parse(standardFmt.format(lastDate1.getTime()))).setParameter(7, standardFmt.parse(standardFmt.format(lastDate2.getTime()))).setParameter(8, users2.getLoginname()).setParameter(9, users2.getLoginname()).setParameter(10, start).setParameter(11, end);
				List ipList0 = ipQuery0.list();
				if (ipList0 != null && ipList0.size() > 0 && ipList0.get(0) != null) {
					if (ipList0.get(0) != null) {
						flow.setBettotalAll(df.format((Double)ipList0.get(0)));
					}
					if (ipList0.get(1) != null) {
						flow.setBettotal(df.format((Double)ipList0.get(1)));
					}
					if (ipList0.get(2) != null) {
						flow.setCommissionAll(df.format((Double)ipList0.get(2)));
					}
					if (ipList0.get(3) != null) {
						flow.setCommission(df.format((Double)ipList0.get(3)));
					}
					if (ipList0.get(4) != null) {
						 flow.setAmountProfitAll(df.format(depositAll-(Double)ipList0.get(4)));
					}
					if (ipList0.get(5) != null) {
						flow.setAmountProfit(df.format(deposit-(Double)ipList0.get(5)));
					}
				}
				// ip pv访问量
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sfDay = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = sfDay.format(start) + " 00:00:00";
				String endTime = sfDay.format(end) + " 00:00:00";
				flow.setIpAccess(0);
				flow.setPvAccess(0);
				flow.setIpAccessAll(0);
				flow.setPvAccessAll(0);
				String ipPvSql="(SELECT SUM(agent_count) as agent_count,SUM(agent_pv) as agent_pv FROM agent_count WHERE agent=?)UNION ALL(SELECT SUM(agent_count) as agent_count,SUM(agent_pv) as agent_pv FROM agent_count WHERE agent=? AND createtime>=? AND createtime<?)";
				Query ipQuery = getSessionTwo.createSQLQuery(ipPvSql);
				ipQuery.setParameter(0, users2.getLoginname()).setParameter(1, users2.getLoginname()).setParameter(2, sf.parse(startTime)).setParameter(3, sf.parse(endTime));
				List ipList = ipQuery.list();
				if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
					Object[] objects0 = (Object[]) ipList.get(0);
					if (objects0[0] != null) {
						String ipAccess = objects0[0].toString();
						flow.setIpAccessAll(Integer.parseInt(ipAccess));
					}
					if (objects0[1] != null) {
						String pvAccess = objects0[1].toString();
						flow.setPvAccessAll(Integer.parseInt(pvAccess));
					}
					Object[] objects1 = (Object[]) ipList.get(1);
					if (objects1[0] != null) {
						String ipAccess = objects1[0].toString();
						flow.setIpAccess(Integer.parseInt(ipAccess));
					}
					if (objects1[1] != null) {
						String pvAccess = objects1[1].toString();
						flow.setPvAccess(Integer.parseInt(pvAccess));
					}
				}
				
				//存款转换率
				if(flow.getAgentActiveCount()!=null&&flow.getAgentCount()!=null&&flow.getAgentCount()>0){
					flow.setRegisterDeposit(df.format((Double.parseDouble(String.valueOf(flow.getAgentActiveCount()))/Double.parseDouble(String.valueOf(flow.getAgentCount()))*100))+"%");
				}else{
					flow.setRegisterDeposit("0.00%");
				}
				//总存款转换率
				if(userSetAll.size()>0&&flow.getAgentCount()!=null&&flow.getAgentCount()>0){
					flow.setRegisterDepositAll(df.format((Double.parseDouble(String.valueOf(userSetAll.size()))/Double.parseDouble(String.valueOf(flow.getAgentCount()))*100))+"%");
				}else{
					flow.setRegisterDepositAll("0.00%");
				}
				log.info(flow.toString());
				list.add(flow);*/
				
				
				
				/*
				// 创建统计对象
				ProxyFlow flow = new ProxyFlow();
				flow.setCreatetime(users2.getCreatetime());
				flow.setAgent(users2.getLoginname());
				flow.setReferWebsite(users2.getReferWebsite());
				// 会员数量
				Criteria criteriaCount = getSession.createCriteria(Users.class);
				criteriaCount.add(Restrictions.eq("role", "MONEY_CUSTOMER"));
				criteriaCount.add(Restrictions.eq("agent", users2.getLoginname()));
				Integer agentCount = (Integer) criteriaCount.setProjection(Projections.rowCount()).uniqueResult();
				flow.setAgentCount(agentCount);
				// 获取活跃数据和存款
				Double deposit=0.0;
				List proposalList = proposalDao.getAgentReferralsCountAtProposal(users2.getLoginname(), start, end);
				List payorderList = payorderDao.getAgentReferralsCountAtPayorder(users2.getLoginname(), start, end);
				Map<String, AgentReferralsVO> usersTotal = this.userListToMap(proposalList, payorderList);
				if (users.isEmpty()) {
					return null;
				}
				Set<String> userSet = usersTotal.keySet();
				Iterator<String> it = userSet.iterator();
				while (it.hasNext()) {
					String key = it.next();
					AgentReferralsVO vo = usersTotal.get(key);
					// 存款金额
					deposit=deposit+vo.getMoney();
					//log.info("***********存款金额："+df.format(vo.getMoney()));
				}
				//log.info("***********存款金额："+df.format(deposit)+"**********************");
				// 活跃会员数量
				flow.setAgentActiveCount(userSet.size());
				//提款金额
				flow.setAmountProfit(0.0+"");
				String amountProfitSql = "SELECT SUM(amount) FROM proposal WHERE type='503' and flag='2' and agent=? and createTime>? and createTime<=?";
				Query qAmountProfit = getSession.createSQLQuery(amountProfitSql);
				qAmountProfit.setParameter(0, users2.getLoginname()).setParameter(1, start).setParameter(2, end);
				List amountProfit = qAmountProfit.list();
				if (amountProfit != null && amountProfit.size() > 0 && amountProfit.get(0) != null) {
					 //log.info("***********提款金额："+df.format((Double)amountProfit.get(0)));
					 //log.info("***********赢利金额："+df.format(deposit-(Double)amountProfit.get(0)));
					 //赢利额
					 flow.setAmountProfit(df.format(deposit-(Double)amountProfit.get(0)));
				}
				
				//总赢利额
				Double depositAll=0.0;
				java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date startDate=format.parse("2007-01-01 00:00:00");
				Date endDate=format.parse("2030-01-01 00:00:00");
				List proposalListAll = proposalDao.getAgentReferralsCountAtProposal(users2.getLoginname(), startDate, endDate);
				List payorderListAll = payorderDao.getAgentReferralsCountAtPayorder(users2.getLoginname(), startDate, endDate);
				Map<String, AgentReferralsVO> usersTotalAll = this.userListToMap(proposalListAll, payorderListAll);
				if (users.isEmpty()) {
					return null;
				}
				Set<String> userSetAll = usersTotalAll.keySet();
				Iterator<String> itAll = userSetAll.iterator();
				while (itAll.hasNext()) {
					String key = itAll.next();
					AgentReferralsVO vo = usersTotalAll.get(key);
					// 总存款金额
					depositAll=depositAll+vo.getMoney();
					//log.info("***********总存款金额："+df.format(vo.getMoney()));
				}
				//log.info("***********总存款金额："+df.format(depositAll)+"******************88888888");
				//总提款金额
				flow.setAmountProfitAll(0.0+"");
				String amountProfitSqlAll = "SELECT SUM(amount) FROM proposal WHERE type='503' and flag='2' and agent=? and createTime>? and createTime<=?";
				Query qAmountProfitAll = getSession.createSQLQuery(amountProfitSqlAll);
				qAmountProfitAll.setParameter(0, users2.getLoginname()).setParameter(1, startDate).setParameter(2, endDate);
				List amountProfitAll = qAmountProfitAll.list();
				if (amountProfitAll != null && amountProfitAll.size() > 0 && amountProfitAll.get(0) != null) {
					 //log.info("***********总提款金额："+df.format((Double)amountProfitAll.get(0)));
					 //log.info("***********总赢利金额："+df.format(deposit-(Double)amountProfitAll.get(0)));
					 //总赢利额
					 flow.setAmountProfitAll(df.format(depositAll-(Double)amountProfitAll.get(0)));
				}
				
				// 投注额
				flow.setBettotal(0.0+"");
				String bettotalSql = "SELECT SUM(bettotal) FROM agprofit WHERE agent=? and createTime > ? and createTime <= ?";
				Query q = getSession.createSQLQuery(bettotalSql);
				q.setParameter(0, users2.getLoginname()).setParameter(1, start).setParameter(2, end);
				List bettotal = q.list();
				if (bettotal != null && bettotal.size() > 0 && bettotal.get(0) != null) {
					 flow.setBettotal(df.format((Double)bettotal.get(0)));
				}
				
				// 总投注额
				flow.setBettotalAll(0.0+"");
				String bettotalSqlAll = "SELECT SUM(bettotal) FROM agprofit WHERE agent=? and createTime > ? and createTime <= ?";
				Query qAll = getSession.createSQLQuery(bettotalSqlAll);
				qAll.setParameter(0, users2.getLoginname()).setParameter(1, startDate).setParameter(2, endDate);
				List bettotalAll = qAll.list();
				if (bettotalAll != null && bettotalAll.size() > 0 && bettotalAll.get(0) != null) {
					 flow.setBettotalAll(df.format((Double)bettotalAll.get(0)));
				}

				// 佣金
				SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar calendar = Calendar.getInstance();
				Date startTime1=format1.parse(format1.format(start));
				calendar.setTime(startTime1);
				Calendar lastDate1 = (Calendar) calendar.clone();
				lastDate1.add(Calendar.MONTH, +1);

				Calendar calendar2 = Calendar.getInstance();
				Date endTime1=format1.parse(format1.format(end));
				calendar2.setTime(endTime1);
				Calendar lastDate2 = (Calendar) calendar2.clone();
				lastDate2.add(Calendar.MONTH, +2);
				standardFmt.format(lastDate2.getTime());

				flow.setCommission(0.0+"");
				String qcommissionSql = "SELECT SUM(amount) FROM commissions WHERE loginname=? and createTime > ? and createTime <= ?";
				Query qcommission = getSession.createSQLQuery(qcommissionSql);
				qcommission.setParameter(0, users2.getLoginname()).setParameter(1, standardFmt.parse(standardFmt.format(lastDate1.getTime()))).setParameter(2, standardFmt.parse(standardFmt.format(lastDate2.getTime())));
				List commission = qcommission.list();
				if (commission != null && commission.size() > 0 && commission.get(0) != null) {
					flow.setCommission(df.format((Double)commission.get(0)));
				}
				// 总佣金
				flow.setCommissionAll(0.0+"");
				String qcommissionSqlAll = "SELECT SUM(amount) FROM commissions WHERE loginname=? and createTime > ? and createTime <= ?";
				Query qcommissionAll = getSession.createSQLQuery(qcommissionSqlAll);
				qcommissionAll.setParameter(0, users2.getLoginname()).setParameter(1, startDate).setParameter(2, endDate);
				List commissionAll = qcommissionAll.list();
				if (commissionAll != null && commissionAll.size() > 0 && commissionAll.get(0) != null) {
					flow.setCommissionAll(df.format((Double)commissionAll.get(0)));
				}

				// ip访问量
				flow.setIpAccess(0);
				String ipSql = "SELECT id FROM agent_visit WHERE agent_website=? and createtime>? and createtime<=? GROUP BY client_ip";
				Query ipQuery = getSessionTwo.createSQLQuery(ipSql);
				ipQuery.setParameter(0, users2.getReferWebsite()).setParameter(1, start).setParameter(2, end);
				List ipList = ipQuery.list();
				if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
					flow.setIpAccess(ipList.size());
				}
				
				// ip总访问量
				flow.setIpAccessAll(0);
				String ipSqlAll = "SELECT id FROM agent_visit WHERE agent_website=?  GROUP BY client_ip";
				Query ipQueryAll = getSessionTwo.createSQLQuery(ipSqlAll);
				ipQueryAll.setParameter(0, users2.getReferWebsite());
				List ipListAll = ipQueryAll.list();
				if (ipListAll != null && ipListAll.size() > 0 && ipListAll.get(0) != null) {
					flow.setIpAccessAll(ipListAll.size());
				}

				// PV访问量
				flow.setPvAccess(0);
				String pvSql = "SELECT id FROM agent_visit WHERE agent_website=? and createtime>? and createtime<=?";
				Query pvSqlQuery = getSessionTwo.createSQLQuery(pvSql);
				pvSqlQuery.setParameter(0, users2.getReferWebsite()).setParameter(1, start).setParameter(2, end);
				List pvSqlQ = pvSqlQuery.list();
				if (pvSqlQ != null && pvSqlQ.size() > 0 && pvSqlQ.get(0) != null) {
					flow.setPvAccess(pvSqlQ.size());
				}
				
				// 总PV访问量
				flow.setPvAccessAll(0);
				String pvSqlAll = "SELECT id FROM agent_visit WHERE agent_website=?";
				Query pvSqlQueryAll = getSessionTwo.createSQLQuery(pvSqlAll);
				pvSqlQueryAll.setParameter(0, users2.getReferWebsite());
				List pvSqlQAll = pvSqlQueryAll.list();
				if (pvSqlQAll != null && pvSqlQAll.size() > 0 && pvSqlQAll.get(0) != null) {
					flow.setPvAccessAll(pvSqlQAll.size());
				}
				//存款转换率
				if(flow.getAgentActiveCount()!=null&&flow.getAgentCount()!=null&&flow.getAgentCount()>0){
					flow.setRegisterDeposit(df.format((Double.parseDouble(String.valueOf(flow.getAgentActiveCount()))/Double.parseDouble(String.valueOf(flow.getAgentCount()))*100))+"%");
				}else{
					flow.setRegisterDeposit("0.00%");
				}
				//总存款转换率
				if(userSetAll.size()>0&&flow.getAgentCount()!=null&&flow.getAgentCount()>0){
					flow.setRegisterDepositAll(df.format((Double.parseDouble(String.valueOf(userSetAll.size()))/Double.parseDouble(String.valueOf(flow.getAgentCount()))*100))+"%");
				}else{
					flow.setRegisterDepositAll("0.00%");
				}
				log.info(flow.toString());
				list.add(flow);*/
			}
			long endTime=System.nanoTime(); //获取结束时间
			System.out.println("程序运行时间： "+(endTime-startTimeend)/(1000*1000));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Integer getProxyFlowListCount(String loginname,String partner, Date start, Date end) {
		Session getSession = universalDao.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Criteria criteria = getSession.createCriteria(Users.class);
		criteria.add(Restrictions.eq("role", "AGENT"));
		criteria.add(Restrictions.eq("flag", 0));
		if(loginname!=null&&!loginname.equals("")){
			criteria.add(Restrictions.eq("loginname", loginname));
		}
		if (StringUtils.isNotEmpty(partner)) {
			DetachedCriteria pDC = DetachedCriteria.forClass(Users.class);
			pDC.setProjection(Property.forName("loginname")).add(Restrictions.eq("partner", partner));
			criteria.add(Property.forName("loginname").in(pDC));
		}
		criteria.add(Restrictions.ne("referWebsite", ""));
		criteria.add(Restrictions.isNotNull("referWebsite"));
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	private Map<String, AgentReferralsVO> userListToMap(List<Object[]> proposalList, List<Object[]> payorderList) throws Exception {
		Map<String, AgentReferralsVO> users = new HashMap<String, AgentReferralsVO>();
		if (proposalList != null) {
			for (int i = 0; i < proposalList.size(); i++) {
				Object[] o = proposalList.get(i);
				users.put(String.valueOf(o[0]), new AgentReferralsVO(String.valueOf(o[0]), ((Number) o[1]).doubleValue()));
			}
		}

		if (payorderList != null) {
			for (int i = 0; i < payorderList.size(); i++) {
				Object[] o = payorderList.get(i);
				String key = String.valueOf(o[0]);
				if (users.containsKey(key)) {
					AgentReferralsVO vo = users.remove(key);
					vo.setMoney(((Number) o[1]).doubleValue());
					users.put(key, vo);
				} else {
					users.put(String.valueOf(o[0]), new AgentReferralsVO(String.valueOf(o[0]), ((Number) o[1]).doubleValue()));
				}
			}
		}

		return users;
	}

	public UniversalDao getUniversalDao() {
		return universalDao;
	}

	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}

	public GuestbookDao getGuestbookDao() {
		return guestbookDao;
	}

	public void setGuestbookDao(GuestbookDao guestbookDao) {
		this.guestbookDao = guestbookDao;
	}

	public ProposalDao getProposalDao() {
		return proposalDao;
	}

	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}

	public NetPayDao getPayorderDao() {
		return payorderDao;
	}

	public void setPayorderDao(NetPayDao payorderDao) {
		this.payorderDao = payorderDao;
	}

}
