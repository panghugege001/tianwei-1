package dfh.model;

import javax.persistence.Embeddable;

/**
 * CommissionrecordsId entity. @author MyEclipse Persistence Tools
 */
@Embeddable
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class CommissionrecordsId implements java.io.Serializable {

	// Fields

	private String loginname;
	private Integer year;
	private Integer month;

	// Constructors

	/** default constructor */
	public CommissionrecordsId() {
	}

	/** full constructor */
	public CommissionrecordsId(String loginname, Integer year, Integer month) {
		this.loginname = loginname;
		this.year = year;
		this.month = month;
	}

	// Property accessors

	@javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@javax.persistence.Column(name = "year")
	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	@javax.persistence.Column(name = "month")
	public Integer getMonth() {
		return this.month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof CommissionrecordsId))
			return false;
		CommissionrecordsId castOther = (CommissionrecordsId) other;

		return ((this.getLoginname() == castOther.getLoginname()) || (this.getLoginname() != null && castOther.getLoginname() != null && this.getLoginname().equals(castOther.getLoginname())))
				&& ((this.getYear() == castOther.getYear()) || (this.getYear() != null && castOther.getYear() != null && this.getYear().equals(castOther.getYear())))
				&& ((this.getMonth() == castOther.getMonth()) || (this.getMonth() != null && castOther.getMonth() != null && this.getMonth().equals(castOther.getMonth())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getLoginname() == null ? 0 : this.getLoginname().hashCode());
		result = 37 * result + (getYear() == null ? 0 : this.getYear().hashCode());
		result = 37 * result + (getMonth() == null ? 0 : this.getMonth().hashCode());
		return result;
	}

}