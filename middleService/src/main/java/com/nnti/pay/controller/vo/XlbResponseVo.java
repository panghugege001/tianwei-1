package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/1/26.
 * 迅联接口返回json对象
 */
public class XlbResponseVo {  

    private String code;
    private String sign;
    private String resultCode;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
