package com.nnti.personal.model.vo;

import java.util.Date;
import com.nnti.common.utils.DateUtil;

public class TopicInfo {
	
	//
	private Integer id;
	// 主题
	private String title;
	// 内容
	private String content;
	// 创建时间
	private Date createTime;
	// 创建人
	private String createUname;
	// 创建人ip地址
	private String ipAddress;
	// 是否审批
	private Integer flag;
	// 回复数量
	private Integer reCount;
	// 发帖类型
	private Integer topicType;
	// 发帖对象
	private Integer userNameType;
	// 群发
	private Integer topicStatus;
	//管理员是否已读 0 未读 1 已读
	private Integer isAdminRead;
	// 创建时间字符串形式
	private String createTimeStr;
	//用户是否已读 0 未读 1 已读
	private Integer isUserRead;
	// 
	private Integer statusId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	
		if (null != this.createTime) {
			
			this.createTimeStr = DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, this.createTime);
		}
	}
	
	public String getCreateUname() {
		return createUname;
	}

	public void setCreateUname(String createUname) {
		this.createUname = createUname;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getReCount() {
		return reCount;
	}

	public void setReCount(Integer reCount) {
		this.reCount = reCount;
	}

	public Integer getTopicType() {
		return topicType;
	}

	public void setTopicType(Integer topicType) {
		this.topicType = topicType;
	}

	public Integer getUserNameType() {
		return userNameType;
	}

	public void setUserNameType(Integer userNameType) {
		this.userNameType = userNameType;
	}

	public Integer getTopicStatus() {
		return topicStatus;
	}

	public void setTopicStatus(Integer topicStatus) {
		this.topicStatus = topicStatus;
	}

	public Integer getIsAdminRead() {
		return isAdminRead;
	}

	public void setIsAdminRead(Integer isAdminRead) {
		this.isAdminRead = isAdminRead;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	public Integer getIsUserRead() {
		return isUserRead;
	}
	
	public void setIsUserRead(Integer isUserRead) {
		this.isUserRead = isUserRead;
	}
	
	public Integer getStatusId() {
		return statusId;
	}
	
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
}