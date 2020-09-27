package dfh.model;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sb_coupon", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Sbcoupon implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7344765238544828494L;
	private Integer id;
	private String loginname;
	private Date createtime;
	private Integer status;
	private String shippingcode;
	private String remark;
	
	/** default constructor */
	public Sbcoupon() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	@javax.persistence.Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@javax.persistence.Column(name = "shippingcode")
	public String getShippingcode() {
		return shippingcode;
	}

	public void setShippingcode(String shippingcode) {
		this.shippingcode = shippingcode;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}