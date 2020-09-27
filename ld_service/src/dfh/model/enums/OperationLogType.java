package dfh.model.enums;

public enum OperationLogType
{
  LOGIN("LOGIN", "登录"),
  AUDIT("AUDIT", "审核"),
  EXCUTE("EXCUTE", "执行"), 
  ADDUSER("ADDUSER","添加用户"),
  ADDAGENT("ADDAGENT","增加代理"),
  REPAIR_PAYORDER("REPAIR_PAYORDER", "补单"),
  RESET_PWD("RESET_PWD", "重设密码"),
  MODIFY_CUS_INFO("MODIFY_CUS_INFO", "修改会员邮箱电话"),
  ADD_ANNOUNCEMENT("ADD_ANNOUNCEMENT", "添加公告"), 
  DEL_ANNOUNCEMENT("DEL_ANNOUNCEMENT", "删除公告"),
  SYN_BET_RECORDS("SYN_BET_RECORDS", "同步投注记录"),
  ENABLE("ENABLE", "启用或禁用会员"),
  SETLEVEL("SETLEVEL", "设定会员等级"),
  CHANGE_CREDIT_MANUAL("CHANGE_CREDIT_MANUAL", "手工增减额度"),
  CREATE_SUB_OP("CREATE_SUB_OP", "创建下级管理员"),
  MODIFY_OWN_PWD("MODIFY_OWN_PWD", "修改管理员密码");
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

