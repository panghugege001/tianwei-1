package app.model.vo;

import java.util.Date;

public class AppCustomVersionVO  {
	private Date modifyTime;  
	private String versionCode; 
	private String agentCode; 
	private String plat;  
	private boolean isForceUpgrade;  
	private String packageUrl;  
	private String qrCodeUrl;  
	private String title;  
	private String upgradeLog;
	private String agentAccount;
	private String product;
	
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getPlat() {
		return plat;
	}
	public void setPlat(String plat) {
		this.plat = plat;
	}
	public boolean getIsForceUpgrade() {
		return isForceUpgrade;
	}
	public void setIsForceUpgrade(boolean isForceUpgrade) {
		this.isForceUpgrade = isForceUpgrade;
	}
	public String getPackageUrl() {
		return packageUrl;
	}
	public void setPackageUrl(String packageUrl) {
		this.packageUrl = packageUrl;
	}
	public String getQrCodeUrl() {
		return qrCodeUrl;
	}
	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUpgradeLog() {
		return upgradeLog;
	}
	public void setUpgradeLog(String upgradeLog) {
		this.upgradeLog = upgradeLog;
	}
	public String getAgentAccount() {
		return agentAccount;
	}
	public void setAgentAccount(String agentAccount) {
		this.agentAccount = agentAccount;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}  

	
}