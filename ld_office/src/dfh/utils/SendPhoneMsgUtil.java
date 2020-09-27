package dfh.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;
import dfh.model.enums.FilterStatus;
import dfh.model.enums.FilterType;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import org.apache.axis.encoding.Base64;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.todaynic.client.mobile.SMS;

import dfh.utils.sendemail.HttpUtils;

//时代互联
public class SendPhoneMsgUtil {
	private static Log log = LogFactory.getLog(SendPhoneMsgUtil.class);

	private static SMS sms;

	public synchronized static String BASE64Encoder(String str)
			throws Exception {
		return Base64.encode(str.getBytes());
	}

	public static String sendSms(String phoneNo, String msg) {
		if (sms == null) {
			Hashtable config = new Hashtable();
			config.put("VCPSERVER", "sms.todaynic.com");
			config.put("VCPSVPORT", "20002");
			config.put("VCPUSERID", "ms101987");
			config.put("VCPPASSWD", "mti2ot");
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
	/**
	 * 获取数据库连接
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static  String getPhpPtService(String key) throws Exception {
		Properties properties = new Properties();
		properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("init.properties"));
		return properties.getProperty(key);
	}
	public static String callTwo(String phone, String msg) {
		String result = null;
		try {
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod("http://utf8.sms.webchinese.cn");
			post.addRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");// 在头文件中设置转码
			NameValuePair[] data = { new NameValuePair("Uid", "agsmc99"),
					new NameValuePair("Key", "cfdc4a63c93a9971c8b5"),
					new NameValuePair("smsMob", phone),
					new NameValuePair("smsText", msg) };
			post.setRequestBody(data);

			client.executeMethod(post);
			Header[] headers = post.getResponseHeaders();
			int statusCode = post.getStatusCode();
			System.out.println("statusCode:" + statusCode);
			result = new String(post.getResponseBodyAsString()
					.getBytes("UTF-8"));
			post.releaseConnection();
			if (result.equals("1")) {
				return "发送成功";
			} else if (result.equals("-4")) {
				return "手机号格式不正确";
			} else if (result.equals("-3")) {
				return "短信数量不足";
			} else if (result.equals("-11")) {
				return "账号被禁用";
			} else {
				return result;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 国际短信服务
	 * @param phone
	 * @param msg
	 * @return
	 */
	public static String InternationSms(String phone, String msg) {


		int countSuccess=0;
		int countFail=0;
		int count=0;
		int filterCount=0;
		phone=phone.replace("，","," );
		phone=phone.replace(";", ",");
		String returnMsg = "";   //返回信息
		String saveStr = null;
		List<String>resultStr=new ArrayList<String>();
		String[] phoneTotal=phone.split(",");
			/*for (int i = 0; i < phoneTotal.length; i++) {
				count++;

				if (StringUtil.isEmpty(saveStr))
					saveStr = "86"+phoneTotal[i];
				else
					saveStr = saveStr + "," +"86"+phoneTotal[i];

				if (i==phoneTotal.length-1) {
					resultStr.add(saveStr);
				}else if (count == 50) {
					resultStr.add(saveStr);
					count = 0;
					saveStr = null;
				}
			}*/

		StringBuffer sbContent = new StringBuffer();

		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			String mysqlUrl = SpecialEnvironmentStringPBEConfig.decryptPBEConfig(getPhpPtService("datasource.url").replace("ENC(", "").replace(")", ""));
			String mysqlname = SpecialEnvironmentStringPBEConfig.decryptPBEConfig(getPhpPtService("datasource.username").replace("ENC(", "").replace(")", ""));
			String mysqlPassword = SpecialEnvironmentStringPBEConfig.decryptPBEConfig(getPhpPtService("datasource.password").replace("ENC(", "").replace(")", ""));

//			String mysqlUrl = getPhpPtService("datasource.url").replace("ENC(", "").replace(")", "");
//			String mysqlname = getPhpPtService("datasource.username").replace("ENC(", "").replace(")", "");
//			String mysqlPassword = getPhpPtService("datasource.password").replace("ENC(", "").replace(")", "");


			conn = DriverManager.getConnection(mysqlUrl, mysqlname, mysqlPassword);
			System.out.println("连接MySql成功!!!");
			// 查询需要过滤的内容
			StringBuffer sbf = new StringBuffer();
			sbf.append(" select fd.filter_content as content,fd.filter_type as type,fd.filter_status as status ");
			sbf.append(" from filter_data fd ");
			sbf.append(" where fd.filter_type = '"+ FilterType.PHONE.getCode()+"'");
			sbf.append(" 	and fd.filter_status = '"+ FilterStatus.Y.getCode()+"'");
			System.out.println("sbf==="+sbf);
			pstmt = conn.prepareStatement(sbf.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String content = rs.getString("content");
				sbContent = sbContent.append(content + ",");
			}
			if (null!=rs ) {
				rs.close();
				rs = null;
			}
			if (null!=pstmt) {
				pstmt.close();
				pstmt = null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (null!=stmt) {
					stmt.close();
					stmt = null;
				}
				if (null!=conn) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//过滤的内容，数组形式
		String[] filterPhone = null;
		if(sbContent != null && sbContent.length() > 0) {
			filterPhone=sbContent.toString().split(",");
		}

		try {
			for (int i = 0; i < phoneTotal.length; i++) {
				boolean flag = false;
				if(filterPhone != null && filterPhone.length > 0) {
					for (int j = 0; j < filterPhone.length; j++) {
						if(("86"+phoneTotal[i]).contains(filterPhone[j])) {
							filterCount ++;
							flag = true;
							break;
						}else {
							flag = false;
						}
					}
				}

				if(!flag) {
					HttpClient httpClient=new HttpClient();
					PostMethod postMethod = new PostMethod("http://api.motosms.com/json");
					postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
					NameValuePair[] data= {
							new NameValuePair("key","u5trz4"),
							new NameValuePair("secret","qaiFc0pY"),
							new NameValuePair("to","86"+phoneTotal[i]),
							new NameValuePair("text",msg),
					};
					postMethod.setRequestBody(data);
					int    statusCode    =    httpClient.executeMethod(postMethod);
					String soapRequestData =    postMethod.getResponseBodyAsString();
					if(statusCode==200)
					{
						JSONObject json_test = JSONObject.parseObject(soapRequestData);
						if (StringUtil.equals(json_test.get("status").toString(), "0"))
							count++;
						else
							countFail++;
					}else{
						countFail++;
					}
				}
			}
			log.info("\n...................发送数量:"+phoneTotal.length+",过滤数量:"+filterCount+",发送成功数量:"+count+",发送失败数量:"+countFail);

		} catch (Exception e) {
			e.printStackTrace();
			return "发送失败：" + e.getMessage();
		}
		return "发送数量:"+phoneTotal.length+",过滤数量:"+filterCount+",发送成功数量:"+count+",发送失败数量:"+countFail;


	}

	/**
	 * 国际短信服务(产品电服使用注意使用通道)
	 * @param phone
	 * @param msg
	 * @return
	 */
	public static String InternationDFSms(String phone, String msg) {
		int countFail=0;
		int count=0;
		int filterCount=0;
		phone=phone.replace("，","," );
		phone=phone.replace(";", ",");
		String[] phoneTotal=phone.split(",");
		StringBuffer sbContent = new StringBuffer();
		Connection conn = null;
		Statement stmt = null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			String mysqlUrl = SpecialEnvironmentStringPBEConfig.decryptPBEConfig(getPhpPtService("datasource.url").replace("ENC(", "").replace(")", ""));
			String mysqlname = SpecialEnvironmentStringPBEConfig.decryptPBEConfig(getPhpPtService("datasource.username").replace("ENC(", "").replace(")", ""));
			String mysqlPassword = SpecialEnvironmentStringPBEConfig.decryptPBEConfig(getPhpPtService("datasource.password").replace("ENC(", "").replace(")", ""));
			conn = DriverManager.getConnection(mysqlUrl, mysqlname, mysqlPassword);
			System.out.println("连接MySql成功!!!");
			// 查询需要过滤的内容
			StringBuffer sbf = new StringBuffer();
			sbf.append(" select fd.filter_content as content,fd.filter_type as type,fd.filter_status as status ");
			sbf.append(" from filter_data fd ");
			sbf.append(" where fd.filter_type = '"+FilterType.PHONE.getCode()+"'");
			sbf.append(" 	and fd.filter_status = '"+FilterStatus.Y.getCode()+"'");
			System.out.println("sbf==="+sbf);
			pstmt = conn.prepareStatement(sbf.toString());
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String content = rs.getString("content");
				sbContent = sbContent.append(content + ",");
			}
			if (null!=rs ) {
				rs.close();
				rs = null;
			}
			if (null!=pstmt) {
				pstmt.close();
				pstmt = null;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (null!=stmt) {
					stmt.close();
					stmt = null;
				}
				if (null!=conn) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//过滤的内容，数组形式
		String[] filterPhone = null;
		if(sbContent != null && sbContent.length() > 0) {
			filterPhone=sbContent.toString().split(",");
		}

		try {
			for (int i = 0; i < phoneTotal.length; i++) {
				boolean flag = false;
				if(filterPhone != null && filterPhone.length > 0) {
					for (int j = 0; j < filterPhone.length; j++) {
						if(("86"+phoneTotal[i]).contains(filterPhone[j])) {
							filterCount ++;
							flag = true;
							break;
						}else {
							flag = false;
						}
					}
				}

				if(!flag) {
					HttpClient httpClient=new HttpClient();
					PostMethod postMethod = new PostMethod("http://api.motosms.com/json");
					postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8");
					NameValuePair[] data= {
							new NameValuePair("key","aljbok"),
							new NameValuePair("secret","EW1udY7c"),
							new NameValuePair("to","86"+phoneTotal[i]),
							new NameValuePair("text",msg),
					};
					postMethod.setRequestBody(data);
					int    statusCode    =    httpClient.executeMethod(postMethod);
					String soapRequestData =    postMethod.getResponseBodyAsString();
					if(statusCode==200)
					{
						JSONObject json_test = JSONObject.parseObject(soapRequestData);
						if (StringUtil.equals(json_test.get("status").toString(), "0"))
							count++;
						else
							countFail++;
					}else{
						countFail++;
					}
				}
			}
			log.info("\n...................发送数量:"+phoneTotal.length+",过滤数量:"+filterCount+",发送成功数量:"+count+",发送失败数量:"+countFail);

		} catch (Exception e) {
			e.printStackTrace();
			return "发送失败：" + e.getMessage();
		}
		return "发送数量:"+phoneTotal.length+",过滤数量:"+filterCount+",发送成功数量:"+count+",发送失败数量:"+countFail;

	}

	public static String callMine(String phone, String msg) throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		long d = Math.round(Math.random() * (32 - 1) + 1);
		System.out.println(d + "短信端口");
		String u = "http://115.238.248.131:808/goip_send_sms.html?username=root&password=root&port="
				+ d + "&recipients=" + phone + "&sms=" + msg;
		System.out.println(u);
		GetMethod method = new GetMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"UTF-8");

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
		log.info("result-->" + result);
		net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(result);
		if (result.contains("OK")) {
			return "发送成功";
		} else {
			return json.getString("reason");
		}
	}

	public static String callThree(String phone, String msg) throws Exception {
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://www.qz876.com/servlet/UserServiceAPI";

		PostMethod method = new PostMethod(u);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,
				"GBK");

		NameValuePair methodName = new NameValuePair("method", "sendSMS");
		NameValuePair smstype = new NameValuePair("smstype", "1");
		NameValuePair username = new NameValuePair("username", "971845");
		NameValuePair password = new NameValuePair("password",
				BASE64Encoder("123"));// 密码采用BASE64加密
		NameValuePair mobile = new NameValuePair("mobile", "13120975740");
		NameValuePair content = new NameValuePair("content", msg);

		method.setRequestBody(new NameValuePair[] { methodName, smstype,
				username, password, mobile, content });
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
		log.info("result-->" + result);
		if (result.startsWith("success")) {
			return "发送成功";
		} else {
			return result.split(";")[1];
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

	public static String callfour(String phone, String msg) {
		phone=phone.replace("，","," );
		phone=phone.replace(";", ",");
		String str = "";
		// 发送参数
		/*	OpenApi.initialzeAccount(sOpenUrl, account, authkey, cgid, csid);
		// 状态及回复参数
		DataApi.initialzeAccount(sDataUrl, account, authkey);
		List<SendResult> listItem = OpenApi.sendOnce(phone, msg, 0, 0, null);
		if (null != listItem && listItem.size() > 0) {
			SendResult t = listItem.get(0);
			if (t.getResult() < 1) {
				str = t.getResult() + t.getErrMsg();
			} else {
				str = "发送成功";
			}
		} else {
			str = "发送失败！";
		}
		 */
		String result = "";
     	msg=java.net.URLEncoder.encode(msg);
		HttpClient httpClient = HttpUtils.createHttpClient();
		String u = "http://wapi.c123.cn/tx/";
		PostMethod method = new PostMethod(u);
		method.setParameter("uid", "50114721");
		method.setParameter("pwd", "9a4a9de952123f72a9da042bf497f955");
		method.setParameter("encode", "utf8");
		method.setParameter("mobile", phone);
		method.setParameter("content", msg);
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
			if ("100".equals(result)) {
				str = "发送成功";
			} else {
				str = result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return str;
	}
	
	/**
	 * @author CK
	 * 闪信通短信平台 
	 * @param phone
	 * @param msg
	 * @return
	 */
	public static String shxtongSend(String phone, String msg) {
		phone=phone.replace("，","," );
		phone=phone.replace(";", ",");
		String returnMsg = "";   //返回信息
		String saveStr = null;
		int count=0; 
		List<String>resultStr=new ArrayList<String>();
		String[] phoneTotal=phone.split(",");
		if (phoneTotal.length<100) {
			  resultStr.add(phone);
		}else {
			for (int i = 0; i < phoneTotal.length; i++) {
				count++;
				
				if (StringUtil.isEmpty(saveStr))
					saveStr = phoneTotal[i];
				else
					saveStr = saveStr + "," + phoneTotal[i];

				if (i==phoneTotal.length-1) 
					resultStr.add(saveStr);
				
				if (count == 100) {
					resultStr.add(saveStr);
					count = 0;
					saveStr = null;
				}
			}
		}

		
		String url="http://119.145.253.67:8080/edeeserver/sendSMS.do";
		String username="lehu888";
		String password="Aa123456";
//	    String TimeStamp = DateUtil.fmtyyyyMMddHHmmss(new Date());
		
		try {
			for (int i = 0; i < resultStr.size(); i++) {
				HttpClient httpClient = new HttpClient();
				PostMethod postMethod = new PostMethod(url);
				postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
			         	NameValuePair[] data = {
						new NameValuePair("UserName", username),
						// new NameValuePair("TimeStamp",""),
						new NameValuePair("Password", password),
						new NameValuePair("MobileNumber", resultStr.get(i)),
						new NameValuePair("MsgContent", msg),
				      // new NameValuePair("MsgIdentify","")
				};
				postMethod.setRequestBody(data);
				int statusCode = httpClient.executeMethod(postMethod);
				String soapRequestData = postMethod.getResponseBodyAsString();
				if (soapRequestData != null && soapRequestData.equals("0")) {
					returnMsg = "发送成功(code:"+statusCode+"),共发送短信 :" + phoneTotal.length + "条";
				} else {
					returnMsg = "发送失败,错误信息:" + soapRequestData;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg = "发送失败：" + e.getMessage();
		} 
		return returnMsg;
	}
	
	/**
	 * msg1平台 
	 * @param phone
	 * @param msg
	 * @return
	 */
	public static String bcSend(String phone, String msg) {
		phone=phone.replace("，","," );
		phone=phone.replace(";", ",");
		String returnMsg = "";   //返回信息
		String saveStr = null;
		int count=0; 
		List<String>resultStr=new ArrayList<String>();
		String[] phoneTotal=phone.split(",");
			for (int i = 0; i < phoneTotal.length; i++) {
				count++;
				
				if (StringUtil.isEmpty(saveStr))
					saveStr = "0086"+phoneTotal[i];
				else
					saveStr = saveStr + "|" +"0086"+phoneTotal[i]; 

				if (i==phoneTotal.length-1) 
					resultStr.add(saveStr);
				
				if (count == 100) {
					resultStr.add(saveStr);
					count = 0;
					saveStr = null;
				}
			}

		String url="http://msg1.as1010.com/SendSms.asp";
		String id="AGIVI00801";
		
		try {
			for (int i = 0; i < resultStr.size(); i++) {		
			HttpClient httpClient=new HttpClient();
			PostMethod postMethod = new PostMethod(url); 
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8"); 
				        NameValuePair[] data= {    
				        new NameValuePair("id",id),  
				        new NameValuePair("to",resultStr.get(i)),  
				        new NameValuePair("msg",msg),  
				        };  
				        postMethod.setRequestBody(data);  
				        int    statusCode    =    httpClient.executeMethod(postMethod); 
				        String soapRequestData =    postMethod.getResponseBodyAsString(); 
				        if(soapRequestData!=null&&soapRequestData.equals("1"))
						{
				        	returnMsg = "发送成功(code:"+statusCode+"),共发送短信 :" + phoneTotal.length+ "条";
						}else{
							returnMsg = "发送失败 - 错误信息:" + soapRequestData;
						}
			}
			
			log.info("\n..................."+returnMsg+"...................");
		} catch (Exception e) {
			e.printStackTrace();
			returnMsg = "发送失败：" + e.getMessage();
		} 
		return returnMsg;
	}
	
	public static String sms63(String phone, String msg) {
		phone=phone.replace("，","," );
		phone=phone.replace(";", ",");
		int count=0; 
		int countFail=0;
		String[] phoneTotal=phone.split(",");
		try {
			for (int i = 0; i < phoneTotal.length; i++) {
			HttpClient httpClient=new HttpClient();
			PostMethod postMethod = new PostMethod("http://user.sms63.com:8888/sms.aspx"); 
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"UTF-8"); 
				        NameValuePair[] data= {    
				        new NameValuePair("action","send"), 
				        new NameValuePair("userid","504"), 
				        new NameValuePair("account","ivan"), 
				        new NameValuePair("password","aa888888"), 
				        new NameValuePair("mobile",phoneTotal[i]),  
				        new NameValuePair("content","["+(int)((Math.random()*9+1)*1000)+"]"+msg),  
				        };  
				        postMethod.setRequestBody(data);  
				        int    statusCode    =    httpClient.executeMethod(postMethod); 
				        String soapRequestData =    postMethod.getResponseBodyAsString(); 
				        String status=DomOperator.compileVerifyData("<returnstatus>(.*?)</returnstatus>", soapRequestData);
				        String message=DomOperator.compileVerifyData("<message>(.*?)</message>", soapRequestData);
				        //String remainpoint=DomOperator.compileVerifyData("<remainpoint>(.*?)</remainpoint>", soapRequestData);
				       // String successCounts=DomOperator.compileVerifyData("<successCounts>(.*?)</successCounts>", soapRequestData);
				        if(statusCode==200&&StringUtil.equals("Success", status))
						{
				        	count++;
						}else{
							countFail++;
						}
			}

			log.info("\n...................发送数量:"+phoneTotal.length+",发送成功数量:"+count+",发送失败数量:"+countFail);
		} catch (Exception e) {
			e.printStackTrace();
		    return	"发送失败：" + e.getMessage();
		} 
		return "发送数量:"+phoneTotal.length+",发送成功数量:"+count+",发送失败数量:"+countFail;
	}
	

	public static void main(String[] args) throws Exception {
		System.out.println(callfour("13120959046", "测试123qwesad"));
		// System.out.println(callTwo("13120971845", "请注意查收您的短信验证码。【1234】"));;
		try {
			// System.out.println(callThree("13120975740", "您的验证码为：" + 1234));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
	}

}
