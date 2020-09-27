package com.nnti.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.nnti.common.security.DESUtil;
import com.nnti.common.security.EncryptionUtil;

import net.sf.json.JSONObject;

public class AGINUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(AGINUtil.class);

	private static final String cagent = "DF4_AGIN";
	private static final String md5encrypt_key = "fZUWuALSqTNX";
	private static final String dspurl = "https://gi.dayunjiang.com";
	private static final String AG_BETS_URL = "http://agback.support/agdata/getPlayerBetsByDate.do";

	// 查询玩家账户余额
	public static Double getBalance(String product, String loginName) {

		HttpClient httpClient = null;
		PostMethod postMethod = null;

		try {

			HashMap<String, String> map = aginMap.get(product);
			String PREFIX = map.get("PREFIX");

			String params = "cagent=" + cagent + "/\\\\/loginname=" + PREFIX + loginName + "/\\\\/method=gb/\\\\/actype=" + getActype(loginName) + "/\\\\/password=" + PREFIX + loginName;
			String targetParams = DESUtil.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);

			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;

			httpClient = HttpUtil.createHttpClientShort();

			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");

			httpClient.executeMethod(postMethod);

			String result = postMethod.getResponseBodyAsString();

			String value = parseDspResponseRequest(result);

			if (StringUtils.isBlank(value)) {

				return null;
			}

			if (!NumberUtils.isNumber(value)) {

				log.error(value + "不是有效数字！");

				return null;
			}

			return Double.parseDouble(value);
		} catch (Exception e) {

			log.error("执行getBalance方法发生异常，异常信息：" + e.getMessage());

			return null;
		} finally {

			if (postMethod != null) {

				postMethod.releaseConnection();
			}
		}
	}

	// 转入AG
	public static Boolean transferToAG(String product, String loginName, Double credit, String refNo) {

		return prepareTransferCredit(product, loginName, credit, refNo, "IN");
	}

	// 转出AG
	public static Boolean transferFromAG(String product, String loginName, Double credit, String refNo) {

		return prepareTransferCredit(product, loginName, credit, refNo, "OUT");
	}

	public static Boolean prepareTransferCredit(String product, String loginName, Double credit, String refNo, String type) {

		HttpClient httpClient = null;
		PostMethod postMethod = null;

		try {

			HashMap<String, String> map = aginMap.get(product);
			String PREFIX = map.get("PREFIX");

			String params = "cagent=" + cagent + "/\\\\/loginname=" + PREFIX + loginName + "/\\\\/actype=" + getActype(loginName) + "/\\\\/method=tc/\\\\/billno=" + cagent + refNo.substring(1)
							+ "/\\\\/type=" + type + "/\\\\/credit=" + credit + "/\\\\/password=" + PREFIX + loginName;
			String targetParams = DESUtil.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);

			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;

			httpClient = HttpUtil.createHttpClient();

			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");

			httpClient.executeMethod(postMethod);

			String result = postMethod.getResponseBodyAsString();
			String value = parseDspResponseRequest(result);
			log.info("玩家" + loginName + "执行" + type + "操作，金额为：" + credit + "，单号为：" + refNo + "，result参数值为：" + result + "，value参数值为：" + value);

			if (StringUtils.isBlank(value) || !value.equals("0")) {

				result = transferCreditConfirm(product, loginName, credit, refNo, type, 0);
				value = parseDspResponseRequest(result);
				log.info("玩家" + loginName + "执行" + type + "操作，value值为空或者不等于0的情况下执行if语句，result参数值为：" + result + "，value参数值为：" + value);

				return false;
			}

			if (value.equals("0")) {

				result = transferCreditConfirm(product, loginName, credit, refNo, type, 1);
				value = parseDspResponseRequest(result);
				log.info("玩家" + loginName + "执行" + type + "操作，value值为0的情况下执行if语句，result参数值为：" + result + "，value参数值为：" + value);

				if (StringUtils.isBlank(value) || !value.equals("0")) {

					return false;
				}

				if (value.equals("0")) {

					return true;
				}
			}

			return false;
		} catch (Exception e) {

			log.error("执行prepareTransferCredit方法发生异常，异常信息：" + e.getMessage());

			return false;
		} finally {

			if (postMethod != null) {

				postMethod.releaseConnection();
			}
		}
	}

	public static String transferCreditConfirm(String product, String loginName, Double credit, String refNo, String type, Integer flag) {

		String result = "";
		HttpClient httpClient = null;
		PostMethod postMethod = null;

		try {

			HashMap<String, String> map = aginMap.get(product);
			String PREFIX = map.get("PREFIX");

			String params = "cagent=" + cagent + "/\\\\/loginname=" + PREFIX + loginName + "/\\\\/actype=" + getActype(loginName) + "/\\\\/method=tcc/\\\\/billno=" + cagent + refNo.substring(1)
							+ "/\\\\/flag=" + flag + "/\\\\/type=" + type + "/\\\\/credit=" + credit + "/\\\\/password=" + PREFIX + loginName;
			String targetParams = DESUtil.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);

			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;

			httpClient = HttpUtil.createHttpClient();

			postMethod = new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B20");

			httpClient.executeMethod(postMethod);

			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {

			log.error("执行transferCreditConfirm方法发生异常，异常信息：" + e.getMessage());
		} finally {

			if (postMethod != null) {

				postMethod.releaseConnection();
			}
		}

		return result;
	}

	public static String parseDspResponseRequest(String xml) {

		xml = StringUtils.trimToEmpty(xml);

		if (StringUtils.isBlank(xml)) {

			log.error("系统繁忙，AGIN无响应，请稍后再试！");

			return null;
		}

		Document doc = string2Document(xml);

		if (doc != null) {

			Element element = (Element) doc.getRootElement();
			String info = element.attributeValue("info");

			if (info.equals("account_not_exit")) {

				log.error("帐号不存在！");

				return null;
			} else if (info.equals("error") || info.equals("network_error")) {

				log.error("系统繁忙！");

				return null;
			} else if (info.equals("not_enough_credit")) {

				log.error("转账失败，额度不足！");

				return null;
			} else if (info.equals("duplicate_transfer")) {

				log.error("重复转账！");

				return null;
			}

			return info;
		} else {

			log.error("document parse failed：" + xml);

			return null;
		}
	}

	public static Document string2Document(String s) {

		Document doc = null;

		try {

			doc = DocumentHelper.parseText(s);
		} catch (Exception ex) {

			ex.printStackTrace();
		}

		return doc;
	}

	//转账只支持真钱账号
	public static String getActype(String loginName) {

		return "1";
	}

	// 查询指定时间段内玩家的投注额
	public static Double getBetAmount(String product, String loginName, String startTime, String endTime) {
		String sql = "select playName,SUM(validBetAmount)bet,SUM(netAmount)amount from agdata where platformType=:platformType and DATE(recalcuTime) =:startTime GROUP BY playName ";
		
		Double betAmount = 0.00;
		

		return betAmount;
	}

	public static void main(String[] args) throws Exception {
		String loginname = "dytest01";
		System.out.println(getBalance("dy", loginname));
		System.out.println(transferToAG("dy", loginname, 1.00, System.currentTimeMillis()+"1"));
		System.out.println(getBalance("dy", loginname));
		System.out.println(transferFromAG("dy", loginname, 1.00, System.currentTimeMillis()+"2"));
		System.out.println(getBalance("dy", loginname));
//		System.out.println(getBetAmount("dy", "devtest999", "2017-06-01 00:00:00", "2017-07-04 00:00:00"));
	}
}