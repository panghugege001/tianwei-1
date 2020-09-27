package dfh.service.implementations;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.dao.SlaveDao;
import dfh.service.interfaces.SlaveService;

public class SlaveServiceImpl implements SlaveService  {

	private static Logger log = Logger.getLogger(SlaveServiceImpl.class);

	private SlaveDao slaveDao;

	public SlaveDao getSlaveDao() {
		return slaveDao;
	}

	public void setSlaveDao(SlaveDao slaveDao) {
		this.slaveDao = slaveDao;
	}

	@Override
	public HibernateTemplate getHibernateTemplate() {
		return slaveDao.getHibernateTemplate();
	}

	@Override
	public Object get(Class clazz, Serializable id) {
		return slaveDao.get(clazz, id);
	}
	
	@Override
	public int getCountProxyFirst(String loginname,String countProxyFirst_start,String countProxyFirst_end){
		 Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
		 Query query = session.createSQLQuery("select countProxyFirst('"+loginname+"','"+countProxyFirst_start+"','"+countProxyFirst_end+"') from dual");
		 List list = query.list();
		return Integer.parseInt(list.get(0).toString());
	}
}
