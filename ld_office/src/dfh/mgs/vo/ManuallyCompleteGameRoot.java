package dfh.mgs.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ManuallyCompleteGameResponse")
public class ManuallyCompleteGameRoot extends OrionResponse {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<CompleteGameResponse> ManuallyCompleteGameResult = new ArrayList<CompleteGameResponse>();

	@XmlElementWrapper(name = "ManuallyCompleteGameResult")
	@XmlElement(name = "CompleteGameResponse", namespace = DataStructuresNS.VanguardAdminNS)
	public List<CompleteGameResponse> getManuallyCompleteGameResult() {
		return ManuallyCompleteGameResult;
	}

	public void setManuallyCompleteGameResult(List<CompleteGameResponse> manuallyCompleteGameResult) {
		ManuallyCompleteGameResult = manuallyCompleteGameResult;
	}
	
}
