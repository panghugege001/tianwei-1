package app.service.implementations;

import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import dfh.model.LosePromo;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import app.dao.QueryDao;
import app.model.vo.LosePromoVO;
import app.service.interfaces.ILosePromoService;
import app.util.DateUtil;

public class LosePromoServiceImpl implements ILosePromoService {

	private Logger log = Logger.getLogger(LosePromoServiceImpl.class);
	
	private QueryDao queryDao;
	
	@SuppressWarnings("unchecked")
	public Page queryLosePromoPageList(LosePromoVO vo) {
		
		String loginName = vo.getLoginName();
		Integer pageIndex = vo.getPageIndex();
		Integer pageSize = vo.getPageSize();
		
		log.info("queryLosePromoPageList方法的参数为：【loginName=" + loginName + "，pageIndex=" + pageIndex + "，pageSize=" + pageSize + "】");
		
		DetachedCriteria dc = DetachedCriteria.forClass(LosePromo.class);
		
		if (StringUtils.isNotBlank(loginName)) {
			
			dc.add(Restrictions.eq("username", loginName));
		}
		
		dc.add(Restrictions.in("platform", new String[] { "pttiger", "ttg", "gpi", "nt", "qt", "mg", "dt" }));
		
		// 显示一个月以内未领取的救援金记录或者已领取/已取消的救援金记录
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		dc.add(Restrictions.or(Restrictions.ne("status", "0"), Restrictions.and(Restrictions.eq("status", "0"), Restrictions.ge("createTime", calendar.getTime()))));
		
		Order o = Order.desc("createTime");
		
		Page page = PageQuery.queryForPagenation(queryDao.getHibernateTemplate(), dc, pageIndex, pageSize, o);
		List<LosePromo> list = page.getPageContents();
		
		if (null != list && !list.isEmpty()) {
			
			for (LosePromo p : list) {
				
				p.setTempCreateTime(DateUtil.getDateFormat(p.getCreateTime()));
			}
		}
		
		return page;
	}
	
	public QueryDao getQueryDao() {
		return queryDao;
	}
	
	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}	
}