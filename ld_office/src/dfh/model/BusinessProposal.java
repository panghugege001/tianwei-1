package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Proposal entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "businessproposal", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class BusinessProposal implements java.io.Serializable {

	// Fields

	private String pno;
	private String proposer;
	private Timestamp createTime;
	private Integer type;
	private String depositname;
	private Double amount;
	private Integer flag;
	private String whereisfrom;
	private String remark;
	private String bankaccount;
	private String depositbank;
	private String attachment;
	private String excattachment;
	private String depositaccount;
	private Integer bankaccountid;
	private Double actualmoney;
	private String belong;

	// Constructors

	/** default constructor */
	public BusinessProposal() {
	}

	

	// Property accessors
	@Id
	@javax.persistence.Column(name = "pno")
	public String getPno() {
		return this.pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	@javax.persistence.Column(name = "proposer")
	public String getProposer() {
		return this.proposer;
	}

	public void setProposer(String proposer) {
		this.proposer = proposer;
	}

	@javax.persistence.Column(name = "createTime")
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	

	

	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	

	@javax.persistence.Column(name = "flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@javax.persistence.Column(name = "whereisfrom")
	public String getWhereisfrom() {
		return this.whereisfrom;
	}

	public void setWhereisfrom(String whereisfrom) {
		this.whereisfrom = whereisfrom;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	

	
	
	@javax.persistence.Column(name = "bankaccount")
	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}


	@javax.persistence.Column(name = "depositname")
	public String getDepositname() {
		return depositname;
	}



	public void setDepositname(String depositname) {
		this.depositname = depositname;
	}


	@javax.persistence.Column(name = "depositbank")
	public String getDepositbank() {
		return depositbank;
	}



	public void setDepositbank(String depositbank) {
		this.depositbank = depositbank;
	}


	@javax.persistence.Column(name = "attachment")
	public String getAttachment() {
		return attachment;
	}



	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
	@javax.persistence.Column(name = "excattachment")
	public String getExcattachment() {
		return excattachment;
	}

	public void setExcattachment(String excattachment) {
		this.excattachment = excattachment;
	}	

	@javax.persistence.Column(name = "depositaccount")
	public String getDepositaccount() {
		return depositaccount;
	}

	public void setDepositaccount(String depositaccount) {
		this.depositaccount = depositaccount;
	}

	@javax.persistence.Column(name = "bankaccountid")
	public Integer getBankaccountid() {
		return bankaccountid;
	}

	public void setBankaccountid(Integer bankaccountid) {
		this.bankaccountid = bankaccountid;
	}

	@javax.persistence.Column(name = "actualmoney")
	public Double getActualmoney() {
		return actualmoney;
	}

	public void setActualmoney(Double actualmoney) {
		this.actualmoney = actualmoney;
	}
	
	@javax.persistence.Column(name = "belong")
	public String getBelong() {
		return belong;
	}
	public void setBelong(String belong) {
		this.belong = belong;
	}

}