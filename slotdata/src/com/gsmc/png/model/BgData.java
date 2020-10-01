package com.gsmc.png.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BgData entity.
 * 
 */
@Entity
@Table(name = "bg_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class BgData implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	  
	 /**
	   * 游戏编码
	   */
	  private String gameType;
	  /**
	   * 投注金额
	   */
	  private Double betAmount = 0D;
	  /**
	   * 用户名
	   */
	  private String account;
	  /**
	   * 唯一编码
	   */
	  private String billNo;
	  /**
	   * 投注时间
	   */
	  private Date betTime;
	  /**
	   * 输赢金额
	   */
	  private Double winAmount = 0D;
	  /**
	   *  -1撒消,0未结算,1已结算
	   */
	  private Integer settle;
	  /**
	   * 添加时间
	   */
	  private Date addTime;
	  /**
	   * 更新时间
	   */
	  private Date updateTime;
	  /**
	   * 游戏种类
	   */
	  private String gameKind;
	  /**
	   * 一级分类
	   */
	  private String firstKind;
	  /**
	   * 下注北京时间
	   */
	  private Date gmtBetTime;
	  /**
	   * 报表结算时间(美东)
	   */
	  private String statTime;
	  /**
	   * 下注美东时间
	   */
	  private Date amesTime;
	  /**
	   * 返水结算时间(美东)
	   */
	  private String rebateTime;

	  /**
	   * 投注内容
	   */
	  private String betContent;

	  /**
	   * 有效投注额
	   */
	  private Double validAmount = 0D;
	
	
	@Id
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	
	
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public Double getBetAmount() {
		return betAmount;
	}
	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Date getBetTime() {
		return betTime;
	}
	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}
	public Double getWinAmount() {
		return winAmount;
	}
	public void setWinAmount(Double winAmount) {
		this.winAmount = winAmount;
	}
	public Integer getSettle() {
		return settle;
	}
	public void setSettle(Integer settle) {
		this.settle = settle;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getGameKind() {
		return gameKind;
	}
	public void setGameKind(String gameKind) {
		this.gameKind = gameKind;
	}
	public String getFirstKind() {
		return firstKind;
	}
	public void setFirstKind(String firstKind) {
		this.firstKind = firstKind;
	}
	public Date getGmtBetTime() {
		return gmtBetTime;
	}
	public void setGmtBetTime(Date gmtBetTime) {
		this.gmtBetTime = gmtBetTime;
	}
	public String getStatTime() {
		return statTime;
	}
	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}
	public Date getAmesTime() {
		return amesTime;
	}
	public void setAmesTime(Date amesTime) {
		this.amesTime = amesTime;
	}
	public String getRebateTime() {
		return rebateTime;
	}
	public void setRebateTime(String rebateTime) {
		this.rebateTime = rebateTime;
	}
	public String getBetContent() {
		return betContent;
	}
	public void setBetContent(String betContent) {
		this.betContent = betContent;
	}
	public Double getValidAmount() {
		return validAmount;
	}
	public void setValidAmount(Double validAmount) {
		this.validAmount = validAmount;
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
		BgData agData = (BgData) o;
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