package com.nnti.personal.model.dto;

import com.nnti.common.model.dto.CommonDTO;

public class SelfExperienceDTO extends CommonDTO {

	// 自助体验金平台
	private String platform;
	// 申请通道 1官网、2WEB、3安卓APP、4苹果APP
	private String channel;
	// 存储条件类型
	private String conditionType;
	// type 1 下载彩金 0：自助体验金
	private String type;

	// 主题
	private String title;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}