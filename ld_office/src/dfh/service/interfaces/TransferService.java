package dfh.service.interfaces;

public interface TransferService extends UniversalService{
	public void updateSignamount();

	String transferIn(String transID,String loginname,Double amount);
	
	String transferOut(String transID,String loginname,Double amount);
	

}