package dfh.mgs.vo.freegame;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import dfh.mgs.vo.DataStructuresNS;
import dfh.mgs.vo.OrionResponse;

public class GetFreegamesPlayerDetailsResult extends OrionResponse {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer offerId;
    private List<OffersPlayerDetail> offersPlayerDetails;
    private Integer serverId;
    private Integer totalPages;

    public GetFreegamesPlayerDetailsResult() {
    }

    public GetFreegamesPlayerDetailsResult(Integer offerId, List<OffersPlayerDetail> offersPlayerDetails, Integer serverId, Integer totalPages) {
           this.offerId = offerId;
           this.offersPlayerDetails = offersPlayerDetails;
           this.serverId = serverId;
           this.totalPages = totalPages;
    }

    @XmlElement(name = "OfferId", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getOfferId() {
		return offerId;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	@XmlElementWrapper(name = "OffersPlayerDetails", namespace = DataStructuresNS.FreegameAdminNS)
	@XmlElement(name = "OffersPlayerDetail", namespace = DataStructuresNS.FreegameAdminNS)
	public List<OffersPlayerDetail> getOffersPlayerDetails() {
		return offersPlayerDetails;
	}

	public void setOffersPlayerDetails(List<OffersPlayerDetail> offersPlayerDetails) {
		this.offersPlayerDetails = offersPlayerDetails;
	}

	@XmlElement(name = "ServerId", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	@XmlElement(name = "TotalPages", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

}