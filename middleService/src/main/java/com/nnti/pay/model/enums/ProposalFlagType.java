package com.nnti.pay.model.enums;

/**
 * 提案状态
 * 
 *
 */
public enum ProposalFlagType
{
  SUBMITED(0, "待审核"), 
  AUDITED(1, "已审核"), 
  EXCUTED(2, "已执行"),
  CANCLED(-1, "已取消");

	public static String getText(Integer code) {
	    ProposalFlagType[] p = values();
	    for (int i = 0; i < p.length; ++i) {
	      ProposalFlagType type = p[i];
	      if (type.getCode().intValue() == code.intValue())
	        return type.getText();
	    }
	    return null;
	  }
	private Integer code;

	private String text;

  private ProposalFlagType(Integer code, String text) {
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
	
