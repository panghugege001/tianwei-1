package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

public interface IUserstatusService {
	
	public String getMessage();
	
	public List getUserstatusList(String loginname,String mail,String phone,
			Integer mailflag, Date start, Date end,int pageno,int length) throws Exception;
	
	public Integer getUserstatusCount(String loginname,String mail,String phone,
			Integer mailflag, Date start, Date end) throws Exception;
	
	public String modifyUserMailFlag(String loginname,String mailflag) throws Exception;
	
	public String closeTouzhuFlag(String loginname,String touzhuflag,String remark) throws Exception;

}
