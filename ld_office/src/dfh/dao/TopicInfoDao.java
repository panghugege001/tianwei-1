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
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;

public class TopicInfoDao extends UniversalDao {

	public TopicInfoDao() {
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

	public void batchDelete(String ids) throws Exception {
		String sql = "delete from topicinfo where id in (" + ids + ")";
		this.getSession().createSQLQuery(sql).executeUpdate();
	}

	public List findTopicDetail(String ids) throws Exception {
		String sql = "select id,title,content,create_time,create_uname,ip_address,flag,re_count,topic_type,user_name_type,topic_status from topicinfo where id in ("
				+ ids + ")";
		return this.getSession().createSQLQuery(sql).list();
	}

	public List findTopicHistory(String lastDate) throws Exception {
		String sql = "select id,title,content,create_time,create_uname,ip_address,flag,re_count,topic_type,user_name_type,topic_status from topicinfo where create_time < date('"
				+ lastDate + "')";
		return this.getSession().createSQLQuery(sql).list();
	}

	public List batchTopicApp(String ids) throws Exception {
		StringBuilder recordSql = new StringBuilder(
				"select id,title,content,create_time,create_uname,ip_address,flag,re_count,topic_type,user_name_type,topic_status,receive_uname,is_user_read,is_admin_read from ")
						.append(" (select t.id,	title,	content,	t.create_time,	create_uname,	t.ip_address,	flag,	re_count,	topic_type,	user_name_type,	topic_status,	null as receive_uname,	null as is_user_read,is_admin_read from topicinfo t where t.topic_status =1 and t.id in("
								+ ids + ")")
						.append(" union all (select t.id,title,content,t.create_time,create_uname,t.ip_address,flag,re_count,topic_type,user_name_type,topic_status,receive_uname,is_user_read,null as is_admin_read")
						.append(" from topicinfo t left join topicstatus s on t.id = s.topic_id where  t.topic_status = 0 and t.id in("
								+ ids + ") and t.flag = 0)) a ");

		return this.getSession().createSQLQuery(recordSql.toString()).list();

	}

	public void updateAdminRead(Integer topicId, String loginName) {
		StringBuilder updateSql = new StringBuilder();
		updateSql.append("update topicinfo set is_admin_read = 1 where id = " + topicId);
		this.getSession().createSQLQuery(updateSql.toString()).executeUpdate();
	}

}
