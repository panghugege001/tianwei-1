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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;


public class TopicStatusDao extends UniversalDao {

	public TopicStatusDao() {
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
	
	public void saveOrUpdateAll(List list) {
		getHibernateTemplate().saveOrUpdateAll(list);
	}

	public void saveOrUpdate(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void update(Object o) {
		getHibernateTemplate().update(o);
	}
	
	public void deleteByTopicId(Integer topicId)  {
		String sql = "delete from topicstatus where topic_id = "+topicId;
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	
	public void deleteById(Integer statusId)  {
		String sql = "delete from topicstatus where id = "+statusId;
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	
	public void batchDelete(String ids) throws Exception {
		String sql = "delete from topicstatus where topic_id in ("+ids+")";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	
	public void disabledTopic(String ids) throws Exception {
		String sql = "update  topicstatus set is_valid = 1 where topic_id in ("+ids+")";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
	
	public void updateUserUnRead(Integer topicId)
	{
		StringBuilder updateSql =  new StringBuilder();
		updateSql.append("update topicstatus set is_user_read = 0 where topic_id = "+topicId);
		this.getSession().createSQLQuery(updateSql.toString()).executeUpdate();
	}
	
	public void batchDeleteTopicStatus(String ids) throws Exception {
		String sql = "delete from topicstatus where id in ("+ids+") and topic_id = -1";//topic_id = -1 可以不加
		this.getSession().createSQLQuery(sql).executeUpdate();
	}
}
