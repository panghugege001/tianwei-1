package com.nnti.personal.model.vo;

import java.util.Date;

/**
 * Transfer entity. @author MyEclipse Persistence Tools
 * APP下载彩金
 */
public class AppPreferential {

	private Long id;
	private String loginName;
	private Long transferId;
	private int version;
	private Date createTime;

	/** default constructor */
	public AppPreferential() {
	}

	/** minimal constructor */
	public AppPreferential(Long id, String loginName, Long transferId, int version, Date createTime) {
		this.id = id;
		this.loginName = loginName;
		this.transferId = transferId;
		this.version = version;
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Long getTransferId() {
		return transferId;
	}

	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}