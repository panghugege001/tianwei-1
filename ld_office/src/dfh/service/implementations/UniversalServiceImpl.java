// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UniversalServiceImpl.java

package dfh.service.implementations;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.dao.UniversalDao;
import dfh.model.Customer;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.service.interfaces.UniversalService;

public class UniversalServiceImpl implements UniversalService {

	private UniversalDao universalDao;

	public UniversalServiceImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#delete(java.lang.Class,
	 * java.io.Serializable)
	 */
	public void delete(Class clazz, Serializable id) {
		universalDao.delete(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dfh.service.implementations.UniversalService#findByCriteria(org.hibernate
	 * .criterion.DetachedCriteria)
	 */
	public List findByCriteria(DetachedCriteria criteria) {
		return universalDao.getHibernateTemplate().findByCriteria(criteria);
	}



	public List findByCriteria(DetachedCriteria criteria, Integer firstResult,
			Integer maxResults) {
		return universalDao.getHibernateTemplate().findByCriteria(criteria,
				firstResult, maxResults);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dfh.service.implementations.UniversalService#findByNamedQuery(java.lang
	 * .String, java.util.Map)
	 */
	public List findByNamedQuery(String sql, Map params) {
		return universalDao.findByNamedQuery(sql, params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#get(java.lang.Class,
	 * java.io.Serializable)
	 */
	public Object get(Class clazz, Serializable id) {
		return universalDao.get(clazz, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#get(java.lang.Class,
	 * java.io.Serializable, org.hibernate.LockMode)
	 */
	public Object get(Class clazz, Serializable id, LockMode mode) {
		return universalDao.get(clazz, id, mode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#getHibernateTemplate()
	 */
	public HibernateTemplate getHibernateTemplate() {
		return universalDao.getHibernateTemplate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dfh.service.implementations.UniversalService#loadAll(java.lang.Class)
	 */
	public List loadAll(Class clazz) {
		return universalDao.loadAll(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dfh.service.implementations.UniversalService#save(java.lang.Object)
	 */
	public Object save(Object o) {
		return universalDao.save(o);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dfh.service.implementations.UniversalService#saveOrUpdate(java.lang.Object
	 * )
	 */
	public void saveOrUpdate(Object o) {
		universalDao.saveOrUpdate(o);
	}

	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * dfh.service.implementations.UniversalService#update(java.lang.Object)
	 */
	public void update(Object o) {
		universalDao.update(o);
	}

	public Customer getCustomer() {
		return universalDao.getCustomer();
	}
	public List list(String sql, Map<String, Object> params) {
		return universalDao.list(sql,params);
	}

	public List<Customer> getCustomerPhoneList() {
		return universalDao.getCustomerPhoneList();
	}

	public List<Proposal> getProposalList(String shippingCode) {
		return universalDao.getProposalList(shippingCode);
	}

	public List<Customer> getCustomerEmailList() {
		return universalDao.getCustomerEmailList();
	}

	public List<Customer> getCustomerEmailList(Integer batch) {
		return universalDao.getCustomerEmailList(batch);
	}
	
	

	/**
	 * 更新邮件发送时间
	 * 
	 * @param batch
	 *            批次
	 */
	public void updateEmailSendTime(Integer batch) {
		universalDao.updateEmailSendTime(batch);
	}

	public Customer getCustomerPhone(String phone) {
		return universalDao.getCustomerPhone(phone);
	}

	public Customer getCustomerEmail(String email) {
		return universalDao.getCustomerEmail(email);
	}

	public Users getFindUser(String loginname) {
		return universalDao.getFindUser(loginname);
	}

	public Boolean updateUser(Double limit, String loginname) {
		return universalDao.updateUser(limit, loginname);
	}

	public <T extends Serializable> void saveAll(Collection<T> entities) {
		universalDao.saveAll(entities);
	}
	
	public <T extends Serializable> void deleteAll(Collection<T> entities){
		universalDao.deleteAll(entities);
	}
	
	public int executeUpdate(String sql, Map<String, Object> params) {
		return universalDao.executeUpdate(sql,params);
	}
}
