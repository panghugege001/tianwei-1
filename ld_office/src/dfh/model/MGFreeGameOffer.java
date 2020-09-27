package dfh.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mgfreegame", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class MGFreeGameOffer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	private Integer id;                           
	private String description;                 
	private Integer cost;                
	private Integer playerCount;         
	private Date startDate;
	private Date endDate;
	
	@Id
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="cost")
	public Integer getCost() {
		return cost;
	}
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	
	@Column(name="playerCount")
	public Integer getPlayerCount() {
		return playerCount;
	}
	public void setPlayerCount(Integer playerCount) {
		this.playerCount = playerCount;
	}
	
	@Column(name="startDate")
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Column(name="endDate")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}