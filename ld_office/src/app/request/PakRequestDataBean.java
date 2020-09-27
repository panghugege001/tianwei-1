package app.request;

/**
 * 
 * @author stan
 *
 */
public class PakRequestDataBean extends BaseDataBean {
	private int id;
	private String agentCode;
	private String versionCode;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAgentCode() {
		return agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

}
