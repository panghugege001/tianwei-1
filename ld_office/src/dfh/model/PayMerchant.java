package dfh.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Const entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="paymerchant",catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class PayMerchant  implements java.io.Serializable {


    // Fields    

     private String id;
     private String constid;
     private String merchantcode ;
     private Date createtime ;
     private Date updatetime ;
     private String updateby ;
     private String note ;
     private int type;


   
    // Property accessors
    @Id 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @javax.persistence.Column(name = "id")
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @javax.persistence.Column(name = "constid")
	public String getConstid() {
		return constid;
	}


	public void setConstid(String constid) {
		this.constid = constid;
	}

	 @javax.persistence.Column(name = "merchantcode")
	public String getMerchantcode() {
		return merchantcode;
	}


	public void setMerchantcode(String merchantcode) {
		this.merchantcode = merchantcode;
	}

	 @javax.persistence.Column(name = "createtime")
	public Date getCreatetime() {
		return createtime;
	}


	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	 @javax.persistence.Column(name = "updatetime")
	public Date getUpdatetime() {
		return updatetime;
	}


	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	 @javax.persistence.Column(name = "updateby")
	public String getUpdateby() {
		return updateby;
	}


	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

	 @javax.persistence.Column(name = "note")
	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}

	 @javax.persistence.Column(name = "type")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
   




}