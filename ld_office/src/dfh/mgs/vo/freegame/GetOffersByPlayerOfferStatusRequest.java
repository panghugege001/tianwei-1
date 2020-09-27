package dfh.mgs.vo.freegame;

public class GetOffersByPlayerOfferStatusRequest implements java.io.Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer offerStatusId;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer serverId;

    public GetOffersByPlayerOfferStatusRequest() {
    }

    public GetOffersByPlayerOfferStatusRequest(Integer offerStatusId, Integer pageNumber, Integer pageSize, Integer serverId) {
           this.offerStatusId = offerStatusId;
           this.pageNumber = pageNumber;
           this.pageSize = pageSize;
           this.serverId = serverId;
    }

	public Integer getOfferStatusId() {
		return offerStatusId;
	}

	public void setOfferStatusId(Integer offerStatusId) {
		this.offerStatusId = offerStatusId;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
    
}