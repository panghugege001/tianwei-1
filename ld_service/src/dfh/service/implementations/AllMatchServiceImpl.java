package dfh.service.implementations;

import dfh.dao.AllMatchDao;
import dfh.dao.UniversalDao;
import dfh.service.interfaces.AllMatchService;
import dfh.utils.Page;

public class AllMatchServiceImpl extends UniversalServiceImpl implements AllMatchService {
	
	private AllMatchDao allMatchDao;
	
	public void setUniversalDao(UniversalDao universalDao) {
		super.setUniversalDao(universalDao);
	};
	
	@Override
	public Page getSlotsMatchPage(String sql, int pageIndex, int size,
			String count) {
		return allMatchDao.getSlotsMatchPage(sql, pageIndex, size, count);
	}

	public void setAllMatchDao(AllMatchDao allMatchDao) {
		this.allMatchDao = allMatchDao;
	}
	
	@Override
	public Page getConcertPage(String sql, int pageIndex, int size, String count)
			throws Exception {
		// TODO Auto-generated method stub
		return allMatchDao.getConcertPage(sql, pageIndex, size, count);
	}
	
	
}
