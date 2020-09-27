package dfh.model;

public class ReturnInfo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String code; // 返回编码
	private String msg; // 返回信息
	private Object data;// 返回数据

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
