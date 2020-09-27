package dfh.model.enums;

public enum CreditChangeType
{
	  NETPAY("NETPAY", "在线支付"),
	  REPAIR_PAYORDER("REPAIR_PAYORDER", "支付补单"),
	  CASHIN("CASHIN", "存款"), 
	  CASHOUT("CASHOUT", "提款"),
	  CASHOUT_RETURN("CASHOUT_RETURN", "退还提款"),
	  REBANKINFO("REBANKINFO", "银改"),
	  PRIZE("PRIZE", "幸运抽奖"),
	  WEEKSENT("WEEKSENT", "周周回馈优惠"),
	  FIRST_DEPOSIT_CONS("FIRST_DEPOSIT_CONS", "首存优惠"), 
	  XIMA_CONS("XIMA_CONS", "洗码优惠"), 
	  BANK_TRANSFER_CONS("BANK_TRANSFER_CONS", "转账优惠"),
	  OFFER_CONS("OFFER_CONS", "再存优惠"),
	  TRANSFER_IN("TRANSFER_IN", "额度转入(LONGDU->EA)"),
	  TRANSFER_OUT("TRANSFER_OUT", "额度转出(EA->LONGDU)"), 
	  TRANSFER_DSPIN("TRANSFER_DSPIN", "额度转入(LONGDU->AG)"),
	  TRANSFER_DSPOUT("TRANSFER_DSPOUT", "额度转出(AG->LONGDU)"), 
	  TRANSFER_KENOOUT("TRANSFER_KENOOUT", "额度转出(KENO->LONGDU)"), 
	  TRANSFER_KENOIN("TRANSFER_KENOIN", "额度转入(LONGDU->KENO)"),
	  TRANSFER_BBINOUT("TRANSFER_BBINOUT", "额度转出(BBIN->LONGDU)"),
	  TRANSFER_BBININ("TRANSFER_BBININ", "额度转入(LONGDU->BBIN)"),
	  TRANSFER_BOKIN("TRANSFER_BOKIN", "额度转入(LONGDU->BOK)"),
	  TRANSFER_BOKOUT("TRANSFER_BOKOUT", "额度转出(BOK->LONGDU)"),
	  CHANGE_MANUAL("CHANGE_MANUAL", "手工增减额度"),
	  TRANSFER_FRIEND_COUPON("TRANSFER_FRIEND_COUPON", "(推荐好友奖金)额度转入(QIANYI->TTG/PT/NT)"),
	  CHANGE_QUOTAL("CHANGE_QUOTAL", "增减额度"),
	  CHANGE_QUOTALSLOT("CHANGE_QUOTALSLOT", "增减额度(代理老虎机佣金)"),
	  CHANGE_COMMISSIONSDAY("CHANGE_COMMISSIONSDAY", "日结佣金"),
//	  ADD_FOR_TEST("ADD_FOR_TEST", "试玩账户添加额度"),
//	  COMMISSION("COMMISSION","合作伙伴佣金"),
//	  CHANGE_MATCH("CHANGE_MATCH","参加擂台赛添加初始额度"),
	  ADD_OFFER("ADD_OFFER","再存优惠"),
	  COMMISSION("COMMISSION","代理佣金");

		public static String getText(String code) {
		    CreditChangeType[] p = values();
		    for (int i = 0; i < p.length; ++i) {
		      CreditChangeType type = p[i];
		      if (type.getCode().equals(code))
		        return type.getText();
		    }
		    return null;
		  }
		private String code;
	
		private String text;
	
	  private CreditChangeType(String code, String text) {
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
