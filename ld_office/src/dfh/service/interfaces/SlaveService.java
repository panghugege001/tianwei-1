package dfh.service.interfaces;

import java.io.Serializable;

import org.springframework.orm.hibernate3.HibernateTemplate;



public interface SlaveService {
public HibernateTemplate getHibernateTemplate();
	
	Object get(Class clazz, Serializable id);

	int getCountProxyFirst(String loginname, String countProxyFirst_start,
			String countProxyFirst_end);
	
}