package dfh.model.enums;

/**
 * 管理员动作
 * 
 *
 */
public enum OperationLogType
{
  LOGIN("LOGIN", "登录"),
  AUDIT("AUDIT", "审核"),
  EXCUTE("EXCUTE", "执行"), 
  ADDUSER("ADDUSER","添加用户"),
  ADDAGENT("ADDAGENT","增加代理"),
  REPAIR_PAYORDER("REPAIR_PAYORDER", "补单"),
    SEND_PHONESMS_LOG("SEND_PHONESMS_LOG","发送手机短信"),

    RESET_PWD("RESET_PWD", "重设密码"),
  MODIFY_CUS_INFO("MODIFY_CUS_INFO", "修改会员邮箱电话"),
  MODIFY_AGENT_INFO("MODIFY_AGENT_INFO", "修改代理资料"),
  ADD_ANNOUNCEMENT("ADD_ANNOUNCEMENT", "添加公告"), 
  DEL_ANNOUNCEMENT("DEL_ANNOUNCEMENT", "删除公告"),
  MODIFY_ANNOUNCEMENT("MODIFY_ANNOUNCEMENT","更新公告"),
  ADD_ACT_CALENDAR("ADD_ACT_CALENDAR", "添加活动日历"),
  MODIFY_ACT_CALENDAR("MODIFY_ACT_CALENDAR","更新活动日历"),
  SYN_BET_RECORDS("SYN_BET_RECORDS", "同步投注记录"),
  ENABLE("ENABLE", "启用或禁用会员"),
    REMARK_EDIT("REMARK_EDIT", "备注清除"),
  RATE("RATE", "修改会员反水"),
  INTRO("INTRO","修改会员推荐码"),
  SETLEVEL("SETLEVEL", "设定会员等级"),
  SETWARNLEVEL("SETWARNLEVEL", "设定警告级别"),
  CHANGE_CREDIT_MANUAL("CHANGE_CREDIT_MANUAL", "手工增减额度"),
  CHANGE_CREDIT_QUOTAL("CHANGE_CREDIT_QUOTAL", "增减额度"),
  CG_BKCREDIT_MANUAL("CG_BKCREDIT_MANUAL", "手工增减银行额度"),
  RELEASE_SELF_YOUHUI("RELEASE_SELF_YOUHUI", "修改自助反水优惠转账限制"),
  CREATE_SUB_OP("CREATE_SUB_OP", "创建下级管理员"),
  CHENGE_EMPLOYEE_STATUS("EMPLOYEE_STATUS", "改变雇员状态"),
  DELETE_EMPLOYEE("DELETE_EMPLOYEE", "删除雇员"),
  MODIFY_OWN_PWD("MODIFY_OWN_PWD", "修改管理员密码"),
  CREATE_DEPOSIT_ORDER("CREATE_DEPOSIT_ORDER","创建存款附言"),
  MODIFYPLATFORMRECORD("MODIFYPLATFORMRECORD","修改游戏平台返水记录"),
  OPERTR_EXCEPTION("OPERTR_EXCEPTION","账号异常"),
  RELIEVE_PTLIMIT("RELIEVE_PTLIMIT", "解除pt流水限制"),
  BANKS_OPERATING("BANKS_OPERATING", "银行操作记录");
//  SET_PARTNERLOWER("SET_PARTNERLOWER", "设置合作伙伴下线"),
//  AUTO_CMN("AUTO_CMN","自动生成合作伙伴佣金"),
//  AUTO_CMN_EXCUTE("AUTO_CMN_EXCUTE","执行结算合作伙伴佣金");

	public static String getText(String code) {
	    OperationLogType[] p = values();
	    for (int i = 0; i < p.length; ++i) {
	      OperationLogType type = p[i];
	      if (type.getCode().equals(code))
	        return type.getText();
	    }
	    return null;
	  }
	private String code;

	private String text;


  private OperationLogType(String code, String text) {
	this.code = code;
	this.text = text;
}

  public String getCode()
  {
    return this.code;
  }

  public String getText() {
    return this.text;
  }
}

