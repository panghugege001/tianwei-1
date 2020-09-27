package dfh.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * 红包雨
 *
 */
@Entity
@Table(name = "raincoupon_record", catalog = "lbao")
@org.hibernate.annotations.Entity(dynamicUpdate = false, dynamicInsert = false)
public class RaincouponRecord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String loginname;
	private Integer typeid;
	private Double amount;
	private Integer times;
	private String coupon;
	private String remark;
	private String createtime;
	private Date updatetime;
	private Integer isused ;
	private String pno ;
	private String tempCreateTime ;
	private String createDate;

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Integer getTypeid() {
		return typeid;
	}
	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public String getCoupon() {
		return coupon;
	}
	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Integer getIsused() {
		return isused;
	}
	public void setIsused(Integer isused) {
		this.isused = isused;
	}
	public String getPno() {
		return pno;
	}
	public void setPno(String pno) {
		this.pno = pno;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	@Transient
	public String getTempCreateTime() {
		return tempCreateTime;
	}
	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}
	
}
