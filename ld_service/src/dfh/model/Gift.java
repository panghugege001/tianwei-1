package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gift", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Gift implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String type; //类型
	private String title; //标题
	private Double depositAmount; //最低存款额
	private Date startTimeDeposit; //存款开始时间
	private Date endTimeDeposit; //存款结束时间
	private Double betAmount; //最低投注额
	private Date startTimeBet; //投注开始时间
	private Date endTimeBet; //投注结束时间
	private Date startTime; //活动开放时间
	private Date endTime; //活动结束时间
	private String levels; //开放等级
	private Date createTime; //创建时间
	private String status;   //状态
	private String remark; //备注

	public Gift() {
	}

	public Gift(String type, String title, Double depositAmount, Date startTimeDeposit, Date endTimeDeposit, Double betAmount,
			Date startTimeBet, Date endTimeBet, Date startTime, Date endTime, String levels, Date createTime, String remark) {
		this.type = type;
		this.title = title;
		this.depositAmount = depositAmount;
		this.startTimeDeposit = startTimeDeposit;
		this.endTimeDeposit = endTimeDeposit;
		this.betAmount = betAmount;
		this.startTimeBet = startTimeBet;
		this.endTimeBet = endTimeBet;
		this.startTime = startTime;
		this.endTime = endTime;
		this.levels = levels;
		this.createTime = createTime;
		this.remark = remark;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name="type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name="title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="depositAmount")
	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	@Column(name="startTime_deposit")
	public Date getStartTimeDeposit() {
		return startTimeDeposit;
	}

	public void setStartTimeDeposit(Date startTimeDeposit) {
		this.startTimeDeposit = startTimeDeposit;
	}

	@Column(name="endTime_deposit")
	public Date getEndTimeDeposit() {
		return endTimeDeposit;
	}

	public void setEndTimeDeposit(Date endTimeDeposit) {
		this.endTimeDeposit = endTimeDeposit;
	}

	@Column(name="betAmount")
	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}

	@Column(name="startTime_bet")
	public Date getStartTimeBet() {
		return startTimeBet;
	}

	public void setStartTimeBet(Date startTimeBet) {
		this.startTimeBet = startTimeBet;
	}

	@Column(name="endTime_bet")
	public Date getEndTimeBet() {
		return endTimeBet;
	}

	public void setEndTimeBet(Date endTimeBet) {
		this.endTimeBet = endTimeBet;
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

	@Column(name="levels")
	public String getLevels() {
		return levels;
	}

	public void setLevels(String levels) {
		this.levels = levels;
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
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	 
}
