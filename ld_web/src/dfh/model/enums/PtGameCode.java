package dfh.model.enums;
public enum PtGameCode {

	RQ(1, "洛奇"),
	GTX2(2, "钢铁侠2"), 
	JGL(3, "金刚狼"), 
	GTX50X(4, "钢铁侠 50线"),
	GTX3(5, "钢铁侠3"),
	SE(6, "索尔"),
	FCZLM(7, "复仇者联盟"),
	GTX(9, "钢铁侠"),
	SQSX(10, "神奇四侠"),
	SQSX50X(11, "神奇四侠50线"),
	SQLJR(12, "神奇绿巨人"),
	SQLJR50X(13, "神奇绿巨人50线"),
	CDX(14, "超胆侠"),
	ALK(15, "艾丽卡"),
	ELQS(16, "恶灵骑士"),
	DFZS(17, "刀锋战士"),
	XZJ(18, "X战警"),
	JDS(19, "角斗士"),
	JG(20, "金刚"),
	HDTB(21, "海底探宝"),
	FHB(23, "粉红豹"),
	KXSGNC(24, "酷炫水果农场"),
	HTQS(27, "火腿骑士"),
	ZZX(28, "蜘蛛侠"),
	WSJCF(30, "万圣节财富"),
	XZJ50X(31, "X战警 50线"),
	CASHBACK(32, "Cashback先生"),
	SZXZYHTL(33, "三只小猪与大灰狼"),
	SDJLRJX(34, "圣诞节老人惊喜"),
	DTLCQ(35, "德托里传奇");
	
	public static String getText(Integer code) {
		PtGameCode[] p = values();
		for (int i = 0; i < p.length; ++i) {
			PtGameCode type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private PtGameCode(Integer code, String text) {
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
