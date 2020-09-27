package dfh.mgs.vo.freegame;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dfh.mgs.vo.OrionResponse;

@XmlRootElement(name = "GetFreegamesResponse")
public class GetFreegamesRoot extends OrionResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Offers offers = new Offers();

	@XmlElement(name = "GetFreegamesResult")
	public Offers getOffers() {
		return offers;
	}

	public void setOffers(Offers offers) {
		this.offers = offers;
	}
	
}
