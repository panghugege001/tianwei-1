package app.service.implementations;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import app.dao.QueryDao;
import app.model.po.LatestPreferential;
import app.model.vo.LatestPreferentialVO;
import app.service.interfaces.ILatestPreferentialService;
import app.util.FTPUtil;

public class LatestPreferentialServiceImpl implements ILatestPreferentialService {

	private Logger log = Logger.getLogger(LatestPreferentialServiceImpl.class);
	
	private QueryDao queryDao;
	
	@SuppressWarnings("unchecked")
	public List<LatestPreferential> queryLatestPreferentialList(LatestPreferentialVO latestPreferentialVO) {
		
		String type = latestPreferentialVO.getType();
		String isNew = latestPreferentialVO.getIsNew();
		String isActive = latestPreferentialVO.getIsActive();
		String isPhone = latestPreferentialVO.getIsPhone();
		Integer pageIndex = latestPreferentialVO.getPageIndex();
		Integer pageSize = latestPreferentialVO.getPageSize();
		
		log.info("queryLatestPreferentialList方法的参数值为：【type=" + type + "，isNew=" + isNew + "，isActive=" + isActive + "，isPhone=" + isPhone + "，pageIndex=" + pageIndex + "，pageSize=" + pageSize + "】");
		
		List<LatestPreferential> list = null;
		
		DetachedCriteria dc = DetachedCriteria.forClass(LatestPreferential.class);
		
		
		dc.add(Restrictions.eq("isQyStyle","0"));//排除U乐风采（已经过时的活动）
		
		if (StringUtils.isNotEmpty(type)) {
		
			dc.add(Restrictions.eq("type", type));
		}
		
		if (StringUtils.isNotEmpty(isNew)) {
			
			dc.add(Restrictions.eq("isNew", isNew));
		}
		
		if (StringUtils.isNotEmpty(isActive)) {
			
			dc.add(Restrictions.eq("isActive", isActive));
		}
		
		if (StringUtils.isNotEmpty(isPhone)) {
			
			dc.add(Restrictions.eq("isPhone", isPhone));
		}
		
		if (null == pageIndex) {
		
			pageIndex = 1;
		}
		
		if (null == pageSize) {
			
			pageSize = 4;
		}
		
		// 产品Molly提出最新优惠不需要根据活动有效时间来查询(modify time:2017-01-19)
//		dc.add(Restrictions.le("activityStartTime", DateUtil.getCurrentDateFormat()));
//		dc.add(Restrictions.ge("activityEndTime", DateUtil.getCurrentDateFormat()));
		dc.addOrder(Order.desc("createTime"));
		
		list = queryDao.findByCriteria(dc, (pageIndex - 1) * pageSize, pageSize);
		
		if (null != list && !list.isEmpty()) {
			
			for (LatestPreferential temp : list) {
				
				setImageUrl(temp);
			}
		}
		
		return list;
	}

	public LatestPreferential queryLatestPreferential(LatestPreferentialVO latestPreferentialVO) {
		
		Integer id = latestPreferentialVO.getId();
		
		log.info("queryLatestPreferential方法的参数值为：【id=" + id + "】");
		
		if (null == id) {
			
			return null;
		}
		
		Object obj = queryDao.get(LatestPreferential.class, id);
		
		if (null == obj) {
			
			return null;			
		}
		
		LatestPreferential temp = (LatestPreferential) obj;
		
		setImageUrl(temp);
		
		return temp;
	}
	
	private void setImageUrl(LatestPreferential temp) {
	
		if (StringUtils.isNotEmpty(temp.getActivityImageUrl())) {
			
			String imageName = temp.getActivityImageUrl().substring(temp.getActivityImageUrl().lastIndexOf("/") + 1);
			
			temp.setActivityImageUrl(FTPUtil.getValue("preferential_image_download_url") + imageName);
		}
		
		if (StringUtils.isNotEmpty(temp.getNewImageUrl())) {
			
			String imageName = temp.getNewImageUrl().substring(temp.getNewImageUrl().lastIndexOf("/") + 1);
			
			temp.setNewImageUrl(FTPUtil.getValue("preferential_image_download_url") + imageName);
		}
	}
	
	public QueryDao getQueryDao() {
		return queryDao;
	}
	
	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}
}