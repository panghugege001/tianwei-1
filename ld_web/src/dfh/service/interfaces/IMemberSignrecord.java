package dfh.service.interfaces;

public interface IMemberSignrecord {

	public void login(String username)throws Exception;
	
	public void logout(String username)throws Exception;
	
	public boolean isLogined(String username)throws Exception;
	
	

}
