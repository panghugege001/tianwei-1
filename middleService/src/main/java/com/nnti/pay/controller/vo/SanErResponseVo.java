package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/1/26.
 * 32Pay接口返回json对象
 */
public class SanErResponseVo {

    private String errcode;
    private String errmsg;
    private String qrcode;
    private String userorderno;
    private String sysorderno;
    private String sign;
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getUserorderno() {
		return userorderno;
	}
	public void setUserorderno(String userorderno) {
		this.userorderno = userorderno;
	}
	public String getSysorderno() {
		return sysorderno;
	}
	public void setSysorderno(String sysorderno) {
		this.sysorderno = sysorderno;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

  
}
