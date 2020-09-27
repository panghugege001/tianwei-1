package app.model.vo;

public class LatestPreferentialVO {
	
	// 编号
	private Integer id;
	// 优惠类型
	private String type;
	// 活动标题
	private String activityTitle;
	// 活动简介
	private String activitySummary;
	// 活动内容
	private String activityContent;
	// 活动图片路径
	private String activityImageUrl;
	// 活动开始时间
	private String activityStartTime;
	// 活动结束时间
	private String activityEndTime;
	// 是否最新优惠，0:否 1:是
	private String isNew;
	// 最新优惠图片路径
	private String newImageUrl;
	// 是否开启，0:禁用 1:开启
	private String isActive;
	// 是否手机端优惠，0:否 1:是
	private String isPhone;
	// 领取人数
	private Integer receiveNumber;
	// 创建时间
	private String createTime;
	// 创建人
	private String createdUser;
	// 修改时间
	private String updateTime;
	// 修改人
	private String updatedUser;
	
	//新增字段
	private String openUrl;
	private String openType;
	
	public String getOpenUrl() {
		return openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public String getActivitySummary() {
		return activitySummary;
	}

	public void setActivitySummary(String activitySummary) {
		this.activitySummary = activitySummary;
	}

	public String getActivityContent() {
		return activityContent;
	}

	public void setActivityContent(String activityContent) {
		this.activityContent = activityContent;
	}

	public String getActivityImageUrl() {
		return activityImageUrl;
	}

	public void setActivityImageUrl(String activityImageUrl) {
		this.activityImageUrl = activityImageUrl;
	}

	public String getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(String activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public String getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(String activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	public String getNewImageUrl() {
		return newImageUrl;
	}

	public void setNewImageUrl(String newImageUrl) {
		this.newImageUrl = newImageUrl;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(String isPhone) {
		this.isPhone = isPhone;
	}

	public Integer getReceiveNumber() {
		return receiveNumber;
	}

	public void setReceiveNumber(Integer receiveNumber) {
		this.receiveNumber = receiveNumber;
	}
	
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
	
}