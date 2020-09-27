package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/4/22.
 */
public class KcPayVo {

    private String partner;
    private String banktype;
    private String paymoney;
    private String ordernumber;
    private String callbackurl;
    private String hrefbackurl;
    private String attach;
    private String isshow;
    private String sign;
    private String url;
    private String orderstatus;
    private String sysnumber;


    public void setZfbWx(String merchantCode, String orderId, String notifyUrl, String shopUrl, String orderAmount, String loginName) {
        this.partner = merchantCode;
        this.ordernumber = orderId;
        this.callbackurl = notifyUrl;
        this.hrefbackurl = shopUrl;
        this.paymoney = orderAmount;
        this.attach = loginName;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getBanktype() {
        return banktype;
    }

    public void setBanktype(String banktype) {
        this.banktype = banktype;
    }

    public String getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(String paymoney) {
        this.paymoney = paymoney;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }

    public String getHrefbackurl() {
        return hrefbackurl;
    }

    public void setHrefbackurl(String hrefbackurl) {
        this.hrefbackurl = hrefbackurl;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getSysnumber() {
        return sysnumber;
    }

    public void setSysnumber(String sysnumber) {
        this.sysnumber = sysnumber;
    }
}
