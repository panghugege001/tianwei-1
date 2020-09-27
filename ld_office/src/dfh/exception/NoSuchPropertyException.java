package dfh.exception;

public class NoSuchPropertyException extends RuntimeException {
	private static final long serialVersionUID = -5807374346006734137L;

	public NoSuchPropertyException() {
		super();
	}

	public NoSuchPropertyException(String msg) {
		super(msg);
	}

}
