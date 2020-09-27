package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import dfh.utils.AESUtil;


@Entity
@Table(name = "other_recordmail", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class RecordMail implements java.io.Serializable{
	
	private Integer id;
	private String email;
	private Integer status;
	private Timestamp createtime;
	private String remark;
	

	public RecordMail() {
		super();
	}
	
	public RecordMail(String email,Integer status,Timestamp createtime,String remark) {
		this.email=email;
		this.status=status;
		this.createtime=createtime;
		this.remark=remark;
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

	
	@javax.persistence.Column(name = "email")
    public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
		this.email = email;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@javax.persistence.Column(name = "createtime")
	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}
	@javax.persistence.Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
}
