package dfh.utils;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;

import com.sun.org.apache.xml.internal.security.utils.Base64;



/**
 * 同略云 自动转账API
 * 
 * @author
 * 
 */
public class TlyUtil {
	
	private static Logger log = Logger.getLogger(TlyUtil.class);
	private static String HMAC_SHA1_ALGORITHM="HmacSHA256";
	public static String key = "ab1f10553128012d8883ebaed5f30bf23b537c6468e465b246d6799e";
	
	
	
	
	/***
	 * 同略云 添加银行卡
	 * Description 
	 * @param pno
	 * @param operator
	 * @param ip
	 * @param loginName
	 * @return 
	 */
	
	public static String api_add_member_card(String card_number,String real_name,String bank_city,String  bank_flag,String bank_area,String  bank_provinces ){
	        String sign;
	        JSONObject data = new JSONObject();
	        data.put("module", "bankcard");
	        data.put("method", "api_add_member_card");
	        data.put("company_name",  "ld");
	        JSONObject payload =  new JSONObject();
	        payload.put("card_number", card_number);//卡号
	        payload.put("real_name", real_name);//持卡人姓名
	        payload.put("bank_city", bank_city);//卡所在城市
	        payload.put("bank_branch", "");//分理处
	        payload.put("bank_flag", bank_flag);//银行名称
	        payload.put("bank_area", bank_area);//卡所在地区:中国
	        payload.put("bank_provinces", bank_provinces);//省份 
	        payload.put("trans_mode", "out_trans");//同添加订单
	        data.put("payload", payload);
	        try {
	        	  SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC_SHA1_ALGORITHM);
	        	  Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
	        	  mac.init(signingKey);
	        	  byte[] rawHmac = mac.doFinal(data.toString().getBytes("UTF-8"));
	        	  sign = base64Encode(rawHmac);
			} catch (Exception e) {
				 sign = "" ;
                e.printStackTrace();
			}
	        log.info(">>>>data=" + data.toString());
	        PostMethod postMethod = new PostMethod();
	        String phpHtml="";
	        try {
	        	  HttpClient httpClient = new HttpClient();
	    		  postMethod = new PostMethod("https://www.tly-transfer.com/sfisapi/");
	        	  postMethod.addRequestHeader("TLYHMAC",sign);
	        	  postMethod.addRequestHeader("content-type","application/json");
	        	  RequestEntity re = new StringRequestEntity(data.toString(), "application/json", "UTF-8");
	        	  postMethod.setRequestEntity(re);
	        	  int statusCode = httpClient.executeMethod(postMethod);
		      	  if (statusCode == HttpStatus.SC_OK) {
						phpHtml = postMethod.getResponseBodyAsString();
						if (phpHtml == null || phpHtml.equals("")) {	
							System.out.println("请求接口出现问题！");
						     log.info(">>>>请求接口出现问题！");
						}
					} else {
					    log.info("Response Code: " + statusCode);
					}
		      	  return phpHtml;
				}   catch(Exception e){
					e.printStackTrace();   
					 log.info("Response 消息: " + e.toString());
				}finally {
					if (postMethod != null) {
						postMethod.releaseConnection();
					}
				}
	           return phpHtml;
	    }
	
	
	
	/***
	 * 同略云 添加订单
	 * Description 
	 * @param pno
	 * @param operator
	 * @param ip
	 * @param loginName
	 * @return 
	 */
	
	public static String api_add_order(String card_number, Double money, String billno,String realname)  {
        String sign;
        JSONObject data = new JSONObject();
        data.put("module", "order");
        data.put("method", "api_add_order");
        data.put("company_name",  "ld");
        JSONObject payload =  new JSONObject();
        payload.put("card_number", card_number);//卡号
        payload.put("amount", money);//金额
        payload.put("trans_mode", "out_trans");//转账模式
        payload.put("order_number", billno);//订单号
        payload.put("atfs_flag", "xxxx");//转账设置
        payload.put("real_name",realname );//
        data.put("payload", payload);
        try {
        	  SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC_SHA1_ALGORITHM);
        	  Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        	  mac.init(signingKey);
        	  byte[] rawHmac = mac.doFinal(data.toString().getBytes("UTF-8"));
        	  sign = base64Encode(rawHmac);
		} catch (Exception e) {
			 sign = "" ;
             e.printStackTrace();
		}
        
        log.info(">>>>data=" + data.toString());
        PostMethod postMethod = new PostMethod();
        String phpHtml ="";
        try {
        	  HttpClient httpClient = new HttpClient();
    		  postMethod = new PostMethod("https://www.tly-transfer.com/sfisapi/");
        	  postMethod.addRequestHeader("TLYHMAC",sign);
        	  postMethod.addRequestHeader("content-type","application/json");
        	  RequestEntity re = new StringRequestEntity(data.toString(), "application/json", "UTF-8");
        	  postMethod.setRequestEntity(re);
        	  int statusCode = httpClient.executeMethod(postMethod);
	      	  if (statusCode == HttpStatus.SC_OK) {
				    phpHtml = postMethod.getResponseBodyAsString();
					if (phpHtml == null || phpHtml.equals("")) {	
						log.info(">>>>请求接口出现问题！");
					}
				} else {
					  log.info("Response Code: " + statusCode);
				}
	      	  return phpHtml;
			}   catch(Exception e){
				e.printStackTrace();   
				log.info("Response 消息: " + e.toString());
			}finally {
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
			}
           return phpHtml;
	}
	
	
	
	/***
	 * 同略云  查询订单状态
	 * Description 
	 * @param pno
	 * @param operator
	 * @param ip
	 * @param loginName
	 * @return 
	 */
	
	public static String api_get_order_status(String order_number)  {
        String sign;
        String phpHtml =null;
        JSONObject data = new JSONObject();
        data.put("module", "order");
        data.put("method", "api_get_order_status");
        data.put("company_name",  "ld");
        JSONObject payload =  new JSONObject();
        payload.put("order_number", order_number);//订单号
        data.put("payload", payload);
        try {
        	  SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC_SHA1_ALGORITHM);
        	  Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        	  mac.init(signingKey);
        	  byte[] rawHmac = mac.doFinal(data.toString().getBytes("UTF-8"));
        	  sign = base64Encode(rawHmac);
		} catch (Exception e) {
             e.printStackTrace();
             return null;
		}
        log.info(">>>>sign："+sign);
        log.info(">>>>data=" + data.toString());
        HttpClient httpClient =null;
        PostMethod postMethod = null;
        try {
        	  httpClient = createHttpClient();
    		  postMethod = new PostMethod("https://www.tly-transfer.com/sfisapi/");
    		  postMethod.setRequestHeader("Connection", "close");
        	  postMethod.addRequestHeader("TLYHMAC",sign);
        	  postMethod.addRequestHeader("content-type","application/json");
        	  RequestEntity re = new StringRequestEntity(data.toString(), "application/json", "UTF-8");
        	  postMethod.setRequestEntity(re);
        	  int statusCode = httpClient.executeMethod(postMethod);
	      	  if (statusCode == HttpStatus.SC_OK) {
				    phpHtml = postMethod.getResponseBodyAsString();
					if (phpHtml == null || phpHtml.equals("")) {	
						log.info(">>>>请求接口出现问题！");
					}
			 }
	      	 else {
					  log.info("Response Code: " + statusCode);
			}
	      	  return phpHtml;
			}   catch(Exception e){
				e.printStackTrace();   
				log.info("Response 消息: " + e.toString());
			}finally {
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
			}
           return phpHtml;
	}
	
	
	/***
	 * 同略云 银行卡状态
	 * Description 
	 * @param pno
	 * @param operator
	 * @param ip
	 * @param loginName
	 * @return 
	 */
	
	public static String api_get_card_status(String card_number)  {
        String sign;
        JSONObject data = new JSONObject();
        data.put("module", "bankcard");
        data.put("method", "api_get_card_status");
        data.put("company_name",  "ld");
        JSONObject payload =  new JSONObject();
        payload.put("card_number", card_number);//卡号
        data.put("payload", payload);
        try {
        	  SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC_SHA1_ALGORITHM);
        	  Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        	  mac.init(signingKey);
        	  byte[] rawHmac = mac.doFinal(data.toString().getBytes("UTF-8"));
        	  sign = base64Encode(rawHmac);
		} catch (Exception e) {
			 sign = "" ;
             e.printStackTrace();
		}
        
        log.info(">>>>data=" + data.toString());
        PostMethod postMethod = new PostMethod();
        String phpHtml ="";
        try {
        	  HttpClient httpClient = new HttpClient();
    		  postMethod = new PostMethod("https://www.tly-transfer.com/sfisapi/");
        	  postMethod.addRequestHeader("TLYHMAC",sign);
        	  postMethod.addRequestHeader("content-type","application/json");
        	  RequestEntity re = new StringRequestEntity(data.toString(), "application/json", "UTF-8");
        	  postMethod.setRequestEntity(re);
        	  int statusCode = httpClient.executeMethod(postMethod);
	      	  if (statusCode == HttpStatus.SC_OK) {
				    phpHtml = postMethod.getResponseBodyAsString();
					if (phpHtml == null || phpHtml.equals("")) {	
						log.info(">>>>请求接口出现问题！");
					}
				} else {
					  log.info("Response Code: " + statusCode);
				}
	      	  return phpHtml;
			}   catch(Exception e){
				e.printStackTrace();   
				log.info("Response 消息: " + e.toString());
			}finally {
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
			}
           return phpHtml;
	}
	
	//加密
    public  static String base64Encode(byte[] s) {
        if (s == null)
            return null;
//        BASE64Encoder b = new sun.misc.BASE64Encoder();
//        return b.encode(s);
        return Base64.encode(s);
    }
    
	public static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
		params.setParameter("http.protocol.content-charset", "UTF-8");
		params.setParameter("http.socket.timeout", Integer.valueOf(500000));
		httpclient.setParams(params);
		return httpclient;
	}
    
    
    
    
    
    
    
    public static void main(String[] args) {
    	//String result = api_get_order_status("ws_by3333_170127fnk");
    	//失败 未找到或未插好U盾的单子返回结果如下
    	//{"data": {"status": "FAIL", "card_number": "6214832106727956", "fees": "0.00"}, "success": true}
    	//String  result = api_add_member_card("12345678910", "张三", "北京", "ICBC", "中国", "北京");
    	String result = api_add_order("123456789", 1.0, "sfsfsdfsdf123", "张三");
    	System.out.println(result+">>>>>>>");
	}

}