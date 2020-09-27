package dfh.mgs.vo;

public class ValidteBetRequest implements java.io.Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private java.lang.String externalReference;

    private java.lang.Long rowId;

    private java.lang.Integer serverId;

    private java.lang.String unlockType;

    private java.lang.Integer userId;

    public ValidteBetRequest() {
    }

    public ValidteBetRequest(String externalReference, Long rowId, Integer serverId, String unlockType, Integer userId) {
           this.externalReference = externalReference;
           this.rowId = rowId;
           this.serverId = serverId;
           this.unlockType = unlockType;
           this.userId = userId;
    }

	public java.lang.String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(java.lang.String externalReference) {
		this.externalReference = externalReference;
	}

	public java.lang.Long getRowId() {
		return rowId;
	}

	public void setRowId(java.lang.Long rowId) {
		this.rowId = rowId;
	}

	public java.lang.Integer getServerId() {
		return serverId;
	}

	public void setServerId(java.lang.Integer serverId) {
		this.serverId = serverId;
	}

	public java.lang.String getUnlockType() {
		return unlockType;
	}

	public void setUnlockType(java.lang.String unlockType) {
		this.unlockType = unlockType;
	}

	public java.lang.Integer getUserId() {
		return userId;
	}

	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}
    
}