package dfh.service.interfaces;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;



public interface SlaveService extends BaseService {
	
	Object get(Class clazz, Serializable id);
	
	List findByCriteria(DetachedCriteria criteria, Integer firstResult, Integer maxResults);
	
	List findByCriteria(DetachedCriteria criteria);
	
	boolean gamecheck(String type, String gameid);
	
	List checkTotalExist(String loginname);

	public List getList(String sql, Map<String, Object> params);

	Double getDeposit(String loginname);

	Double getAllDeposit(String loginname);
	
	List getListBysql(String sql, Map<String, Object> params);
}