package app.model.vo;


/**
 * 将请求的json转bean
 * @author lin
 *
 */
	public class WeedOptRequestDataVO{
		
		
		private String loginName;//账号
		private String platForm;//平台
		private String pno;//提案号
		private String flag;//操作类型
		private String ip;//ip
		
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getLoginName() {
			return loginName;
		}
		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}
		public String getPlatForm() {
			return platForm;
		}
		public void setPlatForm(String platForm) {
			this.platForm = platForm;
		}
		public String getPno() {
			return pno;
		}
		public void setPno(String pno) {
			this.pno = pno;
		}
		public String getFlag() {
			return flag;
		}
		public void setFlag(String flag) {
			this.flag = flag;
		}
		
}
