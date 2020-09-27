package app.response;

/**
 * 
 * @author stan
 *
 */
public class ResponseBean {

	// 最终响应的加密数据
	private String responseData;
	
	public ResponseBean(String responseData) {
	
		this.responseData = responseData;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	
}