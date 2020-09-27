package com.nnti.common.constants;

public interface PlatformCode {

	public enum Transfer {

		SELF(Constant.SELF, "self"),
		PT(Constant.PT, "newpt"),
		MG(Constant.MG, "mg"),
		SW(Constant.SW, "sw"),
		DT(Constant.DT, "dt"),
		NT(Constant.NT, "nt"),
		QT(Constant.QT, "qt"),
		PNG(Constant.PNG, "png"),
		TTG(Constant.TTG, "ttg"),
		N2LIVE(Constant.N2LIVE, "n2live"),
		AGIN(Constant.AGIN, "agin"),
		SBA(Constant.SBA, "sba"),
		MWG(Constant.MWG, "mwg"),
		EA(Constant.EA, "ea"),
		OG(Constant.OG, "og"),
		EBETAPP(Constant.EBETAPP, "ebetapp"),
		SLOT(Constant.SLOT, "slot"),
		CHESS(Constant.CHESS, "chess"),
		BBIN(Constant.BBIN, "bbin");

		// 游戏平台
		private String platform;
		// 对应编码
		private String code;

		public static String getCode(String platform) {

			String value = "";

			Transfer[] t = values();

			for (int i = 0, len = t.length; i < len; i++) {

				Transfer temp = t[i];

				if (temp.getPlatform().equalsIgnoreCase(platform)) {

					value = temp.getCode();
					break;
				}
			}

			return value;
		}

		public static String getPlatform(String code) {

			String value = "";

			Transfer[] t = values();

			for (int i = 0, len = t.length; i < len; i++) {

				Transfer temp = t[i];

				if (temp.getCode().equalsIgnoreCase(code)) {

					value = temp.getPlatform();
					break;
				}
			}

			return value;
		}

		private Transfer(String platform, String code) {

			this.platform = platform;
			this.code = code;
		}

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}

	public enum Platform {

		PT("PT", "pttiger"),
		QT("QT", "qt"),
		NT("NT", "nt"),
		MG("MG", "mg"),
		SW("SW", "sw"),
		DT("DT", "dt"),
		PNG("PNG", "png"),
		TTG("TTG", "ttg"),
		AGIN("AGIN", "agin"),
		SLOT(Constant.SLOT, "slot");

		// 游戏平台
		private String platform;
		// 对应编码
		private String code;

		public static String getCode(String platform) {

			String value = "";

			Platform[] t = values();

			for (int i = 0, len = t.length; i < len; i++) {

				Platform temp = t[i];

				if (temp.getPlatform().equalsIgnoreCase(platform)) {

					value = temp.getCode();
					break;
				}
			}

			return value;
		}

		private Platform(String platform, String code) {

			this.platform = platform;
			this.code = code;
		}

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}

	public enum ProductSign {

		DY("dy", "dayun"),
		TW("tw", "tianwei"),
		YL("yl", "long8"),
		QY("qy", "qianyi"),
		LH("lh", "elufa"),
		UF("ufa", "youfa8"),
		DC("mzc", "dream"),
		QL("ql", "qle"),
		WS("ws", "ws"),
		LOH("loh", "longhu"),
		ZB("zb", "zunbao"),
		UL("ul", "ule");

		// 所属产品
		private String product;
		// 产品编码
		private String code;

		public static String getCode(String product) {

			String value = "";

			ProductSign[] t = values();

			for (int i = 0, len = t.length; i < len; i++) {

				ProductSign temp = t[i];

				if (temp.getProduct().equalsIgnoreCase(product)) {

					value = temp.getCode();
					break;
				}
			}

			return value;
		}

		private ProductSign(String product, String code) {

			this.product = product;
			this.code = code;
		}

		public String getProduct() {
			return product;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
	}
}