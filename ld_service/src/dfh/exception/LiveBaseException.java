package dfh.exception;

public class LiveBaseException extends Exception {
	private static final long serialVersionUID = 7965228488528263586L;
	protected String code;
	protected String desc;
	protected String message;
	protected Object params;
	protected Object response;
	protected String api;
	public static final String JSON_NULL = "JSON_NULL";
	public static final String TIME_OUT = "TIME_OUT";

	public LiveBaseException(String code, String desc, String message, String api, Object params, Object response) {
		this.code = code;
		this.desc = desc;
		this.message = message;
		this.response = response;
		this.params = params;
		this.api = api;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getParams() {
		return this.params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	public Object getResponse() {
		return this.response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public String getApi() {
		return this.api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMessage() {
		String m = "{code='" + this.code + '\'' + "desc='" + this.desc + '\'' + ", message='" + this.message + '\''
				+ ", params='" + this.params + '\'' + ", response='" + this.response + '\'' + ", api='" + this.api
				+ '\'' + '}';
		return m;
	}

	public String getMsg() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString() {
		return "LiveBaseException{code='" + this.code + '\'' + ", message='" + this.message + '\'' + ", params='"
				+ this.params + '\'' + ", response='" + this.response + '\'' + ", api='" + this.api + '\'' + '}';
	}
}
