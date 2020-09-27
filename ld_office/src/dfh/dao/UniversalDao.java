// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UniversalDao.java

package dfh.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dfh.model.Customer;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.utils.Constants;

public class UniversalDao extends HibernateDaoSupport {

	public UniversalDao() {
	}

	public void delete(Class clazz, Serializable id) {
		getHibernateTemplate().delete(get(clazz, id));
	}

	public <T extends Serializable> void deleteAll(Collection<T> entities){
		this.getHibernateTemplate().deleteAll(entities);
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

	@SuppressWarnings("rawtypes")
	public List list(String sql, Map<String, Object> params) {
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setProperties(params);

		return query.list();
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

	public <T extends Serializable> void saveAll(Collection<T> entities){
		this.getHibernateTemplate().saveOrUpdateAll(entities);
	}
	
	public void saveOrUpdate(Object o) {
		getHibernateTemplate().saveOrUpdate(o);
	}

	public void update(Object o) {
		getHibernateTemplate().update(o);
	}
	
	public void updateUserCredit(Users user ){
		Query insertQuery = this.getSession().createSQLQuery("UPDATE users set credit = ? WHERE loginname=? ");
		insertQuery.setParameter(0, user.getCredit()).setParameter(1, user.getLoginname());
		insertQuery.executeUpdate() ;
	}
	
	public Customer getCustomer(){
		Criteria criteria = getSession().createCriteria(Customer.class);
		criteria.addOrder(Order.desc("createTime"));
        criteria.setFirstResult(0);
        criteria.setMaxResults(1);
        List<Customer> list=criteria.list();
		if (list.size() > 0) {
	        return list.get(0);
	    }
		return null;
	}
	
	 public List<Customer> getCustomerPhoneList(){
			Criteria criteria = getSession().createCriteria(Customer.class);
			criteria.add(Restrictions.isNotNull("phone"));
			criteria.add(Restrictions.ne("phone",""));
			criteria.add(Restrictions.eq("isreg", 0));
			List<Customer> list = criteria.list();
			if (list!=null && list.size() > 0) {
		        return list;
		    }
			return null;
		}
	 
	  public Customer getCustomerPhone(String phone){
			Criteria criteria = getSession().createCriteria(Customer.class);
			criteria.add(Restrictions.eq("phone", phone));
			criteria.add(Restrictions.eq("isreg", 0));
			List<Customer> list = criteria.list();
			if (list!=null && list.size() > 0) {
		        return list.get(0);
		    }
			return null;
		}
	 
	 public Customer getCustomerEmail(String email){
			Criteria criteria = getSession().createCriteria(Customer.class);
			criteria.add(Restrictions.eq("email", email));
			criteria.add(Restrictions.eq("isreg", 0));
			List<Customer> list = criteria.list();
			if (list!=null && list.size() > 0) {
		        return list.get(0);
		    }
			return null;
		}
	   
	   public List<Proposal> getProposalList(String shippingCode){
		   	Criteria criteria = getSession().createCriteria(Proposal.class);
		   	criteria.add(Restrictions.eq("shippingCode", shippingCode));
			List<Proposal> list = criteria.list();
			if (list!=null && list.size() > 0) {
		        return list;
		    }
			return null;
	   }
	   
	   public List<Customer> getCustomerEmailList(){
			Criteria criteria = getSession().createCriteria(Customer.class);
			criteria.add(Restrictions.isNotNull("email"));
			criteria.add(Restrictions.ne("email",""));
			criteria.add(Restrictions.eq("isreg", 0));
			List<Customer> list = criteria.list();
			if (list!=null && list.size() > 0) {
		        return list;
		    }
			return null;
		}
	   
	   public List<Customer> getCustomerEmailList(Integer batch){
			Criteria criteria = getSession().createCriteria(Customer.class);
			if(batch != null){
				criteria.add(Restrictions.eq("batch", batch));
			}
			criteria.add(Restrictions.isNotNull("email"));
			criteria.add(Restrictions.ne("email",""));
			criteria.add(Restrictions.eq("isreg", 0));
			criteria.addOrder(Order.asc("noticeTime"));   //邮件通知时间升序
			criteria.setFirstResult(0);
			criteria.setMaxResults(5000);     //最多取5000
			List<Customer> list = criteria.list();
			if (list!=null && list.size() > 0) {
		        return list;
		    }
			return null;
	   }
	   
	   /**
	    * 更新邮件发送时间
	    * @param batch 批次
	    */
	   public void updateEmailSendTime(Integer batch){
		   StringBuilder sql = new StringBuilder("UPDATE other_customer c1 INNER JOIN (select id from other_customer where 1=1");
			if(batch != null){
				sql.append(" and batch=?");
			}
			sql.append(" and isreg=?");
			sql.append(" and email is not null");
			sql.append(" and email!=?");
			sql.append(" order by noticeTime limit 5000) c2 ON c1.id=c2.id SET c1.noticeTime=SYSDATE()");
			
			try {
				PreparedStatement stmt = getSession().connection().prepareStatement(sql.toString());
				int i = 1;
				if(batch != null){
					stmt.setInt(i++, batch);
				}
				stmt.setInt(i++, 0);
				stmt.setString(i++, "");
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	   }
	   
	   public Users getFindUser(String loginname){
		   Criteria criteria = getSession().createCriteria(Users.class);
			criteria.add(Restrictions.eq("loginname", loginname));
			List<Users> list = criteria.list();
			if (list!=null && list.size() > 0) {
		        return (Users)list.get(0);
		    }
			return null;
	   }
	   
	   public Boolean updateUser(Double limit,String loginname){
		   String hql = "update Users set creditlimit=? where loginname = ?";
	       Query q = getSession().createQuery(hql);
	       q.setParameter(0, limit);
	       q.setParameter(1, loginname);
	       q.executeUpdate();
	       return true;
	   }
	   
	   public Boolean updateUserrole(String userrole,Integer id){
		   String hql = "update Bankinfo set userrole=? where id = ?";
	       Query q = getSession().createQuery(hql);
	       q.setParameter(0, userrole);
	       q.setParameter(1, id);
	       q.executeUpdate();
	       return true;
	   }
	   
	   public Boolean updateIsshow(Integer isshow,Integer id){
		   String hql = "update Bankinfo set isshow=? where id = ?";
	       Query q = getSession().createQuery(hql);
	       q.setParameter(0, isshow);
	       q.setParameter(1, id);
	       q.executeUpdate();
	       return true;
		}
	   
	   public Boolean updateIstransfer(Integer istransfer,Integer id){
		   String hql = "update Bankinfo set istransfer=? where id = ?";
	       Query q = getSession().createQuery(hql);
	       q.setParameter(0, istransfer);
	       q.setParameter(1, id);
	       q.executeUpdate();
	       return true;
		}
	   
	   public void updateUserStatusSlotCreditSql(Users user,Double remit){
			Query insertQuery = this.getSession().createSQLQuery("UPDATE userstatus set slotaccount = slotaccount + ?  WHERE loginname=? ");
			insertQuery.setParameter(0, remit).setParameter(1, user.getLoginname());
			insertQuery.executeUpdate() ;
	   }
	   
	   public void updateUserCreditSql(Users user,Double remit){
			Query insertQuery = this.getSession().createSQLQuery("UPDATE users set credit = credit + ?  WHERE loginname=? ");
			insertQuery.setParameter(0, remit).setParameter(1, user.getLoginname());
			insertQuery.executeUpdate() ;
		}
	   /*public List<String> queryUserByCondition(String agents, String isdeposit, Integer usernameType){
		   StringBuffer sbf = new StringBuffer();
		   sbf.append("SELECT ");
		   sbf.append("	t.loginname ");
		   sbf.append("FROM ");
		   sbf.append("	users t ");
		   sbf.append("WHERE ");
		   if("Y".equals(isdeposit)){
				sbf.append("	EXISTS ( ");
				sbf.append("		SELECT ");
				sbf.append("			1 ");
				sbf.append("		FROM ");
				sbf.append("			( ");
				sbf.append("				SELECT DISTINCT ");
				sbf.append("					loginname ");
				sbf.append("				FROM ");
				sbf.append("					proposal ");
				sbf.append("				WHERE ");
				sbf.append("					type = 502 ");
				sbf.append("				AND flag = 2 ");
				sbf.append("				UNION ");
				sbf.append("					SELECT DISTINCT ");
				sbf.append("						loginname ");
				sbf.append("					FROM ");
				sbf.append("						payorder ");
				sbf.append("					WHERE ");
				sbf.append("						type = 0 ");
				sbf.append("					AND flag = 0 ");
				sbf.append("			) u ");
				sbf.append("		WHERE ");
				sbf.append("			u.loginname = t.loginname ");
				sbf.append("	) ");
			}else if ("N".equals(isdeposit)){
				sbf.append(" NOT EXISTS ( ");
				sbf.append("		SELECT ");
				sbf.append("			1 ");
				sbf.append("		FROM ");
				sbf.append("			( ");
				sbf.append("				SELECT DISTINCT ");
				sbf.append("					loginname ");
				sbf.append("				FROM ");
				sbf.append("					proposal ");
				sbf.append("				WHERE ");
				sbf.append("					type = 502 ");
				sbf.append("				AND flag = 2 ");
				sbf.append("				UNION ");
				sbf.append("					SELECT DISTINCT ");
				sbf.append("						loginname ");
				sbf.append("					FROM ");
				sbf.append("						payorder ");
				sbf.append("					WHERE ");
				sbf.append("						type = 0 ");
				sbf.append("					AND flag = 0 ");
				sbf.append("			) u ");
				sbf.append("		WHERE ");
				sbf.append("			t.loginname = u.loginname ");
				sbf.append("	) ");
			}else{
				sbf.append("	1=1 ");
			}
			if (usernameType != 7) {
				sbf.append("	and t.`level` = " + usernameType);
			}
			sbf.append(" and t.agent IN ("+agents+") ");
		   String sql = sbf.toString();
		   return this.getSession().createSQLQuery(sql).list();
	   }
	   */
	   
	   public List<String> queryUserByCondition(String agents, String isdeposit, Integer usernameType) {
			StringBuffer sbf = new StringBuffer();
			sbf.append(" SELECT ");
			sbf.append(" t.loginname ");
			sbf.append(" FROM ");
			sbf.append(" users t ");
			sbf.append(" WHERE  1 = 1 ");
			if ("Y".equals(isdeposit)) {
				// 0标识有存款
				sbf.append(" and isCashin  = 0 ");
			} else if ("N".equals(isdeposit)) {
				// 1标识没有存款
				sbf.append(" and isCashin  = 1 ");
			} 
			if (usernameType != Constants.TOPIC_SEND_TYPE_ALL_MEMBER) {
				sbf.append(" and t.level = " + usernameType);
			}
			sbf.append(" and t.agent IN (" + agents + ") ");
			return this.getSession().createSQLQuery(sbf.toString()).list();
		}

	public void updateSignamount(Double amount, Date startTime, Date endTime){
		Query  updateQuery = this.getSession().createSQLQuery("UPDATE signamount set amountbalane = ? WHERE  amountbalane>0 ");
		updateQuery.setParameter(0, amount);
		updateQuery.executeUpdate() ;
		System.out.println("更新成功! UPDATE signamount set amountbalane = "+amount+" WHERE updatetime= "+startTime+" and updatetime= "+endTime);
	}
	public int executeUpdate(String sql, Map<String, Object> params) {
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		query.setProperties(params);
		
		return query.executeUpdate();
	}
}
