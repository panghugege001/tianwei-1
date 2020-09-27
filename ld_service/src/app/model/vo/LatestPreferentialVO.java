package app.model.vo;

import java.io.Serializable;

public class LatestPreferentialVO extends BaseVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// 编号
	private Integer id;
	// 优惠类型
	private String type;
	// 是否最新优惠，0:否 1:是
	private String isNew;
	// 是否开启，0:禁用 1:开启
	private String isActive;
	// 是否手机端优惠，0:否 1:是
	private String isPhone;
	// 配置项编码
    private String typeNo;
 	// 是否禁用，是/否
    private String flag;
    // 升序/降序
    private String order;
	// 排序字段
    private String by;
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
 	// 最新优惠图片路径
 	private String newImageUrl;
    // 专题优惠，论坛优惠需要跳转到网页，该字段为网页URL
 	private String openUrl;
 	//是否为龙虎风采
 	private String isQyStyle;
 	
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

	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
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

	public String getTypeNo() {
		return typeNo;
	}

	public void setTypeNo(String typeNo) {
		this.typeNo = typeNo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public String getOpenUrl() {
		return openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}

	public String getIsQyStyle() {
		return isQyStyle;
	}

	public void setIsQyStyle(String isQyStyle) {
		this.isQyStyle = isQyStyle;
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

	public String getNewImageUrl() {
		return newImageUrl;
	}

	public void setNewImageUrl(String newImageUrl) {
		this.newImageUrl = newImageUrl;
	}
	
}