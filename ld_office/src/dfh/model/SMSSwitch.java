package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户定制短信发送开关。
 */
@Entity
@Table(name = "smsswitch", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class SMSSwitch implements java.io.Serializable {

	// Fields
	private Integer id;
	private String type;
	private String title;
//	private String content;
	private Integer minvalue;
	private String disable;
	private String remark;


	/** default constructor */
	public SMSSwitch() {
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/*public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}*/

	public Integer getMinvalue() {
		return minvalue;
	}

	public void setMinvalue(Integer minvalue) {
		this.minvalue = minvalue;
	}

	public String getDisable() {
		return disable;
	}
	
	public void setDisable(String disable) {
		this.disable = disable;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


}