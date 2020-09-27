package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "icbc_transfers", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)

public class IcbcTransfers implements java.io.Serializable{
	
	private int transfeId;
	private String currserialNum;
	private String name;
	private String cardNumber;
	private String area;
	private Double amount;
	private Double fee;
	private String notes;
	private String acceptName;
	private String acceptCardnum;
	private String acceptArea;
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
	
	@javax.persistence.Column(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@javax.persistence.Column(name = "card_num")
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	@javax.persistence.Column(name = "area")
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@javax.persistence.Column(name = "fee")
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	
	@javax.persistence.Column(name = "notes")
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@javax.persistence.Column(name = "accept_name")
	public String getAcceptName() {
		return acceptName;
	}
	public void setAcceptName(String acceptName) {
		this.acceptName = acceptName;
	}
	
	@javax.persistence.Column(name = "accept_card_num")
	public String getAcceptCardnum() {
		return acceptCardnum;
	}
	public void setAcceptCardnum(String acceptCardnum) {
		this.acceptCardnum = acceptCardnum;
	}
	
	
	@javax.persistence.Column(name = "accept_area")
	public String getAcceptArea() {
		return acceptArea;
	}
	public void setAcceptArea(String acceptArea) {
		this.acceptArea = acceptArea;
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
	@javax.persistence.Column(name = "currserial_num")
	public String getCurrserialNum() {
		return currserialNum;
	}
	public void setCurrserialNum(String currserialNum) {
		this.currserialNum = currserialNum;
	}
	
	
	
}
