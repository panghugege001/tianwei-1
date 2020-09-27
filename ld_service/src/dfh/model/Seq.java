package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Seq entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "seq", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Seq implements java.io.Serializable {

	// Fields

	private String seqName;
	private String seqValue;

	// Constructors

	/** default constructor */
	public Seq() {
	}

	/** full constructor */
	public Seq(String seqName, String seqValue) {
		this.seqName = seqName;
		this.seqValue = seqValue;
	}

	// Property accessors
	@Id
	@javax.persistence.Column(name = "seqName")
	public String getSeqName() {
		return this.seqName;
	}

	public void setSeqName(String seqName) {
		this.seqName = seqName;
	}

	@javax.persistence.Column(name = "seqValue")
	public String getSeqValue() {
		return this.seqValue;
	}

	public void setSeqValue(String seqValue) {
		this.seqValue = seqValue;
	}

}