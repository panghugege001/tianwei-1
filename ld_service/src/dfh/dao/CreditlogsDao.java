package dfh.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dfh.model.Creditlogs;

public class CreditlogsDao extends HibernateDaoSupport {
	
	
	public List<Creditlogs> searchCreditlogs(Date starttime,Date endtime,String loginname,int offset,int length){
		Criteria c = this.getSession().createCriteria(Creditlogs.class);
		c.add(Restrictions.ge("createtime", starttime)).add(Restrictions.lt("createtime", endtime)).add(Restrictions.eq("loginname", loginname));
		c.setFirstResult(offset).setMaxResults(length);
		return c.list();
	}
	
	public int totalCreditlogs(Date starttime,Date endtime,String loginname){
		Criteria c = this.getSession().createCriteria(Creditlogs.class);
		c.setProjection(Projections.count("id"));
		c.add(Restrictions.ge("createtime", starttime)).add(Restrictions.lt("createtime", endtime)).add(Restrictions.eq("loginname", loginname));
		return ((Integer) c.uniqueResult()).intValue();
	}

}
