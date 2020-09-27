package dfh.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;

public class MGSDao extends MGSHibernateDaoSupport{

	public List<?> getListBySql(String sql, Map<String, Object> params){
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.list();
	}
	
	public Object getOneObject(String sql, Map<String, Object> params) {
		Query query = this.getSession().createSQLQuery(sql);
		query.setProperties(params);
		return query.uniqueResult();
	}

}
