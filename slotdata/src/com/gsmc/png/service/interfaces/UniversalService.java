package com.gsmc.png.service.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;


public interface UniversalService {

	void delete(Class clazz, Serializable id);

	List findByCriteria(DetachedCriteria criteria);

	List findByCriteria(DetachedCriteria criteria, Integer firstResult, Integer maxResults);

	List findByNamedQuery(String sql, Map params);

	Object get(Class clazz, Serializable id);

	Object get(Class clazz, Serializable id, LockMode mode);

	HibernateTemplate getHibernateTemplate();

	List loadAll(Class clazz);

	Object save(Object o);

	void saveOrUpdate(Object o);

	void update(Object o);
	
	void saveOrUpdateAll(List list);

}