package app.dao.support;

import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class QueryHibernateDaoSupport extends HibernateDaoSupport {

	@Resource(name = "sessionFactorySlave")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		
		super.setSessionFactory(sessionFactory);
	}
}