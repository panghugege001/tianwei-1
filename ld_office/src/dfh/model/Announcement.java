package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Announcement entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "announcement", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Announcement implements java.io.Serializable {

	// Fields

	private Integer id;
	private String type;
	private String content;
	private Timestamp createtime;
	private String title;

	// Constructors
	@javax.persistence.Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/** default constructor */
	public Announcement() {
	}

	/** full constructor */
	public Announcement(String type, String content, Timestamp createtime,String title) {
		this.type = type;
		this.content = content;
		this.createtime = createtime;
		this.title=title;
		this.id=id;
	}
	
	public Announcement(Integer id,String type, String content, Timestamp createtime,String title) {
		this.type = type;
		this.content = content;
		this.createtime = createtime;
		this.title=title;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@javax.persistence.Column(name = "content")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@javax.persistence.Column(name = "createtime")
	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

}