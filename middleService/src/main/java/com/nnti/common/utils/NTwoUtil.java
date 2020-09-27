package com.nnti.common.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.nnti.common.constants.NTwoXmlEnum;

public class NTwoUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(NTwoUtil.class);

	private static final String CURRENCYID = "156";
	private static final String A_ID = "id";
	private static final String A_NAME = "name";
	private static final String P_STATUS = "status";
	private static final String P_BALANCE = "balance";
	private static final String P_ERRDESC = "errdesc";
	private static final String P_USERID = "userid";
	private static final String P_VENDORID = "vendorid";
	private static final String P_MERCHANTPASSCODE = "merchantpasscode";
	private static final String P_CURRENCYID = "currencyid";
	private static final String NTWO_DEPOSIT_URL = "/transaction/PlayerDeposit";
	private static final String NTWO_CHECKCLIENT_URL = "/transaction/CheckClient";
	private static final String NTWO_WITHDRAW_URL = "/transaction/PlayerWithdrawal";
	private static final String P_ACODE = "acode";
	private static final String P_AMOUNT = "amount";
	private static final String P_REFNO = "refno";
	private static final String P_PAYMENTID = "paymentid";

	// 正式环境
	private static final String NTWO_HTTPS_HOST = "merchanttalk.azuritebox.com";

	// 获取远程游戏金额
	public static Double getPlayerMoney(String product, String loginName) {

		Map<String, Object> resultMap = checkClient(product, loginName);

		if (null != resultMap.get("status") && "0".equals(String.valueOf(resultMap.get("status")))) {

			return Double.parseDouble(String.valueOf(resultMap.get("balance")));
		}

		return null;
	}

	// 转入游戏
	public static Boolean getDepositPlayerMoney(String product, String loginName, Double amount, String referenceId) {

		Map<String, Object> resultMap = depositPendingRequest(product, loginName, amount, referenceId, "");

		if (null != resultMap.get("status") && "0".equals(String.valueOf(resultMap.get("status")))) {

			String id = String.valueOf(resultMap.get("id"));
			String status = String.valueOf(resultMap.get("status"));
			String paymentId = String.valueOf(resultMap.get("paymentId"));
			String agCode = String.valueOf(resultMap.get("agCode"));

			resultMap = depositConfirmationResponse(product, id, loginName, status, paymentId, agCode);

			if (null != resultMap.get("status") && "0".equals(String.valueOf(resultMap.get("status")))) {

				return true;
			}

			return false;
		}

		return false;
	}

	// 转出游戏
	public static Boolean getWithdrawPlayerMoney(String product, String loginName, Double amount, String referenceId) {

		Map<String, Object> resultMap = withdrawPendingRequest(product, loginName, amount, referenceId);

		if (null != resultMap.get("status") && "0".equals(String.valueOf(resultMap.get("status")))) {

			return true;
		}

		return false;
	}

	public static Map<String, Object> withdrawPendingRequest(String product, String loginName, Double amount, String refNo) {

		HashMap<String, String> map = ntwoMap.get(product);
		String VENDOR_ID = map.get("VENDOR_ID");
		String MERCHANT_PASSCODE = map.get("MERCHANT_PASSCODE");

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put(P_USERID, loginName);
		paramsMap.put(P_VENDORID, VENDOR_ID);
		paramsMap.put(P_MERCHANTPASSCODE, MERCHANT_PASSCODE);
		paramsMap.put(P_AMOUNT, NumericUtil.formatDouble(amount));
		paramsMap.put(P_CURRENCYID, getCurrencyId(loginName));
		paramsMap.put(P_REFNO, refNo);

		String xml = createNTwoRequestXML(NTwoXmlEnum.CWITHDRAWAL.getAction(), generateWithdrawID(), paramsMap);
		Map<String, Object> resultMap = parseNTwoDepositPendingResponseBean(postNTwoXMLBySSL(NTWO_HTTPS_HOST, NTWO_WITHDRAW_URL, xml));

		return resultMap;
	}

	public static Map<String, Object> depositConfirmationResponse(String product, String id, String loginName, String status, String paymentId, String agCode) {

		HashMap<String, String> map = ntwoMap.get(product);
		String VENDOR_ID = map.get("VENDOR_ID");
		String MERCHANT_PASSCODE = map.get("MERCHANT_PASSCODE");

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put(P_ACODE, agCode);
		paramsMap.put(P_STATUS, status);
		paramsMap.put(P_PAYMENTID, paymentId);
		paramsMap.put(P_VENDORID, VENDOR_ID);
		paramsMap.put(P_MERCHANTPASSCODE, MERCHANT_PASSCODE);
		paramsMap.put(P_ERRDESC, "");

		String xml = createNTwoRequestXML(NTwoXmlEnum.CDEPOSIT_CONFIEM.getAction(), id, paramsMap);
		Map<String, Object> resultMap = parseNTwoDepositPendingResponseBean(postNTwoXMLBySSL(NTWO_HTTPS_HOST, NTWO_DEPOSIT_URL, xml));

		return resultMap;
	}

	public static Map<String, Object> depositPendingRequest(String product, String loginName, Double amount, String refNo, String agCode) {

		HashMap<String, String> map = ntwoMap.get(product);
		String VENDOR_ID = map.get("VENDOR_ID");
		String MERCHANT_PASSCODE = map.get("MERCHANT_PASSCODE");

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put(P_USERID, loginName);
		paramsMap.put(P_ACODE, agCode);
		paramsMap.put(P_VENDORID, VENDOR_ID);
		paramsMap.put(P_MERCHANTPASSCODE, MERCHANT_PASSCODE);
		paramsMap.put(P_CURRENCYID, getCurrencyId(loginName));
		paramsMap.put(P_AMOUNT, NumericUtil.formatDouble(amount));
		paramsMap.put(P_REFNO, refNo);

		String xml = createNTwoRequestXML(NTwoXmlEnum.CDEPOSIT.getAction(), generateDepositID(), paramsMap);
		Map<String, Object> resultMap = parseNTwoDepositPendingResponseBean(postNTwoXMLBySSL(NTWO_HTTPS_HOST, NTWO_DEPOSIT_URL, xml));

		return resultMap;
	}

	public static Map<String, Object> checkClient(String product, String loginName) {

		HashMap<String, String> map = ntwoMap.get(product);
		String VENDOR_ID = map.get("VENDOR_ID");
		String MERCHANT_PASSCODE = map.get("MERCHANT_PASSCODE");

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put(P_USERID, loginName);
		paramsMap.put(P_VENDORID, VENDOR_ID);
		paramsMap.put(P_MERCHANTPASSCODE, MERCHANT_PASSCODE);
		paramsMap.put(P_CURRENCYID, getCurrencyId(loginName));

		String xml = createNTwoRequestXML(NTwoXmlEnum.CCHECKCLIENT.getAction(), generateCheckClientID(), paramsMap);

		Map<String, Object> resultMap = parseNTwoCheckClientResponseBean(postNTwoXMLBySSL(NTWO_HTTPS_HOST, NTWO_CHECKCLIENT_URL, xml));

		return resultMap;
	}

	private static String getCurrencyId(String userId) {

		return CURRENCYID;
	}

	private static String generateCheckClientID() {

		return "C" + getDateFmtID();
	}

	private static String generateDepositID() {

		return "D" + getDateFmtID();
	}

	private static String generateWithdrawID() {

		return "W" + getDateFmtID();
	}

	private static String getDateFmtID() {

		SimpleDateFormat idFmt = new SimpleDateFormat("yyyyMMddHHmmssssss");

		return idFmt.format(new Date());
	}

	private static String createNTwoRequestXML(String action, String id, Map<String, Object> paramsMap) {

		StringBuffer sb = new StringBuffer();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-16\"?>");
		sb.append("<request action=\"").append(action).append("\">");
		sb.append("<element id=\"").append(id).append("\">");

		Iterator<Entry<String, Object>> it = paramsMap.entrySet().iterator();

		while (it.hasNext()) {

			Entry<String, Object> entry = it.next();

			if (StringUtils.isNotEmpty(entry.getKey())) {

				sb.append("<properties name=\"").append(entry.getKey()).append("\">").append(StringUtils.trimToEmpty(entry.getValue().toString())).append("</properties>");
			}
		}

		sb.append("</element>");
		sb.append("</request>");

		return sb.toString();
	}

	public static String postNTwoXMLBySSL(String host, String url, String xml) {

		int statusCode = -999;
		PostMethod post = new PostMethod(url);
		HttpClient client = null;

		try {

			// 指定请求内容的类型
			post.setRequestHeader("Content-type", "text/xml;charset=UTF-8");
			// 设置发送的内容
			post.setRequestEntity(new StringRequestEntity(xml));

			client = HttpUtil.createHttpClient();
			client.getHostConfiguration().setHost(host, 443, HttpUtil.https);
			statusCode = client.executeMethod(post);

			if (statusCode != 200) {

				log.warn("the post status is not ok，HttpStatus：" + HttpStatus.getStatusText(statusCode));

				return null;
			}

			return post.getResponseBodyAsString();
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
	public static Map<String, Object> parseNTwoCheckClientResponseBean(String xml) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (StringUtils.isBlank(xml)) {

			return resultMap;
		}

		xml = StringUtils.trimToEmpty(xml);

		Document doc = string2Document(xml);

		if (doc != null) {

			Element statusElement = (Element) doc.getRootElement().selectSingleNode("//status");
			String status = statusElement.getTextTrim();

			Element element = (Element) doc.getRootElement().selectSingleNode("//result/element");
			String id = element.attributeValue(A_ID);

			String userId = "";
			BigDecimal balance = BigDecimal.ZERO;
			String currencyId = "";
			String statusCode = "";
			String errDesc = "";

			Iterator it = element.elements("properties").listIterator();

			while (it.hasNext()) {

				Element elem = (Element) it.next();

				if (elem.attributeValue(A_NAME).equals(P_USERID)) {

					userId = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_STATUS)) {

					statusCode = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_BALANCE)) {

					balance = new BigDecimal(elem.getTextTrim());
				} else if (elem.attributeValue(A_NAME).equals(P_CURRENCYID)) {

					currencyId = elem.getTextTrim();

					if (StringUtils.isBlank(elem.getTextTrim())) {

						currencyId = "-1";
					}
				} else if (elem.attributeValue(A_NAME).equals(P_ERRDESC)) {

					errDesc = elem.getTextTrim();
				}
			}

			resultMap.put("status", "SUCCESS".equalsIgnoreCase(status) ? "0" : statusCode);
			resultMap.put("id", id);
			resultMap.put("userId", userId);
			resultMap.put("balance", balance);
			resultMap.put("currencyId", currencyId);
			resultMap.put("errDesc", errDesc);
		} else {

			log.error("document parse failed：" + xml);
		}

		return resultMap;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, Object> parseNTwoDepositPendingResponseBean(String xml) {

		Map<String, Object> resultMap = new HashMap<String, Object>();

		if (StringUtils.isBlank(xml)) {

			return resultMap;
		}

		xml = StringUtils.trimToEmpty(xml);

		Document doc = string2Document(xml);

		if (doc != null) {

			Element element = (Element) doc.getRootElement().selectSingleNode("//result/element");
			String id = element.attributeValue(A_ID);

			String agCode = null;
			String status = null;
			String refNo = null;
			String paymentId = null;
			String errDesc = null;

			Iterator it = element.elements("properties").listIterator();

			while (it.hasNext()) {

				Element elem = (Element) it.next();

				if (elem.attributeValue(A_NAME).equals(P_ACODE)) {

					agCode = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_STATUS)) {

					status = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_REFNO)) {

					refNo = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_PAYMENTID)) {

					paymentId = elem.getTextTrim();
				} else if (elem.attributeValue(A_NAME).equals(P_ERRDESC)) {

					errDesc = elem.getTextTrim();
				}
			}

			resultMap.put("id", id);
			resultMap.put("agCode", agCode);
			resultMap.put("status", status);
			resultMap.put("refNo", refNo);
			resultMap.put("paymentId", paymentId);
			resultMap.put("errDesc", errDesc);
		} else {

			log.error("document parse failed：" + xml);
		}

		return resultMap;
	}

	public static Document string2Document(String s) {

		Document doc = null;

		try {

			doc = DocumentHelper.parseText(s);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return doc;
	}

	public static void main(String[] args) {

		String testUserId = "devtest999";

//		System.out.println(getDepositPlayerMoney("dy", testUserId, 1.00, "0011000000000511"));
//		System.out.println(getWithdrawPlayerMoney("qy", testUserId, 2.00, "0011000000000519"));
		System.out.println(getPlayerMoney("dy", testUserId));
	}
}