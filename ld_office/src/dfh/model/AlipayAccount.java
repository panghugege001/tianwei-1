package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Const entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="user_alipay_account",catalog="tianwei"
)
public class AlipayAccount  implements java.io.Serializable {

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String alipayAccount;
     private String loginname;
     private String realname;
     private Integer disable;
     private Date createtime;
     private Date updatetime;
     private String remark;
     
     public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public AlipayAccount() {
 		super();
 	}

    public AlipayAccount(String alipayAccount, String loginname, Integer disable, Date createtime,String remark) {
		super();
		this.alipayAccount = alipayAccount;
		this.loginname = loginname;
		this.disable = disable;
		this.createtime = createtime;
		this.remark = remark;
	}

	@Id
    @GeneratedValue(strategy=IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="alipay_account")
	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getDisable() {
		return disable;
	}

	public void setDisable(Integer disable) {
		this.disable = disable;
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
    
}