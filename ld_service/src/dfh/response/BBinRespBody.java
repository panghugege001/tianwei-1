package dfh.response;

import java.io.Serializable;

public class BBinRespBody<T> implements Serializable {

	private static final long serialVersionUID = 3947621060233559497L;

	private boolean result;

	private T data;


	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}

