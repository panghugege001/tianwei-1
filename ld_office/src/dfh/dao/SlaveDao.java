// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UniversalDao.java

package dfh.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;


public class SlaveDao extends SlaveHibernateDaoSupport {

	public SlaveDao() {
	}

	public void delete(Class clazz, Serializable id) {
		getHibernateTemplate().delete(get(clazz, id));
	}

	public List findByCriteria(DetachedCriteria criteria) {
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResult) {
		return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResult);
	}

	public List findByNamedQuery(String sql, Map params) {
		int len = params.size();
		String paramNames[] = new String[len];
		Object paramValues[] = new Object[len];
		int index = 0;
		for (Iterator i = params.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			paramNames[index] = key;
			paramValues[index++] = params.get(key);
		}

		return getHibernateTemplate().findByNamedQueryAndNamedParam(sql, paramNames, paramValues);
	}
	
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
	
	public List getAgentReferralsCountAtPayorder(String agentName,Date start,Date end)throws Exception{
		/*
		String sql="select count(distinct p.loginname),sum(p.money) from payorder p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and flag=0";
		Query q = this.getSession().createSQLQuery(sql).setParameter(0, agentName).setParameter(1, start).setParameter(2, end);
		return (Object[]) q.uniqueResult();
		*/
		
		String sql="select p.loginname,sum(money) from payorder p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and p.flag=0 group by p.loginname";
		Query q = this.getSession().createSQLQuery(sql).setParameter(0, agentName).setParameter(1, start).setParameter(2, end);
		return q.list();
	}
	
	public List getAgentReferralsCountAtProposal(List<String> agentNames, Date start, Date end)throws Exception{
		
		if(agentNames == null || agentNames.size() == 0){
			return null;
		}
		
		String sql = null;
		Query q = null;
		
		if(agentNames.size() > 2000){
			//String sql="select p.loginname,sum(amount) from proposal p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and p.type=? and p.flag=? group by p.loginname";
//			sql="SELECT s.agent agent, s.loginname loginname FROM (SELECT u.agent agent, p.loginname, sum(amount) amount FROM proposal p INNER JOIN users u ON p.loginname = u.loginname WHERE u.agent is not null and p.createtime > ? and p.createtime <= ? AND p.type = ? AND p.flag = ? AND p.amount > 0 GROUP BY p.loginname ORDER BY u.agent) s ";
			sql="SELECT u.agent agent, p.loginname loginname, sum(amount) amount FROM proposal p INNER JOIN users u ON p.loginname = u.loginname WHERE u.agent is not null and p.createtime > ? and p.createtime <= ? AND p.type = ? AND p.flag = ? AND p.amount > 0 GROUP BY p.loginname ORDER BY u.agent";
			q = this.getSession().createSQLQuery(sql);
			q.setParameter(0, start).setParameter(1, end);
			q.setParameter(2, ProposalType.CASHIN.getCode()).setParameter(3, ProposalFlagType.EXCUTED.getCode());
		} else {
			//String sql="select p.loginname,sum(amount) from proposal p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and p.type=? and p.flag=? group by p.loginname";
			//sql="SELECT s.agent agent, s.loginname loginname FROM (SELECT u.agent agent, p.loginname, sum(amount) amount FROM proposal p INNER JOIN users u ON p.loginname = u.loginname WHERE u.agent in(" + this.createIn(agentNames) + ") and p.createtime > ? and p.createtime <= ? AND p.type = ? AND p.flag = ? AND p.amount > 0 GROUP BY p.loginname ORDER BY u.agent) s ";
			sql="SELECT u.agent agent, p.loginname loginname, sum(amount) amount FROM proposal p INNER JOIN users u ON p.loginname = u.loginname WHERE u.agent in(" + this.createIn(agentNames) + ") and p.createtime > ? and p.createtime <= ? AND p.type = ? AND p.flag = ? AND p.amount > 0 GROUP BY p.loginname ORDER BY u.agent";
			q = this.getSession().createSQLQuery(sql);
			q.setParameter(0, start).setParameter(1, end);
			q.setParameter(2, ProposalType.CASHIN.getCode()).setParameter(3, ProposalFlagType.EXCUTED.getCode());
		}
		return q.list();
	}
	
	private Object createIn(List<String> agentNames) {
		String in = "";
		for(String s : agentNames){
			in += "'" + s + "',";
		}
		if(in.endsWith(",")){
			in = in.substring(0,in.length()-1);
		}
		return in;
	}

	public List getAgentReferralsCountAtPayorder(List<String> agentNames, Date start, Date end)throws Exception{
		if(agentNames == null || agentNames.size() == 0){
			return null;
		}
		String sql = null;
		Query q = null;
		if(agentNames.size() > 2000){
			//String sql="select p.loginname,sum(money) from payorder p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and p.flag=0 group by p.loginname";
			//sql="SELECT s.agent agent,s.loginname loginname FROM (SELECT u.agent agent, p.loginname, sum(money) FROM payorder p INNER JOIN users u ON p.loginname = u.loginname WHERE u.agent is not null  and p.createtime > ? and p.createtime <= ? AND p.flag = 0 GROUP BY p.loginname ORDER BY u.agent) s";
			sql="SELECT u.agent agent, p.loginname loginname, sum(money) FROM payorder p INNER JOIN users u ON p.loginname = u.loginname WHERE u.agent is not null  and p.createtime > ? and p.createtime <= ? AND p.flag = 0 GROUP BY p.loginname ORDER BY u.agent";
			q = this.getSession().createSQLQuery(sql).setParameter(0, start).setParameter(1, end);
		} else {
			//sql="SELECT s.agent agent,s.loginname loginname FROM (SELECT u.agent agent, p.loginname, sum(money) FROM payorder p INNER JOIN users u ON p.loginname = u.loginname WHERE u.agent in(" + this.createIn(agentNames) + ")  and p.createtime > ? and p.createtime <= ? AND p.flag = 0 GROUP BY p.loginname ORDER BY u.agent) s";
			sql="SELECT u.agent agent, p.loginname loginname, sum(money) FROM payorder p INNER JOIN users u ON p.loginname = u.loginname WHERE u.agent in(" + this.createIn(agentNames) + ")  and p.createtime > ? and p.createtime <= ? AND p.flag = 0 GROUP BY p.loginname ORDER BY u.agent";
			q = this.getSession().createSQLQuery(sql).setParameter(0, start).setParameter(1, end);
		}
		return q.list();
	}

	public Object get(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	public Object get(Class clazz, Serializable id, LockMode mode) {
		return getHibernateTemplate().get(clazz, id, mode);
	}

	public Integer getCount(Class clazz) {
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		dc.setProjection(Projections.rowCount());
		return (Integer) getHibernateTemplate().findByCriteria(dc).get(0);
	}

	public List loadAll(Class clazz) {
		return getHibernateTemplate().loadAll(clazz);
	}
	
	public Object save(Object o) {
		return getHibernateTemplate().save(o);
	}

	public void saveOrUpdate(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void update(Object o) {
		getHibernateTemplate().update(o);
	}
	
}
