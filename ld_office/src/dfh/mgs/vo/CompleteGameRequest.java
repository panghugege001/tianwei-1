package dfh.mgs.vo;

public class CompleteGameRequest implements java.io.Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer rowId;
    private Long rowIdLong;
    private Integer serverId;

    public CompleteGameRequest() {
    }

    public CompleteGameRequest(Integer rowId, Long rowIdLong, Integer serverId) {
           this.rowId = rowId;
           this.rowIdLong = rowIdLong;
           this.serverId = serverId;
    }

	public java.lang.Integer getRowId() {
		return rowId;
	}

	public void setRowId(java.lang.Integer rowId) {
		this.rowId = rowId;
	}

	public java.lang.Long getRowIdLong() {
		return rowIdLong;
	}

	public void setRowIdLong(java.lang.Long rowIdLong) {
		this.rowIdLong = rowIdLong;
	}

	public java.lang.Integer getServerId() {
		return serverId;
	}

	public void setServerId(java.lang.Integer serverId) {
		this.serverId = serverId;
	}
    
}