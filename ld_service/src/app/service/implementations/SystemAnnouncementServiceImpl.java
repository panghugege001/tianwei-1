package app.service.implementations;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import dfh.model.Announcement;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import app.dao.BaseDao;
import app.model.vo.AnnouncementForAppVO;
import app.model.vo.OnlinePayRecordVO;
import app.service.interfaces.ISystemAnnouncementService;
import app.util.DateUtil;

public class SystemAnnouncementServiceImpl implements ISystemAnnouncementService {

	private BaseDao baseDao;
	
	@SuppressWarnings("unchecked")
	public Page queryIndexTopList(OnlinePayRecordVO vo) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Announcement.class);
		dc.add(Restrictions.eq("type", "INDEX_TOP"));
		Order o = Order.desc("createtime");
		Page page = PageQuery.queryForPagenation(baseDao.getHibernateTemplate(), dc, vo.getPageIndex(), vo.getPageSize(), o);
		
		List<Announcement> list = page.getPageContents();
		List<AnnouncementForAppVO> responseList = new ArrayList<AnnouncementForAppVO>();
		if (null != list && !list.isEmpty()) {
			
			for (Announcement obj : list) {
				
				AnnouncementForAppVO tempVo = new AnnouncementForAppVO();
				tempVo.setId(obj.getId());
				tempVo.setType(obj.getType());
				tempVo.setContent(obj.getContent());
				tempVo.setCreatetime(DateUtil.getDateFormat(obj.getCreatetime()));
				tempVo.setTitle(obj.getTitle());
				
				responseList.add(tempVo);
			}
		}
		page.setPageContents(responseList);
		return page;
	}
	
	public BaseDao getBaseDao() {
		return baseDao;
	}
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
}