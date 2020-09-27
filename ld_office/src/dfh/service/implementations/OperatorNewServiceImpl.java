package dfh.service.implementations;

import java.io.Serializable;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.nnti.office.model.auth.Operator;

import dfh.dao.UniversalDao;
import dfh.service.interfaces.IOperatorNewService;

@Service
public class OperatorNewServiceImpl extends UniversalServiceImpl implements IOperatorNewService {

	private UniversalDao universalDao;

	
	@Override
	public String updateSystemConfig(Operator operator) {
		universalDao.saveOrUpdate(operator);
		return null;
	}
	@Override
	public String saveSystemConfig(Operator operator) {
		String a=universalDao.save(operator)+"";
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
