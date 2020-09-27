package dfh.mgs.vo.validateApiUser;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ValidateApiUserResponse", namespace="http://mgsops.net/AdminAPI_Admin")
public class ValidateApiUserResult {

	private ValidateApiUserResponse validateApiUserResult;
	
	@XmlElement(name = "ValidateApiUserResult")
	public ValidateApiUserResponse getValidateApiUserResult() {
		return validateApiUserResult;
	}

	public void setValidateApiUserResult(ValidateApiUserResponse validateApiUserResult) {
		this.validateApiUserResult = validateApiUserResult;
	}
	
}
