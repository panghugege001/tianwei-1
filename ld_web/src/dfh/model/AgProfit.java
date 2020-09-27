package dfh.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Proposal entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "agprofit", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AgProfit implements java.io.Serializable {

	// Fields

	private String pno;
	private String proposer;
	private Date createTime;
	private Integer type;
	private Integer quickly;
	private String loginname;
	private Double amount;
	private String agent;
	private Integer flag;
	private String whereisfrom;
	private String remark;
	private String generateType;
	private String platform;
	private Double bettotal=0.00;

	private String tempCreateTime;
	// Constructors
    @Transient
	public String getTempCreateTime() {
		return tempCreateTime;
	}

	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}

	/** default constructor */
	public AgProfit() {
	}

	/** minimal constructor */
	public AgProfit(String pno, String proposer, Date createTime, Integer type, Integer quickly, Integer flag, String whereisfrom,String agent) {
		this.pno = pno;
		this.proposer = proposer;
		this.createTime = createTime;
		this.type = type;
		this.quickly = quickly;
		this.flag = flag;
		this.whereisfrom = whereisfrom;
		this.agent=agent;
	}
	
	/** minimal constructor */
	public AgProfit(String pno, String proposer, Date createTime, Integer type, Integer quickly, String loginname, Double amount, String agent, Integer flag, String whereisfrom, String remark) {
		this.pno = pno;
		this.proposer = proposer;
		this.createTime = createTime;
		this.type = type;
		this.quickly = quickly;
		this.loginname = loginname;
		this.amount = amount;
		this.agent = agent;
		this.flag = flag;
		this.whereisfrom = whereisfrom;
		this.remark = remark;
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
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@javax.persistence.Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@javax.persistence.Column(name = "quickly")
	public Integer getQuickly() {
		return this.quickly;
	}

	public void setQuickly(Integer quickly) {
		this.quickly = quickly;
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

	@javax.persistence.Column(name = "agent")
	public String getAgent() {
		return this.agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
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

	@javax.persistence.Column(name = "generateType")
	public String getGenerateType() {
		return this.generateType;
	}

	public void setGenerateType(String generateType) {
		this.generateType = generateType;
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

}