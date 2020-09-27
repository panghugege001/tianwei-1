package dfh.utils.validator;

import dfh.model.TokenInfo;
import org.joda.time.LocalDateTime; 

import java.util.Date;

/**
 * Created by mars on 2016/7/12.
 */
public class DefaultTokenValidatorImpl implements TokenValidator {

    private static final int APP_TOKEN_MAX_MIN = 20;

    @Override
    public boolean isValid(TokenInfo token) {
        LocalDateTime tokenTime = LocalDateTime.fromDateFields(token.getTimestamp());
        if (tokenTime.plusMinutes(APP_TOKEN_MAX_MIN).isBefore(LocalDateTime.fromDateFields(new Date()))) {
            return false;
        }
        return true;
    }
    
    @Override
	public boolean isValidWeb(TokenInfo token) {
		return false;
	}
}
