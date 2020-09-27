package dfh.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Const entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="const",catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Const  implements java.io.Serializable {


    // Fields    

     private String id;
     private String value;


    // Constructors

    /** default constructor */
    public Const() {
    }

    
    /** full constructor */
    public Const(String id, String value) {
        this.id = id;
        this.value = value;
    }

   
    // Property accessors
    @Id 
    
@javax.persistence.Column(name = "id")

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
@javax.persistence.Column(name = "value")

    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }


	@Override
	public String toString() {
		return "Const [id=" + id + ", value=" + value + "]";
	}

}