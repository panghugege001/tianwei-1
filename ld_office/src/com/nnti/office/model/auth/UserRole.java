package com.nnti.office.model.auth;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UserRole {
	private Integer id;
	private String username;
	private Integer roleId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "UserRole [id=" + id + ", username=" + username + ", roleId=" + roleId + ", createTime=" + createTime + "]";
	}
}
