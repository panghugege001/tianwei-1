package com.nnti.pay.controller.vo;

/**
 * Created by pony on 2017/6/20.
 */
public class TTBizCreateEbankPayOrder {
    private String out_trade_no;
    private String total_amount;
    private String subject;
    private String body;
    private String payer_ip;
    private String referer_url;
    private String page_url;
    private String channel = "";//CMB CCB ABC....
    private String pay_type = "DEBIT_CARD";

    public String getPage_url() {
        return page_url;
    }

    public void setPage_url(String page_url) {
        this.page_url = page_url;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPayer_ip() {
        return payer_ip;
    }

    public void setPayer_ip(String payer_ip) {
        this.payer_ip = payer_ip;
    }

    public String getReferer_url() {
        return referer_url;
    }

    public void setReferer_url(String referer_url) {
        this.referer_url = referer_url;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }
}
