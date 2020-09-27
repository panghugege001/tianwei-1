package dfh.mgs.vo;

import java.util.Calendar;

import javax.xml.bind.annotation.XmlElement;

public class GetFailedGamesResponse  implements java.io.Serializable {
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer clientId;
    private String description;
    private Integer moduleId;
    private Long rowId;
    private Integer serverId;
    private Integer sessionId;
    private Calendar timeCompleted;
    private Calendar timeCreated;
    private Integer tournamentId;
    private Integer transNumber;
    private String uniqueId;
    private Integer userId;

    public GetFailedGamesResponse() {
    }

    public GetFailedGamesResponse(Integer clientId, String description, Integer moduleId, Long rowId, Integer serverId, Integer sessionId,
           Calendar timeCompleted, Calendar timeCreated, Integer tournamentId, Integer transNumber, String uniqueId, Integer userId) {
           this.clientId = clientId;
           this.description = description;
           this.moduleId = moduleId;
           this.rowId = rowId;
           this.serverId = serverId;
           this.sessionId = sessionId;
           this.timeCompleted = timeCompleted;
           this.timeCreated = timeCreated;
           this.tournamentId = tournamentId;
           this.transNumber = transNumber;
           this.uniqueId = uniqueId;
           this.userId = userId;
    }
    
    @XmlElement(name="ClientId", namespace=DataStructuresNS.VanguardAdminNS)
	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	@XmlElement(name="Description", namespace=DataStructuresNS.VanguardAdminNS)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name="ModuleId", namespace=DataStructuresNS.VanguardAdminNS)
	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	@XmlElement(name="RowId", namespace=DataStructuresNS.VanguardAdminNS)
	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	@XmlElement(name="ServerId", namespace=DataStructuresNS.VanguardAdminNS)
	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	@XmlElement(name="SessionId", namespace=DataStructuresNS.VanguardAdminNS)
	public Integer getSessionId() {
		return sessionId;
	}

	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	@XmlElement(name="TimeCompleted", namespace=DataStructuresNS.VanguardAdminNS)
	public Calendar getTimeCompleted() {
		return timeCompleted;
	}

	public void setTimeCompleted(Calendar timeCompleted) {
		this.timeCompleted = timeCompleted;
	}

	@XmlElement(name="TimeCreated", namespace=DataStructuresNS.VanguardAdminNS)
	public Calendar getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Calendar timeCreated) {
		this.timeCreated = timeCreated;
	}

	@XmlElement(name="TournamentId", namespace=DataStructuresNS.VanguardAdminNS)
	public Integer getTournamentId() {
		return tournamentId;
	}

	public void setTournamentId(Integer tournamentId) {
		this.tournamentId = tournamentId;
	}

	@XmlElement(name="TransNumber", namespace=DataStructuresNS.VanguardAdminNS)
	public Integer getTransNumber() {
		return transNumber;
	}

	public void setTransNumber(Integer transNumber) {
		this.transNumber = transNumber;
	}

	@XmlElement(name="UniqueId", namespace=DataStructuresNS.VanguardAdminNS)
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@XmlElement(name="UserId", namespace=DataStructuresNS.VanguardAdminNS)
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
    
}