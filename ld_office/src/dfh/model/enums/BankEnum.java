package dfh.model.enums;

public enum BankEnum
{
	INDUSTRIAL_AND_COMMERCIAL_BANK_OF_CHINA("ICBC", "工商银行"),
	CHINA_CMB_BANK("CMB", "招商银行"),
  CHINA_MERCHANTS_BANK("CB", "商业银行"),
  AGRICULTURAL_BANK_OF_CHINA("ABC", "农业银行"),
  CHINA_CONSTRUCTION_BANK("CCB", "建设银行"),
  BANK_OF_COMMUNICATIONS("BCM", "交通银行"),
  MINSHENG_BANK("CMBC", "民生银行"),
  EVERBRIGHT_BANK("CEB", "光大银行"), 
  INDUSTRIAL_BANK("CIB", "兴业银行"), 
  SHANGHAI_PUDONG_DEVELOPMENT_BANK("SPDB", "上海浦东银行"),
  GUANDONG_DEVELOPMENT_BANK("GDB", "广东发展银行"), 
  SHENZHEN_DEVELOPMENT_BANK("SDB", "深圳发展银行"),
  BANK_OF_CHINA("BOC", "中国银行"), 
  CHINA_CITIC_BANK("CNCB", "中信银行"),
	  CHINA_ZGYZ_BANK("PSBC", "邮政银行");

  private String issuingBank;
  private String issuingBankCode;
  

	private BankEnum(String issuingBankCode,String issuingBank) {
		this.issuingBankCode = issuingBankCode;
		this.issuingBank = issuingBank;
	}

  public String getIssuingBank()
  {
    return this.issuingBank;
  }

  public String getIssuingBankCode() {
    return this.issuingBankCode;
  }
  
	
	public static BankEnum getBank(String code){
		BankEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			BankEnum type = p[i];
			if (type.getIssuingBank().equals(code))
				return type;
		}
		return null;
	}
  
  
}
	