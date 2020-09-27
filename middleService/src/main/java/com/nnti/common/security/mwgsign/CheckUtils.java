package com.nnti.common.security.mwgsign;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class CheckUtils {

	public static final String COMMON_FIELD = "flowID,initiator,";

	/**
	 * 验�?�对象是?��为NULL,空�?�符串�?�空?��组�?�空??�Collection??�Map(?��??�空?��??��?�符串�?�认为是空串)
	 * 
	 * @param obj 被�?��?��?�对�?
	 * @param message 异常信息
	 */
	@SuppressWarnings("rawtypes")
	public static void notEmpty(Object obj, String message) {
		if (obj == null) {
			throw new IllegalArgumentException(message + " must be specified");
		}
		if (obj instanceof String && obj.toString().trim().length() == 0) {
			throw new IllegalArgumentException(message + " must be specified");
		}
		if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
			throw new IllegalArgumentException(message + " must be specified");
		}
		if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
			throw new IllegalArgumentException(message + " must be specified");
		}
		if (obj instanceof Map && ((Map) obj).isEmpty()) {
			throw new IllegalArgumentException(message + " must be specified");
		}
	}

}
