package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ExperienceGoldConfig entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "experience_gold_t", catalog = "tianwei")
public class ExperienceGoldConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	// 优惠类型
	private String title;
	// 体验金额度
	private Double amount;
	// 限制最大金额
	private Double maxMoney;
	// 限制最小金额
	private Double minMoney;
	private Date createTime;
	private Date updateTime;
	private Integer isUsed;

	private Date startTime;
	private Date endTime;
	private Integer times;
	// 1.天2.周3.月4.年
	private Integer timesFlag;
	// 等级
	private String vip;
	// 标题
	private String aliasTitle;
	// 游戏平台
	private String platformName;

	// 申请通道 1官网、2WEB、3安卓APP、4苹果APP
	private String isPhone;
	// 是否启用机器码验证，0:否/1:是
	private Integer machineCodeEnabled;
	// 机器码使用次数
	private Integer machineCodeTimes;

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

	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@javax.persistence.Column(name = "max_money")
	public Double getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(Double maxMoney) {
		this.maxMoney = maxMoney;
	}

	@javax.persistence.Column(name = "min_money")
	public Double getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(Double minMoney) {
		this.minMoney = minMoney;
	}

	@javax.persistence.Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@javax.persistence.Column(name = "is_used")
	public Integer getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}

	@javax.persistence.Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@javax.persistence.Column(name = "end_time")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@javax.persistence.Column(name = "times")
	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	@javax.persistence.Column(name = "times_flag")
	public Integer getTimesFlag() {
		return timesFlag;
	}

	public void setTimesFlag(Integer timesFlag) {
		this.timesFlag = timesFlag;
	}

	@javax.persistence.Column(name = "vip")
	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	@javax.persistence.Column(name = "alias_Title")
	public String getAliasTitle() {
		return aliasTitle;
	}

	public void setAliasTitle(String aliasTitle) {
		this.aliasTitle = aliasTitle;
	}

	@javax.persistence.Column(name = "platform_name")
	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	@javax.persistence.Column(name = "machine_code_enabled")
	public Integer getMachineCodeEnabled() {
		return machineCodeEnabled;
	}

	public void setMachineCodeEnabled(Integer machineCodeEnabled) {
		this.machineCodeEnabled = machineCodeEnabled;
	}

	@javax.persistence.Column(name = "machine_code_times")
	public Integer getMachineCodeTimes() {
		return machineCodeTimes;
	}

	public void setMachineCodeTimes(Integer machineCodeTimes) {
		this.machineCodeTimes = machineCodeTimes;
	}
	
	@javax.persistence.Column(name = "is_phone")
	public String getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(String isPhone) {
		this.isPhone = isPhone;
	}
	
	

}
