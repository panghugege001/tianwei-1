package dfh.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Task entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="task",catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Task  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String pno;
     private String level;
     private Integer flag;
     private Timestamp agreeTime;
     private String agreeIp;
     private String manager;


    // Constructors

    /** default constructor */
    public Task() {
    }

	/** minimal constructor */
    public Task(String pno, String level, Integer flag) {
        this.pno = pno;
        this.level = level;
        this.flag = flag;
    }
    
    /** full constructor */
    public Task(String pno, String level, Integer flag, Timestamp agreeTime, String agreeIp, String manager) {
        this.pno = pno;
        this.level = level;
        this.flag = flag;
        this.agreeTime = agreeTime;
        this.agreeIp = agreeIp;
        this.manager = manager;
    }

   
    // Property accessors
    @Id @GeneratedValue(strategy=IDENTITY)
    
@javax.persistence.Column(name = "id")

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
@javax.persistence.Column(name = "pno")

    public String getPno() {
        return this.pno;
    }
    
    public void setPno(String pno) {
        this.pno = pno;
    }
    
@javax.persistence.Column(name = "level")

    public String getLevel() {
        return this.level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
@javax.persistence.Column(name = "flag")

    public Integer getFlag() {
        return this.flag;
    }
    
    public void setFlag(Integer flag) {
        this.flag = flag;
    }
    
@javax.persistence.Column(name = "agreeTime")

    public Timestamp getAgreeTime() {
        return this.agreeTime;
    }
    
    public void setAgreeTime(Timestamp agreeTime) {
        this.agreeTime = agreeTime;
    }
    
@javax.persistence.Column(name = "agreeIp")

    public String getAgreeIp() {
        return this.agreeIp;
    }
    
    public void setAgreeIp(String agreeIp) {
        this.agreeIp = agreeIp;
    }
    
@javax.persistence.Column(name = "manager")

    public String getManager() {
        return this.manager;
    }
    
    public void setManager(String manager) {
        this.manager = manager;
    }
   








}