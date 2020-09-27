package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Bankinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "bankinfo", catalog = "tianwei")
public class Bankinfo implements java.io.Serializable{

	// Fields

	private String username;
	private Integer id;
	private Integer type;
	private String bankname;
	private String remark;
	private Integer useable = 0;
	private double amount = 0.0;
	private String vpnname;
	private String vpnpassword;
	private String accountno;
	private String userrole;
	private Integer isshow;
	private String loginname;
	private String password;
	private Integer banktype;
	private String bankcard;
	private String usb;
	private String realname;
	private Timestamp updatetime;
	private double bankamount = 0.0;
	private String remoteip;
	private Integer istransfer;
	private Integer isactive;
	
	private Integer transferswitch;//开启转账
	private Integer samebankswitch;//开启同行
	private Integer autopay;//是否自动付款
	private String samebank;//同行银行
	private String crossbank;//跨行银行
	private Double transfermoney;//转账额度
	private Double fee;//转账额度
	private String zfbImgCode;
	private Integer scanAccount;//是否支付宝扫描账号
	private Integer paytype;//支付宝 or 网银
	private double depositMin = 0.0;
	private double depositMax = 0.0;

	/** default constructor */
	public Bankinfo() {
	}

	public Bankinfo(String username, Integer id, Integer type,String bankname,String remark) {
		super();
		this.username = username;
		this.id = id;
		this.type = type;
		this.bankname = bankname;
		this.remark = remark;
	}
	
	@Column(name = "paytype")
	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	// Property accessors
	@Column(name = "username", nullable = false, length = 30)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public void setBankname(String bankname){
		this.bankname = bankname;
	}

	@Column(name = "bankname")
	public String getBankname() {
		return bankname;
	}
	
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "useable")
	public Integer getUseable() {
		return useable;
	}

	public void setUseable(Integer useable) {
		this.useable = useable;
	}

	@Column(name = "amount")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	@Column(name = "vpnname")
	public String getVpnname() {
		return vpnname;
	}

	public void setVpnname(String vpnname) {
		this.vpnname = vpnname;
	}

	@Column(name = "vpnpassword")
	public String getVpnpassword() {
		return vpnpassword;
	}

	public void setVpnpassword(String vpnpassword) {
		this.vpnpassword = vpnpassword;
	}

	@Column(name = "accountno")
	public String getAccountno() {
		return accountno;
	}

	public void setAccountno(String accountno) {
		this.accountno = accountno;
	}

	@Column(name = "userrole")
	public String getUserrole() {
		return userrole;
	}

	public void setUserrole(String userrole) {
		this.userrole = userrole;
	}

	@Column(name = "isshow")
	public Integer getIsshow() {
		return isshow;
	}

	public void setIsshow(Integer isshow) {
		this.isshow = isshow;
	}

	@Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "banktype")
	public Integer getBanktype() {
		return banktype;
	}

	public void setBanktype(Integer banktype) {
		this.banktype = banktype;
	}
	
	@Column(name = "bankcard")
	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	@Column(name = "usb")
	public String getUsb() {
		return usb;
	}

	public void setUsb(String usb) {
		this.usb = usb;
	}

	@Column(name = "realname")
	public String getRealname() {
		return realname;
	}
	
	public void setRealname(String realname) {
		this.realname = realname;
	}
	@Column(name = "updatetime")
	public Timestamp getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}
	@Column(name = "bankamount")
	public double getBankamount() {
		return bankamount;
	}

	public void setBankamount(double bankamount) {
		this.bankamount = bankamount;
	}
	@Column(name = "remoteip")
	public String getRemoteip() {
		return remoteip;
	}

	public void setRemoteip(String remoteip) {
		this.remoteip = remoteip;
	}
	@Column(name = "istransfer")
	public Integer getIstransfer() {
		return istransfer;
	}
	
	public void setIstransfer(Integer istransfer) {
		this.istransfer = istransfer;
	}

	public Integer getIsactive() {
		return isactive;
	}

	public void setIsactive(Integer isactive) {
		this.isactive = isactive;
	}

	public Integer getTransferswitch() {
		return transferswitch;
	}

	public void setTransferswitch(Integer transferswitch) {
		this.transferswitch = transferswitch;
	}

	public Integer getSamebankswitch() {
		return samebankswitch;
	}

	public void setSamebankswitch(Integer samebankswitch) {
		this.samebankswitch = samebankswitch;
	}

	public Integer getAutopay() {
		return autopay;
	}

	public void setAutopay(Integer autopay) {
		this.autopay = autopay;
	}

	public String getSamebank() {
		return samebank;
	}

	public void setSamebank(String samebank) {
		this.samebank = samebank;
	}

	public String getCrossbank() {
		return crossbank;
	}

	public void setCrossbank(String crossbank) {
		this.crossbank = crossbank;
	}

	public Double getTransfermoney() {
		return transfermoney;
	}

	public void setTransfermoney(Double transfermoney) {
		this.transfermoney = transfermoney;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getZfbImgCode() {
		return zfbImgCode;
	}

	public void setZfbImgCode(String zfbImgCode) {
		this.zfbImgCode = zfbImgCode;
	}

	public Integer getScanAccount() {
		return scanAccount;
	}

	public void setScanAccount(Integer scanAccount) {
		this.scanAccount = scanAccount;
	}
	
	@Column(name = "depositMin")
	public double getDepositMin() {
		return depositMin;
	}

	public void setDepositMin(double depositMin) {
		this.depositMin = depositMin;
	}
	@Column(name = "depositMax")
	public double getDepositMax() {
		return depositMax;
	}

	public void setDepositMax(double depositMax) {
		this.depositMax = depositMax;
	}
	
}
