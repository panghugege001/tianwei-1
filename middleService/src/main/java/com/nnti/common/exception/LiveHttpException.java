package com.nnti.common.exception;

public class LiveHttpException extends LiveBaseException {

	private static final long serialVersionUID = 8001436140151531739L;

	public LiveHttpException(String code, String desc, String message,String api, Object params, Object response) {
        super(code,desc, message, api, params, response);
    }

}
