package dfh.utils.validator;

import dfh.model.TokenInfo;
import org.joda.time.LocalDateTime;

import java.util.Date;
 
/**
 * Created by mars on 2016/7/12.
 */
public class NTwoAppTokenValidatorImpl implements TokenValidator {

    private static final int NTWO_APP_TOKEN_MAX_SECONDS = 60*2;

    @Override
    public boolean isValid(TokenInfo token) {
        LocalDateTime tokenTime = LocalDateTime.fromDateFields(token.getTimestamp());
        if (tokenTime.plusSeconds(NTWO_APP_TOKEN_MAX_SECONDS).isBefore(LocalDateTime.fromDateFields(new Date()))) {
            return false;
        }
        return true;
    }
    
    @Override
	public boolean isValidWeb(TokenInfo token) {
		return false;
	}
}
