package dfh.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**女神
 * Commissions entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "singleparty", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class SingleParty implements java.io.Serializable {

	// Fields
	private String loginname;
	private Double bettotal;//总投注金额
	private String rankdate;//排名日期
	private Integer ranking;//排名
	
	@Id
	@Column(name="loginname")
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	@Column(name="bettotal")
	public Double getBettotal() {
		return bettotal;
	}
	public void setBettotal(Double bettotal) {
		this.bettotal = bettotal;
	}
	@Column(name="rankdate")
	public String getRankdate() {
		return rankdate;
	}
	public void setRankdate(String rankdate) {
		this.rankdate = rankdate;
	}
	@Column(name="ranking")
	public Integer getRanking() {
		return ranking;
	}
	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}
	
	
}