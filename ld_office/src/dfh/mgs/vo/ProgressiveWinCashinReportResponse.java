package dfh.mgs.vo;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlElement;

public class ProgressiveWinCashinReportResponse implements java.io.Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long cashinAmount;
    private Calendar cashinTime;
    private Integer cashinTransNumber;
    private String loginName;
    private Integer pendingCashinTransNumber;
    private String progressiveGame;
    private Integer progressiveTransNumber;
    private Calendar progressiveWinDate;
    private Integer serverId;
    private Integer userId;

    public ProgressiveWinCashinReportResponse() {
    }

    public ProgressiveWinCashinReportResponse(Long cashinAmount, Calendar cashinTime, Integer cashinTransNumber, String loginName, Integer pendingCashinTransNumber,
           String progressiveGame,  Integer progressiveTransNumber, Calendar progressiveWinDate, Integer serverId, Integer userId) {
           this.cashinAmount = cashinAmount;
           this.cashinTime = cashinTime;
           this.cashinTransNumber = cashinTransNumber;
           this.loginName = loginName;
           this.pendingCashinTransNumber = pendingCashinTransNumber;
           this.progressiveGame = progressiveGame;
           this.progressiveTransNumber = progressiveTransNumber;
           this.progressiveWinDate = progressiveWinDate;
           this.serverId = serverId;
           this.userId = userId;
    }

    @XmlElement(name = "CashinAmount", namespace = DataStructuresNS.VanguardAdminNS)
	public Long getCashinAmount() {
		return cashinAmount;
	}

	public void setCashinAmount(Long cashinAmount) {
		this.cashinAmount = cashinAmount;
	}

	@XmlElement(name = "CashinTime", namespace = DataStructuresNS.VanguardAdminNS)
	public Calendar getCashinTime() {
		return cashinTime;
	}

	public void setCashinTime(Calendar cashinTime) {
		this.cashinTime = cashinTime;
	}

	@XmlElement(name = "CashinTransNumber", namespace = DataStructuresNS.VanguardAdminNS)
	public Integer getCashinTransNumber() {
		return cashinTransNumber;
	}

	public void setCashinTransNumber(Integer cashinTransNumber) {
		this.cashinTransNumber = cashinTransNumber;
	}

	@XmlElement(name = "LoginName", namespace = DataStructuresNS.VanguardAdminNS)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@XmlElement(name = "PendingCashinTransNumber", namespace = DataStructuresNS.VanguardAdminNS)
	public Integer getPendingCashinTransNumber() {
		return pendingCashinTransNumber;
	}

	public void setPendingCashinTransNumber(Integer pendingCashinTransNumber) {
		this.pendingCashinTransNumber = pendingCashinTransNumber;
	}

	@XmlElement(name = "ProgressiveGame", namespace = DataStructuresNS.VanguardAdminNS)
	public String getProgressiveGame() {
		return progressiveGame;
	}

	public void setProgressiveGame(String progressiveGame) {
		this.progressiveGame = progressiveGame;
	}

	@XmlElement(name = "ProgressiveTransNumber", namespace = DataStructuresNS.VanguardAdminNS)
	public Integer getProgressiveTransNumber() {
		return progressiveTransNumber;
	}

	public void setProgressiveTransNumber(Integer progressiveTransNumber) {
		this.progressiveTransNumber = progressiveTransNumber;
	}

	@XmlElement(name = "ProgressiveWinDate", namespace = DataStructuresNS.VanguardAdminNS)
	public Calendar getProgressiveWinDate() {
		return progressiveWinDate;
	}

	public void setProgressiveWinDate(Calendar progressiveWinDate) {
		this.progressiveWinDate = progressiveWinDate;
	}

	@XmlElement(name = "ServerId", namespace = DataStructuresNS.VanguardAdminNS)
	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	@XmlElement(name = "UserId", namespace = DataStructuresNS.VanguardAdminNS)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
    
}