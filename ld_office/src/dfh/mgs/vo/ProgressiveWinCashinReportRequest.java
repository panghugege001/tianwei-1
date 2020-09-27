package dfh.mgs.vo;

import java.util.Date;

import dfh.utils.DateUtil;

/**
 * 要求的时间格式 yyyy-MM-ddTHH:mm:ss.SSS
 */
public class ProgressiveWinCashinReportRequest implements java.io.Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date endDate;
    private Boolean pendingCashinOnly;
    private Integer serverId;
    private Date startDate;

    public ProgressiveWinCashinReportRequest() {
    }

    public ProgressiveWinCashinReportRequest(Date endDate, Boolean pendingCashinOnly, Integer serverId, Date startDate) {
           this.endDate = endDate;
           this.pendingCashinOnly = pendingCashinOnly;
           this.serverId = serverId;
           this.startDate = startDate;
    }

	public String getEndDate() {
		return DateUtil.getDateTimeStr4MG(endDate).replace(" ", "T");
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getPendingCashinOnly() {
		return pendingCashinOnly;
	}

	public void setPendingCashinOnly(Boolean pendingCashinOnly) {
		this.pendingCashinOnly = pendingCashinOnly;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public String getStartDate() {
		return DateUtil.getDateTimeStr4MG(startDate).replace(" ", "T");
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
    
}