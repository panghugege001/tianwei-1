package app.bean;

import org.apache.log4j.Logger;

/**
 * @author lin
 *
 */
public class TokenBean implements java.io.Serializable{ 
    private static final long serialVersionUID = -313223567008147359L;
	private static Logger log = Logger.getLogger(TokenBean.class);
	private String loginname;
	private String password;
	private String role;
	private String sid;
	private long loginTime;
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public long getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	
}
