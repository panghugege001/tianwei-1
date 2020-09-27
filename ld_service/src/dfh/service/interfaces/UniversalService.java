package dfh.service.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import dfh.model.SignRecord;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.model.Guestbook;
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
	
	Integer getGuestbookCount(String loginname);
	
	Integer getGuestbookCountAll(String loginname);
	
	List<Guestbook> getGuestbookList(String loginname,Integer page,Integer count,Integer userstatus,Integer referenceId);
	
	public Integer getGuestbookListCount(String loginname,Integer userstatus);
	
	public Users getUsers(String loginname);
	
	/**
	 * 根据传入的sql 返回list
	 * @return
	 */
	public List getList(String sql,Class a);

	List list(String sql, Map<String, Object> params);

	public List<SignRecord> findSignrecord(String loginName);
}