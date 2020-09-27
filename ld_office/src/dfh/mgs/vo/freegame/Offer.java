package dfh.mgs.vo.freegame;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import dfh.mgs.vo.DataStructuresNS;
import dfh.utils.DateUtil;

public class Offer implements java.io.Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer cost;
    private String description;
    private Date endDate;
    private Integer id;
    private Integer playerCount;
    private Date startDate;

    public Offer() {
    }

    public Offer(Integer cost, String description, String endDate, Integer id, Integer playerCount, String startDate) {
           this.cost = cost;
           this.description = description;
           Date tEndDate = DateUtil.parseDateForStandard(endDate.replace("T", " "));
           this.endDate = tEndDate;
           this.id = id;
           this.playerCount = playerCount;
           Date tStartDate = DateUtil.parseDateForStandard(startDate.replace("T", " "));
           this.startDate = tStartDate;
    }

    @XmlElement(name = "Cost", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	@XmlElement(name = "Description", namespace = DataStructuresNS.FreegameAdminNS)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name = "EndDate", namespace = DataStructuresNS.FreegameAdminNS)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@XmlElement(name = "Id", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement(name = "PlayerCount", namespace = DataStructuresNS.FreegameAdminNS)
	public Integer getPlayerCount() {
		return playerCount;
	}

	public void setPlayerCount(Integer playerCount) {
		this.playerCount = playerCount;
	}

	@XmlElement(name = "StartDate", namespace = DataStructuresNS.FreegameAdminNS)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}