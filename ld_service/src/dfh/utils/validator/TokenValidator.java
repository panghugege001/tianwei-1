package dfh.utils.validator;

import dfh.model.TokenInfo; 

/**
 * Created by mars on 2016/7/12.
 */
public interface TokenValidator {

    /**
     * 个自产品针对Token的验证器
     * .
     * @param token
     * @return
     */
    boolean isValid(TokenInfo token);
    
    /**
     * ebet web端用
     * */
    boolean isValidWeb(TokenInfo token);
}
