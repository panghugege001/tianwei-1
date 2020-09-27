package com.nnti.common.utils;

import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import org.apache.log4j.Logger;

/**
 * Created by wander on 2017/1/25.
 */
public abstract class Assert {

    private static Logger log = Logger.getLogger(Assert.class);

    private Assert() {
    }

    public static void isTrue(boolean expression, String message) throws BusinessException {
        if (!expression) {
            throw new BusinessException(ErrorCode.SC_10001.getCode(), message);
        }
    }

    public static void isTrue(boolean expression) throws BusinessException {
        isTrue(expression, "这个表达式必须为真");
    }

    private static void notEmpty(Object object, String message) throws BusinessException {
        if (!MyUtils.isNotEmpty(object)) {
            throw new BusinessException(ErrorCode.SC_10001.getCode(), message);
        }
    }

    public static void notEmpty(Object... args) throws BusinessException {
        for (int i = 0; i < args.length; i++) {
            notEmpty(args[i], args.length + "个参数，第" + (i + 1) + " 个参数为【空】！");
        }
    }

    public static void state(boolean expression, String message) throws BusinessException {
        if (!expression) {
            throw new BusinessException(ErrorCode.SC_10001.getCode(), message);
        }
    }

    public static void state(boolean expression) throws BusinessException {
        state(expression, "这个状态不能为空");
    }

    public static void isNumeric(String succeed, String message) throws BusinessException {
        if (!NumericUtil.isNumeric(succeed)) {
            throw new BusinessException(ErrorCode.SC_30000_113.getCode(), message);
        }
    }

    public static void isNumeric(String succeed) throws BusinessException {
        notEmpty(succeed);
        isNumeric(succeed, ErrorCode.SC_30000_113.getType());
    }
    
    public static String getBankName(String mctype) throws BusinessException {
        if("tlzf".equals(mctype)){
        	return "通联转账";
        }
        else if("".equals(mctype)){
        	
        }
        return null;
    }
    
    public static final String[] levels = {"A","B","C","D","E","F","G","H","I","J","K","L","M"};
}
