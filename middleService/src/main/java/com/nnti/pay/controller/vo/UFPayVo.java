package com.nnti.pay.controller.vo;

/**
 * Created by pony on 2018/06/09.
 */
public class UFPayVo {

    private String parter;
    private String type;
    private String value;
    private String orderid;
    private String callbackurl;
    private String hrefbackurl;
    private String attach; //备注
    private String payerIp;
    private String sign;
    private String pc;
    private String wap;

    /*** 支付结果*/
    private String opstate; //0：支付成功	
    /*** 支付金额*/
    private String ovalue;
    /*** 商户号 */
    private String sysorderid;
    /*** 亿卡订单时间*/
    private String systime;
    /*** 支付结果中文说明*/
    private String msg;

    public UFPayVo() {
    }

    /*** 支付宝 微信参数值*/
    public void setOnlinePay(String parter, String orderid, String callbackurl, String hrefbackurl, String value, String attach, String payerIp) {
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

    public String getSysorderid() {
		return sysorderid;
	}

	public void setSysorderid(String sysorderid) {
		this.sysorderid = sysorderid;
	}

	public String getSystime() {
		return systime;
	}

	public void setSystime(String systime) {
		this.systime = systime;
	}

	public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

}
