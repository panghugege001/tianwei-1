/**
 * 
 */
package com.nnti.personal.model.vo;

import java.util.Date;



/**
 * TODO 圣诞节活动映射实体
 * 
 * @author Caesar 2017年12月7日下午3:38:41
 */
public class ChristmasActivities {
	/**
	 * 主键id
	 */
	private String id;
	/**
	 * 存放类型
	 */
	private String type;
	/**
	 * 最低转账金额
	 */
	private Double lowestMoney;
	/**
	 * 最高上限金额
	 */
	private Double highestMoney;
	/**
	 * 存送百分比
	 */
	private String percentage;
	/**
	 * 流水倍数
	 */
	private String multiple;
	/**
	 * 开始时间
	 */
	private Date beginTime;
	/**
	 * 结束时间
	 */
	private Date endTime;
	/**
	 * 创建人
	 */
	private String createUser;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改人
	 */
	private String updateUser;
	/**
	 * 修改时间
	 */
	private Date updateTime;
	/**
	 * 状态0默认有效1无效
	 */
	private Integer disabled;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the lowestMoney
	 */
	public Double getLowestMoney() {
		return lowestMoney;
	}
	/**
	 * @param lowestMoney the lowestMoney to set
	 */
	public void setLowestMoney(Double lowestMoney) {
		this.lowestMoney = lowestMoney;
	}
	/**
	 * @return the highestMoney
	 */
	public Double getHighestMoney() {
		return highestMoney;
	}
	/**
	 * @param highestMoney the highestMoney to set
	 */
	public void setHighestMoney(Double highestMoney) {
		this.highestMoney = highestMoney;
	}
	/**
	 * @return the percentage
	 */
	public String getPercentage() {
		return percentage;
	}
	/**
	 * @param percentage the percentage to set
	 */
	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	/**
	 * @return the multiple
	 */
	public String getMultiple() {
		return multiple;
	}
	/**
	 * @param multiple the multiple to set
	 */
	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	/**
	 * @return the beginTime
	 */
	public Date getBeginTime() {
		return beginTime;
	}
	/**
	 * @param beginTime the beginTime to set
	 */
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}
	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the updateUser
	 */
	public String getUpdateUser() {
		return updateUser;
	}
	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * @return the disabled
	 */
	public Integer getDisabled() {
		return disabled;
	}
	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	
}
