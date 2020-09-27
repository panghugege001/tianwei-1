package dfh.action.customer;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.C3P0DBManager;
import dfh.utils.IPSeeker;

public class AuthorityThread extends Thread {

	public HttpServletRequest req;
	public Users user;
	public C3P0DBManager dBManager;

	public AuthorityThread(HttpServletRequest req, Users user) {
		this.dBManager = new C3P0DBManager();
		this.req = req;
		this.user = user;
	}

	private IPSeeker getIPSeeker(HttpServletRequest request) {
		return (IPSeeker) request.getSession().getServletContext().getAttribute("ipSeeker");
	}

	public void run() {
		String forwaredFor = req.getHeader("X-Forwarded-For");
		String ip = "";
		String remoteAddr = req.getRemoteAddr();
		if (StringUtils.isNotEmpty(forwaredFor))
			ip = forwaredFor.split(",")[0];
		else {
			ip = remoteAddr;
		}
		System.out.println("Request ip:" + ip);
		String[] ips = ip.replace('.', ',').split(",");// 如果ip是127.0.0.1
		String forTwo = ips[0] + "." + ips[1] + "." + "***" + "." + "***"; // 转化成127.0.***.***
		String forThree = ips[0] + "." + ips[1] + "." + ips[2] + "." + "***";// 转化成127.0.***.***
		Map<String, String> ipconfineMap = (Map<String, String>) req.getSession().getServletContext().getAttribute("ipconfineMap");
		String allow = ipconfineMap.get("allow");
		String deny = ipconfineMap.get("deny");
		if (deny.indexOf(ip) >= 0 || deny.indexOf(forTwo) >= 0 || deny.indexOf(forThree) >= 0) {
			System.out.println("deny ip:" + ip);
		} else if (allow.indexOf(ip) >= 0 || allow.indexOf(forTwo) >= 0 || allow.indexOf(forThree) >= 0) {
			IPSeeker ipSeeker = getIPSeeker(req);
			if (ipSeeker != null) {
				String area = ipSeeker.getCountry(ip);
				String source_come_url = req.getHeader("Referer");
				if (source_come_url == null || source_come_url.equals("")) {
					source_come_url = "直接输入网址或书签";
				}
				String source_go_url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + req.getServletPath();
				String agent_website = req.getScheme() + "://" + req.getServerName();
				String loginname = "";
				if (user != null) {
					loginname = user.getLoginname();
				} else {
					loginname = "匿名访问";
				}
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				SimpleDateFormat sfDay = new SimpleDateFormat("yyyy-MM-dd");
				Date nowDate = new Date();
				String date = sf.format(nowDate);
				try {
					if (agent_website != null && !agent_website.equals("")) {
						Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUserAgent", new Object[] { agent_website }, Users.class);
						if (user != null) {
							String agent = user.getLoginname();
							if (agent != null && !agent.equals("")) {
								String startTime = sfDay.format(nowDate) + " 00:00:00";
								String endTime = sfDay.format(nowDate) + " 23:59:59";
								// 删除今天以前的数据
								String sqlDelete = "DELETE FROM agent_ip WHERE createtime<'" + startTime + "'";
								dBManager.executeUpdate(sqlDelete);
								// 查询今天是否存在该IP
								String sqlIp = "SELECT ip FROM agent_ip WHERE createtime>='" + startTime + "' AND createtime<'" + endTime + "' AND agent='" + agent + "' AND ip='" + ip + "'";
								ResultSet rs = dBManager.executeQuery(sqlIp);
								Boolean t = true;
								while (rs.next()) {
									t = false;
									break;
								}
								// 如果存在 就不就行统计
								if (t) {
									// 查询今天是否存
									String agent_count_sql = "SELECT * FROM agent_count WHERE createtime>='" + startTime + "' AND createtime<'" + endTime + "' AND agent='" + agent + "'";
									ResultSet rs1 = dBManager.executeQuery(agent_count_sql);
									Boolean t1 = true;
									while (rs1.next()) {
										Integer id = rs1.getInt("id");
										Integer agent_count = rs1.getInt("agent_count") + 1;
										Integer agent_pv = rs1.getInt("agent_pv") + 1;
										String agent_count_update = "UPDATE agent_count SET agent_count='" + agent_count + "', agent_pv='" + agent_pv + "',createtime='" + date + "' WHERE id='" + id + "'";
										dBManager.executeUpdate(agent_count_update);
										// System.out.println("**********1**********"+agent_count_update);
										t1 = false;
										break;
									}
									if (t1) {
										// 新增一条统计
										String agent_count_add = "INSERT INTO agent_count (agent,agent_count,agent_pv,agent_website,createtime)VALUES ('" + agent + "', '1','1','" + agent_website + "','" + date + "')";
										dBManager.executeUpdate(agent_count_add);
										// System.out.println("**********2**********"+agent_count_add);
									}
									// 新增这一条
									String agent_ip_add = "INSERT INTO agent_ip (agent,ip,createtime)VALUES ('" + agent + "', '" + ip + "', '" + date + "')";
									dBManager.executeUpdate(agent_ip_add);
									// System.out.println("**********3**********"+agent_ip_add);
								} else {
									// 查询今天是否存在
									String agent_count_sql = "SELECT * FROM agent_count WHERE createtime>='" + startTime + "' AND createtime<'" + endTime + "' AND agent='" + agent + "'";
									ResultSet rs1 = dBManager.executeQuery(agent_count_sql);
									Boolean t1 = true;
									while (rs1.next()) {
										Integer id = rs1.getInt("id");
										Integer agent_pv = rs1.getInt("agent_pv") + 1;
										String agent_count_update = "UPDATE agent_count SET agent_pv='" + agent_pv + "',createtime='" + date + "' WHERE id='" + id + "'";
										dBManager.executeUpdate(agent_count_update);
										// System.out.println("**********4**********"+agent_count_update);
										t1 = false;
										break;
									}
									if (t1) {
										// 新增一条统计
										String agent_count_add = "INSERT INTO agent_count (agent,agent_count,agent_pv,agent_website,createtime)VALUES ('" + agent + "', '1','1','" + agent_website + "','" + date + "')";
										dBManager.executeUpdate(agent_count_add);
										// System.out.println("**********5**********"+agent_count_add);
									}
								}
								// 详情表
								String sql = "INSERT INTO agent_visit (loginname,agent,agent_website,client_ip,client_address,source_come_url,source_go_url,createtime)VALUES ('" + loginname + "', '" + agent + "', '" + agent_website + "', '" + ip + "', '" + area + "', '" + source_come_url + "', '" + source_go_url + "', '" + date + "')";
								dBManager.executeUpdate(sql);
								// System.out.println("**********6**********"+sql);
								dBManager.close();
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					dBManager.close();
				}
			}
			System.out.println("allow ip:" + ip);
		} else {
			IPSeeker ipSeeker = getIPSeeker(req);
			if (ipSeeker != null) {
				String area = ipSeeker.getCountry(ip);
				if (area.indexOf("菲律宾") >= 0 /*|| area.indexOf("澳门") >= 0*/ || area.indexOf("香港") >= 0 || area.indexOf("台湾") >= 0) {
					System.out.println("Deny Request ip:" + ip);
				} else {
					String source_come_url = req.getHeader("Referer");
					if (source_come_url == null || source_come_url.equals("")) {
						source_come_url = "直接输入网址或书签";
					}
					String source_go_url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + req.getServletPath();
					String agent_website = req.getScheme() + "://" + req.getServerName();
					String loginname = "";
					if (user != null) {
						loginname = user.getLoginname();
					} else {
						loginname = "匿名访问";
					}
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					SimpleDateFormat sfDay = new SimpleDateFormat("yyyy-MM-dd");
					Date nowDate = new Date();
					String date = sf.format(nowDate);
					try {
						if (agent_website != null && !agent_website.equals("")) {
							Users user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUserAgent", new Object[] { agent_website }, Users.class);
							if (user != null) {
								String agent = user.getLoginname();
								if (agent != null && !agent.equals("")) {
									String startTime = sfDay.format(nowDate) + " 00:00:00";
									String endTime = sfDay.format(nowDate) + " 23:59:59";
									// 删除今天以前的数据
									String sqlDelete = "DELETE FROM agent_ip WHERE createtime<'" + startTime + "'";
									dBManager.executeUpdate(sqlDelete);
									// 查询今天是否存在该IP
									String sqlIp = "SELECT ip FROM agent_ip WHERE createtime>='" + startTime + "' AND createtime<'" + endTime + "' AND agent='" + agent + "' AND ip='" + ip + "'";
									ResultSet rs = dBManager.executeQuery(sqlIp);
									Boolean t = true;
									while (rs.next()) {
										t = false;
										break;
									}
									// 如果存在 就不就行统计
									if (t) {
										// 查询今天是否存
										String agent_count_sql = "SELECT * FROM agent_count WHERE createtime>='" + startTime + "' AND createtime<'" + endTime + "' AND agent='" + agent + "'";
										ResultSet rs1 = dBManager.executeQuery(agent_count_sql);
										Boolean t1 = true;
										while (rs1.next()) {
											Integer id = rs1.getInt("id");
											Integer agent_count = rs1.getInt("agent_count") + 1;
											Integer agent_pv = rs1.getInt("agent_pv") + 1;
											String agent_count_update = "UPDATE agent_count SET agent_count='" + agent_count + "', agent_pv='" + agent_pv + "',createtime='" + date + "' WHERE id='" + id + "'";
											dBManager.executeUpdate(agent_count_update);
											// System.out.println("**********1**********"+agent_count_update);
											t1 = false;
											break;
										}
										if (t1) {
											// 新增一条统计
											String agent_count_add = "INSERT INTO agent_count (agent,agent_count,agent_pv,agent_website,createtime)VALUES ('" + agent + "', '1','1','" + agent_website + "','" + date + "')";
											dBManager.executeUpdate(agent_count_add);
											// System.out.println("**********2**********"+agent_count_add);
										}
										// 新增这一条
										String agent_ip_add = "INSERT INTO agent_ip (agent,ip,createtime)VALUES ('" + agent + "', '" + ip + "', '" + date + "')";
										dBManager.executeUpdate(agent_ip_add);
										// System.out.println("**********3**********"+agent_ip_add);
									} else {
										// 查询今天是否存在
										String agent_count_sql = "SELECT * FROM agent_count WHERE createtime>='" + startTime + "' AND createtime<'" + endTime + "' AND agent='" + agent + "'";
										ResultSet rs1 = dBManager.executeQuery(agent_count_sql);
										Boolean t1 = true;
										while (rs1.next()) {
											Integer id = rs1.getInt("id");
											Integer agent_pv = rs1.getInt("agent_pv") + 1;
											String agent_count_update = "UPDATE agent_count SET agent_pv='" + agent_pv + "',createtime='" + date + "' WHERE id='" + id + "'";
											dBManager.executeUpdate(agent_count_update);
											// System.out.println("**********4**********"+agent_count_update);
											t1 = false;
											break;
										}
										if (t1) {
											// 新增一条统计
											String agent_count_add = "INSERT INTO agent_count (agent,agent_count,agent_pv,agent_website,createtime)VALUES ('" + agent + "', '1','1','" + agent_website + "','" + date + "')";
											dBManager.executeUpdate(agent_count_add);
											// System.out.println("**********5**********"+agent_count_add);
										}
									}
									// 详情表
									String sql = "INSERT INTO agent_visit (loginname,agent,agent_website,client_ip,client_address,source_come_url,source_go_url,createtime)VALUES ('" + loginname + "', '" + agent + "', '" + agent_website + "', '" + ip + "', '" + area + "', '" + source_come_url + "', '" + source_go_url + "', '" + date + "')";
									dBManager.executeUpdate(sql);
									// System.out.println("**********6**********"+sql);
									dBManager.close();
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dBManager.close();
					}
				}
			}
		}
	}

}
