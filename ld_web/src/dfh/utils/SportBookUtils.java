package dfh.utils;
import java.net.URLEncoder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.params.CoreConnectionPNames;

import dfh.exception.PostFailedException;
import dfh.exception.ResponseFailedException;
import dfh.remote.RemoteCaller;
import dfh.security.DESEncrypt;
import dfh.security.SpecialEnvironmentStringPBEConfig;

public class SportBookUtils {
	
	private static String  SPORTBOOKWEBSITEURL="http://sb.e68ph.net";
	private static String  SPORTSBOOKSPISITEURL="http://spi.e68.ph";
	private static String KEY="F5623D1F57F64952iofWkynFF+DRdxFz78p8Vw==";
	private static DESEncrypt  des = new DESEncrypt("d6nue9exe8nne2ub");
	public static String getSportBookUrl(String token,String loginname)throws Exception{
			String params = "?t=" + URLEncoder.encode(token,"UTF-8") + "&l=" + 
							URLEncoder.encode("e68_"+loginname,"UTF-8")+"&g=CHS&tz="+
							URLEncoder.encode("GMT+08:00","UTF-8")+
							"&mid="+URLEncoder.encode(KEY,"UTF-8");
			return SPORTBOOKWEBSITEURL+"/Sportsbook/Launch"+params;
	}
	
	public static String getMemberBalance(String loginname){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String memberBalanceXml = RemoteCaller.getSportBookMemberBalanceRequestXml("e68_"+loginname);
			System.out.println(memberBalanceXml);
			String url=SPORTSBOOKSPISITEURL+"/Sportsbook/GetMemberBalance";
			httpClient=HttpUtils.createHttpClientShort();
			postMethod=new PostMethod(url);
			postMethod.setRequestBody(des.encryptSportBookText(memberBalanceXml));
			System.out.println("encrypt:"+des.encryptSportBookText(memberBalanceXml));
			postMethod.setRequestHeader("Connection", "close");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			if(!"".equals(result)){
				result = des.decryptSportBookText(result);
			}
			System.out.println("SB Response:"+result);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String depositFund(String loginname,Double amount,String ReferenceNo) throws Exception{
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		
		String depositFundXml = RemoteCaller.getSportBookDepositFundRequestXml("e68_"+loginname,amount,ReferenceNo);
		System.out.println(depositFundXml);
		String url=SPORTSBOOKSPISITEURL+"/Sportsbook/DepositFund";
		httpClient=HttpUtils.createHttpClient();
		postMethod=new PostMethod(url);
		postMethod.setRequestBody(des.encryptSportBookText(depositFundXml));
		System.out.println("encrypt:"+des.encryptSportBookText(depositFundXml));
		postMethod.setRequestHeader("Connection", "close");
		httpClient.executeMethod(postMethod);
		result = postMethod.getResponseBodyAsString();
		if(!"".equals(result)){
			result = des.decryptSportBookText(result);
		}
		System.out.println("SB Response:"+result);
		if(postMethod!=null){
			postMethod.releaseConnection();
		}
		return result;
	}
	
	public static String withdrawFund(String loginname,Double amount,String ReferenceNo)throws Exception{
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		String depositFundXml = RemoteCaller.getSportBookWithdrawFundRequestXml("e68_"+loginname,amount,ReferenceNo);
		System.out.println(depositFundXml);
		String url=SPORTSBOOKSPISITEURL+"/Sportsbook/WithdrawFund";
		httpClient=HttpUtils.createHttpClient();
		postMethod=new PostMethod(url);
		postMethod.setRequestBody(des.encryptSportBookText(depositFundXml));
		System.out.println("encrypt:"+des.encryptSportBookText(depositFundXml));
		postMethod.setRequestHeader("Connection", "close");
		httpClient.executeMethod(postMethod);
		result = postMethod.getResponseBodyAsString();
		if(!"".equals(result)){
			result = des.decryptSportBookText(result);
		}
		System.out.println("SB Response:"+result);
		if(postMethod!=null){
			postMethod.releaseConnection();
		}
		return result;
	}
	
	public static String getTransferStatus(String ReferenceNo){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		
		try {
			String depositFundXml = RemoteCaller.getSportBookTransferStatusRequestXml(ReferenceNo);
			System.out.println(depositFundXml);
			String url=SPORTSBOOKSPISITEURL+"/Sportsbook/GetTransferStatus";
			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestBody(des.encryptSportBookText(depositFundXml));
			System.out.println("encrypt:"+des.encryptSportBookText(depositFundXml));
			postMethod.setRequestHeader("Connection", "close");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			if(!"".equals(result)){
				result = des.decryptSportBookText(result);
			}
			System.out.println("SB Response:"+result);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String getDecryptAESXML(String encryptxml){
		try {
			String xml =des.decryptSportBookText(encryptxml);
			return xml;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String getEncryptAESXML(String encryptxml){
		try {
			String xml =des.encryptSportBookText(encryptxml);
			return xml;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args)throws Exception {
//		System.out.println(withdrawFund("woodytest",100.00,"A0021"));
//		System.out.println(RemoteCaller.getTransferStatusSBRequest("200000000679056"));
//		String s = "As+DNDsLejZqnHxECwI6Tzc5DrwwQl1YnJjciECsWgYILMqogmwNASW0qnd6+ujdu6XnDdzr3EsMkNxCOsSSfVgZ/fngSYIM+EiFt0G1twhBCtML3cMrqxNBQd+aFqlQxB4fC+uX/NVhOgSE9f3+Af6CAFiO69Td4CT+UYSoeq5xmvGUqA6CWp1pWLTVPTz7qlbHl59J6usw2ZYwGSwNWSS1ZotO0jtMu/4dE3dHyGefmO73RznNTqnebbiyBSWJBKDwTRErwN23qKcBJsssCBSJ32etM3nGQOgL6QvcaLyMH/S1vWOkFabCGT4n1ZZ5";
//		String xml = getDecryptAESXML(s);
//		System.out.println(xml);
//		SportBookLoginValidationBean  sb = DocumentParser.parseSportBookLoginValidationBean(xml);
//		System.out.println(sb.getLoginName()+";"+sb.getToken());
//		System.out.println(TransferCredit("qyag061","10222230","IN",10,100.01));
		//System.out.println(APUtils.getActype("qytest01"));
		//System.out.println("pre&&e68test01".indexOf("&&"));
		//System.out.println(BBinUtils.GetBalance("qyag057","qyag057"));
		//System.out.println(BBinUtils.GetBalance("qyag057","qyag057"));
//		System.out.println(System.currentTimeMillis());
//		System.out.println(EncryptionUtil.encryptPassword("test"+13+System.currentTimeMillis()+"jsn72ksmm"));
//		System.out.println(BBinUtils.CheckOrCreateGameAccount("woodytest","woodytest"));
//		System.out.println(APUtils.GetBalance("woody","1223456"));
		//System.out.println(BBinUtils.PrepareTransferCredit("qyag058", "12345678912326", "IN", 1003.60, "qyag058"));
	//	System.out.println(BBinUtils.TransferCreditConfirm("qyag058", "12345678912326", "IN", 1003.60, "qyag058", 1));
//		System.out.println(APUtils.GetBalance("woody","1223456"));
	//	System.out.println(BBinUtils.GetBalance("qyag057","qyag057"));
		//System.out.println(APUtils.SearchTransferResult("woody", "12345678912333"));
		//System.out.println(DocumentParser.parseDspResponseRequest(APUtils.GetBalance("woo222dy","1232321df")));
	}
}
