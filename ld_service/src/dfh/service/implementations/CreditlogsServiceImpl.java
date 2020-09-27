package dfh.service.implementations;

import java.util.Date;
import java.util.List;

import dfh.dao.CreditlogsDao;
import dfh.model.Creditlogs;
import dfh.service.interfaces.ICreditlogsService;

public class CreditlogsServiceImpl implements ICreditlogsService {
	
	private CreditlogsDao creditlogsDao;

	public CreditlogsDao getCreditlogsDao() {
		return creditlogsDao;
	}

	public void setCreditlogsDao(CreditlogsDao creditlogsDao) {
		this.creditlogsDao = creditlogsDao;
	}

	@Override
	public List<Creditlogs> searchCreditlogs(Date starttime, Date endtime,
			String loginname, int pageno, int length) {
		// TODO Auto-generated method stub
		int offset=(pageno-1)*length;
		return creditlogsDao.searchCreditlogs(starttime, endtime, loginname, offset, length);
	}

	@Override
	public int totalCreditlogs(Date starttime, Date endtime, String loginname) {
		// TODO Auto-generated method stub
		return creditlogsDao.totalCreditlogs(starttime, endtime, loginname);
	}

}
