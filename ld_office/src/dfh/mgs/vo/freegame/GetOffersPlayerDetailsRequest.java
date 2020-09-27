package dfh.mgs.vo.freegame;

public class GetOffersPlayerDetailsRequest implements java.io.Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer offerId;
    private Integer pageNumber;
    private Integer pageSize;
    private Integer serverId;

    public GetOffersPlayerDetailsRequest() {
    }

    public GetOffersPlayerDetailsRequest(Integer offerId, Integer pageNumber, Integer pageSize, Integer serverId) {
           this.offerId = offerId;
           this.pageNumber = pageNumber;
           this.pageSize = pageSize;
           this.serverId = serverId;
    }

	public Integer getOfferId() {
		return offerId;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
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