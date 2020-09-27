package dfh.mgs.vo.freegame;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import dfh.mgs.vo.DataStructuresNS;

public class Offers {

	public Offers(){
		
	}
	
	private List<Offer> offerList = new ArrayList<Offer>();

	@XmlElementWrapper(name = "Offers", namespace = DataStructuresNS.FreegameAdminNS)
	@XmlElement(name = "Offer", namespace = DataStructuresNS.FreegameAdminNS)
	public List<Offer> getOfferList() {
		return offerList;
	}

	public void setOfferList(List<Offer> offerList) {
		this.offerList = offerList;
	}

}
