package dfh.model;

import java.util.Date;

import javax.persistence.Embeddable;

/**
 * CommissionsId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class PtCommissionsId implements java.io.Serializable {

	// Fields

	private String agent;
	private Date createdate ;
	private String platform ;
	
	public PtCommissionsId() {
		super();
	}
	public PtCommissionsId(String agent, Date createdate, String platform) {
		super();
		this.agent = agent;
		this.createdate = createdate;
		this.platform = platform;
	}
	@javax.persistence.Column(name = "agent")
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	@javax.persistence.Column(name = "createdate")
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	@javax.persistence.Column(name = "platform")
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	
	
	

}