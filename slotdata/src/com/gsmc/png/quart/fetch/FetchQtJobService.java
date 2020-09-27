package com.gsmc.png.quart.fetch;

import com.gsmc.png.quart.thread.QtThread;
import com.gsmc.png.service.interfaces.IGetdateService;

public class FetchQtJobService {
	private IGetdateService getdateService;
	private String fromApi;

	public void execute(){
		/*ThreadPoolUtil poolUtil = ThreadPoolUtil.getInstance();
		QtThread Thread = new QtThread(getdateService, fromApi);
		poolUtil.add(Thread);*/
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
