package app.service.implementations;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import dfh.model.SystemConfig;
import app.dao.QueryDao;
import app.model.vo.SystemConfigVO;
import app.service.interfaces.ISystemConfigService;

public class SystemConfigServiceImpl implements ISystemConfigService {

	private Logger log = Logger.getLogger(SystemConfigServiceImpl.class);
	
	private QueryDao queryDao;
	
	@SuppressWarnings("unchecked")
	public List<SystemConfig> querySystemConfigList(SystemConfigVO vo) {
		
		String typeNo = vo.getTypeNo();
		String itemNo = vo.getItemNo();
		String flag = vo.getFlag();
		String by = vo.getBy();
		String order = vo.getOrder();
		
		log.info("querySystemConfigList方法的参数值为：【typeNo=" + typeNo + "，itemNo= " + itemNo + "，flag= " + flag + "，by=" + by + "，order=" + order + "】");
		
		DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
		
		if (StringUtils.isNotEmpty(typeNo)) {
		
			dc.add(Restrictions.eq("typeNo", typeNo));
		}
		
		if (StringUtils.isNotEmpty(itemNo)) {
			
			dc.add(Restrictions.eq("itemNo", itemNo));
		}
		
		if (StringUtils.isNotEmpty(flag)) {
			
			dc.add(Restrictions.eq("flag", flag));
		}
		
		if (StringUtils.isNotEmpty(by)) {
			
			Order o = "desc".equals(order) ? Order.desc(by) : Order.asc(by);
			dc.addOrder(o);
		}
		
		return queryDao.findByCriteria(dc);
	}
	
	public SystemConfig querySystemConfig(SystemConfigVO vo) {
	
		List<SystemConfig> list = this.querySystemConfigList(vo);
		
		if (null != list && !list.isEmpty()) {
			
			return list.get(0);
		}
		
		return null;
	}
	
	public QueryDao getQueryDao() {
		return queryDao;
	}
	
	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}
}