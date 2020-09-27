package dfh.mgs.vo;

public class FlushPendingCashinRequest implements java.io.Serializable {
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String loginName;
    private Integer serverId;
    private Integer transactionNumber;

    public FlushPendingCashinRequest() {
    }

    public FlushPendingCashinRequest(String loginName, Integer serverId, Integer transactionNumber) {
           this.loginName = loginName;
           this.serverId = serverId;
           this.transactionNumber = transactionNumber;
    }

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public Integer getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(Integer transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
    
}