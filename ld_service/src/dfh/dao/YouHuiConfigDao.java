package dfh.dao;

import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import dfh.model.Proposal;
import dfh.model.YouHuiConfig;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.utils.DateUtil;

public class YouHuiConfigDao extends UniversalDao {
	private final Logger log = Logger.getLogger(YouHuiConfigDao.class);

	public YouHuiConfig getYouHuiConfig(String title,Integer level) {
		DetachedCriteria criteria = DetachedCriteria.forClass(YouHuiConfig.class);
		criteria.add(Restrictions.eq("title", title));
		criteria.add(Restrictions.eq("isused", 1));
		
		criteria.add(Restrictions.le("starttime", new Date()));
		criteria.add(Restrictions.ge("endtime", new Date()));
		criteria.add(Restrictions.eq("isused", 1));
		criteria.add(Restrictions.like("vip", level.toString() ,MatchMode.ANYWHERE));
		List<YouHuiConfig> list = findByCriteria(criteria);
		if (null != list && list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public Double getDeposit(String loginname){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT sum(a.money) as money FROM");
		sql.append(" (");
		sql.append(" SELECT sum(t.money) as money FROM payorder t where t.type = 0 and t.flag = 0 and t.loginname = :loginname and (YEAR (t.createTime) = YEAR (NOW()) and  MONTH (t.createTime) = MONTH (NOW()))");
		sql.append(" union all");
		sql.append(" SELECT sum(t.amount) as money FROM proposal t where t.type = 502 and t.flag = 2 and t.loginname = :loginname and (YEAR (t.createTime) = YEAR (NOW()) and  MONTH (t.createTime) = MONTH (NOW()))");
		sql.append(" ) a");

		log.info("getDeposit=" + sql.toString());

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("loginname", loginname);

		Query query = this.getSession().createSQLQuery(sql.toString());
		query.setProperties(params);
		Object data = query.uniqueResult();
		return Double.parseDouble((null == data) ? "0" : data.toString());
	}
	
	public List<YouHuiConfig> getAllYouHuiConfig(Integer level) {
		DetachedCriteria criteria = DetachedCriteria.forClass(YouHuiConfig.class);
		criteria.add(Restrictions.eq("isused", 1));
		
		criteria.add(Restrictions.le("starttime", new Date()));
		criteria.add(Restrictions.ge("endtime", new Date()));
		criteria.add(Restrictions.eq("isused", 1));
		criteria.add(Restrictions.like("vip", level.toString() ,MatchMode.ANYWHERE));
		List<YouHuiConfig> list = findByCriteria(criteria);
		return list;
	}
	
	public Boolean youhuiContinueOrStop(Integer times , Integer timesflag , String loginname , ProposalType type) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(Proposal.class);
			criteria.add(Restrictions.eq("loginname", loginname)) ;
			criteria.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode())) ;
			criteria.add(Restrictions.eq("type", type.getCode())) ;
			if(timesflag == 1){  //日
				criteria.add(Restrictions.ge("executeTime", DateUtil.getTodayZeroHour()));
			}else if (timesflag == 2){ //周
				criteria.add(Restrictions.ge("executeTime", DateUtil.getFirstDayOfWeek(new Date())));
			}else if (timesflag == 3){ //月
				criteria.add(Restrictions.ge("executeTime", DateUtil.getStartDayOfMonth(new Date())));
			}else if (timesflag == 4){ //年
				criteria.add(Restrictions.ge("executeTime", DateUtil.getYearFirst(Calendar.getInstance().get(Calendar.YEAR))));
			}
			List<Proposal> proposals = findByCriteria(criteria);
			if(null != proposals && (proposals.size()<times)){
				return true ;
			}else{
				log.info(loginname+"--"+type.getText()+",已经使用"+proposals.size()+"次。"+"timesflag:"+timesflag+",times"+times);
				return false ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false ;
	}

}
