package dfh.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import dfh.remote.DocumentParser;

/**
 * kenog 
 * @author Administrator
 *
 */
public class Keno2Util {
	
	//测试环境
	//public static final String KONE_SETTING="test.integrate2";
	//public static final String TRIAL="1";
	//正式环境
	public static final String KONE_SETTING="integrate2";
	public static final String TRIAL="0";
	public static final String KENO_HTTP_SERVICE="http://"+KONE_SETTING+".v88kgg.com";
	public static final String  XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"" + "iso-8859-1" + "\"?>";
	public static String VendorId="50";
	public static String PlayerCurrency="C01";
	public static String PlayerAllowStake="1,2,3";
	public static String Language="SC";
	public static String VendorSite="keno2.e68.cc";
	public static String FundLink="http://www.e68.ph/asp/payPage.aspx"; 
	
	public static String login(String PlayerId,String PlayerRealName,
			String PlayerIP,String VendorRef,String Remarks) {
		HttpClient httpClient=HttpUtils.createHttpClient();
		PostMethod loginpostMethod=new PostMethod(KENO_HTTP_SERVICE+"/player_enter_keno.php");
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
		buffer.append("<member><name>Trial</name><value><string>"+TRIAL+"</string></value></member>");
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
		PostMethod transferMethod=new PostMethod(KENO_HTTP_SERVICE+"/player_fund_in_out_confirm.php");
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
		PostMethod transferMethod=new PostMethod(KENO_HTTP_SERVICE+"/player_fund_in_out_first.php");
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
		PostMethod getcreditMethod=new PostMethod(KENO_HTTP_SERVICE+"/player_get_credit.php");
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
		} finally {
			if (getcreditMethod != null) {
				getcreditMethod.releaseConnection();
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		String str=Keno2Util.login("woodytest", "woodytest", "127.0.0.1", "1234567", "测试");
		System.out.println(DocumentParser.parseKenologinResponseRequest(str));
	}
	

}
