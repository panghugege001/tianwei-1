package dfh.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import dfh.security.DESEncrypt;
import dfh.security.EncryptionUtil;
import net.sf.json.JSONObject;

public class AGINUtil{

	private static Logger log = Logger.getLogger(AGINUtil.class);

	private static final String cagent = "DF4_AGIN";
	private static final String prefix = "TW8";
	private static final String encrypt_key = "m9tRBmhB";
	private static final String md5encrypt_key = "fZUWuALSqTNX";
	private static final String dspurl = "https://gi.dayunjiang.com";
	private static final String gamedspurl="https://gci.dayunjiang.com";
	private static final String AG_BETS_URL = "http://agback.support/agdata/getPlayerBetsByDate.do";
	
	private static DESEncrypt  des=new DESEncrypt(encrypt_key);
	
	/**
	 * 发送post请求
	 * @param method api名称
	 * @param parameters 参数
	 * @return
	 */
	private static String sendApi(String url) {

		HttpClient httpClient = HttpUtils.createHttpClient();

		PostMethod med = new PostMethod(url);
		med.setRequestHeader("Connection", "close");
		med.addRequestHeader("User-Agent", "WEB_LIB_GI_"+cagent);

		BufferedReader reader = null;
		try {
			httpClient.executeMethod(med);
			reader = new BufferedReader(new InputStreamReader(med.getResponseBodyAsStream()));  
			StringBuffer stringBuffer = new StringBuffer();  
			String str = "";  
			while((str = reader.readLine())!=null){  
			   stringBuffer.append(str);  
			}
			String result = stringBuffer.toString();
			String value = parseDspResponseRequest(result);
			int responseCode = med.getStatusCode();
			log.info("请求的url:" + url);
			log.info("响应代码:" + responseCode);
			log.info("响应报文:"+result);
			if(responseCode != 200){
				return null;
			}
			return value;
		} catch (Exception e) {
			log.error("请求第三方异常:"+e.getMessage());
			e.printStackTrace();
		} finally {
			if (med != null) {
				med.releaseConnection();
			}
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static String getActype(String loginName) {
		boolean contain = loginName.startsWith("DY8TRY_");//测试账号
		String actype = "";
		// 如果不是测试帐号，返回1，如果是测试帐号，返回0
		if (!contain) {
			actype = "1";
		} else {
			actype = "0";
		}
		return actype;
	}
	
	public static String checkOrCreateGameAccout(String loginname) {
		String result = "";
		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/method=lg/\\\\/actype="
					+ getActype(loginname) + "/\\\\/password=" + loginname+ "/\\\\/oddtype=A/\\\\/cur=CNY";
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);
			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			result = sendApi(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	/**
	 * 
	 * @param loginname 玩家账号（试玩账号DY8TRY_开头）
	 * @param gameType
	 * @param domain
	 * @param loginid 
	 * @param mh5 mh5=y 代表 AGIN 移动网页版
	 * @return
	 */
	public static String agLogin(String loginname,String gameType,String domain,String mh5,String loginid){
		loginname = prefix + loginname;
		
		String value = checkOrCreateGameAccout(loginname);
		if("0".equals(value)){
			if("y".equals(mh5)){
				//处理字符串,开始登录游戏
				String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/dm=" + domain
						+ "/\\\\/actype=" + getActype(loginname) + "/\\\\/password=" + loginname + "/\\\\/sid=" + cagent
						+ loginid + "/\\\\/gameType=" + gameType+ "/\\\\/oddtype=A/\\\\/cur=CNY/\\\\/lang=1/\\\\/mh5=y";
				DESEncrypt des = new DESEncrypt(encrypt_key);
				try {
					String targetParams=des.encrypt(params);
					String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
					return gamedspurl+"/forwardGame.do?params="+targetParams+"&key="+key;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				//处理字符串,开始登录游戏
				String params = "cagent=" + cagent + "/\\\\/loginname=" + loginname + "/\\\\/dm=" + domain
						+ "/\\\\/actype=" + getActype(loginname) + "/\\\\/password=" + loginname + "/\\\\/sid=" + cagent
						+ loginid + "/\\\\/gameType=" + gameType+ "/\\\\/oddtype=A/\\\\/cur=CNY/\\\\/lang=1";
				DESEncrypt des = new DESEncrypt(encrypt_key);
				try {
					String targetParams=des.encrypt(params);
					String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
					return gamedspurl+"/forwardGame.do?params="+targetParams+"&key="+key;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}
	
	public static String agTryLogin(String loginname,String gameType,String domain,String mh5,String loginid){
		loginname = "TRY_"+loginname;
		return agLogin(loginname, gameType, domain, mh5, loginid);
	}

	
	
	// 查询玩家账户余额
	public static Double getBalance(String loginName) {

		try {
			String params = "cagent=" + cagent + "/\\\\/loginname=" + prefix + loginName + "/\\\\/method=gb/\\\\/actype=" + getActype(loginName) + "/\\\\/password=" + prefix + loginName;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);

			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			
			String value = sendApi(url);
			
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
		}
	}

	// 转入AG
	public static Boolean transferToAG(String loginName, Double credit, String refNo) {

		return prepareTransferCredit(loginName, credit, refNo, "IN");
	}

	// 转出AG
	public static Boolean transferFromAG(String loginName, Double credit, String refNo) {

		return prepareTransferCredit(loginName, credit, refNo, "OUT");
	}

	public static Boolean prepareTransferCredit(String loginName, Double credit, String refNo, String type) {

		try {

			String params = "cagent=" + cagent + "/\\\\/loginname=" + prefix + loginName + "/\\\\/actype=" + getActype(loginName) + "/\\\\/method=tc/\\\\/billno=" + cagent + refNo.substring(1)
							+ "/\\\\/type=" + type + "/\\\\/credit=" + credit + "/\\\\/password=" + prefix + loginName;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);

			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			String value = sendApi(url);
			log.info("玩家" + loginName + "执行" + type + "操作，金额为：" + credit + "，单号为：" + refNo + "，value参数值为：" + value);

			if (StringUtils.isBlank(value) || !value.equals("0")) {

				value = transferCreditConfirm(loginName, credit, refNo, type, 0);
				log.info("玩家" + loginName + "执行" + type + "操作，value值为空或者不等于0的情况下执行if语句，value参数值为：" + value);

				return false;
			}

			if (value.equals("0")) {

				value = transferCreditConfirm(loginName, credit, refNo, type, 1);
				log.info("玩家" + loginName + "执行" + type + "操作，value值为0的情况下执行if语句，value参数值为：" + value);

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
		}
	}

	public static String transferCreditConfirm(String loginName, Double credit, String refNo, String type, Integer flag) {

		String result = "";

		try {

			String params = "cagent=" + cagent + "/\\\\/loginname=" + prefix + loginName + "/\\\\/actype=" + getActype(loginName) + "/\\\\/method=tcc/\\\\/billno=" + cagent + refNo.substring(1)
							+ "/\\\\/flag=" + flag + "/\\\\/type=" + type + "/\\\\/credit=" + credit + "/\\\\/password=" + prefix + loginName;
			String targetParams = des.encrypt(params);
			String key = EncryptionUtil.encryptPassword(targetParams + md5encrypt_key);

			String url = dspurl + "/doBusiness.do?params=" + targetParams + "&key=" + key;
			result = sendApi(url);
		} catch (Exception e) {

			log.error("执行transferCreditConfirm方法发生异常，异常信息：" + e.getMessage());
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
			String msg = element.attributeValue("msg");

			if (info.equals("account_not_exit")) {

				log.error("帐号不存在！");

				return null;
			} else if (info.equals("error") || info.equals("network_error")) {

				log.error("AGIN返回信息："+msg);

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


	// 查询指定时间段内玩家的投注额
	public static Double getBetAmount(String loginName, String startTime, String endTime) {

		Double betAmount = 0.00;
		HttpClient httpClient = null;
		PostMethod postMethod = null;

		try {

			Map<String, String> paramsMap = new HashMap<String, String>();

			paramsMap.put("loginname", prefix + loginName);
			paramsMap.put("timeStart", startTime);
			paramsMap.put("timeEnd", endTime);

			httpClient = HttpUtils.createHttpClient();

			postMethod = new PostMethod(AG_BETS_URL);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");

			NameValuePair[] data = new NameValuePair[paramsMap.size()];

			int i = 0;

			for (String pk : paramsMap.keySet()) {
				
				data[i] = new NameValuePair(pk, paramsMap.get(pk));
				i++;
			}

			postMethod.setRequestBody(data);

			httpClient.executeMethod(postMethod);

			String result = postMethod.getResponseBodyAsString();

			if (StringUtils.isNotBlank(result)) {

				JSONObject jsonObject = JSONObject.fromObject(result);
				boolean success = (boolean) jsonObject.get("success");

				if (success) {

					betAmount = jsonObject.getDouble("bets");
				} else {

					String message = jsonObject.getString("message");
					log.error(">>>>>>>>>>>>>>>>>>AGIN getBetAmount ERROR：" + message);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			if (postMethod != null) {

				postMethod.releaseConnection();
			}
		}

		return betAmount;
	}

	public static void main(String[] args) throws Exception {
		String loginname = "agtest1";
		String gameType = "6";
		String domain = "www.dayunguoji.com";
		String mh5 = "";
//		System.out.println(checkOrCreateGameAccout(prefix+loginname));
//		System.out.println(agLogin(loginname, gameType, domain, mh5, System.currentTimeMillis()+"1"));
		System.out.println(agTryLogin(loginname, gameType, domain, mh5, System.currentTimeMillis()+"2"));
//		System.out.println(transferToAG(loginname, 1.00, System.currentTimeMillis()+"3"));
//		System.out.println(transferFromAG(loginname, 1.00, System.currentTimeMillis()+"4"));
//		System.out.println(getBalance(loginname));
//		System.out.println(getBetAmount(loginname, "2017-06-01 00:00:00", "2017-07-04 00:00:00"));
	}
}