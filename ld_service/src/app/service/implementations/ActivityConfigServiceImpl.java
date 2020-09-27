package app.service.implementations;

import app.dao.QueryDao;
import app.service.interfaces.IActivityConfigService;
import dfh.model.ActivityConfig;
import dfh.model.enums.VipLevel;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ActivityConfigServiceImpl implements IActivityConfigService {
	private static Logger log = Logger.getLogger(ActivityConfigServiceImpl.class);

	private QueryDao queryDao;
	

	public QueryDao getQueryDao() {
		return queryDao;
	}


	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}


	@Override
	public String queryActivityConfig(Integer level) {
		log.info("-------------queryActivityConfig--------------------");

		String result = null;
		DetachedCriteria dc = DetachedCriteria.forClass(ActivityConfig.class);
		dc.add(Restrictions.eq("level", VipLevel.getText(level)));
		List<ActivityConfig> list = queryDao.findByCriteria(dc);
		if (!list.isEmpty()) {
			boolean flag = true;
			for (ActivityConfig activityConfig : list) {
				if (activityConfig.getTitle().contains("生日礼金")) {
					result = activityConfig.getAmount() + "元";
					flag=false;
					break;
				}

			}
			if(flag){
				result="活动已取消";
			}
		} else {

			result="活动已取消";
		}

		return result;
	}

}
