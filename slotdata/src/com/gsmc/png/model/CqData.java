package com.gsmc.png.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Cq9Data entity.
 * 
 */
@Entity
@Table(name = "cq_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class CqData implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	  
	private String billNo;//流水号
	private String gameCode;//游戏编码
	private String playName;//玩家账号
	private Double betAmount;//投注
	private Double validBetAmount;//有效投注额
	private Double returnAmount;//结算后金额
	private Double balance;//余额
	private String gameType;//游戏类型
	private String deviceType;//设备类型
	private Date betTime;//下注时间
	private String platform;//平台
	private Date settleTime;//派彩时间
	private Date createTime;//创建时间
	
	
	@Id
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public String getPlayName() {
		return playName;
	}
	public void setPlayName(String playName) {
		this.playName = playName;
	}
	public Double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}
	public Double getValidBetAmount() {
		return validBetAmount;
	}
	public void setValidBetAmount(Double validBetAmount) {
		this.validBetAmount = validBetAmount;
	}

	public Double getReturnAmount() {
		return returnAmount;
	}
	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public Date getBetTime() {
		return betTime;
	}
	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public Date getSettleTime() {
		return settleTime;
	}
	public void setSettleTime(Date settleTime) {
		this.settleTime = settleTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}

		if (getClass() != o.getClass()) {
			return false;
		}
		CqData agData = (CqData) o;
		if (billNo == null) {
			if (agData.billNo != null) {
				return false;
			}
		} else {
			if (!billNo.equals(agData.billNo)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return 1;
	}

}