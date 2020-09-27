package dfh.dao;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import javax.annotation.Resource;

public class ArbitrageHibernateDaoSupport extends HibernateDaoSupport {
    
    @Resource(name="sessionFactoryArbitrage")
    public void setSuperSessionFactory(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

}
