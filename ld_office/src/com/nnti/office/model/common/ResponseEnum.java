package com.nnti.office.model.common;

public enum ResponseEnum {
    SUCCESS("0000", "成功"),
    PARAMETER_MISSING("0100", "必填参数不能为空"),
    PARAMETER_WRONG("0101", "参数有误"),
    SERVER_ERROR("0200", "服务器抛出异常"),
    DUPLICATED_REQUEST("0300", "重复请求"),
    DUPLICATED_VALUE("0301", "重复的值");
    
    private String code;
    private String desc;

    private ResponseEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
    
}
