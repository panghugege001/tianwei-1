package dfh.icbc.quart.fetch;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.model.Users;
import dfh.service.interfaces.IGetdateService;

public class FetchPtJobService {
	private IGetdateService getdateService;
	private Logger log = Logger.getLogger(FetchPtJobService.class);
	public synchronized void processStatusData() {
		/**
		 * 调用Pt获取有投注信息
		 */
		ThreadPoolUtil poolUtil = ThreadPoolUtil.getInstance();
		//if (poolUtil.getThreadPoolExecutor().getActiveCount() == 0) {
			/*DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc.add(Restrictions.eq("ptflag", 1));
			List<Users> list = getdateService.findByCriteria(dc);
			if (list != null && list.size() > 0) {
				for (Users ptUser : list) {
					SkyThread skyThread = new SkyThread(getdateService, ptUser);
					poolUtil.add(skyThread);
				}
			}*/
		SkyThread skyThread = new SkyThread(getdateService);
		skyThread.update();
		//poolUtil.add(skyThread);
		//}
	}
	public void setGetdateService(IGetdateService getdateService) {
		this.getdateService = getdateService;
	}
}
