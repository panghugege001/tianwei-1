package com.nnti.common.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	public static Map<String, String> titles=new HashMap<String, String>();
	public static String EMAIL_NOBODY_HTML="";
	public static String EMAIL_REGISTER_BODY_HTML="";
	
	public static final Double MAXIMUMDTOUT = 388.0;// 大于388 可以转出 体验金 DT
	public static final Integer FLAG_TRUE = 0;
	public static final Integer FLAG_FALSE = 1;
	public static final Integer WITHDRAW = 0;
	public static final Integer DEPOSIT = 1;
	public static final Boolean IN = true;
	public static final Boolean OUT = false;
	public static final Boolean SUCESS = true;
	public static final Boolean FAIL = false;
	public static final Integer FLAG_DEPARTED = -1;
	public static final String STRING_TRUE = "true";
	public static final String STRING_FALSE = "false";
	public static final String FROM_FRONT = "前台";
	public static final String FROM_BACK = "后台";
	public static final String DEFAULT_PASSWORD = "888888";
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String DEFAULT_OPERATOR = "system";
	public static final Integer ENABLE = 0;
	public static final Integer DISABLE = 1;
	public static final Integer QUICKLY_COMMON = 1;
	public static final Integer QUICKLY_FAST = 2;
	public static final String PREFIX_MONEY_CUSTOMER = "";
	public static final String PREFIX_FREE_CUSTOMER = "";
	public static final String PREFIX_AGENT = "";
	public static final String PREFIX_PARTNER = "";
	public static final String PREFIX_PARTNER_SUBMEMBER = "";
	public static Double DEFAULT_CREDIT = 1000.0;
	public static Double CREDIT_ZERO = 0.0;
	public static final String SEQ_CSNO = "SEQ_CSNO";
	public static final String SEQ_TRANSFERID = "SEQ_TRANSFERID";
	public static final String SEQ_PROPOSALID = "SEQ_PROPOSALID";
	public static final String SEQ_NETPAYID = "SEQ_NETPAYID";
	public static final String SEQ_AUTOTASKID = "SEQ_AUTOTASKID";
	public static final String SEQ_VISITORID = "SEQ_VISITORID";
	public static final String SEQ_USERID = "SEQ_USERID";
	public static final String SEO_HFORDERNO ="SEO_HFORDERNO";
	public static final String SEO_HCORDERNO ="SEO_HCORDERNO";
	public static final String SEO_BFBORDERNO ="SEO_BFBORDERNO";
	public static final String SEO_HAIERORDERNO ="SEO_HAIERORDERNO"; 
	public static final String SEQ_GFBORDERNO ="SEQ_GFBORDERNO";
	public static final String SEQ_ZFBORDERNO = "SEQ_ZFBORDERNO";
	public static final String SEQ_WEIXINORDERNO = "SEQ_WEIXINORDERNO";
	public static final String SEQ_COUPON_ID = "SEQ_COUPON_ID";
	public static final String SEQ_PT8_SELF = "SEQ_PT8_SELF";
	public static final String SEO_KDZFORDERNO ="SEO_KDZFORDERNO";
	public static final String SEO_HHBZFORDERNO ="SEO_HHBZFORDERNO";
	public static final String SEO_HHBWXZFORDERNO ="SEO_HHBWXZFORDERNO";
	public static final String SEO_JUBZFBORDERNO ="SEO_JUBZFBORDERNO";
	public static final String SEO_XLBORDERNO ="SEO_XLBORDERNO";
	public static final String SEO_XLBWyORDERNO ="SEO_XLBWyORDERNO";
	public static final String SEQ_COMMISSIONID = "SEQ_COMMISSIONID";
	public static final String SEQ_TRYGAMEID = "SEQ_TRYGAMEID";
	public static final String SEO_ZHIFUORDERNO ="SEO_ZHIFUORDERNO";
	public static final String SEQ_STATION_LETTERS_ID = "SEQ_STATION_LETTERS_ID";
	public static final String SEO_LFWXORDERNO ="SEO_LFWXORDERNO";//乐富微信支付
	public static final String SEO_XBWXORDERNO ="SEO_XBWXORDERNO";
	public static final String SEO_KDWXZFORDERNO ="SEO_KDWXZFORDERNO";
	public static final String ERROR_CLASS = "wwctrl";
	public static Integer SYN_BETRECORDS_DELAY = 30; // Minutes
	public static Double OPEN_MIN_MONEY = 100.0;// 网上申请开户最低存款金额:100RMB
	public static Double NETPAYADD_MIN_MONEY = 100.0;// 网上充值最低金额100RMB
	public static Double NETPAYADD_MAX_MONEY = 1000.0;// 网上充值最高金额
	public static String SESSION_CUSTOMERID = "customer"; // 会员
	public static String SESSION_OPERATORID = "operator";// 管理员后台
	public static String SESSION_RANDID = "rand";// 验证码
	public static String SESSION_PARTNERID = "parnter";// 合作伙伴
	public static String COOKIE_NAME = "elf_agent";// 存储到客户端的KEY

	public static final String NETPAYORDER = "e";// 在线支付本地单号开头字母
	public static final String COMMISSIONORDER = "cmm";// 佣金结算单号开头字母

	public static final String NETPAY_YEEPAY_NAME = "yeepay";// 易宝支付名称
	public static final Double OUTLAYRATE = 0.01;// 存取款在线支付费用比例
	public static final Double PROFITRATE = 0.002;// 拥金比例
	public static Double MATCH_ADDMONEY = 50.0;
	
	public static final String AUDIT_DETAIL="AUDIT_DETAIL";

	public static final String ENCODING = "UTF-8";
	public static final String ENCODING_UTF16 = "UTF-16"; 
	public static final String DEFAULT_ACCOUNTTYPE = "借记卡";
	public static final String SEND_GETACC = "您好，您的账号找回:@username";
	// 注册会员发送email的标题
	public static final String SEND_EMAIL_TITLE = "恭喜您成为e路发的";
	// 注册会员发送email的内容
	public static final String SEND_EMAIL_MSG = "您的用户名是:@username 密码是:@password";

	public static final String GENERATE_AUTO = "auto";

	public final static String[] CLIENT_URLS = new String[] { "http://www.e68.ph" };
	public final static String REQUEST_CONTEXT_PATH = "/asp/forceToRefreshSession.aspx";
	
	//汇潮快捷支付
	public final static String  HC_Key = "PdpmRceYFaKwBtb07GcNANCpKtGHtS6EbxPLiE5ZNrc=";
	public final static String HC_MerNo = "34273";
	public final static String AdviceURL = "http://pay.jiekoue68.com:2112/hc/callBackYmdPay.aspx" ;//（后台接收地址）
	public final static String ReturnURL = "http://www.gzhuipingkeji.com"; //这里的值是汇潮回调商城的地址 ， 商城回调本项目的地址在php代码里写死（页面接收地址）
	
	//汇潮网银支付
	public final static String  MerNo="40886";
	public final static String MD5key="4mBvoq1dtL5eke9qrzy1fNwFGNSb4rXmyW915TpHPq0=";
	public final static String ReturnURLYmd="http://www.zhkangtsmy.com";
	public final static String AdviceURLYmd="http://pay.jiekoue68.com:2112/hc/callBackYmdPay.aspx";


	public static String getText(Integer trueOrFalse) {
		if (FLAG_TRUE.intValue() == trueOrFalse.intValue())
			return "是";
		else
			return "否";
	}

	public static String getTitleText(String loginname) {
		String title = null;
		if (loginname == null) {
			title = null;
		} else if (loginname.startsWith(PREFIX_MONEY_CUSTOMER)) {
			title = "真钱会员";
		} else if (loginname.startsWith(PREFIX_PARTNER)) {
			title = "合作伙伴";
		} else if (loginname.startsWith(PREFIX_PARTNER_SUBMEMBER)) {
			title = "合作伙伴下线";
		} else if (loginname.startsWith(PREFIX_AGENT)) {
			title = "代理";
		} else if (loginname.startsWith(PREFIX_FREE_CUSTOMER)) {
			title = "试玩会员";
		}
		return title;
	}

	public static Double getTransferConsMoney(Double money) {
		Double transferConsMoney = 0.0;
		transferConsMoney = money * 0.01;
		if (transferConsMoney > 100.0)
			transferConsMoney = 100.0;
		return transferConsMoney;
	}

	public void setDEFAULT_CREDIT(Double default_credit) {
		DEFAULT_CREDIT = default_credit;
	}

	public void setSYN_BETRECORDS_DELAY(Integer delay) {
		SYN_BETRECORDS_DELAY = delay;
	}

}
