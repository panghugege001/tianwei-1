package dfh.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import dfh.exception.NoSuchPropertyException;

public class ConfigLoad {

	private static Logger log = Logger.getLogger(ConfigLoad.class);
	
	private static Properties gameProperties;
	private static Properties configProperties;
	
	private final static String GAME_CONFIG_NAME = "gameplatform.properties";
	private final static String CONFIG_NAME = "config.properties";
	
	static {
		initGameProperties();
		initConfigProperties();
	}
	
	public static String getGameProperties(String key){
		return gameProperties.getProperty(key);
	}
	
	public static String getConfigProperties(String key) {
		return configProperties.getProperty(key);
	}
	
	private static void initGameProperties() {
		gameProperties = new Properties();
		try {
			gameProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(GAME_CONFIG_NAME));
		} catch (IOException e) {
			log.error("加载系统配置发生错误：" + e.getMessage(), e);
		}
	}
	
	private static void initConfigProperties() {
		configProperties = new Properties();
		try {
			configProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_NAME));
		} catch (IOException e) {
			log.error("加载系统配置发生错误：" + e.getMessage(), e);
		}
	
	}


	public static boolean isKeyExists(String key) {
		return gameProperties.containsKey(key);
	}
	
	public static boolean isKeyExistsInConfigProperties(String[] keys) {
		for(String key : keys){
			if(!(configProperties.containsKey(key)))
				return false;
		}
		return true;
	}
	
	public static boolean isKeyAndValueExistsInConfigProperties(String[] keys) {
		for(String key : keys){
			if(!(configProperties.containsKey(key)))return false;
			if(StringUtil.isEmpty(configProperties.getProperty(key)))return false; 
		}
		return true;
	}
	
	public static String getConfigValueByKey(String key) {
		InputStream is = null;
		try {
			Properties properties = new Properties();
			//is=SQLHelper.class.getClassLoader().getResourceAsStream("com/demo/util/XXX.properties");
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_NAME);
			properties.load(is);
			return properties.getProperty(key).trim();
		} catch (Exception e) {
			log.error("get value of the key["+key+"] in " + CONFIG_NAME + " failed!", e);
			throw new NoSuchPropertyException("get value of the key["+key+"] in " + CONFIG_NAME + " failed!");
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					log.error("InputStream close error!", e);
				}
			}
		}
	}

}
