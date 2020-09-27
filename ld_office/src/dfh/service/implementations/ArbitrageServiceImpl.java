package dfh.service.implementations;

import dfh.dao.ArbitrageDao;
import dfh.service.interfaces.ArbitrageService;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.Serializable;
import java.util.List;

public class ArbitrageServiceImpl implements ArbitrageService {
	
	//private static Logger log = Logger.getLogger(ArbitrageServiceImpl.class);

	private ArbitrageDao arbitrageDao;
	@Override
	public Object save(Object o) {
		return arbitrageDao.save(o);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Object get(Class clazz, Serializable id) {
		return arbitrageDao.get(clazz, id);
	}

	@Override
	public void saveOrUpdate(Object o) {
		arbitrageDao.saveOrUpdate(o);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void delete(Class clazz, Serializable id) {
		arbitrageDao.delete(clazz, id);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria criteria) {
		return arbitrageDao.findByCriteria(criteria);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List findByCriteria(DetachedCriteria criteria, Integer firstResult,
                               Integer maxResults) {
		return arbitrageDao.findByCriteria(criteria, firstResult, maxResults);
	}

	@Override
	public HibernateTemplate getHibernateTemplate() {
		return arbitrageDao.getHibernateTemplate();
	}
	
	public ArbitrageDao getArbitrageDao() {
		return arbitrageDao;
	}

	public void setArbitrageDao(ArbitrageDao arbitrageDao) {
		this.arbitrageDao = arbitrageDao;
	}

	
}
