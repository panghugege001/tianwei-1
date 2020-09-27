package dfh.model.bean;

public class SmsInfoVo {
	private String phone;
	private String context;
	private String date;
	private String port;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public SmsInfoVo(String phone, String context, String date, String port) {
		super();
		this.phone = phone;
		this.context = context;
		this.date = date;
		this.port = port;
	}

	public SmsInfoVo() {
		super();
	}

}
