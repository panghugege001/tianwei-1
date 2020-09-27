package com.nnti.common.model.dto;

public class DataTransferDTO {
	
	// 最终响应的加密数据
	private String responseData;
	
	public DataTransferDTO(String responseData) {
		
		this.responseData = responseData;
	}
		
	public String getResponseData() {
		return responseData;
	}
	
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
}