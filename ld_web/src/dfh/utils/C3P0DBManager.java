package dfh.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class C3P0DBManager {

	private Connection conn;
	private Statement stmt;
	private ResultSet rs;

	// 使用c3p0做链接池
	private C3P0IConnection conCreator = C3P0ConnectionCreate.getInstance();

	public Connection getConn() throws SQLException {
		if (conn == null || conn.isClosed()) {
			conn = conCreator.getConn();
		}
		return conn;
	}

	public Statement getStmt() throws SQLException {
		if (stmt == null) {
			stmt = getConn().createStatement();
		}
		return stmt;
	}

	public PreparedStatement getPstmt(String sql) throws SQLException {
		return getConn().prepareStatement(sql);
	}

	public PreparedStatement getPstmtWithParam(String sql, List<?> paramList)
			throws SQLException {
		PreparedStatement pstmt = getConn().prepareStatement(sql);
		for (int i = 0; i < paramList.size(); i++) {
			pstmt.setObject(i + 1, paramList.get(i));
		}
		return pstmt;
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		rs = getStmt().executeQuery(sql);
		return rs;
	}

	public int executeUpdate(String sql) throws SQLException {
		return getStmt().executeUpdate(sql);
	}

	public void close() {
		try {
			if(rs!=null){
				rs.close();
			}
			if(stmt!=null){
				stmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception e) {
		} finally {
			conn = null;
			stmt = null;
			rs = null;
		}
	}
}
