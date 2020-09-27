package com.nnti.common.model.vo;

import java.util.Date;

public class LosePromo {

	// 提案编号
	private String pno;
	// 玩家账号
	private String userName;
	// 输赢额
	private Double amount;
	//
	private Double deduct;
	//
	private Double rate;
	//
	private Double promo;
	//
	private Integer times;
	// 状态(0:未领取/1:已领取/2:已处理/3:已取消)
	private String status;
	// 已投注额(领取优惠时游戏总投注额)
	private Double betting;
	// 创建时间
	private Date creatTime;
	// 领取时间
	private Date getTime;
	//
	private String promoDate;
	// 游戏平台
	private String platform;
	// 备注
	private String remark;
	
	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getDeduct() {
		return deduct;
	}

	public void setDeduct(Double deduct) {
		this.deduct = deduct;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Double getPromo() {
		return promo;
	}

	public void setPromo(Double promo) {
		this.promo = promo;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getBetting() {
		return betting;
	}

	public void setBetting(Double betting) {
		this.betting = betting;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public Date getGetTime() {
		return getTime;
	}

	public void setGetTime(Date getTime) {
		this.getTime = getTime;
	}

	public String getPromoDate() {
		return promoDate;
	}

	public void setPromoDate(String promoDate) {
		this.promoDate = promoDate;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}