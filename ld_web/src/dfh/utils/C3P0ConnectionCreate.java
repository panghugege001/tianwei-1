package dfh.utils;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0ConnectionCreate implements C3P0IConnection {

	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://218.213.197.91:3306/e68email?characterEncoding=utf8";
	private static final String user = "e68email";
	private static final String password = "PZpIq8eya1Xsl4u";

	private static C3P0ConnectionCreate instance;
	private static ComboPooledDataSource ds;

	static {
		ds = new ComboPooledDataSource();
		try {
			Properties properties = new Properties();
			try {
				properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("init.properties"));
				ds.setUser(properties.getProperty("datasource.username"));
				ds.setPassword(properties.getProperty("datasource.password"));
				ds.setJdbcUrl(properties.getProperty("datasource.url"));
				ds.setDriverClass(properties.getProperty("datasource.driverClassName"));
			} catch (Exception e) {
				ds.setUser(user);
				ds.setPassword(password);
				ds.setJdbcUrl(url);
				ds.setDriverClass(driver);
			}
			// 初始化时获取三个连接，取值应在minPoolSize与maxPoolSize之间。Default: 3
			// initialPoolSize
			ds.setInitialPoolSize(3);
			// 连接池中保留的最大连接数。Default: 15 maxPoolSize
			ds.setMaxPoolSize(10);
			// // 连接池中保留的最小连接数。
			ds.setMinPoolSize(5);
			// 当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。Default: 3 acquireIncrement
			ds.setAcquireIncrement(2);

			// 每60秒检查所有连接池中的空闲连接。Default: 0 idleConnectionTestPeriod
			ds.setIdleConnectionTestPeriod(60);
			// 最大空闲时间,25000秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0 maxIdleTime
			ds.setMaxIdleTime(25000);
			// 连接关闭时默认将所有未提交的操作回滚。Default: false autoCommitOnClose
			ds.setAutoCommitOnClose(true);

		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	/**
	 * singleton
	 */
	private C3P0ConnectionCreate() {
	}

	public static synchronized C3P0ConnectionCreate getInstance() {
		if (instance == null) {
			instance = new C3P0ConnectionCreate();
		}
		return instance;
	}

	public synchronized Connection getConn() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
