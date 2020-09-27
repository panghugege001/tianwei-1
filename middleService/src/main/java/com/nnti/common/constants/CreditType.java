package com.nnti.common.constants;

public enum CreditType {

	PHONEFEE_CHARGE("PHONEFEE_CHARGE","抢话费活动充值"),
    NETPAY("NETPAY", "在线支付"),
    CASHOUT_RETURN("CASHOUT_RETURN", "退还提款"),
    CASHOUT("CASHOUT", "客户提款");

    private String code;

    private String text;

    CreditType(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
