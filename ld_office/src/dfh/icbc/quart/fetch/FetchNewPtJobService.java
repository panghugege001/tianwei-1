package dfh.icbc.quart.fetch;

import dfh.service.interfaces.IGetdateService;
public class FetchNewPtJobService {

	private IGetdateService getdateService;

	public synchronized void processStatusData() {
		/*ThreadPoolUtil poolUtil = ThreadPoolUtil.getInstance();
		PtThread skyThread = new PtThread(getdateService);
		poolUtil.add(skyThread);*/
		PtUpdate.execute(getdateService);
	}

	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
	
	/**
	 * 
	* @methods dealNewPtdata 
	* @description <p>区分both和tiger类型的游戏投注额度（这里导入的是both）</p> 
	* @author erick
	* @date 2014年11月14日 上午10:43:39 参数说明
	* @return void 返回结果的说明
	 */
	public synchronized void dealNewPtdata(){
		ThreadPoolUtil poolUtil = ThreadPoolUtil.getInstance();
		PtNewXXThread thread = new PtNewXXThread(getdateService);
		poolUtil.add(thread);
	}

}
