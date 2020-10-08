package dfh.model.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BBinData implements Serializable {

	private static final long serialVersionUID = -6641021778906553154L;

	/**
	 * "Code":"编码",
	 */
	@JsonProperty(value = "sessionid")
	private String sessionid;

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	

	
}

