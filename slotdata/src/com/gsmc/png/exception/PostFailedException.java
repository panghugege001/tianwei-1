
package com.gsmc.png.exception;

public class PostFailedException extends GenericDfhException {

	private static final long serialVersionUID = 1L;

	public PostFailedException() {
		super("Post failed,please check the network connection");
	}

	public PostFailedException(String error) {
		super(error);
	}
}
