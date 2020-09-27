package dfh.model;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Commissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "commissions", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Commissions implements java.io.Serializable {

	// Fields

	private CommissionsId id;
	private Integer flag;
	private Integer subCount;
	private Double eaProfitAmount;
	private Double amount;
	private Date createTime;
	private Date excuteTime;
	private String remark;

	// Constructors

	/** default constructor */
	public Commissions() {
	}

	/** minimal constructor */
	public Commissions(CommissionsId id, Integer flag, Integer subCount, Double eaProfitAmount) {
		this.id = id;
		this.flag = flag;
		this.subCount = subCount;
		this.eaProfitAmount = eaProfitAmount;
	}

	/** full constructor */
	public Commissions(CommissionsId id, Integer flag, Integer subCount, Double eaProfitAmount, Double amount, Date createTime, Date excuteTime, String remark) {
		this.id = id;
		this.flag = flag;
		this.subCount = subCount;
		this.eaProfitAmount = eaProfitAmount;
		this.amount = amount;
		this.createTime = createTime;
		this.excuteTime = excuteTime;
		this.remark = remark;
	}

	// Property accessors
	@EmbeddedId
	@javax.persistence.Column(name = "id")
	public CommissionsId getId() {
		return this.id;
	}

	public void setId(CommissionsId id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@javax.persistence.Column(name = "subCount")
	public Integer getSubCount() {
		return this.subCount;
	}

	public void setSubCount(Integer subCount) {
		this.subCount = subCount;
	}

	@javax.persistence.Column(name = "eaProfitAmount")
	public Double getEaProfitAmount() {
		return this.eaProfitAmount;
	}

	public void setEaProfitAmount(Double eaProfitAmount) {
		this.eaProfitAmount = eaProfitAmount;
	}

	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@javax.persistence.Column(name = "createTime")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "excuteTime")
	public Date getExcuteTime() {
		return this.excuteTime;
	}

	public void setExcuteTime(Date excuteTime) {
		this.excuteTime = excuteTime;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}