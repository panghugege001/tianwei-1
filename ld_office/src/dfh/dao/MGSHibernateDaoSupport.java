package dfh.dao;

import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class MGSHibernateDaoSupport extends HibernateDaoSupport {
    
    @Resource(name="sessionFactoryMGS")   
    public void setSuperSessionFactory(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

}
