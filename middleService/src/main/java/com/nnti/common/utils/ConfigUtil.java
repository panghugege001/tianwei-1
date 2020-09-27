package com.nnti.common.utils;

import java.util.ResourceBundle;

public class ConfigUtil {
	
	private static ResourceBundle rb = null;
	private static final String CONFIG_FILE_NAME = "config";
	
	static {
		
		rb = ResourceBundle.getBundle(CONFIG_FILE_NAME);
	}

	private ConfigUtil() {
		
	}
	
	public static String getValue(String key) {
		
		return rb.getString(key);
	}
	
	public static void main(String [] args) {
		
		System.out.println(getValue("YL_PHP_PT_SERVICE"));
	}
}