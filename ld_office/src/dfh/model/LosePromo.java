package dfh.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "losepromo", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class LosePromo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pno;                //提案号
	private String username;           //用户名
	private Double amount;             //输赢额
	private Double deduct;             //扣款（已领取的优惠）	
	private Double rate;               //反赠比例
	private Double promo;              //反赠金额
	private int times;                 //提款所需流水倍数
	private String status;             //状态（0：未领取   1：已领取  2：未领取）	
	private Double betting;            //已投注额(0点到领取优惠时PT游戏总投注额)
	private Date createTime;           //创建时间
	private Date getTime;              //领取时间
	private String promoDate;            //反赠日期（派发的是哪一天的反赠）
	private String remark;             //备注
	private String platform;           //平台
	
	
	public LosePromo(){}
	
	public LosePromo(String username, double amount, double deduct, double rate, double promo, int times, String status, Date createTime, String promoDate, String platform, String pno, String remark){
		this.username = username;
		this.amount = amount;
		this.deduct = deduct;
		this.rate = rate;
		this.promo = promo;
		this.times = times;
		this.status = status;
		this.createTime = createTime;
		this.promoDate = promoDate;
		this.platform = platform;
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
	
	@Column(name="deduct")
	public Double getDeduct() {
		return deduct;
	}
	public void setDeduct(Double deduct) {
		this.deduct = deduct;
	}
	
	@Column(name="rate")
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
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
	
	@Column(name="promodate")
	public String getPromoDate() {
		return promoDate;
	}
	public void setPromoDate(String promoDate) {
		this.promoDate = promoDate;
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

}
