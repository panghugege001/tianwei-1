package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * topicstatus entity. by boots
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "topicstatus", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TopicStatus implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7352349855161460124L;

	private Integer id;
	// 主题id
	private Integer topicId;
	// 接收人
	private String receiveUname;
	// ip地址
	private String ipAddress;
	// 用户是否已读
	private Integer isUserRead;
	
	// 创建时间
	private Date createTime;
	// 是否有效
	private Integer isValid;
	
	private String content;
	
	private String title;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "topic_id")
	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	@javax.persistence.Column(name = "ip_address")
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@javax.persistence.Column(name = "receive_uname")
	public String getReceiveUname() {
		return receiveUname;
	}

	public void setReceiveUname(String receiveUname) {
		this.receiveUname = receiveUname;
	}

	@javax.persistence.Column(name = "is_user_read")
	public Integer getIsUserRead() {
		return isUserRead;
	}

	public void setIsUserRead(Integer isUserRead) {
		this.isUserRead = isUserRead;
	}

	

	@javax.persistence.Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@javax.persistence.Column(name = "is_valid")
	public Integer getIsValid() {
		return isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}
	
	@javax.persistence.Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@javax.persistence.Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}