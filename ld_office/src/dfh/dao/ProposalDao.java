package dfh.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import dfh.model.Proposal;
import dfh.model.bean.AgSlotXima;
import dfh.model.bean.GPI4Xima;
import dfh.model.bean.QT4Xima;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.utils.DateUtil;

public class ProposalDao extends UniversalDao {

	private static Logger log = Logger.getLogger(ProposalDao.class);

	public ProposalDao() {
	}
	
	@SuppressWarnings("unchecked")
	public List getAgentReferralsDetailAtProposal(String agentName,Date start,Date end,int offset,int length)throws Exception{
		String sql="select p.loginname,sum(amount) from proposal p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and p.type=? and p.flag=? group by p.loginname";
		Query q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, agentName).setParameter(1, start).setParameter(2, end);
		q.setParameter(3, ProposalType.CASHIN.getCode()).setParameter(4, ProposalFlagType.EXCUTED.getCode());
		q.setFirstResult(offset).setMaxResults(length);
		return q.list();
	}
	
	@SuppressWarnings("unchecked")
	public List getAgentReferralsCountAtProposal(String agentName,Date start,Date end)throws Exception{
		/*
		String sql="select count(distinct p.loginname),sum(p.amount) from proposal p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and type=? and flag=?";
		Query q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, agentName).setParameter(1, start);
		q.setParameter(2, end).setParameter(3, ProposalType.CASHIN.getCode());
		q.setParameter(4, ProposalFlagType.EXCUTED.getCode());
		return (Object[]) q.uniqueResult();
		*/
		
		String sql="select p.loginname,sum(amount) from proposal p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and p.type=? and p.flag=? group by p.loginname";
		Query q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, agentName).setParameter(1, start).setParameter(2, end);
		q.setParameter(3, ProposalType.CASHIN.getCode()).setParameter(4, ProposalFlagType.EXCUTED.getCode());
		return q.list();
	}

	public Proposal getFistConcessionsUsers(String name)throws Exception{
		Criteria c = this.getSession().createCriteria(Proposal.class);
		c.add(Restrictions.eq("loginname", name));//.add(Restrictions.eq("type", ProposalType.CONCESSIONS.getCode()))
		c.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		//c.addOrder(Order.asc("createTime"));
		return (Proposal) c.uniqueResult();
	}
	
	public List getFistConcessionsUsers(List names)throws Exception{
		Criteria c = this.getSession().createCriteria(Proposal.class);
		c.add(Property.forName("loginname").in(names));
		//c.add(Restrictions.eq("type", ProposalType.CONCESSIONS.getCode()));
		c.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		//c.addOrder(Order.asc("createTime"));
		return c.list();
	}
	
	
	
	private List getFirstCashinUsers(List list){
		List<Proposal> recvList=new ArrayList<Proposal>();
		for (int i = 0; i < list.size(); i++) {
			Object[] o=(Object[]) list.get(i);
			Proposal proposal=new Proposal();
			proposal.setAmount((Double)o[0]);
			proposal.setLoginname(String.valueOf(o[1]));
			proposal.setCreateTime((Timestamp)o[2]);
			recvList.add(proposal);
		}
		return recvList;
	}
	
	

	public List getFirstCashinUsers(String oneDate,String twoDate,int offset,int length,String agent)throws Exception{
		//String sql="select abs(amount) amount,loginname,min(createtime) mintime from proposal where type=? and flag=? and agent=? group by loginname having mintime>=? and mintime<=?";
		String sql="select * from (select abs(amount) amount,loginname,min(createtime) mintime from proposal where type=? and flag=? and agent=? group by loginname) t where t.mintime >=? and t.mintime<=? order by t.mintime asc";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter(0, ProposalType.CASHIN.getCode());
		query.setParameter(1, ProposalFlagType.EXCUTED.getCode());
		query.setParameter(2, agent);
		query.setParameter(3, oneDate).setParameter(4, twoDate);
		query.setFirstResult(offset).setMaxResults(length);
		
		return getFirstCashinUsers(query.list());
	}
	

	public List getFirstCashinUsers(String oneDate,String twoDate,int offset,int length)throws Exception{
		//String sql="select abs(amount) amount,loginname,min(createtime) mintime from proposal where type=? and flag=? group by loginname having mintime>=? and mintime<=?";
		String sql="select * from (select abs(amount) amount,loginname,min(createtime) mintime from proposal where type=? and flag=? group by loginname) t where t.mintime >=? and t.mintime<=? order by t.mintime asc";
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter(0, ProposalType.CASHIN.getCode());
		query.setParameter(1, ProposalFlagType.EXCUTED.getCode());
		query.setParameter(2, oneDate).setParameter(3, twoDate);
		query.setFirstResult(offset).setMaxResults(length);
		
		return getFirstCashinUsers(query.list());
	}
	

	/**
	 * 获取指定时间段的首次存款用户
	 * (参数格式由外部程序控制)
	 * @param oneDate	存款起始日期，默认1900-00-00
	 * @param twoDate	存款截止日期，默认当前日期
	 * @param oneDays	第一次存款的最小间隔天数，默认0
	 * @param twoDays	第一次存款的最大间隔天数，默认9999
	 * @param offset 	批量查询的起始位置
	 * @param length	批量查询的长度
	 * @return
	 */
	public List getFirstCashinUsers(String oneDate,String twoDate,int oneDays,int twoDays,int offset,int length)throws Exception{
		String hql="select c.loginname," +
				"ELT(1,u.aliasname) aliasname," +
				"ELT(1,u.accountname) accountname," +
				"abs(c.remit) firstAmount," +
				"DATE_FORMAT(u.createtime,GET_FORMAT(DATETIME,'ISO')) registerTime ," +
				"DATE_FORMAT(c.createtime,GET_FORMAT(DATETIME,'ISO')) firstTime," +
				"DATEDIFF(c.createtime,u.createtime) SpaceDays, " +
				"ELT(1,referwebsite) referWebsite," +
				"STR_TO_DATE(c.createtime,GET_FORMAT(DATETIME,'ISO')) cDate " +
				"from creditlogs c left join users u " +
				"on c.loginname=u.loginname  " +
				"where u.iscashin=0 and flag=0 " +
				"group by c.loginname " +
				"having count(c.loginname)=1 and SpaceDays >= "+oneDays+" and SpaceDays <= "+twoDays+" and cDate >= '"+oneDate+"' and cDate <= '"+twoDate+"' " +
				"limit "+offset+","+length;
		Query query = this.getSession().createSQLQuery(hql);
		return query.list();
	}
	
	
	public Object getProposalObject(String loginname){
		Criteria c = this.getSession().createCriteria(Proposal.class);
		c.add(Restrictions.eq("type", ProposalType.CASHIN.getCode())).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		c.add(Restrictions.eq("loginname", loginname)).addOrder(Order.asc("createTime"));
		c.setMaxResults(1);  // top 1   time asc
		return c.uniqueResult();
	}

	public boolean existNotAuditedProposal(String loginname, ProposalType type) {
		if (loginname == null || type == null)
			return false;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", type.getCode())).add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", ProposalFlagType.SUBMITED.getCode()));
		return getHibernateTemplate().findByCriteria(dCriteria).size() > 0;
	}

	public boolean existNotCancledProposal(String loginname, ProposalType type) {
		if (loginname == null || type == null)
			return false;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", type.getCode())).add(Restrictions.eq("loginname", loginname)).add(Restrictions.ne("flag", ProposalFlagType.CANCLED.getCode()));
		return getHibernateTemplate().findByCriteria(dCriteria).size() > 0;
	}

	public boolean existNotFinishedProposal(String loginname, ProposalType type) {
		if (loginname == null || type == null)
			return false;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", type.getCode())).add(Restrictions.eq("loginname", loginname)).add(
				Restrictions.or(Restrictions.eq("flag", ProposalFlagType.SUBMITED.getCode()), Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
		return getHibernateTemplate().findByCriteria(dCriteria).size() > 0;
	}


	public List<Proposal> getCashoutToday(String loginname) {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode())).add(Restrictions.eq("loginname", loginname))
				.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode())).add(Restrictions.ge("createTime", DateUtil.getToday()));
		return getHibernateTemplate().findByCriteria(dCriteria);
	}

	public boolean existNotFinishedProposal(String loginname, ProposalType type, Integer count) {
		if (loginname == null || type == null)
			return false;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", type.getCode())).add(Restrictions.eq("loginname", loginname)).add(
				Restrictions.or(Restrictions.eq("flag", ProposalFlagType.SUBMITED.getCode()), Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
		return findByCriteria(dCriteria).size() > count.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<Proposal> findEntities(String sql)throws Exception{
		return this.getSession().createQuery(sql).list();
	}

	/**
	 * 查询前一天用户的存款额
	 * @return
	 */
	public Double getDepositAmountByUserYes(String loginname, Date date){
		Double total = 0.0;
		String sql = "select sum(money) from payorder where loginname=:loginname and type=:orderType and TO_DAYS(createTime)=TO_DAYS(:date)";
		Query q = this.getSession().createSQLQuery(sql);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		params.put("orderType", 0);  //支付成功的订单
		params.put("date", date);
		q.setProperties(params);
		Object result = q.uniqueResult();
		total += result==null?0.0:(Double)result;
		sql = "select sum(amount) from proposal where loginname=:loginname and type=:pType and flag=:optFlag and TO_DAYS(createTime)=TO_DAYS(:date)";
		q = this.getSession().createSQLQuery(sql);
		params.put("pType", 502);   //存款提案
		params.put("optFlag", 2);   //已执行
		q.setProperties(params);
		result = q.uniqueResult();
		total += result==null?0.0:(Double)result;
		return total;
	}
	
	/**
	 * 查询投注额
	 * @param loginname
	 * @param platform
	 * @param date
	 * @return
	 */
	public Double getBetTotal(String loginname, String platform, Date date){
		String sql="select bettotal from agprofit where loginname=:loginname and platform=:platform and TO_DAYS(createTime) = TO_DAYS(:date)";
		Query q = this.getSession().createSQLQuery(sql);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		params.put("platform", platform);  
		params.put("date", date);   
		q.setProperties(params);
		Object result = q.uniqueResult();
		return result==null?0.0:(Double)result;
	}
	
	/**
	 * 使用原生sql, 获取double值
	 * @return
	 */
	public Double getDoubleValueBySql(String sql, Map<String, Object> params){
		//Query query = getSession().createSQLQuery("select SUM(bet) from platform_data where loginname=? and starttime>=?");
		//query.setParameter(0, startTime);
		Query query = getSession().createSQLQuery(sql);
		query.setProperties(params);
		Object result = query.uniqueResult();
		return null==result?0.00:(Double)result;
	}
	
	/**
	 * 查询单个值
	 * @param sql
	 * @param params
	 * @return
	 */
	public Object getOneObjectBySql(String sql, Map<String, Object> params){
		Query query = getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.uniqueResult();
	}
	
	/**
	 * 查询GPI指定日期的投注额、输赢值（包含rslot）
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GPI4Xima> getGPIXimaList(Date date){
		List<GPI4Xima> gpiResult = new ArrayList<GPI4Xima>();
		String sql = "select SUM(bet), SUM(profit), loginname from platform_data where TO_DAYS(starttime) = TO_DAYS(:date) and platform in(:gpi, :rslot, :png, :bs, :ctxm) group by loginname";
		Query query = this.getSession().createSQLQuery(sql);
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("date", date); 
		params.put("gpi", "gpi"); 
		params.put("rslot", "rslot"); 
		params.put("png", "png"); 
		params.put("bs", "bs"); 
		params.put("ctxm", "ctxm"); 
		query.setProperties(params);
		List list = query.list();
		for (Object object : list) {
			Object [] obj = (Object[]) object;
			gpiResult.add(new GPI4Xima(obj[2].toString(), Double.parseDouble(obj[0].toString()), Double.parseDouble(obj[1].toString())));
		}
		return gpiResult;
	}
	
	public List<QT4Xima> getQTXimaList(Date date){
		List<QT4Xima> qtResult = new ArrayList<QT4Xima>();
		String sql = "select SUM(bet), SUM(profit), loginname from platform_data where TO_DAYS(starttime) = TO_DAYS(:date) and platform = 'qt' group by loginname";
		Query query = this.getSession().createSQLQuery(sql);
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("date", date); 
		query.setProperties(params);
		List list = query.list();
		for (Object object : list) {
			Object [] obj = (Object[]) object;
			qtResult.add(new QT4Xima(obj[2].toString(), Double.parseDouble(obj[0].toString()), Double.parseDouble(obj[1].toString())));
		}
		return qtResult;
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List getListBySql(String sql, Map<String, Object> params)throws Exception{
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.list();
	}
	
	/**
	 * 查询kg百家乐指定日期的投注额、输赢值
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GPI4Xima> getKenoBCXimaList(Date date){
		List<GPI4Xima> gpiResult = new ArrayList<GPI4Xima>();
		String sql = "select SUM(bet), SUM(profit), loginname from platform_data where TO_DAYS(starttime) = TO_DAYS(:date) and platform=:platform group by loginname";
		Query query = this.getSession().createSQLQuery(sql);
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("date", date); 
		params.put("platform", "bc"); 
		query.setProperties(params);
		List list = query.list();
		for (Object object : list) {
			Object [] obj = (Object[]) object;
			gpiResult.add(new GPI4Xima(obj[2].toString(), Double.parseDouble(obj[0].toString()), Double.parseDouble(obj[1].toString())));
		}
		return gpiResult;
	}
	
	public List<AgSlotXima> sqlQueryList(String start,String end,String platform) {
		List<AgSlotXima> ximaResult = new ArrayList<AgSlotXima>();
		String sql = "select playerName,sum(validbetamount),sum(netamount) from agdata  where  startTime >= '"+start+"' and endTime <= '"+end+"' and platform = '"+platform+"' group by playerName";
		Query query = this.getSession().createSQLQuery(sql);
		List list = query.list();
		for (Object object : list) {
			Object [] obj = (Object[]) object;
			ximaResult.add(new AgSlotXima(obj[0].toString(), Double.parseDouble(obj[1].toString()), Double.parseDouble(obj[2].toString())));
		}
	
		return ximaResult;
		
	}
	
	
	public List<AgSlotXima> sqlQueryAginSlotList(String start,String end) {
		List<AgSlotXima> ximaResult = new ArrayList<AgSlotXima>();
		String sql = "select playerName,sum(validbetamount),sum(netamount) from agdata  where  startTime >= '"+start+"' and endTime <= '"+end+"' and platform IN ('BG', 'XIN', 'ENDO', 'NYX')  group by playerName";
		Query query = this.getSession().createSQLQuery(sql);
		List list = query.list();
		for (Object object : list) {
			Object [] obj = (Object[]) object;
			ximaResult.add(new AgSlotXima(obj[0].toString(), Double.parseDouble(obj[1].toString()), Double.parseDouble(obj[2].toString())));
		}
		return ximaResult;
	}
	
	/**
	 * 获得代理及类型（1:'SEO',2:'电销',3:'推广',4:'广告'）
	 * @param agent 代理账号
	 * @param type	代理类型
	 * @return 
	 */
	public Map<String, Integer> getAgentType(){
		Map<String, Integer> map = new HashMap<String, Integer>(); 
		String sql = "select loginname ,type from internal_agency";
		Query query = this.getSession().createSQLQuery(sql);
		List list = query.list();
		if(list !=null && list.size() >0){
			for (int i = 0; i < list.size(); i++) {
				Object[] obj = (Object[]) list.get(i);
				String agent = (String) obj[0];
				Integer type = (Integer) obj[1];
				map.put(agent, type);
			}
		}
		return map;
	}
	/**
	 * 查询平台对应的费率
	 * @return
	 */
	public Map<String, Double> getPlatformFee(){
		Map<String, Double> map = new HashMap<String, Double>();
		String sql = "select platform,fee from platformfee ";
		Query query = this.getSession().createSQLQuery(sql);
		List list = query.list();
		for (int i = 0; i < list.size(); i++) {
			Object[] arr = (Object[]) list.get(i);
			String platform = (String) arr[0];
			Double fee = (Double) arr[1];
			map.put(platform, fee);
		}
		return map;
	}

	public List<QT4Xima> getTTGXimaList(Date date){
		List<QT4Xima> qtResult = new ArrayList<QT4Xima>();
		String sql = "select SUM(bet), SUM(profit), loginname from platform_data where TO_DAYS(starttime) = TO_DAYS(:date) and platform = 'TTG' group by loginname";
		Query query = this.getSession().createSQLQuery(sql);
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("date", date); 
		query.setProperties(params);
		List list = query.list();
		for (Object object : list) {
			Object [] obj = (Object[]) object;
			qtResult.add(new QT4Xima(obj[2].toString(), Double.parseDouble(obj[0].toString()), Double.parseDouble(obj[1].toString())));
		}
		return qtResult;
	}
	
}
