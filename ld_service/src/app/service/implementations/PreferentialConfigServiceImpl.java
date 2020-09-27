package app.service.implementations;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import app.dao.QueryDao;
import app.model.po.PreferentialConfig;
import app.service.interfaces.IPreferentialConfigService;
import app.util.DateUtil;

public class PreferentialConfigServiceImpl implements IPreferentialConfigService {

	private Logger log = Logger.getLogger(PreferentialConfigServiceImpl.class);
	
	private QueryDao queryDao;
	
	public List<PreferentialConfig> queryPreferentialConfigList(String platformId) {
		
		return this.queryPreferentialConfigList(platformId, null);
	}
	
	public List<PreferentialConfig> queryPreferentialConfigList(String platformId, String titleId) {
		
		return this.queryPreferentialConfigList(platformId, titleId, null);
	}
	
	public List<PreferentialConfig> queryPreferentialConfigList(String platformId, String titleId, String level) {
	
		return this.queryPreferentialConfigList(platformId, titleId, level, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<PreferentialConfig> queryPreferentialConfigList(String platformId, String titleId, String level, String passage) {
		
		log.info("queryPreferentialConfigList方法的参数为：【platformId=" + platformId + ",titleId=" + titleId + ",level=" + level + ",passage=" + passage + "】");
		
		DetachedCriteria dc = DetachedCriteria.forClass(PreferentialConfig.class);
		
		dc.add(Restrictions.eq("isUsed", 1));
		dc.add(Restrictions.eq("deleteFlag", 1));
		dc.add(Restrictions.le("startTime", DateUtil.getCurrentDateFormat()));
		dc.add(Restrictions.ge("endTime", DateUtil.getCurrentDateFormat()));
		
		if (StringUtils.isNotBlank(platformId)) {
			
			dc.add(Restrictions.eq("platformId", platformId));
		}
		
		if (StringUtils.isNotBlank(titleId)) {
			
			dc.add(Restrictions.eq("titleId", titleId));
		}

		if (StringUtils.isNotBlank(level)) {
			
			dc.add(Restrictions.like("vip", level, MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotBlank(passage)) {
		
			List<Integer> list = new ArrayList<Integer>();
			
			for (String str : passage.split(",")) {
			
				list.add(Integer.parseInt(str));
			}
			
			dc.add(Restrictions.in("isPhone", list.toArray()));
		}
		
		dc.addOrder(Order.asc("createTime"));
		
		return queryDao.findByCriteria(dc);
	}
	
	public PreferentialConfig queryPreferentialConfig(Integer id) {
	
		return (PreferentialConfig) queryDao.get(PreferentialConfig.class, id);
	}
	
	public QueryDao getQueryDao() {
		return queryDao;
	}
	
	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}	
}