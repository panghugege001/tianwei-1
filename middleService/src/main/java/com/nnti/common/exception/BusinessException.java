package com.nnti.common.exception;

import com.nnti.common.constants.ErrorCode;

public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;

    // 错误编码
    private String code;
    // 错误信息
    private String message;

    public BusinessException() {

        super();
    }

    public BusinessException(String code, String message) {

        super();

        this.code = code;
        this.message = message;
    }

    public BusinessException(ErrorCode errorEnum) {
        this.code = errorEnum.getCode();
        this.message = errorEnum.getType();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toString() {
        return "[BusinessException] code = " + this.code + ", message = " + this.message;
    }
}