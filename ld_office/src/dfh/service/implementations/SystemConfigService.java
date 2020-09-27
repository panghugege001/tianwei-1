package dfh.service.implementations;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import dfh.dao.BankinfoDao;
import dfh.dao.LogDao;
import dfh.dao.UniversalDao;
import dfh.model.Bankinfo;
import dfh.model.Const;
import dfh.model.PayMerchant;
import dfh.model.SystemConfig;
import dfh.model.enums.OperationLogType;
import dfh.service.interfaces.IBankinfoService;
import dfh.service.interfaces.ISystemConfigService;

@Service
public class SystemConfigService extends UniversalServiceImpl implements ISystemConfigService {

	private UniversalDao universalDao;

	
	@Override
	public String updateSystemConfig(SystemConfig payMer) {
		universalDao.saveOrUpdate(payMer);
		return null;
	}
	@Override
	public String saveSystemConfig(SystemConfig payMer) {
		String a=universalDao.save(payMer)+"";
		return a;
	}
	@Override
	public void deleteSystemConfig(Class a,String id) {
		universalDao.delete(a, id);
	}
	public UniversalDao getUniversalDao() {
		return universalDao;
	}
	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}
	
	@Override
	public HibernateTemplate getHibernateTemplate() {
		return universalDao.getHibernateTemplate();
	}
	
	@Override
	public Object get(Class clazz, Serializable id){
		return universalDao.get(clazz, id+"");
	}

}
