package dfh.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Commissionrecords entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "commissionrecords", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Commissionrecords implements java.io.Serializable {

	// Fields

	private CommissionrecordsId id;
	private String parent;
	private Double cashinAmount;
	private Double cashoutAmount;
	private Double firstDepositAmount;
	private Double ximaAmount;
	private Double otherAmount;
	private Double agAmount;
	private String remark;
	private Double sixLotteryBet;

	// Constructors

	public Double getSixLotteryBet() {
		return sixLotteryBet;
	}

	public void setSixLotteryBet(Double sixLotteryBet) {
		this.sixLotteryBet = sixLotteryBet;
	}

	/** default constructor */
	public Commissionrecords() {
	}

	/** minimal constructor */
	public Commissionrecords(CommissionrecordsId id, String parent, Double cashinAmount, Double cashoutAmount, Double firstDepositAmount, Double ximaAmount, Double otherAmount) {
		this.id = id;
		this.parent = parent;
		this.cashinAmount = cashinAmount;
		this.cashoutAmount = cashoutAmount;
		this.firstDepositAmount = firstDepositAmount;
		this.ximaAmount = ximaAmount;
		this.otherAmount = otherAmount;
	}

	/** full constructor */
	public Commissionrecords(CommissionrecordsId id, String parent, Double cashinAmount, Double cashoutAmount, Double firstDepositAmount, Double ximaAmount, Double otherAmount, String remark) {
		this.id = id;
		this.parent = parent;
		this.cashinAmount = cashinAmount;
		this.cashoutAmount = cashoutAmount;
		this.firstDepositAmount = firstDepositAmount;
		this.ximaAmount = ximaAmount;
		this.otherAmount = otherAmount;
		this.remark = remark;
	}

	// Property accessors
	@EmbeddedId
	@javax.persistence.Column(name = "id")
	public CommissionrecordsId getId() {
		return this.id;
	}

	public void setId(CommissionrecordsId id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "parent")
	public String getParent() {
		return this.parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	@javax.persistence.Column(name = "cashinAmount")
	public Double getCashinAmount() {
		return this.cashinAmount;
	}

	public void setCashinAmount(Double cashinAmount) {
		this.cashinAmount = cashinAmount;
	}

	@javax.persistence.Column(name = "cashoutAmount")
	public Double getCashoutAmount() {
		return this.cashoutAmount;
	}

	public void setCashoutAmount(Double cashoutAmount) {
		this.cashoutAmount = cashoutAmount;
	}

	@javax.persistence.Column(name = "firstDepositAmount")
	public Double getFirstDepositAmount() {
		return this.firstDepositAmount;
	}

	public void setFirstDepositAmount(Double firstDepositAmount) {
		this.firstDepositAmount = firstDepositAmount;
	}

	@javax.persistence.Column(name = "ximaAmount")
	public Double getXimaAmount() {
		return this.ximaAmount;
	}

	public void setXimaAmount(Double ximaAmount) {
		this.ximaAmount = ximaAmount;
	}

	@javax.persistence.Column(name = "otherAmount")
	public Double getOtherAmount() {
		return this.otherAmount;
	}

	public void setOtherAmount(Double otherAmount) {
		this.otherAmount = otherAmount;
	}
	
	@javax.persistence.Column(name = "agAmount")
	public Double getAgAmount() {
		return agAmount;
	}

	public void setAgAmount(Double agAmount) {
		this.agAmount = agAmount;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}