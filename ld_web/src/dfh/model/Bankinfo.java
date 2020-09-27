package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Bankinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "bankinfo", catalog = "longhu")
public class Bankinfo implements java.io.Serializable{
 
	// Fields
	private String massage ;
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
	private String zfbImgCode;
	private Integer scanAccount;//是否支付宝扫描账号
	private Integer paytype;//支付宝 or 网银
	private boolean force;//当为true时，前端会询问玩家是否删除旧订单，生成新订单。
	private String  depositId;//附言。
	private double quota=0.0;
	
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
	
	@Transient
	public String getMassage() {
		return massage;
	}

	public void setMassage(String massage) {
		this.massage = massage;
	}

	public String getZfbImgCode() {
		return zfbImgCode;
	}

	public void setZfbImgCode(String zfbImgCode) {
		this.zfbImgCode = zfbImgCode;
	}
	@Column(name = "paytype")
	public Integer getPaytype() {
		return paytype;
	}

	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}

	@Transient
	public boolean getForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}
	
	@Transient
	public String getDepositId() {
		return depositId;
	}

	public void setDepositId(String depositId) {
		this.depositId = depositId;
	}
	
	
	@Transient
	public double getQuota() {
		return quota;
	}

	public void setQuota(double quota) {
		this.quota = quota;
	}
}
