package dfh.model;

import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Commissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "agentvip", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class AgentVip implements java.io.Serializable {

	// Fields
	private AgentVipId id ;
	private Integer level ;
	private Integer activemonth;
	private Integer activeuser;
	private Date registertime;
	private Date createtime;
	private String remark;
	private Double historyfee ;
	
	public AgentVip() {
		super();
	}
	public AgentVip(AgentVipId id) {
		super();
		this.id = id;
	}
	@EmbeddedId
	@javax.persistence.Column(name = "id")
	public AgentVipId getId() {
		return id;
	}
	public void setId(AgentVipId id) {
		this.id = id;
	}
	public Integer getActivemonth() {
		return activemonth;
	}
	public void setActivemonth(Integer activemonth) {
		this.activemonth = activemonth;
	}
	public Integer getActiveuser() {
		return activeuser;
	}
	public void setActiveuser(Integer activeuser) {
		this.activeuser = activeuser;
	}
	public Date getRegistertime() {
		return registertime;
	}
	public void setRegistertime(Date registertime) {
		this.registertime = registertime;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getHistoryfee() {
		return historyfee;
	}
	public void setHistoryfee(Double historyfee) {
		this.historyfee = historyfee;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}

}