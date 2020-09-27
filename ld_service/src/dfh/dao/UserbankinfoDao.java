package dfh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dfh.model.Userbankinfo;

public class UserbankinfoDao extends HibernateDaoSupport {
	
	public List<Userbankinfo> getUserbankinfoList(String loginname)throws Exception{
		Criteria c = this.getSession().createCriteria(Userbankinfo.class);
		c.add(Restrictions.eq("flag", 0)).add(Restrictions.eq("loginname", loginname));
		return c.list();
	}
	
	public void save(Userbankinfo bankinfo)throws Exception{
		this.getSession().save(bankinfo);
	}
	
	public Userbankinfo getUserbankinfo(String loginname,String bankname)throws Exception{
		Criteria c = this.getSession().createCriteria(Userbankinfo.class);
		c.add(Restrictions.eq("flag", 0)).add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("bankname", bankname));
		return (Userbankinfo) c.uniqueResult();
	}
	
	public List getList(String sql,String username,String bankname){
		SQLQuery query = this.getSession().createSQLQuery(sql);
		query.setParameter(0, username);
		query.setParameter(1, bankname);
		return query.list();
	}
	

}
