package dfh.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	public static Map<String, String> titles=new HashMap<String, String>();
	public static String EMAIL_NOBODY_HTML="";
	public static String EMAIL_REGISTER_BODY_HTML="";
	
	public static final Integer FLAG_TRUE = 0;
	public static final Integer FLAG_FALSE = 1;
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
	public static final String SEQ_TRANSFERID = "SEQ_TRANSFERID";
	public static final String SEQ_PROPOSALID = "SEQ_PROPOSALID";
	public static final String SEQ_NETPAYID = "SEQ_NETPAYID";
	public static final String SEQ_AUTOTASKID = "SEQ_AUTOTASKID";
	public static String SESSION_PAYPASSWORD = "payPassword";
	public static final String SEQ_VISITORID = "SEQ_VISITORID";
	public static final String SEQ_USERID = "SEQ_USERID";
	public static final String SEQ_COMMISSIONID = "SEQ_COMMISSIONID";
	public static final String ERROR_CLASS = "wwctrl";
	public static Integer SYN_BETRECORDS_DELAY = 30; // Minutes
	public static Double OPEN_MIN_MONEY = 100.0;// 网上申请开户最低存款金额:100RMB
	public static Double NETPAYADD_MIN_MONEY = 100.0;// 网上充值最低金额100RMB
	public static Double NETPAYADD_MAX_MONEY = 1000.0;// 网上充值最高金额
	public static String SESSION_CUSTOMERID = "customer"; // 会员
	public static String PT_SESSION_USER = "ptPassword"; // 会员
	public static String PT_SESSION_ID = "ptId"; // 会员
	public static String SESSION_DT_SLOTKEY = "slotKey"; // dt登录的token
	public static String SESSION_DT_GAMEURL = "gameurl"; // dt登录游戏的url
	public static String SESSION_AGTRYUSER = "agTryUser"; // ag试玩账号
	public static String SESSION_OPERATORID = "operator";// 管理员后台
	public static String SESSION_RANDID = "rand";// 验证码
	public static String SESSION_AG_TRY_RANDID = "agrand";// 验证码
	public static String SESSION_PARTNERID = "parnter";// 合作伙伴
	public static String SESSION_PHONECODE = "PHONECODE";//手机语音验证码
	public static String COOKIE_NAME = "elf_agent";// 存储到客户端的KEY
	public static String JC_LOGINID = "jc_loginid";//JC登录UUID
	public static String NT_SESSION = "nt_session"; // NT登录key
	
	public static String USER_DEPOSIT="USER_DEPOSIT";

	public static final String NETPAYORDER = "e";// 在线支付本地单号开头字母
	public static final String COMMISSIONORDER = "cmm";// 佣金结算单号开头字母

	public static final String NETPAY_YEEPAY_NAME = "yeepay";// 易宝支付名称
	public static final Double OUTLAYRATE = 0.01;// 存取款在线支付费用比例
	public static final Double PROFITRATE = 0.002;// 拥金比例
	public static Double MATCH_ADDMONEY = 50.0;
	
	public static final String AUDIT_DETAIL="AUDIT_DETAIL";

	public static final String ENCODING = "UTF-8";
	public static final String DEFAULT_ACCOUNTTYPE = "借记卡";

	// 注册会员发送email的标题
	public static final String SEND_EMAIL_TITLE = "恭喜您成为龙都的";
	// 注册会员发送email的内容
	public static final String SEND_EMAIL_MSG = "您的用户名是:@username 密码是:@password";

	public static final String GENERATE_AUTO = "auto";

	public final static String[] CLIENT_URLS = new String[] { "http://www.e68.ph" };
	public final static String REQUEST_CONTEXT_PATH = "/asp/forceToRefreshSession.aspx";
	
/*	public final static String  HC_Key = "^gsYh^Rg" ;
	public final static String HC_MerNo = "34275" ;
	public final static String AdviceURL = "http://pay.jiekoue68.com:2112/hc/callBackPay.aspx" ;//（后台接收地址）
	public final static String ReturnURL = "http://www.yuanhuohuakj.com/"; //这里的值是汇潮回调商城的地址 ， 商城回调本项目的地址在php代码里写死（页面接收地址）
*/	
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
	public final static String payType="B2CDebit";
	
	//口袋微信1
	public final static String KDWX1_MerNo="1002397";
	public final static String KDWX1_KEY="2fed5e622429470fbbe7879ec8f6f315";
	public final static String KDWX1_Result_URL="http://pay.jiekoue68.com:2112/asp/payKdWxZfsReturn.aspx";
	public final static String KDWX1_Notify_URL="http://pay.zhongxingyue.cn/";
	
	//口袋微信2
	public final static String KDWX2_MerNo="1002400";
	public final static String KDWX2_KEY="017f1310e0ac4e43b79e2d70f4ce69a1";
	public final static String KDWX2_Result_URL="http://pay.jiekoue68.com:2112/asp/payKdWxZfsReturn.aspx";
	public final static String KDWX2_Notify_URL="http://pay.zhongxingyue.cn/";
	
	//口袋微信3
	public final static String KDWX3_MerNo="1002401";
	public final static String KDWX3_KEY="6c5d20ddf3654a59bd792a5c3fcd9b3d";
	public final static String KDWX3_Result_URL="http://pay.jiekoue68.com:2112/asp/payKdWxZfsReturn.aspx";
	public final static String KDWX3_Notify_URL="http://pay.zhongxingyue.cn/";
	
	//口袋支付宝
	public final static String  KDZFB_MerNo="1004058";
	public final static String KDZFB_KEY="b8388969212e4e60b4718267e2a4390e";
	public final static String KDZFB_Result_URL="http://pay.jiekoue68.com:2112/asp/payKdWxZfsReturn.aspx";
	public final static String  KDZFB_Notify_URL="http://www.zhkangtsmy.com/";
	
	//优付支付宝
	public final static String  YFZFB_MerNo="11136";
	public final static String  YFZFB_KEY="ee2c8337cbedb3d3c43e4650a566af7f";
	public final static String  YFZFB_Result_URL="http://www.glubanyunshang.com/order/returnUrl.html";
	public final static String  YFZFB_Notify_URL="http://www.glubanyunshang.com/";
	public final static String  YFZF_apiUrl="https://api.yompay.com/";
	
	//优付微信
	public final static String  YFWX_MerNo="11212";
	public final static String  YFWX_KEY="3f8e6817763b6afae14daeb2e3d9f126";
	public final static String  YFWX_Result_URL="http://www.glubanyunshang.com/order/returnUrl.html";
	public final static String  YFWX_Notify_URL="http://www.glubanyunshang.com/";
	
	//银宝支付宝
	public final static String  YBZFB_MerNo="19878";
	public final static String  YBZFB_KEY="6587d21e5d615b20202fa34aa9dba8ed";
	public final static String  YBZFB_Result_URL="http://pay.jiekoue68.com:2112/asp/payYbZfbReturn.aspx";
	public final static String  YBZFB_Notify_URL="http://xinyisifuzhuangmy.com/";
	
	//千网支付宝
	public final static String  QWZFB_MerNo="7517";
	public final static String  QWZFB_KEY="0b84eae6514b4cf083a6352480ce399b";
	public final static String  QWZFB_AIP_URL="http://apika.10001000.com/chargebank.aspx";
	public final static String  QWZFB_Result_URL="http://pay.jiekoue68.com:2112/asp/payQwZfReturn.aspx";
	public final static String  QWZFB_Notify_URL="http://www.glubanyunshang.com/";
	
	//口袋支付宝2
	public final static String  KDZFB2_MerNo="1004048";
	public final static String KDZFB2_KEY="c5c142a0d669492c878727e58900457e";
	public final static String KDZFB2_Result_URL="http://pay.jiekoue68.com:2112/asp/payKdWxZfsReturn.aspx";
	public final static String  KDZFB2_Notify_URL="http://www.zhdingtsmy.com/";
	
	//千网微信
	public final static String  QWWX_MerNo="8080";
	public final static String  QWWX_KEY="d9b8bd3e8494493e80bbc6251fa1d359";
	public final static String  QWWX_AIP_URL="http://apika.10001000.com/chargebank.aspx";
	public final static String  QWWX_Result_URL="http://pay.jiekoue68.com:2112/asp/payQwZfReturn.aspx";
	public final static String  QWWX_Notify_URL="http://www.glubanyunshang.com/";
	
	
	//产品提款
	public final static String PRODUCT_NAME="ld";
		
	
	public final static String AGENG_CODE = "AGENG_CODE";
	
//	public final static String CPUID = "cpuid";
	public static final String OURDEVICE = "ourdeviceid";   //记录我们自己采集信息计算出的设备ID
	public static final String DEVICE4WEB = "device4web";   //我们自己采集的原始设备信息，存放在前台
	public static final String mobileDeviceID = "mobileDeviceID";   //移动端设备ID
	public static final String mobilePlatform = "mobilePlatform";   //移动端设备系统类型（Android|IOS）
	
	public static final String LIVE800INFOVALUE = "infoValue4Live800";
	public static final String LIVE800KEY = "K8nR98L66dkZfnVu";
	public static final String AGENTVIP = "AGENTVIP";

	public static final String NEEDMODIFY = "NEEDMODIFY"; //是否为安全密码
	
	public static final String SMSVALIDATE = "SMSVALIDATE";
	
	
	// 站内信

	// 0发帖给玩家 1发帖给管理员
	public static Integer TOPIC_TOPIC_TYPE_USER = 0;
	public static Integer TOPIC_TOPIC_TYPE_ADMIN = 1;
	// 0 单发 1 群发
	public static Integer TOPIC_STATUS_PERSONAL = 0;
	public static Integer TOPIC_STATUS_GROUP = 1;

	// 100 全部会员 101全部代理 102个人群发 103代表是按照客服推荐码群发104代表是按照代理账号群发 105代表管理员 大于0对应玩家等级
	public static Integer TOPIC_SEND_TYPE_ALL_MEMBER = 100;
	public static Integer TOPIC_SEND_TYPE_ALL_AGENT = 101;
	public static Integer TOPIC_SEND_TYPE_PERSONAL_GROUP = 102;
	public static Integer TOPIC_SEND_TYPE_CODE_GROUP = 103;
	public static Integer TOPIC_SEND_TYPE_AGENT_GROUP = 104;
	public static Integer TOPIC_SEND_TYPE_ADMIN = 105;
	// 已读和未读
	public static Integer TOPIC_STATUS_IS_READ_YES = 1;
	public static Integer TOPIC_STATUS_IS_READ_NO = 0;
	
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
