package dfh.service.interfaces;

import java.util.List;
import java.util.Map;

import dfh.model.UpgradeLog;

/**
 * 自动升降级
 */
public interface IAutoUpgradeService {

	//public void autoUpgrade();
	
	public Integer getCountBySql(String sql, Map<String, Object> params);
	
	@SuppressWarnings("unchecked")
	public List queryList(String sql, Map<String, Object> params);

	public void upgrade(UpgradeLog upgradeLog, Integer warnflag);
	
	public void optUpgrade(UpgradeLog upgradeLog, String targetAction);
}
