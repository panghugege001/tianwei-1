package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cpu_compare", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class CpuCompare implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String username;
	private String ip;
	private String iesnarecpu;
	private String ourcpu;
	private Date createTime;
	private String remark;
	
	
	public CpuCompare(){}
	
	public CpuCompare(String username, String ip, String iesnarecpu, String ourcpu, Date createTime, String remark){
		this.username = username;
		this.ip = ip;
		this.iesnarecpu = iesnarecpu;
		this.ourcpu = ourcpu;
		this.createTime = createTime;
		this.remark = remark;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "username")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "ip")
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Column(name = "iesnarecpu")
	public String getIesnarecpu() {
		return iesnarecpu;
	}
	public void setIesnarecpu(String iesnarecpu) {
		this.iesnarecpu = iesnarecpu;
	}
	
	@Column(name = "ourcpu")
	public String getOurcpu() {
		return ourcpu;
	}
	public void setOurcpu(String ourcpu) {
		this.ourcpu = ourcpu;
	}
	
	@Column(name = "createTime")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
