package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Const entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="agent_address",catalog="tianwei"
)
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class AgentAddress  implements java.io.Serializable {


    // Fields    

     private String address;
     private String loginname;
     private Integer deleteflag;
     private Date createtime;
     


    @Id
    @javax.persistence.Column(name = "address")
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getLoginname() {
		return loginname;
	}


	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}


	public Integer getDeleteflag() {
		return deleteflag;
	}


	public void setDeleteflag(Integer deleteflag) {
		this.deleteflag = deleteflag;
	}


	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

   
}