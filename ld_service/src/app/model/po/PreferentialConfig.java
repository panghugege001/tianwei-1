package app.model.po;

import static javax.persistence.GenerationType.IDENTITY;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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
	// 是否手机端优惠，0:否 1:是，默认为1
	private Integer isPhone;

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
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@javax.persistence.Column(name = "end_time")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
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

	@javax.persistence.Column(name = "deposit_amount")
	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	@javax.persistence.Column(name = "deposit_start_time")
	public String getDepositStartTime() {
		return depositStartTime;
	}

	public void setDepositStartTime(String depositStartTime) {
		this.depositStartTime = depositStartTime;
	}

	@javax.persistence.Column(name = "deposit_end_time")
	public String getDepositEndTime() {
		return depositEndTime;
	}

	public void setDepositEndTime(String depositEndTime) {
		this.depositEndTime = depositEndTime;
	}

	@javax.persistence.Column(name = "bet_amount")
	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}

	@javax.persistence.Column(name = "bet_start_time")
	public String getBetStartTime() {
		return betStartTime;
	}

	public void setBetStartTime(String betStartTime) {
		this.betStartTime = betStartTime;
	}

	@javax.persistence.Column(name = "bet_end_time")
	public String getBetEndTime() {
		return betEndTime;
	}

	public void setBetEndTime(String betEndTime) {
		this.betEndTime = betEndTime;
	}

	@javax.persistence.Column(name = "create_time")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "update_time")
	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
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
	public Integer getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(Integer isPhone) {
		this.isPhone = isPhone;
	}
	
}