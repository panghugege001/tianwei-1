package dfh.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import dfh.utils.AESUtil;

/**
 * Users entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "users_bak", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Users_bak implements java.io.Serializable {

	// Fields

	private String loginname;
	private String password;
	private String intro;
	private Double credit;
	private Timestamp lastLoginTime;
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
	private Timestamp createtime;
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
	private Timestamp birthday;
	private Integer id;
	private Integer warnflag;
	private String warnremark;
	private Double rate;
	private Double bbinrate;
	private Double agrate;
	private Double aginrate;
	private Double kenorate;
	private Double sbrate;
	private String mailaddress;
	private String clientos;
	private Double skyrate;
	private String area;
	private String lastarea;
	private String userRemark;

	private Double creditlimit;//-1转账没有限制 0 表示不能转账 1000表示一天最高转账1000
	private String randnum;
	private Integer ptflag;
	private Double earebate;
	private Double bbinrebate;
	private Double aginrebate;
	private Double agrebate;
	private Double kenorebate;
	private Double sbrebate;
	private Double ptrebate;
	private String invitecode ; //用来存放邀请码  intro用来存放a/b/c/d/e
	private String shippingcodePt ;
	private String microchannel;
	
	
	@javax.persistence.Column(name = "shippingcodePt")
	public String getShippingcodePt() {
		return shippingcodePt;
	}

	public void setShippingcodePt(String shippingcodePt) {
		this.shippingcodePt = shippingcodePt;
	}

	@javax.persistence.Column(name = "invitecode")
	public String getInvitecode() {
		return invitecode;
	}

	public void setInvitecode(String invitecode) {
		this.invitecode = invitecode;
	}

	@javax.persistence.Column(name = "user_remark")
	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	
	@javax.persistence.Column(name = "rate")
	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}
	
	@javax.persistence.Column(name = "warnflag")
	public Integer getWarnflag() {
		return warnflag;
	}

	public void setWarnflag(Integer warnflag) {
		this.warnflag = warnflag;
	}
	@javax.persistence.Column(name = "warnremark")
	public String getWarnremark() {
		return warnremark;
	}

	public void setWarnremark(String warnremark) {
		this.warnremark = warnremark;
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
	public Users_bak() {
	}

	/** minimal constructor */
	public Users_bak(String loginname, String password, Double credit, String md5str, Integer flag, String role, Integer loginTimes) {
		this.loginname = loginname;
		this.password = password;
		this.credit = credit;
		this.md5str = md5str;
		this.flag = flag;
		this.role = role;
		this.loginTimes = loginTimes;
	}

	/** full constructor */
	public Users_bak(String loginname, String password, Double credit, Timestamp lastLoginTime, Integer level, String md5str, String agent, String partner, String accountName, String phone, Integer flag,
			String aliasName, String email, String address, String postcode, Timestamp createtime, String role, String howToKnow, Integer loginTimes, String lastLoginIp, String agcode, String qq,
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
	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
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

	@javax.persistence.Column(name = "phone")
    public String getPhone() {
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
		return this.aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
	@javax.persistence.Column(name = "email")
    public String getEmail() {
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
	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
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
	public Timestamp getBirthday() {
		return birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@javax.persistence.Column(name = "bbinrate")
	public Double getBbinrate() {
		return bbinrate;
	}

	public void setBbinrate(Double bbinrate) {
		this.bbinrate = bbinrate;
	}
	@javax.persistence.Column(name = "agrate")
	public Double getAgrate() {
		return agrate;
	}

	public void setAgrate(Double agrate) {
		this.agrate = agrate;
	}
	@javax.persistence.Column(name = "kenorate")
	public Double getKenorate() {
		return kenorate;
	}

	public void setKenorate(Double kenorate) {
		this.kenorate = kenorate;
	}
	@javax.persistence.Column(name = "intro")
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	@javax.persistence.Column(name = "mailaddress")
	public String getMailaddress() {
		return mailaddress;
	}

	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}
	@javax.persistence.Column(name = "aginrate")
	public Double getAginrate() {
		return aginrate;
	}

	public void setAginrate(Double aginrate) {
		this.aginrate = aginrate;
	}
	@javax.persistence.Column(name = "sbrate")
	public Double getSbrate() {
		return sbrate;
	}

	public void setSbrate(Double sbrate) {
		this.sbrate = sbrate;
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

	@javax.persistence.Column(name = "skyrate")
	public Double getSkyrate() {
		return skyrate;
	}

	public void setSkyrate(Double skyrate) {
		this.skyrate = skyrate;
	}
	@javax.persistence.Column(name = "randnum")
	public String getRandnum() {
		return randnum;
	}

	public void setRandnum(String randnum) {
		this.randnum = randnum;
	}
	@javax.persistence.Column(name = "ptflag")
	public Integer getPtflag() {
		return ptflag;
	}

	public void setPtflag(Integer ptflag) {
		this.ptflag = ptflag;
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
	@javax.persistence.Column(name = "microchannel")
	public String getMicrochannel() {
		return microchannel;
	}

	public void setMicrochannel(String microchannel) {
		this.microchannel = microchannel;
	}
}