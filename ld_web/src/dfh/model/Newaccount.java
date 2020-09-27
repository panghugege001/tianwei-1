package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Newaccount entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="newaccount",catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Newaccount  implements java.io.Serializable {


    // Fields    

     private String pno;
     private String title;
     private String loginname;
     private String pwd;
     private String phone;
     private String email;
     private String aliasname;
     private String remark;


    // Constructors

    /** default constructor */
    public Newaccount() {
    }

	/** minimal constructor */
    public Newaccount(String pno, String title, String loginname, String pwd) {
        this.pno = pno;
        this.title = title;
        this.loginname = loginname;
        this.pwd = pwd;
    }
    
    /** full constructor */
    public Newaccount(String pno, String title, String loginname, String pwd, String phone, String email, String aliasname, String remark) {
        this.pno = pno;
        this.title = title;
        this.loginname = loginname;
        this.pwd = pwd;
        this.phone = phone;
        this.email = email;
        this.aliasname = aliasname;
        this.remark = remark;
    }

   
    // Property accessors
    @Id 
    
@javax.persistence.Column(name = "pno")

    public String getPno() {
        return this.pno;
    }
    
    public void setPno(String pno) {
        this.pno = pno;
    }
    
@javax.persistence.Column(name = "title")

    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
@javax.persistence.Column(name = "loginname")

    public String getLoginname() {
        return this.loginname;
    }
    
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
    
@javax.persistence.Column(name = "pwd")

    public String getPwd() {
        return this.pwd;
    }
    
    public void setPwd(String pwd) {
        this.pwd = pwd;
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
    
@javax.persistence.Column(name = "aliasname")

    public String getAliasname() {
        return this.aliasname;
    }
    
    public void setAliasname(String aliasname) {
        this.aliasname = aliasname;
    }
    
@javax.persistence.Column(name = "remark")

    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
   








}