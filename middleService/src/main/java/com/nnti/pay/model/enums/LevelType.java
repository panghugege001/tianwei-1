package com.nnti.pay.model.enums;

/**
 * 提案任务执行程度
 * 
 *
 */
public enum LevelType
{
  AUDIT("AUDIT", "审核"), 
  EXCUTE("EXCUTE", "执行");

	public static String getText(String code) {
	    LevelType[] p = values();
	    for (int i = 0; i < p.length; ++i) {
	      LevelType type = p[i];
	      if (type.getCode().equals(code))
	        return type.getText();
	    }
	    return null;
	  }
	private String code;

	private String text;


  private LevelType(String code, String text) {
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
