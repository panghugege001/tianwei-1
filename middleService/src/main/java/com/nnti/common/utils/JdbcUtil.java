package com.nnti.common.utils;

import java.util.ResourceBundle;

public class JdbcUtil {

	private static ResourceBundle rb = null;
	private static final String CONFIG_FILE_NAME = "jdbc";
	
	static {
		
		rb = ResourceBundle.getBundle(CONFIG_FILE_NAME);
	}

	private JdbcUtil() {
		
	}
	
	public static String getValue(String key) {
		
		return rb.getString(key);
	}
	
	public static void main(String [] args) {
		
		//System.out.println(SpecialEnvironmentConfig.decrypt(getValue("master.datasource.password")));
	}
}