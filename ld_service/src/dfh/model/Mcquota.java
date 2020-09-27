package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mcquota", catalog = "tianwei")
public class Mcquota implements java.io.Serializable {

	// Fields
	private Integer id;
	private Double amount;
	private Integer status;
	private String loginname;
	private Date createtime;
	
	
	public Mcquota(Integer id, Double amount, Integer status,String loginname,Date createtime) {
		this.id = id;
		this.amount = amount;
		this.status = status;
		this.loginname = loginname;
		this.createtime = createtime;
	}
	
	
	public Mcquota() {
		
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
	
	
	@javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return amount;
	}
	
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	@javax.persistence.Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}

	@javax.persistence.Column(name = "loginname")

	public String getLoginname() {
		return loginname;
	}


	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}


	@javax.persistence.Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	



	
	
	
}