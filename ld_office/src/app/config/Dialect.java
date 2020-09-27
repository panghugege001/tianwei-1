package app.config;

import org.hibernate.dialect.MySQLInnoDBDialect;
import java.sql.Types;

public class Dialect extends MySQLInnoDBDialect {

	public Dialect() {
	
		super();
		
		registerHibernateType(Types.LONGVARCHAR, 65535, "text");
	}
	
}
