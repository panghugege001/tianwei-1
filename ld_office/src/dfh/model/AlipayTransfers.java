package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cashin entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "alipay_transfers", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AlipayTransfers implements java.io.Serializable {

	// Fields

	private String transferId;
	private Date payDate;
	private String tradeNo;
	private String orderNo;
	private String tradeType;
	private Double amount;
	private Double fee;
	private Double balance;
	private String notes;
	private String acceptName;
	private String acceptNo;
	private Integer adminId;
	private Integer status;
	private Date date ;
	private String timecha;
	private Integer overtime;
	private Integer paytype;
	// Constructors

	/** default constructor */
	public AlipayTransfers() {
	}
	// Property accessors
	@Id
	@javax.persistence.Column(name = "transfer_id")
	public String getTransferId() {
		return transferId;
	}

	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	@javax.persistence.Column(name = "pay_date")
	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	@javax.persistence.Column(name = "trade_no")
	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	@javax.persistence.Column(name = "order_no")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	@javax.persistence.Column(name = "trade_type")
	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
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
	@javax.persistence.Column(name = "balance")
	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
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
	@javax.persistence.Column(name = "accept_no")
	public String getAcceptNo() {
		return acceptNo;
	}

	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}
	@javax.persistence.Column(name = "admin_id")
	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}
	@javax.persistence.Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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
	public Integer getPaytype() {
		return paytype;
	}
	public void setPaytype(Integer paytype) {
		this.paytype = paytype;
	}


}