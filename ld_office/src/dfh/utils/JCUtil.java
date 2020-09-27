package dfh.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import dfh.security.EncryptionUtil;

public class JCUtil {
	private static Logger log = Logger.getLogger(JCUtil.class);
	public static String op_name = "fromld";
	public static String op_pd = "954ac97d07991fa02343f022a30d63d8";
	public static String jc_uri = Configuration.getInstance().getValue("JC_Services");
	
	/**
	 * 注册JC和获得登录key
	 * @param uname
	 * @param password
	 * @return
	 */
	public static String regAndGetLoginKeyJC(String uname, String password) throws Exception{
		HttpClient client = HttpUtils.createHttpClient();
		PostMethod post = new PostMethod();
		try {
			log.info("Register JC --> Username:"+uname);
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			URI uri = new URI(jc_uri+"outlogin.do?method=ssoentry", false);
			post.setURI(uri);
			//HttpMethodParams params = new HttpMethodParams();
			NameValuePair[] param = {new NameValuePair("user_name", "E"+uname),
					new NameValuePair("password", password),
					new NameValuePair("op_name", op_name),
					new NameValuePair("op_secure", getSecure(op_name, op_pd))};
			post.setRequestBody(param);
			//String[] keys = {"user_name","password","op_name","op_secure"};
			//params.setParameters(keys, new Object[]{"L"+uname,password,op_name,op_secure});
			//post.setParams(params);
			client.executeMethod(post);
			return post.getResponseBodyAsString();
		} catch (Exception e) {
			log.error("Register JC Error --> Username:"+uname, e);
			throw e;
		}
	}
	
	/**
	 * 登录JC(暂无用)
	 * @param loginKey
	 * @return
	 * @throws Exception
	 */
	public static String loginJC(String loginKey) throws Exception{
		HttpClient client = HttpUtils.createHttpClient();
		PostMethod post = new PostMethod();
		post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		URI uri = new URI(jc_uri+"trylogin.do", false);
		post.setURI(uri);
		NameValuePair[] param = {new NameValuePair("key", loginKey)};
		post.setRequestBody(param);
		client.executeMethod(post);
		return post.getResponseBodyAsString();
	}
	
	/**
	 * 查询JC余额
	 * @param uname
	 * @return
	 * @throws Exception
	 */
	public static String balanceJC(String uname){
		HttpClient client = HttpUtils.createHttpClient();
		PostMethod post = new PostMethod();
		try {
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			URI uri = new URI(jc_uri+"depAndWit.do?method=queryBalance", false);
			post.setURI(uri);
			NameValuePair[] param = {new NameValuePair("user_name", "E"+uname),
					new NameValuePair("op_name", op_name),
					new NameValuePair("op_secure", getSecure(op_name, op_pd))};
			post.setRequestBody(param);
			client.executeMethod(post);
			String body = post.getResponseBodyAsString();
			log.info("Query JC Balance --> Username:"+uname+body);
			return body;
		} catch (Exception e) {
			log.error("Query JC Balance Error --> Username:"+uname,e);
		}
		return null;
	}
	
	/**
	 * 从JC转入
	 * @param uname
	 * @return
	 * @throws Exception
	 */
	public static String transferFromJC(String uname, Double amount){
		HttpClient client = HttpUtils.createHttpClient();
		PostMethod post = new PostMethod();
		try {
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			URI uri = new URI(jc_uri+"depAndWit.do?method=withdrawal", false);
			post.setURI(uri);
			NameValuePair[] param = {new NameValuePair("user_name", "E"+uname),
					new NameValuePair("withdrawal_amount", amount+""),
					new NameValuePair("op_name", op_name),
					new NameValuePair("op_secure", getSecure(op_name, op_pd))};
			post.setRequestBody(param);
			client.executeMethod(post);
			String body = post.getResponseBodyAsString();
			log.info("Transfer From JC --> Username:"+uname+" amount:"+amount+body);
			return body;
		} catch (Exception e) {
			log.error("Transfer From JC --> Username:"+uname,e);
		}
		return null;
	}
	
	/**
	 * 转出自JC
	 * @param uname
	 * @return
	 * @throws Exception
	 */
	public static String transferToJC(String uname, Double amount){
		HttpClient client = HttpUtils.createHttpClient();
		PostMethod post = new PostMethod();
		try {
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			URI uri = new URI(jc_uri+"depAndWit.do?method=deposit", false);
			post.setURI(uri);
			NameValuePair[] param = {new NameValuePair("user_name", "E"+uname),
					new NameValuePair("deposit_amount", amount+""),
					new NameValuePair("op_name", op_name),
					new NameValuePair("op_secure", getSecure(op_name, op_pd))};
			post.setRequestBody(param);
			client.executeMethod(post);
			String body = post.getResponseBodyAsString();
			log.error("Transfer To JC --> Username:"+uname+" amount:"+amount+body);
			return body;
		} catch (Exception e) {
			log.error("Transfer To JC Error --> UserName:"+uname, e);
		}
		return null;
	} 
	
	/**
	 * 根据日期查询当天玩家盈利情况
	 * @param date
	 * @return
	 */
	public static String dailyProfitJC(String date){
		HttpClient client = HttpUtils.createHttpClient();
		PostMethod post = new PostMethod();
		try {
			post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			URI uri = new URI(jc_uri+"depAndWit.do?method=queryDailyProfit", false);
			post.setURI(uri);
			NameValuePair[] param = {new NameValuePair("date", date),
					new NameValuePair("op_name", op_name),
					new NameValuePair("op_secure", getSecure(op_name, op_pd))};
			post.setRequestBody(param);
			client.executeMethod(post);
			String body = post.getResponseBodyAsString();
			log.error("DailyProfit from JC --> date:"+date+body);
			return body;
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}
	
	private static String getSecure(String op_name, String op_pd){
		return EncryptionUtil.md5Encrypt(op_name+"NNTI"+op_pd);
	}
}
