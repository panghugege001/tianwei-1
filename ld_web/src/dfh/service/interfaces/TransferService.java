package dfh.service.interfaces;

public interface TransferService extends UniversalService{

	String transferIn(String transID,String loginname,Double amount);
	
	String transferOut(String transID,String loginname,Double amount);
	
	String transferOutforDsp(String transID,String loginname,Double amount);
	
	String transferInforDsp(String transID,String loginname,Double amount);
	
	String transferOutforkeno(String transID,String loginname,Double amount,String ip);
	
	String transferInforkeno(String transID,String loginname,Double amount,String ip);
	
	String transferOutforBbin(String transID,String loginname,Double amount);
	
	String transferInforBbin(String transID,String loginname,Double amount);
	
	String transferInforBok(String transID,String loginname,Double amount);

	String transferOutforBok(String transID,String loginname, Double amount);
}