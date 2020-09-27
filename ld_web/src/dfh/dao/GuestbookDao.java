// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UniversalDao.java

package dfh.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.MessageVo;
import dfh.action.vo.Messages;
import dfh.model.Guestbook;
import dfh.model.Users;
import dfh.utils.Constants;
import dfh.utils.DateUtil;


public class GuestbookDao extends MyTwoHibernateDaoSupport {

	public GuestbookDao() {
	}

	public void delete(Class clazz, Serializable id) {
		getHibernateTemplate().delete(get(clazz, id));
	}

	public List findByCriteria(DetachedCriteria criteria) {
		return getHibernateTemplate().findByCriteria(criteria);
	}

	public List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResult) {
		return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResult);
	}

	public List findByNamedQuery(String sql, Map params) {
		int len = params.size();
		String paramNames[] = new String[len];
		Object paramValues[] = new Object[len];
		int index = 0;
		for (Iterator i = params.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			paramNames[index] = key;
			paramValues[index++] = params.get(key);
		}

		return getHibernateTemplate().findByNamedQueryAndNamedParam(sql, paramNames, paramValues);
	}

	public Object get(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	public Object get(Class clazz, Serializable id, LockMode mode) {
		return getHibernateTemplate().get(clazz, id, mode);
	}

	public Integer getCount(Class clazz) {
		DetachedCriteria dc = DetachedCriteria.forClass(clazz);
		dc.setProjection(Projections.rowCount());
		return (Integer) getHibernateTemplate().findByCriteria(dc).get(0);
	}

	public List loadAll(Class clazz) {
		return getHibernateTemplate().loadAll(clazz);
	}

	public Object save(Object o) {
		return getHibernateTemplate().save(o);
	}

	public void saveOrUpdate(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void update(Object o) {
		getHibernateTemplate().update(o);
	}

	public Integer getGuestbookCount(String loginname) {
		Criteria criteria = getSession().createCriteria(Guestbook.class);
		criteria.add(Restrictions.eq("username", loginname));
		criteria.add(Restrictions.isNull("referenceId"));
		criteria.add(Restrictions.eq("flag", 0));
		criteria.add(Restrictions.eq("userstatus", 0));
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}

	public Integer getGuestbookCountAll(String loginname) {
		Criteria criteria = getSession().createCriteria(Guestbook.class);
		criteria.add(Restrictions.eq("username", loginname));
		criteria.add(Restrictions.isNull("referenceId"));
		criteria.add(Restrictions.eq("flag", 0));
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	public Integer getGuestbookCountMessage(Integer agent){
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		Criteria criteria = getSession().createCriteria(Guestbook.class);
		criteria.add(Restrictions.isNull("referenceId"));
		if(agent==8){//如果是代理则只查询代理 
			criteria.add(Restrictions.eq("message", agent));
			}else{//否则查询当前用户等级下的数据 和 针对全体的 数据 
			    criteria.add(Restrictions.or(Restrictions.eq("message", agent), Restrictions.eq("message", 7)));
				//criteria.add(Restrictions.or(Restrictions.eq("message", agent), Restrictions.or(Restrictions.eq("message", 7),Restrictions.eq("username", user.getIntro()))));
		}
		criteria.add(Restrictions.eq("flag", 0));
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	
	public HttpSession getHttpSession() {
		return getRequest().getSession();
	}
	
	public List<Guestbook> getGuestbookMessageList(String loginname, Integer page,Integer count,Integer userstatus,Integer agent) {
		Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
		Criteria criteria = getSession().createCriteria(Guestbook.class);
		criteria.add(Restrictions.isNull("referenceId"));
		if(agent==8){//如果是代理则只查询代理 
			criteria.add(Restrictions.eq("message", agent));
		}else{//否则查询当前用户等级下的数据0-6 和 针对全体的 数据7 
			criteria.add(Restrictions.or(Restrictions.eq("message", agent), Restrictions.eq("message", 7)));
		}
		//criteria.add(Restrictions.eq("message", agent));
		criteria.add(Restrictions.eq("flag", 0));
		criteria.addOrder(Order.desc("createdate"));
		criteria.setFirstResult(count * (page - 1));
		criteria.setMaxResults(count);
		List<Guestbook> list = criteria.list();
		if (list != null && list.size() > 0) {
			return list;
		}
		return null;
	}

	public List<Guestbook> getGuestbookList(String loginname, Integer page,Integer count,Integer userstatus,Integer referenceId) {
		if(referenceId!=null){
			Criteria criteria = getSession().createCriteria(Guestbook.class);
			criteria.add(Restrictions.eq("referenceId", referenceId));
			criteria.add(Restrictions.eq("flag", 0));
			List<Guestbook> list=criteria.list();
			if(list!=null && list.size()>0){
				return list;
			}
			return null;
		}else{
			Criteria criteria = getSession().createCriteria(Guestbook.class);
			criteria.add(Restrictions.eq("username", loginname));
			criteria.add(Restrictions.isNull("referenceId"));
			if (userstatus != null) {
				criteria.add(Restrictions.eq("userstatus", 0));
			}
			criteria.add(Restrictions.eq("flag", 0));
			criteria.addOrder(Order.desc("createdate"));
			criteria.setFirstResult(count * (page - 1));
			criteria.setMaxResults(count);
			List<Guestbook> list=criteria.list();
			if(list!=null && list.size()>0){
				return list;
			}
			return null;
		}
	}
	
	public Integer getGuestbookListCount(String loginname,Integer userstatus) {
		Criteria criteria = getSession().createCriteria(Guestbook.class);
		criteria.add(Restrictions.eq("username", loginname));
		criteria.add(Restrictions.isNull("referenceId"));
		if (userstatus != null) {
			criteria.add(Restrictions.eq("userstatus", 0));
		}
		criteria.add(Restrictions.eq("flag", 0));
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	/**
	 * 未读邮件数量（包括公共邮件）
	 * @param sql
	 * @param params
	 * @return
	 */
	public Integer getUnReadMessageCount(String loginname, Integer agent){
		//select (select COUNT(*) from guestbook where username='' and flag='' and userstatus='') + 
		//(select COUNT(*) from guestbook g where g.flag='' and g.message in() and createdate> and not EXISTS(select 1 from msg_readed r where username='' and g.id=r.mid))
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sqlStr = new StringBuilder("select (select COUNT(*) from guestbook where username=:username and flag=:flag and userstatus=:userstatus) + " +
				"(select COUNT(*) from guestbook g where g.username is null and g.flag=:flag and ");
		if(agent.equals(8)){
			//代理
			sqlStr.append("g.message=:message and ");
			params.put("message", 8);
		}else{
			sqlStr.append("g.message in (:self, :all) and ");
			params.put("self", agent);
			params.put("all", 7);
		}
		sqlStr.append("createdate>:lastmonthTime and not EXISTS(select 1 from msg_readed r where username=:username and g.id=r.mid)) + ");
		sqlStr.append("(select COUNT(*) from guestbook g1 where g1.userstatus=:userstatus and EXISTS (select 1 from guestbook g2 where g2.username=:username and g2.id=g1.referenceId))");
		
		params.put("username", loginname);
		params.put("flag", 0);
		params.put("userstatus", 0);
		
		//公共信件取一个月以内的
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DAY_OF_MONTH, -5);
		params.put("lastmonthTime", cd.getTime());
		
		Query query = this.getSession().createSQLQuery(sqlStr.toString());
		query.setProperties(params);
		return Integer.parseInt(query.uniqueResult().toString());
	}
	
	/**
	 * 查询玩家的站内信
	 * （所有个人站内信以及5天以内的群发站内信）
	 * @param loginName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void getMessagesByUser(String loginName, Integer agent, Messages msg){
		Map<String, Object> params = new HashMap<String, Object>();
		//站内信总数
		StringBuilder countSql = new StringBuilder("select count(*) from guestbook g1 where flag=:flag and (username=:username or EXISTS (select 1 from guestbook g2 where g2.username=:username and g2.id=g1.referenceId) or (username is null ");
		if(agent.equals(8)){
			//代理
			countSql.append(" and message=:message ");
			params.put("message", 8);
		}else{
			countSql.append(" and message in (:self, :all) ");
			params.put("self", agent);
			params.put("all", 7);
		}
		countSql.append(" and createdate>:lastmonthTime))");
		
		params.put("username", loginName);
		params.put("flag", 0);		
		//公共信件取一个月以内的
		Calendar cd = Calendar.getInstance();
		cd.add(Calendar.DAY_OF_MONTH, -5);
		params.put("lastmonthTime", cd.getTime());
		
		Query query = this.getSession().createSQLQuery(countSql.toString());
		query.setProperties(params);
		msg.setCount(Integer.parseInt(query.uniqueResult().toString()));
		
		StringBuilder msgSql = new StringBuilder("select id, title, createdate, userstatus, isprivate from ");
		msgSql.append("(select id, title, createdate, userstatus, 1 as isprivate from guestbook where username=:username and flag=:flag ").append("union ");
		msgSql.append("select id, title, createdate, userstatus, 0 as isprivate from guestbook where username is null and flag=:flag ");
		if(agent.equals(8)){
			//代理
			msgSql.append(" and message=:message ");
		}else{
			msgSql.append(" and message in (:self, :all) ");
		}
		msgSql.append(" and createdate>:lastmonthTime ").append("union ");
		msgSql.append("select id, title, createdate, userstatus, 1 as isprivate from guestbook g1 where EXISTS (select 1 from guestbook g2 where g2.username=:username and g2.id=g1.referenceId))t ");
		msgSql.append("order by createdate desc limit :offset, :size");
		params.put("offset", msg.getPageSize()*(msg.getPageNo()-1));
		params.put("size", msg.getPageSize());
		
		query = this.getSession().createSQLQuery(msgSql.toString());
		query.setProperties(params);
		List msgList = query.list();
		for (Object object : msgList) {
			Object[] arrObj = (Object[]) object;
			MessageVo msgVo = new MessageVo();
			msgVo.setId(Integer.parseInt(arrObj[0].toString()));
			msgVo.setTitle(arrObj[1].toString());
			msgVo.setCreateDate(DateUtil.formatDateForStandard((Date)arrObj[2]));
			msgVo.setPrivate(arrObj[4].toString().equals("1")?true:false);
			if(msgVo.isPrivate()){
				msgVo.setRead(arrObj[3].toString().equals("1")?true:false);
			}else{
				//判断群发邮件是否已读
				msgVo.setRead(isRead4PublicMsg(msgVo.getId(), loginName));
			}
			msg.getMsgList().add(msgVo);
		}
	}
	
	/**
	 * 判断群发站内信是否已读
	 * @return
	 */
	public boolean isRead4PublicMsg(Integer msgID, String loginName){
		String sql = "select count(*) from msg_readed where mid=:msgid and username=:username";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", loginName);
		params.put("msgid", msgID);
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return Integer.parseInt(query.uniqueResult().toString())>0?true:false;
	}
	
	public Integer getSubMessage(Integer id){
		Map<String, Object> params = new HashMap<String, Object>();
		String sql = "select count(*) from guestbook where referenceId=:rid";
		params.put("rid", id);
		
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return Integer.parseInt(query.uniqueResult().toString());
	} 
}
