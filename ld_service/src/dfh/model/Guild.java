package dfh.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Accountinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "guild", catalog = "elufa")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Guild implements java.io.Serializable {

	// Fields



	private int id;
	private String part;
	private String president;
	private String name;
	private Date startTime;
	private Date endTime;
	private Date createTime;
	private Date updateTime;
	private String level;
	private int max;
	private String game;
	private int state;
	private String remark;
	private String creator;
	@javax.persistence.Column(name = "creator")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Id
	@javax.persistence.Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@javax.persistence.Column(name = "part")
	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}
	@javax.persistence.Column(name = "president")
	public String getPresident() {
		return president;
	}

	public void setPresident(String president) {
		this.president = president;
	}
	@javax.persistence.Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@javax.persistence.Column(name = "start_time")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@javax.persistence.Column(name = "end_time")

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@javax.persistence.Column(name = "createTime")

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@javax.persistence.Column(name = "updateTime")

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@javax.persistence.Column(name = "level")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	@javax.persistence.Column(name = "max")

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	@javax.persistence.Column(name = "game")
	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}
	@javax.persistence.Column(name = "state")

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	@javax.persistence.Column(name = "remark")

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}







}