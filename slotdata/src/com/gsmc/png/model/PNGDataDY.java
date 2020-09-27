package com.gsmc.png.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PNG_DATA_DY")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PNGDataDY extends PNGData4OracleVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String transactionId;
	private String status;
	private String externalUserId;
	private String roundId;
	private Double roundLoss;//目測是流水
	private Double balance;
	private Double jackpotLoss;
	private Double jackpotGain;
	private Double totalLoss;
	private Double totalGain;
	private String externalFreegameId;
	private String messageType;
	
	private String timeTransaction;//交易時間
	private String messageTimestamp;
	private String createtime;
	
	/** default constructor */
	public PNGDataDY() {
	}
	
	@Id
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExternalUserId() {
		return externalUserId;
	}

	public void setExternalUserId(String externalUserId) {
		this.externalUserId = externalUserId;
	}

	public String getRoundId() {
		return roundId;
	}

	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}

	public Double getRoundLoss() {
		return roundLoss;
	}

	public void setRoundLoss(Double roundLoss) {
		this.roundLoss = roundLoss;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getJackpotLoss() {
		return jackpotLoss;
	}

	public void setJackpotLoss(Double jackpotLoss) {
		this.jackpotLoss = jackpotLoss;
	}

	public Double getJackpotGain() {
		return jackpotGain;
	}

	public void setJackpotGain(Double jackpotGain) {
		this.jackpotGain = jackpotGain;
	}

	public Double getTotalLoss() {
		return totalLoss;
	}

	public void setTotalLoss(Double totalLoss) {
		this.totalLoss = totalLoss;
	}

	public Double getTotalGain() {
		return totalGain;
	}

	public void setTotalGain(Double totalGain) {
		this.totalGain = totalGain;
	}

	public String getExternalFreegameId() {
		return externalFreegameId;
	}

	public void setExternalFreegameId(String externalFreegameId) {
		this.externalFreegameId = externalFreegameId;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getTimeTransaction() {
		return timeTransaction;
	}

	public void setTimeTransaction(String timeTransaction) {
		this.timeTransaction = timeTransaction;
	}

	public String getMessageTimestamp() {
		return messageTimestamp;
	}

	public void setMessageTimestamp(String messageTimestamp) {
		this.messageTimestamp = messageTimestamp;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	@Override
	public String toString() {
		return "PNGDataVO [transactionId=" + transactionId + ", status="
				+ status + ", externalUserId=" + externalUserId + ", roundId="
				+ roundId + ", roundLoss=" + roundLoss + ", balance="
				+ balance + ", externalFreegameId=" + externalFreegameId
				+ ", messageType=" + messageType + ", timeTransaction="
				+ timeTransaction + ", messageTimestamp=" + messageTimestamp
				+ ", createtime=" + createtime + "]";
	}
	
}