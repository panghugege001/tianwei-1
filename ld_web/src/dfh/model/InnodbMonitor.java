package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * InnodbMonitor entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "innodb_monitor", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class InnodbMonitor implements java.io.Serializable {

	// Fields

	private Integer a;

	// Constructors

	/** default constructor */
	public InnodbMonitor() {
	}

	/** full constructor */
	public InnodbMonitor(Integer a) {
		this.a = a;
	}

	// Property accessors
	@Id
	@javax.persistence.Column(name = "a")
	public Integer getA() {
		return this.a;
	}

	public void setA(Integer a) {
		this.a = a;
	}

}