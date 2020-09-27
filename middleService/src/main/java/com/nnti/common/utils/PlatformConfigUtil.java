package com.nnti.common.utils;

import java.util.HashMap;
import java.util.Map;

import com.nnti.common.constants.PlatformCode;

public class PlatformConfigUtil {

	protected static Map<String, HashMap<String, String>> ptMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> ntMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> qtMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> mgMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> dtMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> ttgMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> pngMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> aginMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> ntwoMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> sbMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> mwgMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> eaMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> ebetMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> slotMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> ogMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> chessMap = new HashMap<String, HashMap<String, String>>();
	protected static Map<String, HashMap<String, String>> swMap = new HashMap<String, HashMap<String, String>>();


	private static final String QT_BASE_URL = "https://api.qtplatform.com";

	static {

		/************************************************************PT平台相关配置开始处************************************************************/
		/**大运相关配置开始处**/
		HashMap<String, String> yhMap = new HashMap<String, String>();
		yhMap.put("PALY_START", "DY8");
		yhMap.put("PT_SERVICE", "https://kioskpublicapi.mistral88.com");
		yhMap.put("PT_KEY", "da77d0e8d0c6231b2d768f12b3d1a5035e57490fadb323f9f4c3161ac272a0d6264a45143fe0ff37d68571252b613c89a740ed14340e3a4e9abed68677392bd3");
		yhMap.put("KIOSKNAME", "DY8");
		yhMap.put("ADMINNAME", "DY8ADMIN");
		yhMap.put("PT_PASSWORD", "WpuxWHd8IzQVPtLm");
		yhMap.put("trackingId", "dy");
		ptMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		HashMap<String, String> ldMap = new HashMap<String, String>();
		ldMap.put("PALY_START", "TW8");
		ldMap.put("PT_SERVICE", "https://kioskpublicapi.mistral88.com");
		ldMap.put("PT_KEY", "aa13d5340ad66ce29ea2b3eec6753aefffcf4bffd3caf8829ad624a8b9c9cade57a2fbc4664ab6a6254aa5b6d6b5642b3f23a3f5323ff94fe1c0dae993ace50d");
		ldMap.put("KIOSKNAME", "TW8");
		ldMap.put("ADMINNAME", "CNYKLTWYLAPI");
		ldMap.put("PT_PASSWORD", "WpuxWHd8IzQVPtLm");
		ldMap.put("trackingId", "tw");
		ptMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/
		

		/************************************************************PT平台相关配置结束处************************************************************/

		/************************************************************NT平台相关配置开始处************************************************************/

		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("group_id", "823");
		yhMap.put("startWith", "S");
		yhMap.put("dspurl", "http://api5.dljj99.com:8383");
		yhMap.put("skyToken", "ebetnt4");
		yhMap.put("skySecretkey", "bc52c57a755cb90b05a8aab1142094d4");
		ntMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("group_id", "3464");
		ldMap.put("startWith", "K");
		ldMap.put("dspurl", "http://api5.totemcasino.biz:8383");
		ldMap.put("skyToken", "ntlongdu");
		ldMap.put("skySecretkey", "afbeHmDw6jxxT6wpStWWl0u4HbjzpOEh");
		ntMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/

		/************************************************************NT平台相关配置结束处************************************************************/

		/************************************************************QT平台相关配置开始处************************************************************/

		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("API_USER", "api_dayun");
		yhMap.put("API_PASSWORD", "1YfE9IbR");
		yhMap.put("ACCESS_TOKEN_URL", QT_BASE_URL + "/v1/auth/token?grant_type=password&response_type=token&username={username}&password={password}");
		yhMap.put("PLAYER_BALANCE_URL", QT_BASE_URL + "/v1/wallet/ext/{playerId}");
		yhMap.put("REVOKE_TOKEN_URL", QT_BASE_URL + "/v1/auth/token");
		yhMap.put("PREPARE_TRANSFER_URL", QT_BASE_URL + "/v1/fund-transfers");
		yhMap.put("COMPLETE_TRANSFER_URL", QT_BASE_URL + "/v1/fund-transfers/{transferId}/status");
		qtMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("API_USER", "api_ld");
		ldMap.put("API_PASSWORD", "cc95f35d");
		ldMap.put("ACCESS_TOKEN_URL", QT_BASE_URL + "/v1/auth/token?grant_type=password&response_type=token&username={username}&password={password}");
		ldMap.put("PLAYER_BALANCE_URL", QT_BASE_URL + "/v1/wallet/ext/{playerId}");
		ldMap.put("REVOKE_TOKEN_URL", QT_BASE_URL + "/v1/auth/token");
		ldMap.put("PREPARE_TRANSFER_URL", QT_BASE_URL + "/v1/fund-transfers");
		ldMap.put("COMPLETE_TRANSFER_URL", QT_BASE_URL + "/v1/fund-transfers/{transferId}/status");
		qtMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/

		/************************************************************QT平台相关配置结束处************************************************************/

		/************************************************************MG平台相关配置开始处************************************************************/

		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("ServerID", "18257");
		yhMap.put("gameUrl_real", "https://redirector3.valueactive.eu/Casino/Default.aspx?applicationid=1023&theme=quickfiressl&variant=TNG&usertype=0&sext1=genauth&sext2=genauth&ul=zh");
		yhMap.put("h5gameUrl_real", "https://mobile22.gameassists.co.uk/MobileWebServices_40/casino/game/launch");
		yhMap.put("h5LobbyName", "yahucom");
		yhMap.put("mgconsole", "http://yahu.mgsjiekou.com/api/microgaming/");
		yhMap.put("key", "GSMCkGCSqGSIb3DQEBAQUAAsUCcsCBiQKBgQCEOUjtnkQtkPoNdWnFRBwW+Z/ftUYz5cNFKgb+rvkJko+OltGairQpSRYYc7dDMuwiW1oYu58iOw0ETenPM+NHtu/88yJJVCDYPCKCBSWKCdsph/ospR7YtGWrnFjYHJ1nDUmHnUCC9ENdsat8a77KwFwaM5pkM0gvQ9nsdmNhwIDAkkS");
		mgMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("ServerID", "21613");
		ldMap.put("gameUrl_real", "https://redirector3.valueactive.eu/Casino/Default.aspx?applicationid=1023&theme=quickfiressl&variant=TNG&usertype=0&sext1=genauth&sext2=genauth&ul=zh");
		ldMap.put("h5gameUrl_real", "https://mobile22.gameassists.co.uk/MobileWebServices_40/casino/game/launch");
		ldMap.put("h5LobbyName", "LDcom");
		ldMap.put("mgconsole", "http://oldlongdu.mgsjiekou.com/api/microgaming/");
		ldMap.put("key", "GSMCkGCSqGSIb3DQEBAQUAAsUCcsCBiQKBgQCEOUjtnkQtkPoNdWnFRBwW+Z/ftUYz5cNFKgb+rvkJko+OltGairQpSRYYc7dDMuwiW1oYu58iOw0ETenPM+NHtu/88yJJVCDYPCKCBSWKCdsph/ospR7YtGWrnFjYHJ1nDUmHnUCC9ENdsat8a77KwFwaM5pkM0gvQ9nsdmNhwIDAkkS");
		mgMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/

		/************************************************************MG平台相关配置结束处************************************************************/

		/************************************************************DT平台相关配置开始处************************************************************/

		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("BUSINESS", "NNTI_FHC_DYGJ");
		yhMap.put("URL", "http://api-dtservice.com/dtApi.html");
		yhMap.put("APIKEY", "LYGWcgO1qwfvG23fUPJQbw==");
		dtMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("BUSINESS", "NNTI_FHC_TWYL");
		ldMap.put("URL", "http://api-dtservice.com/dtApi.html");
		ldMap.put("APIKEY", "OZusgRNOgnxXmsOkojXxbw==");
		dtMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/
		
		/************************************************************DT平台相关配置结束处************************************************************/

		/************************************************************TTG平台相关配置开始处************************************************************/
		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("PLAYER_URL", "https://ams5-api.ttms.co:8443/cip/player/");//https://ams-api.stg.ttms.co:8443
		yhMap.put("PALY_START", "DY8_");
		yhMap.put("TRANSACTION_URL", "https://ams5-api.ttms.co:8443/cip/transaction/DY8/");
		ttgMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("PLAYER_URL", "https://ams-api.stg.ttms.co:8443/cip/player/");
		ldMap.put("PALY_START", "LF123_k");
		ldMap.put("TRANSACTION_URL", "https://ams-api.stg.ttms.co:8443/cip/transaction/DY8/");
		ttgMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/

		/************************************************************TTG平台相关配置结束处************************************************************/

		/************************************************************PNG平台相关配置开始处************************************************************/

		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("PREFIX", "DY8");
		yhMap.put("BRANDID", "dayun");
		yhMap.put("PID", PNGUtil.PID);
		yhMap.put("PNG_URL_SERVICE", PNGUtil.PNG_URL_SERVICE);
		yhMap.put("SOAP_URL_ACTION", PNGUtil.SOAP_URL_ACTION);
		yhMap.put("USERNAME", PNGUtil.USERNAME);
		yhMap.put("PWD", PNGUtil.PWD);
		pngMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("PREFIX", "k_");
		ldMap.put("BRANDID", "LONGDO");
		ldMap.put("PID", PNGUtil.PID);
		ldMap.put("PNG_URL_SERVICE", PNGUtil.PNG_URL_SERVICE);
		ldMap.put("SOAP_URL_ACTION", PNGUtil.SOAP_URL_ACTION);
		ldMap.put("USERNAME", PNGUtil.USERNAME);
		ldMap.put("PWD", PNGUtil.PWD);
		pngMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/

		/************************************************************PNG平台相关配置结束处************************************************************/

		/************************************************************AGIN平台相关配置开始处************************************************************/
		
		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("PREFIX", "DY8");
		aginMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("PREFIX", "TW8");
		aginMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/

		/************************************************************AGIN平台相关配置结束处************************************************************/

		/************************************************************NTWO平台相关配置开始处************************************************************/

		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("VENDOR_ID", "53");
		yhMap.put("MERCHANT_PASSCODE", "CE404460391DFB5DC9A2148CD04EB4D3");
		ntwoMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("VENDOR_ID", "75");
		ldMap.put("MERCHANT_PASSCODE", "D5E157590791396E1482E3A917FF30C4");
		ntwoMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/

		/************************************************************NTWO平台相关配置结束处************************************************************/

		/************************************************************沙巴体育平台相关配置开始处************************************************************/

		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("PREFIX", "DY8");
		sbMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("PREFIX", "TW8");
		sbMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/

		/************************************************************沙巴体育平台相关配置结束处************************************************************/
		
		/************************************************************mwg平台相关配置开始处************************************************************/
		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("siteId", "10005204");
		yhMap.put("merchantId", "yh");
		yhMap.put("prefix", "g_");
		mwgMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("siteId", "10005210");
		ldMap.put("merchantId", "ld");
		ldMap.put("prefix", "k_");
		mwgMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/
		
		/************************************************************mwg平台相关配置结束处************************************************************/
		
		/************************************************************og平台相关配置开始处************************************************************/
		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("secrect_key", "");
		yhMap.put("url", "");
		yhMap.put("prefix", "g_");
		ogMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("secrect_key", "");
		ldMap.put("url", "");
		ldMap.put("prefix", "k_");
		ogMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/
		
		/************************************************************og平台相关配置结束处************************************************************/

		/************************************************************EA平台相关配置开始处************************************************************/
		/**大运相关配置开始处**/
		yhMap = new HashMap<String, String>();
		yhMap.put("CHECKCLIENT_URL", "");
		yhMap.put("DEPOSIT_URL", "");
		yhMap.put("WITHDRAW_URL", "");
		eaMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap = new HashMap<String, String>();
		ldMap.put("CHECKCLIENT_URL", "");
		ldMap.put("DEPOSIT_URL", "");
		ldMap.put("WITHDRAW_URL", "");
		eaMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/

		/************************************************************EA平台相关配置结束处************************************************************/

		/************************************************************棋乐游平台相关配置开始处************************************************************/
		/**大运相关配置开始处**/
		yhMap.put("PREFIX", "DY8");
		yhMap.put("AGENT", "1041");
		yhMap.put("APPKEY", "3dc602f6e8ccd71c7908beb2bbc2db49");
		yhMap.put("CG_URL", "http://tapi.761city.com:10018/agent/");
		chessMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap.put("PREFIX", "DY8");
		ldMap.put("AGENT", "1041");
		ldMap.put("APPKEY", "3dc602f6e8ccd71c7908beb2bbc2db49");
		ldMap.put("CG_URL", "http://tapi.761city.com:10018/agent/");
		chessMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/
		
		/************************************************************棋乐游平台相关配置结束处************************************************************/
		
		/************************************************************SW平台相关配置开始处************************************************************/
		/**大运相关配置开始处**/
		yhMap.put("prefix", "DY8");
		yhMap.put("secretKey", "7f3fdf8b-09c6-47cf-8553-e7c4c18da812");
		yhMap.put("username", "Dayun_user");
		yhMap.put("password", "QAZqazQAZ123");
		swMap.put(PlatformCode.ProductSign.DY.getProduct(), yhMap);
		/**大运相关配置结束处**/
		/**天威相关配置开始处**/
		ldMap.put("prefix", "DY8");
		ldMap.put("secretKey", "897c9ece-d39e-4840-a398-256644a2c732");
		ldMap.put("username", "dayun_user");
		ldMap.put("password", "Dayun@123");
		swMap.put(PlatformCode.ProductSign.TW.getProduct(), ldMap);
		/**天威相关配置结束处**/
		
		/************************************************************SW平台相关配置结束处************************************************************/
	}
}