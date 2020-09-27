package dfh.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import java.io.Serializable;
import java.util.List;

public class ArbitrageDao extends ArbitrageHibernateDaoSupport {

	public ArbitrageDao() {
	}

	@SuppressWarnings("rawtypes")
	public void delete(Class clazz, Serializable id) {
		getHibernateTemplate().delete(get(clazz, id));
	}

	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria criteria) {
		return getHibernateTemplate().findByCriteria(criteria);
	}

	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria criteria, int firstResult,
                               int maxResult) {
		return getHibernateTemplate().findByCriteria(criteria, firstResult,
				maxResult);
	}

	@SuppressWarnings("rawtypes")
	public Object get(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	public Object save(Object o) {
		return getHibernateTemplate().save(o);
	}

	@SuppressWarnings("rawtypes")
	public void saveOrUpdateAll(List list) throws DataAccessException {
		getHibernateTemplate().saveOrUpdateAll(list);
	}

	public void saveOrUpdate(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

}
