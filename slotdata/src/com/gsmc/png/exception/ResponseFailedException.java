package com.gsmc.png.exception;

public class ResponseFailedException extends GenericDfhException {

	private static final long serialVersionUID = 1L;

	public ResponseFailedException() {
		super("fail to get the http response,it's sucessful to send http request");
	}

	public ResponseFailedException(String error) {
		super("fail to get the http response,it's sucessful to send http request;" + error);
	}
}
