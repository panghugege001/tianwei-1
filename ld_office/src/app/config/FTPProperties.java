package app.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class FTPProperties {

	private static Object lock = new Object();
	private static FTPProperties config = null;
	private static ResourceBundle rb = null;
	private static final String CONFIG_FILE = "ftp";

	private FTPProperties() {

		rb = ResourceBundle.getBundle(CONFIG_FILE);
	}

	public static FTPProperties getInstance() {

		if (null == config) {

			synchronized (lock) {

				if (null == config) {

					config = new FTPProperties();
				}
			}
		}

		return config;
	}

	public String getValue(String key) throws MissingResourceException {
		
		return rb.getString(key);
	}
	
	public static void main(String [] args) {
		
		System.out.println(FTPProperties.getInstance().getValue("topic_image_upload_url"));
	}
	
}