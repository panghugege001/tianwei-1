package dfh.utils;

public class Constants {

	
	public static String EMAIL_NOBODY_HTML="";
	public static String EMAIL_REGISTER_BODY_HTML="";
	public static String EMAIL_CASHIN_BODY_HTML="";
	public static String EMAIL_CASHOUT_BODY_HTML="";
	
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
	public static final String SEQ_PROPOSALIDBANKTRANSFER = "SEQ_PROPOSALIDBANKTRANSFER";
	
	public static final String SEQ_COUPON_ID = "SEQ_COUPON_ID";
	public static final String SEQ_STATION_LETTERS_ID = "SEQ_STATION_LETTERS_ID";
	public static final String SEQ_BUSINESSPROPOSALID = "SEQ_BUSINESSPROPOSALID";
	public static final String SEQ_PAY_C_ID = "SEQ_PAY_C_ID";
	public static final String SEQ_NETPAYID = "SEQ_NETPAYID";
	public static final String SEQ_AUTOTASKID = "SEQ_AUTOTASKID";
	public static final String SEQ_VISITORID = "SEQ_VISITORID";
	public static final String SEQ_USERID = "SEQ_USERID";
	public static final String SEQ_USERID_BAK = "SEQ_USERID_BAK";
	public static final String SEQ_COMMISSIONID = "SEQ_COMMISSIONID";
	public static final String ERROR_CLASS = "wwctrl";
	public static Integer SYN_BETRECORDS_DELAY = 30; // Minutes
	public static Double OPEN_MIN_MONEY = 100.0;// 网上申请开户最低存款金额:100RMB
	public static Double NETPAYADD_MIN_MONEY = 100.0;// 网上充值最低金额100RMB
	public static Double NETPAYADD_MAX_MONEY = 1000.0;// 网上充值最高金额
	public static String SESSION_CUSTOMERID = "customer"; // 会员
	public static String SESSION_OPERATORID = "operator";// 管理员后台
	public static String SESSION_OPERATORNAME = "operatename";// 管理员后台
	public static String SESSION_RANDID = "rand";// 验证码
	public static String SESSION_EMAIL_RANDID = "emailrand";// 验证码
	public static String SESSION_PARTNERID = "parnter";// 合作伙伴
	public static String SESSION_AUTH_ROLE = "AUTH_ROLE";// 用户的角色列表
	public static String SESSION_AUTH_PERMISSION = "AUTH_PERMISSION";// 用户的权限列表
	public static String SESSION_AUTH_CODE = "AUTH_CODE";// 用户的权限代码列表
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

	// 注册会员发送email的标题
	public static final String SEND_EMAIL_TITLE = "恭喜您成为9win的";
	// 注册会员发送email的内容
	public static final String SEND_EMAIL_MSG = "您的用户名是:@username 密码是:@password";

	public static final String GENERATE_AUTO = "auto";

	public final static String[] CLIENT_URLS = new String[] { "http://www.e68.ph" };
	public final static String REQUEST_CONTEXT_PATH = "/asp/forceToRefreshSession.aspx";
	
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

	public static Integer TOPIC_STATUS_IS_READ_YES = 1;
	public static Integer TOPIC_STATUS_IS_READ_NO = 0;
	// 是否审批 0 已审批 1 未审批
	public static Integer TOPIC_STATUS_IS_FLAG_YES = 0;
	public static Integer TOPIC_STATUS_IS_FLAG_NO = 1;

	// 0 有效 1 无效。
	public static Integer TOPIC_IS_VALID_YES = 0;
	public static Integer TOPIC_IS_VALID_NO = 1;

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
		if (transferConsMoney > 50.0)
			transferConsMoney = 50.0;
		return transferConsMoney;
	}

	public void setDEFAULT_CREDIT(Double default_credit) {
		DEFAULT_CREDIT = default_credit;
	}

	public void setSYN_BETRECORDS_DELAY(Integer delay) {
		SYN_BETRECORDS_DELAY = delay;
	}

}
