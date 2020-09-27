package dfh.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * jc data entity
 * @author 
 *
 */
@Entity
@Table(name = "jc_data", catalog = "tianwei")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class JCProfitData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String playerName;
	private Date startTime;
	private Date endTime;
	private Double actual;
	private Double bonus;
	private Double win;
	private Double orderSum;
	private Date createTime;
	private Date updateTime;
	private Integer vipLevel;
	
	@Override
	public boolean equals(Object obj) {
		String sname = null;
		JCProfitData jc = null;
		if (obj instanceof String){
			sname = obj.toString();
			if (this.playerName.equals(sname)){
				return true;
			}
		} else if (obj instanceof JCProfitData){
			jc = (JCProfitData) obj;
			//TODO 定义对象对比方式
		}
		return super.equals(obj);
	}
	
	public JCProfitData() {
		super();
	}
	
	public JCProfitData(String playerName, Date startTime,
			Date endTime, Double actual, Double win, Date createTime,
			Date updateTime, int vipLevel) {
		super();
		this.playerName = playerName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.actual = actual;
		this.win = win;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.vipLevel = vipLevel;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@javax.persistence.Column(name = "id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@javax.persistence.Column(name = "PLAYERNAME")
	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	@javax.persistence.Column(name = "STARTTIME")
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@javax.persistence.Column(name = "ENDTIME")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@javax.persistence.Column(name = "ACTUAL")
	public Double getActual() {
		return actual;
	}
	public void setActual(Double actual) {
		this.actual = actual;
	}
	@javax.persistence.Column(name = "BONUS")
	public Double getBonus() {
		return bonus;
	}
	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}
	@javax.persistence.Column(name = "WIN")
	public Double getWin() {
		return win;
	}
	public void setWin(Double win) {
		this.win = win;
	}
	@javax.persistence.Column(name = "ORDERSUM")
	public Double getOrderSum() {
		return orderSum;
	}
	public void setOrderSum(Double orderSum) {
		this.orderSum = orderSum;
	}
	@javax.persistence.Column(name = "CREATETIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@javax.persistence.Column(name = "UPDATETIME")
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@javax.persistence.Column(name = "VIPLEVEL")
	public Integer getVipLevel() {
		return vipLevel;
	}
	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	} 
}
