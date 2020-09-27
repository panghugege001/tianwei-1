package com.nnti.pay.controller.vo;

/**
 * Created by Addis on 2017/2/13.
 */
public class HtPayVo {

	// 异步通知地址
	private String notify_url;

	// 同步通知地址
	private String return_url;

	// 支付类型（1:网银）
	private String pay_type;

	// 银行卡编码
	private String bank_code;

	// 商户号
	private String merchant_code;

	// 商家订单号
	private String order_no;

	// 订单金额
	private String order_amount;

	// 订单时间
	private String order_time;

	// 来路域名
	private String req_referer;

	// 消费者 IP
	private String customer_ip;

	// 回传参数
	private String return_params;

	// 签名
	private String sign;

	/************** 以下回调参数 ******************/

	// 支付平台订单 号
	private String trade_no;

	// 支付平台订单 时间
	private String trade_time;

	// 交易状态
	private String trade_status;
	
	//交易类型
	private String notify_type;
	
	//查询交易状态
	private String is_success;

	public void setOnline(String notify_url, String return_url, String bank_code, String merchant_code, String order_no,
			String order_amount, String order_time, String req_referer, String customer_ip, String return_params) {
		this.notify_url = notify_url;
		this.return_url = return_url;
		this.bank_code = bank_code;
		this.merchant_code = merchant_code;
		this.order_no = order_no;
		this.order_amount = order_amount;
		this.order_time = order_time;
		this.req_referer = req_referer;
		this.customer_ip = customer_ip;
		this.return_params = return_params;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getMerchant_code() {
		return merchant_code;
	}

	public void setMerchant_code(String merchant_code) {
		this.merchant_code = merchant_code;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public String getOrder_time() {
		return order_time;
	}

	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}

	public String getReq_referer() {
		return req_referer;
	}

	public void setReq_referer(String req_referer) {
		this.req_referer = req_referer;
	}

	public String getCustomer_ip() {
		return customer_ip;
	}

	public void setCustomer_ip(String customer_ip) {
		this.customer_ip = customer_ip;
	}

	public String getReturn_params() {
		return return_params;
	}

	public void setReturn_params(String return_params) {
		this.return_params = return_params;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getTrade_time() {
		return trade_time;
	}

	public void setTrade_time(String trade_time) {
		this.trade_time = trade_time;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getNotify_type() {
		return notify_type;
	}

	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}

	public String getIs_success() {
		return is_success;
	}

	public void setIs_success(String is_success) {
		this.is_success = is_success;
	}
	

	
}
