package com.nnti.pay.controller.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by wander on 2017/4/1.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RbPayVo {

    private String openApiUrl;
    private String appid;
    private String session;
    private String secretkey;

    private String ordernumber;
    private String body;
    private String paymenttypeid;
    private String subpaymenttypeid;
    private String amount;// 单位分 100=1元
    private String businesstype;
    private String extraparams;
    private String fronturl;
    private String backurl;

    private String masget;

    private String businesstypename;
    private String qrcode;
    private String subpaymenttypename;
    private String payorderid;
    private String paymenttypename;
    private String respcode;
    private String createdtime;
    private String companyid;
    private String ordercode;
    private String respmsg;
    private String qrcodeurl;


    public void setWxZfb(String orderId, String loginName, String amount, String loginName1, String notifyUrl, String shopUrl) {
        this.ordernumber = orderId;
        this.body = loginName;
        this.amount = amount;
        this.extraparams = loginName;
        this.fronturl = shopUrl;
        this.backurl = notifyUrl;
    }

    public String getOpenApiUrl() {
        return openApiUrl;
    }

    public void setOpenApiUrl(String openApiUrl) {
        this.openApiUrl = openApiUrl;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPaymenttypeid() {
        return paymenttypeid;
    }

    public void setPaymenttypeid(String paymenttypeid) {
        this.paymenttypeid = paymenttypeid;
    }

    public String getSubpaymenttypeid() {
        return subpaymenttypeid;
    }

    public void setSubpaymenttypeid(String subpaymenttypeid) {
        this.subpaymenttypeid = subpaymenttypeid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBusinesstype() {
        return businesstype;
    }

    public void setBusinesstype(String businesstype) {
        this.businesstype = businesstype;
    }

    public String getExtraparams() {
        return extraparams;
    }

    public void setExtraparams(String extraparams) {
        this.extraparams = extraparams;
    }

    public String getFronturl() {
        return fronturl;
    }

    public void setFronturl(String fronturl) {
        this.fronturl = fronturl;
    }

    public String getBackurl() {
        return backurl;
    }

    public void setBackurl(String backurl) {
        this.backurl = backurl;
    }

    public String getMasget() {
        return masget;
    }

    public void setMasget(String masget) {
        this.masget = masget;
    }

    public String getBusinesstypename() {
        return businesstypename;
    }

    public void setBusinesstypename(String businesstypename) {
        this.businesstypename = businesstypename;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getSubpaymenttypename() {
        return subpaymenttypename;
    }

    public void setSubpaymenttypename(String subpaymenttypename) {
        this.subpaymenttypename = subpaymenttypename;
    }

    public String getPayorderid() {
        return payorderid;
    }

    public void setPayorderid(String payorderid) {
        this.payorderid = payorderid;
    }

    public String getPaymenttypename() {
        return paymenttypename;
    }

    public void setPaymenttypename(String paymenttypename) {
        this.paymenttypename = paymenttypename;
    }

    public String getRespcode() {
        return respcode;
    }

    public void setRespcode(String respcode) {
        this.respcode = respcode;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public String getRespmsg() {
        return respmsg;
    }

    public void setRespmsg(String respmsg) {
        this.respmsg = respmsg;
    }

    public String getQrcodeurl() {
        return qrcodeurl;
    }

    public void setQrcodeurl(String qrcodeurl) {
        this.qrcodeurl = qrcodeurl;
    }
}
