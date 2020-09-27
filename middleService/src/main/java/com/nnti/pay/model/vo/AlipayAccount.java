package com.nnti.pay.model.vo;

/**
 * Created by wander on 2017/2/6.
 */
public class AlipayAccount {
    private String loginName;
    private int disable;

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setDisable(int disable) {
        this.disable = disable;
    }

    public int getDisable() {
        return disable;
    }
}
