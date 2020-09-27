package com.gsmc.png.model;

public class QtBetVo {
	
	private String playerId;
	private Double bet;
	private Double payout;
	private Double rollback;
	
	public QtBetVo(){}

	public QtBetVo(String playerId, Double bet, Double payout, Double rollback) {
		super();
		this.playerId = playerId;
		this.bet = bet;
		this.payout = payout;
		this.rollback = rollback;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public Double getBet() {
		return bet;
	}

	public void setBet(Double bet) {
		this.bet = bet;
	}

	public Double getPayout() {
		return payout;
	}

	public void setPayout(Double payout) {
		this.payout = payout;
	}

	public Double getRollback() {
		return rollback;
	}

	public void setRollback(Double rollback) {
		this.rollback = rollback;
	}

}
