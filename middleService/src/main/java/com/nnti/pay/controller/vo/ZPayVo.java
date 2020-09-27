package com.nnti.pay.controller.vo;

import java.io.Serializable;

/**
 * 知付
 */
public class ZPayVo implements Serializable {

    private static final long serialVersionUID = -5704303451746387318L;
    private String CustomerId;
    private String Mode;//支付渠道，参数取值 1:网银支付, 4:微信支付,8:支付宝支付,16:QQ钱包,18:京东钱包,32:银联在线(银联快捷),33:银联云闪付
    private String BankCode;//支付宝:ALIPAY
    private String Money;
    private String UserId;//商户号
    private String Message;
    private String CallBackUrl;
    private String ReturnUrl;
    private String sign;
    
    /********以下回调通知参数**************/
    
    private String OrderId;
    private Integer Status;//订单状态 1 表示付款成功,0 表示失败
    private String Time;
    private String Type;//固定值 1 ，表示充值订单
    
    /*** 订单结果说明*/
    private String msg;


    public ZPayVo() {
    }

    /*** 提交接口参数值 */
    public void setOnline_pay(String CustomerId, String Mode, String BankCode, String Money, String  UserId,
                         String CallBackUrl, String ReturnUrl, String Message ) {
    	this.CustomerId = CustomerId;
    	this.Mode = Mode;
    	this.BankCode = BankCode;
    	this.Money = Money;
    	this.UserId = UserId;
    	this.CallBackUrl = CallBackUrl;
    	this.ReturnUrl = ReturnUrl;
    	this.Message = Message;
    }

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	public String getMode() {
		return Mode;
	}

	public void setMode(String mode) {
		Mode = mode;
	}

	public String getBankCode() {
		return BankCode;
	}

	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}

	public String getMoney() {
		return Money;
	}

	public void setMoney(String money) {
		Money = money;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getCallBackUrl() {
		return CallBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		CallBackUrl = callBackUrl;
	}

	public String getReturnUrl() {
		return ReturnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		ReturnUrl = returnUrl;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
