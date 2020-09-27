package dfh.icbc.quart.fetch;

import dfh.service.interfaces.IGetdateService;

public class FetchSixLotteryJobService {
	private IGetdateService getdateService;
	
	public void execute(){
		ThreadPoolUtil poolUtil = ThreadPoolUtil.getInstance();
		SixLotteryThread Thread = new SixLotteryThread(getdateService);
		poolUtil.add(Thread);
	}

	public IGetdateService getGetdateService() {
		return getdateService;
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
	
}
