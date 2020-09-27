package dfh.mgs.vo;

import javax.xml.bind.annotation.XmlElement;

public class FlushPendingCashinResponse implements java.io.Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loginName;

    private Integer serverId;

    private Boolean success;

    private Integer transactionNumber;

    public FlushPendingCashinResponse() {
    }

    public FlushPendingCashinResponse(String loginName, Integer serverId, Boolean success, Integer transactionNumber) {
           this.loginName = loginName;
           this.serverId = serverId;
           this.success = success;
           this.transactionNumber = transactionNumber;
    }

    @XmlElement(name = "LoginName", namespace = DataStructuresNS.VanguardAdminNS)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@XmlElement(name = "ServerId", namespace = DataStructuresNS.VanguardAdminNS)
	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	@XmlElement(name = "Success", namespace = DataStructuresNS.VanguardAdminNS)
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	@XmlElement(name = "TransactionNumber", namespace = DataStructuresNS.VanguardAdminNS)
	public Integer getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(Integer transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
    
}