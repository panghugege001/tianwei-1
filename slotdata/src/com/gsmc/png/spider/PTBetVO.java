package com.gsmc.png.spider;

import java.text.NumberFormat;
import java.text.ParseException;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;


public class PTBetVO {

	private String loginName;
	private Double bets = 0.0;
	private Double profit = 0.0;
	private Double progressiveBet = 0.0;
	private Double progressviceProfit = 0.0;
	// 中奖奖池额度
	private Double progressiveFee = 0.0;

	public PTBetVO(String loginName, String bets, String profit) throws ParseException {
		this.loginName = loginName;
		this.bets = (Double) NumberFormat.getNumberInstance(java.util.Locale.US).parse(bets.replace("CNY", "")).doubleValue();
		this.profit = (Double) NumberFormat.getNumberInstance(java.util.Locale.US).parse(profit.replace("CNY", "")).doubleValue();
	}
	public PTBetVO(String loginName, String bets, String profit, String progressviceWin) throws ParseException {
		this.loginName = loginName;
		this.bets = (Double) NumberFormat.getNumberInstance(java.util.Locale.US).parse(bets.replace("CNY", "")).doubleValue();
		this.profit = (Double) NumberFormat.getNumberInstance(java.util.Locale.US).parse(profit.replace("CNY", "")).doubleValue();
		this.progressiveFee = (Double) NumberFormat.getNumberInstance(java.util.Locale.US).parse(progressviceWin.replace("CNY", "")).doubleValue();
	}
	
	public PTBetVO(String username, String progressiveWins) throws ParseException {
		this.loginName = username;
		this.progressiveFee = (Double) NumberFormat.getNumberInstance(java.util.Locale.US).parse(progressiveWins.replace("CNY", "")).doubleValue();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PTBetVO ptBetVO = (PTBetVO) o;
		return Objects.equal(loginName, ptBetVO.loginName);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(loginName);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("loginName", loginName)
				.add("bets", bets)
				.add("profit", profit)
				.add("progressiveFee", progressiveFee)
				.toString();
	}

	
	public Double getProgressiveFee() {
		return progressiveFee;
	}
	public void setProgressiveFee(Double progressiveFee) {
		this.progressiveFee = progressiveFee;
	}
	public String getLoginName() {
		return loginName;
	}

	public Double getBets() {
		return bets;
	}

	public void setBets(Double bets) {
		this.bets = bets;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Double getProgressiveBet() {
		return progressiveBet;
	}

	public void setProgressiveBet(Double progressiveBet) {
		this.progressiveBet = progressiveBet;
	}

	public Double getProgressviceProfit() {
		return progressviceProfit;
	}

	public void setProgressviceProfit(Double progressviceProfit) {
		this.progressviceProfit = progressviceProfit;
	}

}