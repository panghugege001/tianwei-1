package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**好友推荐的积分池
 * Commissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "friendintroduce", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Friendintroduce implements java.io.Serializable {

	// Fields
	private Integer id ;
	private String toplineuser ;//上线user
	private String downlineuser ;//下线user
    private Date createtime;//创建时间
    private String remark;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)  
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="toplineuser")
	public String getToplineuser() {
		return toplineuser;
	}
	public void setToplineuser(String toplineuser) {
		this.toplineuser = toplineuser;
	}
	
	@Column(name="downlineuser")
	public String getDownlineuser() {
		return downlineuser;
	}
	public void setDownlineuser(String downlineuser) {
		this.downlineuser = downlineuser;
	}
	
	@Column(name="createtime")
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public Date getCreatetime() {
		return createtime;
	}
	
	
}