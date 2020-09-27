package dfh.model;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "B_RED_ENVELOPE_ACTIVITY", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class RedEnvelopeActivity {

	// 编号
	private Integer id;
	// 活动标题
	private String title;
	// 活动最小红利
	private Double minBonus;
	// 活动最大红利
	private Double maxBonus;
	// 等级
	private String vip;
	// 领取次数
	private Integer times;
	// 转入平台编号
	private String platformId;
	// 转入平台名称
	private String platformName;
	// 流水倍数
	private Integer multiples;
	// 存款额
	private Double depositAmount;
	// 存款开始时间
	private Date depositStartTime;
	// 存款结束时间
	private Date depositEndTime;
	// 投注额
	private Double betAmount;
	// 投注开始时间
	private Date betStartTime;
	// 投注结束时间
	private Date betEndTime;
	// 活动开始时间
	private Date startTime;
	// 活动结束时间
	private Date endTime;
	// 删除标志，Y:已删除/N:未删除
	private String deleteFlag;
	// 创建人
	private String createUser;
	// 创建时间
	private Date createTime;
	// 修改人
	private String updateUser;
	// 修改时间
	private Date updateTime;
	// 存款开始时间(字符串形式)
	private String depositStartTimeStr;
	// 存款结束时间(字符串形式)
	private String depositEndTimeStr;
	// 投注开始时间(字符串形式)
	private String betStartTimeStr;
	// 投注结束时间(字符串形式)
	private String betEndTimeStr;
	// 活动开始时间(字符串形式)
	private String startTimeStr;
	// 活动结束时间(字符串形式)
	private String endTimeStr;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@javax.persistence.Column(name = "min_bonus")
	public Double getMinBonus() {
		return minBonus;
	}

	public void setMinBonus(Double minBonus) {
		this.minBonus = minBonus;
	}

	@javax.persistence.Column(name = "max_bonus")
	public Double getMaxBonus() {
		return maxBonus;
	}

	public void setMaxBonus(Double maxBonus) {
		this.maxBonus = maxBonus;
	}

	@javax.persistence.Column(name = "vip")
	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	@javax.persistence.Column(name = "times")
	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	@javax.persistence.Column(name = "platform_id")
	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	@javax.persistence.Column(name = "platform_name")
	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	@javax.persistence.Column(name = "multiples")
	public Integer getMultiples() {
		return multiples;
	}

	public void setMultiples(Integer multiples) {
		this.multiples = multiples;
	}

	@javax.persistence.Column(name = "deposit_amount")
	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	@javax.persistence.Column(name = "deposit_start_time")
	public Date getDepositStartTime() {
		return depositStartTime;
	}

	public void setDepositStartTime(Date depositStartTime) {
		this.depositStartTime = depositStartTime;

		if (null != depositStartTime) {

			this.depositStartTimeStr = app.util.DateUtil.getDateFormat(depositStartTime);
		}
	}

	@javax.persistence.Column(name = "deposit_end_time")
	public Date getDepositEndTime() {
		return depositEndTime;
	}

	public void setDepositEndTime(Date depositEndTime) {
		this.depositEndTime = depositEndTime;

		if (null != depositEndTime) {

			this.depositEndTimeStr = app.util.DateUtil.getDateFormat(depositEndTime);
		}
	}

	@javax.persistence.Column(name = "bet_amount")
	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}

	@javax.persistence.Column(name = "bet_start_time")
	public Date getBetStartTime() {
		return betStartTime;
	}

	public void setBetStartTime(Date betStartTime) {
		this.betStartTime = betStartTime;

		if (null != betStartTime) {

			this.betStartTimeStr = app.util.DateUtil.getDateFormat(betStartTime);
		}
	}

	@javax.persistence.Column(name = "bet_end_time")
	public Date getBetEndTime() {
		return betEndTime;
	}

	public void setBetEndTime(Date betEndTime) {
		this.betEndTime = betEndTime;

		if (null != betEndTime) {

			this.betEndTimeStr = app.util.DateUtil.getDateFormat(betEndTime);
		}
	}

	@javax.persistence.Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;

		if (null != startTime) {

			this.startTimeStr = app.util.DateUtil.getDateFormat(startTime);
		}
	}

	@javax.persistence.Column(name = "end_time")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;

		if (null != endTime) {

			this.endTimeStr = app.util.DateUtil.getDateFormat(endTime);
		}
	}

	@javax.persistence.Column(name = "delete_flag")
	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	@javax.persistence.Column(name = "create_user")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@javax.persistence.Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "update_user")
	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@javax.persistence.Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Transient
	public String getDepositStartTimeStr() {
		return depositStartTimeStr;
	}

	public void setDepositStartTimeStr(String depositStartTimeStr) {
		this.depositStartTimeStr = depositStartTimeStr;
	}

	@Transient
	public String getDepositEndTimeStr() {
		return depositEndTimeStr;
	}

	public void setDepositEndTimeStr(String depositEndTimeStr) {
		this.depositEndTimeStr = depositEndTimeStr;
	}

	@Transient
	public String getBetStartTimeStr() {
		return betStartTimeStr;
	}

	public void setBetStartTimeStr(String betStartTimeStr) {
		this.betStartTimeStr = betStartTimeStr;
	}

	@Transient
	public String getBetEndTimeStr() {
		return betEndTimeStr;
	}

	public void setBetEndTimeStr(String betEndTimeStr) {
		this.betEndTimeStr = betEndTimeStr;
	}

	@Transient
	public String getStartTimeStr() {
		return startTimeStr;
	}

	public void setStartTimeStr(String startTimeStr) {
		this.startTimeStr = startTimeStr;
	}

	@Transient
	public String getEndTimeStr() {
		return endTimeStr;
	}

	public void setEndTimeStr(String endTimeStr) {
		this.endTimeStr = endTimeStr;
	}
}