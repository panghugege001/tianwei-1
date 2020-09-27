package dfh.utils;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import dfh.model.bean.png.RegisterUser;


/**
 * PNGBean转XML工厂
 * */
public class PNGBeanToXMLFactory {
	public static final String Envelope = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://playngo.com/v1\">";
	public static final String EnvelopeTail = "</soapenv:Envelope>";
	public static final String Header = "<soapenv:Header/>";
	public static final String Body = "<soapenv:Body>";
	public static final String BodyTail = "</soapenv:Body>";
	public static final String v1 = "<v1:";
	public static final String v1Tail = "</v1:";
	public static final String tail = ">";
	
	public static String processXMLByBean(String lablename1, String lablename2, Object obj) throws IllegalArgumentException, IllegalAccessException{
		if(obj == null){
			return null;
		}
		
		String result = Envelope + Header + Body;
		if(StringUtils.isNotBlank(lablename1)){
			
			result += v1 + lablename1 + tail;
			if(StringUtils.isNotBlank(lablename2)){
				result += v1 + lablename2 + tail;
				result += processXMLByBean(obj);
				result += v1Tail + lablename2 + tail;
			} else {
				result += processXMLByBean(obj);
			}
			result += v1Tail + lablename1 + tail;
		}
		result += BodyTail + EnvelopeTail;
//		System.out.println(result);
		return result;
	}
	
	public static String processXMLByBean(Object obj) throws IllegalArgumentException, IllegalAccessException{
		if(obj == null){
			return null;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		String xml = "";
		if(fields != null && fields.length > 0){
			for(int i = 0; i < fields.length; i++){
				fields[i].setAccessible(true);
				if(fields[i].get(obj) != null){
					xml += v1 + fields[i].getName() + tail + fields[i].get(obj) + v1Tail + fields[i].getName() + tail;
				}
			}
		}
		return xml;
	}
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		RegisterUser u = new RegisterUser();
		System.out.println(PNGBeanToXMLFactory.processXMLByBean("RegisterUser", "UserInfo", u));
	}

}
