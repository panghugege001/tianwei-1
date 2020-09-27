package dfh.icbc.quart.fetch;

import dfh.service.interfaces.IGetdateService;

public class FetchQtJobService {
	private IGetdateService getdateService;
	private String fromApi;

	public void execute(){
		/*//ThreadPoolUtil poolUtil = ThreadPoolUtil.getInstance();
		QtThread Thread = new QtThread(getdateService, fromApi);
		Thread.start();
		//poolUtil.add(Thread);*/
		QtThread.execute(getdateService, fromApi);
		}

	public IGetdateService getGetdateService() {
		return getdateService;
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}

	public String getFromApi() {
		return fromApi;
	}

	public void setFromApi(String fromApi) {
		this.fromApi = fromApi;
	}
}
