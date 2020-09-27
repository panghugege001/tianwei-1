package dfh.model;

import java.io.Serializable;

public class PreferentialConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 编号
	private Long id;
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
	private String startTime;
	// 启用结束时间
	private String endTime;
	// 优惠使用次数
	private Integer times;
	// 优惠次数类别，1:天 2:周 3:月 4:年，默认为1
	private Integer timesFlag;
	// 等级
	private String vip;
	// 存款额
	private Double depositAmount;
	// 存款开始时间
	private String depositStartTime;
	// 存款结束时间
	private String depositEndTime;
	// 输赢值
	private Double betAmount;
	// 输赢开始时间
	private String betStartTime;
	// 输赢结束时间
	private String betEndTime;
	// 创建时间
	private String createTime;
	// 修改时间
	private String updateTime;
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
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	
	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	
	public String getAliasTitle() {
		return aliasTitle;
	}
	
	public void setAliasTitle(String aliasTitle) {
		this.aliasTitle = aliasTitle;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	public Integer getBetMultiples() {
		return betMultiples;
	}

	public void setBetMultiples(Integer betMultiples) {
		this.betMultiples = betMultiples;
	}

	public Double getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(Double limitMoney) {
		this.limitMoney = limitMoney;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getTimesFlag() {
		return timesFlag;
	}

	public void setTimesFlag(Integer timesFlag) {
		this.timesFlag = timesFlag;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public String getDepositStartTime() {
		return depositStartTime;
	}

	public void setDepositStartTime(String depositStartTime) {
		this.depositStartTime = depositStartTime;
	}

	public String getDepositEndTime() {
		return depositEndTime;
	}

	public void setDepositEndTime(String depositEndTime) {
		this.depositEndTime = depositEndTime;
	}

	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}

	public String getBetStartTime() {
		return betStartTime;
	}

	public void setBetStartTime(String betStartTime) {
		this.betStartTime = betStartTime;
	}

	public String getBetEndTime() {
		return betEndTime;
	}

	public void setBetEndTime(String betEndTime) {
		this.betEndTime = betEndTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(String isPhone) {
		this.isPhone = isPhone;
	}
	
	public String getIsPassSms() {
		return isPassSms;
	}
	
	public void setIsPassSms(String isPassSms) {
		this.isPassSms = isPassSms;
	}

	public Integer getMachineCodeEnabled() {
		return machineCodeEnabled;
	}

	public void setMachineCodeEnabled(Integer machineCodeEnabled) {
		this.machineCodeEnabled = machineCodeEnabled;
	}

	public Integer getMachineCodeTimes() {
		return machineCodeTimes;
	}

	public void setMachineCodeTimes(Integer machineCodeTimes) {
		this.machineCodeTimes = machineCodeTimes;
	}

	public Double getLowestAmount() {
		return lowestAmount;
	}
	
	public void setLowestAmount(Double lowestAmount) {
		this.lowestAmount = lowestAmount;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}