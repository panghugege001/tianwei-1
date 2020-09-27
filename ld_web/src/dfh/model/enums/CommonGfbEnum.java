package dfh.model.enums;

public enum CommonGfbEnum {
	CMGFB1("gfb1", "国付宝1", "0000010784", "sduifyho87sdkfh2", "0000000002000004007", "http://pay.fumeihedu.cn/paybank.asp", 62, 0.994);
	
	public static Boolean existKey(String code) {
		CommonGfbEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonGfbEnum type = p[i];
			if (type.getCode().equals(code))
				return true;
		}
		return false;
	}
	
	public static Boolean existText(String text) {
		CommonGfbEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonGfbEnum type = p[i];
			if (type.getText().equals(text))
				return true;
		}
		return false;
	}
	
	public static CommonGfbEnum getCommonGfb(String code){
		CommonGfbEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonGfbEnum type = p[i];
			if (type.getCode().equals(code))
				return type;
		}
		return null;
	}
	
	public static CommonGfbEnum getCommonGfbByMerchantCode(String merchantCode) {
		CommonGfbEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonGfbEnum type = p[i];
			if (type.getMerchantCode().equals(merchantCode))
				return type;
		}
		return null;
	}
	
	public static CommonGfbEnum getCommonGfbByText(String text) {
		CommonGfbEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonGfbEnum type = p[i];
			if (type.getText().equals(text))
				return type;
		}
		return null;
	}

	private String code;

	private String text;
	
	private String merchantCode;
	
	private String merchantKey;
	
	private String virCardNoIn;
	
	private String url;
	
	private Integer bankinfoType;
	
	private Double bankinfoRate;

	private CommonGfbEnum(String code, String text, String merchantCode, String merchantKey, String virCardNoIn, String url, Integer bankinfoType, Double bankinfoRate) {
		this.code = code;
		this.text = text;
		this.merchantCode = merchantCode;
		this.merchantKey = merchantKey;
		this.virCardNoIn = virCardNoIn;
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
	
	public String getVirCardNoIn() {
		return virCardNoIn;
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
