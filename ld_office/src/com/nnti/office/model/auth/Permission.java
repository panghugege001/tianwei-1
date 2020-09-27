package com.nnti.office.model.auth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Permission {
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * parent id
	 */
	private Integer pid;
	/**
	 * name of the permission
	 */
	private String name;
	/**
	 * "N":navigation menu "O":operation
	 */
	private String type;
	/**
	 * request url path
	 */
	private String url;
	
	/**
	 * code
	 */
	private String code;
	
	/**
	 * main menu icon
	 * resource: http://www.w3schools.com/icons/fontawesome_icons_webapp.asp
	 */
	private String icon;
	
	/**
	 * explanation of the permission
	 */
	private String description;
	
	/**
	 * the key for i18n reference to t_i18n
	 */
	private String i18nKey;
	
	/**
	 * time this permission is created
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	
	/**
	 * checked or ""
	 */
	private String checked;
	
	private List<Permission> subPermissonList;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public List<Permission> getSubPermissonList() {
		if(subPermissonList == null) {
			subPermissonList = new ArrayList<Permission>();
		}
		return subPermissonList;
	}
	public void setSubPermissonList(List<Permission> subPermissonList) {
		this.subPermissonList = subPermissonList;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	
	public String getI18nKey() {
		return i18nKey;
	}
	public void setI18nKey(String i18nKey) {
		this.i18nKey = i18nKey;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if( !(obj instanceof Permission) ) {
			return false;
		}
		Permission permission  = (Permission)obj;
		return id.equals(permission.getId());
	}
	@Override
	public String toString() {
		return "Permission [id=" + id + ", pid=" + pid + ", name=" + name + ", type=" + type + ", url=" + url + ", code=" + code + ", icon=" + icon
				+ ", description=" + description + ", i18nKey=" + i18nKey + ", createTime=" + createTime + ", checked=" + checked
				+ ", subPermissonList=" + subPermissonList + "]";
	}
	
}
