package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author jat
 *
 */
@Entity
@Table(name = "totalpoint", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TotalPoint implements java.io.Serializable {

	// Fields
	private Integer id ;
	private String username ;
	private Double totalpoints;//可用积分
	private Date updatetime;
	private String remark ;
	private Double oldtotalpoints;//历史总积分
	
	@Id
	@GeneratedValue(strategy = IDENTITY)  
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Column(name="totalpoints")
	public Double getTotalpoints() {
		return totalpoints;
	}
	public void setTotalpoints(Double totalpoints) {
		this.totalpoints = totalpoints;
	}
	@Column(name="updatetime")
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	@Column(name="oldtotalpoints")
	public Double getOldtotalpoints() {
		return oldtotalpoints;
	}
	public void setOldtotalpoints(Double oldtotalpoints) {
		this.oldtotalpoints = oldtotalpoints;
	}
}