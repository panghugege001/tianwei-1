package com.gsmc.png.remote;

import java.math.BigDecimal;

public class TTGNetWinBean {
	
	private String playerId;
	private BigDecimal totalWin;
	private BigDecimal totalWager;
	
	public TTGNetWinBean(String playerId, BigDecimal totalWin, BigDecimal totalWager) {
		super();
		this.playerId = playerId;
		this.totalWin = totalWin;
		this.totalWager = totalWager;
	}
	
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public BigDecimal getTotalWin() {
		return totalWin;
	}
	public void setTotalWin(BigDecimal totalWin) {
		this.totalWin = totalWin;
	}
	public BigDecimal getTotalWager() {
		return totalWager;
	}
	public void setTotalWager(BigDecimal totalWager) {
		this.totalWager = totalWager;
	}
	
	@Override
	public String toString() {
		return "TTGNetWinBean [playerId=" + playerId + ", totalWin=" + totalWin + ", totalWager=" + totalWager + "]";
	}
}
