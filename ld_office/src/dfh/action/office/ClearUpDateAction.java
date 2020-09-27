package dfh.action.office;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import com.ibm.icu.text.SimpleDateFormat;

import dfh.action.SubActionSupport;

public class ClearUpDateAction extends SubActionSupport {

	private static final long serialVersionUID = 1L;

	private Date endTime;

	/**
	 * 自动清理数据
	 * 
	 * @return
	 */
	public String clearUpDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -6);
		endTime = calendar.getTime();
		return INPUT;
	}

	/**
	 * 获取数据库连接
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getPhpPtService(String key) throws Exception {
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("init.properties"));
		return properties.getProperty(key);
	}

	/**
	 * 清理用户数据
	 * 
	 * @return
	 */
	public String submitClearUserAction() {
		Connection conn = null;
		Statement stmt = null;
		try {
			// 获取6月份之前的
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -6);
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
			System.out.println("处理时间:" + time.format(calendar.getTime()) + "");
			// 连接数据库
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			String mysqlUrl = getPhpPtService("datasource.url");
			String mysqlname = getPhpPtService("datasource.username");
			String mysqlPassword = getPhpPtService("datasource.password");
			conn = DriverManager.getConnection(mysqlUrl, mysqlname, mysqlPassword);
			System.out.println("连接MySql成功!!!");
			String vpi = "";
			Integer d=0;
			Integer d1=0;
			for (int i = 0; i <= 8; i++) {
				if (i == 0) {
					vpi = "天兵";
				} else if (i == 1) {
					vpi = "天将";
				} else if (i == 2) {
					vpi = "天王";
				} else if (i == 3) {
					vpi = "星君";
				} else if (i == 4) {
					vpi = "真君";
				} else if (i == 5) {
					vpi = "仙君";
				} else if (i == 6) {
					vpi = "帝君";
				} else if (i == 7) {
					vpi = "天尊";
				} else if (i == 8) {
					vpi = "天帝";
				}
				// 查询用户表数据
				String sqlUser = "SELECT loginname from users where role='MONEY_CUSTOMER' and level='" + i + "' and createtime < '" + time.format(calendar.getTime()) + "'";
				PreparedStatement pstmt = conn.prepareStatement(sqlUser);
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					String loginname = rs.getString("loginname");
					d++;
					System.out.println("******************第"+d+"条****************"+loginname +"******"+time.format(calendar.getTime()));
					// 判断是否已经存在
					Boolean t1 = false;
					// 查询提案表
					String sqlproposal = "SELECT count(*) from proposal where loginname='" + loginname + "'";
					PreparedStatement pstmtproposal = conn.prepareStatement(sqlproposal);
					ResultSet rsproposal = pstmtproposal.executeQuery();
					while (rsproposal.next()) {
						Integer countProposa = Integer.parseInt(rsproposal.getString(1));
						if (countProposa > 0) {
							t1=true;
						}
					}
					// 如果是true 就删除该玩家
					if(t1){
						System.out.println("******************处理提案有数据****************"+loginname+"******"+vpi);
						continue;
					}
					if (pstmtproposal != null) {
						pstmtproposal.close();
						pstmtproposal = null;
					}
					if (rsproposal != null) {
						rsproposal.close();
						rsproposal = null;
					}
					// 查询在线支付表
					t1 = false;
					String sqlpayorder = "SELECT count(*) from payorder where loginname='" + loginname + "'";
					PreparedStatement pstmtpayorder = conn.prepareStatement(sqlpayorder);
					ResultSet rspayorder = pstmtpayorder.executeQuery();
					while (rspayorder.next()) {
						Integer countpayorder = Integer.parseInt(rspayorder.getString(1));
						if (countpayorder > 0) {
							t1=true;
						}
					}
					// 如果是true 就删除该玩家
					if(t1){
						System.out.println("******************处理在线支付有数据****************"+loginname+"******"+vpi);
						continue;
					}
					if (rspayorder != null) {
						rspayorder.close();
						rspayorder = null;
					}
					if (pstmtpayorder != null) {
						pstmtpayorder.close();
						pstmtpayorder = null;
					}
					// 查询额度记录
					t1 = false;
					String sqlcreditlogs = "SELECT count(*) from creditlogs where loginname='" + loginname + "'";
					PreparedStatement pstmtcreditlogs = conn.prepareStatement(sqlcreditlogs);
					ResultSet rscreditlogs = pstmtcreditlogs.executeQuery();
					while (rscreditlogs.next()) {
						Integer countcreditlogs = Integer.parseInt(rscreditlogs.getString(1));
						if (countcreditlogs > 0) {
							t1=true;
						}
					}
					// 如果是true 就删除该玩家
					if(t1){
						System.out.println("******************处理额度记录有数据****************"+loginname+"******"+vpi);
						continue;
					}
					
				    deleteUser(conn, stmt, loginname, vpi);
					d1++;
					System.out.println("******************处理第"+d1+"条****************"+loginname);
					
					if (rscreditlogs != null) {
						rscreditlogs.close();
						rscreditlogs = null;
					}
					if (pstmtcreditlogs != null) {
						pstmtcreditlogs.close();
						pstmtcreditlogs = null;
					}
				}
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void deleteUser(Connection conn, Statement stmt, String loginname, String vpi) {
		try {
			String sqlInsert = "insert into users_backup select * from users where loginname='" + loginname + "'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlInsert);
			System.out.println(vpi + " 玩家:  " + loginname + " 没有数据! " + sqlInsert);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			String sqlInsertuserbankinfo = "insert into userbankinfo_backup select * from userbankinfo where loginname='" + loginname + "'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlInsertuserbankinfo);
			System.out.println(vpi + " 玩家:  " + loginname + " 没有数据! " + sqlInsertuserbankinfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			String sqlInsertuserstatus = "insert into userstatus_backup select * from userstatus where loginname='" + loginname + "'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlInsertuserstatus);
			System.out.println(vpi + " 玩家:  " + loginname + " 没有数据! " + sqlInsertuserstatus);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			String sqlDelete = "delete from users where loginname='" + loginname + "'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlDelete);
			System.out.println(vpi + " 玩家:  " + loginname + " 没有数据! " + sqlDelete);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			String sqldeleteuserbankinfo = "delete from userbankinfo where loginname='" + loginname + "'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sqldeleteuserbankinfo);
			System.out.println(vpi + " 玩家:  " + loginname + " 没有数据! " + sqldeleteuserbankinfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			String sqldeleteuserstatus = "delete from userstatus where loginname='" + loginname + "'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sqldeleteuserstatus);
			System.out.println(vpi + " 玩家:  " + loginname + " 没有数据! " + sqldeleteuserstatus);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			String sqldeleteuserstatus = "delete from iesnare where loginname='" + loginname + "'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sqldeleteuserstatus);
			System.out.println(vpi + " 玩家:  " + loginname + " 没有数据! " + sqldeleteuserstatus);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

	public static void main(String[] args) {
		ClearUpDateAction action = new ClearUpDateAction();
		action.submitClearUserAction();
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
