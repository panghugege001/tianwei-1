package dfh.action.vo;

import java.io.Serializable;

public class OperatorVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4335149514487881732L;
	private String username;
	private String password;
	private String enabled;
	private String authority;
	private String loginTimes;
	private String lastLoginTime;
	private String lastLoginIp;
	private String createTime;
	private String phonenoGX;
	private String phonenoBL;
	
	
	public OperatorVO(String username, String enabled,
			String loginTimes, String lastLoginTime,
			String lastLoginIp, String createTime,String authority,String phonenoGX,String phonenoBL) {
		super();
		this.username = username;
		this.enabled = enabled;
		this.loginTimes = loginTimes;
		this.lastLoginTime = lastLoginTime;
		this.lastLoginIp = lastLoginIp;
		this.createTime = createTime;
		this.authority=authority;
		this.phonenoGX=phonenoGX;
		this.phonenoBL=phonenoBL;
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public String getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(String loginTimes) {
		this.loginTimes = loginTimes;
	}
	public String getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getPhonenoGX() {
		return phonenoGX;
	}


	public void setPhonenoGX(String phonenoGX) {
		this.phonenoGX = phonenoGX;
	}


	public String getPhonenoBL() {
		return phonenoBL;
	}


	public void setPhonenoBL(String phonenoBL) {
		this.phonenoBL = phonenoBL;
	}

}
