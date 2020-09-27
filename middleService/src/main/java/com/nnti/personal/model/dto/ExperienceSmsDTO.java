package com.nnti.personal.model.dto;

import java.util.Date;

import com.nnti.common.model.dto.CommonDTO;

public class ExperienceSmsDTO extends CommonDTO {

	private String id;
	private String phone; // '电话号码',
	private String ipAddress; // 'ip地址',
	private String gatewayId; // '端口号',
	private Date createTime; // '创建时间',
	private Date sendTime;// '发送时间',
	private String smsContent; // '短信内容',
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getSmsContent() {
		return smsContent;
	}
	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	
}