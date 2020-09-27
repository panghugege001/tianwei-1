package app.enums;

public enum CacheEnums {
	
	CACHEENUMS_IPLIMIT01("LD_IP_ALLOW", "IP通过"),
	CACHEENUMS_IPLIMIT02("LD_IP_DENY", "IP拒绝"),
	CACHEENUMS_IPLIMIT03("LD_AREA_DENY", "地区拒绝"),
	CACHEENUMS_TOPIC("LD_APP_TOPIC", "APP首页专题"),
	CACHEENUMS_WIN("LD_APP_WIN", "APP首页最新中奖"),
	CACHEENUMS_HOT_PREFERENTIAL("LD_APP_HOT_PREFERENTIAL", "APP首页热门优惠"),
	CACHEENUMS_LD_STYLE("LD_STYLE", "天威风采"),
	CACHEENUMS_APPPAK("LD_APP_PAK", "APP定制打包信息");
	
	private CacheEnums(String code, String text) {
	
		this.code = code;
		this.text = text;
	}
	
	private String code;
	private String text;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}

