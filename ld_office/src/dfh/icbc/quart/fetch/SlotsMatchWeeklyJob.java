package dfh.icbc.quart.fetch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import dfh.dao.ProposalDao;
import dfh.dao.SlotsMatchWeeklyDao;
import dfh.dao.TransferDao;
import dfh.model.Const;
import dfh.model.Proposal;
import dfh.model.SlotsMatchWeekly;
import dfh.model.Transfer;
import dfh.utils.MatchDateUtil;

public class SlotsMatchWeeklyJob {
	
	private Logger log = Logger.getLogger(SlotsMatchWeeklyJob.class);
	private ProposalDao proposalDao;
	private TransferDao transferDao;
	private SlotsMatchWeeklyDao smWeeklyDao;
	
	public void generateWeeklyData(String date) throws Exception{
		String[] pfs = new String[]{"pt","gpi","ttg","nt"};
		//try {
			Date startTime=MatchDateUtil.parseDatetime(date+" 00:00:00"),
					endTime=MatchDateUtil.parseDatetime(date+" 23:59:59");
			Date weekStart=MatchDateUtil.parseDatetime(MatchDateUtil.getWeekStart(MatchDateUtil.parseDate(date))+" 00:00:00"),
					weekEnd=MatchDateUtil.parseDatetime(MatchDateUtil.getWeekEnd(MatchDateUtil.parseDate(date))+" 23:59:59");
			/*
			Date tsd = MatchDateUtil.parseDate("2015-10-01");
			Date ted = MatchDateUtil.parseDate("2015-10-01");
			
			startTime = MatchDateUtil.getWeekStart(new Date()tsd)+" 00:00:00";
			endTime = MatchDateUtil.getWeekEnd(new Date()ted)+" 23:59:59";
			*/
			for (int i = 0; i < pfs.length; i++) {
				String platform = pfs[i];
				DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
				dc = dc.add(Restrictions.eq("id", platform.toUpperCase()+"体验金周赛"));
				List<Const> ConstList = this.smWeeklyDao.getHibernateTemplate().findByCriteria(dc);
				if (ConstList.isEmpty() || ConstList.get(0).getValue() == "1"){
					log.info("当前"+platform.toUpperCase()+"老虎机周赛已关闭");
					continue;
				}
				//List<String> list = (List<String>) SessionCacheUtil.getCacheByNameIndex(request, "match_cache", 2);
				//if (list == null || list.isEmpty()){
				dc = DetachedCriteria.forClass(Proposal.class);
				dc.setProjection(Projections.projectionList().add(Projections.property("loginname")).add(Projections.property("executeTime")));
				dc.add(Restrictions.ge("executeTime", weekStart));
				dc.add(Restrictions.le("executeTime", weekEnd));
				dc.add(Restrictions.eq("flag", 2));
				dc.add(Restrictions.in("remark", new String[]{platform.toUpperCase()+"8.0元自助优惠",platform.toUpperCase()+"18.0元自助优惠"}));
				List<Object[]> list = proposalDao.findByCriteria(dc);
				if (list.isEmpty()){ //如果无人领取体验金直接返回
					/*page.setTotalRecords(0);
					page.setTotalPages(0);
					page.setPageNumber(0);
					page.setPageContents(null);
					page.setNumberOfRecordsShown(0);*/
					continue;
				}
				dc=null;
				
				//将List<Object[]>中的loginname提出来作为in条件使用
				Map<String, Date> maps = new CaseInsensitiveMap();
				Map<String, Double> gift = new CaseInsensitiveMap();
				for (Object[] obs : list) {
					maps.put(obs[0].toString(), MatchDateUtil.parseDatetime(obs[1].toString()));
					Double dg=Double.valueOf(obs[2].toString().substring(obs[2].toString().indexOf(platform.toUpperCase())+platform.length(),
							obs[2].toString().indexOf(platform.toUpperCase())+(platform.length()+2)));
					gift.put(obs[0].toString(), dg);
				}
				list = null;
				
				/** 
				 * 1,玩家活动期间内第一笔转出为盈利金额
				 * 2,玩家活动期间内第一笔为转入,则比赛结束,盈利为0
				 * 3,玩家活动期间内无转出记录则比赛结束,盈利为0
				 */
				
				platform = platform.equals("pt")?"newpt":platform; //由于agprofit中表记录的是newpt类型
				//查询当前用户在平台内的第一次转出记录
				List<Transfer> transList = new ArrayList<Transfer>();
				dc = DetachedCriteria.forClass(Transfer.class);
				//dc.setProjection(Projections.projectionList().add(Projections.property("loginname"),"lname"));
				//dc.setProjection(Projections.groupProperty("loginname"));
				dc.add(Restrictions.ge("createtime", startTime));
				dc.add(Restrictions.le("createtime", endTime));
				dc.add(Restrictions.in("target", new String[]{"elufa",platform}));
				dc.add(Restrictions.in("source", new String[]{"elufa",platform}));
				dc.add(Restrictions.eq("flag", 0));
				dc.add(Restrictions.in("loginname", maps.keySet().toArray()));
				dc.add(Restrictions.like("remark", "%成功"));
				dc.addOrder(Order.asc("createtime"));
				transList = transferDao.findByCriteria(dc);
				
				//将transList中有的用户从list中删除,list为最终查询输赢值的集合
				/*for (String tn : transList) {
					if (list.contains(tn)){
						list.remove(tn);
					}
				}*/
				//SessionCacheUtil.saveCacheByNameIndex(request, "match_cache", 2, list);
				//}
				
				//根据体验金用户查询时间段内玩家平台盈利
				/*StringBuffer c_sql = new StringBuffer("select count(ag1.loginname) from (select loginname from agprofit ag where");
				c_sql.append(" ag.platform='"+ platform +"' and ag.createTime>='"+ startTime +"' and ag.createTime<='"+ endTime +"' and ag.loginname in(");
				for (String pro : list) {
					c_sql.append("'"+pro+"',");
				}
				c_sql.append("'') group by ag.loginname) as ag1");
				Integer count = Integer.valueOf(this.smWeeklyDao.getSessionFactory().getCurrentSession().createSQLQuery(c_sql.toString()).uniqueResult().toString());
				c_sql = null;*/
				
				/*final StringBuffer sql = new StringBuffer("select ag.loginname,sum(ag.bettotal) as bettotal,sum(ag.amount) as amount from AgProfit ag where");
				sql.append(" ag.platform='"+platform+"' and ag.createTime>='"+startTime+"' and ag.createTime<='"+endTime+"' and ag.loginname in(");
				for (String pro : list) {
					sql.append("'"+pro+"',");
				}
				sql.append("'')");
				//int pages = PagenationUtil.computeTotalPages(count, size).intValue();
				//final int limit = (pageIndex-1)*size;
				//final int f_size = size;
				sql.append(" group by ag.loginname order by sum(ag.amount) asc");
				List agList = this.smWeeklyDao.getHibernateTemplate().executeFind(new HibernateCallback() {
					@Override
					public Object doInHibernate(Session session) throws HibernateException,
							SQLException {
						Query query = session.createQuery(sql.toString());
						query.setFirstResult(0);
						query.setMaxResults(200); //设置总共统计前200名
						return query.list();
					}
				});*/
				
				//查询目前已有的周竞赛数据,已有则不新增
				dc = DetachedCriteria.forClass(SlotsMatchWeekly.class);
				dc.setProjection(Projections.projectionList().add(Projections.property("loginname"),"loginname"));
				dc.add(Restrictions.eq("startTime", weekStart));
				dc.add(Restrictions.eq("endTime", weekEnd));
				dc.add(Restrictions.eq("platform", platform));
				List<String> sml = this.smWeeklyDao.getHibernateTemplate().findByCriteria(dc);
				
				List<String> exist = new ArrayList<String>();
				for (Transfer t : transList) { //将有转出记录的玩家记录在周比赛表中
					String t_platform = platform.equals("pt")?"newpt":platform;
					if (sml.contains(t.getLoginname()) || exist.contains(t.getLoginname())){ //该玩家已有盈利数据,或者在transList出现两次(因为查询sql语句并没有每个用户只取一条)
						//log.info("本周已有该用户: "+t.getLoginname());
						continue;
					}
					if (maps.get(t.getLoginname()).before(t.getCreatetime())){ //此次转账是否在参加体验金之后
						SlotsMatchWeekly smw = new SlotsMatchWeekly();
						smw.setLoginname(t.getLoginname());
						smw.setStartTime(weekStart);
						smw.setEndTime(weekEnd);
						smw.setGetTime(t.getCreatetime());
						smw.setPlatform(platform);
						if (t.getTarget().equals(t_platform) && t.getSource().equals("elufa")){ //判断玩家的第一笔转账是转入还是转出
							smw.setWin(0.0);
						} else if (t.getTarget().equals("elufa") && t.getSource().equals(t_platform)) {
							Double win = t.getRemit()<gift.get(t.getLoginname())?0:t.getRemit()-gift.get(t.getLoginname());
							smw.setWin(win);
						}
						this.smWeeklyDao.save(smw);
						exist.add(t.getLoginname());
					}
				}
				log.info(platform+"老虎机"+startTime+"比赛数据更新完成!");
				
				/*for (Object obj : agList) {
					Object[] os = (Object[]) obj;
					//将金额纠正为整数并减去8元
					Double win = Double.parseDouble(os[2].toString());
					win = -win<0?0:-win-8;
					boolean ifupdate = false;
					for (SlotsMatchWeekly smw : sml) { //多重判断,效率低,但由于要使用对象以及hibernate session,没发现更好的方案
						if (smw.getLoginname().equals(os[0]) && smw.getWin()==win){
							smw.setBet(Double.valueOf(os[1].toString()));
							smw.setWin(win);
							this.smWeeklyDao.update(smw);
							ifupdate = true;
							break;
						}
					}
					if (!ifupdate){ 
						SlotsMatchWeekly smw = new SlotsMatchWeekly();
						smw.setLoginname(os[0].toString());
						smw.setBet(Double.valueOf(os[1].toString()));
						smw.setWin(win);
						smw.setStartTime(MatchDateUtil.parseDatetime(startTime));
						smw.setEndTime(MatchDateUtil.parseDatetime(endTime));
						smw.setPlatform(platform);
						this.smWeeklyDao.save(smw);
					}
				}*/
			}
		//} catch (Exception e){
		//	log.error("SlotsMatchWeeklyJob -- generateWeeklyData : ", e);
		//	throw e;
		//}
	}

	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}

	public void setTransferDao(TransferDao transferDao) {
		this.transferDao = transferDao;
	}

	public void setSmWeeklyDao(SlotsMatchWeeklyDao smWeeklyDao) {
		this.smWeeklyDao = smWeeklyDao;
	}
}
