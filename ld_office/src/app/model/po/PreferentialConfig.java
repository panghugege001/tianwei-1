package app.model.po;

import static javax.persistence.GenerationType.IDENTITY;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "preferential_config", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PreferentialConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 编号
	private Integer id;
	// 游戏平台编号
	private String platformId;
	// 游戏平台名称
	private String platformName;
	// 自助优惠类型编号
	private String titleId;
	// 自助优惠类型名称
	private String titleName;
	// 自助优惠名称
	private String aliasTitle;
	// 优惠百分比
	private Double percent;
	// 流水倍数要求
	private Integer betMultiples;
	// 限额
	private Double limitMoney;
	// 体验金额度
	private Double amount;
	// 是否开启使用，0:关闭 1:开启，默认为1
	private Integer isUsed;
	// 启用开始时间
	private Date startTime;
	// 最高转账金额
	private Double highestAmount;
	// 启用结束时间
	private Date endTime;
	// 优惠使用次数
	private Integer times;
	// 优惠次数类别，1:天 2:周 3:月 4:年，默认为1
	private Integer timesFlag;
	// 等级
	private String vip;
	// 存款额
	private Double depositAmount;
	// 存款开始时间
	private Date depositStartTime;
	// 存款结束时间
	private Date depositEndTime;
	// 输赢值
	private Double betAmount;
	// 输赢开始时间
	private Date betStartTime;
	// 输赢结束时间
	private Date betEndTime;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date updateTime;
	// 删除标志，0:已删除 1:未删除，默认为1
	private Integer deleteFlag;
	// 申请通道(1:官网/2:WEB/3:安卓APP/4:苹果APP)
	private String isPhone;
	// 是否让未通过短信反向验证的玩家使用，0:否/1:是
	private String isPassSms;
	// 是否启用机器码验证，0:否/1:是
	private Integer machineCodeEnabled;
	// 机器码使用次数
	private Integer machineCodeTimes;
	// 最低转账金额
	private Double lowestAmount;
	// 优惠互斥组别
	private String groupId;
	// 互斥组别申请次数
	private Integer mutexTimes;
	// 编号字符串，用于存储多个编号
	private String ids;
	// 启用开始时间(字符串形式)
	private String startTimeStr;
	// 启用结束时间(字符串形式)
	private String endTimeStr;
	// 存款开始时间(字符串形式)
	private String depositStartTimeStr;
	// 存款结束时间(字符串形式)
	private String depositEndTimeStr;
	// 输赢开始时间(字符串形式)
	private String betStartTimeStr;
	// 输赢结束时间(字符串形式)
	private String betEndTimeStr;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
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

	@javax.persistence.Column(name = "title_id")
	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	@javax.persistence.Column(name = "title_name")
	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	@javax.persistence.Column(name = "highest_amount")
	public Double getHighestAmount() {
		return highestAmount;
	}

	public void setHighestAmount(Double highestAmount) {
		this.highestAmount = highestAmount;
	}
	
	@javax.persistence.Column(name = "alias_title")
	public String getAliasTitle() {
		return aliasTitle;
	}
	
	public void setAliasTitle(String aliasTitle) {
		this.aliasTitle = aliasTitle;
	}

	@javax.persistence.Column(name = "percent")
	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	@javax.persistence.Column(name = "bet_multiples")
	public Integer getBetMultiples() {
		return betMultiples;
	}

	public void setBetMultiples(Integer betMultiples) {
		this.betMultiples = betMultiples;
	}

	@javax.persistence.Column(name = "limit_money")
	public Double getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(Double limitMoney) {
		this.limitMoney = limitMoney;
	}

	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
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

	@javax.persistence.Column(name = "delete_flag")
	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	@javax.persistence.Column(name = "is_phone")
	public String getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(String isPhone) {
		this.isPhone = isPhone;
	}
	
	@javax.persistence.Column(name = "is_pass_sms")
	public String getIsPassSms() {
		return isPassSms;
	}

	public void setIsPassSms(String isPassSms) {
		this.isPassSms = isPassSms;
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

	@javax.persistence.Column(name = "lowest_amount")
	public Double getLowestAmount() {
		return lowestAmount;
	}

	public void setLowestAmount(Double lowestAmount) {
		this.lowestAmount = lowestAmount;
	}

	@Transient
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
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
	
	@javax.persistence.Column(name = "group_id")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@javax.persistence.Column(name = "mutex_times")
	public Integer getMutexTimes() {
		return mutexTimes;
	}

	public void setMutexTimes(Integer mutexTimes) {
		this.mutexTimes = mutexTimes;
	}
}