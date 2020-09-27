package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Operator entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "operator", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Operator implements java.io.Serializable {

	// Fields

	private String username;
	private String password;
	private Integer enabled;
	private String authority;
	private Integer loginTimes;
	private Timestamp lastLoginTime;
	private String lastLoginIp;
	private Timestamp createTime;

	private String phonenoGX;
	private String phoneno;
	private String phonenoBL;
	private String cs;
	private String type;
	// Constructors

	/** default constructor */
	public Operator() {
	}

	/** minimal constructor */
	public Operator(String username, String password, Integer enabled, String authority, Integer loginTimes, Timestamp createTime) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.authority = authority;
		this.loginTimes = loginTimes;
		this.createTime = createTime;
	}

	/** full constructor */
	public Operator(String username, String password, Integer enabled, String authority, Integer loginTimes, Timestamp lastLoginTime, String lastLoginIp, Timestamp createTime) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.authority = authority;
		this.loginTimes = loginTimes;
		this.lastLoginTime = lastLoginTime;
		this.lastLoginIp = lastLoginIp;
		this.createTime = createTime;
	}

	// Property accessors
	@Id
	@javax.persistence.Column(name = "username")
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@javax.persistence.Column(name = "password")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@javax.persistence.Column(name = "enabled")
	public Integer getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	@javax.persistence.Column(name = "authority")
	public String getAuthority() {
		return this.authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@javax.persistence.Column(name = "loginTimes")
	public Integer getLoginTimes() {
		return this.loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	@javax.persistence.Column(name = "lastLoginTime")
	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@javax.persistence.Column(name = "lastLoginIp")
	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	@javax.persistence.Column(name = "createTime")
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getPhonenoGX() {
		return phonenoGX;
	}

	public void setPhonenoGX(String phonenoGX) {
		this.phonenoGX = phonenoGX;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getPhonenoBL() {
		return phonenoBL;
	}

	public void setPhonenoBL(String phonenoBL) {
		this.phonenoBL = phonenoBL;
	}

	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}