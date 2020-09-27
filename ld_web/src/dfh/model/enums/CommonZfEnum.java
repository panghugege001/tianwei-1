package dfh.model.enums;

public enum CommonZfEnum {
	CMZF1("cmzf1", "通用智付1", "2000295699", "", "http://www.anyuanxinkeji.com", 411, 0.994),
	CMZF2("cmzf2", "通用智付2", "2000295640", "", "http://www.zhkangtsmy.com", 412, 0.994),
	CMZFWX("zfwx", "智付微信", "2000295666",  "", "", 413, 0.992), 
	CMZFWX1("zfwx1", "智付微信1", "2000299861",  "skJsaolsy8qj8sQlzinl_", "http://pay.fumeihedu.cn", 414, 0.992);
	
	public static Boolean existKey(String code) {
		CommonZfEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonZfEnum type = p[i];
			if (type.getCode().equals(code))
				return true;
		}
		return false;
	}
	
	public static Boolean existText(String text) {
		CommonZfEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonZfEnum type = p[i];
			if (type.getText().equals(text))
				return true;
		}
		return false;
	}
	
	public static CommonZfEnum getCommonZf(String code){
		CommonZfEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonZfEnum type = p[i];
			if (type.getCode().equals(code))
				return type;
		}
		return null;
	}
	
	public static CommonZfEnum getCommonZfByMerchantCode(String merchantCode) {
		CommonZfEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonZfEnum type = p[i];
			if (type.getMerchantCode().equals(merchantCode))
				return type;
		}
		return null;
	}
	
	public static CommonZfEnum getCommonZfByText(String text) {
		CommonZfEnum[] p = values();
		for (int i = 0; i < p.length; i++) {
			CommonZfEnum type = p[i];
			if (type.getText().equals(text))
				return type;
		}
		return null;
	}

	private String code;

	private String text;
	
	private String merchantCode;
	
	private String merchantKey;
	
	private String url;
	
	private Integer bankinfoType;
	
	private Double bankinfoRate;

	private CommonZfEnum(String code, String text, String merchantCode, String merchantKey, String url, Integer bankinfoType, Double bankinfoRate) {
		this.code = code;
		this.text = text;
		this.merchantCode = merchantCode;
		this.merchantKey = merchantKey;
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
