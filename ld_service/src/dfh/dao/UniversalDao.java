// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UniversalDao.java

package dfh.dao;

import java.io.Serializable;
import java.util.*;

import dfh.model.*;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dfh.utils.Page;
import dfh.utils.PagenationUtil;
import dfh.utils.StringUtil;

public class UniversalDao extends HibernateDaoSupport {

	public UniversalDao() {
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
	public List<SignRecord> findSignrecord(String loginname, Date startTime, Date endTime) {
		Criteria dc = getSession().createCriteria(SignRecord.class);
		dc.add(Restrictions.eq("username", loginname));
		dc.add(Restrictions.eq("isdelete", "0"));//未删除
		dc.add(Restrictions.ge("createtime", startTime));
		dc.add(Restrictions.le("createtime", endTime));
		dc.add(Restrictions.eq("type", "0"));
		List<SignRecord> signRecord = dc.list();
		return signRecord;
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
	
	public void updatelockUser(Users user){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set flag = ?,remark = ? WHERE loginname=? ");
		insertQuery.setParameter(0, user.getFlag()).setParameter(1, user.getRemark()).setParameter(2, user.getLoginname());
		insertQuery.executeUpdate() ;
		System.out.println("更新成功!UPDATE users set flag = "+user.getFlag()+",remark = "+user.getRemark()+" WHERE loginname="+user.getLoginname());
	}
	
	public void updateUserCreditSqlVip(Users user){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set level = ? WHERE loginname=? ");
		insertQuery.setParameter(0, user.getLevel()).setParameter(1, user.getLoginname());
		insertQuery.executeUpdate() ;
		System.out.println("更新成功! UPDATE users set level = "+user.getLevel()+" WHERE loginname= "+user.getLoginname());
	}
	
	//在线支付专用
	public void updateUserCreditSqlOnline(String loginname , Double remit ){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set credit = credit + ? WHERE loginname=? ");
		insertQuery.setParameter(0, remit).setParameter(1, loginname);
		insertQuery.executeUpdate() ;
	}
	
	public void updateUserSqlIsCashinOnline(String loginname){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set isCashin = 0 WHERE loginname=? ");
		insertQuery.setParameter(0, loginname);
		insertQuery.executeUpdate() ;
	}
	
	public void updateTaskAmountSql(TaskAmount taskAmount){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE task_amount set amount = ? WHERE loginname=? ");
		insertQuery.setParameter(0, taskAmount.getAmount()).setParameter(1, taskAmount.getLoginname());
		insertQuery.executeUpdate() ;
	}
	
	public void updateAgentSlotAccountSql(String loginname , Double remit){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE userstatus set slotaccount = slotaccount + ? WHERE loginname=? ");
		insertQuery.setParameter(0, remit).setParameter(1, loginname);
		insertQuery.executeUpdate() ;
	}
	

	public void updateUserGifTamountSqlOnline(String loginname , Double gifTamount,String shippingcode){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set giftamount = ?,shippingcode = ? WHERE loginname=? ");
		insertQuery.setParameter(0, gifTamount).setParameter(1, shippingcode).setParameter(2, loginname);
		insertQuery.executeUpdate() ;
	}
	

	public void updateUserShippingcodeTtSql(Users user){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set shippingcodePt = ? WHERE loginname=? ");
		insertQuery.setParameter(0,  null).setParameter(1,  user.getLoginname());
		insertQuery.executeUpdate() ;
	}  
	
	public void updateUserCdaySql(Users user,Double creditday , String creditdaydate){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set creditday = ? , creditdaydate = ?  WHERE loginname=? ");
		insertQuery.setParameter(0,  creditday).setParameter(1,  creditdaydate).setParameter(2,  user.getLoginname());
		insertQuery.executeUpdate() ;
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
	
	public Integer getUsersCountByRandNum(String randnum) {
		Criteria criteria = getSession().createCriteria(Users.class);
		criteria.add(Restrictions.eq("randnum", randnum));
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
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
	
	public Users getUsers(String loginname){
		Criteria criteria = getSession().createCriteria(Users.class);
		criteria.add(Restrictions.eq("loginname", loginname));
		List<Users> user = criteria.list();
		if (user!=null && user.size() > 0) {
			return user.get(0);
		}
		return null;
	}
	
	public void updateUserCreditSql(Users user,Double remit){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set credit = credit + ? ,creditday= creditday - ? WHERE loginname=? ");
		insertQuery.setParameter(0, remit).setParameter(1, remit).setParameter(2, user.getLoginname());
		insertQuery.executeUpdate() ;
	}
	
	public void updateUserShippingcodeSql(Users user){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set shippingcode = ? WHERE loginname=? ");
		insertQuery.setParameter(0,  null).setParameter(1,  user.getLoginname());
		insertQuery.executeUpdate() ;
	}
	
	public void updateUserShippingcodePtSql(Users user){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set shippingcodePt = ? WHERE loginname=? ");
		insertQuery.setParameter(0,  null).setParameter(1,  user.getLoginname());
		insertQuery.executeUpdate() ;
	}
	
	public Const getConsts(String id){
		Criteria criteria = getSession().createCriteria(Const.class);
		criteria.add(Restrictions.eq("id", id));
		List<Const> constPt = criteria.list();
		if (constPt!=null && constPt.size() > 0) {
			return constPt.get(0);
		}
		return null;
	}
	
	public List getList(String sql,Class a){
		Query insertQuery = getSession().createSQLQuery(sql).addEntity(a);
		return insertQuery.list();
	}
	
	
	public List getListBysql(String sql){
		  SQLQuery query = getSession().createSQLQuery(sql);
		return query.list();
	}
	
	public Page getPage(String sql,int pageIndex,int size,String count){
		Page page = new Page();
		  SQLQuery query1 =  getSession().createSQLQuery(sql);
		  query1.setFirstResult((pageIndex - 1) * size);
		  query1.setMaxResults(size);
		  List list = query1.list();
		  List<BetRankModel> listplatformData=new ArrayList<BetRankModel>();
		  int z = (pageIndex - 1) * size+1;
			for(Object obj : list){
				BetRankModel br = new BetRankModel();
				Object[]objarray = (Object[])obj;
				 br.setAmount(objarray[0].toString());
				 
				 String loginname=(String)objarray[1];
					if(!StringUtil.isEmpty(loginname)&&loginname.length()>2){
						loginname=loginname.substring(0, loginname.length()-3);
						loginname=loginname+"***";
					br.setLoginname(loginname);
					}
				 br.setPlatform(objarray[2].toString());
				 if(null!=objarray[3]){
				 String address =objarray[3].toString();
				 if(!StringUtil.isEmpty(address)&&address.length()>1){
				 if(address.contains("黑龙江")){
					 address="黑龙江";
					 br.setAddress(address);
				 }else if(address.contains("内蒙古")){
					 address="内蒙古";
					 br.setAddress(address);
				 }else{
					 address=address.substring(0, 2);
					 br.setAddress(address);
				 }
				 }else{
					 br.setAddress("中国");  
				 }
				 }else{
					 br.setAddress("中国");  
				 }
				
				br.setNo(z);
				listplatformData.add(br);
				z++;
			}
			page.setPageNumber(pageIndex);
			page.setSize(size);
			page.setTotalRecords(Integer.parseInt(count));
			int pages = PagenationUtil.computeTotalPages(Integer.parseInt(count), size).intValue();
			page.setTotalPages(Integer.valueOf(pages));
			if (pageIndex > pages)
				pageIndex = Page.PAGE_BEGIN_INDEX;
			page.setPageNumber(pageIndex);
			page.setPageContents(listplatformData);
			page.setNumberOfRecordsShown(Integer.valueOf(page.getPageContents().size()));
			return page;
	}
	
	//保存额度订单
	public Integer saveValidatedPayOrderTwo(String sql){
		Query insertQuery = this.getSession().createSQLQuery(sql);
		int numSize = insertQuery.executeUpdate() ;
		System.out.println("此次添加额度处理数为："+numSize);
		return numSize;
	}
	
	public List list(String sql, Map<String, Object> params) {
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.list();
	}
	
	public int executeBySql(String sql){
		return getSession().createSQLQuery(sql).executeUpdate();
	}
	
}