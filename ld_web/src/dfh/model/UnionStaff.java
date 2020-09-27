package dfh.model;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Accountinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "union_staff", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class UnionStaff implements java.io.Serializable {

	// Fields

	private int id;
	private int unionId;
	@Column(name="union_id")
	public int getUnionId() {
		return unionId;
	}

	public void setUnionId(int unionId) {
		this.unionId = unionId;
	}
	private String username;
	private Date updateTime;
	private String remark;
	private int level;
	@Column(name="level")
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	// Constructors

	/** default constructor */
	public UnionStaff() {
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Column(name="username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="updateTime")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}