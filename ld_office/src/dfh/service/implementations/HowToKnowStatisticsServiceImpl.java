package dfh.service.implementations;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dfh.action.vo.HowToKnowStatisticsVO;
import dfh.dao.HowToKnowStatisticsDao;
import dfh.model.Users;
import dfh.service.interfaces.IHowToKnowStatisticsService;

public class HowToKnowStatisticsServiceImpl implements
		IHowToKnowStatisticsService {
	
	private HowToKnowStatisticsDao howToKnowStatisticsDao;

	

	@Override
	public int getTotalRecordCount(Date start, Date end, String url)
			throws Exception {
		// TODO Auto-generated method stub
		
		return howToKnowStatisticsDao.getTotalRecordCount(start, end, url);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HowToKnowStatisticsVO> searchUserRecord(Date start, Date end,String url, int pageno,
			int length) throws Exception {
		// TODO Auto-generated method stub
		int offset=(pageno-1)*length;
		Object objects = howToKnowStatisticsDao.searchUserRecord(start, end, url, offset, length);
		
		List userRrcord=null;
		if (objects!=null) {
			userRrcord=new ArrayList<HowToKnowStatisticsVO>();
			if (objects instanceof Object[]) {
				Object[] arr=(Object[]) objects;
				userRrcord.add(new HowToKnowStatisticsVO(String.valueOf(arr[0]), ((BigInteger)arr[1]).intValue()));
			}else{
				List<Object[]> users=(List) objects;
				for (int i = 0; i < users.size(); i++) {
					Object[] arr = users.get(i);
					if (((BigInteger)arr[1]).intValue()==0) {
						continue;
					}
					userRrcord.add(new HowToKnowStatisticsVO(String.valueOf(arr[0]), ((BigInteger)arr[1]).intValue()));
				}
			}
		}
		return userRrcord;
	}
	
	
	public HowToKnowStatisticsDao getHowToKnowStatisticsDao() {
		return howToKnowStatisticsDao;
	}

	public void setHowToKnowStatisticsDao(
			HowToKnowStatisticsDao howToKnowStatisticsDao) {
		this.howToKnowStatisticsDao = howToKnowStatisticsDao;
	}
	

}
