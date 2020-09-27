package dfh.mgs.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="GetCommitQueueDataResponse", namespace="http://mgsops.net/AdminAPI_Admin")
public class GetCommitQueueDataRoot extends OrionResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<QueueDataResponse> GetCommitQueueDataResult = new ArrayList<QueueDataResponse>();

	@XmlElementWrapper(name = "GetCommitQueueDataResult")
	@XmlElement(name = "QueueDataResponse", namespace = DataStructuresNS.VanguardAdminNS)
	public List<QueueDataResponse> getGetCommitQueueDataResult() {
		return GetCommitQueueDataResult;
	}

	public void setGetCommitQueueDataResult(List<QueueDataResponse> getCommitQueueDataResult) {
		GetCommitQueueDataResult = getCommitQueueDataResult;
	}
	
}
