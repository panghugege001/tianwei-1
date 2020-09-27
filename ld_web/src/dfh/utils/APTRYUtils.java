package dfh.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import dfh.security.DESEncrypt;
import dfh.security.EncryptionUtil;

public class APTRYUtils {

	static String  encrypt_key="njvs90z1";
	static String  md5encrypt_key="sdf7&^#gfas";
	static String  cagent="B20_AG";
	static String  dspurl="http://gi.sunrise88.net:81";
	static DESEncrypt  des=new DESEncrypt(encrypt_key);
	public static String getActype(String loginname) {
		return "0";
	}

	public static String isCustomerExist(String loginname) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=ice";
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}

		return result;
	}

	public static String CheckOrCreateGameAccount(String loginname, String password) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=lg/\\\\/actype=" + getActype(loginname) + "/\\\\/password=" + password;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}

		return result;
	}

	public static String GetBalance(String loginname, String password) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=gb/\\\\/actype=" + getActype(loginname) + "/\\\\/password=" + password;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

	public static String PrepareTransferCredit(String loginname, String billno, String type, Double credit, String password) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/actype=" + getActype(loginname) + "/\\\\/method=tc/\\\\/billno=" + cagent + billno.substring(1) + "/\\\\/type=" + type + "/\\\\/credit=" + credit + "/\\\\/password=" + password;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

	public static String TransferCreditConfirm(String loginname, String billno, String type, Double credit, String password, Integer flag) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/actype=" + getActype(loginname) + "/\\\\/method=tcc/\\\\/billno=" + cagent + billno.substring(1) + "/\\\\/flag=" + flag + "/\\\\/type=" + type + "/\\\\/credit=" + credit + "/\\\\/password=" + password;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

	public static String SearchTransferResult(String loginname, String billno) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=str/\\\\/billno=" + cagent + billno.substring(1);
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

	public static String UpdateTransferResultStatus(String loginname, String billno, String operator, Integer flag) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=uts/\\\\/billno=" + billno.substring(1) + "/\\\\/flag=" + flag + "/\\\\/operator=" + operator;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

	public static String forwardGame(String loginname, String password, String website, Integer sid) {
		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/dm=" + website + "/\\\\/actype=" + getActype(loginname) + "/\\\\/password=" + password + "/\\\\/sid=" + sid;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/forwardGame.do?params=" + targetParams + "&key=" + key;
			httpClient = HttpUtils.createHttpClient();
			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

}
