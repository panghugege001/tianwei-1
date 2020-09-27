package com.nnti.pay.model.enums;

public enum HcResponseCode {

    SUCCESS("88", "成功"),
    FAILED("0", "失败"),
    GFXK("1", "高风险卡"),
    BLACKCARD("2", "黑卡"),
    SINGLEOVERLIMIT("3", "交易金额超过单笔限额"),
    MONTHOVERLIMIT("4", "本月交易金额超过月限额"),
    IPMANYTRADE("5", "同一Ip发生多次交易"),
    EMAILMANYTRADE("6", "同一邮箱发生多次交易"),
    NOMANYTRADE("7", "同一卡号发生多次交易"),
    COOKIEMANYTRADE("8", "同一Cookies发生多次交易"),
    MAXMINDHIGH("9", "Maxmind分值过高。"),
    UNREGISTER("10", "商户未注册"),
    KEYNOEXIST("11", "密匙不存在"),
    SIGNNOCOM("13", "签名不配备，数据发生篡改"),
    RETURNURLERROR("14", "返回网址错误"),
    MERUNOK("15", "商户未开通"),
    TDUNOK("16", "通道未开通"),
    BLACKCARDBEAN("17", "黑卡bean"),
    SYSTEMERROR("18", "系统出现异常"),
    VIPDEALING("19", "Vip商户交易处理中"),
    TDCONFIGNOOK("20", "通道信息设置不全"),
    CARDCGLIMIT("21", "卡号支付超过限制"),
    TRADEURLNOOK("22", "交易网址不正确"),
    MERCARDNOOK("23", "商户交易卡种不正确"),
    TYLCHCXDCJY("24", "同一流水号出现多次交易"),
    CKRXXCW("25", "持卡人信息错误"),
    JECGXZ("26", "金额超过限定值（50000）"),
    TDZDWSZ("27", "通道终端未设置"),
    HLSZCW("28", "汇率设置错误");

    private final String value;
    private final String name;

    HcResponseCode(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String value() {
        return value;
    }
}
