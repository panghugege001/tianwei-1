package dfh.utils;

import java.util.Date;

import dfh.dao.UniversalDao;

public class DatabaseNow extends UniversalDao {
	
	public Date getDatabaseNow() {
		return new Date();
		/*Date date = (Date) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("select now()");
				return sqlQuery.uniqueResult();
			}
		});
		if (date != null) {
			return date;
		}
		return null;*/
	}

}
