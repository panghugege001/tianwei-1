package app.model.po;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "latest_preferential", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class LatestPreferential implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// 编号
	private Integer id;
	// 优惠类型编号
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
	private Date activityStartTime;
	// 活动结束时间
	private Date activityEndTime;
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
	private Date createTime;
	// 创建人
	private String createdUser;
	// 修改时间
	private Date updateTime;
	// 修改人
	private String updatedUser;
	// 优惠类型名称
	private String typeName;
	
	// 龙八没有此字段，U乐的专题优惠，论坛优惠需要跳转到网页，该字段为网页URL
	private String openUrl;
	private String isQyStyle;//是否为风采
		
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@javax.persistence.Column(name = "activity_title")
	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	@javax.persistence.Column(name = "activity_summary")
	public String getActivitySummary() {
		return activitySummary;
	}

	public void setActivitySummary(String activitySummary) {
		this.activitySummary = activitySummary;
	}

	@javax.persistence.Column(name = "activity_content")
	public String getActivityContent() {
		return activityContent;
	}

	public void setActivityContent(String activityContent) {
		this.activityContent = activityContent;
	}

	@javax.persistence.Column(name = "activity_image_url")
	public String getActivityImageUrl() {
		return activityImageUrl;
	}

	public void setActivityImageUrl(String activityImageUrl) {
		this.activityImageUrl = activityImageUrl;
	}

	@javax.persistence.Column(name = "activity_start_time")
	public Date getActivityStartTime() {
		return activityStartTime;
	}

	public void setActivityStartTime(Date activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	@javax.persistence.Column(name = "activity_end_time")
	public Date getActivityEndTime() {
		return activityEndTime;
	}

	public void setActivityEndTime(Date activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	@javax.persistence.Column(name = "is_new")
	public String getIsNew() {
		return isNew;
	}

	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}

	@javax.persistence.Column(name = "new_image_url")
	public String getNewImageUrl() {
		return newImageUrl;
	}

	public void setNewImageUrl(String newImageUrl) {
		this.newImageUrl = newImageUrl;
	}

	@javax.persistence.Column(name = "is_active")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@javax.persistence.Column(name = "is_phone")
	public String getIsPhone() {
		return isPhone;
	}

	public void setIsPhone(String isPhone) {
		this.isPhone = isPhone;
	}

	@javax.persistence.Column(name = "receive_number")
	public Integer getReceiveNumber() {
		return receiveNumber;
	}

	public void setReceiveNumber(Integer receiveNumber) {
		this.receiveNumber = receiveNumber;
	}

	@javax.persistence.Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "created_user")
	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	@javax.persistence.Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@javax.persistence.Column(name = "updated_user")
	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	@javax.persistence.Transient
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
	@javax.persistence.Column(name = "openUrl")
	public String getOpenUrl() {
		return openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}

	@javax.persistence.Column(name = "isQyStyle")
	public String getIsQyStyle() {
		return isQyStyle;
	}

	public void setIsQyStyle(String isQyStyle) {
		this.isQyStyle = isQyStyle;
	}


	
}