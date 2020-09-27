package com.nnti.pay.controller.vo;

/**
 * Created by Addis on 2017/10/12.
 */
public class MifPayVo {

    /*** 商户号*/
    private String merchantCode;

    /*** 商户订单号 */
    private String outOrderId;

    /*** 设备号*/
    private String deviceNo;

    /*** 金额（单位：分）*/
    private String amount;

    /*** 商品名称 */
    private String goodsName;

    /*** 商品描述*/
    private String goodsExplain;
    
    /*** 扩展字段 */
    private String ext;

    /*** 同步通知地址*/
    private String merUrl;
    
    /*** 商户订单创建时间*/
    private String orderCreateTime;

    /*** 通知商户服务端 地址*/
    private String noticeUrl;

    /*** 商品标记 */
    private String goodsMark;
    
    /*** 银行编码 */
    private String bankCode;
    
    /*** 银行卡类型 */
    private String bankCardType;
    
    /*** 银行卡号 */
    private String bankCardNo;

    /*** 到账方式 */
    private String arrivalType;

    /*** 渠道编码*/
    private String payChannel;
    
    /*** 渠道编码*/
    private String payType;
    
    

    /*** ip */
    private String ip;

    private String sign;
    
    /***** 以下 支付请求返回数据*****/
    
    /******支付订单号*/
    private String orderId;
    
    /******二维码支付地址*/
    private String url;
    
    
    
    /***** 以下 回调返回数据*****/
    
    /******交易订单号*/
    private String instructCode;
    
    /******交易时间 */
    private String transTime;
    
    /******消费金额 */
    private String totalAmount;
    
    /******响应码 */
    private String code;
    
    /******响应消息 */
    private String msg;
    
    
    
    
    
    
    
	public void setOnline(String merchantCode, String outOrderId,String totalAmount, String goodsName,
			String goodsExplain,String orderCreateTime, String merUrl, String noticeUrl,String bankCode,String bankCardNo,String ext) {
		this.merchantCode = merchantCode;
		this.outOrderId = outOrderId;
		this.totalAmount = totalAmount;
		this.goodsName = goodsName;
		this.goodsExplain = goodsExplain;
		this.orderCreateTime = orderCreateTime;
		this.merUrl = merUrl;
		this.noticeUrl = noticeUrl;
		this.bankCode = bankCode;
		this.bankCardNo = bankCardNo;
		this.ext = ext;
	}
	
	
	public void setWxZfb(String merchantCode, String outOrderId,String amount, String goodsName,
			String goodsExplain, String ext, String orderCreateTime, String noticeUrl, String goodsMark,String ip) {
		this.merchantCode = merchantCode;
		this.outOrderId = outOrderId;
		this.amount = amount;
		this.goodsName = goodsName;
		this.goodsExplain = goodsExplain;
		this.ext = ext;
		this.orderCreateTime = orderCreateTime;
		this.noticeUrl = noticeUrl;
		this.goodsMark = goodsMark;
		this.ip = ip;
	}
	
	
	public void setWxZfb2(String merchantCode, String outOrderId,String totalAmount, String goodsName,
			String goodsExplain, String ext, String orderCreateTime, String merUrl,String noticeUrl, String goodsMark,String ip) {
		this.merchantCode = merchantCode;
		this.outOrderId = outOrderId;
		this.totalAmount = totalAmount;
		this.goodsName = goodsName;
		this.goodsExplain = goodsExplain;
		this.ext = ext;
		this.orderCreateTime = orderCreateTime;
		this.merUrl = merUrl;
		this.noticeUrl = noticeUrl;
		this.goodsMark = goodsMark;
		this.ip = ip;
	}
	

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getOutOrderId() {
		return outOrderId;
	}

	public void setOutOrderId(String outOrderId) {
		this.outOrderId = outOrderId;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsExplain() {
		return goodsExplain;
	}

	public void setGoodsExplain(String goodsExplain) {
		this.goodsExplain = goodsExplain;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public String getNoticeUrl() {
		return noticeUrl;
	}

	public void setNoticeUrl(String noticeUrl) {
		this.noticeUrl = noticeUrl;
	}

	public String getGoodsMark() {
		return goodsMark;
	}

	public void setGoodsMark(String goodsMark) {
		this.goodsMark = goodsMark;
	}

	public String getArrivalType() {
		return arrivalType;
	}

	public void setArrivalType(String arrivalType) {
		this.arrivalType = arrivalType;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}



	public String getOrderId() {
		return orderId;
	}



	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public String getInstructCode() {
		return instructCode;
	}



	public void setInstructCode(String instructCode) {
		this.instructCode = instructCode;
	}



	public String getTransTime() {
		return transTime;
	}



	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}



	public String getTotalAmount() {
		return totalAmount;
	}



	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String getMsg() {
		return msg;
	}



	public void setMsg(String msg) {
		this.msg = msg;
	}


	public String getMerUrl() {
		return merUrl;
	}


	public void setMerUrl(String merUrl) {
		this.merUrl = merUrl;
	}


	public String getBankCode() {
		return bankCode;
	}


	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}


	public String getBankCardType() {
		return bankCardType;
	}


	public void setBankCardType(String bankCardType) {
		this.bankCardType = bankCardType;
	}


	public String getBankCardNo() {
		return bankCardNo;
	}


	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}


	public String getPayType() {
		return payType;
	}


	public void setPayType(String payType) {
		this.payType = payType;
	}
	
	
	
	
	
	
}
