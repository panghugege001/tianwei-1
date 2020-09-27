package app.service.implementations;

import dfh.model.Userstatus;
import app.dao.QueryDao;
import app.service.interfaces.IUserStatusService;

public class UserStatusServiceImpl implements IUserStatusService {

	private QueryDao queryDao;
	
	public Userstatus queryUserStatus(String loginName) {
		
		return (Userstatus) queryDao.get(Userstatus.class, loginName);
	}
	
	public QueryDao getQueryDao() {
		return queryDao;
	}
	
	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}
}