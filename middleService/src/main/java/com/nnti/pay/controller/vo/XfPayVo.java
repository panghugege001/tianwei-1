package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/3/1.
 */
public class XfPayVo {

    /*** 商户号*/
    private String merNo;

    /*** 支付网关(支付宝填写ZFB,微信填写WX,支付宝WAP填写：ZFB_WAP) */
    private String netway;

    /*** 随机数*/
    private String random;

    /*** 订单号*/
    private String orderNum;

    /*** 金额（单位：分） */
    private String amount;

    /*** 商品名称*/
    private String goodsName;
    /*** 支付结果通知地址*/
    private String callBackUrl;

    /*** 回显地址*/
    private String callBackViewUrl;

    /*** 签名（字母大写）*/
    private String sign;

    /*** 00表示成功*/
    private String stateCode;

    /*** 状态描述*/
    private String msg;

    /*** 支付地址*/
    private String qrcodeUrl;

    /*** 支付状态 */
    private String payResult;

    private String payDate;

    public void setWxZfb(String merchantCode, String orderId, String orderAmount, String loginName, String notifyUrl, String shopUrl) {
        this.merNo = merchantCode;
        this.amount = orderAmount;
        this.orderNum=orderId;
        this.goodsName = loginName;
        this.callBackUrl = notifyUrl;
        this.callBackViewUrl = shopUrl;
    }


    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getNetway() {
        return netway;
    }

    public void setNetway(String netway) {
        this.netway = netway;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getCallBackViewUrl() {
        return callBackViewUrl;
    }

    public void setCallBackViewUrl(String callBackViewUrl) {
        this.callBackViewUrl = callBackViewUrl;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public String getPayResult() {
        return payResult;
    }

    public void setPayResult(String payResult) {
        this.payResult = payResult;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }
}
