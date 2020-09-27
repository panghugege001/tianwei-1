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
	public final static String ZF_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRA3rgTSBqlBmofXnM0W7QEn2AGE3zfF88uCl3Yo6zmXBzgreA70GxHXKoh5ElSOYn8BNqScoM1r4sgLha+DXrjkJDFBheKiKWHXAnAvBekYHEXlvHV5UT39NzYholVmPNh+KtOZJXnWgGLsMo0voBFztsqSfBIQ46Xl/JqfEBgQIDAQAB";
	public final static String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAJQ0I/tKOxYCROpa"
											+ "cLwVOHviD2XxI8Q2unJoD3wSoC5/FKucD9Lk7qy27aHuvqrru6YoZaR3gOhWJYQQ"
											+ "tWsectf1xH8QtThoGDTANee/ymuCNGX1CgkcvabZ+V/+dQ0+E1x3lEWgJ4bP/OZ7"
											+ "g4JWc6/l4XKfS8OOfD7xzZAUbwqfAgMBAAECgYBQ3na321SMeOaD/p+/cdDlgIAo"
											+ "f0GmArsQTIoOiNdRJ9Tuo3Ta/kLiidr3XqA6AfHeA/MaoH79yGwmOTvVXMMLEiQe"
											+ "SuTOQqe6PlC2TiM5eUZ5Rp0umXQUNBbYsDp5z6QXiyKeOHR/U5B/pDkxGb4YwXr2"
											+ "rye+p3JqCQTywqLfyQJBAMOzoc8kCwOpXyGXyprHDgSmNQqg584n54fCarfNFJUd"
											+ "pI2ioABtKPPW8bmAkJGxesbBTLRq/SsU2jYO/WRRytMCQQDB3gGX4MmTfnDltAWh"
											+ "KV2znipLFbOq+9ilrxw5wU4ynSwCz9diEpsF3OaV84eQQ09YwS4y/bYmsOUZVFN6"
											+ "68mFAkEAqfqtMXix5GVJTqlD/fmk8F7YtTma/ZHlZSJ1hN2o4ffURH0bEiO+LUTl"
											+ "weSsLQ+Ff3rHv/EpojDDc67DYk40pwJBAKV0T2/LjL2wX7Hq3hMZNrsg9X93WgtH"
											+ "PTLKLBCuhYp00SblMzCkKGHskW7QlJXunD1nQYKLPXXv6tafPU/fA/ECQQCWsoyh"
											+ "nUYKHhrLa2yoP8tNGGvd5yw9P0gK/r07D0uvyT96CMA8I2r0J/5SdGZCs1GK7Byf"
											+ "ZPaACi0KdSF7Vn45";
	
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
