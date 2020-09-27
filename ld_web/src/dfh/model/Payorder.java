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
@Table(name="payorder",catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Payorder  implements java.io.Serializable {


    // Fields    

     private String billno;
     private String payPlatform;
     private Double money;
     private Integer newaccount;
     private String password;
     private String loginname;
     private String aliasName;
     private String phone;
     private String email;
     private String partner;
     private String attach;
     private String msg;
     private String md5info;
     private Date returnTime;
     private String ip;
     private Date createTime;
     private Integer flag;
     private Integer type;
     //用于axis2对date不支持
     private String tempCreateTime;
    // Constructors

    /** default constructor */
    public Payorder() {
    }

	/** minimal constructor */
    public Payorder(String billno, String payPlatform, Double money, Integer newaccount, String loginname, String ip, Timestamp createTime, Integer flag) {
        this.billno = billno;
        this.payPlatform = payPlatform;
        this.money = money;
        this.newaccount = newaccount;
        this.loginname = loginname;
        this.ip = ip;
        this.createTime = createTime;
        this.flag = flag;
    }
    
    /** full constructor */
	public Payorder(String billno, String payPlatform, Double money, Integer newaccount, String password, String loginname, String aliasName, String phone, String email, String partner, String attach, String msg, String md5info, Timestamp returnTime, String ip, Timestamp createTime, Integer flag) {
        this.billno = billno;
        this.payPlatform = payPlatform;
        this.money = money;
        this.newaccount = newaccount;
        this.password = password;
        this.loginname = loginname;
        this.aliasName = aliasName;
        this.phone = phone;
        this.email = email;
        this.partner = partner;
        this.attach = attach;
        this.msg = msg;
        this.md5info = md5info;
        this.returnTime = returnTime;
        this.ip = ip;
        this.createTime = createTime;
        this.flag = flag;
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
    
@javax.persistence.Column(name = "payPlatform")

    public String getPayPlatform() {
        return this.payPlatform;
    }
    
    public void setPayPlatform(String payPlatform) {
        this.payPlatform = payPlatform;
    }
    
@javax.persistence.Column(name = "money")

    public Double getMoney() {
        return this.money;
    }
    
    public void setMoney(Double money) {
        this.money = money;
    }
    
@javax.persistence.Column(name = "newaccount")

    public Integer getNewaccount() {
        return this.newaccount;
    }
    
    public void setNewaccount(Integer newaccount) {
        this.newaccount = newaccount;
    }
    
@javax.persistence.Column(name = "password")

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
@javax.persistence.Column(name = "loginname")

    public String getLoginname() {
        return this.loginname;
    }
    
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
    
@javax.persistence.Column(name = "aliasName")

    public String getAliasName() {
        return this.aliasName;
    }
    
    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }
    
@javax.persistence.Column(name = "phone")

    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
@javax.persistence.Column(name = "email")

    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
@javax.persistence.Column(name = "partner")

    public String getPartner() {
        return this.partner;
    }
    
    public void setPartner(String partner) {
        this.partner = partner;
    }
    
@javax.persistence.Column(name = "attach")

    public String getAttach() {
        return this.attach;
    }
    
    public void setAttach(String attach) {
        this.attach = attach;
    }
    
@javax.persistence.Column(name = "msg")

    public String getMsg() {
        return this.msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
    
@javax.persistence.Column(name = "md5info")

    public String getMd5info() {
        return this.md5info;
    }
    
    public void setMd5info(String md5info) {
        this.md5info = md5info;
    }
    
@javax.persistence.Column(name = "returnTime")

    public Date getReturnTime() {
        return this.returnTime;
    }
    
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }
    
@javax.persistence.Column(name = "ip")

    public String getIp() {
        return this.ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
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
    @Transient
	public String getTempCreateTime() {
		return tempCreateTime;
	}

	public void setTempCreateTime(String tempCreateTime) {
		this.tempCreateTime = tempCreateTime;
	}

	@javax.persistence.Column(name = "type")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
   








}