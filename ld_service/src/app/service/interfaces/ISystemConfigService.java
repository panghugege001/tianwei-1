package app.service.interfaces;

import java.util.List;
import app.model.vo.SystemConfigVO;
import dfh.model.SystemConfig;

public interface ISystemConfigService {

	List<SystemConfig> querySystemConfigList(SystemConfigVO vo);
	
	SystemConfig querySystemConfig(SystemConfigVO vo);
	
}