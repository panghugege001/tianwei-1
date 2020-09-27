package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Const entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="call_config",catalog="tianwei"
)
@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class CallConfig  implements java.io.Serializable {


    // Fields    
     private Integer id;
     private String company;
     private String url;
     private String serviceid;
     private Integer flag;
     private Integer type;
     private Date createtime;
     private Date updatetime;
     
     @Id
     @javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getServiceid() {
		return serviceid;
	}
	public void setServiceid(String serviceid) {
		this.serviceid = serviceid;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
     
}