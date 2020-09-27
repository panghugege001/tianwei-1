package com.gsmc.png.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sw_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class SwData implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private String roundId;

	private String brandId;

	private String playerCode;

	private String gameCode;

	private String currency;

	private Double win; // 派彩

	private Double bet; // 投注

	private Double revenue; // 收入

	private String ts;

//	private boolean isTest;//是否测试 false

//	private boolean finished;//是否完成 true

	private String balanceBefore;

	private String balanceAfter;

	private String device;

	private String insertedAt;

	private String totalEvents;

	
	
	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}

	@Id
	public String getRoundId() {
		return this.roundId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandId() {
		return this.brandId;
	}

	public void setPlayerCode(String playerCode) {
		this.playerCode = playerCode;
	}

	public String getPlayerCode() {
		return this.playerCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public String getGameCode() {
		return this.gameCode;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setWin(double win) {
		this.win = win;
	}

	public double getWin() {
		return this.win;
	}

	public void setBet(Double bet) {
		this.bet = bet;
	}

	public Double getBet() {
		return this.bet;
	}

	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	public Double getRevenue() {
		return this.revenue;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public String getTs() {
		return this.ts;
	}

	public void setBalanceBefore(String balanceBefore) {
		this.balanceBefore = balanceBefore;
	}

	public String getBalanceBefore() {
		return this.balanceBefore;
	}

	public void setBalanceAfter(String balanceAfter) {
		this.balanceAfter = balanceAfter;
	}

	public String getBalanceAfter() {
		return this.balanceAfter;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getDevice() {
		return this.device;
	}

	public void setInsertedAt(String insertedAt) {
		this.insertedAt = insertedAt;
	}

	public String getInsertedAt() {
		return this.insertedAt;
	}

	public void setTotalEvents(String totalEvents) {
		this.totalEvents = totalEvents;
	}

	public String getTotalEvents() {
		return this.totalEvents;
	}

}
