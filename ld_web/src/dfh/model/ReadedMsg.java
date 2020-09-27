package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "msg_readed", catalog = "longdu_email")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class ReadedMsg implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer msgID;
	private String username;
	private Date createTime;

	public ReadedMsg() {
	}

	public ReadedMsg(Integer msgID, String username,  Date createTime) {
		this.msgID = msgID;
		this.username = username;
		this.createTime = createTime;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "mid")
	public Integer getMsgID() {
		return msgID;
	}

	public void setMsgID(Integer msgID) {
		this.msgID = msgID;
	}

	@javax.persistence.Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@javax.persistence.Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}