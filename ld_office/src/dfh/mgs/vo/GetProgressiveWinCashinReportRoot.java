package dfh.mgs.vo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "GetProgressiveWinCashinReportResponse")
public class GetProgressiveWinCashinReportRoot extends OrionResponse {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<ProgressiveWinCashinReportResponse> pwrRespList = new ArrayList<ProgressiveWinCashinReportResponse>();

	@XmlElementWrapper(name = "GetProgressiveWinCashinReportResult")
	@XmlElement(name = "ProgressiveWinCashinReportResponse", namespace = DataStructuresNS.VanguardAdminNS)
	public List<ProgressiveWinCashinReportResponse> getPwrRespList() {
		return pwrRespList;
	}

	public void setPwrRespList(List<ProgressiveWinCashinReportResponse> pwrRespList) {
		this.pwrRespList = pwrRespList;
	}
	
}
