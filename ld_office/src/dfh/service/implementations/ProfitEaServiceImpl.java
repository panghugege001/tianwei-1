package dfh.service.implementations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;

import dfh.dao.ProfitEaDao;
import dfh.service.interfaces.ProfitEaService;
import dfh.utils.Configuration;


public class ProfitEaServiceImpl implements ProfitEaService{
	
	public ProfitEaDao profitEaDao;
	
	public HibernateTemplate getHibernateTemplate() {
		return profitEaDao.getHibernateTemplateDate();
	}

	public ProfitEaDao getProfitEaDao() {
		return profitEaDao;
	}

	public void setProfitEaDao(ProfitEaDao profitEaDao) {
		this.profitEaDao = profitEaDao;
	}
	
	@Override
	public String synMemberInfo(String loginname, String password) throws Exception {

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
			// 判断用户是否已经同步过了
			String sql = "select username from pre_ucenter_members where username='" + loginname + "'";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String sqlTwo1 = "update pre_ucenter_members set password='" + password + "' where username='" + loginname + "'";
				stmt.executeUpdate(sqlTwo1);
				return "成功更新BBS密码";
			}
			String sqlTwo = "insert into pre_ucenter_members(username , password , regdate , salt) values('"+loginname+"' , '"+password+"' , "+new Long((new Date().getTime()/1000))+" , '"+RandomStringUtils.random(6, true, true).toLowerCase()+"' )";
			stmt.executeUpdate(sqlTwo);
			return "更新成功" ;
		} catch (Exception e) {
			e.printStackTrace();
			return "更新出现异常："+e.getMessage();
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
				return "更新出现异常："+e.getMessage();
			}
		}
	}

}
