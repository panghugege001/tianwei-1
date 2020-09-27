package com.gsmc.png.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PgData entity.
 * 
 */
@Entity
@Table(name = "pg_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PgData implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	  
	private String billNo;//流水号
	private String gameCode;//游戏编码
	private String gameName;//游戏名称
	private String playName;//玩家账号
	private BigDecimal betAmount;//投注
	private BigDecimal validBetAmount;//有效投注额
	private BigDecimal winAmount;//所盈金额
	private BigDecimal balanceBefore;//交易前余额
	private BigDecimal balanceAfter;//交易后余额
	private int gameType;//游戏类型(1.slot)
	private int deviceType;//设备类型(1 Windows 2 macOS 3 Android 4 iOS 5 其它)
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

	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public BigDecimal getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(BigDecimal betAmount) {
		this.betAmount = betAmount;
	}
	public BigDecimal getValidBetAmount() {
		return validBetAmount;
	}
	public void setValidBetAmount(BigDecimal validBetAmount) {
		this.validBetAmount = validBetAmount;
	}
	public BigDecimal getWinAmount() {
		return winAmount;
	}
	public void setWinAmount(BigDecimal winAmount) {
		this.winAmount = winAmount;
	}
	public BigDecimal getBalanceBefore() {
		return balanceBefore;
	}
	public void setBalanceBefore(BigDecimal balanceBefore) {
		this.balanceBefore = balanceBefore;
	}
	public BigDecimal getBalanceAfter() {
		return balanceAfter;
	}
	public void setBalanceAfter(BigDecimal balanceAfter) {
		this.balanceAfter = balanceAfter;
	}
	public int getGameType() {
		return gameType;
	}
	public void setGameType(int gameType) {
		this.gameType = gameType;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
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
		PgData agData = (PgData) o;
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