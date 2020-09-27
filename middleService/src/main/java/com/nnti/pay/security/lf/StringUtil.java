package com.nnti.pay.security.lf;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by wander on 2017/4/28.
 */
public class StringUtil  extends StringUtils {

    public static boolean areNotEmpty(String[] values) {
        boolean result = true;
        if ((values == null) || (values.length == 0))
            result = false;
        else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

}
