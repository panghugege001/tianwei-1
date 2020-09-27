package dfh.utils;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import dfh.security.DESEncrypt;
import dfh.security.EncryptionUtil;

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
		}finally{
			if (postMethod!=null) {
				postMethod.releaseConnection();
			}
			if (httpClient!=null) {
	              try {
	                  ((SimpleHttpConnectionManager)httpClient.getHttpConnectionManager()).shutdown();
	              } catch (Exception e) {
	                  e.printStackTrace();
	              }
	              httpClient = null;
	           }
		}
		return -1;
	}
	
	
	public int modifyPassword(String url,String username){
		HttpClient httpClient=new HttpClient();
		PostMethod postMethod=new PostMethod(url);
		postMethod.setParameter("username", username);
		postMethod.setParameter("action", "modifyMemberPassword");
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
		}finally{
			if (postMethod!=null) {
				postMethod.releaseConnection();
			}
			if (httpClient!=null) {
	              try {
	                  ((SimpleHttpConnectionManager)httpClient.getHttpConnectionManager()).shutdown();
	              } catch (Exception e) {
	                  e.printStackTrace();
	              }
	              httpClient = null;
	           }
		}
		
		return -1;
	}
	public void register(String url,String nickname,String username){
		HttpClient httpClient=new HttpClient();
		PostMethod postMethod=new PostMethod(url);
		
//?action=register&nickname=dabao&username=jason&email=jason@e68pb.com		
		postMethod.setParameter("username", username);
		postMethod.setParameter("nickname", nickname);
		postMethod.setParameter("action", "register");
		postMethod.setParameter("email", "your@e68.ph");		
		//String resxml="";
		try {
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
			int status = httpClient.executeMethod(postMethod);
//			String resxml= postMethod.getResponseBodyAsString();
//			System.out.println(resxml);
//			System.out.println(status);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			log.error("执行postMehtod时发生异常", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("执行postMehtod时发生异常", e);
		}finally{
			if (postMethod!=null) {
				postMethod.releaseConnection();
			}
			if (httpClient!=null) {
	              try {
	                  ((SimpleHttpConnectionManager)httpClient.getHttpConnectionManager()).shutdown();
	              } catch (Exception e) {
	                  e.printStackTrace();
	              }
	              httpClient = null;
	           }
		}
		
		//return resxml;
	}
	
	public void e68Register(String url, String username, String password) {
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
			int status = httpClient.executeMethod(postMethodTwo);
			String resxml = postMethodTwo.getResponseBodyAsString();
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			System.out.println("执行postMehtod时发生异常" + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("执行postMehtod时发生异常" + e);
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
		clientHelper.e68Register("http://bbs.e68ph.net/member.php", "dt1234561", "123456");
//		for (int i = 0; i < 100; i++) {
//			System.out.println(new Date().getTime());
//		}
//		HttpClientHelper httpHelper=new HttpClientHelper();
//		try {
//			//httpHelper.register("http://bbs.e68ph.net/register.do", StringUtil.convertByteArrayToHexStr("woody123".getBytes("gbk")), StringUtil.convertByteArrayToHexStr("woody123".getBytes("gbk")));
//			String params="cagent=B18_AG/\\\\/loginname=woodytest/\\\\/method=lg/\\\\/actype=0/\\\\/password=123456";
//			String encrypt_key="jsn72ksm";
//			DESEncrypt des=new DESEncrypt(encrypt_key);
//			String targetParams=des.encrypt(params);
//			String key=EncryptionUtil.encryptPassword(targetParams+"jsn72ksmm");
//			String url="http://220.90.206.25:81/doBusiness.do?params="+targetParams+"&key="+key;
//			HttpClient httpClient=new HttpClient();
//			PostMethod postMethod=new PostMethod(url);
//			System.out.println(httpClient.executeMethod(postMethod));
//			System.out.println(postMethod.getResponseBodyAsString());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
}
