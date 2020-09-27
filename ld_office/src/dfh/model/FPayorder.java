package dfh.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * Payorder entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="fpayorder" ,catalog = "tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class FPayorder  implements java.io.Serializable {


    // Fields    
     private String pno;
     private Double amout;
     private String bankname;
     private String card_number;
     private String billno;
     private String loginname;
     private String accountName;
     private String bankAddress;
     private String phone;
     private Date createTime;
     private Date updateTime;
     private Integer flag;


    /** default constructor */
    public FPayorder() {
    }
    
    @Id 
    @javax.persistence.Column(name = "pno")
    public String getPno() {
		return pno;
	}



	public void setPno(String pno) {
		this.pno = pno;
	}
	
	@javax.persistence.Column(name = "bankname")
    public String getBankname() {
		return bankname;
	}



	public void setBankname(String bankname) {
		this.bankname = bankname;
	}



	@javax.persistence.Column(name = "amout")
	public Double getAmout() {
		return amout;
	}



	public void setAmout(Double amout) {
		this.amout = amout;
	}


    @javax.persistence.Column(name = "card_number")
	public String getCard_number() {
		return card_number;
	}



	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	@javax.persistence.Column(name = "billno")

    public String getBillno() {
        return this.billno;
    }

	public void setBillno(String billno) {
        this.billno = billno;
    }
    
@javax.persistence.Column(name = "loginname")

    public String getLoginname() {
        return this.loginname;
    }
    
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
    

    
@javax.persistence.Column(name = "createTime")

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
@javax.persistence.Column(name = "flag")

    public Integer getFlag() {
        return this.flag;
    }
    
    public void setFlag(Integer flag) {
        this.flag = flag;
    }


    @javax.persistence.Column(name = "accountName")
	public String getAccountName() {
		return accountName;
	}



	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	 @javax.persistence.Column(name = "bankAddress")
	public String getBankAddress() {
		return bankAddress;
	}



	public void setBankAddress(String bankAddress) {
		this.bankAddress = bankAddress;
	}


	 @javax.persistence.Column(name = "phone")
	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}

	@javax.persistence.Column(name = "updateTime")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	
}