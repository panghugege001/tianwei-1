package dfh.dao;

import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BLHibernateDaoSupport extends HibernateDaoSupport {
    
    @Resource(name="sessionFactoryBL")
    public void setSuperSessionFactory(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

}
