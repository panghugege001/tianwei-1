package dfh.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "deposit_order", catalog = "tianwei")
public class DepositOrder implements java.io.Serializable {

	// Fields
	private String depositId;
	private String loginname;
	private String accountname; //开户人
	private String bankname; //银行类型
	private String bankno; //银行卡号
	private Integer status; //状态
	private Date createtime;
	private Date updatetime;
	private String remark;
	private String spare;  //存款额度
	
	private String tempCreateTime;
	private String ubankname;
	private String uaccountname;
	private String ubankno;
	private Double amount;
	private Integer flag;
	private String type;    
	private String realname;
	private Double inputamount;
	// Constructors

	@Transient
	public String getTempCreateTime() {
		return tempCreateTime;
	}
	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}
	
	@Id
	@javax.persistence.Column(name = "depositId")
	public String getDepositId() {
		return depositId;
	}
	public void setDepositId(String depositId) {
		this.depositId = depositId;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public DepositOrder() {
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	
	public String getUbankname() {
		return ubankname;
	}
	public void setUbankname(String ubankname) {
		this.ubankname = ubankname;
	}
	public String getUaccountname() {
		return uaccountname;
	}
	public void setUaccountname(String uaccountname) {
		this.uaccountname = uaccountname;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getUbankno() {
		return ubankno;
	}
	public void setUbankno(String ubankno) {
		this.ubankno = ubankno;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getSpare() {
		return spare;
	}
	public void setSpare(String spare) {
		this.spare = spare;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public Double getInputamount() {
		return inputamount;
	}
	public void setInputamount(Double inputamount) {
		this.inputamount = inputamount;
	}
	
}