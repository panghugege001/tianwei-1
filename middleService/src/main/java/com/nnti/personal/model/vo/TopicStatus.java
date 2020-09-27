package com.nnti.personal.model.vo;

import java.util.Date;

public class TopicStatus{
	
	//
	private Integer id;
	// 主题id
	private Integer topicId;
	// 接收人
	private String receiveUname;
	// ip地址
	private String ipAddress;
	// 用户是否已读
	private Integer isUserRead;
	// 创建时间
	private Date createTime;
	// 是否有效
	private Integer isValid;
	//
	private String title;
	//
	private String content;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getReceiveUname() {
		return receiveUname;
	}

	public void setReceiveUname(String receiveUname) {
		this.receiveUname = receiveUname;
	}

	public Integer getIsUserRead() {
		return isUserRead;
	}

	public void setIsUserRead(Integer isUserRead) {
		this.isUserRead = isUserRead;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
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
}