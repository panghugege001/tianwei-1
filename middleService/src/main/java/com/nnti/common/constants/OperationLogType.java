package com.nnti.common.constants;

public enum OperationLogType
{
  LOGIN("LOGIN", "登录"),
  AUDIT("AUDIT", "审核"),
  EXCUTE("EXCUTE", "执行"), 
  ADDUSER("ADDUSER","添加用户"),
  ADDAGENT("ADDAGENT","增加代理"),
  RESET_PWD("RESET_PWD", "重设密码"),
  MODIFY_CUS_INFO("MODIFY_CUS_INFO", "修改会员邮箱电话"),
  SYN_BET_RECORDS("SYN_BET_RECORDS", "同步投注记录"),
  ENABLE("ENABLE", "启用或禁用会员");

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

