package dfh.mgs.vo.validateApiUser;

import javax.xml.bind.annotation.XmlElement;

public class ValidateApiUserResponse implements java.io.Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private OrionString[] claims;
    private Boolean isValid;
    private OrionInt[] validServerIds;

    public ValidateApiUserResponse() {
    }

    public ValidateApiUserResponse(OrionString[] claims, Boolean isValid, OrionInt[] validServerIds) {
           this.claims = claims;
           this.isValid = isValid;
           this.validServerIds = validServerIds;
    }

    @XmlElement(name = "Claims", namespace = "http://schemas.datacontract.org/2004/07/Orion.Contracts.VanguardAdmin.DataStructures", type=OrionString.class)
	public OrionString[] getClaims() {
		return claims;
	}

	public void setClaims(OrionString[] claims) {
		this.claims = claims;
	}

	@XmlElement(name = "IsValid", namespace = "http://schemas.datacontract.org/2004/07/Orion.Contracts.VanguardAdmin.DataStructures")
	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	@XmlElement(name = "ValidServerIds", namespace = "http://schemas.datacontract.org/2004/07/Orion.Contracts.VanguardAdmin.DataStructures", type=OrionInt.class)
	public OrionInt[] getValidServerIds() {
		return validServerIds;
	}

	public void setValidServerIds(OrionInt[] validServerIds) {
		this.validServerIds = validServerIds;
	}
    
}