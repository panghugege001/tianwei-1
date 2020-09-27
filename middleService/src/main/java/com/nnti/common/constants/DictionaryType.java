package com.nnti.common.constants;

/**
 * Created by wander on 2017/2/15.
 * 字典表相关
 */
public enum DictionaryType {

    TRUST_IP_TYPE("TRUST_IP_TYPE","IP白名单"),
    POINT_CARD_TYPE("POINT_CARD_TYPE","点卡类型"),
    DB_TRUST_IP_TYPE("DB_TRUST_IP_TYPE","多宝支付IP白名单"),
    HC_TRUST_IP_TYPE("HC_TRUST_IP_TYPE","汇潮支付IP白名单"),
    KD_TRUST_IP_TYPE("KD_TRUST_IP_TYPE","口袋支付IP白名单"),
    QW_TRUST_IP_TYPE("QW_TRUST_IP_TYPE","千网支付IP白名单"),
    RB_TRUST_IP_TYPE("RB_TRUST_IP_TYPE","荣邦支付IP白名单"),
    TH_TRUST_IP_TYPE("TH_TRUST_IP_TYPE","通汇支付IP白名单"),
    XB_TRUST_IP_TYPE("XB_TRUST_IP_TYPE","新贝支付IP白名单"),   
    XLB_TRUST_IP_TYPE("XLB_TRUST_IP_TYPE","讯联宝支付IP白名单"),    
    YF_TRUST_IP_TYPE("YF_TRUST_IP_TYPE","优付支付IP白名单"),
    JHZ_TRUST_IP_TYPE("JHZ_TRUST_IP_TYPE","金海哲支付IP白名单"),
    XFT_TRUST_IP_TYPE("XFT_TRUST_IP_TYPE","讯付通支付IP白名单"),
    ZHF_TRUST_IP_TYPE("ZHF_TRUST_IP_TYPE","讯付通支付IP白名单"),
    RX_TRUST_IP_TYPE("RX_TRUST_IP_TYPE","仁信支付IP白名单"),
    DDB_TRUST_IP_TYPE("DDB_TRUST_IP_TYPE","多得宝支付IP白名单"),
    SHB_TRUST_IP_TYPE("SHB_TRUST_IP_TYPE","速汇宝支付IP白名单"),
    JHB_TRUST_IP_TYPE("JHB_TRUST_IP_TYPE","聚汇宝支付IP白名单"),
    LF_TRUST_IP_TYPE("LF_TRUST_IP_TYPE","乐付支付IP白名单"),
    JH_TRUST_IP_TYPE("JH_TRUST_IP_TYPE","聚会支付IP白名单"),
    ZF_TRUST_IP_TYPE("ZF_TRUST_IP_TYPE","智付支付IP白名单"),
	XM_TRUST_IP_TYPE("XM_TRUST_IP_TYPE","新码支付IP白名单"),
	YIFU_TRUST_IP_TYPE("YIFU_TRUST_IP_TYPE","亿付支付IP白名单"),
	ONEPAT_TRUST_IP_TYPE("ONEPAY_TRUST_IP_TYPE","万付支付IP白名单"), 
	XXLB_TRUST_IP_TYPE("XXLB_TRUST_IP_TYPE","新讯联宝支付IP白名单"),
	EBAO_TRUST_IP_TYPE("EBAO_TRUST_IP_TYPE","E宝支付IP白名单"),
	HT_TRUST_IP_TYPE("HT_TRUST_IP_TYPE","汇通支付IP白名单"),
	MIF_TRUST_IP_TYPE("MIF_TRUST_IP_TYPE","米付IP白名单"),
	SANERPay_TRUST_IP_TYPE("SANERPay_TRUST_IP_TYPE","32PayIP白名单"),
	ZBPay_TRUST_IP_TYPE("ZBPay_TRUST_IP_TYPE","众宝IP白名单"),
	GTPay_TRUST_IP_TYPE("GTPay_TRUST_IP_TYPE","高通IP白名单"),
	ZPPay_TRUST_IP_TYPE("ZPPay_TRUST_IP_TYPE","直付IP白名单"),
	BSPay_TRUST_IP_TYPE("BSPay_TRUST_IP_TYPE","百盛IP白名单"),
	LKPay_TRUST_IP_TYPE("LKPay_TRUST_IP_TYPE","乐卡IP白名单"),
	MMPay_TRUST_IP_TYPE("MMPay_TRUST_IP_TYPE","么么IP白名单"),
	YMPay_TRUST_IP_TYPE("YMPay_TRUST_IP_TYPE","怡秒IP白名单"),
	DDPay_TRUST_IP_TYPE("DDPay_TRUST_IP_TYPE","多多支付IP白名单"),
	MBKJ_TRUST_IP_TYPE("MBKJ_TRUST_IP_TYPE","摩宝快捷支付IP白名单"),
	HTFPay_TRUST_IP_TYPE("HTFPay_TRUST_IP_TYPE","汇天付IP白名单"),
	WFPay_TRUST_IP_TYPE("WFPay_TRUST_IP_TYPE","微付IP白名单"),
	ZinFTongPay_TRUST_IP_TYPE("ZinFTongPay_TRUST_IP_TYPE","信付通IP白名单"),
	RRPAY_TRUST_IP_TYPE("RRPAY_TRUST_IP_TYPE","人人支付IP白名单"),
	RuYIFuPay_TRUST_IP_TYPE("RuYIFuPay_TRUST_IP_TYPE","如一付IP白名单"),
	HDPAY_TRUST_IP_TYPE("HDPAY_TRUST_IP_TYPE","汇达支付IP白名单"),
    JAN_TRUST_IP_TYPE("JAN_TRUST_IP_TYPE","久安支付IP白名单"),

	//秒付宝 回调白名单
	MFBTL_TRUST_IP_TYPE("MFBTL_TRUST_IP_TYPE","秒付宝白名单");

    String name;
    String text;

    DictionaryType(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
