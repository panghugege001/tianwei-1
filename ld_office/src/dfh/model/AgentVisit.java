package dfh.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Guestbook entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "agent_visit", catalog = "longdu_email")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AgentVisit implements java.io.Serializable {

	// Fields

	private Integer id;
	private String loginname;
	private String agent;
	private String agent_website;
	private String client_ip;
	private String client_address;
	private String source_come_url;
	private String source_go_url;
	private Timestamp createtime;

	// Constructors

	/** default constructor */
	public AgentVisit() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "agent")
	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	@javax.persistence.Column(name = "agent_website")
	public String getAgent_website() {
		return agent_website;
	}

	public void setAgent_website(String agent_website) {
		this.agent_website = agent_website;
	}

	@javax.persistence.Column(name = "client_ip")
	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	@javax.persistence.Column(name = "client_address")
	public String getClient_address() {
		return client_address;
	}

	public void setClient_address(String client_address) {
		this.client_address = client_address;
	}

	@javax.persistence.Column(name = "source_come_url")
	public String getSource_come_url() {
		return source_come_url;
	}

	public void setSource_come_url(String source_come_url) {
		this.source_come_url = source_come_url;
	}

	@javax.persistence.Column(name = "source_go_url")
	public String getSource_go_url() {
		return source_go_url;
	}

	public void setSource_go_url(String source_go_url) {
		this.source_go_url = source_go_url;
	}

	@javax.persistence.Column(name = "createtime")
	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	
	
}