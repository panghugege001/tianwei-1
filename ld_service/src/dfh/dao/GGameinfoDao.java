package dfh.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dfh.action.vo.AutoXima;
import dfh.model.GGameinfo;


public class GGameinfoDao extends HibernateDaoSupport  {
	
	public Double getValidBetAmount(Date endTime, Date startTime,
			String loginname){
		Criteria c = this.getSession().createCriteria(GGameinfo.class);
		c.setProjection(Projections.sum("validbetamount"));
		c.add(Restrictions.eq("flag", 0)).add(Restrictions.eq("loginname", loginname));
		c.add(Restrictions.ge("starttime", startTime));
		c.add(Restrictions.lt("starttime", endTime));

		return (Double) c.uniqueResult();
	}
	
	public void save(Object o){
		this.getHibernateTemplate().save(o);
	}
	
	public List findByCriteria(DetachedCriteria c){
		return this.getHibernateTemplate().findByCriteria(c);
	}
	
	public List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResult) {
		return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResult);
	}
	
	public List searchXimaDetail(String loginname, Date startTime, Date endTime,int offset,int length)throws Exception{
		String hql="select a.pno,a.type,a.flag,b.firstcash,b.trycredit,b.rate,b.starttime,b.endtime from proposal a,xima b where a.pno=b.pno and b.endtime>=? and b.endtime<? and a.loginname=? and a.type in(517,561, 611 , 612 , 613 ,614 ,615 ,616 ,617,618,619,620,622,623,624,625,628,629) order by endtime desc";
		 SQLQuery query = this.getSession().createSQLQuery(hql);
		 query.setParameter(0, startTime);
		 query.setParameter(1, endTime);
		 query.setParameter(2, loginname);
		 query.setFirstResult(offset).setMaxResults(length);
		return query.list();
	 }
	
	public AutoXima getTotalCount(String loginname,Date startTime,Date endTime)throws Exception{
		String hql="select count(*) totalCount,sum(b.firstcash) firstcash,sum(b.trycredit) trycredit from proposal a,xima b where a.pno=b.pno and b.endtime>=? and b.endtime<? and a.loginname=? and a.type in(517,561, 611 , 612 , 613 ,614 ,615 ,616 ,617,618,619,623,624,625,628,629)";
		SQLQuery query = this.getSession().createSQLQuery(hql);
		 query.setParameter(0, startTime);
		 query.setParameter(1, endTime);
		 query.setParameter(2, loginname);
		 Object result = query.uniqueResult();
		 if (result==null) {
			return null;
		 }
		 Object[] arr=(Object[]) result;
		 return new AutoXima((Double)arr[1], (Double)arr[2],((BigInteger)arr[0]).intValue());
	}
	
	public List searchXimaList(String hql,Object[] param){
		return this.getHibernateTemplate().find(hql, param);		
	}

}
