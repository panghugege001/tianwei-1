package com.nnti.common.model.vo;

import java.io.Serializable;

/**
 * Created by ck on 2017/1/25.
 * 官网请求游戏登入参数
 */
public class GameRequestVo implements Serializable{

    private static final long serialVersionUID = -1727790835804499143L;

    private String productContract;
    private String productKey;
    private String gameCode;
    private Double amount;
    private String originalName;
    private String accountType;
    private String platform;
    private String timeBegin;
    private String timeEnd;


	public String getProductContract() {
		return productContract;
	}



	public void setProductContract(String productContract) {
		this.productContract = productContract;
	}



	public String getProductKey() {
		return productKey;
	}



	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}



	public String getGameCode() {
		return gameCode;
	}



	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}



	public String getOriginalName() {
		return originalName;
	}



	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}



	public String getPlatform() {
		return platform;
	}



	public void setPlatform(String platform) {
		this.platform = platform;
	}



	public Double getAmount() {
		return amount;
	}



	public void setAmount(Double amount) {
		this.amount = amount;
	}



	public String getAccountType() {
		return accountType;
	}



	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}



	public String getTimeBegin() {
		return timeBegin;
	}



	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}



	public String getTimeEnd() {
		return timeEnd;
	}



	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}


   
}
