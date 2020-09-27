package dfh.model;

import java.sql.Timestamp;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Commissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ptcommissions_record", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PtCommissionRecords implements java.io.Serializable {

	// Fields
	private PtCommissionsId id ;
	private Timestamp createTime;
	private String remark;
	private Double betall ;
	private Double profitall ;
	private Double couponfee ;
	private Double ximafee ;
	
	public PtCommissionRecords() {
		super();
	}
	public PtCommissionRecords(PtCommissionsId id) {
		super();
		this.id = id;
	}
	@EmbeddedId
	@javax.persistence.Column(name = "id")
	public PtCommissionsId getId() {
		return id;
	}
	public void setId(PtCommissionsId id) {
		this.id = id;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getBetall() {
		return betall;
	}
	public void setBetall(Double betall) {
		this.betall = betall;
	}
	public Double getProfitall() {
		return profitall;
	}
	public void setProfitall(Double profitall) {
		this.profitall = profitall;
	}
	public Double getCouponfee() {
		return couponfee;
	}
	public void setCouponfee(Double couponfee) {
		this.couponfee = couponfee;
	}
	public Double getXimafee() {
		return ximafee;
	}
	public void setXimafee(Double ximafee) {
		this.ximafee = ximafee;
	}

}