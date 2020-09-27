package dfh.service.interfaces;


import java.io.Serializable;

import dfh.model.PayMerchant;
import dfh.model.SystemConfig;

public interface ISystemConfigService extends UniversalService{
	String updateSystemConfig(SystemConfig payMer);
	
	String saveSystemConfig(SystemConfig payMer);
	
	void deleteSystemConfig(Class a,String id);
	
	public Object get(Class clazz, Serializable id) ;

}
