package com.nnti.personal.model.dto;

import java.util.Date;
import com.nnti.common.model.dto.CommonDTO;

public class TopicInfoDTO extends CommonDTO {

	private Integer topicId;
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
	//批量删除的id
	private String ids;
	
	
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
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
}