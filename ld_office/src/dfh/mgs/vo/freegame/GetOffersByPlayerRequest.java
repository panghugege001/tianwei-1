package dfh.mgs.vo.freegame;

public class GetOffersByPlayerRequest implements java.io.Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer offerStatusId;
    private Integer pageNumber;
    private Integer pageSize;
    private String playerName;
    private Integer serverId;

    public GetOffersByPlayerRequest() {
    }

    public GetOffersByPlayerRequest(Integer offerStatusId, Integer pageNumber, Integer pageSize, String playerName,  Integer serverId) {
           this.offerStatusId = offerStatusId;
           this.pageNumber = pageNumber;
           this.pageSize = pageSize;
           this.playerName = playerName;
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

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
    
}