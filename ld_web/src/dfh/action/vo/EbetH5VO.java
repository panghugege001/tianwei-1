package dfh.action.vo;

public class EbetH5VO {
	private String loginname;
	private String channelId;
	private String subChannelId;//备用
	private String accessToken;
	
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getSubChannelId() {
		return subChannelId;
	}
	public void setSubChannelId(String subChannelId) {
		this.subChannelId = subChannelId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
}
