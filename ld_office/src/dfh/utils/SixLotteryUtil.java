package dfh.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import dfh.model.PlatformData;

/**
 * 六合彩
 */
public class SixLotteryUtil {
	private static Logger log = Logger.getLogger(SixLotteryUtil.class);
	
	private static final String MODE = "0"; // 0:正式 1:測試
	private static final String DOMAIN = "www.longdu97.com";
	private static final String REGISTER_URL = "http://api1.dios.com.ph/cash_api/api.php" ; //注册
//	private static final String REGISTER_URL = "http://api1.dios.com.ph/cash_api/blue_test.php"; // 注册
	
	private static final String STTAF = "E" ;

	@SuppressWarnings("deprecation")
	public static String register(String username, String password,
			String nickname) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");

		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>registerPlayer</command>");
		buffer.append("<params>");
		buffer.append("<username>"+STTAF + username + "</username>");
		buffer.append("<password>"+STTAF + username + "</password>");
		buffer.append("<nickname>" + nickname + "</nickname>");
		buffer.append("<mode>" + MODE + "</mode>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	
	public static String login(String username, String password) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");

		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>login</command>");
		buffer.append("<params>");
		buffer.append("<username>"+STTAF + username + "</username>");
		buffer.append("<password>"+STTAF + username + "</password>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		if(result.contains("帐号密码错误，请重新输入")){ //修改密码
			
		}
		return result;
	}
	
	/**
	 * 玩家是否存在 
	 * @param username
	 * @return Y/N
	 */
	public static String userIsExist(String username) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");

		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>IsPlayerExist</command>");
		buffer.append("<params>");
		buffer.append("<username>"+STTAF + username + "</username>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info("userIsExist-->"+result);
		return compileVerifyData("<result>(.*?)</result>", result);
	}
	/**
	 * 存款
	 * @param username
	 * @param amount
	 * @param refno
	 * @return
	 */
	public static String deposit(String username,Double amount , String refno) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");

		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>deposit</command>");
		buffer.append("<params>");
		buffer.append("<username>"+STTAF + username + "</username>");
		buffer.append("<curId>RMB</curId>");
		buffer.append("<amt>"+amount+"</amt>");
		buffer.append("<refno>"+refno+"</refno>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	/**
	 * 提款
	 * @param username
	 * @param amount
	 * @param refno
	 * @return
	 */
	public static String withdrawal(String username,Double amount , String refno) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>withdrawal</command>");
		buffer.append("<params>");
		buffer.append("<username>"+STTAF + username + "</username>");
		buffer.append("<curId>RMB</curId>");
		buffer.append("<amt>"+amount+"</amt>");
		buffer.append("<refno>"+refno+"</refno>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	/**
	 * 查看余额
	 * @param username
	 * @return
	 */
	public static String balance(String username) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>balance</command>");
		buffer.append("<params>");
			buffer.append("<username>"+STTAF + username + "</username>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	/**
	 * 取得报表
	 * @param username
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String get_betSheet(String username , String startTime , String endTime) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>get_betSheet</command>");
		buffer.append("<params>");
			buffer.append("<username>"+STTAF + username + "</username>");
			buffer.append("<startTime>" + startTime + "</startTime>");
			buffer.append("<endTime>" + endTime + "</endTime>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	/**
	 * 获取现金账本
	 * @param username
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String get_cashbook(String username , String startTime , String endTime) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>get_cashbook</command>");
		buffer.append("<params>");
			buffer.append("<username>"+STTAF + username + "</username>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	/**
	 * 取得期数开奖结果
	 * @param gametype //遊戲類別 ex. SB、D3、CQ
	 * @param gamenum //期數
	 * @param gamedate//期數日期 ex. 2014-01-01
	 * @return
	 */
	public static String get_game_result(String gametype , String gamenum , String gamedate) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>get_game_result</command>");
		buffer.append("<params>");
			buffer.append("<gametype>" + gametype + "</gametype>");
			buffer.append("<gamenum>" + gamenum + "</gamenum>");
			buffer.append("<gamedate>" + gamedate + "</gamedate>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	
	public static String ChangePassword(String username , String password , String newpassword) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>ChangePassword</command>");
		buffer.append("<params>");
		buffer.append("<username>"+STTAF + username + "</username>");
		buffer.append("<password>"+STTAF + password + "</password>");
		buffer.append("<newpassword>"+STTAF + newpassword + "</newpassword>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	
	
	
	public static String getValidateBet(String username , String startTime , String endTime) {
		if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)){
			startTime = DateUtil.parseDateForStandard(startTime).getTime()/1000+"";
			endTime = DateUtil.parseDateForStandard(endTime).getTime()/1000+"";
		}else{
			return null ;
		}
		log.info(startTime+"---"+endTime);
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(REGISTER_URL);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\'1.0\' encoding=\'UTF-8\'?>"
				+ "<request>");
		buffer.append("<domain>" + DOMAIN + "</domain>");
		buffer.append("<command>get_real_order</command>");
		buffer.append("<params>");
		if(StringUtils.isNotBlank(username)){
			buffer.append("<username>"+STTAF + username + "</username>");
		}else{
			buffer.append("<username></username>");
		}
		buffer.append("<startTime>" + startTime + "</startTime>");
		buffer.append("<endTime>" + endTime + "</endTime>");
		buffer.append("</params>");
		buffer.append("</request>");
		NameValuePair entity = new NameValuePair( "data" , buffer.toString() );
		method.setRequestBody( new NameValuePair[]{entity});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	
	public static List<PlatformData> parseBetXml(String xml){
		List<PlatformData> list = new ArrayList<PlatformData>() ;
		if(StringUtils.isNotBlank(xml)){  
		    if(xml.indexOf("<") != -1 && xml.lastIndexOf(">") != -1 && xml.lastIndexOf(">") > xml.indexOf("<"))  
		    	xml = xml.substring(xml.indexOf("<"), xml.lastIndexOf(">") + 1);  
		}
		
		Document doc = DomOperator.string2Document(xml);
		if(null != doc){
			Element rootele = doc.getRootElement() ;
			String status = rootele.elementTextTrim("status");
			if(status.equals("0")){
				Element resultEle = rootele.element("result");
				Iterator it = resultEle.elementIterator() ;
				PlatformData bean = null ;
				String username = "" ;
				
				while(it.hasNext()){
					Element subEle = (Element) it.next() ;
					if(subEle.getText().contains("@e68")){
						bean = new PlatformData() ;
						bean.setLoginname(null);
						username = subEle.getTextTrim().substring(1).replace("@e68", "") ;
					}else{
						Iterator subIt = subEle.elements().iterator();
						double bet1 = 0.0 ;
						double profit1 = 0.0;
						while(subIt.hasNext()){
							Element singleEle = (Element) subIt.next() ;
							if(singleEle.getQName().getName().equals("realgold")){
								bet1 += new Double(singleEle.getTextTrim()) ;
							}
							if(singleEle.getQName().getName().equals("win")){
								profit1 += new Double(singleEle.getTextTrim()) ;
							}
						}
						PlatformData validBean = listHaveBean(list, username) ;
						if(null == validBean){
							bean.setBet(bet1);
							bean.setProfit(-1*profit1);
							bean.setLoginname(username);
							list.add(bean) ;
						}else{
							validBean.setBet(validBean.getBet() + bet1);
							validBean.setProfit(validBean.getProfit() + (-1*profit1));
							list.add(validBean);
						}
					}
				}
				
			}else{
				return null ;
			}
		}
		return list ;
	}
	
	public static PlatformData listHaveBean(List<PlatformData> list ,String username){
		for (PlatformData platformData : list) {
			if(platformData.getLoginname().equals(username)){
				return platformData ;
			}
		}
		return null ;
	}
	
	
	public  static String compileVerifyData(String pattern,String verifyData){
    	Pattern p=Pattern.compile(pattern);
    	Matcher m=p.matcher(verifyData);
    	String result = "";
    	while(m.find()){
    		result = m.group(1);
        }
    	return result;
    }
	
	public static void main(String[] args) {
//		System.out.println(register("woody", "admin_admin", "woodytest"));
//		System.out.println(login("woodytest", "123456"));
//		System.out.println(userIsExist("woodytest1"));
//		System.out.println(deposit("woodytest", 2.0, null));
//		System.out.println(withdrawal("woodytest", 1.0, null));
//		System.out.println(balance("woodytest"));
		System.out.println(get_betSheet("ericktest","2014-12-7","2014-12-11"));
//		System.out.println(get_cashbook("woodytest","2014-12-7","2014-12-8"));
//		System.out.println(get_game_result("SB","1","2014-12-7"));
//		System.out.println(ChangePassword("woodytest","admin_admin","woodytest"));
		Date date = new Date();
		Date startT = null;
		Date endT = null;
		
		Calendar cals = Calendar.getInstance();
		cals.setTime(date);
		cals.add(Calendar.DAY_OF_MONTH, -1);
		startT = cals.getTime();
		
		Calendar endcals = Calendar.getInstance();
		endcals.setTime(new Date());
		endT = endcals.getTime();
		
		System.out.println(getValidateBet(null, DateUtil.fmtDateForBetRecods(startT), DateUtil.fmtDateForBetRecods(endT)));
	}

}
