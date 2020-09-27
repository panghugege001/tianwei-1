package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Remoteurls entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="remoteurls",catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Remoteurls  implements java.io.Serializable {


    // Fields    

     private String url;
     private Integer flag;
     private Timestamp updatetime;


    // Constructors

    /** default constructor */
    public Remoteurls() {
    }

    
    /** full constructor */
    public Remoteurls(String url, Integer flag, Timestamp updatetime) {
        this.url = url;
        this.flag = flag;
        this.updatetime = updatetime;
    }

   
    // Property accessors
    @Id 
    
@javax.persistence.Column(name = "url")

    public String getUrl() {
        return this.url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
@javax.persistence.Column(name = "flag")

    public Integer getFlag() {
        return this.flag;
    }
    
    public void setFlag(Integer flag) {
        this.flag = flag;
    }
    
@javax.persistence.Column(name = "updatetime")

    public Timestamp getUpdatetime() {
        return this.updatetime;
    }
    
    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }
   








}