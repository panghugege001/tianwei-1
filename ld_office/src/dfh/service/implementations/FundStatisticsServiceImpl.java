package dfh.service.implementations;

import java.util.Date;
import java.util.List;

import dfh.action.vo.FundStatisticsVO;
import dfh.dao.FundStatisticsDao;
import dfh.service.interfaces.IFundStatisticsService;

public class FundStatisticsServiceImpl implements IFundStatisticsService {
	
	private FundStatisticsDao fundStatisticsDao;

	
	
	@Override
	public List<FundStatisticsVO> getFundDetails(Date start, Date end,
			String bankName, int pageno, int length,String searchType) throws Exception {
		// TODO Auto-generated method stub
		int offset=(pageno-1)*length;
		if (searchType.equals("1")) {
			// 存款明细
			if (bankName==null||bankName.trim().equals("")) {
				return fundStatisticsDao.getCashinFundDetails(start, end, offset, length);
			}
			return fundStatisticsDao.getCashinFundDetails(start, end, bankName, offset, length);
		}else{
			// 提款明细
			if (bankName==null||bankName.trim().equals("")) {
				return fundStatisticsDao.getCashoutFundDetails(start, end, offset, length);
			}
			return fundStatisticsDao.getCashoutFundDetails(start, end, bankName, offset, length);
		}
		
	}

	@Override
	public FundStatisticsVO getTotalReccord(Date start, Date end,
			String bankName,String searchType) throws Exception {
		// TODO Auto-generated method stub
		if (searchType.equals("1")) {
			// 存款
			if (bankName==null||bankName.trim().equals("")) {
				return fundStatisticsDao.getCashinTotalReccord(start, end);
			}
			return fundStatisticsDao.getCashinTotalReccord(start, end, bankName);
		}else{
			// 提款
			if (bankName==null||bankName.trim().equals("")) {
				return fundStatisticsDao.getCashoutTotalReccord(start, end);
			}
			return fundStatisticsDao.getCashoutTotalReccord(start, end, bankName);
		}
		
	}
	
	
	
	public FundStatisticsDao getFundStatisticsDao() {
		return fundStatisticsDao;
	}

	public void setFundStatisticsDao(FundStatisticsDao fundStatisticsDao) {
		this.fundStatisticsDao = fundStatisticsDao;
	}


}
