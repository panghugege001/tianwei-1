package app.model.vo;

public class OnlinePayVO {
		private String onlinePayUrl;
		private String msg;
		private String domain;
		private String tempToken;
		public String getTempToken() {
			return tempToken;
		}

		public void setTempToken(String tempToken) {
			this.tempToken = tempToken;
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		public String getOnlinePayUrl() {
			return onlinePayUrl;
		}

		public void setOnlinePayUrl(String onlinePayUrl) {
			this.onlinePayUrl = onlinePayUrl;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

}