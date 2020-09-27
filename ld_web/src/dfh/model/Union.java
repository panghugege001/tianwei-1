package dfh.model;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Accountinfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "union", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class Union implements java.io.Serializable {

	// Fields

	private int id;
	private String president;
	private int level;
	private Date createTime;
	private int max;
	private int rank;
	private int nums;
	@Column(name="nums")
	public int getNums() {
		return nums;
	}

	public void setNums(int nums) {
		this.nums = nums;
	}
	@Column(name="rank")
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	private String remark;

	// Constructors

	/** default constructor */
	public Union() {
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name="id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Column(name="president")
	public String getPresident() {
		return president;
	}

	public void setPresident(String president) {
		this.president = president;
	}
	@Column(name="level")
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	@Column(name="createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name="max")
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}