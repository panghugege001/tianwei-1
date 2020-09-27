package com.gsmc.png.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.gsmc.png.dao.IGetdataDao;
import com.gsmc.png.model.PlatformData;
import com.gsmc.png.model.enums.GamePlatform;
import com.gsmc.png.utils.DateUtil;

public class GetdataDao extends HibernateDaoSupport implements IGetdataDao {

	private static Logger log = Logger.getLogger(GetdataDao.class);
	
	
	@Override
	public List findByCriteria(DetachedCriteria criteria) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public Object save(Object o) {
		// TODO Auto-generated method stub
		return getHibernateTemplate().save(o);
	}

	@Override
	public void saveOrUpdate(Object o) {
		getHibernateTemplate().saveOrUpdate(o);

	}

	@Override
	public void update(Object o) {
		getHibernateTemplate().update(o);

	}

	@Override
	public Object get(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	@Override
	public Object get(Class clazz, Serializable id, LockMode mode) {
		return getHibernateTemplate().get(clazz, id, mode);
	}

	@Override
	public int discardWxZzMcQuota() {
		return 0;
	}

	@Override
	public int discardWxZzMcOrder() {
		return 0;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean updateTTGPlatForm(PlatformData data) {
		String platFormName = GamePlatform.TTG.name(); 
		//log.info(platFormName + "更新数据："+data.getLoginname());
		String querysql = "SELECT * FROM platform_data where platform='" + platFormName + "' and starttime='"+DateUtil.formatDateForStandard(data.getStarttime())+"' and loginname='"+data.getLoginname()+"'" ;
		Query query = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(querysql);
		List ipList = query.list();
		int flag = -1 ;
		if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
			Object[] objects0 = (Object[]) ipList.get(0);
			String uuid = objects0[0].toString();
			String updatesql = "UPDATE platform_data set bet=? , profit=? , updatetime=? where uuid=?" ;
			Query updateQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(updatesql);
			updateQuery.setParameter(0, data.getBet()).setParameter(1, data.getProfit()).setParameter(2, DateUtil.fmtyyyyMMddHHmmss(data.getUpdatetime())).setParameter(3, uuid) ;
			flag = updateQuery.executeUpdate() ;
			if(ipList.size()>1){
				String delsql = "DELETE FROM platform_data where platform='" + platFormName + "' and starttime='"+DateUtil.formatDateForStandard(data.getStarttime())+"' and loginname='"+data.getLoginname()+"' and uuid!=?";
				Query delQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(delsql);
				delQuery.setParameter(0, uuid);
				flag = delQuery.executeUpdate();
			}
		}else {
			String insertSql = "INSERT INTO platform_data( uuid , platform , loginname , bet , profit , starttime , endtime , updatetime) values(?,?,?,?,?,?,?,?)" ;
			Query insertQuery = this.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(insertSql);
			insertQuery.setParameter(0, data.getUuid()).setParameter(1, data.getPlatform())
						.setParameter(2, data.getLoginname()).setParameter(3, data.getBet())
						.setParameter(4, data.getProfit()).setParameter(5, data.getStarttime())
						.setParameter(6, data.getEndtime()).setParameter(7, DateUtil.fmtyyyyMMddHHmmss(data.getUpdatetime())) ;
			flag = insertQuery.executeUpdate() ;
		}
		return flag == 1;
	}
	
	@Override
	public boolean updateQtPlatForm(PlatformData data) {
		log.info("qt更新数据："+data.getLoginname());
		String querysql = "SELECT * FROM platform_data where platform='qt' and starttime='"+DateUtil.formatDateForStandard(data.getStarttime())+"' and loginname='"+data.getLoginname()+"'" ;
		Query query = this.getSession().createSQLQuery(querysql);
		List ipList = query.list();
		int flag = -1 ;
		if (ipList != null && ipList.size() > 0 && ipList.get(0) != null) {
			Object[] objects0 = (Object[]) ipList.get(0);
			String uuid = objects0[0].toString();
			String updatesql = "UPDATE platform_data set bet=? , profit=? , updatetime=? where uuid=?" ;
			Query updateQuery = this.getSession().createSQLQuery(updatesql);
			updateQuery.setParameter(0, data.getBet()).setParameter(1, data.getProfit()).setParameter(2, DateUtil.fmtyyyyMMddHHmmss(new Date())).setParameter(3, uuid) ;
			flag = updateQuery.executeUpdate() ;
			if(ipList.size()>1){
				String delsql = "DELETE FROM platform_data where platform='qt' and starttime='"+DateUtil.formatDateForStandard(data.getStarttime())+"' and loginname='"+data.getLoginname()+"' and uuid!=?";
				Query delQuery = this.getSession().createSQLQuery(delsql);
				delQuery.setParameter(0, uuid);
				flag = delQuery.executeUpdate();
			}
		}else {
			String insertSql = "INSERT INTO platform_data( uuid , platform , loginname , bet , profit , starttime , endtime , updatetime) values(?,?,?,?,?,?,?,?)" ;
			Query insertQuery = this.getSession().createSQLQuery(insertSql);
			insertQuery.setParameter(0, data.getUuid()).setParameter(1, data.getPlatform())
						.setParameter(2, data.getLoginname()).setParameter(3, data.getBet())
						.setParameter(4, data.getProfit()).setParameter(5, data.getStarttime())
						.setParameter(6, data.getEndtime()).setParameter(7, DateUtil.fmtyyyyMMddHHmmss(new Date())) ;
			flag = insertQuery.executeUpdate() ;
		}
		return flag==1?true:false;
	}
	
	public List<PlatformData> selectQtData(Date startT, Date endT){
		String querysql = "SELECT playerid, sum(bet), sum(payout) FROM qtdata where status='COMPLETED' and completed >='"+DateUtil.formatDateForStandard(startT)+"' and completed <='"+DateUtil.formatDateForStandard(endT)+"' group by playerid";
		Query query = this.getSession().createSQLQuery(querysql);
		List ipList = query.list();
		List<PlatformData> list = new ArrayList<PlatformData>();
		if (ipList != null && ipList.size() > 0) {
			for(int i=0; i<ipList.size(); i++){
				Object[] objects = (Object[]) ipList.get(i);
				Double realBet = Double.valueOf(objects[1].toString());
				Double payout = Double.valueOf(objects[2].toString());
				PlatformData bean = new PlatformData();
				bean.setLoginname(objects[0].toString());
				bean.setBet(realBet);
				bean.setProfit(realBet-payout);
				list.add(bean);
			}
		}
		return list;
	}
	
}