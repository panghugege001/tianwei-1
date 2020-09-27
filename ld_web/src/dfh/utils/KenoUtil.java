package dfh.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.params.CoreConnectionPNames;

import dfh.remote.DocumentParser;

public class KenoUtil {
	
	static String  XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"" + "iso-8859-1" + "\"?>";
	static String VendorId="50";
	static String PlayerCurrency="C01";
	static String PlayerAllowStake="161,162,163";
	static String Language="SC";
	static String VendorSite="keno.e68.cc";
	static String FundLink="http://www.e68.ph/asp/payPage.aspx"; 
	
	public static String login(String PlayerId,String PlayerRealName,
			String PlayerIP,String VendorRef,String Remarks) {
		HttpClient httpClient=HttpUtils.createHttpClient();
		PostMethod loginpostMethod=new PostMethod("http://integrate.v33kgg.com/player_enter_keno.php");
		loginpostMethod.setRequestHeader("Connection", "close");
		StringBuffer buffer = new StringBuffer();
		buffer.append(XML_DECLARATION);
		buffer.append("<methodCall>");
		buffer.append("<methodName>PlayerLanding</methodName>");
		buffer.append("<params><param><value><struct>");
		buffer.append("<member><name>VendorSite</name><value><string>"+VendorSite+"</string></value></member>");
		buffer.append("<member><name>FundLink</name><value><string>"+FundLink+"</string></value></member>");
		buffer.append("<member><name>VendorId</name><value><string>"+VendorId+"</string></value></member>");
		buffer.append("<member><name>PlayerId</name><value><string>e_"+PlayerId+"</string></value></member>");
		buffer.append("<member><name>PlayerRealName</name><value><string>"+PlayerRealName+"</string></value></member>");
		buffer.append("<member><name>PlayerCurrency</name><value><string>"+PlayerCurrency+"</string></value></member>");
		buffer.append("<member><name>PlayerCredit</name><value><string>0.00</string></value></member>");
		buffer.append("<member><name>PlayerAllowStake</name><value><string>"+PlayerAllowStake+"</string></value></member>");
		buffer.append("<member><name>Trial</name><value><string>0</string></value></member>");
		buffer.append("<member><name>Language</name><value><string>"+Language+"</string></value></member>");
		buffer.append("<member><name>PlayerIP</name><value><string>"+PlayerIP+"</string></value></member>");
		buffer.append("<member><name>VendorRef</name><value><string>"+VendorRef+"</string></value></member>");
		buffer.append("<member><name>Remarks</name><value><string>"+Remarks+"</string></value></member>");
		buffer.append("</struct></value></param></params></methodCall>");
		
		loginpostMethod.setRequestEntity(new StringRequestEntity(buffer.toString()));
		String result="";
		try {
			httpClient.executeMethod(loginpostMethod);
			result = loginpostMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (loginpostMethod != null) {
				loginpostMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String transferconfirm(String PlayerId,Double amount,
			String PlayerIP,Integer FundIntegrationId,String VendorRef) throws Exception{
		HttpClient httpClient=HttpUtils.createHttpClient();
		PostMethod transferMethod=new PostMethod("http://integrate.v33kgg.com/player_fund_in_out_confirm.php");
		transferMethod.setRequestHeader("Connection", "close");
		StringBuffer buffer = new StringBuffer();
		buffer.append(XML_DECLARATION);
		buffer.append("<methodCall>");
		buffer.append("<methodName>FundInOutConfirm</methodName>");
		buffer.append("<params><param><value><struct>");
		
		buffer.append("<member><name>VendorId</name><value><string>"+VendorId+"</string></value></member>");
		buffer.append("<member><name>PlayerId</name><value><string>e_"+PlayerId+"</string></value></member>");
		buffer.append("<member><name>Amount</name><value><string>"+amount+"</string></value></member>");
	
		buffer.append("<member><name>PlayerIP</name><value><string>"+PlayerIP+"</string></value></member>");
		buffer.append("<member><name>FundIntegrationId</name><value><string>"+FundIntegrationId+"</string></value></member>");
		buffer.append("<member><name>VendorRef</name><value><string>"+VendorRef+"</string></value></member>");
		buffer.append("</struct></value></param></params></methodCall>");
		
		String result="";
		
		transferMethod.setRequestEntity(new StringRequestEntity(buffer.toString()));
		try {
			httpClient.executeMethod(transferMethod);
			result = transferMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			if (transferMethod != null) {
				transferMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String transferfirst(String PlayerId,Double amount,
			String PlayerIP,String VendorRef) throws Exception{
		HttpClient httpClient=HttpUtils.createHttpClient();
		PostMethod transferMethod=new PostMethod("http://integrate.v33kgg.com/player_fund_in_out_first.php");
		transferMethod.setRequestHeader("Connection", "close");
		StringBuffer buffer = new StringBuffer();
		buffer.append(XML_DECLARATION);
		buffer.append("<methodCall>");
		buffer.append("<methodName>FundInOutFirst</methodName>");
		buffer.append("<params><param><value><struct>");
		
		buffer.append("<member><name>VendorId</name><value><string>"+VendorId+"</string></value></member>");
		buffer.append("<member><name>PlayerId</name><value><string>e_"+PlayerId+"</string></value></member>");
		buffer.append("<member><name>Amount</name><value><string>"+amount+"</string></value></member>");
		buffer.append("</struct></value></param></params></methodCall>");
		
		String result="";
		
		transferMethod.setRequestEntity(new StringRequestEntity(buffer.toString()));
		try {
			httpClient.executeMethod(transferMethod);
			result = transferMethod.getResponseBodyAsString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}finally{
			if (transferMethod != null) {
				transferMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static String checkcredit(String PlayerId) {
		HttpClient httpClient=HttpUtils.createHttpClientShort();
		PostMethod getcreditMethod=new PostMethod("http://integrate.v33kgg.com/player_get_credit.php");
		getcreditMethod.setRequestHeader("Connection", "close");
		StringBuffer buffer = new StringBuffer();
		buffer.append(XML_DECLARATION);
		buffer.append("<methodCall>");
		buffer.append("<methodName>GetCredit</methodName>");
		buffer.append("<params><param><value><struct>");
		
		buffer.append("<member><name>VendorId</name><value><string>"+VendorId+"</string></value></member>");
		buffer.append("<member><name>PlayerId</name><value><string>e_"+PlayerId+"</string></value></member>");
		
		buffer.append("</struct></value></param></params></methodCall>");
		String result="";
		getcreditMethod.setRequestEntity(new StringRequestEntity(buffer.toString()));
		try {
			httpClient.executeMethod(getcreditMethod);
			result = getcreditMethod.getResponseBodyAsString();
			//System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if (getcreditMethod != null) {
				getcreditMethod.releaseConnection();
			}
		}
		return result;
	}
	
	
	
	
	public static void main(String[] args) {
		String result="";
		HttpClient httpClient=new HttpClient();
		//PostMethod loginpostMethod=new PostMethod("http://test.integrate.v33kgg.com/player_enter_keno.php");
		//PostMethod transferMethod=new PostMethod("http://test.integrate.v33kgg.com/player_fund_in_out.php");
		//PostMethod getcreditMethod=new PostMethod("http://test.integrate.v33kgg.com/player_get_credit.php");
		try {
			//PostMethod post = new PostMethod("http://test.integrate.v33kgg.com");
			//post.setRequestHeader("Content-type", "text/xml;charset=" + Constants.ENCODING + "");
			
			//HttpClientParams params = new HttpClientParams();
			//postMethod.setParameter("content", KenoUtil.login("keno.e68.ph", "www.e68.ph", "name", "name", "", "1234567", "remark"));
//			loginpostMethod.setRequestEntity(new StringRequestEntity(KenoUtil.login("name1", "姓名", "", "1234567", "remark")));
//			httpClient.executeMethod(loginpostMethod);
//			loginpostMethod.getResponseBodyAsStream();
//			result = loginpostMethod.getResponseBodyAsString();
			//System.out.println(result);
		//	postMethod.getResponseBodyAsStream();
			System.out.println(DocumentParser.parseKenologinResponseRequest(KenoUtil.login("name1", "姓名", "", "1234567", "remark")));
			
//			transferMethod.setRequestEntity(new StringRequestEntity(KenoUtil.transfer("name1",-10.00, "127.0.0.1", "1234567")));
//			httpClient.executeMethod(transferMethod);
//			transferMethod.getResponseBodyAsStream();
//			result = transferMethod.getResponseBodyAsString();
		//	System.out.println(result);
			//System.out.println(DocumentParser.parseKenologinResponseRequest(KenoUtil.transfer("name1", 100.00, "", "12345")));
			
//			getcreditMethod.setRequestEntity(new StringRequestEntity(KenoUtil.checkcredit("name1")));
//			httpClient.executeMethod(getcreditMethod);
//			getcreditMethod.getResponseBodyAsStream();
//			result = getcreditMethod.getResponseBodyAsString();
		//	System.out.println(result);
			System.out.println(DocumentParser.parseKenocheckcreditResponseRequest(KenoUtil.checkcredit("woodytest")).getAmount());
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	//String	result = postMethod.getResponseBodyAsString();
		
	}
}
