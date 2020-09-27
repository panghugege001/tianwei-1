package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.model.Creditlogs;

public interface ICreditlogsService {
	
	public List<Creditlogs> searchCreditlogs(Date starttime,Date endtime,String loginname,int pageno,int length);
	
	public int totalCreditlogs(Date starttime,Date endtime,String loginname);

}
