package com.gsmc.png.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BgByBetRecordItem {
  @JsonProperty(value = "sn")
  private String sn;
  @JsonProperty(value = "userId")
  private String userId;
  @JsonProperty(value = "loginId")
  private String loginId;
  @JsonProperty(value = "issueId")
  private String issueId;
  @JsonProperty(value = "betId")
  private String betId;
  @JsonProperty(value = "gameBalance")
  private Double gameBalance;
  @JsonProperty(value = "fireCount")
  private String fireCount;
  @JsonProperty(value = "betAmount")
  private Double betAmount;
  @JsonProperty(value = "validAmount")
  private Double validAmount;
  @JsonProperty(value = "calcAmount")
  private Double calcAmount;
  @JsonProperty(value = "payout")
  private Double payout;
  @JsonProperty(value = "orderTime")
  private String orderTime;
  @JsonProperty(value = "orderFrom")
  private String orderFrom;
  @JsonProperty(value = "jackpot")
  private String jackpot;
  @JsonProperty(value = "extend")
  private String extend;
  @JsonProperty(value = "jackpotType")
  private String jackpotType;
  @JsonProperty(value = "gameType")
  private String gameType;

  public String getSn() {
    return sn;
  }

  public void setSn(String sn) {
    this.sn = sn;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getLoginId() {
    return loginId;
  }

  public void setLoginId(String loginId) {
    this.loginId = loginId;
  }

  public String getIssueId() {
    return issueId;
  }

  public void setIssueId(String issueId) {
    this.issueId = issueId;
  }

  public String getBetId() {
    return betId;
  }

  public void setBetId(String betId) {
    this.betId = betId;
  }

  public Double getGameBalance() {
    return gameBalance;
  }

  public void setGameBalance(Double gameBalance) {
    this.gameBalance = gameBalance;
  }

  public String getFireCount() {
    return fireCount;
  }

  public void setFireCount(String fireCount) {
    this.fireCount = fireCount;
  }

  public Double getBetAmount() {
    return betAmount;
  }

  public void setBetAmount(Double betAmount) {
    this.betAmount = betAmount;
  }

  public Double getValidAmount() {
    return validAmount;
  }

  public void setValidAmount(Double validAmount) {
    this.validAmount = validAmount;
  }

  public Double getCalcAmount() {
    return calcAmount;
  }

  public void setCalcAmount(Double calcAmount) {
    this.calcAmount = calcAmount;
  }

  public Double getPayout() {
    return payout;
  }

  public void setPayout(Double payout) {
    this.payout = payout;
  }

  public String getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(String orderTime) {
    this.orderTime = orderTime;
  }

  public String getOrderFrom() {
    return orderFrom;
  }

  public void setOrderFrom(String orderFrom) {
    this.orderFrom = orderFrom;
  }

  public String getJackpot() {
    return jackpot;
  }

  public void setJackpot(String jackpot) {
    this.jackpot = jackpot;
  }

  public String getExtend() {
    return extend;
  }

  public void setExtend(String extend) {
    this.extend = extend;
  }

  public String getJackpotType() {
    return jackpotType;
  }

  public void setJackpotType(String jackpotType) {
    this.jackpotType = jackpotType;
  }

  public String getGameType() {
    return gameType;
  }

  public void setGameType(String gameType) {
    this.gameType = gameType;
  }

  @Override
  public String toString() {
    return "BgByBetRecordItem{" +
        "sn='" + sn + '\'' +
        ", userId='" + userId + '\'' +
        ", loginId='" + loginId + '\'' +
        ", issueId='" + issueId + '\'' +
        ", betId='" + betId + '\'' +
        ", gameBalance=" + gameBalance +
        ", fireCount='" + fireCount + '\'' +
        ", betAmount=" + betAmount +
        ", validAmount=" + validAmount +
        ", calcAmount=" + calcAmount +
        ", payout=" + payout +
        ", orderTime=" + orderTime +
        ", orderFrom='" + orderFrom + '\'' +
        ", jackpot='" + jackpot + '\'' +
        ", extend='" + extend + '\'' +
        ", jackpotType='" + jackpotType + '\'' +
        ", gameType='" + gameType + '\'' +
        '}';
  }
}

