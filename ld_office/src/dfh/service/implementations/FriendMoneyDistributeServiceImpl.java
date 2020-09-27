package dfh.service.implementations;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.FriendMoneyVo;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.model.Friendbonus;
import dfh.model.Friendbonusrecord;
import dfh.model.Goddessrecord;
import dfh.model.Proposal;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.service.interfaces.IFriendMoneyDistributeService;
import dfh.utils.Arith;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.compare.GoddessrecordComparator;
import edu.emory.mathcs.backport.java.util.Collections;

public class FriendMoneyDistributeServiceImpl extends UniversalServiceImpl implements
		IFriendMoneyDistributeService {
	
	private static Logger log = Logger.getLogger(FriendMoneyDistributeServiceImpl.class);
	
	private SeqDao seqDao;
	private TaskDao taskDao;
	
	
	public SeqDao getSeqDao() {
		return seqDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	@Override
	public String calculateFriendMoneyByDate(String distributeDate)
			throws Exception {
		
		//时间6点
		
		//已经派发过奖不能再派发
		DetachedCriteria dcd = DetachedCriteria.forClass(Friendbonusrecord.class);
		dcd.add(Restrictions.eq("distributeDate", distributeDate));
		dcd.add(Restrictions.eq("type", "1"));
		List<Friendbonusrecord> friendbonusrecords = this.findByCriteria(dcd);
		
		if(friendbonusrecords != null && friendbonusrecords.size() > 0){
			return distributeDate + "已经派发过好友推荐金，不能重复派发";
		}
				
		//优惠与平台输赢差1天
		Date timeBegin_y = DateUtil.parseDateForStandard(distributeDate + " 00:00:00");//优惠日期
//		Date timeEnd_y = DateUtil.parseDateForStandard(distributeDate + " 23:59:59");
		Date timeBegin_p = DateUtil.getDateAfter(timeBegin_y,1);//平台输赢日期
//		Date timeEnd_p = this.getDateAfter(timeEnd_y,1);
		Date now = new Date();
		if(timeBegin_p.compareTo(now)>0){
			return "注意:派发时间必须是昨天及其以前!";
		}
		
		String agsql = " SELECT f.toplineuser as toplineuser, f.downlineuser as downlineuser, sum(a.amount) as amount FROM friendintroduce f LEFT JOIN agprofit a ON (f.downlineuser = a.loginname) "
				+ " WHERE	TO_DAYS(a.createTime) = TO_DAYS(:date) AND a.platform in('newpt' , 'nt' , 'qt' , 'ttg', 'mg', 'dt') "
				+ " GROUP BY f.downlineuser ";
		Map<String,Object> agmap = new HashMap<String, Object>();
		agmap.put("date", timeBegin_p);
		
		String giftamountsql = " SELECT f.toplineuser as toplineuser, f.downlineuser as downlineuser, SUM(p.amount) as amount FROM friendintroduce f "
				+ " LEFT JOIN proposal p ON (f.downlineuser = p.loginname) "
				+ " WHERE TO_DAYS(p.executeTime) = TO_DAYS(:date) AND p.flag =:pstatus AND p.type NOT IN (:depositcode, :withdrawalcode) "
				+ " AND (p.giftamount IS NULL OR p.giftamount =:amount) "
				+ " GROUP BY f.downlineuser ";
		Map<String, Object> pramas_amount = new HashMap<String, Object>();
		pramas_amount.put("pstatus", 2);
		pramas_amount.put("date", timeBegin_y);
		pramas_amount.put("amount", 0);
		pramas_amount.put("depositcode", 502);
		pramas_amount.put("withdrawalcode", 503);

		String amountsql = " SELECT f.toplineuser as toplineuser, f.downlineuser as downlineuser, SUM(p.giftamount) as amount FROM friendintroduce f "
				+ " LEFT JOIN proposal p ON (f.downlineuser = p.loginname) "
				+ " WHERE TO_DAYS(p.executeTime) = TO_DAYS(:date) "
				+ " AND p.flag =:pstatus AND p.giftamount >:amount "
				+ " GROUP BY f.downlineuser ";
		
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query_ag = session.createSQLQuery(agsql).setProperties(agmap);
		List<Object[]> list_ag = query_ag.list();
		
		Query query_giftamount = session.createSQLQuery(giftamountsql).setProperties(pramas_amount);
		List<Object[]> list_giftamount = query_giftamount.list();
		
		Query query_amount = session.createSQLQuery(amountsql).setProperties(pramas_amount);
		List<Object[]> list_amount = query_amount.list();
		
		Map<String,FriendMoneyVo> map = this.creatDistributeUsers(list_ag, list_giftamount, list_amount);
		if(map.size() == 0){
			return "没有查询到" + distributeDate + "可派发好友推荐金的记录";
		}
		
		int total = 0;
		log.info("派发好友推荐金开始-----------推荐列表人数:" + map.size() + ":::   " + timeBegin_p.toString());
		for(Entry<String, FriendMoneyVo> e : map.entrySet()){
			FriendMoneyVo fm = e.getValue();
			Double money = Arith.sub(fm.getAmount_ag(), fm.getAmount_pro());
			money = Arith.round(Arith.mul(money, 0.3),2);
			
			Friendbonusrecord fr = new Friendbonusrecord();
			fr.setCreatetime(new Date());
			fr.setDownlineuser(fm.getDownlineuser());
			fr.setToplineuser(fm.getToplineuser());
			fr.setMoney(money);
			fr.setType("1");
			fr.setDistributeDate(distributeDate);
			DetachedCriteria dc = DetachedCriteria.forClass(Friendbonus.class);
			dc.add(Restrictions.eq("toplineuser", fm.getToplineuser()));
			List<Friendbonus> friendbonuses = this.findByCriteria(dc);
			Friendbonus fb = null;
			if(friendbonuses != null && friendbonuses.size() > 0){
				fb = friendbonuses.get(0);
				fb.setCreatetime(new Date());
				fb.setMoney(Arith.round(Arith.add(money, fb.getMoney()),2));
			} else {
				fb = new Friendbonus();
				fb.setCreatetime(new Date());
				fb.setMoney(money);
				fb.setToplineuser(fm.getToplineuser());
			}
			fr.setRemark("下线玩家输赢值：" + fm.getAmount_ag() + ",提案优惠额：" + fm.getAmount_pro() + ",好友推荐奖金派发额:" + money + "元,上线玩家" +fm.getToplineuser() + "奖励后余额为：" + fb.getMoney());
			this.save(fr);
			this.saveOrUpdate(fb);
			total++;
		}
		
		log.info("派发好友推荐金完毕------------，派发记录条数：" + total);
		return "派发好友推荐金成功，派发记录条数：" + total;
	}

	private Map<String, FriendMoneyVo> creatDistributeUsers(List<Object[]> list_ag,
			List<Object[]> list_giftamount, List<Object[]> list_amount) {
		
		Map<String, FriendMoneyVo> map = new HashMap<String, FriendMoneyVo>();
		
		if(list_ag != null && list_ag.size() > 0){
			
			Iterator<Object[]> it = list_ag.iterator();
			while(it.hasNext()){
				Object[] objs = it.next();
				if(objs[0] == null || objs[1] == null){
					continue;
				}
				
				FriendMoneyVo fm = new FriendMoneyVo();
				fm.setToplineuser(objs[0].toString());
				fm.setDownlineuser(objs[1].toString());
				fm.setAmount_ag(Double.parseDouble(objs[2].toString()));
				fm.setAmount_pro(0.0);
				map.put(fm.getDownlineuser(), fm);
			}
		}
		
		if(list_giftamount != null && list_giftamount.size() > 0){
			
			Iterator<Object[]> it = list_giftamount.iterator();
			while(it.hasNext()){
				
				Object[] objs = it.next();
				if(objs[0] == null || objs[1] == null){
					continue;
				}
				
				if(map.containsKey(objs[1].toString())){
					FriendMoneyVo fm = map.get(objs[1].toString());
					fm.setAmount_pro(Arith.add(fm.getAmount_pro(), Double.parseDouble(objs[2].toString())));
				} else {
					FriendMoneyVo fm = new FriendMoneyVo();
					fm.setToplineuser(objs[0].toString());
					fm.setDownlineuser(objs[1].toString());
					fm.setAmount_ag(0.0);
					fm.setAmount_pro(Double.parseDouble(objs[2].toString()));
					map.put(fm.getDownlineuser(), fm);
				}
			}
		}
		
		if(list_amount != null && list_giftamount.size() > 0){
			Iterator<Object[]> it = list_amount.iterator();
			while(it.hasNext()){
				
				Object[] objs = it.next();
				if(objs[0] == null || objs[1] == null){
					continue;
				}
				
				if(map.containsKey(objs[1].toString())){
					FriendMoneyVo fm = map.get(objs[1].toString());
					fm.setAmount_pro(Arith.add(fm.getAmount_pro(), Double.parseDouble(objs[2].toString())));
				} else {
					FriendMoneyVo fm = new FriendMoneyVo();
					fm.setToplineuser(objs[0].toString());
					fm.setDownlineuser(objs[1].toString());
					fm.setAmount_ag(0.0);
					fm.setAmount_pro(Double.parseDouble(objs[2].toString()));
					map.put(fm.getDownlineuser(), fm);
				}
			}
		}
		return map;
	}

	@Override
	public String calculateFlower() throws Exception {
		
		Date today = DateUtil.getYYYY_MM_DD();
		Date yesterday = DateUtil.getDateAfter(today,-1);
		Date begin = DateUtil.parseDateForStandard("2016-08-02 00:00:00");
		Date end = DateUtil.parseDateForStandard("2016-09-01 23:59:59");
		if(today.compareTo(begin) < 0 || today.compareTo(end) > 0){
			return "您所使用的功只能在：" + DateUtil.fmtYYYY_MM_DD(begin) + "到" + DateUtil.fmtYYYY_MM_DD(end) + "进行操作";
		}
		int date = yesterday.getDate();
		DetachedCriteria dcg = DetachedCriteria.forClass(Goddessrecord.class);
		dcg.add(Restrictions.like("distributeMonth", "%" + date + ",%"));
		List<Goddessrecord> list = this.findByCriteria(dcg);
		
		if(list != null && list.size() > 0){
			return "您已经派发过" + DateUtil.fmtYYYY_MM_DD(yesterday) + "的鲜花了，不能重复派发！";
		}
		
		String agsql = " SELECT g.loginname AS loginname, SUM(a.bettotal) AS bettotal FROM goddessrecord g LEFT JOIN agprofit a ON (g.loginname = a.loginname) "
				+ " WHERE a.platform IN ('qt', 'nt', 'ttg') and TO_DAYS(a.createTime) = TO_DAYS(:date) "
				+ " GROUP BY a.loginname ";
		
		Map<String,Object> agmap = new HashMap<String, Object>();
		agmap.put("date", today);
		
		String ptsql = " SELECT g.loginname as loginname, p.bets_tiger as bets_tiger FROM goddessrecord g LEFT JOIN pt_data_new p ON(g.loginname = SUBSTR(p.playername,2)) "
				+ " WHERE p.STARTTIME =:disbegin and p.ENDTIME =:disend ";
		Date disbegin = DateUtil.parseDateForStandard(DateUtil.fmtYYYY_MM_DD(yesterday) + " 00:00:00");
		Date disend =  DateUtil.parseDateForStandard(DateUtil.fmtYYYY_MM_DD(yesterday) + " 23:59:59");
		agmap.put("disbegin", disbegin);
		agmap.put("disend", disend);
		
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query_ag = session.createSQLQuery(agsql).setProperties(agmap);
		List<Object[]> list_ag = query_ag.list();
		Query query_pt = session.createSQLQuery(ptsql).setProperties(agmap);
		List<Object[]> list_pt = query_pt.list();
		
		Map<String, Double> map = this.mergeData(list_ag, list_pt);
		if(map == null || map.size() == 0){
			return "没有找到" + DateUtil.fmtYYYY_MM_DD(yesterday) + "可派发数据。";
		}
		
		DetachedCriteria dcall = DetachedCriteria.forClass(Goddessrecord.class);
		List<Goddessrecord> all = this.findByCriteria(dcall);
		Iterator<Goddessrecord> it = all.iterator();
		while(it.hasNext()){
			Goddessrecord goddess = it.next();
			Double bet = map.get(goddess.getLoginname());
			if(bet != null){
				goddess.setBettotal(Arith.add(goddess.getBettotal() == null? 0.0 : goddess.getBettotal(), bet));
				goddess.setFlowernum((int)(goddess.getBettotal()/1000));
			}
			goddess.setDistributeMonth((goddess.getDistributeMonth() == null? "" : goddess.getDistributeMonth()) + date + ",");
			//this.update(goddess);
		}
		
		Collections.sort(all, new GoddessrecordComparator());
		this.ranking(all);
		Iterator<Goddessrecord> it_ = all.iterator();
		while(it_.hasNext()){
			this.update(it_.next());
		}
		return "派发鲜花成功";
	}

	private Map<String, Double> mergeData(List<Object[]> list_ag,
			List<Object[]> list_pt) {
		
		Map<String, Double> map = new HashMap<String, Double>();
		
		if(list_ag != null && list_ag.size() > 0){
			
			Iterator<Object[]> it_ag = list_ag.iterator();
			while(it_ag.hasNext()){
				Object[] objs = it_ag.next();
				if(objs[0] == null || objs[1] == null){
					continue;
				}
				String loginname = objs[0].toString();
				Double bettotal = Double.parseDouble(objs[1].toString());
				map.put(loginname, bettotal);
			}
		}
		
		if(list_pt != null && list_pt.size() > 0){
			Iterator<Object[]> it_pt = list_pt.iterator();
			while(it_pt.hasNext()){
				Object[] objs = it_pt.next();
				if(objs[0] == null || objs[1] == null){
					continue;
				}
				String loginname = objs[0].toString();
				Double bettotal = Double.parseDouble(objs[1].toString());
				if(map.containsKey(loginname)){
					map.put(loginname, Arith.add(map.get(loginname), bettotal));
				} else {
					map.put(loginname, bettotal);
				}
			}
			
		}
		
		return map;
	}


	@Override
	public String calculateRankingAndCoupon(String proposer) throws Exception {
		Date today = DateUtil.getYYYY_MM_DD();
		Date begin = DateUtil.parseDateForStandard("2016-09-01 00:00:00");
		Date end = DateUtil.parseDateForStandard("2016-09-01 23:59:59");
		if(today.compareTo(begin) < 0 || today.compareTo(end) > 0){
			return "您所使用的功只能在：" + DateUtil.fmtYYYY_MM_DD(begin) + "进行操作";
		}
		
		DetachedCriteria dcall = DetachedCriteria.forClass(Goddessrecord.class);
		dcall.addOrder(Order.desc("flowernum"));
		List<Goddessrecord> all = this.findByCriteria(dcall);
		
		String firstGoddess = this.findFirstRankingGoddess();
		if(StringUtils.isBlank(firstGoddess)){
			return "查询女神排名出错";
		}
		if(all != null && all.size() > 0){
			
			this.ranking(all);
			
			Iterator<Goddessrecord> it_all = all.iterator();
			while(it_all.hasNext()){
				Goddessrecord record = it_all.next();
				if(record.getFlowernum() == null || record.getFlowernum() < 8 || !firstGoddess.equals(record.getGoddessname())){
					this.save(record);
					continue;
				}
				
				Double giftmoney = null;
				if(record.getFlowernum() >= 1888){
					giftmoney = 3888.0;
				} else if(record.getFlowernum() >= 888){
					giftmoney = 1888.0;
				} else if(record.getFlowernum() >= 88){
					giftmoney = 188.0;
				} else if(record.getFlowernum() >= 8){
					giftmoney = 58.0;
				}
				
				Proposal p = this.createProposal(proposer, record.getLoginname(), giftmoney, "15", "乐虎守护女神活动一15倍流水");
				record.setCouponnum(p.getShippingCode());
				this.save(record);
			}
			
		}
		
		return "派发红包计算排名执行结束";
	}

	private String findFirstRankingGoddess() {

		String sql = " SELECT goddessname,SUM(flowernum) as flowernum,SUM(bettotal) as bettotal,COUNT(loginname) as usernum from goddessrecord GROUP BY goddessname ORDER BY flowernum DESC,bettotal DESC,usernum ASC ";
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);
		List<Object[]> list = query.list();
		if(session != null && session.isOpen()){
			session.close();
		}
		if(list == null || list.size() == 0){
			return null;
		} else {
			return list.get(0)[0].toString();
		}
	}

	private void ranking(List<Goddessrecord> all) {
		
		if(all != null && all.size() > 0){
			
			int flowernum = -1;
			int ranking = 1;
			for(int i = 0; i < all.size(); i++){
				
				Goddessrecord record = all.get(i);
				if(i == 0){
					record.setRanking(ranking);
					flowernum = record.getFlowernum() == null? 0 : record.getFlowernum();
				} else {
					if(record.getFlowernum() != null && record.getFlowernum() == flowernum){
						record.setRanking(ranking);
					} else {
						ranking = i + 1;
						record.setRanking(ranking);
						flowernum = record.getFlowernum() == null? 0 : record.getFlowernum();
					}
				}
			}
		}
	}

	private Proposal createProposal(String proposer,String loginname, Double giftmoney, String betMultiples, String remark) {
		try {
			String pno = seqDao.generateProposalPno(ProposalType.REDCOUPON419);
			Proposal proposal = new Proposal(pno, proposer, DateUtil.now(), ProposalType.REDCOUPON419.getCode(), null, null, 0.0, null, ProposalFlagType.AUDITED.getCode(), Constants.FROM_BACK, null, null);
			proposal.setLoginname(loginname);
			proposal.setBetMultiples(betMultiples);
			proposal.setShippinginfo(remark);
			if (giftmoney == null) {
				proposal.setGifTamount(0.00);
			} else {
				proposal.setGifTamount(giftmoney);
			}
			String str = "HB";
			String sqlCouponId = seqDao.generateYhjID();
			String codeOne = dfh.utils.StringUtil.getRandomString(3);
			String codeTwo = dfh.utils.StringUtil.getRandomString(3);
			proposal.setShippingCode(str + codeOne + sqlCouponId + codeTwo);
			save(proposal);
			taskDao.generateTasks(pno, proposer);
			return proposal;
		} catch (Exception e) {
			log.error("生成红包失败：" + e.toString());
		}
		return null;
	}

	@Override
	public String updateBettotal(String loginname, Double newbet) {
		DetachedCriteria dc = DetachedCriteria.forClass(Goddessrecord.class);
		dc.add(Restrictions.eq("loginname", loginname));
		List<Goddessrecord> list = this.findByCriteria(dc);
		if(list == null || list.size() == 0){
			return "未查找到账户" + loginname + "守护女神数据";
		}
		Goddessrecord g = list.get(0);
		g.setBettotal(newbet);
		g.setFlowernum((int)(newbet/1000));
		this.save(g);
		return "更新成功";
	}

/*	@Override
	public void updateSingleParty(Date agStart, Date agEnd,	Date ptStart, Date ptEnd, String rankdate) {
		
		String countQ = " SELECT count(*) FROM singleparty WHERE TO_DAYS(rankdate) >= TO_DAYS(:rankdate) ";
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("rankdate", rankdate);
		Session session = this.getHibernateTemplate().getSessionFactory().getCurrentSession();
		Query queryC = session.createSQLQuery(countQ).setProperties(param);
		List q = queryC.list();
		if(q.get(0) != null && Integer.parseInt(q.get(0).toString()) > 0){
			if(session != null && session.isOpen()){
				session.close();
			}
			return;
		}
//		String countS = " SELECT COUNT(s4.loginname) FROM( ";
		String selectS = " SELECT s3.loginname as loginname,	SUM(s3.bettotal) as bettotal FROM "
				+ " ((SELECT SUM(a.bettotal) AS bettotal,	u.loginname AS loginname FROM agprofit a LEFT JOIN users u ON (u.loginname = a.loginname) "
				+ " WHERE a.createTime >=:agStart AND a.createTime <=:agEnd AND a.platform IN('nt','qt','ttg') AND a.bettotal > 0.0 GROUP BY u.loginname ) "
				+ " UNION (SELECT SUM(p.BETS_TIGER) AS bettotal, u.loginname AS loginname FROM pt_data_new p LEFT JOIN users u ON ( u.loginname = SUBSTR(p.playername, 2)) "
				+ " WHERE p.STARTTIME >=:ptStart AND p.STARTTIME <=:ptEnd GROUP BY u.loginname AND p.BETS_TIGER > 0.0)) AS s3 GROUP BY s3.loginname "
				+ " ORDER BY s3.bettotal  desc ";
//		String limitS = " LIMIT :index,:size ";
//		String countT = " ) as s4 ";
		
		param.put("agStart", agStart);
		param.put("agEnd", agEnd);
		param.put("ptStart", ptStart);
		param.put("ptEnd", ptEnd);
//		param.put("index", index);
//		param.put("size", size);
		
		
		Query queryS = session.createSQLQuery(selectS).setProperties(param);
//		Query queryC = session.createSQLQuery(countS + selectS + countT).setProperties(param);
		List<Object[]> listS = queryS.list();
//		List<Object[]> listC = queryC.list();
		if(session != null && session.isOpen()){
			session.close();
		}
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
			this.saveOrUpdate(it_s.next());
		}
//		System.out.println(listS.size());
//		return null;
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
	}*/

}
