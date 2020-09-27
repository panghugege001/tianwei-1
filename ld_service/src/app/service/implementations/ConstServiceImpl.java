package app.service.implementations;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import dfh.model.Const;
import app.dao.QueryDao;
import app.service.interfaces.IConstService;

public class ConstServiceImpl implements IConstService {

	private QueryDao queryDao;
	
	@SuppressWarnings("unchecked")
	public List<Const> queryConstList() {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
		
		return queryDao.findByCriteria(dc);
	}
	
	public String queryConstValue(String id) {
	
		String value = "1";
		
		List<Const> list = this.queryConstList();
		
		if (null != list && !list.isEmpty()) {
			
			for (Const c : list) {
				
				if (c.getId().equalsIgnoreCase(id)) {
					
					value = c.getValue();
					break;
				}
			}
		}
			
		return value;
	}
	
	public QueryDao getQueryDao() {
		return queryDao;
	}
	
	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}
}