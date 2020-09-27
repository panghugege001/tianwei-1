package com.nnti.pay.controller.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SFBPayVO {

    // 商户支付key
    private String payKey;
    
    private String subPayKey;
    // 订单金额
    private String orderPrice;
    // 商户订单号
    private String outTradeNo;
    // 产品类型
    private String productType;
    // 下单时间
    private String orderTime;
    // 产品名称
    private String productName;
    // 下单IP
    private String orderIp;
    //银行编码
    private String bankCode;
    //支付银行卡类型,对私借记卡:PRIVATE_DEBIT_ACCOUNT;对私贷记卡:PRIVATE_CREDIT_ACCOUNT
    private String bankAccountType;
    // 页面通知地址
    private String returnUrl;
    // 异步通知地址
    private String notifyUrl;
    // 签名
    private String sign;
    // 备注
    private String remark;
    // 订单状态
    private String tradeStatus;
    // 成功时间
    private String successTime;
    // 交易流水
    private String trxNo;
    // 商户支付秘钥
    private String paySecret;
    
    /*** pc****/
    private String pc;
    /*** wap****/
    private String wap;
    
    
    
    
    

    public String getPc() {
		return pc;
	}

	public void setPc(String pc) {
		this.pc = pc;
	}

	public String getWap() {
		return wap;
	}

	public void setWap(String wap) {
		this.wap = wap;
	}

	public String getPayKey() {
        return payKey;
    }

    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderIp() {
        return orderIp;
    }

    public void setOrderIp(String orderIp) {
        this.orderIp = orderIp;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(String successTime) {
        this.successTime = successTime;
    }

    public String getTrxNo() {
        return trxNo;
    }

    public void setTrxNo(String trxNo) {
        this.trxNo = trxNo;
    }

    public String getPaySecret() {
        return paySecret;
    }

    public void setPaySecret(String paySecret) {
        this.paySecret = paySecret;
    }

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public String getSubPayKey() {
		return subPayKey;
	}

	public void setSubPayKey(String subPayKey) {
		this.subPayKey = subPayKey;
	}
	
}
