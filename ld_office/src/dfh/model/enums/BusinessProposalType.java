package dfh.model.enums;

/**
 * 提案类型
 * 
 *
 */
public enum BusinessProposalType
{
	 SERVERFEE(601, "公司运维开销"),
	  SEOFEE(602, "市场推广费用"),
	  GOVFEE(603, "政府开销"),
	  SALARYFEE(604, "工资开销"), 
	  NORMALFEE(605, "日常开销"),
	  HARDWAREFEE(606, "硬件设备开销"),
	  PUBLICISTFEE (607, "公关开支"),
	  LOANFEE(608, "借款"),
	  BROKENBILL(610,"坏账"),
	  OTHER(609, "其他开销");
	  

	public static String getText(Integer code) {
	    BusinessProposalType[] p = values();
	    for (int i = 0; i < p.length; ++i) {
	      BusinessProposalType type = p[i];
	      if (type.getCode().intValue() == code.intValue())
	        return type.getText();
	    }
	    return null;
	  }

	private Integer code;


  private String text;

  private BusinessProposalType(Integer code, String text) {
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

