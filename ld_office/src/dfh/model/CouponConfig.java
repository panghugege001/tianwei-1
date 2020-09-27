package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coupon_config", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class CouponConfig implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// 编号，主键，自动增长
	private Long id;
	// 游戏平台编号
	private String platformId;
	// 游戏平台名称
	private String platformName;
	// 优惠券类型(419:红包优惠券/319:存送优惠券)
	private String couponType;
	// 优惠券代码
	private String couponCode;
	// 赠送百分比
	private Double percent;
	// 赠送金额
	private Double giftAmount;
	// 流水倍数
	private Integer betMultiples;
	// 最低转账金额
	private Double minAmount;
	// 最高转账金额
	private Double maxAmount;
	// 赠送金额上限
	private Double limitMoney;
	// 备注
	private String remark;
	// 状态(0:待审核/1:已审核/2:已领取)
	private String status;
	// 是否已删除(Y:是/N:否)
	private String isDelete;
	// 领取时间
	private Date receiveTime;
	// 领取账号
	private String loginName;
	// 创建时间
	private Date createTime;
	// 创建人
	private String createUser;
	// 删除时间
	private Date deleteTime;
	// 删除人
	private String deleteUser;
	// 审核时间
	private Date auditTime;
	// 审核人
	private String auditUser;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	@javax.persistence.Column(name = "coupon_type")
	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	@javax.persistence.Column(name = "coupon_code")
	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	@javax.persistence.Column(name = "percent")
	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	@javax.persistence.Column(name = "gift_amount")
	public Double getGiftAmount() {
		return giftAmount;
	}

	public void setGiftAmount(Double giftAmount) {
		this.giftAmount = giftAmount;
	}

	@javax.persistence.Column(name = "bet_multiples")
	public Integer getBetMultiples() {
		return betMultiples;
	}

	public void setBetMultiples(Integer betMultiples) {
		this.betMultiples = betMultiples;
	}

	@javax.persistence.Column(name = "min_amount")
	public Double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}

	@javax.persistence.Column(name = "max_amount")
	public Double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}

	@javax.persistence.Column(name = "limit_money")
	public Double getLimitMoney() {
		return limitMoney;
	}

	public void setLimitMoney(Double limitMoney) {
		this.limitMoney = limitMoney;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@javax.persistence.Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@javax.persistence.Column(name = "is_delete")
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@javax.persistence.Column(name = "receive_time")
	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	@javax.persistence.Column(name = "login_name")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@javax.persistence.Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "create_user")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@javax.persistence.Column(name = "delete_time")
	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	@javax.persistence.Column(name = "delete_user")
	public String getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}

	@javax.persistence.Column(name = "audit_time")
	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	@javax.persistence.Column(name = "audit_user")
	public String getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(String auditUser) {
		this.auditUser = auditUser;
	}
}