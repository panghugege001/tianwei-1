package com.nnti.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.nnti.common.constants.OddsType;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class ShaBaUtil extends PlatformConfigUtil {

	private static Logger log = Logger.getLogger(ShaBaUtil.class);

	private static final String MD5KEY = "SWKMX76CHD5SJK4328";
	private static final String URL = "http://api.prod.ib.gsoft88.net";
	// 货币类型13(人民币)
	private static final Integer CURRENCY = 13;
	// 最大转入额度
	private static final BigDecimal MAXTRANSFER = new BigDecimal(200000.00);
	// 最小转入额度
	private static final BigDecimal MINTRANSFER = new BigDecimal(1.00);

	private static String getMd5String(String parameters){
		return DigestUtils.md5Hex(MD5KEY+parameters).toUpperCase();
	}
	
	
	// 查询玩家余额
	@SuppressWarnings("rawtypes")
	public static Double getUserBalance(String product, String loginname) {

		HashMap<String, String> sbmap = sbMap.get(product);
		String PREFIX = sbmap.get("PREFIX");

		String urlpart = "/api/CheckUserBalance?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname 
				+ "&wallet_id=1";
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			if(error_code == 0){
				Object obj = a.get("Data");
				JSONArray dataList = JSONArray.fromObject(obj);
				Map map = (Map) dataList.get(0);
				Double balance = (Double) map.get("balance");
				return balance;
			}
			if(error_code == 23005){//账号不存在就创建一个
				createMember(product,loginname);
				return 0.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 转入
	public static Boolean transferInAccount(String product, String loginName, Double amount, String seqId) {

		return fundTransfer(product, loginName, amount, seqId, 1);
	}

	// 转出
	public static Boolean transferOutAccount(String product, String loginName, Double amount, String seqId) {

		return fundTransfer(product, loginName, amount, seqId, 0);
	}

	private static Boolean fundTransfer(String product, String loginName, Double amount, String seqId, Integer direction) {
		
		HashMap<String, String> map = sbMap.get(product);
		String PREFIX = map.get("PREFIX");
		
		String urlpart = "/api/FundTransfer?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginName 
				+ "&OpTransId="+ seqId 
				+ "&amount="+ amount 
				+ "&direction=" + direction;
		
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			if(0 == error_code){
				JSONObject b = JSONObject.fromObject(a.get("Data").toString());
				Integer status = (Integer) b.get("status");
				if(status == 0){
					return Boolean.TRUE;
				}
				if(status == 2){
					Integer ftstatus = CheckFundTransfer(product,loginName,seqId);
					if(ftstatus == 0){
						return Boolean.TRUE;
					}else{
						return Boolean.FALSE;
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Integer ftstatus = CheckFundTransfer(product,loginName,seqId);
			if(ftstatus == 0){
				return Boolean.TRUE;
			}else{
				return Boolean.FALSE;
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * 检查交易情况
	 * @param loginname 用户名
	 * @param OpTransId 交易ID
	 * @return
	 */
	public static Integer CheckFundTransfer(String product,String loginname,String OpTransId){
		HashMap<String, String> map = sbMap.get(product);
		String PREFIX = map.get("PREFIX");
		
		String urlpart = "/api/CheckFundTransfer?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname 
				+ "&OpTransId="+ OpTransId;
		
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			if( 0 == error_code){
				JSONObject b = JSONObject.fromObject(a.get("Data").toString());
				Integer status = (Integer) b.get("status");
				return status;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	private static String createMember(String product, String loginName) {
		return createMember(product,loginName, OddsType.Hong_Kong.getCode(), CURRENCY);
	}

	private static String createMember(String product, String loginname, String oddstype, Integer currency) {

		HashMap<String, String> map = sbMap.get(product);
		String PREFIX = map.get("PREFIX");

		String urlpart = "/api/CreateMember?"; 
		String parameters = "OpCode=DYGJCN"
				+ "&PlayerName="+ PREFIX + loginname 
				+ "&oddstype="+ oddstype
				+ "&currency=" + currency 
				+ "&maxtransfer="+ MAXTRANSFER
				+ "&mintransfer=" + MINTRANSFER;
		String SecurityToken = getMd5String(urlpart+parameters);
		String url = URL+urlpart+"SecurityToken="+SecurityToken+"&"+parameters;
		String result = sendPost(url);
		try {
			JSONObject a = JSONObject.fromObject(result);
			Integer error_code = (Integer) a.get("error_code");
			String message = (String) a.get("message");
			if(0== error_code){
				return "sucess";
			}else{
				return message;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return result;
		}
	}
	
	public static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(0, false));
		params.setParameter("http.protocol.content-charset", "UTF-8");
		params.setParameter("http.socket.timeout", 100000);
		httpclient.setParams(params);
		return httpclient;
	}

	private static String sendPost(String url) {
		HttpClient httpClient = createHttpClient();
		GetMethod med = new GetMethod(url);
		med.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
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
			int responseCode = med.getStatusCode();
			log.info("请求的url:" + url);
			log.info("响应代码:" + responseCode);
			log.info("响应报文:"+result);
			return result;
		} catch (Exception e) {
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
		return "请求第三方异常！";
	}

	public static void main(String[] args) throws Exception {

//		System.out.println(transferInAccount("dy", "dytest06", 1.0, System.currentTimeMillis()+""));
		System.out.println(getUserBalance("dy", "dytest06"));
	}
}