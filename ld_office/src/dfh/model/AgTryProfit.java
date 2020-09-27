package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Proposal entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "agtryprofit", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AgTryProfit implements java.io.Serializable {

	// Fields

	private Integer id;
	private Timestamp createTime;
	private String loginname;
	private Double amount;
	private String remark;
	private String platform;
	private Double bettotal=0.00;
	private Integer betnum;

	// Constructors

	/** default constructor */
	public AgTryProfit() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	@javax.persistence.Column(name = "createTime")
	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
	@javax.persistence.Column(name = "platform")
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@javax.persistence.Column(name = "bettotal")
	public Double getBettotal() {
		return bettotal;
	}

	public void setBettotal(Double bettotal) {
		this.bettotal = bettotal;
	}

	public AgTryProfit(Timestamp createTime, String loginname, Double amount,
			String remark, String platform, Double bettotal,Integer betnum) {
		this.createTime = createTime;
		this.loginname = loginname;
		this.amount = amount;
		this.remark = remark;
		this.platform = platform;
		this.bettotal = bettotal;
		this.betnum = betnum;
	}
	@javax.persistence.Column(name = "betnum")
	public Integer getBetnum() {
		return betnum;
	}

	public void setBetnum(Integer betnum) {
		this.betnum = betnum;
	}
}