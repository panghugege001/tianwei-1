package dfh.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import sun.misc.BASE64Encoder;


/**
 * 同略云 自动转账API
 * 
 * @author
 * 
 */
public class TlyUtil {
	
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
		    String HMAC_SHA1_ALGORITHM = "HmacSHA256";
	        String sign;
	        String key = "712538fdfcfee436af6b6810324d0e46ae486d420b99d7a4bfc6ac15";
	        JSONObject data = new JSONObject();
	        data.put("module", "bankcard");
	        data.put("method", "api_add_member_card");
	        data.put("company_name",  "test");
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
	        	 // sign = Base64.getEncoder().encodeToString(rawHmac);
	        	  sign = base64Encode(rawHmac);
			} catch (Exception e) {
				 sign = "" ;
                e.printStackTrace();
			}
	        System.out.println("sign=" + sign);
	        System.out.println("data=" + data.toString());
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
						System.out.println("resule--"+phpHtml);
						if (phpHtml == null || phpHtml.equals("")) {	
							System.out.println("请求接口出现问题！");
						}
					} else {
						System.out.println("Response Code: " + statusCode);
					}
		      	  return phpHtml;
				}   catch(Exception e){
					e.printStackTrace();   
					System.out.println("Response 消息: " + e.toString());
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
	
	@SuppressWarnings("null")
	public static String api_add_order(String card_number, Double money, String billno,String realname)  {
		String HMAC_SHA1_ALGORITHM = "HmacSHA256";
        String sign;
        String key = "712538fdfcfee436af6b6810324d0e46ae486d420b99d7a4bfc6ac15";
        JSONObject data = new JSONObject();
        data.put("module", "order");
        data.put("method", "api_add_order");
        data.put("company_name",  "test");
        JSONObject payload =  new JSONObject();
        payload.put("card_number", card_number);//卡号
        payload.put("amount", money);//金额
        payload.put("trans_mode", "out_trans");//转账模式
        payload.put("order_number", billno);//订单号
        payload.put("real_name", realname);//订单号
        //payload.put("atfs_flag", "xxxxx");//
        data.put("payload", payload);
        try {
        	  SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC_SHA1_ALGORITHM);
        	  Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        	  mac.init(signingKey);
        	  byte[] rawHmac = mac.doFinal(data.toString().getBytes("UTF-8"));
        	  //sign = Base64.getEncoder().encodeToString(rawHmac);
        	  sign = base64Encode(rawHmac);
		} catch (Exception e) {
			 sign = "" ;
             e.printStackTrace();
		}
        System.out.println("sign=" + sign);
        System.out.println("data=" + data.toString());
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
					System.out.println("resule--"+phpHtml);
					if (phpHtml == null || phpHtml.equals("")) {	
						System.out.println("请求接口出现问题！");
					}
				} else {
					System.out.println("Response Code: " + statusCode);
				}
	      	  return phpHtml;
			}   catch(Exception e){
				e.printStackTrace();   
				System.out.println("Response 消息: " + e.toString());
			}finally {
				if (postMethod != null) {
					postMethod.releaseConnection();
				}
			}
           return phpHtml;
	}
    public  static String base64Encode(byte[] s) {
        if (s == null)
            return null;
        BASE64Encoder b = new sun.misc.BASE64Encoder();
        return b.encode(s);
    }

}