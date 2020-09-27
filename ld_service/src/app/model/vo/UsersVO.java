package app.model.vo;

import java.util.List;


/**
 * Users entity. @author MyEclipse Persistence Tools
 */
public class UsersVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 2268248098619331452L;
	
	private String loginname;
	private String password;
	private Double credit;
	private String lastLoginTime;
	private String level;
	private String levelCode;
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
	private String createtime;
	private String role;
	private String howToKnow;
	private Integer loginTimes;
	private String lastLoginIp;
	private String agcode;
	private String qq;
	private String wechat;
	private String remark;
	private String referWebsite;
	private String pwdinfo;
	private String registerIp;
	private Integer isCashin;
	private Integer sms;
	private String birthday;
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
	private Integer ptlogin;
	private String pointBalance;//积分余额
	
	private String area;
	private String lastarea;
	private Double creditlimit;//-1转账没有限制 0 表示不能转账 1000表示一天最高转账1000
	private Double creditday;
	private String creditdaydate;
	
	private String clientos;
	
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
	private String invitecode ;
	
	private String ealoginid ;
	private String eatoken ;
	private String warnremark ;

	private String qrCodeInfoStr;//专属客服二维码URL
	
	private String sid;//设备码
	
	private String os;//操作系统（ios or android）
	private String osVersion;//操作系统版本
	private String mobileModel;//机型
	
	
	//Android 用
	public String imei;// 手机唯一码;
	public String imsi;//SIM卡串码
	public String macAddress;//mac地址
	public String emulator;// 模拟器标记项;
	
	private List<UserbankVO> availableBankList;//用户绑定的可用银行卡列表
	
	// 玩家绑定密保问题总记录数
	private Integer questionNumber;
	
	
	private SmsCustomizationRequestDataVO smsCustomizationInit;//玩家短信定制
	
	private WithdrawalTipsVO withdrawalTips;//玩家提款提示信息

	public String getQrCodeInfoStr() {
		return qrCodeInfoStr;
	}

	public void setQrCodeInfoStr(String qrCodeInfoStr) {
		this.qrCodeInfoStr = qrCodeInfoStr;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getMobileModel() {
		return mobileModel;
	}

	public void setMobileModel(String mobileModel) {
		this.mobileModel = mobileModel;
	}

	public WithdrawalTipsVO getWithdrawalTips() {
		return withdrawalTips;
	}

	public void setWithdrawalTips(WithdrawalTipsVO withdrawalTips) {
		this.withdrawalTips = withdrawalTips;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPointBalance() {
		return pointBalance;
	}

	public void setPointBalance(String pointBalance) {
		this.pointBalance = pointBalance;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}
	
	
	public SmsCustomizationRequestDataVO getSmsCustomizationInit() {
		return smsCustomizationInit;
	}

	public void setSmsCustomizationInit(
			SmsCustomizationRequestDataVO smsCustomizationInit) {
		this.smsCustomizationInit = smsCustomizationInit;
	}

	public Integer getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}

	public List<UserbankVO> getAvailableBankList() {
		return availableBankList;
	}

	public void setAvailableBankList(List<UserbankVO> availableBankList) {
		this.availableBankList = availableBankList;
	}


	public String getLoginname() {
		return loginname;
	}
	


	public String getSid() {
		return sid;
	}



	public void setSid(String sid) {
		this.sid = sid;
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
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getMd5str() {
		return md5str;
	}
	public void setMd5str(String md5str) {
		this.md5str = md5str;
	}
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
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getHowToKnow() {
		return howToKnow;
	}
	public void setHowToKnow(String howToKnow) {
		this.howToKnow = howToKnow;
	}
	public Integer getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(Integer loginTimes) {
		this.loginTimes = loginTimes;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public String getAgcode() {
		return agcode;
	}
	public void setAgcode(String agcode) {
		this.agcode = agcode;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getReferWebsite() {
		return referWebsite;
	}
	public void setReferWebsite(String referWebsite) {
		this.referWebsite = referWebsite;
	}
	public String getPwdinfo() {
		return pwdinfo;
	}
	public void setPwdinfo(String pwdinfo) {
		this.pwdinfo = pwdinfo;
	}
	public String getRegisterIp() {
		return registerIp;
	}
	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}
	public Integer getIsCashin() {
		return isCashin;
	}
	public void setIsCashin(Integer isCashin) {
		this.isCashin = isCashin;
	}
	public Integer getSms() {
		return sms;
	}
	public void setSms(Integer sms) {
		this.sms = sms;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public Integer getPasswdflag() {
		return passwdflag;
	}
	public void setPasswdflag(Integer passwdflag) {
		this.passwdflag = passwdflag;
	}
	public String getTempCreateTime() {
		return tempCreateTime;
	}
	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}
	public String getTempLastLoginTime() {
		return tempLastLoginTime;
	}
	public void setTempLastLoginTime(String tempLastLoginTime) {
		this.tempLastLoginTime = tempLastLoginTime;
	}
	public String getMailaddress() {
		return mailaddress;
	}
	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}
	public Integer getWarnflag() {
		return warnflag;
	}
	public void setWarnflag(Integer warnflag) {
		this.warnflag = warnflag;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getShippingcode() {
		return shippingcode;
	}
	public void setShippingcode(String shippingcode) {
		this.shippingcode = shippingcode;
	}
	public Double getGifTamount() {
		return gifTamount;
	}
	public void setGifTamount(Double gifTamount) {
		this.gifTamount = gifTamount;
	}
	public Integer getPtlogin() {
		return ptlogin;
	}
	public void setPtlogin(Integer ptlogin) {
		this.ptlogin = ptlogin;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLastarea() {
		return lastarea;
	}
	public void setLastarea(String lastarea) {
		this.lastarea = lastarea;
	}
	public Double getCreditlimit() {
		return creditlimit;
	}
	public void setCreditlimit(Double creditlimit) {
		this.creditlimit = creditlimit;
	}
	public Double getCreditday() {
		return creditday;
	}
	public void setCreditday(Double creditday) {
		this.creditday = creditday;
	}
	public String getCreditdaydate() {
		return creditdaydate;
	}
	public void setCreditdaydate(String creditdaydate) {
		this.creditdaydate = creditdaydate;
	}
	public String getClientos() {
		return clientos;
	}
	public void setClientos(String clientos) {
		this.clientos = clientos;
	}
	public String getMicrochannel() {
		return microchannel;
	}
	public void setMicrochannel(String microchannel) {
		this.microchannel = microchannel;
	}
	public String getShippingcodePt() {
		return shippingcodePt;
	}
	public void setShippingcodePt(String shippingcodePt) {
		this.shippingcodePt = shippingcodePt;
	}
	public Integer getPtflag() {
		return ptflag;
	}
	public void setPtflag(Integer ptflag) {
		this.ptflag = ptflag;
	}
	public String getKenokey() {
		return kenokey;
	}
	public void setKenokey(String kenokey) {
		this.kenokey = kenokey;
	}
	public String getSbLoginname() {
		return sbLoginname;
	}
	public void setSbLoginname(String sbLoginname) {
		this.sbLoginname = sbLoginname;
	}
	public String getSbPassword() {
		return sbPassword;
	}
	public void setSbPassword(String sbPassword) {
		this.sbPassword = sbPassword;
	}
	public Double getEarebate() {
		return earebate;
	}
	public void setEarebate(Double earebate) {
		this.earebate = earebate;
	}
	public Double getBbinrebate() {
		return bbinrebate;
	}
	public void setBbinrebate(Double bbinrebate) {
		this.bbinrebate = bbinrebate;
	}
	public Double getAginrebate() {
		return aginrebate;
	}
	public void setAginrebate(Double aginrebate) {
		this.aginrebate = aginrebate;
	}
	public Double getAgrebate() {
		return agrebate;
	}
	public void setAgrebate(Double agrebate) {
		this.agrebate = agrebate;
	}
	public Double getKenorebate() {
		return kenorebate;
	}
	public void setKenorebate(Double kenorebate) {
		this.kenorebate = kenorebate;
	}
	public Double getSbrebate() {
		return sbrebate;
	}
	public void setSbrebate(Double sbrebate) {
		this.sbrebate = sbrebate;
	}
	public Double getPtrebate() {
		return ptrebate;
	}
	public void setPtrebate(Double ptrebate) {
		this.ptrebate = ptrebate;
	}
	public String getInvitecode() {
		return invitecode;
	}
	public void setInvitecode(String invitecode) {
		this.invitecode = invitecode;
	}
	public String getEaloginid() {
		return ealoginid;
	}
	public void setEaloginid(String ealoginid) {
		this.ealoginid = ealoginid;
	}
	public String getEatoken() {
		return eatoken;
	}
	public void setEatoken(String eatoken) {
		this.eatoken = eatoken;
	}
	public String getWarnremark() {
		return warnremark;
	}
	public void setWarnremark(String warnremark) {
		this.warnremark = warnremark;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getEmulator() {
		return emulator;
	}

	public void setEmulator(String emulator) {
		this.emulator = emulator;
	}

	public String getLevelCode() {
		return levelCode;
	}

	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	
	
	
}