package com.nnti.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.nnti.common.constants.Constant;

public class EAUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(EAUtil.class);

	private static final String P_USERID = "userid";
	private static final String P_VENDORID = "vendorid";
	private static final String P_CURRENCYID = "currencyid";
	private static final String A_ID = "id";
	private static final String A_NAME = "name";
	private static final String P_BALANCE = "balance";
	private static final String P_LOCATION = "location";
	private static final String P_ACODE = "acode";
	private static final String P_AMOUNT = "amount";
	private static final String P_REFNO = "refno";
	private static final String P_STATUS = "status";
	private static final String P_PAYMENTID = "paymentid";
	private static final String P_ERRDESC = "errdesc";
	private static final String CURRENCYID = "156";
	private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"" + Constant.ENCODE + "\"?>";
	private static final String NEWLINE = "";
	// 正式环境为3，测试环境为2
	private static final String VENDORID = "3";
	// 正式地址
	private static final String HTTPS_HOST = "mis.ea3-mission.com";
	// 获取EA服务器地址
	private static final String EA_GAME_URL = ConfigUtil.getValue("EA_GAME_URL");

	// 获取远程游戏金额
	public static Double getPlayerMoney(String product, String loginName) {

		Map<String, Object> resultMap = checkClient(product, loginName);

		if (null != resultMap) {

			return Double.parseDouble(String.valueOf(resultMap.get("balance")));
		}

		return null;
	}

	// 转入游戏
	public static Boolean getDepositPlayerMoney(String product, String loginName, Double amount, String referenceId) {

		Map<String, Object> resultMap = depositPendingRequest(product, loginName, amount, referenceId, "");

		if (null == resultMap) {

			return false;
		}

		try {

			// 当调用转账接口后，该笔订单有可能会延迟，直接调用确认接口，status的值有可能返回为0(0代表为充值中)，所以需要特殊处理，使当前线程等待五秒，保证调用确认接口返回正确的status值
			Thread.sleep(5000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		String id = String.valueOf(resultMap.get("id"));
		String agCode = String.valueOf(resultMap.get("agcode"));
		String paymentid = String.valueOf(resultMap.get("paymentid"));

		Boolean flag = depositConfirmationResponse(product, id, "0", paymentid, agCode);

		return flag;
	}

	// 转出游戏
	public static Boolean getWithdrawPlayerMoney(String product, String loginName, Double amount, String referenceId) {

		Map<String, Object> resultMap = withdrawPendingRequest(product, loginName, amount, referenceId, "");

		if (null != resultMap && "0".equals(String.valueOf(resultMap.get("status")))) {

			return true;
		}

		return false;
	}

	private static Map<String, Object> withdrawPendingRequest(String product, String loginName, Double amount, String refNo, String agCode) {

		HashMap<String, String> map = eaMap.get(product);
		String WITHDRAW_URL = map.get("WITHDRAW_URL");

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put(P_USERID, loginName);
		paramsMap.put(P_ACODE, agCode);
		paramsMap.put(P_VENDORID, VENDORID);
		paramsMap.put(P_CURRENCYID, CURRENCYID);
		paramsMap.put(P_AMOUNT, NumericUtil.formatDouble(amount));
		paramsMap.put(P_REFNO, refNo);

		String xml = createRequestXML("cwithdrawal", generateWithdrawID(), paramsMap);

		return parseWithdrawalConfirmationResponseBean(postXMLBySSL(HTTPS_HOST, WITHDRAW_URL, xml));
	}

	private static Boolean depositConfirmationResponse(String product, String id, String status, String paymentid, String agCode) {

		HashMap<String, String> map = eaMap.get(product);
		String DEPOSIT_URL = map.get("DEPOSIT_URL");

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put(P_ACODE, agCode);
		paramsMap.put(P_STATUS, status);
		paramsMap.put(P_PAYMENTID, paymentid);
		paramsMap.put(P_ERRDESC, "");

		String xml = createRequestXML("cdeposit-confirm", id, paramsMap);
//		log.info("******xml******" + xml);

		String response = postXMLBySSL(HTTPS_HOST, DEPOSIT_URL, xml);

		if (StringUtils.isBlank(response)) {

			return true;
		}

		return false;
	}

	private static Map<String, Object> depositPendingRequest(String product, String loginName, Double amount, String refNo, String agCode) {

		HashMap<String, String> map = eaMap.get(product);
		String DEPOSIT_URL = map.get("DEPOSIT_URL");

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put(P_USERID, loginName);
		paramsMap.put(P_ACODE, agCode);
		paramsMap.put(P_VENDORID, VENDORID);
		paramsMap.put(P_CURRENCYID, CURRENCYID);
		paramsMap.put(P_AMOUNT, NumericUtil.formatDouble(amount));
		paramsMap.put(P_REFNO, refNo);

		String xml = createRequestXML("cdeposit", generateDepositID(), paramsMap);

		return parseDepositPendingResponseBean(postXMLBySSL(HTTPS_HOST, DEPOSIT_URL, xml));
	}

	private static Map<String, Object> checkClient(String product, String loginName) {

		HashMap<String, String> map = eaMap.get(product);
		String CHECKCLIENT_URL = map.get("CHECKCLIENT_URL");

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put(P_USERID, loginName);
		paramsMap.put(P_VENDORID, VENDORID);
		paramsMap.put(P_CURRENCYID, CURRENCYID);

		String xml = createRequestXML("ccheckclient", generateCheckClientID(), paramsMap);

		Map<String, Object> resultMap = parseCheckClientResponseBean(postXMLBySSLShort(HTTPS_HOST, CHECKCLIENT_URL, xml));

		return resultMap;
	}

	private static String generateWithdrawID() {

		return "W" + getDateFmtID();
	}

	private static String generateCheckClientID() {

		return "C" + getDateFmtID();
	}

	private static String generateDepositID() {

		return "D" + getDateFmtID();
	}

	private static String getDateFmtID() {

		SimpleDateFormat idFmt = new SimpleDateFormat("yyyyMMddHHmmssssss");

		return idFmt.format(new Date());
	}

	private static String createRequestXML(String action, String id, Map<String, Object> paramsMap) {

		String xml = XML_DECLARATION + NEWLINE + "<request action=\"" + action + "\">" + NEWLINE + "<element id=\"" + id + "\">" + NEWLINE;

		Iterator<Entry<String, Object>> it = paramsMap.entrySet().iterator();

		while (it.hasNext()) {

			Entry<String, Object> entry = it.next();

			if (StringUtils.isNotEmpty(entry.getKey())) {

				xml += "<properties name=\"" + entry.getKey() + "\">" + StringUtils.trimToEmpty(entry.getValue().toString()) + "</properties>" + NEWLINE;
			}
		}

		xml += "</element>" + NEWLINE;
		xml += "</request>" + NEWLINE;

		return xml;
	}

	private static String postXMLBySSL(String host, String url, String xml) {

		int statusCode = -999;

		PostMethod post = new PostMethod(EA_GAME_URL + "trade.htm");
		post.setRequestHeader("Connection", "close");

		HttpClient client = null;

		try {

			post.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=" + Constant.ENCODE + "");

			NameValuePair[] param = { new NameValuePair("host", host), new NameValuePair("url", url), new NameValuePair("xml", xml) };

			post.setRequestBody(param);

			client = HttpUtil.createHttpClient();

			statusCode = client.executeMethod(post);

			if (statusCode != 200) {

				log.error("the post status is not ok，HttpStatus:" + HttpStatus.getStatusText(statusCode));
				return null;
			}

			String responseBody = post.getResponseBodyAsString();

			return responseBody;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			if (post != null) {

				post.releaseConnection();
			}
		}

		return null;
	}

	private static String postXMLBySSLShort(String host, String url, String xml) {

		int statusCode = -999;

		PostMethod post = new PostMethod(EA_GAME_URL + "balance.htm");
		post.setRequestHeader("Connection", "close");

		HttpClient client = null;

		try {

			post.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=" + Constant.ENCODE + "");

			NameValuePair[] param = { new NameValuePair("host", host), new NameValuePair("url", url), new NameValuePair("xml", xml) };

			post.setRequestBody(param);

			client = HttpUtil.createHttpClientShort();

			statusCode = client.executeMethod(post);

			if (statusCode != 200) {

				log.error("the post status is not ok，HttpStatus:" + HttpStatus.getStatusText(statusCode));
				return null;
			}

			String responseBody = post.getResponseBodyAsString();

			return responseBody;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			if (post != null) {

				post.releaseConnection();
			}
		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, Object> parseDepositPendingResponseBean(String xml) {

		Map<String, Object> resultMap = null;

		if (StringUtils.isBlank(xml)) {

			return resultMap;
		}

		xml = StringUtils.trimToEmpty(xml);

		Document doc = string2Document(xml);

		if (doc != null) {

			resultMap = new HashMap<String, Object>();

			Element element = (Element) doc.getRootElement().selectSingleNode("//request/element");
			String id = element.attributeValue(A_ID);

			String agcode = null;
			String status = null;
			String refno = null;
			String paymentid = null;
			String errdesc = null;

			Iterator it = element.elements("properties").listIterator();

			while (it.hasNext()) {

				Element elem = (Element) it.next();

				if (elem.attributeValue(A_NAME).equals(P_ACODE)) {

					agcode = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_STATUS)) {

					status = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_REFNO)) {

					refno = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_PAYMENTID)) {

					paymentid = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_ERRDESC)) {

					errdesc = elem.getTextTrim();
				}

				resultMap.put("id", id);
				resultMap.put("agcode", agcode);
				resultMap.put("status", status);
				resultMap.put("refno", refno);
				resultMap.put("paymentid", paymentid);
				resultMap.put("errdesc", errdesc);
			}
		} else {

			log.error("document parse failed：" + xml);
		}

		return resultMap;
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, Object> parseWithdrawalConfirmationResponseBean(String xml) {

		Map<String, Object> resultMap = null;

		if (StringUtils.isBlank(xml)) {

			return resultMap;
		}

		xml = StringUtils.trimToEmpty(xml);

		Document doc = string2Document(xml);

		if (doc != null) {

			resultMap = new HashMap<String, Object>();

			Element element = (Element) doc.getRootElement().selectSingleNode("//request/element");
			String id = element.attributeValue(A_ID);

			String agcode = null;
			String status = null;
			String refno = null;
			String paymentid = null;
			String errdesc = null;

			Iterator it = element.elements("properties").listIterator();

			while (it.hasNext()) {

				Element elem = (Element) it.next();

				if (elem.attributeValue(A_NAME).equals(P_ACODE)) {

					agcode = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_STATUS)) {

					status = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_REFNO)) {

					refno = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_PAYMENTID)) {

					paymentid = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_ERRDESC)) {

					errdesc = elem.getTextTrim();
				}

				resultMap.put("id", id);
				resultMap.put("agcode", agcode);
				resultMap.put("status", status);
				resultMap.put("refno", refno);
				resultMap.put("paymentid", paymentid);
				resultMap.put("errdesc", errdesc);
			}
		} else {

			log.error("document parse failed：" + xml);
		}

		return resultMap;
	}

	@SuppressWarnings("rawtypes")
	private static Map<String, Object> parseCheckClientResponseBean(String xml) {

		Map<String, Object> resultMap = null;

		if (StringUtils.isBlank(xml)) {

			return resultMap;
		}

		xml = StringUtils.trimToEmpty(xml);

		Document doc = string2Document(xml);

		if (doc != null) {

			resultMap = new HashMap<String, Object>();

			Element element = (Element) doc.getRootElement().selectSingleNode("//request/element");
			String id = element.attributeValue(A_ID);

			String userid = null;
			Double balance = null;
			String currencyid = null;
			String location = null;

			Iterator it = element.elements("properties").listIterator();

			while (it.hasNext()) {

				Element elem = (Element) it.next();

				if (elem.attributeValue(A_NAME).equals(P_USERID)) {

					userid = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_BALANCE)) {

					balance = Double.parseDouble(elem.getTextTrim());
				} else if (elem.attributeValue(A_NAME).equals(P_CURRENCYID)) {

					currencyid = elem.getTextTrim().equals("") ? "-1" : elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_LOCATION)) {

					location = elem.getTextTrim();
				}
			}

			resultMap.put("id", id);
			resultMap.put("userid", userid);
			resultMap.put("balance", balance);
			resultMap.put("currencyid", currencyid);
			resultMap.put("location", location);
		} else {

			log.error("document parse failed：" + xml);
		}

		return resultMap;
	}

	private static Document string2Document(String s) {

		Document doc = null;

		try {

			doc = DocumentHelper.parseText(s);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return doc;
	}

	public static void main(String[] args) throws Exception {

		System.out.println(getPlayerMoney("qy", "devtest999"));
//		System.out.println(getDepositPlayerMoney("qy", "devtest999", 1.0, "323456789"));
//		System.out.println(getWithdrawPlayerMoney("qy", "devtest999", 2.0, "423456789"));
	}
}