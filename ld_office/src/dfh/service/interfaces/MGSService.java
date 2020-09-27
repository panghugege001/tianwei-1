package dfh.service.interfaces;

import java.util.List;
import java.util.Map;

import dfh.dao.MGSDao;

public interface MGSService {

	public MGSDao getMgsDao();
	
	public List<?> getListBySql(String sql, Map<String, Object> params);
	
	public Object getOneObject(String sql, Map<String, Object> params);
	
	public Map<String,Object> queryMgLogStaticsPage(String loginname, String startTime, String endTime,int startIndex,int pageSize,String orderBy,String order);
}
