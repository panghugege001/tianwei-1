package dfh.dao;

import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SlaveHibernateDaoSupport extends HibernateDaoSupport {
    
    @Resource(name="sessionFactorySlave")   
    public void setSuperSessionFactory(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

}
