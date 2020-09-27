package dfh.utils;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import dfh.remote.DocumentParser;
import dfh.remote.RemoteCaller;
import dfh.remote.RemoteConstant;
import dfh.remote.bean.Property;
import dfh.security.DESEncrypt;
import dfh.security.SpecialEnvironmentStringPBEConfig;

public class SportBookUtils {
	
	private static String  SPORTBOOKWEBSITEURL="http://sb.e68.ph";
	private static String  SPORTSBOOKSPISITEURL="http://spi.e68.ph";
	private static String KEY="F5623D1F57F64952iofWkynFF+DRdxFz78p8Vw==";
	private static DESEncrypt  des = new DESEncrypt("d6nue9exe8nne2ub");
	public static String getSportBookUrl(String token,String loginname)throws Exception{
			String params = "?t=" + URLEncoder.encode(token,"UTF-8") + "&l=" + 
							URLEncoder.encode("ld_"+loginname,"UTF-8")+"&g=CHS&tz="+
							URLEncoder.encode("GMT+08:00","UTF-8")+
							"&mid="+URLEncoder.encode(KEY,"UTF-8");
			return SPORTBOOKWEBSITEURL+"/Sportsbook/Launch"+params;
	}
	
	public static String getMemberBalance(String loginname){
		String result="";
		HttpClient httpClient = null;
		PostMethod postMethod = null;
		try {
			String memberBalanceXml = RemoteCaller.getSportBookMemberBalanceRequestXml("ld_"+loginname);
			System.out.println(memberBalanceXml);
			String url=SPORTSBOOKSPISITEURL+"/Sportsbook/GetMemberBalance";
			httpClient=new HttpClient();
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
	
	public static String getSportBookMemberBalanceRequestXml(String loginName) {
		List<Property> list = new ArrayList<Property>();
		list.add(new Property(RemoteConstant.SPORT_BOOK_LOGINNAME, loginName));
		return DocumentParser.createSportBookRequestXML("GetMemberBalance", list);
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
		System.out.println(getMemberBalance("woodytest"));
	}
}
