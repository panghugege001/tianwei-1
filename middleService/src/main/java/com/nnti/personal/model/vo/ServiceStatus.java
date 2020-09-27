package com.nnti.personal.model.vo;

public class ServiceStatus {

	private String name;
	private Integer status;
	private String message;
	private String exceptionMsg;
	private Long execMs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public Long getExecMs() {
		return execMs;
	}

	public void setExecMs(Long execMs) {
		this.execMs = execMs;
	}

}
