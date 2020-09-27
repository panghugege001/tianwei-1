package com.gsmc.png.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mg_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class MgData implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;

	private String keyinfo;

	private String colId;

	private Integer agentId;

	private Integer mbrId;

	private String mbrCode;

	private String transId;

	private Integer gameId;

	private String transType;

	private Date transTime;

	private String mgsGameId;

	private String mgsActionId;

	private Double amnt;

	private Double clrngAmnt;

	private Double balance;

	private String refTransId;

	private String refTransType;

	public String getKeyinfo() {
		return keyinfo;
	}

	public void setKeyinfo(String keyinfo) {
		this.keyinfo = keyinfo;
	}

	public String getColId() {
		return colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public Integer getMbrId() {
		return mbrId;
	}

	public void setMbrId(Integer mbrId) {
		this.mbrId = mbrId;
	}

	public String getMbrCode() {
		return mbrCode;
	}

	public void setMbrCode(String mbrCode) {
		this.mbrCode = mbrCode;
	}

	@Id
	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public String getMgsGameId() {
		return mgsGameId;
	}

	public void setMgsGameId(String mgsGameId) {
		this.mgsGameId = mgsGameId;
	}

	public String getMgsActionId() {
		return mgsActionId;
	}

	public void setMgsActionId(String mgsActionId) {
		this.mgsActionId = mgsActionId;
	}

	public Double getAmnt() {
		return amnt;
	}

	public void setAmnt(Double amnt) {
		this.amnt = amnt;
	}

	public Double getClrngAmnt() {
		return clrngAmnt;
	}

	public void setClrngAmnt(Double clrngAmnt) {
		this.clrngAmnt = clrngAmnt;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getRefTransId() {
		return refTransId;
	}

	public void setRefTransId(String refTransId) {
		this.refTransId = refTransId;
	}

	public String getRefTransType() {
		return refTransType;
	}

	public void setRefTransType(String refTransType) {
		this.refTransType = refTransType;
	}
}
