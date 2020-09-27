package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * 天机付 支付提交网关参数
 */
public class TjfPayVo implements Serializable {

    private static final long serialVersionUID = 7324634869954607545L;
    /*** 接口名字*/
    private String service;
    /*** 接口版本*/
    private String version;
    /*** 商户号*/
    private String merId;
    /*** 类型*/
    private String typeId;
    /*** 订单号*/
    private String tradeNo;
    /*** 交易日期*/
    private String tradeDate;
    /*** 充值金额*/
    private String amount;
    /*** 第三方回掉地址 */
    private String notifyUrl;
    
    /*** 手机h5 */
    private String wapH;

    /*** 支付时上送的商户参数*/
    private String extra;
    /******交易摘要***********/ 
    private String summary;
    /*** 超时时间 */
    private String expireTime;
    /*** 客户请求IP */
    private String clientIp;
    /*** bankID*/
    private String bankId;
    
    /*** 银行卡号 */
	private String bankcard;
	/*** 银行卡户名 */
	private String bankname;
	/*** 手机号码 */
	private String phoneNumber;
    
    /*** 签名*/
    private String sign;
    
    /*** url*/
    private String url;
    
    /**** apiUrl */
    private String apiUrl;
    

    /*******************以下回调参数*******************/
    /*** 支付平台订单号 */
    private String opeNo;

    /*** 支付平台订单支付日期 */
    private String opeDate;

    /*** 处理结果0 未支付，1 成功，2失败 */
    private String status;
    
    /*** 支付时间 */
    private String payTime;

    /*** 服务器，商户需回写应答。*/
    private String notifyType;


    public TjfPayVo() {
    }


    /*** 设置支付宝或微信接口属性值*/
    public void setWxZfb(String merId, String tradeNo, String tradeDate, String amount,
    		String notifyUrl,String extra,String summary,String clientIp) {
      this.merId = merId;
      this.tradeNo = tradeNo;
      this.tradeDate = tradeDate;
      this.amount = amount;
      this.notifyUrl = notifyUrl;
      this.extra = extra;
      this.summary = summary;
      this.clientIp = clientIp;
    }
    
    
    /*** 设置网银接口属性值*/
    public void setOnline(String merId, String tradeNo, String tradeDate, String amount,
    		String notifyUrl,String extra,String summary,String clientIp,String bankId,String bankcard,String bankname,String phoneNumber) {
      this.merId = merId;
      this.tradeNo = tradeNo;
      this.tradeDate = tradeDate;
      this.amount = amount;
      this.notifyUrl = notifyUrl;
      this.extra = extra;
      this.summary = summary;
      this.clientIp = clientIp;
      this.bankId = bankId;
      this.bankcard = bankcard;
      this.bankname = bankname;
      this.phoneNumber = phoneNumber;
      
    }
    


	public String getService() {
		return service;
	}


	public void setService(String service) {
		this.service = service;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getMerId() {
		return merId;
	}


	public void setMerId(String merId) {
		this.merId = merId;
	}


	public String getTypeId() {
		return typeId;
	}


	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}


	public String getTradeNo() {
		return tradeNo;
	}


	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}


	public String getTradeDate() {
		return tradeDate;
	}


	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}


	public String getAmount() {
		return amount;
	}


	public void setAmount(String amount) {
		this.amount = amount;
	}


	public String getNotifyUrl() {
		return notifyUrl;
	}


	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}


	public String getExtra() {
		return extra;
	}


	public void setExtra(String extra) {
		this.extra = extra;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}


	public String getExpireTime() {
		return expireTime;
	}


	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}


	public String getClientIp() {
		return clientIp;
	}


	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}


	public String getSign() {
		return sign;
	}


	public void setSign(String sign) {
		this.sign = sign;
	}


	public String getOpeNo() {
		return opeNo;
	}


	public void setOpeNo(String opeNo) {
		this.opeNo = opeNo;
	}


	public String getOpeDate() {
		return opeDate;
	}


	public void setOpeDate(String opeDate) {
		this.opeDate = opeDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getPayTime() {
		return payTime;
	}


	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}


	public String getNotifyType() {
		return notifyType;
	}


	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getBankId() {
		return bankId;
	}


	public void setBankId(String bankId) {
		this.bankId = bankId;
	}


	public String getBankcard() {
		return bankcard;
	}


	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}


	public String getBankname() {
		return bankname;
	}


	public void setBankname(String bankname) {
		this.bankname = bankname;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getApiUrl() {
		return apiUrl;
	}


	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}


	public String getWapH() {
		return wapH;
	}


	public void setWapH(String wapH) {
		this.wapH = wapH;
	}
    
    

   
    
    
}
