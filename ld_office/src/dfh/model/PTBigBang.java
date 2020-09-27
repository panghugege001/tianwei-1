package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * PT 大爆炸
 *
 */
@Entity
@Table(name = "ptbigbang", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PTBigBang implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String username;               //用户名
	private Double netWinOrLose;           //纯赢 或 纯输
	private Double bonus;                  //红利
	private Date startTime;                //开始时间
	private Date endTime;                  //结束时间
	private Date createTime;               //创建时间
	private String status;                 //状态
	private Double giftMoney;              //礼金
	private Integer times;                 //流水倍数
	private Date distributeTime;           //派发时间
	private Date getTime;                  //领取时间
	private Double betAmount;              //领取时的投注额
	private String remark;                 //备注
	private String distributeDay;          //派发日期
	
	private String introCode;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)  
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name="netwin_lose")
	public Double getNetWinOrLose() {
		return netWinOrLose;
	}
	public void setNetWinOrLose(Double netWinOrLose) {
		this.netWinOrLose = netWinOrLose;
	}
	
	@Column(name="bonus")
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	
	@Column(name="startTime")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Column(name="endTime")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name="createTime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="giftMoney")
	public Double getGiftMoney() {
		return giftMoney;
	}
	public void setGiftMoney(Double giftMoney) {
		this.giftMoney = giftMoney;
	}
	
	@Column(name="times")
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	
	@Column(name="distributeTime")
	public Date getDistributeTime() {
		return distributeTime;
	}
	public void setDistributeTime(Date distributeTime) {
		this.distributeTime = distributeTime;
	}
	
	@Column(name="getTime")
	public Date getGetTime() {
		return getTime;
	}
	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}
	
	@Column(name="betAmount")
	public Double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="distributeDay")
	public String getDistributeDay() {
		return distributeDay;
	}
	public void setDistributeDay(String distributeDay) {
		this.distributeDay = distributeDay;
	}
	
	@Transient
	public String getIntroCode() {
		return introCode;
	}
	public void setIntroCode(String introCode) {
		this.introCode = introCode;
	}
}
