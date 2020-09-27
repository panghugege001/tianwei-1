package dfh.dao;

import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ProfitHibernateDaoSupport extends HibernateDaoSupport {
    
    @Resource(name="sessionFactoryProfit")   
    public void setSuperSessionFactory(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);
    }

}
