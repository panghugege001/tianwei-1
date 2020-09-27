package dfh.model.enums;


/**
 * 银行名称
 */
public enum IssuingBankEnum
{
	ZFB("支付宝","支付宝"),
	WECHAT("微信","微信"),
	INDUSTRIAL_AND_COMMERCIAL_BANK_OF_CHINA("工商银行", "工商银行"),
  CHINA_CMB_BANK("招商银行", "招商银行"),
  CITY_MERCHANTS_BANK("城市商业银行", "城市商业银行"),
  COUNTRY_MERCHANTS_BANK("农村商业银行", "农村商业银行"),
  SHANGHAI_MERCHANTS_BANK("上海农村商业银行", "上海农村商业银行"),
  AGRICULTURAL_BANK_OF_CHINA("农业银行", "农业银行"),
  CHINA_CONSTRUCTION_BANK("建设银行", "建设银行"),
  BANK_OF_COMMUNICATIONS("交通银行", "交通银行"),
  MINSHENG_BANK("民生银行", "民生银行"),
  EVERBRIGHT_BANK("光大银行", "光大银行"), 
  INDUSTRIAL_BANK("兴业银行", "兴业银行"), 
  
  SHANGHAI_PUDONG_DEVELOPMENT_BANK("上海浦东银行", "上海浦东银行"),
  GUANDONG_DEVELOPMENT_BANK("广东发展银行", "广东发展银行"), 
  SHENZHEN_DEVELOPMENT_BANK("深圳发展银行", "深圳发展银行"),
  BANK_OF_CHINA("中国银行", "中国银行"),
  CHINA_CITIC_BANK("中信银行", "中信银行"),
  HUAXING_BANK("华兴银行", "华兴银行"),
  
  PINGAN_BANK("平安银行", "平安银行"),
  GUANGFA_BANK("广发银行", "广发银行"),
  HUAXIA_BANK("华夏银行", "华夏银行"),
  QINGDAO_BANK("青岛银行", "青岛银行"),
  GUILING_BANK("桂林银行", "桂林银行"),
  YOUZHEN_BANK("邮政储蓄银行", "邮政储蓄银行"),
  TONGLIAN_PAY("通联转账", "通联转账"),
  YUNSHANFU_PAY("云闪付", "云闪付"),
  TLY_BANK_PAY("同略云银行", "同略云银行"),
  QRCODE_PAY("银行二维码", "银行二维码"),
  WECHAT_PAY("微信二维码收款", "微信二维码收款"),
  ZFB_PAY("支付宝二维码收款", "支付宝二维码收款"),
  BPI("BPI", "BPI"),
  MTC("MTC", "MTC"),
  CASH("CASH", "CASH"),
  UNINO_BANK("UNINO_BANK", "UNINO_BANK");

  private String issuingBank;
  private String issuingBankCode;
  

	private IssuingBankEnum(String issuingBankCode,String issuingBank) {
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
}
	