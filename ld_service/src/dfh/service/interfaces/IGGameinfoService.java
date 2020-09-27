package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.action.vo.AutoXima;
import dfh.action.vo.AutoXimaReturnVo;
import dfh.model.Users;

public interface IGGameinfoService {
	
	
	
	public AutoXima getAutoXimaObject(Date _endTime,Date _startTime,String _loginname);
	public AutoXima getAgAutoXimaObject(Date _endTime,Date _startTime,String _loginname);
	public AutoXima getAginAutoXimaObject(Date _endTime,Date _startTime,String _loginname);
	public AutoXima getBbinAutoXimaObject(Date _endTime,Date _startTime,String _loginname);
	public AutoXima getKgAutoXimaObject(Date _endTime,Date _startTime,String _loginname);
	public AutoXima getKenoAutoXimaObject(Date _endTime,Date _startTime,String _loginname);
	public AutoXima getSbAutoXimaObject(Date _endTime,Date _startTime,String _loginname);
	public AutoXima getPtTigerAutoXimaObject(Date _endTime,Date _startTime,String _loginname);
	public AutoXima getPtOtherAutoXimaObject(Date _endTime,Date _startTime,String _loginname);
	public AutoXima getSixLotteryAutoXimaObject(Date endTime, Date startTime,String loginname);
	public AutoXima getEbetAutoXimaObject(Date endTime, Date startTime, String loginname);
	public AutoXima getGPIAutoXimaObject(Date endTime, Date startTime, String loginname);
	public AutoXima getTTGAutoXimaObject(Date endTime, Date startTime, String loginname);
	public AutoXima getNTAutoXimaObject(Date endTime, Date startTime, String loginname);
	public AutoXima getQTAutoXimaObject(Date endTime, Date startTime, String loginname);
	public AutoXima getMGAutoXimaObject(Date endTime, Date startTime, String loginname);
	public AutoXima getDTAutoXimaObject(Date endTime, Date startTime, String loginname);
	public AutoXima getPNGAutoXimaObject(Date endTime, Date startTime, String loginname);
	public AutoXima getCQ9AutoXimaObject(Date etime, Date stime, String _loginname);

	public AutoXima getSlotAutoXimaObject(String loginname);
	
	public Double getGameValidBetAmount(String loginname, String platform,
			Date startTime, Date endTime);
	
	public AutoXima getAutoXimaPtObject(String _loginname);
	
	public String getMessage();
	
	public String execXima(Users user,Date endTime,Date startTime ,String platform)throws Exception;
	
	public String execXimaPt(Users user,Date endTime,Date startTime)throws Exception;
	
	public String execXimaSlot(String loginname,AutoXima autoXima,Date timeEnd)throws Exception;

	public List<AutoXima> searchXimaDetail(String loginname,Date startTime,Date endTime,int pageno,int length)throws Exception;
	
	public AutoXimaReturnVo checkSubmitXima(String loginname ,String platform);
	
	public AutoXimaReturnVo checkSubmitPtXima(String loginname);
	
	public String getXimaEndTime(String loginname);
	
	public String getXimaEndPtTime(String loginname);
	
	public AutoXima getTotalCount(String loginname,Date startTime,Date endTime)throws Exception;
	
	public String getOtherXimaEndTime(String loginname , String platform) ;
	
	public AutoXima getPGAutoXimaObject(Date etime, Date stime, String loginname);
	
	
	

}
