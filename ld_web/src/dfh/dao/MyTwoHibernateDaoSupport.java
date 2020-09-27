package dfh.dao;

import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class MyTwoHibernateDaoSupport extends HibernateDaoSupport {
    
    @Resource(name="sessionFactoryEmail")   
    public void setSuperSessionFactory(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

}
