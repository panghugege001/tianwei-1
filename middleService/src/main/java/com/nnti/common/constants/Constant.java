package com.nnti.common.constants;

public interface Constant {

    // 正整数正则表格式
	String REG_INTEGER = "^\\d*[1-9]\\d*$";
    // 整数正则表格式
	String REG_NUMERIC = "^[1-9][0-9]*([0-9]|.0)$";
	// 判断金额格式
	String REG_NUMERIC_DOUBLE = "^(([1-9][0-9]*)|([0-9]+\\.[0-9]{1,2}))$";
	// 主账户名称
	String SELF = "SELF";
	// 老虎机平台
	String SLOT = "SLOT";
	// 棋牌平台
	String CHESS = "CHESS";
	// BBIN平台
	String BBIN = "BBIN";
    // PT平台名称
	String PT = "PT";
	// PTSW平台名称
	String PTSW = "PTSW";
	// QT平台名称
	String QT = "QT";
	// NT平台名称
	String NT = "NT";
	// NEWPT平台名称
	String NEWPT = "NEWPT";
	// MG平台名称
	String MG = "MG";
	//BG平台
	String BG = "BG";
	// SW平台名称
	String SW = "SW";
	// DT平台名称
	String DT = "DT";
	// PG平台名称
	String PG = "PG";
	// TTG平台名称
	String TTG = "TTG";
	// PNG平台名称
	String PNG = "PNG";
	// N2LIVE平台名称
	String N2LIVE = "N2LIVE";
	// AGIN真人平台名称
	String AGIN = "AGIN";
	// 沙巴体育平台名称
	String SBA = "SBA";
	// 大满贯平台名称
	String MWG = "MWG";
	// 云谷彩票名称
	String OG = "OG";
	// EA平台名称
	String EA = "EA";
	//CQ9平台
	String CQ9 = "CQ9";
	// EBETAPP平台名称
	String EBETAPP = "EBETAPP";
	// 字符集编码
	String ENCODE = "UTF-8";
	// 转入标识
	String IN = "IN";
	// 转出标识
	String OUT = "OUT";
	// 数据入库类型
	String FROM_FRONT = "前台";
	// 数据入库类型
	String FROM_BACK = "后台";
	// 默认页数
	Integer PAGE_INDEX = 1;
	// 默认每页显示记录数
	Integer PAGE_SIZE = 5;
	
	public static final String DEFAULT_ACCOUNTTYPE = "借记卡";
	
    public static final Integer FLAG_FALSE = 1;
    /*** 支付开关 1.开 2关*/
    public static final Integer PAY_SWITCH_ON = 1;
    public static final Integer PAY_SWITCH_OFF = 2;

	/*** 支付是否禁用 1.启用 2禁止*/
    public static final Integer PAY_USEABLE_ON = 1;
    public static final Integer PAY_USEABLE_OFF = 2;
    
    /** 用户是否禁用 1 禁用 0 启用*/
    public static final Integer DISABLE = 1;
    
    /*** 真钱用户*/
    public static final String MONEY_CUSTOMER = "MONEY_CUSTOMER";
    
    /*** 代理用户*/
    public static final String AGENT = "AGENT";
    
	/*** 使用类型 1 wab 2 pc*/
    public static final Integer USE_TYPE_WEB = 1;
    public static final Integer USE_TYPE_PC = 2;

	/*** 支付使用KEY */
    public static final String PAY_KEY = "ule&Jdos8doUd3DB";
    
    
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
	// 审批0 未审批1
	public static Integer TOPIC_INFO_FLAG_APPROVAL = 0;
	public static Integer TOPIC_INFO_FLAG_NOT_APPROVAL = 1;

	// 0 有效 1 无效。
	public static Integer TOPIC_IS_VALID_YES = 0;
	public static Integer TOPIC_IS_VALID_NO = 1;
	
	
	public static final Integer APPREWARDVERSION = 1;
	public static final String APPPREFERENTIAL = "APP下载彩金";
	
	
}