package dfh.model.notdb;

import java.sql.Timestamp;
import java.util.Date;

public class PtCommissionsVo {
	private String agent;
	private Date createdate ;
	private String platform ;
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
	private Double historyfee ;
	private Double slotaccount ;
	
	public PtCommissionsVo() {
		super();
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getPercent() {
		return percent;
	}
	public void setPercent(Double percent) {
		this.percent = percent;
	}
	public Integer getSubCount() {
		return subCount;
	}
	public void setSubCount(Integer subCount) {
		this.subCount = subCount;
	}
	public Integer getActiveuser() {
		return activeuser;
	}
	public void setActiveuser(Integer activeuser) {
		this.activeuser = activeuser;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
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
	public Double getHistoryfee() {
		return historyfee;
	}
	public void setHistoryfee(Double historyfee) {
		this.historyfee = historyfee;
	}
	public Double getSlotaccount() {
		return slotaccount;
	}
	public void setSlotaccount(Double slotaccount) {
		this.slotaccount = slotaccount;
	}

}
