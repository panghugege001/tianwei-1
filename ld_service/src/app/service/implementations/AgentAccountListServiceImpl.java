package app.service.implementations;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import dfh.model.AgProfit;
import dfh.model.Creditlogs;
import dfh.model.Payorder;
import dfh.model.PlatformData;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.PayOrderFlagType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.UserRole;
import dfh.utils.Page;
import dfh.utils.PageQuery;
import app.dao.QueryDao;
import app.model.vo.AgentAccountListVO;
import app.service.interfaces.IAgentAccountListService;
import app.util.DateUtil;

public class AgentAccountListServiceImpl implements IAgentAccountListService {

	private static Logger log = Logger.getLogger(AgentAccountListServiceImpl.class);
	
	private QueryDao queryDao;
	
	// 查询额度记录信息
	@SuppressWarnings("unchecked")
	public Page queryCreditLogList(AgentAccountListVO vo) {
		
		String loginName = vo.getLoginName();
		String startTime = vo.getStartTime();
		String endTime = vo.getEndTime();
		Integer pageIndex = vo.getPageIndex();
		Integer pageSize = vo.getPageSize();
		
		if (null == pageIndex) {
			
			pageIndex = 1;
		}
		
		if (null == pageSize) {
		
			pageSize = 10;
		}
		
		log.info("queryCreditLogList方法接收的参数为：【loginName="+ loginName +",startTime=" + startTime + ",endTime=" + endTime + ",pageIndex=" + pageIndex + ",pageSize=" + pageSize + "】");
		
		DetachedCriteria dc = DetachedCriteria.forClass(Creditlogs.class);
		
		try {
			
			dc.add(Restrictions.eq("loginname", loginName));
			dc.add(Restrictions.ge("createtime", DateUtil.getDateFromDateStr(startTime + " 00:00:00")));
			dc.add(Restrictions.lt("createtime", DateUtil.getDateFromDateStr(endTime + " 00:00:00")));
		} catch (ParseException e) {

			log.error("执行queryCreditLogList方法发生异常，异常信息：" + e.getCause().getMessage());
			return null;
		}
		
		Order o = Order.desc("createtime");
		
		Page page = PageQuery.queryForPagenationForApp(queryDao.getHibernateTemplate(), dc, pageIndex, pageSize, o);
		
		if (null != page && !page.getPageContents().isEmpty()) {
		
			List<Creditlogs> list = page.getPageContents();
			
			for (Creditlogs c : list) {
			
				c.setTempCreateTime(DateUtil.getDateFormat(c.getCreatetime()));
				String type = CreditChangeType.getText(c.getType());
				
				if (StringUtils.isNotBlank(type)) {
					
					c.setType(type);
				}
			}
		}
		
		return page;
	}
	
	// 查询下线提案信息
	@SuppressWarnings("unchecked")
	public Page queryUnderLineProposalList(AgentAccountListVO vo) {
	
		String loginName = vo.getLoginName();
		String startTime = vo.getStartTime();
		String endTime = vo.getEndTime();
		String account = vo.getAccount();
		Integer proposalType = vo.getProposalType();
		Integer pageIndex = vo.getPageIndex();
		Integer pageSize = vo.getPageSize();
		
		if (null == pageIndex) {
			
			pageIndex = 1;
		}
		
		if (null == pageSize) {
		
			pageSize = 10;
		}
		
		log.info("queryUnderLineProposalList方法接收的参数为：【loginName="+ loginName +",startTime=" + startTime + ",endTime=" + endTime + ",account=" + account + ",proposalType=" + proposalType + ",pageIndex=" + pageIndex + ",pageSize=" + pageSize + "】");
		
		// 起始时间
		Date start = null;
		// 结束时间
		Date end = null;
		
		try {

			if (StringUtils.isNotBlank(startTime)) {

				start = DateUtil.getDateFromDateStr(startTime + " 00:00:00");
			}

			if (StringUtils.isNotBlank(endTime)) {

				end = DateUtil.getDateFromDateStr(endTime + " 00:00:00");
			}
		} catch (Exception e) {

			log.error("执行queryUnderLineProposalList方法发生异常，异常信息：" + e.getCause().getMessage());
			return null;
		}
		
		// 在线支付
		if (null != proposalType && proposalType == 1000) {
			
			DetachedCriteria uc = DetachedCriteria.forClass(Users.class);
			
			// 查询当前代理下的所有玩家
			uc.setProjection(Property.forName("loginname")).add(Restrictions.eq("agent", loginName));
			
			// 根据当前页面输入的会员账号进行查询
			if (StringUtils.isNotEmpty(account)) {
				
				uc.add(Restrictions.eq("loginname", account));
			}
			
			DetachedCriteria dc = DetachedCriteria.forClass(Payorder.class);
			
			dc.add(Property.forName("loginname").in(uc));
			
			if (start != null) {

				dc.add(Restrictions.ge("createTime", start));
			}

			if (end != null) {

				dc.add(Restrictions.lt("createTime", end));
			}
			
			dc.add(Restrictions.eq("type", PayOrderFlagType.SUCESS.getCode()));
			Order o = Order.desc("createTime");
			
			Page page = PageQuery.queryForPagenationWithStatisticsForApp(queryDao.getHibernateTemplate(), dc, pageIndex, pageSize, "money", null, null, o);
		
			if (null != page && !page.getPageContents().isEmpty()) {
			
				List<Payorder> list = page.getPageContents();
				
				for (Payorder p : list) {
					
					p.setTempCreateTime(DateUtil.getDateFormat(p.getCreateTime()));
				}
			}
			
			return page;
		}
		// 代理信用预支
		else if (null != proposalType && proposalType == 550) {
			
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			
			dc.add(Restrictions.eq("type", proposalType));
			dc.add(Restrictions.eq("loginname", loginName));
			
			if (start != null) {

				dc.add(Restrictions.ge("createTime", start));
			}

			if (end != null) {

				dc.add(Restrictions.lt("createTime", end));
			}
			
			dc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			Order o = Order.desc("createTime");
			
			Page page = PageQuery.queryForPagenationWithStatisticsForApp(queryDao.getHibernateTemplate(), dc, pageIndex, pageSize, "amount", null, null, o);
			
			if (null != page && !page.getPageContents().isEmpty()) {
			
				List<Proposal> list = page.getPageContents();
				
				for (Proposal p : list) {
					
					p.setTempCreateTime(DateUtil.getDateFormat(p.getCreateTime()));
				}
			}
			
			return page;
		}
		else {
			
			DetachedCriteria dc = DetachedCriteria.forClass(Proposal.class);
			
			dc.add(Restrictions.eq("agent", loginName));
			
			if (null != proposalType) {

				dc.add(Restrictions.eq("type", proposalType));
			}
			
			// 根据当前页面输入的会员账号进行查询
			if (StringUtils.isNotEmpty(account)) {
				
				dc.add(Restrictions.eq("loginname", account));
			}
			
			if (start != null) {

				dc.add(Restrictions.ge("createTime", start));
			}

			if (end != null) {

				dc.add(Restrictions.lt("createTime", end));
			}
			
			dc.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			Order o = Order.desc("createTime");
			
			Page page = PageQuery.queryForPagenationWithStatisticsForApp(queryDao.getHibernateTemplate(), dc, pageIndex, pageSize, "amount", "gifTamount", null, o);
			
			if (null != page && !page.getPageContents().isEmpty()) {
			
				List<Proposal> list = page.getPageContents();
				
				for (Proposal p : list) {
					
					p.setTempCreateTime(DateUtil.getDateFormat(p.getCreateTime()));
				}
			}
			
			return page;
		}
	}
	
	// 查询平台输赢信息
	@SuppressWarnings("unchecked")
	public Page queryPlatformLoseWinList(AgentAccountListVO vo) {
		
		String loginName = vo.getLoginName();
		String startTime = vo.getStartTime();
		String endTime = vo.getEndTime();
		String account = vo.getAccount();
		String platform = vo.getPlatform();
		Integer pageIndex = vo.getPageIndex();
		Integer pageSize = vo.getPageSize();
		
		if (null == pageIndex) {
			
			pageIndex = 1;
		}
		
		if (null == pageSize) {
		
			pageSize = 10;
		}
		
		log.info("queryPlatformLoseWinList方法接收的参数为：【loginName="+ loginName +",startTime=" + startTime + ",endTime=" + endTime + ",account=" + account + ",platform=" + platform + ",pageIndex=" + pageIndex + ",pageSize=" + pageSize + "】");
		
		// 起始时间
		Date start = null;
		// 结束时间
		Date end = null;
		
		try {

			if (StringUtils.isNotBlank(startTime)) {

				start = DateUtil.getDateFromDateStr(startTime + " 00:00:00");
			}

			if (StringUtils.isNotBlank(endTime)) {

				end = DateUtil.getDateFromDateStr(endTime + " 00:00:00");
			}
		} catch (Exception e) {

			log.error("执行queryPlatformLoseWinList方法发生异常，异常信息：" + e.getCause().getMessage());
			return null;
		}
		
		
		DetachedCriteria dc = DetachedCriteria.forClass(AgProfit.class);
		
		dc.add(Restrictions.eq("agent", loginName));
		
		if (StringUtils.isNotBlank(account)) {
		
			dc.add(Restrictions.eq("loginname", account));
		}
		
		if (StringUtils.isNotBlank(platform)) {
		
			dc.add(Restrictions.eq("platform", platform));
		}
		
		if (start != null) {
			
			dc.add(Restrictions.ge("createTime", start));
		}
			
		if (end != null) {
			
			dc.add(Restrictions.lt("createTime", end));
		}
		
		Page page = PageQuery.queryForPagenationWithStatisticsForApp(queryDao.getHibernateTemplate(), dc, pageIndex, pageSize, "amount", "bettotal", null, null);
		
		if (null != page) {
		
			if (!page.getPageContents().isEmpty()) {
				
				List<AgProfit> list = page.getPageContents();
				
				for (AgProfit agProfit : list) {
					
					agProfit.setTempCreateTime(DateUtil.getDateFormat(agProfit.getCreateTime()));
				}
			}
			
			if (null == page.getStatics1()) {
				
				page.setStatics1(0.00);
			}
			
			if (null == page.getStatics2()) {
				
				page.setStatics2(0.00);
			}
		}
		
		return page;
	}
		
	// 查询实时输赢信息
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page queryRealTimeLoseWinList(AgentAccountListVO vo) {
		
		String loginName = vo.getLoginName();
		String startTime = vo.getStartTime();
		String endTime = vo.getEndTime();
		String account = vo.getAccount();
		String platform = vo.getPlatform();
		Integer pageIndex = vo.getPageIndex();
		Integer pageSize = vo.getPageSize();
		
		if (null == pageIndex) {
			
			pageIndex = 1;
		}
		
		if (null == pageSize) {
		
			pageSize = 10;
		}
		
		log.info("queryRealTimeLoseWinList方法接收的参数为：【loginName="+ loginName +",startTime=" + startTime + ",endTime=" + endTime + ",account=" + account + ",platform=" + platform + ",pageIndex=" + pageIndex + ",pageSize=" + pageSize + "】");
		
		// 起始时间
		Date start = null;
		
		try {

			if (StringUtils.isNotBlank(startTime)) {

				start = DateUtil.getDateFromDateStr(startTime + " 00:00:00");
			}
		} catch (Exception e) {

			log.error("执行queryRealTimeLoseWinList方法发生异常，异常信息：" + e.getCause().getMessage());
			return null;
		}
		
		DetachedCriteria uc = DetachedCriteria.forClass(Users.class);
		
		uc.add(Restrictions.eq("agent", loginName));
		uc.add(Restrictions.eq("role", "MONEY_CUSTOMER"));
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("loginname"));
		uc.setProjection(pList);
		
		List loginnames = queryDao.findByCriteria(uc);
		
		DetachedCriteria dc = DetachedCriteria.forClass(PlatformData.class);
		
		dc.add(Restrictions.in("loginname", loginnames.toArray()));
		dc.add(Restrictions.eq("starttime", start));
		
		if (StringUtils.isNotBlank(platform)) {
			
			dc.add(Restrictions.eq("platform", platform));
		} else {
			
			dc.add(Restrictions.ne("platform", "pttiger"));
		}
		
		if (StringUtils.isNotBlank(account)) {
			
			dc.add(Restrictions.eq("loginname", account));
		}
		
		Page page = PageQuery.queryForPagenationWithStatisticsForApp(queryDao.getHibernateTemplate(), dc, pageIndex, pageSize, "bet", "profit", null, null);
		
		if (null != page) {
		
			if (null == page.getStatics1()) {
				
				page.setStatics1(0.00);
			}
			
			if (null == page.getStatics2()) {
				
				page.setStatics2(0.00);
			}
			
			List<PlatformData> list = page.getPageContents();
			
			for (PlatformData p : list) {
				
				p.setTempStarttime(DateUtil.getDateFormat(p.getStarttime()));
				p.setTempEndtime(DateUtil.getDateFormat(p.getEndtime()));
				p.setTempUpdatetime(DateUtil.getDateFormat(p.getUpdatetime()));
			}
		}
		
		return page;
	}
	
	// 查询下线会员信息
	@SuppressWarnings("unchecked")
	public Page queryUnderLineMemberList(AgentAccountListVO vo) {
		
		String loginName = vo.getLoginName();
		String startTime = vo.getStartTime();
		String endTime = vo.getEndTime();
		Integer pageIndex = vo.getPageIndex();
		Integer pageSize = vo.getPageSize();
		
		if (null == pageIndex) {
			
			pageIndex = 1;
		}
		
		if (null == pageSize) {
		
			pageSize = 10;
		}
		
		log.info("queryUnderLineMemberList方法接收的参数为：【loginName="+ loginName +",startTime=" + startTime + ",endTime=" + endTime + ",pageIndex=" + pageIndex + ",pageSize=" + pageSize + "】");
		
		// 起始时间
		Date start = null;
		// 结束时间
		Date end = null;
		
		try {

			if (StringUtils.isNotBlank(startTime)) {

				start = DateUtil.getDateFromDateStr(startTime + " 00:00:00");
			}

			if (StringUtils.isNotBlank(endTime)) {

				end = DateUtil.getDateFromDateStr(endTime + " 00:00:00");
			}
		} catch (Exception e) {

			log.error("执行queryUnderLineMemberList方法发生异常，异常信息：" + e.getCause().getMessage());
			return null;
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		
		dc.add(Restrictions.eq("role", UserRole.MONEY_CUSTOMER.getCode()));
		dc.add(Restrictions.eq("agent", loginName));
		dc.add(Restrictions.ge("createtime", start));
		dc.add(Restrictions.le("createtime", end));
		Order o = Order.desc("createtime");
		
		Page page = PageQuery.queryForPagenationForApp(queryDao.getHibernateTemplate(), dc, pageIndex, pageSize, o);
		
		if (null != page && !page.getPageContents().isEmpty()) {
		
			List<Users> list = page.getPageContents();
			
			for (Users user : list) {
				
				user.setTempCreateTime(DateUtil.getDateFormat(user.getCreatetime()));
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