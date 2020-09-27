package dfh.service.interfaces;

import org.springframework.orm.hibernate3.HibernateTemplate;

public interface ProfitEaService {
	
	public HibernateTemplate getHibernateTemplate();
	
	public String synMemberInfo(String loginname, String password) throws Exception ;

}
