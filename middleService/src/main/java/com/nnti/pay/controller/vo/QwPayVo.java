package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/1/31.
 */
public class QwPayVo {

    private String parter;
    private String type;
    /*** 订单号 */
    private String orderid;
    private String callbackurl;
    private String hrefbackurl;
    private String value;
    /*** 备注信息，上行提交参数原样返回*/
    private String attach;
    private String payerIp;

    /*** 签名*/
    private String sign;

    /*** 支付结果*/
    private String opstate;

    /*** 支付金额*/
    private String ovalue;

    /*** 商户号 */
    private String cid;

    /*** 亿卡订单号*/
    private String ekaorderid;

    /*** 亿卡订单时间*/
    private String ekatime;
    /*** 支付结果中文说明*/
    private String msg;

    public QwPayVo() {
    }

    /*** 支付宝 微信参数值*/
    public void setZfbWx(String parter, String orderid, String callbackurl, String hrefbackurl, String value, String attach, String payerIp) {
        this.parter = parter;
        this.orderid = orderid;
        this.callbackurl = callbackurl;
        this.hrefbackurl = hrefbackurl;
        this.value = value;
        this.attach = attach;
        this.payerIp = payerIp;
    }

    public String getParter() {
        return parter;
    }

    public void setParter(String parter) {
        this.parter = parter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getPayerIp() {
        return payerIp;
    }

    public void setPayerIp(String payerIp) {
        this.payerIp = payerIp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOpstate() {
        return opstate;
    }

    public void setOpstate(String opstate) {
        this.opstate = opstate;
    }

    public String getOvalue() {
        return ovalue;
    }

    public void setOvalue(String ovalue) {
        this.ovalue = ovalue;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getEkaorderid() {
        return ekaorderid;
    }

    public void setEkaorderid(String ekaorderid) {
        this.ekaorderid = ekaorderid;
    }

    public String getEkatime() {
        return ekatime;
    }

    public void setEkatime(String ekatime) {
        this.ekatime = ekatime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
