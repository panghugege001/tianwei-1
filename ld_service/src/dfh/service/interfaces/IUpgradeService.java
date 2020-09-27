package dfh.service.interfaces;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import dfh.model.UpgradeLog;
import dfh.model.Users;
import dfh.model.bean.Bet;

/**
 * 自动升降级
 */
public interface IUpgradeService {

	public Double upgrade(UpgradeLog upgradeLog, Users user);
	
	public Object get(Class clazz, Serializable id);
	
	public Double getMaxDepoist(String username, String startTime, String endTime);
	
	public void save(Object obj);
	
	public Integer isUpgradeClosed();
	
	public Double getDepositAmount(String username, String startTime, String endTime);
	
	public LinkedList<Bet> getBetsAllPlatform(String sql, Map<String, Object> params);
	
	public Map<String, Double> getTotalBetByUser(String username, String start, String end);

	public List findByCriteria(DetachedCriteria criteria);
}
