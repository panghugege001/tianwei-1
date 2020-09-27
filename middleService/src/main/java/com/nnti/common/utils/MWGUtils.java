package com.nnti.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.nnti.common.security.mwgsign.AES;
import com.nnti.common.security.mwgsign.RSA;



public class MWGUtils extends PlatformConfigUtil{
	private static Logger log = Logger.getLogger(MWGUtils.class);
	//public static final String siteId = "10005200";
	public static final String MW_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYvuROvaCkAir5bsDckRDV91YqEhWqhUp+/YXqASUypeteutSZTGy5RGds2jxp6jYac3ZslfmhiwXJCi0S8sIHZmzmA/KAmfCKhYeRjZNs9waWDMv2ibb2WXxlI/20ynRgQtQUt7reojBPvS6DRq0R35S6M6ddDVfj4e4Lo1lxwwIDAQAB";
	public static final String EC_private_key = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJWhpnFsnmNcPeVvUwPvlN2QdkBnqHNzZmmjrkzN3FX73iU2v9e+oozsAep/hTrvc7bAYKAkX62S0KP3bsrvIQrAkUw5L0C3QCmu6OMWS5Qcy886k4FwdvcrEIC6frQP3aNg0V8dIokeduyin7iCZPU8eWs8zessXJQOWxIIW2VJAgMBAAECgYBDYzC9hkhywRkTGibN7/kgK65F5SklDYTC8LdP2apwguUR521QsYctZiCaxmFvIGYBmPQrSxZX6nXSUl3wQO+k3wMZKa3xSu0t1U1vhrYldA34llsnwJVf+ktBPEiocpxwgnklM/HbuRtN/pHGbMnY5bDni2eIvUrYqHNXjBImrQJBAPAz+gPo9rj2/+isEUPlyCh5Os/Z9SHUOvCmKXa/GTMRcBPdFJFmSNA3Ho2hye5LNsj7hTba5zTuGcMesohnNBMCQQCfeNLN3hXOP19xMOdUvO4H8nifvWO3mhmnZ8Bmm8AzJw4KLfbn7RGf+KM6IBsWGPLek8Vs4uhO3VW5PhcmbZSzAkEA0pxQUFNY2S9BjUopzUXRraM2LP8nz2Sd1VlsK8E9ICjfA5uqKB7uIxrhQEAmpTjvrWPUFxfy99b5YbstwSn0XwJADbxtg/gGLs68nNZWhrEDW8Hh6/h3N2BZp1bDdtMhmgZaKjxNUSrVYs8a2C3dx2h8uvlFfxyIYAqmkJ8thUzZCwJBAMMwn6hk9asCcnKponqh3RMDb9YufLWMeSogKti/Wdtly3qPnHwVruMEgfbLw5AxMSOaRIbyN4JJTMnFU/tdIRo=";
	public static final String EC_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVoaZxbJ5jXD3lb1MD75TdkHZAZ6hzc2Zpo65MzdxV+94lNr/XvqKM7AHqf4U673O2wGCgJF+tktCj927K7yEKwJFMOS9At0AprujjFkuUHMvPOpOBcHb3KxCAun60D92jYNFfHSKJHnbsop+4gmT1PHlrPM3rLFyUDlsSCFtlSQIDAQAB";
	
	public static final String EC_AES_key = "af123@#$qrqwr123";// EC 随机产生
	public static final String PARTTOKEN = "4e4cad6035b44396bebc40e6f7daca72";//用于生成utoken，授权之后不可变更，长度必须为 32位
	public static final String resultType = "json";
	public static final String lang = "cn";
	
	//public static final String merchantId = "qy";
	
	public static TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
	public static String dataSign = "";
	
	public static final String ENCODE_TYPE = "UTF-8";

	public static JSONObject sendApi(String siteId,String func,String domainURL,TreeMap<String, Object> dataMap) throws Exception {
		
		if(func.equals("siteUsergamelog")){
			domainURL = domainURL.replace("as-lobby", "as-service");
		}
		
		String apiURL = domainURL + "api/"+func+"?";
		
		String contentJson = setDataContent(dataMap);

		String data = AES.encryptToBase64(contentJson, EC_AES_key);
//		data = URLEncoder.encode(data, ENCODE_TYPE);

		String key = RSA.encrypt(EC_AES_key, MW_public_key);
//		key = URLEncoder.encode(key, ENCODE_TYPE);

		URL url = new URL(apiURL);
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("func", func);
		params.put("resultType", resultType);
		params.put("siteId", siteId);
		params.put("lang", lang);
		params.put("data", data);
		params.put("key", key);

		String response = httpPostRequest(url, params);
		JSONObject json = JSONObject.parseObject(response);

		return json;
	}

	private static String httpPostRequest(URL url, Map<String,Object> params) throws IOException
	{
		StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), ENCODE_TYPE));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), ENCODE_TYPE));
        }
        
        // 如果请求 有问题, 请把这串请求串 提供给MW技术检视
        System.out.printf("[%s_RequestURL]:%s%s\n",params.get("func"), url, postData);
        byte[] postDataBytes = postData.toString().getBytes(ENCODE_TYPE);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), ENCODE_TYPE));

        StringBuffer resInfo = new StringBuffer();
        for (int c; (c = in.read()) >= 0;)
        {
        	char ch = (char)c;
        	resInfo.append(ch);
        }
        
        System.out.printf("[%s_Response]:%s\n\n",params.get("func"), resInfo);
        return resInfo.toString();    
	}
	
	private static String setDataContent(TreeMap<String, Object> map) {
		
		String contentSign = StringUtils.EMPTY;

		for (Map.Entry<String, Object> item : map.entrySet()) {
			String key = item.getKey();
			Object obj = item.getValue();
			String value = (obj == null) ? "" : obj.toString();
			contentSign += (key + "=" + value);
		}

		dataSign = RSA.sign(contentSign, EC_private_key);

		// step4. 将步骤3 获得的参数sign，和步骤1 涉及的参数，构建JSON 数据
		map.put("sign", dataSign);
		JSONObject jsonObject = new JSONObject(map);
		String jsonContent = jsonObject.toString();

		return jsonContent;
	}
	
	// 執行異步通知 解開收到的 key & data
	public String decryptNotifierData(String key, String data, String ecPrivateKey) throws Exception
	{
		String aesKey = RSA.decrypt(key, ecPrivateKey);
		String resultData = AES.decryptFromBase64(data, aesKey);  
		return resultData;
	}
	
	public boolean verifyNotifierData(String notifierData, String mwPublicKey) throws Exception
	{
		JSONObject json = JSONObject.parseObject(notifierData);
		String sign = json.getString("sign");
		
		TreeSet<String> keySortSet = new TreeSet<String>(json.keySet());
		String jsonSortContent = "";
		
		for(String jsonKey : keySortSet)
		{
			if(jsonKey.equals("sign"))
				continue;
			jsonSortContent += jsonKey+"="+json.get(jsonKey);
		}
		
		return  RSA.checkSign(jsonSortContent, sign, mwPublicKey);
	}
	// Domain API
	public static String domain(String product) {
		String func = "domain";
		String domainUrl = "http://www.168at168.com/as-lobby/";
		
		HashMap<String, String> map = mwgMap.get(product);
		String siteId = map.get("siteId");
		
		dataMap.clear();
		dataMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
		
		String domainValue = "";
		try {
			JSONObject json = sendApi(siteId,func,domainUrl,dataMap);
			domainValue = json.getString("domain");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("获取MWG平台domain异常");
			domainValue = "http://www.666wins.com/as-lobby/";
		}
		return domainValue;
	}
	
	private static String getToken(String uid){
		return uid.toLowerCase()+PARTTOKEN.substring(uid.length());
	}
	
	// 获取用户信息里面的余额
	public static Double getUserBalance(String product,String loginname) throws Exception{
		String func = "userInfo";
		String domainURL = domain(product);
		
		HashMap<String, String> map = mwgMap.get(product);
		String siteId = map.get("siteId");
		String prefix = map.get("prefix");
		String merchantId = map.get("merchantId");
		String uid = prefix+loginname.toLowerCase();
		
		dataMap.clear();
		dataMap.put("uid", uid);
		dataMap.put("utoken", getToken(uid));
		dataMap.put("timestamp", Long.toString(System.currentTimeMillis()));
		dataMap.put("merchantId", merchantId);
		dataMap.put("currency", "CNY");
		JSONObject result = sendApi(siteId,func,domainURL,dataMap);
		String code = result.getString("ret");
		if("0000".equals(code)){
			JSONObject user = result.getJSONObject("userInfo");
			Double money = user.getDouble("money");
			return money;
		}else{
			String msg = result.getString("msg");
			log.info(msg);
		}
		return null;
	}
	
	// 转入
	public static Boolean transferInAccount(String product, String loginName, Double amount, String seqId) {

		try {
			return FundTransfer(product, loginName, amount, seqId, "0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}

	// 转出
	public static Boolean transferOutAccount(String product, String loginName, Double amount, String seqId) {

		try {
			return FundTransfer(product, loginName, amount, seqId, "1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 额度转入转出
	 * @param loginname
	 * @param amount
	 * @param direction "0"转入，"1"转出
	 * @return
	 * @throws Exception
	 */
	public static Boolean FundTransfer(String product,String loginname,Double amount,String transferOrderNo,String direction) throws Exception
	{
		//第一步准备
		String func = "transferPrepare";//转入/转出准备
		String domainURL = domain(product);
		
		HashMap<String, String> map = mwgMap.get(product);
		String siteId = map.get("siteId");
		String merchantId = map.get("merchantId");
		String prefix = map.get("prefix");
		String uid = prefix+loginname.toLowerCase();
		
		dataMap.clear();
		dataMap.put("uid", uid);
		dataMap.put("utoken", getToken(uid));
		dataMap.put("transferType", direction);
		dataMap.put("transferAmount", amount.intValue());
		dataMap.put("transferOrderNo", transferOrderNo);
		dataMap.put("transferOrderTime", DateUtil.format(new Date()));
		dataMap.put("transferClientIp", "127.0.0.1");
		dataMap.put("transferNotifierUrl", "");
		dataMap.put("merchantId", merchantId);
		dataMap.put("timestamp", Long.toString(System.currentTimeMillis()));
		dataMap.put("currency", "CNY");
		JSONObject resultp = sendApi(siteId,func,domainURL,dataMap);
		String code = resultp.getString("ret");
		if("0000".equals(code)){
			String orderNo = resultp.getString("asinTransferOrderNo");
			String orderTime = resultp.getString("asinTransferDate");
			//第二步确认
			func = "transferPay";//转入/转出确认
			dataMap.clear();
			dataMap.put("uid", uid);
			dataMap.put("utoken", getToken(uid));
			dataMap.put("asinTransferOrderNo", orderNo);
			dataMap.put("asinTransferOrderTime", orderTime);
			dataMap.put("transferOrderNo", transferOrderNo);
			dataMap.put("transferAmount", amount.intValue());
			dataMap.put("transferClientIp", "127.0.0.1");
			dataMap.put("merchantId", merchantId);
			dataMap.put("timestamp", Long.toString(System.currentTimeMillis()));
			dataMap.put("currency", "CNY");
			JSONObject resultc = sendApi(siteId,func,domainURL,dataMap);
			String codec = resultc.getString("ret");
			if("0000".equals(codec)){
				return Boolean.TRUE;
			}else{
				String msg = resultc.getString("msg");
				log.info(msg);
				return Boolean.FALSE;
			}
		}else{
			String msg = resultp.getString("msg");
			log.info(msg);
			return Boolean.FALSE;
		}
	}
	
	public static void main(String[] args) throws Exception {
		String loginname = "devtest999";
		String product = "l8";
		//String loginUrl = MWGUtils.oauth(product,loginname);
		//System.out.println(loginUrl);
		Boolean result = FundTransfer(product,loginname, 1.0,loginname+System.currentTimeMillis(), "0");
		System.out.println(result);
		System.out.println(MWGUtils.getUserBalance(product,loginname));
		//System.out.println(getbetByMerchantId("2017-09-14 00:00:00", "2017-09-14 23:59:59"));
		//System.out.println(getbetByUser(loginname,"2017-09-14 00:00:00", "2017-09-14 23:59:59"));
		//gameInfo();
	}
}
