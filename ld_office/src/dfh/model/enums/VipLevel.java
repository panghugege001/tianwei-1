package dfh.model.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 会员类型(等级)
 * 
 * @author
 *
 */
public enum VipLevel {
	/*NEWMEMBER(0, "新会员"), 
	COMMON(1, "忠实vip"), 
	XINGJI(2, "星级vip"), 
	HUANGJIN(3, "黄金vip"), 
	BAIJIN(4, "白金vip"), 
	ZUANSHI(5,"钻石vip"), 
	ZHIZUN(6, "至尊vip");*/
	TIANBING(0, "天兵"), 
	TIANJIANG(1, "天将"), 
	TIANWANG(2, "天王"), 
	XINGJUN(3, "星君"), 
	ZHENJUN(4, "真君"), 
	XIANJUN(5,"仙君"), 
	DIJUN(6, "帝君"),
	TIANZUN(7, "天尊"),
	TIANDI(8, "天帝"),
	;

	public static String getText(Integer code) {
		VipLevel[] p = values();
		for (int i = 0; i < p.length; ++i) {
			VipLevel type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	public static String getTextStr(String vip) {
		if (StringUtils.isBlank(vip)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		VipLevel[] p = values();
		for (String s : vip.split(",")) {
			for (int i = 0; i < p.length; ++i) {
				VipLevel type = p[i];
				if (String.valueOf(type.getCode()).equals(s)) {
					sb.append(type.getText()).append(",");
					break;
				}
			}
		}
		String str = sb.toString().substring(0, sb.toString().length() - 1);
		return str;
	}

	private Integer code;

	private String text;

	private VipLevel(Integer code, String text) {
		this.code = code;
		this.text = text;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}

	public static VipLevel getLevel(int level) {
		return VipLevel.values()[level];
	}
}
