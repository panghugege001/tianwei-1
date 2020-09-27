package dfh.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class AgentDao extends HibernateDaoSupport{
	
	public Integer getOneInteger(String sql){
		BigInteger oneInteger = (BigInteger)this.getSession().createSQLQuery(sql).uniqueResult();
		return oneInteger.intValue();
	}
	
	public Integer getTwoInteger(String sql){
		List list = this.getSession().createSQLQuery(sql).list();
		return (((BigInteger)list.get(0)).intValue()+((BigInteger)list.get(1)).intValue());
	}
	
	public Double getOneDouble(String sql){
		Double doublenumber = (Double)this.getSession().createSQLQuery(sql).uniqueResult();
		if(doublenumber ==null)
			return 0.0;
		return doublenumber;
	}
	
	public Double getTwoDouble(String sql){
		List list = this.getSession().createSQLQuery(sql).list();
		Double double1 = (Double)list.get(0);
		if(double1 == null){
			double1 = 0.0;
		}
		Double double2 = (Double)list.get(1);
		if(double2 == null){
			double2 = 0.0;
		}
		return double1+double2;
	}
	
	public List getList(String sql,int offset,int length){
		List list = this.getSession().createSQLQuery(sql).setFirstResult(offset).setMaxResults(length).list();
		return list; 
	}
}
