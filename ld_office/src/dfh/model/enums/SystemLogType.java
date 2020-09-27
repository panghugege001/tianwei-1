package dfh.model.enums;
  
/**
 * 会员登陆记录、登陆后操作记录标志
 * @author 
 *
 */
public enum SystemLogType {

	XIMAMG("XIMAMG", "MG系统洗码","MG"),
	XIMAPNG("XIMAPNG", "PNG系统洗码","PNG"),
	XIMATTG("XIMATTG", "TTG系统洗码","TTG"),
	XIMAAG("XIMAAG", "AGIN系统洗码","AGIN"),
	XIMAQT("XIMAQT", "QT系统洗码","QT"),
	XIMANT("XIMANT", "NT系统洗码","NT"),
	XIMAKYQP("XIMAKYQP", "开元棋牌系统洗码","KYQP"),
	XIMAVR("XIMAVR", "VR官方彩系统洗码","VR"),
	XIMAVRLIVE("XIMAVRLIVE", "VR彩系统洗码","VRLIVE"),
	XIMAPB("XIMAPB", "PB系统洗码","PB"),
	XIMABBINELE("XIMABBINELE", "BBIN电子系统洗码","BBINELE"),
	XIMABBINVID("XIMABBINVID", "BBIN真人系统洗码","BBINVID"),
	XIMADT("XIMADT", "DT系统洗码","DT"),
	XIMAHYG("XIMAHYG", "HYG系统洗码","HYG"),
	XIMAFANYA("XIMAFANYA", "FANYA系统洗码","FANYA"),
	XIMA761("XIMA761", "761系统洗码","761"),
	XIMAEBETAPP("XIMAEBETAPP", "EBETAPP系统洗码","EBETAPP"),
	XIMAAGFISH("XIMAAGFISH", "AG捕鱼系统洗码","AGINFISH"),
	XIMAEBET("XIMAEBET", "EBET系统洗码","EBET"),
	XIMAEA("XIMAEA", "EA系统洗码","EA"),
	XIMASB("XIMASB", "SB系统洗码","SB"),
	XIMASBA("XIMASBA", "SBA系统洗码","SBA"),
	XIMAMWG("XIMAMWG", "MWG系统洗码","MWG"),
	XIMAAGSLOT("XIMAAGSLOT", "AG老虎机系统洗码","AGINSLOT"),
	XIMAPT("XIMAPT", "PT系统洗码","PT"),
	XIMAPTSKY("XIMAPTSKY", "PTSKY系统洗码","PTSKY"),
	XIMASWFISH("XIMASWFISH", "SWFISH系统洗码","SWFISH"),
	XIMANN2LIVE("XIMANN2LIVE", "N2live系统洗码","N2LIVE"),
	XIMABIT("XIMABIT", "BIT系统洗码","BIT");


	public static String getText(String code) {
		SystemLogType p[] = values();
		for (int i = 0; i < p.length; i++) {
			SystemLogType type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	
	public static String getPlatform(String platform) {
		SystemLogType p[] = values();
		for (int i = 0; i < p.length; i++) {
			SystemLogType type = p[i];
			if (type.getPlatform().equals(platform))
				return type.getText();
		}
		return null;
	}
	private String code;

	private String text;
	
	private String platform;

	private SystemLogType(String code, String text,String platform) {
		this.code = code;
		this.text = text;
		this.platform = platform;
	}

	public String getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	
}
