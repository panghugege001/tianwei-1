package dfh.service.implementations;


import java.util.List;

import dfh.dao.UniversalDao;
import dfh.service.interfaces.IQuerybetrankService;
import dfh.utils.Page;
/**
 * 
 * @author 
 *
 */
public class QueryBetrankServiceImpl extends UniversalServiceImpl implements IQuerybetrankService {
	private UniversalDao universalDao;
	
	
	public UniversalDao getUniversalDao() {
		return universalDao;
	}

	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}

	@Override
	public Page getPage(String sql,int pageIndex,int size,String count) {
		return universalDao.getPage(sql, pageIndex, size, count);
	}

	@Override
	public List getListBysql(String sql) {
		return universalDao.getListBysql(sql);
	}


}
