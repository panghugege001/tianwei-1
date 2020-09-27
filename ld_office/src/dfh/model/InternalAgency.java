package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Const entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="internal_agency",catalog="tianwei"
)
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class InternalAgency  implements java.io.Serializable {


    // Fields    

	private String loginname;
     private Integer type;
     private Date createtime;
     private String remark;

     
    public InternalAgency() {
		super();
	}

	public InternalAgency(String loginname, Integer type, Date createtime, String remark) {
		super();
		this.loginname = loginname;
		this.type = type;
		this.createtime = createtime;
		this.remark = remark;
	}
	@Id
    @javax.persistence.Column(name = "loginname")
	public String getLoginname() {
		return loginname;
	}


	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


   
}