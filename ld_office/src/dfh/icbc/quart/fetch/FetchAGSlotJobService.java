package dfh.icbc.quart.fetch;
import dfh.service.interfaces.IGetdateService;

public class FetchAGSlotJobService {
	private IGetdateService getdateService;
	
	public void processStatusData() {
		
		AGSlotThread agSlotThread2=new AGSlotThread(getdateService,"XIN");
		AGThreadPoolUtil.getInstance().add(agSlotThread2);
		
		AGSlotThread agSlotThread3=new AGSlotThread(getdateService,"NYX");
		AGThreadPoolUtil.getInstance().add(agSlotThread3);
		
//		AGSlotThread agSlotThread4=new AGSlotThread(getdateService,"BG");
//		AGThreadPoolUtil.getInstance().add(agSlotThread4);
		
		AGSlotThread agSlotThread5=new AGSlotThread(getdateService,"ENDO");
		AGThreadPoolUtil.getInstance().add(agSlotThread5);
	}
	
	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
}
