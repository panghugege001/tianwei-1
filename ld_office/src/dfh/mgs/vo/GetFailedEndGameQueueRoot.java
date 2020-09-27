package dfh.mgs.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="GetFailedEndGameQueueResponse")
public class GetFailedEndGameQueueRoot extends OrionResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<GetFailedGamesResponse> GetFailedEndGameQueueResult = new ArrayList<GetFailedGamesResponse>();

	@XmlElementWrapper(name = "GetFailedEndGameQueueResult")
	@XmlElement(name = "GetFailedGamesResponse", namespace = DataStructuresNS.VanguardAdminNS)
	public List<GetFailedGamesResponse> getGetFailedEndGameQueueResult() {
		return GetFailedEndGameQueueResult;
	}

	public void setGetFailedEndGameQueueResult(List<GetFailedGamesResponse> getFailedEndGameQueueResult) {
		GetFailedEndGameQueueResult = getFailedEndGameQueueResult;
	}

}
