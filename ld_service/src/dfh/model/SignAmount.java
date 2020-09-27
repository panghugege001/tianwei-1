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
 * Commissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "signamount", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class SignAmount implements java.io.Serializable {

	// Fields
	private Integer id ;
	private String username ;
    private Double amountbalane;//奖金余额
    private Date updatetime;//更新时间
    private String remark;
    private int continuesigncount;  
	
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
	@Column(name="amountbalane")
	public Double getAmountbalane() {
		return amountbalane;
	}
	public void setAmountbalane(Double amountbalane) {
		this.amountbalane = amountbalane;
	}
	
	@Column(name="continuesigncount")
	public int getContinuesigncount() {
		return continuesigncount;
	}
	public void setContinuesigncount(int continuesigncount) {
		this.continuesigncount = continuesigncount;
	}

	
}