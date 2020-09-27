package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**好友推荐奖励记录
 * Commissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "friendbonusrecord", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Friendbonusrecord implements java.io.Serializable {

	// Fields
	private Integer id ;
	private String toplineuser ;//上线user
	private String downlineuser ;//下线user
	private String type ;//类型 1.收入  2.支出
	private Double money ;//奖励金额
    private Date createtime;//创建时间
    private String distributeDate;//派发日期
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
	
	@Column(name="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="money")
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
	@Column(name="createtime")
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
	
	@Column(name="distributedate")
	public String getDistributeDate() {
		return distributeDate;
	}
	public void setDistributeDate(String distributeDate) {
		this.distributeDate = distributeDate;
	}

	
}