package com.nnti.pay.model.dinpay;

import java.io.Serializable;

/**
 * Created by wander on 2017/1/19.
 */
public class WeixinRequest implements Serializable {

    private String sign;
    private String merchant_code;
    private String order_no;
    private String order_amount;
    private String service_type;
    private String notify_url;
    private String interface_version;
    private String sign_type;
    private String order_time;
    private String product_name;
    private String extra_return_param;

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

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getInterface_version() {
        return interface_version;
    }

    public void setInterface_version(String interface_version) {
        this.interface_version = interface_version;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
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

    public String getExtra_return_param() {
        return extra_return_param;
    }

    public void setExtra_return_param(String extra_return_param) {
        this.extra_return_param = extra_return_param;
    }

    @Override
    public String toString() {
        return "sign='" + sign + '\'' +
                "&merchant_code='" + merchant_code + '\'' +
                "&order_no='" + order_no + '\'' +
                "&order_amount='" + order_amount + '\'' +
                "&service_type='" + service_type + '\'' +
                "&notify_url='" + notify_url + '\'' +
                "&interface_version='" + interface_version + '\'' +
                "&sign_type='" + sign_type + '\'' +
                "&order_time='" + order_time + '\'' +
                "&product_name='" + product_name + '\'' +
                "&extra_return_param='" + extra_return_param + '\'';
    }
}
