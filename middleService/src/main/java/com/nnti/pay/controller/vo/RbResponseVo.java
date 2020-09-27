package com.nnti.pay.controller.vo;

/**
 * Created by wander on 2017/4/1.
 */
public class RbResponseVo {

    private String ret;

    private String message;

    private RbPayVo data;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RbPayVo getData() {
        return data;
    }

    public void setData(RbPayVo data) {
        this.data = data;
    }
}
