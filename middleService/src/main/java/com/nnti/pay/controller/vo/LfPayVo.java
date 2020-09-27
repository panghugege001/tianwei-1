package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/4/28.
 */
public class LfPayVo {

    private String amount_str;
    private String out_trade_no;
    private String partner;
    private String remark;
    private String service;
    private String sub_body;
    private String subject;
    private String wx_pay_type;//微信
    private String ali_pay_type;//支付宝
    private String qq_pay_type;//QQ支付
    private String return_url;

    private String content;
    private String input_charset;
    private String sign_type;
    private String sign;
    private String publicKey;
    private String interface_version;
    private String status;
    private String request_time;

    public void setZfbWx(String amount_str, String out_trade_no, String merchantCode, String loginName, String sub_body, String subject, String notifyUrl) {
        this.amount_str = amount_str;
        this.out_trade_no = out_trade_no;
        this.partner = merchantCode;
        this.remark = loginName;
        this.sub_body = sub_body;
        this.subject = subject;
        this.return_url = notifyUrl;
    }
    
    public String getAmount_str() {
        return amount_str;
    }

    public void setAmount_str(String amount_str) {
        this.amount_str = amount_str;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSub_body() {
        return sub_body;
    }

    public void setSub_body(String sub_body) {
        this.sub_body = sub_body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWx_pay_type() {
        return wx_pay_type;
    }

    public void setWx_pay_type(String wx_pay_type) {
        this.wx_pay_type = wx_pay_type;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInput_charset() {
        return input_charset;
    }

    public void setInput_charset(String input_charset) {
        this.input_charset = input_charset;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getInterface_version() {
        return interface_version;
    }

    public void setInterface_version(String interface_version) {
        this.interface_version = interface_version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

	public String getAli_pay_type() {
		return ali_pay_type;
	}

	public void setAli_pay_type(String ali_pay_type) {
		this.ali_pay_type = ali_pay_type;
	}

	public String getQq_pay_type() {
		return qq_pay_type;
	}

	public void setQq_pay_type(String qq_pay_type) {
		this.qq_pay_type = qq_pay_type;
	}
    
    

}
