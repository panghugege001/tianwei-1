package app.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BaseDao extends HibernateDaoSupport {

	public BaseDao() {}
	
	@SuppressWarnings("rawtypes")
	public Object get(Class entityClass, Serializable id) {
	
		return getHibernateTemplate().get(entityClass, id);
	}
	
	@SuppressWarnings("rawtypes")
	public Object get(Class entityClass, Serializable id, LockMode lockMode) {
	
		return getHibernateTemplate().get(entityClass, id, lockMode);
	}
	
	public void delete(Object entity) {
		
		getHibernateTemplate().delete(entity);
	}
	
	public void delete(Object entity, LockMode lockMode) {
	
		getHibernateTemplate().delete(entity, lockMode);
	}
	
	public Serializable save(Object entity) {
		
		return getHibernateTemplate().save(entity);
	}
	
	public void saveOrUpdate(Object entity) {
	
		getHibernateTemplate().saveOrUpdate(entity);
	}
	
	public void update(Object entity) {
		
		getHibernateTemplate().update(entity);
	}
	
	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria criteria) {
	
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResult) {
	
		return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResult);
	}
	
	public int executeUpdate(String sql) {
		
		return this.getSession().createSQLQuery(sql).executeUpdate();
	}

	public int executeUpdate(String sql, Map<String, Object> params) {
		
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		
		return query.executeUpdate();
	}
	
	@SuppressWarnings("rawtypes")
	public List list(String sql, Map<String, Object> params) {
	
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		
		return query.list();
	}
	
	public Object uniqueResult(String sql, Map<String, Object> params) {
	
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		
		return query.uniqueResult();
	}
	
}