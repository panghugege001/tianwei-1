package dfh.icbc.quart.fetch;
import dfh.service.interfaces.IGetdateService;

public class FetchAbcJobService {
	private IGetdateService getdateService;
	public synchronized  void processStatusData() {
		getdateService.updateoperatorNew("ABC");
	}
	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
}
