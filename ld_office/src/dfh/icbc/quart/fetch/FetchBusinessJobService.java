package dfh.icbc.quart.fetch;

import dfh.service.interfaces.IGetdateService;

public class FetchBusinessJobService {

	private IGetdateService getdateService;

	public synchronized void execute() {
		
		//
		getdateService.detailData();
		
		//
		getdateService.summaryData();
	}
	
	public IGetdateService getGetdateService() {
		return getdateService;
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
}