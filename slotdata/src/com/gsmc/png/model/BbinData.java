package com.gsmc.png.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//bbin实时电子数据，定时器每3分钟获取
@Entity
@Table(name = "bbin_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class BbinData implements Serializable {
	private static final long serialVersionUID = 1L;
	private String billNo; // 订单号
	private String account; // 玩家账号
	private String gameType;
	private Double betAmount;
	private Double winAmount;
	private Double validAmount;
	private Integer settle;
	private Date betTime;
	private String result;
	private String gameKind;
	private String firstKind;
	private String subGameKind;
	private String statTime;
	private Date addTime;
	private Date gmtBetTime;
	private Date updateTime;
	private Date amesTime;
	private String betContent;
	private String rebateTime; // 返水统计时间
	
	public BbinData() {
	}

	@Id
	@Column(name = "bill_no")
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	@Column(name = "account")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name = "game_type")
	public String getGameType() {
		return gameType;
	}

	public void setGameType(String gameType) {
		this.gameType = gameType;
	}

	@Column(name = "bet_amount")
	public Double getBetAmount() {
		return betAmount;
	}

	public void setBetAmount(Double betAmount) {
		this.betAmount = betAmount;
	}

	@Column(name = "win_amount")
	public Double getWinAmount() {
		return winAmount;
	}

	public void setWinAmount(Double winAmount) {
		this.winAmount = winAmount;
	}

	@Column(name = "valid_amount")
	public Double getValidAmount() {
		return validAmount;
	}

	public void setValidAmount(Double validAmount) {
		this.validAmount = validAmount;
	}

	@Column(name = "settle")
	public Integer getSettle() {
		return settle;
	}

	public void setSettle(Integer settle) {
		this.settle = settle;
	}

	@Column(name = "bet_time")
	public Date getBetTime() {
		return betTime;
	}

	public void setBetTime(Date betTime) {
		this.betTime = betTime;
	}

	@Column(name = "result")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Column(name = "game_kind")
	public String getGameKind() {
		return gameKind;
	}

	public void setGameKind(String gameKind) {
		this.gameKind = gameKind;
	}

	@Column(name = "sub_game_kind")
	public String getSubGameKind() {
		return subGameKind;
	}

	public void setSubGameKind(String subGameKind) {
		this.subGameKind = subGameKind;
	}
	@Column(name = "stat_time")
	public String getStatTime() {
		return statTime;
	}

	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}
	@Column(name = "add_time")
	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	@Column(name = "gmt_bet_time")
	public Date getGmtBetTime() {
		return gmtBetTime;
	}

	public void setGmtBetTime(Date gmtBetTime) {
		this.gmtBetTime = gmtBetTime;
	}
	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@Column(name = "ames_time")
	public Date getAmesTime() {
		return amesTime;
	}

	public void setAmesTime(Date amesTime) {
		this.amesTime = amesTime;
	}
	
	@Column(name = "rebate_time")
	public String getRebateTime() {
		return rebateTime;
	}

	public void setRebateTime(String rebateTime) {
		this.rebateTime = rebateTime;
	}
	@Column(name = "first_kind")
	public String getFirstKind() {
		return firstKind;
	}

	public void setFirstKind(String firstKind) {
		this.firstKind = firstKind;
	}

	@Column(name = "bet_content")
	public String getBetContent() {
		return betContent;
	}

	public void setBetContent(String betContent) {
		this.betContent = betContent;
	}
}