package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.action.vo.HowToKnowStatisticsVO;

public interface IHowToKnowStatisticsService {
	
	public List<HowToKnowStatisticsVO> searchUserRecord(Date start,Date end,String url,int pageno,int length)throws Exception;
	
	public int getTotalRecordCount(Date start,Date end,String url)throws Exception;

}
