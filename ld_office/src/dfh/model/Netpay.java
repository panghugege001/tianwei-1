package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Netpay entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "netpay", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Netpay implements java.io.Serializable {

	// Fields

	private String name;
	private String merno;
	private String puturl;
	private Integer flag;
	private String code;
	private String key;
	private Integer next;
	private Integer type;
	private String email;

	// Constructors

	/** default constructor */
	public Netpay() {
	}

	/** minimal constructor */
	public Netpay(String name, String puturl, Integer flag) {
		this.name = name;
		this.puturl = puturl;
		this.flag = flag;
	}

	/** full constructor */
	public Netpay(String name, String merno, String puturl, Integer flag, String code, String key, Integer next, Integer type, String email) {
		this.name = name;
		this.merno = merno;
		this.puturl = puturl;
		this.flag = flag;
		this.code = code;
		this.key = key;
		this.next = next;
		this.type = type;
		this.email = email;
	}

	// Property accessors
	@Id
	@javax.persistence.Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@javax.persistence.Column(name = "merno")
	public String getMerno() {
		return this.merno;
	}

	public void setMerno(String merno) {
		this.merno = merno;
	}

	@javax.persistence.Column(name = "puturl")
	public String getPuturl() {
		return this.puturl;
	}

	public void setPuturl(String puturl) {
		this.puturl = puturl;
	}

	@javax.persistence.Column(name = "flag")
	public Integer getFlag() {
		return this.flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	@javax.persistence.Column(name = "code")
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@javax.persistence.Column(name = "key")
	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@javax.persistence.Column(name = "next")
	public Integer getNext() {
		return this.next;
	}

	public void setNext(Integer next) {
		this.next = next;
	}

	@javax.persistence.Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@javax.persistence.Column(name = "email")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}