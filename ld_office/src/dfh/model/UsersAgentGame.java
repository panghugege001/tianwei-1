package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 代理账号与游戏账号映射
 */
@Entity
@Table(name = "users_agent_game", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class UsersAgentGame implements java.io.Serializable {

	// Fields
	private Integer id;
	private String loginnameAgent;
	private String loginnameGame;
	private Date createTime;
	private Integer deleteFlag;
	private String remark;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "loginnameAgent")
	public String getLoginnameAgent() {
		return loginnameAgent;
	}
	public void setLoginnameAgent(String loginnameAgent) {
		this.loginnameAgent = loginnameAgent;
	}
	@Column(name = "loginnameGame")
	public String getLoginnameGame() {
		return loginnameGame;
	}
	public void setLoginnameGame(String loginnameGame) {
		this.loginnameGame = loginnameGame;
	}
	@Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name = "deleteFlag")
	public Integer getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}