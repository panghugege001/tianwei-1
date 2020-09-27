package dfh.icbc.getdata.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;

public interface BaseInterfaceDao {

	public Object save(Object o);
	
	public void saveOrUpdate(Object o);
	
	public void update(Object o) ;
	
	public List findByCriteria(DetachedCriteria criteria) ;
	
	public Object get(Class clazz, Serializable id);
	
	Object get(Class clazz, Serializable id, LockMode mode);
	
	int discardWxZzMcQuota();

	int discardWxZzMcOrder();
	
	int discardTlyMcOrder();
	
}
