package dfh.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import dfh.model.Proposal;
import dfh.model.SingleParty;
import dfh.service.interfaces.ProposalService;

/**
 * 处理同步工具类
 * 每处理一个同步功能，需要一个Obj对象，一个带同步块的方法，同步块获取obj对象的锁
 *  
 */
public class SynchronizedUtil {

	private static final SynchronizedUtil instance = new SynchronizedUtil();
	
	private static Logger log = Logger.getLogger(SynchronizedUtil.class);
	
	/**
	 * 防止外部实例化
	 */
	private SynchronizedUtil(){};
	
	/**
	 * 单例
	 * @return
	 */
	public static SynchronizedUtil getInstance(){
		return instance;
	}
	
	private Object ximaExeObj = new Object();
	public String exeXima(ProposalService proposalService){
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(7*24-12));
		synchronized (ximaExeObj) {
			List<Proposal> notExecProposal;
			try {
				notExecProposal = proposalService.getNotExecProposal(starttime, new Date());
			} catch (Exception e1) {
				return "获取数据失败";
			}
			if (notExecProposal==null||notExecProposal.size()<=0) {
				return "提案记录为空，不需要执行该任务";
			}else{
				for (Proposal proposal : notExecProposal) {
					try {
						log.debug(">>>>>>>>>>>>>>>>>>提案执行start单号: " + proposal.getPno()+"玩家账号：" + proposal.getLoginname());
						proposalService.excuteAutoXimaProposal(proposal);
						log.debug(">>>>>>>>>>>>>>>>>>提案执行end单号: " + proposal.getPno()+"玩家账号：" + proposal.getLoginname());
					} catch (Exception e) {
						log.debug(">>>>>>>>>>>>>>>>>>提案执行错误单号: " + proposal.getPno()+"玩家账号：" + proposal.getLoginname());
						continue;
					}
				}
				return "执行洗码成功...";
			}
		}
	}
	
	private Object batchPrivilegesObj = new Object();
	public String exePrivilegesOfToday(ProposalService proposalService){
		synchronized (batchPrivilegesObj) {
			String sqlStr = "select id, loginname, amount, remark from privilege where status = :status and TO_DAYS(createtime) = TO_DAYS(:now)";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", 0);
			params.put("now", new Date());
			List privilegesList = null;
			try {
				privilegesList = proposalService.getListBySql(sqlStr, params);
			} catch (Exception e) {
				log.error(e.getMessage());
				return "查询批量优惠异常";
			}
			for (Object obj : privilegesList) {
				Object [] privilegeObj = (Object[]) obj;
				try {
					proposalService.exeSinglePrivilege(Integer.parseInt(privilegeObj[0].toString()), privilegeObj[1].toString(), Double.parseDouble(privilegeObj[2].toString()), privilegeObj[3].toString());
				} catch (Exception e) {
					log.error(e.getMessage());
					continue;
				}
			}
			return "执行完毕";
		}
	}
	
	public void cancelPrivilegesOfToday(ProposalService proposalService, String operator){
		synchronized (batchPrivilegesObj) {
			String sqlStr = "update privilege set status=:cancelStatus, remark=concat(remark, :operator) where status = :status and TO_DAYS(createtime) = TO_DAYS(:today)";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("cancelStatus", 2);
			params.put("operator", "  手动取消:" + operator);
			params.put("status", 0);
			params.put("today", new Date());
			proposalService.excuteSql(sqlStr, params);
		}
	}

	public synchronized void updateSingleParty(ProposalService proposalService, Date agStart, Date agEnd, Date ptStart,
			Date ptEnd, String rankdate) {
		String countQ = " SELECT count(*) FROM singleparty WHERE TO_DAYS(rankdate) >= TO_DAYS(:rankdate) ";
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("rankdate", rankdate);
		Session session = proposalService.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query queryC = session.createSQLQuery(countQ).setProperties(param);
		List q = queryC.list();
		if(q.get(0) != null && Integer.parseInt(q.get(0).toString()) > 0){
			return;
		}
		
		String selectS = " SELECT s3.loginname as loginname, SUM(s3.bettotal) as bettotal FROM "
				+ " ((SELECT SUM(a.bettotal) AS bettotal, u.loginname AS loginname FROM agprofit a LEFT JOIN users u ON (u.loginname = a.loginname) "
				+ " WHERE a.createTime >=:agStart AND a.createTime <=:agEnd AND a.platform IN('nt','qt','ttg') AND a.bettotal > 0.0 GROUP BY u.loginname ) "
				+ " UNION (SELECT SUM(p.BETS_TIGER) AS bettotal, u.loginname AS loginname FROM pt_data_new p LEFT JOIN users u ON ( u.loginname = SUBSTR(p.playername, 2)) "
				+ " WHERE p.STARTTIME >=:ptStart AND p.STARTTIME <=:ptEnd AND p.BETS_TIGER > 0.0 GROUP BY u.loginname)) AS s3 GROUP BY s3.loginname "
				+ " ORDER BY bettotal desc ";
		param.put("agStart", agStart);
		param.put("agEnd", agEnd);
		param.put("ptStart", ptStart);
		param.put("ptEnd", ptEnd);
		
		Query queryS = session.createSQLQuery(selectS).setProperties(param);
		List<Object[]> listS = queryS.list();

		Iterator<Object[]> it = listS.iterator();
		List<SingleParty> singles = new ArrayList<SingleParty>();
		while(it.hasNext()){
			Object[] obj = it.next();
			if(obj[0] == null || obj[1] == null){
				continue;
			}
			SingleParty sp = new SingleParty();
			sp.setLoginname(obj[0].toString());
			sp.setBettotal(Double.parseDouble(obj[1].toString()));
			sp.setRankdate(rankdate);
			singles.add(sp);
		}
		this.ranksingle(singles);
		Iterator<SingleParty> it_s = singles.iterator();
		while(it_s.hasNext()){
			proposalService.saveOrUpdate(it_s.next());
		}
	}
	
	private void ranksingle(List<SingleParty> singles) {
		if(singles != null && singles.size() > 0){
			
			Double bettotal = -1.0;
			int ranking = 1;
			for(int i = 0; i < singles.size(); i++){
				
				SingleParty sp = singles.get(i);
				if(i == 0){
					sp.setRanking(ranking);
					bettotal = sp.getBettotal() == null? 0.0 : sp.getBettotal();
				} else {
					if(sp.getBettotal() != null && sp.getBettotal().equals(bettotal)){
						sp.setRanking(ranking);
					} else {
						ranking = i + 1;
						sp.setRanking(ranking);
						bettotal = sp.getBettotal() == null? 0 : sp.getBettotal();
					}
				}
			}
		}
	}
}
