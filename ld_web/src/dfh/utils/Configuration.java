package dfh.utils;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;


 
public class Configuration {
	

	private static Object lock              = new Object();
	private static Configuration config     = null;
	private static ResourceBundle rb        = null;
	private static final String CONFIG_FILE = "config";
	
	private Configuration() {
		rb = ResourceBundle.getBundle(CONFIG_FILE,Locale.CHINA);
	}
	
	public static Configuration getInstance() {
		if(null == config) {
			synchronized(lock) {
				if(null == config) {
					config = new Configuration();
				}
			}
		}
		return (config);
	}
	
	public String getValue(String key) {
		return (rb.getString(key));
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String areaListString=Configuration.getInstance().getValue("denyLoginArea");
		System.out.println(new String(areaListString.getBytes("iso-8859-1"),"utf-8"));
	}
}
