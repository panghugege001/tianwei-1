package dfh.service.interfaces;


import java.io.Serializable;

import com.nnti.office.model.auth.Operator;

public interface IOperatorNewService extends UniversalService{
	String updateSystemConfig(Operator operator);
	
	String saveSystemConfig(Operator operator);
	
	void deleteSystemConfig(Class a,String id);
	
	public Object get(Class clazz, Serializable id) ;

}
