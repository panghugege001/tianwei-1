package com.nnti.office.model.common;

import com.nnti.office.util.StringUtil;

/**
 * common response object
 * @author michael
 *
 */
public class ResponseData {
	
	private String code;
	private String desc;
	private Object data;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void setResponseEnum(ResponseEnum responseEnum) {
		this.code = responseEnum.getCode();
		this.desc = responseEnum.getDesc();
	}
	
	public void setResponseEnum(ResponseEnum responseEnum,Exception e) {
		this.code = responseEnum.getCode();
		this.desc = responseEnum.getDesc();
		if(StringUtil.isNotEmpty(e.getMessage())) {
			this.desc = this.desc + "," + e.getMessage(); 
		}
	}
	
	@Override
	public String toString() {
		return "Response [code=" + code + ", desc=" + desc + ", data=" + data + "]";
	}

}
