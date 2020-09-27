package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.action.vo.FundStatisticsVO;

public interface IFundStatisticsService {
	
	public List<FundStatisticsVO> getFundDetails(Date start,Date end,String bankName,int pageno, int length,String searchType)throws Exception;
	
	public FundStatisticsVO getTotalReccord(Date start,Date end,String bankName,String searchType)throws Exception;

}
