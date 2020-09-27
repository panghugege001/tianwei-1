package dfh.model;

import java.util.Date;

import javax.persistence.Embeddable;

/**
 * CommissionsId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AgentVipId implements java.io.Serializable {

	// Fields

	private String agent;
	private String createdate ;
	
	public AgentVipId(String agent, String createdate) {
		super();
		this.agent = agent;
		this.createdate = createdate;
	}
	public AgentVipId() {
		super();
	}
	@javax.persistence.Column(name = "agent")
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	@javax.persistence.Column(name = "createdate")
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

}