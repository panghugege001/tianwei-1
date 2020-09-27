package dfh.icbc.quart.fetch;
import dfh.service.interfaces.IGetdateService;

public class FetchIcbcJobService {
	private IGetdateService getdateService;
	public synchronized  void processStatusData() {
		getdateService.updateoperatorNew("ICBC");
	}
	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
}
