package dfh.service.interfaces;

import dfh.model.Users;

public interface ILogin {
	
	public String getErrorMessage();
	
	public Users login(Users olduser,String loginname,String password,String ip,String city,String regcity,String clientos,String ioBB);
	
	public Users nTwoTicketlogin(Users olduser,String loginname,String ip,String city,String regcity,String clientos);

	public Users loginByEncryptPassword(Users customer,String loginname,String password,String ip,String city,String regcity,String clientos,String ioBB);

}
