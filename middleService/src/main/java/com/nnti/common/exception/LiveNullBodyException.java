package com.nnti.common.exception;

/**
 * 请求无反回值异常
 */
public class LiveNullBodyException extends LiveHttpException {

	private static final long serialVersionUID = 1L;

	public LiveNullBodyException(String api, Object params, Object response) {
		super(JSON_NULL, "请求无返回值", "网络连接超时，请稍后在试。", api, params, response);
	}
}