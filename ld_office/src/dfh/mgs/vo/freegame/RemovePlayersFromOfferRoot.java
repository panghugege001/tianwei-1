package dfh.mgs.vo.freegame;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dfh.mgs.vo.OrionResponse;
						
@XmlRootElement(name = "RemovePlayersFromFreegameResponse")
public class RemovePlayersFromOfferRoot extends OrionResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private RemovePlayersFromOfferResult removePlayersFromOffer;

	@XmlElement(name = "RemovePlayersFromFreegameResult")
	public RemovePlayersFromOfferResult getRemovePlayersFromOffer() {
		return removePlayersFromOffer;
	}

	public void setRemovePlayersFromOffer(RemovePlayersFromOfferResult removePlayersFromOffer) {
		this.removePlayersFromOffer = removePlayersFromOffer;
	}
	
}
