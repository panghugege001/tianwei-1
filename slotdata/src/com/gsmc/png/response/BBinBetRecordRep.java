package com.gsmc.png.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BBinBetRecordRep implements Serializable {

	private static final long serialVersionUID = -3144897284906494931L;

	/**
	 * 注单号码 WagersID
	 */
	@JsonProperty(value = "WagersID")
	private String wagersId;

	/**
	 * 帐号 UserName
	 */
	@JsonProperty(value = "UserName")
	private String username;

	/**
	 * 下注时间 WagersDate
	 */
	@JsonProperty(value = "WagersDate")
	private Date wagersDate;

	private String gameKind;

	/**
	 * 游戏种类 GameType
	 */
	@JsonProperty(value = "GameType")
	private String gameType;

	/**
	 * 注单结果 Result
	 */
	@JsonProperty(value = "Result")
	private String result;

	/**
	 * 下注金额 BetAmount
	 */
	@JsonProperty(value = "BetAmount")
	private Double betAmount;

	/**
	 * 派彩金额 Payoff
	 */
	@JsonProperty(value = "Payoff")
	private Double payoff;

	@JsonProperty(value = "ResultType")
	private String resultType;

	/**
	 * 会员有效投注额 Commissionable
	 */
	@JsonProperty(value = "Commissionable")
	private Double commissionable;

	/**
	 * Y：已派彩、N：未派彩 IsPaid
	 */
	@JsonProperty(value = "IsPaid")
	private String isPaid;

	private String subGameKind;
	private Integer settle;
	private String statTime;
	@JsonProperty(value = "OrderDate")
	private Date orderDate; // 账务时间
	private String rebateTime; // 返水时间
	private String amesTime; // 美东时间
	private String betContent;

	public String getBetContent() {
		return betContent;
	}

	public void setBetContent(String betContent) {
		this.betContent = betContent;
	}

	public String getWagersId() {
		return wagersId;
	}

	public void setWagersId(String wagersId) {
		this.wagersId = wagersId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getWagersDate() {
		return wagersDate;
	}

	public void setWagersDate(Date wagersDate) {
		this.wagersDate = wagersDate;
	}

	public String getGameKind() {
		return gameKind;
	}

	public void setGameKind(String gameKind) {
		this.gameKind = gameKind;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Double getBetAmount() {
		return betAmount == null ? 0D : betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}

	public Double getPayoff() {
		return payoff == null ? 0D : payoff;
	}

	public void setPayoff(Double payoff) {
		this.payoff = payoff;
	}

	public Double getCommissionable() {
		return commissionable == null ? 0D : commissionable;
	}

	public void setCommissionable(Double commissionable) {
		this.commissionable = commissionable;
	}

	public String getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(String isPaid) {
		this.isPaid = isPaid;
	}

	public String getSubGameKind() {
		return subGameKind;
	}

	public void setSubGameKind(String subGameKind) {
		this.subGameKind = subGameKind;
	}

	public Integer getSettle() {
		return settle;
	}

	public void setSettle(Integer settle) {
		this.settle = settle;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getStatTime() {
		return statTime;
	}

	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getRebateTime() {
		return rebateTime;
	}

	public void setRebateTime(String rebateTime) {
		this.rebateTime = rebateTime;
	}

	public String getAmesTime() {
		return amesTime;
	}

	public void setAmesTime(String amesTime) {
		this.amesTime = amesTime;
	}

}
