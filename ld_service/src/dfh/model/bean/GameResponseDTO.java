package dfh.model.bean;

import java.io.Serializable;

public class GameResponseDTO implements Serializable {

	private static final long serialVersionUID = -7033046578977357036L;
	
	private String code;
    private String message;
    private String status;
    private Integer data;
    private String responseData;
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	public Integer getData() {
		return data;
	}
	public void setData(Integer data) {
		this.data = data;
	}
	
	
	@Override
	public String toString() {
		return "MessageDTO [code=" + code + ", message=" + message + ", status=" + status + ", data=" + data
				+ ", responseData=" + responseData + "]";
	}
	
	

}
