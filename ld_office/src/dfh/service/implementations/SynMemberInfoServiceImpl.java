package dfh.service.implementations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import bbs.client.Client;

import dfh.dao.UserDao;
import dfh.model.Users;
import dfh.service.interfaces.ISynMemberInfoService;
import dfh.utils.Configuration;
import dfh.utils.HttpClientHelper;
import dfh.utils.StringUtil;

public class SynMemberInfoServiceImpl implements ISynMemberInfoService {
	
	private UserDao userdao;

	public UserDao getUserdao() {
		return userdao;
	}

	public void setUserdao(UserDao userdao) {
		this.userdao = userdao;
	}

	@Override
	public Users getUserObject(String loginname) throws Exception {
		// TODO Auto-generated method stub
		return (Users) userdao.get(Users.class, loginname);
	}

	@Override
	public int synMemberInfo(String loginname, String password)
			throws Exception {
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
				String sqlT1 = "update pre_ucenter_members set password='" + password + "' where username='" + loginname + "'";
				stmt.executeUpdate(sqlT1);
				return 300;
			}
			// 同步注册用户信息
			Client uc = new Client();
			String $returns = uc.uc_user_register(loginname, "123456" ,"e68@e68.ph" );
			int $uid = Integer.parseInt($returns);
			if($uid <= 0) {
				return 100;
			} else {
				String sqlTwo = "update pre_ucenter_members set password='" + password + "' where username='" + loginname + "'";
				stmt.executeUpdate(sqlTwo);
				System.out.println("OK:"+$returns);
				return 200;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 100;
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
				return 100;
			}
		}
	}

}
