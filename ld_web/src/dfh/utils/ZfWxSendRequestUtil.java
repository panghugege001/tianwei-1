package dfh.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

/**
 * 支付
 */
public class ZfWxSendRequestUtil {
	private static Logger log = Logger.getLogger(ZfWxSendRequestUtil.class);
	
	private final static String WeiXinUrl = "https://api.dinpay.com/gateway/api/weixin"; 
	public final static String ZF_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD4LUmU9jGoievjNSNgUr3HqqllR6ZInow25WTJnkPghnJu1H6GkCZdTqQfeIluODiKtatHx2P2xRKKq4CLBCiXtSFZY9/IcqkSsobU9CrmWuGp5xkzjOm4AlyEOGA2+rqVZH+N1LX21+UmNAX+Qg43pwXQJZ5+w7iEu2kaAmMJTQIDAQAB";
	public final static String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANjr14P2Gr5GxSKo"
																				+"dcDTG+gOBDbFWXC81Nnr4gWrf442nG/4N/JchEUOVZ6VjO88qhwQdx2rJ9GGXc9W"
																				+"y7fqv7V/tTUwJR8/ZaDCTe4PJOJYLTm+IKTHZiUQt/TRUHDw0/Keke8A424F1kJB"
																				+"a12DNfmnVsJ8MkCqPpsYPNlzFDbLAgMBAAECgYEAi41efwTrO5Jn7N4Xs0+dWnL5"
																				+"/wqDeeXhwbjhFei5DRLRHa/AvmkKpyqLppRrsNWEUr29BBdCbKdW3rbmJJIfdkMA"
																				+"dB8v9gxkI9RxgRD/xg1WHBAQ3YCuY4VaW4LvlUKdiWBVVVSz/ZwEkFdA+ffgemlI"
																				+"vQozxhlCqhesmFMFi2ECQQD3DRXgFhwg3XSP1EBZ/qrYbFTJqG4/MLng2zixbZBH"
																				+"OOTAt7Lz/azrMlDq04WpiBrEWDnFkTFM0ec9gaWbbLqZAkEA4Mdcbw+TT4fFQ6BM"
																				+"XHWOkwZDMEJbzbtNEoMGSj+tlc6E2crNUxFJj+LbUqzFeMhLH9bMT7HaU9B6hbKF"
																				+"EtGfAwJBANyr0I1f0nI8vYA3m9+HDb3RTg7upsNE19OUnVhGwGgN1q6Zp3wvbESN"
																				+"ph+LHsomq5oUIE5r+XhfychzxKRHA4kCQB1QgrKNQnX1y/k016CbSDvObkbF8LkU"
																				+"+0k1w0U9psLJyzOoKjzyHTjhqh7PH3XQEVQWrFCcbk/N+ZykGozIv8cCQC5qZvPZ"
																				+"q1UYzy40tvF/77lRr21jg7iN7fHqXWM10W3/o/7nQ7Gbj9O9NDIgjbtHR4WtLJiO"
																				+"RlkGzT4A0Y9sPRE=";
																				
																					
	@SuppressWarnings("deprecation")
	public static String sendRequest(String sign, String merchant_code,String order_no , String order_amount , String service_type ,String notify_url ,String interface_version ,String sign_type,String order_time ,String product_name ,String extra_return_param) {
		HttpClient httpClient = HttpUtils.createHttpClient();
		PostMethod method = new PostMethod(WeiXinUrl);
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");

		NameValuePair signE = new NameValuePair( "sign" , sign);
		NameValuePair merchant_codeE = new NameValuePair( "merchant_code" , merchant_code);
		NameValuePair order_noE = new NameValuePair( "order_no" , order_no);
		NameValuePair order_amountE = new NameValuePair( "order_amount" , order_amount);
		NameValuePair service_typeE = new NameValuePair( "service_type" , service_type);
		NameValuePair notify_urlE = new NameValuePair( "notify_url" , notify_url);
		NameValuePair interface_versionE = new NameValuePair( "interface_version" , interface_version);
		NameValuePair sign_typeE = new NameValuePair( "sign_type" , sign_type);
		NameValuePair order_timeE = new NameValuePair( "order_time" , order_time);
		NameValuePair product_nameE = new NameValuePair( "product_name" , product_name);
		NameValuePair extra_return_paramE = new NameValuePair( "extra_return_param" , extra_return_param);
		method.setRequestBody( new NameValuePair[]{signE , merchant_codeE , order_noE , order_amountE , service_typeE , notify_urlE , interface_versionE , sign_typeE , order_timeE , product_nameE , extra_return_paramE});
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
		log.info(result);
		return result;
	}
	
	public static String exportQrcode(String info){
		String resp_code = compileVerifyData("<resp_code>(.*?)</resp_code>", info) ;
		String resp_desc = compileVerifyData("<resp_desc>(.*?)</resp_desc>", info) ;
		if(resp_code.equals("SUCCESS")){
			//解密 , 验签
//			String signStr = RSAWithSoftware.validateSignByPublicKey(paramStr, publicKey, signedData) ;
			String qrcode = compileVerifyData("<qrcode>(.*?)</qrcode>", info) ;
			return qrcode ;
		}else{
			log.info("智付微信生成二维码失败resp_desc:"+resp_code+"  resp_desc:" + resp_desc);
			return null ;
		}
	}
	
	public  static String compileVerifyData(String pattern,String verifyData){
    	Pattern p=Pattern.compile(pattern);
    	Matcher m=p.matcher(verifyData);
    	String result = "";
    	while(m.find()){
    		result = m.group(1);
        }
    	return result;
    }
	
	public static void main(String[] args) {
	}

}
