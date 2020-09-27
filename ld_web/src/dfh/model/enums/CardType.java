package dfh.model.enums;

/**
 * 点卡类型
 *
 * @author
 */
public enum CardType {
	YDSZX(0, "移动神州行"), 
	DXGK(1, "电信国卡"),
	QBCZK(2, "QQ币充值卡"),
	LTYKT(3, "联通一卡通"),
	JWYKT(4, "骏网一卡通"),
	SDYKT(5, "盛大一卡通"),
	WMYKT(6, "完美一卡通"),
	ZTYKT(7, "征途一卡通"),
	WYYKT(8, "网易一卡通"),
	SFTYKT(9, "盛付通一卡通"),
	SHYKT(10, "搜狐一卡通"),
	JYYKT(11, "九游一卡通"),
	THYKT(12, "天宏一卡通"),
	TXYKT(13, "天下一卡通"),
	ZYYKT(14, "纵游一卡通"),
	TXYKTZX(15, "天下一卡通专项");

	public static String getText(Integer code) {
		CardType[] p = values();
		for (int i = 0; i < p.length; ++i) {
			CardType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private CardType(Integer code, String text) {
		this.code = code;
		this.text = text;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}

	public static CardType getCardType(Integer code) {
		return CardType.values()[code];
	}
}
