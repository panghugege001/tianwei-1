package dfh.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Proposal entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "proposal", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Proposal implements java.io.Serializable {

	// Fields

	private String pno;
	private String proposer;
	private Date createTime;
	private Date executeTime;
	private Integer type;
	private Integer quickly;
	private String loginname;
	private Double amount;
	private String agent;
	private Integer flag;
	private String whereisfrom;
	private String remark;
	private String generateType;
	private Double afterLocalAmount;
	private Double afterRemoteAmount;
	private Double afterAgRemoteAmount;
	private Double afterAgInRemoteAmount;
	private Double afterBbinRemoteAmount;
	private Double afterKenoRemoteAmount;
	private Double afterSkyRemoteAmount;
	private Double afterSbRemoteAmount;
	private String bankaccount;
	private String saveway;
	private String bankname;
	private Integer msflag=0;
	private Integer mstype=0;
	private Integer passflag=0;
	private Integer unknowflag=1;
	// Constructors
	private String tempCreateTime;
	private Double gifTamount;
	private String betMultiples;
	private String shippingCode;
	
	@Transient
	public String getTempCreateTime() {
		return tempCreateTime;
	}

	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}

	/** default constructor */
	public Proposal() {
	}

	/** minimal constructor */
	public Proposal(String pno, String proposer, Date createTime, Integer type, Integer quickly, Integer flag, String whereisfrom,String agent) {
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
	public Proposal(String pno, String proposer, Date createTime, Integer type, Integer quickly, String loginname, Double amount, String agent, Integer flag, String whereisfrom, String remark,
			String generateType) {
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
		this.generateType = generateType;
	}

	/** full constructor */
	public Proposal(String pno, String proposer, Date createTime, Integer type, Integer quickly, String loginname, Double amount, String agent, Integer flag, String whereisfrom, String remark,
			String generateType, Double afterLocalAmount, Double afterRemoteAmount) {
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
		this.generateType = generateType;
		this.afterLocalAmount = afterLocalAmount;
		this.afterRemoteAmount = afterRemoteAmount;
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
	
	
	@javax.persistence.Column(name = "executeTime")
	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
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

	@javax.persistence.Column(name = "afterLocalAmount")
	public Double getAfterLocalAmount() {
		return this.afterLocalAmount;
	}

	public void setAfterLocalAmount(Double afterLocalAmount) {
		this.afterLocalAmount = afterLocalAmount;
	}

	@javax.persistence.Column(name = "afterRemoteAmount")
	public Double getAfterRemoteAmount() {
		return this.afterRemoteAmount;
	}

	public void setAfterRemoteAmount(Double afterRemoteAmount) {
		this.afterRemoteAmount = afterRemoteAmount;
	}

	@javax.persistence.Column(name = "bankaccount")
	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	@javax.persistence.Column(name = "saveway")
	public String getSaveway() {
		return saveway;
	}

	public void setSaveway(String saveway) {
		this.saveway = saveway;
	}
	@javax.persistence.Column(name = "bankname")
	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	
	@javax.persistence.Column(name = "msflag")
	public Integer getMsflag() {
		return msflag;
	}

	public void setMsflag(Integer msflag) {
		this.msflag = msflag;
	}

	@javax.persistence.Column(name = "mstype")
	public Integer getMstype() {
		return mstype;
	}

	public void setMstype(Integer mstype) {
		this.mstype = mstype;
	}
	@javax.persistence.Column(name = "afterAgRemoteAmount")
	public Double getAfterAgRemoteAmount() {
		return afterAgRemoteAmount;
	}

	public void setAfterAgRemoteAmount(Double afterAgRemoteAmount) {
		this.afterAgRemoteAmount = afterAgRemoteAmount;
	}
	@javax.persistence.Column(name = "afterBbinRemoteAmount")
	public Double getAfterBbinRemoteAmount() {
		return afterBbinRemoteAmount;
	}

	public void setAfterBbinRemoteAmount(Double afterBbinRemoteAmount) {
		this.afterBbinRemoteAmount = afterBbinRemoteAmount;
	}
	@javax.persistence.Column(name = "afterKenoRemoteAmount")
	public Double getAfterKenoRemoteAmount() {
		return afterKenoRemoteAmount;
	}

	public void setAfterKenoRemoteAmount(Double afterKenoRemoteAmount) {
		this.afterKenoRemoteAmount = afterKenoRemoteAmount;
	}
	@javax.persistence.Column(name = "passflag")
	public Integer getPassflag() {
		return passflag;
	}

	public void setPassflag(Integer passflag) {
		this.passflag = passflag;
	}
	@javax.persistence.Column(name = "unknowflag")
	public Integer getUnknowflag() {
		return unknowflag;
	}

	public void setUnknowflag(Integer unknowflag) {
		this.unknowflag = unknowflag;
	}
	
	@javax.persistence.Column(name = "giftamount")
	public Double getGifTamount() {
		return gifTamount;
	}

	public void setGifTamount(Double gifTamount) {
		this.gifTamount = gifTamount;
	}

	@javax.persistence.Column(name = "betmultiples")
	public String getBetMultiples() {
		return betMultiples;
	}

	public void setBetMultiples(String betMultiples) {
		this.betMultiples = betMultiples;
	}

	@javax.persistence.Column(name = "shippingcode")
	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	@javax.persistence.Column(name = "afterAgInRemoteAmount")
	public Double getAfterAgInRemoteAmount() {
		return afterAgInRemoteAmount;
	}

	public void setAfterAgInRemoteAmount(Double afterAgInRemoteAmount) {
		this.afterAgInRemoteAmount = afterAgInRemoteAmount;
	}

	@javax.persistence.Column(name = "afterSkyRemoteAmount")
	public Double getAfterSkyRemoteAmount() {
		return afterSkyRemoteAmount;
	}

	public void setAfterSkyRemoteAmount(Double afterSkyRemoteAmount) {
		this.afterSkyRemoteAmount = afterSkyRemoteAmount;
	}
	@javax.persistence.Column(name = "afterSbRemoteAmount")
	public Double getAfterSbRemoteAmount() {
		return afterSbRemoteAmount;
	}

	public void setAfterSbRemoteAmount(Double afterSbRemoteAmount) {
		this.afterSbRemoteAmount = afterSbRemoteAmount;
	}
	
	

}