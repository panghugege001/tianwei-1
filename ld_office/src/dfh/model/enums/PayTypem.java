package dfh.model.enums;


/**
 * 银行名称
 */
public enum PayTypem
{
	ZFB("支付宝","支付宝"),
	Wy("网银","网银");
	

  private String Bank;
  private String Code;
  

	private PayTypem(String issuingBankCode,String issuingBank) {
		this.Bank = issuingBankCode;
		this.Code = issuingBank;
	}

  public String getIssuingBank()
  {
    return this.Bank;
  }

  public String getIssuingBankCode() {
    return this.Code;
  }
}
	