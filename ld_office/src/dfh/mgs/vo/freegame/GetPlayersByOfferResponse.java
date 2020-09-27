package dfh.mgs.vo.freegame;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import dfh.mgs.vo.DataStructuresNS;

public class GetPlayersByOfferResponse implements java.io.Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer offerId;
    private List<PlayerOfferDetail> playersInOffer;
    private Integer serverId;
    private Integer totalPages;

    public GetPlayersByOfferResponse() {
    }

    public GetPlayersByOfferResponse(Integer offerId, List<PlayerOfferDetail> playersInOffer, Integer serverId, Integer totalPages) {
           this.offerId = offerId;
           this.playersInOffer = playersInOffer;
           this.serverId = serverId;
           this.totalPages = totalPages;
    }

    @XmlElement(name = "OfferId", namespace = DataStructuresNS.FreegameAdminNS)
	public java.lang.Integer getOfferId() {
		return offerId;
	}

	public void setOfferId(java.lang.Integer offerId) {
		this.offerId = offerId;
	}

	@XmlElementWrapper(name = "PlayersInOffer", namespace = DataStructuresNS.FreegameAdminNS)
	@XmlElement(name = "PlayerOfferDetail", namespace = DataStructuresNS.FreegameAdminNS)
	public List<PlayerOfferDetail> getPlayersInOffer() {
		return playersInOffer;
	}

	public void setPlayersInOffer(List<PlayerOfferDetail> playersInOffer) {
		this.playersInOffer = playersInOffer;
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