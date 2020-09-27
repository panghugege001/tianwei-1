package dfh.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class DBUtil {
	private Logger logger = Logger.getLogger(DBUtil.class);
	public Connection getConnectionJDBC(){
		DBProperty pro = new DBProperty();
		Connection conn = null;
		try{
			Class.forName(pro.getClassName()).newInstance();
			String url = pro.getUrl();
			String user = pro.getName();
			String password = pro.getPassword();
			conn = DriverManager.getConnection(url,user,password);
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	public ArrayList<HashMap> selectPrepare(Connection conn, String sql,
			Object[] obj) throws Exception {
		ArrayList<HashMap> result = new ArrayList<HashMap>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = conn.prepareStatement(sql);
		if (obj != null && obj.length > 0) {
			for (int j = 0; j < obj.length; j++) {
				if (obj[j] != null) {
					if (obj[j] instanceof String) {
						ps.setString(j + 1, obj[j].toString());
					} else if (obj[j] instanceof Integer) {
						ps.setInt(j + 1, Integer.parseInt(obj[j].toString()));
					} else if (obj[j] instanceof java.util.Date) {
						ps.setTimestamp(j + 1, new Timestamp(
								((java.util.Date) obj[j]).getTime()));
					}
				}
			}
		}
		rs = ps.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int cols = rsmd.getColumnCount();
		int i = 0;
		while (rs.next() != false) {
			HashMap map = new HashMap();
			for (i = 1; i <= cols; ++i) {
				if (rs.getString(i) != null) {
					String colName = rsmd.getColumnName(i);
					if (java.sql.Types.INTEGER == rsmd.getColumnType(i)) {
						map.put(colName, rs.getInt(colName));
					} else if (java.sql.Types.DATE == rsmd.getColumnType(i)) {
						map.put(colName, rs.getDate(colName));
					} else {
						map.put(colName, rs.getString(colName));
					}
				}
			}
			result.add(map);
		}
		if (rs != null) {
			rs.close();
		}
		if (ps != null) {
			ps.close();
		}
		return result;
	}

	public void insertParam(Connection conn, String sql, Object[] obj)
			throws Exception {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; obj != null && i < obj.length; i++) {
				if (obj[i] instanceof String) {
					ps.setString(i + 1, (String) obj[i]);
				} else if (obj[i] instanceof java.util.Date) {
					ps.setTimestamp(i + 1, new Timestamp(
							((java.util.Date) obj[i]).getTime()));
				}
			}
			ps.executeUpdate();
		} catch (Exception sqle) {
			sqle.printStackTrace();
			logger.error("[" + DBUtil.class.getName() + "]" + sqle);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public void updateParam(Connection conn, String sql, Object[] obj) throws Exception {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(sql);
			for (int i = 0; obj != null && i < obj.length; i++) {
				if (obj[i] instanceof String) {
					ps.setString(i + 1, (String) obj[i]);
				} else if (obj[i] instanceof java.util.Date) {
					ps.setTimestamp(i + 1, new Timestamp(
							((java.util.Date) obj[i]).getTime()));
				}
			}
			ps.executeUpdate();
		} catch (Exception sqle) {
			sqle.printStackTrace();
			logger.error("[" + DBUtil.class.getName() + "]" + sqle);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}
	private static class SingleProcess {
		private static DBUtil dbUtil = new DBUtil();
	}

	public static DBUtil getInstanceDBUtil() {
		return SingleProcess.dbUtil;
	}
}
