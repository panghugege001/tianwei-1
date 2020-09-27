package dfh.service.interfaces;

import java.util.Date;

public interface IchangeLineUserService{
	
	public void changeLineUser() throws Exception;
	
	public double[] getDepositAndWinorlose(String loginname,Date createtime,Date endTimeDeposit) ;
}
