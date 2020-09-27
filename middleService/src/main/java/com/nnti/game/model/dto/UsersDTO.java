package com.nnti.game.model.dto;

import com.nnti.common.model.dto.CommonDTO;

/**
 * Users entity. @author boots
 */
public class UsersDTO extends CommonDTO{
	
	
	private String password;
	
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}