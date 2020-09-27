package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/3/13.
 */
public class ThPayVo {

    private String input_charset;
    private String notify_url;
    private String return_url;
    private String pay_type;
    private String bank_code;
    private String merchant_code;
    private String order_no;
    private String order_amount;
    private String order_time;
    private String product_name;
    private String product_num;
    /*** 来路域名 */
    private String req_referer;
    private String customer_ip;
    private String customer_phone;
    private String receive_address;
    private String return_params;
    private String sign;
    private String notify_type;
    private String trade_no;
    private String trade_time;
    /** success 交易成功 failed 交易失败 paying 交易中*/
    private String trade_status;

    public void setWxZfb(String merchantCode, String orderId, String orderAmount, String order_time, String product_name, String product_num, String loginName, String notifyUrl, String shopUrl, String req_referer, String customer_ip) {
        this.notify_url = notifyUrl;
        this.return_url = shopUrl;
        this.merchant_code = merchantCode;
        this.order_no = orderId;
        this.order_amount = orderAmount;
        this.order_time = order_time;
        this.product_name = product_name;
        this.product_num = product_num;
        this.req_referer = req_referer;
        this.customer_ip = customer_ip;
        this.customer_phone = "";
        this.receive_address = "";
        this.return_params = loginName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_num() {
        return product_num;
    }

    public void setProduct_num(String product_num) {
        this.product_num = product_num;
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

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getReceive_address() {
        return receive_address;
    }

    public void setReceive_address(String receive_address) {
        this.receive_address = receive_address;
    }

    public String getReturn_params() {
        return return_params;
    }

    public void setReturn_params(String return_params) {
        this.return_params = return_params;
    }

    public String getNotify_type() {
        return notify_type;
    }

    public void setNotify_type(String notify_type) {
        this.notify_type = notify_type;
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
}
