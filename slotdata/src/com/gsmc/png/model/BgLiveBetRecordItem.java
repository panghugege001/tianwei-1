package com.gsmc.png.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BgLiveBetRecordItem {
  @JsonProperty(value = "tranId")
  private String tranId;
  @JsonProperty(value = "aAmount")
  private Double aAmount;
  @JsonProperty(value = "loginId")
  private String loginId;
  @JsonProperty(value = "orderId")
  private String orderId;
  @JsonProperty(value = "moduleName")
  private String moduleName;
  @JsonProperty(value = "orderStatus")
  private Integer orderStatus;
  @JsonProperty(value = "playId")
  private String playId;
  @JsonProperty(value = "uid")
  private String uid;
  @JsonProperty(value = "orderTime")
  private String orderTime;
  @JsonProperty(value = "gameName")
  private String gameName;
  @JsonProperty(value = "payment")
  private Double payment;
  @JsonProperty(value = "sn")
  private String sn;
  @JsonProperty(value = "bAmount")
  private Double bAmount;
  @JsonProperty(value = "moduleId")
  private String moduleId;
  @JsonProperty(value = "noComm")
  private String noComm;
  @JsonProperty(value = "gameId")
  private String gameId;
  @JsonProperty(value = "playNameEn")
  private String playNameEn;
  @JsonProperty(value = "issueId")
  private String issueId;
  @JsonProperty(value = "playName")
  private String playName;
  @JsonProperty(value = "userId")
  private String userId;
  @JsonProperty(value = "gameNameEn")
  private String gameNameEn;
  @JsonProperty(value = "fromIp")
  private String fromIp;
  @JsonProperty(value = "orderFrom")
  private String orderFrom;
  @JsonProperty(value = "validBet")
  private Double validBet;
  @JsonProperty(value = "betContent")
  private String betContent;
  @JsonProperty(value = "lastUpdateTime")
  private String lastUpdateTime;

  public String getTranId() {
    return tranId;
  }

  public void setTranId(String tranId) {
    this.tranId = tranId;
  }

  public Double getaAmount() {
    return aAmount;
  }

  public void setaAmount(Double aAmount) {
    this.aAmount = aAmount;
  }

  public String getLoginId() {
    return loginId;
  }

  public void setLoginId(String loginId) {
    this.loginId = loginId;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public Integer getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(Integer orderStatus) {
    this.orderStatus = orderStatus;
  }

  public String getPlayId() {
    return playId;
  }

  public void setPlayId(String playId) {
    this.playId = playId;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getOrderTime() {
    return orderTime;
  }

  public void setOrderTime(String orderTime) {
    this.orderTime = orderTime;
  }

  public String getGameName() {
    return gameName;
  }

  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  public Double getPayment() {
    return payment;
  }

  public void setPayment(Double payment) {
    this.payment = payment;
  }

  public String getSn() {
    return sn;
  }

  public void setSn(String sn) {
    this.sn = sn;
  }

  public Double getbAmount() {
    return bAmount;
  }

  public void setbAmount(Double bAmount) {
    this.bAmount = bAmount;
  }

  public String getModuleId() {
    return moduleId;
  }

  public void setModuleId(String moduleId) {
    this.moduleId = moduleId;
  }

  public String getNoComm() {
    return noComm;
  }

  public void setNoComm(String noComm) {
    this.noComm = noComm;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public String getPlayNameEn() {
    return playNameEn;
  }

  public void setPlayNameEn(String playNameEn) {
    this.playNameEn = playNameEn;
  }

  public String getIssueId() {
    return issueId;
  }

  public void setIssueId(String issueId) {
    this.issueId = issueId;
  }

  public String getPlayName() {
    return playName;
  }

  public void setPlayName(String playName) {
    this.playName = playName;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getGameNameEn() {
    return gameNameEn;
  }

  public void setGameNameEn(String gameNameEn) {
    this.gameNameEn = gameNameEn;
  }

  public String getFromIp() {
    return fromIp;
  }

  public void setFromIp(String fromIp) {
    this.fromIp = fromIp;
  }

  public String getOrderFrom() {
    return orderFrom;
  }

  public void setOrderFrom(String orderFrom) {
    this.orderFrom = orderFrom;
  }

  public Double getValidBet() {
    return validBet;
  }

  public void setValidBet(Double validBet) {
    this.validBet = validBet;
  }

  public String getBetContent() {
    return betContent;
  }

  public void setBetContent(String betContent) {
    this.betContent = betContent;
  }

  public String getLastUpdateTime() {
    return lastUpdateTime;
  }

  public void setLastUpdateTime(String lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }

  @Override
  public String toString() {
    return "BgLiveBetRecordItem{" +
        "tranId='" + tranId + '\'' +
        ", aAmount=" + aAmount +
        ", loginId='" + loginId + '\'' +
        ", orderId='" + orderId + '\'' +
        ", moduleName='" + moduleName + '\'' +
        ", orderStatus=" + orderStatus +
        ", playId='" + playId + '\'' +
        ", uid='" + uid + '\'' +
        ", orderTime=" + orderTime +
        ", gameName='" + gameName + '\'' +
        ", payment=" + payment +
        ", sn='" + sn + '\'' +
        ", bAmount=" + bAmount +
        ", moduleId='" + moduleId + '\'' +
        ", noComm='" + noComm + '\'' +
        ", gameId='" + gameId + '\'' +
        ", playNameEn='" + playNameEn + '\'' +
        ", issueId='" + issueId + '\'' +
        ", playName='" + playName + '\'' +
        ", userId='" + userId + '\'' +
        ", gameNameEn='" + gameNameEn + '\'' +
        ", fromIp='" + fromIp + '\'' +
        ", orderFrom='" + orderFrom + '\'' +
        ", validBet=" + validBet +
        ", betContent='" + betContent + '\'' +
        ", lastUpdateTime=" + lastUpdateTime +
        '}';
  }
}

