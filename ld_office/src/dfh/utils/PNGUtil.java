package dfh.utils;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import dfh.model.bean.png.Balance;
import dfh.model.bean.png.RegisterUser;

public class PNGUtil {

	private static Logger log = Logger.getLogger(PNGUtil.class);
	
	private static final String PREFIX = "DY8";
	private static final String BRANDID = "dayun";
	private static final String LANG = "zh_CN";
	private static final String COUNTRY = "CN";
	private static final String CURRENCY = "CNY";
	private static final String PNG_URL_SERVICE = "https://agaapi.playngonetwork.com:24259/CasinoGameService";
	private static final String SOAP_URL_ACTION = "http://playngo.com/v1/CasinoGameService/";
	private static final String USERNAME = "dayunapi";
	private static final String PWD = "Oh9VPJNh";
	
	private static final String ERROR_MESSAGE = "<ErrorMessage>(.*?)</ErrorMessage>";
	
	
	
	/**
	 * 获取PNG余额，如果用户不存在则创建用户，成功后返回0
	 * @param loginName
	 * @return
	 * */
	public static Double getBalance(String loginName){
		
		loginName = loginName.trim();
		Balance balanceVO = new Balance();
		balanceVO.setExternalUserId(PREFIX + loginName);
		String xmlResponse = null;
		try {
			xmlResponse = requestPNGConsole("Balance", PNGBeanToXMLFactory.processXMLByBean("Balance", null, balanceVO));
			
			if(xmlResponse.contains("Real")){
				String balance = SixLotteryUtil.compileVerifyData("<Real>(.*?)</Real>", xmlResponse);
				if(StringUtils.isNotBlank(balance)){
					return Double.parseDouble(balance);
				}
			} else if(xmlResponse.contains("ErrorMessage")){
				String msg = SixLotteryUtil.compileVerifyData(ERROR_MESSAGE, xmlResponse);
				log.error(">>>>>>>>>>>>>>>>>>PNG getBalance ErrorMessage:" + msg);
				if(StringUtils.isNotBlank(msg) && msg.contains("Unknown user")){
					if("success".equals(createPlayer(loginName))){
						return 0.0;
					}
				}
			} else {
				log.error(">>>>>>>>>>>>>>>>>>PNG getBalance Error, xmlResponse:" + xmlResponse);
			}
			
		} catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>PNG getBalance ERROR:", e);
		}
		
		return null;
	}
	
	/**
	 * 创建玩家
	 * @param loginName
	 * @return
	 * */
	public static String createPlayer(String loginName){
		
		loginName = loginName.trim();
		RegisterUser registerUser = new RegisterUser();
		registerUser.setExternalUserId(PREFIX + loginName);
		registerUser.setBrandId(BRANDID);
		registerUser.setNickname(createNickName(loginName));
		registerUser.setBirthdate("1980-01-01");//PNG规定未满18岁不能玩游戏
		registerUser.setRegistration(DateUtil.fmtYYYY_MM_DD(new Date()));
		registerUser.setLanguage(LANG);
		registerUser.setCountry(COUNTRY);
		registerUser.setCurrency(CURRENCY);
		String xmlResponse = null;
		try {
			xmlResponse = requestPNGConsole("RegisterUser", PNGBeanToXMLFactory.processXMLByBean("RegisterUser", "UserInfo", registerUser));
			
			if(StringUtils.isNotBlank(xmlResponse)){
				if(xmlResponse.contains("RegisterUserResponse")){
					return "success";
				} else if(xmlResponse.contains("ErrorMessage")){
					log.error(">>>>>>>>>>>>>>>>>>PNG createPlayer ErrorMessage:" + SixLotteryUtil.compileVerifyData(ERROR_MESSAGE, xmlResponse));
				} else {
					log.error(">>>>>>>>>>>>>>>>>>PNG createPlayer Error, xmlResponse:" + xmlResponse);
				}
			}
		} catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>PNG createPlayer ERROR:", e);
		}
		
		return null;
		
	}
	
	/**
	 * 请求PNGConsole
	 * @param params
	 * @param url
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	private static String requestPNGConsole(String method, String xml) throws HttpException, IOException{
		
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod postMethod = new PostMethod(PNG_URL_SERVICE);
		try {
			
			//method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
			postMethod.setRequestHeader("Connection", "close");
			postMethod.setRequestHeader("Authorization", getAuthorization());
			postMethod.setRequestHeader("Content-Type", "text/xml;charset=UTF-8");
			postMethod.setRequestHeader("SOAPAction", SOAP_URL_ACTION + method);
			postMethod.setRequestEntity(new StringRequestEntity(xml));
			
			int statusCode = httpClient.executeMethod(postMethod);
			
			if(statusCode != 200){
				log.error(">>>>>>>>>>>>>>>>>>PNG ERROR StatusCode===============" + statusCode);
			}
			return postMethod.getResponseBodyAsString();
		}  finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
	}
	
	private static String getAuthorization(){
		String auth = USERNAME + ":" + PWD;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));
		return "Basic " + new String(encodedAuth);
	}
	
	private static String createNickName(String loginname){
		
		if(loginname != null){
			String regEx="[\\W]";
			Pattern p = Pattern.compile(regEx); 
			Matcher m = p.matcher(loginname);
			loginname = m.replaceAll("");
		}
		
		if(StringUtils.isBlank(loginname)){
			loginname = DateUtil.fmtyyMMddHHmmss(new Date());
		}
		return loginname.length() > 12? loginname.substring(0, 12) : loginname;
	}
	
	public static void main(String[] args) {
		
//		System.out.println(mobileGameLogin("test1", "holidayseasonmobile"));
		System.out.println(getBalance("dytest01"));
//		System.out.println(transferToPNG("test1", 200.5));
//		System.out.println(tranferFromPNG("test2", 2.5));
//		System.out.println(createPlayer("woodytest1"));
//		System.out.println(DateUtil.fmtyyyy_MM_d(new Date()));
    }

}
