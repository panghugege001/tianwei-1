package dfh.service.interfaces;

import dfh.model.Users;

public interface ILogin {
	
	public String getErrorMessage();
	
	public Users login(String loginname,String password,String ip);

}
