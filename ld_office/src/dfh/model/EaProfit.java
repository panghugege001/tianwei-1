package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "eaprofit", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class EaProfit implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4518969296949663976L;
	private Integer id;
	private Integer year;
	private Integer month;
	private Double eaprofit;
	private Double xingzheng;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@javax.persistence.Column(name = "year")
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	
	@javax.persistence.Column(name = "month")
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	
	@javax.persistence.Column(name = "eaprofit")
	public Double getEaprofit() {
		return eaprofit;
	}
	public void setEaprofit(Double eaprofit) {
		this.eaprofit = eaprofit;
	}
	
	@javax.persistence.Column(name = "xingzheng")
	public Double getXingzheng() {
		return xingzheng;
	}
	public void setXingzheng(Double xingzheng) {
		this.xingzheng = xingzheng;
	}
	
}
