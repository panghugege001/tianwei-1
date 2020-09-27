package dfh.utils;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import dfh.model.bean.png.Balance;
import dfh.model.bean.png.Credit;
import dfh.model.bean.png.Debit;
import dfh.model.bean.png.GetTicket;
import dfh.model.bean.png.RegisterUser;

public class PNGUtil {

	private static Logger log = Logger.getLogger(PNGUtil.class);
	
	private static final String PREFIX = "TW8";
	private static final String BRANDID = "dayun";
	private static final Integer PID = 425;
	private static final String LANG = "zh_CN";
	private static final String COUNTRY = "CN";
	private static final String CURRENCY = "CNY";
	private static final String BASE_URL = "https://agacw.playngonetwork.com";
	private static final String GAME_URL_MOBILE = BASE_URL + "/casino/PlayMobile";
	private static final String GAME_URL_FLASH = BASE_URL + "/Casino/js";
	private static final String PNG_URL_SERVICE = "https://agaapi.playngonetwork.com:24259/CasinoGameService";
	private static final String SOAP_URL_ACTION = "http://playngo.com/v1/CasinoGameService/";
	private static final String USERNAME = "dayunapi";
	private static final String PWD = "Oh9VPJNh";
	
	private static final String ERROR_MESSAGE = "<ErrorMessage>(.*?)</ErrorMessage>";
	
	//private static final String PNG_BETS_URL = "http://pngback.rocks/pngdata/getPlayerBetsByDate.do";
	
	
	
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
		registerUser.setUsername(PREFIX + loginName);
		registerUser.setNickname(createNickName(loginName));
		registerUser.setCurrency(CURRENCY);
		registerUser.setCountry(COUNTRY);
		registerUser.setBirthdate("1980-01-01");//PNG规定未满18岁不能玩游戏
		registerUser.setRegistration(DateUtil.fmtyyyy_MM_d(new Date()));
		registerUser.setBrandId(BRANDID);
		registerUser.setLanguage(LANG);
		registerUser.setIP("127.0.0.1");
		registerUser.setLocked("false");
		registerUser.setGender("m");
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
	 * 转入PNG账户
	 * @param loginName
	 * @param amount
	 * @return
	 * */
	public static String transferToPNG(String loginName, Double amount){
		if(StringUtils.isBlank(loginName) || amount == null || amount <= 0){
			return null;
		}
		
		loginName = loginName.trim();
		Credit credit = new Credit();
		credit.setExternalUserId(PREFIX + loginName);
		credit.setAmount(Arith.round(amount, 2) + "");//PNG支持最多两位小数
		
		String xmlResponse = null;
		try {
			xmlResponse = requestPNGConsole("Credit", PNGBeanToXMLFactory.processXMLByBean("Credit", null, credit));
			if(StringUtils.isNotBlank(xmlResponse)){
				if(xmlResponse.contains("TransactionId")){
					return "success";
				} else if(xmlResponse.contains("ErrorMessage")){
					log.error(">>>>>>>>>>>>>>>>>>PNG transferToPNG ErrorMessage:" + SixLotteryUtil.compileVerifyData(ERROR_MESSAGE, xmlResponse));
				} else {
					log.error(">>>>>>>>>>>>>>>>>>PNG transferToPNG Error, xmlResponse:" + xmlResponse);
				}
			}
		} catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>PNG transferToPNG ERROR:", e);
		}
		
		return null;
	}
	
	/**
	 * 从PNG账户转出
	 * @param loginName
	 * @param amount
	 * @return
	 * */
	public static String tranferFromPNG(String loginName, Double amount){
		if(StringUtils.isBlank(loginName) || amount == null || amount <= 0){
			return null;
		}
		
		loginName = loginName.trim();
		Debit debit = new Debit();
		debit.setExternalUserId(PREFIX + loginName);
		debit.setAmount(Arith.round(amount, 2) + "");
		
		String xmlResponse = null;
		try {
			xmlResponse = requestPNGConsole("Debit", PNGBeanToXMLFactory.processXMLByBean("Debit", null, debit));
			if(StringUtils.isNotBlank(xmlResponse)){
				if(xmlResponse.contains("TransactionId")){
					return "success";
				} else if(xmlResponse.contains("ErrorMessage")){
					log.error(">>>>>>>>>>>>>>>>>>PNG tranferFromPNG ErrorMessage:" + SixLotteryUtil.compileVerifyData(ERROR_MESSAGE, xmlResponse));
				} else {
					log.error(">>>>>>>>>>>>>>>>>>PNG tranferFromPNG Error, xmlResponse:" + xmlResponse);
				}
			}
		} catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>PNG tranferFromMG ERROR:", e);
		}
		
		return null;
	}
	
	/**
	 * 获取玩家mobile登陆URL
	 * @param loginName
	 * @param gid
	 * @return
	 * */
	public static String mobileGameLogin(String loginName, String gid, String reloadUrl){
		if(StringUtils.isBlank(loginName) || StringUtils.isBlank(gid)){
			return null;
		}
		String ticket = getTicket(loginName);
		if(StringUtils.isNotBlank(ticket)){
			String returnUrl = GAME_URL_MOBILE + "?pid=" + PID + "&gid=" + gid + "&lang=" + LANG + "&practice=0&ticket=" + ticket;
			if(StringUtils.isNotBlank(reloadUrl)){
				returnUrl += "&reloadgame=" + reloadUrl + "/mobi/gamePNGRedirect.php?gameCode=" + gid;// + "&oldticket=" + ticket;//文档写的，但是目测不需要
			}
			return returnUrl;
		}
		return null;
	}
	
	/**
	 * 获取玩家flash登陆URL
	 * @param loginName
	 * @param gid
	 * @return
	 * */
	public static String flashGameLogin(String loginName, String gid) {
		if(StringUtils.isBlank(loginName) || StringUtils.isBlank(gid)){
			return null;
		}
		String ticket = getTicket(loginName);
		if(StringUtils.isNotBlank(ticket)){
			return GAME_URL_FLASH + "?div=pngCasinoGame&pid=" + PID + "&gid=" + gid + "&lang=" + LANG + "&practice=0&username=" + ticket + "&width=100%&height=100%";//practice = 0 表示real
		}
		return null;
	}
	
	/**
	 * 获取ticket
	 * @param loginName
	 * @return
	 * */
	private static String getTicket(String loginName){
		
		loginName = loginName.trim();
		GetTicket getTicket = new GetTicket();
		getTicket.setExternalUserId(PREFIX + loginName);
		String xmlResponse = null;
		try {
			xmlResponse = requestPNGConsole("GetTicket", PNGBeanToXMLFactory.processXMLByBean("GetTicket", null, getTicket));
			if(StringUtils.isNotBlank(xmlResponse)){
				if(xmlResponse.contains("Ticket")){
					return SixLotteryUtil.compileVerifyData("<Ticket>(.*?)</Ticket>", xmlResponse);
				} else if(xmlResponse.contains("ErrorMessage")){
					String msg = SixLotteryUtil.compileVerifyData(ERROR_MESSAGE, xmlResponse);
					log.error(">>>>>>>>>>>>>>>>>>PNG getTicket ErrorMessage:" + msg);
					//如果没有用户，尝试创建一次。成功后重新查询ticket
					if(StringUtils.isNotBlank(msg) && msg.contains("Unknown user")){
						if("success".equals(createPlayer(loginName))){
							xmlResponse = requestPNGConsole("GetTicket", PNGBeanToXMLFactory.processXMLByBean("GetTicket", null, getTicket));
							if(StringUtils.isNotBlank(xmlResponse)){
								if(xmlResponse.contains("Ticket")){
									return SixLotteryUtil.compileVerifyData("<Ticket>(.*?)</Ticket>", xmlResponse);
								} else if(xmlResponse.contains("ErrorMessage")){
									log.error(">>>>>>>>>>>>>>>>>>Repeat PNG getTicket ErrorMessage:" + SixLotteryUtil.compileVerifyData(ERROR_MESSAGE, xmlResponse));
								} else {
									log.error(">>>>>>>>>>>>>>>>>>Repeat PNG getTicket ERROR, xmlResponse:" + xmlResponse);
								}
							}
						}
					}
				} else {
					log.error(">>>>>>>>>>>>>>>>>>PNG getTicket ERROR:, xmlResponse:" + xmlResponse);
				}
			}
			
		} catch (Exception e) {
			log.error(">>>>>>>>>>>>>>>>>>PNG getTicket ERROR:", e);
		}
		return null;
	}
	
	/**
	 * 根据用户及其实践获取PNG流水,如果返回空说明发生异常。
	 * @param loginname
	 * @param timeStart
	 * @param timeEnd
	 * @return
	 * */
/*	public static Double getBetsAmount(String loginname, String timeStart, String timeEnd){
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("loginname", PREFIX + loginname);
		params.put("timeStart", timeStart);
		params.put("timeEnd", timeEnd);
		
		try {
			
			String result = requestPNGBetsConsole(params, PNG_BETS_URL);
			
			if (StringUtils.isNotBlank(result)) {
				
				JSONObject json = JSONObject.fromObject(result);
				boolean success = (boolean) json.get("success");
				if(success){
					
					Double bets = json.getDouble("bets");
					return bets;
				} else {
					
					String message = json.getString("message");
					log.error(">>>>>>>>>>>>>>>>>>PNG getBetsAmount ERROR:" + message);
				}
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	/**
	 * 请求PNGBetsConsole
	 * @param params
	 * @param url
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	private static String requestPNGBetsConsole(Map<String, String> params, String url) throws HttpException, IOException{
		
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(url);
		method.setRequestHeader("Connection", "close");
		method.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		
		NameValuePair[] data = new NameValuePair[params.size()];
		int i = 0;
		for (String pk : params.keySet()) {
			data[i] =  new NameValuePair(pk, params.get(pk));
			i++;
		}
		
		try {
			method.setRequestBody(data);
			httpClient.executeMethod(method);
			return method.getResponseBodyAsString();
		}  finally{
			if(method!=null){
				method.releaseConnection();
			}
		}
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
		String loginname = "dytest06";
//		System.out.println(mobileGameLogin(loginname,"aztecwarriorprincessmobile", "trollhunters"));
		System.out.println(flashGameLogin(loginname, "towerquest"));
//		System.out.println(transferToPNG(loginname, 20.0));
//		System.out.println(tranferFromPNG(loginname, 40.0));
		System.out.println(getBalance(loginname));
//		System.out.println(createPlayer(loginname));
//		System.out.println(DateUtil.fmtyyyy_MM_d(new Date()));
//		System.out.println(createNickName(loginname));
    }

}
