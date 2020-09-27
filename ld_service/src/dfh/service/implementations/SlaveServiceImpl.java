package dfh.service.implementations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.dao.SlaveDao;
import dfh.service.interfaces.SlaveService;
import java.util.Calendar;
import java.util.Date;
import dfh.service.interfaces.CustomerService;
import dfh.utils.DateUtil;

public  class SlaveServiceImpl implements SlaveService  {

	private SlaveDao slaveDao;

	public SlaveDao getSlaveDao() {
		return slaveDao;
	}

	public void setSlaveDao(SlaveDao slaveDao) {
		this.slaveDao = slaveDao;
	}

	public HibernateTemplate getHibernateTemplate() {
		return slaveDao.getHibernateTemplate();
	}

	public Object get(Class clazz, Serializable id) {
		return slaveDao.get(clazz, id);
	}
	
	public List findByCriteria(DetachedCriteria criteria,Integer  firstResult,Integer maxResults) {
		return slaveDao.getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
	}
	
	public List findByCriteria(DetachedCriteria criteria) {
		return slaveDao.getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public boolean gamecheck(String type, String gameid) {
		String sql = "select count(1) from gamecode where type =:type and code = :code";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		params.put("code", gameid);
		int count = slaveDao.getCount(sql, params);
		if(count > 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public List checkTotalExist(String loginname) {
		String sql = "select alldeposit,updatetime from total_deposits where loginname = :loginname";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		List list = slaveDao.queryList(sql, params);
		return list;
	}

	@Override
	public List getList(String sql, Map<String, Object> params) {
		return slaveDao.getList(sql, params);
	}
	
	
	//查询到目前为止的总存款额
	@Override
	public Double getDeposit(String loginname) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT sum(a.money) as money FROM");
		sql.append(" (");
		sql.append(" 	SELECT sum(t.money) as money FROM payorder t where t.type = 0 and t.flag = 0 and t.loginname = :loginname and createTime > '2017-01-01' ");
		sql.append(" union all");
		sql.append(" 	SELECT sum(t.amount) as money FROM proposal t where t.type = 502 and t.flag = 2 and t.loginname = :loginname and createTime > '2017-01-01'");
		sql.append(" ) a");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		List list = slaveDao.queryList(sql.toString(), params);
		if(list !=null && !list.isEmpty()){
			Double deposit = (Double) list.get(0);
			return deposit == null? 0.0 : deposit;
		}else{
			return 0.0;
		}
	}

	//查询到目前为止的总存款额
	@Override
	public Double getAllDeposit(String loginname) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT sum(a.money) as money FROM");
		sql.append(" (");
		sql.append(" 	SELECT sum(t.money) as money FROM payorder t where t.type = 0 and t.flag = 0 and t.loginname = :loginname  ");
		sql.append(" union all");
		sql.append(" 	SELECT sum(t.amount) as money FROM proposal t where t.type = 502 and t.flag = 2 and t.loginname = :loginname ");
		sql.append(" ) a");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loginname", loginname);
		List list = slaveDao.queryList(sql.toString(), params);
		if(list !=null && !list.isEmpty()){
			Double deposit = (Double) list.get(0);
			return deposit == null? 0.0 : deposit;
		}else{
			return 0.0;
		}
	}

	@Override
	public List getListBysql(String sql,Map<String, Object> params) {
		return slaveDao.queryListBySql(sql, params);
	}	
}
