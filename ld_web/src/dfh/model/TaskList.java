package dfh.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Const entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "task_list", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class TaskList implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer type;
	private String title;
	private Double giftmoney;
	private Double requireData;
	private Integer disable;
	private Date createtime;
	private Date updatetime;

	public TaskList() {
		super();
	}


	public TaskList(Integer id, Integer type, String title, Double giftmoney, Double requireData, Integer disable,
			Date createtime, Date updatetime) {
		super();
		this.id = id;
		this.type = type;
		this.title = title;
		this.giftmoney = giftmoney;
		this.requireData = requireData;
		this.disable = disable;
		this.createtime = createtime;
		this.updatetime = updatetime;
	}


	@Id
	@GeneratedValue(strategy = IDENTITY)
	@javax.persistence.Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getGiftmoney() {
		return giftmoney;
	}

	public void setGiftmoney(Double giftmoney) {
		this.giftmoney = giftmoney;
	}

	public Double getRequireData() {
		return requireData;
	}


	public void setRequireData(Double requireData) {
		this.requireData = requireData;
	}


	public Integer getDisable() {
		return disable;
	}

	public void setDisable(Integer disable) {
		this.disable = disable;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

}