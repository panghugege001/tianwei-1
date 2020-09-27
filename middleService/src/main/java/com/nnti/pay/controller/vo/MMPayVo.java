package com.nnti.pay.controller.vo;

/**
 * Created by pony on 2018/07/01.
 */
public class MMPayVo {

	private String trx_key;	
	private String ord_amount;	
	private String request_id;	
	private String request_ip;	
	private String product_type;	
	private String request_time;	
	private String goods_name;	
	private String return_url;	
	private String callback_url;	
	private String remark;		
	private String sign;
	private String bank_code;
	private String account_type;//PRIVATE_DEBIT_ACCOUNT借记卡  PRIVATE_CREDIT_ACCOUNT贷记卡

    
    private String pc;
    private String wap;

    private String trx_status; //SUCCESS 交易成功 FAILED 交易失败 WAITING_PAYMENT 等待支付
    private String trx_time;
    private String pay_request_id;

    public MMPayVo() {
    }

    public void setOnlinePay(String ord_amount, String request_id, String request_ip, String product_type, String request_time, String goods_name, String return_url,
    		String callback_url, String remark, String bank_code, String account_type) {
        this.ord_amount = ord_amount;
        this.request_id = request_id;
        this.request_ip = request_ip;
        this.product_type = product_type;
        this.request_time = request_time;
        this.goods_name = goods_name;
        this.return_url = return_url;
        this.callback_url = callback_url;
        this.remark = remark;
        this.bank_code = account_type;
    }

	public String getTrx_key() {
		return trx_key;
	}

	public void setTrx_key(String trx_key) {
		this.trx_key = trx_key;
	}

	public String getOrd_amount() {
		return ord_amount;
	}

	public void setOrd_amount(String ord_amount) {
		this.ord_amount = ord_amount;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public String getRequest_ip() {
		return request_ip;
	}

	public void setRequest_ip(String request_ip) {
		this.request_ip = request_ip;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getRequest_time() {
		return request_time;
	}

	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getCallback_url() {
		return callback_url;
	}

	public void setCallback_url(String callback_url) {
		this.callback_url = callback_url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
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

	public String getTrx_status() {
		return trx_status;
	}

	public void setTrx_status(String trx_status) {
		this.trx_status = trx_status;
	}

	public String getTrx_time() {
		return trx_time;
	}

	public void setTrx_time(String trx_time) {
		this.trx_time = trx_time;
	}

	public String getPay_request_id() {
		return pay_request_id;
	}

	public void setPay_request_id(String pay_request_id) {
		this.pay_request_id = pay_request_id;
	}

}
