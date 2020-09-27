package app.service.implementations;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import app.model.po.PreferentialConfig;
import app.util.DateUtil;
import dfh.model.PlatformData;
import dfh.model.PreferentialRecord;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.model.enums.ProposalFlagType;

public class BaseService {

	private static Logger log = Logger.getLogger(BaseService.class);

	/**
	 * @description：获取指定时间段内玩家的存款额
	 * @param：loginName(玩家账号)
	 * @param：startTime(开始时间)
	 * @param：endTime(结束时间) 
	 * return：Double
	 */
	public Double getDeposit(HibernateTemplate hibernateTemplate, String loginName, String startTime, String endTime) {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT sum(a.money) as money FROM");
		sql.append(" (");
		sql.append(" SELECT sum(t.money) as money FROM payorder t where t.type = 0 and t.flag = 0 and t.loginname = :loginname and (t.createTime BETWEEN :starttime and :endtime)");
		sql.append(" union all");
		sql.append(" SELECT sum(t.amount) as money FROM proposal t where t.type = 502 and t.flag = 2 and t.loginname = :loginname and (t.createTime BETWEEN :starttime and :endtime)");
		sql.append(" ) a");

		log.info("getDeposit=" + sql.toString());

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("loginname", loginName);
		params.put("starttime", startTime);
		params.put("endtime", endTime);

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql.toString());
		query.setProperties(params);

		Object data = query.uniqueResult();

		return Double.parseDouble((null == data) ? "0" : data.toString());
	}

	/**
	 * @description：获取指定时间段内玩家的输赢值
	 * @param：loginName(玩家账号)
	 * @param：startTime(开始时间)
	 * @param：endTime(结束时间) 
	 * return：Double
	 */
	public Double getBet(HibernateTemplate hibernateTemplate, String loginName, String startTime, String endTime) {

		StringBuilder sql = new StringBuilder();

		sql.append("select sum(profitall) profitall from");
		sql.append(" (");
		sql.append(" select 0 - sum(money) as profitall from payorder t where type = 0 and flag = 0 and loginname = :loginname and (t.createTime BETWEEN :starttime and :endtime)");
		sql.append(" union all");
		sql.append(" select 0 - sum(amount) as profitall from proposal t where type = 502 and flag = 2 and loginname = :loginname and (t.createTime BETWEEN :starttime and :endtime)");
		sql.append(" union all");
		sql.append(" select sum(amount) as profitall from proposal t where type = 503 and flag = 2 and loginname = :loginname and (t.createTime BETWEEN :starttime and :endtime)");
		sql.append(" ) t");

		log.info("getBet=" + sql.toString());

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("loginname", loginName);
		params.put("starttime", startTime);
		params.put("endtime", endTime);

		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql.toString());
		query.setProperties(params);

		Object data = query.uniqueResult();

		return Double.parseDouble((null == data) ? "0" : data.toString());
	}

	/**
	 * @description：获取玩家上一次领取优惠后的有效投注额
	 * @param：loginName(玩家账号)
	 * @param：platform(游戏平台)
	 * @param：pno(提案号)
	 * return：Double
	 */
	@SuppressWarnings("rawtypes")
	public Double getPreferentialBet(HibernateTemplate hibernateTemplate, String loginName, String platform, String pno) {

		DetachedCriteria dc = DetachedCriteria.forClass(PlatformData.class);
		dc.add(Restrictions.eq("loginname", loginName));
		dc.add(Restrictions.eq("platform", platform));
		dc.setProjection(Projections.sum("bet"));

		List pfList = hibernateTemplate.findByCriteria(dc);

		if (null == pfList || pfList.isEmpty()) {

			return null;
		}

		Double betAmount = (Double) pfList.get(0);

		if (null == betAmount) {

			return null;
		}

		DetachedCriteria rc = DetachedCriteria.forClass(PreferentialRecord.class);
		rc.add(Restrictions.eq("pno", pno));

		List rfList = hibernateTemplate.findByCriteria(rc);

		if (null == rfList || rfList.isEmpty()) {

			return null;
		}

		PreferentialRecord record = (PreferentialRecord) rfList.get(0);
		Double validBet = null == record.getValidBet() ? 0.00 : record.getValidBet();

		// 后台人工控制开关
		if (record.getType() == 1) {

			return -1.0;
		}

		return betAmount - validBet;
	}

	/**
	 * @description：获取玩家当前申请的自助优惠次数
	 * @param：loginName(玩家账号)
	 * @param：platform(游戏平台编号)
	 * @param：type(存送优惠类型编号)
	 * @param：preferentialId(自助优惠编号)
	 * @param：timesFlag(次数类别)
	 * return：Integer
	 */
	public Integer useCount(HibernateTemplate hibernateTemplate, String loginName, Integer preferentialId, String platform, Integer type, Integer timesFlag) {

		StringBuilder sql = new StringBuilder();

		sql.append("select count(1) from proposal t1, proposal_extend t2 where t1.pno = t2.pno and t1.flag = 2");
		sql.append(" and t2.platform = :platform");
		sql.append(" and t1.type = :type");
		sql.append(" and t2.preferential_id = :preferentialid");
		sql.append(" and t1.loginname = :loginname");
		sql.append(" and t1.executeTime >= :executetime");

		log.info("useCount=" + sql.toString());

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("platform", platform);
		params.put("type", type);
		params.put("preferentialid", preferentialId);
		params.put("loginname", loginName);
		
		Date executetime = new Date();
		
		// 日
		if (timesFlag == 1) {
			
			executetime = DateUtil.getTodayByZeroHour();
		}
		// 周
		else if (timesFlag == 2) {
			
			executetime = DateUtil.getWeekByFirstDay();
		}
		// 月
		else if (timesFlag == 3) {
			
			executetime = DateUtil.getMonthByFirstDay();
		}
		// 年
		else if (timesFlag == 4) {

			executetime = DateUtil.getYearByFirstDay();
		}
		
		params.put("executetime", executetime);
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql.toString());
		query.setProperties(params);

		Object data = query.uniqueResult();

		return Integer.valueOf((null == data) ? "0" : data.toString());
	}
	
	/**
	 * @description：自助优惠配置公共逻辑处理
	 * @param：id(优惠ID)
	 * @param：platform(优惠平台ID)
	 * @param：youhuiType(优惠类型ID)
	 * @param：loginName(玩家账号)
	 * @param：level(玩家等级)
	 * return：String
	 */
	public Map<String, Object> preferentialLogicHandle(HibernateTemplate hibernateTemplate, Integer id, String platform, Integer youhuiType, String loginName, Integer level) {
	
		Map<String, Object> returnMap = new HashMap<String, Object>();
		
		PreferentialConfig config = (PreferentialConfig) hibernateTemplate.get(PreferentialConfig.class, id);
		
		if (null == config || config.getIsUsed() == 0) {
		
			returnMap.put("message", "自助优惠配置尚未开启！");
			return returnMap;
		}
		
		if (!String.valueOf(youhuiType).equals(config.getTitleId())) {
		
			returnMap.put("message", "优惠类型不一致，请联系客服进行核实后再操作！");
			return returnMap;
		}
		
		if (config.getDeleteFlag() == 0) {
			
			returnMap.put("message", "优惠配置已被删除，请联系客服进行核实后再操作！");
			return returnMap;
		}
		
		if (StringUtils.isNotBlank(config.getVip()) && !config.getVip().contains(String.valueOf(level))) {
		
			returnMap.put("message", "申请的优惠不包含在玩家对应的等级中，请联系客服确认后再进行操作！");
			return returnMap;
		}
		
		if (StringUtils.isNotBlank(config.getStartTime()) && StringUtils.isNotBlank(config.getEndTime())) {
			
			long sTime = 0;
			long eTime = 0;
			
			try {
				
				sTime = DateUtil.getDateFromDateStr(config.getStartTime()).getTime();
				eTime = DateUtil.getDateFromDateStr(config.getEndTime()).getTime();
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
			
			long cTime = DateUtil.getCurrentDate().getTime();
			
			if (!(sTime <= cTime && cTime <= eTime)) {
				
				returnMap.put("message", "优惠不在申请的时间范围内，请联系客服确认后再进行操作！");
				return returnMap;
			}
		} else {
			
			returnMap.put("message", "申请的优惠配置未设置启用开始时间或者启用结束时间，请联系客服设置后再申请此优惠！");
			return returnMap;
		}
		
		// 判断优惠使用剩余次数是否够用
		Integer times = useCount(hibernateTemplate, loginName, id, platform, youhuiType, config.getTimesFlag());
		
		if (times >= config.getTimes()) {
		
			returnMap.put("message", "超过最大使用次数。");
			return returnMap;
		}
		
		// 新增逻辑1(存款额)：判断当前用户是否在配置的时间段内达到指定的存款额，如果没有，则不允许转账
		if (null != config.getDepositAmount() && config.getDepositAmount() > 0) {
			
			log.info("自助MG限时优惠->自助优惠配置信息：[标题=" + config.getAliasTitle() + ",存款额要求=" + config.getDepositAmount() + ",存款开始时间=" + config.getDepositStartTime() + ",存款结束时间=" + config.getDepositEndTime() + "]");
			
			Double depositAmount = config.getDepositAmount();
			
			Double deposit = getDeposit(hibernateTemplate, loginName, config.getDepositStartTime(), config.getDepositEndTime());
			
			log.info("玩家：" + loginName + "在配置的时间段内存款额为：" + deposit);
				
			if (deposit < depositAmount) {
				
				returnMap.put("message", "对不起，您的条件不满足！");
				return returnMap;
			}
		}
		
		// 新增逻辑2(输赢值)：判断当前用户是否在配置的时间段内达到指定的输赢值，如果没有，则不允许转账
		if (null != config.getBetAmount() && config.getBetAmount() < 0) {
		
			log.info("自助MG限时优惠->自助优惠配置信息：[标题=" + config.getAliasTitle() + ",输赢值要求=" + config.getBetAmount() + ",输赢开始时间=" + config.getBetStartTime() + ",输赢结束时间=" + config.getBetEndTime() + "]");
			
			Double betAmount = config.getBetAmount();
			
			Double bet = getBet(hibernateTemplate, loginName, config.getBetStartTime(), config.getBetEndTime());
			
			log.info("玩家：" + loginName + "在配置的时间段内输赢值为：" + bet);
				
			if (bet > betAmount) {
				
				returnMap.put("message", "对不起，您的条件不满足！");
				return returnMap;
			}
		}
		
		returnMap.put("message", "SUCCESS");
		returnMap.put("data", config);
		
		return returnMap;
	}
	
	/**
	 * @description：验证同姓名或同IP下的玩家是否在三个月内领取过体验金
	 * @param：loginName(玩家账号)
	 * @param：type(存送优惠类型编号)
	 * return：Boolean
	 */
	@SuppressWarnings("unchecked")
	public Boolean isReceiveExperienceGold(HibernateTemplate hibernateTemplate, String loginName, Integer type) {
	
		Users user = (Users) hibernateTemplate.get(Users.class, loginName);
		String accountName = user.getAccountName();
		String registerIp = user.getRegisterIp();
		
		// 查询同姓名或同IP的所有玩家
		DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
		dc.add(Restrictions.or(Restrictions.eq("accountName", accountName), Restrictions.eq("registerIp", registerIp)));
		
		List<Users> userList = hibernateTemplate.findByCriteria(dc);
		
		List<String> list = new ArrayList<String>();
		
		if (null != userList && !userList.isEmpty()) {
		
			for (Users users : userList) {
			
				list.add(users.getLoginname());
			}
		}
		
		// 查询这些玩家三个月内是否领过首存，如果没领过，则可以继续进行下去
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		Date beginDate = calendar.getTime();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
		criteria.add(Restrictions.in("loginname", list));
		criteria.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.ge("createTime", beginDate));
		
		List<Proposal> proposalList = hibernateTemplate.findByCriteria(criteria);
		
		if (null != proposalList && !proposalList.isEmpty()) {
		
			return true;
		}
		
		return false;
	}
	
	// NT
	public Double queryBetAmountFromPTPROFIT(HibernateTemplate hibernateTemplate, String loginName) {
	
		String totalBetSql = "select betCredit from ptprofit where loginname=:username and starttime>=:startTime";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", loginName);
		
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		
		params.put("startTime", cd.getTime());
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(totalBetSql);
		query.setProperties(params);

		Object data = query.uniqueResult();

		return null == data ? null : Double.parseDouble(String.valueOf(data));		
	}
	
	// TTG/QT
	public Double queryBetAmountFromPLATFORMDATA(HibernateTemplate hibernateTemplate, String loginName, String platform) {
		
		String totalBetSql = "select bet from platform_data where loginname=:username and starttime>=:startTime and platform=:platform";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", loginName);
		
		Calendar cd = Calendar.getInstance();
		cd.setTime(new Date());
		cd.set(Calendar.HOUR_OF_DAY, 0);
		cd.set(Calendar.MINUTE, 0);
		cd.set(Calendar.SECOND, 0);
		cd.set(Calendar.MILLISECOND, 0);
		
		params.put("startTime", cd.getTime());
		params.put("platform", platform);
		
		Session session = hibernateTemplate.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(totalBetSql);
		query.setProperties(params);

		Object data = query.uniqueResult();

		return null == data ? null : Double.parseDouble(String.valueOf(data));
	}
	
}