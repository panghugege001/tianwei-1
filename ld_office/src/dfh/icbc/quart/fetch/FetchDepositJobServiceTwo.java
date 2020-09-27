package dfh.icbc.quart.fetch;

import dfh.service.interfaces.IGetdateService;

/**
 * 提取额度验证存款定时器服务
 * 2015/03/03
 */
public class FetchDepositJobServiceTwo {
  
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
	
	//数据量大处理方法
	public void processStatusData() {
		DepositWechatThread depositWechatThread=new DepositWechatThread(getdateService);
		DepositWechatThreadPoolUtil.getInstance().add(depositWechatThread);
		//设置两小时销毁
		discardOrder();
	}
	
	
}
