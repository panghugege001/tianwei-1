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
	
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码,MD5简单加密,无key
	 */
	private String password;
	/**
	 * 0:正常 1:被禁用(无法登陆)
	 */
	private Integer enabled;
	/**
	 * 角色,longdu已经废弃这种写死角色的方式,该字段不再使用,用户角色对应关系参考t_user_role 和 t_role
	 */
	private String authority;
	/**
	 * 登陆次数,成功登陆一次+1
	 */
	private Integer loginTimes;
	/**
	 * 上一次登陆的时间
	 */
	private Timestamp lastLoginTime;
	/**
	 * 上一次登陆的IP
	 */
	private String lastLoginIp;
	/**
	 * 数据创建时间
	 */
	private Timestamp createTime;

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

}