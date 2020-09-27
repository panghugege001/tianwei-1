package dfh.mgs.vo.freegame;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dfh.mgs.vo.OrionResponse;

@XmlRootElement(name = "GetOffersPlayerDetailsResponse")
public class GetFreegamesPlayerDetailsRoot extends OrionResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GetFreegamesPlayerDetailsResult getfreegamesPlayerDetailsResult;

	@XmlElement(name = "GetFreegamesPlayerDetailsResult")
	public GetFreegamesPlayerDetailsResult getGetfreegamesPlayerDetailsResult() {
		return getfreegamesPlayerDetailsResult;
	}

	public void setGetfreegamesPlayerDetailsResult(GetFreegamesPlayerDetailsResult getfreegamesPlayerDetailsResult) {
		this.getfreegamesPlayerDetailsResult = getfreegamesPlayerDetailsResult;
	}

}
