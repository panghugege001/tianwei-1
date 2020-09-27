// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UniversalServiceImpl.java

package dfh.service.implementations;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.dao.UniversalDao;
import dfh.service.interfaces.UniversalService;

public class UniversalServiceImpl implements UniversalService {

	private UniversalDao universalDao;

	public UniversalServiceImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#delete(java.lang.Class,
	 * java.io.Serializable)
	 */
	public void delete(Class clazz, Serializable id) {
		universalDao.delete(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dfh.service.implementations.UniversalService#findByCriteria(org.hibernate
	 * .criterion.DetachedCriteria)
	 */
	public List findByCriteria(DetachedCriteria criteria) {
		return universalDao.getHibernateTemplate().findByCriteria(criteria);
	}
	
	public List findByCriteria(DetachedCriteria criteria,Integer  firstResult,Integer maxResults) {
		return universalDao.getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dfh.service.implementations.UniversalService#findByNamedQuery(java.lang
	 * .String, java.util.Map)
	 */
	public List findByNamedQuery(String sql, Map params) {
		return universalDao.findByNamedQuery(sql, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#get(java.lang.Class,
	 * java.io.Serializable)
	 */
	public Object get(Class clazz, Serializable id) {
		return universalDao.get(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#get(java.lang.Class,
	 * java.io.Serializable, org.hibernate.LockMode)
	 */
	public Object get(Class clazz, Serializable id, LockMode mode) {
		return universalDao.get(clazz, id, mode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#getHibernateTemplate()
	 */
	public HibernateTemplate getHibernateTemplate() {
		return universalDao.getHibernateTemplate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dfh.service.implementations.UniversalService#loadAll(java.lang.Class)
	 */
	public List loadAll(Class clazz) {
		return universalDao.loadAll(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#save(java.lang.Object)
	 */
	public Object save(Object o) {
		return universalDao.save(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dfh.service.implementations.UniversalService#saveOrUpdate(java.lang.Object
	 * )
	 */
	public void saveOrUpdate(Object o) {
		universalDao.saveOrUpdate(o);
	}

	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dfh.service.implementations.UniversalService#update(java.lang.Object)
	 */
	public void update(Object o) {
		universalDao.update(o);
	}
}
