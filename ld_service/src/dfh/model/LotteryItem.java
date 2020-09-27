package dfh.model;

import java.util.Date;  

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Transfer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lottery_item", catalog = "tianwei")

@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class LotteryItem implements java.io.Serializable {

	// Fields

	public LotteryItem(Long id, String type, String itemName, Double percent, int period) {
		super();
		this.id = id;
		this.type = type;
		this.itemName = itemName;
		this.percent = percent;
		this.period = period;
	}

	private static final long serialVersionUID = 1L;
	private Long id;
	private String type;
	private String itemName;
	private Double percent;
	private int period;

	// Constructors
	
	
	/** default constructor */
	public LotteryItem() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@javax.persistence.Column(name = "id")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	@javax.persistence.Column(name = "type")
	public String getType() {
		return type;
	}
	@javax.persistence.Column(name = "item_name")
	public String getItemName() {
		return itemName;
	}

	@javax.persistence.Column(name = "percent")
	public Double getPercent() {
		return percent;
	}

	@javax.persistence.Column(name = "period")
	public int getPeriod() {
		return period;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public void setPercent(Double percent) {
		this.percent = percent;
	}


	public void setPeriod(int period) {
		this.period = period;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "LotteryItem [id=" + id + ", type=" + type + ", itemName=" + itemName + ", percent=" + percent
				+ ", period=" + period + "]";
	}

	
}