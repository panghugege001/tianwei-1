package dfh.listener;

import java.io.IOException;
import java.util.Properties;


public class WebProperties {
	
	/**
	 * 读取Properties文件
	 * @return
	 */
	public static final Properties getProperties(){
		Properties prop = new Properties();
		try {
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("dfh/listener/webservice.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop;
	}
	
	/**
	 * 获取解码值
	 * @param key
	 * @return
	 */
	public static final String getValue(String key){
		return getProperties().getProperty(key);
	}
	
}
