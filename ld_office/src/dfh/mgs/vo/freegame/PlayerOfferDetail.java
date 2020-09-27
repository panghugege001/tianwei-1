package dfh.mgs.vo.freegame;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import dfh.mgs.vo.DataStructuresNS;
import dfh.utils.DateUtil;

public class PlayerOfferDetail implements java.io.Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date dateAwarded;
    private Date endDate;
    private Integer gamesAwarded;
    private Integer gamesLeft;
    private Integer instanceId;
    private String loginName;
    private Date startDate;
    private Long totalWinnings;

    public PlayerOfferDetail() {
    }

    public PlayerOfferDetail(String dateAwarded, String endDate, Integer gamesAwarded, Integer gamesLeft, Integer instanceId, String loginName, String startDate, Long totalWinnings) {
           Date tDateAwarded = DateUtil.parseDateForStandard(dateAwarded.replace("T", " "));
    	   this.dateAwarded = tDateAwarded;
    	   Date tEndDate = DateUtil.parseDateForStandard(dateAwarded.replace("T", " "));
           this.endDate = tEndDate;
           this.gamesAwarded = gamesAwarded;
           this.gamesLeft = gamesLeft;
           this.instanceId = instanceId;
           this.loginName = loginName;
           Date tStartDate = DateUtil.parseDateForStandard(dateAwarded.replace("T", " "));
           this.startDate = tStartDate;
           this.totalWinnings = totalWinnings;
    }

    @XmlElement(name = "DateAwarded", namespace = DataStructuresNS.FreegameAdminNS)
	public Date getDateAwarded() {
		return dateAwarded;
	}

	public void setDateAwarded(Date dateAwarded) {
		this.dateAwarded = dateAwarded;
	}

	@XmlElement(name = "EndDate", namespace = DataStructuresNS.FreegameAdminNS)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@XmlElement(name = "GamesAwarded", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getGamesAwarded() {
		return gamesAwarded;
	}

	public void setGamesAwarded(Integer gamesAwarded) {
		this.gamesAwarded = gamesAwarded;
	}

	@XmlElement(name = "GamesLeft", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getGamesLeft() {
		return gamesLeft;
	}

	public void setGamesLeft(Integer gamesLeft) {
		this.gamesLeft = gamesLeft;
	}

	@XmlElement(name = "InstanceId", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	@XmlElement(name = "LoginName", namespace = DataStructuresNS.FreegameAdminNS)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@XmlElement(name = "StartDate", namespace = DataStructuresNS.FreegameAdminNS)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@XmlElement(name = "TotalWinnings", namespace = DataStructuresNS.FreegameAdminNS)
	public Long getTotalWinnings() {
		return totalWinnings;
	}

	public void setTotalWinnings(Long totalWinnings) {
		this.totalWinnings = totalWinnings;
	}
 
}