package dfh.service.interfaces;

public interface IAutoDeposit {
	
	public String Login(String requestXML,String requestIP);
	
	public String CallBack(String responseXML,String requestIP);
	
	public String validationAmount(String responseXML,String requestIP);

}
