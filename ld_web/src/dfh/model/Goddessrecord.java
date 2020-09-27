package dfh.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**守护女神记录
 * Commissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "goddessrecord", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Goddessrecord implements java.io.Serializable {

	// Fields
	private String loginname;
	private String goddessname;//女神名
	private Integer flowernum;//玫瑰数量
	private String couponnum;//红包优惠码
    private Date createtime;//创建时间
    private String distributeMonth;//派发过的日期
    private Integer ranking;//排名
	private Double bettotal ;//总投注金额
	private Integer peonum;
	
	@Id
	@Column(name="loginname")
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	
	@Column(name="goddessname")
	public String getGoddessname() {
		return goddessname;
	}
	public void setGoddessname(String goddessname) {
		this.goddessname = goddessname;
	}
	
	@Column(name="flowernum")
	public Integer getFlowernum() {
		return flowernum;
	}
	public void setFlowernum(Integer flowernum) {
		this.flowernum = flowernum;
	}
	
	@Column(name="couponnum")
	public String getCouponnum() {
		return couponnum;
	}
	public void setCouponnum(String couponnum) {
		this.couponnum = couponnum;
	}
	
	@Column(name="createtime")
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	@Column(name="distributemonth")
	public String getDistributeMonth() {
		return distributeMonth;
	}
	public void setDistributeMonth(String distributeMonth) {
		this.distributeMonth = distributeMonth;
	}
	
	@Column(name="ranking")
	public Integer getRanking() {
		return ranking;
	}
	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
	
	@Column(name="bettotal")
	public Double getBettotal() {
		return bettotal;
	}
	public void setBettotal(Double bettotal) {
		this.bettotal = bettotal;
	}
	@Transient
	public Integer getPeonum() {
		return peonum;
	}
	public void setPeonum(Integer peonum) {
		this.peonum = peonum;
	}
	
}