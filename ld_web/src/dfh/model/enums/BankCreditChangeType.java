package dfh.model.enums;

/**
 * 额度变化类型
 * 
 *
 */
public enum BankCreditChangeType
{
	  NETPAY("NETPAY", "在线支付"),
	  REPAIR_PAYORDER("REPAIR_PAYORDER", "在线支付补单"),
	  CASHIN("CASHIN", "存款"), 
	  CASHOUT("CASHOUT", "客户提款"),
	  SHIWU("SHIWU", "事务提款"),
	  INTRANFER("INTRANFER", "银行内部转账"),
	  CHANGE_BANKMANUAL("CHANGE_BANKMANUAL", "手工增减银行额度");
//	  ADD_FOR_TEST("ADD_FOR_TEST", "试玩账户添加额度"),
//	  COMMISSION("COMMISSION","合作伙伴佣金"),
//	  CHANGE_MATCH("CHANGE_MATCH","参加擂台赛添加初始额度"),

		public static String getText(String code) {
		    BankCreditChangeType[] p = values();
		    for (int i = 0; i < p.length; ++i) {
		      BankCreditChangeType type = p[i];
		      if (type.getCode().equals(code))
		        return type.getText();
		    }
		    return null;
		  }
		private String code;
	
		private String text;
	
	  private BankCreditChangeType(String code, String text) {
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
