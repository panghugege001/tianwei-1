package dfh.utils.validator;

import dfh.model.TokenInfo;
import org.joda.time.LocalDateTime;
 
import java.util.Date;

/**
 * Created by mars on 2016/7/12.
 */
public class EBetAppTokenValidatorImpl implements TokenValidator {

    private static final int EBET_APP_TOKEN_MAX_DAY = 5;
    private static final int EBET_WEB_TOKEN_MAX_DAY = 4;

    @Override
    public boolean isValid(TokenInfo token) {
        LocalDateTime tokenTime = LocalDateTime.fromDateFields(token.getTimestamp());
        if (tokenTime.plusDays(EBET_APP_TOKEN_MAX_DAY).isBefore(LocalDateTime.fromDateFields(new Date()))) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean isValidWeb(TokenInfo token) {
        LocalDateTime tokenTime = LocalDateTime.fromDateFields(token.getTimestamp());
        if (tokenTime.plusDays(EBET_WEB_TOKEN_MAX_DAY).isBefore(LocalDateTime.fromDateFields(new Date()))) {
            return false;
        }
        return true;
    }
}
