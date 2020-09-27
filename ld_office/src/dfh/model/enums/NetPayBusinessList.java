package dfh.model.enums;

public enum NetPayBusinessList {

	/*YEEPAYA("ips", "环讯"),*/
	ZHIFU("zf", "智付"),
	ZHIFU1("zf1", "智付1"),
	ZHIFU2("zf2", "智付2"),
	ZHIFU3("zf3", "智付3"),
	CMZHIFU1("cmzf1", "通用智付1"),
	CMZHIFU2("cmzf2", "通用智付2"),
	
	DIANKA("dk", "点卡"),
	DIANKA1("dk1", "点卡1"),
	DIANKA2("dk2", "点卡2"),
	DIANKA3("dk3", "点卡3"),
	DIANKA10("zfdk", "智付点卡(2000295555)"),
	DIANKA11("zfdk1", "智付点卡1(2000295566)"),
	//DIANKA12("zfdk2", "智付点卡2(2000295588)"),
	
	HUICHAO("hc", "汇潮"),
	HUICHAOYMD("hcymd", "汇潮网银"),
	BIFUBAO("bfb", "币付宝"),
	CMGFB1("gfb1", "国付宝1"),
	LFWXZF("lfwx","乐富微信"),
	XBWXZF("xbwx","新贝微信"),
	KDZF("kdzfb","口袋支付宝"),
	KDWXZF("kdwxzf","口袋微信支付"),
	KDWXZF2("kdwxzf2","口袋微信支付2"),   
	KDWXZF3("kdwxzf3","口袋微信支付3"),
	HAIER("haier","海尔支付"),
	HHWXBZF("hhbwxzf","汇付宝微信"),
	JUBZFB("jubzfb","聚宝支付宝"),
	XLBZF("xlb","迅联宝"),
	XLBWY("xlbwy","迅联宝网银"),
	XLBZFB("xlbzfb","迅联宝支付宝"),
	YFZFB("yfzfb","优付支付宝"),
	YFWX("yfwx","优付微信"),
	WECHAT("wechat","微信验证"), 
	WEIXINPAY("weixinpay","微信支付直连"),
	XBZFB("xbzfb","新贝支付宝"),
	YBZFB("ybzfb","银宝支付宝"),
	QWZFB("qwzfb","千网支付宝"),
	KDZFB2("kdzfb2","口袋支付宝2"),
	QWWX("qwwx","千网微信"),
	HF("hf", "汇付"),
	HF1("汇付1", "汇付1"),
	HF2("汇付2", "汇付2"),
	HF3("汇付3", "汇付3"),
	HF4("汇付4", "汇付4"),
	HF5("汇付5", "汇付5"),
	HF6("汇付6", "汇付6"),
	HF7("汇付7", "汇付7"),
	ZHIFUWX("zfwx", "智付微信"),
	ZHIFUWX1("zfwx1", "智付微信1"),
	ZHIFUBAO("zfb", "支付宝");
	

	private String code;
	private String text;

	private NetPayBusinessList(String code, String text) {
		this.code = code;
		this.text = text;
	}

	public String getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}
	
	public static String getText(String code) {
	    NetPayBusinessList[] p = values();
	    for (int i = 0; i < p.length; ++i) {
	      NetPayBusinessList type = p[i];
	      if (type.getCode().equals(code))
	        return type.getText();
	    }
	    return null;
	  }

}
