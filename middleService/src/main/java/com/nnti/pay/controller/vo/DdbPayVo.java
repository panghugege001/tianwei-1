package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/4/26.
 */
public class DdbPayVo {

    private String url;
    private String sign;
    private String merchant_code;
    private String service_type;
    private String interface_version;
    private String input_charset;
    private String notify_url;
    private String sign_type;
    private String order_no;
    private String order_time;
    private String order_amount;
    private String product_name;
    private String return_url;
    private String bank_code;
    private String product_code;
    private String product_num;
    private String product_desc;
    private String client_ip;
    private String extend_param;
    private String extra_return_param;
    private String pay_type;
    private String show_url;
    private String ddbPublicKey;
    private String bank_seq_no;
    private String trade_status;
    private String trade_time;
    private String trade_no;
    private String notify_id;
    private String notify_type;
    private String orginal_money; 
    
    private String apiUrl;


    public void setZfbWx(String bank_code, String client_ip, String extend_param, String extra_return_param, String merchant_code, String notify_url,
                         String order_amount, String order_no, String order_time, String product_code, String product_desc,
                         String product_name, String product_num, String return_url) {
        this.bank_code = bank_code;
        this.client_ip = client_ip;
        this.extend_param = extend_param;
        this.extra_return_param = extra_return_param;
        this.merchant_code = merchant_code;
        this.notify_url = notify_url;
        this.order_amount = order_amount;
        this.order_no = order_no;
        this.order_time = order_time;
        this.product_code = product_code;
        this.product_desc = product_desc;
        this.product_name = product_name;
        this.product_num = product_num;
        this.return_url = return_url;
    }
    
    public void setOnline(String bank_code, String client_ip, String extend_param, String extra_return_param, String merchant_code, String notify_url,
            String order_amount, String order_no, String order_time, String product_code, String product_desc,
            String product_name, String product_num, String return_url) {
			this.bank_code = bank_code;
			this.client_ip = client_ip;
			this.extend_param = extend_param;
			this.extra_return_param = extra_return_param;
			this.merchant_code = merchant_code;
			this.notify_url = notify_url;
			this.order_amount = order_amount;
			this.order_no = order_no;
			this.order_time = order_time;
			this.product_code = product_code;
			this.product_desc = product_desc;
			this.product_name = product_name;
			this.product_num = product_num;
			this.return_url = return_url;
			}
    

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getInterface_version() {
        return interface_version;
    }

    public void setInterface_version(String interface_version) {
        this.interface_version = interface_version;
    }

    public String getInput_charset() {
        return input_charset;
    }

    public void setInput_charset(String input_charset) {
        this.input_charset = input_charset;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getClient_ip() {
        return client_ip;
    }

    public void setClient_ip(String client_ip) {
        this.client_ip = client_ip;
    }

    public String getExtend_param() {
        return extend_param;
    }

    public void setExtend_param(String extend_param) {
        this.extend_param = extend_param;
    }

    public String getExtra_return_param() {
        return extra_return_param;
    }

    public void setExtra_return_param(String extra_return_param) {
        this.extra_return_param = extra_return_param;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getShow_url() {
        return show_url;
    }

    public void setShow_url(String show_url) {
        this.show_url = show_url;
    }

    public String getDdbPublicKey() {
        return ddbPublicKey;
    }

    public void setDdbPublicKey(String ddbPublicKey) {
        this.ddbPublicKey = ddbPublicKey;
    }

    public String getBank_seq_no() {
        return bank_seq_no;
    }

    public void setBank_seq_no(String bank_seq_no) {
        this.bank_seq_no = bank_seq_no;
    }

    public String getTrade_status() {
        return trade_status;
    }

    public void setTrade_status(String trade_status) {
        this.trade_status = trade_status;
    }

    public String getTrade_time() {
        return trade_time;
    }

    public void setTrade_time(String trade_time) {
        this.trade_time = trade_time;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getNotify_id() {
        return notify_id;
    }

    public void setNotify_id(String notify_id) {
        this.notify_id = notify_id;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
    }

	public String getOrginal_money() {
		return orginal_money;
	}

	public void setOrginal_money(String orginal_money) {
		this.orginal_money = orginal_money;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}
	
	
    
    
}
