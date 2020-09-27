package dfh.utils;

import java.util.Hashtable;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shcm.bean.SendResult;
import com.shcm.send.DataApi;
import com.shcm.send.OpenApi;
import com.todaynic.client.mobile.SMS;

import dfh.json.JSONObject;

//时代互联
public class SendPhoneMsgUtil {  
	private static Log log = LogFactory.getLog(SendPhoneMsgUtil.class);
	
	private static SMS sms;
	
	
	public synchronized static String BASE64Encoder(String str)throws Exception {
		return new sun.misc.BASE64Encoder().encode(str.getBytes());
	}
	
	public static String sendSms(String phoneNo, String msg) {
		if (sms == null) {
			Hashtable config = new Hashtable();
			config.put("VCPSERVER", "sms.todaynic.com");
			config.put("VCPSVPORT", "20002");
			config.put("VCPUSERID", "ms101987");
			config.put("VCPPASSWD", "mti2ot");
//			config.put("VCPUSERID", "ms102035");
//			config.put("VCPPASSWD", "mtq4od");
			sms = new SMS(config);
			sms.setEncodeType("UTF-8");
		}
		log.info((new StringBuilder("send sms to ")).append(phoneNo).toString());
		try {
			sms.sendSMS(phoneNo, msg, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("2000".equals(sms.getCode()))
			return "发送成功";
		else
			return sms.getMsg();
	}
	
	
	public static String callTwo(String phone, String msg){
		String result = null ;
		try {
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod("http://utf8.sms.webchinese.cn"); 
			post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");//在头文件中设置转码
			NameValuePair[] data ={ new NameValuePair("Uid", "agsmc99"),new NameValuePair("Key", "cfdc4a63c93a9971c8b5"),new NameValuePair("smsMob",phone),new NameValuePair("smsText",msg)};
			post.setRequestBody(data);

			client.executeMethod(post);
			Header[] headers = post.getResponseHeaders();
			int statusCode = post.getStatusCode();
			System.out.println("statusCode:"+statusCode);
			result = new String(post.getResponseBodyAsString().getBytes("UTF-8")); 
			post.releaseConnection();
			if(result.equals("1")){
				return "发送成功";
			}else if(result.equals("-4")){
				return "手机号格式不正确";
			}else if(result.equals("-3")){
				return "短信数量不足";
			}else if(result.equals("-11")){
				return "账号被禁用";
			}else {
				return result;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result; 
	}
	
	
	public static String callThree(String phone, String msg) throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://www.qz876.com/servlet/UserServiceAPI";

		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"GBK"); 
		
		NameValuePair methodName = new NameValuePair("method", "sendSMS");
		NameValuePair smstype = new NameValuePair("smstype", "1");
		NameValuePair username = new NameValuePair("username", "971845");
		NameValuePair password = new NameValuePair("password",BASE64Encoder("123"));// 密码采用BASE64加密
		NameValuePair mobile = new NameValuePair("mobile", phone);
		NameValuePair content = new NameValuePair("content", msg);
		
		method.setRequestBody(new NameValuePair[] { methodName, smstype,username, password, mobile, content });
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info("result-->"+result);
		if(result.startsWith("success")){
			return "发送成功";
		}else{
			return result.split(";")[1];
		}
	}
	
	public static String callMine(String phone, String msg) throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		long d = Math.round(Math.random()*(32-1)+1);
		System.out.println(d+"短信端口");
		String u = "http://115.238.248.131:808/goip_send_sms.html?username=root&password=root&port="+d+"&recipients="+phone+"&sms="+msg;
        System.out.println(u);
		GetMethod method = new GetMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8"); 
		
		
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		log.info("result-->"+result);
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result) ;
		if(result.contains("OK")){
			return "发送成功";
		}else{
			return json.getString("reason") ;
		}
	}
	
	
	
	
	private static String sOpenUrl = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
	private static String sDataUrl = "http://smsapi.c123.cn/DataPlatform/DataApi";
	
	// 接口帐号
	private static final String account = "1001@501113300015";
	
	// 接口密钥
	private static final String authkey = "257D62C5E1F23A2DC8D5110386BF42BF";
	
	// 通道组编号
	private static final int cgid = 7738;
	
	// 默认使用的签名编号(未指定签名编号时传此值到服务器)
	private static final int csid = 0;
	public static String callfour(String phone, String msg){
		String str="";
		// 发送参数
		OpenApi.initialzeAccount(sOpenUrl, account, authkey, cgid, csid);
		// 状态及回复参数
		DataApi.initialzeAccount(sDataUrl, account, authkey);
		List<SendResult> listItem =OpenApi.sendOnce(phone, msg, 0, 0, null);
		if(null!=listItem&&listItem.size()>0){
		SendResult t = listItem.get(0);
		if(t.getResult()<1){
			str=t.getResult()+"";
		}else {
			str="发送成功";
		}
		}else{
			str="发送失败！";
		}
		return str;
	}
	
	
	
	public static void main(String[] args) {
		System.out.println( Math.round(Math.random()*(32-1)+1));
//		System.out.println(sendSms("13120975740", "1234"));
		try {
//			System.out.println(callTwo("13120967646", "1234"));
//			System.out.println(callThree("13120967646", "1234"));
			System.out.println(callfour("18237188961", "7914"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}

}
