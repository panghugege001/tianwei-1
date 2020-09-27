package dfh.service.interfaces;

import java.util.List;

import dfh.action.vo.OperatorVO;


public interface IEmployeeManager {
	
	public boolean addEmployee(String e_name,String pwd,String manager);
	
	public boolean addEmployee(String e_name,String pwd,String manager,String role);
	
	public boolean delEmployee(String e_name,String manager);
	
//	public boolean changeEmployeeStatus(String e_name,String status,String manager);
	
	public boolean changeEmployeeStatus(String e_name ,String manager);
	
	public List findAllEmployye(String manager,int pageno,int length);
	
	public OperatorVO findById(String e_name,String manager);
	
	public int getPageCount(String manager);
	
	public String getErrorMessage();

}
