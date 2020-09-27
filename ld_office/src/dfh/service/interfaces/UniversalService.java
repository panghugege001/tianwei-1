package dfh.service.interfaces;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.model.Customer;
import dfh.model.Users;

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
	
	public Customer getCustomer();
	List list(String sql, Map<String, Object> params);
	
	public Users getFindUser(String loginname);
	
	public Boolean updateUser(Double limit,String loginname);
	
	<T extends Serializable> void saveAll(Collection<T> entities);
	
	<T extends Serializable> void deleteAll(Collection<T> entities);
	
	public int executeUpdate(String sql, Map<String, Object> params);
}