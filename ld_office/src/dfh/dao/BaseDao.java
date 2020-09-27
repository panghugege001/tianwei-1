package dfh.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BaseDao<T,K extends Serializable> extends HibernateDaoSupport {

	public void saveAll(Collection<T> entities)throws Exception{
		this.getHibernateTemplate().saveOrUpdateAll(entities);
	}
	
	public void deleteAll(Collection<T> entities)throws Exception{
		this.getHibernateTemplate().deleteAll(entities);
	}
	
	public T saveorupdate(T t)throws Exception{
		this.getHibernateTemplate().saveOrUpdate(t);
		return t;
	}
	
	public T loadEntity(Class<T> _class,K k)throws Exception{
		return (T) this.getHibernateTemplate().load(_class, k);
	}
	
	public T getEntity(Class<T> _class,K k)throws Exception{
		return (T) this.getHibernateTemplate().get(_class, k);
	}
	
	public List<T> findEntity(String sql)throws Exception{
		return this.getHibernateTemplate().find(sql);
	}
	
	public List<T> findEntity(String sql,Object o)throws Exception{
		return this.getHibernateTemplate().find(sql, o);
	}
	
	public List<Object> findEntity(String sql,Object[] o)throws Exception{
		return this.getHibernateTemplate().find(sql, o);
	}
	
	public List<Object> findEntity(String sql,int offset,int length)throws Exception{
		return this.getSession().createQuery(sql).setFirstResult(offset).setMaxResults(length).list();
	}
	
}
