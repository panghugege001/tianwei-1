package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.action.vo.AutoXima;
import dfh.model.Users;

public interface IGGameinfoService {
	
	
	
	public AutoXima getAutoXimaObject(Date _endTime,Date _startTime,String _loginname);
	
	public String getMessage();
	
	public boolean execXima(Users user,AutoXima xima)throws Exception;
	
	public List searchXimaDetail(String loginname,Date startTime,Date endTime,int pageno,int length)throws Exception;
	
	public boolean checkSubmitXima(String loginname);
	
	public String getXimaEndTime(String loginname);
	
	public AutoXima getTotalCount(String loginname,Date startTime,Date endTime)throws Exception;
	
	

}
