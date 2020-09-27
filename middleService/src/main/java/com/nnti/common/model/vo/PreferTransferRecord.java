package com.nnti.common.model.vo;

import java.util.Date;

public class PreferTransferRecord {
	
	// 编号
	private Long id;
	// 玩家账号
	private String loginName;
	// 当前的投注额
	private Double nowBet;
	// 还需要的投注额
	private Double needBet;
	// 备注
	private String remark;
	// 创建时间
	private Date createTime;
	// 游戏平台
	private String platform;
	// 类型(IN:转入游戏平台)
	private String type;
	
	public PreferTransferRecord() {
		
	}
	
	public PreferTransferRecord(String loginName, Double nowBet, Double needBet, String remark, Date createTime, String platform, String type) {
	
		this.loginName = loginName;
		this.nowBet = nowBet;
		this.needBet = needBet;
		this.remark = remark;
		this.createTime = createTime;
		this.platform = platform;
		this.type = type;
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

	public Double getNowBet() {
		return nowBet;
	}

	public void setNowBet(Double nowBet) {
		this.nowBet = nowBet;
	}

	public Double getNeedBet() {
		return needBet;
	}

	public void setNeedBet(Double needBet) {
		this.needBet = needBet;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}