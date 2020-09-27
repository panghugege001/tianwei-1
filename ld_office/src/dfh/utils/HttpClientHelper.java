package dfh.utils;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class HttpClientHelper {
	
	private Logger log=Logger.getLogger(HttpClientHelper.class);
	

	public int modifyNickName(String url,String username,String nickname){
		HttpClient httpClient=new HttpClient();
		PostMethod postMethod=new PostMethod(url);
		postMethod.setParameter("username", username);
		postMethod.setParameter("nickname", nickname);
		postMethod.setParameter("action", "modifyMemberNickName");
	//	String resxml="";
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
			return httpClient.executeMethod(postMethod);
			
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			log.error("执行postMehtod时发生异常", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("执行postMehtod时发生异常", e);
		}
		finally{
			if (postMethod!=null) {
				postMethod.releaseConnection();
			}
		}
		
		return -1;
	}
	
	

	public int modifyEA_bbsMemberPassword(String url,String username){
		HttpClient httpClient=new HttpClient();
		PostMethod postMethod=new PostMethod(url);
		postMethod.setParameter("username", username);
		postMethod.setParameter("action", "modifyMemberPassword");
//		String resxml="";
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
			return httpClient.executeMethod(postMethod);
//			resxml= postMethod.getResponseBodyAsString();
			
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			log.error("执行postMehtod时发生异常", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("执行postMehtod时发生异常", e);
		}
		finally{
			if (postMethod!=null) {
				postMethod.releaseConnection();
			}
		}
		
		return -1;
	}
	public int synRegister(String url,String nickname,String username){
		HttpClient httpClient=new HttpClient();
		PostMethod postMethod=new PostMethod(url);
		
//?action=register&nickname=dabao&username=jason&email=your@e68.ph		
		postMethod.setParameter("username", username);
		postMethod.setParameter("nickname", nickname);
		postMethod.setParameter("action", "register");
		postMethod.setParameter("email", "your@qy8.cc");		
//		String resxml="";
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
			return httpClient.executeMethod(postMethod);
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			log.error("执行postMehtod时发生异常", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("执行postMehtod时发生异常", e);
		}
		finally{
			if (postMethod!=null) {
				postMethod.releaseConnection();
			}
		}
		
		return 1;
	}
	
	public int e68Register(String url, String username, String password) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url+"?mod=register");
		PostMethod postMethodTwo = new PostMethod(url);
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
			int s = httpClient.executeMethod(postMethod);
			String registerXml = postMethod.getResponseBodyAsString().trim();
			String formhash = registerXml.substring(registerXml.indexOf("formhash") + 17, registerXml.indexOf("formhash") + 25);
			String inputName = registerXml.substring(registerXml.indexOf("用户名:</label>")-8, registerXml.indexOf("用户名:</label>") - 2);
			String inputPassword= registerXml.substring(registerXml.indexOf("密码:</label>")-8, registerXml.indexOf("密码:</label>") - 2);
			String inputPasswordTwo = registerXml.substring(registerXml.indexOf("确认密码:</label>")-8, registerXml.indexOf("确认密码:</label>") - 2);
			String inputMailbox = registerXml.substring(registerXml.indexOf("Email:</label>")-8, registerXml.indexOf("Email:</label>") - 2);
			postMethodTwo.setParameter("mod", "register");
			postMethodTwo.setParameter("regsubmit", "yes");
			postMethodTwo.setParameter("formhash", formhash);
			postMethodTwo.setParameter("referer", "http://bbs.e68ph.net/member.php");
			postMethodTwo.setParameter("activationauth", "");
			postMethodTwo.setParameter(inputName, username);
			postMethodTwo.setParameter(inputPassword, password);
			postMethodTwo.setParameter(inputPasswordTwo, password);
			postMethodTwo.setParameter(inputMailbox, username + "@e68.ph");
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
			//int status = httpClient.executeMethod(postMethodTwo);
			//String resxml = postMethodTwo.getResponseBodyAsString();
			return httpClient.executeMethod(postMethodTwo);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			System.out.println("执行postMehtod时发生异常" + e);
			return 100;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("执行postMehtod时发生异常" + e);
			return 100;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
			if (postMethodTwo != null) {
				postMethodTwo.releaseConnection();
			}
			if (httpClient != null) {
				try {
					((SimpleHttpConnectionManager) httpClient.getHttpConnectionManager()).shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				}
				httpClient = null;
			}
		}
	}
	
	public static void main(String[] args) {
		HttpClientHelper clientHelper=new HttpClientHelper();
		System.out.println(clientHelper.e68Register("http://bbs.e68ph.net/member.php", "dt123455674", "123456"));
	}
	
	
}
