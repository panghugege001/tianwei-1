package dfh.icbc.quart.fetch;

import dfh.service.interfaces.IGetdateService;

/**
 * 提取额度验证存款定时器服务
 * 2015/03/03
 */
public class FetchDepositJobService {
  
	private IGetdateService getdateService;

	public synchronized  void execute() {
		getdateService.processValiteDeposit();
	}

	public void discardOrder(){
		getdateService.discardOrder();
	}
	
	public IGetdateService getGetdateService() {
		return getdateService;
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
	
}
