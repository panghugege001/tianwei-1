package dfh.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dfh.model.Userbankinfo;

public class UserbankinfoDao extends HibernateDaoSupport {
	
	
	public List<Userbankinfo> getUserbankinfoList(Userbankinfo userbankinfo,int offset,int length)throws Exception{
		Criteria c = this.getSession().createCriteria(Userbankinfo.class);
		c.add(Example.create(userbankinfo).excludeNone()).setFirstResult(offset).setMaxResults(length);
		return c.list();
	}
	
	public List<Userbankinfo> getUserbankinfoList(Userbankinfo userbankinfo)throws Exception{
		Criteria c = this.getSession().createCriteria(Userbankinfo.class);
		c.add(Example.create(userbankinfo).excludeZeroes());
		return c.list();
	}
	
	public List<Userbankinfo> getUserbankinfoList(String hql,int offset,int length)throws Exception{

		return this.getSession().createQuery(hql).setFirstResult(offset).setMaxResults(length).list();
	}
	
	public List<Userbankinfo> getUserbankinfoList(String hql)throws Exception{

		return this.getSession().createQuery(hql).list();
	}
	
	public boolean update(int id)throws Exception{
		Query q = this.getSession().createQuery("update Userbankinfo set flag=1 where id=?");
		q.setParameter(0, id);
		return q.executeUpdate()>=1?true:false;
	}
	
	public Object get(Class clazz, Serializable id) {
		return getHibernateTemplate().get(clazz, id);
	}

	public Object save(Object o) {
		return getHibernateTemplate().save(o);
	}

}
