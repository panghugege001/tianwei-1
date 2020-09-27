package dfh.icbc.quart.fetch;
import dfh.service.interfaces.IGetdateService;

public class FetchAlipayJobService {
	private IGetdateService getdateService;
	
	public void processStatusData() {
		AlipayThread alipayThread=new AlipayThread(getdateService);
		AlipayThreadPoolUtil.getInstance().add(alipayThread);
	}
	
	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
}
