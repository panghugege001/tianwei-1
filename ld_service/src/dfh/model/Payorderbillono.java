package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Payorder entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="payorderbillno",catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Payorderbillono  implements java.io.Serializable {


    // Fields    

     private String billno;
     private String payplatform;
     private Double money;
     private String loginname;
     private String remark;
     
    @Id
 	@Column(name="billno")
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	
	
	@Column(name="payplatform")
	public String getPayplatform() {
		return payplatform;
	}
	public void setPayplatform(String payplatform) {
		this.payplatform = payplatform;
	}
	@Column(name="money")
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	
	@Column(name="loginname")
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
   
     


}