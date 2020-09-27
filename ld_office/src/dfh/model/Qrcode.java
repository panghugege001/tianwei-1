package dfh.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 */
@Entity
@Table(name="qrcode"
    ,catalog="tianwei"
)

@org.hibernate.annotations.Entity(dynamicUpdate=true,dynamicInsert=true)
public class Qrcode implements java.io.Serializable {


    // Fields    
	private static final long serialVersionUID = 1L;
	
     private String agent;
     private String recommendCode;
     private String address;
 	 private int id;
 	 private  int type;//保留字段,暂时不用
	private String updateoperator; // 修改人
	private  String remark;
	private  String qrcode;



	@javax.persistence.Column(name = "type")
	public int getType() {
		return type;
	}



	public void setType(int type) {
		this.type = type;
	}
	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** default constructor */
    public Qrcode() {
    }
	public Qrcode(String agent, String recommendCode, String address, String updateoperator, String remark, String qrcode) {
		this.agent = agent;
		this.recommendCode = recommendCode;
		this.address = address;
		this.updateoperator = updateoperator;
		this.remark = remark;
		this.qrcode = qrcode;
	}
	@javax.persistence.Column(name = "qrcode")
	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	@javax.persistence.Column(name = "agent")
    public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
	}
	@javax.persistence.Column(name = "recommendCode")
	public String getRecommendCode() {
		return recommendCode;
	}
	public void setRecommendCode(String recommendCode) {
		this.recommendCode = recommendCode;
	}
	
	@javax.persistence.Column(name = "address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@javax.persistence.Column(name = "id")
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@javax.persistence.Column(name = "updateoperator")
	public String getUpdateoperator() {
		return updateoperator;
	}
	public void setUpdateoperator(String updateoperator) {
		this.updateoperator = updateoperator;
	}

	
	
}