package com.nnti.common.model.dto;

public class MessageDTO {
	
	// 返回信息编码
	private String code;
	// 返回信息数据
	private Object data;
	// 返回信息内容
	private String message;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}