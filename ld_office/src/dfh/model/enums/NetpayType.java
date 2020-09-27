package dfh.model.enums;

public enum NetpayType
{
  FREE_CUSTOMER("FREE_CUSTOMER", "试玩会员"),
  REAL_CUSTOMER("MONEY_CUSTOMER", "真钱会员"),
  AGENT("AGENT", "代理"), 
  PARTNER("PARTNER", "合作伙伴");

	private String code;
	private String text;

	private NetpayType(String code, String text) {
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
