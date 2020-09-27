package dfh.service.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IWeekSentService {

	public void excWeekSent(String loginName, Double bettotal, Date startTime, Date endTime);
	
	@SuppressWarnings("unchecked")
	public List getListBySql(String sql, Map<String, Object> params) throws Exception ;
}
