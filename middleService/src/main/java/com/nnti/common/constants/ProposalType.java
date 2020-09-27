package com.nnti.common.constants;

import java.util.ArrayList;
import java.util.List;

public enum ProposalType {
	
	SIGNDEPOSIT420(420 , "签到礼金"),
	SELF_503(503, "提款"),
	SELF_590(590, "自助PT首存优惠"),
	SELF_591(591, "自助PT次存优惠"),
	SELF_705(705, "自助PT限时优惠"),
	SELF_707(707, "自助NT首存优惠"),
	SELF_708(708, "自助NT次存优惠"),
	SELF_709(709, "自助NT限时优惠"),
	SELF_710(710, "自助QT首存优惠"),
	SELF_711(711, "自助QT次存优惠"),
	SELF_712(712, "自助QT限时优惠"),
	SELF_730(730, "自助MG首存优惠"),
	SELF_731(731, "自助MG次存优惠"),
	SELF_732(732, "自助MG限时优惠"),
	SELF_733(733, "自助DT首存优惠"),
	SELF_734(734, "自助DT次存优惠"),
	SELF_735(735, "自助DT限时优惠"),
	SELF_598(598, "自助TTG首存优惠"),
	SELF_599(599, "自助TTG次存优惠"),
	SELF_706(706, "自助TTG限时优惠"),
	SELF_740(740, "自助PNG首存优惠"),
	SELF_741(741, "自助PNG次存优惠"),
	SELF_742(742, "自助PNG限时优惠"),
	SELF_743(743, "自助AG真人首存优惠"),
	SELF_744(744, "自助AG真人次存优惠"),
	SELF_745(745, "自助AG真人限时优惠"),
	SELF_791(791, "自助老虎机首存优惠"),
	SELF_792(792, "自助老虎机次存优惠"),
	SELF_793(793, "自助老虎机限时优惠"),
	SELF_794(794, "自助SW首存优惠"),
	SELF_795(795, "自助SW次存优惠"),
	SELF_796(796, "自助SW限时优惠"),
	SELF_888(888, "抢话费"),
	SELF_701(701, "自助PT8元优惠"),
	SELF_512(512, "自助APP下载彩金"),
	SELF_771(771, "自助PT体验金"),
	SELF_772(772, "自助MG体验金"),
	SELF_773(773, "自助DT体验金"),
	SELF_774(774, "自助QT体验金"),
	SELF_775(775, "自助NT体验金"),
	SELF_776(776, "自助TTG体验金"),
	SELF_777(777, "自助PNG体验金"),
	SELF_778(778, "自助N2LIVE体验金"),
	SELF_779(779, "自助SLOT体验金"),
	SELF_780(780, "自助CQ9体验金"),
	SELF_419(419, "红包优惠券"),
	SELF_319(319, "存送优惠券"),
	SELF_101(101, "红包雨"),
	SELF_105(105, "存送三重奏活动"),
	PRIZE(506, "幸运抽奖"),
	VIPFREE(631,"vip每月免费筹码");
	// 优惠类型编码
	private Integer code;
	// 优惠类型名称
	private String text;

	private ProposalType(Integer code, String text) {

		this.code = code;
		this.text = text;
	}

	public static String getText(Integer code) {

		String value = "";

		ProposalType[] p = values();

		for (int i = 0, len = p.length; i < len; i++) {

			ProposalType type = p[i];

			if (type.getCode().intValue() == code.intValue()) {

				value = type.getText();
				break;
			}
		}

		return value;
	}

	public static List<String> getCodeList() {

		List<String> list = new ArrayList<String>();

		ProposalType[] p = values();

		for (int i = 0, len = p.length; i < len; i++) {

			list.add(p[i].getCode().toString());
		}

		return list;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}