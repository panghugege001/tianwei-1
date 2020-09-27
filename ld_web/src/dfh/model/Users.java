package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import dfh.utils.AESUtil;

/**
 * Users entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "users", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Users implements java.io.Serializable {

	// Fields
	/**
	 * 用户名
	 */
	private String loginname;
	/**
	 * 密码
	 */
	private String password;
	private Double credit;
	private java.util.Date lastLoginTime;
	private Integer level;
	private String md5str;
	private String agent;
	private String partner;
	private String accountName;
	private String phone;
	private Integer flag;
	private String aliasName;
	private String email;
	private String address;
	private String postcode;
	private java.util.Date createtime;
	/**
	 * MONEY_CUSTOMER:真钱用户 ;AGENT:代理用户;
	 */
	private String role;
	private String howToKnow;
	private Integer loginTimes;
	private String lastLoginIp;
	private String agcode;
	private String qq;
	private String remark;
	private String referWebsite;
	private String pwdinfo;
	private String registerIp;
	private Integer isCashin;
	private Integer sms;
	private java.util.Date birthday;
	private Integer id;
	private String intro;
	private Integer passwdflag;
	private String tempCreateTime;
	private String tempLastLoginTime;
	private String mailaddress;
	private Integer warnflag;
	private Double rate;
	private String shippingcode;
	private Double gifTamount;
	
	private String area;
	private String lastarea;
	private Double creditlimit;//-1转账没有限制 0 表示不能转账 1000表示一天最高转账1000
	private Double creditday;
	private String creditdaydate;
	
	private String clientos;
	private String randnum;
	private String microchannel;
	private String shippingcodePt;
	private Integer ptflag;
	private String kenokey;
	private String sbLoginname;
	private String sbPassword;
	
	private Double earebate;
	private Double bbinrebate;
	private Double aginrebate;
	private Double agrebate;
	private Double kenorebate;
	private Double sbrebate;
	private Double ptrebate;
	private String invitecode ; //用来存放邀请码  intro用来存放a/b/c/d/e
	@javax.persistence.Column(name = "invitecode")
	public String getInvitecode() {
		return invitecode;
	}

	public void setInvitecode(String invitecode) {
		this.invitecode = invitecode;
	}
	
	@javax.persistence.Column(name = "rate")
	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	// Constructors

	@javax.persistence.Column(name = "sms")
	public Integer getSms() {
		return sms;
	}

	public void setSms(Integer sms) {
		this.sms = sms;
	}

	/** default constructor */
	public Users() {
	}

	/** minimal constructor */
	public Users(String loginname, String password, Double credit, String md5str, Integer flag, String role, Integer loginTimes) {
		this.loginname = loginname;
		this.password = password;
		this.credit = credit;
		this.md5str = md5str;
		this.flag = flag;
		this.role = role;
		this.loginTimes = loginTimes;
	}

	/** full constructor */
	public Users(String loginname, String password, Double credit, Date lastLoginTime, Integer level, String md5str, String agent, String partner, String accountName, String phone, Integer flag,
			String aliasName, String email, String address, String postcode, Date createtime, String role, String howToKnow, Integer loginTimes, String lastLoginIp, String agcode, String qq,
			String remark, String referWebsite, String pwdinfo, String registerIp) {
		this.loginname = loginname;
		this.password = password;
		this.credit = credit;
		this.lastLoginTime = lastLoginTime;
		this.level = level;
		this.md5str = md5str;
		this.agent = agent;
		this.partner = partner;
		this.accountName = accountName;
		this.phone = phone;
		this.flag = flag;
		this.aliasName = aliasName;
		this.email = email;
		this.address = address;
		this.postcode = postcode;
		this.createtime = createtime;
		this.role = role;
		this.howToKnow = howToKnow;
		this.loginTimes = loginTimes;
		this.lastLoginIp = lastLoginIp;
		this.agcode = agcode;
		this.qq = qq;
		this.remark = remark;
		this.referWebsite = referWebsite;
		this.pwdinfo = pwdinfo;
		this.registerIp = registerIp;
	}

	// Property accessors
	@Id
	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "password")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@javax.persistence.Column(name = "credit")
	public Double getCredit() {
		return this.credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	@javax.persistence.Column(name = "lastLoginTime")
	public java.util.Date getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(java.util.Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@javax.persistence.Column(name = "level")
	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@javax.persistence.Column(name = "md5str")
	public String getMd5str() {
		return this.md5str;
	}

	public void setMd5str(String md5str) {
		this.md5str = md5str;
	}

	@javax.persistence.Column(name = "agent")
	public String getAgent() {
		return this.agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	@javax.persistence.Column(name = "partner")
	public String getPartner() {
		return this.partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	@javax.persistence.Column(name = "accountName")
	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@javax.persistence.Column(name = "phone",updatable=false)
	public String getPhone() {
		if(StringUtils.isNotBlank(phone) && !dfh.utils.StringUtil.isNumeric(phone)){
			try {
				return AESUtil.aesDecrypt(phone, AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@javax.persistence.Column(name = "flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@javax.persistence.Column(name = "aliasName")
	public String getAliasName() {
		return this.loginname;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	@javax.persistence.Column(name = "email",updatable=false)
	public String getEmail() {
		if(StringUtils.isNotBlank(email) && !email.contains("@")){
			try {
				return AESUtil.aesDecrypt(email, AESUtil.KEY);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@javax.persistence.Column(name = "address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@javax.persistence.Column(name = "postcode")
	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@javax.persistence.Column(name = "createtime")
	public java.util.Date getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}

	@javax.persistence.Column(name = "role")
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@javax.persistence.Column(name = "howToKnow")
	public String getHowToKnow() {
		return this.howToKnow;
	}

	public void setHowToKnow(String howToKnow) {
		this.howToKnow = howToKnow;
	}

	@javax.persistence.Column(name = "loginTimes")
	public Integer getLoginTimes() {
		return this.loginTimes;
	}

	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}

	@javax.persistence.Column(name = "lastLoginIp")
	public String getLastLoginIp() {
		return this.lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	@javax.persistence.Column(name = "agcode")
	public String getAgcode() {
		return this.agcode;
	}

	public void setAgcode(String agcode) {
		this.agcode = agcode;
	}

	@javax.persistence.Column(name = "qq")
	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@javax.persistence.Column(name = "referWebsite")
	public String getReferWebsite() {
		return this.referWebsite;
	}

	public void setReferWebsite(String referWebsite) {
		this.referWebsite = referWebsite;
	}

	@javax.persistence.Column(name = "pwdinfo")
	public String getPwdinfo() {
		return this.pwdinfo;
	}

	public void setPwdinfo(String pwdinfo) {
		this.pwdinfo = pwdinfo;
	}

	@javax.persistence.Column(name = "registerIp")
	public String getRegisterIp() {
		return this.registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	@javax.persistence.Column(name = "isCashin")
	public Integer getIsCashin() {
		return isCashin;
	}

	public void setIsCashin(Integer isCashin) {
		this.isCashin = isCashin;
	}

	@javax.persistence.Column(name = "birthday")
	public java.util.Date getBirthday() {
		return birthday;
	}

	public void setBirthday(java.util.Date birthday) {
		this.birthday = birthday;
	}

	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@javax.persistence.Column(name = "intro")
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	@javax.persistence.Column(name = "passwdflag")
	public Integer getPasswdflag() {
		return passwdflag;
	}

	public void setPasswdflag(Integer passwdflag) {
		this.passwdflag = passwdflag;
	}
	@Transient
	public String getTempCreateTime() {
		return tempCreateTime;
	}

	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}
	@Transient
	public String getTempLastLoginTime() {
		return tempLastLoginTime;
	}

	public void setTempLastLoginTime(String tempLastLoginTime) {
		this.tempLastLoginTime = tempLastLoginTime;
	}
	@javax.persistence.Column(name = "mailaddress")
	public String getMailaddress() {
		return mailaddress;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}
	@javax.persistence.Column(name = "warnflag")
	public Integer getWarnflag() {
		return warnflag;
	}

	public void setWarnflag(Integer warnflag) {
		this.warnflag = warnflag;
	}
	
	@javax.persistence.Column(name = "shippingcode")
	public String getShippingcode() {
		return shippingcode;
	}

	public void setShippingcode(String shippingcode) {
		this.shippingcode = shippingcode;
	}
	
	@javax.persistence.Column(name = "giftamount")
	public Double getGifTamount() {
		return gifTamount;
	}

	public void setGifTamount(Double gifTamount) {
		this.gifTamount = gifTamount;
	}
	@javax.persistence.Column(name = "area")
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	@javax.persistence.Column(name = "lastarea")
	public String getLastarea() {
		return lastarea;
	}

	public void setLastarea(String lastarea) {
		this.lastarea = lastarea;
	}
	@javax.persistence.Column(name = "creditlimit")
	public Double getCreditlimit() {
		return creditlimit;
	}

	public void setCreditlimit(Double creditlimit) {
		this.creditlimit = creditlimit;
	}
	
	@javax.persistence.Column(name = "creditday")
	public Double getCreditday() {
		return creditday;
	}

	public void setCreditday(Double creditday) {
		this.creditday = creditday;
	}

	@javax.persistence.Column(name = "creditdaydate")
	public String getCreditdaydate() {
		return creditdaydate;
	}
	
	public void setCreditdaydate(String creditdaydate) {
		this.creditdaydate = creditdaydate;
	}

	@javax.persistence.Column(name = "microchannel")
	public String getMicrochannel() {
		return microchannel;
	}

	public void setMicrochannel(String microchannel) {
		this.microchannel = microchannel;
	}
	
	@javax.persistence.Column(name = "shippingcodePt")
	public String getShippingcodePt() {
		return shippingcodePt;
	}

	public void setShippingcodePt(String shippingcodePt) {
		this.shippingcodePt = shippingcodePt;
	}

	@javax.persistence.Column(name = "ptflag")
	public Integer getPtflag() {
		return ptflag;
	}

	public void setPtflag(Integer ptflag) {
		this.ptflag = ptflag;
	}
	
	@javax.persistence.Column(name = "kenokey")
	public String getKenokey() {
		return kenokey;
	}

	public void setKenokey(String kenokey) {
		this.kenokey = kenokey;
	}
	

	@javax.persistence.Column(name = "sbloginname")
	public String getSbLoginname() {
		return sbLoginname;
	}

	public void setSbLoginname(String sbLoginname) {
		this.sbLoginname = sbLoginname;
	}

	@javax.persistence.Column(name = "sbpassword")
	public String getSbPassword() {
		return sbPassword;
	}

	public void setSbPassword(String sbPassword) {
		this.sbPassword = sbPassword;
	}
	
	@javax.persistence.Column(name = "earebate")
	public Double getEarebate() {
		return earebate;
	}

	public void setEarebate(Double earebate) {
		this.earebate = earebate;
	}

	@javax.persistence.Column(name = "bbinrebate")
	public Double getBbinrebate() {
		return bbinrebate;
	}

	public void setBbinrebate(Double bbinrebate) {
		this.bbinrebate = bbinrebate;
	}

	@javax.persistence.Column(name = "agrebate")
	public Double getAgrebate() {
		return agrebate;
	}

	public void setAgrebate(Double agrebate) {
		this.agrebate = agrebate;
	}

	@javax.persistence.Column(name = "kenorebate")
	public Double getKenorebate() {
		return kenorebate;
	}

	public void setKenorebate(Double kenorebate) {
		this.kenorebate = kenorebate;
	}

	@javax.persistence.Column(name = "sbrebate")
	public Double getSbrebate() {
		return sbrebate;
	}

	public void setSbrebate(Double sbrebate) {
		this.sbrebate = sbrebate;
	}

	@javax.persistence.Column(name = "ptrebate")
	public Double getPtrebate() {
		return ptrebate;
	}

	public void setPtrebate(Double ptrebate) {
		this.ptrebate = ptrebate;
	}

	@javax.persistence.Column(name = "aginrebate")
	public Double getAginrebate() {
		return aginrebate;
	}

	public void setAginrebate(Double aginrebate) {
		this.aginrebate = aginrebate;
	}
	@javax.persistence.Column(name = "clientos")
	public String getClientos() {
		return clientos;
	}

	public void setClientos(String clientos) {
		this.clientos = clientos;
	}
	@javax.persistence.Column(name = "randnum")
	public String getRandnum() {
		return randnum;
	}

	public void setRandnum(String randnum) {
		this.randnum = randnum;
	}

	@Override
	public String toString() {
		return "Users [loginname=" + loginname + ", password=" + password + ", credit=" + credit + ", lastLoginTime="
				+ lastLoginTime + ", level=" + level + ", md5str=" + md5str + ", agent=" + agent + ", partner="
				+ partner + ", accountName=" + accountName + ", phone=" + phone + ", flag=" + flag + ", aliasName="
				+ aliasName + ", email=" + email + ", address=" + address + ", postcode=" + postcode + ", createtime="
				+ createtime + ", role=" + role + ", howToKnow=" + howToKnow + ", loginTimes=" + loginTimes
				+ ", lastLoginIp=" + lastLoginIp + ", agcode=" + agcode + ", qq=" + qq + ", remark=" + remark
				+ ", referWebsite=" + referWebsite + ", pwdinfo=" + pwdinfo + ", registerIp=" + registerIp
				+ ", isCashin=" + isCashin + ", sms=" + sms + ", birthday=" + birthday + ", id=" + id + ", intro="
				+ intro + ", passwdflag=" + passwdflag + ", tempCreateTime=" + tempCreateTime + ", tempLastLoginTime="
				+ tempLastLoginTime + ", mailaddress=" + mailaddress + ", warnflag=" + warnflag + ", rate=" + rate
				+ ", shippingcode=" + shippingcode + ", gifTamount=" + gifTamount + ", area=" + area + ", lastarea="
				+ lastarea + ", creditlimit=" + creditlimit + ", creditday=" + creditday + ", creditdaydate="
				+ creditdaydate + ", clientos=" + clientos + ", randnum=" + randnum + ", microchannel=" + microchannel
				+ ", shippingcodePt=" + shippingcodePt + ", ptflag=" + ptflag + ", kenokey=" + kenokey
				+ ", sbLoginname=" + sbLoginname + ", sbPassword=" + sbPassword + ", earebate=" + earebate
				+ ", bbinrebate=" + bbinrebate + ", aginrebate=" + aginrebate + ", agrebate=" + agrebate
				+ ", kenorebate=" + kenorebate + ", sbrebate=" + sbrebate + ", ptrebate=" + ptrebate + ", invitecode="
				+ invitecode + "]";
	}
	
}