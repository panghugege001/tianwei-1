package dfh.service.interfaces;


public interface IGameinfoService extends Runnable{
	
	
	public void autoAddXimaProposal()throws Exception;
	
	public void excuteAutoXimaProposal()throws Exception;
	
	public String getMessage();
	
	public void sendMessage()throws Exception;


	void updateXimaStatus() throws Exception;

}
