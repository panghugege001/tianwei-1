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
@Table(name = "friendbonus", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Friendbonus implements java.io.Serializable {

	// Fields
	private Integer id ;
	private String toplineuser;//玩家账号
	private Double money ;//奖励金额
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
	
	
	@Column(name="money")
	public Double getMoney() {
		return money;
	}
	
	
	public void setMoney(Double money) {
		this.money = money;
	}
	
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	
}