package com.nnti.common.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.nnti.common.model.vo.png.Balance;
import com.nnti.common.model.vo.png.Credit;
import com.nnti.common.model.vo.png.Debit;
import com.nnti.common.model.vo.png.RegisterUser;

public class PNGUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(PNGUtil.class);

	public static final String PID = "425";
	public static final String PNG_URL_SERVICE = "https://agaapi.playngonetwork.com:24259/CasinoGameService";
	public static final String SOAP_URL_ACTION = "http://playngo.com/v1/CasinoGameService/";
	public static final String USERNAME = "dayunapi";
	public static final String PWD = "Oh9VPJNh";
	private static final String ERROR_MESSAGE = "<ErrorMessage>(.*?)</ErrorMessage>";
	private static final String LANG = "zh_CN";
	private static final String COUNTRY = "CN";
	private static final String CURRENCY = "CNY";

	// 获取PNG余额，如果用户不存在则创建用户，成功后返回0
	public static Double getBalance(String product, String loginName) {

		HashMap<String, String> map = pngMap.get(product);
		String PREFIX = map.get("PREFIX");

		loginName = loginName.trim();

		Balance balanceVO = new Balance();

		balanceVO.setExternalUserId(PREFIX + loginName);

		try {

			String xmlResponse = requestPNGConsole(product, "Balance", PNGBeanToXMLFactory.processXMLByBean("Balance", null, balanceVO));

			if (xmlResponse.contains("Real")) {

				String balance = StringUtil.match("<Real>(.*?)</Real>", xmlResponse);

				if (StringUtils.isNotBlank(balance)) {

					return Double.parseDouble(balance);
				}
			} else if (xmlResponse.contains("ErrorMessage")) {

				String msg = StringUtil.match(ERROR_MESSAGE, xmlResponse);

				if (StringUtils.isNotBlank(msg) && msg.contains("Unknown user")) {

					if ("success".equals(createPlayer(product, loginName))) {

						return 0.0;
					}
				}
			}
		} catch (Exception e) {

			log.error("执行getBalance方法发生异常，异常信息：" + e.getMessage());
		}

		return null;
	}

	// 创建玩家
	public static String createPlayer(String product, String loginName) {

		HashMap<String, String> map = pngMap.get(product);
		String PREFIX = map.get("PREFIX");
		String BRANDID = map.get("BRANDID");

		loginName = loginName.trim();

		RegisterUser registerUser = new RegisterUser();

		registerUser.setExternalUserId(PREFIX + loginName);
		registerUser.setBrandId(BRANDID);
		registerUser.setNickname(createNickName(loginName));
		registerUser.setBirthdate("1980-01-01");
		registerUser.setRegistration(DateUtil.format(DateUtil.YYYY_MM_DD, new Date()));
		registerUser.setLanguage(LANG);
		registerUser.setCountry(COUNTRY);
		registerUser.setCurrency(CURRENCY);

		String xmlResponse = null;

		try {

			xmlResponse = requestPNGConsole(product, "RegisterUser", PNGBeanToXMLFactory.processXMLByBean("RegisterUser", "UserInfo", registerUser));

			if (StringUtils.isNotBlank(xmlResponse)) {

				if (xmlResponse.contains("RegisterUserResponse")) {

					return "success";
				}
			}
		} catch (Exception e) {

			log.error("执行createPlayer方法发生异常，异常信息：" + e.getMessage());
		}

		return null;
	}

	// 转入PNG账户
	public static String transferToPNG(String product, String loginName, Double amount) {

		HashMap<String, String> map = pngMap.get(product);
		String PREFIX = map.get("PREFIX");

		loginName = loginName.trim();

		Credit credit = new Credit();

		try {

			credit.setExternalUserId(PREFIX + loginName);
			credit.setAmount(String.valueOf(NumericUtil.round(amount, 2)));

			String xmlResponse = requestPNGConsole(product, "Credit", PNGBeanToXMLFactory.processXMLByBean("Credit", null, credit));

			if (StringUtils.isNotBlank(xmlResponse)) {

				if (xmlResponse.contains("TransactionId")) {

					return "success";
				}
			}
		} catch (Exception e) {

			log.error("执行transferToPNG方法发生异常，异常信息：" + e.getMessage());
		}

		return null;
	}

	// 转出PNG账户
	public static String tranferFromPNG(String product, String loginName, Double amount) {

		HashMap<String, String> map = pngMap.get(product);
		String PREFIX = map.get("PREFIX");

		loginName = loginName.trim();

		Debit debit = new Debit();

		try {

			debit.setExternalUserId(PREFIX + loginName);
			debit.setAmount(String.valueOf(NumericUtil.round(amount, 2)));

			String xmlResponse = requestPNGConsole(product, "Debit", PNGBeanToXMLFactory.processXMLByBean("Debit", null, debit));

			if (StringUtils.isNotBlank(xmlResponse)) {

				if (xmlResponse.contains("TransactionId")) {

					return "success";
				}
			}
		} catch (Exception e) {

			log.error("执行tranferFromPNG方法发生异常，异常信息：" + e.getMessage());
		}

		return null;
	}


	private static String requestPNGConsole(String product, String method, String xml) throws HttpException, IOException {

		HashMap<String, String> map = pngMap.get(product);
		String PNG_URL_SERVICE = map.get("PNG_URL_SERVICE");
		String SOAP_URL_ACTION = map.get("SOAP_URL_ACTION");

		System.setProperty("https.protocols", "TLSv1.1");
		HttpClient httpClient = HttpUtil.createHttpClient();

		PostMethod postMethod = new PostMethod(PNG_URL_SERVICE);

		try {

			postMethod.setRequestHeader("Connection", "close");
			postMethod.setRequestHeader("Authorization", getAuthorization(product));
			postMethod.setRequestHeader("Content-Type", "text/xml;charset=UTF-8");
			postMethod.setRequestHeader("SOAPAction", SOAP_URL_ACTION + method);
			postMethod.setRequestEntity(new StringRequestEntity(xml));

			int statusCode = httpClient.executeMethod(postMethod);

			log.info(">>>>>>>>PNGUtil requestPNGConsole statusCode>>>>>>>>" + statusCode + ">>>>>>>>" + postMethod.getResponseBodyAsString());

			if (statusCode != 200) {

			}

			return postMethod.getResponseBodyAsString();
		} finally {

			if (postMethod != null) {

				postMethod.releaseConnection();
			}
		}
	}

	private static String getAuthorization(String product) {

		HashMap<String, String> map = pngMap.get(product);
		String USERNAME = map.get("USERNAME");
		String PWD = map.get("PWD");

		String auth = USERNAME + ":" + PWD;

		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("UTF-8")));

		return "Basic " + new String(encodedAuth);
	}

	private static String createNickName(String loginName) {

		if (StringUtils.isNotBlank(loginName)) {

			String regEx = "[\\W]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(loginName);
			loginName = m.replaceAll("");
		}

		if (StringUtils.isBlank(loginName)) {

			loginName = DateUtil.format(DateUtil.YYMMDDHHMMSS, new Date());
		}

		return loginName.length() > 12 ? loginName.substring(0, 12) : loginName;
	}

	public static void main(String[] args) {
//		System.out.println(transferToPNG("dy", "dytest01", 100.0));
//		System.out.println(tranferFromPNG("dy", "dytest01", 5.0));
		System.out.println(getBalance("dy", "dytest01"));
	}
}