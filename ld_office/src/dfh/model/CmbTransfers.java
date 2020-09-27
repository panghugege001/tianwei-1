package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "cmb_transfers", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)

public class CmbTransfers implements java.io.Serializable{
	
	private int transfeId;
	private Double amount;
	private Double balance;
	private String jylx;
	private String notes;
	private String acceptName;
	private String acceptCardnum;
	private Date payDate;
	private int adminId;
	private int status;
	private Date date;
	private String timecha;
	private Integer overtime;
	private String uaccountname;   
	private String uaccountno; 
	private String loginname;
	private String remark;
	private Integer payType;
	private String orderNumber;
	
	@Id
	@javax.persistence.Column(name = "transfer_id")
	public int getTransfeId() {
		return transfeId;
	}
	public void setTransfeId(int transfeId) {
		this.transfeId = transfeId;
	}
	
	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@javax.persistence.Column(name = "notes")
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@javax.persistence.Column(name = "accept_card_num")
	public String getAcceptCardnum() {
		return acceptCardnum;
	}
	public void setAcceptCardnum(String acceptCardnum) {
		this.acceptCardnum = acceptCardnum;
	}
	
	@javax.persistence.Column(name = "pay_date")
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	
	@javax.persistence.Column(name = "admin_id")
	public int getAdminId() {
		return adminId;
	}
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}
	
	@javax.persistence.Column(name = "status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@javax.persistence.Column(name = "date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@javax.persistence.Column(name = "timecha")
	public String getTimecha() {
		return timecha;
	}
	public void setTimecha(String timecha) {
		this.timecha = timecha;
	}
	
	@javax.persistence.Column(name = "overtime")
	public Integer getOvertime() {
		return overtime;
	}
	public void setOvertime(Integer overtime) {
		this.overtime = overtime;
	}
	@javax.persistence.Column(name = "balance")
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	@javax.persistence.Column(name = "jylx")
	public String getJylx() {
		return jylx;
	}
	public void setJylx(String jylx) {
		this.jylx = jylx;
	}
	public CmbTransfers() {
	}
	@javax.persistence.Column(name = "accept_name")
	public String getAcceptName() {
		return acceptName;
	}
	public void setAcceptName(String acceptName) {
		this.acceptName = acceptName;
	}
	
	public String getUaccountname() {
		return uaccountname;
	}
	public void setUaccountname(String uaccountname) {
		this.uaccountname = uaccountname;
	}
	@javax.persistence.Column(name = "loginname")   
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	
	@javax.persistence.Column(name = "pay_type")
	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	@javax.persistence.Column(name = "order_number")
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	
	@javax.persistence.Column(name = "uaccountno")
	public String getUaccountno() {
		return uaccountno;
	}
	public void setUaccountno(String uaccountno) {
		this.uaccountno = uaccountno;
	}
	
	
}
