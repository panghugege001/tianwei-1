package com.nnti.office.model.i18n;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class I18n {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * key
	 */
	private String key;
	
	/**
	 * value to the local enviroment
	 */
	private String value;
	
	/**
	 * chinese value
	 */
	private String cnVal;
	/**
	 * english value
	 */
	private String enVal;
	/**
	 * create time
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getCnVal() {
		return cnVal;
	}
	public void setCnVal(String cnVal) {
		this.cnVal = cnVal;
	}
	public String getEnVal() {
		return enVal;
	}
	public void setEnVal(String enVal) {
		this.enVal = enVal;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "I18n [id=" + id + ", key=" + key + ", value=" + value + ", cnVal=" + cnVal + ", enVal=" + enVal + ", createTime=" + createTime + "]";
	}
	
}
