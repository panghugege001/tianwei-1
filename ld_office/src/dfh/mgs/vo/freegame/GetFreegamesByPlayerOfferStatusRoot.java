package dfh.mgs.vo.freegame;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dfh.mgs.vo.OrionResponse;

@XmlRootElement(name = "GetFreegamesByPlayerOfferStatusResponse")
public class GetFreegamesByPlayerOfferStatusRoot extends OrionResponse{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Offers offers;

	@XmlElement(name = "GetFreegamesByPlayerOfferStatusResult")
	public Offers getOffers() {
		return offers;
	}

	public void setOffers(Offers offers) {
		this.offers = offers;
	}
	
}
