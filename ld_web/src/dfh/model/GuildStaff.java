package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Accountinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "guild_staff", catalog = "elf")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class GuildStaff implements java.io.Serializable {

	// Fields

	private int id;
	private int guildId;
	private String username;
	private Date joinTime;
	private String name;
	private String day;
	@javax.persistence.Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Double deposet;
	@javax.persistence.Column(name = "day")
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	private Double rangeDeposet;
	private Double rangeGameAmount;
	@javax.persistence.Column(name = "rangeDeposet")
	public Double getRangeDeposet() {
		return rangeDeposet;
	}

	public void setRangeDeposet(Double rangeDeposet) {
		this.rangeDeposet = rangeDeposet;
	}
	@javax.persistence.Column(name = "rangeGameAmount")

	public Double getRangeGameAmount() {
		return rangeGameAmount;
	}

	public void setRangeGameAmount(Double rangeGameAmount) {
		this.rangeGameAmount = rangeGameAmount;
	}

	private Double gameAmount;
	private int state;
	private String remark;
	private Date updatetime;
	@javax.persistence.Column(name = "updatetime")
	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Id
	@javax.persistence.Column(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@javax.persistence.Column(name = "guild_id")
	public int getGuildId() {
		return guildId;
	}

	public void setGuildId(int guildId) {
		this.guildId = guildId;
	}
	@javax.persistence.Column(name = "username")

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	@javax.persistence.Column(name = "join_time")

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	@javax.persistence.Column(name = "deposet")

	public Double getDeposet() {
		return deposet;
	}

	public void setDeposet(Double deposet) {
		this.deposet = deposet;
	}
	@javax.persistence.Column(name = "game_amount")

	public Double getGameAmount() {
		return gameAmount;
	}

	public void setGameAmount(Double gameAmount) {
		this.gameAmount = gameAmount;
	}
	@javax.persistence.Column(name = "state")

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}