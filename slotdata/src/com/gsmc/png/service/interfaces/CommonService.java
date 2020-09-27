package com.gsmc.png.service.interfaces;

import java.util.List;
import java.util.Map;

public interface CommonService extends UniversalService{
	
	
	public int executeSql(String sql, Map<String, Object> params);
	
	public Object getOneValue(String sql, Map<String, Object> params);

	@SuppressWarnings("rawtypes")
	public List excuteQuerySql(String sql, Map<String, Object> params);

}
