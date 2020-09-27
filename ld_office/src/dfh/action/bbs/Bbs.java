package dfh.action.bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import dfh.security.EncryptionUtil;
import dfh.utils.Configuration;

public class Bbs extends Thread{
	
	String loginname;
	String password;
	
	public Bbs(String loginname,String password){
		super();
		this.loginname=loginname;
		this.password=password;
	}
	
	public void run(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			String databasesIp = Configuration.getInstance().getValue("bbs_ip");
			String databasesPort = Configuration.getInstance().getValue("bbs_port");
			String databases = Configuration.getInstance().getValue("bbs_databases");
			String dbuser = Configuration.getInstance().getValue("bbs_user");
			String dbpassword = Configuration.getInstance().getValue("bbs_password");
			String usrl = "jdbc:mysql://" + databasesIp + ":" + databasesPort + "/" + databases + "?useUnicode=true&characterEncoding=UTF-8";
			conn = DriverManager.getConnection(usrl, dbuser, dbpassword);
			String sqlTwo = "update pre_ucenter_members set password='" + EncryptionUtil.encryptPassword(password) + "' where username='" + loginname + "'";
			stmt = conn.prepareStatement(sqlTwo);
			stmt.executeUpdate(sqlTwo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
