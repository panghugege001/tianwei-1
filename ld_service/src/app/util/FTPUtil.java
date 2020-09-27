package app.util;

import java.util.ResourceBundle;

public class FTPUtil {

	private static ResourceBundle rb = null;
	private static final String CONFIG_FILE_NAME = "ftp";
	
	static {
	
		rb = ResourceBundle.getBundle(CONFIG_FILE_NAME);
	}
	
	private FTPUtil() {}

	public static String getValue(String key) {
		
		return rb.getString(key);
	}
	
	public static void main(String [] args) {
	
		System.out.println(FTPUtil.getValue("preferential_image_download_url"));
	}
	
}