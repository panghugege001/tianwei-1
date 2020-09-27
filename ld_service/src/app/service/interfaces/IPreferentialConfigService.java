package app.service.interfaces;

import java.util.List;
import app.model.po.PreferentialConfig;

public interface IPreferentialConfigService {

	List<PreferentialConfig> queryPreferentialConfigList(String platformId);
	
	List<PreferentialConfig> queryPreferentialConfigList(String platformId, String titleId);
	
	List<PreferentialConfig> queryPreferentialConfigList(String platformId, String titleId, String level);
	
	List<PreferentialConfig> queryPreferentialConfigList(String platformId, String titleId, String level, String passage);
	
	PreferentialConfig queryPreferentialConfig(Integer id);
	
}