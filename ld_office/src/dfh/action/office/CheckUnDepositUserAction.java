package dfh.action.office;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.ibm.icu.text.SimpleDateFormat;

import dfh.action.SubActionSupport;
import dfh.model.Bankinfo;
import dfh.model.SystemConfig;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.utils.AESUtil;
import dfh.utils.StringUtil;
import dfh.utils.sendemail.SendEmailWsByEdm;

/**
 * 查询一个月以内没有存款的玩家
 * 
 * @author jat
 * 
 */
public class CheckUnDepositUserAction extends SubActionSupport {

	private static final long serialVersionUID = 1L;

	// private String start;

	private String DeEndDate;

	private String beginTime;

	private String endTime;

	private String cs;

	// private Date end;

	/**
	 * 当前时间减去一个月
	 * 
	 * @return
	 */
	public String clearUpDate() {
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();   
		calendar.add(Calendar.MONTH, -1);
		String endTime = time.format(calendar.getTime());
		return "2017-09-03";
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
		properties.load(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("init.properties"));
		return properties.getProperty(key);
	}

	//查询代理
	public static String[] getAgentList(Connection conn, Statement stmt) throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String value=null;
		StringBuffer sbf = new StringBuffer("select  * from  systemconfig where typeNo='type101' ");
		pstmt = conn.prepareStatement(sbf.toString());
		rs = pstmt.executeQuery();
		while (rs.next()) {
			 value = rs.getString("value");
	     }
		String [] strArray = null;
		strArray = value.split(";");
		return strArray;
		
	}
	
	
	/**
	 * 插入数据并更新玩家所属客服
	 * 
	 * @return
	 */
	public String updateotherCustomAction() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		Connection conn = null;
		Statement stmt = null;
		String start = "2017-07-03";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		PreparedStatement pstmtproposal = null;
		ResultSet rsproposal = null;
		/*
		 * SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd"); Calendar
		 * calendar = Calendar.getInstance(); calendar.set(2015, 5, 10);
		 * start=time.format(calendar.getTime());
		 */
		try {
			out = response.getWriter();
			// 获取6月份之前的
			System.out.println("处理时间开始：" + start + ";处理时间至:" + clearUpDate()
					+ "");
			// 连接数据库
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			String mysqlUrl = decodeString(getPhpPtService("datasource.url"));
			String mysqlname = decodeString(getPhpPtService("datasource.username"));
			String mysqlPassword = decodeString(getPhpPtService("datasource.password"));
			conn = DriverManager.getConnection(mysqlUrl, mysqlname,
					mysqlPassword);
			System.out.println("连接MySql成功!!!");
			String endDate = clearUpDate();
			// 查询玩家在2015年至 一个月之前有存款
			StringBuffer sbf = new StringBuffer(
					"select DISTINCT a.loginname,u.accountName,u.phone,u.email,u.flag,u.qq,u.intro,u.agent  from ( ");
			sbf.append("SELECT	DISTINCT(p4.loginname) as loginname	FROM	payorder p4 ");
			sbf.append("where p4.type='0' and	p4.createTime > '");
			sbf.append(start);
			sbf.append("' AND p4.createTime < '");
			sbf.append(endDate);
			sbf.append("'  and p4.loginname not LIKE '%->%' union ALL  ");
			sbf.append("SELECT	DISTINCT(p1.loginname) as loginname FROM proposal p1 WHERE ");
			sbf.append("p1.createTime > '");
			sbf.append(start);
			sbf.append("' AND p1.createTime < '");
			sbf.append(endDate);
			sbf.append("' and p1.loginname not LIKE '%->%'  and p1.type='502' and p1.flag='2' ");
			sbf.append(") a left join users u on a.loginname=u.loginname  ");
			System.out.println("sbf===" + sbf);
			pstmt = conn.prepareStatement(sbf.toString());
			rs = pstmt.executeQuery();
			int d = 0;
			boolean t1 = false;
			String cs = "ts12";
			while (rs.next()) {
				d++;
				String loginname = rs.getString("loginname");
				String phone = rs.getString("phone");
				String email = rs.getString("email");
				String accountName = rs.getString("accountName");
				String flag = rs.getString("flag");
				String qq = rs.getString("qq");
				String intro = rs.getString("intro");
				String agent = rs.getString("agent");
				if (!StringUtils.isEmpty(flag) && flag.equals("1")) {
					continue;
				}
				/*String strs[] = { "a_e68cs1", "a_e68cs2", "a_e68cs3",
						"a_e68cs4", "a_e68cs5", "a_kiki", "a_lode", "a_test05",
						"a_test012", "a_test013", "a_judy", "a_jennies",
						"a_zoe01", "a_eva01", "a_kiki", "a_kelly", "a_doris",
						"a_tina", "a_rachel", "a_helen", "a_yoyo", "a_judy02",
						"a_jennies02", "a_doris02", "a_kelly02", "a_rachel02",
						"a_helen02", "a_tina02", "a_alice", "a_miya",
						"a_tina01", "a_haifa", "a_dorischang", "a_haifa",
						"a_monica", "a_kacy","a_asa88","a_abeni","a_linda","a_salma" };*/
				
				
				//通过方法查询代理
				String strs[] = getAgentList(conn,stmt );
				boolean bo = Arrays.asList(strs).contains(agent);
				if (!bo) {
					System.out
							.println("******************第"
									+ d
									+ "条开始-----------------------------****************"
									+ loginname + "******");
					// 查询当前玩家是否在近一个月有存款
					String sql = "SELECT count(*) from (select DISTINCT(loginname) from payorder pa where pa.type='0' and  pa.createTime>'"
							+ endDate
							+ "' and pa.loginname='"
							+ loginname
							+ "' UNION ALL select DISTINCT(loginname) from proposal pr where  pr.createTime>'"
							+ endDate
							+ "' and pr.loginname='"
							+ loginname
							+ "' and  pr.type='502' and pr.flag='2' ) a";
					pstmtproposal = conn.prepareStatement(sql);
					System.out.println("sql===" + sql);
					rsproposal = pstmtproposal.executeQuery();
					while (rsproposal.next()) {
						Integer countProposa = Integer.parseInt(rsproposal
								.getString(1));
						if (countProposa > 0) {
							t1 = true;
						} else {
							t1 = false;
						}
					}
					System.out.println(t1);
					// 如果是false 就把该玩家数据插入到专员电话记录中
					if (!t1) {
						if(intro !=null){
							if(intro.contains("ts")){
								insertinfo(conn, stmt, pstmt, loginname, accountName,
										phone, email, intro, qq, intro);
							}else{
								if (cs.equals("ts12")) {
									cs = "ts1";
								} else if (cs.equals("ts1")) {
									cs = "ts2";
								} else if (cs.equals("ts2")) {
									cs = "ts3";
								} else if (cs.equals("ts3")) {
									cs = "ts4";
								} else if (cs.equals("ts4")) {
									cs = "ts5";
								} else if (cs.equals("ts5")) {
									cs = "ts6";
								} else if (cs.equals("ts6")) {
									cs = "ts7";
								} else if (cs.equals("ts7")) {
									cs = "ts8";
								} else if (cs.equals("ts8")) {
									cs = "ts9";
								} else if (cs.equals("ts9")) {
									cs = "ts10";
								} else if (cs.equals("ts10")) {
									cs = "ts11";
								} else if (cs.equals("ts11")) {
									cs = "ts12";
								}
								insertinfo(conn, stmt, pstmt, loginname, accountName,
										phone, email, cs, qq, intro);
							}
						}

						// -----------------------插入数据------------------------------
						continue;
					} else {
						System.out
								.println("******************该玩家1个月内有存款无需插入数据end****************"
										+ loginname + "******");
					}
				} else {
					System.out.println("内部代理 不更新：" + loginname + "||" + agent);
				}
				if (null != pstmtproposal) {
					pstmtproposal.close();
					pstmtproposal = null;
				}
				if (null != rsproposal) {
					rsproposal.close();
					rsproposal = null;
				}
			}
			out.println("更新成功");
			if (null != rs) {
				rs.close();
				rs = null;
			}
			if (null != pstmt) {
				pstmt.close();
				pstmt = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (null != stmt) {
					stmt.close();
					stmt = null;
				}
				if (null != conn) {
					conn.close();
					conn = null;
				}
				if (null != out) {
					out.close();
				}
				out.flush();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String decodeString(String str){
		if(StringUtils.isNotBlank(str) && str.contains("ENC(")){
			return SpecialEnvironmentStringPBEConfig.decryptPBEConfig(str.replace("ENC(", "").replace(")", ""));
		} else {
			return str;
		}
	}

	/**
	 * 
	 * @param conn
	 * @param stmt
	 * @param loginname用户名
	 * @param accountname名字
	 * @param phone
	 * @param email
	 * @param cs
	 *            属于哪个客服
	 * @return
	 */
	public static void insertinfo(Connection conn, Statement stmt,
			PreparedStatement pstm, String loginname, String accountname,
			String phone, String email, String cs, String qq, String intro) {
		try {
			if (StringUtils.isNotBlank(email) && !email.contains("@")) {
				try {
					email = AESUtil.aesDecrypt(email, AESUtil.KEY);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (StringUtils.isNotBlank(phone)
					&& !dfh.utils.StringUtil.isNumeric(phone)) {
				try {
					phone = AESUtil.aesDecrypt(phone, AESUtil.KEY);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss");
			String date = sdf.format(calendar.getTime());
			// 查询玩家是否已存在于other_custom表中--------------第一次执行，可以不用检测，执行一次后，再放开
			/*
			 * String
			 * sqlcheck="select count(*) from other_customer where phone='"
			 * +phone+"' and email='"+email+"'and type='und'"; pstm =
			 * conn.prepareStatement(sqlcheck.toString()); ResultSet rs =
			 * pstmt.executeQuery(); while (rs.next()) { int a = rs.getInt(1);
			 * if(a>0){
			 * System.out.println("-------------------------------玩家已存在************"
			 * +loginname); return; } }
			 */

			// 将玩家信息插入专员电话记录
			String sqlInsert = "insert into other_customer (name,email,phone,isreg,isdeposit,phonestatus,userstatus,cs,remark,createTime,batch,type,qq) VALUES "
					+ "('"
					+ accountname
					+ "','"
					+ email
					+ "','"
					+ phone
					+ "','1','0','0','0','"
					+ cs
					+ "','"
					+ loginname
					+ ","
					+ intro + ",','" + date + "','1','und','" + qq + "')";
			System.out.println("sqlInsert==" + sqlInsert);
			stmt = conn.createStatement();
			stmt.execute(sqlInsert);

			// 更新玩家所属客服
			String sqlUpdateUser = "update users set intro='" + cs
					+ "' where loginname='" + loginname + "'";
			System.out.println("sqlUpdateUser==" + sqlUpdateUser);
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlUpdateUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	/**
	 * 更新支付状态
	 * 
	 * @return
	 */
	public String updateDeposFlag() {
		Connection conn = null;
		Statement stmt = null;
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			// 连接数据库
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			String mysqlUrl = getPhpPtService("datasource.url");
			String mysqlname = getPhpPtService("datasource.username");
			String mysqlPassword = getPhpPtService("datasource.password");
			conn = DriverManager.getConnection(mysqlUrl, mysqlname,
					mysqlPassword);
			System.out.println("连接MySql成功!!!");
			// 查询在规定时间内 所有有存款的玩家数据
			String sqlisDeposit = "select DISTINCT(loginname) from payorder pa where pa.type='0' and  pa.createTime>'"
					+ DeEndDate
					+ "'  UNION ALL select DISTINCT(loginname) from proposal pr where  pr.createTime>'"
					+ DeEndDate + "'  and  pr.type='502' and pr.flag='2' ";
			PreparedStatement pstmt1 = conn.prepareStatement(sqlisDeposit
					.toString());
			ResultSet rs1 = pstmt1.executeQuery();
			List listDeposit = new ArrayList();
			while (rs1.next()) {// 将玩家信息放入List中
				String loginname = rs1.getString("loginname");
				listDeposit.add(loginname);
			}
			// 查询所有导入的玩家信息 未存款的
			String sqlOtherCustom = "select remark,id from other_customer where type='und' and isdeposit='0'";
			PreparedStatement pstmt2 = conn.prepareStatement(sqlOtherCustom
					.toString());
			ResultSet rs2 = pstmt2.executeQuery();
			int z = 0;
			while (rs2.next()) {// 如果专员表中的玩家 在list中存在 则说明已经开始存款了 则更新状态
				String remark = rs2.getString("remark");
				String id = rs2.getString("id");
				String loginname = "";
				if ((!StringUtils.isBlank(remark)) && remark.contains(",")) {
					String str[] = remark.trim().split(",");
					if (str.length > 0) {
						loginname = str[0];
					}
				}
				if (listDeposit.contains(loginname)
						&& !StringUtils.isBlank(loginname)) {
					// 更新玩家存款状态
					String sqlUpdate = "update other_customer  set isdeposit='1' where id ='"
							+ id + "'";
					z++;
					System.out.println("更新玩家电话专员中的付款状态为已付款：" + loginname);
					updateIsDeposit(conn, stmt, sqlUpdate);
				}
			}
			out.println("更新成功,本次更新新增" + z + "个存款玩家！");
			if (rs1 != null) {
				rs1.close();
				rs1 = null;
			}
			if (rs2 != null) {
				rs2.close();
				rs2 = null;
			}
			if (pstmt1 != null) {
				pstmt1.close();
				pstmt1 = null;
			}
			if (pstmt2 != null) {
				pstmt2.close();
				pstmt2 = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
				if (out != null) {
					out.close();
				}
				out.flush();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 更新支付狀態
	 * 
	 * @param conn
	 * @param stmt
	 * @param sqlInsert
	 */
	public static void updateIsDeposit(Connection conn, Statement stmt,
			String sqlInsert) {
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlInsert);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}

	/**
	 * 统计数据
	 * 
	 * @return
	 */
	public String statisticData() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = null;
		Connection conn = null;
		Statement stmt = null;
		try {
			out = response.getWriter();
			// 连接数据库
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			String mysqlUrl = getPhpPtService("datasource.url");
			String mysqlname = getPhpPtService("datasource.username");
			String mysqlPassword = getPhpPtService("datasource.password");
			conn = DriverManager.getConnection(mysqlUrl, mysqlname,
					mysqlPassword);
			// System.out.println("连接MySql成功!!!");beginTime endTime
			StringBuffer sbf = new StringBuffer(
					"select SUM(money) as money,count(DISTINCT(name)) as totalno from  (select  p.loginname as name,p.amount as money from "
							+ "( select SUBSTRING_INDEX(o.remark,',',1) as username from other_customer o where isdeposit='1' and type='und'   and createTime>'2016-04-19'  ");
			if (!dfh.utils.StringUtil.isEmpty(cs)) {
				sbf.append("    and cs='" + cs + "'  ");
			}
			sbf.append(" )a ");
			sbf.append(" ,proposal p where p.loginname=a.username and p.createTime>'"
					+ beginTime
					+ "' and p.createTime<'"
					+ endTime
					+ "' and p.type='502' and p.flag='2'  UNION ALL ");
			sbf.append(" select  p.loginname as name,p.money as money from ( select SUBSTRING_INDEX(o.remark,',',1) as username from other_customer o where  isdeposit='1' and type='und'  and createTime>'2016-04-19'   ");
			if (!dfh.utils.StringUtil.isEmpty(cs)) {
				sbf.append("    and cs='" + cs + "'  ");
			}
			sbf.append(" )a ");
			sbf.append(" ,payorder p where p.loginname=a.username and p.createTime>'"
					+ beginTime
					+ "' and p.createTime<'"
					+ endTime
					+ "' and p.type='0' ) as b ");
			System.out.println("sbf===" + sbf);
			PreparedStatement pstmt = conn.prepareStatement(sbf.toString());
			ResultSet rs = pstmt.executeQuery();
			String money = "0";
			String total = "0";
			while (rs.next()) {
				money = rs.getString("money");
				if (StringUtil.isEmpty(money)) {
					money = "0";
				}
				total = rs.getString("totalno");
			}
			out.println("在" + beginTime + "至" + endTime + "时间内，共有" + total
					+ "位玩家存款，存款总量为：" + money + "元");
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
				if (out != null) {
					out.close();
				}
				out.flush();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getDeEndDate() {
		return DeEndDate;
	}

	public void setDeEndDate(String deEndDate) {
		DeEndDate = deEndDate;
	}

	public static void main(String[] args) {
		CheckUnDepositUserAction action = new CheckUnDepositUserAction();
		action.updateotherCustomAction();

		// updateotherCustomAction();
	}

	/*
	 * public String getStart() { return start; }
	 * 
	 * public void setStart(String start) { this.start = start; }
	 */

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	/**
	 * 更新email的状态
	 */
	public void updateEmailFlag() {
		int i = this.queryEmail();
		while (i > 1) {
			i = this.queryEmail();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 更新email的状态
	 */
	public void updateAgentEmailFlag() {
		int i = this.queryAgentEmail();
		while (i > 1) {
			i = this.queryAgentEmail();
			while (i > 1) {
				i = this.queryAgentEmail();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	int q = 0;

	/**
	 * 更新邮件是否有效
	 * 
	 * @return
	 */
	private int queryEmail() {
		int i = 0;
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2 = null;
		try {
			// 连接数据库
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			String mysqlUrl = getPhpPtService("datasource.url");
			String mysqlname = getPhpPtService("datasource.username");
			String mysqlPassword = getPhpPtService("datasource.password");
			conn = DriverManager.getConnection(mysqlUrl, mysqlname,
					mysqlPassword);
			System.out.println("连接MySql成功!!!");
			// 查询在规定时间内 所有有存款的玩家数据
			String sqlisDeposit = "select id,email from other_customer where emailflag is null ORDER BY id desc limit 30";
			String updateDeposit = "update other_customer set emailflag='3' where emailflag is null ORDER BY id desc limit 30";
			pstmt1 = conn.prepareStatement(sqlisDeposit.toString());
			rs1 = pstmt1.executeQuery();
			pstmt2 = conn.prepareStatement(updateDeposit.toString());
			pstmt2.execute();
			if (pstmt2 != null) {
				pstmt2.close();
				pstmt2 = null;
			}
			while (rs1.next()) {// 将玩家信息放入List中
				i++;
				q++;
				String email = rs1.getString("email");
				String id = rs1.getString("id");
				if ((!StringUtil.isEmpty(email)) && email.contains("hotmail")) {
					String sqlUpdate = "update other_customer set emailflag='2' where id='"
							+ id + "'";
					updateEmailFlag(conn, stmt, sqlUpdate);
					System.out.println(id + "检测邮件" + q + "：未知..." + i);
				} else {
					String str = SendEmailWsByEdm.checkemailFlagByexec(email);
					if (null == str) {// 0是无效 1是有效 null是超时了
						System.out.println(id + "检测邮件" + q + "：连接超时跳过..." + i);
					} else if (str.equals("false")) {// 0是无效 1是有效
						String sqlUpdate = "update other_customer set emailflag='0' where id='"
								+ id + "'";
						updateEmailFlag(conn, stmt, sqlUpdate);
						System.out.println(id + "检测邮件" + q + "：无效..." + i);
					} else if (str.equals("true")) {
						String sqlUpdate = "update other_customer set emailflag='1' where id='"
								+ id + "'";
						updateEmailFlag(conn, stmt, sqlUpdate);
						System.out.println(id + "检测邮件" + q + "：有效..." + i);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
					rs1 = null;
				}
				if (pstmt1 != null) {
					pstmt1.close();
					pstmt1 = null;
				}
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
		return i;
	}

	int q1 = 0;

	/**
	 * 更新邮件是否有效
	 * 
	 * @return
	 */
	private int queryAgentEmail() {
		int i = 0;
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt1 = null;
		ResultSet rs1 = null;
		PreparedStatement pstmt2 = null;
		try {
			// 连接数据库
			String driverName = "com.mysql.jdbc.Driver";
			Class.forName(driverName);
			String mysqlUrl = getPhpPtService("datasource.url");
			String mysqlname = getPhpPtService("datasource.username");
			String mysqlPassword = getPhpPtService("datasource.password");
			conn = DriverManager.getConnection(mysqlUrl, mysqlname,
					mysqlPassword);
			System.out.println("连接MySql成功!!!");
			// 查询在规定时间内 所有有存款的玩家数据
			String sqlisDeposit = "select id,email from agent_customer where emailflag is null ORDER BY id desc limit 50";
			String updateDeposit = "update agent_customer set emailflag='3' where emailflag is null ORDER BY id desc limit 50";
			pstmt1 = conn.prepareStatement(sqlisDeposit.toString());
			rs1 = pstmt1.executeQuery();
			pstmt2 = conn.prepareStatement(updateDeposit.toString());
			pstmt2.execute();
			if (pstmt2 != null) {
				pstmt2.close();
				pstmt2 = null;
			}
			while (rs1.next()) {// 将玩家信息放入List中
				i++;
				q1++;
				String email = rs1.getString("email");
				String id = rs1.getString("id");
				if ((!StringUtil.isEmpty(email)) && email.contains("hotmail")) {
					String sqlUpdate = "update agent_customer set emailflag='2' where id='"
							+ id + "'";
					updateEmailFlag(conn, stmt, sqlUpdate);
					System.out.println(id + "检测邮件" + q1 + "：未知..." + i);
				} else {
					String str = SendEmailWsByEdm.checkemailFlagByexec(email);
					if (null == str) {// 0是无效 1是有效 null是超时了
						System.out.println(id + "检测邮件" + q1 + "：连接超时跳过..." + i);
					} else if (str.equals("false")) {// 0是无效 1是有效
						String sqlUpdate = "update agent_customer set emailflag='0' where id='"
								+ id + "'";
						updateEmailFlag(conn, stmt, sqlUpdate);
						System.out.println(id + "检测邮件" + q1 + "：无效..." + i);
					} else if (str.equals("true")) {
						String sqlUpdate = "update agent_customer set emailflag='1' where id='"
								+ id + "'";
						updateEmailFlag(conn, stmt, sqlUpdate);
						System.out.println(id + "检测邮件" + q1 + "：有效..." + i);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null) {
					rs1.close();
					rs1 = null;
				}
				if (pstmt1 != null) {
					pstmt1.close();
					pstmt1 = null;
				}
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
		return i;
	}

	/**
	 * 更新邮件状态
	 * 
	 * @param conn
	 * @param stmt
	 * @param sqlInsert
	 */
	private void updateEmailFlag(Connection conn, Statement stmt,
			String sqlInsert) {
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlInsert);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}

}
