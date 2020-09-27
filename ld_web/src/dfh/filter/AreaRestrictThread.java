package dfh.filter;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.C3P0DBManager;

public class AreaRestrictThread extends Thread {

	public C3P0DBManager dBManager;
	public String loginname;// 用户
	public String agent_website;// 代理网址
	public String client_ip;// 访问ip
	public String client_address;// 地址地址
	public String source_come_url;// 来源网址
	public String source_go_url;// 受访地址

	public AreaRestrictThread(String loginname, String agent_website, String client_ip, String client_address, String source_come_url, String source_go_url) {
		this.dBManager = new C3P0DBManager();
		this.loginname = loginname;
		this.agent_website = agent_website;
		this.client_ip = client_ip;
		this.client_address = client_address;
		this.source_come_url = source_come_url;
		this.source_go_url = source_go_url;
	}

	public void run() {
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
						String sqlIp = "SELECT ip FROM agent_ip WHERE createtime>='" + startTime + "' AND createtime<'" + endTime + "' AND agent='" + agent + "' AND ip='" + client_ip + "'";
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
							String agent_ip_add = "INSERT INTO agent_ip (agent,ip,createtime)VALUES ('" + agent + "', '" + client_ip + "', '" + date + "')";
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
						String sql = "INSERT INTO agent_visit (loginname,agent,agent_website,client_ip,client_address,source_come_url,source_go_url,createtime)VALUES ('" + loginname + "', '" + agent + "', '" + agent_website + "', '" + client_ip + "', '" + client_address + "', '" + source_come_url + "', '" + source_go_url + "', '" + date + "')";
						dBManager.executeUpdate(sql);
						// System.out.println("**********6**********"+sql);
						dBManager.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String sql = "INSERT INTO agent_visit (loginname,agent,agent_website,client_ip,client_address,source_come_url,source_go_url,createtime)VALUES ('" + loginname + "', null, '" + agent_website + "', '" + client_ip + "', '" + client_address + "', '" + source_come_url + "', '" + source_go_url + "', '" + date + "')";
			// System.out.println("插入失败!*****"+sql);
			dBManager.close();
		}
	}

}
