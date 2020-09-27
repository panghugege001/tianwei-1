package dfh.model.enums;


/**
 * 会员类型 
 * 
 *
 */
public enum Banktype {
//	FREE_CUSTOMER("FREE_CUSTOMER", "试玩会员"),
	CASHIN("1", "存款账户"),
	CASHOUT("2", "支付账户"),
	CASHSAVE("3", "存储账户"),
	CASHRESERVE("10", "付款储备"),
	ISSUEDRESERVE("111", "下发储备"),
	CASHOnline("4", "在线账户[IPS]"), 
	CASHOnline40("40", "在线账户[智付(2030028802)]"),
	CASHOnline41("41", "在线账户[智付1(2030000006)]"),
	CASHOnline42("42", "在线账户[智付2(2030020118)]"),
	CASHOnline43("43", "在线账户[智付3(2030020119)]"),
	CASHOnline411("411", "在线账户[通用智付1(2000295699)]"),
	CASHOnline412("412", "在线账户[通用智付2]"),
	CASHOnline413("413", "在线账户[智付微信]"),
	CASHOnline414("414", "在线账户[智付微信1(2000299861)]"),
	
	DIANKA4000("4000", "在线账户[智付点卡]"),
	DIANKA4001("4001", "在线账户[智付点卡1]"),
	DIANKA4002("4002", "在线账户[智付点卡2]"),
	DIANKA4003("4003", "在线账户[智付点卡3]"),
	DIANKA4010("4010", "在线账户[智付点卡(2000295555)]"),
	DIANKA4011("4011", "在线账户[智付点卡1(2000295566)]"),
	//DIANKA4012("4012", "在线账户[智付点卡(2000295588)]"),
	
	CASHOnline44("44", "在线账户[汇付]"),
	CASHOnline441("441", "在线账户[汇付1(873098)]"),
	CASHOnline442("442", "在线账户[汇付2(873096)]"),
	CASHOnline443("443", "在线账户[汇付3(873102)]"),
	CASHOnline444("444", "在线账户[汇付4(873103)]"),
	CASHOnline445("445", "在线账户[汇付5(873100)]"),
	CASHOnline446("446", "在线账户[汇付6(873095)]"),
	CASHOnline447("447", "在线账户[汇付7(873099)]"),
	CASHOnline50("50", "在线账户[汇潮]"),
	CASHOnline60("60", "在线账户[支付宝]"),
	LFWXZF450("450", "在线账户[乐富微信]"),
	XBWXZF460("460", "在线账户[新贝微信]"),
	KDZF470("470", "口袋支付宝"),
	KDWXZF471("471", "口袋微信支付"),
	KDWXZF474("474", "口袋微信支付2"),
	KDWXZF478("478", "口袋微信支付3"),
	HAIER472("472", "海尔支付"),
	HHBZF481("481", "汇付宝微信"),
	JUBZFB473("473", "聚宝支付宝"),
	XLBWX485("485", "迅联宝"),
	XLBWY486("486", "迅联宝网银"),
	YFZFB488("488", "优付支付宝"),
	XBZFB489("489", "新贝支付宝"),
	YBZFB491("491", "银宝支付宝"),
	YFWX492("492", "优付微信"),
	QWZFB493("493", "千网支付宝"),
	KDZFB494("494", "口袋支付宝2"),
	QWWX495("495", "千网微信"),
	XLBZFB497("497", "迅联宝支付宝"),
	HCWY51("51", "汇潮网银"), 
	CASHOnline61("61", "在线账户[币付宝]"),
	CASHOnline62("62", "在线账户[国付宝1]"),
	CASHOARMB("5", "事务账户(人民币)"),
	CASHOABISUO("6", "事务账户(比索)"),
	CASHOABISUOTwo("7", "VIP存款账户"),
	CASHTRANSFER("8", "中转账户"),
	CASHINVALIDATE("9", "额度验证存款账号");
	
//	PARTNER("PARTNER", "合作伙伴");

	public static String getText(String code) {
		Banktype[] p = values();
		for (int i = 0; i < p.length; ++i) {
			Banktype type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}
	
	private String code;

	private String text;

	private Banktype(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}
}