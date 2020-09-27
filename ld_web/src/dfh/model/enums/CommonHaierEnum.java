package dfh.model.enums;

public enum CommonHaierEnum {
	HAIER("haier", "海尔支付", "200002058681", "0whWeWhpxGVIeA4msYnS2w2m6GpCAJqe ", "gpi001@sina.com","http://pay.mengruoling.cn/paybankHaier.asp", 472, 0.995);
	
	public static Boolean existKey(String code) {
		CommonHaierEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonHaierEnum type = p[i];
			if (type.getCode().equals(code))
				return true;
		}
		return false;
	}
	
	public static Boolean existText(String text) {
		CommonHaierEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonHaierEnum type = p[i];
			if (type.getText().equals(text))
				return true;
		}
		return false;
	}
	
	public static CommonHaierEnum getCommonHaier(String code){
		CommonHaierEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonHaierEnum type = p[i];
			if (type.getCode().equals(code))
				return type;
		}
		return null;
	}
	
	public static CommonHaierEnum getCommonHaierByMerchantCode(String merchantCode) {
		CommonHaierEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonHaierEnum type = p[i];
			if (type.getMerchantCode().equals(merchantCode))
				return type;
		}
		return null;
	}
	
	public static CommonHaierEnum getCommonHaierByText(String text) {
		CommonHaierEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonHaierEnum type = p[i];
			if (type.getText().equals(text))
				return type;
		}
		return null;
	}

	private String code;

	private String text;
	
	private String merchantCode;
	
	private String merchantKey;
	
    private String merchanName;
	
	private String url;
	
	private Integer bankinfoType;
	
	private Double bankinfoRate;

	private CommonHaierEnum(String code, String text, String merchantCode, String merchantKey, String merchanName, String url,Integer bankinfoType, Double bankinfoRate) {
		this.code = code;
		this.text = text;
		this.merchantCode = merchantCode;
		this.merchantKey = merchantKey;
		this.merchanName =merchanName; 
		this.url = url;
		this.bankinfoType = bankinfoType;
		this.bankinfoRate = bankinfoRate;
	}

	public String getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public String getMerchantKey() {
		return merchantKey;
	}
	
	public String getmerchanName() {
		return merchanName;
	}
	

	public String getUrl() {
		return url;
	}

	public Integer getBankinfoType() {
		return bankinfoType;
	}

	public Double getBankinfoRate() {
		return bankinfoRate;
	}
}
