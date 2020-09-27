package com.nnti.pay.model.enums;

/**
 * 提案处理过程标志
 * @author 
 *
 */
public enum TaskFlagType
{
  SUBMITED(0, "待处理"),
  FINISHED(1, "已完成"),
  CANCLED(-1, "已取消");

	public static String getText(Integer code) {
	    TaskFlagType[] p = values();
	    for (int i = 0; i < p.length; ++i) {
	      TaskFlagType type = p[i];
	      if (type.getCode().intValue() == code.intValue())
	        return type.getText();
	    }
	    return null;
	  }
	private Integer code;
	
	private String text;


  private TaskFlagType(Integer code, String text) {
	this.code = code;
	this.text = text;
}

  public Integer getCode()
  {
    return this.code;
  }

  public String getText() {
    return this.text;
  }
}

