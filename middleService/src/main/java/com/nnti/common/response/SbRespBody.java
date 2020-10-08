package com.nnti.common.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SbRespBody implements Serializable {

    private static final long serialVersionUID = 1432805153640954503L;

    private String error_code;

    private String message;

    @JsonProperty
    private Object Data;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object Data) {
        this.Data = Data;
    }
}
