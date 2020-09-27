// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UniversalDao.java

package dfh.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;


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
	
	public List queryListBySql(String sql, Map<String, Object> params){
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.list();
	}

	public List getList(String sql, Map<String, Object> params) {
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.list();
	}

	public Double getDoubleValue(String sql, Map params) {
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		Object obj = query.uniqueResult();
		return null==obj?0.00:Double.parseDouble(obj.toString());
	}
	
	public Integer getCount(String sql, Map<String, Object> params) {
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return Integer.parseInt(query.uniqueResult().toString());
	}
	
	public List queryList(String sql, Map<String, Object> params){
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.list();
	}
	
	public int excuteSql(String sql, Map<String, Object> params){
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.executeUpdate();
	}
}
