package dfh.utils.validator;

import dfh.utils.NTwoUtil; 

/**
 * Created by mars on 2016/7/12.
 */
public class TokenValidatorFactory {

    TokenValidator defaultValidator;
    TokenValidator eBetAppValidator;
    TokenValidator nTwoAppValidator;

    public TokenValidator getvalidator(String platform) {
        if ("ebetapp".equals(platform)) {
            return eBetAppValidator;
        }
        if (NTwoUtil.PLATFORM.equals(platform)) {
            return nTwoAppValidator;
        }
        return defaultValidator;
    }

    public void setDefaultValidator(TokenValidator defaultValidator) {
        this.defaultValidator = defaultValidator;
    }

	public TokenValidator geteBetAppValidator() {
		return eBetAppValidator;
	}

	public void seteBetAppValidator(TokenValidator eBetAppValidator) {
		this.eBetAppValidator = eBetAppValidator;
	}

	public void setnTwoAppValidator(TokenValidator nTwoAppValidator) {
		this.nTwoAppValidator = nTwoAppValidator;
	}
    
    
}
