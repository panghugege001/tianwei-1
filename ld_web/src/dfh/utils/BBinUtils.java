package dfh.utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.params.CoreConnectionPNames;

import dfh.security.EncryptionUtil;

public class BBinUtils {
	
	static String  website="LWIN999";
	static String  uppername="de68e68";
	static String  dspurl="http://888.e68.me";
	static String  createMemberKeyB="qYI0s9qmp";
	static String  loginKeyB="jVT56kw";
	static String  logoutKeyB="2c4URy4";
	static String  checkUsrBalanceKeyB="F7rhvnElc";
	static String  transferKeyB="53IkD3JMon";
	static String  getbetKeyB="wIPOb81es7";

	public static String CheckOrCreateGameAccount(String loginname){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		String key = StringUtil.getRandomString(5)
				+ EncryptionUtil.encryptPassword(website + "eb"+loginname
						+ createMemberKeyB + getUsEastTime())
				+ StringUtil.getRandomString(2);
		try {
			String params = "?website=" + website + "&username=" + "eb"+loginname
					+ "&uppername=" + uppername + "&password=12345678&key=" + key;
			String url=dspurl+"/app/WebService/XML/display.php/CreateMember"+params;
			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			System.out.println("BBIN Response:"+result);
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
	
	public static String GetBalance(String loginname){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String key = StringUtil.getRandomString(9)
			+ EncryptionUtil.encryptPassword(website + "eb"+loginname
					+ checkUsrBalanceKeyB + getUsEastTime())
			+ StringUtil.getRandomString(6);
			
			String params = "?website=" + website + "&username=" + "eb"+loginname
			+ "&uppername=" + uppername +"&key=" + key;
			String url=dspurl+"/app/WebService/XML/display.php/CheckUsrBalance"+params;
			
			httpClient=HttpUtils.createHttpClientShort();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			System.out.println("BBIN Response:"+result);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		
		return result;
	}
	
	public static String TransferCredit(String loginname,String tranferno,String type,Integer remit,Double localCredit)throws Exception{
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String key = StringUtil.getRandomString(2)
			+ EncryptionUtil.encryptPassword(website + "eb"+loginname+tranferno
					+ transferKeyB + getUsEastTime())
			+ StringUtil.getRandomString(7);
			Double curCredit = null;
			Double newcredit = null;
			if("IN".equals(type)){
				curCredit = localCredit;
				
				BigDecimal bCurCredit = new BigDecimal(curCredit.toString());
				BigDecimal bRemit = new BigDecimal(remit.toString());
				
				newcredit = bCurCredit.subtract(bRemit).doubleValue();
				
			}else if("OUT".equals(type)){
				curCredit = localCredit;
				
				BigDecimal bCurCredit = new BigDecimal(curCredit.toString());
				BigDecimal bRemit = new BigDecimal(remit.toString());
				
				newcredit = bCurCredit.add(bRemit).doubleValue();
			}
			String params = "?website=" + website + "&username=" + "eb"+loginname
			+ "&uppername=" + uppername +"&remitno="+tranferno+"&action="+type+"&remit="+remit.intValue()
			+ "&newcredit=" + newcredit +"&credit="+curCredit+"&key=" + key;
			String url=dspurl+"/app/WebService/XML/display.php/Transfer"+params;

			httpClient=HttpUtils.createHttpClient();
			postMethod=new PostMethod(url);
			postMethod.setRequestHeader("Connection", "close");
			httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			System.out.println("BBIN Response:"+result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			if(postMethod!=null){
				postMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String forwardGame(String loginname){
		String key = dfh.utils.StringUtil.getRandomString(8)
		+ EncryptionUtil.encryptPassword(website + "eb"+loginname
				+ loginKeyB + getUsEastTime())
		+ StringUtil.getRandomString(1);
		String params="?website="+website+"&username="+"eb"+loginname+"&uppername="
				+uppername+"&password=12345678&lang=zh-cn&page_site=live"
				+"&key="+key;
		return dspurl+"/app/WebService/XML/display.php/Login"+params; 
	}
	private static String getUsEastTime(){//美东时间yyyyMMdd
		long time = new Date().getTime()-12*3600*1000;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return DateUtil.fmtyyyyMMdd(calendar.getTime());
	}
	public static void main(String[] args) {
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
