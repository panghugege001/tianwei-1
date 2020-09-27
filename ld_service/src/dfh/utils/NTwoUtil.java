package dfh.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import java.util.Map.Entry;

import org.apache.log4j.Logger;

import dfh.exception.PostFailedException;
import dfh.exception.RemoteApiException;
import dfh.exception.ResponseFailedException;
import dfh.model.PlatformData;
import dfh.model.enums.NTwoXmlEnum;
import dfh.remote.DocumentParser;
import dfh.remote.ErrorCode;
import dfh.remote.RemoteConstant;
import dfh.remote.bean.NTwoCheckClientResponseBean;
import dfh.remote.bean.NTwoClientBetBean;
import dfh.remote.bean.NTwoDepositPendingResponseBean;
import dfh.remote.bean.NTwoWithdrawalConfirmationResponseBean;
import dfh.remote.bean.Property;

public class NTwoUtil {
	
	private static Logger log = Logger.getLogger(NTwoUtil.class);
	//******************测试环境參數********************
//	public static final String DOMAINNAME = "n2webpc-jiekould.com";//N2Live手机网页版域名, N2Live游戏登入帐密e68test01~e68test05/test1234
//	public static String NTWO_HTTPS_HOST = "stgmerchanttalk.azuritebox.com";
//	private static final String VENDOR_ID = "88";
//	private static final String MERCHANT_PASSCODE = "55327A13AA2B4538F390D01363AF44B9"; 
	//******************测试环境參數********************
	
	//******************正式环境參數********************
	public static final String DOMAINNAME = "n2webpc-longdu.com";//N2Live手机网页版域名, N2Live游戏登入帐密e68protest01~e68protest05/123def
	public static String NTWO_HTTPS_HOST = "merchanttalk.azuritebox.com";
	private static final String VENDOR_ID = "75";
	private static final String MERCHANT_PASSCODE = "D5E157590791396E1482E3A917FF30C4"; 
	//******************正式环境參數********************
	
	public static final String PLATFORM = "n2live";
	public static final String MERCHANT_CODE = "ld";
	public static final String HTTP = "http://";
	public static final String AZURITEAPP = "azuriteapp://";
	
	public static final String SINGLELOGIN_URL = "/SingleLogin";
	
	
	public static String NTWO_DEPOSIT_URL = "/transaction/PlayerDeposit";
	public static String NTWO_WITHDRAW_URL = "/transaction/PlayerWithdrawal";
	public static String NTWO_CHECKCLIENT_URL = "/transaction/CheckClient";
	public static String NTWO_GAMEINFO_URL = "/Trading/GameInfo";
	
	
	private static final String TIMEZONE_DIFF = "480";//设定特定开始日期和结束日期的时区偏差(分钟)
	private static List<String> testAcocuntList = Arrays.asList("ldtest01", "ldtest02", "ldtest03", "ldtest04", "ldtest05", "ldttc01", "ldttc02", "ldttc03", "ldttc04", "ldttc05");
	
	public static String getNTwoGameLoginValidationResponseXml(String id, String userid, String username, String status, String errdesc, String acode) {
		log.info("getNTwoGameLoginValidationResponseXml...");
		Map<String, String> propMap = new LinkedHashMap<String, String>();
		propMap.put(RemoteConstant.P_USERID, userid);
		propMap.put(RemoteConstant.P_USERNAME, username);
		propMap.put(RemoteConstant.P_ACODE, acode);
		propMap.put(RemoteConstant.P_VENDORID, getNTwoVendorID());
		propMap.put(RemoteConstant.P_MERCHANTPASSCODE, getNTwoMerchantPasscode());
		propMap.put(RemoteConstant.P_CURRENCYID, getCurrencyId(userid));
		propMap.put(RemoteConstant.P_STATUS, status);
		propMap.put(RemoteConstant.P_ERRDESC, errdesc);
		return DocumentParser.createNTwoResponseXML(NTwoXmlEnum.CLOGIN.getAction(), id, propMap, getLoginStatus(status));
	}
	
	public static String getNTwoGameAutoLoginValidationResponseXml(String id, String userid, String username, String status, String errdesc, String acode, String uuid, String clientIp) {
		log.info("getNTwoGameAutoLoginValidationResponseXml...");
		Map<String, String> propMap = new LinkedHashMap<String, String>();
		propMap.put(RemoteConstant.P_USERID, userid);
		propMap.put(RemoteConstant.P_USERNAME, username);
		propMap.put(RemoteConstant.P_UUID, uuid);

		propMap.put(RemoteConstant.P_VENDORID, getNTwoVendorID());
		propMap.put(RemoteConstant.P_MERCHANTPASSCODE, getNTwoMerchantPasscode());
		propMap.put(RemoteConstant.P_CLIENTIP, clientIp);
		propMap.put(RemoteConstant.P_CURRENCYID, getCurrencyId(userid));
		propMap.put(RemoteConstant.P_ACODE, acode);
		propMap.put(RemoteConstant.P_ERRDESC, errdesc);
		propMap.put(RemoteConstant.P_STATUS, status);
		return DocumentParser.createNTwoResponseXML(NTwoXmlEnum.USERVERF.getAction(), id, propMap, getLoginStatus(status));
	}
	
	public static String getNTwoGameTicketResponeXml(String id, String userName, String ticket, String status) {
		log.info("getNTwoGameTicketResponeXml...");
		Map<String, String> propMap = new LinkedHashMap<String, String>();
		propMap.put(RemoteConstant.P_USERNAME, userName);
		propMap.put(RemoteConstant.P_TICKET, ticket);
		propMap.put(RemoteConstant.P_STATUS, status);
		return DocumentParser.createNTwoResponseXML(NTwoXmlEnum.CGETTICKET.getAction(), id, propMap, getLoginStatus(status));
	}
	
	public static NTwoDepositPendingResponseBean depositPendingRequest(String userid, Double amount, String refno, String acode) throws Exception {
		log.info("NTwoDepositPendingRequest...");
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_USERID, userid));
		list.add(new Property(RemoteConstant.P_ACODE, acode));
		list.add(new Property(RemoteConstant.P_VENDORID, getNTwoVendorID()));
		list.add(new Property(RemoteConstant.P_MERCHANTPASSCODE, getNTwoMerchantPasscode()));
		list.add(new Property(RemoteConstant.P_CURRENCYID, getCurrencyId(userid)));
		list.add(new Property(RemoteConstant.P_Amount, NumericUtil.formatDouble(amount)));
		list.add(new Property(RemoteConstant.P_REFNO, refno));

		String xml = DocumentParser.createNTwoRequestXML(NTwoXmlEnum.CDEPOSIT.getAction(), generateDepositID(), list);
		NTwoDepositPendingResponseBean bean = DocumentParser.parseNTwoDepositPendingResponseBean(HttpUtils.postNTwoXMLBySSL(NTWO_HTTPS_HOST, NTWO_DEPOSIT_URL, xml));
		if (bean.isFail()) {
			log.error("cdeposit fail:" + bean.getErrdesc());
		}
		return bean;
	}
	
	public static NTwoDepositPendingResponseBean depositConfirmationResponse(String id, String userid, String status, String paymentid, String acode) throws PostFailedException, ResponseFailedException, RemoteApiException {
		log.info("NTwoDepositConfirmationResponse...");
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_ACODE, acode));
		list.add(new Property(RemoteConstant.P_STATUS, status));
		list.add(new Property(RemoteConstant.P_PAYMENTID, paymentid));
		list.add(new Property(RemoteConstant.P_VENDORID, getNTwoVendorID()));
		list.add(new Property(RemoteConstant.P_MERCHANTPASSCODE, getNTwoMerchantPasscode()));
		list.add(new Property(RemoteConstant.P_ERRDESC, ""));

		String xml = DocumentParser.createNTwoRequestXML(NTwoXmlEnum.CDEPOSIT_CONFIEM.getAction(), id, list);
		NTwoDepositPendingResponseBean bean = DocumentParser.parseNTwoDepositPendingResponseBean(HttpUtils.postNTwoXMLBySSL(NTWO_HTTPS_HOST, NTWO_DEPOSIT_URL, xml));
		if (bean.isFail()) {
			log.error("cdeposit-confirm fail:" + bean.getErrdesc());
			throw new RemoteApiException("cdeposit-confirm fail:" + bean.getErrdesc());
		}
		return bean;
	}
	
	public static NTwoWithdrawalConfirmationResponseBean withdrawPendingRequest(String userid, Double amount, String refno) throws PostFailedException, ResponseFailedException {
		log.info("NTwoWithdrawPendingRequest...");
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_USERID, userid));
		list.add(new Property(RemoteConstant.P_VENDORID, getNTwoVendorID()));
		list.add(new Property(RemoteConstant.P_MERCHANTPASSCODE, getNTwoMerchantPasscode()));
		list.add(new Property(RemoteConstant.P_Amount, NumericUtil.formatDouble(amount)));
		list.add(new Property(RemoteConstant.P_CURRENCYID, getCurrencyId(userid)));
		list.add(new Property(RemoteConstant.P_REFNO, refno));
		String xml = DocumentParser.createNTwoRequestXML(NTwoXmlEnum.CWITHDRAWAL.getAction(), generateWithdrawID(), list);
		NTwoWithdrawalConfirmationResponseBean bean = DocumentParser.parseNTwoWithdrawalConfirmationResponseBean(HttpUtils.postNTwoXMLBySSL(NTWO_HTTPS_HOST, NTWO_WITHDRAW_URL, xml)); 
		if (bean.isFail()) {
			log.error("cwithdrawal fail:" + bean.getErrdesc());
		}
		return bean;
	}
	
	public static NTwoCheckClientResponseBean checkClient(String userid) throws PostFailedException, ResponseFailedException {
		log.info("NTwoCheckClient...");
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_USERID, userid));
		list.add(new Property(RemoteConstant.P_VENDORID, getNTwoVendorID()));
		list.add(new Property(RemoteConstant.P_MERCHANTPASSCODE, getNTwoMerchantPasscode()));
		list.add(new Property(RemoteConstant.P_CURRENCYID, getCurrencyId(userid)));
		String xml = DocumentParser.createNTwoRequestXML(NTwoXmlEnum.CCHECKCLIENT.getAction(), generateCheckClientID(), list);
		NTwoCheckClientResponseBean bean = DocumentParser.parseNTwoCheckClientResponseBean(HttpUtils.postNTwoXMLBySSL(NTWO_HTTPS_HOST, NTWO_CHECKCLIENT_URL, xml));
		if (bean.isFail()) {
			log.error("ccheckclient fail:" + bean.getErrdesc());
		}
		return bean;
	}
	
	public static Map<String, NTwoClientBetBean> gameInfoPendingRequest(Calendar searchDate) throws Exception {
		log.info("NTwoGameInfoPendingRequest...");
		Calendar now = Calendar.getInstance();
		if (searchDate.after(now)) {
			throw new Exception("不支持未來日期");
		}
		Calendar startDate = getStartDate(searchDate);
		Calendar endDate = getEndDate(searchDate);
		
		if (endDate.after(now)) {
			now.add(Calendar.MINUTE, -16);
			endDate.setTime(now.getTime());//結束日期必須是當前時間的15分鐘之前
		}
		
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.P_VENDORID, getNTwoVendorID()));
		list.add(new Property(RemoteConstant.P_MERCHANTPASSCODE, getNTwoMerchantPasscode()));
		list.add(new Property(RemoteConstant.P_STARTDATE, DateUtil.formatDateForStandard(startDate.getTime())));
		list.add(new Property(RemoteConstant.P_ENDDATE, DateUtil.formatDateForStandard(endDate.getTime())));
		list.add(new Property(RemoteConstant.P_TIMEZONE, TIMEZONE_DIFF));
		String xml = DocumentParser.createNTwoGameInfoRequestXML(NTwoXmlEnum.GAMEINFO.getAction(), list);
		Map<String, NTwoClientBetBean> beanMap = DocumentParser.parseNTwoClientBetBeanResponse(HttpUtils.postNTwoXMLBySSL_GZIP(NTWO_HTTPS_HOST, NTWO_GAMEINFO_URL, xml));
		return beanMap;
	}
	
	/**
	 * 搜寻前一天 12:00:00 到当天11:59:59
	 * @param beanMap
	 * @param searchDate
	 * @return
	 */
	public static List<PlatformData> convertToPlatformData(Map<String, NTwoClientBetBean> beanMap, Calendar searchDate) {
		List<PlatformData> result = new ArrayList<PlatformData>();
		Date sDate = getStartDate(searchDate).getTime();
		Date eDate = getEndDate(searchDate).getTime();
		for (Entry<String, NTwoClientBetBean> entry : beanMap.entrySet()) {
			PlatformData data = new PlatformData();
			data.setUuid(UUID.randomUUID().toString());
			data.setPlatform(PLATFORM);
			data.setLoginname(entry.getKey());
			data.setBet(entry.getValue().getBetAmount().doubleValue());
			data.setProfit(entry.getValue().getHold().negate().doubleValue());
			data.setStarttime(sDate);
			data.setEndtime(eDate);
			data.setUpdatetime(new Date());
			result.add(data);
		}
		return result;
	}
	
	private static String getNTwoVendorID() {
		return VENDOR_ID;
	}
	
	private static String getNTwoMerchantPasscode() {
		return MERCHANT_PASSCODE;
	}
	
	private static String getCurrencyId(String userId){
		if (testAcocuntList.contains(userId)) {
			return RemoteConstant.TESTCURRENCYID;
		} else {
			return RemoteConstant.CURRENCYID;
		}
		
	}
	
	private static String getLoginStatus(String status) {
		return ErrorCode.SUCCESS.getCode().equalsIgnoreCase(status) ? "success" : "fail";//success需小写,不然N2 Live接口会判断错误
	}
	/**
	 * 签名格式为:玩家登入ID + 日期 + key, 使用32位数的MD5加密玩家登入ID使用小写, Key是预设好供签名生成用途的ID （请参考由真人娱乐场提供的merchant passcode ）
	 * @param username
	 * @param date
	 * @param sign
	 * @return
	 * @throws Exception
	 */
	public static boolean validSign(String username, String date, String sign) throws Exception {
		return date.equals(DateUtil.fmtyyyyMMdd(Calendar.getInstance().getTime())) && sign.equals(MD5(username.toLowerCase() + date +  getNTwoMerchantPasscode()));
	}
	
	private static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private static String generateDepositID() {
		return "D" + DateUtil.getDateFmtID();
	}
	
	private static String generateWithdrawID() {
		return "W" + DateUtil.getDateFmtID();
	}
	
	private static String generateCheckClientID() {
		return "C" + DateUtil.getDateFmtID(); 
	}
	
	private static String generateCheckAffiliateID() {
		return "CF" + DateUtil.getDateFmtID();
	}
	
	private static Calendar getStartDate(Calendar searchDate) {
		Calendar calendar = (Calendar)searchDate.clone();
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}
	
	private static Calendar getEndDate(Calendar searchDate) {
		Calendar calendar = (Calendar)searchDate.clone();
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 11);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar;
	}
	
	
	
	
	//There is test code below
	public static void main(String args[]) throws Exception {
		
		//String md5 = MD5("tai_test20160628"+SIGNKEY);
		testNTwoDepositPendingRequest();
		//testNTwoWithdrawPendingRequest();
		//testNTwoCheckClient();
		//testNTwoGameInfoPendingRequest();
	}
	
	private static final String testUserID = "ldcny01";
	
	private static void testNTwoDepositPendingRequest() throws Exception {
		String userId = testUserID;
		NTwoDepositPendingResponseBean bean = depositPendingRequest(userId, 10000.00, String.valueOf(System.currentTimeMillis()), "0011000000000524");
		depositConfirmationResponse(bean.getId(), userId, bean.getStatus(), bean.getPaymentid(), bean.getAgcode());
	}
	
	private static void testNTwoWithdrawPendingRequest() throws Exception {
		String userId = testUserID;
		withdrawPendingRequest(userId, 10.00, String.valueOf(System.currentTimeMillis()));
	}
	
	private static void testNTwoCheckClient() throws Exception {
		String userId = testUserID;
		checkClient(userId);
	}
	
	private static void testNTwoGameInfoPendingRequest() throws Exception {
		Calendar searchDate = Calendar.getInstance();
		gameInfoPendingRequest(searchDate);
	}
}
