package app.service.implementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import dfh.model.Users;
import dfh.model.Userstatus;
import app.dao.BaseDao;
import app.dao.QueryDao;
import app.model.po.PreferentialComment;
import app.model.vo.PreferentialCommentVO;
import app.service.interfaces.IPreferentialCommentService;
import app.util.DateUtil;

public class PreferentialCommentServiceImpl implements IPreferentialCommentService {

	private Logger log = Logger.getLogger(PreferentialCommentServiceImpl.class);
	
	private BaseDao baseDao;
	private QueryDao queryDao;
	
	@SuppressWarnings("unchecked")
	public List<PreferentialComment> queryPreferentialCommentList(PreferentialCommentVO vo) {
		
		Integer pId = vo.getpId();
		Integer pageIndex = vo.getPageIndex();
		Integer pageSize = vo.getPageSize();
		
		log.info("queryPreferentialCommentList方法的参数值为：【pId=" + pId + "，pageIndex=" + pageIndex + "，pageSize=" + pageSize + "】");
		
		DetachedCriteria dc = DetachedCriteria.forClass(PreferentialComment.class);
		
		if (null != pId) {
		
			dc.add(Restrictions.eq("pId", pId));
		}
		
		if (null == pageIndex) {
			
			pageIndex = 1;
		}
		
		if (null == pageSize) {
			
			pageSize = 5;
		}
		
		dc.addOrder(Order.desc("replyTime"));
		
		List<PreferentialComment> list = queryDao.findByCriteria(dc, (pageIndex - 1) * pageSize, pageSize);
		
		if (null != list && !list.isEmpty()) {
			
			for (PreferentialComment temp : list) {
				
				try {
					
					temp.setReplyTime(DateUtil.getDateFormat(DateUtil.getDateFromDateStr(temp.getReplyTime())));
				} catch (Exception e) {
					
				}
			}
		}
		
		return list;
	}
	
	public String savePreferentialComment(PreferentialComment preferentialComment) {
		
		String message = "";
		
		try {
			
			Userstatus userstatus = (Userstatus) baseDao.get(Userstatus.class, preferentialComment.getLoginName());
			
			if ("0".equals(String.valueOf(userstatus.getDiscussflag()))) {
				
				message = "没有评论权限，请联系客服开启评论权限后重试！";
			} else {
				
				Users users = (Users) baseDao.get(Users.class, preferentialComment.getLoginName());
				
				if (null != users) {
					
					preferentialComment.setNickName(users.getAliasName());
				}
				
				preferentialComment.setReplyTime(DateUtil.getCurrentDateFormat());
				
				baseDao.save(preferentialComment);
			}
		} catch (Exception e) {
		
			log.error("savePreferentialComment方法保存评论发生异常，异常信息："  + e.getMessage());
			message = "评论失败，请稍后重试！";
		}
		
		return message;
	}
	
	public Integer countPreferentialComment(PreferentialCommentVO vo) {
	
		Integer pId = vo.getpId();
		
		log.info("countPreferentialComment方法的参数值为：【pId=" + pId + "】");
		
		String sql = "select count(1) from preferential_comment t where t.p_id = :pId";
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("pId", pId);
		
		Object obj = queryDao.uniqueResult(sql, paramsMap);
		
		Integer commentNumber = 0;
		
		if (null != obj) {
		
			commentNumber = ((Number) obj).intValue();
		}
		
		return commentNumber;
	}
	
	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
	public QueryDao getQueryDao() {
		return queryDao;
	}
	
	public void setQueryDao(QueryDao queryDao) {
		this.queryDao = queryDao;
	}
}