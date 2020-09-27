package dfh.mgs.vo;

import javax.xml.bind.annotation.XmlElement;

public class CompleteGameResponse  implements java.io.Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long rowId;
    private Integer serverId;
    private Boolean success;

    public CompleteGameResponse() {
    }

    public CompleteGameResponse(Long rowId, Integer serverId, Boolean success) {
           this.rowId = rowId;
           this.serverId = serverId;
           this.success = success;
    }

    @XmlElement(name="RowId", namespace=DataStructuresNS.VanguardAdminNS)
	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	@XmlElement(name="ServerId", namespace=DataStructuresNS.VanguardAdminNS)
	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	@XmlElement(name="Success", namespace=DataStructuresNS.VanguardAdminNS)
	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
    
}