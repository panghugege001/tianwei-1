package dfh.model;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 红包奖金记录
 * @author jalen
 *
 */
@Entity
@Table(name = "hb_record", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class HBrecord implements java.io.Serializable {

	// Fields
	private Integer id ;
	private Integer hbid ; //红包id
	private String username ;//玩家账号
	private String type ;//类型 1.收入  2.支出
	private Double money ;//奖励金额
    private Date updatetime;//创建时间
    private String remark;//备注 保存几个老虎机的流水
    private Integer deposit;//备注 保存几个老虎机的流水
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="hbid")
	public Integer getHbid() {
		return hbid;
	}
	public void setHbid(Integer hbid) {
		this.hbid = hbid;
	}
	@Column(name="username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	@Column(name="updatetime")
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="deposit")
	public Integer getDeposit() {
		return deposit;
	}
	public void setDeposit(Integer deposit) {
		this.deposit = deposit;
	}
}