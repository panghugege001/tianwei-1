package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Commissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ptcommissions", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PtCommissions implements java.io.Serializable {

	// Fields
	private PtCommissionsId id ;
	private Double amount;
	private Double percent;
	private Integer subCount;
	private Integer activeuser;
	private Integer flag;
	private Timestamp createTime;
	private Timestamp excuteTime;
	private String remark;
	
	private Double betall ;
	private Double profitall ;
	private Double platformfee ;
	private Double couponfee ;
	private Double ximafee ;
	private String tempExcuteTime;
	private Double progressive_bets ;
	private Double depositfee=0.0;

	public Double getProgressive_bets() {
		return progressive_bets;
	}

	public void setProgressive_bets(Double progressive_bets) {
		this.progressive_bets = progressive_bets;
	}

	public Double getDepositfee() {
		return depositfee;
	}

	public void setDepositfee(Double depositfee) {
		this.depositfee = depositfee;
	}

	@Transient
	public String getTempExcuteTime() {
		return tempExcuteTime;
	}
	public void setTempExcuteTime(String tempExcuteTime) {
		this.tempExcuteTime = tempExcuteTime;
	}
	
	@EmbeddedId
	@javax.persistence.Column(name = "id")
	public PtCommissionsId getId() {
		return id;
	}
	public void setId(PtCommissionsId id) {
		this.id = id;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Integer getSubCount() {
		return subCount;
	}
	public void setSubCount(Integer subCount) {
		this.subCount = subCount;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getExcuteTime() {
		return excuteTime;
	}
	public void setExcuteTime(Timestamp excuteTime) {
		this.excuteTime = excuteTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getActiveuser() {
		return activeuser;
	}
	public void setActiveuser(Integer activeuser) {
		this.activeuser = activeuser;
	}
	public Double getPercent() {
		return percent;
	}
	public void setPercent(Double percent) {
		this.percent = percent;
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
	public Double getPlatformfee() {
		return platformfee;
	}
	public void setPlatformfee(Double platformfee) {
		this.platformfee = platformfee;
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