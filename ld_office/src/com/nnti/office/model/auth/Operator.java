package com.nnti.office.model.auth;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	private String email;
	/**
	 * 0:正常 1:被禁用
	 */
	private Integer enabled;
	private String authority;
	private Integer loginTimes;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Timestamp lastLoginTime;
	private String lastLoginIp;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	private String blServerUrl;
	
	private Date firstDayWeek ;
	
	private String phonenoGX;
	
	private String phonenoBL;
	
	private String cs;
	
	private String type;
	private String partner; 
	private String agent;
	private String randomStr ; //判断是否重复登录的随机字符串
	
	private Integer passwordNumber; //密码错误次数
	private String phoneno;
	//1,短信验证，2，打卡验证，3无需验证
	private Integer validType;
	private String smsPwd;
	private Timestamp smsUpdateTime; 
	private String employeeNo;
	/**
	 * 如果具备创建用户的权限,可创建的用户角色
	 */
	private String subRole;
    
    
    @javax.persistence.Column(name = "passwordNumber")
    public Integer getPasswordNumber() {
		return passwordNumber;
	}

	public void setPasswordNumber(Integer passwordNumber) {
		this.passwordNumber = passwordNumber;
	}	
	
	// Constructors
	@javax.persistence.Column(name = "firstDayWeek")
	public Date getFirstDayWeek() {
		return firstDayWeek;
	}

	public void setFirstDayWeek(Date firstDayWeek) {
		this.firstDayWeek = firstDayWeek;
	}

	// Constructors

	/** default constructor */
	public Operator() {
	}

	/** minimal constructor */
	public Operator(String username, String password, Integer enabled, String authority, Integer loginTimes, Timestamp createTime,Integer validType,String phoneno,String employeeNo) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.authority = authority;
		this.loginTimes = loginTimes;
		this.createTime = createTime;
		this.validType = validType;
		this.phoneno = phoneno;
		this.employeeNo = employeeNo;
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
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@javax.persistence.Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@javax.persistence.Column(name = "phonenoGX")
	public String getPhonenoGX() {
		return phonenoGX;
	}
	public void setPhonenoGX(String phonenoGX) {
		this.phonenoGX = phonenoGX;
	}
	@javax.persistence.Column(name = "phonenoBL")
	public String getPhonenoBL() {
		return phonenoBL;
	}

	public void setPhonenoBL(String phonenoBL) {
		this.phonenoBL = phonenoBL;
	}
	
	
	@javax.persistence.Column(name = "cs")
	public String getCs() {
		return cs;
	}

	public void setCs(String cs) {
		this.cs = cs;
	}
	@javax.persistence.Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
    @javax.persistence.Column(name = "agent")
    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}
	
	@javax.persistence.Column(name = "blserver_url")
	public String getBlServerUrl() {
		return blServerUrl;
	}

	public void setBlServerUrl(String blServerUrl) {
		this.blServerUrl = blServerUrl;
	}
	
	public String getRandomStr() {
		
		return randomStr;
	}

	
	public void setRandomStr(String randomStr) {
	
		this.randomStr = randomStr;
	}

	public Integer getValidType() {
		return validType;
	}

	public void setValidType(Integer validType) {
		this.validType = validType;
	}

	public String getSmsPwd() {
		return smsPwd;
	}

	public void setSmsPwd(String smsPwd) {
		this.smsPwd = smsPwd;
	}

	public Timestamp getSmsUpdateTime() {
		return smsUpdateTime;
	}

	public void setSmsUpdateTime(Timestamp smsUpdateTime) {
		this.smsUpdateTime = smsUpdateTime;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}
	
	@javax.persistence.Column(name = "employeeNo")
	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getSubRole() {
		return subRole;
	}

	public void setSubRole(String subRole) {
		this.subRole = subRole;
	}

	@Override
	public String toString() {
		return "Operator [username=" + username + ", password=" + password + ", email=" + email + ", enabled=" + enabled + ", authority=" + authority
				+ ", loginTimes=" + loginTimes + ", lastLoginTime=" + lastLoginTime + ", lastLoginIp=" + lastLoginIp + ", createTime=" + createTime
				+ ", blServerUrl=" + blServerUrl + ", firstDayWeek=" + firstDayWeek + ", phonenoGX=" + phonenoGX + ", phonenoBL=" + phonenoBL
				+ ", cs=" + cs + ", type=" + type + ", partner=" + partner + ", agent=" + agent + ", randomStr=" + randomStr + ", passwordNumber="
				+ passwordNumber + ", phoneno=" + phoneno + ", validType=" + validType + ", smsPwd=" + smsPwd + ", smsUpdateTime=" + smsUpdateTime
				+ ", employeeNo=" + employeeNo + ", subRole=" + subRole + "]";
	}
	
}