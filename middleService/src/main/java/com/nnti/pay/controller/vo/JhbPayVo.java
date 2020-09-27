package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/5/16.
 */
public class JhbPayVo {

    private String merchno;
    private String amount;
    private String traceno;
    private String payType;
    private String goodsName;
    private String notifyUrl;
    private String remark;

    private String respCode;
    private String message;
    private String refno;
    private String barCode;

    private String transDate;
    private String transTime;
    private String merchName;
    private String customerno;
    private String orderno;
    private String channelOrderno;
    private String channelTraceno;
    private String openId;
    private String status;
    private String signature;
    private String cust1;
    private String cust2;
    private String cust3;


    public void setZfbWx(String merchno, String amount, String traceno, String goodsName, String notifyUrl, String cust1) {
        this.merchno = merchno;
        this.amount = amount;
        this.traceno = traceno;
        this.goodsName = goodsName;
        this.notifyUrl = notifyUrl;
        this.cust1 = cust1;
    }

    public String getMerchno() {
        return merchno;
    }

    public void setMerchno(String merchno) {
        this.merchno = merchno;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTraceno() {
        return traceno;
    }

    public void setTraceno(String traceno) {
        this.traceno = traceno;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRefno() {
        return refno;
    }

    public void setRefno(String refno) {
        this.refno = refno;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getCustomerno() {
        return customerno;
    }

    public void setCustomerno(String customerno) {
        this.customerno = customerno;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getChannelOrderno() {
        return channelOrderno;
    }

    public void setChannelOrderno(String channelOrderno) {
        this.channelOrderno = channelOrderno;
    }

    public String getChannelTraceno() {
        return channelTraceno;
    }

    public void setChannelTraceno(String channelTraceno) {
        this.channelTraceno = channelTraceno;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCust1() {
        return cust1;
    }

    public void setCust1(String cust1) {
        this.cust1 = cust1;
    }

    public String getCust2() {
        return cust2;
    }

    public void setCust2(String cust2) {
        this.cust2 = cust2;
    }

    public String getCust3() {
        return cust3;
    }

    public void setCust3(String cust3) {
        this.cust3 = cust3;
    }
}

