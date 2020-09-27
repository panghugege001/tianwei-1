package dfh.mgs.vo.freegame;

import javax.xml.bind.annotation.XmlElement;

import dfh.mgs.vo.DataStructuresNS;

public class OffersPlayerDetail implements java.io.Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer gamesAwarded;
    private Integer gamesLeft;
    private Integer instanceId;
    private String loginName;
    private String offerDescription;
    private Integer offerId;
    private Long totalWinnings;

    public OffersPlayerDetail() {
    }

    public OffersPlayerDetail(Integer gamesAwarded, Integer gamesLeft, Integer instanceId, String loginName, String offerDescription, Integer offerId, Long totalWinnings) {
           this.gamesAwarded = gamesAwarded;
           this.gamesLeft = gamesLeft;
           this.instanceId = instanceId;
           this.loginName = loginName;
           this.offerDescription = offerDescription;
           this.offerId = offerId;
           this.totalWinnings = totalWinnings;
    }

    @XmlElement(name = "GamesAwarded", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getGamesAwarded() {
		return gamesAwarded;
	}

	public void setGamesAwarded(Integer gamesAwarded) {
		this.gamesAwarded = gamesAwarded;
	}

	@XmlElement(name = "GamesLeft", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getGamesLeft() {
		return gamesLeft;
	}

	public void setGamesLeft(Integer gamesLeft) {
		this.gamesLeft = gamesLeft;
	}

	@XmlElement(name = "InstanceId", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	@XmlElement(name = "LoginName", namespace = DataStructuresNS.FreegameAdminNS)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@XmlElement(name = "OfferDescription", namespace = DataStructuresNS.FreegameAdminNS)
	public String getOfferDescription() {
		return offerDescription;
	}

	public void setOfferDescription(String offerDescription) {
		this.offerDescription = offerDescription;
	}

	@XmlElement(name = "OfferId", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getOfferId() {
		return offerId;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	@XmlElement(name = "TotalWinnings", namespace = DataStructuresNS.FreegameAdminNS)
	public Long getTotalWinnings() {
		return totalWinnings;
	}

	public void setTotalWinnings(Long totalWinnings) {
		this.totalWinnings = totalWinnings;
	}

}