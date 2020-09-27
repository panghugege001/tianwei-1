package dfh.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "weeksent", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class WeekSent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pno;                //提案号
	private String username;           //用户名
	private Double amount;             //上周投注额
	private Double promo;              //赠送金额
	private int times;                 //提款所需流水倍数
	private String status;             //状态（0：未领取   1：已领取  2：未领取）
	private Double betting;            //已投注额(0点到领取优惠时platform的游戏总投注额)
	private Date createTime;           //创建时间
	private Date getTime;              //领取时间
	private String remark;             //备注
	private String platform;           //平台
	private Date promoDateStart;       //上周的开始日期（上周一）
	private Date promoDateEnd;		   //上周的结束日期（上周日）
	
	private String tempCreateTime;
	
	@Transient
	public String getTempCreateTime() {
		return tempCreateTime;
	}

	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}
	
	public WeekSent(){}
	
	public WeekSent(String username, double amount, double promo, int times, String status, Date createTime, String platform, Date promoDateStart, Date promoDateEnd, String pno, String remark){
		this.username = username;
		this.amount = amount;
		this.promo = promo;
		this.times = times;
		this.status = status;
		this.createTime = createTime;
		this.platform = platform;
		this.promoDateStart = promoDateStart;
		this.promoDateEnd = promoDateEnd;
		this.pno = pno;
		this.remark = remark;
	}
	
	@Id
	@Column(name="pno")
	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}
	
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name="amount")
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@Column(name="promo")
	public Double getPromo() {
		return promo;
	}
	public void setPromo(Double promo) {
		this.promo = promo;
	}
	
	@Column(name="times")
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="betting")
	public Double getBetting() {
		return betting;
	}
	public void setBetting(Double betting) {
		this.betting = betting;
	}	
	
	@Column(name="creattime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="gettime")
	public Date getGetTime() {
		return getTime;
	}
	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="platform")
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Column(name="promodatestart")
	public Date getPromoDateStart() {
		return promoDateStart;
	}

	public void setPromoDateStart(Date promoDateStart) {
		this.promoDateStart = promoDateStart;
	}

	@Column(name="promodateend")
	public Date getPromoDateEnd() {
		return promoDateEnd;
	}

	public void setPromoDateEnd(Date promoDateEnd) {
		this.promoDateEnd = promoDateEnd;
	}

}
