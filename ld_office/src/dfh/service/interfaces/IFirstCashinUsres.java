package dfh.service.interfaces;

import java.util.List;

import dfh.action.vo.FirstCashinUsresVoOfTotal;

public interface IFirstCashinUsres {
	
	public List getUserList(String loginname);
	
	public List getUserList(String oneDate,String twoDate,int pageno,int length,String checktype,String checkcontent);
	
	public FirstCashinUsresVoOfTotal getTotalPageNO(String oneDate,String twoDate,String checktype,String checkcontent);
	
	public String getErrorMessage();

}
