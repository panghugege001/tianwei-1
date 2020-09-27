package com.nnti.pay.controller.vo;

//美名互联支付
public class MMHLPayVo {

    private String mch_id;
    private String type;
    private String notify_url;
    private String back_url;
    private String card_type;//0:仅允许使用借记卡支付 1:仅允许使用信用卡支付 2:借记卡和信用卡都能进行支付
    private String out_trade_no;
    private String body;
    private String total_fee;
    private String card_no;//银行卡号，快捷支付必填
    private String sign;
    /*** pc****/
    private String pc;
    /*** wap****/
    private String wap;
    
    //回调
    private String order_id;
    private String pay_status;//0：未支付1：支付成功2：支付失败
    
    public void setOnline_pay(String mch_id, String type, String notify_url,String back_url, String out_trade_no,String total_fee,String body,
			String card_type,String card_no) {
    	this.mch_id = mch_id;
    	this.type = type;
    	this.notify_url = notify_url;
    	this.back_url = back_url;
    	this.out_trade_no = out_trade_no;
    	this.total_fee = total_fee;
    	this.body = body;
    	this.card_type = card_type;
    	this.card_no = card_no;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getBack_url() {
		return back_url;
	}

	public void setBack_url(String back_url) {
		this.back_url = back_url;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getCard_no() {
		return card_no;
	}

	public void setCard_no(String card_no) {
		this.card_no = card_no;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

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

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
}
