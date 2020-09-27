package com.gsmc.png.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//bbin实时电子数据，定时器每3分钟获取
@Entity
@Table(name = "bbin_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class BbinData implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer productID; // 自增ＩＤ，唯一性
	private String userName; // 玩家账号
	private String orderNumber; // 单号
	private Integer resultType; // 游戏结果类型：1 表示输，2 表示赢 3 表示和，4 表示无效
	private Double bettingAmount; // 投注金额
	private Double winLoseAmount; // 输赢金额
	private Double balance; // 余额
	private Date addTime; // 交易时间
	private String platformID; // 17bbin,2ag
	private BigInteger vendorId;// 顺序号
	private Double validAmount;// 有效投注额
	private Date ballTime; //结算时间
	private String type;//游戏类型 gebrbv电子 gfbrbv捕鱼 gbrbv真人 gsbrbvbbin体育 glbrbv彩票

	// Constructors

	/** default constructor */
	public BbinData() {
	}

	@Id
	public Integer getProductID() {
		return productID;
	}

	public void setProductID(Integer productID) {
		this.productID = productID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Integer getResultType() {
		return resultType;
	}

	public void setResultType(Integer resultType) {
		this.resultType = resultType;
	}

	public Double getBettingAmount() {
		return bettingAmount;
	}

	public void setBettingAmount(Double bettingAmount) {
		this.bettingAmount = bettingAmount;
	}

	public Double getWinLoseAmount() {
		return winLoseAmount;
	}

	public void setWinLoseAmount(Double winLoseAmount) {
		this.winLoseAmount = winLoseAmount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getPlatformID() {
		return platformID;
	}

	public void setPlatformID(String platformID) {
		this.platformID = platformID;
	}

	public BigInteger getVendorId() {
		return vendorId;
	}

	public void setVendorId(BigInteger vendorId) {
		this.vendorId = vendorId;
	}

	public Double getValidAmount() {
		return validAmount;
	}

	public void setValidAmount(Double validAmount) {
		this.validAmount = validAmount;
	}

	public Date getBallTime() {
		return ballTime;
	}

	public void setBallTime(Date ballTime) {
		this.ballTime = ballTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}