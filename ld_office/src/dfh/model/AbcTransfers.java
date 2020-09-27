package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "abc_transfers", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)

public class AbcTransfers implements java.io.Serializable{
	
	private int transfeId;
	private Double amount;
	private Double balance;
	private String jyhm;
	private String jyfs;
	private String jyqd;
	private String jysm;
	private String jyzy;
	private String acceptName;
	private String acceptCardnum;
	private Date payDate;
	private int adminId;
	private int status;
	private Date date;
	private String timecha;
	private Integer overtime;
	
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
	public AbcTransfers() {
	}
	@javax.persistence.Column(name = "accept_name")
	public String getAcceptName() {
		return acceptName;
	}
	public void setAcceptName(String acceptName) {
		this.acceptName = acceptName;
	}
	@javax.persistence.Column(name = "jyhm")
	public String getJyhm() {
		return jyhm;
	}
	public void setJyhm(String jyhm) {
		this.jyhm = jyhm;
	}
	@javax.persistence.Column(name = "jyfs")
	public String getJyfs() {
		return jyfs;
	}
	public void setJyfs(String jyfs) {
		this.jyfs = jyfs;
	}
	@javax.persistence.Column(name = "jyqd")
	public String getJyqd() {
		return jyqd;
	}
	public void setJyqd(String jyqd) {
		this.jyqd = jyqd;
	}
	@javax.persistence.Column(name = "jysm")
	public String getJysm() {
		return jysm;
	}
	public void setJysm(String jysm) {
		this.jysm = jysm;
	}
	@javax.persistence.Column(name = "jyzy")
	public String getJyzy() {
		return jyzy;
	}
	public void setJyzy(String jyzy) {
		this.jyzy = jyzy;
	}
}
