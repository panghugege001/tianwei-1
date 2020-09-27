package com.nnti.office.util;

public class StringUtil {
	
	/**
	 * 判断str是否为null或者是否为""
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if(str == null || str.equals("")) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 判断str是否非空
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if(isEmpty(str)) {
			return false;
		}else {
			return true;
		}
	}
	
}
