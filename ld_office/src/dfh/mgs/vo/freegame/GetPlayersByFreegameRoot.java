package dfh.mgs.vo.freegame;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dfh.mgs.vo.OrionResponse;

@XmlRootElement(name = "GetPlayersByFreegameResponse")
public class GetPlayersByFreegameRoot extends OrionResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GetPlayersByOfferResponse playersOfFreegame;
	
	@XmlElement(name = "GetPlayersByFreegameResult")
	public GetPlayersByOfferResponse getPlayersOfFreegame() {
		return playersOfFreegame;
	}

	public void setPlayersOfFreegame(GetPlayersByOfferResponse playersOfFreegame) {
		this.playersOfFreegame = playersOfFreegame;
	}

}
