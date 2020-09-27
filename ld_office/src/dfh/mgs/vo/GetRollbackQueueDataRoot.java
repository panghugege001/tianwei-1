package dfh.mgs.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="GetRollbackQueueDataResponse")
public class GetRollbackQueueDataRoot extends OrionResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<QueueDataResponse> GetRollbackQueueDataResult = new ArrayList<QueueDataResponse>();

	@XmlElementWrapper(name = "GetRollbackQueueDataResult")
	@XmlElement(name = "QueueDataResponse", namespace = DataStructuresNS.VanguardAdminNS)
	public List<QueueDataResponse> getGetRollbackQueueDataResult() {
		return GetRollbackQueueDataResult;
	}

	public void setGetRollbackQueueDataResult(List<QueueDataResponse> getRollbackQueueDataResult) {
		GetRollbackQueueDataResult = getRollbackQueueDataResult;
	}

}
