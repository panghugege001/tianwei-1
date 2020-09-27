package dfh.utils;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import dfh.exception.PostFailedException;
import dfh.exception.ResponseFailedException;
import dfh.security.DESEncrypt;
import dfh.security.EncryptionUtil;

public class APUtils { 
	private static Logger log = Logger.getLogger(APUtils.class);
	static String  encrypt_key="njvs90z1";
	static String  md5encrypt_key="sdf7&^#gfas";
	static String  cagent="B20_AG";
	static String  dspurl="http://gi.sunrise88.net:81";
	static DESEncrypt  des=new DESEncrypt(encrypt_key);
	
	public static final List<String> TEST_ACCOUNTS = Arrays.asList(new String[] { "e68test01", "e68test02", "e68test03", "e68test04", "e68test05", "e68test06", "e68test07" });
	
	public static String getActype(String loginname) {
		boolean contain = TEST_ACCOUNTS.contains(loginname);
		String actype = "";
		
		if (!contain) {// 如果不是测试帐号
			actype = "1";
		} else {// 如果是测试帐号
			actype = "0";
		}
		return actype;
	}
	
	public static String isCustomerExist(String loginname){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params="cagent="+cagent+"/\\\\/loginname=e_"+loginname+"/\\\\/method=ice"+"/\\\\/oddtype=B";
			String targetParams=des.encrypt(params);
			String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
			String url=dspurl+"/doBusiness.do?params="+targetParams+"&key="+key;
			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B18");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String CheckOrCreateGameAccount(String loginname,String password){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params="cagent="+cagent+"/\\\\/loginname=e_"+loginname+"/\\\\/method=lg/\\\\/actype="+getActype(loginname)+"/\\\\/password=e_"+password;
			String targetParams=des.encrypt(params);
			String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
			String url=dspurl+"/doBusiness.do?params="+targetParams+"&key="+key;
			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B18");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		
		return result;
	}
	
	public static String GetBalance(String loginname,String password){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params="cagent="+cagent+"/\\\\/loginname=e_"+loginname+"/\\\\/method=gb/\\\\/actype="+getActype(loginname)+"/\\\\/password=e_"+password;
			String targetParams=des.encrypt(params);
			//System.out.println(params);
			String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
			String url=dspurl+"/doBusiness.do?params="+targetParams+"&key="+key;
			//System.out.println(url);
			httpClient=HttpUtils.createHttpClientShort();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B18");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			//System.out.println(result+"1111111111");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		
		return result;
	}
	
	public static String PrepareTransferCredit(String loginname,String billno,String type,Double credit,String password)throws PostFailedException, ResponseFailedException{
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params="cagent="+cagent+"/\\\\/loginname=e_"+loginname+"/\\\\/actype="+getActype(loginname)+"/\\\\/method=tc/\\\\/billno="+cagent+billno+"/\\\\/type="+type+"/\\\\/credit="+credit+"/\\\\/password=e_"+password;
			String targetParams=des.encrypt(params);
			String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
			String url=dspurl+"/doBusiness.do?params="+targetParams+"&key="+key;
			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B18");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw new PostFailedException();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	
	public static String TransferCreditConfirm(String loginname,String billno,String type,Double credit,String password,Integer flag){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params="cagent="+cagent+"/\\\\/loginname=e_"+loginname+"/\\\\/actype="+getActype(loginname)+"/\\\\/method=tcc/\\\\/billno="+cagent+billno+"/\\\\/flag="+flag+"/\\\\/type="+type+"/\\\\/credit="+credit+"/\\\\/password=e_"+password;
			String targetParams=des.encrypt(params);
			String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
			String url=dspurl+"/doBusiness.do?params="+targetParams+"&key="+key;
			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B18");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		log.info(result);
		return result;
	}
	
	public static String SearchTransferResult(String loginname,String billno){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params="cagent="+cagent+"/\\\\/loginname=e_"+loginname+"/\\\\/method=str/\\\\/billno="+cagent+billno;
			String targetParams=des.encrypt(params);
			String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
			String url=dspurl+"/doBusiness.do?params="+targetParams+"&key="+key;
			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B18");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		
		return result;
	}
	
	public static String UpdateTransferResultStatus(String loginname,String billno,String operator,Integer flag){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params="cagent="+cagent+"/\\\\/loginname=e_"+loginname+"/\\\\/method=uts/\\\\/billno="+billno+"/\\\\/flag="+flag+"/\\\\/operator="+operator;
			String targetParams=des.encrypt(params);
			String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
			String url=dspurl+"/doBusiness.do?params="+targetParams+"&key="+key;
			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			postMethod.addRequestHeader("User-Agent", "WEB_LIB_GI_B18");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	
	public static String forwardGame(String loginname,String password,String website,Integer sid){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String params="cagent="+cagent+"/\\\\/loginname=e_"+loginname+"/\\\\/dm="+website+"/\\\\/actype="+getActype(loginname)+"/\\\\/password=e_"+password+"/\\\\/sid="+sid;
			String targetParams=des.encrypt(params);
			String key=EncryptionUtil.encryptPassword(targetParams+md5encrypt_key);
			String url=dspurl+"/forwardGame.do?params="+targetParams+"&key="+key;
			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {
		System.out.println(APUtils.getActype("qytest01"));
		//System.out.println("pre&&e68test01".indexOf("&&"));
		//System.out.println(APUtils.CheckOrCreateGameAccount("qytest01","qytest01"));
//		System.out.println(System.currentTimeMillis());
//		System.out.println(EncryptionUtil.encryptPassword("test"+13+System.currentTimeMillis()+"jsn72ksmm"));
		//System.out.println(APUtils.CheckOrCreateGameAccount("woody","1223456"));
		System.out.println(APUtils.GetBalance("woody","1223456"));
//		System.out.println(APUtils.PrepareTransferCredit("woody", "12345678912318", "IN", 103.60, "1223456"));
//		System.out.println(APUtils.TransferCreditConfirm("woody", "12345678912318", "IN", 103.60, "1223456", 1));
//		System.out.println(APUtils.GetBalance("woody","1223456"));
		//System.out.println(APUtils.SearchTransferResult("woody", "12345678912333"));
		//System.out.println(DocumentParser.parseDspResponseRequest(APUtils.GetBalance("woo222dy","1232321df")));
	}
}
