package dfh.utils;

import java.sql.SQLException;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import dfh.dao.UniversalDao;

public class DatabaseNow extends UniversalDao {
	
	public Date getDatabaseNow() {
		Date date = (Date) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery sqlQuery = session.createSQLQuery("select now()");
				return sqlQuery.uniqueResult();
			}
		});
		if (date != null) {
			return date;
		}
		return null;
	}

}
