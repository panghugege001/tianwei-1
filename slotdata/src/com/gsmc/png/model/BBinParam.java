package com.gsmc.png.model;

import java.io.Serializable;

public class BBinParam implements Serializable {

	private static final long serialVersionUID = -8054016116930669131L;

	private String roundDate;

	private String startTime;

	private String endTime;

	private String gameKind;

	private String gameSubkind;

	private String gameType;

	private int pageNo;

	public String getRoundDate() {
		return roundDate;
	}

	public void setRoundDate(String roundDate) {
		this.roundDate = roundDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getGameKind() {
		return gameKind;
	}

	public void setGameKind(String gameKind) {
		this.gameKind = gameKind;
	}

	public String getGameSubkind() {
		return gameSubkind;
	}

	public void setGameSubkind(String gameSubkind) {
		this.gameSubkind = gameSubkind;
	}

	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "BBinParam [roundDate=" + roundDate + ", startTime=" + startTime + ", endTime=" + endTime + ", gameKind="
				+ gameKind + ", gameSubkind=" + gameSubkind + ", gameType=" + gameType + ", pageNo=" + pageNo + "]";
	}
}
