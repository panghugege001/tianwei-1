package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Const entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="deposit_wechat",catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class DepositWechat  implements java.io.Serializable {


    // Fields    

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String billno;
	 private Date paytime;
	 private Double amount;
	 private String wechat;
	 private String tailno;
     private Integer state;
     private Date dealtime;
     private String remark;
     private String username;

    // Constructors

    /** default constructor */
    public DepositWechat() {
    }

   
    // Property accessors
    @Id 
    @javax.persistence.Column(name = "billno")
    public String getBillno() {
        return this.billno;
    }
    
    public void setBillno(String billno) {
        this.billno = billno;
    }
    
    @javax.persistence.Column(name = "paytime")
    public Date getPaytime() {
        return this.paytime;
    }
    
    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

    @javax.persistence.Column(name = "amount")
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
    
	@javax.persistence.Column(name = "wechat")
    public String getWechat() {
        return this.wechat;
    }
    
    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
   
    @javax.persistence.Column(name = "tailno")
    public String getTailno() {
        return this.tailno;
    }
    
    public void setTailno(String tailno) {
        this.tailno = tailno;
    }

    @javax.persistence.Column(name = "state")
    public Integer getState() {
        return this.state;
    }
    
    public void setState(Integer state) {
        this.state = state;
    }
    
    @javax.persistence.Column(name = "dealtime")
    public Date getDealtime() {
        return this.dealtime;
    }
    
    public void setDealtime(Date dealtime) {
        this.dealtime = dealtime;
    }
    
    @javax.persistence.Column(name = "remark")
    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @javax.persistence.Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}