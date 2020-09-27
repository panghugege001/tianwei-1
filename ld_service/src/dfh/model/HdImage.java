package dfh.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Guestbook entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "hd_image", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class HdImage implements java.io.Serializable {

	// Fields

	private Integer id;
	private String imageName;
	private String image001;
	private String image002;
	private String image003;
	private String image004;
	private Integer imageStatus;
	private String imageIp;
	private Date createdate;
	private Date imageStart;
	private Date imageEnd;
	private Integer imageType;
	private String remark;
	

	// Constructors

	/** default constructor */
	public HdImage() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@javax.persistence.Column(name = "imageName")
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@javax.persistence.Column(name = "image001")
	public String getImage001() {
		return image001;
	}

	public void setImage001(String image001) {
		this.image001 = image001;
	}

	@javax.persistence.Column(name = "image002")
	public String getImage002() {
		return image002;
	}

	public void setImage002(String image002) {
		this.image002 = image002;
	}

	@javax.persistence.Column(name = "image003")
	public String getImage003() {
		return image003;
	}

	public void setImage003(String image003) {
		this.image003 = image003;
	}

	@javax.persistence.Column(name = "imageStatus")
	public Integer getImageStatus() {
		return imageStatus;
	}

	
	public void setImageStatus(Integer imageStatus) {
		this.imageStatus = imageStatus;
	}

	@javax.persistence.Column(name = "imageIp")
	public String getImageIp() {
		return imageIp;
	}

	public void setImageIp(String imageIp) {
		this.imageIp = imageIp;
	}

	@javax.persistence.Column(name = "createdate")
	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	@javax.persistence.Column(name = "image004")
	public String getImage004() {
		return image004;
	}

	public void setImage004(String image004) {
		this.image004 = image004;
	}

	@javax.persistence.Column(name = "imageStart")
	public Date getImageStart() {
		return imageStart;
	}

	public void setImageStart(Date imageStart) {
		this.imageStart = imageStart;
	}

	@javax.persistence.Column(name = "imageEnd")
	public Date getImageEnd() {
		return imageEnd;
	}

	public void setImageEnd(Date imageEnd) {
		this.imageEnd = imageEnd;
	}

	@javax.persistence.Column(name = "imageType")
	public Integer getImageType() {
		return imageType;
	}

	public void setImageType(Integer imageType) {
		this.imageType = imageType;
	}

	@javax.persistence.Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
	
}