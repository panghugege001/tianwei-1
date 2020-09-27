package dfh.model.enums;

public enum PtCouponUseType {
	
	UR0(0, "未使用"), UR1(1, "已使用");
	
	public static String getText(Integer code) {
		PtCouponUseType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			PtCouponUseType type = p[i];
			if (type.getCode().intValue() == code.intValue())
				return type.getText();
		}
		return null;
	}
	private Integer code;

	private String text;

	private PtCouponUseType(Integer code, String text) {
		this.code = code;
		this.text = text;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}

}
