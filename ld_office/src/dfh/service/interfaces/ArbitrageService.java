package dfh.service.interfaces;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("rawtypes") 
public interface ArbitrageService {

	Object save(Object o);
	
	void saveOrUpdate(Object o);
	
	void delete(Class clazz, Serializable id);
	
	Object get(Class clazz, Serializable id);
	
	List findByCriteria(DetachedCriteria criteria);

	List findByCriteria(DetachedCriteria criteria, Integer firstResult, Integer maxResults);
	
	HibernateTemplate getHibernateTemplate();

	
}