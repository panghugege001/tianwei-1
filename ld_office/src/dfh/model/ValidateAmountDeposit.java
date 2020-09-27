package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "validateamount_transfer", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class ValidateAmountDeposit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer transID;      //ID
	private String transferNo;    //流水号
	private Double amount;        //存入金额
	private Double fee;           //手续费
	private String depositor;     //存款人
	private String cardNo;        //存款卡号
	private String note;          //附言
	private String acceptName;    //存入卡号持卡人姓名
	private String acceptNo;      //存入卡号
	private Double blance;        //卡内余额
	private Date payTime;         //存入时间
	private Integer status;       //处理状态  0未处理 1已充值 2充值超时处理
	private Date readTime;        //入库时间
	private String timecha;      //时间差
	private Integer overTime;     
	private Integer adminID;      //管理员ID
	private String bankname;      //收款卡银行名
	private String remark;        //备注
	
	@Id
	@GeneratedValue(strategy = IDENTITY)  
	@Column(name="transfer_id")
	public Integer getTransID() {
		return transID;
	}
	public void setTransID(Integer transID) {
		this.transID = transID;
	}
	
	@Column(name="transferNo")
	public String getTransferNo() {
		return transferNo;
	}
	public void setTransferNo(String transferNo) {
		this.transferNo = transferNo;
	}
	
	@Column(name="amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Column(name="fee")
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	
	@Column(name="depositor")
	public String getDepositor() {
		return depositor;
	}
	public void setDepositor(String depositor) {
		this.depositor = depositor;
	}
	
	@Column(name="cardNo")
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	@Column(name="note")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	@Column(name="acceptName")
	public String getAcceptName() {
		return acceptName;
	}
	public void setAcceptName(String acceptName) {
		this.acceptName = acceptName;
	}
	
	@Column(name="acceptNo")
	public String getAcceptNo() {
		return acceptNo;
	}
	public void setAcceptNo(String acceptNo) {
		this.acceptNo = acceptNo;
	}
	
	@Column(name="balance")
	public Double getBlance() {
		return blance;
	}
	public void setBlance(Double blance) {
		this.blance = blance;
	}
	
	@Column(name="paytime")
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name="readtime")
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	
	@Column(name="timecha")
	public String getTimecha() {
		return timecha;
	}
	public void setTimecha(String timecha) {
		this.timecha = timecha;
	}
	
	@Column(name="overtime")
	public Integer getOverTime() {
		return overTime;
	}
	public void setOverTime(Integer overTime) {
		this.overTime = overTime;
	}
	
	@Column(name="adminID")
	public Integer getAdminID() {
		return adminID;
	}
	public void setAdminID(Integer adminID) {
		this.adminID = adminID;
	}
	
	@Column(name="bankname")
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
